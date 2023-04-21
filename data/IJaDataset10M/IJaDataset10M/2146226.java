package ru.nsu.ccfit.pm.econ.controller.player.roles;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import ru.nsu.ccfit.pm.econ.common.engine.data.IUShareHolding;
import ru.nsu.ccfit.pm.econ.common.engine.roles.IUPlayer;
import ru.nsu.ccfit.pm.econ.common.engine.roles.IUShareholder;
import ru.nsu.ccfit.pm.econ.controller.player.data.ShareHolding;

public class Shareholder extends Player implements IUShareholder {

    private Map<Long, ShareHolding> shares = new HashMap<Long, ShareHolding>();

    public Shareholder() {
    }

    public Shareholder(IUPlayer other) {
        super(other);
    }

    @Override
    public Collection<? extends IUShareHolding> getUnmodifiableShareList() {
        return Collections.unmodifiableCollection(shares.values());
    }

    public IUShareHolding addShareHolding(IUShareHolding toAdd) {
        ShareHolding sh = shares.get(toAdd.getCompanyId());
        if (sh == null) {
            sh = new ShareHolding(toAdd.getCompanyId(), this.getId(), 0);
            shares.put(sh.getCompanyId(), sh);
        }
        sh.setAmount(sh.getAmount() + toAdd.getAmount());
        return sh;
    }

    public IUShareHolding subtractShareHolding(IUShareHolding toSub) {
        ShareHolding sh = shares.get(toSub.getCompanyId());
        if (sh == null || sh.getAmount() < toSub.getAmount()) throw new IllegalArgumentException("Unable to subtract share holding");
        sh.setAmount(sh.getAmount() - toSub.getAmount());
        if (sh.getAmount() == 0) {
            shares.remove(sh.getCompanyId());
        }
        return sh;
    }

    public IUShareHolding getShareHoldingForCompanyId(long companyId) {
        ShareHolding sh = shares.get(companyId);
        if (sh == null) return new ShareHolding(companyId, this.getId(), 0);
        return sh;
    }
}
