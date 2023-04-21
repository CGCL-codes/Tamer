package cn.edu.thss.iise.beehivez.server.metric.mypetrinet;

import java.util.Vector;

/**
 * Petri�������
 * 
 * MyPetriNet Place class
 * 
 * @author zhp, wwx
 *
 */
public class MyPetriPlace extends MyPetriObject implements Cloneable {

    /**
	 * input and output transitions
	 */
    private Vector<String> inputtransition;

    private Vector<String> outputtransition;

    private int initialtokens = 0;

    private int currenttokens = 0;

    public MyPetriPlace(String id, String name) {
        this.setid(id);
        this.setname(name);
        this.initialtokens = 0;
        this.settype(MyPetriObject.PLACE);
    }

    public MyPetriPlace(String id, String name, String tokens) {
        this.setid(id);
        this.setname(name);
        this.settype(MyPetriObject.PLACE);
        int i = 0;
        if (tokens != null) {
            while (i < tokens.length() && tokens.charAt(i) >= '0' && tokens.charAt(i) <= '9') {
                this.initialtokens += this.initialtokens * 10 + (tokens.charAt(i) - '0');
                i++;
            }
            currenttokens = initialtokens;
            this.settype(MyPetriObject.PLACE);
        } else currenttokens = 0;
    }

    public boolean isempty() {
        if (currenttokens > 0) return false; else return true;
    }

    public void empty() {
        currenttokens = 0;
    }

    public void marking(int n) {
        currenttokens = n;
    }

    public int getmarking() {
        return currenttokens;
    }

    public void addtoken(int n) {
        currenttokens += n;
    }

    @Override
    public String toString() {
        return name;
    }

    public Object clone() {
        MyPetriPlace obj = null;
        obj = (MyPetriPlace) super.clone();
        return obj;
    }
}
