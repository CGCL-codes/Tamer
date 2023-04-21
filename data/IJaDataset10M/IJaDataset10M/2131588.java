package com.antilia.demo.manager;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import com.antilia.web.field.impl.EnumDropDownChoice;
import com.antilia.web.login.LogInRoundPanel;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class LanguagePanel extends Panel {

    private static final long serialVersionUID = 1L;

    /**
	 * @param id
	 * @param model
	 */
    public LanguagePanel(String id) {
        super(id);
        EnumDropDownChoice<Language> language = new EnumDropDownChoice<Language>("language", Language.class, new Model<Language>() {

            private static final long serialVersionUID = 1L;

            @Override
            public Language getObject() {
                return ManagerSession.getSession().getLanguage();
            }

            @Override
            public void setObject(Language language) {
                ManagerSession.getSession().setLanguage(language);
            }
        });
        language.setRequired(true);
        language.setNullValid(false);
        language.add(new OnChangeAjaxBehavior() {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                if (target != null) {
                    target.addComponent(getPanel().getRoundpane());
                }
            }
        });
        add(language);
    }

    private LogInRoundPanel getPanel() {
        return (LogInRoundPanel) findParent(LogInRoundPanel.class);
    }
}
