package za.co.OO7J;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Original OO7 comment:
 * 
 * structure for keeping track of a list of base assemblies that reference a
 * composite part as either a shared or private member
 * 
 * Some implementations store the BaseAssembly id's to be linked to
 * compositeParts later: Ontos implementation Others like the original oo7
 * versant version stores the basesAssembly objects
 * 
 * This implementations stores the BaseAssembly id's.
 * 
 * The lookup for the matching BaseAssembly object using the id is done
 * in the CompositePart with the linking.
 * 
 * It could be implemented here as in the Ontos version
 * 
 * @author pvz 23 Nov 2007
 * 
 */
public class BAidList {

    private Set<Long> baIdList = null;

    /**
	 * pvz 10 Nov 2007
	 * 
	 * @return the baIdList
	 */
    public Set<Long> getBaIdList() {
        return baIdList;
    }

    /**
	 * pvz 10 Nov 2007
	 * 
	 * @param baIdList
	 *            the baIdList to set
	 */
    public void setBaIdList(Set<Long> baIdList) {
        this.baIdList = baIdList;
    }

    public Set<Long> next() {
        Set<Long> set = new TreeSet<Long>();
        set.add(baIdList.iterator().next());
        return set;
    }
}
