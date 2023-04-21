package cz.cuni.mff.abacs.burglar.logics.objects.agents;

import cz.cuni.mff.abacs.burglar.logics.objects.BaseInterface;
import cz.cuni.mff.abacs.burglar.logics.objects.Room;
import cz.cuni.mff.abacs.burglar.logics.objects.items.Item;
import cz.cuni.mff.abacs.burglar.logics.objects.positions.Camera;
import cz.cuni.mff.abacs.burglar.logics.objects.positions.Container;
import cz.cuni.mff.abacs.burglar.logics.objects.positions.Door;
import cz.cuni.mff.abacs.burglar.logics.objects.positions.Position;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Representation of the knowledge base of an agent.
 * 
 * @author abacs
 *
 */
public class BeliefBase {

    /** Remembered positions from a distance.
	 * Held by identifier.
	 * Changeable property. */
    protected Map<Integer, Position> _positionsFar = new HashMap<Integer, Position>();

    /** Remembered positions after examination.
	 * Held by identifier.
	 * Changeable property. */
    protected Map<Integer, Position> _positionsNear = new HashMap<Integer, Position>();

    /** IDs of the containers where the known items are.
	 * Position Ids by Item Id.
	 * Currently not in use.
	 * 
	 * Changeable property. */
    protected Map<Integer, Integer> _itemLocations = new HashMap<Integer, Integer>();

    /**  */
    protected List<Integer> _roomIds = new LinkedList<Integer>();

    /**
	 * Remembered agents by their identifiers.
	 */
    protected Map<Integer, Agent> _agents = new HashMap<Integer, Agent>();

    /**
	 * Items are seen from a distance, some details are incorrect.
	 * 
	 * @return true if something new was added.
	 */
    public boolean seenFromFar(Position examined) {
        boolean seenSomethingNew = false;
        int id = examined.getId();
        if (this._positionsFar.containsKey(id)) {
            Position remembered = this._positionsFar.get(id);
            if (remembered.matchesFromFar(examined) == false) {
                this._positionsFar.remove(id);
                Position ourCopy = examined.examinedFromFar();
                this._positionsFar.put(id, ourCopy);
                if (ourCopy.isTypeOf(BaseInterface.Type.CONTAINER)) this.seenContents((Container) ourCopy);
                seenSomethingNew = true;
            }
            return seenSomethingNew;
        }
        if (this._positionsNear.containsKey(id)) {
            Position remembered = this._positionsNear.get(id);
            if (remembered.matchesFromFar(examined) == false) {
                Position oldPosition = this._positionsNear.remove(id);
                Position ourCopy = examined.examinedFromFar();
                this._positionsFar.put(id, ourCopy);
                if (ourCopy.isTypeOf(BaseInterface.Type.CONTAINER)) {
                    this.seenContents((Container) ourCopy);
                    ((Container) ourCopy).addItems(((Container) oldPosition).getItems());
                }
                seenSomethingNew = true;
            }
            return seenSomethingNew;
        }
        Position ourCopy = examined.examinedFromFar();
        this._positionsFar.put(id, ourCopy);
        if (ourCopy.isTypeOf(BaseInterface.Type.CONTAINER)) this.seenContents((Container) ourCopy);
        seenSomethingNew = true;
        this.addContainingRoom(examined);
        return seenSomethingNew;
    }

    /**
	 * Items are seen from a distance, some details are incorrect.
	 * 
	 * @return true if something new was added.
	 */
    public boolean seenFromFar(List<Position> positions) {
        boolean seenSomethingNew = false;
        for (Position examined : positions) {
            seenSomethingNew = this.seenFromFar(examined) || seenSomethingNew;
        }
        return seenSomethingNew;
    }

    /**
	 * Item is seen from a small distance, all details are correct.
	 * 
	 * @return true if something new was added.
	 */
    public boolean seenFromNear(List<Position> positions) {
        boolean seenSomethingNew = false;
        for (Position pos : positions) {
            seenSomethingNew = this.seenFromNear(pos) || seenSomethingNew;
        }
        return seenSomethingNew;
    }

    /**
	 * Item is seen from a small distance, all details are correct.
	 * 
	 * @return true if something new was added.
	 */
    public boolean seenFromNear(Position examined) {
        boolean seenSomethingNew = false;
        int id = examined.getId();
        if (this._positionsFar.containsKey(id)) {
            Position remembered = this._positionsFar.remove(id);
            if (remembered.matchesFromClose(examined)) {
                this._positionsNear.put(id, remembered);
            } else {
                Position ourCopy = examined.examinedFromNear();
                this._positionsNear.put(id, ourCopy);
                if (ourCopy.isTypeOf(BaseInterface.Type.CONTAINER)) this.seenContents((Container) ourCopy);
                seenSomethingNew = true;
            }
            return seenSomethingNew;
        }
        if (this._positionsNear.containsKey(id)) {
            Position remembered = this._positionsNear.get(id);
            if (remembered.matchesFromClose(examined) == false) {
                this._positionsNear.remove(id);
                Position ourCopy = examined.examinedFromNear();
                this._positionsNear.put(id, ourCopy);
                if (ourCopy.isTypeOf(BaseInterface.Type.CONTAINER)) this.seenContents((Container) ourCopy);
                seenSomethingNew = true;
            }
            return seenSomethingNew;
        }
        Position ourCopy = examined.examinedFromNear();
        this._positionsNear.put(id, ourCopy);
        if (ourCopy.isTypeOf(BaseInterface.Type.CONTAINER)) this.seenContents((Container) ourCopy);
        seenSomethingNew = true;
        this.addContainingRoom(examined);
        return seenSomethingNew;
    }

    /**
	 * An agent observed from far.
	 * 
	 * @param observedAgent
	 * @return true if seen something new 
	 */
    public boolean seenFromFar(Agent observedAgent) {
        int agentId = observedAgent.getId();
        if (this.isKnownAgent(agentId)) {
            Agent remembered = this._agents.get(agentId);
            if (remembered.matchesFromFar(observedAgent)) return false;
        }
        this._agents.put(agentId, observedAgent.examinedFromFar());
        this.addRoom(observedAgent.getRoomId());
        return true;
    }

    /**
	 * Examines the contents of a container.
	 * 
	 * @param container
	 * @return 
	 */
    private boolean seenContents(Container container) {
        boolean seenSomethingNew = false;
        for (Item item : container.getItems()) {
            if (this._itemLocations.containsKey(item.getId())) {
                if (this._itemLocations.get(item.getId()) != container.getId()) {
                    this._itemLocations.remove(item.getId());
                    this._itemLocations.put(item.getId(), container.getId());
                    seenSomethingNew = true;
                }
            } else {
                this._itemLocations.put(item.getId(), container.getId());
                seenSomethingNew = true;
            }
        }
        return seenSomethingNew;
    }

    /**
	 * 
	 * 
	 * @param roomId 
	 */
    private void addRoom(int roomId) {
        if (this._roomIds.contains(roomId)) return;
        this._roomIds.add(roomId);
    }

    /**
	 * 
	 * @param position 
	 */
    private void addContainingRoom(Position position) {
        if (position.isTypeOf(BaseInterface.Type.DOOR)) {
            int[] roomIds = ((Door) position).getRoomIds();
            this.addRoom(roomIds[0]);
            this.addRoom(roomIds[1]);
        } else {
            this.addRoom(position.getRoomId());
        }
    }

    /**
	 * Saves the location of an item.
	 */
    public void addItemPosition(Item item, Position position) {
        this._itemLocations.put(item.getId(), position.getId());
    }

    /**
	 * Removes the location of a given item.
	 */
    public void forgetItemPosition(int itemId) {
        this._itemLocations.remove(itemId);
    }

    /**
	 * Removes the location of a given item.
	 */
    public void forgetItemPosition(Item item) {
        this.forgetItemPosition(item.getId());
    }

    /** 
	 * Returns all the positions the agent knows about in their believed state.
	 * 
	 * It includes all items, seen near or far.
	 */
    public List<Position> getKnownPositions() {
        List<Position> ret = new LinkedList<Position>();
        ret.addAll(this._positionsFar.values());
        ret.addAll(this._positionsNear.values());
        return ret;
    }

    public Position getKnownPosition(int id) {
        if (this._positionsFar.containsKey(id)) return this._positionsFar.get(id);
        return this._positionsNear.get(id);
    }

    /**
	 * Returns known stunned agents.
	 */
    public List<Agent> getPassiveAgents() {
        List<Agent> ret = new LinkedList<Agent>();
        for (Agent agent : this._agents.values()) {
            if (agent.isInState(Agent.State.STUNNED)) {
                ret.add(agent);
            }
        }
        return ret;
    }

    /** 
	 * Returns the identifier of the item holder.
	 * 
	 * Returns null if nothing found.
	 */
    public Integer getItemPositionId(int itemId) {
        if (this._itemLocations.containsKey(itemId)) {
            return this._itemLocations.get(itemId);
        }
        return null;
    }

    /** 
	 * Returns the identifier of the item holder.
	 * 
	 * Returns null if nothing found.
	 */
    public Integer getItemPositionId(Item item) {
        return this.getItemPositionId(item.getId());
    }

    /**
	 * Decides whether the agent knows the position.
	 */
    public boolean isKnownPosition(int positionId) {
        return this._positionsFar.containsKey(positionId) || this._positionsNear.containsKey(positionId);
    }

    /**
	 * Decides whether the agent knows the position.
	 */
    public boolean isKnownPosition(Position position) {
        if (position == null) return false; else return this.isKnownPosition(position.getId());
    }

    /**
	 * Decides whether the agent knows the other agent.
	 */
    public boolean isKnownAgent(int agentId) {
        return this._agents.containsKey(agentId);
    }

    /**
	 * Decides whether the agent knows the other agent.
	 */
    public boolean isKnownAgent(Agent agent) {
        if (agent == null) return false; else return this.isKnownAgent(agent.getId());
    }

    /**
	 * Decides whether the agent knows the item.
	 */
    public boolean isKnownItemLocation(int itemId) {
        return this._itemLocations.containsKey(itemId);
    }

    /**
	 * Decides whether the agent knows the item.
	 */
    public boolean isKnownItemLocation(Item item) {
        return this.isKnownItemLocation(item.getId());
    }

    /**
	 * 
	 * @param roomId
	 * @return 
	 */
    public boolean isKnownRoom(int roomId) {
        return this._roomIds.contains(roomId);
    }

    /**
	 * 
	 * @param room
	 * @return 
	 */
    public boolean isKnownRoom(Room room) {
        return this._roomIds.contains(room.getId());
    }

    /**
	 * 
	 * @param roomId
	 * @return 
	 */
    public boolean isObservedRoom(int roomId) {
        for (Position position : this._positionsFar.values()) {
            if (position.isTypeOf(BaseInterface.Type.CAMERA) && ((Camera) position).isActive() && position.getRoomId() == roomId) return true;
        }
        for (Position position : this._positionsNear.values()) {
            if (position.isTypeOf(BaseInterface.Type.CAMERA) && ((Camera) position).isActive() && position.getRoomId() == roomId) return true;
        }
        for (Agent agent : this._agents.values()) {
            if (agent.isTypeOf(BaseInterface.Type.GUARD) && ((Guard) agent).isInState(Agent.State.WELL) && agent.getRoomId() == roomId) return true;
        }
        return false;
    }

    /**
	 * 
	 * @param room
	 * @return 
	 */
    public boolean isObservedRoom(Room room) {
        return this.isObservedRoom(room.getId());
    }

    /**
	 * Returns whether the position matches with something in the belief base.
	 */
    public boolean matches(Position examined) {
        Position own = this._positionsFar.get(examined.getId());
        if (own != null) return own.matchesFromClose(examined);
        own = this._positionsNear.get(examined.getId());
        if (own != null) return own.matchesFromClose(examined);
        return false;
    }

    /**
	 * Returns whether the agent matches with something in the belief base.
	 */
    public boolean matches(Agent examined) {
        Agent own = this._agents.get(examined.getId());
        if (own != null) return own.matchesFromFar(examined); else return false;
    }

    /**
	 * Copies the beliefs of another belief base.
	 * 
	 * Does not change the map!
	 * 
	 * @param original
	 */
    public void copyChangeables(BeliefBase original) {
        for (Position pos : original._positionsFar.values()) this._positionsFar.put(pos.getId(), pos);
        for (Position pos : original._positionsNear.values()) this._positionsNear.put(pos.getId(), pos);
        for (Entry<Integer, Integer> pair : original._itemLocations.entrySet()) this._itemLocations.put(pair.getKey(), pair.getValue());
        for (Integer roomId : original._roomIds) this._roomIds.add(roomId);
    }
}
