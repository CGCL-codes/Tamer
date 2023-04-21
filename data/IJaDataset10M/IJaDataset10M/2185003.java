package org.jcryptool.analysis.freqanalysis.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;
import org.jcryptool.analysis.freqanalysis.DemoEPMAnager;
import org.jcryptool.analysis.freqanalysis.ui.FreqAnalysisUI;
import org.jcryptool.core.operations.editors.EditorsManager;

/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */
public class SampleView extends ViewPart {

    /**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
    public void createPartControl(Composite parent) {
        FreqAnalysisUI myUI = new FreqAnalysisUI(parent, SWT.NONE);
    }

    /**
	 * Passing the focus request to the viewer's control.
	 */
    public void setFocus() {
    }
}
