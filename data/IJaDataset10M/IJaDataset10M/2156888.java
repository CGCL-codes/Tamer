package org.designerator.media.image.wizards;

import org.designerator.common.interfaces.GuiProvider;
import org.designerator.common.interfaces.IImageEditor;
import org.designerator.common.interfaces.IProcessor;
import org.designerator.common.interfaces.IProcessListenerAdapter;
import org.designerator.media.image.ImageCanvas;
import org.designerator.media.image.filter.Brighten;
import org.designerator.media.image.filter.Processor;
import org.designerator.media.image.filter.Saturation;
import org.designerator.media.image.filter.control.FilterViewToolBar;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class SaturationPage extends WizardPage implements IProcessorPage {

    ImageCanvas imageCanvas;

    private Composite main;

    private boolean init;

    private Saturation processor;

    ImageEnhanceWizard imageEnhanceWizard;

    private boolean firstRun;

    protected int lastSelection = Saturation.DEFAULTINPUT;

    public SaturationPage(ImageCanvas imageCanvas, String pageName, ImageEnhanceWizard imageEnhanceWizard) {
        super(pageName);
        setTitle(pageName);
        setDescription(Messages.SaturationPage_desc);
        this.imageCanvas = imageCanvas;
        this.imageEnhanceWizard = imageEnhanceWizard;
    }

    public boolean canFlipToNextPage() {
        return true;
    }

    @Override
    public void createControl(Composite parent) {
        main = new Composite(parent, SWT.None);
        GridData fileSelectionData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
        main.setLayoutData(fileSelectionData);
        GridLayout fileSelectionLayout = new GridLayout(1, false);
        main.setLayout(fileSelectionLayout);
        setControl(main);
    }

    @Override
    public int getLastSelection() {
        return lastSelection;
    }

    public void init() {
        if (init) {
            return;
        }
        init = true;
        IImageEditor imageEditmanager = imageCanvas.getImageEditmanager();
        FilterViewToolBar.getOptionsBar(main, imageEditmanager);
        processor = new Saturation();
        int[] s = { lastSelection };
        processor.createControl(main, new IProcessListenerAdapter() {

            @Override
            public void setDescription(String name) {
                SaturationPage.this.setDescription(name);
            }

            @Override
            public void processStarted(int work) {
                imageEnhanceWizard.setBusy(true);
            }

            @Override
            public void processEnded() {
                imageEnhanceWizard.setBusy(false);
            }

            @Override
            public void setCurrentSelection(int selection, String name, int index) {
                lastSelection = selection;
            }
        }, s);
        main.layout(true);
    }

    public void initProcessor() {
        if (processor != null) {
            IImageEditor imageEditmanager = imageCanvas.getImageEditmanager();
            processor.init(imageEditmanager, true, false);
            if (!firstRun) {
                processor.processDefault();
                firstRun = true;
            }
        }
    }

    public void disposeProcessor() {
        processor.dispose();
    }

    public IWizardPage getNextPage() {
        return super.getNextPage();
    }

    public void setSelection(int sel) {
        lastSelection = sel;
    }

    public void runProcessorDefault(IProgressMonitor monitor) {
    }

    public void setIProcessor(IProcessor p) {
    }
}
