package org.cmsuite2.web.action.impl;

import it.ec.commons.struts2.action.web.WebActionSupport;
import it.ec.commons.struts2.enumeration.Outcome;
import it.ec.commons.web.PaginateUtil;
import it.ec.commons.web.PaginatedList;
import it.ec.commons.web.ValidateBean;
import it.ec.commons.web.ValidateException;
import it.ec.commons.web.WhereBuilder;
import it.ec.commons.web.WhereBuilder.Operator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.xwork.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.cmsuite2.business.form.SupplierPackListForm;
import org.cmsuite2.business.handler.SupplierPackListHandler;
import org.cmsuite2.business.search.SupplierPackListSearch;
import org.cmsuite2.enumeration.PackListType;
import org.cmsuite2.model.employee.Employee;
import org.cmsuite2.model.packagelist.PackListRate;
import org.cmsuite2.model.store.Store;
import org.cmsuite2.model.supplier.Supplier;
import org.cmsuite2.model.supplier.SupplierPackList;
import org.cmsuite2.model.vector.Vector;
import org.cmsuite2.util.splitter.ProductSplitter;
import org.cmsuite2.web.action.ISupplierPackListAction;
import org.springframework.context.ApplicationContextAware;

public class SupplierPackListAction extends WebActionSupport implements ISupplierPackListAction, ApplicationContextAware {

    private static final long serialVersionUID = 3989526300723557410L;

    private static Logger logger = Logger.getLogger(SupplierPackListAction.class);

    private PackListRate packListRate;

    private ProductSplitter productSplitter;

    private SupplierPackList supplierPackList;

    private SupplierPackListForm supplierPackListForm;

    private SupplierPackListHandler supplierPackListHandler;

    private SupplierPackListSearch supplierPackListSearch;

    private long supplierId;

    private long employeeId;

    private long originalPackListId;

    private long originalBillId;

    private long paymentId;

    private long storeId;

    private long vectorId;

    private String products;

    private Map<Long, Float> availableItems = new HashMap<Long, Float>();

    private Map<Long, Float> chosenItems = new HashMap<Long, Float>();

    private String abortButton;

    private String backButton;

    private String deleteButton;

    private String insertButton;

    private String loadButton;

    private String editButton;

    private String newButton;

    private String pdfButton;

    private String saveButton;

    private String searchButton;

    private PaginateUtil paginateUtil;

    private PaginatedList pagList;

    private WhereBuilder whereBuilder;

    private int pageItem;

    private int page;

    private String sort;

    private String dir;

    private List<PackListType> packListTypes = Arrays.asList(PackListType.values());

    public String index() {
        try {
            if (supplierPackListSearch != null) {
                if (StringUtils.isNotEmpty(supplierPackListSearch.getNumber())) getWhereBuilder().add(supplierPackListSearch.getNumber(), "_obj.number like '%?%'", Operator.AND);
                if (supplierPackListSearch.getStoreId() != 0) getWhereBuilder().add(supplierPackListSearch.getStoreId(), "_obj.store like '%?%'", Operator.AND);
                if (supplierPackListSearch.getEmployeeId() != 0) getWhereBuilder().add(supplierPackListSearch.getEmployeeId(), "_obj.employee like '%?%'", Operator.AND);
                if (supplierPackListSearch.getSupplierId() != 0) getWhereBuilder().add(supplierPackListSearch.getSupplierId(), "_obj.supplier like '%?%'", Operator.AND);
                if (supplierPackListSearch.getVectorId() != 0) getWhereBuilder().add(supplierPackListSearch.getVectorId(), "_obj.vector like '%?%'", Operator.AND);
                if (supplierPackListSearch.getPackListType() != null) getWhereBuilder().add(supplierPackListSearch.getPackListType(), "_obj.packListType like '%?%'", Operator.AND);
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                if (StringUtils.isNotEmpty(supplierPackListSearch.getFrom())) try {
                    getWhereBuilder().add(sdf.parse(supplierPackListSearch.getFrom()), "_obj.insDate >= '?'", Operator.AND);
                } catch (ParseException e) {
                    logger.error(e, e);
                }
                if (StringUtils.isNotEmpty(supplierPackListSearch.getTo())) try {
                    Date to = sdf.parse(supplierPackListSearch.getTo());
                    to = DateUtils.setHours(to, 23);
                    to = DateUtils.setMinutes(to, 59);
                    to = DateUtils.setSeconds(to, 59);
                    getWhereBuilder().add(to, "_obj.insDate <= '?'", Operator.AND);
                } catch (ParseException e) {
                    logger.error(e, e);
                }
            }
            HttpServletRequest request = ServletActionContext.getRequest();
            Object pagListObj = request.getAttribute("PAGINATED_LIST");
            pagList = (PaginatedList) pagListObj;
            pageItem = pagList.getItemsPerPage();
            Employee employee = (Employee) session.getAttribute("EMPLOYEE");
            if (employee != null) {
                employeeId = employee.getId();
                for (Store store : employee.getStores()) getWhereBuilder().add(store.getId(), "_obj.store.id = '?'", Operator.OR);
            }
            pagList = paginateUtil.getPaginate(SupplierPackList.class, pagList, whereBuilder.toString());
            supplierPackListForm = supplierPackListHandler.fillFormFieldsIndex(employeeId);
            if (!StringUtils.isBlank(getForwardedError())) addActionError(getForwardedError());
            return SUCCESS;
        } catch (Throwable e) {
            logger.error(e, e);
            addActionError(e.toString());
            return Outcome.JAVA_EXCEPTION.name();
        }
    }

    public String view() {
        try {
            supplierPackListForm = supplierPackListHandler.fillFormFieldsView(idToLoad);
            supplierPackList = supplierPackListForm.getPackList();
            return SUCCESS;
        } catch (Throwable e) {
            logger.error(e, e);
            addActionError(e.toString());
            return Outcome.JAVA_EXCEPTION.name();
        }
    }

    public String action() {
        try {
            if (newButton != null) return "action.create";
            if (deleteButton != null) {
                if (intList != null && intList.size() > 0) for (Integer i : intList) supplierPackListHandler.delete(i, storeId); else setForwardedError(getText("message.selection.em"));
            }
            if (editButton != null) return "action.edit";
            if (pdfButton != null) return "action.pdf";
            return "action.index";
        } catch (Throwable e) {
            logger.error(e, e);
            addActionError(e.toString());
            return Outcome.JAVA_EXCEPTION.name();
        }
    }

    public String create() {
        try {
            if (abortButton != null) return "action.index";
            Employee employee = (Employee) session.getAttribute("EMPLOYEE");
            if (employee != null) employeeId = employee.getId();
            supplierPackListForm = supplierPackListHandler.fillFormFieldsCreate(employeeId);
            return "jsp.createStep1";
        } catch (Throwable e) {
            logger.error(e, e);
            addActionError(e.toString());
            return Outcome.JAVA_EXCEPTION.name();
        }
    }

    public String edit() {
        try {
            if (abortButton != null) return "action.index";
            supplierPackListForm = supplierPackListHandler.fillFormFieldsEdit(idToLoad);
            supplierPackList = supplierPackListForm.getPackList();
            Supplier supplier = supplierPackList.getSupplier();
            if (supplier != null) supplierId = supplier.getId();
            Employee employee = supplierPackList.getEmployee();
            if (employee != null) employeeId = employee.getId();
            Store store = supplierPackList.getStore();
            if (store != null) storeId = store.getId();
            Vector vector = supplierPackList.getVector();
            if (vector != null) vectorId = vector.getId();
            return "jsp.editStep1";
        } catch (Throwable e) {
            logger.error(e, e);
            addActionError(e.toString());
            return Outcome.JAVA_EXCEPTION.name();
        }
    }

    public String step1() {
        try {
            if (abortButton != null) return "action.index";
            try {
                supplierPackList = supplierPackListHandler.step1(idToLoad, supplierPackList, storeId, employeeId, supplierId, vectorId);
                Supplier supplier = supplierPackList.getSupplier();
                if (supplier != null) supplierId = supplier.getId();
                Employee employee = supplierPackList.getEmployee();
                if (employee != null) employeeId = employee.getId();
                Store store = supplierPackList.getStore();
                if (store != null) storeId = store.getId();
                Vector vector = supplierPackList.getVector();
                if (vector != null) {
                    vectorId = vector.getId();
                    if (packListRate == null) packListRate = new PackListRate();
                    packListRate.setShippingCosts(vector.getBaseCost());
                }
            } catch (ValidateException e) {
                for (ValidateBean eb : e.getErrors()) addFieldError(eb.getKey(), getText(eb.getValue(), eb.getParams()));
                if (idToLoad > 0) {
                    supplierPackListForm = supplierPackListHandler.fillFormFieldsEdit(idToLoad);
                    return "jsp.editStep1";
                } else {
                    Employee employee = (Employee) session.getAttribute("EMPLOYEE");
                    if (employee != null) employeeId = employee.getId();
                    supplierPackListForm = supplierPackListHandler.fillFormFieldsCreate(employeeId);
                    return "jsp.createStep1";
                }
            }
            supplierPackListForm = supplierPackListHandler.fillFormFieldsStep1(idToLoad, storeId, supplierId, supplierPackList.getPackListType());
            if (idToLoad > 0) {
                products = productSplitter.compose(supplierPackList.getProductItems());
                if (PackListType.made.equals(supplierPackList.getPackListType()) || PackListType.repair.equals(supplierPackList.getPackListType()) || PackListType.substitution.equals(supplierPackList.getPackListType()) || PackListType.restitution.equals(supplierPackList.getPackListType())) return "jsp.editStep2"; else return "jsp.editStep3";
            } else {
                if (PackListType.made.equals(supplierPackList.getPackListType()) || PackListType.repair.equals(supplierPackList.getPackListType()) || PackListType.substitution.equals(supplierPackList.getPackListType()) || PackListType.restitution.equals(supplierPackList.getPackListType())) return "jsp.createStep2"; else return "jsp.createStep3";
            }
        } catch (Throwable e) {
            logger.error(e, e);
            addActionError(e.toString());
            return Outcome.JAVA_EXCEPTION.name();
        }
    }

    public String step2() {
        try {
            if (abortButton != null) return "action.index";
            try {
                supplierPackList = supplierPackListHandler.step2(idToLoad, supplierPackList, originalPackListId, originalBillId, storeId, employeeId, supplierId, vectorId);
            } catch (ValidateException e) {
                for (ValidateBean eb : e.getErrors()) addFieldError(eb.getKey(), getText(eb.getValue(), eb.getParams()));
                supplierPackListForm = supplierPackListHandler.fillFormFieldsStep1(idToLoad, storeId, supplierId, supplierPackList.getPackListType());
                if (supplierPackList == null) supplierPackList = new SupplierPackList();
                if (idToLoad > 0) {
                    supplierPackList = supplierPackListHandler.read(idToLoad);
                    Supplier supplier = supplierPackList.getSupplier();
                    if (supplier != null) supplierId = supplier.getId();
                    Employee employee = supplierPackList.getEmployee();
                    if (employee != null) employeeId = employee.getId();
                    Store store = supplierPackList.getStore();
                    if (store != null) storeId = store.getId();
                    Vector vector = supplierPackList.getVector();
                    if (vector != null) vectorId = vector.getId();
                    return "jsp.editStep2";
                } else {
                    Supplier supplier = (Supplier) e.getObjects().get("Supplier");
                    if (supplier != null) {
                        supplierId = supplier.getId();
                        supplierPackList.setSupplier(supplier);
                    }
                    Employee employee = (Employee) e.getObjects().get("Employee");
                    if (employee != null) {
                        employeeId = employee.getId();
                        supplierPackList.setEmployee(employee);
                    }
                    Store store = (Store) e.getObjects().get("Store");
                    if (store != null) {
                        storeId = store.getId();
                        supplierPackList.setStore(store);
                    }
                    Vector vector = (Vector) e.getObjects().get("Vector");
                    if (vector != null) {
                        vectorId = vector.getId();
                        supplierPackList.setVector(vector);
                    }
                    return "jsp.createStep2";
                }
            }
            supplierPackListForm = supplierPackListHandler.fillFormFieldsStep2(idToLoad, supplierId, originalPackListId, originalBillId, supplierPackList.getPackListType());
            if (idToLoad > 0) {
                products = productSplitter.compose(supplierPackList.getProductItems());
                return "jsp.editStep3";
            } else return "jsp.createStep3";
        } catch (Throwable e) {
            logger.error(e, e);
            addActionError(e.toString());
            return Outcome.JAVA_EXCEPTION.name();
        }
    }

    public String step3() {
        try {
            if (abortButton != null) return "action.index";
            try {
                chosenItems = productSplitter.split(products);
                supplierPackList = supplierPackListHandler.step3(idToLoad, supplierPackList, packListRate, storeId, originalPackListId, originalBillId, employeeId, supplierId, vectorId, chosenItems);
            } catch (ValidateException e) {
                for (ValidateBean eb : e.getErrors()) addFieldError(eb.getKey(), getText(eb.getValue(), eb.getParams()));
                supplierPackListForm = supplierPackListHandler.fillFormFieldsStep2(idToLoad, supplierId, originalPackListId, originalBillId, supplierPackList.getPackListType());
                if (supplierPackList == null) supplierPackList = new SupplierPackList();
                if (idToLoad > 0) {
                    supplierPackList = supplierPackListHandler.read(idToLoad);
                    Supplier supplier = supplierPackList.getSupplier();
                    if (supplier != null) supplierId = supplier.getId();
                    Employee employee = supplierPackList.getEmployee();
                    if (employee != null) employeeId = employee.getId();
                    Store store = supplierPackList.getStore();
                    if (store != null) storeId = store.getId();
                    Vector vector = supplierPackList.getVector();
                    if (vector != null) vectorId = vector.getId();
                    return "jsp.editStep3";
                } else {
                    Supplier supplier = (Supplier) e.getObjects().get("Supplier");
                    if (supplier != null) {
                        supplierId = supplier.getId();
                        supplierPackList.setSupplier(supplier);
                    }
                    Employee employee = (Employee) e.getObjects().get("Employee");
                    if (employee != null) {
                        employeeId = employee.getId();
                        supplierPackList.setEmployee(employee);
                    }
                    Store store = (Store) e.getObjects().get("Store");
                    if (store != null) {
                        storeId = store.getId();
                        supplierPackList.setStore(store);
                    }
                    Vector vector = (Vector) e.getObjects().get("Vector");
                    if (vector != null) {
                        vectorId = vector.getId();
                        supplierPackList.setVector(vector);
                    }
                    return "jsp.createStep3";
                }
            }
            Employee employee = (Employee) session.getAttribute("EMPLOYEE");
            if (employee != null) employeeId = employee.getId();
            supplierPackListForm = supplierPackListHandler.fillFormFieldsIndex(employeeId);
            return "action.index";
        } catch (Throwable e) {
            logger.error(e, e);
            addActionError(e.toString());
            return Outcome.JAVA_EXCEPTION.name();
        }
    }

    public String delete() {
        try {
            supplierPackListHandler.delete(idToLoad, storeId);
            return "action.index";
        } catch (ValidateException e) {
            for (ValidateBean eb : e.getErrors()) setForwardedError(getText(eb.getValue(), eb.getParams()));
            return "action.index";
        } catch (Throwable e) {
            logger.error(e, e);
            addActionError(e.toString());
            return Outcome.JAVA_EXCEPTION.name();
        }
    }

    public SupplierPackList getSupplierPackList() {
        return supplierPackList;
    }

    public void setSupplierPackList(SupplierPackList supplierPackList) {
        this.supplierPackList = supplierPackList;
    }

    public SupplierPackListForm getSupplierPackListForm() {
        return supplierPackListForm;
    }

    public void setSupplierPackListForm(SupplierPackListForm supplierPackListForm) {
        this.supplierPackListForm = supplierPackListForm;
    }

    public SupplierPackListHandler getSupplierPackListHandler() {
        return supplierPackListHandler;
    }

    public void setSupplierPackListHandler(SupplierPackListHandler supplierPackListHandler) {
        this.supplierPackListHandler = supplierPackListHandler;
    }

    public PackListRate getPackListRate() {
        return packListRate;
    }

    public void setPackListRate(PackListRate packListRate) {
        this.packListRate = packListRate;
    }

    public SupplierPackListSearch getSupplierPackListSearch() {
        return supplierPackListSearch;
    }

    public void setSupplierPackListSearch(SupplierPackListSearch supplierPackListSearch) {
        this.supplierPackListSearch = supplierPackListSearch;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(long employeeId) {
        this.employeeId = employeeId;
    }

    public long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(long paymentId) {
        this.paymentId = paymentId;
    }

    public long getStoreId() {
        return storeId;
    }

    public void setStoreId(long storeId) {
        this.storeId = storeId;
    }

    public long getVectorId() {
        return vectorId;
    }

    public void setVectorId(long vectorId) {
        this.vectorId = vectorId;
    }

    public Map<Long, Float> getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(Map<Long, Float> availableItems) {
        this.availableItems = availableItems;
    }

    public Map<Long, Float> getChosenItems() {
        return chosenItems;
    }

    public void setChosenItems(Map<Long, Float> chosenItems) {
        this.chosenItems = chosenItems;
    }

    public String getAbortButton() {
        return abortButton;
    }

    public void setAbortButton(String abortButton) {
        this.abortButton = abortButton;
    }

    public String getBackButton() {
        return backButton;
    }

    public void setBackButton(String backButton) {
        this.backButton = backButton;
    }

    public String getDeleteButton() {
        return deleteButton;
    }

    public void setDeleteButton(String deleteButton) {
        this.deleteButton = deleteButton;
    }

    public String getInsertButton() {
        return insertButton;
    }

    public void setInsertButton(String insertButton) {
        this.insertButton = insertButton;
    }

    public String getLoadButton() {
        return loadButton;
    }

    public void setLoadButton(String loadButton) {
        this.loadButton = loadButton;
    }

    public String getEditButton() {
        return editButton;
    }

    public void setEditButton(String editButton) {
        this.editButton = editButton;
    }

    public String getNewButton() {
        return newButton;
    }

    public void setNewButton(String newButton) {
        this.newButton = newButton;
    }

    public String getSaveButton() {
        return saveButton;
    }

    public void setSaveButton(String saveButton) {
        this.saveButton = saveButton;
    }

    public String getSearchButton() {
        return searchButton;
    }

    public void setSearchButton(String searchButton) {
        this.searchButton = searchButton;
    }

    public PaginateUtil getPaginateUtil() {
        return paginateUtil;
    }

    public void setPaginateUtil(PaginateUtil paginateUtil) {
        this.paginateUtil = paginateUtil;
    }

    public PaginatedList getPagList() {
        return pagList;
    }

    public void setPagList(PaginatedList pagList) {
        this.pagList = pagList;
    }

    public WhereBuilder getWhereBuilder() {
        return whereBuilder;
    }

    public void setWhereBuilder(WhereBuilder whereBuilder) {
        this.whereBuilder = whereBuilder;
    }

    public int getPageItem() {
        return pageItem;
    }

    public void setPageItem(int pageItem) {
        this.pageItem = pageItem;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public ProductSplitter getProductSplitter() {
        return productSplitter;
    }

    public void setProductSplitter(ProductSplitter productSplitter) {
        this.productSplitter = productSplitter;
    }

    public String getPdfButton() {
        return pdfButton;
    }

    public void setPdfButton(String pdfButton) {
        this.pdfButton = pdfButton;
    }

    public long getOriginalPackListId() {
        return originalPackListId;
    }

    public void setOriginalPackListId(long originalPackListId) {
        this.originalPackListId = originalPackListId;
    }

    public List<PackListType> getPackListTypes() {
        return packListTypes;
    }

    public void setPackListTypes(List<PackListType> packListTypes) {
        this.packListTypes = packListTypes;
    }

    public long getOriginalBillId() {
        return originalBillId;
    }

    public void setOriginalBillId(long originalBillId) {
        this.originalBillId = originalBillId;
    }
}
