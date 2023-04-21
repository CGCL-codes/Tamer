package com.dukesoftware.ongakumusou.search.mode;

import javax.swing.JTextField;
import com.dukesoftware.ongakumusou.gui.main.IntegratedController;

/**
 * 
 * <p></p>
 *
 * <h5>update history</h5> 
 * <p>2007/10/03 This file was created.</p>
 *
 * @author 
 * @since 2007/10/03
 * @version last update 2007/10/03
 */
public class SnapshotSearch extends SearchMode {

    public SnapshotSearch(JTextField textField, IntegratedController controller) {
        super(textField, controller);
    }

    @Override
    public String modeName() {
        return "Snapshot";
    }

    @Override
    public void search() {
        controller.searchSnapshot(getText());
    }
}
