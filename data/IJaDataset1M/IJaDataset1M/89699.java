package net.jforum.view.forum;

import net.jforum.Command;
import net.jforum.JForumExecutionContext;
import net.jforum.dao.BannerDAO;
import net.jforum.dao.DataAccessDriver;
import net.jforum.entities.Banner;
import net.jforum.view.forum.common.BannerCommon;

/**
 * @author Samuel Yung
 * @version $Id: BannerAction.java,v 1.6 2006/08/20 12:19:15 sergemaslyukov Exp $
 */
public class BannerAction extends Command {

    public void list() {
    }

    public void redirect() {
        int bannerId = this.request.getIntParameter("banner_id");
        if (!(new BannerCommon()).canBannerDisplay(bannerId)) {
            JForumExecutionContext.setRedirect("");
            return;
        }
        BannerDAO dao = DataAccessDriver.getInstance().newBannerDAO();
        Banner banner = dao.selectById(bannerId);
        banner.setClicks(banner.getClicks() + 1);
        dao.update(banner);
        JForumExecutionContext.setRedirect(banner.getUrl());
    }
}
