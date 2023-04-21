package org.epo.jbps.heart.request;

import java.util.ArrayList;
import org.epo.jbps.generic.BpsException;
import org.epo.jbps.heart.main.BpsProcessException;
import org.epo.jbps.heart.xmlparser.SaxHandler;

/**
 * Object representing an overlay in a control file
 * It can be on a DOC or an APPDOC or a SINGLE SHEET
 * 
 * @author Infotel Conseil
 */
public class Overlay {

    private ArrayList listPage = new ArrayList();

    /**
 * Overlay constructor
 */
    public Overlay() {
        super();
    }

    /**
 * Add a page on which overlay is to be done
 * @param theOverlayPage OverlayPage
 */
    public void addPage(OverlayPage theOverlayPage) {
        listPage.add(theOverlayPage);
    }

    /**
 * listPage variable accesser
 * @return java.util.ArrayList
 */
    public ArrayList getListPage() {
        return listPage;
    }

    /**
 * Get the overlay page corresponding to a given page number
 * @param thePage int
 * @return OverlayPage
 */
    public OverlayPage getPage(int thePage) {
        return (OverlayPage) listPage.get(thePage);
    }

    /**
 * Writes the overlay data to be overlaid on document
 * @param theHandler SaxHandler
 * @exception IOException
 * @exception BpsProcessException
 */
    public void writeOverlay(SaxHandler theHandler) throws BpsException, BpsProcessException {
        int myCpt = 0;
        for (myCpt = 0; myCpt < getListPage().size(); myCpt++) {
            getPage(myCpt).writePage(theHandler);
        }
    }
}
