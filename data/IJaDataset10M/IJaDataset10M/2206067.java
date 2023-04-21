package sample.annotations;

/**
 * <code>BankService</code> sample implementation.
 *  
 * @author Mark St.Godard
 * @version $Id: BankServiceImpl.java,v 1.1 2005/09/05 04:38:44 markstg Exp $
 */
public class BankServiceImpl implements BankService {

    public float balance(String accountNumber) {
        return 42000000;
    }

    public String[] listAccounts() {
        return new String[] { "1", "2", "3" };
    }
}
