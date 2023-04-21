package genestudio.Controllers.PrimerWizzard;

import genestudio.Controls.CopyMouseListener;
import genestudio.Controllers.Main.MainWindowCode;
import genestudio.Controllers.Main.MenuItemMouseListener;
import genestudio.Interfaces.PrimerWizzard.PrimerReady;
import genestudio.Utilities.Nucleotide.StaticTools;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author nrovinskiy
 */
public class PrimerReadyController extends PrimerReady {

    public static final int NOT_NECCESARY = -200;

    public static final int THREE_PRIME = 10;

    public static final int MINIMAL_LOOP = 7;

    /**
     * Creates new instance of PrimerReadyController.
     * @param main the main window
     * @param forward HashMap with forward primers as key and their Tm as value
     * @param reverse HashMap with revers primers as key and their Tm as value
     * @param TmDiff allowed difference between Tm's of forwerd and reverse primers. If  this parameter
     * is set to negative value or NOT_NECCESARY constant, it will be ignored.
     */
    MainWindowCode mwcMain;

    public PrimerReadyController(MainWindowCode main, HashMap<String, Double> forward, HashMap<String, Double> reverse, int TmDiff) {
        super(main, false);
        mwcMain = main;
        tblPrimers.addMouseListener(new CopyMouseListener(mnuCopy));
        btnSave.addActionListener(new PrimerSaveListener(this));
        pmiCopy.addActionListener(new RowCopyListener(tblPrimers));
        mnuCopy.addPopupMenuListener(new PopupTableListener(this));
        pmiCopy.addMouseListener(new MenuItemMouseListener());
        ArrayList<Object[]> arrPrimerData = new ArrayList();
        for (Iterator<String> i = forward.keySet().iterator(); i.hasNext(); ) {
            String strFwd = i.next();
            double dblTmFwd = forward.get(strFwd).doubleValue();
            for (Iterator<String> j = reverse.keySet().iterator(); j.hasNext(); ) {
                String strRev = j.next();
                double dblTmRev = reverse.get(strRev);
                if (TmDiff >= Math.abs(dblTmFwd - dblTmRev)) {
                    arrPrimerData.add(new Object[] { strFwd, forward.get(strFwd), SearchForHairpin(strFwd), StaticTools.getInvertedDNASequence(strRev), reverse.get(strRev), SearchForHairpin(strRev) });
                }
            }
        }
        for (int k = 0; k < arrPrimerData.size(); k++) {
            for (int l = 0; l < arrPrimerData.size() - k - 1; l++) {
                double dblTm1Difference = Math.abs(((Double) arrPrimerData.get(l)[1]).doubleValue() - ((Double) arrPrimerData.get(l)[4]).doubleValue());
                if (arrPrimerData.get(l)[2].toString().equals("YES")) dblTm1Difference += 100;
                if (arrPrimerData.get(l)[5].toString().equals("YES")) dblTm1Difference += 100;
                double dblTm2Difference = Math.abs(((Double) arrPrimerData.get(l + 1)[1]).doubleValue() - ((Double) arrPrimerData.get(l + 1)[4]).doubleValue());
                if (arrPrimerData.get(l + 1)[2].toString().equals("YES")) dblTm2Difference += 100;
                if (arrPrimerData.get(l + 1)[5].toString().equals("YES")) dblTm2Difference += 100;
                if (dblTm1Difference > dblTm2Difference) {
                    arrPrimerData.add(l + 2, arrPrimerData.get(l));
                    arrPrimerData.remove(l);
                }
            }
        }
        DefaultTableModel dtmPrimers = (DefaultTableModel) tblPrimers.getModel();
        for (int i = 0; i < arrPrimerData.size(); i++) {
            dtmPrimers.addRow(arrPrimerData.get(i));
        }
        for (int i = 0; i < dtmPrimers.getColumnCount(); i++) {
            TableColumn column = tblPrimers.getColumnModel().getColumn(i);
            int intMaxString = tblPrimers.getFontMetrics(tblPrimers.getFont()).stringWidth(tblPrimers.getColumnName(i));
            for (int l = 0; l < tblPrimers.getRowCount(); l++) {
                intMaxString = Math.max(intMaxString, tblPrimers.getFontMetrics(tblPrimers.getFont()).stringWidth(tblPrimers.getValueAt(l, i).toString()));
            }
            column.setPreferredWidth(Math.max(column.getPreferredWidth(), intMaxString + 10));
            intMaxString = 0;
        }
    }

    /**
     * Searches three bp across 10bp 3' region (end of sequence) complimentary to somrthing 7bp
     * apart from it.
     * @param sequence primer sequence
     * @return true if complimentarity was found, false otherwise/
     */
    private String SearchForHairpin(String sequence) {
        int intTripletBegin = -1;
        StringBuilder sbTriplet = new StringBuilder();
        for (int i = 1; i <= THREE_PRIME; i++) {
            if (i > sequence.length()) break;
            if ((sequence.toUpperCase().charAt(sequence.length() - i) == 'C') || ((sequence.toUpperCase().charAt(sequence.length() - i) == 'G'))) {
                sbTriplet.insert(0, sequence.toUpperCase().charAt(sequence.length() - i));
                intTripletBegin = sequence.length() - i;
            } else {
                sbTriplet.delete(0, sbTriplet.length());
                intTripletBegin = -1;
            }
            if (sbTriplet.length() == 3) {
                String strTriplet = StaticTools.getInvertedDNASequence(sbTriplet.toString());
                for (int j = 0; j < intTripletBegin - MINIMAL_LOOP; j++) {
                    if (sequence.substring(j, intTripletBegin - MINIMAL_LOOP).length() < 3) break;
                    if (sequence.substring(j).startsWith(strTriplet)) {
                        strTriplet = null;
                        sbTriplet = null;
                        return "YES";
                    }
                }
                strTriplet = null;
                intTripletBegin = -1;
            }
        }
        return "NO";
    }

    @Override
    public void dispose() {
        mnuCopy.setVisible(false);
        super.dispose();
    }
}
