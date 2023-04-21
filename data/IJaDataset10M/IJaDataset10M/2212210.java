package playground.marcel.filters.filter.finalFilters;

import java.util.HashSet;
import java.util.Set;
import org.matsim.basic.v01.Id;
import org.matsim.plans.Person;
import playground.marcel.filters.filter.PersonFilterA;

/**
 * A PersonIDsExporter exports a set of Person- IDs, these persons have passed
 * all the PersonFilters. So a PersonIDsExporter should be used as the last
 * PersonFilterA.
 * 
 * @author ychen
 * 
 */
public class PersonIDsExporter extends PersonFilterA {

    private static Set<Id> idSet = new HashSet<Id>();

    @Override
    public boolean judge(Person person) {
        return true;
    }

    /**
	 * Returns a set of Person-IDs
	 * 
	 * @return a Set of Person-IDs
	 */
    public Set<Id> idSet() {
        System.out.println("exporting " + idSet.size() + " person- IDs.");
        return idSet;
    }

    /**
	 * When the function is called, the Person- ID of the person is being put
	 * into the idSet.
	 */
    @Override
    public void run(Person person) {
        idSet.add(person.getId());
    }
}
