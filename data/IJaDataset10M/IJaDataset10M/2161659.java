package com.ibm.celldt.managedbuilder.gnu.core.preferences;

import java.io.File;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;
import com.ibm.celldt.preferences.core.StringFieldEditorPreferenceSearcher;
import com.ibm.celldt.preferences.core.SearcherMessages;
import com.ibm.celldt.utils.linux.findutils.Find;
import com.ibm.celldt.utils.linux.which.Which;
import com.ibm.celldt.utils.packagemanager.PackageManagementSystemManager;
import com.ibm.celldt.utils.searcher.SearchFailedException;

/**
 * @author laggarcia
 * @since 3.0.0
 */
public class GnuToolsSearcher extends StringFieldEditorPreferenceSearcher {

    public GnuToolsSearcher(StringFieldEditor stringFieldEditor, Composite parent) {
        super(stringFieldEditor, parent);
    }

    protected void fastSearch() throws SearchFailedException {
        String currentPreferenceValue = this.stringFieldEditor.getStringValue();
        if (isValidGNUToolsDirectory(currentPreferenceValue)) {
            return;
        }
        String defaultPreferenceValue = this.preferenceStore.getDefaultString(this.preferenceName);
        if ((!currentPreferenceValue.equals(defaultPreferenceValue)) && (isValidGNUToolsDirectory(defaultPreferenceValue))) {
            setText(defaultPreferenceValue);
            return;
        }
        String gnuToolsPath;
        if (Platform.getOS().equals(Platform.OS_LINUX)) {
            gnuToolsPath = new Path(Which.which(GnuToolsProperties.ppugccExecutable)).removeLastSegments(1).toOSString();
            if (isValidGNUToolsDirectory(gnuToolsPath)) {
                setText(gnuToolsPath);
                return;
            }
        }
        gnuToolsPath = PackageManagementSystemManager.getPackageManager().searchFileInQueryListAndReturnInitialPathSegments(GnuToolsProperties.ppugccPackage, File.separator + GnuToolsProperties.ppugccExecutable);
        if (isValidGNUToolsDirectory(gnuToolsPath)) {
            setText(gnuToolsPath);
            return;
        }
        throw new SearchFailedException(SearcherMessages.fastSearchFailedMessage);
    }

    protected void longSearch() {
        new Thread() {

            public void run() {
                if (Platform.getOS().equals(Platform.OS_LINUX)) {
                    String systemsim_cellPath = new Path(Find.findFile(GnuToolsProperties.ppugccSearchRootDirectory, GnuToolsProperties.ppugccExecutable)).removeLastSegments(1).toOSString();
                    if (isValidGNUToolsDirectory(systemsim_cellPath)) {
                        setText(systemsim_cellPath);
                        showInfoMessage(SearcherMessages.searchSucceededDialogTitle, NLS.bind(SearcherMessages.searchSucceededDialogMessage, GnuToolsProperties.gnuToolsPathLabel));
                        return;
                    }
                    showInfoMessage(SearcherMessages.searchFailedDialogTitle, NLS.bind(SearcherMessages.searchFailedDialogMessage, GnuToolsProperties.gnuToolsPathLabel));
                }
            }
        }.start();
    }

    protected boolean isValidGNUToolsDirectory(String directoryPath) {
        if (directoryPath != null) {
            File gnuToolsDirectory = new File(directoryPath);
            if (gnuToolsDirectory.exists() && gnuToolsDirectory.isDirectory()) {
                String[] gnuExecutables = GnuToolsProperties.gnuTools.split(GnuToolsProperties.separator);
                for (int i = 0; i < gnuExecutables.length; i++) {
                    File gnuExecutable = new File(gnuToolsDirectory, gnuExecutables[i]);
                    if (!(gnuExecutable.exists() && gnuExecutable.isFile())) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
