package net.java.aij.pathfinder.astar;

import net.java.aij.Position;
import net.java.aij.pathfinder.Path;
import java.util.Enumeration;
import java.util.Vector;

public class AbstractPath implements Path {

    private AbstractPath g_next = null;

    private Position g_position = null;

    private class PathEnumeration implements Enumeration {

        private AbstractPath m_iter = AbstractPath.this;

        private boolean m_first = true;

        public boolean hasMoreElements() {
            if (m_first) return true;
            if (m_iter.g_next == null) return false;
            return true;
        }

        public Object nextElement() {
            if (m_first) {
                m_first = false;
                return m_iter.g_position;
            }
            m_iter = m_iter.g_next;
            return m_iter.g_position;
        }
    }

    public AbstractPath(AbstractPath next, Position position) {
        g_next = next;
        g_position = position;
    }

    public Path getNextPath() {
        return g_next;
    }

    public Position getPosition() {
        return g_position;
    }

    public Enumeration reverse() {
        return new PathEnumeration();
    }

    public Enumeration elements() {
        Vector reverse = new Vector();
        Enumeration pe = reverse();
        while (pe.hasMoreElements()) {
            reverse.add(0, pe.nextElement());
        }
        return reverse.elements();
    }
}
