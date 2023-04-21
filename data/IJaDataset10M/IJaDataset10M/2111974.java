package jgnash.net.currency;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.SwingWorker;
import jgnash.engine.CurrencyNode;
import jgnash.engine.EngineFactory;
import jgnash.ui.UIApplication;

/**
 * Fetches latest exchange rates in the background
 *
 * @author Craig Cavanaugh
 * @version $Id: CurrencyUpdateFactory.java 3051 2012-01-02 11:27:23Z ccavanaugh $
 */
public class CurrencyUpdateFactory {

    private static final String UPDATE_ON_STARTUP = "updateOnStartup";

    private CurrencyUpdateFactory() {
    }

    public static void setUpdateOnStartup(boolean update) {
        Preferences pref = Preferences.userNodeForPackage(CurrencyUpdateFactory.class);
        pref.putBoolean(UPDATE_ON_STARTUP, update);
    }

    public static boolean getUpdateOnStartup() {
        Preferences pref = Preferences.userNodeForPackage(CurrencyUpdateFactory.class);
        return pref.getBoolean(UPDATE_ON_STARTUP, false);
    }

    public static ExchangeRateUpdateWorker getUpdateWorker() {
        return new ExchangeRateUpdateWorker();
    }

    public static class ExchangeRateUpdateWorker extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() throws Exception {
            if (UIApplication.getFrame() != null) {
                UIApplication.getFrame().setNetworkBusy(true);
            }
            List<CurrencyNode> list = new ArrayList<CurrencyNode>(EngineFactory.getEngine(EngineFactory.DEFAULT).getCurrencies());
            int lengthOfTask = (list.size() * list.size() - list.size()) / 2;
            int count = 0;
            for (CurrencyNode i : list) {
                String source = i.getSymbol();
                for (CurrencyNode j : list) {
                    String target = j.getSymbol();
                    if (!source.equals(target) && source.compareToIgnoreCase(target) > 0 && !isCancelled()) {
                        CurrencyParser parser = new YahooParser();
                        if (parser.parse(source, target)) {
                            BigDecimal conv = parser.getConversion();
                            if (conv != null && conv.compareTo(BigDecimal.ZERO) != 0) {
                                EngineFactory.getEngine(EngineFactory.DEFAULT).setExchangeRate(i, j, conv);
                            }
                        }
                        Thread.sleep(Math.round(1010 * Math.random()));
                        count++;
                        setProgress((int) ((float) count / (float) lengthOfTask * 100f));
                    }
                }
            }
            return null;
        }

        @Override
        protected void done() {
            if (UIApplication.getFrame() != null) {
                UIApplication.getFrame().setNetworkBusy(false);
            }
        }
    }
}
