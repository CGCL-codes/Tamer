package joelib.gui.render;

import java.util.Vector;
import org.apache.log4j.Category;
import joelib.molecule.JOEMol;
import joelib.util.JHM;

/**
 * Holding multiple orthogonal line informations.
 *
 * @author     wegnerj
 * @license    GPL
 * @cvsversion    $Revision: 1.5 $, $Date: 2004/08/31 14:23:21 $
 */
public class OrthoLines {

    private static Category logger = Category.getInstance("joelib.gui.render.Arrows");

    public JOEMol molecule;

    public OrthoLine[] orthoLines;

    public OrthoLines(JOEMol mol, String entries) {
        if (!parseFromToAtoms(mol, entries)) {
            logger.error("from-option-to;from-option-to;... line could not be parsed.");
        }
    }

    /**
     * Parse entries which start with a non-digit.
     *
     * @param optionEntry
     * @return
     */
    public boolean parseFromToAtoms(JOEMol mol, String fromto) {
        molecule = mol;
        Vector entries = new Vector();
        OrthoLine oLine;
        Vector oLineV = new Vector();
        JHM.tokenize(entries, fromto, ";");
        String entry;
        for (int i = 0; i < entries.size(); i++) {
            entry = (String) entries.get(i);
            oLine = new OrthoLine();
            if (!oLine.parseFromToAtom(entry)) {
                return false;
            }
            oLineV.add(oLine);
        }
        orthoLines = new OrthoLine[oLineV.size()];
        for (int i = 0; i < oLineV.size(); i++) {
            orthoLines[i] = (OrthoLine) oLineV.get(i);
        }
        return true;
    }
}
