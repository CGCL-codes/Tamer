package org.nordugrid.job.gridftp;

import org.nordugrid.job.gridftp.ARCGridFTPJob;
import java.util.Vector;

public class Test {

    public static void main(String[] args) {
        try {
            ARCGridFTPJob job = new ARCGridFTPJob("gsiftp://grid.fi.uib.no:2811/jobs");
            Vector files = new Vector();
            Vector names = new Vector();
            job.Submit("&(executable=/bin/echo)(action=request)(arguments=\"/bin/echo\" \"Test\")(join=yes)(stdout=out.txt)(outputfiles=(\"test\" \"\"))", files, names);
            System.err.println("ID: " + job.Id());
            System.err.println("State: " + job.State());
            System.err.println("Get");
            job.Get("/tmp/" + job.Id());
            System.err.println("Cancel");
            job.Cancel();
            System.err.println("Clean");
            job.Clean();
            System.err.println("State: " + job.State());
            job.Disconnect();
        } catch (java.lang.Exception err) {
            System.err.println("Error: " + err.getMessage());
        }
        System.err.println("Exiting");
    }
}
