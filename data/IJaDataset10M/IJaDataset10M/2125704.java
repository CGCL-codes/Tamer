package net.sf.jcgm.core;

import java.io.*;

/**
 * Class=7, Element=2
 * @author Luke Quinane
 * @since Nov 03, 2011
 */
public class ApplicationData extends Command {

    int identifier;

    String data;

    public ApplicationData(int ec, int eid, int l, DataInput in) throws IOException {
        super(ec, eid, l, in);
        identifier = makeInt();
        data = makeString();
    }

    @Override
    public String toString() {
        return "ApplicationData: " + identifier + " " + this.data;
    }

    public int getIdentifier() {
        return identifier;
    }

    public String getData() {
        return data;
    }
}
