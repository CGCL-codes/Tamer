package ecom.util.shell;

import ecom.beans.EcomAdminRemote;
import ecom.beans.ProductStoreBean;
import ecom.util.shell.ShellCommand;
import ecom.util.shell.ShellContext;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class AdminProductStoreCommand implements ShellCommand {

    private ShellContext _context = null;

    private EcomAdminRemote ecombean;

    private ResourceBundle bundle;

    public AdminProductStoreCommand(ShellContext context, EcomAdminRemote ecombean) {
        this._context = context;
        this.ecombean = ecombean;
    }

    public String getName() {
        return "productstore";
    }

    public String getUsage() {
        return "productstore [-list|-add \"name\" \"address\" \"zipCode\" \"city\" \"country\" balance|-remove productStoreId]";
    }

    public String getShortDescription() {
        Locale locale = (Locale) _context.getVar("LANG");
        bundle = ResourceBundle.getBundle("ecom.util.shell.MessageBundle", locale);
        return bundle.getString("ProductStoreHelp");
    }

    public void execute(String cmdline, PrintStream out, PrintStream err) {
        Locale locale = (Locale) _context.getVar("LANG");
        bundle = ResourceBundle.getBundle("ecom.util.shell.MessageBundle", locale);
        String[] pars = ParseArgs.split(cmdline, ' ');
        if (pars.length > 1) {
            if (pars.length == 2 && "-list".equals(pars[1])) {
                List productStores = ecombean.listAllProductsStore();
                for (Iterator iter = productStores.iterator(); iter.hasNext(); ) {
                    ProductStoreBean productStore = (ProductStoreBean) iter.next();
                    String address = productStore.getAddress();
                    String city = productStore.getCity();
                    String country = productStore.getCountry();
                    String name = productStore.getName();
                    System.out.println(name + ", " + address + " " + city + " " + country);
                }
            } else if (pars.length == 8 && "-add".equals(pars[1])) {
                String name = pars[2].substring(1, pars[2].length() - 1);
                String address = pars[3].substring(1, pars[3].length() - 1);
                String zipCode = pars[4].substring(1, pars[4].length() - 1);
                String city = pars[5].substring(1, pars[5].length() - 1);
                String country = pars[6].substring(1, pars[6].length() - 1);
                double balance = Double.parseDouble(pars[7]);
                ecombean.createProductStore(name, address, zipCode, city, country, balance);
            } else if (pars.length == 3 && "-remove".equals(pars[1])) {
                int productStoreId = Integer.parseInt(pars[2]);
                ecombean.removeProductStore(productStoreId);
            } else out.println(bundle.getString("WrongArgument"));
        } else out.println(bundle.getString("MissingArgument"));
    }
}
