package atai.questions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public interface Answer {

    /**
	 * Role origin in a relationship.
	 */
    public static final String ROLE_ORIGIN = "origin";

    /**
	 * Role target in a relationship.
	 */
    public static final String ROLE_TARGET = "target";

    /**
	 * @return the language
	 */
    public String getLanguage();

    /**
	 * @param language the language to set
	 */
    public void setLanguage(String language);

    /**
	 * @return the entities
	 */
    public List<String> getEntities();

    /**
	 * @return the entity types
	 */
    public Map<String, String> getEntityTypes();

    /**
	 * @param entityTypes the entityTypes to set
	 */
    public void setEntityTypes(Map<String, String> entityTypes);

    /**
	 * @return the relations
	 */
    public List<String> getRelations();

    /**
	 * @return the relation types
	 */
    public Map<String, String> getRelationTypes();

    /**
	 * @param relationTypes the relationTypes to set
	 */
    public void setRelationTypes(Map<String, String> relationTypes);

    /**
	 * @return the roles of relations.
	 */
    public Map<String, List<String>> getRoles();

    /**
	 * @return the role types.
	 */
    public Map<String, String> getRoleTypes();

    /**
	 * @param roles the roles to set
	 */
    public void setRoles(Map<String, List<String>> roles);

    /**
	 * @param roleTypes the role types to set
	 */
    public void setRoleTypes(Map<String, String> roleTypes);

    /**
	 * @return the players as pairs <role id, player id>
	 */
    public Map<String, String> getPlayers();

    /**
	 * @param players the players to set
	 */
    public void setPlayers(Map<String, String> players);

    /**
	 * 
	 * @param relation
	 * @param role
	 */
    public String getPlayer(String relation, String role);

    /**
	 * 
	 * @param relation
	 * @param role
	 * @param player 
	 */
    public void setPlayer(String relation, String role, String player);

    /**
	 * @return the elements that are new in this answer.
	 */
    public List<String> getNewElements();

    /**
	 * param newElements  the elements that are new in this answer to set.
	 */
    public void setNewElements(List<String> newElements);

    /**
	 * 
	 * @param element
	 * @return if the elememt is new.
	 */
    public boolean isNewElement(String element);

    /**
	 * 
	 * @param element   the new element to set
	 */
    public void addNewElement(String element);
}
