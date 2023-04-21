package br.jabuti.mobility.mobile;

import br.jabuti.probe.*;

public class HostProbedNode extends ProbedNode {

    /**
	 * Added to jdk1.5.0_04 compiler
	 */
    private static final long serialVersionUID = 1785140362905450583L;

    public String host;

    public HostProbedNode(String h, String th, String ob, String cl, int mt, String n) {
        super(th, ob, cl, mt, n);
        host = h;
    }

    public HostProbedNode(String h, ProbedNode pb) {
        super(pb.threadCode, pb.objectCode, pb.clazz, pb.metodo, pb.node);
        host = h;
    }

    public String getHost() {
        return host;
    }

    public boolean isSame(HostProbedNode x) {
        return host.equals(x.host) && super.equals(x);
    }

    public String toString() {
        return "<Host: " + host + ", Thread: " + threadCode + ", Object: " + objectCode + ", Class: " + clazz + ", Method: " + metodo + ", Node: " + node + ">";
    }

    public boolean equals(Object x) {
        if (!(x instanceof HostProbedNode)) {
            return false;
        }
        return super.equals(x);
    }

    public int hashCode() {
        return host.hashCode() + super.hashCode();
    }
}
