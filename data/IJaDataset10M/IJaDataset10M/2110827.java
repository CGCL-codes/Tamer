package org.mikha.utils.web.examples;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.mikha.utils.Pair;
import org.mikha.utils.web.BaseControllerServlet;
import org.mikha.utils.web.ControllerMethodMapping;
import org.mikha.utils.web.HttpMethod;
import org.mikha.utils.web.HttpParamsRequest;

/**
 * Demonstrates controller method mapping and basic HTTP parameter handling. 
 */
@SuppressWarnings("serial")
public class FormsServlet extends BaseControllerServlet {

    @SuppressWarnings("unchecked")
    private static final Pair<Integer, String>[] SALUTATIONS = new Pair[] { new Pair<Integer, String>(0, "Mr."), new Pair<Integer, String>(1, "Ms."), new Pair<Integer, String>(2, "Mrs."), new Pair<Integer, String>(3, "Dr.") };

    @SuppressWarnings("unchecked")
    private static final Pair<Integer, String>[] SPAM_TOPICS = new Pair[] { new Pair<Integer, String>(0, "Cars"), new Pair<Integer, String>(1, "PC - Hardware"), new Pair<Integer, String>(2, "PC - Software"), new Pair<Integer, String>(3, "PC - Games"), new Pair<Integer, String>(4, "Consoles - Hardware"), new Pair<Integer, String>(5, "Consoles - Games"), new Pair<Integer, String>(6, "Travel") };

    /**
     * Parses "scalar" form.
     * @param req request
     */
    @ControllerMethodMapping(paths = "/scalar")
    public String parseScalarForm(HttpParamsRequest req) {
        String submit = req.getParameter("submit");
        if (submit != null) {
            Integer int1 = req.getInteger("int1");
            Integer int2 = req.getInteger("int2", 100);
            req.setAttribute("int1", String.valueOf(int1));
            req.setAttribute("int2", String.valueOf(int2));
        }
        if (submit != null) {
            String str1 = req.getString("str1");
            String str2 = req.getString("str2", "la-la-la");
            String str3 = req.getEmail("str3");
            req.setAttribute("str1", String.valueOf(str1));
            req.setAttribute("str2", String.valueOf(str2));
            req.setAttribute("str3", String.valueOf(str3));
        }
        if (submit != null) {
            boolean bool1 = req.getBoolean("bool1");
            boolean bool2 = req.getBoolean("bool2");
            req.setAttribute("bool1", String.valueOf(bool1));
            req.setAttribute("bool2", String.valueOf(bool2));
        }
        return "scalarform.jsp";
    }

    /**
     * Parses "array" form.
     * @param req request
     */
    @ControllerMethodMapping(paths = "/array")
    public String parseArrayForm(HttpParamsRequest req) {
        String submit = req.getParameter("submit");
        String[] out;
        out = new String[4];
        if (submit != null) {
            Integer[] in = req.getIntegerArray("ints", 4);
            for (int i = 0; i < Math.min(out.length, in.length); i++) {
                out[i] = String.valueOf(in[i]);
            }
        }
        req.setAttribute("ints", out);
        out = new String[4];
        if (submit != null) {
            Boolean[] in = req.getBooleanArray("bools", 4);
            for (int i = 0; i < Math.min(out.length, in.length); i++) {
                out[i] = String.valueOf(in[i]);
            }
        }
        req.setAttribute("bools", out);
        out = new String[4];
        if (submit != null) {
            String[] in = req.getStringArray("strs", 4);
            for (int i = 0; i < Math.min(out.length, in.length); i++) {
                out[i] = String.valueOf(in[i]);
            }
        }
        req.setAttribute("strs", out);
        return "arrayform.jsp";
    }

    @ControllerMethodMapping(methods = HttpMethod.GET, paths = "/demo")
    public String showDemoForm(HttpParamsRequest req) {
        putStandardDefinitions(req);
        return "demoform.jsp";
    }

    /**
     * Processes "demo" form.
     * @param req request
     */
    @ControllerMethodMapping(methods = HttpMethod.POST, paths = "/demo")
    public String processDemoForm(HttpParamsRequest req) {
        putStandardDefinitions(req);
        String first = req.getString("first");
        String family = req.getString("family");
        Pair<Integer, String> salut = req.getOption("salut", SALUTATIONS);
        String email = req.getEmail("email");
        Integer age = req.getInteger("age");
        if (age != null && (age < 18 || age > 120)) {
            req.logParameterError("age");
        }
        List<Pair<Integer, String>> spam = req.getOptions("spam", SPAM_TOPICS);
        String comments = req.getString("comments", "");
        if (comments.length() > 100) {
            req.logParameterError("comments");
        }
        FileItem picture = req.getFile("picture");
        if (!req.hasRecentErrors()) {
            req.setAttribute("first", first);
            req.setAttribute("family", family);
            req.setAttribute("salut", salut);
            req.setAttribute("email", email);
            req.setAttribute("age", age);
            req.setAttribute("spam", spam);
            req.setAttribute("comments", comments);
            req.setAttribute("picture", picture);
            return "demoresult.jsp";
        }
        return "demoform.jsp";
    }

    /**
     * Parses "file" form.
     * @param req request
     * @param rsp response
     * @throws ServletException if failed to forward
     * @throws IOException if failed to forward
     */
    @ControllerMethodMapping(paths = "/file")
    public String parseFileForm(HttpParamsRequest req, HttpServletResponse rsp) throws ServletException, IOException {
        String submit = req.getParameter("submit");
        if (submit != null) {
            FileItem file = req.getFile("file");
            if (!req.hasRecentErrors()) {
                rsp.setContentType(file.getContentType());
                rsp.setContentLength((int) file.getSize());
                rsp.getOutputStream().write(file.get());
                return null;
            }
        }
        return "fileform.jsp";
    }

    private void putStandardDefinitions(HttpParamsRequest req) {
        req.setAttribute("SALUTATIONS", SALUTATIONS);
        req.setAttribute("SPAM_TOPICS", SPAM_TOPICS);
    }
}
