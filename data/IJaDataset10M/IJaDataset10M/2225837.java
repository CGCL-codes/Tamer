package com.tysanclan.site.projectewok.components;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.spring.injection.annot.SpringBean;
import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.components.RequiresAttentionLink.AttentionType;
import com.tysanclan.site.projectewok.components.RequiresAttentionLink.IRequiresAttentionCondition;
import com.tysanclan.site.projectewok.entities.User;
import com.tysanclan.site.projectewok.entities.dao.MobileUserAgentDAO;
import com.tysanclan.site.projectewok.entities.dao.filters.MobileUserAgentFilter;
import com.tysanclan.site.projectewok.pages.member.PaypalSettingsPage;
import com.tysanclan.site.projectewok.pages.member.admin.GameRealmAllowAccountTypePage;
import com.tysanclan.site.projectewok.pages.member.admin.RequestPaymentPage;
import com.tysanclan.site.projectewok.pages.member.admin.SiteWideNotificationPage;
import com.tysanclan.site.projectewok.pages.member.admin.StewardManageBugMastersPage;
import com.tysanclan.site.projectewok.pages.member.admin.StewardTransferPage;
import com.tysanclan.site.projectewok.pages.member.admin.UserAgentPage;

public class StewardPanel extends TysanOverviewPanel<User> {

    public class UnknownUserAgentsCondition implements IRequiresAttentionCondition {

        private static final long serialVersionUID = 1L;

        @Override
        public AttentionType requiresAttention() {
            MobileUserAgentFilter filter = new MobileUserAgentFilter();
            filter.setSearchUnknownType(true);
            if (userAgentDAO.countByFilter(filter) > 0) {
                return AttentionType.ERROR;
            }
            return null;
        }

        @Override
        public Long getDismissableId() {
            return null;
        }
    }

    private static final long serialVersionUID = 1L;

    @SpringBean
    private RoleService roleService;

    @SpringBean
    private MobileUserAgentDAO userAgentDAO;

    public StewardPanel(String id, User user) {
        super(id, ModelMaker.wrap(user), "Steward");
        setVisible(user.equals(roleService.getSteward()));
        add(new Link<User>("bugmasters") {

            private static final long serialVersionUID = 1L;

            /**
			* @see org.apache.wicket.markup.html.link.Link#onClick()
			*/
            @Override
            public void onClick() {
                setResponsePage(new StewardManageBugMastersPage());
            }
        });
        add(new Link<User>("notifications", ModelMaker.wrap(user)) {

            private static final long serialVersionUID = 1L;

            /**
			* @see org.apache.wicket.markup.html.link.Link#onClick()
			*/
            @Override
            public void onClick() {
                setResponsePage(new SiteWideNotificationPage(getModelObject()));
            }
        });
        add(new Link<User>("allowedGames") {

            private static final long serialVersionUID = 1L;

            /**
			* @see org.apache.wicket.markup.html.link.Link#onClick()
			*/
            @Override
            public void onClick() {
                setResponsePage(new GameRealmAllowAccountTypePage());
            }
        });
        add(createConditionalVisibilityLink("useragent", UserAgentPage.class, "User Agents", new UnknownUserAgentsCondition()));
        add(createLink("paypal", PaypalSettingsPage.class, "PayPal settings", new DoesNotHavePaypalCondition(user)));
        add(new Link<User>("requestPayment") {

            private static final long serialVersionUID = 1L;

            /**
			* @see org.apache.wicket.markup.html.link.Link#onClick()
			*/
            @Override
            public void onClick() {
                setResponsePage(new RequestPaymentPage(getUser()));
            }
        });
        add(new Link<User>("transfer") {

            private static final long serialVersionUID = 1L;

            /**
			* @see org.apache.wicket.markup.html.link.Link#onClick()
			*/
            @Override
            public void onClick() {
                setResponsePage(new StewardTransferPage());
            }
        });
    }
}
