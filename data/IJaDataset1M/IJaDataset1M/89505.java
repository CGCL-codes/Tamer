package com.android.ide.eclipse.adt.internal.wizards.export;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs.BuildVerbosity;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.jarutils.KeystoreHelper;
import com.android.jarutils.SignedJarBuilder;
import com.android.jarutils.DebugKeyProvider.IKeyGenOutput;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * Export wizard to export an apk signed with a release key/certificate.
 */
public final class ExportWizard extends Wizard implements IExportWizard {

    private static final String PROJECT_LOGO_LARGE = "icons/android_large.png";

    private static final String PAGE_PROJECT_CHECK = "Page_ProjectCheck";

    private static final String PAGE_KEYSTORE_SELECTION = "Page_KeystoreSelection";

    private static final String PAGE_KEY_CREATION = "Page_KeyCreation";

    private static final String PAGE_KEY_SELECTION = "Page_KeySelection";

    private static final String PAGE_KEY_CHECK = "Page_KeyCheck";

    static final String PROPERTY_KEYSTORE = "keystore";

    static final String PROPERTY_ALIAS = "alias";

    static final String PROPERTY_DESTINATION = "destination";

    static final String PROPERTY_FILENAME = "baseFilename";

    static final int APK_FILE_SOURCE = 0;

    static final int APK_FILE_DEST = 1;

    static final int APK_COUNT = 2;

    /**
     * Base page class for the ExportWizard page. This class add the {@link #onShow()} callback.
     */
    abstract static class ExportWizardPage extends WizardPage {

        /** bit mask constant for project data change event */
        protected static final int DATA_PROJECT = 0x001;

        /** bit mask constant for keystore data change event */
        protected static final int DATA_KEYSTORE = 0x002;

        /** bit mask constant for key data change event */
        protected static final int DATA_KEY = 0x004;

        protected static final VerifyListener sPasswordVerifier = new VerifyListener() {

            public void verifyText(VerifyEvent e) {
                int len = e.text.length();
                if (len + ((Text) e.getSource()).getText().length() > 127) {
                    e.doit = false;
                    return;
                }
                for (int i = 0; i < len; i++) {
                    if (e.text.charAt(i) < 32) {
                        e.doit = false;
                        return;
                    }
                }
            }
        };

        /**
         * Bit mask indicating what changed while the page was hidden.
         * @see #DATA_PROJECT
         * @see #DATA_KEYSTORE
         * @see #DATA_KEY
         */
        protected int mProjectDataChanged = 0;

        ExportWizardPage(String name) {
            super(name);
        }

        abstract void onShow();

        @Override
        public void setVisible(boolean visible) {
            super.setVisible(visible);
            if (visible) {
                onShow();
                mProjectDataChanged = 0;
            }
        }

        final void projectDataChanged(int changeMask) {
            mProjectDataChanged |= changeMask;
        }

        /**
         * Calls {@link #setErrorMessage(String)} and {@link #setPageComplete(boolean)} based on a
         * {@link Throwable} object.
         */
        protected void onException(Throwable t) {
            String message = getExceptionMessage(t);
            setErrorMessage(message);
            setPageComplete(false);
        }
    }

    private ExportWizardPage mPages[] = new ExportWizardPage[5];

    private IProject mProject;

    private String mKeystore;

    private String mKeystorePassword;

    private boolean mKeystoreCreationMode;

    private String mKeyAlias;

    private String mKeyPassword;

    private int mValidity;

    private String mDName;

    private PrivateKey mPrivateKey;

    private X509Certificate mCertificate;

    private File mDestinationParentFolder;

    private ExportWizardPage mKeystoreSelectionPage;

    private ExportWizardPage mKeyCreationPage;

    private ExportWizardPage mKeySelectionPage;

    private ExportWizardPage mKeyCheckPage;

    private boolean mKeyCreationMode;

    private List<String> mExistingAliases;

    private Map<String, String[]> mApkMap;

    public ExportWizard() {
        setHelpAvailable(false);
        setWindowTitle("Export Android Application");
        setImageDescriptor();
    }

    @Override
    public void addPages() {
        addPage(mPages[0] = new ProjectCheckPage(this, PAGE_PROJECT_CHECK));
        addPage(mKeystoreSelectionPage = mPages[1] = new KeystoreSelectionPage(this, PAGE_KEYSTORE_SELECTION));
        addPage(mKeyCreationPage = mPages[2] = new KeyCreationPage(this, PAGE_KEY_CREATION));
        addPage(mKeySelectionPage = mPages[3] = new KeySelectionPage(this, PAGE_KEY_SELECTION));
        addPage(mKeyCheckPage = mPages[4] = new KeyCheckPage(this, PAGE_KEY_CHECK));
    }

    @Override
    public boolean performFinish() {
        ProjectHelper.saveStringProperty(mProject, PROPERTY_KEYSTORE, mKeystore);
        ProjectHelper.saveStringProperty(mProject, PROPERTY_ALIAS, mKeyAlias);
        ProjectHelper.saveStringProperty(mProject, PROPERTY_DESTINATION, mDestinationParentFolder.getAbsolutePath());
        ProjectHelper.saveStringProperty(mProject, PROPERTY_FILENAME, mApkMap.get(null)[APK_FILE_DEST]);
        IWorkbench workbench = PlatformUI.getWorkbench();
        final boolean[] result = new boolean[1];
        try {
            workbench.getProgressService().busyCursorWhile(new IRunnableWithProgress() {

                /**
                 * Run the export.
                 * @throws InvocationTargetException
                 * @throws InterruptedException
                 */
                public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                    try {
                        result[0] = doExport(monitor);
                    } finally {
                        monitor.done();
                    }
                }
            });
        } catch (InvocationTargetException e) {
            return false;
        } catch (InterruptedException e) {
            return false;
        }
        return result[0];
    }

    private boolean doExport(IProgressMonitor monitor) {
        try {
            mProject.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);
            if (mKeystoreCreationMode || mKeyCreationMode) {
                final ArrayList<String> output = new ArrayList<String>();
                boolean createdStore = KeystoreHelper.createNewStore(mKeystore, null, mKeystorePassword, mKeyAlias, mKeyPassword, mDName, mValidity, new IKeyGenOutput() {

                    public void err(String message) {
                        output.add(message);
                    }

                    public void out(String message) {
                        output.add(message);
                    }
                });
                if (createdStore == false) {
                    displayError(output.toArray(new String[output.size()]));
                    return false;
                }
                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                FileInputStream fis = new FileInputStream(mKeystore);
                keyStore.load(fis, mKeystorePassword.toCharArray());
                fis.close();
                PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(mKeyAlias, new KeyStore.PasswordProtection(mKeyPassword.toCharArray()));
                if (entry != null) {
                    mPrivateKey = entry.getPrivateKey();
                    mCertificate = (X509Certificate) entry.getCertificate();
                } else {
                    displayError("Could not find key");
                    return false;
                }
            }
            if (mPrivateKey != null && mCertificate != null) {
                IFolder outputIFolder = BaseProjectHelper.getOutputFolder(mProject);
                if (outputIFolder == null) {
                    return false;
                }
                String outputOsPath = outputIFolder.getLocation().toOSString();
                Set<Entry<String, String[]>> set = mApkMap.entrySet();
                boolean runZipAlign = false;
                String path = AdtPlugin.getOsAbsoluteZipAlign();
                File zipalign = new File(path);
                runZipAlign = zipalign.isFile();
                for (Entry<String, String[]> entry : set) {
                    String[] defaultApk = entry.getValue();
                    String srcFilename = defaultApk[APK_FILE_SOURCE];
                    String destFilename = defaultApk[APK_FILE_DEST];
                    File destFile;
                    if (runZipAlign) {
                        destFile = File.createTempFile("android", ".apk");
                    } else {
                        destFile = new File(mDestinationParentFolder, destFilename);
                    }
                    FileOutputStream fos = new FileOutputStream(destFile);
                    SignedJarBuilder builder = new SignedJarBuilder(fos, mPrivateKey, mCertificate);
                    FileInputStream fis = new FileInputStream(new File(outputOsPath, srcFilename));
                    try {
                        builder.writeZip(fis, null);
                        builder.close();
                        if (runZipAlign) {
                            File realDestFile = new File(mDestinationParentFolder, destFilename);
                            String message = zipAlign(path, destFile, realDestFile);
                            if (message != null) {
                                displayError(message);
                                return false;
                            }
                        }
                    } finally {
                        try {
                            fis.close();
                        } finally {
                            fos.close();
                        }
                    }
                }
                if (runZipAlign == false) {
                    AdtPlugin.displayWarning("Export Wizard", "The zipalign tool was not found in the SDK.\n\n" + "Please update to the latest SDK and re-export your application\n" + "or run zipalign manually.\n\n" + "Aligning applications allows Android to use application resources\n" + "more efficiently.");
                }
                return true;
            }
        } catch (Throwable t) {
            displayError(t);
        }
        return false;
    }

    @Override
    public boolean canFinish() {
        return mApkMap != null && mApkMap.size() > 0 && ((mPrivateKey != null && mCertificate != null) || mKeystoreCreationMode || mKeyCreationMode) && mDestinationParentFolder != null;
    }

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        Object selected = selection.getFirstElement();
        if (selected instanceof IProject) {
            mProject = (IProject) selected;
        } else if (selected instanceof IAdaptable) {
            IResource r = (IResource) ((IAdaptable) selected).getAdapter(IResource.class);
            if (r != null) {
                mProject = r.getProject();
            }
        }
    }

    ExportWizardPage getKeystoreSelectionPage() {
        return mKeystoreSelectionPage;
    }

    ExportWizardPage getKeyCreationPage() {
        return mKeyCreationPage;
    }

    ExportWizardPage getKeySelectionPage() {
        return mKeySelectionPage;
    }

    ExportWizardPage getKeyCheckPage() {
        return mKeyCheckPage;
    }

    /**
     * Returns an image descriptor for the wizard logo.
     */
    private void setImageDescriptor() {
        ImageDescriptor desc = AdtPlugin.getImageDescriptor(PROJECT_LOGO_LARGE);
        setDefaultPageImageDescriptor(desc);
    }

    IProject getProject() {
        return mProject;
    }

    void setProject(IProject project) {
        mProject = project;
        updatePageOnChange(ExportWizardPage.DATA_PROJECT);
    }

    void setKeystore(String path) {
        mKeystore = path;
        mPrivateKey = null;
        mCertificate = null;
        updatePageOnChange(ExportWizardPage.DATA_KEYSTORE);
    }

    String getKeystore() {
        return mKeystore;
    }

    void setKeystoreCreationMode(boolean createStore) {
        mKeystoreCreationMode = createStore;
        updatePageOnChange(ExportWizardPage.DATA_KEYSTORE);
    }

    boolean getKeystoreCreationMode() {
        return mKeystoreCreationMode;
    }

    void setKeystorePassword(String password) {
        mKeystorePassword = password;
        mPrivateKey = null;
        mCertificate = null;
        updatePageOnChange(ExportWizardPage.DATA_KEYSTORE);
    }

    String getKeystorePassword() {
        return mKeystorePassword;
    }

    void setKeyCreationMode(boolean createKey) {
        mKeyCreationMode = createKey;
        updatePageOnChange(ExportWizardPage.DATA_KEY);
    }

    boolean getKeyCreationMode() {
        return mKeyCreationMode;
    }

    void setExistingAliases(List<String> aliases) {
        mExistingAliases = aliases;
    }

    List<String> getExistingAliases() {
        return mExistingAliases;
    }

    void setKeyAlias(String name) {
        mKeyAlias = name;
        mPrivateKey = null;
        mCertificate = null;
        updatePageOnChange(ExportWizardPage.DATA_KEY);
    }

    String getKeyAlias() {
        return mKeyAlias;
    }

    void setKeyPassword(String password) {
        mKeyPassword = password;
        mPrivateKey = null;
        mCertificate = null;
        updatePageOnChange(ExportWizardPage.DATA_KEY);
    }

    String getKeyPassword() {
        return mKeyPassword;
    }

    void setValidity(int validity) {
        mValidity = validity;
        updatePageOnChange(ExportWizardPage.DATA_KEY);
    }

    int getValidity() {
        return mValidity;
    }

    void setDName(String dName) {
        mDName = dName;
        updatePageOnChange(ExportWizardPage.DATA_KEY);
    }

    String getDName() {
        return mDName;
    }

    void setSigningInfo(PrivateKey privateKey, X509Certificate certificate) {
        mPrivateKey = privateKey;
        mCertificate = certificate;
    }

    void setDestination(File parentFolder, Map<String, String[]> apkMap) {
        mDestinationParentFolder = parentFolder;
        mApkMap = apkMap;
    }

    void resetDestination() {
        mDestinationParentFolder = null;
        mApkMap = null;
    }

    void updatePageOnChange(int changeMask) {
        for (ExportWizardPage page : mPages) {
            page.projectDataChanged(changeMask);
        }
    }

    private void displayError(String... messages) {
        String message = null;
        if (messages.length == 1) {
            message = messages[0];
        } else {
            StringBuilder sb = new StringBuilder(messages[0]);
            for (int i = 1; i < messages.length; i++) {
                sb.append('\n');
                sb.append(messages[i]);
            }
            message = sb.toString();
        }
        AdtPlugin.displayError("Export Wizard", message);
    }

    private void displayError(Throwable t) {
        String message = getExceptionMessage(t);
        displayError(message);
        AdtPlugin.log(t, "Export Wizard Error");
    }

    /**
     * Executes zipalign
     * @param zipAlignPath location of the zipalign too
     * @param source file to zipalign
     * @param destination where to write the resulting file
     * @return null if success, the error otherwise
     * @throws IOException
     */
    private String zipAlign(String zipAlignPath, File source, File destination) throws IOException {
        String[] command = new String[5];
        command[0] = zipAlignPath;
        command[1] = "-f";
        command[2] = "4";
        command[3] = source.getAbsolutePath();
        command[4] = destination.getAbsolutePath();
        Process process = Runtime.getRuntime().exec(command);
        ArrayList<String> output = new ArrayList<String>();
        try {
            if (grabProcessOutput(process, output) != 0) {
                StringBuilder sb = new StringBuilder("Error while running zipalign:");
                for (String msg : output) {
                    sb.append('\n');
                    sb.append(msg);
                }
                return sb.toString();
            }
        } catch (InterruptedException e) {
        }
        return null;
    }

    /**
     * Get the stderr output of a process and return when the process is done.
     * @param process The process to get the ouput from
     * @param results The array to store the stderr output
     * @return the process return code.
     * @throws InterruptedException
     */
    private final int grabProcessOutput(final Process process, final ArrayList<String> results) throws InterruptedException {
        new Thread("") {

            @Override
            public void run() {
                InputStreamReader is = new InputStreamReader(process.getErrorStream());
                BufferedReader errReader = new BufferedReader(is);
                try {
                    while (true) {
                        String line = errReader.readLine();
                        if (line != null) {
                            results.add(line);
                        } else {
                            break;
                        }
                    }
                } catch (IOException e) {
                }
            }
        }.start();
        new Thread("") {

            @Override
            public void run() {
                InputStreamReader is = new InputStreamReader(process.getInputStream());
                BufferedReader outReader = new BufferedReader(is);
                IProject project = getProject();
                try {
                    while (true) {
                        String line = outReader.readLine();
                        if (line != null) {
                            AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, project, line);
                        } else {
                            break;
                        }
                    }
                } catch (IOException e) {
                }
            }
        }.start();
        return process.waitFor();
    }

    /**
     * Returns the {@link Throwable#getMessage()}. If the {@link Throwable#getMessage()} returns
     * <code>null</code>, the method is called again on the cause of the Throwable object.
     * <p/>If no Throwable in the chain has a valid message, the canonical name of the first
     * exception is returned.
     */
    static String getExceptionMessage(Throwable t) {
        String message = t.getMessage();
        if (message == null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            t.printStackTrace(new PrintStream(baos));
            message = baos.toString();
        }
        return message;
    }
}
