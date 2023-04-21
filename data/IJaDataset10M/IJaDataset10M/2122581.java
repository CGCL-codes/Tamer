package com.ibm.tuningfork.histogram;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import com.ibm.tuningfork.core.dialog.FigureColorDialogComponent;
import com.ibm.tuningfork.core.dialog.TabbedDialog;

public class ConfigureHistogramDialog extends TabbedDialog {

    private Histogram histogram;

    private HistogramAxesDialogComponent axesPage;

    private FigureColorDialogComponent colorPage;

    public ConfigureHistogramDialog(Shell parent, Histogram histogram) {
        super(parent, "Configure Histogram View");
        this.histogram = histogram;
    }

    public void addTabs(TabFolder tabFolder) {
        Composite axesComposite = addTab(tabFolder, "Axes");
        axesPage = new HistogramAxesDialogComponent(histogram);
        axesPage.createControl(axesComposite);
        Composite colorComposite = addTab(tabFolder, "Color");
        colorPage = new FigureColorDialogComponent(histogram);
        colorPage.createControl(colorComposite);
    }

    public boolean save() {
        return axesPage.save() && colorPage.save();
    }
}
