package portal.presentation.cal;

import com.lutris.appserver.server.httpPresentation.HttpPresentationComms;
import org.w3c.dom.Element;
import org.w3c.dom.html.HTMLTableRowElement;
import java.util.Vector;
import java.util.Enumeration;
import java.sql.SQLException;
import java.math.BigDecimal;
import hambo.pim.ToDoDO;
import hambo.pim.ToDoBO;
import hambo.svc.ServiceException;
import hambo.pim.ParameterHandler;
import hambo.app.util.DOMUtil;
import hambo.app.util.Link;
import hambo.app.base.VirtualPortalPage;

/**
 * A virtual page for a list of todos. Each todo is presented on a row with
 * its title, an edit icon, a delete icon, and possibly a done icon. Above the
 * todos is a titlebar with a button for adding new todos.
 * <p>This currently availible for html only.
 */
public class castodo extends VirtualPortalPage {

    private boolean onlyundone = false;

    /**
     * Create a new todo virtual page.
     */
    public castodo() {
        super("ca", "castodo");
        onlyundone = true;
    }

    /**
     * Create a new todo virtual page.
     */
    public castodo(HttpPresentationComms comms, String user_id) {
        super("ca", "castodo");
        run(comms, user_id);
    }

    /**
     * This is the actual processing. It grabs the template row for a todo,
     * copies it for each todo, fills in the data and removes the template.
     */
    public Element getElement() {
        ToDoBO t = new ToDoBO();
        Vector todos = null;
        try {
            if (onlyundone) todos = t.getUndoneToDos(new BigDecimal(getContext().getSessionAttributeAsString("oid")), "subject"); else todos = t.getAllToDos(new BigDecimal(getContext().getSessionAttributeAsString("oid")), "subject");
        } catch (SQLException e) {
            todos = new Vector();
        }
        DOMUtil.setAttribute(getElement("todoAddLink"), "href", new Link("caladdtodo").toString());
        Element oldEntry = getElement("todoList");
        Element parent = (Element) DOMUtil.getParent(oldEntry);
        int color = 0;
        for (Enumeration e = todos.elements(); e.hasMoreElements(); ) {
            Element newEntry = (Element) DOMUtil.clone(oldEntry);
            ToDoDO todo = (ToDoDO) e.nextElement();
            String oidStr = todo.getOId().toString();
            if (newEntry instanceof HTMLTableRowElement) newEntry.setAttribute("class", color++ % 2 == 0 ? "listeven" : "listodd");
            Link changestatus = new Link("todovalidator");
            changestatus.addParam("oid", oidStr);
            if (!todo.getStatus()) {
                removeElement(newEntry, "done");
                changestatus.addParam("changestatus", "done");
                DOMUtil.setFirstMatchingAttribute(getElement(newEntry, "notdone"), "href", changestatus.toString());
            } else {
                removeElement(newEntry, "notdone");
                changestatus.addParam("changestatus", "not");
                DOMUtil.setFirstMatchingAttribute(getElement(newEntry, "done"), "href", changestatus.toString());
            }
            Element viewLink = getElement(newEntry, "todoViewLink");
            Element editLink = getElement(newEntry, "todoEditLink");
            Element deleteLink = getElement(newEntry, "todoDeleteLink");
            String url = ParameterHandler.addParam(viewLink.getAttribute("href"), "oid", oidStr);
            DOMUtil.setFirstMatchingAttribute(viewLink, "href", url);
            DOMUtil.setFirstNodeText(viewLink, todo.getSubject());
            url = ParameterHandler.addParam(editLink.getAttribute("href"), "oid", oidStr);
            DOMUtil.setFirstMatchingAttribute(editLink, "href", url);
            url = ParameterHandler.addParam(deleteLink.getAttribute("href"), "oid", oidStr);
            url = ParameterHandler.addParam(url, "deleteTodo", "");
            DOMUtil.setFirstMatchingAttribute(deleteLink, "href", url);
            DOMUtil.appendChild(parent, newEntry);
        }
        if (todos.size() != 0) {
            removeElement("notodo");
        }
        if (parent != null) parent.removeChild(oldEntry);
        return super.getElement();
    }
}
