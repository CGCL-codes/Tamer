package org.mcisb.ui.io;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import org.mcisb.ui.util.*;
import org.mcisb.ui.wizard.*;
import org.mcisb.util.*;
import org.mcisb.util.task.*;

/**
 * 
 * @author Neil Swainston
 */
public class FileMergeWizard extends Wizard {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 * @param parent
	 * @param bean
	 * @param task
	 */
    public FileMergeWizard(final Component parent, final GenericBean bean, final GenericBeanTask task) {
        super(bean, task);
        final ResourceBundle resourceBundle = ResourceBundle.getBundle("org.mcisb.ui.io.messages");
        final JFileChooser fileChooser = new JFileChooser();
        addWizardComponent(new FileChooserWizardComponent(bean, new FileChooserPanel(parent, resourceBundle.getString("FileMergeWizard.importTitle"), ParameterPanel.DEFAULT_COLUMNS, fileChooser, true, true, false, JFileChooser.FILES_ONLY), org.mcisb.util.PropertyNames.IMPORT_FILEPATHS));
        addWizardComponent(new FileChooserWizardComponent(bean, new FileChooserPanel(parent, resourceBundle.getString("FileMergeWizard.exportTitle"), ParameterPanel.DEFAULT_COLUMNS, fileChooser, false, false, false, JFileChooser.FILES_ONLY), org.mcisb.util.PropertyNames.EXPORT_FILEPATHS));
        init();
    }
}
