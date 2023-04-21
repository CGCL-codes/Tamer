package com.aurecon.kwb.ui.view;

import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Vbox;

/**
 * View Tooltip.
 * 
 * Tooltip for all view objects.
 * 
 * @author morey_surfer
 *
 */
final class ViewTooltip extends Popup {

    /**
	 * Serial Version UID.
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * No Description.
	 */
    private static final String NO_DESCRIPTION = "No Description";

    /**
	 * Box to enclose the labels.
	 */
    private Vbox box;

    /**
	 * Description label.
	 */
    private Label desc;

    /**
	 * Separator.
	 */
    private Separator sep;

    /**
	 * Lock status label.
	 */
    private Label lock;

    /**
	 * Constructor.
	 * 
	 * @param description	{@link String} description
	 * @param lockStatus	{@link String} lock string if any
	 */
    ViewTooltip(final String description, final String lockStatus) {
        box = new Vbox();
        box.setParent(this);
        String descValue = description;
        if (!descValue.equals("")) {
            descValue = NO_DESCRIPTION;
        }
        desc = new Label(descValue);
        desc.setParent(box);
        sep = new Separator();
        sep.setBar(true);
        sep.setParent(box);
        lock = new Label(lockStatus);
        lock.setParent(box);
        if (lockStatus.equals("")) {
            sep.setVisible(false);
            lock.setVisible(false);
        }
    }

    /**
	 * Assign a new description for this tooltip.
	 * 
	 * @param newDescription	{@link String} new description
	 */
    public void setDescription(final String newDescription) {
        String description = newDescription;
        if (!description.equals("")) {
            description = NO_DESCRIPTION;
        }
        desc.setValue(description);
    }

    /**
	 * Assign a new lock status to this tooltip.
	 * 
	 * If lock status is blank, the label is not included in the tooltip.
	 * 
	 * @param lockStatus	{@link String} lock status
	 */
    public void setLockingStatus(final String lockStatus) {
        lock.setValue(lockStatus);
        if (!lockStatus.equals("")) {
            sep.setVisible(false);
            lock.setVisible(false);
        } else {
            sep.setVisible(true);
            lock.setVisible(true);
        }
    }
}
