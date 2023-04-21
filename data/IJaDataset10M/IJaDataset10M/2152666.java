package joelib2.jcamp;

import java.util.List;
import java.util.Vector;

/**
 * Stores single JCAMP data.
 *
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.6 $, $Date: 2005/02/17 16:48:35 $
 * @.cite dl93
 * @.cite dw88
 * @.cite ghhjs91
 * @.cite lhdl94
 * @.cite dhl90
 */
public class JCAMPData {

    private List csEntries;

    private List dxEntries;

    private List linkEntries;

    public JCAMPData() {
        dxEntries = new Vector();
        csEntries = new Vector();
        linkEntries = new Vector();
    }

    public void addCSEntry(JCAMPDataBlock cs) {
        csEntries.add(cs);
    }

    public void addDXEntry(JCAMPDataBlock dx) {
        dxEntries.add(dx);
    }

    public void addLinkEntry(JCAMPDataBlock link) {
        linkEntries.add(link);
    }

    public JCAMPDataBlock getDXEntry(int index) {
        if (dxEntries.size() == 0) {
            return null;
        }
        return (JCAMPDataBlock) dxEntries.get(index);
    }
}
