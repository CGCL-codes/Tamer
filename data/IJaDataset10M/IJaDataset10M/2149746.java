package de.iritgo.aktera.journal;

import de.iritgo.aktera.authentication.defaultauth.entity.AkteraUser;
import de.iritgo.simplelife.data.Tuple2;
import java.util.Map;

public interface JournalSearch {

    /**
	 * Return the category id
	 *
	 * @return The category id
	 */
    public String getCategoryId();

    /**
	 * Return the condition sql string for the listing query
	 *
	 * @param username The username
	 * @param search The search param
	 * @return The query
	 */
    public String getCondition(String search, AkteraUser user);

    /**
	 * Return the translated category label
	 *
	 * @param The user object
	 * @return The translated label for the category
	 */
    public String getCategoryLabel(AkteraUser user);

    /**
	 * Return a map with the key and objects for the condition
	 *
	 * @param search The search string
	 * @param user The user
	 * @return The contition map
	 */
    public Map<String, Object> getConditionMap(String search, AkteraUser user);
}
