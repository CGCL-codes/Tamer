package org.jledger.ui.gj;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import org.jledger.model.IChartOfAccounts;
import org.jledger.model.ITransaction;
import org.jledger.ui.JLedgerConstants;
import org.jledger.ui.internal.JLedgerWorkbenchPlugin;
import org.jledger.util.JLedgerUIUtils;
import org.jledger.util.ObjectListModelZoneListener;
import org.jlense.uiworks.action.OpenSelectedActionDelegate;
import org.jlense.uiworks.content.ObjectListModel;
import org.jlense.uiworks.part.OutlookPart;
import org.jlense.uiworks.workbench.IWorkbenchSession;
import org.jlense.uiworks.workbench.PlatformUI;
import org.jlense.zone.ZoneConnection;
import org.jlense.zone.ZoneConnections;
import org.jlense.zone.ZoneException;
import org.jlense.zone.ZoneListener;

/**
 * Displays a list of all transactions made against the current chart of 
 * accounts.
 * 
 * @author ted stockwell [emorning@sourceforge.net]
 */
public class GeneralJournalView extends OutlookPart {

    static JLedgerWorkbenchPlugin PLUGIN = JLedgerWorkbenchPlugin.getDefault();

    ZoneListener _zoneListener = null;

    public JComponent createPartControl() {
        TransactionListPanel control = null;
        ZoneConnection connection = null;
        try {
            IWorkbenchSession session = PlatformUI.getWorkbench().getWorkbenchSession();
            IChartOfAccounts chart = (IChartOfAccounts) session.getAttribute(this, JLedgerConstants.SESSION_ATTRIBUTE_CHART_OF_ACCOUNTS);
            control = new TransactionListPanel();
            ObjectListModel model = control.getModel();
            model.clear();
            model.addAll(JLedgerUIUtils.getAllTransactions(chart));
            _zoneListener = new ObjectListModelZoneListener(model);
            connection = ZoneConnections.getDefault().getAgentConnection(chart.getSourceId());
            connection.addZoneListener(ITransaction.OBJECT_NAME, _zoneListener);
            getSite().setSelectionProvider(control.getSelectionProvider());
            control.registerKeyboardAction(new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    openSelection(event);
                }
            }, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        } catch (Exception x) {
            String msg = PLUGIN.bind("msg.could.not.display.clients");
            PLUGIN.logError(msg, x);
            PLUGIN.displayError(null, msg, x);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (Throwable t) {
                }
            }
        }
        return (control != null) ? control : new JPanel();
    }

    protected void finalize() {
        if (_zoneListener != null) {
            ZoneConnection connection = null;
            try {
                connection = ZoneConnections.getDefault().getZoneConnection(this);
                connection.removeZoneListener(ITransaction.OBJECT_NAME, _zoneListener);
            } catch (ZoneException x) {
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (Throwable t) {
                    }
                }
            }
        }
    }

    protected void openSelection(ActionEvent event) {
        OpenSelectedActionDelegate openSelectionAction = new OpenSelectedActionDelegate();
        openSelectionAction.init(getSite().getWorkbenchWindow());
        openSelectionAction.run(null, event);
    }
}
