package org.sf.xrime.algorithms.HITS.HITSLabel;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.text.DecimalFormat;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableFactories;
import org.apache.hadoop.io.WritableFactory;

public class HubLabel implements Cloneable, Writable {

    private static DecimalFormat format = new DecimalFormat("#0.00000000");

    private double hubscore;

    private double prehubscore;

    static {
        WritableFactories.setFactory(HubLabel.class, new WritableFactory() {

            public Writable newInstance() {
                return new HubLabel();
            }
        });
    }

    public HubLabel() {
        hubscore = 1;
        prehubscore = 0;
    }

    public HubLabel(double hubscore) {
        this.hubscore = hubscore;
        this.prehubscore = 0;
    }

    public HubLabel(HubLabel hubscore) {
        this.hubscore = hubscore.getHubscore();
        this.prehubscore = hubscore.getPreHubscore();
    }

    public double getHubscore() {
        return hubscore;
    }

    public void setHubscore(double hubscore) {
        this.hubscore = hubscore;
    }

    public double getPreHubscore() {
        return prehubscore;
    }

    public void setPreHubscore(double prehubscore) {
        this.prehubscore = prehubscore;
    }

    public String toString() {
        String ret = "<";
        ret = ret + format.format(prehubscore) + "," + format.format(hubscore);
        return ret + ">";
    }

    public Object clone() {
        return new HubLabel(this);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        hubscore = in.readDouble();
        prehubscore = in.readDouble();
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeDouble(hubscore);
        out.writeDouble(prehubscore);
    }
}
