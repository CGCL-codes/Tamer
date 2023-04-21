package net.simapro.connector.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.simapro.connector.TProcessType;
import net.simapro.connector.model.MethodDescriptor;
import net.simapro.connector.model.ProductCategory;
import net.simapro.connector.model.ProductDesignCategories;
import net.simapro.connector.model.ProductDesignResult;

/**
 * Server response for the calculation request of a product design project.
 * 
 * @author Michael Srocka
 * 
 */
public class CalculateDesignServlet extends HttpServlet {

    private static final long serialVersionUID = 3792605924302465141L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        calculate(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        calculate(req, resp);
    }

    private void calculate(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String methodName = req.getParameter("methodCombo");
        ProductDesignCategories resultCategories = (ProductDesignCategories) req.getSession().getAttribute("resultCategories");
        String productName = req.getParameter("productNameText");
        productName = productName != null ? productName : "";
        if (methodName == null || resultCategories == null || resultCategories.isEmpty()) {
            req.getSession().setAttribute("clearCache", false);
            req.getSession().setAttribute("errorString", "No method or process selected.");
            RequestDispatcher dispatcher = req.getSession().getServletContext().getRequestDispatcher("/project.con");
            dispatcher.forward(req, resp);
        } else {
            try {
                List<MethodDescriptor> methods = Server.getInstance().getMethodDescriptors(req.getSession().getId());
                MethodDescriptor method = null;
                Iterator<MethodDescriptor> it = methods.iterator();
                while (method == null && it.hasNext()) {
                    MethodDescriptor md = it.next();
                    if (methodName.equals(md.getName())) {
                        method = md;
                    }
                }
                if (method == null) throw new Exception("Unknown method " + methodName);
                List<ProductCategory> categories = new ArrayList<ProductCategory>();
                ProductCategory category = new ProductCategory(TProcessType.ptEnergy);
                category.getDescriptors().addAll(resultCategories.getEnergy());
                categories.add(category);
                category = new ProductCategory(TProcessType.ptMaterial);
                category.getDescriptors().addAll(resultCategories.getMaterials());
                categories.add(category);
                category = new ProductCategory(TProcessType.ptProcessing);
                category.getDescriptors().addAll(resultCategories.getProcessing());
                categories.add(category);
                category = new ProductCategory(TProcessType.ptTransport);
                category.getDescriptors().addAll(resultCategories.getTransport());
                categories.add(category);
                category = new ProductCategory(TProcessType.ptUse);
                category.getDescriptors().addAll(resultCategories.getUse());
                categories.add(category);
                category = new ProductCategory(TProcessType.ptWasteTreatment);
                category.getDescriptors().addAll(resultCategories.getWaste());
                categories.add(category);
                ProductDesignResult result = Server.getInstance().calculate(req.getSession().getId(), productName, categories, method);
                req.getSession().setAttribute("projectResult", result);
                RequestDispatcher dispatcher = req.getSession().getServletContext().getRequestDispatcher("/project_result.con");
                dispatcher.forward(req, resp);
            } catch (Exception e) {
                req.getSession().setAttribute("error.con", e);
                RequestDispatcher dispatcher = req.getSession().getServletContext().getRequestDispatcher("/error.con");
                dispatcher.forward(req, resp);
            }
        }
    }
}
