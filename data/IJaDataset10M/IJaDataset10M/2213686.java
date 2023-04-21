package org.magicable.openmuseau.client.behavior;

import org.magicable.openmuseau.client.views.CreateObjectView;

public class ShowCreateObjectCommand extends ShowCommand {

    @Override
    public void execute() {
        super.execute();
        this.mainContainer.add(new CreateObjectView());
    }
}
