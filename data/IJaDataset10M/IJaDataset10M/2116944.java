package org.colombbus.tangara.ide.controller.action;

import java.awt.event.ActionEvent;

/**
 * Tangara close action.
 * 
 * @version $Id: CloseTangaraAction.java 196 2009-09-01 14:26:25Z swip $
 * @author Aurelien Bourdon <aurelien.bourdon@gmail.com>
 */
@SuppressWarnings("serial")
public class CloseTangaraAction extends TangaraAction {

    public CloseTangaraAction() {
        super();
        configure();
    }

    private void configure() {
        putValue(NAME, RESOURCE_BUNDLE.getString("CloseTangaraAction.name"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}
