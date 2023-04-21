package polr.server.mechanics.moves;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import org.simpleframework.xml.Element;

/**
 * This class represents an entry in a pokemon's move list.
 * @author Colin
 */
public class MoveListEntry implements Serializable, Cloneable {

    private static final long serialVersionUID = 873410794589044553L;

    @Element
    private String m_name;

    @Element
    private transient PokemonMove m_move;

    public MoveListEntry() {
    }

    public Object clone() {
        try {
            MoveListEntry ret = (MoveListEntry) super.clone();
            if (ret.m_move != null) {
                ret.m_move = (PokemonMove) ret.m_move.clone();
                ret.m_move.setMoveListEntry(ret);
            }
            return ret;
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public boolean equals(Object o) {
        if (!(o instanceof MoveListEntry)) {
            return false;
        }
        return m_name.equals(((MoveListEntry) o).m_name);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        MoveListEntry entry = MoveList.getDefaultData().getMove(m_name);
        if (entry != null) {
            m_move = entry.getMove();
        } else {
            m_move = null;
        }
    }

    public MoveListEntry(String name, PokemonMove move) {
        m_name = name;
        m_move = move;
        if (m_move.getMoveListEntry() != null) {
            System.out.println(name + " is used by two MoveListEntries!");
        }
        m_move.setMoveListEntry(this);
        if (m_move.isBuggy()) {
            System.out.println(name + " is buggy.");
        }
    }

    public String getName() {
        return m_name;
    }

    public PokemonMove getMove() {
        return m_move;
    }
}
