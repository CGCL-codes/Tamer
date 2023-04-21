package net.sourceforge.eclipsetrader.trading.internal.watchlist;

import java.text.NumberFormat;
import java.util.Comparator;
import net.sourceforge.eclipsetrader.core.CurrencyConverter;
import net.sourceforge.eclipsetrader.core.db.WatchlistItem;
import org.eclipse.jface.viewers.LabelProvider;

public class ClosePrice extends LabelProvider implements Comparator {

    private NumberFormat formatter = NumberFormat.getInstance();

    public ClosePrice() {
        formatter.setGroupingUsed(true);
        formatter.setMinimumIntegerDigits(1);
        formatter.setMinimumFractionDigits(4);
        formatter.setMaximumFractionDigits(4);
    }

    public String getText(Object element) {
        if (element instanceof WatchlistItem) {
            WatchlistItem item = (WatchlistItem) element;
            if (item.getSecurity() == null) return "";
            if (item.getSecurity().getClose() != null) return formatter.format(CurrencyConverter.getInstance().convert(item.getSecurity().getClose(), item.getSecurity().getCurrency(), item.getParent().getCurrency()));
        }
        return "";
    }

    public int compare(Object arg0, Object arg1) {
        if (getValue((WatchlistItem) arg0) > getValue((WatchlistItem) arg1)) return 1; else if (getValue((WatchlistItem) arg0) < getValue((WatchlistItem) arg1)) return -1;
        return 0;
    }

    private double getValue(WatchlistItem item) {
        if (item.getSecurity() == null) return 0;
        if (item.getSecurity().getClose() != null) return CurrencyConverter.getInstance().convert(item.getSecurity().getClose(), item.getSecurity().getCurrency(), item.getParent().getCurrency());
        return 0;
    }
}
