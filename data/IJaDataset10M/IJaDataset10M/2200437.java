package org.magicable.openmuseau.client.behavior;

import org.magicable.openmuseau.client.views.WelcomeView;

public class ShowWelcomeCommand extends ShowCommand {

    @Override
    public void execute() {
        super.execute();
        this.mainContainer.add(new WelcomeView());
    }
}
