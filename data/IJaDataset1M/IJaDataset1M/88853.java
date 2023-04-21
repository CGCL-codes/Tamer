package aurora.hwc;

import java.io.*;
import java.util.*;
import org.w3c.dom.*;
import aurora.*;
import aurora.util.Util;

/**
 * Event that changes weaving factor matrix at given simple Node.
 * @author Alex Kurzhanskiy
 * @version $Id: EventWFM.java 56 2010-04-04 05:02:29Z akurzhan $
 */
public final class EventWFM extends AbstractEvent {

    private static final long serialVersionUID = -8770931794877717089L;

    protected double[][] weavingFactorMatrix = null;

    public EventWFM() {
        description = "Weaving factor change at Node";
    }

    public EventWFM(int neid) {
        this();
        this.neid = neid;
    }

    public EventWFM(int neid, double[][] wfm) {
        this(neid);
        if (wfm != null) {
            int m = wfm.length;
            int n = wfm[0].length;
            weavingFactorMatrix = new double[m][n];
            for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) weavingFactorMatrix[i][j] = wfm[i][j];
        }
    }

    public EventWFM(int neid, double[][] wfm, double tstamp) {
        this(neid, wfm);
        if (tstamp >= 0.0) this.tstamp = tstamp;
    }

    /**
	 * Initializes the event from given DOM structure.
	 * @param p DOM node.
	 * @return <code>true</code> if operation succeeded, <code>false</code> - otherwise.
	 * @throws ExceptionConfiguration
	 */
    public boolean initFromDOM(Node p) throws ExceptionConfiguration {
        boolean res = super.initFromDOM(p);
        if (!res) return res;
        try {
            if (p.hasChildNodes()) {
                NodeList pp = p.getChildNodes();
                for (int i = 0; i < pp.getLength(); i++) {
                    if (pp.item(i).getNodeName().equals("wfm")) {
                        if (pp.item(i).hasChildNodes()) {
                            NodeList pp2 = pp.item(i).getChildNodes();
                            int m = 0;
                            int n = 0;
                            for (int j = 0; j < pp2.getLength(); j++) if (pp2.item(j).getNodeName().equals("weavingfactors")) {
                                StringTokenizer st = new StringTokenizer(pp2.item(j).getTextContent(), ", \t");
                                if (st.countTokens() > n) n = st.countTokens();
                                m++;
                            }
                            weavingFactorMatrix = new double[m][n];
                            m = 0;
                            for (int j = 0; j < pp2.getLength(); j++) if (pp2.item(j).getNodeName().equals("weavingfactors")) {
                                StringTokenizer st = new StringTokenizer(pp2.item(j).getTextContent(), ", \t");
                                int mm = 0;
                                while (st.hasMoreTokens()) {
                                    try {
                                        weavingFactorMatrix[m][mm] = Math.max(1, Double.parseDouble(st.nextToken()));
                                    } catch (Exception e) {
                                        weavingFactorMatrix[m][mm] = 1;
                                    }
                                    mm++;
                                }
                                while (mm < n) {
                                    weavingFactorMatrix[m][mm] = 1;
                                    mm++;
                                }
                                m++;
                            }
                        } else res = false;
                    }
                }
            } else res = false;
        } catch (Exception e) {
            res = false;
            throw new ExceptionConfiguration(e.getMessage());
        }
        return res;
    }

    /**
	 * Generates XML description of the weaving factor matrix Event.<br>
	 * If the print stream is specified, then XML buffer is written to the stream.
	 * @param out print stream.
	 * @throws IOException
	 */
    public void xmlDump(PrintStream out) throws IOException {
        super.xmlDump(out);
        out.print("<wfm>");
        for (int i = 0; i < weavingFactorMatrix.length; i++) {
            String buf = "";
            for (int j = 0; j < weavingFactorMatrix[0].length; j++) {
                if (j > 0) buf += ", ";
                buf += Double.toString(weavingFactorMatrix[i][j]);
            }
            out.print("<weavingfactors>" + buf + "</weavingfactors>");
        }
        out.print("</wfm></event>");
        return;
    }

    /**
	 * Changes weaving factor matrix for the assigned simple Node.
	 * @return <code>true</code> if operation succeeded, <code>false</code> - otherwise.
	 * @throws ExceptionEvent
	 */
    public final boolean activate(AbstractNodeComplex top) throws ExceptionEvent {
        if (top == null) return false;
        super.activate(top);
        if (!enabled) return enabled;
        AbstractNode nd = top.getNodeById(neid);
        if (nd == null) throw new ExceptionEvent("Node (" + Integer.toString(neid) + ") not found.");
        if (!nd.isSimple()) throw new ExceptionEvent(nd, "Wrong type.");
        System.out.println("Event! Time " + Util.time2string(tstamp) + ": " + description);
        double[][] wfm = ((AbstractNodeHWC) nd).getWeavingFactorMatrix();
        boolean res = ((AbstractNodeHWC) nd).setWeavingFactorMatrix(weavingFactorMatrix);
        weavingFactorMatrix = wfm;
        return res;
    }

    /**
	 * Changes split ratio matrix for the assigned simple Node back to what it was.
	 * @return <code>true</code> if operation succeeded, <code>false</code> - otherwise.
	 * @throws ExceptionEvent
	 */
    public final boolean deactivate(AbstractNodeComplex top) throws ExceptionEvent {
        if (top == null) return false;
        if (!enabled) return enabled;
        AbstractNode nd = top.getNodeById(neid);
        if (nd == null) throw new ExceptionEvent("Node (" + Integer.toString(neid) + ") not found.");
        if (!nd.isSimple()) throw new ExceptionEvent(nd, "Wrong type.");
        System.out.println("Event rollback! Time " + Util.time2string(tstamp) + ": " + description);
        double[][] wfm = ((AbstractNodeHWC) nd).getWeavingFactorMatrix();
        boolean res = ((AbstractNodeHWC) nd).setWeavingFactorMatrix(weavingFactorMatrix);
        weavingFactorMatrix = wfm;
        return res;
    }

    /**
	 * Returns type description. 
	 */
    public final String getTypeString() {
        return "Weaving Factors";
    }

    /**
	 * Returns letter code of the event type.
	 */
    public final String getTypeLetterCode() {
        return "WFM";
    }

    /**
	 * Returns weaving factor matrix.
	 */
    public double[][] getWeavingFactorMatrix() {
        if (weavingFactorMatrix == null) return null;
        int m = weavingFactorMatrix.length;
        int n = weavingFactorMatrix[0].length;
        double[][] wfm = new double[m][n];
        for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) wfm[i][j] = weavingFactorMatrix[i][j];
        return wfm;
    }

    /**
	 * Sets weaving factor matrix.<br>
	 * @param x weaving factor matrix.
	 * @return <code>true</code> if operation succeeded, <code>false</code> - otherwise.
	 */
    public synchronized boolean setWeavingFactorMatrix(double[][] x) {
        if (x == null) return false;
        int m = x.length;
        int n = x[0].length;
        weavingFactorMatrix = new double[m][n];
        for (int i = 0; i < m; i++) for (int j = 0; j < n; j++) weavingFactorMatrix[i][j] = x[i][j];
        return true;
    }
}
