package com.antilia.demo.manager.crud;

import org.apache.wicket.Component;
import com.antilia.web.dialog.ModalContainer;

/**
 * 
 *
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class CountryModalContainer extends ModalContainer {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * @param id
	 */
    public CountryModalContainer(String id) {
        super(id);
    }

    @Override
    protected Component createBody(String id) {
        return new CountryCRUDPanel(id);
    }
}
