package de.fraunhofer.isst.axbench.timing.algorithms.special;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import de.fhg.isst.vts.vts_lib.tools.Tools;
import de.fraunhofer.isst.axbench.timing.TimingDemoMain;
import de.fraunhofer.isst.axbench.timing.algorithms.Combinator.NueTuple;
import de.fraunhofer.isst.axbench.timing.io.TiKZDocument;
import de.fraunhofer.isst.axbench.timing.model.Task;
import de.fraunhofer.isst.axbench.timing.model.Transaction;
import de.fraunhofer.isst.axbench.timing.model.TransactionSet;
import de.fraunhofer.isst.axbench.timing.model.special.TransactionResult;
import de.fraunhofer.isst.axbench.timing.persistence.Persistence;

public class ResultTransactionSort {

    private NueTuple v;

    private int p0;

    private int pL;

    private double r;

    private TransactionSet gamma;

    private Task tauua;

    private TiKZDocument tikzd = null;

    /**
	 * Diese Klasse erzeugt eine Transaktion, die das Runmuster
	 * f�r den exakten WCRT-Algorithmus darstellt.<br>
	 * Ist ein TiKZDocument gesetzt, so werden diesem Objekt 
	 * Kopien von Zwischenergebnissen f�r eine Visualisierung �bergeben.<br>
	 * Die Zwischenergebnisse sind:<br>
	 * "RES" - das Endergebnis,<br>
	 * "VORRES" - das gesamte Aktivierungsmuster,<br>
	 * je Transaktion:<br>
	 * "AKT" - das komulative Aktivierungsmuster (uninteressant),<br>
	 * "Start" - die Anfangsbelegung der Transaktion (uninteressant),<br>
	 * "Verschiebung" - erster Zwischenschritt (uninteressant),<br>
	 * "Jitter" - das Aktivierungsmuster der Transaktion,<br>
	 *  
	 * @param gamma das Transaktionsset
	 * @param tauua Task under analysis
	 * @param v das NueTuple (f�r den schechtesten Fall)
	 * @param p0 erster Instanzindex von tauua
	 * @param pL letzter Instanzindex von tauua
	 * @param r Responsetime der letzten Instanz von tauua
	 */
    public ResultTransactionSort(TransactionSet gamma, Task tauua, NueTuple v, int p0, int pL, double r) {
        this.gamma = gamma;
        this.tauua = tauua;
        this.v = v;
        this.p0 = p0;
        this.pL = pL;
        this.r = r;
    }

    /**
	 * Das Erzeugen der Transaktion, die das Runmuster
	 * f�r den exakten WCRT-Algorithmus darstellt.<br> 
	 * Seiteneffekte siehe oben!
	 * @return TransactionResult
	 */
    public TransactionResult getTransactionResult() {
        TransactionResult tr = null;
        TransactionResult tr1 = null;
        Transaction ta = gamma.findTransactionOf(tauua);
        double tu = ta.getT();
        double tmin = p0 * tu;
        double tmax = pL * tu + r;
        double tint = tmax - tmin;
        tikzdsettgesmax(tint);
        int i = 0;
        for (Transaction gammai : gamma) {
            double ti = gammai.getT();
            int pfac = ceil(tint / ti);
            Task tauic = v.get(i);
            int p0vi = -(floor((tauic.j + ti) / ti) - 1);
            double oicjic = tauic.o + tauic.j;
            tr1 = new TransactionResult(pfac, tint, gammai, p0vi);
            if (!tauic.equals(tauua)) {
                tikzdaddInstAll(tr1, "Start3 ", i);
            } else {
                tikzdaddInstAll(tr1, "Start4 ", i);
            }
            tr1.shiftTo(-oicjic);
            tikzdaddInstAll(tr1, "Verschiebung3 ", i);
            tr1.jitterShift();
            if (!tauic.equals(tauua)) {
                tikzdaddInstAll(tr1, "Jitter3 ", i);
            } else {
                tikzdaddInstAll(tr1, "Jitter4 ", i);
            }
            if (tr == null) {
                tr = tr1;
            } else {
                tr.addsort(tr1);
            }
            tikzdaddInstAll(tr, "AKT3 ", i);
            i++;
        }
        tikzdaddInstAll(tr, "VORRES");
        tr.sortandshift();
        tikzdaddInstAll(tr, "RES");
        return tr;
    }

    private void tikzdaddInstAll(TransactionResult tr, String key, int i) {
        if (tikzd != null) tikzd.add(tr.copy(), key + i);
    }

    private void tikzdaddInstAll(TransactionResult tr, String key) {
        if (tikzd != null) tikzd.add(tr.copy(), key);
    }

    private void tikzdsettgesmax(double tgesmax) {
        if (tikzd != null) tikzd.setXgesmax(tgesmax);
    }

    public static final int ceil(double a) {
        return (int) Math.ceil(a);
    }

    protected static final int floor(double a) {
        return (int) Math.floor(a);
    }

    /**
     * Setzen des TiKZDocuments zum Sammeln der Zwischenergebnisse
     * @param tikzd
     */
    public void setTiKZDocument(TiKZDocument tikzd) {
        this.tikzd = tikzd;
    }

    public static void main(String... args) throws IOException {
        File f = new File("sample_data/taskdata_rr5_beisp.csv");
        TransactionSet gamma = Persistence.readCsv(f, false);
        NueTuple v = new NueTuple();
        TiKZDocument tikzd = new TiKZDocument();
        Map<String, String> col = new HashMap<String, String>();
        col.put("Ta_1_1", "tblue");
        col.put("Ta_1_2", "tyellow");
        col.put("Ta_2_1", "tred");
        col.put("Ta_2_2", "tgreen");
        col.put("Ta_2_3", "tcyan");
        col.put("Ta_3_1", "tred");
        col.put("Ta_3_1+", "tred");
        col.put("Transaktion 1 Task 1", "tblue");
        col.put("Transaktion 1 Task 2", "tyellow");
        col.put("Transaktion 1 Task 3", "tred");
        col.put("Transaktion 1 Task 4", "tgreen");
        col.put("Transaktion 1 Task 5", "tcyan");
        col.put("Transaktion 1 Task 6", "tred");
        col.put("Transaktion 1 Task 7", "tblue");
        col.put("Transaktion 2 Task 1", "tred");
        col.put("Transaktion 2 Task 2", "tred");
        col.put("Transaktion 2 Task 3", "tblue");
        col.put("Transaktion 2 Task 4", "tyellow");
        col.put("Transaktion 2 Task 5", "tred");
        col.put("Transaktion 2 Task 6", "tcyan");
        col.put("Transaktion 2 Task 7", "tgreen");
        Map<String, Boolean> inst = new HashMap<String, Boolean>();
        inst.put("Ta_3_1", new Boolean(true));
        inst.put("Ta_3_1+", new Boolean(true));
        inst.put("Ta_1_1", new Boolean(true));
        inst.put("Ta_1_2", new Boolean(true));
        inst.put("Ta_2_1", new Boolean(true));
        inst.put("Ta_2_2", new Boolean(true));
        inst.put("Ta_2_3", new Boolean(true));
        inst.put("Transaktion 1 Task 1", new Boolean(true));
        inst.put("Transaktion 1 Task 2", new Boolean(true));
        inst.put("Transaktion 1 Task 3", new Boolean(true));
        inst.put("Transaktion 1 Task 4", new Boolean(true));
        inst.put("Transaktion 1 Task 5", new Boolean(true));
        inst.put("Transaktion 1 Task 6", new Boolean(true));
        inst.put("Transaktion 1 Task 7", new Boolean(true));
        inst.put("Transaktion 2 Task 1", new Boolean(true));
        inst.put("Transaktion 2 Task 2", new Boolean(true));
        inst.put("Transaktion 2 Task 3", new Boolean(true));
        inst.put("Transaktion 2 Task 4", new Boolean(true));
        inst.put("Transaktion 2 Task 5", new Boolean(true));
        inst.put("Transaktion 2 Task 6", new Boolean(true));
        inst.put("Transaktion 2 Task 7", new Boolean(true));
        tikzd.setInsttab(inst);
        Map<String, Boolean> fig = new HashMap<String, Boolean>();
        fig.put("RES", new Boolean(true));
        tikzd.setFigtab(fig);
        tikzd.setGamma(gamma);
        v.addFirst(gamma.get(0).tau(0));
        v.add(gamma.get(1).tau(0));
        int p0 = -3;
        int pL = 2;
        double r = 5.;
        ResultTransactionSort rts = new ResultTransactionSort(gamma, gamma.get(1).tau(0), v, p0, pL, r);
        rts.setTiKZDocument(tikzd);
        TransactionResult tr = rts.getTransactionResult();
        try {
            File binDir = Tools.getProgramDirectoryAsFile(TimingDemoMain.class, new String[] { "bin" });
            File showScript = new File(binDir, "latex_prozess_show.bat");
            File outputFile = File.createTempFile("algorithmen2z7", ".pdf");
            File workingDir = outputFile.getParentFile();
            String baseName = outputFile.getName().replaceFirst("\\.pdf$", "");
            File tikzDir = Tools.getProgramDirectoryAsFile(TimingDemoMain.class, new String[] { "" });
            tikzd.setTikzphad(tikzDir.getAbsolutePath().replace('\\', '/') + "/tikz");
            tikzd.showDocumentAsPDF(showScript, workingDir, baseName, null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ErgTransaktion: " + tr);
    }
}
