package org.javatb.search.ui.results.actions;

import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.*;
import org.javatb.search.SearchElement;
import org.javatb.util.FileUtils;

/**
 * Action for opening the selected results using their associated applications.
 * @author Laurent Cohen
 */
public class OpenAction extends AbstractResultAction {

    /**
	 * Initialize this action.
	 */
    public OpenAction(JComponent component) {
        super(component);
        putValue(Action.NAME, "Open");
    }

    /**
	 * Peform the "edit" action on each selected element.
	 * @param event not used.
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
    public void actionPerformed(ActionEvent event) {
        List<SearchElement> sel = getSelection();
        for (SearchElement elt : sel) {
            if (elt.isFile() || elt.isArchiveEntry()) {
                FileUtils.openSearchElement(elt);
            }
        }
    }
}
