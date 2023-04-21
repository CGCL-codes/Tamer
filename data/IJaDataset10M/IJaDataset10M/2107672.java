package org.elogistics.domain.entities.game;

import java.io.Serializable;
import org.elogistics.domain.entities.AbstractBusinessGameEntity;
import org.elogistics.domain.exception.DomainException;
import org.elogistics.domain.interfaces.entities.IProcess;
import org.elogistics.domain.interfaces.visitor.IVisitor;

/**
 * 
 * @see	{@link ITurn}
 * @author Jurkschat, Oliver
 */
public class Turn extends AbstractBusinessGameEntity<Integer> implements IProcess, Serializable, ITurn {

    /**
	 * Seriennummer der Klasse Turn.
	 */
    private static final long serialVersionUID = -1764954898532950080L;

    /**
	 * Das Spiel, dem diese Spielrunde angeh___rt.
	 */
    private Game game;

    /**
	 * Die Parameter der Spielrunde.
	 */
    private TurnParameters turnParameters;

    private IProcess.State state = IProcess.State.CREATED;

    /**
	 * Der Standart-Constructor wird von Hibernate gefordert und
	 * kann auch verwendet werden, um Instanzen der Spielrunde zu erzeugen.
	 * Es wird nichts weiter instantiiert.
	 */
    public Turn() {
        this.state = State.CREATED;
    }

    /**
	 * Erzeugt eine Spielrunde mit einem neuen Namen.
	 * 
	 * Ein mit diesem Constructor erzeugtes Turn-Objekt
	 * wird nur persistent, wenn mit newGame ein persistentes
	 * Spiel ___bergeben wurde.
	 * 
	 * @param newName	Der Name der Spielrunde.
	 * @param newGame	Das Spiel, dem die Spielrunde angeh___ren soll.
	 * @throws 	DomainException	
	 * 		Wenn die vorherige Spielrunde des Spiels nicht beentet wurde.
	 *	TODO	Game sollte weg, Turn wird transient erzeugt und persistent, wenn
	 *			zugewiesen.					
	 * 
	 */
    public Turn(final String newName, final Game newGame) throws DomainException {
        this.setName(newName);
        this.state = IProcess.State.CREATED;
    }

    /**
	 * Creates a Turn as copy of the passed source-turn.
	 * @param source
	 */
    public Turn(ITurn source) {
        super(source);
        this.setGame(source.getGame());
        this.state = source.getState();
    }

    /**
	 * Erzeugt eine Spielrunde mit einem neuen Namen.
	 * 
	 * Ein mit diesem Constructor erzeugtes Turn-Objekt
	 * wird erst dann persistent, wenn ihm ___ber
	 * setGame() ein persistentes Spiel zugewiesen
	 * wurde.
	 * 
	 * @param newName	Der Name der neuen Spielrunde.
	 */
    public Turn(final String newName) {
        this.setName(newName);
        this.state = State.CREATED;
    }

    public Game getGame() {
        return this.game;
    }

    public void setGame(final Game newGame) {
        this.game = newGame;
    }

    public TurnParameters getTurnParameters() {
        if (this.turnParameters == null) {
            this.turnParameters = new TurnParameters(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        }
        return this.turnParameters;
    }

    public void setTurnParameters(final TurnParameters newTurnParameters) {
        this.turnParameters = newTurnParameters;
    }

    public final boolean isModifiable() {
        return (!this.getState().equals(IProcess.State.RUNNING));
    }

    public void accept(IVisitor visitor) {
        this.getTurnParameters().accept(visitor);
        visitor.visitTurn(this);
    }

    public IProcess.State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
        this.notifyObservers();
    }
}
