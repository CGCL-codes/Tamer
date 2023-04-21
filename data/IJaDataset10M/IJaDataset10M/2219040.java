package org.pyre.finance.dao;

import java.util.Date;
import java.util.List;
import org.pyre.finance.entity.FundHistory;
import org.pyre.finance.view.VBalance;
import org.pyre.finance.view.VFundHistory;

public interface FundHistoryHome {

    public abstract List<VFundHistory> search();

    public abstract List<VFundHistory> search(Date start, Date end, String keyword);

    public VBalance calculateBalance();

    public void saveAll(List<FundHistory> data);

    public FundHistory getFundHistory(int fundHistoryId);
}
