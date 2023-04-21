package traviaut.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import traviaut.data.ServerInfo;
import traviaut.data.ServerParser;
import traviaut.data.ServerTotals;
import traviaut.data.VillageParser;

public class ServerPanel extends javax.swing.JPanel {

    private final int[] stamp = new int[] { -1 };

    private final List<VillagePanel> panels = new ArrayList<VillagePanel>();

    private final StockPanel total = new StockPanel();

    private final StockPanel product = new StockPanel();

    private final VillagePanel.BuildPanel minBuild = new VillagePanel.BuildPanel();

    public final TraviGUI parent;

    public ServerPanel(TraviGUI p) {
        parent = p;
        setLayout(new java.awt.GridBagLayout());
        total.setColor(new Color(0, 0, 192));
        product.setColor(Color.GRAY);
    }

    public void disconnectLabels(Tickers tickers) {
        for (VillagePanel vill : panels) vill.disconnectLabels(tickers);
        total.disconnectLabels(tickers);
        minBuild.update(tickers, null);
    }

    public void update(ServerParser server) {
        ServerInfo info = server.getData(stamp);
        if (info != null) {
            List<VillageParser> vills = info.getVillageData();
            if (vills.size() != panels.size()) relayout(vills);
        }
        info = server.getData();
        updateData(info);
    }

    private void updateData(ServerInfo info) {
        List<VillageParser> vills = info.getVillageData();
        for (int i = 0; i < vills.size(); i++) {
            panels.get(i).update(vills.get(i));
        }
        ServerTotals totals = info.getTotals();
        if (totals.stock == null) return;
        total.update(parent.tickers, totals.stock);
        product.update(totals.product);
        minBuild.update(parent.tickers, totals.getMinBuild());
    }

    private void relayout(List<VillageParser> vills) {
        int rows = 0;
        disconnectLabels(parent.tickers);
        panels.clear();
        removeAll();
        for (VillageParser p : vills) {
            panels.add(new VillagePanel(this, rows, p));
            rows += 3;
        }
        add(new JLabel("totals: "), new GBCreator(0, rows).east().gb);
        add(total, new GBCreator(1, rows).fill().gb);
        add(product, new GBCreator(1, rows + 1).fill().gb);
        minBuild.addBuild(this, rows);
    }
}
