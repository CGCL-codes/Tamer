package com.hilaver.dzmis.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import com.hilaver.dzmis.Constants;
import com.hilaver.dzmis.basicinfo.BiAccessoryButton;
import com.hilaver.dzmis.basicinfo.BiAccessoryOthers;
import com.hilaver.dzmis.basicinfo.BiAccessoryZip;
import com.hilaver.dzmis.basicinfo.BiDZColor;
import com.hilaver.dzmis.basicinfo.BiMeasureSpecification;
import com.hilaver.dzmis.codetable.CtColorFamily;
import com.hilaver.dzmis.codetable.CtColorType;
import com.hilaver.dzmis.codetable.CtDyeing;
import com.hilaver.dzmis.codetable.CtFilsType;
import com.hilaver.dzmis.codetable.CtSeasonNumber;
import com.hilaver.dzmis.codetable.CtSendBy;
import com.hilaver.dzmis.codetable.CtYarnMaterial;
import com.hilaver.dzmis.codetable.CtYarnMeasure;
import com.hilaver.dzmis.dao.StockDAO;
import com.hilaver.dzmis.order.OrderProduct;
import com.hilaver.dzmis.order.OrderProductItem;
import com.hilaver.dzmis.product.ProductAccessoryButton;
import com.hilaver.dzmis.product.ProductAccessoryOthers;
import com.hilaver.dzmis.product.ProductAccessoryZip;
import com.hilaver.dzmis.product.ProductDZColor;
import com.hilaver.dzmis.product.ProductIdentification;
import com.hilaver.dzmis.product.ProductLaunching;
import com.hilaver.dzmis.service.AbstractBaseService;
import com.hilaver.dzmis.util.SimpleObj2XML;
import com.hilaver.dzmis.util.StringUtils;

public class StockServiceImpl extends AbstractBaseService {

    private CtServiceImpl ctService;

    private BiAccessoryButtonServiceImpl buttonService;

    private BiAccessoryZipServiceImpl zipService;

    private BiAccessoryOthersServiceImpl othersService;

    private BiDZColorServiceImpl colorService;

    protected StockDAO dao;

    public StockServiceImpl() {
        this.ctService = new CtServiceImpl();
        this.buttonService = new BiAccessoryButtonServiceImpl();
        this.zipService = new BiAccessoryZipServiceImpl();
        this.othersService = new BiAccessoryOthersServiceImpl();
        this.colorService = new BiDZColorServiceImpl();
        this.dao = new StockDAO();
    }

    public String getDZColorStock(int page, String sort, String order, String[] filters) throws Exception {
        this.colorService.setRealFileDir(this.realFileDir);
        this.colorService.setLocale(this.locale);
        int offset = Constants.PAGE_SIZE * (page - 1);
        List<BiDZColor> objList = this.dao.getYarnAllPagination(offset, Constants.PAGE_SIZE, sort, order, filters);
        StringBuffer xmlSB = new StringBuffer();
        xmlSB.append(Constants.XML_DOC_DEFINE);
        xmlSB.append(Constants.XML_ROOT_TAG[0]);
        for (BiDZColor obj : objList) {
            writeFile(realFileDir + obj.getPhoto1Name(), obj.getPhoto1());
            writeFile(realFileDir + obj.getPhoto2Name(), obj.getPhoto2());
            obj = adjustDZColorStock(obj);
            xmlSB.append(Constants.XML_ITEM_TAG[0]);
            xmlSB.append(Constants.XML_INDEX_TAG[0]);
            xmlSB.append(++offset);
            xmlSB.append(Constants.XML_INDEX_TAG[1]);
            xmlSB.append("<factoryNameCN>");
            xmlSB.append(Constants.XML_CDATA[0]);
            if (obj.getBiYarn().getBiFactory() != null) {
                xmlSB.append(obj.getBiYarn().getBiFactory().getNameCN());
            } else {
                xmlSB.append("");
            }
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</factoryNameCN>");
            xmlSB.append("<factoryNameEN>");
            xmlSB.append(Constants.XML_CDATA[0]);
            if (obj.getBiYarn().getBiFactory() != null) {
                xmlSB.append(obj.getBiYarn().getBiFactory().getNameEN());
            } else {
                xmlSB.append("");
            }
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</factoryNameEN>");
            xmlSB.append("<yarnReference>");
            xmlSB.append(Constants.XML_CDATA[0]);
            xmlSB.append(obj.getBiYarn().getReference());
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</yarnReference>");
            xmlSB.append("<ctYarnMeasure>");
            xmlSB.append(Constants.XML_CDATA[0]);
            xmlSB.append(this.ctService.getDescription(CtYarnMeasure.class.getName(), obj.getBiYarn().getCtYarnMeasure(), locale));
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</ctYarnMeasure>");
            xmlSB.append("<yarnAccount>");
            xmlSB.append(Constants.XML_CDATA[0]);
            xmlSB.append(obj.getBiYarn().getYarnAccount() == null ? "" : obj.getBiYarn().getYarnAccount());
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</yarnAccount>");
            xmlSB.append("<ctYarnMaterial1>");
            xmlSB.append(Constants.XML_CDATA[0]);
            xmlSB.append(this.ctService.getDescription(CtYarnMaterial.class.getName(), obj.getBiYarn().getCtYarnMaterial1(), locale));
            if (!StringUtils.isEmpty(obj.getBiYarn().getCtYarnMaterial1()) && obj.getBiYarn().getComposition1() != null) {
                xmlSB.append("&nbsp;(&nbsp;" + obj.getBiYarn().getComposition1() + "%&nbsp;)");
            }
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</ctYarnMaterial1>");
            xmlSB.append("<ctYarnMaterial2>");
            xmlSB.append(Constants.XML_CDATA[0]);
            xmlSB.append(this.ctService.getDescription(CtYarnMaterial.class.getName(), obj.getBiYarn().getCtYarnMaterial2(), locale));
            if (!StringUtils.isEmpty(obj.getBiYarn().getCtYarnMaterial2()) && obj.getBiYarn().getComposition2() != null) {
                xmlSB.append("&nbsp;(&nbsp;" + obj.getBiYarn().getComposition2() + "%&nbsp;)");
            }
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</ctYarnMaterial2>");
            xmlSB.append("<ctYarnMaterial3>");
            xmlSB.append(Constants.XML_CDATA[0]);
            xmlSB.append(this.ctService.getDescription(CtYarnMaterial.class.getName(), obj.getBiYarn().getCtYarnMaterial3(), locale));
            if (!StringUtils.isEmpty(obj.getBiYarn().getCtYarnMaterial3()) && obj.getBiYarn().getComposition3() != null) {
                xmlSB.append("&nbsp;(&nbsp;" + obj.getBiYarn().getComposition3() + "%&nbsp;)");
            }
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</ctYarnMaterial3>");
            xmlSB.append("<ctYarnMaterial4>");
            xmlSB.append(Constants.XML_CDATA[0]);
            xmlSB.append(this.ctService.getDescription(CtYarnMaterial.class.getName(), obj.getBiYarn().getCtYarnMaterial4(), locale));
            if (!StringUtils.isEmpty(obj.getBiYarn().getCtYarnMaterial4()) && obj.getBiYarn().getComposition4() != null) {
                xmlSB.append("&nbsp;(&nbsp;" + obj.getBiYarn().getComposition4() + "%&nbsp;)");
            }
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</ctYarnMaterial4>");
            xmlSB.append("<ctYarnMaterial5>");
            xmlSB.append(Constants.XML_CDATA[0]);
            xmlSB.append(this.ctService.getDescription(CtYarnMaterial.class.getName(), obj.getBiYarn().getCtYarnMaterial5(), locale));
            if (!StringUtils.isEmpty(obj.getBiYarn().getCtYarnMaterial5()) && obj.getBiYarn().getComposition5() != null) {
                xmlSB.append("&nbsp;(&nbsp;" + obj.getBiYarn().getComposition5() + "%&nbsp;)");
            }
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</ctYarnMaterial5>");
            xmlSB.append("<ctDyeing>");
            xmlSB.append(Constants.XML_CDATA[0]);
            xmlSB.append(this.ctService.getDescription(CtDyeing.class.getName(), obj.getCtDyeing(), locale));
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</ctDyeing>");
            xmlSB.append("<ctFilsType>");
            xmlSB.append(Constants.XML_CDATA[0]);
            xmlSB.append(this.ctService.getDescription(CtFilsType.class.getName(), obj.getCtFilsType(), locale));
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</ctFilsType>");
            xmlSB.append("<ctColorFamily>");
            xmlSB.append(Constants.XML_CDATA[0]);
            xmlSB.append(this.ctService.getDescription(CtColorFamily.class.getName(), obj.getCtColorFamily(), locale));
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</ctColorFamily>");
            xmlSB.append("<ctColorType>");
            xmlSB.append(Constants.XML_CDATA[0]);
            xmlSB.append(this.ctService.getDescription(CtColorType.class.getName(), obj.getCtColorType(), locale));
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</ctColorType>");
            xmlSB.append("<ctSeasonNumber>");
            xmlSB.append(Constants.XML_CDATA[0]);
            xmlSB.append(this.ctService.getDescription(CtSeasonNumber.class.getName(), obj.getCtSeasonNumber(), locale));
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</ctSeasonNumber>");
            xmlSB.append("<ctSendBy1>");
            xmlSB.append(Constants.XML_CDATA[0]);
            xmlSB.append(this.ctService.getDescription(CtSendBy.class.getName(), obj.getCtSendBy1(), locale));
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</ctSendBy1>");
            xmlSB.append("<ctSendBy2>");
            xmlSB.append(Constants.XML_CDATA[0]);
            xmlSB.append(this.ctService.getDescription(CtSendBy.class.getName(), obj.getCtSendBy2(), locale));
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</ctSendBy2>");
            xmlSB.append("<photo1Name>");
            xmlSB.append(Constants.XML_CDATA[0]);
            if (obj.getPhoto1Name() == null) {
                xmlSB.append(NO_IMAGE_NAME);
            } else {
                xmlSB.append(obj.getPhoto1Name());
            }
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</photo1Name>");
            xmlSB.append("<photo2Name>");
            xmlSB.append(Constants.XML_CDATA[0]);
            if (obj.getPhoto2Name() == null) {
                xmlSB.append(NO_IMAGE_NAME);
            } else {
                xmlSB.append(obj.getPhoto2Name());
            }
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</photo2Name>");
            xmlSB.append("<photo3Name>");
            xmlSB.append(Constants.XML_CDATA[0]);
            xmlSB.append(this.colorService.getIsColorApprovedPhoto(obj));
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</photo3Name>");
            if (obj.getBiFranceCustomer() != null) {
                xmlSB.append("<" + COLUMN_CUSTOMER_NAME + ">");
                xmlSB.append(Constants.XML_CDATA[0]);
                xmlSB.append(obj.getBiFranceCustomer().getName());
                xmlSB.append(Constants.XML_CDATA[1]);
                xmlSB.append("</" + COLUMN_CUSTOMER_NAME + ">");
                xmlSB.append("<" + COLUMN_CUSTOEMR_DEPARTMENT + ">");
                xmlSB.append(Constants.XML_CDATA[0]);
                xmlSB.append(obj.getBiFranceCustomer().getDepartment() == null ? "" : obj.getBiFranceCustomer().getDepartment());
                xmlSB.append(Constants.XML_CDATA[1]);
                xmlSB.append("</" + COLUMN_CUSTOEMR_DEPARTMENT + ">");
            }
            xmlSB.append("<isApproved>");
            xmlSB.append(Constants.XML_CDATA[0]);
            xmlSB.append(this.colorService.getIsColorApproved(obj));
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</isApproved>");
            xmlSB.append(SimpleObj2XML.toXMLFragment(obj));
            xmlSB.append(Constants.XML_ITEM_TAG[1]);
        }
        xmlSB.append(Constants.XML_CURRENT_PAGE_TAG[0]);
        xmlSB.append(page);
        xmlSB.append(Constants.XML_CURRENT_PAGE_TAG[1]);
        xmlSB.append(Constants.XML_ITEM_TOTAL_TAG[0]);
        xmlSB.append(this.dao.getYarnCount(filters));
        xmlSB.append(Constants.XML_ITEM_TOTAL_TAG[1]);
        xmlSB.append(Constants.XML_ROOT_TAG[1]);
        return xmlSB.toString();
    }

    private BiDZColor adjustDZColorStock(BiDZColor color) throws Exception {
        Set<ProductDZColor> pDZColor = color.getProductDZColors();
        Float total = 0f;
        Float bookedTotal = 0f;
        for (ProductDZColor productDZColor : pDZColor) {
            ProductIdentification pi = productDZColor.getProductIdentification();
            Set<OrderProduct> ops = pi.getOrderProducts();
            for (OrderProduct orderProduct : ops) {
                Set<OrderProductItem> opis = orderProduct.getOrderProductItems();
                for (OrderProductItem orderProductItem : opis) {
                    if (productDZColor.getVariant().equals(orderProductItem.getProductDZColor().getVariant())) {
                        if ("01".equals(pi.getIsConfirmed())) {
                            total += productDZColor.getActualWeight() * orderProductItem.getTotal();
                        } else {
                            bookedTotal += productDZColor.getActualWeight() * orderProductItem.getTotal();
                        }
                    }
                }
            }
            Set<ProductLaunching> pls = pi.getProductLaunchings();
            for (ProductLaunching productLaunching : pls) {
                if (productDZColor.getVariant().equals(productLaunching.getVariant())) {
                    if ("01".equals(productLaunching.getIsStock())) {
                        total += (productDZColor.getActualWeight() * productLaunching.getQuantity1());
                        if (productLaunching.getQuantity2() != null) {
                            total += (productDZColor.getActualWeight() * 0.1f);
                        }
                    } else {
                        bookedTotal += (productDZColor.getActualWeight() * productLaunching.getQuantity1());
                        if (productLaunching.getQuantity2() != null) {
                            bookedTotal += (productDZColor.getActualWeight() * 0.1f);
                        }
                    }
                }
            }
        }
        color.setBookedStock(bookedTotal);
        color.setOutStock(total);
        this.dao.saveOrUpdate(color);
        return color;
    }

    public String getButtonStock(int page, String sort, String order, String[] filters) throws Exception {
        this.buttonService.setRealFileDir(this.realFileDir);
        this.buttonService.setLocale(this.locale);
        int offset = Constants.PAGE_SIZE * (page - 1);
        List<BiAccessoryButton> objList = this.dao.getButtonAllPagination(offset, Constants.PAGE_SIZE, sort, order, filters);
        StringBuffer xmlSB = new StringBuffer();
        xmlSB.append(Constants.XML_DOC_DEFINE);
        xmlSB.append(Constants.XML_ROOT_TAG[0]);
        for (BiAccessoryButton obj : objList) {
            xmlSB.append(Constants.XML_ITEM_TAG[0]);
            xmlSB.append(Constants.XML_INDEX_TAG[0]);
            xmlSB.append(++offset);
            xmlSB.append(Constants.XML_INDEX_TAG[1]);
            this.adjustButtonStock(obj);
            BiAccessoryButton newObj = (BiAccessoryButton) this.dao.get(BiAccessoryButton.class.getName(), obj.getId());
            xmlSB.append(this.buttonService.toXMLFragment(newObj));
            xmlSB.append(Constants.XML_ITEM_TAG[1]);
        }
        xmlSB.append(Constants.XML_CURRENT_PAGE_TAG[0]);
        xmlSB.append(page);
        xmlSB.append(Constants.XML_CURRENT_PAGE_TAG[1]);
        xmlSB.append(Constants.XML_ITEM_TOTAL_TAG[0]);
        xmlSB.append(this.dao.getButtonCount(filters));
        xmlSB.append(Constants.XML_ITEM_TOTAL_TAG[1]);
        xmlSB.append(Constants.XML_ROOT_TAG[1]);
        return xmlSB.toString();
    }

    private void adjustButtonStock(BiAccessoryButton bab) throws Exception {
        Set<ProductAccessoryButton> pabs = bab.getProductAccessoryButtons();
        Integer total = 0;
        Integer bookedTotal = 0;
        for (ProductAccessoryButton productAccessoryButton : pabs) {
            ProductIdentification pi = (ProductIdentification) this.dao.get(ProductIdentification.class.getName(), productAccessoryButton.getProductIdentification().getId());
            BiMeasureSpecification biMS = pi.getBiMeasureSpecification();
            if (biMS == null) continue;
            String[] sizeNames = BiMeasureSpecificationServiceImpl.getSizeName(biMS.getCtStature());
            Set<OrderProduct> orderProducts = pi.getOrderProducts();
            for (OrderProduct orderProduct : orderProducts) {
                Set<OrderProductItem> orderProductItems = orderProduct.getOrderProductItems();
                if ("01".equals(pi.getIsConfirmed())) {
                    total += (productAccessoryButton.getUnitNumber() + productAccessoryButton.getExtra()) * orderProduct.getTotal(this.getSizeIndex(sizeNames, productAccessoryButton.getForSize()), productAccessoryButton.getVariant());
                } else {
                    bookedTotal += (productAccessoryButton.getUnitNumber() + productAccessoryButton.getExtra()) * orderProduct.getTotal(this.getSizeIndex(sizeNames, productAccessoryButton.getForSize()), productAccessoryButton.getVariant());
                }
            }
            Set<ProductLaunching> pls = pi.getProductLaunchings();
            for (ProductLaunching productLaunching : pls) {
                if (productAccessoryButton.getVariant().equals(productLaunching.getVariant()) && productAccessoryButton.getForSize().equalsIgnoreCase(productLaunching.getSize()) || productAccessoryButton.getVariant().equals("all") && productAccessoryButton.getForSize().equalsIgnoreCase(productLaunching.getSize()) || productAccessoryButton.getVariant().equalsIgnoreCase(productLaunching.getVariant()) && productAccessoryButton.getForSize().equals("all") || productAccessoryButton.getVariant().equals("all") && productAccessoryButton.getForSize().equals("all")) {
                    if ("01".equals(productLaunching.getIsStock())) {
                        total += (productAccessoryButton.getUnitNumber() + productAccessoryButton.getExtra()) * productLaunching.getQuantity1();
                    } else {
                        bookedTotal += (productAccessoryButton.getUnitNumber() + productAccessoryButton.getExtra()) * productLaunching.getQuantity1();
                    }
                }
            }
        }
        bab.setBookedStock(bookedTotal);
        bab.setOutStock(total);
        this.dao.saveOrUpdate(bab);
    }

    public String getZipStock(int page, String sort, String order, String[] filters) throws Exception {
        this.zipService.setRealFileDir(this.realFileDir);
        this.zipService.setLocale(this.locale);
        int offset = Constants.PAGE_SIZE * (page - 1);
        List<BiAccessoryZip> objList = this.dao.getZipAllPagination(offset, Constants.PAGE_SIZE, sort, order, filters);
        this.zipService.setLocale(locale);
        StringBuffer xmlSB = new StringBuffer();
        xmlSB.append(Constants.XML_DOC_DEFINE);
        xmlSB.append(Constants.XML_ROOT_TAG[0]);
        for (BiAccessoryZip obj : objList) {
            xmlSB.append(Constants.XML_ITEM_TAG[0]);
            xmlSB.append(Constants.XML_INDEX_TAG[0]);
            xmlSB.append(++offset);
            xmlSB.append(Constants.XML_INDEX_TAG[1]);
            this.adjustZipStock(obj);
            BiAccessoryZip newObj = (BiAccessoryZip) this.dao.get(BiAccessoryZip.class.getName(), obj.getId());
            xmlSB.append(this.zipService.toXMLFragment(newObj));
            xmlSB.append(Constants.XML_ITEM_TAG[1]);
        }
        xmlSB.append(Constants.XML_CURRENT_PAGE_TAG[0]);
        xmlSB.append(page);
        xmlSB.append(Constants.XML_CURRENT_PAGE_TAG[1]);
        xmlSB.append(Constants.XML_ITEM_TOTAL_TAG[0]);
        xmlSB.append(this.dao.getZipCount(filters));
        xmlSB.append(Constants.XML_ITEM_TOTAL_TAG[1]);
        xmlSB.append(Constants.XML_ROOT_TAG[1]);
        return xmlSB.toString();
    }

    private void adjustZipStock(BiAccessoryZip baz) throws Exception {
        Set<ProductAccessoryZip> pazs = baz.getProductAccessoryZips();
        Integer total = 0;
        Integer bookedTotal = 0;
        for (ProductAccessoryZip productAccessoryZip : pazs) {
            ProductIdentification pi = (ProductIdentification) this.dao.get(ProductIdentification.class.getName(), productAccessoryZip.getProductIdentification().getId());
            BiMeasureSpecification biMS = pi.getBiMeasureSpecification();
            if (biMS == null) continue;
            String[] sizeNames = BiMeasureSpecificationServiceImpl.getSizeName(biMS.getCtStature());
            Set<OrderProduct> orderProducts = pi.getOrderProducts();
            for (OrderProduct orderProduct : orderProducts) {
                Set<OrderProductItem> orderProductItems = orderProduct.getOrderProductItems();
                if ("01".equals(pi.getIsConfirmed())) {
                    total += productAccessoryZip.getUnitNumber() * orderProduct.getTotal(this.getSizeIndex(sizeNames, productAccessoryZip.getForSize()), productAccessoryZip.getVariant());
                } else {
                    bookedTotal += productAccessoryZip.getUnitNumber() * orderProduct.getTotal(this.getSizeIndex(sizeNames, productAccessoryZip.getForSize()), productAccessoryZip.getVariant());
                }
            }
            Set<ProductLaunching> pls = pi.getProductLaunchings();
            for (ProductLaunching productLaunching : pls) {
                if (productAccessoryZip.getVariant().equals(productLaunching.getVariant()) && productAccessoryZip.getForSize().equalsIgnoreCase(productLaunching.getSize()) || productAccessoryZip.getVariant().equals("all") && productAccessoryZip.getForSize().equalsIgnoreCase(productLaunching.getSize()) || productAccessoryZip.getVariant().equalsIgnoreCase(productLaunching.getVariant()) && productAccessoryZip.getForSize().equals("all") || productAccessoryZip.getVariant().equals("all") && productAccessoryZip.getForSize().equals("all")) {
                    if ("01".equals(productLaunching.getIsStock())) {
                        total += (productAccessoryZip.getUnitNumber() * productLaunching.getQuantity1());
                    } else {
                        bookedTotal += (productAccessoryZip.getUnitNumber() * productLaunching.getQuantity1());
                    }
                }
            }
        }
        baz.setBookedStock(bookedTotal);
        baz.setOutStock(total);
        this.dao.saveOrUpdate(baz);
    }

    public String getOthersStock(int page, String sort, String order, String[] filters) throws Exception {
        this.othersService.setRealFileDir(this.realFileDir);
        this.othersService.setLocale(this.locale);
        int offset = Constants.PAGE_SIZE * (page - 1);
        List<BiAccessoryOthers> objList = this.dao.getOthersAllPagination(offset, Constants.PAGE_SIZE, sort, order, filters);
        StringBuffer xmlSB = new StringBuffer();
        xmlSB.append(Constants.XML_DOC_DEFINE);
        xmlSB.append(Constants.XML_ROOT_TAG[0]);
        for (BiAccessoryOthers obj : objList) {
            xmlSB.append(Constants.XML_ITEM_TAG[0]);
            xmlSB.append(Constants.XML_INDEX_TAG[0]);
            xmlSB.append(++offset);
            xmlSB.append(Constants.XML_INDEX_TAG[1]);
            this.adjustOthersStock(obj);
            BiAccessoryOthers newObj = (BiAccessoryOthers) this.dao.get(BiAccessoryOthers.class.getName(), obj.getId());
            xmlSB.append(this.othersService.toXMLFragment(newObj));
            xmlSB.append(Constants.XML_ITEM_TAG[1]);
        }
        xmlSB.append(Constants.XML_CURRENT_PAGE_TAG[0]);
        xmlSB.append(page);
        xmlSB.append(Constants.XML_CURRENT_PAGE_TAG[1]);
        xmlSB.append(Constants.XML_ITEM_TOTAL_TAG[0]);
        xmlSB.append(this.dao.getOthersCount(filters));
        xmlSB.append(Constants.XML_ITEM_TOTAL_TAG[1]);
        xmlSB.append(Constants.XML_ROOT_TAG[1]);
        return xmlSB.toString();
    }

    private void adjustOthersStock(BiAccessoryOthers bao) throws Exception {
        Set<ProductAccessoryOthers> paos = bao.getProductAccessoryOtherses();
        Integer total = 0;
        Integer bookedTotal = 0;
        for (ProductAccessoryOthers productAccessoryOther : paos) {
            ProductIdentification pi = (ProductIdentification) this.dao.get(ProductIdentification.class.getName(), productAccessoryOther.getProductIdentification().getId());
            BiMeasureSpecification biMS = pi.getBiMeasureSpecification();
            if (biMS == null) continue;
            String[] sizeNames = BiMeasureSpecificationServiceImpl.getSizeName(biMS.getCtStature());
            Set<OrderProduct> orderProducts = pi.getOrderProducts();
            for (OrderProduct orderProduct : orderProducts) {
                Set<OrderProductItem> orderProductItems = orderProduct.getOrderProductItems();
                if ("01".equals(pi.getIsConfirmed())) {
                    total += productAccessoryOther.getUnitNumber() * orderProduct.getTotal(this.getSizeIndex(sizeNames, productAccessoryOther.getForSize()), productAccessoryOther.getVariant());
                } else {
                    bookedTotal += productAccessoryOther.getUnitNumber() * orderProduct.getTotal(this.getSizeIndex(sizeNames, productAccessoryOther.getForSize()), productAccessoryOther.getVariant());
                }
            }
            Set<ProductLaunching> pls = pi.getProductLaunchings();
            for (ProductLaunching productLaunching : pls) {
                if (productAccessoryOther.getVariant().equals(productLaunching.getVariant()) && productAccessoryOther.getForSize().equalsIgnoreCase(productLaunching.getSize()) || productAccessoryOther.getVariant().equals("all") && productAccessoryOther.getForSize().equalsIgnoreCase(productLaunching.getSize()) || productAccessoryOther.getVariant().equalsIgnoreCase(productLaunching.getVariant()) && productAccessoryOther.getForSize().equals("all") || productAccessoryOther.getVariant().equals("all") && productAccessoryOther.getForSize().equals("all")) {
                    if ("01".equals(productLaunching.getIsStock())) {
                        total += (productAccessoryOther.getUnitNumber() * productLaunching.getQuantity1());
                    } else {
                        bookedTotal += (productAccessoryOther.getUnitNumber() * productLaunching.getQuantity1());
                    }
                }
            }
        }
        bao.setBookedStock(bookedTotal);
        bao.setOutStock(total);
        this.dao.saveOrUpdate(bao);
    }

    private Integer getSizeIndex(String[] sizeNames, String forSize) {
        Integer sizeIndex = 0;
        for (int i = 0; i < sizeNames.length; i++) {
            if (sizeNames[i].equals(forSize)) {
                sizeIndex = i + 1;
            }
        }
        return sizeIndex;
    }

    @Override
    public String delete(int id) throws Exception {
        return null;
    }

    @Override
    public String get(int id) throws Exception {
        return null;
    }

    @Override
    public String getAllPagination(int page, String sort, String order) throws Exception {
        return null;
    }
}
