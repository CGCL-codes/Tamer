package br.jabuti.probe;

import java.io.*;

public class ProbedNode implements Serializable {

    /**
	 * Added to jdk1.5.0_04 compiler
	 */
    private static final long serialVersionUID = -5221701009238934021L;

    public String threadCode;

    public String objectCode;

    public String clazz;

    public int metodo;

    public String node;

    public ProbedNode(String th, String ob, String cl, int mt, String n) {
        threadCode = th;
        objectCode = ob;
        clazz = cl;
        metodo = mt;
        node = n;
    }

    public boolean isSame(ProbedNode x) {
        return threadCode.equals(x.threadCode) && objectCode.equals(x.objectCode) && clazz.equals(x.clazz) && metodo == x.metodo;
    }

    public String toString() {
        return "<Thread: " + threadCode + ", Object: " + objectCode + ", Class: " + clazz + ", Method: " + metodo + ", Node: " + node + ">";
    }

    public boolean equals(Object x) {
        if (!(x instanceof ProbedNode)) {
            return false;
        }
        ProbedNode y = (ProbedNode) x;
        return isSame(y) && node.equals(y.node);
    }

    public int hashCode() {
        return threadCode.hashCode() + objectCode.hashCode() + (clazz.hashCode() * 10) + (metodo * 100) + node.hashCode();
    }

    public void setNode(String s) {
        node = s;
    }

    public String getNode() {
        return node;
    }
}
