package com.tysanclan.site.projectewok.components.accountpanel;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import com.jeroensteenbeeke.hyperion.data.ModelMaker;
import com.tysanclan.site.projectewok.beans.GameService;
import com.tysanclan.site.projectewok.entities.GameAccount;
import com.tysanclan.site.projectewok.entities.GameAccount.AccountType;
import com.tysanclan.site.projectewok.entities.UserGameRealm;
import com.tysanclan.site.projectewok.pages.member.EditAccountsPage;

public class LeagueOfLegendsPanel extends Panel {

    private static final long serialVersionUID = 1L;

    @SpringBean
    private GameService gameService;

    public LeagueOfLegendsPanel(String id, UserGameRealm userGameRealm) {
        super(id);
        Form<UserGameRealm> form = new Form<UserGameRealm>("addLoLForm", ModelMaker.wrap(userGameRealm)) {

            private static final long serialVersionUID = 1L;

            @SuppressWarnings("unchecked")
            @Override
            protected void onSubmit() {
                UserGameRealm ugr = getModelObject();
                TextField<String> nameField = (TextField<String>) get("name");
                GameAccount acc = gameService.createLeagueOfLegendsAccount(ugr, nameField.getModelObject());
                if (acc == null) {
                    error("Invalid account name");
                    return;
                }
                setResponsePage(new EditAccountsPage());
            }
        };
        form.add(new TextField<String>("name", new Model<String>("")).setRequired(true));
        setVisible(gameService.isValidAccountType(userGameRealm.getGame(), userGameRealm.getRealm(), AccountType.LEAGUE_OF_LEGENDS));
        add(form);
    }
}
