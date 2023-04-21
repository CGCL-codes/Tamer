package net.ezcontent.web.admin;

import net.ezcontent.auth.User;
import net.ezcontent.web.admin.conf.Section;
import net.ezcontent.web.admin.conf.MenuOption;
import net.ezcontent.web.admin.conf.ViewConfiguration;
import java.io.Serializable;
import java.util.TreeMap;

/**
 * Representa una sesion de usuario en Tapestry. Guarda el usuario que
 * esta identificado para la sesion, la seccion y opcion actuales.
 * 
 * @author Enrique Zamudio
 */
public class Visit implements Serializable {

    private User user;

    private Object editedObject;

    private Section currentSection;

    private MenuOption currentOption;

    private ViewConfiguration currentView;

    private TreeMap keys;

    public Visit() {
    }

    /** Fija el usuario que esta usando esta sesion. */
    public void setUser(User value) {
        user = value;
    }

    public User getUser() {
        return user;
    }

    /** Fija el objeto que esta siendo editado. */
    public void setEditedObject(Object value) {
        editedObject = value;
    }

    public Object getEditedObject() {
        return editedObject;
    }

    /** Fija la seccion donde esta actualmente el usuario. Limpia los
	 * valores de opcion y vista actuales. */
    public void setCurrentSection(Section value) {
        currentSection = value;
        setCurrentOption(null);
        setCurrentView(null);
    }

    public Section getCurrentSection() {
        return currentSection;
    }

    /** Fija la opcion actual, dentro de la seccion actual.
	 * Olvida la vista actual. */
    public void setCurrentOption(MenuOption value) {
        currentOption = value;
        setCurrentView(null);
    }

    public MenuOption getCurrentOption() {
        return currentOption;
    }

    /** Fija la vista actual, dentro de la opcion y seccion actuales. */
    public void setCurrentView(ViewConfiguration value) {
        currentView = value;
    }

    public ViewConfiguration getCurrentView() {
        return currentView;
    }

    /** Guarda un objeto arbitrario bajo una llave. */
    public void putValue(String key, Object value) {
        if (keys == null) keys = new TreeMap();
        keys.put(key, value);
    }

    /** Devuelve el objeto que ha sido guardado bajo la llave indicada. */
    public Object getValue(String key) {
        if (keys == null) return null;
        return keys.get(key);
    }

    public void forgetValue(String key) {
        if (keys != null) keys.remove(key);
    }
}
