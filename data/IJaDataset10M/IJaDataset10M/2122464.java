package pl.pyrkon.cm.client.cashier.common;

import java.io.Serializable;
import java.util.List;

public class PaymentStatisticsTO implements Serializable {

    private static final long serialVersionUID = -2009142105236127604L;

    private PaymentStatistics stats;

    private List<PaymentStatisticsDetails> detailStats;

    public PaymentStatisticsTO() {
        super();
    }

    public PaymentStatisticsTO(PaymentStatistics stats, List<PaymentStatisticsDetails> detailStats) {
        super();
        this.stats = stats;
        this.detailStats = detailStats;
    }

    public PaymentStatistics getStats() {
        return stats;
    }

    public void setStats(PaymentStatistics stats) {
        this.stats = stats;
    }

    public List<PaymentStatisticsDetails> getDetailStats() {
        return detailStats;
    }

    public void setDetailStats(List<PaymentStatisticsDetails> detailStats) {
        this.detailStats = detailStats;
    }
}
