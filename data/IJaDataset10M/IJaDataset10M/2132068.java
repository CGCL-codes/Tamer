package com.diotalevi.ubernotes.plugins.core;

import com.diotalevi.ubernotes.data.InternalLink;
import com.diotalevi.ubernotes.data.Page;
import com.diotalevi.ubernotes.plugins.api.GenericPlugin;
import com.diotalevi.ubernotes.plugins.core.gui.ChooseFile;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Vector;
import javax.swing.text.StyledEditorKit;

/**
 *
 * @author Filippo Diotalevi
 */
public class AddLinkToPagePlugin extends GenericPlugin implements UsingFileChooser {

    final ChooseFile view = new ChooseFile(this);

    @Override
    public String getVersion() {
        return "0.0.1";
    }

    @Override
    public void execute() {
        view.setVisible(true);
    }

    public Vector<String> getPagesAvailable(String criteria) {
        List<String> all = getUbernotesManager().getStorage().getAllPageNames();
        Vector<String> filtered = new Vector<String>();
        for (String name : all) {
            if (isEmpty(criteria) || name.toLowerCase().indexOf(criteria.toLowerCase()) > -1) filtered.add(name);
        }
        return filtered;
    }

    private boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    @Override
    public String getName() {
        return "ubernotes.core.AddLinkToPagePlugin";
    }

    public void chooseFile(String fileName) {
    }
}
