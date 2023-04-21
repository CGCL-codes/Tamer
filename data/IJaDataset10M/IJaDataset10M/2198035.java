package org.tripcom.distribution.oprocessorTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import net.jini.space.JavaSpace;
import net.jini.core.lease.Lease;
import org.apache.log4j.Logger;
import org.tripcom.distribution.util.SpaceLookUP;
import org.tripcom.integration.entry.RdDMEntry;
import org.tripcom.integration.entry.Template;

/**
 * This Class holds methods to write RdDMEntry Entries into the Blitz-Space It
 * is used to populate the Bus with Tuples to take and process for DM until
 * other components (Security Manager) provide them
 * 
 * @author Kia Teymourian
 */
public class DummySMReadWithSpaceURL {

    private static Logger log = Logger.getLogger(DummySMReadWithSpaceURL.class);

    /**
	 * 
	 * This methods populates the System Bus with Entries for Distribution
	 * Manager to take out It acts as a dummy for Security Manager
	 */
    public void exec(JavaSpace myBus, String query, String space) {
        try {
            java.net.URI uri = new java.net.URI(space);
            RdDMEntry rdDMEntry = new RdDMEntry();
            Template tpl = new Template();
            tpl.setQuery(query);
            rdDMEntry.template = tpl;
            rdDMEntry.space = uri;
            myBus.write(rdDMEntry, null, Lease.FOREVER);
            log.info("DummySM.write(): Wrote: " + rdDMEntry.toString() + "  " + rdDMEntry.template.getQuery());
        } catch (Exception e) {
            log.error(e.toString(), e);
        }
    }

    /**
	 * This method is to invoke the dummy security manager, which populates the
	 * system bus with entries dedicated for distribution manager
	 * 
	 * @param
	 */
    public static void main(String[] args) {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Found System BUS: " + br);
        String query = null;
        String space = null;
        try {
            DummySMReadWithSpaceURL myDummyWriter = new DummySMReadWithSpaceURL();
            JavaSpace myBus = SpaceLookUP.lookupJavaSpace();
            while (true) {
                System.out.print("Enter your query:");
                query = br.readLine();
                System.out.print("Enter the target Space URL:");
                space = br.readLine();
                myDummyWriter.exec(myBus, query, space);
                System.out.println();
            }
        } catch (Exception e) {
            log.error(e.toString(), e);
        }
        log.info("DummyWriter.main(): populating the space");
    }
}
