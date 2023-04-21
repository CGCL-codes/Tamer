package us.wthr.jdem846.swtui.config;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class LightPositionConfigPanel extends Composite {

    private LightingPreviewPanel previewPanel;

    public LightPositionConfigPanel(Composite parent) {
        super(parent, SWT.FLAT);
        setLayout(new GridLayout(1, false));
        previewPanel = new LightingPreviewPanel(this);
        this.addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent arg0) {
                previewPanel.updatePreview(true);
            }
        });
        GridData gridData = new GridData();
        gridData.horizontalAlignment = SWT.FILL;
        gridData.verticalAlignment = SWT.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        previewPanel.setLayoutData(gridData);
        this.pack();
    }

    public void updatePreview(boolean recreatePolygons) {
        previewPanel.updatePreview(recreatePolygons);
    }

    public void setSolarAzimuth(double solarAzimuth) {
        previewPanel.setSolarAzimuth(solarAzimuth);
    }

    public double getSolarAzimuth() {
        return 0;
    }

    public void setSolarElevation(double solarElevation) {
        previewPanel.setSolarElevation(solarElevation);
    }

    public double getSolarElevation() {
        return 0;
    }
}
