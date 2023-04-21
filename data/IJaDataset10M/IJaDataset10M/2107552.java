package org.cashforward.ui.action;

import java.util.Collection;
import org.cashforward.model.Payment;
import org.cashforward.ui.UIContext;
import org.cashforward.ui.adapter.PaymentServiceAdapter;
import org.cashforward.ui.internal.UILogger;
import org.cashforward.ui.payment.PaymentDetailPanel;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.util.HelpCtx;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle;
import org.openide.util.actions.CallableSystemAction;

/**
 *
 * Determines the next transaction for a scheduled payment.
 *
 * @author Bill
 */
public final class EnterScheduledPaymentAction extends CallableSystemAction {

    private Payment payment;

    private PaymentServiceAdapter paymentService = new PaymentServiceAdapter();

    private Lookup.Result paymentNotifier = UIContext.getDefault().lookupResult(Payment.class);

    public EnterScheduledPaymentAction() {
        super();
        setEnabled(payment != null);
        paymentNotifier.addLookupListener(new LookupListener() {

            public void resultChanged(LookupEvent event) {
                Lookup.Result r = (Lookup.Result) event.getSource();
                Collection c = r.allInstances();
                if (!c.isEmpty()) {
                    EnterScheduledPaymentAction.this.payment = (Payment) c.iterator().next();
                    setEnabled(!Payment.Occurence.NONE.name().equals(payment.getOccurence()));
                } else setEnabled(false);
            }
        });
    }

    public void performAction() {
        if (payment == null) return;
        PaymentDetailPanel paymentDetailPanel = new PaymentDetailPanel();
        Payment newPayment = new Payment(payment.getAmount(), payment.getPayee(), payment.getStartDate());
        newPayment.setOccurence(Payment.Occurence.NONE.name());
        paymentDetailPanel.setPayment(newPayment);
        DialogDescriptor dd = new DialogDescriptor(paymentDetailPanel, NbBundle.getMessage(EnterScheduledPaymentAction.class, "CTL_EnterScheduledPaymentAction"));
        dd.setModal(true);
        dd.setLeaf(true);
        dd.setOptionType(DialogDescriptor.OK_CANCEL_OPTION);
        Object result = DialogDisplayer.getDefault().notify(dd);
        boolean skipped = false;
        if (result == DialogDescriptor.OK_OPTION) {
            paymentDetailPanel.getPayment();
            if (paymentService.addOrUpdatePayment(newPayment)) {
                if (paymentService.skipNextPayment(payment)) {
                    skipped = true;
                }
            }
            if (!skipped) {
                UILogger.displayError(NbBundle.getMessage(EnterScheduledPaymentAction.class, "CTL_unable_to_enter"));
            }
        }
    }

    public String getName() {
        return NbBundle.getMessage(EnterScheduledPaymentAction.class, "CTL_EnterScheduledPaymentAction");
    }

    @Override
    protected String iconResource() {
        return "org/cashforward/ui/action/payment-next.png";
    }

    public HelpCtx getHelpCtx() {
        return HelpCtx.DEFAULT_HELP;
    }

    @Override
    protected boolean asynchronous() {
        return false;
    }
}
