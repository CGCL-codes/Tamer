package org.sf.jspread.marketdata.inetats.gui;

import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.swing.*;
import org.w3c.dom.*;
import org.sf.jspread.marketdata.inetats.xml.*;
import org.sf.jspread.marketdata.inetats.gui.book.*;
import org.sf.jspread.marketdata.inetats.gui.sales.*;
import org.sf.jspread.marketdata.inetats.gui.stats.*;
import org.sf.jspread.marketdata.inetats.gui.toplist.*;

public class InetAtsXmlViewer extends JFrame implements Runnable {

    private JPanel inpanel;

    private JPanel rtpanel;

    private JLabel label;

    private JTextField ric;

    private JButton refresh;

    private Document document;

    private Socket inetSocket;

    private JTabbedPane tabPane;

    private String server;

    private String stock;

    private InetAtsXmlParser parser;

    private InetAtsXmlViewPanel bookPanel;

    private InetAtsXmlViewPanel salesPanel;

    private InetAtsXmlViewPanel statsPanel;

    private InetAtsXmlViewPanel topListPanel;

    private Thread inetThread;

    private boolean STOP;

    private String file1;

    private String file2;

    private String file3;

    private String file4;

    public InetAtsXmlViewer(String[] args) {
        tabPane = new JTabbedPane();
        inpanel = new JPanel();
        rtpanel = new JPanel();
        ric = new JTextField("CSCO");
        stock = ric.getText().trim();
        label = new JLabel("  Input Stock :");
        refresh = new JButton("Refresh");
        refresh.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                stock = ric.getText().trim();
                refresh();
            }
        });
        rtpanel.setLayout(new BorderLayout());
        inpanel.setLayout(new GridLayout());
        inpanel.add(label);
        inpanel.add(ric);
        inpanel.add(refresh);
        rtpanel.add(inpanel, BorderLayout.WEST);
        parser = new InetAtsXmlParser();
        server = new String();
        server = "http://xml.island.com/ws/xml/book.xml?token=PaEFechp8h62QdR7&symbol=CSCO";
        document = getXmlDocument(server);
        if (document != null) {
            InetAtsXmlBookTreeModel trmodel = new InetAtsXmlBookTreeModel(document);
            InetAtsXmlBookTableModel tamodel = new InetAtsXmlBookTableModel(document);
            bookPanel = new InetAtsXmlViewPanel(trmodel, tamodel);
        }
        server = "http://xml.island.com/ws/xml/timeandsales.xml?token=PaEFechp8h62QdR7&symbol=CSCO";
        document = getXmlDocument(server);
        if (document != null) {
            InetAtsXmlSalesTreeModel trmodel = new InetAtsXmlSalesTreeModel(document);
            InetAtsXmlSalesTableModel tamodel = new InetAtsXmlSalesTableModel(document);
            salesPanel = new InetAtsXmlViewPanel(trmodel, tamodel);
        }
        server = "http://xml.island.com/ws/xml/stats.xml?token=PaEFechp8h62QdR7&symbol=CSCO";
        document = getXmlDocument(server);
        if (document != null) {
            InetAtsXmlStatsTreeModel trmodel = new InetAtsXmlStatsTreeModel(document);
            InetAtsXmlStatsTableModel tamodel = new InetAtsXmlStatsTableModel(document);
            statsPanel = new InetAtsXmlViewPanel(trmodel, tamodel);
        }
        server = "http://xml.island.com/ws/xml/toplist.xml?token=PaEFechp8h62QdR7";
        document = getXmlDocument(server);
        if (document != null) {
            InetAtsXmlTopListTreeModel trmodel = new InetAtsXmlTopListTreeModel(document);
            InetAtsXmlTopListTableModel tamodel = new InetAtsXmlTopListTableModel(document);
            topListPanel = new InetAtsXmlViewPanel(trmodel, tamodel);
        }
        tabPane.add("Book", bookPanel);
        tabPane.add("Sales", salesPanel);
        tabPane.add("Stats", statsPanel);
        tabPane.add("TopList", topListPanel);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("INET ECN Data");
        getContentPane().add(rtpanel, BorderLayout.NORTH);
        getContentPane().add(tabPane, BorderLayout.CENTER);
        setSize(900, 750);
        show();
        inetThread = new Thread(this);
        inetThread.setName("InetThread");
        inetThread.start();
    }

    private Document getXmlDocument(String url) {
        try {
            URL u = new URL(url);
            URLConnection uc = u.openConnection();
            HttpURLConnection connection = (HttpURLConnection) uc;
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            document = parser.parse(connection.getInputStream());
            connection.disconnect();
            return document;
        } catch (Exception e) {
            System.out.println("Exception Reading XML from " + url);
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void stop() {
        STOP = true;
    }

    public void refresh() {
        server = "http://xml.island.com/ws/xml/book.xml?token=PaEFechp8h62QdR7&symbol=" + stock;
        document = getXmlDocument(server);
        bookPanel.refresh(document);
        server = "http://xml.island.com/ws/xml/timeandsales.xml?token=PaEFechp8h62QdR7&symbol=" + stock;
        document = getXmlDocument(server);
        salesPanel.refresh(document);
        server = "http://xml.island.com/ws/xml/stats.xml?token=PaEFechp8h62QdR7&symbol=" + stock;
        document = getXmlDocument(server);
        statsPanel.refresh(document);
        server = "http://xml.island.com/ws/xml/toplist.xml?token=PaEFechp8h62QdR7";
        document = getXmlDocument(server);
        topListPanel.refresh(document);
    }

    public void run() {
        parser = new InetAtsXmlParser();
        while (!STOP) {
            try {
                Thread.sleep(60000);
            } catch (Exception e) {
            }
            refresh();
        }
    }
}
