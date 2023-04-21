package nakayo.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

/**
 * @author Sarynth
 *         Global Default Pricing
 */
public class PricesConfig {

    @Property(key = "gameserver.prices.default.prices", defaultValue = "100")
    public static int DEFAULT_PRICES;

    @Property(key = "gameserver.prices.default.modifier", defaultValue = "100")
    public static int DEFAULT_MODIFIER;

    @Property(key = "gameserver.prices.default.taxes", defaultValue = "100")
    public static int DEFAULT_TAXES;

    @Property(key = "gameserver.prices.vendor.buymod", defaultValue = "100")
    public static int VENDOR_BUY_MODIFIER;

    @Property(key = "gameserver.prices.vendor.sellmod", defaultValue = "20")
    public static int VENDOR_SELL_MODIFIER;
}
