package com.hilaver.dzmis.service.impl;

import java.util.List;
import com.hilaver.dzmis.Constants;
import com.hilaver.dzmis.basicinfo.BiArmor;
import com.hilaver.dzmis.codetable.CtArmorSet;
import com.hilaver.dzmis.codetable.CtArmorType;
import com.hilaver.dzmis.codetable.CtArmor1Type;
import com.hilaver.dzmis.dao.BiArmorDAO;
import com.hilaver.dzmis.service.AbstractBaseService;
import com.hilaver.dzmis.service.IEntityService;
import com.hilaver.dzmis.util.SimpleObj2XML;
import com.hilaver.dzmis.util.StringUtils;

public class BiArmorServiceImpl extends AbstractBaseService implements IEntityService {

    private CtServiceImpl ctService;

    private BiArmorDAO dao;

    public BiArmorServiceImpl() {
        this.dao = new BiArmorDAO();
        this.ctService = new CtServiceImpl();
    }

    @Override
    public String delete(int id) throws Exception {
        super.editSysMbox(BiArmor.class.getName(), id);
        return super.delete(BiArmor.class.getName(), id);
    }

    public String edit(BiArmor biArmor) throws Exception {
        String operation = null;
        if (biArmor.getId() != null) {
            BiArmor old = (BiArmor) this.dao.get(BiArmor.class.getName(), biArmor.getId());
            if (biArmor.getPhoto1Name() == null) {
                biArmor.setPhoto1(old.getPhoto1());
                biArmor.setPhoto1Name(old.getPhoto1Name());
            }
            if (biArmor.getPhoto2Name() == null) {
                biArmor.setPhoto2(old.getPhoto2());
                biArmor.setPhoto2Name(old.getPhoto2Name());
            }
            if (biArmor.getPhoto3Name() == null) {
                biArmor.setPhoto3(old.getPhoto3());
                biArmor.setPhoto3Name(old.getPhoto3Name());
            }
            if (biArmor.getPhoto4Name() == null) {
                biArmor.setPhoto4(old.getPhoto4());
                biArmor.setPhoto4Name(old.getPhoto4Name());
            }
            this.dao.getSession().evict(old);
            operation = "02";
        } else {
            operation = "01";
        }
        this.dao.saveOrUpdate(biArmor);
        super.editSysMbox(biArmor, operation);
        return "{success: true}";
    }

    @Override
    public String get(int id) throws Exception {
        BiArmor biArmor = (BiArmor) this.dao.get(BiArmor.class.getName(), id);
        writeFile(realFileDir + biArmor.getPhoto1Name(), biArmor.getPhoto1());
        writeFile(realFileDir + biArmor.getPhoto2Name(), biArmor.getPhoto2());
        writeFile(realFileDir + biArmor.getPhoto3Name(), biArmor.getPhoto3());
        writeFile(realFileDir + biArmor.getPhoto4Name(), biArmor.getPhoto4());
        StringBuffer xmlSB = new StringBuffer();
        xmlSB.append(Constants.XML_DOC_DEFINE);
        xmlSB.append(Constants.XML_ROOT_TAG[0]);
        xmlSB.append(Constants.XML_ITEM_TAG[0]);
        xmlSB.append("<photo1Name>");
        xmlSB.append(Constants.XML_CDATA[0]);
        if (biArmor.getPhoto1Name() == null) {
            xmlSB.append(NO_IMAGE_NAME);
        } else {
            xmlSB.append(biArmor.getPhoto1Name());
        }
        xmlSB.append(Constants.XML_CDATA[1]);
        xmlSB.append("</photo1Name>");
        xmlSB.append("<photo2Name>");
        xmlSB.append(Constants.XML_CDATA[0]);
        if (biArmor.getPhoto2Name() == null) {
            xmlSB.append(NO_IMAGE_NAME);
        } else {
            xmlSB.append(biArmor.getPhoto2Name());
        }
        xmlSB.append(Constants.XML_CDATA[1]);
        xmlSB.append("</photo2Name>");
        xmlSB.append("<photo3Name>");
        xmlSB.append(Constants.XML_CDATA[0]);
        if (biArmor.getPhoto3Name() == null) {
            xmlSB.append(NO_IMAGE_NAME);
        } else {
            xmlSB.append(biArmor.getPhoto3Name());
        }
        xmlSB.append(Constants.XML_CDATA[1]);
        xmlSB.append("</photo3Name>");
        xmlSB.append("<photo4Name>");
        xmlSB.append(Constants.XML_CDATA[0]);
        if (biArmor.getPhoto4Name() == null) {
            xmlSB.append(NO_IMAGE_NAME);
        } else {
            xmlSB.append(biArmor.getPhoto4Name());
        }
        xmlSB.append(Constants.XML_CDATA[1]);
        xmlSB.append("</photo4Name>");
        xmlSB.append(SimpleObj2XML.toXMLFragment(biArmor));
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
        List<BiArmor> objList = this.dao.getAllPagination(BiArmor.class.getName(), offset, Constants.PAGE_SIZE, sort, order, filters);
        StringBuffer xmlSB = new StringBuffer();
        xmlSB.append(Constants.XML_DOC_DEFINE);
        xmlSB.append(Constants.XML_ROOT_TAG[0]);
        for (BiArmor obj : objList) {
            writeFile(realFileDir + obj.getPhoto1Name(), obj.getPhoto1());
            writeFile(realFileDir + obj.getPhoto2Name(), obj.getPhoto2());
            writeFile(realFileDir + obj.getPhoto3Name(), obj.getPhoto3());
            writeFile(realFileDir + obj.getPhoto4Name(), obj.getPhoto4());
            xmlSB.append(Constants.XML_ITEM_TAG[0]);
            xmlSB.append(Constants.XML_INDEX_TAG[0]);
            xmlSB.append(++offset);
            xmlSB.append(Constants.XML_INDEX_TAG[1]);
            xmlSB.append("<ctArmorType>");
            xmlSB.append(Constants.XML_CDATA[0]);
            xmlSB.append(this.ctService.getDescription(CtArmorType.class.getName(), obj.getCtArmorType(), locale));
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</ctArmorType>");
            xmlSB.append("<ctArmorSet>");
            xmlSB.append(Constants.XML_CDATA[0]);
            xmlSB.append(this.ctService.getDescription(CtArmorSet.class.getName(), obj.getCtArmorSet(), locale));
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</ctArmorSet>");
            xmlSB.append("<ctArmor1Type>");
            xmlSB.append(Constants.XML_CDATA[0]);
            xmlSB.append(this.ctService.getDescription(CtArmor1Type.class.getName(), obj.getCtArmor1Type(), locale));
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</ctArmor1Type>");
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
            if (obj.getPhoto3Name() == null) {
                xmlSB.append(NO_IMAGE_NAME);
            } else {
                xmlSB.append(obj.getPhoto3Name());
            }
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</photo3Name>");
            xmlSB.append("<photo4Name>");
            xmlSB.append(Constants.XML_CDATA[0]);
            if (obj.getPhoto4Name() == null) {
                xmlSB.append(NO_IMAGE_NAME);
            } else {
                xmlSB.append(obj.getPhoto4Name());
            }
            xmlSB.append(Constants.XML_CDATA[1]);
            xmlSB.append("</photo4Name>");
            xmlSB.append(SimpleObj2XML.toXMLFragment(obj));
            xmlSB.append(Constants.XML_ITEM_TAG[1]);
        }
        xmlSB.append(Constants.XML_CURRENT_PAGE_TAG[0]);
        xmlSB.append(page);
        xmlSB.append(Constants.XML_CURRENT_PAGE_TAG[1]);
        xmlSB.append(Constants.XML_ITEM_TOTAL_TAG[0]);
        xmlSB.append(this.dao.getCount(BiArmor.class.getName(), filters));
        xmlSB.append(Constants.XML_ITEM_TOTAL_TAG[1]);
        xmlSB.append(Constants.XML_ROOT_TAG[1]);
        return xmlSB.toString();
    }

    public String getMaxReferenceIndex(String ctArmorType, String ctJauge) throws Exception {
        List<Integer> referenceIndexList = this.dao.getMaxReferenceIndex(ctArmorType, ctJauge);
        StringBuffer xmlSB = new StringBuffer();
        xmlSB.append(Constants.XML_DOC_DEFINE);
        xmlSB.append(Constants.XML_ROOT_TAG[0]);
        xmlSB.append(Constants.XML_ITEM_TAG[0]);
        xmlSB.append(Constants.XML_CDATA[0]);
        if (referenceIndexList.size() == 0 || referenceIndexList.get(0) == null) {
            xmlSB.append("0001");
        } else {
            Integer index = referenceIndexList.get(0);
            xmlSB.append(StringUtils.to4Digital(++index));
        }
        xmlSB.append(Constants.XML_CDATA[1]);
        xmlSB.append(Constants.XML_ITEM_TAG[1]);
        xmlSB.append(Constants.XML_ROOT_TAG[1]);
        return xmlSB.toString();
    }
}
