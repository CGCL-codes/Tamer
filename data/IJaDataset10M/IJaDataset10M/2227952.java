package org.delafer.benchmark.ui;

import org.delafer.benchmark.helpers.UIHelpers;
import org.delafer.benchmark.ui.charts.InitialPage;
import org.delafer.benchmark.ui.charts.SecondPage;
import org.delafer.benchmark.ui.charts.ZeroPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.wb.swt.SWTResourceManager;

public class SmartWizard extends Composite {

    private LinkedChain<WizardPage> chain = new LinkedChain<WizardPage>();

    private Composite cmpMain;

    private BufferedComposite bufferedCmp;

    public static final GridData gridData(int horizontalAlignment, int verticalAlignment, boolean fillHorizontal, boolean fillVertical, int horizontalWidth, int verticalWidth) {
        GridData ret = new GridData(horizontalAlignment, verticalAlignment, fillHorizontal, fillVertical, 1, 1);
        if (horizontalWidth != -1) ret.widthHint = horizontalWidth;
        if (verticalWidth != -1) ret.heightHint = verticalWidth;
        return ret;
    }

    /**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
    public SmartWizard(Composite parent, int style) {
        super(parent, style);
        GridLayout baseLayout = new GridLayout(1, false);
        setLayout(baseLayout);
        cmpMain = new Composite(this, SWT.NONE);
        cmpMain.setBackground(SWTResourceManager.getColor(SWT.COLOR_CYAN));
        cmpMain.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        bufferedCmp = new BufferedComposite(cmpMain);
        Composite cmpProgress = new Composite(this, SWT.NONE);
        cmpProgress.setBackground(SWTResourceManager.getColor(SWT.COLOR_MAGENTA));
        cmpProgress.setLayoutData(gridData(SWT.FILL, SWT.BOTTOM, true, false, -1, 30));
        cmpProgress.setLayout(new FillLayout(SWT.VERTICAL));
        ProgressBar currentProgress = new ProgressBar(cmpProgress, SWT.SMOOTH);
        currentProgress.setSelection(30);
        currentProgress.setBackground(SWTResourceManager.getColor(30, 200, 150));
        ProgressBar totalProgress = new ProgressBar(cmpProgress, SWT.SMOOTH);
        totalProgress.setSelection(76);
        Composite cmpButtons = new Composite(this, SWT.NONE);
        cmpButtons.setBackground(SWTResourceManager.getColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND));
        cmpButtons.setLayoutData(gridData(SWT.FILL, SWT.BOTTOM, true, false, -1, 40));
        cmpButtons.setLayout(new GridLayout(4, false));
        new Label(cmpButtons, SWT.NONE);
        new Label(cmpButtons, SWT.NONE);
        final Button btnPrevious = new Button(cmpButtons, SWT.NONE);
        btnPrevious.setText("_Previous");
        btnPrevious.setLayoutData(gridData(SWT.RIGHT, SWT.CENTER, true, true, 70, -1));
        final Button btnNext = new Button(cmpButtons, SWT.NONE);
        btnNext.setText("_Next");
        btnNext.setLayoutData(gridData(SWT.RIGHT, SWT.CENTER, false, true, 70, -1));
        Listener listener = new Listener() {

            @Override
            public void handleEvent(Event event) {
                if (event.widget == btnNext) {
                    SmartWizard.this.doNext();
                } else if (event.widget == btnPrevious) {
                    SmartWizard.this.doPrevious();
                }
                ;
            }
        };
        btnPrevious.addListener(SWT.Selection, listener);
        btnNext.addListener(SWT.Selection, listener);
    }

    public void doNext() {
        drawChain(chain.next());
    }

    public void doPrevious() {
        drawChain(chain.previous());
    }

    public void drawChain(LinkedChain<WizardPage>.Chain<WizardPage> page) {
        if (page == null) return;
        Composite cmp = bufferedCmp.getHidden();
        page.getValue().drawComposite(cmp);
        bufferedCmp.swapComposite();
    }

    public void init() {
        chain.add(chain.newChain(new ZeroPage(), 1));
        chain.add(chain.newChain(new InitialPage(), 2));
        chain.add(chain.newChain(new SecondPage(), 3));
        chain.setCurrent(chain.get(1));
        drawChain(chain.getCurrent());
    }

    @Override
    protected void checkSubclass() {
    }

    public static class BufferedComposite {

        private StackLayout layout;

        private Composite active;

        private Composite inactive;

        private Composite cmpMain;

        private BufferedComposite(Composite cmpMain) {
            this.cmpMain = cmpMain;
            this.layout = new StackLayout();
            this.cmpMain.setLayout(this.layout);
            this.active = new Composite(cmpMain, SWT.NONE);
            this.inactive = new Composite(cmpMain, SWT.NONE);
            this.layout.topControl = active;
        }

        public Composite getHidden() {
            return this.inactive;
        }

        public Composite getActive() {
            return this.active;
        }

        public void swapComposite() {
            final Composite tmp = this.active;
            this.active = this.inactive;
            this.inactive = tmp;
            System.out.println("Active is:" + System.identityHashCode(active));
            this.layout.topControl = active;
            active.layout(true);
            this.cmpMain.layout();
            UIHelpers.disposeComposite(this.inactive, false);
        }
    }
}
