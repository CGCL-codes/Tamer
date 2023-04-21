package cn.edu.buaa.scse.liyi.compile.types;

import java.util.LinkedList;
import java.util.TreeMap;
import java.util.Vector;

/**
 * 
 * @author liyi
 */
public class Function {

    public static final int R_VOID = 0;

    public static final int R_INT = 1;

    private int retype;

    private String name;

    private Vector<Constant> constant = null;

    private Vector<String> parameter = null;

    private Vector<String> variable = null;

    private LinkedList<Quaternion> quaterList = null;

    private TreeMap<String, Integer> pmap = null;

    private TreeMap<String, Integer> vmap = null;

    /**
	 * �����๹�췽��
	 * @param retype
	 * @param name
	 */
    public Function(int retype, String name) {
        this.name = name;
        this.retype = retype;
        this.constant = new Vector<Constant>();
        this.parameter = new Vector<String>();
        this.variable = new Vector<String>();
        this.quaterList = new LinkedList<Quaternion>();
        this.pmap = new TreeMap<String, Integer>();
        this.vmap = new TreeMap<String, Integer>();
    }

    public void setRetype(int retype) {
        this.retype = retype;
    }

    public int getRetype() {
        return retype;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setConst(Vector<Constant> constant) {
        this.constant = constant;
    }

    public Vector<Constant> getConst() {
        return constant;
    }

    public void setPara(Vector<String> parameter) {
        this.parameter = parameter;
    }

    public Vector<String> getPara() {
        return parameter;
    }

    public void setVar(Vector<String> variable) {
        this.variable = variable;
    }

    public Vector<String> getVar() {
        return variable;
    }

    public void setQuaterList(LinkedList<Quaternion> quaterList) {
        this.quaterList = quaterList;
    }

    public LinkedList<Quaternion> getQuaterList() {
        return quaterList;
    }

    public void setPmap(TreeMap<String, Integer> pmap) {
        this.pmap = pmap;
    }

    public TreeMap<String, Integer> getPmap() {
        return pmap;
    }

    public void setVmap(TreeMap<String, Integer> vmap) {
        this.vmap = vmap;
    }

    public TreeMap<String, Integer> getVmap() {
        return vmap;
    }
}
