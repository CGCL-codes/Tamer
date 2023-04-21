package org.elogistics.domain.entities.dto;

import org.elogistics.domain.entities.AbstractBusinessGameEntity;
import org.elogistics.domain.entities.game.Corporation;
import org.elogistics.domain.entities.game.ICorporation;
import org.elogistics.domain.entities.game.IGameData;
import org.elogistics.domain.entities.game.ITurn;
import org.elogistics.domain.entities.game.Turn;
import org.elogistics.domain.entities.transportplan.TransportPlan;

/**
 * A transfer-objekt to transmit a flat gamedata from the server. This object will
 * not be saved locally and is used to realize high performance-remote-list-operations.
 * 
 * @author Jurkschat, Oliver
 *
 */
public class GameDataDTO extends AbstractBusinessGameEntity<Integer> implements IGameData {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3296069596795781029L;

    private int orderPackageCount;

    private int terminalCount;

    private int supplyDepotCount;

    private int transportplanCount;

    protected TransportPlan actualTransportPlan;

    /**
	 * Das Unternehmen des Spielstandes.
	 */
    protected Corporation corporation;

    /**
	 * Die Spielrunde des Spielstandes.
	 */
    protected Turn turn;

    protected State state;

    public GameDataDTO() {
        this.state = State.CREATED;
    }

    public GameDataDTO(String name) {
        super(name);
        this.state = State.CREATED;
    }

    /**
	 * @param orderPackageCount
	 * @param terminalCount
	 * @param supplyDepotCount
	 * @param transportplanCount
	 * @param actualTransportPlanID
	 */
    public GameDataDTO(IGameData source) {
        super(source);
        this.orderPackageCount = source.getOrderPackageCount();
        this.terminalCount = source.getTerminalCount();
        this.supplyDepotCount = source.getSupplyDepotCount();
        this.transportplanCount = source.getTransportPlanCount();
        this.state = source.getState();
        if (source.getActualTransportPlan() != null) {
            this.actualTransportPlan = new TransportPlan(source.getActualTransportPlan());
        }
        this.corporation = new Corporation(source.getCorporation());
        this.turn = new Turn(source.getTurn());
    }

    public int getOrderPackageCount() {
        return this.orderPackageCount;
    }

    public State getState() {
        return this.state;
    }

    public int getSupplyDepotCount() {
        return this.supplyDepotCount;
    }

    public int getTerminalCount() {
        return this.terminalCount;
    }

    public int getTransportPlanCount() {
        return this.transportplanCount;
    }

    public ITurn getTurn() {
        return this.turn;
    }

    public TransportPlan getActualTransportPlan() {
        return this.actualTransportPlan;
    }

    public boolean isModifiable() {
        return this.getState().equals(State.CREATED);
    }

    public ICorporation getCorporation() {
        return this.corporation;
    }
}
