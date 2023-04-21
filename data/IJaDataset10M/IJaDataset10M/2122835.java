package ecom.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import ecom.beans.AccountBean;
import ecom.beans.BrandBean;
import ecom.beans.CurrencyType;
import ecom.beans.EcomAdminRemote;
import ecom.beans.ModelBean;
import ecom.beans.OutputType;
import ecom.beans.ProductStoreBean;

public class CreateStore extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private InitialContext ic;

    private EcomAdminRemote admin;

    private OutputType outputType;

    private CurrencyType currency;

    private Locale locale;

    private ResourceBundle messageBundle;

    private EcomAdminRemote ecombean;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateStore() {
        super();
    }

    private void initialise() {
        outputType = OutputType.TEXT;
        locale = Locale.ENGLISH;
        currency = CurrencyType.EUR;
        try {
            ic = new InitialContext();
            ecombean = (EcomAdminRemote) ic.lookup("EcomAdminEJB");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    /**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        initialise();
        String idStr = request.getParameter("id");
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String zipCode = request.getParameter("zipcode");
        String city = request.getParameter("city");
        String country = request.getParameter("country");
        String balanceStr = request.getParameter("balance");
        double balance = 0;
        try {
            balance = Double.parseDouble(balanceStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        ProductStoreBean store;
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            id = -1;
        }
        if (id < 0) {
            try {
                ecombean.begin();
                store = ecombean.createProductStore(name, address, zipCode, city, country, balance);
                ecombean.commit();
                this.getServletContext().getRequestDispatcher("/admin/modifystore.jsp?id=" + store.getRef()).include(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                ecombean.begin();
                store = ecombean.findProductStoreById(id);
                store.setName(name);
                store.setAddress(address);
                store.setZipCode(zipCode);
                store.setCity(city);
                store.setCountry(country);
                AccountBean account = store.getAccount();
                account.setbalance(balance);
                ecombean.updateAccount(account);
                ecombean.updateProductStore(store);
                ecombean.commit();
                this.getServletContext().getRequestDispatcher("/admin/modifystore.jsp?id=" + store.getRef()).include(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
