package ecom.util.shell;

import java.io.PrintStream;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import ecom.beans.ClientBean;
import ecom.beans.EcomCustomerRemote;

public class CustomerLoginCommand implements ShellCommand {

    private ShellContext _context = null;

    private EcomCustomerRemote ecombean;

    private ResourceBundle bundle;

    public CustomerLoginCommand(ShellContext context, EcomCustomerRemote ecombean) {
        this._context = context;
        this.ecombean = ecombean;
    }

    public String getName() {
        return "login";
    }

    public String getUsage() {
        return "login [email] [password]";
    }

    public String getShortDescription() {
        Locale locale = (Locale) _context.getVar("LANG");
        bundle = ResourceBundle.getBundle("ecom.util.shell.MessageBundle", locale);
        return bundle.getString("LoginHelp");
    }

    public void execute(String cmdline, PrintStream out, PrintStream err) {
        StringTokenizer st = new StringTokenizer(cmdline, " ");
        String format;
        st.nextToken();
        if (st.countTokens() != 0) {
            String email = st.nextToken();
            if (st.hasMoreTokens()) {
                String password = st.nextToken();
                ClientBean customer = ecombean.findClientByLogin(email);
                if (customer != null && customer.getPassword() != null && customer.getPassword().equals(password)) {
                    out.println(getWelcome(customer));
                    _context.setVar("ACCOUNT", customer.getAccount().getId());
                } else {
                    out.println(bundle.getString("LoginFailed"));
                }
            } else {
                out.println(bundle.getString("TypePassword"));
            }
        } else out.println(bundle.getString("TypeLoginPassword"));
    }

    private String getWelcome(ClientBean client) {
        StringBuffer sb = new StringBuffer();
        sb.append("\n-------------------------------------------------------------");
        sb.append("\n Welcome ");
        sb.append(client.getFirstName() + " " + client.getLastName());
        sb.append("\n" + new Date());
        sb.append("\n-------------------------------------------------------------");
        return sb.toString();
    }
}
