package com.limegroup.gnutella.gui.options.panes;

import javax.swing.JCheckBox;
import org.limewire.i18n.I18nMarker;
import com.limegroup.gnutella.gui.I18n;
import com.limegroup.gnutella.gui.LabeledComponent;
import com.limegroup.gnutella.settings.SharingSettings;

public final class AutoClearUploadsPaneItem extends AbstractPaneItem {

    public static final String TITLE = I18n.tr("Clear Uploads");

    public static final String LABEL = I18n.tr("You can choose whether or not to automatically clear uploads that have completed.");

    /**
	 * Constant for the key of the locale-specific <code>String</code> for the 
	 * upload pane check box label in the options window.
	 */
    private final String CHECK_BOX_LABEL = I18nMarker.marktr("Automatically Clear Completed Uploads:");

    /**
	 * Constant for the check box that specifies whether or not uploads 
	 * should be automatically cleared.
	 */
    private final JCheckBox CHECK_BOX = new JCheckBox();

    /**
	 * The stored value to allow rolling back changes.
	 */
    private boolean _clearUploads;

    /**
	 * The constructor constructs all of the elements of this
	 * <tt>AbstractPaneItem</tt>.
	 * 
	 * @param key the key for this <tt>AbstractPaneItem</tt> that the
	 *        superclass uses to generate locale-specific keys
	 */
    public AutoClearUploadsPaneItem() {
        super(TITLE, LABEL);
        LabeledComponent comp = new LabeledComponent(CHECK_BOX_LABEL, CHECK_BOX, LabeledComponent.LEFT_GLUE, LabeledComponent.LEFT);
        add(comp.getComponent());
    }

    /**
	 * Defines the abstract method in <tt>AbstractPaneItem</tt>.<p>
	 *
	 * Sets the options for the fields in this <tt>PaneItem</tt> when the 
	 * window is shown.
	 */
    public void initOptions() {
        _clearUploads = SharingSettings.CLEAR_UPLOAD.getValue();
        CHECK_BOX.setSelected(_clearUploads);
    }

    /**
	 * Defines the abstract method in <tt>AbstractPaneItem</tt>.<p>
	 *
	 * Applies the options currently set in this window.
	 */
    public boolean applyOptions() {
        final boolean clearUploads = CHECK_BOX.isSelected();
        if (clearUploads != _clearUploads) {
            SharingSettings.CLEAR_UPLOAD.setValue(clearUploads);
            _clearUploads = clearUploads;
        }
        return false;
    }

    public boolean isDirty() {
        return SharingSettings.CLEAR_UPLOAD.getValue() != CHECK_BOX.isSelected();
    }
}
