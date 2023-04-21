package de.beas.explicanto.client.sec.dialogs;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import de.beas.explicanto.client.I18N;
import de.beas.explicanto.client.sec.model.ScreenplayDO;

public class NewComponentDialog extends Dialog {

    public static int CONTENT_HEIGHT = 100;

    public static int CONTENT_WIDTH = 250;

    protected Label frameNoInfo;

    protected Text frameNo;

    protected Label positionInfo;

    protected Text position;

    protected Label titleInfo;

    protected Label titleColorInfo;

    protected Label contentInfo;

    protected Label spanInfo;

    protected Text title;

    protected StyledText content;

    protected Text titleColor;

    protected Text span;

    protected Composite contentZone;

    protected ScreenplayDO screenplay;

    protected String frameNoValue;

    protected String positionValue;

    protected String titleValue;

    protected String contentValue;

    protected String titleColorValue;

    protected String spanValue;

    public NewComponentDialog(Shell parentShell, ScreenplayDO screenplay) {
        super(parentShell);
        this.screenplay = screenplay;
    }

    protected Control createDialogArea(Composite parent) {
        Composite dialog = (Composite) super.createDialogArea(parent);
        contentZone = new Composite(dialog, SWT.NONE);
        contentZone.setLayout(new GridLayout(2, false));
        GridData layoutData1 = new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.HORIZONTAL_ALIGN_FILL);
        layoutData1.minimumWidth = 100;
        GridData layoutData2 = new GridData(GridData.VERTICAL_ALIGN_FILL | GridData.FILL_HORIZONTAL);
        frameNoInfo = new Label(contentZone, SWT.NONE);
        frameNoInfo.setText(I18N.translate("sec.dialog.new.frameNo"));
        frameNoInfo.setLayoutData(layoutData1);
        frameNo = new Text(contentZone, SWT.NONE);
        frameNo.setLayoutData(layoutData2);
        positionInfo = new Label(contentZone, SWT.NONE);
        positionInfo.setText(I18N.translate("sec.dialog.new.position"));
        positionInfo.setLayoutData(layoutData1);
        position = new Text(contentZone, SWT.NONE);
        position.setLayoutData(layoutData2);
        titleInfo = new Label(contentZone, SWT.NONE);
        titleInfo.setText(I18N.translate("sec.dialog.new.title"));
        titleInfo.setLayoutData(layoutData1);
        title = new Text(contentZone, SWT.NONE);
        title.setLayoutData(layoutData2);
        titleColorInfo = new Label(contentZone, SWT.NONE);
        titleColorInfo.setText(I18N.translate("sec.dialog.new.titleColor"));
        titleColorInfo.setLayoutData(layoutData1);
        titleColor = new Text(contentZone, SWT.NONE);
        titleColor.setLayoutData(layoutData2);
        contentInfo = new Label(contentZone, SWT.NONE);
        contentInfo.setText(I18N.translate("sec.dialog.new.content"));
        contentInfo.setLayoutData(layoutData1);
        content = new StyledText(contentZone, SWT.V_SCROLL | SWT.MULTI);
        content.addTraverseListener(new TraverseListener() {

            public void keyTraversed(TraverseEvent e) {
                if (e.detail == SWT.TRAVERSE_TAB_NEXT || e.detail == SWT.TRAVERSE_TAB_PREVIOUS) e.doit = true;
            }
        });
        GridData data = new GridData(GridData.FILL_BOTH);
        data.minimumHeight = CONTENT_HEIGHT;
        data.minimumWidth = CONTENT_WIDTH;
        content.setLayoutData(data);
        spanInfo = new Label(contentZone, SWT.NONE);
        spanInfo.setText(I18N.translate("sec.dialog.new.span"));
        spanInfo.setLayoutData(layoutData1);
        span = new Text(contentZone, SWT.NONE);
        span.setLayoutData(layoutData2);
        return dialog;
    }

    public String getTitle() {
        return titleValue;
    }

    public String getTitleColor() {
        return titleColorValue;
    }

    public String getContent() {
        return contentValue;
    }

    public String getSpanText() {
        return spanValue;
    }

    public int getSpan() {
        int span = -1;
        try {
            span = Integer.parseInt(spanValue);
        } catch (Exception e) {
        }
        return span;
    }

    public String getFrameNoText() {
        return frameNoValue;
    }

    public int getFrameNo() {
        int frameNo = -1;
        try {
            frameNo = Integer.parseInt(frameNoValue);
        } catch (Exception e) {
        }
        return frameNo;
    }

    public String getPositionText() {
        return positionValue;
    }

    public int getPosition() {
        int position = -1;
        try {
            position = Integer.parseInt(positionValue);
        } catch (Exception e) {
        }
        return position;
    }

    protected void okPressed() {
        frameNoValue = frameNo.getText();
        positionValue = position.getText();
        titleValue = title.getText();
        titleColorValue = titleColor.getText();
        contentValue = content.getText();
        spanValue = span.getText();
        super.okPressed();
    }

    public void setTitleInfo(String text) {
        titleInfo.setText(text);
    }

    public void setFrameNoInfo(String text) {
        frameNoInfo.setText(text);
    }

    public void setPositionInfo(String text) {
        positionInfo.setText(text);
    }

    public void setTitleColorInfo(String text) {
        titleColorInfo.setText(text);
    }

    public void setContentInfo(String text) {
        contentInfo.setText(text);
    }

    public void setSpanInfo(String text) {
        spanInfo.setText(text);
    }
}
