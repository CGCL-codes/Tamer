package com.sobek.web.shop.admin;

import com.sobek.shop.dao.DataManager;
import com.sobek.shop.dao.Feature;
import com.sobek.shop.dao.Image;
import com.sobek.shop.dao.Operator;
import com.sobek.shop.dao.Product;
import com.sobek.shop.dao.ProductGroup;
import com.sobek.shop.dao.SEOTags;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.*;
import javax.servlet.http.*;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 *
 * @author Alexandra Sobek
 * @version
 */
public class AdminServlet extends HttpServlet {

    private ApplicationContext context = null;

    private DataManager dataManager = null;

    public static final String GROUP_PATH = "shop.productgroup.images.dir";

    public static final String PRODUCT_PREVIEW_PATH = "shop.product.images.normal.dir";

    public static final String PRODUCT_NORMAL_PATH = "shop.product.images.preview.dir";

    public static final int MAX_FEATURES = 5;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        context = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());
        dataManager = (DataManager) context.getBean("dataManager");
    }

    /** Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(true);
        Boolean authentificated = (session.getAttribute("authToken") != null);
        Map params = request.getParameterMap();
        if (params.containsKey("loginState")) {
            if (request.getParameter("loginState").equals("logout")) {
                request.getSession().invalidate();
                this.getServletContext().getRequestDispatcher("/admin/Login.jsp").forward(request, response);
            } else processLoginState(request, response);
        } else if (params.containsKey("editAdminState")) {
            processAdminEditState(request, response);
        } else if (!authentificated) {
            this.getServletContext().getRequestDispatcher("/admin/Login.jsp").forward(request, response);
        } else if (params.containsKey("editProductGroupState")) {
            processProductGroupEditState(request, response);
        } else if (params.containsKey("editProductState")) {
            processProductEditState(request, response);
        } else if (params.containsKey("action")) {
            if (request.getParameter("action").equals("delete")) {
                processDelete(request, response);
            } else if (request.getParameter("action").equals("deleteImage")) {
                processDeleteImage(request, response);
            }
        }
    }

    private void processAdminEditState(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String state = request.getParameter("editAdminState");
        String username = request.getParameter("username");
        String firstName = request.getParameter("firstName");
        String secondName = request.getParameter("secondName");
        String password = request.getParameter("password");
        if (state.equalsIgnoreCase("reset")) {
            dataManager.removeOperator(dataManager.getOperatorList().get(0));
            this.getServletContext().getRequestDispatcher("/admin/EditAdmin.jsp").forward(request, response);
        } else {
            Operator operator = new Operator();
            if (dataManager.hasOperators()) {
                operator = dataManager.getOperatorList().get(0);
            }
            operator.setAdmin(true);
            operator.setUsername(username);
            operator.setFirstName(firstName);
            operator.setSecondName(secondName);
            operator.setPassword(password);
            dataManager.saveOrUpdateOperator(operator);
            this.getServletContext().getRequestDispatcher("/admin/Login.jsp").forward(request, response);
        }
    }

    private void processDeleteImage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ImageType imageType = ImageType.valueOf(request.getParameter("context"));
        Long contextID = Long.valueOf(request.getParameter("contextID"));
        if (imageType.equals(ImageType.Product)) {
            Product product = dataManager.getProduct(contextID);
            dataManager.deleteImage(dataManager.getImage(product.getImageID()));
            product.setImageID(null);
            dataManager.updateProduct(product);
        } else if (imageType.equals(ImageType.ProductPreview)) {
            Product product = dataManager.getProduct(contextID);
            dataManager.deleteImage(dataManager.getImage(product.getPreviewImageID()));
            product.setPreviewImageID(null);
            dataManager.updateProduct(product);
        } else {
            ProductGroup group = dataManager.getProductGroup(contextID);
            dataManager.deleteImage(dataManager.getImage(group.getImageID()));
            group.setImageID(null);
            dataManager.updateProductGroup(group);
        }
        String parentGroupID = "";
        if (request.getParameter("parentGroupID") != null) {
            parentGroupID = request.getParameter("parentGroupID");
        }
        if (imageType.equals(ImageType.Product) || imageType.equals(ImageType.ProductPreview)) {
            this.getServletContext().getRequestDispatcher("/admin/ProductEdit.jsp?productID=" + request.getParameter("contextID") + "&amp;parentGroupID=" + parentGroupID).forward(request, response);
        } else {
            this.getServletContext().getRequestDispatcher("/admin/EditProductGroup.jsp?groupID=" + request.getParameter("contextID") + "&amp;parentGroupID=" + parentGroupID).forward(request, response);
        }
    }

    private void processDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map params = request.getParameterMap();
        if (params.containsKey("groupID")) {
            dataManager.deleteProductGroup(dataManager.getProductGroup(Long.parseLong(request.getParameter("groupID"))));
        } else if (params.containsKey("productID")) {
            dataManager.deleteProduct(dataManager.getProduct(Long.parseLong(request.getParameter("productID"))));
        }
        this.getServletContext().getRequestDispatcher("/admin/ProductGroupOverview.jsp?parentGroupID=" + request.getParameter("parentGroupID")).forward(request, response);
    }

    private void processLoginState(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = (String) request.getParameter("username");
        String password = (String) request.getParameter("password");
        if (!dataManager.hasOperators()) {
            this.getServletContext().getRequestDispatcher("/admin/EditAdmin.jsp").forward(request, response);
        } else {
            if (dataManager.isOperator(username, password)) {
                request.getSession(true).setAttribute("authToken", "true");
                this.getServletContext().getRequestDispatcher("/admin/ProductGroupOverview.jsp").forward(request, response);
            } else {
                request.setAttribute("ERROR_CODE", "Falsches Password bzw. Benutzername!");
                this.getServletContext().getRequestDispatcher("/admin/Login.jsp").forward(request, response);
            }
        }
    }

    private void processProductEditState(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map params = request.getParameterMap();
        String state = request.getParameter("editProductState");
        Long productID = null;
        Product product = null;
        if (state.equals("new")) {
            product = new Product();
        } else {
            productID = Long.parseLong(request.getParameter("productID"));
            product = dataManager.getProduct(productID);
        }
        product.setName(request.getParameter("name"));
        if (request.getParameter("price") != null && request.getParameter("price").length() > 0) {
            product.setPrice(Double.parseDouble(request.getParameter("price")));
        }
        product.setShortdescription(request.getParameter("shortdescription"));
        product.setDescription(request.getParameter("description"));
        SEOTags seoTags = product.getSeoTags();
        if (seoTags == null) {
            seoTags = new SEOTags();
        }
        seoTags.setTitle(request.getParameter("seotitle"));
        seoTags.setKeywords(request.getParameter("seokeywords"));
        seoTags.setDescription(request.getParameter("seodescription"));
        product.setSeoTags(seoTags);
        if (request.getParameter("onlineStatus") != null) {
            product.setOnlineStatus(true);
        } else {
            product.setOnlineStatus(false);
        }
        if (request.getParameter("rank") != null && request.getParameter("rank").length() > 0 && isNumber(request.getParameter("rank"))) {
            product.setRank(Integer.parseInt(request.getParameter("rank")));
        } else {
            product.setRank(null);
        }
        if (request.getParameter("parentID") != null && !request.getParameter("parentID").equals("null")) {
            Long parentID = Long.parseLong(request.getParameter("parentID"));
            product.setProductGroupId(dataManager.getProductGroup(parentID).getId());
        }
        List<Feature> features = new ArrayList<Feature>();
        for (int i = 0; i < MAX_FEATURES; i++) {
            String title = request.getParameter("feature_title_" + i);
            String enumerations = request.getParameter("feature_enumerations_" + i);
            if (title != null && title.length() > 0 && enumerations != null && enumerations.length() > 0) {
                Feature feature = new Feature();
                feature.setTitle(title);
                feature.setEnumerations(enumerations);
                features.add(feature);
            }
        }
        product.setFeatures(features);
        if (state.equals("new")) {
            dataManager.addProduct(product);
        } else {
            dataManager.updateProduct(product);
        }
        this.getServletContext().getRequestDispatcher("/admin/ProductGroupOverview.jsp?parentGroupID=" + product.getProductGroupId()).forward(request, response);
    }

    private void processProductGroupEditState(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map params = request.getParameterMap();
        String state = request.getParameter("editProductGroupState");
        Long groupID = null;
        ProductGroup group = null;
        if (state.equals("new")) {
            group = new ProductGroup();
        } else {
            groupID = Long.parseLong(request.getParameter("groupID"));
            group = dataManager.getProductGroup(groupID);
        }
        group.setName(request.getParameter("name"));
        group.setDescription(request.getParameter("description"));
        SEOTags seoTags = group.getSeoTags();
        if (seoTags == null) {
            seoTags = new SEOTags();
        }
        seoTags.setTitle(request.getParameter("seotitle"));
        seoTags.setKeywords(request.getParameter("seokeywords"));
        seoTags.setDescription(request.getParameter("seodescription"));
        group.setSeoTags(seoTags);
        if (request.getParameter("rank") != null && request.getParameter("rank").length() > 0 && isNumber(request.getParameter("rank"))) {
            group.setRank(Integer.parseInt(request.getParameter("rank")));
        } else {
            group.setRank(null);
        }
        if (request.getParameter("parentID") != null && !request.getParameter("parentID").equals("null")) {
            Long parentID = Long.parseLong(request.getParameter("parentID"));
            group.setParent(dataManager.getProductGroup(parentID));
        } else {
            group.setParent(null);
        }
        if (state.equals("new")) {
            dataManager.addProductGroup(group);
        } else {
            dataManager.updateProductGroup(group);
        }
        if (group.getParent() != null) {
            this.getServletContext().getRequestDispatcher("/admin/ProductGroupOverview.jsp?parentGroupID=" + group.getParent().getId()).forward(request, response);
        } else {
            this.getServletContext().getRequestDispatcher("/admin/ProductGroupOverview.jsp").forward(request, response);
        }
    }

    public boolean isNumber(String content) {
        for (char c : content.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    /** Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /** Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }
}
