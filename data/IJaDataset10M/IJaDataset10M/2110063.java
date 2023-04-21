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
import ecom.beans.BrandBean;
import ecom.beans.CurrencyType;
import ecom.beans.EcomAdminRemote;
import ecom.beans.OutputType;

public class CreateBrand extends HttpServlet {

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
    public CreateBrand() {
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
        out.print("init");
        String idStr = request.getParameter("id");
        String name = request.getParameter("name");
        String imagePath = request.getParameter("imagepath");
        BrandBean brand;
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            id = -1;
        }
        out.print("id parsed");
        if (id < 0) {
            try {
                ecombean.begin();
                brand = ecombean.createBrand(name, imagePath);
                ecombean.commit();
                this.getServletContext().getRequestDispatcher("/admin/modifybrand.jsp?id=" + brand.getId()).forward(request, response);
            } catch (Exception e) {
                out.print(e.getMessage());
                e.printStackTrace();
            }
        } else {
            try {
                ecombean.begin();
                brand = ecombean.findBrandById(id);
                brand.setName(name);
                brand.setImagePath(imagePath);
                ecombean.updateBrand(brand);
                ecombean.commit();
                this.getServletContext().getRequestDispatcher("/admin/modifybrand.jsp?id=" + brand.getId()).forward(request, response);
            } catch (Exception e) {
                out.print(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
