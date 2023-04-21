package fr.univ_tln.inf9.exaplanning.api.facade.event;

import java.util.EventObject;
import java.util.List;

/**
 * @author pivi
 * Classe Event( pour communiquer avec  les controlleurs)
 * qui gère quand la liste d'administrateurs change
 */
public class ListAdminChangedEvent extends EventObject {

    private List<String> newListAdmin;

    public ListAdminChangedEvent(Object source, List<String> string) {
        super(source);
        this.newListAdmin = string;
    }

    /**
	 * @return the new admin
	 */
    public List<String> getNewListAdmin() {
        return newListAdmin;
    }
}
