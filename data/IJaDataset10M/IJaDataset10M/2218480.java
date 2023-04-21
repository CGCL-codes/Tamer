package net.mogray.infinitypfm.ui.view.views;

import java.sql.SQLException;
import net.mogray.infinitypfm.client.InfinityPfm;
import net.mogray.infinitypfm.core.conf.MM;
import net.mogray.infinitypfm.core.data.Account;
import net.mogray.infinitypfm.core.data.DataFormatUtil;
import net.mogray.infinitypfm.core.data.Transaction;
import net.mogray.infinitypfm.ui.view.toolbars.RegisterToolbar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

/**
 * @author wayne
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class RegisterView extends BaseView {

    private Composite sh;

    private Table tblRegister = null;

    private Label lblBalance = null;

    private RegisterToolbar tbMain = null;

    private Label lblActName = null;

    private Label lblFrDate = null;

    private Label lblToDate = null;

    TableColumn tc3 = null;

    TableColumn tc4 = null;

    /**
	 * @param arg0
	 * @param arg1
	 */
    public RegisterView(Composite arg0, int arg1) {
        super(arg0, arg1);
        sh = arg0;
        LoadUI();
        LoadLayout();
    }

    protected void LoadUI() {
        lblBalance = new Label(this, SWT.NONE);
        lblBalance.setText(MM.PHRASES.getPhrase("2"));
        tbMain = new RegisterToolbar(this);
        DataFormatUtil formater = new DataFormatUtil();
        tblRegister = new Table(this, SWT.BORDER);
        tblRegister.addListener(SWT.MeasureItem, paintListener);
        tblRegister.addControlListener(onRegisterResize);
        Account act = InfinityPfm.qzMain.getTrMain().getSelectedAccount();
        lblActName = new Label(this, SWT.NONE);
        lblActName.setText(act.getActName());
        lblFrDate = new Label(this, SWT.NONE);
        lblToDate = new Label(this, SWT.NONE);
        if (act != null) {
            try {
                java.util.List tranList = MM.sqlMap.queryForList("getTransactionsForAccount", act.getActName());
                if (tranList != null) {
                    LoadColumns();
                    LoadTransactions(tranList);
                }
                Account account = (Account) MM.sqlMap.queryForObject("getAccountForName", act.getActName());
                lblBalance.setText(MM.PHRASES.getPhrase("2") + ": " + formater.getAmountFormatted(account.getActBalance()));
                tblRegister.setLinesVisible(true);
            } catch (SQLException se) {
                InfinityPfm.LogMessage(se.getMessage());
            }
        }
    }

    protected void LoadLayout() {
        this.setLayout(new FormLayout());
        FormData lblactnamedata = new FormData();
        lblactnamedata.top = new FormAttachment(0, 30);
        lblactnamedata.left = new FormAttachment(0, 20);
        lblActName.setLayoutData(lblactnamedata);
        FormData lblfrdatedata = new FormData();
        lblfrdatedata.top = new FormAttachment(0, 30);
        lblfrdatedata.left = new FormAttachment(40, 0);
        lblFrDate.setLayoutData(lblfrdatedata);
        FormData lbltodatedata = new FormData();
        lbltodatedata.top = new FormAttachment(0, 30);
        lbltodatedata.left = new FormAttachment(60, 0);
        lblToDate.setLayoutData(lbltodatedata);
        FormData lblbalancedata = new FormData();
        lblbalancedata.top = new FormAttachment(0, 30);
        lblbalancedata.right = new FormAttachment(100, -20);
        lblBalance.setLayoutData(lblbalancedata);
        FormData tblregisterdata = new FormData();
        tblregisterdata.top = new FormAttachment(lblBalance, 10);
        tblregisterdata.right = new FormAttachment(100, -20);
        tblregisterdata.left = new FormAttachment(0, 20);
        tblregisterdata.bottom = new FormAttachment(100, -20);
        tblRegister.setLayoutData(tblregisterdata);
        FormData tbmaindata = new FormData();
        tbmaindata.right = new FormAttachment(100, 0);
        tbMain.setLayoutDat(tbmaindata);
    }

    private void LoadTransactions(java.util.List tranList) {
        TableItem ti = null;
        Transaction tran = null;
        DataFormatUtil formater = new DataFormatUtil();
        final Display display = InfinityPfm.shMain.getDisplay();
        if (tranList != null) {
            for (int i = 0; i < tranList.size(); i++) {
                tran = (Transaction) tranList.get(i);
                ti = new TableItem(tblRegister, SWT.NONE);
                if (i % 2D == 0) {
                    ti.setBackground(0, display.getSystemColor(MM.ROW_BACKGROUND));
                    ti.setBackground(1, display.getSystemColor(MM.ROW_BACKGROUND));
                    ti.setBackground(2, display.getSystemColor(MM.ROW_BACKGROUND));
                    ti.setBackground(3, display.getSystemColor(MM.ROW_BACKGROUND));
                }
                ti.setText(0, tran.getTranDateFmt());
                ti.setText(1, tran.getTranMemo());
                if (tran.getTranAmount() > 0) {
                    ti.setText(2, formater.getAmountFormatted(Math.abs(tran.getTranAmount())));
                } else {
                    ti.setText(3, formater.getAmountFormatted(Math.abs(tran.getTranAmount())));
                }
            }
        }
    }

    private void LoadColumns() {
        TableColumn tc1 = new TableColumn(tblRegister, SWT.CENTER);
        TableColumn tc2 = new TableColumn(tblRegister, SWT.LEFT);
        tc3 = new TableColumn(tblRegister, SWT.CENTER);
        tc4 = new TableColumn(tblRegister, SWT.CENTER);
        tc1.setText(MM.PHRASES.getPhrase("24"));
        tc2.setText(MM.PHRASES.getPhrase("41"));
        tc3.setText(MM.PHRASES.getPhrase("48"));
        tc4.setText(MM.PHRASES.getPhrase("46"));
        tc1.setWidth(100);
        tc2.setWidth(320);
        tc3.setWidth(70);
        tc4.setWidth(70);
        tblRegister.setHeaderVisible(true);
    }

    Listener paintListener = new Listener() {

        public void handleEvent(Event event) {
            switch(event.type) {
                case SWT.MeasureItem:
                    {
                        event.height = 20;
                        break;
                    }
            }
        }
    };

    ControlAdapter onRegisterResize = new ControlAdapter() {

        public void controlResized(ControlEvent e) {
            tc4.setWidth((tc3.getWidth() + tc4.getWidth()) / 2);
            tc3.setWidth(tc4.getWidth());
        }
    };
}
