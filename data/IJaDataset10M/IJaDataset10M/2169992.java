package net.sourceforge.obexftpfrontend.gui;

import java.text.DateFormat;
import java.util.Locale;
import net.sourceforge.obexftpfrontend.model.OBEXElement;
import net.sourceforge.obexftpfrontend.util.StringUtils;
import org.apache.log4j.Logger;

/**
 * FilePropertiesDialog UI helper class.
 * @author Daniel F. Martins
 */
public class FilePropertiesDialogHelper extends AbstractUIHelper<FilePropertiesDialog> {

    private static final Logger log = Logger.getLogger(FilePropertiesDialogHelper.class);

    private DateFormat format;

    /**
     * Create a new instance of FilePropertiesDialogHelper.
     * @param dialog FilePropertiesDialog window.
     */
    public FilePropertiesDialogHelper(FilePropertiesDialog dialog) {
        super(dialog);
        format = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, Locale.ENGLISH);
    }

    @Override
    public void prepareWindow() {
        log.debug("Restoring the initial dialog state before show the dialog to the user");
        window.getTabbedPane().setSelectedIndex(0);
        window.getRootPane().setDefaultButton(window.getOkButton());
    }

    /**
     * Method to set the OBEXElement which you want to display
     * in this frame.
     * @param file OBEXElement to be displayed.
     */
    public void setFile(OBEXElement file) {
        if (file == null) {
            return;
        }
        log.debug("Updating displayed information");
        setFileName(file);
        setLocation(file);
        setSize(file);
        setCreated(file);
        setModified(file);
        setAccessed(file);
        setOwner(file);
        setGroup(file);
        setOther(file);
    }

    /**
     * Extract the file name from the given OBEXElement object.
     * @param file OBEXElement object.
     */
    private void setFileName(OBEXElement file) {
        window.getDescriptionLabel().setText("<html>Properties of <strong>" + file.getName() + "</strong> ...</html>");
    }

    /**
     * Extract the file location from the given OBEXElement object.
     * @param file OBEXElement object.
     */
    private void setLocation(OBEXElement file) {
        if (file == null) {
            return;
        }
        String[] path = file.getStringPath();
        String stringPath = "";
        for (int i = 1; i < path.length; i++) {
            stringPath += "/" + path[i];
        }
        window.getLocationTextField().setText(stringPath);
    }

    /**
     * Extract the file size from the given OBEXElement object.
     * @param file OBEXElement object.
     */
    private void setSize(OBEXElement file) {
        if (file == null) {
            return;
        }
        if (file.getType() == OBEXElement.OBEXElementType.FOLDER) {
            window.getSizeTextField().setText("-");
            return;
        }
        boolean smallFile = file.getSize() < 1024;
        window.getSizeTextField().setText((smallFile ? "" : StringUtils.getCompactFileSize(file.getSize()) + " (") + file.getSize() + " bytes" + (smallFile ? "" : ")"));
    }

    /**
     * Extract the file creation date from the given OBEXElement object.
     * @param file OBEXElement object.
     */
    private void setCreated(OBEXElement file) {
        try {
            window.getCreatedTextField().setText("-");
            window.getCreatedTextField().setText(format.format(file.getCreated()));
        } catch (Exception exc) {
            log.warn("Cannot set the formatted creation date", exc);
        }
    }

    /**
     * Extract the file modification date from the given OBEXElement object.
     * @param file OBEXElement object.
     */
    private void setModified(OBEXElement file) {
        try {
            window.getModifiedTextField().setText("-");
            window.getModifiedTextField().setText(format.format(file.getModified()));
        } catch (Exception exc) {
            log.warn("Cannot set the formatted modified date", exc);
        }
    }

    /**
     * Extract the file last access date from the given OBEXElement object.
     * @param file OBEXElement object.
     */
    private void setAccessed(OBEXElement file) {
        try {
            window.getAccessedTextField().setText("-");
            window.getAccessedTextField().setText(format.format(file.getAccessed()));
        } catch (Exception exc) {
            log.warn("Cannot set the formatted last accessed date", exc);
        }
    }

    /**
     * Extract the owner permissions from the given OBEXElement object.
     * @param file OBEXElement object.
     */
    private void setOwner(OBEXElement file) {
        window.getOwnerTextField().setText("-");
        window.getOwnerPermTextField().setText("-");
        if (!"".equals(file.getOwner())) {
            window.getOwnerTextField().setText(file.getOwner());
        }
        if (!"".equals(file.getOwnerPerm())) {
            window.getOwnerPermTextField().setText(file.getOwnerPerm());
        }
    }

    /**
     * Extract the group permissions from the given OBEXElement object.
     * @param file OBEXElement object.
     */
    private void setGroup(OBEXElement file) {
        window.getGroupTextField().setText("-");
        window.getGroupPermTextField().setText("-");
        if (!"".equals(file.getGroup())) {
            window.getGroupTextField().setText(file.getGroup());
        }
        if (!"".equals(file.getGroupPerm())) {
            window.getGroupPermTextField().setText(file.getGroupPerm());
        }
    }

    /**
     * Extract other permissions from the given OBEXElement object.
     * @param file OBEXElement object.
     */
    private void setOther(OBEXElement file) {
        window.getOtherPermTextField().setText("-");
        if (!"".equals(file.getOtherPerm())) {
            window.getOtherPermTextField().setText(file.getOtherPerm());
        }
    }
}
