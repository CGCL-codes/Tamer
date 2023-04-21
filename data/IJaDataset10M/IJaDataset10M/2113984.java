package cn.ekuma.epos.datalogic.define.dao.sale;

import cn.ekuma.epos.datalogic.define.dao.PaymentInfoTicketDAO;
import cn.ekuma.epos.datalogic.define.dao.ReceiptDAO;
import com.openbravo.data.basic.BasicException;
import com.openbravo.data.loader.I_Session;
import com.openbravo.data.loader.KeyBuilder;
import com.openbravo.data.loader.SentenceExecTransaction;
import com.openbravo.pos.bean.QuickPayment;

public class QuickPaymentDAO {

    private ReceiptDAO receiptDAO;

    private PaymentInfoTicketDAO paymentInfoDAO;

    private I_Session s;

    public QuickPaymentDAO(I_Session s, ReceiptDAO receiptDAO, PaymentInfoTicketDAO paymentInfoDAO) {
        this.receiptDAO = receiptDAO;
        this.paymentInfoDAO = paymentInfoDAO;
        this.s = s;
    }

    public int paymentMovementInsert(final QuickPayment obj) throws BasicException {
        return new SentenceExecTransaction(s) {

            public int execInTransaction(Object params) throws BasicException {
                receiptDAO.insert(obj.getReceipt());
                return paymentInfoDAO.insert(obj.getPaymentInfoTicket());
            }
        }.exec();
    }

    public int paymentMovementDelete(final String receiptId, final String paymentId) throws BasicException {
        return new SentenceExecTransaction(s) {

            public int execInTransaction(Object params) throws BasicException {
                receiptDAO.delete(KeyBuilder.getKey(receiptId));
                return paymentInfoDAO.delete(KeyBuilder.getKey(paymentId));
            }
        }.exec();
    }
}
