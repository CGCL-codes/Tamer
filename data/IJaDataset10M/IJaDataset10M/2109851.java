package com.antilia.demo.manager.components.roundpane;

import java.io.Serializable;
import org.apache.wicket.Component;
import com.antilia.web.layout.BackToHomeTopMenuPanel;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class ShowRoundPanel extends BackToHomeTopMenuPanel<Serializable> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * @param id
	 */
    public ShowRoundPanel(String id) {
        super(id);
    }

    @Override
    protected Component createBody(String id) {
        return new RoundPanels(id);
    }
}
