package org.imogene.tools.encrypter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import org.imogene.encryption.EncryptionManager;

public class View extends ViewPart {

    public static final String ID = "MedooIdEncrypter.view";

    private Button selectEncrypt;

    private Button crypt;

    private Text pathTextCrypt;

    private Button selectDecrypt;

    private Button decrypt;

    private Text pathTextDecrypt;

    private Text idText;

    /**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
    public void createPartControl(Composite parent) {
        parent.setLayout(new GridLayout());
        encrypterGroup(parent);
        createDecryptGroup(parent);
        createGeneratorGroup(parent);
    }

    /**
	 * 
	 * @param parent
	 */
    private void encrypterGroup(Composite parent) {
        Group group = new Group(parent, SWT.NONE);
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        group.setText("Encrypter");
        group.setLayout(new GridLayout(3, false));
        Label help = new Label(group, SWT.NONE);
        help.setText("Select the file to encrypt, and press 'crypt'");
        GridData helpData = new GridData(GridData.FILL_HORIZONTAL);
        helpData.horizontalSpan = 3;
        help.setLayoutData(helpData);
        pathTextCrypt = new Text(group, SWT.BORDER);
        pathTextCrypt.setEditable(false);
        pathTextCrypt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        selectEncrypt = new Button(group, SWT.PUSH);
        selectEncrypt.setText("Select");
        selectEncrypt.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
                fd.setFilterExtensions(new String[] { "*.*" });
                String path = fd.open();
                if (path != null) {
                    pathTextCrypt.setText(path);
                    crypt.setEnabled(true);
                }
            }
        });
        crypt = new Button(group, SWT.PUSH);
        crypt.setText("Crypt");
        crypt.setEnabled(false);
        crypt.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                secureCopy(pathTextCrypt.getText());
                pathTextCrypt.setText("");
                crypt.setEnabled(false);
            }
        });
    }

    /**
	 * create the group that permits to decrypt a file.
	 * @param parent the parent composite.
	 */
    private void createDecryptGroup(Composite parent) {
        Group group = new Group(parent, SWT.NONE);
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        group.setText("Decrypter");
        group.setLayout(new GridLayout(3, false));
        Label help = new Label(group, SWT.NONE);
        help.setText("Select the file to decrypt, and press 'decrypt'");
        GridData helpData = new GridData(GridData.FILL_HORIZONTAL);
        helpData.horizontalSpan = 3;
        help.setLayoutData(helpData);
        pathTextDecrypt = new Text(group, SWT.BORDER);
        pathTextDecrypt.setEditable(false);
        pathTextDecrypt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        selectDecrypt = new Button(group, SWT.PUSH);
        selectDecrypt.setText("Select");
        selectDecrypt.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                FileDialog fd = new FileDialog(Display.getCurrent().getActiveShell(), SWT.OPEN);
                fd.setFilterExtensions(new String[] { "*.*" });
                String path = fd.open();
                if (path != null) {
                    pathTextDecrypt.setText(path);
                    decrypt.setEnabled(true);
                }
            }
        });
        decrypt = new Button(group, SWT.PUSH);
        decrypt.setText("Decrypt");
        decrypt.setEnabled(false);
        decrypt.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                unsecureCopy(pathTextDecrypt.getText());
                pathTextDecrypt.setText("");
                decrypt.setEnabled(false);
            }
        });
    }

    /**
	 * create the group that permits to generate an id.
	 * @param parent the parent composite
	 */
    private void createGeneratorGroup(Composite parent) {
        Group group = new Group(parent, SWT.NONE);
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        group.setText("Generator");
        group.setLayout(new GridLayout(2, false));
        idText = new Text(group, SWT.BORDER);
        idText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        idText.setEditable(true);
        Button generate = new Button(group, SWT.PUSH);
        generate.setText("Generate");
        generate.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                UUID id = UUID.randomUUID();
                idText.setText(id.toString());
            }
        });
    }

    /**
	 * Passing the focus request to the viewer's control.
	 */
    public void setFocus() {
        selectEncrypt.setFocus();
    }

    /**
	 * 
	 * @param unsecurePath
	 */
    private void secureCopy(String unsecurePath) {
        File src = new File(unsecurePath);
        File dest = new File(unsecurePath + ".imogid");
        encryptfile(src, dest);
    }

    /**
	 * 
	 * @param unsecurePath
	 */
    private void unsecureCopy(String unsecurePath) {
        File src = new File(unsecurePath);
        File dest = new File(unsecurePath + ".txt");
        decryptfile(src, dest);
    }

    /**
	 * Copy files.
	 * @param f1 source file
	 * @param f2 destination file
	 */
    public static void encryptfile(File f1, File f2) {
        try {
            InputStream in = new FileInputStream(f1);
            OutputStream out = EncryptionManager.getInstance().getEncryptedOutputStream(new FileOutputStream(f2));
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
	 * Copy files.
	 * @param f1 source file
	 * @param f2 destination file
	 */
    public static void decryptfile(File f1, File f2) {
        try {
            InputStream in = EncryptionManager.getInstance().getDecryptedInputStream(new FileInputStream(f1));
            OutputStream out = new FileOutputStream(f2);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
