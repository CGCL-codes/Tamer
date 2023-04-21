package org.ujac.web.demo.actions;

import java.io.IOException;
import javax.servlet.ServletException;
import org.ujac.form.Form;
import org.ujac.util.BeanUtils;
import org.ujac.util.UjacException;
import org.ujac.web.demo.beans.CustomerModel;
import org.ujac.web.servlet.ActionContext;
import org.ujac.web.servlet.ForwardAction;

/**
 * Name: CommitCustomerAction<br>
 * Description: Description of the class.
 * <br>Log: $Log$
 * <br>Log: Revision 1.1  2004/12/24 02:06:38  lauerc
 * <br>Log: Initial revision.
 * <br>Log:
 * @author $Author: lauerc $
 * @version $Revision: 2011 $
 */
public class CommitCustomerAction extends BaseCustomerAction {

    /**
   * @see org.ujac.web.servlet.Action#getName()
   */
    public String getName() {
        return "commit-customer";
    }

    /**
   * Tells whether or not to add this action to the history.
   * @return true if this action shall be added to the history, else false.
   */
    public boolean isHistoryRelevant() {
        return false;
    }

    /**
   * Gets the list of actions this action may go back to.
   * @return The action references.
   */
    public String[] getBackwardReferences() {
        return new String[] { "customer-list", "customer-details" };
    }

    /**
   * Performs the request action. 
   * @param ctx The object, describing the request
   * @return The action to forward to, or null.
   * @exception ServletException In case a servlet problem occurred.
   * @exception IOException In case an I/O problem occurred.
   * @exception UjacException In case an UJAC related problem occurred.
   */
    public ForwardAction perform(ActionContext ctx) throws ServletException, IOException, UjacException {
        Form form = ctx.parseForm();
        if (form.hasErros()) {
            return null;
        }
        Integer customerId = (Integer) form.getFormField(PARAM_CUSTOMER_ID).getValue();
        CustomerModel customer = getCustomer(ctx, customerId);
        if (customer == null) {
            customer = new CustomerModel(customerId, (String) form.getFormField(PARAM_NAME).getValue(), (String) form.getFormField(PARAM_FIRST_NAME).getValue(), (String) form.getFormField(PARAM_ADDRESS).getValue(), (Integer) form.getFormField(PARAM_ZIP_CODE).getValue(), (String) form.getFormField(PARAM_CITY).getValue());
            getCustomerList(ctx).add(customer);
        } else {
            customer.setName((String) form.getFormField("name").getValue());
            customer.setFirstName((String) form.getFormField("firstName").getValue());
            customer.setAddress((String) form.getFormField("address").getValue());
            customer.setZipCode((Integer) form.getFormField("zipCode").getValue());
            customer.setCity((String) form.getFormField("city").getValue());
        }
        String forwardAction = ctx.rollbackHistory("edit-customer");
        ctx.rollbackHistory();
        if (BeanUtils.equals(forwardAction, "new-customer")) {
            forwardAction = ctx.rollbackHistory();
        }
        if (forwardAction == null) {
            forwardAction = "customer-list";
        }
        return new ForwardAction(ForwardAction.REDIRECT, forwardAction);
    }
}
