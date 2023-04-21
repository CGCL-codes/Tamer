package org.shopformat.controller.admin.products;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Vector;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.myfaces.trinidad.component.core.input.CoreInputText;
import org.apache.myfaces.trinidad.event.DisclosureEvent;
import org.apache.myfaces.trinidad.model.UploadedFile;
import org.hibernate.Criteria;
import org.jfree.data.general.AbstractDataset;
import org.shopformat.controller.admin.crud.form.SessionCrudFormViewImpl;
import org.shopformat.controller.admin.crud.list.CrudList;
import org.shopformat.controller.admin.crud.list.OrderedCrudList;
import org.shopformat.controller.admin.crud.list.StackedCrudListImpl;
import org.shopformat.controller.admin.reports.ChartService;
import org.shopformat.domain.order.purchase.PurchaseOrder;
import org.shopformat.domain.order.sales.SalesOrder;
import org.shopformat.domain.product.Brand;
import org.shopformat.domain.product.Category;
import org.shopformat.domain.product.ProductGroup;
import org.shopformat.domain.product.ProductItem;
import org.shopformat.services.ImageUtils;

public class ProductGroupForm extends SessionCrudFormViewImpl<Long, ProductGroup> {

    private static Log log = LogFactory.getLog(ProductGroupForm.class);

    private CoreInputText descriptionTextArea;

    private CoreInputText extraDetailTextArea;

    private StackedCrudListImpl<Long, PurchaseOrder, Long, ProductGroup> purchaseOrderList;

    private StackedCrudListImpl<Long, SalesOrder, Long, ProductGroup> salesOrderList;

    private ProductItemList productItemList;

    private boolean descriptionTabSelected = false;

    @Override
    public void init() {
        super.init();
        productItemList = new ProductItemList();
        salesOrderList = new StackedCrudListImpl<Long, SalesOrder, Long, ProductGroup>(this);
        purchaseOrderList = new StackedCrudListImpl<Long, PurchaseOrder, Long, ProductGroup>(this);
    }

    @Override
    public void preprocess() {
        super.preprocess();
        productItemList.setProductGroup((ProductGroup) getCrudObject());
        productItemList.setListData(loadProductItemDataModel());
        buildLists();
    }

    @Override
    public void prerender() {
        super.prerender();
        productItemList.setListData(loadProductItemDataModel());
        if (!isPostBack()) {
            buildLists();
        }
    }

    @Override
    public String cancelPressed() {
        setBean("productGroup", getCrudObject());
        return super.cancelPressed();
    }

    private void buildLists() {
        ProductGroup productGroup = (ProductGroup) getCrudObject();
        ListDataModel model = new ListDataModel();
        model.setWrappedData(getShopService().getSalesOrdersForProduct(productGroup));
        salesOrderList.setListData(model);
        salesOrderList.setCrudFormOutcome("salesOrderForm");
        model = new ListDataModel();
        model.setWrappedData(getShopService().getPurchaseOrdersForProduct(productGroup));
        purchaseOrderList.setListData(model);
        purchaseOrderList.setCrudFormOutcome("purchaseOrderForm");
    }

    @Override
    public void createLinkActivated(ActionEvent event) {
        super.createLinkActivated(event);
        ProductGroup productGroup = (ProductGroup) getCrudObject();
        productGroup.setBrand((Brand) getBean("brand"));
        productGroup.setCategory((Category) getBean("category"));
    }

    @Override
    protected ProductGroup instantiateCrudObject() {
        return new ProductGroup();
    }

    public void smallImageChanged(ValueChangeEvent event) {
        ProductGroup productGroup = (ProductGroup) getCrudObject();
        saveSmallImage(productGroup.getDisplayId(), (UploadedFile) event.getNewValue());
    }

    public void largeImageChanged(ValueChangeEvent event) {
        saveLargeImage((ProductGroup) getCrudObject(), 0, (UploadedFile) event.getNewValue());
    }

    public void largeImage1Changed(ValueChangeEvent event) {
        saveLargeImage((ProductGroup) getCrudObject(), 1, (UploadedFile) event.getNewValue());
    }

    public void largeImage2Changed(ValueChangeEvent event) {
        saveLargeImage((ProductGroup) getCrudObject(), 2, (UploadedFile) event.getNewValue());
    }

    public void largeImage3Changed(ValueChangeEvent event) {
        saveLargeImage((ProductGroup) getCrudObject(), 3, (UploadedFile) event.getNewValue());
    }

    private void saveLargeImage(ProductGroup productGroup, int imageNumber, UploadedFile imageFile) {
        try {
            if (imageFile != null) {
                String displayId = productGroup.getDisplayId();
                if (imageNumber > 0) {
                    displayId = displayId + imageNumber;
                    if (productGroup.getExtraImagesCount() < imageNumber) {
                        productGroup.setExtraImagesCount(imageNumber);
                    }
                }
                Image image = ImageIO.read(imageFile.getInputStream());
                String path = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getContext("/content").getRealPath("/images/products");
                ImageUtils.saveImage(ImageUtils.constrainImage(image, 280, 280), new File(path + "/large/" + displayId + ".jpg").getPath());
                ImageUtils.saveImage(ImageUtils.constrainImage(image, 500, 500), new File(path + "/xlarge/" + displayId + ".jpg").getPath());
            }
        } catch (IOException ex) {
            log.error("Problem saving large image for " + productGroup.getId(), ex);
        }
    }

    private void saveSmallImage(String displayId, UploadedFile imageFile) {
        try {
            if (imageFile != null) {
                Image image = ImageIO.read(imageFile.getInputStream());
                String path = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getContext("/content").getRealPath("/images/products");
                ImageUtils.saveImage(ImageUtils.constrainImage(image, 60, 60), new File(path + "/xsml/" + displayId + ".jpg").getPath());
                ImageUtils.saveImage(ImageUtils.constrainImage(image, 120, 85), new File(path + "/sml/" + displayId + ".jpg").getPath());
            }
        } catch (IOException ex) {
            log.error("Problem saving small image for " + displayId, ex);
        }
    }

    public CoreInputText getDescriptionTextArea() {
        return descriptionTextArea;
    }

    public void setDescriptionTextArea(CoreInputText descriptionTextArea) {
        this.descriptionTextArea = descriptionTextArea;
    }

    public CoreInputText getExtraDetailTextArea() {
        return extraDetailTextArea;
    }

    public void setExtraDetailTextArea(CoreInputText extraDetailTextArea) {
        this.extraDetailTextArea = extraDetailTextArea;
    }

    public String getDescriptionTextAreaId() {
        return descriptionTextArea.getClientId(FacesContext.getCurrentInstance());
    }

    public String getExtraDetailTextAreaId() {
        return extraDetailTextArea.getClientId(FacesContext.getCurrentInstance());
    }

    public StackedCrudListImpl<Long, PurchaseOrder, Long, ProductGroup> getPurchaseOrderList() {
        return purchaseOrderList;
    }

    public StackedCrudListImpl<Long, SalesOrder, Long, ProductGroup> getSalesOrderList() {
        return salesOrderList;
    }

    public AbstractDataset getMonthsSalesChart() {
        return ChartService.getSalesChart((ProductGroup) getCrudObject(), ChartService.MONTH_RANGE);
    }

    public AbstractDataset getQuartersSalesChart() {
        return ChartService.getSalesChart((ProductGroup) getCrudObject(), ChartService.QUARTER_RANGE);
    }

    public AbstractDataset getYearsSalesChart() {
        return ChartService.getSalesChart((ProductGroup) getCrudObject(), ChartService.YEAR_RANGE);
    }

    public OrderedCrudList<Long, ProductItem> getProductItemList() {
        return productItemList;
    }

    public void setProductItemList(ProductItemList productItemList) {
        this.productItemList = productItemList;
    }

    private DataModel loadProductItemDataModel() {
        DataModel dataModel = new ListDataModel();
        ProductGroup productGroup = (ProductGroup) getCrudObject();
        dataModel.setWrappedData(productGroup.getProductItems());
        return dataModel;
    }

    public void descriptionTabSelected(DisclosureEvent event) {
        descriptionTabSelected = event.isExpanded();
    }

    public boolean isDescriptionTabSelected() {
        return descriptionTabSelected;
    }

    public void setDescriptionTabSelected(boolean descriptionTabSelected) {
        this.descriptionTabSelected = descriptionTabSelected;
    }
}
