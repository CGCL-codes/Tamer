package jEcoSim.Model;

import jEcoSim.Client.view.pnlList;
import java.awt.Container;
import java.util.Vector;

/**
 * 
 * @author TFadmin
 * 
 * @param <T>
 *          Type implementing BusinessObject
 */
public abstract class List<T extends BusinessObject> implements IDesign {

    /**
   * stores the T items
   */
    private Vector<T> _list = null;

    /**
   * create new List
   */
    public List() {
        this._list = new Vector<T>();
    }

    /**
   * check if type already in list
   * 
   * @param itm
   *          to check
   * @return true if already in list
   */
    public boolean containsListItemType(BusinessObject itm) {
        for (BusinessObject i : this._list) {
            if (i.getClass().equals(itm.getClass())) {
                return true;
            }
        }
        return false;
    }

    /**
   * 
   * @return all items in this list
   */
    public Vector<T> Items() {
        return this._list;
    }

    public void Load(java.util.List<T> items) {
        for (T itm : items) {
            _list.add(itm);
        }
    }

    public void Load(java.util.List<T> items, BusinessObject Parent) {
        for (T itm : items) {
            _list.add(itm);
            itm.setParent(Parent);
        }
    }

    /**
   * 
   * @return title of the List
   */
    public abstract String getTitle();

    /**
   * 
   * @return true if unique items are required
   */
    public boolean uniqueItems() {
        return true;
    }

    /**
   * 
   * @return enable scrolling, default true
   */
    public boolean scroll() {
        return true;
    }

    /**
   * 
   * @return all possible items from type T
   */
    public abstract T[] getPossibleListItems();

    private Container _panel = null;

    @Override
    public Container getPanel() {
        if (this._panel == null) {
            return new pnlList<T>(this, this.scroll());
        }
        return this._panel;
    }
}
