package org.gzigzag.ztp.client;

import org.gzigzag.ztp.*;
import org.gzigzag.*;
import java.io.*;
import java.util.*;

public class Put implements Action {

    public String name() {
        return "PUT";
    }

    public boolean needSession() {
        return true;
    }

    public void action(RemotePieceData rpd, Session ses) {
        if (rpd.subspace == null) throw new ZZError("there's nothing to put!");
        Subspace ss = new Subspace(rpd.subspace.s("d.ztp-subspace"));
        try {
            ses.put(rpd.subspaceName, ss.getCells(), ss.getSoftDims());
        } catch (IOException e) {
            throw new ZZError("" + e);
        }
    }
}
