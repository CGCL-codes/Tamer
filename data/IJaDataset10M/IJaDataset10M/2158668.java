package com.hilaver.dzmis.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.hilaver.dzmis.Constants;
import com.hilaver.dzmis.basicinfo.BiAccessoryButton;
import com.hilaver.dzmis.basicinfo.BiAccessoryOthers;
import com.hilaver.dzmis.basicinfo.BiAccessoryZip;
import com.hilaver.dzmis.basicinfo.BiFranceCustomer;
import com.hilaver.dzmis.basicinfo.BiYarn;
import com.hilaver.dzmis.dao.ProcurementDAO;
import com.hilaver.dzmis.procurement.Procurement;
import com.hilaver.dzmis.product.ProductAccessoryButton;
import com.hilaver.dzmis.product.ProductAccessoryOthers;
import com.hilaver.dzmis.product.ProductAccessoryZip;
import com.hilaver.dzmis.product.ProductIdentification;
import com.hilaver.dzmis.product.ProductOrderItem;
import com.hilaver.dzmis.product.ProductOrderItemColor;
import com.hilaver.dzmis.product.ProductYarn;
import com.hilaver.dzmis.service.AbstractBaseService;
import com.hilaver.dzmis.util.SimpleObj2XML;
import com.hilaver.dzmis.util.StringUtils;

public class ProcurementServiceImpl extends AbstractBaseService {

    public static final String COLUMN_PROCUREMENT_ID = "procurement.id";

    public static final String CT_ACCESSORY_TYPE = "10";

    public static final String IS_SPECIAL = "01";

    private ProductIdentificationServiceImpl piService;

    private ProcurementDAO pDAO;

    public ProcurementServiceImpl() {
        this.pDAO = new ProcurementDAO();
    }

    public Procurement getObj(Integer id) throws Exception {
        return (Procurement) this.pDAO.get(Procurement.class.getName(), id);
    }

    @Override
    public String delete(int id) throws Exception {
        return super.delete(Procurement.class.getName(), id);
    }

    public String edit(Procurement procurement) throws Exception {
        if (procurement.getId() == null) {
            procurement.setCreateTime(new Date());
        } else {
            Procurement old = (Procurement) this.dao.get(Procurement.class.getName(), procurement.getId());
            procurement.setCreateTime(old.getCreateTime());
            this.dao.getSession().evict(old);
        }
        this.dao.saveOrUpdate(procurement);
        return "{success: true}";
    }

    @Override
    public String get(int id) throws Exception {
        Procurement obj = (Procurement) this.dao.get(Procurement.class.getName(), id);
        StringBuffer xmlSB = new StringBuffer();
        xmlSB.append(Constants.XML_DOC_DEFINE);
        xmlSB.append(Constants.XML_ROOT_TAG[0]);
        xmlSB.append(Constants.XML_ITEM_TAG[0]);
        xmlSB.append(SimpleObj2XML.toXMLFragment(obj));
        xmlSB.append(Constants.XML_ITEM_TAG[1]);
        xmlSB.append(Constants.XML_ROOT_TAG[1]);
        return xmlSB.toString();
    }

    @Override
    public String getAllPagination(int page, String sort, String order) throws Exception {
        return null;
    }

    public String getAllPagination(int page, String sort, String order, String[] filters) throws Exception {
        int offset = Constants.PAGE_SIZE * (page - 1);
        List objList = this.dao.getAllPagination(Procurement.class.getName(), offset, Constants.PAGE_SIZE, sort, order, filters);
        StringBuffer xmlSB = new StringBuffer();
        xmlSB.append(Constants.XML_DOC_DEFINE);
        xmlSB.append(Constants.XML_ROOT_TAG[0]);
        for (Object obj : objList) {
            xmlSB.append(Constants.XML_ITEM_TAG[0]);
            xmlSB.append(Constants.XML_INDEX_TAG[0]);
            xmlSB.append(++offset);
            xmlSB.append(Constants.XML_INDEX_TAG[1]);
            xmlSB.append(SimpleObj2XML.toXMLFragment(obj));
            xmlSB.append(Constants.XML_ITEM_TAG[1]);
        }
        xmlSB.append(Constants.XML_CURRENT_PAGE_TAG[0]);
        xmlSB.append(page);
        xmlSB.append(Constants.XML_CURRENT_PAGE_TAG[1]);
        xmlSB.append(Constants.XML_ITEM_TOTAL_TAG[0]);
        xmlSB.append(this.dao.getCount(Procurement.class.getName(), filters));
        xmlSB.append(Constants.XML_ITEM_TOTAL_TAG[1]);
        xmlSB.append(Constants.XML_ROOT_TAG[1]);
        return xmlSB.toString();
    }

    public String getAll(Integer piId) throws Exception {
        return null;
    }

    public String editItem(Integer pId, Integer[] ids) throws Exception {
        ProductIdentification pi;
        for (int i = 0; i < ids.length; i++) {
            pi = (ProductIdentification) this.dao.get(ProductIdentification.class.getName(), ids[i]);
            if (pi != null) {
                Procurement p = new Procurement();
                p.setId(pId);
                pi.setProcurement(p);
                this.dao.saveOrUpdate(pi);
            }
        }
        return "{success: true}";
    }

    public String getItemAll(Integer id) throws Exception {
        this.piService = new ProductIdentificationServiceImpl();
        this.piService.setLocale(this.locale);
        String[] filters = new String[] { COLUMN_PROCUREMENT_ID + " = '" + id + "'" };
        List<ProductIdentification> objList = this.dao.getAll(ProductIdentification.class.getName(), COLUMN_ID, Constants.ORDER_DESC, filters);
        StringBuffer xmlSB = new StringBuffer();
        int offset = 0;
        xmlSB.append(Constants.XML_DOC_DEFINE);
        xmlSB.append(Constants.XML_ROOT_TAG[0]);
        for (ProductIdentification pi : objList) {
            xmlSB.append(Constants.XML_ITEM_TAG[0]);
            xmlSB.append(Constants.XML_INDEX_TAG[0]);
            xmlSB.append(++offset);
            xmlSB.append(Constants.XML_INDEX_TAG[1]);
            xmlSB.append("<" + COLUMN_ID + ">");
            xmlSB.append(pi.getId());
            xmlSB.append("</" + COLUMN_ID + ">");
            xmlSB.append(this.piService.toXMLFragment(pi));
            xmlSB.append(Constants.XML_ITEM_TAG[1]);
        }
        xmlSB.append(Constants.XML_CURRENT_PAGE_TAG[0]);
        xmlSB.append(Constants.XML_CURRENT_PAGE_TAG[1]);
        xmlSB.append(Constants.XML_ITEM_TOTAL_TAG[0]);
        xmlSB.append(this.dao.getCount(ProductIdentification.class.getName(), filters));
        xmlSB.append(Constants.XML_ITEM_TOTAL_TAG[1]);
        xmlSB.append(Constants.XML_ROOT_TAG[1]);
        return xmlSB.toString();
    }

    public List<ProductIdentification> getProductIdentificationAll(Integer id) throws Exception {
        String[] filters = new String[] { COLUMN_PROCUREMENT_ID + " = '" + id + "'" };
        List<ProductIdentification> objList = this.dao.getAll(ProductIdentification.class.getName(), COLUMN_ID, Constants.ORDER_DESC, filters);
        return objList;
    }

    public String deleteItem(Integer id) throws Exception {
        ProductIdentification pi = (ProductIdentification) this.dao.get(ProductIdentification.class.getName(), id);
        if (pi != null) {
            pi.setProcurement(null);
            this.dao.saveOrUpdate(pi);
        }
        return "success";
    }

    public List<ProductYarn> getYarn(Integer id) throws Exception {
        List<ProductYarn> yarnList = new ArrayList<ProductYarn>();
        this.piService = new ProductIdentificationServiceImpl();
        this.piService.setLocale(this.locale);
        String[] filters = new String[] { COLUMN_PROCUREMENT_ID + " = '" + id + "'" };
        List<ProductIdentification> piList = this.dao.getAll(ProductIdentification.class.getName(), COLUMN_ID, Constants.ORDER_DESC, filters);
        for (ProductIdentification pi : piList) {
            Set<ProductYarn> pys = pi.getProductYarns();
            for (ProductYarn py : pys) {
                boolean flag = false;
                for (ProductYarn in : yarnList) {
                    if (py.getBiYarn().getReference().equals(in.getBiYarn().getReference())) {
                        flag = true;
                    }
                }
                if (!flag) {
                    yarnList.add(py);
                }
            }
        }
        return yarnList;
    }

    public List<ProductIdentification> getProductIdentificationFromYarn(ProductYarn productYarn) throws Exception {
        Integer[] ids = this.pDAO.getProductIdentificationIdFromYarn(productYarn.getProductIdentification().getProcurement().getId(), productYarn.getBiYarn().getId());
        return this.pDAO.getProductIdentificationsFromIds(ids);
    }

    public String[] getDiffPantoneColorFrom(List<ProductIdentification> piList) throws Exception {
        return this.pDAO.getDiffProductOrderItemColorIds(piList);
    }

    public List<String> getDiffCustomerColor(List<ProductIdentification> piList, ProductYarn py) {
        List<String> customerColorList = new ArrayList<String>();
        for (ProductIdentification pi : piList) {
            Set<ProductYarn> pySet = pi.getProductYarns();
            for (ProductYarn item : pySet) {
            }
        }
        return customerColorList;
    }

    public List<String> getDiffColorFrom(Set<ProductIdentification> piSet) {
        List<String> colorList = new ArrayList();
        for (ProductIdentification pi : piSet) {
            Set<ProductOrderItem> pois = pi.getProductOrderItems();
            for (ProductOrderItem poi : pois) {
                if (!colorList.contains(poi.getColorNumber())) {
                    colorList.add(poi.getColorNumber());
                }
            }
        }
        Collections.sort(colorList, (new Constants()).new StrComparator());
        return colorList;
    }

    public List<ProductAccessoryButton> getDiffProductButton(ProductIdentification pi) {
        List<ProductAccessoryButton> pabList = new ArrayList<ProductAccessoryButton>();
        List<BiAccessoryButton> abList = new ArrayList<BiAccessoryButton>();
        Set<ProductOrderItem> pois = pi.getProductOrderItems();
        for (ProductOrderItem poi : pois) {
            Set<ProductAccessoryButton> pabs = poi.getProductAccessoryButtons();
            for (ProductAccessoryButton pab : pabs) {
                if (!abList.contains(pab.getBiAccessoryButton())) {
                    pabList.add(pab);
                    abList.add(pab.getBiAccessoryButton());
                }
            }
        }
        return pabList;
    }

    public List<BiAccessoryButton> getDiffButton(Set<ProductIdentification> piList) throws Exception {
        List<BiAccessoryButton> abList = new ArrayList<BiAccessoryButton>();
        for (ProductIdentification pi : piList) {
            Set<ProductOrderItem> pois = pi.getProductOrderItems();
            for (ProductOrderItem poi : pois) {
                Set<ProductAccessoryButton> pabs = poi.getProductAccessoryButtons();
                for (ProductAccessoryButton pab : pabs) {
                    if (!abList.contains(pab.getBiAccessoryButton())) {
                        abList.add(pab.getBiAccessoryButton());
                    }
                }
            }
        }
        return abList;
    }

    public List<String> getDiffColor(Set<ProductIdentification> piList, BiAccessoryButton ab) throws Exception {
        List<String> colorList = new ArrayList<String>();
        for (ProductIdentification pi : piList) {
            Set<ProductOrderItem> pois = pi.getProductOrderItems();
            for (ProductOrderItem poi : pois) {
                Set<ProductAccessoryButton> pabs = poi.getProductAccessoryButtons();
                for (ProductAccessoryButton pab : pabs) {
                    if (!colorList.contains(poi.getColorNumber()) && pab.getBiAccessoryButton().equals(ab)) {
                        colorList.add(poi.getColorNumber());
                    }
                }
            }
        }
        Collections.sort(colorList, (new Constants()).new StrComparator());
        return colorList;
    }

    public String getWeightFromPantonColor(ProductIdentification pi, String pantonColor) {
        Float sum = 0f;
        Set<ProductOrderItem> orderItems = pi.getProductOrderItems();
        for (ProductOrderItem poi : orderItems) {
            sum += poi.getTotal();
        }
        Double weight = 0d;
        for (ProductOrderItem poi : orderItems) {
            Set<ProductOrderItemColor> orderItemColors = poi.getProductOrderItemColors();
            for (ProductOrderItemColor poic : orderItemColors) {
                if (poic.getPantoneNumber().equals(pantonColor)) {
                    weight += poic.getPercentage() * sum * pi.getWeight() / 100;
                }
            }
        }
        return StringUtils.toString(weight / 1000, Constants.TOTAL_SCALE);
    }

    public String getWeightFromCustomerColor(ProductIdentification pi, ProductYarn py, String customerColor) {
        Double totalWeight = 0d;
        Set<ProductYarn> pySet = pi.getProductYarns();
        for (ProductYarn item : pySet) {
            if (customerColor.equals(py.getBiYarn().getReference().equals(item.getBiYarn().getReference()))) {
                totalWeight += item.getQuantity();
            }
        }
        return StringUtils.toString(totalWeight, Constants.TOTAL_SCALE);
    }

    public String getPITotalWeight(ProductIdentification pi) {
        Float sum = 0f;
        Set<ProductOrderItem> orderItems = pi.getProductOrderItems();
        for (ProductOrderItem poi : orderItems) {
            sum += poi.getTotal();
        }
        return StringUtils.toString(pi.getWeight() * sum / 1000, Constants.TOTAL_SCALE);
    }

    public String getWeightFromYarn(ProductIdentification pi, ProductYarn py) {
        Double totalWeight = 0d;
        Set<ProductYarn> pySet = pi.getProductYarns();
        for (ProductYarn item : pySet) {
            if (py.getBiYarn().getReference().equals(item.getBiYarn().getReference())) {
                totalWeight += item.getQuantity();
            }
        }
        return StringUtils.toString(totalWeight, Constants.TOTAL_SCALE);
    }

    public String getTotalWeightFromColor(List<ProductIdentification> piList, ProductYarn py, String customerColor) throws Exception {
        Double totalWeight = 0d;
        for (ProductIdentification pi : piList) {
            Set<ProductYarn> pySet = pi.getProductYarns();
            for (ProductYarn item : pySet) {
                if (item.getBiYarn().getReference().equals(py.getBiYarn().getReference())) {
                    totalWeight += item.getQuantity();
                }
            }
        }
        return StringUtils.toString(totalWeight, Constants.TOTAL_SCALE);
    }

    public String getTotalWeightFromYarn(List<ProductIdentification> piList, ProductYarn py) throws Exception {
        Double totalWeight = 0d;
        for (ProductIdentification pi : piList) {
            Set<ProductYarn> pySet = pi.getProductYarns();
            for (ProductYarn item : pySet) {
                if (item.getBiYarn().getReference().equals(py.getBiYarn().getReference())) {
                    totalWeight += item.getQuantity();
                }
            }
        }
        return StringUtils.toString(totalWeight, Constants.TOTAL_SCALE);
    }

    public List<ProductIdentification> getPiListFromYarn(Procurement pro, ProductYarn py) throws Exception {
        List<ProductIdentification> piList = new ArrayList<ProductIdentification>();
        Set<ProductIdentification> piSet = pro.getProductIndentifications();
        for (ProductIdentification pi : piSet) {
            Set<ProductYarn> pySet = pi.getProductYarns();
            for (ProductYarn item : pySet) {
                if (item.getBiYarn().getReference().equals(py.getBiYarn().getReference())) {
                    piList.add(pi);
                }
            }
        }
        return piList;
    }

    public List<ProductIdentification> getProductIdentificationFromButton(Set<ProductIdentification> piSet, BiAccessoryButton ab) {
        List<ProductIdentification> piList = new ArrayList<ProductIdentification>();
        for (ProductIdentification pi : piSet) {
            Set<ProductOrderItem> pois = pi.getProductOrderItems();
            for (ProductOrderItem poi : pois) {
                Set<ProductAccessoryButton> pabs = poi.getProductAccessoryButtons();
                for (ProductAccessoryButton pab : pabs) {
                    if (pab.getBiAccessoryButton().equals(ab) && !piList.contains(pi)) {
                        piList.add(pi);
                    }
                }
            }
        }
        return piList;
    }

    public String getButtonColorFrom(ProductIdentification pi, String color) {
        Set<ProductOrderItem> pois = pi.getProductOrderItems();
        for (ProductOrderItem poi : pois) {
            if (poi.getColorNumber().equals(color)) {
                Set<ProductAccessoryButton> pabs = poi.getProductAccessoryButtons();
                for (ProductAccessoryButton pab : pabs) {
                    return pab.getBiAccessoryButton().getCustomerColor();
                }
            }
        }
        return "";
    }

    public String getButtonQuantityFrom(ProductIdentification pi, String color) {
        Set<ProductOrderItem> pois = pi.getProductOrderItems();
        for (ProductOrderItem poi : pois) {
            if (poi.getColorNumber().equals(color)) {
                Set<ProductAccessoryButton> pabs = poi.getProductAccessoryButtons();
                for (ProductAccessoryButton pab : pabs) {
                    return pab.getTotal().toString();
                }
            }
        }
        return "";
    }

    public List<String> getDiffButtonColor(Set<ProductIdentification> piSet, BiAccessoryButton ab) {
        List<String> colorList = new ArrayList<String>();
        for (ProductIdentification pi : piSet) {
            Set<ProductOrderItem> pois = pi.getProductOrderItems();
            for (ProductOrderItem poi : pois) {
                Set<ProductAccessoryButton> pabs = poi.getProductAccessoryButtons();
                for (ProductAccessoryButton pab : pabs) {
                    if (pab.getBiAccessoryButton().getReference().equals(ab.getReference()) && !colorList.contains(pab.getBiAccessoryButton().getCustomerColor())) {
                        colorList.add(pab.getBiAccessoryButton().getCustomerColor());
                    }
                }
            }
        }
        Collections.sort(colorList, (new Constants()).new StrComparator());
        return colorList;
    }

    public String getButtonTotalQuantity(Set<ProductIdentification> piSet, BiAccessoryButton ab, String color) {
        Integer total = 0;
        for (ProductIdentification pi : piSet) {
            Set<ProductOrderItem> pois = pi.getProductOrderItems();
            for (ProductOrderItem poi : pois) {
                Set<ProductAccessoryButton> pabs = poi.getProductAccessoryButtons();
                for (ProductAccessoryButton pab : pabs) {
                    if (pab.getBiAccessoryButton().getReference().equals(ab.getReference()) && color.equals(pab.getBiAccessoryButton().getCustomerColor())) {
                        total += pab.getTotal();
                    }
                }
            }
        }
        return total == 0 ? "" : Integer.toString(total);
    }

    public List<String> getDiffZipCollarType(Set<ProductIdentification> piSet) {
        List<String> collarTypeList = new ArrayList<String>();
        for (ProductIdentification pi : piSet) {
            Set<ProductAccessoryZip> pazs = pi.getProductAccessoryZips();
            for (ProductAccessoryZip paz : pazs) {
                if (!collarTypeList.contains(paz.getBiAccessoryZip().getCtCollarType())) {
                    collarTypeList.add(paz.getBiAccessoryZip().getCtCollarType());
                }
            }
        }
        return collarTypeList;
    }

    public Map<String, List<ProductAccessoryZip>> getClassifyZipByCollarType(Set<ProductIdentification> piSet) {
        Map<String, List<ProductAccessoryZip>> classifiedMap = new HashMap<String, List<ProductAccessoryZip>>();
        for (ProductIdentification pi : piSet) {
            Set<ProductAccessoryZip> pazs = pi.getProductAccessoryZips();
            for (ProductAccessoryZip paz : pazs) {
                String collarType = paz.getBiAccessoryZip().getCtCollarType();
                if (!classifiedMap.containsKey(collarType)) {
                    classifiedMap.put(collarType, new ArrayList<ProductAccessoryZip>());
                }
                writeFile(realFileDir + paz.getBiAccessoryZip().getBlockPhotoName(), paz.getBiAccessoryZip().getBlockPhoto());
                writeFile(realFileDir + paz.getBiAccessoryZip().getHeadPhotoName(), paz.getBiAccessoryZip().getHeadPhoto());
                classifiedMap.get(collarType).add(paz);
            }
        }
        return classifiedMap;
    }

    public List<ProductAccessoryOthers> getProductCardboardFrom(Set<ProductIdentification> piSet) {
        List<ProductAccessoryOthers> paoList = new ArrayList<ProductAccessoryOthers>();
        for (ProductIdentification pi : piSet) {
            Set<ProductAccessoryOthers> paoSet = pi.getProductAccessoryOtherses();
            for (ProductAccessoryOthers pao : paoSet) {
                if (pao.getBiAccessoryOthers().getCtAccessoryType().equals(CT_ACCESSORY_TYPE)) {
                    paoList.add(pao);
                }
            }
        }
        return paoList;
    }

    public int getCarboardTotal(List<ProductAccessoryOthers> paoList) {
        int total = 0;
        for (ProductAccessoryOthers pao : paoList) {
            total += pao.getTotal();
        }
        return total;
    }

    public List<ProductAccessoryButton> getProductAccessoryButtonIsSpecial(Set<ProductIdentification> piSet) {
        List<ProductAccessoryButton> pabList = new ArrayList<ProductAccessoryButton>();
        List<BiAccessoryButton> abList = new ArrayList<BiAccessoryButton>();
        for (ProductIdentification pi : piSet) {
            Set<ProductOrderItem> pois = pi.getProductOrderItems();
            for (ProductOrderItem poi : pois) {
                Set<ProductAccessoryButton> pabs = poi.getProductAccessoryButtons();
                for (ProductAccessoryButton pab : pabs) {
                    if (pab.getBiAccessoryButton().getIsSpecial().equals(IS_SPECIAL) && !abList.contains(pab.getBiAccessoryButton())) {
                        writeFile(realFileDir + pab.getBiAccessoryButton().getPhotoName(), pab.getBiAccessoryButton().getPhoto());
                        pabList.add(pab);
                        abList.add(pab.getBiAccessoryButton());
                    }
                }
            }
        }
        return pabList;
    }

    public List<ProductAccessoryButton> getProductAccessoryButtonIsNotSpecial(Set<ProductIdentification> piSet) {
        List<ProductAccessoryButton> pabList = new ArrayList<ProductAccessoryButton>();
        List<BiAccessoryButton> abList = new ArrayList<BiAccessoryButton>();
        for (ProductIdentification pi : piSet) {
            Set<ProductOrderItem> pois = pi.getProductOrderItems();
            for (ProductOrderItem poi : pois) {
                Set<ProductAccessoryButton> pabs = poi.getProductAccessoryButtons();
                for (ProductAccessoryButton pab : pabs) {
                    if (!pab.getBiAccessoryButton().getIsSpecial().equals(IS_SPECIAL) && !abList.contains(pab.getBiAccessoryButton())) {
                        writeFile(realFileDir + pab.getBiAccessoryButton().getPhotoName(), pab.getBiAccessoryButton().getPhoto());
                        pabList.add(pab);
                        abList.add(pab.getBiAccessoryButton());
                    }
                }
            }
        }
        return pabList;
    }

    public List<ProductAccessoryZip> getProductAccessoryZipIsSpecial(Set<ProductIdentification> piSet) {
        List<ProductAccessoryZip> pazList = new ArrayList<ProductAccessoryZip>();
        List<BiAccessoryZip> azList = new ArrayList<BiAccessoryZip>();
        for (ProductIdentification pi : piSet) {
            Set<ProductAccessoryZip> pazSet = pi.getProductAccessoryZips();
            for (ProductAccessoryZip paz : pazSet) {
                if (paz.getBiAccessoryZip().getIsSpecial().equals(IS_SPECIAL) && !azList.contains(paz.getBiAccessoryZip())) {
                    writeFile(realFileDir + paz.getBiAccessoryZip().getBlockPhotoName(), paz.getBiAccessoryZip().getBlockPhoto());
                    writeFile(realFileDir + paz.getBiAccessoryZip().getHeadPhotoName(), paz.getBiAccessoryZip().getHeadPhoto());
                    pazList.add(paz);
                    azList.add(paz.getBiAccessoryZip());
                }
            }
        }
        return pazList;
    }

    public List<ProductAccessoryZip> getProductAccessoryZipIsNotSpecial(Set<ProductIdentification> piSet) {
        List<ProductAccessoryZip> pazList = new ArrayList<ProductAccessoryZip>();
        List<BiAccessoryZip> azList = new ArrayList<BiAccessoryZip>();
        for (ProductIdentification pi : piSet) {
            Set<ProductAccessoryZip> pazSet = pi.getProductAccessoryZips();
            for (ProductAccessoryZip paz : pazSet) {
                if (!paz.getBiAccessoryZip().getIsSpecial().equals(IS_SPECIAL) && !azList.contains(paz.getBiAccessoryZip())) {
                    writeFile(realFileDir + paz.getBiAccessoryZip().getBlockPhotoName(), paz.getBiAccessoryZip().getBlockPhoto());
                    writeFile(realFileDir + paz.getBiAccessoryZip().getHeadPhotoName(), paz.getBiAccessoryZip().getHeadPhoto());
                    pazList.add(paz);
                    azList.add(paz.getBiAccessoryZip());
                }
            }
        }
        return pazList;
    }

    public List<ProductAccessoryOthers> getProductAccessoryOthersIsSpecial(Set<ProductIdentification> piSet) {
        List<ProductAccessoryOthers> paoList = new ArrayList<ProductAccessoryOthers>();
        List<BiAccessoryOthers> aoList = new ArrayList<BiAccessoryOthers>();
        for (ProductIdentification pi : piSet) {
            Set<ProductAccessoryOthers> paoSet = pi.getProductAccessoryOtherses();
            for (ProductAccessoryOthers pao : paoSet) {
                if (!pao.getBiAccessoryOthers().getCtAccessoryType().equals(CT_ACCESSORY_TYPE) && pao.getBiAccessoryOthers().getIsSpecial().equals(IS_SPECIAL) && !aoList.contains(pao.getBiAccessoryOthers())) {
                    writeFile(realFileDir + pao.getBiAccessoryOthers().getBindPhotoName(), pao.getBiAccessoryOthers().getBindPhoto());
                    writeFile(realFileDir + pao.getBiAccessoryOthers().getPhotoName(), pao.getBiAccessoryOthers().getPhoto());
                    paoList.add(pao);
                    aoList.add(pao.getBiAccessoryOthers());
                }
            }
        }
        return paoList;
    }

    public List<ProductAccessoryOthers> getProductAccessoryOthersIsNotSpecial(Set<ProductIdentification> piSet) {
        List<ProductAccessoryOthers> paoList = new ArrayList<ProductAccessoryOthers>();
        List<BiAccessoryOthers> aoList = new ArrayList<BiAccessoryOthers>();
        for (ProductIdentification pi : piSet) {
            Set<ProductAccessoryOthers> paoSet = pi.getProductAccessoryOtherses();
            for (ProductAccessoryOthers pao : paoSet) {
                if (!pao.getBiAccessoryOthers().getCtAccessoryType().equals(CT_ACCESSORY_TYPE) && !pao.getBiAccessoryOthers().getIsSpecial().equals(IS_SPECIAL) && !aoList.contains(pao.getBiAccessoryOthers())) {
                    writeFile(realFileDir + pao.getBiAccessoryOthers().getBindPhotoName(), pao.getBiAccessoryOthers().getBindPhoto());
                    writeFile(realFileDir + pao.getBiAccessoryOthers().getPhotoName(), pao.getBiAccessoryOthers().getPhoto());
                    paoList.add(pao);
                    aoList.add(pao.getBiAccessoryOthers());
                }
            }
        }
        return paoList;
    }

    public List<BiFranceCustomer> getDiffFranceCustomerFrom(Set<ProductIdentification> piSet) {
        List<BiFranceCustomer> fcList = new ArrayList<BiFranceCustomer>();
        for (ProductIdentification pi : piSet) {
            if (!fcList.contains(pi.getBiFranceCustomer())) {
                fcList.add(pi.getBiFranceCustomer());
            }
        }
        return fcList;
    }

    public String getTotalWeightFrom(Set<ProductIdentification> piSet, BiFranceCustomer franceCustomer, ProductYarn productYarn) {
        Double total = 0d;
        for (ProductIdentification pi : piSet) {
            if (pi.getBiFranceCustomer().equals(franceCustomer)) {
                Set<ProductYarn> pySet = pi.getProductYarns();
                for (ProductYarn py : pySet) {
                    if (py.getBiYarn().getReference().equals(productYarn.getBiYarn().getReference())) {
                        total += py.getQuantity();
                    }
                }
            }
        }
        return StringUtils.toString(total, Constants.TOTAL_SCALE);
    }

    public String getTotalWeightFrom(Set<ProductIdentification> piSet, ProductYarn productYarn) {
        Double total = 0d;
        for (ProductIdentification pi : piSet) {
            Set<ProductYarn> pySet = pi.getProductYarns();
            for (ProductYarn py : pySet) {
                if (py.getBiYarn().getReference().equals(productYarn.getBiYarn().getReference())) {
                    total += py.getQuantity();
                }
            }
        }
        return StringUtils.toString(total, Constants.TOTAL_SCALE);
    }

    public Float getTotalWeightFromColor(Set<ProductIdentification> piSet, ProductYarn productYarn) {
        Float total = 0f;
        for (ProductIdentification pi : piSet) {
            Set<ProductYarn> pySet = pi.getProductYarns();
            for (ProductYarn py : pySet) {
                if (py.getBiYarn().getReference().equals(productYarn.getBiYarn().getReference())) {
                    total += py.getQuantity();
                }
            }
        }
        return total;
    }

    public String getTotalWeightFrom(Set<ProductIdentification> piSet, BiFranceCustomer franceCustomer) {
        Double total = 0d;
        for (ProductIdentification pi : piSet) {
            boolean flag = false;
            Double subTotal = 0d;
            if (pi.getBiFranceCustomer().equals(franceCustomer)) {
                flag = true;
            }
            if (flag) {
                Set<ProductYarn> pySet = pi.getProductYarns();
                for (ProductYarn py : pySet) {
                    total += py.getQuantity();
                }
            }
            total += (pi.getWeight() * subTotal);
        }
        return StringUtils.toString(total, Constants.TOTAL_SCALE);
    }

    public String getTotalWeightFrom(Set<ProductIdentification> piSet) {
        Double total = 0d;
        for (ProductIdentification pi : piSet) {
            Set<ProductYarn> pySet = pi.getProductYarns();
            for (ProductYarn py : pySet) {
                total += py.getQuantity();
            }
        }
        return StringUtils.toString(total, Constants.TOTAL_SCALE);
    }

    public List<ProductIdentification> sortByCustomer(Set<ProductIdentification> piSet) {
        List<ProductIdentification> piList = new ArrayList<ProductIdentification>();
        for (ProductIdentification pi : piSet) {
            writeFile(realFileDir + pi.getPhotoName(), pi.getPhoto());
            System.out.println(pi.getProductOrderItems().size());
            piList.add(pi);
        }
        Collections.sort(piList, new piComparator());
        return piList;
    }

    private class piComparator implements Comparator {

        public int compare(Object arg0, Object arg1) {
            ProductIdentification pi1 = (ProductIdentification) arg0;
            ProductIdentification pi2 = (ProductIdentification) arg1;
            return pi1.getBiFranceCustomer().getName().compareTo(pi2.getBiFranceCustomer().getName());
        }
    }

    public static void main(String[] args) throws Exception {
        ProcurementServiceImpl os = new ProcurementServiceImpl();
        List<ProductYarn> pyList = os.getYarn(2);
        for (ProductYarn py : pyList) {
            List<ProductIdentification> piList = os.getProductIdentificationFromYarn(py);
            os.getDiffPantoneColorFrom(piList);
        }
    }
}
