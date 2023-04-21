package bgo.website;

import org.hibernate.Session;
import bgo.model.JNLPDocument;
import bgo.model.OpenGame;
import bgo.model.Player;
import bgo.model.RunGame;
import bgo.utils.HibernateUtil;
import wicket.PageParameters;
import wicket.markup.html.basic.Label;
import wicket.markup.html.image.Image;
import wicket.markup.html.link.ResourceLink;
import wicket.markup.html.resources.StyleSheetReference;

/**
 * @author Matthias Becker
 * This page is used to display a link to a JNLP-Document
 * it is neccessary because of some problems on wicket and ajax
 */
public class DeliverJNLPPage extends BGOWebsitePage {

    /** makes wicket and eclipse happy */
    private static final long serialVersionUID = 1L;

    /**
	 * Display a page that delivers the JNLP-File to launch a game
	 * @param doc JNLP-File, needed to launch Java-Client
	 * @param isFirst is this the first player?
	 * @param og the OpenGame-Object 
	 */
    public DeliverJNLPPage(JNLPDocument doc, boolean isFirst, OpenGame og, boolean wasLoaded) {
        Player p = ((BGOWebsiteSession) getSession()).getPlayer();
        if (!p.isLoggedIn()) {
            setResponsePage(new Failed("Sie können auf diese Seite nicht zugreifen," + " weil Sie nicht angemeldet sind.", "Sie sind nicht angemeldet", Index.class));
        }
        if (isFirst) {
            String description = "";
            description += og.getOpenGameID();
            description += ":";
            description += og.getGameName();
            description += ":";
            description += og.getPlayers().size();
            for (int count = 0; count < og.getPlayers().size(); count++) {
                description += ":";
                description += og.getPlayers().get(count).getName();
            }
            for (int count = 0; count < og.getPlayers().size(); count++) {
                description += ":";
                description += og.getPlayers().get(count).getPlayerId();
            }
            RunGame rg = new RunGame();
            rg.setDesc(description);
            rg.setServer(og.getGame().getWebserviceURL());
            if (wasLoaded) {
                rg.setWasloaded("true");
            } else {
                rg.setWasloaded("false");
            }
            rg.save();
        }
        add(new StyleSheetReference("pageCSS", getClass(), "bgo.css"));
        add(new Image("logo", "bgo-logo.png"));
        add(new Navigation("navigation", ((BGOWebsiteSession) getSession())));
        add(new Label("changedate", new BGOWebsiteModificationDate("WaitForGamePage.html").getDate()));
        add(new ResourceLink("jnlpdatei", doc) {

            /** makes wicket and eclipse happy */
            private static final long serialVersionUID = 1L;

            public void onClick() {
                setResponsePage(new Index(PageParameters.NULL));
            }
        });
    }
}
