package com.tensegrity.palobrowser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import org.palo.api.Cube;
import org.palo.api.Database;
import org.palo.api.Dimension;
import org.palo.api.Hierarchy;
import com.tensegrity.palobrowser.preferences.PreferenceConstants;

/**
 * <code>DomainFilterManager</code>
 * <p>
 * A singleton to manage <code>DomainFilter</code>s. Two filters are defined
 * by default, namely a filter to exclude system entities and a dummy filter
 * which accepts every cube and dimension. Use the <code>PreferenceStore</code>
 * of the <code>PalobrowserPlugin</code> and the defined preference constants 
 * to switch between those filters. 
 * Refer to {@link com.tensegrity.palobrowser.preferences.PreferenceConstants}. 
 * </p>
 * <p>
 * Note: only one filter can be active at the same time. A class can add itself
 * as a {@link com.tensegrity.palobrowser.DomainFilterListener} to get notified
 * if the used filter changes. 
 * </p>
 *
 * @author Stepan Rutz
 * @version $ID$
 */
public class DomainFilterManager {

    /**
	 * A filter to exclude system entities, like internal used system cubes
	 */
    public static final DomainFilter DOMAINFILTER_EXCLUDE_SYSTEMENTITIES = new DomainFilter() {

        public String getName() {
            return "DOMAINFILTER_EXCLUDE_SYSTEMENTITIES";
        }

        public boolean acceptCube(Cube cube) {
            return !SystemDomainObject.isSystemCube(cube);
        }

        public boolean acceptDimension(Dimension dimension) {
            return !SystemDomainObject.isSystemDimension(dimension);
        }

        public boolean acceptDatabase(Database database) {
            return !database.isSystem();
        }

        public boolean acceptHierarchy(Hierarchy hierarchy) {
            return hierarchy.isNormal();
        }
    };

    /**
	 * A dummy filter which accepts any cube and dimension.
	 */
    public static final DomainFilter DOMAINFILTER_EXCLUDE_NONE = new DomainFilter() {

        public String getName() {
            return "DOMAINFILTER_EXCLUDE_NONE";
        }

        public boolean acceptCube(Cube cube) {
            return true;
        }

        public boolean acceptDimension(Dimension dimension) {
            return true;
        }

        public boolean acceptDatabase(Database database) {
            return true;
        }

        public boolean acceptHierarchy(Hierarchy hierarchy) {
            return true;
        }
    };

    private static DomainFilterManager instance = new DomainFilterManager();

    /**
     * Returns the used <code>DomainFilterManager</code> instance.
     * @return the <code>DomainFilterManager</code> instance
     */
    public static DomainFilterManager getInstance() {
        return instance;
    }

    private LinkedHashSet listeners;

    private DomainFilter domainFilter;

    private DomainFilterManager() {
        listeners = new LinkedHashSet();
        if (PalobrowserPlugin.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.PREF_SHOW_SYSTEM_CUBES_AND_DIMENSIONS)) domainFilter = DOMAINFILTER_EXCLUDE_NONE; else domainFilter = DOMAINFILTER_EXCLUDE_SYSTEMENTITIES;
    }

    /**
     * Returns the currently active <code>DomainFilter</code> 
     * @return the currently active <code>DomainFilter</code>
     */
    public DomainFilter getDomainFilter() {
        return domainFilter;
    }

    /**
     * Sets the currently active <code>DomainFilter</code>. If the given filter
     * is not the actual used filter a filter changed event is fired. 
     * @param domainFilter the new <code>DomainFilter</code>
     */
    public void setDomainFilter(DomainFilter domainFilter) {
        if (this.domainFilter == domainFilter) return;
        DomainFilter oldFilter = this.domainFilter;
        DomainFilter newFilter = domainFilter;
        this.domainFilter = domainFilter;
        fireDomainFilterChanged(oldFilter, newFilter);
    }

    /**
     * Adds the given <code>DomainFilterListener</code> to the listener list
     * @param listener a <code>DomainFilterListener</code>
     */
    public void addDomainFilterListener(DomainFilterListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes the given <code>DomainFilterListener</code> from the listener list
     * @param listener the <code>DomainFilterListener</code> to remove
     */
    public void removeDomainFilterListener(DomainFilterListener listener) {
        listeners.remove(listener);
    }

    private void fireDomainFilterChanged(DomainFilter oldFilter, DomainFilter newFilter) {
        ArrayList copy = new ArrayList(listeners);
        for (Iterator it = copy.iterator(); it.hasNext(); ) {
            DomainFilterListener listener = (DomainFilterListener) it.next();
            listener.domainFilterChanged(oldFilter, newFilter);
        }
    }
}
