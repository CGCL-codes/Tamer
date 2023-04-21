package edu.ucsd.ncmir.imod_model;

import edu.ucsd.ncmir.spl.io.Accessor;
import java.io.IOException;

/**
 *
 * @author spl
 * Reference: http://bio3d.colorado.edu/imod/doc/binspec.html
 */
public class CONT implements Type {

    private int _psize;

    private int _flags;

    private int _time;

    private int _surf;

    private float[][] _pt;

    public CONT(Accessor accessor) throws IOException {
        this._psize = accessor.getNextAsInteger();
        this._flags = accessor.getNextAsInteger();
        this._time = accessor.getNextAsInteger();
        this._surf = accessor.getNextAsInteger();
        this._pt = new float[this._psize][3];
        for (int i = 0; i < this._psize; i++) for (int j = 0; j < 3; j++) this._pt[i][j] = accessor.getNextAsFloat();
    }

    /**
     * @return the _psize
     */
    public int getPsize() {
        return this._psize;
    }

    /**
     * @return the _flags
     */
    public int getFlags() {
        return this._flags;
    }

    /**
     * @return the _time
     */
    public int getTime() {
        return this._time;
    }

    /**
     * @return the _surf
     */
    public int getSurf() {
        return this._surf;
    }

    /**
     * @return the _pt
     */
    public float[][] getPt() {
        return this._pt;
    }
}
