package nuts.ext.struts2.components;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nuts.core.lang.StringUtils;
import nuts.ext.xwork2.util.ContextUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.components.Form;
import org.apache.struts2.components.UIBean;
import org.apache.struts2.util.MakeIterator;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.TextProvider;
import com.opensymphony.xwork2.util.ValueStack;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Render an HTML pager.
 */
@StrutsTag(name = "pager", tldTagClass = "nuts.ext.struts2.taglib.views.jsp.ui.PagerTag", description = "Renders a pager navigator", allowDynamicAttributes = true)
public class Pager extends UIBean {

    private static final Log log = LogFactory.getLog(Pager.class);

    private static final Integer DEFAULT_LINKSIZE = 5;

    private static final String DEFAULT_COMMAND = "goto(#)";

    private static final String NODATA_TEXT = "pager-text-nodata";

    private static final String INFO_TEMPLATE = "pager-template-info";

    private static final String LIMIT_LABEL = "pager-limit-label";

    private static final String LIMIT_LIST = "pager-limit-list";

    private static final String DEFAULT_LIMIT_LIST = "10,20,30,40,50,60,70,80,90,100";

    protected static final String TEMPLATE = "n-pager";

    protected String command;

    protected String start;

    protected String end;

    protected String limit;

    protected String total;

    protected String nodataText;

    protected String infoTemplate;

    protected String linkSize;

    protected String linkStyle;

    protected String limitName;

    protected String limitLabel;

    protected Object limitList;

    protected String onLimitChange;

    private static int sequence = 0;

    /**
	 * @see UIBean
	 * @param stack value stack
	 * @param request http servlet request
	 * @param response http servlet response
	 */
    public Pager(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
        super(stack, request, response);
    }

    /**
	 * @return default template
	 */
    protected String getDefaultTemplate() {
        return TEMPLATE;
    }

    /**
	 * Override UIBean's implementation, such that component Html id is determined
	 * in the following order :-
	 * <ol>
	 *   <li>This component id attribute</li>
	 *   <li>pager_[an increasing sequential number]</li>
	 * </ol>
	 *
	 * @param form form
	 */
    protected void populateComponentHtmlId(Form form) {
        String tryId;
        if (id != null) {
            tryId = findStringIfAltSyntax(id);
        } else {
            tryId = "pager_" + (sequence++);
        }
        addParameter("id", tryId);
    }

    /**
	 * Evaluate extra parameters
	 */
    protected void evaluateExtraParams() {
        super.evaluateExtraParams();
        TextProvider txt = ContextUtils.findTextProviderInStack(getStack());
        if (nodataText != null) {
            addParameter("nodataText", findString(nodataText));
        } else if (parameters.get("nodataText") == null) {
            addParameter("nodataText", txt.getText(NODATA_TEXT, (String) null));
        }
        if (linkSize != null) {
            addParameter("linkSize", findString(linkSize));
        }
        if (parameters.get("linkSize") == null) {
            addParameter("linkSize", DEFAULT_LINKSIZE);
        }
        if (linkStyle != null) {
            addParameter("linkStyle", findString(linkStyle));
        }
        if (command != null) {
            addParameter("command", findString(command));
        }
        if (parameters.get("command") == null) {
            addParameter("command", DEFAULT_COMMAND);
        }
        if (limitLabel != null) {
            addParameter("limitLabel", findString(limitLabel));
        } else if (parameters.get("limitLabel") == null) {
            addParameter("limitLabel", txt.getText(LIMIT_LABEL, (String) null));
        }
        addParameter("limitName", limitName);
        addParameter("onLimitChange", onLimitChange);
        Object value;
        if (limitList == null) {
            limitList = parameters.get("limitList");
        }
        value = null;
        if (limitList instanceof String) {
            value = findValue((String) limitList);
        } else if (MakeIterator.isIterable(limitList)) {
            value = limitList;
        } else {
            value = StringUtils.parseCsv(txt.getText(LIMIT_LIST, DEFAULT_LIMIT_LIST));
        }
        addParameter("limitList", value);
        Integer istart = (Integer) findValue(start, Integer.class);
        Integer ilimit = (Integer) findValue(limit, Integer.class);
        Integer itotal = (Integer) findValue(total, Integer.class);
        Integer iend = null;
        if (end != null) {
            iend = (Integer) findValue(end, Integer.class);
        }
        Integer itotalpages;
        Integer ipageno;
        if (itotal == null || itotal < 1) {
            itotal = 0;
            itotalpages = 0;
            ipageno = 0;
        } else if (ilimit < 1) {
            itotalpages = 1;
            ipageno = 1;
        } else {
            itotalpages = ((itotal - 1) / ilimit) + 1;
            ipageno = istart / ilimit + 1;
        }
        if (itotalpages == 1 && istart > 0) {
            itotalpages = 2;
            ipageno = 2;
        }
        if (itotal != null && itotal > 0) {
            Integer ie2 = (ilimit < 1 ? itotal - 1 : istart + ilimit - 1);
            if (ie2 >= itotal) {
                ie2 = itotal - 1;
            }
            if (iend == null || iend > ie2 || iend < 0) {
                iend = ie2;
            }
        }
        addParameter("start", istart);
        addParameter("end", iend);
        addParameter("limit", ilimit);
        addParameter("total", itotal);
        addParameter("pageNo", ipageno);
        addParameter("totalPages", itotalpages);
        if (itotal != null && itotal > 0) {
            if (infoTemplate != null) {
                try {
                    Template t = new Template("pager.info", new StringReader(infoTemplate), new Configuration());
                    StringWriter sw = new StringWriter();
                    t.process(parameters, sw);
                    addParameter("pageInfo", sw.toString());
                } catch (Exception e) {
                    log.error("error when create template - " + infoTemplate, e);
                }
            } else {
                ValueStack vs = ActionContext.getContext().getValueStack();
                vs.push(parameters);
                String info = txt.getText(INFO_TEMPLATE, (String) null, (List<Object>) null, vs);
                vs.pop();
                addParameter("pageInfo", info);
            }
        }
    }

    /**
	 * @param nodataText the nodataText to set
	 */
    @StrutsTagAttribute(description = "the property for the nodataText")
    public void setNodataText(String nodataText) {
        this.nodataText = nodataText;
    }

    /**
	 * @param infoTemplate the infoTemplate to set
	 */
    @StrutsTagAttribute(description = "the property for the infoTemplate")
    public void setInfoTemplate(String infoTemplate) {
        this.infoTemplate = infoTemplate;
    }

    /**
	 * @param linkSize the linkSize to set
	 */
    @StrutsTagAttribute(description = "the property for the linkSize")
    public void setLinkSize(String linkSize) {
        this.linkSize = linkSize;
    }

    /**
	 * @param linkStyle the linkStyle to set
	 */
    @StrutsTagAttribute(description = "the property for the linkStyle")
    public void setLinkStyle(String linkStyle) {
        this.linkStyle = linkStyle;
    }

    /**
	 * @param limitLabel the limitLabel to set
	 */
    @StrutsTagAttribute(description = "the property for the limitLabel")
    public void setLimitLabel(String limitLabel) {
        this.limitLabel = limitLabel;
    }

    /**
	 * @param limitName the limitName to set
	 */
    @StrutsTagAttribute(description = "the property for the limitName")
    public void setLimitName(String limitName) {
        this.limitName = limitName;
    }

    /**
	 * @param limitList the limitList to set
	 */
    @StrutsTagAttribute(description = "the property for the limitList")
    public void setLimitList(Object limitList) {
        this.limitList = limitList;
    }

    /**
	 * @param onLimitChange the onLimitChange to set
	 */
    @StrutsTagAttribute(description = "the property for the onLimitChange")
    public void setOnLimitChange(String onLimitChange) {
        this.onLimitChange = onLimitChange;
    }

    /**
	 * @param command the command to set
	 */
    @StrutsTagAttribute(description = "the action property for the pager command")
    public void setCommand(String command) {
        this.command = command;
    }

    /**
	 * @param start the start to set
	 */
    @StrutsTagAttribute(description = "the property for the start")
    public void setStart(String start) {
        this.start = start;
    }

    /**
	 * @param end the end to set
	 */
    @StrutsTagAttribute(description = "the property for the end")
    public void setEnd(String end) {
        this.end = end;
    }

    /**
	 * @param limit the limit to set
	 */
    @StrutsTagAttribute(description = "the property for the limit")
    public void setLimit(String limit) {
        this.limit = limit;
    }

    /**
	 * @param total the total to set
	 */
    @StrutsTagAttribute(description = "the property for the total")
    public void setTotal(String total) {
        this.total = total;
    }
}
