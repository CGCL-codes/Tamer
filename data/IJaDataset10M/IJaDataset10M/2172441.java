package org.chess.quasimodo.engine.model;

public class Info {

    public String depth;

    public String pv;

    public String hashfull;

    public String nps;

    public String time;

    public String nodes;

    public String tbhits;

    public String currmove;

    public String currmovenumber;

    public String currline;

    public String string;

    public String multipv;

    public String refutation;

    public String cp;

    public String mate;

    @Override
    public String toString() {
        return "Info [cp=" + cp + ", currline=" + currline + ", currmove=" + currmove + ", currmovenumber=" + currmovenumber + ", depth=" + depth + ", hashfull=" + hashfull + ", mate=" + mate + ", multipv=" + multipv + ", nodes=" + nodes + ", nps=" + nps + ", pv=" + pv + ", refutation=" + refutation + ", string=" + string + ", tbhits=" + tbhits + ", time=" + time + "]";
    }
}
