package org.localstorm.mcc.web.cashflow.actions;

import java.math.BigDecimal;
import java.util.Collection;
import org.localstorm.mcc.web.cashflow.*;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import org.localstorm.mcc.ejb.cashflow.entity.Asset;
import org.localstorm.mcc.ejb.cashflow.AssetManager;
import org.localstorm.mcc.ejb.cashflow.OperationManager;
import org.localstorm.mcc.ejb.cashflow.entity.HistoricalValue;
import org.localstorm.mcc.ejb.cashflow.HistoricalValuesManager;
import org.localstorm.mcc.ejb.cashflow.entity.ValuableObject;
import org.localstorm.mcc.ejb.cashflow.entity.ValueType;
import org.localstorm.mcc.ejb.users.User;
import org.localstorm.mcc.web.cashflow.actions.wrap.AssetWrapper;
import org.localstorm.mcc.web.cashflow.actions.wrap.WrapUtil;
import org.localstorm.tools.aop.runtime.Logged;

/**
 * @secure-by session (no security check)
 * @author localstorm
 */
@UrlBinding("/actions/cash/nil/ViewAssets")
public class AssetsViewActionBean extends CashflowBaseActionBean {

    private static final BigDecimal EPS = new BigDecimal("0.0001");

    private Collection<Asset> assets;

    private BigDecimal netWealthWoDebt;

    private BigDecimal netWealth;

    private BigDecimal balance;

    private BigDecimal debt;

    private boolean checkpointUpdateNeeded;

    public Collection<Asset> getAssets() {
        return assets;
    }

    public void setAssets(Collection<Asset> assets) {
        this.assets = assets;
    }

    public BigDecimal getNetWealth() {
        return this.netWealth;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public boolean isCheckpointUpdateNeeded() {
        return checkpointUpdateNeeded;
    }

    public BigDecimal getNetWealthWoDebt() {
        return netWealthWoDebt;
    }

    public void setNetWealthWoDebt(BigDecimal netWealthWoDebt) {
        this.netWealthWoDebt = netWealthWoDebt;
    }

    public BigDecimal getDebt() {
        return debt;
    }

    public void setDebt(BigDecimal debt) {
        this.debt = debt;
    }

    @DefaultHandler
    @Logged
    public Resolution filling() {
        AssetManager am = super.getAssetManager();
        OperationManager om = super.getOperationManager();
        User user = super.getUser();
        this.assets = am.getAssets(user);
        this.assets = WrapUtil.wrapAssets(assets, om);
        this.netWealth = BigDecimal.ZERO;
        this.netWealthWoDebt = BigDecimal.ZERO;
        this.balance = BigDecimal.ZERO;
        this.debt = BigDecimal.ZERO;
        for (Asset a : assets) {
            AssetWrapper aw = (AssetWrapper) a;
            ValuableObject vo = aw.getValuable();
            BigDecimal worth = aw.getNetWealth();
            if (!vo.isDebt()) {
                this.netWealth = this.netWealth.add(worth);
                this.netWealthWoDebt = this.netWealthWoDebt.add(worth);
            } else {
                this.debt = this.debt.subtract(worth);
                this.netWealthWoDebt = this.netWealthWoDebt.add(worth);
            }
            if (vo.isUsedInBalance()) {
                this.balance = this.balance.add(aw.getBalance());
            }
        }
        this.checkpointUpdateNeeded = this.getCheckpointStatus(user, this.netWealth, this.balance, this.netWealthWoDebt, this.debt);
        return new ForwardResolution(Views.ASSETS);
    }

    private boolean getCheckpointStatus(User user, BigDecimal netWealth, BigDecimal balance, BigDecimal netWealthWoDebt, BigDecimal debt) {
        HistoricalValuesManager hvm = super.getHistoricalValuesManager();
        ValueType twc = ValueType.NET_WEALTH_CHECKPOINT;
        ValueType twc2 = ValueType.BALANCE_CHECKPOINT;
        ValueType twc3 = ValueType.NET_WEALTH_WO_DEBT_CHECKPOINT;
        ValueType twc4 = ValueType.DEBT_CHECKPOINT;
        HistoricalValue hv = hvm.getLastHistoricalValue(twc, BigDecimal.ZERO, user);
        HistoricalValue hv2 = hvm.getLastHistoricalValue(twc2, BigDecimal.ZERO, user);
        HistoricalValue hv3 = hvm.getLastHistoricalValue(twc3, BigDecimal.ZERO, user);
        HistoricalValue hv4 = hvm.getLastHistoricalValue(twc4, BigDecimal.ZERO, user);
        return !equals(netWealth, hv) || !equals(balance, hv2) || !equals(netWealthWoDebt, hv3) || !equals(debt, hv4);
    }

    private boolean equals(BigDecimal orig, HistoricalValue hv) {
        return hv.getVal().subtract(orig).abs().compareTo(EPS) <= 0;
    }
}
