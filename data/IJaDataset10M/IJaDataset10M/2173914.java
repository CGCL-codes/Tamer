package net.sf.balm.common;

/**
 * 
 * 
 * @author dz
 */
public interface CompositeAction extends Action {

    /**
     * 
     * @return
     */
    public boolean isEmpty();

    /**
     * 
     * @param action
     */
    public void add(Action action);

    /**
     * 
     *
     */
    public void clear();

    /**
     * 
     * @param action
     */
    public void remove(Action action);

    /**
     * 
     * @return
     */
    public Action[] getChildren();
}
