package info.joseluismartin.dao;

import java.util.EventListener;

/**
 * Listener interface for paginator changes
 * 
 * @author Jose Luis Martin - (jlm@joseluismartin.info)
 * @see info.joseluismartin.gui.Paginator
 * @see info.joseluismartin.gui.PaginatorView
 */
public interface PaginatorListener extends EventListener {

    /**
	 * Notify of paginator changes with a PaginatorChangedEvent
	 * @param event the PaginatorChanged event
	 */
    public void pageChanged(PageChangedEvent event);
}
