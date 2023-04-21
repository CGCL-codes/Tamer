package ti.plato.components.ui.oscript.internal.file;

import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import ti.mcore.u.log.PlatoLogger;
import ti.plato.components.ui.oscript.WorkspaceSaveContainer;

public class NewFileDialog extends Dialog {

    private static final String TOOLTIP_DIRECTORY = "Select directory or enter directory path";

    private static final int LEFT_OFFSET = 6;

    private static final String LABEL_SELECT_DIRECTORY = "Select Directory";

    private static final int LABEL_WIDTH = LABEL_SELECT_DIRECTORY.length() * 5;

    private static final int ROW_SPACING = 18;

    private static final int ROW_SPACING_SHORT = 6;

    private static final String LABEL_FILE_NAME = "File Name";

    private static final String TOOLTIP_FILE_NAME = "Enter new file name";

    private static final String LABEL_NEW_FILE_DIRECTORY_CHOOSER_DIALOG = "Choose directory...";

    private static final String SHELL_TITLE = "Create a new file...";

    private static final int TEXT_WIDTH = 350;

    private static final PlatoLogger LOGGER = PlatoLogger.getLogger(NewFileDialog.class);

    protected static final String OS_EXT = ".os";

    private Text _textDirectoryPath;

    private Label _labelSelectDirectory;

    private Label _labelFileName;

    private Text _textFileName;

    private Button _buttonBrowseDirectory;

    private String _fileNameStr;

    private String _directoryPathStr;

    private KeyListener _keyListener;

    private boolean _isAutoCreateDirectory = true;

    private class FindDirectoryListener implements SelectionListener {

        private Text _text;

        private String _label;

        FindDirectoryListener(String label, Text text) {
            _label = label;
            _text = text;
        }

        public void widgetSelected(SelectionEvent e) {
            DirectoryDialog dialog = new DirectoryDialog(new Shell(), SWT.OPEN | SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE);
            dialog.setText(_label);
            dialog.setFilterPath(getDirectoryPath0());
            String dirName = dialog.open();
            if (dirName != null) {
                _text.setText(dirName);
            }
        }

        public void widgetDefaultSelected(SelectionEvent e) {
        }
    }

    public NewFileDialog(Shell parent) {
        super(new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | SWT.RESIZE));
        _keyListener = new KeyListener() {

            public void keyPressed(KeyEvent e) {
                switch(e.keyCode) {
                    case SWT.CR:
                    case SWT.KEYPAD_CR:
                        okPressed();
                        break;
                    case SWT.CANCEL:
                    case SWT.ESC:
                        cancelPressed();
                        break;
                    default:
                }
            }

            public void keyReleased(KeyEvent e) {
            }
        };
    }

    @Override
    protected Control createContents(Composite parent) {
        addPathChoser(parent);
        Control contents = super.createContents(parent);
        return contents;
    }

    private void addPathChoser(Composite parentComposite) {
        Group composite = new Group((Composite) super.createDialogArea(parentComposite), SWT.RESIZE);
        FormLayout formLayout = new FormLayout();
        formLayout.marginHeight = 20;
        formLayout.marginWidth = 20;
        composite.setLayout(formLayout);
        _labelSelectDirectory = new Label(composite, SWT.PUSH);
        _labelSelectDirectory.setText(LABEL_SELECT_DIRECTORY);
        FormData data = new FormData();
        data.width = LABEL_WIDTH;
        _labelSelectDirectory.setLayoutData(data);
        _labelSelectDirectory.setToolTipText(TOOLTIP_DIRECTORY);
        _textDirectoryPath = new Text(composite, SWT.BORDER | SWT.SINGLE);
        data = getRow(_labelSelectDirectory, TEXT_WIDTH, true);
        _textDirectoryPath.setLayoutData(data);
        _textDirectoryPath.setText(getLastUsedDirectory());
        _textDirectoryPath.setToolTipText(TOOLTIP_DIRECTORY);
        _textDirectoryPath.addKeyListener(_keyListener);
        _buttonBrowseDirectory = new Button(composite, SWT.PUSH);
        _buttonBrowseDirectory.setText("Browse...");
        data = new FormData();
        data.left = new FormAttachment(_textDirectoryPath, LEFT_OFFSET, SWT.RIGHT);
        data.top = new FormAttachment(_textDirectoryPath, 0, SWT.CENTER);
        data.width = TEXT_WIDTH;
        data.width = LABEL_WIDTH;
        _buttonBrowseDirectory.setLayoutData(data);
        _buttonBrowseDirectory.setToolTipText(TOOLTIP_DIRECTORY);
        _buttonBrowseDirectory.addSelectionListener(new FindDirectoryListener(LABEL_NEW_FILE_DIRECTORY_CHOOSER_DIALOG, _textDirectoryPath));
        _labelFileName = new Label(composite, SWT.NONE);
        _labelFileName.setText(LABEL_FILE_NAME);
        data = getRow(_textDirectoryPath, LABEL_WIDTH, false);
        _labelFileName.setLayoutData(data);
        _labelFileName.setToolTipText(TOOLTIP_FILE_NAME);
        _textFileName = new Text(composite, SWT.BORDER | SWT.SINGLE);
        data = getRow(_labelFileName, TEXT_WIDTH, true);
        _textFileName.setLayoutData(data);
        _textFileName.setToolTipText(TOOLTIP_FILE_NAME);
        _textFileName.addKeyListener(_keyListener);
        _textFileName.setFocus();
    }

    private String getLastUsedDirectory() {
        return WorkspaceSaveContainer.getGlobalString(WorkspaceSaveContainer.KEY_NEW_FILE_DIALOG__LAST_USED_DIRECTORY);
    }

    private FormData getRow(Control base, int width, boolean isShortSpacing) {
        if (base == null) {
            LOGGER.logError("base == null", true);
            return null;
        }
        FormData data = new FormData();
        data.width = width;
        data.left = new FormAttachment(base, 0, SWT.LEFT);
        int spacing;
        if (isShortSpacing) {
            spacing = ROW_SPACING_SHORT;
        } else {
            spacing = ROW_SPACING;
        }
        data.top = new FormAttachment(base, spacing, SWT.BOTTOM);
        return data;
    }

    protected void okPressed() {
        _directoryPathStr = getDirectoryPath0();
        _fileNameStr = getFileName0();
        super.okPressed();
    }

    private String getDirectoryPath0() {
        if (_textDirectoryPath != null) {
            return _textDirectoryPath.getText();
        }
        return null;
    }

    private String getFileName0() {
        if (_textFileName == null) {
            return null;
        }
        appendOsExtension();
        return _textFileName.getText();
    }

    private synchronized void appendOsExtension() {
        if (_textFileName == null) {
            return;
        }
        String fileName = _textFileName.getText();
        if (fileName == null || fileName.equals("") || fileName.equals(OS_EXT)) {
            return;
        }
        Path filePath = new Path(fileName);
        String ext = filePath.getFileExtension();
        if (ext != null && !fileName.equals("")) {
            return;
        }
        _textFileName.setText(fileName + OS_EXT);
    }

    public String getDirectoryPath() {
        if (_directoryPathStr != null) {
            WorkspaceSaveContainer.setGlobalString(WorkspaceSaveContainer.KEY_NEW_FILE_DIALOG__LAST_USED_DIRECTORY, _directoryPathStr);
        }
        return _directoryPathStr;
    }

    public String getFileName() {
        return _fileNameStr;
    }

    public boolean isAutoCreateDirectory() {
        return _isAutoCreateDirectory;
    }

    protected void cancelPressed() {
        _directoryPathStr = null;
        _textDirectoryPath = null;
        _fileNameStr = null;
        _textFileName = null;
        _isAutoCreateDirectory = false;
        super.cancelPressed();
    }

    protected void configureShell(Shell shell) {
        super.configureShell(shell);
        shell.setText(SHELL_TITLE);
    }
}
