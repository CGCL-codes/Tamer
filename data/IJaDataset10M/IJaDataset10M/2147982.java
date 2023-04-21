package org.geoforge.guitlcolg.dialog.edit.data.opr.panel;

import org.geoforge.guitlc.dialog.edit.data.panel.GfrPnlFileIoImportShapefileAbs;
import gov.nasa.worldwind.formats.shapefile.DBaseRecord;
import gov.nasa.worldwind.formats.shapefile.ShapefileRecord;
import gov.nasa.worldwind.formats.shapefile.ShapefileRecordPolyline;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.swing.event.DocumentListener;
import org.geoforge.guillc.frame.FrmGfrAbs;
import org.geoforge.guitlc.dialog.edit.data.io.OurShapefileLoader;
import org.geoforge.guitlc.dialog.edit.data.shape.ShapeAbs;
import org.geoforge.guillc.optionpane.GfrOptionPaneAbs;
import org.geoforge.guillc.panel.PnlStatusBarMain;
import org.geoforge.guitlc.dialog.edit.data.panel.PnlSettingsImportFileShape;
import org.geoforge.guitlc.dialog.edit.data.shape.ShapeLinesOpen;
import org.geoforge.lang.util.logging.FileHandlerLogger;

/**
 *
 * @author bantchao
 *
 * email: bantchao_AT_gmail.com
 * ... please remove "_AT_" from the above string to get the right email address
 *
 */
public class PnlContentsOkImportFileShapeLinePipeline extends GfrPnlFileIoImportShapefileAbs {

    private static final Logger _LOGGER_ = Logger.getLogger(PnlContentsOkImportFileShapeLinePipeline.class.getName());

    static {
        PnlContentsOkImportFileShapeLinePipeline._LOGGER_.addHandler(FileHandlerLogger.s_getInstance());
    }

    public PnlContentsOkImportFileShapeLinePipeline(DocumentListener dlrParentDialog, String[] strsExistingLabels) {
        super("Pipeline", dlrParentDialog, strsExistingLabels);
        super._pnlSettings = new PnlSettingsImportFileShape(dlrParentDialog, (ActionListener) this);
    }

    @Override
    public void doJob() throws Exception {
        String strKeyLabel = ((PnlSettingsImportFileShape) super._pnlSettings).getValueKeyLabel();
        String strKeyDescription = ((PnlSettingsImportFileShape) super._pnlSettings).getValueKeyDescription();
        String strKeyUrl = ((PnlSettingsImportFileShape) super._pnlSettings).getValueKeyUrl();
        super._altValue = new ArrayList<ShapeAbs>();
        for (int i = 0; i < this._altRec.size(); i++) {
            ShapefileRecord sfrCur = this._altRec.get(i);
            String strValueLabelCandidate = (String) sfrCur.getAttributes().getValue(strKeyLabel);
            if (strValueLabelCandidate == null || strValueLabelCandidate.trim().length() < 1) {
                String str = "strValueLabelCandidate==null || strValueLabelCandidate.trim().length()<1, record #" + i + 1;
                PnlContentsOkImportFileShapeLinePipeline._LOGGER_.severe(str);
                throw new Exception(str);
            }
            String strValueLabelUnique = super._getUniqueLabel(strValueLabelCandidate);
            if (strValueLabelUnique == null) {
                String str = "by-passing duplicated label name: " + strValueLabelCandidate;
                PnlContentsOkImportFileShapeLinePipeline._LOGGER_.warning(str);
                GfrOptionPaneAbs.s_showDialogWarning(FrmGfrAbs.s_getFrameOwner(PnlStatusBarMain.s_getInstance()), str);
                continue;
            }
            String strValueDescription = null;
            if (strKeyDescription != null && strKeyDescription.trim().length() > 0) strValueDescription = (String) sfrCur.getAttributes().getValue(strKeyDescription);
            String strValueUrl = null;
            if (strKeyUrl != null && strKeyUrl.trim().length() > 0) strValueUrl = (String) sfrCur.getAttributes().getValue(strKeyUrl);
            ArrayList<ArrayList<Point2D.Double>> alt = new ArrayList<ArrayList<Point2D.Double>>();
            ShapefileRecordPolyline pol = (ShapefileRecordPolyline) sfrCur;
            int intNbParts = pol.getNumberOfParts();
            if (intNbParts < 1) {
                System.out.println("intNbParts < 1, i=" + i + ", ignoring");
                continue;
            }
            for (int j = 0; j < intNbParts; j++) {
                ArrayList<Point2D.Double> altPartCur = new ArrayList<Point2D.Double>();
                Iterable<double[]> itrDoublesCur = pol.getPoints(j);
                for (double[] dbls : itrDoublesCur) {
                    double dblLat = dbls[1];
                    double dblLont = dbls[0];
                    Point2D.Double p2dCur = new Point2D.Double(dblLat, dblLont);
                    altPartCur.add(p2dCur);
                }
                alt.add(altPartCur);
            }
            ShapeAbs swlCur = new ShapeLinesOpen(strValueLabelUnique, strValueDescription, strValueUrl, alt);
            super._altValue.add(swlCur);
        }
    }

    @Override
    protected boolean _check_(File fle) throws Exception {
        super._check_(fle);
        OurShapefileLoader sfl = new OurShapefileLoader();
        Object objSource = (Object) fle;
        super._altRec = sfl.getRecordsLinePipeline(objSource);
        if (super._altRec == null || super._altRec.size() < 1) {
            String strWarning = "No valid records found in file";
            strWarning += "\n" + fle.getAbsolutePath();
            PnlContentsOkImportFileShapeLinePipeline._LOGGER_.warning(strWarning);
            GfrOptionPaneAbs.s_showDialogError(FrmGfrAbs.s_getFrameOwner(PnlStatusBarMain.s_getInstance()), strWarning);
            return false;
        }
        ShapefileRecord sfr1 = super._altRec.get(0);
        DBaseRecord brd = sfr1.getAttributes();
        if (brd == null) {
            String str = "brd == null, fle.getAbsolutePath()=" + fle.getAbsolutePath();
            PnlContentsOkImportFileShapeLinePipeline._LOGGER_.warning(str);
            throw new Exception(str);
        }
        Set<Map.Entry<String, Object>> setAttributes = brd.getEntries();
        if (setAttributes == null || setAttributes.isEmpty()) {
            String strWarning = "No valid attributes associated with records of file";
            strWarning += "\n" + fle.getAbsolutePath();
            PnlContentsOkImportFileShapeLinePipeline._LOGGER_.warning(strWarning);
            GfrOptionPaneAbs.s_showDialogError(FrmGfrAbs.s_getFrameOwner(PnlStatusBarMain.s_getInstance()), strWarning);
            return false;
        }
        super._strsAtrributeKey = new String[setAttributes.size()];
        int intCount = 0;
        Iterator itr = setAttributes.iterator();
        while (itr.hasNext()) {
            Map.Entry<String, Object> map = (Map.Entry<String, Object>) itr.next();
            String strKey = map.getKey();
            super._strsAtrributeKey[intCount] = strKey;
            intCount++;
        }
        return true;
    }
}
