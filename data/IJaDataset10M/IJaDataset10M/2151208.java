package gameslave.servlet;

import gameslave.GameslaveStorage;
import gameslave.db.CompareNamedEntitiesByName;
import gameslave.db.Entity;
import gameslave.db.NamedEntityInABook;
import gameslave.dnd35.db.Book;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * @author Dobes
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class GameslaveMultiActionController extends MultiActionController {

    public abstract GameslaveStorage getStorage();

    protected void bind(ServletRequest request, Object command) throws ServletException {
        ServletRequestDataBinder binder = new ServletRequestDataBinder(command, "this");
        new GameslaveBindInitializer(getStorage()).initBinder(request, binder);
        binder.bind(request);
        binder.closeNoCatch();
    }

    protected Map bindAndValidate(ServletRequest request, Object command) throws ServletException {
        ServletRequestDataBinder binder = new ServletRequestDataBinder(command, "this");
        new GameslaveBindInitializer(getStorage()).initBinder(request, binder);
        binder.bind(request);
        return binder.getErrors().getModel();
    }

    protected void putIntoDefaultBook(HttpServletRequest request, NamedEntityInABook entity) {
        Integer defaultBookId = (Integer) request.getSession(true).getAttribute("defaultBookId");
        if (defaultBookId != null) {
            Book book = getStorage().getBook(defaultBookId);
            if (book != null) {
                entity.addToBook(book);
            }
        }
    }

    protected Map bindChangeBookAndSetDefaultBook(HttpServletRequest request, NamedEntityInABook entity) throws ServletException {
        Book oldBook = (Book) entity.getBook();
        Map model = bindAndValidate(request, (Object) entity);
        if (entity.getBook() != oldBook) {
            if (oldBook != null) entity.removeFromBook(oldBook);
            if (entity.getBook() != null) entity.addToBook(entity.getBook());
        }
        request.getSession(true).setAttribute("defaultBookId", entity.getBook().getId());
        return model;
    }

    public Map getModel(Object command) {
        return new BindException(command, "this").getModel();
    }

    public TreeSet getEntitySet(HttpServletRequest request, String prefix, Class clazz) {
        TreeSet entities = new TreeSet(new CompareNamedEntitiesByName());
        Pattern quantities = Pattern.compile(prefix + "\\[([0-9]+)\\]");
        for (Iterator i = request.getParameterMap().keySet().iterator(); i.hasNext(); ) {
            String paramName = (String) i.next();
            Matcher m = quantities.matcher(paramName);
            if (m.matches()) {
                if (request.getParameter(paramName).equals("on")) {
                    Object member = getStorage().getEntity(clazz, new Integer(m.group(1)));
                    entities.add(member);
                }
            }
        }
        return entities;
    }

    public TreeSet getIntegerSet(HttpServletRequest request, String prefix) {
        TreeSet result = new TreeSet();
        Pattern quantities = Pattern.compile(prefix + "\\[([0-9]+)\\]");
        for (Iterator i = request.getParameterMap().keySet().iterator(); i.hasNext(); ) {
            String paramName = (String) i.next();
            Matcher m = quantities.matcher(paramName);
            if (m.matches()) {
                if (request.getParameter(paramName).equals("on")) {
                    Integer integer = new Integer(m.group(1));
                    result.add(integer);
                    System.out.println("Adding class bonus feat level: " + integer);
                }
            }
        }
        return result;
    }

    public TreeMap getEntityToIntMap(HttpServletRequest request, Class clazz, String prefix) {
        TreeMap newRacialSkills = new TreeMap(new CompareNamedEntitiesByName());
        Pattern skillPts = Pattern.compile(prefix + "\\[([0-9]+)\\]");
        for (Iterator i = request.getParameterMap().keySet().iterator(); i.hasNext(); ) {
            String paramName = (String) i.next();
            Matcher m = skillPts.matcher(paramName);
            if (m.matches()) {
                Integer integer = new Integer(request.getParameter(paramName));
                if (integer.intValue() != 0) {
                    Entity ent = getStorage().getEntity(clazz, new Integer(m.group(1)));
                    newRacialSkills.put(ent, integer);
                }
            }
        }
        return newRacialSkills;
    }
}
