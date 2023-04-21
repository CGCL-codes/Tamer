package citadelles.metier.roles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import citadelles.metier.roles.actions.ActionJeu;

public class ActionsDisponibles implements Iterable<ActionJeu> {

    private List<ActionJeu> listeDActions = new ArrayList<ActionJeu>();

    @Override
    public Iterator<ActionJeu> iterator() {
        return listeDActions.iterator();
    }

    public void addAll(Collection<ActionJeu> actions) {
        this.listeDActions.addAll(actions);
    }

    public void add(ActionJeu action) {
        this.listeDActions.add(action);
    }

    public void remove(ActionJeu actionJeu) {
        this.listeDActions.remove(actionJeu);
    }
}
