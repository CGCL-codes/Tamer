package com.dukesoftware.ongakumusou.data.element;

import com.dukesoftware.ongakumusou.gui.main.IntegratedController;

/**
 * 
 * <p></p>
 * 
 * <h5>update history</h5>
 * <p>2007/12/01 File is created. </p>
 * 
 * @author 
 * @since 2007/12/01 15:46:36
 * @version last update 2007/12/01
 */
@SuppressWarnings("serial")
public class URLGroup extends ElementGroup {

    public URLGroup(String key, String title, IntegratedController controller) {
        super(key, title, controller);
    }

    @Override
    protected Element createElement(String elemKey, String elemTitle) {
        return new URLElement(elemKey, elemTitle, this);
    }

    @Override
    public String getType() {
        return "";
    }
}
