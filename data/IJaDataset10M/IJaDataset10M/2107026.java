package com.ingenico.insider.swing;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import com.ingenico.insider.services.impl.LayoutControlSupplier;

public class LayoutSaveDefaultAction extends AbstractAction {

    /**
	 * Generated Serial Version UID
	 */
    private static final long serialVersionUID = 3290747622018687406L;

    @Override
    public void actionPerformed(ActionEvent e) {
        LayoutControlSupplier.getInstance().saveDefault();
    }
}
