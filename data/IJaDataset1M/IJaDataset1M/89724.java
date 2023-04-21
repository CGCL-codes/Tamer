package dmeduc.wicket.weblink.member;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.IPageLink;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.modelibra.wicket.concept.EntityAddFormPanel;
import org.modelibra.wicket.container.DmPage;
import org.modelibra.wicket.security.AccessPoint;
import org.modelibra.wicket.view.View;
import org.modelibra.wicket.view.ViewModel;
import dmeduc.DmEduc;
import dmeduc.weblink.WebLink;
import dmeduc.weblink.member.Member;
import dmeduc.weblink.member.Members;
import dmeduc.wicket.app.DmEducApp;

/**
 * Sign Up page.
 * 
 * @author Dzenan Ridjanovic
 * @version 2007-11-28
 */
@SuppressWarnings("serial")
public class SignUpPage extends DmPage {

    /**
	 * Constructs the Sign Up page.
	 * 
	 * @param viewModel
	 *            view model
	 * @param view
	 *            view
	 */
    public SignUpPage(final ViewModel viewModel, final View view) {
        DmEducApp dmEducApp = (DmEducApp) getApplication();
        DmEduc dmEduc = dmEducApp.getDmEduc();
        WebLink webLink = dmEduc.getWebLink();
        Member guest = null;
        if (!getAppSession().isUserSignedIn()) {
            guest = new Member(webLink);
            guest.setCode(AccessPoint.GUEST);
            guest.setPassword(AccessPoint.GUEST);
            getAppSession().authenticate(guest, AccessPoint.CODE, AccessPoint.PASSWORD);
        }
        add(new FeedbackPanel("signUpFeedback"));
        ViewModel signUpModel = new ViewModel(webLink);
        Members members = webLink.getMembers();
        signUpModel.setEntities(members);
        View signUpView = new View();
        signUpView.setPage(this);
        signUpView.setWicketId("signUp");
        signUpView.setContextView(view);
        add(new EntityAddFormPanel(signUpModel, signUpView));
    }

    /**
	 * Constructs a link to this page.
	 * 
	 * @param linkId
	 *            link Wicket id
	 * @param viewModel
	 *            view model
	 * @param view
	 *            view
	 */
    public static PageLink link(final String linkId, final ViewModel viewModel, final View view) {
        PageLink link = new PageLink(linkId, new IPageLink() {

            public Page getPage() {
                return new SignUpPage(viewModel, view);
            }

            public Class<? extends Page> getPageIdentity() {
                return SignUpPage.class;
            }
        });
        return link;
    }
}
