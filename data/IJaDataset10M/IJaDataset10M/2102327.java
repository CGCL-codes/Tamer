package com.prolix.editor.mainmenue.activites;

import java.util.ArrayList;
import java.util.Iterator;
import com.prolix.editor.listener.MenueActivityChangeListener;
import com.prolix.editor.mainmenue.MenueItem;
import uk.ac.reload.straker.datamodel.DataComponent;
import uk.ac.reload.straker.datamodel.learningdesign.LD_DataComponent;

/**
 * Head of all Menue Activities Templates
 * (Templates are Generated to Create new Instances for the Model and they dont affact the XML Export file)
 * @author prenner
 *
 */
public abstract class MenueActivity extends MenueItem {

    /**
	 * Link to the Reload Activity Element
	 */
    private LD_DataComponent _activity;

    /**
	 * Manages the Listener for the Activity Template Link
	 */
    private ArrayList _menueActivityChangeListener;

    public MenueActivity(LD_DataComponent activity, MenueItem parent) {
        _activity = activity;
        setParent(parent);
    }

    /**
	 * Adds a new Listener
	 * @param listener
	 */
    public void addMenueActivityChangeListener(MenueActivityChangeListener listener) {
        if (_menueActivityChangeListener == null) _menueActivityChangeListener = new ArrayList();
        _menueActivityChangeListener.add(listener);
    }

    /**
	 * Removes a Listener
	 * @param listener
	 */
    public void removeMenueActivityChangeListener(MenueActivityChangeListener listener) {
        if (_menueActivityChangeListener != null && listener != null) _menueActivityChangeListener.remove(listener);
    }

    /**
	 * Fire the Listener when a Template is changed
	 *
	 */
    public void fireMenueActivityChangeListener() {
        if (_menueActivityChangeListener != null && _menueActivityChangeListener.size() > 0) {
            Iterator it = _menueActivityChangeListener.iterator();
            while (it.hasNext()) ((MenueActivityChangeListener) it.next()).ActivityChanged(this);
        }
    }

    public DataComponent getData() {
        return _activity;
    }

    public String getName() {
        if (_activity != null) return _activity.getTitle();
        return "";
    }

    public String toString() {
        return getName();
    }

    /**
	 * Set a new Name for the Activity Template
	 * @param name
	 */
    public void setTitle(String name) {
        if (name != null) {
            _activity.setTitle(name);
            fireMenueModelLinkListenerName();
        }
    }

    protected String getMenueIdentifier() {
        return _activity.getIdentifier();
    }

    /**
	 * Removes Self from the Tree and if wished all linked Elements from the Model
	 * @param removeDiagramElements
	 */
    public boolean canDelete() {
        return true;
    }

    public void removeSelf() {
        super.removeSelf();
        refresh();
    }
}
