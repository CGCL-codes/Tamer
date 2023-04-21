package de.ibis.permoto.gui.preddesigner.model;

import java.util.Iterator;
import java.util.Vector;
import de.ibis.permoto.model.basic.scenario.ClassPart;
import de.ibis.permoto.model.basic.scenario.OutgoingConnection;
import de.ibis.permoto.model.definitions.IClassSection;

/**
 * @author Slavko Segota
 *
 */
public class StructuredService {

    private IClassSection cs;

    private ClassPart cp;

    private Vector<StructuredService> services;

    public StructuredService(ClassPart cp, IClassSection cs) {
        this.cp = cp;
        this.cs = cs;
        this.services = new Vector<StructuredService>();
        this.extractInformation();
    }

    public Vector<StructuredService> getServices() {
        return this.services;
    }

    public ClassPart getClassPart() {
        return this.cp;
    }

    private void extractInformation() {
        Iterator<OutgoingConnection> cons = this.cp.getOutgoingConnections().getOutgoingConnection().listIterator();
        while (cons.hasNext()) {
            OutgoingConnection con = cons.next();
            if (con.isSubservice()) {
                this.services.add(new StructuredService(cs.getClassPartByID(con.getTarget()), this.cs));
            }
        }
    }
}
