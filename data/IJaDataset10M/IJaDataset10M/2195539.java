package org.eclipse.smd.gef.command.state;

import java.util.ArrayList;
import org.eclipse.smd.gef.command.SMDCommand;
import org.eclipse.smd.gef.command.transition.DeleteTransitionCommand;
import org.eclipse.smd.model.IStateContainer;
import org.eclipse.smd.model.State;
import org.eclipse.smd.model.Transition;
import org.eclipse.smd.rcp.conf.lang.Language;

/**
 * Commande de suppression d'un �tat.
 * 
 * @author Pierrick HYMBERT (phymbert [at] users.sourceforge.net)
 *         
 */
public class DeleteStateCommand extends SMDCommand {

    /**
	 * L'identifiant de la commande.
	 */
    public static final int ID = 201;

    /**
	 * L'�tat � retirer du mod�le.
	 */
    private State state;

    /**
	 * Le mod�le duquel on va retirer le mod�le.
	 */
    private IStateContainer parent;

    /**
	 * Les commandes pour supprimer les transitions.
	 */
    private ArrayList<DeleteTransitionCommand> deleteTransitionCommands;

    /**
	 * Construit une nouvelle commande de suppression d'un �tat.
	 */
    public DeleteStateCommand() {
        super(Language.get(Language.CMD_DELETE_STATE));
        logger.debug("Cr�ation d'une nouvelle commande pour supprimer un �tat.");
    }

    /**
	 * Annule la suppression de l'�tat et restore ces transitions.
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
    public void undo() {
        logger.debug("Annule la suppression de l'�tat.");
        restoreTransitions();
        parent.addState(state);
    }

    /**
	 * Supprime l'�tat et ses transitions.
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
    public void execute() {
        logger.debug("Execute la suppression de l'�tat.");
        deleteTransitions();
        parent.removeState(state);
    }

    /**
	 * Execute toutes les commandes de suppressions des transitions.
	 */
    private void deleteTransitions() {
        logger.debug("Supprime les transitions de l'�tat.");
        deleteTransitionCommands = new ArrayList<DeleteTransitionCommand>();
        for (Transition transition : state.getAllTransitions()) {
            DeleteTransitionCommand command = new DeleteTransitionCommand();
            command.setTransition(transition);
            command.execute();
            deleteTransitionCommands.add(command);
        }
    }

    /**
	 * Annule toutes les suppression des transitions.
	 */
    private void restoreTransitions() {
        logger.debug("Restore les transitions de l'�tat.");
        for (DeleteTransitionCommand command : deleteTransitionCommands) {
            command.undo();
        }
    }

    /**
	 * D�finit l'�tat � supprimer.
	 * 
	 * @param state
	 *            L'�tat � supprimer.
	 */
    public void setState(State state) {
        this.state = state;
    }

    /**
	 * D�finit le mod�le duquel on va supprimer l'�tat.
	 * 
	 * @param statesMachines
	 *            Le mod�le duquel on va supprimer l'�tat.
	 */
    public void setParent(IStateContainer parent) {
        this.parent = parent;
    }

    /**
	 * Retourne l'identifiant de la commande.
	 * 
	 * @return L'identifiant de la commande.
	 */
    public int getId() {
        return ID;
    }
}
