package com.cignadev.sqlgen;

import com.cignadev.core.ConfigurationManager;

/**
 * @author yxli
 *
 */
public class SQLStrategyFactory {

    public static SQLStrategy getSQLStrategy(ConfigurationManager config) {
        SQLStrategy strategy = new MSSQLStrategy();
        strategy.setConfig(config);
        return strategy;
    }
}
