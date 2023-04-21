package ru.dragon.bugzilla.api.v3;

import org.apache.commons.digester.Digester;
import org.apache.commons.digester.xmlrules.DigesterLoader;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.dragon.bugzilla.BugzillaException;
import ru.dragon.bugzilla.api.BugService;
import ru.dragon.bugzilla.model.Bug;
import ru.dragon.bugzilla.model.NewBug;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto: NIzhikov@gmail.com">Izhikov Nikolay</a>
 */
public class BugServiceImpl extends AbstractService implements BugService {

    public List<String> legalValues(String bugzillaUrl, String field, Integer productId) throws BugzillaException {
        Map resultMap = executeService(getWebServiceUrl(bugzillaUrl), "Bug.legal_values", new ServiceExecutor.Parameter("field", field), new ServiceExecutor.Parameter("product_id", productId));
        Object[] values = (Object[]) resultMap.get("values");
        List<String> result = new ArrayList<String>();
        for (Object value : values) {
            result.add((String) value);
        }
        return result;
    }

    public List<Bug> search(String bugzillaUrl, String searchUrl) throws BugzillaException {
        if (!searchUrl.contains(bugzillaUrl)) {
            searchUrl = bugzillaUrl + ((!bugzillaUrl.endsWith("/") && !searchUrl.startsWith("/")) ? "/" : "") + searchUrl;
        }
        String htmlPage = readStringFromInputStream(ServiceExecutorFactory.getInstance().getServiceExecutor(bugzillaUrl).executeQuery(searchUrl));
        List<Bug> result = new ArrayList<Bug>();
        int curPosition = 0;
        while (curPosition < htmlPage.length()) {
            int idHrefStart = htmlPage.indexOf("show_bug.cgi?id=", curPosition) + "show_bug.cgi?id=".length();
            int idHrefEnd = htmlPage.indexOf("\"", idHrefStart);
            if (idHrefStart == -1 || idHrefStart < curPosition || idHrefEnd == -1 || idHrefEnd < curPosition) {
                break;
            }
            Bug bug = new Bug();
            bug.setId(Integer.parseInt(htmlPage.substring(idHrefStart, idHrefEnd)));
            curPosition = idHrefEnd;
            int trEnd = htmlPage.indexOf("</tr>", idHrefEnd);
            int summaryTdEnd = htmlPage.lastIndexOf("</td>", trEnd);
            int offset;
            int summaryTdStart = htmlPage.lastIndexOf("<td >", trEnd);
            if (summaryTdStart == -1) {
                summaryTdStart = htmlPage.lastIndexOf("<td>");
                offset = 4;
            } else {
                offset = 5;
            }
            bug.setSummary(htmlPage.substring(summaryTdStart + 5, summaryTdEnd));
            curPosition = summaryTdEnd;
            result.add(bug);
        }
        return result;
    }

    public List<Bug> getBugs(String bugzillaUrl, List<Integer> ids) throws BugzillaException {
        ServiceExecutor.Parameter[] parameters = new ServiceExecutor.Parameter[ids.size() + 1];
        parameters[0] = new ServiceExecutor.Parameter("ctype", "xml");
        for (int i = 0; i < ids.size(); i++) {
            parameters[i + 1] = new ServiceExecutor.Parameter("id", ids.get(i));
        }
        Digester digester = DigesterLoader.createDigester(BugServiceImpl.class.getResource("/ru/dragon/bugzilla/api/v3/digester/bugListParser.xml"));
        digester.setEntityResolver(new EntityResolver() {

            public InputSource resolveEntity(String publicId, String systemId) {
                return new InputSource(new ByteArrayInputStream(new byte[0]));
            }
        });
        try {
            return (List<Bug>) digester.parse(ServiceExecutorFactory.getInstance().getServiceExecutor(bugzillaUrl).executeQuery(bugzillaUrl + (bugzillaUrl.endsWith("/") ? "" : "/") + "show_bug.cgi", parameters));
        } catch (IOException e) {
            throw new BugzillaException(e);
        } catch (SAXException e) {
            throw new BugzillaException(e);
        }
    }

    public Integer create(String bugzillaUrl, NewBug bug) throws BugzillaException {
        List<ServiceExecutor.Parameter> parameters = new ArrayList<ServiceExecutor.Parameter>(15);
        addIfNotNull(parameters, "product", bug.getProductName());
        addIfNotNull(parameters, "component", bug.getComponentName());
        addIfNotNull(parameters, "summary", bug.getSummary());
        addIfNotNull(parameters, "version", bug.getVersion());
        addIfNotNull(parameters, "description", bug.getDescription());
        addIfNotNull(parameters, "op_sys", bug.getOperationSystem());
        addIfNotNull(parameters, "platform", bug.getPlatform());
        addIfNotNull(parameters, "priority", bug.getPriority());
        addIfNotNull(parameters, "severity", bug.getSeverity());
        addIfNotNull(parameters, "alias", bug.getAlias());
        addIfNotNull(parameters, "assigned_to", bug.getAssignedTo());
        addIfNotNull(parameters, "cc", bug.getCc());
        addIfNotNull(parameters, "qa_contact", bug.getQaContact());
        addIfNotNull(parameters, "status", bug.getStatus());
        addIfNotNull(parameters, "target_milestone", bug.getTargetMilestone());
        ServiceExecutor.Parameter[] parametersArray = new ServiceExecutor.Parameter[parameters.size()];
        parameters.toArray(parametersArray);
        return (Integer) executeService(getWebServiceUrl(bugzillaUrl), "Bug.create", parametersArray).get("id");
    }

    private void addIfNotNull(List<ServiceExecutor.Parameter> parameters, String name, String parameter) {
        addIfNotNull(parameters, name, "".equals(parameter) ? null : (Object) parameter);
    }

    private void addIfNotNull(List<ServiceExecutor.Parameter> parameters, String name, Object parameter) {
        if (parameter != null) {
            parameters.add(new ServiceExecutor.Parameter(name, parameter));
        }
    }
}
