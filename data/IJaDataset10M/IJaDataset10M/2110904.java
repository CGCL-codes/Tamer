package net.adrianromero.tpv.payment;

import net.adrianromero.format.Formats;
import org.posper.hibernate.Payment;

/**
 * Modified payment dialog to pay open notes.
 * Only the open amount will be shown and an additional payment record
 * will be created.
 * 
 * @author Hans
 */
public class JPaymentNoteSelect extends JPaymentSelect {

    private double m_ticketTotal;

    private int m_newPayments;

    JPaymentNoteSelect(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
    }

    @Override
    protected void initVariables() {
        m_dTotal = m_ticket.calculateDebt();
        m_ticketTotal = m_ticket.calculateTotal();
        m_newPayments = 0;
    }

    @Override
    protected void printState() {
        Double remaining = new Double(m_ticketTotal - m_ticket.calculateTotalPayments() + m_ticket.calculateDebt());
        m_jRemainingEuros.setText(Formats.CURRENCY.formatValue(remaining));
        m_jButtonRemove.setEnabled(m_newPayments > 0);
        m_jTabPayment.setSelectedIndex(0);
        ((JPaymentInterface) m_jTabPayment.getSelectedComponent()).activate(m_sTransaction, remaining);
    }

    @Override
    protected void stateChangedEvent() {
        ((JPaymentInterface) m_jTabPayment.getSelectedComponent()).activate(m_sTransaction, m_ticketTotal - m_ticket.calculateTotalPayments() + m_ticket.calculateDebt());
    }

    @Override
    protected void addActionEvent() {
        Payment returnPayment = ((JPaymentInterface) m_jTabPayment.getSelectedComponent()).executePayment();
        if (returnPayment != null) {
            m_ticket.addPayment(returnPayment);
            addNotePayment(returnPayment);
            m_newPayments++;
            printState();
        }
    }

    private void addNotePayment(Payment returnPayment) {
        Payment paperout = new Payment();
        paperout.setMethod("paperout");
        paperout.setAmount(-returnPayment.getAmount());
        m_ticket.addPayment(paperout);
        m_newPayments++;
    }

    @Override
    protected void disposeOK(String sresourcename) {
        Payment returnPayment = ((JPaymentInterface) m_jTabPayment.getSelectedComponent()).executePayment();
        if (returnPayment != null) {
            m_ticket.retrievePaymentInfo();
            m_ticket.addPayment(returnPayment);
            addNotePayment(returnPayment);
            m_sresourcename = sresourcename;
            dispose();
        }
    }

    @Override
    protected void removeActionEvent() {
        m_ticket.getPayments().remove(m_ticket.getPayments().size() - 1).setTicket(null);
        m_newPayments--;
        printState();
    }

    @Override
    protected void cancelAction() {
        resetPayments();
        dispose();
    }

    private void resetPayments() {
        while (m_newPayments > 0) {
            removeActionEvent();
        }
    }
}
