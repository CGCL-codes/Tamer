package net.sf.edevtools.tools.logviewer.core.ui.wizards.opensource;

import java.util.Arrays;
import net.sf.edevtools.lib.baselib.ui.utils.SWTUtils;
import net.sf.edevtools.tools.logviewer.core.model.ILogViewCoreConstants;
import net.sf.edevtools.tools.logviewer.core.parsers.ILogParser;
import net.sf.edevtools.tools.logviewer.core.preferences.ILogViewerPrefs;
import net.sf.edevtools.tools.logviewer.core.sources.ILogSource;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;

/**
 * TODO comment
 * 
 * @author Christoph Graupner
 * @since 0.1.0
 * @version 0.1.0
 */
public class PageLogSource extends WizardPage {

    public static final String NAME = PageLogSource.class.getName();

    private static final int FORMAT_XML = 0;

    protected int fSourceType = ILogSource.TYPE_SOCKET;

    private String fSourceLogFrameworkID;

    private String fSourceLogFormatID;

    private Combo fCmbType;

    private Combo fCmbLogFormat;

    private Combo fCmbLogFramework;

    String[] fDataLogTypeNames = new String[] { "Socket", "File" };

    int[] fDataLogTypeIDs = new int[] { ILogSource.TYPE_SOCKET, ILogSource.TYPE_FILE };

    String[] fDataLogFormat = new String[] { "XML" };

    String[] fDataLogFrameworkNames = new String[] { "Java Util Logging (JDK1.4)" };

    String[] fDataLogFrameworkIDs = new String[] { ILogViewCoreConstants.LOGFRAMEWORK_JUTIL };

    private Button fBtnAutostart;

    protected boolean fSourceAutoStart = false;

    private boolean fClearLog;

    private Button fBtnClearLog;

    protected PageLogSource() {
        super(NAME, "Select Log Input Source", null);
        setSourceLogFormatID(null);
        setSourceLogFrameworkID(null);
        setSourceType(-1);
    }

    public void createControl(Composite aParent) {
        Composite lComp = new Composite(aParent, SWT.NONE);
        lComp.setLayout(new GridLayout(1, false));
        Group grp = SWTUtils.createGroup(lComp, "Log Input Source", 2);
        Label lLbl = new Label(grp, SWT.NONE);
        lLbl.setText("Type:");
        fCmbType = new Combo(grp, SWT.READ_ONLY | SWT.BORDER);
        fCmbType.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        grp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        grp = SWTUtils.createGroup(lComp, "Logger Format", 2);
        grp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        lLbl = new Label(grp, SWT.NONE);
        lLbl.setText("Log Framework:");
        fCmbLogFramework = new Combo(grp, SWT.READ_ONLY | SWT.BORDER);
        fCmbLogFramework.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        lLbl = new Label(grp, SWT.NONE);
        lLbl.setText("Log Format:");
        fCmbLogFormat = new Combo(grp, SWT.READ_ONLY | SWT.BORDER);
        fCmbLogFormat.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fBtnAutostart = new Button(lComp, SWT.CHECK);
        fBtnAutostart.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fBtnAutostart.setText("Auto start source");
        fBtnAutostart.setToolTipText("Auto start the source after finishing this wizard.");
        fBtnClearLog = new Button(lComp, SWT.CHECK);
        fBtnClearLog.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        fBtnClearLog.setText("Clear log view table");
        fBtnClearLog.setToolTipText("Clear the log view table after finishing this wizard.");
        fillComboFramework(fCmbLogFramework);
        fillComboType(fCmbType);
        fillComboFormat(fCmbLogFormat);
        initializeValues();
        hookListeners();
        setControl(lComp);
        checkState();
    }

    @Override
    public IWizardPage getNextPage() {
        switch(fSourceType) {
            case ILogSource.TYPE_SOCKET:
                return getWizard().getPage(PageLogSourceSocket.NAME);
            case ILogSource.TYPE_FILE:
                return getWizard().getPage(PageLogSourceFile.NAME);
        }
        return super.getNextPage();
    }

    public String getSourceLogFormatID() {
        return fSourceLogFormatID;
    }

    public String getSourceLogFrameworkID() {
        return fSourceLogFrameworkID;
    }

    public int getSourceType() {
        return fSourceType;
    }

    public boolean isClearLog() {
        return fClearLog;
    }

    /**
	 * @return the sourceAutoStart
	 */
    public boolean isSourceAutoStart() {
        return fSourceAutoStart;
    }

    /**
	 * @param aClearLog
	 *          the clearLog to set
	 */
    public void setClearLog(boolean aClearLog) {
        fClearLog = aClearLog;
    }

    /**
	 * @param aSourceAutoStart
	 *          the sourceAutoStart to set
	 */
    public void setSourceAutoStart(boolean aSourceAutoStart) {
        fSourceAutoStart = aSourceAutoStart;
    }

    /**
	 * @param aSourceLogFormatID
	 *          the sourceLogFormatID to set
	 */
    public void setSourceLogFormatID(String aSourceLogFormatID) {
        if (aSourceLogFormatID == null) fSourceLogFormatID = ILogViewerPrefs.INSTANCE.getDefaultLogFormatID(fSourceLogFrameworkID); else fSourceLogFormatID = aSourceLogFormatID;
    }

    /**
	 * @param aSourceLogFrameworkID
	 *          the sourceLogFrameworkID to set
	 */
    public void setSourceLogFrameworkID(String aSourceLogFrameworkID) {
        if (aSourceLogFrameworkID == null) fSourceLogFrameworkID = ILogViewerPrefs.INSTANCE.getDefaultLogFrameworkID(); else fSourceLogFrameworkID = aSourceLogFrameworkID;
    }

    /**
	 * @param aSourceType
	 *          the sourceType to set
	 */
    public void setSourceType(int aSourceType) {
        if (aSourceType < 0) fSourceType = ILogViewerPrefs.INSTANCE.getDefaultLogSourceType(); else fSourceType = aSourceType;
    }

    protected void checkState() {
        if (fCmbType.getSelectionIndex() < 0) {
            setErrorMessage("Select a Log Source Type.");
            setPageComplete(false);
            return;
        }
        fSourceType = getUISourceType();
        if (fCmbLogFramework.getSelectionIndex() < 0) {
            setErrorMessage("Select a Logging Framework.");
            setPageComplete(false);
            return;
        }
        fSourceLogFrameworkID = getUISourceLogFrameworkID();
        if (fCmbType.getSelectionIndex() < 0) {
            setErrorMessage("Select a Log Format.");
            setPageComplete(false);
            return;
        }
        fSourceLogFormatID = getUISourceLogFormatID();
        setErrorMessage(null);
        setPageComplete(true);
    }

    protected void initializeValues() {
        int lBinarySearch = Arrays.binarySearch(fDataLogTypeIDs, getSourceType());
        fCmbType.select(lBinarySearch < 0 ? 0 : lBinarySearch);
        lBinarySearch = Arrays.binarySearch(fDataLogFrameworkIDs, getSourceLogFrameworkID());
        fCmbLogFramework.select(lBinarySearch < 0 ? 0 : lBinarySearch);
        lBinarySearch = 0;
        fCmbLogFormat.select(lBinarySearch < 0 ? 0 : lBinarySearch);
        fBtnAutostart.setSelection(fSourceAutoStart);
        fBtnClearLog.setSelection(fClearLog);
    }

    private void fillComboFormat(Combo aCombo) {
        aCombo.removeAll();
        for (String lString : fDataLogFormat) {
            aCombo.add(lString);
        }
    }

    private void fillComboFramework(Combo aCombo) {
        aCombo.removeAll();
        for (String lString : fDataLogFrameworkNames) {
            aCombo.add(lString);
        }
    }

    private void fillComboType(Combo aCombo) {
        aCombo.removeAll();
        for (String lString : fDataLogTypeNames) {
            aCombo.add(lString);
        }
    }

    private String getUISourceLogFormatID() {
        int sel = fCmbLogFormat.getSelectionIndex();
        if (ILogSource.LOGFRAMEWORK_JUTIL.equals(getSourceLogFrameworkID())) {
            switch(sel) {
                case FORMAT_XML:
                    return ILogParser.FORMAT_JUTIL_XML;
                default:
                    break;
            }
        } else if (ILogSource.LOGFRAMEWORK_LOG4J.equals(getSourceLogFrameworkID())) {
            switch(sel) {
                case FORMAT_XML:
                    return ILogParser.FORMAT_LOG4J_XML;
                default:
                    break;
            }
        }
        return null;
    }

    private String getUISourceLogFrameworkID() {
        int sel = fCmbLogFramework.getSelectionIndex();
        if (sel < 0) return null;
        return fDataLogFrameworkIDs[sel];
    }

    private int getUISourceType() {
        int sel = fCmbType.getSelectionIndex();
        if (sel < 0) return -1;
        return fDataLogTypeIDs[sel];
    }

    private void hookListeners() {
        ModifyListener lListener = new ModifyListener() {

            public void modifyText(ModifyEvent aE) {
                checkState();
            }
        };
        fCmbLogFormat.addModifyListener(lListener);
        fCmbLogFramework.addModifyListener(lListener);
        fCmbType.addModifyListener(lListener);
        fBtnAutostart.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent aE) {
                fSourceAutoStart = fBtnAutostart.getSelection();
            }
        });
        fBtnClearLog.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent aE) {
                fClearLog = fBtnClearLog.getSelection();
            }
        });
    }
}
