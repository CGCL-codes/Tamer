package org.fudaa.fudaa.crue.study.actions;

import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.fudaa.fudaa.crue.common.helper.DialogHelper;
import org.openide.util.NbBundle;

/**
 *
 * @author deniger ( genesis)
 */
public class OverwriteFileHelper {

    public enum OverwriteResult {

        DO_OVERWRITE, DONT, CANCEL
    }

    public static OverwriteResult confirmOverwriteFiles(List<String> overwrittenFiles) {
        if (!overwrittenFiles.isEmpty()) {
            String[] options = new String[] { NbBundle.getMessage(OverwriteFileHelper.class, "ExistingFile.DontOverwriteAction"), NbBundle.getMessage(OverwriteFileHelper.class, "ExistingFile.OverwriteAction"), NbBundle.getMessage(OverwriteFileHelper.class, "Action.Cancel") };
            final String title = NbBundle.getMessage(OverwriteFileHelper.class, "RenameAction.PhysicalFilesExists");
            String msg = "<li>" + StringUtils.join(overwrittenFiles, "</li><li>") + "</li>";
            final String message = NbBundle.getMessage(OverwriteFileHelper.class, "RenameAction.PhysicalFilesExists.DialogMessage", msg);
            Object showQuestion = DialogHelper.showQuestion(title, message, options);
            if (options[0].equals(showQuestion)) {
                return OverwriteResult.DONT;
            }
            if (options[1].equals(showQuestion)) {
                return OverwriteResult.DO_OVERWRITE;
            }
            return OverwriteResult.CANCEL;
        }
        return OverwriteResult.DONT;
    }
}
