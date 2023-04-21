package net.sourceforge.jasa.view;

import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import net.sourceforge.jabm.event.SimEvent;
import net.sourceforge.jabm.report.Report;
import net.sourceforge.jasa.event.MarketEvent;
import net.sourceforge.jasa.event.OrderPlacedEvent;
import net.sourceforge.jasa.event.TransactionExecutedEvent;
import net.sourceforge.jasa.market.Order;
import net.sourceforge.jasa.market.auctioneer.Auctioneer;

public class OrderBookView implements Report, TableModel, InitializingBean {

    protected JFrame frame;

    protected JTable table;

    protected LinkedList<TableModelListener> listeners = new LinkedList<TableModelListener>();

    protected Auctioneer auctioneer;

    Map<Object, Number> variableBindings;

    protected int currentDepth;

    protected List<Order> bids = new ArrayList<Order>(0);

    protected List<Order> asks = new ArrayList<Order>(0);

    protected int maxDepth;

    DecimalFormat priceFormat = new DecimalFormat("#00000.00");

    DecimalFormat qtyFormat = new DecimalFormat("#00000");

    public static final int NUM_COLUMNS = 4;

    public OrderBookView() {
    }

    public DecimalFormat getPriceFormat() {
        return priceFormat;
    }

    public void setPriceFormat(DecimalFormat format) {
        this.priceFormat = format;
    }

    public DecimalFormat getQtyFormat() {
        return qtyFormat;
    }

    public void setQtyFormat(DecimalFormat qtyFormat) {
        this.qtyFormat = qtyFormat;
    }

    public void notifyTableChanged() {
        for (TableModelListener l : listeners) {
            l.tableChanged(new TableModelEvent(this));
        }
    }

    public void update() {
        this.bids = auctioneer.getUnmatchedBids();
        this.asks = auctioneer.getUnmatchedAsks();
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Override
    public int getRowCount() {
        this.currentDepth = Math.max(asks.size(), bids.size());
        return Math.max(maxDepth, currentDepth);
    }

    @Override
    public int getColumnCount() {
        return NUM_COLUMNS;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch(columnIndex) {
            case 0:
                return "Bid Price";
            case 1:
                return "Bid Qty";
            case 2:
                return "Ask Price";
            case 3:
                return "Ask Qty";
        }
        return "NA";
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch(columnIndex) {
            case 0:
                return rowIndex < bids.size() ? priceFormat.format(bids.get(rowIndex).getPrice()) : "";
            case 1:
                return rowIndex < bids.size() ? qtyFormat.format(bids.get(rowIndex).getQuantity()) : "";
            case 2:
                return rowIndex < asks.size() ? priceFormat.format(asks.get(rowIndex).getPrice()) : "";
            case 3:
                return rowIndex < asks.size() ? qtyFormat.format(asks.get(rowIndex).getQuantity()) : "";
        }
        return "";
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }

    @Override
    public void eventOccurred(SimEvent event) {
        if (event instanceof MarketEvent) {
            this.auctioneer = ((MarketEvent) event).getAuction().getAuctioneer();
            update();
            notifyTableChanged();
        }
    }

    @Override
    public Map<Object, Number> getVariableBindings() {
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        frame = new JFrame();
        frame.setTitle("JASA: Order Book");
        table = new JTable(this);
        table.setPreferredSize(new Dimension(400, 200));
        frame.add(table);
        frame.pack();
        frame.setVisible(true);
    }
}
