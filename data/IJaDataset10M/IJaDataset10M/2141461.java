package ecom.util.shell;

import ecom.beans.EcomCustomerRemote;
import ecom.util.shell.ShellCommand;
import ecom.util.shell.ShellContext;
import java.io.PrintStream;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

public class CustomerCommitCommand implements ShellCommand {

    private ShellContext _context = null;

    private UserTransaction utx;

    private EcomCustomerRemote ecombean;

    private ResourceBundle bundle;

    public CustomerCommitCommand(ShellContext context, EcomCustomerRemote ecombean) {
        this._context = context;
        this.ecombean = ecombean;
    }

    public String getName() {
        return "commit";
    }

    public String getUsage() {
        return "commit";
    }

    public String getShortDescription() {
        Locale locale = (Locale) _context.getVar("LANG");
        bundle = ResourceBundle.getBundle("ecom.util.shell.MessageBundle", locale);
        return bundle.getString("CommitHelp");
    }

    public void execute(String cmdline, PrintStream out, PrintStream err) {
        Locale locale = (Locale) _context.getVar("LANG");
        bundle = ResourceBundle.getBundle("ecom.util.shell.MessageBundle", locale);
        StringTokenizer st = new StringTokenizer(cmdline, " ");
        st.nextToken();
        if (st.countTokens() == 0) {
            try {
                if (ecombean.getTransactionStatus() == Status.STATUS_NO_TRANSACTION) {
                    System.out.println(bundle.getString("NoTransaction"));
                    return;
                }
                ecombean.commit();
                ecombean.removeAllProductsFromCart();
                out.println(bundle.getString("CommitTransaction"));
            } catch (SystemException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (RollbackException e) {
                e.printStackTrace();
            } catch (HeuristicMixedException e) {
                e.printStackTrace();
            } catch (HeuristicRollbackException e) {
                e.printStackTrace();
            }
        } else {
            out.println(bundle.getString("WrongArgument"));
        }
    }
}
