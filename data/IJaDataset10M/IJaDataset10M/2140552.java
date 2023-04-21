package de.knowwe.diaflux.kbinfo;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import de.knowwe.core.Attributes;
import de.knowwe.core.Environment;
import de.knowwe.core.kdom.parsing.Section;
import de.knowwe.core.kdom.parsing.Sections;
import de.knowwe.core.user.UserContext;
import de.knowwe.diaflux.type.FlowchartType;
import de.knowwe.kdom.defaultMarkup.DefaultMarkupType;
import de.knowwe.kdom.xml.AbstractXMLType;

public class JSPHelper {

    private final UserContext userContext;

    public JSPHelper(UserContext userContext) {
        this.userContext = userContext;
        if (this.userContext.getWeb() == null) {
            this.userContext.getParameters().put(Attributes.WEB, Environment.DEFAULT_WEB);
        }
    }

    private static List<String> getAllMatches(String className, String web) {
        return SearchInfoObjects.searchObjects(Environment.getInstance(), web, null, className, 65535);
    }

    public String getArticleIDsAsArray() {
        List<String> matches = getAllMatches("Article", this.userContext.getWeb());
        Collections.sort(matches);
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        boolean first = true;
        for (String id : matches) {
            if (first) {
                first = false;
            } else {
                buffer.append(", ");
            }
            buffer.append("'").append(id).append("'");
        }
        buffer.append("]");
        return buffer.toString();
    }

    public String getSectionText(String id) {
        Section<?> sec = Sections.getSection(id);
        String data = "Section not found: " + id;
        if (sec != null) {
            data = sec.getText();
        }
        return data;
    }

    public String getArticleInfoObjectsAsXML() {
        List<String> matches = getAllMatches("Article", this.userContext.getWeb());
        StringBuilder bob = new StringBuilder();
        GetInfoObjects.appendHeader(bob);
        for (String id : matches) {
            GetInfoObjects.appendInfoObject(this.userContext.getWeb(), id, bob);
        }
        GetInfoObjects.appendFooter(bob);
        return bob.toString();
    }

    public String getReferredInfoObjectsAsXML() {
        return getReferrdInfoObjectsAsXML(this.userContext.getWeb());
    }

    public static String getReferrdInfoObjectsAsXML(String web) {
        List<String> matches = getAllMatches(null, web);
        StringBuilder bob = new StringBuilder();
        GetInfoObjects.appendHeader(bob);
        for (String id : matches) {
            GetInfoObjects.appendInfoObject(web, id, bob);
        }
        GetInfoObjects.appendFooter(bob);
        return bob.toString();
    }

    @SuppressWarnings("unchecked")
    public String loadFlowchart(String kdomID) {
        Section<DefaultMarkupType> diaFluxSection = (Section<DefaultMarkupType>) Sections.getSection(kdomID);
        if (diaFluxSection == null) {
            throw new IllegalArgumentException("Could not find flowchart at: " + kdomID);
        }
        Section<FlowchartType> flowchart = Sections.findSuccessor(diaFluxSection, FlowchartType.class);
        if (flowchart == null) {
            return getEmptyFlowchart();
        }
        return getSectionText(kdomID);
    }

    /**
	 * 
	 * @created 25.11.2010
	 * @return
	 */
    private String getEmptyFlowchart() {
        String id = createNewFlowchartID();
        return "<flowchart fcid=\"flow_" + id + "\" name=\"New Flowchart\" icon=\"sanduhr.gif\" width=\"750\" height=\"500\" idCounter=\"1\">" + "</flowchart>";
    }

    /**
	 * 
	 * @created 23.02.2011
	 * @return
	 */
    public static String createNewFlowchartID() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public String getFlowchartID() {
        return getFlowchartAttributeValue("fcid");
    }

    public String getFlowchartWidth() {
        return getFlowchartAttributeValue("width");
    }

    public String getFlowchartHeight() {
        return getFlowchartAttributeValue("height");
    }

    private String getFlowchartAttributeValue(String attributeName) {
        Section<FlowchartType> section = Sections.findSuccessor(Sections.getSection(userContext.getParameter("kdomID")), FlowchartType.class);
        return AbstractXMLType.getAttributeMapFor(section).get(attributeName);
    }
}
