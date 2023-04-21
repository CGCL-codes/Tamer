package org.geoforge.guitlc.dialog.edit.data.panel;

import java.awt.geom.Point2D;
import javax.swing.event.DocumentListener;
import org.geoforge.guitlc.dialog.edit.data.textfield.TfdCheckableSmallDoubleLat;
import org.geoforge.guitlc.dialog.edit.data.textfield.TfdCheckableSmallDoubleLon;
import org.geoforge.guitlc.dialog.edit.panel.PnlSelEditTfdMultipleAbs;
import org.geoforge.guitlc.dialog.edit.textfield.TfdCheckableSmallDouble;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class PnlSelEditTfdMultipleLocationDegrees extends PnlSelEditTfdMultipleAbs {

    private static final String _STR_WHAT_ = "Location (Lat-Lon in degrees):";

    private TfdCheckableSmallDouble _tfdValueLat_ = null;

    private TfdCheckableSmallDouble _tfdValueLon_ = null;

    public Point2D.Double getValue() throws Exception {
        double dblLat = Double.parseDouble(this._tfdValueLat_.getText().trim());
        double dblLon = Double.parseDouble(this._tfdValueLon_.getText().trim());
        Point2D.Double p2d = new Point2D.Double(dblLon, dblLat);
        return p2d;
    }

    public PnlSelEditTfdMultipleLocationDegrees(DocumentListener lstDocument) {
        super(PnlSelEditTfdMultipleLocationDegrees._STR_WHAT_, true);
        this._tfdValueLat_ = new TfdCheckableSmallDoubleLat(lstDocument);
        this._tfdValueLon_ = new TfdCheckableSmallDoubleLon(lstDocument);
    }

    @Override
    public boolean init() {
        if (!super.init()) return false;
        this._tfdValueLat_.getDocument().addDocumentListener(this);
        this._tfdValueLon_.getDocument().addDocumentListener(this);
        if (!this._tfdValueLat_.init()) return false;
        if (!this._tfdValueLon_.init()) return false;
        super._pnl_.add(this._tfdValueLat_);
        super._pnl_.add(this._tfdValueLon_);
        return true;
    }
}
