package org.geonetwork.domain.filter110.sort;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author heikki doeleman
 *
 */
public class SortBy {

    List<SortProperty> sortPropertyList = new ArrayList<SortProperty>();

    public List<SortProperty> getSortPropertyList() {
        return sortPropertyList;
    }

    public void setSortPropertyList(List<SortProperty> sortPropertyList) {
        this.sortPropertyList = sortPropertyList;
    }

    /**
	 * For Jixb binding.
	 * 
	 * @return
	 */
    @SuppressWarnings("unused")
    private static List<SortProperty> sortPropertyFactory() {
        return new ArrayList<SortProperty>();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((sortPropertyList == null) ? 0 : sortPropertyList.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SortBy other = (SortBy) obj;
        if (sortPropertyList == null) {
            if (other.sortPropertyList != null) return false;
        } else if (!sortPropertyList.equals(other.sortPropertyList)) return false;
        return true;
    }
}
