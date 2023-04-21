// $Id: BestBooksView.java,v 1.1.1.1 2005/10/22 06:55:54 vcrfix Exp $
package bestbooks3.view.controller;

importview.controller.awt.*;
import java.awt.event.*;
import javax.swing.*;

import bestbooks3.controller.*;
import bestbooks3.reports.*;

/** The class which constructs the Menu bar and associated
  * menu items that invoke the various View/Entry objects.
  *
  * @author P. Ingle
  * @version 1.0
  * @since 1.0
  *
 */
public class BestBooksView extends JFrame implements ActionListener {
    public static final String rcsid = "$Id: BestBooksView.java,v 1.1.1.1 2005/10/22 06:55:54 vcrfix Exp $";

    private JDesktopPane desktop;

    public BestBooksView(String version) {
        super("BestBooks for Java - "+version);
        setSize(300,1);

        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                  screenSize.width  - inset*2,
                  screenSize.height - inset*2);

        //Set up the GUI.
        desktop = new JDesktopPane(); //a specialized layered pane
        setContentPane(desktop);

        // create menu bar and add it to the frame
        MenuBar mbar = new MenuBar();
        setMenuBar(mbar);

        // create the menu items
        Menu options = new Menu("Options");
        Menu journals = new Menu("Journals");
        Menu reports = new Menu("Reports");

        MenuItem item1, item2, item3, item4, item5, item6;
        MenuItem item7, item8, item9, item10, item11, item12;
        MenuItem item13, item14, item15, item16;

        options.add(item1 = new MenuItem("Chart of Accounts"));
        options.add(item2 = new MenuItem("Ledger"));
        options.add(item3 = new MenuItem("-"));
        options.add(item4 = new MenuItem("Exit"));

        Menu sub1 = new Menu("General");
        sub1.add(item5 = new MenuItem("GL View"));
        sub1.add(item6 = new MenuItem("GL Entry"));
        journals.add(sub1);

        Menu sub5 = new Menu("Sales");
        sub5.add(item14 = new MenuItem("SJ View"));
        sub5.add(item15 = new MenuItem("SJ Entry"));
        journals.add(sub5);

        Menu sub2 = new Menu("Purchases");
        sub2.add(item7 = new MenuItem("PJ View"));
        sub2.add(item8 = new MenuItem("PJ Entry"));
        journals.add(sub2);

        Menu sub3 = new Menu("Cash Receipts");
        sub3.add(item16 = new MenuItem("Cash Entry"));
        sub3.add(item9 = new MenuItem("CR View"));
        sub3.add(item10 = new MenuItem("CR Entry"));
        journals.add(sub3);

        Menu sub4 = new Menu("Cash Payments");
        sub4.add(item11 = new MenuItem("CP View"));
        sub4.add(item12 = new MenuItem("CP Entry"));
        journals.add(sub4);

        reports.add(item13 = new MenuItem("Balance Sheet"));

        mbar.add(options);
        mbar.add(journals);
        mbar.add(reports);

        // register the menu items to receive events
        item1.addActionListener(this);
        item2.addActionListener(this);
        item4.addActionListener(this);
        item5.addActionListener(this);
        item6.addActionListener(this);
        item7.addActionListener(this);
        item8.addActionListener(this);
        item9.addActionListener(this);
        item10.addActionListener(this);
        item11.addActionListener(this);
        item12.addActionListener(this);
        item13.addActionListener(this);
        item14.addActionListener(this);
        item15.addActionListener(this);
        item16.addActionListener(this);
    }

    public void actionPerformed(ActionEvent ae) {
        String arg = (String)ae.getActionCommand();

        if (arg.equals("Chart of Accounts")) {
            CoaView coaView = new CoaView();
            coaView.setVisible(true);
            desktop.add(coaView);
        } else if (arg.equals("Ledger")) {
            LedgerView lv = new LedgerView();
            lv.setVisible(true);
            desktop.add(lv);
        } else if (arg.equals("GL View")) {
            GeneralJournalView glv = new GeneralJournalView();
            glv.setVisible(true);
            desktop.add(glv);
        } else if (arg.equals("GL Entry")) {
            GeneralJournalEntry gle = new GeneralJournalEntry();
            gle.setVisible(true);
            desktop.add(gle);
        } else if (arg.equals("SJ View")) {
            SalesJournalView sjv = new SalesJournalView();
            sjv.setVisible(true);
            desktop.add(sjv);
        } else if (arg.equals("SJ Entry")) {
            SalesJournalEntry sje = new SalesJournalEntry();
            sje.setVisible(true);
            desktop.add(sje);
        } else if (arg.equals("PJ View")) {
            PurchaseJournalView pjv = new PurchaseJournalView();
            pjv.setVisible(true);
            desktop.add(pjv);
        } else if (arg.equals("PJ Entry")) {
            PurchaseJournalEntry pje = new PurchaseJournalEntry();
            pje.setVisible(true);
            desktop.add(pje);
        } else if (arg.equals("CR View")) {
            CashReceiptsJournalView crjv = new CashReceiptsJournalView();
            crjv.setVisible(true);
            desktop.add(crjv);
        } else if (arg.equals("Cash Entry")) {

        } else if (arg.equals("CR Entry")) {
            CashReceiptsJournalEntry crje = new CashReceiptsJournalEntry();
            crje.setVisible(true);
            desktop.add(crje);
        } else if (arg.equals("CP View")) {
            CashPaymentsJournalView cpjv = new CashPaymentsJournalView();
            cpjv.setVisible(true);
            desktop.add(cpjv);
        } else if (arg.equals("CP Entry")) {
            CashPaymentsJournalEntry cpje = new CashPaymentsJournalEntry();
            cpje.setVisible(true);
            desktop.add(cpje);
        } else if (arg.equals("Balance Sheet")) {
            /**
             * Balance is no longer a GUI frame, but a controller
             * returning the balance sheet formatted data to the
             * caller.
             */
//            BalanceSheet bs = new BalanceSheet();
//            bs.setVisible(true);
//            desktop.add(bs);
        } else if (arg.equals("Exit")) {
            System.exit(0);
        }
    }
}

