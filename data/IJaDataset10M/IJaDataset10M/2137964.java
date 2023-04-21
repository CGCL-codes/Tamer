// $Id: PurchaseJournalView.java,v 1.1.1.1 2005/10/22 06:55:55 vcrfix Exp $
package bestbooks3.view.controller;

importview.controller.awt.*;
import java.awt.event.*;
import javax.swing.*;

import bestbooks3.controller.*;

/** This class is used to inspect the contects of the
  * PurchaseJournal table. Use PurchaseJournalEntry to add
  * new entries to the PurchaseJournal table.
  *
  * @author P. Ingle
  * @version 1.0
  * @since 1.0
  *
 */
class PurchaseJournalView extends JInternalFrame implements ActionListener {
    public static final String rcsid = "$Id: PurchaseJournalView.java,v 1.1.1.1 2005/10/22 06:55:55 vcrfix Exp $";
    
	private JTextArea	area;
	private JButton		btnRefresh;
	private JPanel		pane;

	private PurchaseJournal	pj;

	private String		date;
	private Ledger		acct;
	private String		invDate;
	private String		terms;
	private boolean		chk;
	private float		netCost;

	public PurchaseJournalView() {
		super("Purchase Journal View",false,true,false,true);
		setSize(400,400);

		pane = new JPanel();

		// Account text area
		area = new JTextArea(15,10);
		pj = new PurchaseJournal();
		pj.read();
		while (pj.isThereMoreRecords() == true) {
			date = pj.getDate();
			acct = pj.getAccount();
			invDate = pj.getInvoiceDate();
			terms = pj.getTerms();
			chk = pj.isEntryPosted();
			netCost = pj.getNetCost();
			area.append(date+" "+acct.getAccountName()+" "+invDate+" "+terms+" "+netCost+"\n");
			pj.read();
		}
		pane.add(area);

		// Refresh button
		btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(this);
		pane.add(btnRefresh);

		// content pane
		setContentPane(pane);
	}

	public void actionPerformed(ActionEvent evt) {
		Object source = evt.getSource();
		if (source == btnRefresh) {
			area.setText("");
			pj.read();
			while (pj.isThereMoreRecords() == true) {
				date = pj.getDate();
				acct = pj.getAccount();
				invDate = pj.getInvoiceDate();
				terms = pj.getTerms();
				chk = pj.isEntryPosted();
				netCost = pj.getNetCost();
				area.append(date+" "+acct.getAccountName()+" "+invDate+" "+terms+" "+netCost+"\n");
				pj.read();
			}
		}
	}
}
