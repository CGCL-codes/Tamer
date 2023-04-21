package de.flingelli.scrum.gui.database.products;

import de.flingelli.scrum.observer.ProductPropertyChangeSupport;

/**
 * 
 * @author Markus Flingelli
 *
 */
class DatabaseProductViewerPanelRunnable implements Runnable {

    private EnumDatabaseProduct type = null;

    public DatabaseProductViewerPanelRunnable(EnumDatabaseProduct type) {
        this.type = type;
    }

    public void run() {
        DatabaseProductsViewerPanel panel = new DatabaseProductsViewerPanel(type);
        ProductPropertyChangeSupport.getInstance().databaseViewerPanelCreated(panel);
    }
}
