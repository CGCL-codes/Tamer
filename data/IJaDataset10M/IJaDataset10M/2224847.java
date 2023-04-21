package org.granite.scan;

/**
 * @author Franck WOLFF
 */
public interface ScannedItemHandler {

    public boolean handleMarkerItem(ScannedItem item);

    public void handleScannedItem(ScannedItem item);
}
