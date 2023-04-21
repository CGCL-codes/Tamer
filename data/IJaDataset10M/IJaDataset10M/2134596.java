package vidis.modules.byzantineGenerals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import vidis.data.AUserNode;
import vidis.data.annotation.ColorType;
import vidis.data.annotation.Display;
import vidis.data.annotation.DisplayColor;
import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserPacket;
import vidis.modules.byzantineGenerals.APacket.PacketType;

public abstract class ANode extends AUserNode {

    protected enum NodeType {

        GOOD(ColorType.GREEN), BAD(ColorType.RED), DONTKNOW(ColorType.ORANGE);

        private ColorType color;

        private NodeType(ColorType c) {
            this.color = c;
        }

        public ColorType getColor() {
            return color;
        }
    }

    ;

    @DisplayColor
    public ColorType getColor() {
        return getNodeType().getColor();
    }

    protected abstract NodeType getNodeType();

    @Override
    public void init() {
    }

    @Display(name = "General: ATTACK!")
    public void sendAttack() {
        sendAttackPacket(null);
    }

    @Display(name = "General: RETREAT!")
    public void sendRetreat() {
        sendRetreatPacket(null);
    }

    protected void mySend(APacket p, IUserLink l) {
        if (!alreadySeen(p.getId())) alreadySeen.put(p.getId(), new HashSet<APacket>());
        send(p, l);
    }

    protected void sendAttackPacket(APacket sourcePacket) {
        int id = (int) (Math.random() * (double) Integer.MAX_VALUE);
        for (IUserLink l : getConnectedLinks()) {
            if (sourcePacket != null) {
                mySend(new AttackPacket(sourcePacket.getId()), l);
            } else {
                mySend(new AttackPacket(id), l);
            }
        }
    }

    protected void sendRetreatPacket(APacket sourcePacket) {
        int id = (int) (Math.random() * (double) Integer.MAX_VALUE);
        for (IUserLink l : getConnectedLinks()) {
            if (sourcePacket != null) {
                mySend(new RetreatPacket(sourcePacket.getId()), l);
            } else {
                mySend(new RetreatPacket(id), l);
            }
        }
    }

    private Map<Integer, Set<APacket>> alreadySeen = new HashMap<Integer, Set<APacket>>();

    private boolean alreadySeen(int id) {
        return alreadySeen.containsKey(id);
    }

    /**
	 * retrieve attacks (where enough votes exist and we decide for attack)
	 * @return a list of the attack ids
	 */
    @Display(name = "Attacks")
    public List<Integer> getAttackIds() {
        List<Integer> ids = new LinkedList<Integer>();
        for (Entry<Integer, Set<APacket>> e : alreadySeen.entrySet()) {
            int attacks = 0;
            int retreats = 0;
            if (e.getValue().size() == getConnectedLinks().size()) {
                for (APacket p : e.getValue()) {
                    switch(p.getPacketType()) {
                        case ATTACK:
                            attacks++;
                            break;
                        case RETREAT:
                            retreats++;
                            break;
                    }
                }
                if (attacks > retreats) {
                    ids.add(e.getKey());
                } else if (attacks < retreats) {
                } else {
                }
            }
        }
        return ids;
    }

    /**
	 * retrieve retreats (where enough votes exist and we decide for retreat)
	 * @return a list of the attack ids
	 */
    @Display(name = "Retreats")
    public List<Integer> getRetreatIds() {
        List<Integer> ids = new LinkedList<Integer>();
        for (Entry<Integer, Set<APacket>> e : alreadySeen.entrySet()) {
            int attacks = 0;
            int retreats = 0;
            if (e.getValue().size() == getConnectedLinks().size()) {
                for (APacket p : e.getValue()) {
                    switch(p.getPacketType()) {
                        case ATTACK:
                            attacks++;
                            break;
                        case RETREAT:
                            retreats++;
                            break;
                    }
                }
                if (attacks > retreats) {
                } else if (attacks < retreats) {
                    ids.add(e.getKey());
                } else {
                }
            }
        }
        return ids;
    }

    /**
	 * retrieve clashing ids (where enough votes exist and we CANNOT decide for attack OR retreat)
	 * @return a list of the clashing ids
	 */
    @Display(name = "Clashing")
    public List<Integer> getClashingIds() {
        List<Integer> ids = new LinkedList<Integer>();
        for (Entry<Integer, Set<APacket>> e : alreadySeen.entrySet()) {
            int attacks = 0;
            int retreats = 0;
            if (e.getValue().size() == getConnectedLinks().size()) {
                for (APacket p : e.getValue()) {
                    switch(p.getPacketType()) {
                        case ATTACK:
                            attacks++;
                            break;
                        case RETREAT:
                            retreats++;
                            break;
                    }
                }
                if (attacks > retreats) {
                } else if (attacks < retreats) {
                } else {
                    ids.add(e.getKey());
                }
            }
        }
        return ids;
    }

    /**
	 * retrieve yet unknown ids (where we have not enough votes to decide something)
	 * @return a list of the unknown ids
	 */
    @Display(name = "Unknowns")
    public List<Integer> getYetUnknownIds() {
        List<Integer> ids = new LinkedList<Integer>();
        for (Entry<Integer, Set<APacket>> e : alreadySeen.entrySet()) {
            if (e.getValue().size() == getConnectedLinks().size()) {
            } else {
                ids.add(e.getKey());
            }
        }
        return ids;
    }

    protected void receive(APacket p) {
        if (!alreadySeen(p.getId())) {
            alreadySeen.put(p.getId(), new HashSet<APacket>());
            switch(getNodeType()) {
                case GOOD:
                    if (p.getPacketType().equals(PacketType.ATTACK)) {
                        sendAttackPacket(p);
                    } else if (p.getPacketType().equals(PacketType.RETREAT)) {
                        sendRetreatPacket(p);
                    }
                    break;
                case BAD:
                    if (p.getPacketType().equals(PacketType.ATTACK)) {
                        sendRetreatPacket(p);
                    } else if (p.getPacketType().equals(PacketType.RETREAT)) {
                        sendAttackPacket(p);
                    }
                    break;
                case DONTKNOW:
                    if (Math.random() < 0.5) {
                        sendRetreatPacket(p);
                    } else {
                        sendAttackPacket(p);
                    }
                    break;
            }
        }
        {
            alreadySeen.get(p.getId()).add(p);
        }
    }

    public void receive(IUserPacket packet) {
        if (packet instanceof APacket) {
            receive((APacket) packet);
        }
    }

    public void execute() {
    }
}
