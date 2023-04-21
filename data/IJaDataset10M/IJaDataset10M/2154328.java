package net.liveseeds.base.action;

import net.liveseeds.base.liveseed.LiveSeed;
import net.liveseeds.base.cell.Cell;
import net.liveseeds.base.world.DefaultWorld;
import net.liveseeds.base.LiveSeedsException;
import java.util.ResourceBundle;

/**
 * <a href="mailto:misha@ispras.ru>Mikhail Ksenzov</a>
 */
public class ConsumeAction extends DefaultAction {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(ConsumeAction.class.getName());

    private static final String EMPTY_STRING = "";

    ConsumeAction(final int index) {
        super(index, RESOURCE_BUNDLE.getString("name"));
    }

    protected void execute(final LiveSeed liveSeed, final Cell cell, final int price, final int param) throws LiveSeedsException {
        super.execute(liveSeed, cell, price, param);
        ((DefaultWorld) liveSeed.getWorld()).checkResource();
        for (int i = 0; i < cell.getLiveSeeds().length; i++) {
            final LiveSeed anotherSeed = cell.getLiveSeeds()[i];
            if (anotherSeed.getResourceAmount() < liveSeed.getResourceAmount()) {
                if (price > liveSeed.getResourceAmount()) {
                    return;
                }
                liveSeed.consume(anotherSeed);
            }
        }
        ((DefaultWorld) liveSeed.getWorld()).checkResource();
    }

    public String getParamString(final int param) {
        return EMPTY_STRING;
    }
}
