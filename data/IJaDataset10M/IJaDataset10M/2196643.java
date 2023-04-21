package com.tysanclan.site.projectewok.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.odlabs.wiquery.core.options.LiteralOption;
import org.odlabs.wiquery.ui.accordion.Accordion;
import org.odlabs.wiquery.ui.accordion.AccordionHeader;
import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.TysanPage;
import com.tysanclan.site.projectewok.beans.RealmService;
import com.tysanclan.site.projectewok.components.MemberListItem;
import com.tysanclan.site.projectewok.components.RealmGamePanel;
import com.tysanclan.site.projectewok.entities.Game;
import com.tysanclan.site.projectewok.entities.Realm;
import com.tysanclan.site.projectewok.entities.dao.RealmDAO;

/**
 * @author Jeroen Steenbeeke
 */
public class RealmPage extends TysanPage {

    @SpringBean
    private RealmDAO realmDAO;

    @SpringBean
    private RealmService realmService;

    private IModel<Realm> realmModel;

    public RealmPage(PageParameters params) {
        super("Realm overview");
        Long id = Long.parseLong(params.get("id").toString());
        init(realmDAO.load(id));
    }

    public void init(Realm realm) {
        setPageTitle("Realm overview - " + realm.getName());
        add(new BookmarkablePageLink<Void>("back", AboutPage.class));
        realmModel = ModelMaker.wrap(realm);
        Accordion accordion = new Accordion("accordion");
        accordion.setHeader(new AccordionHeader(new LiteralOption("h2")));
        accordion.setAutoHeight(false);
        accordion.add(new Label("name", realm.getName()));
        if (realm.getOverseer() != null) {
            accordion.add(new MemberListItem("supervisor", realm.getOverseer()));
        } else {
            accordion.add(new Label("supervisor", "-"));
        }
        accordion.add(new Label("playercount", new Model<Integer>(realmService.countActivePlayers(realm))));
        add(accordion);
        add(new ListView<Game>("games", ModelMaker.wrap(realm.getGames())) {

            private static final long serialVersionUID = 1L;

            /**
			 * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
			 */
            @Override
            protected void populateItem(ListItem<Game> item) {
                Game game = item.getModelObject();
                Realm rlm = getRealm();
                item.add(new RealmGamePanel("game", rlm, game));
            }
        });
    }

    public Realm getRealm() {
        return realmModel.getObject();
    }

    /**
	 * @see org.apache.wicket.Page#onDetach()
	 */
    @Override
    protected void onDetach() {
        super.onDetach();
        realmModel.detach();
    }
}
