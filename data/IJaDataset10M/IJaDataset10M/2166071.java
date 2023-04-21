package edu.osu.cse.be.model.exceptions;

import java.util.Collection;
import edu.osu.cse.be.model.Game;

public class AllowedGameConflictException extends Exception {

    public Collection conflicts;

    /**
	 * 
	 * @uml.property name="game"
	 * @uml.associationEnd multiplicity="(1 1)"
	 */
    public Game game;

    public AllowedGameConflictException(Collection c, Game game) {
        conflicts = c;
        this.game = game;
    }
}
