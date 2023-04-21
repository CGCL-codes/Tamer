package com.globalretailtech.pos.events;

import java.util.Hashtable;
import java.util.Vector;
import jpos.JposException;
import com.globalretailtech.util.*;
import com.globalretailtech.data.*;
import com.globalretailtech.pos.context.*;
import com.globalretailtech.pos.hardware.FiscalPrinter;

/**
 * Terminal report gets the total records from the database and
 * updates displays.
 *
 * @author  Quentin Olson
 */
public class TerminalReport extends PosEvent {

    public static int X_REPORT = 1;

    public static int Z_REPORT = 2;

    /** Simple constructor */
    public TerminalReport() {
        initTransition();
    }

    /** Constructor sets the context*/
    public TerminalReport(PosContext context) {
        setContext(context);
        initTransition();
    }

    /**
     * Switch on the report type and update displays.
     */
    public void engage(int value) {
        context().receipt().update(this);
        if (!context().trainingMode()) {
            try {
                context().cashDrawer().openDrawer();
                FiscalPrinter fiscalPrinter = context().fiscalPrinter();
                if (value == Z_REPORT) {
                    fiscalPrinter.zReport();
                    String fetchSpec = PosTotal.getBySiteAndPos(context().siteID(), context().posNo());
                    Vector results = Application.dbConnection().fetch(new PosTotal(), fetchSpec);
                    if (results.size() > 0) {
                        PosTotal posTotal = (PosTotal) results.elementAt(0);
                        posTotal.delete();
                    }
                } else if (value == X_REPORT) {
                    fiscalPrinter.xReport();
                }
                context().eventStack().pushEvent(new Pause(context()));
                context().operPrompt().update((Pause) context().eventStack().event());
            } catch (PosException e) {
                PosError posError = new PosError(context(), e);
                context().eventStack().pushEvent(posError);
                context().operPrompt().update(posError);
            } catch (JposException e) {
                PosError posError = new PosError(context(), e);
                context().eventStack().pushEvent(posError);
                context().operPrompt().update(posError);
            }
        }
    }

    /** Validate transistions state */
    private Hashtable transistions;

    private void initTransition() {
        transistions = new Hashtable();
        transistions.put(RegisterOpen.eventName(), new Boolean(true));
        transistions.put(PosError.eventName(), new Boolean(true));
    }

    public boolean validTransition(String event) {
        if (transistions.get(event) != null) return true; else return false;
    }

    /** Clear key implementation for this class */
    public void clear() {
    }

    public boolean checkProfile(int event) {
        return true;
    }

    private static String eventname = "TerminalReport";

    /** Return staic name. */
    public String toString() {
        return eventname;
    }

    /** Return staic name. */
    public static String eventName() {
        return eventname;
    }
}
