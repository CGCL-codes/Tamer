package fr.soleil.hdbtdbArchivingApi.ArchivingWatchApi.strategy.choosing;

import java.util.Hashtable;

/**
 * A basic strategy that picks all attributes unconditionally.
 * 
 * @author CLAISSE
 */
public class TotalChoosingStrategy implements IChoosingStrategy {

    private int numberOfAttributesToControl;

    TotalChoosingStrategy() {
        super();
    }

    public Hashtable filterAttributesToControl(Hashtable allAttributes) {
        this.numberOfAttributesToControl = allAttributes.size();
        return allAttributes;
    }

    /**
	 * @return Returns the numberOfAttributesToControl.
	 */
    public int getNumberOfAttributesToControl() {
        return numberOfAttributesToControl;
    }

    public Hashtable getUndeterminedAttributes() {
        return null;
    }
}
