package traviaut.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JLabel;
import javax.swing.Timer;
import traviaut.tickers.TickerData;

public class Ticker implements ActionListener {

    private final int delay;

    private final Map<JLabel, TickerData> map = new HashMap<JLabel, TickerData>();

    public Ticker() {
        this(1000);
    }

    public Ticker(int p) {
        delay = p;
        new Timer(p, this).start();
    }

    public void setLabel(JLabel lab, TickerData t) {
        map.put(lab, t);
    }

    public void removeLabel(JLabel lab, Tickers tickers) {
        map.remove(lab);
        lab.setText("");
        if (map.isEmpty()) tickers.removeTicker(delay);
    }

    public void actionPerformed(ActionEvent e) {
        long mil = System.currentTimeMillis();
        for (Entry<JLabel, TickerData> ent : map.entrySet()) {
            String s = ent.getValue().getActLabel(mil);
            ent.getKey().setText(s);
        }
    }
}
