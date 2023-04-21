package net.sf.doolin.app.sc.client.model;

import java.math.BigDecimal;
import net.sf.doolin.app.sc.client.bean.GameBean;
import net.sf.doolin.app.sc.client.field.BudgetBarsModel;
import net.sf.doolin.bus.bean.Bean;

public class EconomyModel extends Bean {

    public static final String BUDGET_SAVINGS = "budgetSavings";

    public static final String BUDGET_RESEARCH = "budgetResearch";

    private static final long serialVersionUID = 1L;

    public static final String LOGARITHMIC_SCALE = "logarithmicScale";

    public static final String INTEREST_AMOUNT = "interestAmount";

    public static final String INCOME = "income";

    public static final String SAVINGS = "savings";

    private final GameBean game;

    private final BudgetBarsModel budgetBarsModel;

    private int selectedBarIndex = -1;

    private boolean logarithmicScale;

    private long income;

    private long savings;

    private long interestAmount;

    private BigDecimal budgetSavings = new BigDecimal(0.5);

    private BigDecimal budgetResearch = new BigDecimal(0.5);

    public EconomyModel(GameBean gameModel) {
        this.game = gameModel;
        this.budgetBarsModel = new BudgetBarsModel(this);
    }

    public BudgetBarsModel getBudgetBarsModel() {
        return this.budgetBarsModel;
    }

    public BigDecimal getBudgetResearch() {
        return this.budgetResearch;
    }

    public BigDecimal getBudgetSavings() {
        return this.budgetSavings;
    }

    public GameBean getGame() {
        return this.game;
    }

    public long getIncome() {
        return this.income;
    }

    public long getInterestAmount() {
        return this.interestAmount;
    }

    public long getSavings() {
        return this.savings;
    }

    public int getSelectedBarIndex() {
        return this.selectedBarIndex;
    }

    public boolean isLogarithmicScale() {
        return this.logarithmicScale;
    }

    public void setBudgetResearch(BigDecimal budgetResearch) {
        notify(BUDGET_RESEARCH, this.budgetResearch, this.budgetResearch = budgetResearch);
    }

    public void setBudgetSavings(BigDecimal budgetSavings) {
        notify(BUDGET_SAVINGS, this.budgetSavings, this.budgetSavings = budgetSavings);
    }

    public void setIncome(long income) {
        notify(INCOME, this.income, this.income = income);
    }

    public void setInterestAmount(long interestAmount) {
        notify(INTEREST_AMOUNT, this.interestAmount, this.interestAmount = interestAmount);
    }

    public void setLogarithmicScale(boolean logarithmicScale) {
        notify(LOGARITHMIC_SCALE, this.logarithmicScale, this.logarithmicScale = logarithmicScale);
    }

    public void setSavings(long savings) {
        notify(SAVINGS, this.savings, this.savings = savings);
    }

    public void setSelectedBarIndex(int selectedBarIndex) {
        this.selectedBarIndex = selectedBarIndex;
        int index = this.selectedBarIndex - 2;
        if (index >= 0) {
            PlanetModel planetModel = this.budgetBarsModel.getOwnedPlanetList().get(index);
            this.game.setSelectedPlanetModel(planetModel);
        }
    }
}
