package pl.conx;

import webX.service.Site;
import webX.loader.XmlLoader;
import org.jdom.Element;

public class Session extends webX.Session {

    public Session(Site site) {
        super(site);
    }

    /**
    * initialize session
    */
    public void initNew() {
        super.initNew();
        setPlayer((Player) XmlLoader.get("hermes/player/guest").clone());
    }

    public void setPlayer(Player player) {
        removeChild("Player");
        removeChild("Menu");
        addChild(player);
        addChild(player.getMenu());
    }

    /**
    * Clean session, making it ready to reuse
    */
    public void clear() {
        super.clear();
    }
}
