package de.mse.mogwai.swingframework.example.bookstore;

import java.util.HashMap;
import de.mse.mogwai.swingframework.controller.NavigatableController;
import de.mse.mogwai.swingframework.example.bookstore.pojo.Customer;
import de.mse.mogwai.swingframework.message.Message;

/**
 * @author msertic
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BookstoreCustomerController extends NavigatableController {

    private Customer m_model;

    public BookstoreCustomerController() {
    }

    /**
	 * @return Returns the model.
	 */
    public Customer getModel() {
        return m_model;
    }

    /**
	 * @param model The model to set.
	 */
    public void setModel(Customer model) {
        m_model = model;
    }
}
