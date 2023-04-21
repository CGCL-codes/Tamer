package org.xtoto;

import org.xtoto.dao.UserDao;
import org.xtoto.model.SpecialTip;
import org.xtoto.model.User;
import org.xtoto.ui.NavigablePage;
import org.xtoto.utils.SessionUtils;
import wicket.PageParameters;
import wicket.markup.html.basic.Label;
import wicket.markup.html.link.PageLink;
import wicket.spring.injection.annot.SpringBean;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SpecialTips extends NavigablePage {

    public static final Date SPECIAL_TIP_DATE = new Date(106, 5, 15);

    @SpringBean(name = "MyUserDao")
    private UserDao userDao;

    public SpecialTips(PageParameters parameters) {
        User user = SessionUtils.getUser(this, userDao);
        SpecialTip specialTip = user.getSpecialTip();
        if (specialTip == null) {
            add(new Label("team1", "Offen"));
            add(new Label("team2", "Offen"));
            add(new Label("numberOfGoals", "Offen"));
        } else {
            add(new Label("team1", specialTip.getTeam1().getName()));
            add(new Label("team2", specialTip.getTeam2().getName()));
            add(new Label("numberOfGoals", Integer.toString(specialTip.getNumberOfGoals())));
        }
        PageLink editSpecialTipLink = new PageLink("editSpecialTip", EditSpecialTip.class);
        editSpecialTipLink.setEnabled(new Date().before(SPECIAL_TIP_DATE));
        add(editSpecialTipLink);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        add(new Label("specialTipDate", sdf.format(SPECIAL_TIP_DATE)));
    }
}
