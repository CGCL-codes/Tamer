package com.antilia.letsplay;

import org.apache.wicket.markup.html.panel.Panel;
import com.antilia.letsplay.components.ScrambledWordPanel;
import com.antilia.web.login.IProtectedPage;

/**
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class PlayPage extends RoundBasePage implements IProtectedPage {

    public PlayPage() {
        super();
    }

    @Override
    protected Panel newContentPanel(String id) {
        return new ScrambledWordPanel(id);
    }
}
