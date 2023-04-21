package net.cucup.sample.tomahawk.misc;

import java.io.Serializable;

/**
 * @author Sylvain Vieujot (latest modification by $Author: grantsmith $)
 * @version $Revision: 472610 $ $Date: 2006-11-08 14:46:34 -0500 (mi鑼� 08 nov 2006) $
 */
public class TestCheckBox implements Serializable {

    private boolean checked;

    private String text;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
