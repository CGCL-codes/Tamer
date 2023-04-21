package org.designerator.media.image.editor.wizard;

import org.designerator.common.interfaces.IImageEditor;
import org.designerator.common.interfaces.IProcessor;
import org.designerator.common.interfaces.IProcessListenerAdapter;
import org.designerator.media.image.ImageCanvas;
import org.designerator.media.image.filter.Contrast;
import org.designerator.media.image.filter.control.FilterViewToolBar;
import org.designerator.media.image.wizards.IProcessorPage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class EditorPage extends WizardPage implements IProcessorPage {

    ImageCanvas imageCanvas;

    private Composite main;

    private IProcessor processor;

    ImageEditorWizard imageEnhanceWizard;

    protected int lastSelection = Contrast.DEFAULTINPUT;

    private boolean performFinish;

    public EditorPage(ImageCanvas imageCanvas, String pageName, ImageEditorWizard imageEnhanceWizard) {
        super(pageName);
        setTitle(pageName);
        setDescription(pageName);
        this.imageCanvas = imageCanvas;
        this.imageEnhanceWizard = imageEnhanceWizard;
    }

    public boolean canFlipToNextPage() {
        return false;
    }

    @Override
    public void createControl(Composite parent) {
        main = new Composite(parent, SWT.None);
        GridData fileSelectionData = new GridData(GridData.GRAB_HORIZONTAL | GridData.FILL_HORIZONTAL);
        main.setLayoutData(fileSelectionData);
        GridLayout fileSelectionLayout = new GridLayout(1, false);
        main.setLayout(fileSelectionLayout);
        setControl(main);
        initProcessor();
        init();
        if (imageEnhanceWizard.isRunOnStartup()) {
            processor.processDefault(null, true);
        }
    }

    @Override
    public void disposeProcessor() {
        processor.dispose();
    }

    @Override
    public int getLastSelection() {
        return lastSelection;
    }

    @Override
    public void init() {
        IImageEditor imageEditmanager = imageCanvas.getImageEditmanager();
        FilterViewToolBar.getOptionsBar(main, imageEditmanager);
        if (processor != null) {
            processor.createControl(main, new IProcessListenerAdapter() {

                @Override
                public void setDescription(String name) {
                    EditorPage.this.setDescription(name);
                }

                @Override
                public void processStarted(int work) {
                    imageEnhanceWizard.setBusy(true);
                }

                @Override
                public void processEnded() {
                    imageEnhanceWizard.setBusy(false);
                    if (performFinish) {
                        final WizardDialog container = (WizardDialog) imageEnhanceWizard.getContainer();
                        if (container != null) {
                            container.close();
                        }
                    }
                }

                @Override
                public void setCurrentSelection(int selection, String name, int index) {
                    lastSelection = selection;
                }
            }, null);
        }
        main.layout(true);
    }

    public boolean isPerformFinish() {
        return performFinish;
    }

    @Override
    public void setIProcessor(IProcessor p) {
        processor = p;
    }

    @Override
    public void initProcessor() {
        if (processor != null) {
            IImageEditor imageEditmanager = imageCanvas.getImageEditmanager();
            processor.init(imageEditmanager, true, false);
        }
    }

    @Override
    public void runProcessorDefault(IProgressMonitor monitor) {
        if (processor != null) {
            processor.processDefault(monitor, false);
        }
    }

    public void setSelection(int sel) {
        lastSelection = sel;
    }

    public void setPerformFinish(boolean b) {
        performFinish = b;
    }
}
