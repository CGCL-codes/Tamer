package org.likken.web.states;

import org.likken.web.*;
import org.likken.web.actions.*;
import org.likken.web.inspection.*;
import org.likken.core.Category;
import org.likken.core.CategoryIterator;
import org.likken.core.schema.AttributeType;
import org.likken.core.schema.AttributeTypeIterator;
import org.likken.core.inspection.InspectionException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Stephane Boisson
 * @version $Revision: 1.7 $ $Date: 2004/02/24 22:44:13 $
 */
public class EntryDisplayState extends AbstractEntryState {

    public EntryDisplayState() throws StateException {
        super();
    }

    public void visitAction(Action theEvent) throws StateException {
        theEvent.perform(this);
    }

    public String getName() {
        return Constants.States.ENTRY_DISPLAY;
    }

    public ActionFactory getActionFactory() {
        return EntryDisplayActionFactory.getInstance();
    }

    public String getCategoryMenu() {
        StringBuffer sb = new StringBuffer();
        CategoryIterator it = getEntry().getCategories();
        while (it.hasNext()) {
            Category category = it.nextCategory();
            if (!hasCurrentCategory(category)) {
                sb.append("<A HREF=\"");
                sb.append(getURLBase());
                sb.append("selectCategory?category=");
                sb.append(category.getID());
                sb.append("&entryDN=");
                sb.append(Utils.encodeToURL(getEntry().getDN().toString()));
                sb.append("\" TITLE=\"");
                sb.append(category.getDescription());
                sb.append("\">");
                sb.append(category.getDisplayName());
                sb.append("</A>");
            } else {
                sb.append(category.getDisplayName());
            }
            sb.append("<BR>\n");
        }
        return sb.toString();
    }

    public String getCategoryView() throws InspectionException {
        AttributeWebInspectorFactory inspectorFactory = AttributeWebInspectorFactory.getInstance();
        StringBuffer sb = new StringBuffer();
        AttributeTypeIterator it;
        sb.append("<INPUT TYPE=\"hidden\" NAME=\"entryDN\" VALUE=\"");
        sb.append(getEntry().getDN());
        sb.append("\">\n");
        sb.append("<INPUT TYPE=\"hidden\" NAME=\"category\" VALUE=\"");
        sb.append(getCurrentCategory().getID());
        sb.append("\">\n");
        it = getCurrentCategory().getAttributeTypes(getAttributes());
        while (it.hasNext()) {
            AttributeType type = it.nextAttributeType();
            sb.append("<P><STRONG>");
            sb.append(type.getDisplayName());
            sb.append("</STRONG><BR>\n");
            if (inspectorFactory.canHandle(type)) {
                AttributeWebInspector inspector = inspectorFactory.getWebInspector(type);
                int count = getAttributes().getSize(type);
                for (int i = 0; i < count; ++i) {
                    if (i > 0) {
                        sb.append("<BR>\n");
                    }
                    sb.append(inspector.getView(getEntry().getDN(), type, getAttributes().get(type, i), i));
                }
            } else {
                sb.append("<EM>Not supported</EM>");
            }
            sb.append("</P>\n");
        }
        return sb.toString();
    }

    public void sendResponse(ServletContext context, HttpServletRequest request, HttpServletResponse response) throws WebException {
        ForwardingResponse fwd = new ForwardingResponse(this, "/jsp/entry/display.jsp");
        fwd.sendResponse(context, request, response);
    }
}
