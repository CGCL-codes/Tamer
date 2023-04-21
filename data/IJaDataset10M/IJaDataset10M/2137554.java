package cz.zcu.kiv.properties_dialogs.beans;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import org.apache.commons.logging.Log;
import org.opencms.jsp.CmsJspXmlContentBean;
import org.opencms.jsp.I_CmsXmlContentContainer;
import org.opencms.main.CmsLog;

/**
 * Web KIV modul <strong>cz.zcu.kiv.properties_dialogs</strong>
 *
 * Copyright (c) 2007-2009 Department of Computer Science,
 * University of West Bohemia, Pilsen, CZ
 *
 * This software and this file is available under the Creative Commons
 * Attribution-Noncommercial-Share Alike license.  You may obtain a copy
 * of the License at   http://creativecommons.org/licenses/   .
 *
 * This software is provided on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.  See the License
 * for the specific language governing permissions and limitations.
 * 
 * @author Stanislav Skalicky <skalicky.s@gmail.com>
 */
public class PropertyBean extends CmsJspXmlContentBean implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /** XmlContent element name from property.xsd  */
    private static final String ELEMENT_PROPERTY_NAME = "PropertyName";

    /** XmlContent element name from property.xsd  */
    private static final String ELEMENT_NICE_NAME = "NiceName";

    /** XmlContent element name from property.xsd  */
    private static final String ELEMENT_HELP = "Help";

    /** XmlContent element name from property.xsd  */
    private static final String ELEMENT_WIDGET = "Widget";

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(PropertyBean.class);

    private String name;

    private String niceName;

    private String help;

    private WidgetBean widget;

    /**
	 * Default constructor, required for every Java Bean
	 */
    public PropertyBean() {
        super();
    }

    /**
	 * 
	 * @param context the JSP page context object
	 * @param req the JSP request
	 * @param res the JSP response
	 */
    public PropertyBean(PageContext context, HttpServletRequest req, HttpServletResponse res) {
        super(context, req, res);
    }

    /**
	 * This method reads fields of xmlContent <code>property-widget</code>
	 * and saves it into the instance variables
	 * 
	 * @param container XML content container
	 */
    public void contentSave(I_CmsXmlContentContainer container) {
        this.name = this.contentshow(container, ELEMENT_PROPERTY_NAME);
        this.niceName = this.contentshow(container, ELEMENT_NICE_NAME);
        this.help = this.contentshow(container, ELEMENT_HELP);
        I_CmsXmlContentContainer widgetContainer = this.contentloop(container, ELEMENT_WIDGET);
        try {
            if (widgetContainer.hasMoreContent()) {
                WidgetBean widget = new WidgetBean(this.getJspContext(), this.getRequest(), this.getResponse());
                widget.contentSave(widgetContainer);
                this.widget = widget;
            }
        } catch (JspException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info(e.getLocalizedMessage());
            }
        }
    }

    public String getName() {
        return name;
    }

    public String getNiceName() {
        return niceName;
    }

    public String getHelp() {
        return help;
    }

    public WidgetBean getWidget() {
        return widget;
    }

    @Override
    public String toString() {
        StringBuffer result = new StringBuffer("");
        result.append("Property: " + name);
        if (widget != null) result.append(" || " + widget.toString());
        return result.toString();
    }
}
