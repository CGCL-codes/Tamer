package fr.tango.tangopanels.abstractpanels.ccd;

import fr.esrf.tangoatk.widget.attribute.RawImageViewer;
import fr.esrf.tangoatk.widget.attribute.IRoiListener;
import fr.esrf.tangoatk.widget.util.ErrorHistory;
import fr.esrf.tangoatk.core.*;
import fr.esrf.tangoatk.core.attribute.NumberScalar;
import javax.swing.*;
import java.awt.*;

/**
 * The image panel
 */
public class ImagePanel extends JPanel implements INumberScalarListener, IRefreshPanel {

    private fr.esrf.tangoatk.core.AttributeList attributeList;

    private NumberScalar depthModel;

    private int lastDepth = -1;

    private RawImageViewer imgViewer;

    private ErrorHistory errWin;

    /**
   * Construct an image panel
   * @param devName CCD device name
   * @param err The application error history
   */
    public ImagePanel(String devName, ErrorHistory err, IRoiListener roiListener) {
        this.errWin = err;
        imgViewer = new RawImageViewer();
        imgViewer.addRoiListener(roiListener);
        setLayout(new BorderLayout());
        add(imgViewer, BorderLayout.CENTER);
        attributeList = new fr.esrf.tangoatk.core.AttributeList();
        attributeList.addErrorListener(errWin);
        try {
            depthModel = (NumberScalar) attributeList.add(devName + "/Depth");
            depthModel.refresh();
            updateImageFormat((int) depthModel.getNumberScalarValue());
            depthModel.addNumberScalarListener(this);
            IRawImage imgModel = (IRawImage) attributeList.add(devName + "/Image");
            imgViewer.setModel(imgModel);
        } catch (ConnectionException e1) {
        }
        attributeList.setRefreshInterval(1000);
        attributeList.startRefresher();
    }

    public void numberScalarChange(NumberScalarEvent evt) {
        Object src = evt.getSource();
        if (src == depthModel) {
            updateImageFormat((int) evt.getValue());
        }
    }

    public void stateChange(AttributeStateEvent evt) {
    }

    public void errorChange(ErrorEvent evt) {
    }

    public void clearModel() {
        imgViewer.clearModel();
        if (depthModel != null) {
            depthModel.removeNumberScalarListener(this);
            depthModel = null;
        }
        attributeList.removeErrorListener(errWin);
        attributeList.stopRefresher();
    }

    public void stopRefresher() {
        attributeList.stopRefresher();
    }

    public void startRefresher() {
        attributeList.startRefresher();
    }

    public int getRefreshPeriod() {
        return attributeList.getRefreshInterval();
    }

    public void setRefreshPeriod(int ms) {
        attributeList.setRefreshInterval(ms);
    }

    public void refreshOnce() {
        attributeList.refresh();
    }

    public boolean isRefreshing() {
        return attributeList.isRefresherStarted();
    }

    private void updateImageFormat(int depth) {
        if (depth == lastDepth) return;
        lastDepth = depth;
        switch(depth) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }
}
