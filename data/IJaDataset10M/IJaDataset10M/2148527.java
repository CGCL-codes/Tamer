package org.eclipse.smd.gef.command.state;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.smd.gef.command.SMDCommand;
import org.eclipse.smd.model.FinalState;
import org.eclipse.smd.model.IStateContainer;
import org.eclipse.smd.model.InitialState;
import org.eclipse.smd.model.State;
import org.eclipse.smd.rcp.conf.lang.Language;

/**
 * Cette commande permet d'ajouter un nouvel �tat au mod�le.
 * 
 * @author Pierrick HYMBERT (phymbert [at] users.sourceforge.net)
 *         
 */
public class CreateStateCommand extends SMDCommand {

    /**
	 * L'identifiant de la commande.
	 */
    public static final int ID = 200;

    /**
	 * Le nouvel �tat � ajouter au mod�le.
	 */
    private State state;

    /**
	 * Les coordonn�es de l'�tat.
	 */
    private Rectangle bounds;

    /**
	 * Le mod�le � �tat transition dans lequel on ajoute l'�tat.
	 */
    private IStateContainer parent;

    /**
	 * Construit une nouvelle commande pour cr�er un �tat.
	 */
    public CreateStateCommand() {
        super(Language.get(Language.CMD_CREATE_STATE));
        logger.debug("Cr�ation d'une nouvelle commande pour ajouter un �tat.");
    }

    /**
     * V�rifie que le parent ne contient pas d�j� un �tat initial.
     */
    public boolean canExecute() {
        boolean parentInitialState = false;
        boolean parentFinalState = false;
        parentFinalState = parent.getFinalState() != null;
        parentInitialState = parent.getInitialState() != null;
        return !(parentInitialState && state instanceof InitialState || parentFinalState && state instanceof FinalState);
    }

    /**
	 * Annule la commande d'ajout d'un �tat au mod�le.
	 */
    public void undo() {
        logger.debug("Annule la commande d'ajout d'un �tat.");
        parent.removeState(state);
    }

    /**
	 * Execute la commande pour ajouter un �tat au mod�le.
	 */
    public void execute() {
        logger.debug("Execute commande pour ajouter un �tat.");
        if (state instanceof InitialState) {
            state.setName("I");
        } else if (state instanceof FinalState) {
            state.setName("F");
        } else state.setName(generateUniqueName());
        state.setBounds(bounds);
        state.setParent(parent);
        parent.addState(state);
    }

    /**
	 * Execute � nouveau la commande pour ajouter l'�tat au mod�le.
	 */
    public void redo() {
        logger.debug("Re-execute commande pour ajouter un �tat.");
        parent.addState(state);
    }

    /**
	 * D�finit l'�tat � ajouter au mod�le.
	 * 
	 * @param state
	 *            L'�tat � ajouter au mod�le.
	 */
    public void setState(State state) {
        this.state = state;
    }

    /**
	 * D�finit les coordonn�es du nouvel �tat.
	 * 
	 * @param r
	 */
    public void setBounds(Rectangle r) {
        bounds = r;
    }

    /**
	 * D�finit le p�re du mod�le.
	 * 
	 * @param diagram
	 *            Le mod�le auquel on ajoute l'�tat.
	 */
    public void setParent(IStateContainer parent) {
        this.parent = parent;
    }

    /**
	 * Recherche un nom unique pour cet �tat.
	 * 
	 * @return Un nom unique.
	 */
    private String generateUniqueName() {
        int index = 0;
        String name = "S" + index;
        boolean found = false;
        while (!found) {
            boolean unique = true;
            for (State concurentState : parent.getStates()) {
                if (concurentState instanceof InitialState || concurentState instanceof FinalState) continue;
                if (concurentState.getName().equals(name)) {
                    unique = false;
                    break;
                }
            }
            if (unique) {
                found = true;
            } else {
                index++;
                name = "S" + index;
            }
        }
        return name;
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
