package topology.graphParsers.common;

import javolution.util.FastList;
import Opus5.Comparable;

public class GraphAuxNode implements Comparable {

    private String m_iASvertice;

    private FastList<String> m_llLinks;

    public GraphAuxNode(String vertice) {
        m_iASvertice = vertice;
        m_llLinks = new FastList<String>();
    }

    public int compare(Comparable obj) {
        if (obj instanceof GraphAuxNode) {
            GraphAuxNode tmp = (GraphAuxNode) obj;
            return this.m_iASvertice.compareTo(tmp.getASvertex());
        } else return -2;
    }

    public void addlink(String ASname) {
        this.m_llLinks.addFirst(ASname);
    }

    public String getASvertex() {
        return m_iASvertice;
    }

    public String getlinkedAS() {
        return (String) this.m_llLinks.removeFirst();
    }

    public boolean islinkedASempty() {
        return this.m_llLinks.isEmpty();
    }

    public boolean isEQ(Comparable object) {
        return false;
    }

    public boolean isGE(Comparable object) {
        return false;
    }

    public boolean isGT(Comparable object) {
        return false;
    }

    public boolean isLE(Comparable object) {
        return false;
    }

    public boolean isLT(Comparable object) {
        return false;
    }

    public boolean isNE(Comparable object) {
        return false;
    }
}
