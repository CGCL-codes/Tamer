package com.peterhi.client.views;

import java.util.ResourceBundle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import com.peterhi.app.Application;
import com.peterhi.app.View;

public class TxtMsgViewImpl extends View {

    private static final String ls = System.getProperty("line.separator");

    private static ResourceBundle bundle = ResourceBundle.getBundle("com/peterhi/client/resource/lang");

    private StyledText messages = null;

    private Composite bottom = null;

    private StyledText outMessage = null;

    private Button cmdSend = null;

    private boolean sendByEnter = true;

    public TxtMsgViewImpl(Composite parent, int style) {
        super(parent, style);
        initialize();
    }

    @Override
    public boolean setFocus() {
        return outMessage.setFocus();
    }

    public void appendSystemMessage(final String text) {
        if (Thread.currentThread() == getDisplay().getThread()) {
            int begin = messages.getText().length();
            int len = text.length();
            messages.append(text);
            messages.append(ls);
            StyleRange sr = new StyleRange(begin, len, (Color) Application.get("gray"), null, SWT.NORMAL);
            messages.setStyleRange(sr);
            if (!messages.isFocusControl()) {
                scrollToEnd();
            }
        } else {
            getDisplay().asyncExec(new Runnable() {

                public void run() {
                    appendSystemMessage(text);
                }
            });
        }
    }

    public void appendUserMessage(final String text) {
        if (Thread.currentThread() == getDisplay().getThread()) {
            int userMsgIndent = (Integer) Application.get("userMsgIndent");
            String indentStr = "";
            for (int i = 0; i < userMsgIndent; i++) {
                indentStr += " ";
            }
            String finalText = text.replace(ls, ls + indentStr);
            messages.append(indentStr);
            messages.append(finalText);
            messages.append(ls);
            if (!messages.isFocusControl()) {
                scrollToEnd();
            }
        } else {
            getDisplay().asyncExec(new Runnable() {

                public void run() {
                    appendUserMessage(text);
                }
            });
        }
    }

    public void scrollToEnd() {
        if (Thread.currentThread() == getDisplay().getThread()) {
            messages.setSelection(messages.getText().length());
        } else {
            getDisplay().asyncExec(new Runnable() {

                public void run() {
                    scrollToEnd();
                }
            });
        }
    }

    public void addAndSend(final String text) {
        if (text == null || text.length() <= 0) {
            return;
        }
        if (Thread.currentThread() == getDisplay().getThread()) {
            appendSystemMessage("You said: ");
            appendUserMessage(text);
            outMessage.setText("");
            outMessage.setFocus();
        } else {
            getDisplay().asyncExec(new Runnable() {

                public void run() {
                    addAndSend(text);
                }
            });
        }
    }

    private void initialize() {
        GridData gridData = new GridData();
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;
        gridData.grabExcessVerticalSpace = true;
        gridData.verticalAlignment = GridData.FILL;
        messages = new StyledText(this, SWT.WRAP | SWT.READ_ONLY | SWT.V_SCROLL);
        messages.setEnabled(true);
        messages.setLayoutData(gridData);
        createBottom();
        setSize(new Point(300, 200));
    }

    /**
	 * This method initializes bottom	
	 *
	 */
    private void createBottom() {
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = GridData.FILL;
        gridData3.heightHint = -1;
        gridData3.widthHint = -1;
        gridData3.verticalAlignment = GridData.FILL;
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = GridData.FILL;
        gridData2.grabExcessHorizontalSpace = true;
        gridData2.grabExcessVerticalSpace = true;
        gridData2.verticalAlignment = GridData.FILL;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        gridLayout.verticalSpacing = 0;
        gridLayout.marginWidth = 0;
        gridLayout.marginHeight = 0;
        gridLayout.horizontalSpacing = 5;
        GridData gridData1 = new GridData();
        gridData1.horizontalAlignment = GridData.FILL;
        gridData1.grabExcessHorizontalSpace = true;
        gridData1.heightHint = -1;
        gridData1.verticalAlignment = GridData.FILL;
        bottom = new Composite(this, SWT.NONE);
        bottom.setLayoutData(gridData1);
        bottom.setLayout(gridLayout);
        outMessage = new StyledText(bottom, SWT.MULTI | SWT.WRAP | SWT.V_SCROLL);
        outMessage.setEnabled(true);
        outMessage.setLayoutData(gridData2);
        outMessage.addModifyListener(new org.eclipse.swt.events.ModifyListener() {

            public void modifyText(org.eclipse.swt.events.ModifyEvent e) {
                cmdSend.setEnabled(outMessage.getText().length() > 0);
            }
        });
        outMessage.addVerifyKeyListener(new org.eclipse.swt.custom.VerifyKeyListener() {

            public void verifyKey(org.eclipse.swt.events.VerifyEvent e) {
                if (e.character == SWT.CR) {
                    boolean ctrl = ((e.stateMask &= SWT.CONTROL) == SWT.CONTROL);
                    if (sendByEnter && (!ctrl)) {
                        addAndSend(outMessage.getText());
                    } else if (sendByEnter && ctrl) {
                        outMessage.append(ls);
                        outMessage.setSelection(outMessage.getText().length());
                    } else if ((!sendByEnter) && ctrl) {
                        addAndSend(outMessage.getText());
                    } else {
                        outMessage.append(ls);
                        outMessage.setSelection(outMessage.getText().length());
                    }
                    e.doit = false;
                }
            }
        });
        outMessage.addFocusListener(new org.eclipse.swt.events.FocusAdapter() {

            public void focusGained(org.eclipse.swt.events.FocusEvent e) {
                sendByEnter = (Boolean) Application.get("sendByEnter");
            }
        });
        cmdSend = new Button(bottom, SWT.NONE);
        cmdSend.setText(bundle.getString("control.send.text"));
        cmdSend.setEnabled(false);
        cmdSend.setLayoutData(gridData3);
        cmdSend.setImage((Image) Application.get("icon"));
        cmdSend.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                addAndSend(outMessage.getText());
            }
        });
    }

    @Override
    protected void closing() {
        MenuItem miTxtMsg = (MenuItem) Application.get("miTxtMsg");
        if (!miTxtMsg.isDisposed()) {
            miTxtMsg.setSelection(false);
        }
        super.closing();
    }
}
