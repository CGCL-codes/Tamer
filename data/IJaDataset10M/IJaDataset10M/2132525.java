package gameserver.model.broker.filter;

import gameserver.model.templates.item.ItemTemplate;

public abstract class BrokerFilter {

    /**
	 * 
	 * @param template
	 * @return
	 */
    public abstract boolean accept(ItemTemplate template);
}
