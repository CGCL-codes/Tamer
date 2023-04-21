package com.tysanclan.site.projectewok.components;

import java.math.BigDecimal;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.FinanceService;
import com.tysanclan.site.projectewok.entities.Expense.ExpensePeriod;
import com.tysanclan.site.projectewok.entities.Subscription;
import com.tysanclan.site.projectewok.entities.User;

public class ViewSubscriptionPanel extends Panel {

    private static final long serialVersionUID = 1L;

    private IPageLink onSubmitLink;

    public ViewSubscriptionPanel(String id, User user, IPageLink onSubmitLink) {
        super(id);
        this.onSubmitLink = onSubmitLink;
        if (user.getSubscription() == null) {
            setVisible(false);
        }
        ExpensePeriod period = ExpensePeriod.MONTHLY;
        BigDecimal amount = BigDecimal.ZERO;
        int due = 0;
        if (user.getSubscription() != null) {
            period = user.getSubscription().getInterval();
            amount = user.getSubscription().getAmount();
            due = user.getSubscription().countPaymentsDue();
        }
        add(new Label("amount", new Model<BigDecimal>(amount)));
        add(new Label("interval", period.getOmschrijving()));
        add(new Label("due", new Model<Integer>(due)));
        add(new Form<Subscription>("unsubscribe", ModelMaker.wrap(user.getSubscription())) {

            private static final long serialVersionUID = 1L;

            @SpringBean
            private FinanceService financeService;

            @Override
            protected void onSubmit() {
                super.onSubmit();
                if (!financeService.cancelSubscription(getModelObject())) {
                    error("You have outstanding payments for this subscription, you cannot cancel until they are paid for");
                    return;
                }
                setResponsePage(ViewSubscriptionPanel.this.onSubmitLink.getPage());
            }
        }.setVisible(due == 0));
    }
}
