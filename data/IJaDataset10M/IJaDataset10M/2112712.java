package com.tysanclan.site.projectewok.pages.member;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import nl.topicus.wqplot.components.JQPlot;
import nl.topicus.wqplot.data.AbstractSeries;
import org.apache.wicket.Page;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.joda.time.DateTime;
import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.components.ActivateSubscriptionPanel;
import com.tysanclan.site.projectewok.components.ViewSubscriptionPanel;
import com.tysanclan.site.projectewok.entities.Expense;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.DonationDAO;
import com.tysanclan.site.projectewok.entities.dao.ExpenseDAO;
import com.tysanclan.site.projectewok.entities.dao.PaidExpenseDAO;
import com.tysanclan.site.projectewok.entities.dao.SubscriptionDAO;
import com.tysanclan.site.projectewok.model.DollarSignModel;
import com.tysanclan.site.projectewok.pages.CharterPage;
import com.tysanclan.site.projectewok.util.FinancialTimeline;
import com.tysanclan.site.projectewok.util.GraphUtil;

/**
 * @author Jeroen Steenbeeke
 */
@TysanMemberSecured
public class FinancePage extends AbstractMemberPage {

    private static final BigDecimal factor12 = new BigDecimal(12);

    private static final BigDecimal factor4 = new BigDecimal(4);

    private static final BigDecimal factor2 = new BigDecimal(2);

    @SpringBean
    private ExpenseDAO expenseDAO;

    @SpringBean
    private DonationDAO donationDAO;

    @SpringBean
    private PaidExpenseDAO paidExpenseDAO;

    @SpringBean
    private SubscriptionDAO subscriptionDAO;

    @SpringBean
    private RoleService roleService;

    /**
	 * 
	 */
    public FinancePage() {
        super("Clan Finances");
        List<Expense> expenses = expenseDAO.findAll();
        List<Expense> filtered = new LinkedList<Expense>();
        BigDecimal sum = new BigDecimal(0).setScale(2);
        DateTime now = new DateTime();
        DateTime oneMonthAgo = now.minusMonths(1);
        DateTime oneYearAgo = now.minusYears(1);
        DateTime year1997 = new DateTime(1997, 1, 1, 12, 0, 0, 0);
        for (Expense expense : expenses) {
            if (expense.getEnd() == null || expense.getEnd().after(now.toDate())) {
                switch(expense.getPeriod()) {
                    case MONTHLY:
                        sum = sum.add(expense.getAmount().multiply(factor12));
                        break;
                    case QUARTERLY:
                        sum = sum.add(expense.getAmount().multiply(factor4));
                        break;
                    case SEMIANNUALLY:
                        sum = sum.add(expense.getAmount().multiply(factor2));
                        break;
                    case ANNUALLY:
                        sum = sum.add(expense.getAmount());
                        break;
                    case BIANNUALLY:
                        sum = sum.add(expense.getAmount().divide(factor2, RoundingMode.HALF_UP));
                        break;
                }
                filtered.add(expense);
            }
        }
        add(new ListView<Expense>("expenses", ModelMaker.wrap(filtered)) {

            private static final long serialVersionUID = 1L;

            /**
			 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
			 */
            @Override
            protected void populateItem(ListItem<Expense> item) {
                Expense expense = item.getModelObject();
                item.add(new Label("name", expense.getName()));
                String regularity = null;
                IModel<String> yearlyExpense = null;
                boolean amountPerTermVisible = true;
                switch(expense.getPeriod()) {
                    case MONTHLY:
                        regularity = "Monthly";
                        yearlyExpense = new DollarSignModel(new Model<BigDecimal>(expense.getAmount().multiply(factor12)));
                        break;
                    case QUARTERLY:
                        regularity = "Quarterly";
                        yearlyExpense = new DollarSignModel(new Model<BigDecimal>(expense.getAmount().multiply(factor4)));
                        break;
                    case SEMIANNUALLY:
                        regularity = "Semiannually";
                        yearlyExpense = new DollarSignModel(new Model<BigDecimal>(expense.getAmount().multiply(factor2)));
                        break;
                    case BIANNUALLY:
                        regularity = "Biannually";
                        yearlyExpense = new DollarSignModel(new Model<BigDecimal>(expense.getAmount().divide(factor2, RoundingMode.HALF_UP)));
                        break;
                    default:
                        amountPerTermVisible = false;
                        regularity = "Annually";
                        yearlyExpense = new DollarSignModel(new Model<BigDecimal>(expense.getAmount()));
                }
                item.add(new Label("regularity", regularity));
                item.add(new Label("amounteach", new DollarSignModel(new Model<BigDecimal>(expense.getAmount()))).setVisible(amountPerTermVisible));
                item.add(new Label("annualfee", yearlyExpense));
            }
        });
        add(new Label("annualcost", new DollarSignModel(new Model<BigDecimal>(sum))));
        FinancialTimeline timeline = new FinancialTimeline(expenseDAO, donationDAO, subscriptionDAO, paidExpenseDAO);
        addLineChart("monthlychart", "Cash flow last month", 2, timeline.getCashFlow(oneMonthAgo, now));
        addLineChart("yearlychart", "Cash flow last year", 2, timeline.getCashFlow(oneYearAgo, now));
        addLineChart("alltimechart", "Cash flow all time", 2, timeline.getCashFlow(year1997, now));
        add(GraphUtil.makePieChart("monthlyparticipation", "Participation last month", timeline.getParticipation(oneMonthAgo, now)));
        add(GraphUtil.makePieChart("annualparticipation", "Participation last year", timeline.getParticipation(oneYearAgo, now)));
        add(GraphUtil.makePieChart("alltimeparticipation", "All time participation", timeline.getParticipation(year1997, now)));
        add(GraphUtil.makeReservesBarChart("reserves", "Cash reserves by donator", timeline.getReservesAt(now)));
        add(new BookmarkablePageLink<CharterPage>("charter", CharterPage.class));
        add(new WebMarkupContainer("un").add(new SimpleAttributeModifier("value", "Tysan Donation by " + getUser().getUsername())));
        User treasurer = roleService.getTreasurer();
        add(new HiddenField<String>("paypalAddress", new Model<String>(treasurer != null ? treasurer.getPaypalAddress() : "disable")).add(new SimpleAttributeModifier("name", "business")));
        add(new ActivateSubscriptionPanel("activatesubscription", getUser(), new FinancePageLink()));
        add(new ViewSubscriptionPanel("viewsubscription", getUser(), new FinancePageLink()));
    }

    protected void addBarChart(String id, String title, int page, IModel<? extends List<? extends AbstractSeries<?, ?, ?>>> model) {
        JQPlot plot = GraphUtil.makeDonationBarChart(id, title, model);
        add(plot);
    }

    protected void addLineChart(String id, String title, int page, IModel<? extends List<? extends AbstractSeries<?, ?, ?>>> model) {
        JQPlot plot = GraphUtil.makeCashFlowLineChart(id, title, model);
        add(plot);
    }

    private static class FinancePageLink implements IPageLink {

        private static final long serialVersionUID = 1L;

        @Override
        public Page getPage() {
            return new FinancePage();
        }

        @Override
        public Class<? extends Page> getPageIdentity() {
            return FinancePage.class;
        }
    }
}
