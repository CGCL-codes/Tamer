package de.fzi.herakles.commons.herakles;

import de.fzi.herakles.commons.configuration.FunctionConfiguration;
import de.fzi.herakles.commons.configuration.StrategyConfiguration;
import de.fzi.herakles.commons.configuration.impl.FunctionConfigurationImp;
import de.fzi.herakles.commons.configuration.impl.ReasonerConfigurationImp;
import de.fzi.herakles.commons.configuration.ReasonerConfiguration;
import de.fzi.herakles.commons.configuration.impl.StrategyConfigurationImp;
import de.fzi.herakles.commons.reasoner.ReasonerRegistry;
import de.fzi.herakles.commons.statistic.StatisticData;

/**
 * center component manager of Herakles, provide a to create Herakles components
 * @author Xu
 *
 */
public class HeraklesManager {

    private static ReasonerRegistry register;

    private static StrategyConfiguration strategyConfig;

    private static FunctionConfiguration functionConfig;

    private static ReasonerConfiguration reasonerConfig;

    private static StatisticData statisticData;

    /**
	 * get the reasoner register, which saves the remote reasoner adapters
	 * @return reasoner register
	 */
    public static ReasonerRegistry getReasonerRegistry() {
        if (register == null) {
            register = ReasonerRegistry.getInstance();
        }
        return register;
    }

    /**
	 * get the strategy configuration, which saves all available load and execution strategies
	 * @return strategy configuration object
	 */
    public static StrategyConfiguration getStrategyConfiguration() {
        if (strategyConfig == null) {
            strategyConfig = new StrategyConfigurationImp();
        }
        return strategyConfig;
    }

    /**
	 * get the function configuration, which saves the function condition for task selection execution strategy
	 * @return function configuration object
	 */
    public static FunctionConfiguration getFunctionConfiguration() {
        if (functionConfig == null) {
            functionConfig = new FunctionConfigurationImp();
        }
        return functionConfig;
    }

    /**
	 * get the reasoner configuration, which saves the information of the remote reasoners 
	 * @return reasoner configuration object
	 */
    public static ReasonerConfiguration getReasonerConfiguration() {
        if (reasonerConfig == null) {
            reasonerConfig = new ReasonerConfigurationImp();
        }
        return reasonerConfig;
    }

    /**
	 * get the statistic data object , whiach saves the statistic data of the herakles
	 * @return statistic data object
	 */
    public static StatisticData getStatisticData() {
        if (statisticData == null) {
            statisticData = StatisticData.getInstance();
        }
        return statisticData;
    }
}
