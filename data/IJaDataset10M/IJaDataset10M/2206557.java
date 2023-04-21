package net.sourceforge.eclipsetrader.trading.internal.watchlist;

import java.text.NumberFormat;
import java.util.Comparator;
import net.sourceforge.eclipsetrader.core.CurrencyConverter;
import net.sourceforge.eclipsetrader.core.db.WatchlistItem;
import net.sourceforge.eclipsetrader.core.db.feed.Quote;
import org.eclipse.jface.viewers.LabelProvider;

public class LastPrice extends LabelProvider implements Comparator {

    private NumberFormat formatter = NumberFormat.getInstance();

    public LastPrice() {
        formatter.setGroupingUsed(true);
        formatter.setMinimumIntegerDigits(1);
        formatter.setMinimumFractionDigits(4);
        formatter.setMaximumFractionDigits(4);
    }

    public String getText(Object element) {
        if (element instanceof WatchlistItem) {
            WatchlistItem item = (WatchlistItem) element;
            if (item.getSecurity() == null) return "";
            Quote quote = item.getSecurity().getQuote();
            if (quote != null && quote.getLast() != 0) return formatter.format(CurrencyConverter.getInstance().convert(quote.getLast(), item.getSecurity().getCurrency(), item.getParent().getCurrency()));
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
        if (quote != null && quote.getLast() != 0) return CurrencyConverter.getInstance().convert(quote.getLast(), item.getSecurity().getCurrency(), item.getParent().getCurrency());
        return 0;
    }
}
