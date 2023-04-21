package net.sourceforge.phpuniteclipse.testsrunner.ui;

import net.sourceforge.phpuniteclipse.testsrunner.testpool.TestPool;
import net.sourceforge.phpuniteclipse.testsrunner.SimpletestPlugin;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ProgressInfoComposite extends Composite {

    private Label labelRuns, labelRunsVal;

    private Label labelErrors, labelErrorsImage, labelErrorsVal;

    private Label labelFailures, labelFailuresImage, labelFailuresVal;

    private final Image fFailureIcon = SimpletestPlugin.createImage("testfail.gif");

    private final Image fErrorIcon = SimpletestPlugin.createImage("testerr.gif");

    private int curstep = 0;

    private ProgressBar progressBar;

    /**
	 * @param arg0
	 * @param arg1
	 */
    public ProgressInfoComposite(Composite parent) {
        super(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        setLayout(gridLayout);
        progressBar = new ProgressBar(parent);
        progressBar.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL));
        Composite labelsComposite = new Composite(this, SWT.WRAP);
        labelsComposite.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_CENTER));
        GridLayout lblGridLayout = new GridLayout();
        lblGridLayout.numColumns = 8;
        lblGridLayout.makeColumnsEqualWidth = false;
        lblGridLayout.marginWidth = 0;
        labelsComposite.setLayout(lblGridLayout);
        labelRuns = new Label(labelsComposite, SWT.NONE);
        labelRuns.setText("Runs: ");
        labelRuns.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        labelRunsVal = new Label(labelsComposite, SWT.READ_ONLY);
        labelRunsVal.setText("0 / 0");
        labelRunsVal.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING));
        labelFailuresImage = new Label(labelsComposite, SWT.NONE);
        labelFailuresImage.setImage(fFailureIcon);
        labelFailuresImage.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        labelFailures = new Label(labelsComposite, SWT.NONE);
        labelFailures.setText("Failures: ");
        labelFailures.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        labelFailuresVal = new Label(labelsComposite, SWT.READ_ONLY);
        labelFailuresVal.setText("0");
        labelFailuresVal.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING));
        labelErrorsImage = new Label(labelsComposite, SWT.NONE);
        labelErrorsImage.setImage(fErrorIcon);
        labelErrorsImage.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        labelErrors = new Label(labelsComposite, SWT.NONE);
        labelErrors.setText("Errors: ");
        labelErrors.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        labelErrorsVal = new Label(labelsComposite, SWT.READ_ONLY);
        labelErrorsVal.setText("0");
        labelErrorsVal.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING));
    }

    public void resetInfo() {
        curstep = 0;
        labelErrorsVal.setText("0");
        labelFailuresVal.setText("0");
        labelRunsVal.setText("0 / 0");
        progressBar.reset();
    }

    public void updateInfo(TestPool testPool) {
        int numTestsOverall = testPool.numTestCount;
        int numTestsRun = testPool.numTestRun;
        progressBar.setMaximum(numTestsOverall);
        while (curstep < numTestsRun) {
            progressBar.step(testPool.numTestFailures + testPool.numTestErrors);
            curstep++;
        }
        labelRunsVal.setText(numTestsRun + " / " + numTestsOverall);
        labelFailuresVal.setText("" + testPool.numTestFailures);
        labelErrorsVal.setText("" + testPool.numTestErrors);
    }
}
