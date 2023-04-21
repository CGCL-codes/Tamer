package pl.pyrkon.cm.server.cashiershift.service;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.pyrkon.cm.client.badge.data.BadgeType;
import pl.pyrkon.cm.client.cashier.common.PaymentStatisticsDetails;
import pl.pyrkon.cm.client.cashier.data.Cashier;
import pl.pyrkon.cm.client.cashiershift.common.CashierShiftService;
import pl.pyrkon.cm.client.cashiershift.data.BadgeBalance;
import pl.pyrkon.cm.client.cashiershift.data.CashierShiftBalance;
import pl.pyrkon.cm.server.badge.dao.BadgeDao;
import pl.pyrkon.cm.server.cashier.dao.CashierDao;
import pl.pyrkon.cm.server.cashiershift.dao.CashierShiftDao;
import pl.pyrkon.cm.server.payment.dao.PaymentDao;

@Service("cashierShiftServiceImpl")
public class CashierShiftServiceImpl implements CashierShiftService {

    @Autowired
    private BadgeDao badgeDao;

    @Autowired
    private CashierShiftDao dao;

    @Autowired
    private PaymentDao paymentDao;

    @Autowired
    private CashierDao cashierDao;

    @Override
    public CashierShiftBalance getCashierShiftBalance(Cashier cashier) {
        return cashier.getOnShift() != null && cashier.getOnShift() ? getOpenShift(cashier) : createNewShift(cashier);
    }

    private CashierShiftBalance createNewShift(Cashier cashier) {
        List<BadgeType> badges = badgeDao.listBadgeTypes(cashier.getPerson().getConvention());
        CashierShiftBalance csb = new CashierShiftBalance();
        csb.setIsOpen(true);
        csb.setCashier(cashier);
        csb.setOpeningCashBalance(0.0);
        csb.setClosingCashBalance(0.0);
        csb.setStartTime(new Date());
        csb.setBadgeBalances(new LinkedList<BadgeBalance>());
        for (BadgeType badgeType : badges) {
            BadgeBalance bb = new BadgeBalance();
            bb.setBadgeType(badgeType);
            bb.setOpenCount(0);
            bb.setCloseCount(0);
            bb.setShiftBalance(csb);
            csb.getBadgeBalances().add(bb);
        }
        return csb;
    }

    private CashierShiftBalance getOpenShift(Cashier cashier) {
        CashierShiftBalance shift = dao.getOpenShift(cashier);
        List<PaymentStatisticsDetails> stats = paymentDao.loadSummary(shift.getStartTime(), cashier);
        Double calculatedRevenue = 0.0;
        for (final PaymentStatisticsDetails stat : stats) {
            BadgeBalance balance = (BadgeBalance) CollectionUtils.find(shift.getBadgeBalances(), new Predicate() {

                @Override
                public boolean evaluate(Object object) {
                    return object instanceof BadgeBalance && ((BadgeBalance) object).getBadgeType().equals(stat.getBadgeType());
                }
            });
            balance.setCalculatedSale(stat.getPaymentCount().intValue());
            calculatedRevenue += stat.getPaymentQuota();
        }
        shift.setCalculatedRevenue(calculatedRevenue);
        return dao.getOpenShift(cashier);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Void storeCashierShift(CashierShiftBalance balance) {
        Cashier cashier = balance.getCashier();
        boolean isNewBalance = balance.getId() == null;
        cashier.setOnShift(isNewBalance);
        cashierDao.merge(cashier);
        balance.setIsOpen(isNewBalance);
        if (!isNewBalance) balance.setEndTime(new Date());
        dao.storeShift(balance);
        return null;
    }
}
