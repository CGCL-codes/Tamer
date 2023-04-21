package org.byars.struts2.actions;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.byars.struts2.models.CheckoutItem;
import org.byars.struts2.services.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.code.rees.scope.conversation.annotations.ConversationController;
import com.google.code.rees.scope.conversation.annotations.ConversationField;

@Namespace(value = "/struts2/booking")
@ConversationController(conversations = "booking")
public class CheckoutController extends BaseController {

    private static final long serialVersionUID = -3057806960547418325L;

    @ConversationField
    private CheckoutItem checkoutItem;

    @Autowired
    MyService myService;

    @Action("begin-checkout")
    public String beginCheckout() {
        myService.printPrice();
        return SUCCESS;
    }

    @Action("end-checkout")
    public String endCheckout() {
        myService.printPrice();
        return SUCCESS;
    }

    public CheckoutItem getCheckoutItem() {
        return checkoutItem;
    }
}
