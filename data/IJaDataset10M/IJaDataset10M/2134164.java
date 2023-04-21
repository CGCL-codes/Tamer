package nakayo.gameserver.model.broker.filter;

import nakayo.gameserver.model.PlayerClass;
import nakayo.gameserver.model.templates.item.ItemTemplate;

/**
 * @author ATracer
 */
public class BrokerPlayerClassFilter extends BrokerFilter {

    private PlayerClass playerClass;

    /**
     * @param playerClass
     */
    public BrokerPlayerClassFilter(PlayerClass playerClass) {
        super();
        this.playerClass = playerClass;
    }

    @Override
    public boolean accept(ItemTemplate template) {
        return template.isClassSpecific(playerClass);
    }
}
