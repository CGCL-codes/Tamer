package de.beas.explicanto.client.sec.dialogs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import de.beas.explicanto.client.rcp.dialogs.BaseDialog;
import de.beas.explicanto.client.sec.control.SceneTemplate;

public abstract class FramesListDialog extends BaseDialog {

    protected Composite contentZone;

    protected Label firstFrameInfo;

    protected Combo firstFrameList;

    protected Label secondFrameInfo;

    protected Combo secondFrameList;

    protected SceneTemplate sceneTemplate;

    public FramesListDialog(Shell parentShell, SceneTemplate sceneTemplate) {
        super(parentShell);
        this.sceneTemplate = sceneTemplate;
    }

    protected Control createDialogArea(Composite parent) {
        Composite dialog = (Composite) super.createDialogArea(parent);
        dialog.setLayout(new GridLayout(1, true));
        setLabels();
        contentZone = new Composite(dialog, SWT.NONE);
        contentZone.setLayout(new GridLayout(2, false));
        GridData layoutData1 = new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.FILL_HORIZONTAL);
        layoutData1.minimumWidth = 90;
        GridData layoutData2 = new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.FILL_HORIZONTAL);
        layoutData2.minimumWidth = 150;
        firstFrameInfo = new Label(contentZone, SWT.NONE);
        firstFrameInfo.setLayoutData(layoutData1);
        firstFrameList = new Combo(contentZone, SWT.READ_ONLY);
        for (int i = 0, count = sceneTemplate.getFrames().size(); i < count; i++) {
            firstFrameList.add("" + (i + 1));
        }
        firstFrameList.addSelectionListener(new FrameNoSelectionListener());
        firstFrameList.setLayoutData(layoutData2);
        secondFrameInfo = new Label(contentZone, SWT.NONE);
        secondFrameInfo.setLayoutData(layoutData1);
        secondFrameList = new Combo(contentZone, SWT.READ_ONLY);
        secondFrameList.setEnabled(false);
        secondFrameList.setLayoutData(layoutData2);
        contentZone.setLayoutData(new GridData(GridData.FILL_BOTH));
        setSpecificLabels();
        return dialog;
    }

    protected abstract void setSpecificLabels();

    public String getFirstFrameAsText() {
        return selectedFirstFrame;
    }

    public int getFirstFrame() {
        int frameNo = -1;
        try {
            frameNo = Integer.parseInt(selectedFirstFrame);
        } catch (Exception e) {
        }
        return frameNo;
    }

    public String getSecondFrameAsText() {
        return selectedSecondFrame;
    }

    public int getSecondFrame() {
        int frameNo = -1;
        try {
            frameNo = Integer.parseInt(selectedSecondFrame);
        } catch (Exception e) {
        }
        return frameNo;
    }

    protected String selectedFirstFrame;

    protected String selectedSecondFrame;

    protected void okPressed() {
        selectedFirstFrame = firstFrameList.getItem(firstFrameList.getSelectionIndex());
        selectedSecondFrame = secondFrameList.getItem(secondFrameList.getSelectionIndex());
        super.okPressed();
    }

    class FrameNoSelectionListener extends SelectionAdapter {

        public void widgetSelected(SelectionEvent event) {
            super.widgetSelected(event);
            String selectedFrameTxt = firstFrameList.getItem(firstFrameList.getSelectionIndex());
            secondFrameList.removeAll();
            for (int i = 0, count = sceneTemplate.getFrames().size(); i < count; i++) {
                String newFrameTxt = "" + (i + 1);
                if (!newFrameTxt.equalsIgnoreCase(selectedFrameTxt)) {
                    secondFrameList.add(newFrameTxt);
                }
            }
            secondFrameList.select(0);
            secondFrameList.setEnabled(true);
        }
    }
}
