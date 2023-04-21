package de.mogwai.common.web.backingbean.messagebox;

import de.mogwai.common.web.backingbean.BackingBeanDataModel;

/**
 * Datenmodell f�r eine MessageBox.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-09-04 18:15:54 $
 */
@SuppressWarnings("serial")
public class MessageBoxDataModel extends BackingBeanDataModel {

    private String title;

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
