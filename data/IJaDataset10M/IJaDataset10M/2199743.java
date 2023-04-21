package jmax.fts;

import java.io.*;
import java.util.*;

/**
 * The proxy of an Fts connection.
 * Deals with the connection creation/deletion and saving/loading.
 */
public class FtsConnection {

    Fts fts;

    private int id;

    boolean deleted = false;

    FtsObject from;

    int outlet;

    FtsObject to;

    int inlet;

    /** Create a FTS connection instance.
   * The FTS side of the connection is created in the Fts class.
   * @see jmax.fts.Fts#makeFtsConnection
   */
    FtsConnection(Fts fts, FtsPatcherDataObject data, int id, FtsObject from, int outlet, FtsObject to, int inlet) {
        super();
        this.fts = fts;
        this.id = id;
        this.from = from;
        this.outlet = outlet;
        this.to = to;
        this.inlet = inlet;
        if (data != null) data.addConnection(this);
    }

    /** Locally redefine a connection */
    void redefine(FtsObject from, int outlet, FtsObject to, int inlet) {
        this.from = from;
        this.outlet = outlet;
        this.to = to;
        this.inlet = inlet;
    }

    /**
   * Get the fts connection id. <p>
   */
    final int getConnectionId() {
        return id;
    }

    /** Set the objid. Private, used only by the server. */
    final void setConnectionId(int id) {
        this.id = id;
    }

    /** Ask FTS to Undo the connection. */
    public void delete() {
        if (deleted) return;
        deleted = true;
        fts.getSelection().removeConnection(this);
        from.setDirty();
        fts.getServer().deleteConnection(this);
    }

    /** Undo the connection, only the client part.
   *  indirectly called by FTS.
   */
    void release() {
        deleted = true;
        from.setDirty();
        if (from.getPatcherDataObject() != null) from.getPatcherDataObject().removeConnection(this);
        from = null;
        to = null;
    }

    /** Access the From. The From is the FtsObject origin of the connection. */
    public FtsObject getFrom() {
        return from;
    }

    /** Access the To. The To is the FtsObject destination of the connection. */
    public FtsObject getTo() {
        return to;
    }

    /** Access the From outlet. */
    public int getFromOutlet() {
        return outlet;
    }

    /** Access the To inlet. */
    public int getToInlet() {
        return inlet;
    }

    /** Get a string debug representation for the connection */
    public String toString() {
        return "FtsConnection(" + from + "," + outlet + "," + to + "," + inlet + ", #" + id + ")";
    }
}
