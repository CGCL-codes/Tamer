package traviaut.gui;

import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import traviaut.tickers.TickerData;

public class Tickers {

    private final Map<Integer, Ticker> tickers = new HashMap<Integer, Ticker>();

    private final Map<JLabel, Ticker> labels = new HashMap<JLabel, Ticker>();

    public void removeTicker(int d) {
        tickers.remove(d);
    }

    public Ticker setLabel(JLabel lab, TickerData t) {
        Ticker oldTicker = labels.get(lab);
        int d = t.getDelay();
        Ticker newTicker = tickers.get(d);
        if (newTicker == null) {
            newTicker = new Ticker(d);
            tickers.put(d, newTicker);
        }
        if (oldTicker != newTicker) {
            if (oldTicker != null) oldTicker.removeLabel(lab, this);
            labels.put(lab, newTicker);
        }
        newTicker.setLabel(lab, t);
        return newTicker;
    }

    public void removeLabel(JLabel lab) {
        Ticker oldTicker = labels.remove(lab);
        if (oldTicker != null) oldTicker.removeLabel(lab, this);
    }
}
