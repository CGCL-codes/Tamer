package org.yccheok.jstock.gui;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import javax.swing.JRadioButton;
import javax.swing.SwingWorker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.yccheok.jstock.engine.Code;
import org.yccheok.jstock.engine.StockNotFoundException;
import org.yccheok.jstock.engine.StockServerFactory;
import org.yccheok.jstock.internationalization.GUIBundle;

/**
 *
 * @author yccheok
 */
public class StockServerFactoryJRadioButton extends JRadioButton {

    public StockServerFactoryJRadioButton(StockServerFactory stockServerFactory) {
        this.stockServerFactory = stockServerFactory;
        this.setStatus(Status.Busy);
        this.setToolTipText("Checking for server health...");
        final Class c = stockServerFactory.getClass();
        if (c == org.yccheok.jstock.engine.GoogleStockServerFactory.class) {
            this.setEnabled(false);
        }
        initSwingWorker();
    }

    public static String toReadableText(StockServerFactory stockServerFactory) {
        Class c = stockServerFactory.getClass();
        if (c == org.yccheok.jstock.engine.SingaporeYahooStockServerFactory.class) {
            return GUIBundle.getString("StockServerFactoryJRadioButton_SingaporeYahooStockServerFactory");
        } else if (c == org.yccheok.jstock.engine.BrazilYahooStockServerFactory.class) {
            return GUIBundle.getString("StockServerFactoryJRadioButton_BrazilYahooStockServerFactory");
        } else if (c == org.yccheok.jstock.engine.YahooStockServerFactory.class) {
            return GUIBundle.getString("StockServerFactoryJRadioButton_YahooStockServerFactory");
        } else if (c == org.yccheok.jstock.engine.GoogleStockServerFactory.class) {
            return GUIBundle.getString("StockServerFactoryJRadioButton_GoogleStockServerFactory");
        }
        return c.getSimpleName();
    }

    private Health getServerHealth() {
        final Health health = new Health();
        Class c = stockServerFactory.getStockServer().getClass();
        Code code = Code.newInstance("MSFT");
        if (c == org.yccheok.jstock.engine.SingaporeYahooStockServer.class) {
            code = Code.newInstance("1295.KL");
        } else if (c == org.yccheok.jstock.engine.BrazilYahooStockServer.class) {
            code = Code.newInstance("ALLL11.SA");
        } else if (c == org.yccheok.jstock.engine.YahooStockServer.class) {
            code = Code.newInstance("MSFT");
        } else if (c == org.yccheok.jstock.engine.GoogleStockServerFactory.class) {
            code = Code.newInstance("MSFT");
        }
        try {
            stockServerFactory.getStockServer().getStock(code);
            health.stock = true;
        } catch (StockNotFoundException ex) {
            log.error(null, ex);
        }
        if (null != stockServerFactory.getStockHistoryServer(code)) {
            health.history = true;
        }
        if (null != stockServerFactory.getMarketServer().getMarket()) {
            health.market = true;
        }
        return health;
    }

    private String toHTML(Health health) {
        String html = "<html><body>";
        if (health == null || health.market == false) {
            html += "Index : <b>Failed</b><br/>";
        } else {
            html += "Index : <b>Success</b><br/>";
        }
        if (health == null || health.stock == false) {
            html += "Stock : <b>Failed</b><br/>";
        } else {
            html += "Stock : <b>Success</b><br/>";
        }
        if (health == null || health.history == false) {
            html += "History : <b>Failed</b><br/>";
        } else {
            html += "History : <b>Success</b><br/>";
        }
        html += "</body></html>";
        return html;
    }

    private void initSwingWorker() {
        SwingWorker worker = new SwingWorker<Health, Void>() {

            @Override
            public Health doInBackground() {
                return getServerHealth();
            }

            @Override
            public void done() {
                if (this.isCancelled()) {
                    return;
                }
                Health health = null;
                try {
                    health = get();
                } catch (InterruptedException ex) {
                    log.error(null, ex);
                } catch (ExecutionException ex) {
                    log.error(null, ex);
                } catch (CancellationException ex) {
                    log.error(null, ex);
                }
                if (health == null || health.isGood() == false) {
                    StockServerFactoryJRadioButton.this.setStatus(Status.Failed);
                } else {
                    StockServerFactoryJRadioButton.this.setStatus(Status.Success);
                }
                StockServerFactoryJRadioButton.this.setToolTipText(toHTML(health));
            }
        };
        worker.execute();
    }

    public StockServerFactory getStockServerFactory() {
        return this.stockServerFactory;
    }

    private void setStatus(Status status) {
        this.status = status;
        final String text = toReadableText(stockServerFactory);
        String label = "<html><table cellpadding=0><tr><td><img src=\"" + status.getFileName() + "\"/></td><td width=" + 3 + "><td>" + text + "</td></tr></table></html>";
        this.setText(label);
    }

    private static class Health {

        public boolean market = false;

        public boolean history = false;

        public boolean stock = false;

        public boolean isGood() {
            return market && history && stock;
        }
    }

    private enum Status {

        Busy(Utils.toHTMLFileSrcFormat(Utils.getExtraDataDirectory() + "spinner.gif")), Success(Utils.toHTMLFileSrcFormat(Utils.getExtraDataDirectory() + "network-transmit-receive.png")), Failed(Utils.toHTMLFileSrcFormat(Utils.getExtraDataDirectory() + "network-error.png"));

        Status(String fileName) {
            this.fileName = fileName;
        }

        public String getFileName() {
            return fileName;
        }

        private final String fileName;
    }

    private Status status = Status.Busy;

    private final StockServerFactory stockServerFactory;

    private static final Log log = LogFactory.getLog(StockServerFactoryJRadioButton.class);
}
