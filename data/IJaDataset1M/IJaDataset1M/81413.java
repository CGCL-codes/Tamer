package pub.servlets.update.propagate_annotation;

import pub.beans.*;
import pub.db.*;
import pub.servlets.*;
import pub.utils.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import com.oreilly.servlet.MultipartRequest;

public class PropAnnotation implements RequestHandler {

    private PubServlet parentServlet;

    private PubConnection conn;

    private static final String EXCLUDED_TYPES = "meth gene anat user";

    public void handleRequest(RequestHandlerState state) throws IOException, ServletException {
        this.parentServlet = state.getParent();
        this.conn = state.getConn();
        clearSearchResults(state);
        String user_id = Login.getUserId(state.getRequest());
        if (user_id == null) {
            Login.redirectToLoginServlet(state.getRequest(), state.getResponse());
            return;
        }
        HashMap parameters = getRequestParameters(state.getRequest());
        String action = (String) parameters.get("update_action");
        String term_id = getTermId(parameters);
        String nextAction = doRemoveTerm(state, parameters, user_id, term_id, action);
        if (nextAction != null) {
            action = nextAction;
        }
        doActionDispatch(action, state, term_id, user_id, parameters);
    }

    /** TermSummary stores results in session, so at the entry point,
	removes old results from session if it exists.
    */
    private void clearSearchResults(RequestHandlerState state) {
        HttpSession session = state.getRequest().getSession(false);
        if (session != null) session.removeAttribute("search_results");
    }

    private String getTermId(HashMap parameters) throws ServletException {
        if (parameters.containsKey("term_id")) {
            return (String) parameters.get("term_id");
        } else {
            throw new ServletException("missing parameter: term_id");
        }
    }

    /**
       Returns non-null action string if we should continue on with
       the rest of processing, null otherwise.
     */
    private String doRemoveTerm(RequestHandlerState state, HashMap parameters, String user_id, String term_id, String action) throws IOException, ServletException {
        String removeTerm = (String) parameters.get("remove_term");
        if (removeTerm != null && (!removeTerm.trim().equals("")) && (action == null)) {
            String terms = (String) parameters.get("term_term_id");
            String remove = (String) parameters.get("remove_term");
            System.out.println("removing terms: " + remove);
            handleTermRemoval(user_id, term_id, state.getRequest(), terms, remove);
            gotoPropagateAnnotationPage(user_id, term_id, state.getRequest(), state.getResponse());
            return null;
        } else if (parameters.containsKey("update_action")) {
            return (String) parameters.get("update_action");
        } else {
            throw new ServletException("missing parameter: update_action");
        }
    }

    private void doActionDispatch(String action, RequestHandlerState state, String term_id, String user_id, HashMap parameters) throws IOException, ServletException {
        if (action.startsWith("Search")) {
            handleTermSearchRequest(term_id, parameters, state.getRequest(), state.getResponse());
        } else if (action.equals("save_genes")) {
            saveTermList(user_id, term_id, state.getRequest());
            gotoPropagateAnnotationPage(user_id, term_id, state.getRequest(), state.getResponse());
        } else if (action.equals("save_annotations")) {
            saveAnnotationList(user_id, term_id, state.getRequest());
            gotoPropagateAnnotationPage(user_id, term_id, state.getRequest(), state.getResponse());
        } else if (action.startsWith("Propagate")) {
            handlePropagateAnnotationRequest(user_id, term_id, state.getRequest(), state.getResponse());
        } else if (action.indexOf("Predefine") != -1) {
            handleSearchFromPredefineList(term_id, state.getRequest(), state.getResponse(), parameters);
        } else throw new ServletException("unknown action " + action);
    }

    /** 
     * Handles search for term from a predefine list of term id's
     */
    private void handleSearchFromPredefineList(String term_id, HttpServletRequest request, HttpServletResponse response, HashMap parameters) throws IOException, ServletException {
        String type = (String) parameters.get("list_type");
        List term_ids = getPredefineTermList(type);
        TermSearcher search = new TermSearcher();
        search.addTermNamesForSearch(term_ids);
        term_ids = idsOnly(search.search());
        String limit = (String) parameters.get("limit");
        request.setAttribute("limit", limit);
        request.setAttribute("results", term_ids);
        request.setAttribute("term_id", term_id);
        request.setAttribute("update_action", "Use Predefine List");
        parentServlet.include("/jsp/propagate_annotation/TermSummary.jsp", request, response);
    }

    /**
     *  Reads in the predefine list from a file
     *  @param file location of the file containing the term ID's
     */
    private List getPredefineTermList(String type) {
        List term_ids = null;
        String content = pub.config.TermList.getListByType(type);
        if (content != null) {
            String[] list = content.split("[\\t\\n\\r]+");
            term_ids = Arrays.asList(list);
        }
        return term_ids;
    }

    /** set attribution and go to propagate annotation page */
    private void gotoPropagateAnnotationPage(String user_id, String term_id, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PropagateAnnotationsToTerms propagate = getPropagateObjectFromSession(user_id, term_id, request);
        if (propagate.getAnnotations() != null) {
            request.setAttribute("annotationIds", propagate.getAnnotations());
        }
        if (propagate.getTerms() != null) {
            request.setAttribute("termTermIds", propagate.getTerms());
        }
        request.setAttribute("term_id", term_id);
        List types = Arrays.asList(pub.config.TermList.getTypeLocationMap().keySet().toArray());
        request.setAttribute("term_types", types);
        parentServlet.include("/jsp/propagate_annotation/PropagateAnnotationsToTerms.jsp", request, response);
    }

    private Map getTermTypes() {
        HashMap ans = new HashMap();
        ans.put("stru", "stru");
        ans.put("func", "func");
        ans.put("proc", "proc");
        return ans;
    }

    /** 
     *  Do term search and go to term search result page. 
     */
    private void handleTermSearchRequest(String term_id, HashMap parameters, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List term_ids = doTermSearch(parameters, request);
        term_ids = idsOnly(term_ids);
        term_ids = excludeTypes(term_ids);
        String limit = (String) parameters.get("limit");
        request.setAttribute("limit", limit);
        request.setAttribute("results", term_ids);
        request.setAttribute("term_id", term_id);
        request.setAttribute("update_action", "Search");
        parentServlet.include("/jsp/propagate_annotation/TermSummary.jsp", request, response);
    }

    /**
     * Hemoves prefix "term:" from all strings in ids, and drop the ones that start with "gene:"
     * @param ids List<String> of term ids containing prefix "term:" or "gene:"
     * @return List<String> of term ids without the prefix "term:"
     */
    private List idsOnly(List ids) {
        ArrayList ans = new ArrayList();
        if (ids == null) return ans;
        String prefix = "term:";
        for (int i = 0; i < ids.size(); i++) {
            String s = (String) ids.get(i);
            if (s.startsWith(prefix)) ans.add(s.substring(prefix.length())); else if (s.startsWith("gene:")) continue; else ans.add(s);
        }
        return ans;
    }

    /**
     * Returns the index of the first term displayed
     */
    private String getCurrentRow(HttpServletRequest request) {
        return StringUtils.safeGetParameter("start", request, "1");
    }

    /** 
     * Do term search from text fields or an uploaded file.
     * @param parameters a map all all parameters from the request
     * @param request the the servlet request
     */
    private List doTermSearch(HashMap parameters, HttpServletRequest request) throws IOException, ServletException {
        if (parameters.containsKey("has_file_upload") && ((String) parameters.get("has_file_upload")).equals("true")) {
            File file = (File) parameters.get("file");
            String term_name_string = "";
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = reader.readLine()) != null) {
                term_name_string += (line.trim() + "\n");
            }
            String[] term_names = term_name_string.split("[\\t\\n\\r]+");
            TermSearcher search = new TermSearcher();
            search.addTermNamesForSearch(Arrays.asList(term_names));
            return search.search();
        } else {
            HashMap tname_methods = new HashMap();
            for (int index = 1; index <= 2; index++) {
                if (parameters.containsKey("term_name" + index)) {
                    String term_name = (String) parameters.get("term_name" + index);
                    String method = (String) parameters.get("method" + index);
                    term_name = term_name.trim();
                    method = method.trim();
                    try {
                        if (!Character.isLetterOrDigit(term_name.charAt(0))) continue;
                    } catch (Exception e) {
                        continue;
                    }
                    String[] m = method.split("\\s");
                    method = m[0].toLowerCase();
                    tname_methods.put(term_name.trim(), method != null ? method.trim() : "exactly");
                    request.setAttribute("term_name" + index, term_name);
                    request.setAttribute("method" + index, method);
                }
            }
            TermSearcher search = new TermSearcher(tname_methods);
            return search.search();
        }
    }

    /** 
     * remove PropagateObjectFromSession object from user session 
     */
    private void removePropagateAnnotationFromSession(String user_id, String term_id, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new RuntimeException("User session time out");
        }
        if (session.getAttribute(user_id + "_" + term_id + "_propterm") != null) {
            session.removeAttribute(user_id + "_" + term_id + "_propterm");
        }
    }

    /** get PropagateObjectFromSession object from user session */
    private PropagateAnnotationsToTerms getPropagateObjectFromSession(String user_id, String term_id, HttpServletRequest request) {
        if (user_id == null || term_id == null) {
            System.out.println("user_id: " + user_id + " term_id: " + term_id);
        }
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new RuntimeException("User session time out");
        }
        if (session.getAttribute(user_id + "_" + term_id + "_propterm") == null) {
            session.setAttribute(user_id + "_" + term_id + "_propterm", new PropagateAnnotationsToTerms());
        }
        return (PropagateAnnotationsToTerms) session.getAttribute(user_id + "_" + term_id + "_propterm");
    }

    /** save user selected annotations list to  PropagateAnnotationsToTerms */
    private void saveAnnotationList(String user_id, String term_id, HttpServletRequest request) throws IOException, ServletException {
        PropagateAnnotationsToTerms propagate = getPropagateObjectFromSession(user_id, term_id, request);
        ArrayList validAnnotationIds = new ArrayList();
        String[] annotationIds = request.getParameterValues("annotation_id");
        if (annotationIds != null && annotationIds.length > 0) {
            for (int i = 0; i < annotationIds.length; i++) {
                if (!StringUtils.isEmpty(annotationIds[i])) {
                    validAnnotationIds.add(annotationIds[i]);
                }
            }
        }
        if (validAnnotationIds.size() > 0) {
            propagate.setAnnotations(validAnnotationIds);
            System.out.println("after save annotation : " + propagate);
        } else {
            throw new ServletException("No annotation was checked for propagation, please go back to correct");
        }
    }

    /**
     * Removes a selected term.
     * @param terms String of all selected terms separated by ,
     * @param remove the term to remove 
     */
    private void handleTermRemoval(String user_id, String term_id, HttpServletRequest request, String terms, String remove) throws IOException, ServletException {
        PropagateAnnotationsToTerms propagate = getPropagateObjectFromSession(user_id, term_id, request);
        if (terms == null) System.out.println("terms is null");
        String[] termIds = terms.split(",");
        if (termIds == null) System.out.println("termIds is null");
        String exclude_term = remove;
        if (exclude_term == null) System.out.println("exclude_term is null");
        ArrayList validTermIds = new ArrayList();
        if (termIds != null && termIds.length > 0) {
            for (int i = 0; i < termIds.length; i++) {
                if (termIds[i] == null) continue;
                if (!StringUtils.isEmpty(termIds[i])) {
                    if (!termIds[i].trim().equals(exclude_term.trim())) validTermIds.add(termIds[i].trim());
                }
            }
        }
        propagate.setTerms(validTermIds);
    }

    /** 
     * save user selected gene list to  PropagateAnnotationsToTerms 
     */
    private void saveTermList(String user_id, String term_id, HttpServletRequest request) throws IOException, ServletException {
        PropagateAnnotationsToTerms propagate = getPropagateObjectFromSession(user_id, term_id, request);
        String[] termIds = request.getParameterValues("term_term_id");
        ArrayList validTermIds = new ArrayList();
        if (termIds != null && termIds.length > 0) {
            for (int i = 0; i < termIds.length; i++) {
                if (!StringUtils.isEmpty(termIds[i])) {
                    validTermIds.add(termIds[i]);
                }
            }
        }
        if (validTermIds.size() > 0) {
            propagate.setTerms(validTermIds);
        } else {
            throw new ServletException("No term id checked for propagation, please go back to correct");
        }
    }

    /**
     * do the propagation and go back to the gene where all the annotation propagate from 
     */
    private void handlePropagateAnnotationRequest(String user_id, String term_id, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List new_annotation_ids = doPropagate(user_id, term_id, request);
        removePropagateAnnotationFromSession(user_id, term_id, request);
        request.setAttribute("term_id", term_id);
        request.setAttribute("new_annotation_ids", new_annotation_ids);
        parentServlet.include("/jsp/propagate_annotation/PropagateSummary.jsp", request, response);
    }

    /** 
     * do annotation propagation by calling PropagateAnnotationsToTerms.propagate() method 
     */
    private List doPropagate(String user_id, String term_id, HttpServletRequest request) {
        PropagateAnnotationsToTerms propagate = getPropagateObjectFromSession(user_id, term_id, request);
        System.out.println("propagate is " + propagate);
        propagate.setUserId(Integer.parseInt(user_id));
        propagate.setPubConnection(conn);
        return propagate.propagate();
    }

    /**
     * Extracts all paramters from a request and return it as a HashMap
     * @param request the http request to extract parameters from
     */
    private HashMap getRequestParameters(HttpServletRequest request) throws IOException, ServletException {
        HashMap parameters = new HashMap();
        if (request.getContentType() != null && request.getContentType().startsWith("multipart/form-data")) {
            String upload_file_dir = PubProperties.getTestDataDirectory() + File.separator;
            MultipartRequest multi = new MultipartRequest(request, upload_file_dir, 5 * 1024 * 1024);
            Enumeration params = multi.getParameterNames();
            while (params.hasMoreElements()) {
                String name = (String) params.nextElement();
                String value = multi.getParameter(name);
                parameters.put(name, value);
            }
            Enumeration files = multi.getFileNames();
            while (files.hasMoreElements()) {
                String name = (String) files.nextElement();
                String filename = multi.getFilesystemName(name);
                if (!StringUtils.isEmpty(filename)) {
                    parameters.put("has_file_upload", "true");
                    parameters.put("file", multi.getFile(name));
                }
            }
        } else {
            Enumeration params = request.getParameterNames();
            while (params.hasMoreElements()) {
                String name = (String) params.nextElement();
                String values[] = request.getParameterValues(name);
                if (values != null) {
                    String vals = values[0];
                    for (int i = 1; i < values.length; i++) {
                        vals += " , " + values[i];
                    }
                    parameters.put(name, vals);
                }
            }
        }
        return parameters;
    }

    private List excludeTypes(List ids) {
        ArrayList ans = new ArrayList();
        int size = ids.size();
        for (int i = 0; i < size; i++) {
            pub.beans.TermBean termBean = pub.beans.BeanFactory.getTermBean(conn, "" + ids.get(i));
            if (!termBean.isGene() && !termBean.isObsolete() && (EXCLUDED_TYPES.indexOf(termBean.getType().toLowerCase()) == -1)) ans.add(ids.get(i));
        }
        return ans;
    }
}
