package mrusanov.fantasyruler.player.message.peace;

import java.math.BigDecimal;
import mrusanov.fantasyruler.player.Player;

public class MoneyPeaceTreatyItem extends AbstractPeaceTreatyItem {

    private final BigDecimal moneyAmount;

    public MoneyPeaceTreatyItem(Player receiver, Player giver, BigDecimal moneyAmount) {
        super(receiver, giver);
        this.moneyAmount = moneyAmount;
    }

    public BigDecimal getMoneyAmount() {
        return moneyAmount;
    }

    @Override
    public void executePeaceTreatyItem(int currentTurn) {
        getGiver().getBudget().spendFromTreasury(moneyAmount);
        getReceiver().getBudget().addMoneyToTreasury(moneyAmount);
    }

    @Override
    public boolean isValid() {
        return getGiver().getBudget().getTreasury().compareTo(moneyAmount) >= 0;
    }

    @Override
    public AbstractPeaceTreatyTransferObject buildTransferObject() {
        return new MoneyPeaceTreatyTransferObject(moneyAmount, getGiver().getName(), getReceiver().getName());
    }
}
