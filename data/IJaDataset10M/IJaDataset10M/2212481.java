package net.sf.doolin.app.sc.common.stats;

import java.math.BigDecimal;
import net.sf.doolin.app.sc.common.model.Player;
import net.sf.doolin.app.sc.common.model.SCGame;

public class IncomeCollector extends AbstractStatsCollector {

    public static final String AXIS = "Income";

    public IncomeCollector() {
        super(AXIS);
    }

    @Override
    public BigDecimal collect(SCGame game, Player player) {
        long income = player.getIncome();
        return new BigDecimal(income);
    }
}
