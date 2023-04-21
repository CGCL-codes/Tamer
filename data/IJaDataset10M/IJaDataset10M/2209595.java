package es.devel.opentrats.view.controller;

import es.devel.opentrats.constants.OpenTratsConstants;
import es.devel.opentrats.dao.IAttentionDao;
import es.devel.opentrats.dao.ICustomerDao;
import es.devel.opentrats.dao.IEmployeeDao;
import es.devel.opentrats.dao.IProductDao;
import es.devel.opentrats.dao.IServiceDao;
import es.devel.opentrats.dao.IStockDao;
import es.devel.opentrats.dao.exception.AttentionDaoException;
import es.devel.opentrats.dao.exception.CustomerDaoException;
import es.devel.opentrats.dao.exception.EmployeeDaoException;
import es.devel.opentrats.dao.exception.ProductDaoException;
import es.devel.opentrats.dao.exception.ServiceDaoException;
import es.devel.opentrats.model.Customer;
import es.devel.opentrats.model.DTO.AttentionForm;
import es.devel.opentrats.model.DTO.DoubtInvoiceDTO;
import es.devel.opentrats.model.DTO.ProductStockDTO;
import es.devel.opentrats.model.DTO.ServiceDTO;
import es.devel.opentrats.model.Employee;
import es.devel.opentrats.model.Product;
import es.devel.opentrats.model.Service;
import es.devel.opentrats.service.IInvoiceService;
import es.devel.opentrats.service.exception.InvoiceServiceException;
import es.devel.opentrats.view.controller.common.OpenTratsMultiActionController;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author pau
 */
public class AttentionMultiActionController extends OpenTratsMultiActionController {

    private static final String DECIMAL_FORMAT_PATTERN = "#.###";

    private static final String DELEGATION_ATTRIBUTE_NAME = "delegation";

    private static final String DELEGATION_PARAM_NAME = "delegation";

    private static final String SEARCH_FILTER_PARAM_NAME = "filterSearch";

    private static final String FORM_NAME = "attentionForm";

    private static final String CUSTOMER_PARAM_NAME = "idCustomer";

    private static final String EMPLOYEE_PARAM_NAME = "idEmployee";

    private static final String ACTIVE_LIST_PARAM_NAME = "active";

    private static final String CATEGORY_PARAM_NAME = "category";

    private static final String BRAND_PARAM_NAME = "brand";

    private static final String ACTIVE_LIST_PRODUCTS = "products";

    private static final String ACTIVE_LIST_SERVICES = "services";

    private static final String PAYMENT_MODE_PARAM_NAME = "paymentMode";

    private static final String DISCOUNT_PARAM_NAME = "discount";

    private ICustomerDao customerDao;

    private IEmployeeDao employeeDao;

    private IServiceDao serviceDao;

    private IProductDao productDao;

    private IAttentionDao attentionDao;

    private IStockDao stockDao;

    private IInvoiceService invoiceService;

    private AttentionForm attentionForm;

    private DecimalFormat df;

    public AttentionMultiActionController() {
        df = new DecimalFormat(DECIMAL_FORMAT_PATTERN);
    }

    public void getCustomerData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        Map<String, Boolean> groups = (Map<String, Boolean>) request.getSession().getAttribute("groups");
        String idCustomer = (String) request.getParameter(CUSTOMER_PARAM_NAME);
        attentionForm = (AttentionForm) request.getSession().getAttribute(FORM_NAME);
        Customer customer = null;
        List<DoubtInvoiceDTO> invoicelist = null;
        if (idCustomer != null && idCustomer.equals("") == false) {
            try {
                customer = (Customer) customerDao.load(new Long(idCustomer));
                attentionForm.setCustomer(customer);
            } catch (CustomerDaoException e) {
                e.printStackTrace();
            }
            try {
                invoicelist = (List<DoubtInvoiceDTO>) invoiceService.getDoubtsByRefCustomer(new Integer(idCustomer));
            } catch (InvoiceServiceException e) {
                e.printStackTrace();
            }
        }
        if (customer == null) {
            out.print("Cliente inexistente");
        } else {
            if (invoicelist.size() > 0) {
                if (groups.containsKey(OpenTratsConstants.GROUP_PAYMENTS_NAME)) {
                    out.print("<a href='initDoubts.htm?idCustomer=" + customer.getIdCustomer() + "'>" + "<button class='button' style='float: right; width: 200px; background: #D94F00; font-size: 15px;'>PAGOS PENDIENTES</button>" + "</a><a href ='customers.htm?idCustomer=" + customer.getIdCustomer() + "'>" + customer.getSurname() + ", " + customer.getName() + " " + "<img src='img/iconos/eye.png'/></a>");
                } else {
                    out.print("<span style='float: right; width: 200px;font-size: 12px;'>Existen pagos pendientes</span>" + "<a href ='customers.htm?idCustomer=" + customer.getIdCustomer() + "'>" + customer.getSurname() + ", " + customer.getName() + " " + "<img src='img/iconos/eye.png'/></a>");
                }
            } else {
                out.print("<a href ='customers.htm?idCustomer=" + customer.getIdCustomer() + "'>" + customer.getSurname() + ", " + customer.getName() + "<img src='img/iconos/eye.png'/></a>");
            }
        }
        request.getSession().setAttribute(FORM_NAME, attentionForm);
        out.flush();
        out.close();
    }

    public void getEmployeeData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        String idEmployee = (String) request.getParameter(EMPLOYEE_PARAM_NAME);
        attentionForm = (AttentionForm) request.getSession().getAttribute(FORM_NAME);
        Employee employee = null;
        if (idEmployee != null && idEmployee.equals("") == false) {
            try {
                employee = (Employee) employeeDao.load(Integer.parseInt(idEmployee));
                attentionForm.setEmployee(employee);
            } catch (EmployeeDaoException e) {
                e.printStackTrace();
            }
        }
        if (employee == null) {
            out.print("Empleado inexistente");
        } else {
            out.print("<a href ='employees.htm?idEmployee=" + employee.getIdEmployee() + "'>" + employee.getSurname() + ", " + employee.getName() + "</a>");
        }
        request.getSession().setAttribute(FORM_NAME, attentionForm);
        out.flush();
        out.close();
    }

    public void filterSearch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        StringBuilder html = new StringBuilder("");
        List<Service> serviceList = null;
        StringBuilder htmlServices = new StringBuilder("");
        List<ProductStockDTO> productList = null;
        StringBuilder htmlProducts = new StringBuilder("");
        String searchFilter = (String) request.getParameter(SEARCH_FILTER_PARAM_NAME).trim();
        String categoryFilter = ((String) request.getParameter(CATEGORY_PARAM_NAME));
        String brandFilter = (String) request.getParameter(BRAND_PARAM_NAME);
        String activeList = (String) request.getParameter(ACTIVE_LIST_PARAM_NAME);
        attentionForm = (AttentionForm) request.getSession().getAttribute(FORM_NAME);
        attentionForm.setFilterSearch(searchFilter);
        attentionForm.setActiveList(activeList);
        if (attentionForm.getActiveList().equals(ACTIVE_LIST_SERVICES)) {
            attentionForm.setSelectedCategory(categoryFilter);
            try {
                serviceList = (List<Service>) serviceDao.findActiveServiceByNameAndCategory(searchFilter, categoryFilter);
                attentionForm.setSelectionServiceList(serviceList);
            } catch (ServiceDaoException e) {
                e.printStackTrace();
            }
            if (attentionForm.getSelectionServiceList() == null) {
                htmlServices.append(" <tr class='odd'>");
                htmlServices.append("<td colspan='4'>No existen coincidencias.</td>");
                htmlServices.append("</tr>");
            } else {
                Iterator it = serviceList.iterator();
                int cont = 0;
                while (it.hasNext()) {
                    Service service = (Service) it.next();
                    if (cont % 2 == 0) {
                        htmlServices.append(" <tr class='odd'>");
                        htmlServices.append("<td>" + service.getIdService() + "</td>");
                        htmlServices.append("<td>" + service.getName() + "</td>");
                        htmlServices.append("<td>" + service.getPrice() + "</td>");
                        if (service.getType().equals("LASER")) {
                            htmlServices.append("<td><a onclick='javascript:getDateService(" + service.getIdService() + ");'>");
                        } else {
                            htmlServices.append("<td><a onclick='javascript:addService(" + service.getIdService() + ",0,0);'>");
                        }
                        htmlServices.append("   <img alt='Agregar servicio' title='Agregar servicio' src='img/iconos/add.png'/></a>");
                        htmlServices.append("</td>");
                        htmlServices.append("</tr>");
                    } else {
                        htmlServices.append(" <tr class='even'>");
                        htmlServices.append("<td>" + service.getIdService() + "</td>");
                        htmlServices.append("<td>" + service.getName() + "</td>");
                        htmlServices.append("<td>" + service.getPrice() + "</td>");
                        if (service.getType().equals("LASER")) {
                            htmlServices.append("<td><a onclick='javascript:getDateService(" + service.getIdService() + ");'>");
                        } else {
                            htmlServices.append("<td><a onclick='javascript:addService(" + service.getIdService() + ",0,0);'>");
                        }
                        htmlServices.append("   <img alt='Agregar servicio' title='Agregar servicio' src='img/iconos/add.png'/></a>");
                        htmlServices.append("</td>");
                        htmlServices.append("</tr>");
                    }
                    cont = cont + 1;
                }
            }
        }
        if (attentionForm.getActiveList().equals(ACTIVE_LIST_PRODUCTS)) {
            attentionForm.setSelectedBrand(brandFilter);
            Integer delegation = (Integer) request.getSession().getAttribute(DELEGATION_ATTRIBUTE_NAME);
            try {
                productList = (List<ProductStockDTO>) productDao.findDTOByNameAndBrand(searchFilter, brandFilter, String.valueOf(delegation));
                attentionForm.setSelectionProductList(productList);
            } catch (ProductDaoException e) {
                e.printStackTrace();
            }
            if (attentionForm.getSelectionProductList() == null) {
                htmlProducts.append(" <tr class='odd'>");
                htmlProducts.append("<td colspan='4'>No existen coincidencias.</td>");
                htmlProducts.append("</tr>");
            } else {
                Iterator it = productList.iterator();
                int cont = 0;
                while (it.hasNext()) {
                    ProductStockDTO product = (ProductStockDTO) it.next();
                    if (cont % 2 == 0) {
                        htmlProducts.append("<tr class='odd'>");
                        htmlProducts.append("<td>" + product.getIdProduct() + "</td>");
                        htmlProducts.append("<td>" + product.getName() + "</td>");
                        htmlProducts.append("<td>" + product.getPrice() + "</td>");
                        htmlProducts.append("<td>" + product.getUnits() + "</td>");
                        htmlProducts.append("<td><a onclick='javascript:addProduct(" + product.getIdProduct() + ");'>");
                        htmlProducts.append("   <img alt='Agregar producto' title='Agregar producto' src='img/iconos/add.png'/></a>");
                        htmlProducts.append("</td>");
                        htmlProducts.append("</tr>");
                    } else {
                        htmlProducts.append(" <tr class='even'>");
                        htmlProducts.append("<td>" + product.getIdProduct() + "</td>");
                        htmlProducts.append("<td>" + product.getName() + "</td>");
                        htmlProducts.append("<td>" + product.getPrice() + "</td>");
                        htmlProducts.append("<td>" + product.getUnits() + "</td>");
                        htmlProducts.append("<td><a onclick='javascript:addProduct(" + product.getIdProduct() + ");'>");
                        htmlProducts.append("   <img alt='Agregar producto' title='Agregar producto' src='img/iconos/add.png'/></a>");
                        htmlProducts.append("</td></tr>");
                    }
                    cont = cont + 1;
                }
            }
        }
        if (attentionForm.getActiveList().equals(ACTIVE_LIST_PRODUCTS)) {
            html = new StringBuilder("<h4>Lista de Productos</h4>");
            html.append("<table id='selectionProducts' class='tablaDatos'>");
            html.append("<thead><tr><th class='table_header'>C&oacute;d.</th><th class='table_header'>Nombre</th>");
            html.append("<th class='table_header'>Precio</th><th class='table_header'>Stock</th><th class='table_header'>Acci&oacute;n</th></tr></thead>");
            html.append("<tbody>");
        } else {
            html = new StringBuilder("<h4>Lista de Servicios</h4>");
            html.append("<table id='selectionServices' class='tablaDatos'>");
            html.append("<thead><tr><th class='table_header'>C&oacute;d.</th><th class='table_header'>Nombre</th>");
            html.append("<th class='table_header'>Precio</th><th class='table_header'>Acci&oacute;n</th></tr></thead>");
            html.append("<tbody>");
        }
        if (attentionForm.getActiveList().equals(ACTIVE_LIST_PRODUCTS)) {
            html.append(htmlProducts.toString());
        } else {
            html.append(htmlServices.toString());
        }
        html.append("</tbody></table>");
        request.getSession().setAttribute(FORM_NAME, attentionForm);
        out.print(html);
        out.flush();
        out.close();
    }

    public ModelAndView addProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        StringBuilder html = new StringBuilder("");
        attentionForm = (AttentionForm) request.getSession().getAttribute(FORM_NAME);
        String idProduct = (String) request.getParameter("idProduct");
        Product product = null;
        if (attentionForm.getProductList() == null) {
            attentionForm.setProductList(new ArrayList<Product>());
        }
        if (idProduct != null && idProduct.equals("") == false) {
            try {
                product = (Product) productDao.load(Integer.parseInt(idProduct));
            } catch (ProductDaoException e) {
                e.printStackTrace();
            }
            attentionForm.getProductList().add(product);
        }
        double price = product.getPrice();
        double total = attentionForm.getTotalAmount();
        attentionForm.setTotalAmount(total + price);
        attentionForm.setFinalAmount(total + price);
        html = new StringBuilder("<h4>Productos solicitados</h4>");
        html.append("<table id='selectionProducts' class='tablaDatos'>");
        html.append("<thead><tr><th class='table_header'>Acci&oacute;n</th><th class='table_header'>C&oacute;d.</th><th class='table_header'>Nombre</th>");
        html.append("<th class='table_header'>Precio</th></tr></thead>");
        html.append("<tbody>");
        Iterator it = attentionForm.getProductList().iterator();
        int cont = 0;
        while (it.hasNext()) {
            Product prod = (Product) it.next();
            if (cont % 2 == 0) {
                html.append("<tr class='odd'>");
                html.append("<td><a onclick='javascript:removeProduct(" + prod.getIdProduct() + ");'>");
                html.append("   <img alt='Eliminar producto' title='Eliminar producto' src='img/iconos/return.png'/></a></td>");
                html.append("<td>" + prod.getIdProduct() + "</td><td>" + prod.getName() + "</td><td>" + prod.getPrice() + "</td>");
                html.append("</tr>");
            } else {
                html.append("<tr class='even'>");
                html.append("<td><a onclick='javascript:removeProduct(" + prod.getIdProduct() + ");'>");
                html.append("   <img alt='Eliminar producto' title='Eliminar producto' src='img/iconos/return.png'/></a></td>");
                html.append("<td>" + prod.getIdProduct() + "</td><td>" + prod.getName() + "</td><td>" + prod.getPrice() + "</td>");
                html.append("</tr>");
            }
            cont = cont + 1;
        }
        html.append("</tbody></table>");
        request.getSession().setAttribute(FORM_NAME, attentionForm);
        out.print(html);
        out.flush();
        out.close();
        return null;
    }

    public ModelAndView addService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        StringBuilder html = new StringBuilder("");
        attentionForm = (AttentionForm) request.getSession().getAttribute(FORM_NAME);
        String idService = (String) request.getParameter("idService");
        String sesion = (String) request.getParameter("sesion");
        int session = 0;
        if (sesion.equals("") == false || sesion != null) {
            session = Integer.parseInt(sesion);
        }
        String intensidad = (String) request.getParameter("intensidad");
        int intensity = 0;
        if (intensidad.equals("") == false || intensidad != null) {
            intensity = Integer.parseInt(intensidad);
        }
        Service service = null;
        if (attentionForm.getServiceList() == null) {
            attentionForm.setServiceList(new ArrayList<ServiceDTO>());
        }
        if (idService != null && idService.equals("") == false) {
            try {
                service = (Service) serviceDao.load(Integer.parseInt(idService));
            } catch (ServiceDaoException e) {
                e.printStackTrace();
            }
            ServiceDTO serviceDTO = new ServiceDTO(service, session, intensity);
            attentionForm.getServiceList().add(serviceDTO);
        }
        double price = service.getPrice();
        double total = attentionForm.getTotalAmount();
        attentionForm.setTotalAmount(total + price);
        attentionForm.setFinalAmount(total + price);
        html = new StringBuilder("<h4>Servicios solicitados</h4>");
        html.append("<table id='selectionServices' class='tablaDatos'>");
        html.append("<thead><tr><th class='table_header'></th><th class='table_header'>C&oacute;d.</th><th class='table_header'>Nombre</th>");
        html.append("<th class='table_header'>Sesion</th><th class='table_header'>Intensidad</th><th class='table_header'>Precio</th></tr></thead>");
        html.append("<tbody>");
        Iterator it = attentionForm.getServiceList().iterator();
        int cont = 0;
        while (it.hasNext()) {
            ServiceDTO serv = (ServiceDTO) it.next();
            if (cont % 2 == 0) {
                html.append(" <tr class='odd'>");
                html.append("<td><a onclick='javascript:removeService(" + serv.getIdService() + ");'>");
                html.append("   <img alt='Eliminar servicio' title='Eliminar servicio' src='img/iconos/return.png'/></a></td>");
                html.append("<td>" + serv.getIdService() + "</td><td>" + serv.getName() + "</td>");
                if (serv.getSession() == 0) {
                    html.append("<td>--</td>");
                } else {
                    html.append("<td>" + serv.getSession() + "</td>");
                }
                if (serv.getIntensity() == 0) {
                    html.append("<td>--</td>");
                } else {
                    html.append("<td>" + serv.getIntensity() + "</td>");
                }
                html.append("<td>" + serv.getPrice() + "</td>");
                html.append("</tr>");
            } else {
                html.append(" <tr class='even'>");
                html.append("<td><a onclick='javascript:removeService(" + serv.getIdService() + ");'>");
                html.append("   <img alt='Eliminar servicio' title='Eliminar servicio' src='img/iconos/return.png'/></a></td>");
                html.append("<td>" + serv.getIdService() + "</td>");
                html.append("<td>" + serv.getName() + "</td>");
                if (serv.getSession() == 0) {
                    html.append("<td>--</td>");
                } else {
                    html.append("<td>" + serv.getSession() + "</td>");
                }
                if (serv.getIntensity() == 0) {
                    html.append("<td>--</td>");
                } else {
                    html.append("<td>" + serv.getIntensity() + "</td>");
                }
                html.append("<td>" + serv.getPrice() + "</td>");
                html.append("</tr>");
            }
            cont = cont + 1;
        }
        html.append("</tbody></table>");
        request.getSession().setAttribute(FORM_NAME, attentionForm);
        out.print(html);
        out.flush();
        out.close();
        return null;
    }

    public ModelAndView registerAttention(HttpServletRequest request, HttpServletResponse response) throws Exception {
        getLogService().debug("###################################################");
        getLogService().debug("REGISTRO DE ANTECIONES");
        attentionForm = (AttentionForm) request.getSession().getAttribute(FORM_NAME);
        String payNow = (String) request.getParameter("payNow");
        Long idCustomer = new Long(attentionForm.getCustomer().getIdCustomer().intValue());
        getLogService().debug("Codigo de cliente: " + idCustomer.intValue());
        Integer idEmployee = attentionForm.getEmployee().getIdEmployee();
        getLogService().debug("Codigo de empleado: " + idEmployee.intValue());
        List<Product> productList = (List) attentionForm.getProductList();
        List<ServiceDTO> serviceList = (List<ServiceDTO>) attentionForm.getServiceList();
        Integer discount = attentionForm.getDiscount();
        getLogService().debug("Descuento aplicado: " + discount.intValue());
        String paymentMode = attentionForm.getPaymentMode();
        getLogService().debug("Forma de pago: " + paymentMode);
        Integer delegation = (Integer) request.getSession().getAttribute(DELEGATION_ATTRIBUTE_NAME);
        getLogService().debug("Delegacion: " + delegation.intValue());
        String terminal = request.getRemoteAddr() + " --- " + request.getRemoteHost();
        try {
            attentionDao.registerAttention(idCustomer, idEmployee, productList, serviceList, discount, paymentMode, delegation, terminal, payNow);
        } catch (AttentionDaoException ex) {
            ex.printStackTrace();
            Logger.getRootLogger().error(ex);
        }
        PrintWriter out = response.getWriter();
        StringBuilder html = new StringBuilder("");
        html = new StringBuilder("<h4>Atencion realizada con �xito</h4>");
        request.getSession().setAttribute(FORM_NAME, new AttentionForm());
        out.print(html);
        out.flush();
        out.close();
        return new ModelAndView("attention.htm");
    }

    public ModelAndView updateTotal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("index");
    }

    public ModelAndView removeService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        StringBuilder html = new StringBuilder("");
        attentionForm = (AttentionForm) request.getSession().getAttribute(FORM_NAME);
        String idService = (String) request.getParameter("idService");
        ServiceDTO service = null;
        if (idService != null && idService.equals("") == false) {
            Iterator it = attentionForm.getServiceList().iterator();
            while (it.hasNext()) {
                service = (ServiceDTO) it.next();
                if (String.valueOf(service.getIdService()).equals(idService)) {
                    double total = attentionForm.getTotalAmount();
                    double price = service.getPrice();
                    attentionForm.setTotalAmount(total - price);
                    attentionForm.setFinalAmount(total - price);
                    it.remove();
                    break;
                }
            }
        }
        html = new StringBuilder("<h4>Servicios solicitados</h4>");
        html.append("<table id='selectionServices' class='tablaDatos'>");
        html.append("<thead><tr><th class='table_header'></th><th class='table_header'>C&oacute;d.</th><th class='table_header'>Nombre</th>");
        html.append("<th class='table_header'>Sesion</th><th class='table_header'>Intensidad</th><th class='table_header'>Precio</th></tr></thead>");
        html.append("<tbody>");
        Iterator it = attentionForm.getServiceList().iterator();
        int cont = 0;
        while (it.hasNext()) {
            ServiceDTO serv = (ServiceDTO) it.next();
            if (cont % 2 == 0) {
                html.append(" <tr class='odd'>");
                html.append("<td><a onclick='javascript:removeService(" + serv.getIdService() + ");'>");
                html.append("   <img alt='Eliminar servicio' title='Eliminar servicio' src='img/iconos/return.png'/></a>");
                html.append("</td>");
                html.append("<td>" + serv.getIdService() + "</td>");
                html.append("<td>" + serv.getName() + "</td>");
                if (serv.getSession() == 0) {
                    html.append("<td>--</td>");
                } else {
                    html.append("<td>" + serv.getSession() + "</td>");
                }
                if (serv.getIntensity() == 0) {
                    html.append("<td>--</td>");
                } else {
                    html.append("<td>" + serv.getIntensity() + "</td>");
                }
                html.append("<td>" + serv.getPrice() + "</td>");
                html.append("</tr>");
            } else {
                html.append(" <tr class='even'>");
                html.append("<td><a onclick='javascript:removeService(" + serv.getIdService() + ");'>");
                html.append("   <img alt='Eliminar servicio' title='Eliminar servicio' src='img/iconos/return.png'/></a>");
                html.append("</td>");
                html.append("<td>" + serv.getIdService() + "</td>");
                html.append("<td>" + serv.getName() + "</td>");
                if (serv.getSession() == 0) {
                    html.append("<td>--</td>");
                } else {
                    html.append("<td>" + serv.getSession() + "</td>");
                }
                if (serv.getIntensity() == 0) {
                    html.append("<td>--</td>");
                } else {
                    html.append("<td>" + serv.getIntensity() + "</td>");
                }
                html.append("<td>" + serv.getPrice() + "</td>");
                html.append("</tr>");
            }
            cont = cont + 1;
        }
        html.append("</tbody></table>");
        request.getSession().setAttribute(FORM_NAME, attentionForm);
        out.print(html);
        out.flush();
        out.close();
        return null;
    }

    public ModelAndView removeProduct(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        StringBuilder html = new StringBuilder("");
        attentionForm = (AttentionForm) request.getSession().getAttribute(FORM_NAME);
        String idProduct = (String) request.getParameter("idProduct");
        Product product = null;
        if (idProduct != null && idProduct.equals("") == false) {
            Iterator it = attentionForm.getProductList().iterator();
            while (it.hasNext()) {
                product = (Product) it.next();
                if (String.valueOf(product.getIdProduct()).equals(idProduct)) {
                    double total = attentionForm.getTotalAmount();
                    double price = product.getPrice();
                    attentionForm.setTotalAmount(total - price);
                    attentionForm.setFinalAmount(total - price);
                    it.remove();
                    break;
                }
            }
        }
        html = new StringBuilder("<h4>Productos solicitados</h4>");
        html.append("<table id='selectionProducts' class='tablaDatos'>");
        html.append("<thead><tr><th class='table_header'>Acci&oacute;n</th><th class='table_header'>C&oacute;d.</th><th class='table_header'>Nombre</th>");
        html.append("<th class='table_header'>Precio</th></tr></thead>");
        html.append("<tbody>");
        Iterator it = attentionForm.getProductList().iterator();
        int cont = 0;
        while (it.hasNext()) {
            Product prod = (Product) it.next();
            if (cont % 2 == 0) {
                html.append("<tr class='odd'>");
                html.append("<td><a onclick='javascript:removeProduct(" + prod.getIdProduct() + ");'>");
                html.append("   <img alt='Eliminar producto' title='Eliminar producto' src='img/iconos/return.png'/></a></td>");
                html.append("<td>" + prod.getIdProduct() + "</td><td>" + prod.getName() + "</td><td>" + prod.getPrice() + "</td>");
                html.append("</tr>");
            } else {
                html.append(" <tr class='even'>");
                html.append("<td><a onclick='javascript:removeProduct(" + prod.getIdProduct() + ");'>");
                html.append("   <img alt='Eliminar producto' title='Eliminar producto' src='img/iconos/return.png'/></a></td>");
                html.append("<td>" + prod.getIdProduct() + "</td><td>" + prod.getName() + "</td><td>" + prod.getPrice() + "</td>");
                html.append("</tr>");
            }
            cont = cont + 1;
        }
        html.append("</tbody></table>");
        request.getSession().setAttribute(FORM_NAME, attentionForm);
        out.print(html);
        out.flush();
        out.close();
        return null;
    }

    public void getDiscount(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        StringBuilder html = new StringBuilder("");
        String paymentMode = (String) request.getParameter(PAYMENT_MODE_PARAM_NAME);
        attentionForm = (AttentionForm) request.getSession().getAttribute(FORM_NAME);
        attentionForm.setPaymentMode(paymentMode);
        String discount = (String) request.getParameter(DISCOUNT_PARAM_NAME);
        Float discountFloatValue = new Float(discount);
        attentionForm.setDiscount(new Integer(discount));
        if (discount != null && discount.equals("") == false) {
            Double finalAmount = (attentionForm.getTotalAmount() * (1 - (discountFloatValue / 100)));
            attentionForm.setFinalAmount(finalAmount);
        }
        html = new StringBuilder("<div class='total'><span class=''>" + df.format(attentionForm.getFinalAmount()) + "</span> &euro;</div>");
        html.append("Total a pagar:");
        request.getSession().setAttribute("attentionForm", attentionForm);
        out.print(html);
        out.flush();
        out.close();
    }

    public void getReturn(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintWriter out = response.getWriter();
        StringBuilder html = new StringBuilder("");
        String pago = (String) request.getParameter("pago");
        attentionForm = (AttentionForm) request.getSession().getAttribute(FORM_NAME);
        Double returnValue = (new Double(pago.replaceAll(",", "."))) - attentionForm.getFinalAmount();
        html = new StringBuilder("<label>Vueltas:&nbsp;<b>" + df.format(returnValue) + "&euro; </b></label>");
        html.append("<input type='button' class='button' value='Calcular' onclick='javascript:calculaVueltas();'/>");
        request.getSession().setAttribute(FORM_NAME, attentionForm);
        out.print(html);
        out.flush();
        out.close();
    }

    public ModelAndView cleanChart(HttpServletRequest request, HttpServletResponse response) throws Exception {
        attentionForm = (AttentionForm) request.getSession().getAttribute(FORM_NAME);
        attentionForm.setProductList(new ArrayList<Product>());
        attentionForm.setServiceList(new ArrayList<ServiceDTO>());
        attentionForm.setDiscount(0);
        attentionForm.setTotalAmount(0);
        attentionForm.setFinalAmount(0);
        attentionForm.setPaymentMode("");
        request.getSession().setAttribute(FORM_NAME, attentionForm);
        return new ModelAndView("redirect:attention.htm");
    }

    public ModelAndView cancelChart(HttpServletRequest request, HttpServletResponse response) throws Exception {
        attentionForm = new AttentionForm();
        request.getSession().setAttribute(FORM_NAME, attentionForm);
        return new ModelAndView("redirect:initAttentions.htm");
    }

    public ICustomerDao getCustomerDao() {
        return customerDao;
    }

    public void setCustomerDao(ICustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public IEmployeeDao getEmployeeDao() {
        return employeeDao;
    }

    public void setEmployeeDao(IEmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    public IProductDao getProductDao() {
        return productDao;
    }

    public void setProductDao(IProductDao productDao) {
        this.productDao = productDao;
    }

    public IServiceDao getServiceDao() {
        return serviceDao;
    }

    public void setServiceDao(IServiceDao serviceDao) {
        this.serviceDao = serviceDao;
    }

    public IAttentionDao getAttentionDao() {
        return attentionDao;
    }

    public void setAttentionDao(IAttentionDao attentionDao) {
        this.attentionDao = attentionDao;
    }

    public IInvoiceService getInvoiceService() {
        return invoiceService;
    }

    public void setInvoiceService(IInvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    public IStockDao getStockDao() {
        return stockDao;
    }

    public void setStockDao(IStockDao stockDao) {
        this.stockDao = stockDao;
    }
}
