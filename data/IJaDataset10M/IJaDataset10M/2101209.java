package net.sourceforge.eclipsetrader.trading.internal.watchlist;

import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Iterator;
import net.sourceforge.eclipsetrader.core.CurrencyConverter;
import net.sourceforge.eclipsetrader.core.db.Watchlist;
import net.sourceforge.eclipsetrader.core.db.WatchlistItem;
import net.sourceforge.eclipsetrader.core.db.feed.Quote;
import org.eclipse.jface.viewers.LabelProvider;

public class BookValue extends LabelProvider implements Comparator {

    private NumberFormat formatter = NumberFormat.getInstance();

    private NumberFormat percentFormatter = NumberFormat.getInstance();

    public BookValue() {
        formatter.setGroupingUsed(true);
        formatter.setMinimumIntegerDigits(1);
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        percentFormatter.setGroupingUsed(false);
        percentFormatter.setMinimumIntegerDigits(1);
        percentFormatter.setMinimumFractionDigits(2);
        percentFormatter.setMaximumFractionDigits(2);
    }

    public String getText(Object element) {
        if (element instanceof WatchlistItem) {
            WatchlistItem item = (WatchlistItem) element;
            if (item.getSecurity() == null) return "";
            Quote quote = item.getSecurity().getQuote();
            if (quote != null && item.getPosition() != null && item.getPaidPrice() != null) {
                double paid = CurrencyConverter.getInstance().convert(item.getPosition().intValue() * item.getPaidPrice().doubleValue(), item.getSecurity().getCurrency(), item.getParent().getCurrency());
                return formatter.format(paid);
            }
        }
        if (element instanceof Watchlist) {
            double paid = 0;
            for (Iterator iter = ((Watchlist) element).getItems().iterator(); iter.hasNext(); ) {
                WatchlistItem item = (WatchlistItem) iter.next();
                if (item.getSecurity() == null) continue;
                Quote quote = item.getSecurity().getQuote();
                if (quote != null && item.getPosition() != null && item.getPaidPrice() != null) {
                    paid += item.getPosition().intValue() * item.getPaidPrice().doubleValue();
                }
            }
            if (paid == 0) return "";
            return formatter.format(paid);
        }
        return "";
    }

    public int compare(Object arg0, Object arg1) {
        if (getValue((WatchlistItem) arg0) > getValue((WatchlistItem) arg1)) return 1; else if (getValue((WatchlistItem) arg0) < getValue((WatchlistItem) arg1)) return -1;
        return 0;
    }

    private double getValue(WatchlistItem item) {
        if (item.getSecurity() == null) return 0;
        Quote quote = item.getSecurity().getQuote();
        if (quote != null && item.getPosition() != null && item.getPaidPrice() != null) {
            double paid = CurrencyConverter.getInstance().convert(item.getPosition().intValue() * item.getPaidPrice().doubleValue(), item.getSecurity().getCurrency(), item.getParent().getCurrency());
            return paid;
        }
        return 0;
    }
}
