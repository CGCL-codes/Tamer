package cz.cvut.phone.bp.facade;

import javax.ejb.Local;

/**
 *
 * @author 
 */
@Local
public interface BankParserFacadeLocal {

    public cz.cvut.phone.bp.dto.PaymentListDTO checkEmail(java.lang.String pop3, int pop3Port, java.lang.String pass, java.lang.String username, java.lang.String accountNumber);

    public cz.cvut.phone.bp.dto.PaymentListDTO parsePayments(java.lang.String filename, java.lang.String mime, byte[] data);
}
