package edu.mit.wi.omnigene.omnidas.handler;

import edu.mit.wi.omnigene.omnidas.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Class which handles a DASTypesRequest
 *@author Brian Gilman
 *@version $Revision: 1.6 $
 */
public class TypesRequestHandler extends RequestHandler {

    private DASMetaData dsmd = null;

    private DASResponse resp = null;

    private DASTypesRequest req = null;

    private String theDataSource = null;

    private Segment[] segments = null;

    private Type[] types = null;

    private URL dasSource = null;

    public TypesRequestHandler() {
        super();
    }

    public TypesRequestHandler(DASRequest req) {
        super();
        this.req = (DASTypesRequest) req;
    }

    public void setDASRequest(DASRequest req) {
        this.req = (DASTypesRequest) req;
    }

    /**
     * Gets the DASResponse from the server 
     * checks to see if user has asked for Types 
     * in a given segment or constrained by type
     *@return DASResponse
     */
    public DASResponse getDASResponse() throws DASException {
        try {
            if ((segments = req.getSegments()) == null) {
                theDataSource = req.getDASSource().toString() + "/" + req.getDSN().getID() + "/types";
            } else if ((types = req.getTypes()) == null) {
                theDataSource = req.getDASSource().toString() + "/" + req.getDSN().getID() + constructURI(segments);
            } else {
                theDataSource = req.getDASSource().toString() + "/" + req.getDSN().getID() + constructURI(segments, types);
            }
            this.dasSource = new URL(theDataSource);
            dsmd = super.fillInDASMetaData(dasSource);
            resp = new DASResponseImpl(dasSource.openStream(), dsmd);
        } catch (IOException e) {
            throw new DASException(e.toString());
        }
        return resp;
    }

    private String constructURI(Segment[] segments) {
        StringBuffer uri = new StringBuffer();
        String id = null;
        Range r = null;
        uri.append("/types?");
        for (int i = 0; i < segments.length; i++) {
            r = segments[i].getRange();
            if (i < segments.length - 1) {
                uri.append("segment=" + segments[i].getID() + ":" + r.toString() + ";");
            } else {
                uri.append("segment=" + segments[i].getID() + ":" + r.toString());
            }
        }
        System.out.println("DNAURI: " + uri.toString());
        return uri.toString();
    }

    private String constructURI(Segment[] segments, Type[] types) {
        StringBuffer uri = new StringBuffer();
        String partialURI = constructURI(segments);
        uri.append(partialURI);
        uri.append(";");
        for (int i = 0; i < types.length; i++) {
            if (i < types.length - 1) {
                uri.append("type=" + types[i].getID() + ";");
            } else {
                uri.append("type=" + types[i].getID());
            }
        }
        return uri.toString();
    }

    /**
     * Returns the URL of the DAS server
     *@return the URL of the DAS server
     */
    public URL getDASSource() {
        return this.dasSource;
    }
}
