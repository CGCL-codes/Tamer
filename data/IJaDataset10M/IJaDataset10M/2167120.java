package fr.univ_tln.inf9.exaplanning.api.facade.event;

import java.util.EventObject;
import java.util.List;

/**
 * @author pivi
 *
 * Classe Event( pour communiquer avec  les controlleurs)
 * qui gère quand la liste de professeurs change
 */
public class ListTeacherChangedEvent extends EventObject {

    private List<String> newListTeacher;

    public ListTeacherChangedEvent(Object source, List<String> string) {
        super(source);
        this.newListTeacher = string;
    }

    /**
	 * @return the newType
	 */
    public List<String> getNewListTeacher() {
        return newListTeacher;
    }
}
