package com.fisoft.phucsinh.phucsinhsrv.service.common;

import com.fisoft.phucsinh.phucsinhsrv.eao.IParamListItemEAO;
import com.fisoft.phucsinh.phucsinhsrv.entity.ParamlistitemEntity;
import com.fisoft.phucsinh.phucsinhsrv.exception.ERPException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.faces.model.SelectItem;

/**
 *
 * @author MOONLIGHT
 */
@Stateless
public class CommonManager implements ICommonManager {

    @EJB
    private ICRUDManager crudManager;

    @EJB
    private IParamListItemEAO paramEAO;

    private List<ParamlistitemEntity> EMparamdetails = null;

    private ParamlistitemEntity eParam;

    private SelectItem[] item4dropdown;

    public SelectItem[] getItem4dropdown() {
        return item4dropdown;
    }

    public void setItem4dropdown(SelectItem[] item4dropdown) {
        this.item4dropdown = item4dropdown;
    }

    public CommonManager() {
        eParam = new ParamlistitemEntity();
    }

    /**
     * list of param with key is paramlist code
     * now just use for drp Age of Contact module
     * @param param String type ParamList
     * @return String[] type
     */
    public String[] DropdownLoad(String param) {
        HashMap[] hList;
        HashMap hListItem;
        String[] oList;
        hList = getParam(param);
        if (hList != null) {
            oList = new String[hList.length + 1];
            for (int i = 0; i < hList.length; i++) {
                hListItem = hList[i];
                oList[i + 1] = new String(hListItem.get("Code").toString());
            }
            oList[0] = new String(" ");
        } else {
            oList = new String[1];
            oList[0] = new String(" ");
        }
        if (param.equals("Age")) {
            int ageLen = oList.length;
            if (ageLen > 1) {
                int len1, len2, age1, age2 = 0;
                for (len1 = 1; len1 < ageLen - 1; len1++) {
                    for (len2 = len1 + 1; len2 < ageLen; len2++) {
                        age1 = Integer.parseInt(oList[len1]);
                        age2 = Integer.parseInt(oList[len2]);
                        if (age1 <= age2) {
                            oList[len1] = age1 + "";
                            oList[len2] = age2 + "";
                        } else {
                            oList[len1] = age2 + "";
                            oList[len2] = age1 + "";
                        }
                    }
                }
                for (len1 = 1; len1 < ageLen - 1; len1++) {
                    oList[len1] = oList[len1] + " - " + oList[len1 + 1];
                }
                oList[0] = "";
                oList[ageLen - 1] = "Over " + oList[ageLen - 1];
            }
        }
        return oList;
    }

    public HashMap[] getParam(String pParamID) {
        HashMap[] hResults = null;
        HashMap hItem;
        ParamlistitemEntity EMparamdetail = new ParamlistitemEntity();
        try {
            EMparamdetails = paramEAO.searchParam(pParamID);
            int iSize = EMparamdetails.size();
            if (iSize == 0) {
                return null;
            }
            hResults = new HashMap[iSize];
            for (int i = 0; i < iSize; i++) {
                EMparamdetail = EMparamdetails.get(i);
                hItem = new HashMap();
                hItem.put("ParamID", EMparamdetail.getParamListID().toString());
                hItem.put("Code", EMparamdetail.getName().toString());
                hResults[i] = hItem;
            }
        } catch (Exception e) {
        }
        return hResults;
    }

    /**
     * fill dropdown field have paramID is pParam have empty
     * fill by SelectItem[]
     * @author Vinh
     * @param pParam
     * @return SelectItem[] type
     */
    public SelectItem[] seParamItem(String pParam) {
        List<ParamlistitemEntity> lParam = new ArrayList<ParamlistitemEntity>();
        try {
            lParam = paramEAO.searchParam(pParam);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return convertListParamEntity2SelectItem_hasEmpty(lParam);
    }

    /**
     * 
     * @param pParam
     * @return
     */
    public SelectItem[] items_paramID_description(String pParam) {
        List<ParamlistitemEntity> lParam = new ArrayList<ParamlistitemEntity>();
        try {
            lParam = paramEAO.searchParam(pParam);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return convert_paramid_desc_2Items_hasEmpty(lParam);
    }

    /**
     * fill dropdown field have paramID is pParam (not empty record)
     * fill by SelectItem[]
     * @author Vinh
     * @param pParam
     * @return SelectItem[] type
     */
    public SelectItem[] seParamNotEmpty(String pParam) {
        List<ParamlistitemEntity> lParam = new ArrayList<ParamlistitemEntity>();
        try {
            lParam = paramEAO.searchParam(pParam);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return convertListParamEntity2SelectItem_notEmpty(lParam);
    }

    /**
     * fill HashMap have key is rule for loading, value is SelectItem[]
     * @author Vinh
     * @param pParam String type
     * @param rule4Load String type
     * @return hParamResult HashMap type with value is SelectItem[] type
     */
    public HashMap listParamHaveRule(String pParam, String rule4Load) {
        HashMap hParamResult = new HashMap();
        List<ParamlistitemEntity> lParamRule = new ArrayList<ParamlistitemEntity>();
        try {
            lParamRule = paramEAO.searchParam(rule4Load);
            SelectItem[] selectParamItem = convertListParamEntity2SelectItem_notEmpty(lParamRule);
            String sRule;
            for (SelectItem eRule : selectParamItem) {
                sRule = eRule.getValue().toString();
                List<ParamlistitemEntity> listParam = new ArrayList<ParamlistitemEntity>();
                try {
                    listParam = paramEAO.searchParamInRule(pParam, sRule);
                    if (listParam.size() > 0) {
                        SelectItem[] seParamItem = convertListParamEntity2SelectItem_hasEmpty(listParam);
                        hParamResult.put(sRule, seParamItem);
                    }
                } catch (ERPException ex) {
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return hParamResult;
    }

    /**
     * convert ParamlistitemEntity List to SelectItem[] (not have empty record)
     * @author Vinh
     * @param ParamlistitemEntity List type
     * @return SelectItem[] type
     */
    private SelectItem[] convertListParamEntity2SelectItem_notEmpty(List<ParamlistitemEntity> listParam) {
        int iItem = 0;
        SelectItem[] selectParamItem = new SelectItem[listParam.size()];
        for (ParamlistitemEntity eParamEntity : listParam) {
            selectParamItem[iItem] = new SelectItem(eParamEntity.getID(), eParamEntity.getName());
            iItem++;
        }
        return selectParamItem;
    }

    /**
     * 
     * @param listParam
     * @return
     */
    private SelectItem[] convert_paramid_desc_2Items_hasEmpty(List<ParamlistitemEntity> listParam) {
        int iItem = 1;
        SelectItem[] selectParamItem = new SelectItem[listParam.size() + 1];
        selectParamItem[0] = new SelectItem(0, " ");
        for (ParamlistitemEntity eParamEntity : listParam) {
            selectParamItem[iItem] = new SelectItem(eParamEntity.getID(), eParamEntity.getValue());
            iItem++;
        }
        return selectParamItem;
    }

    /**
     * convert ParamlistitemEntity List to SelectItem[] (has empty record)
     * @author Vinh
     * @param ParamlistitemEntity List type
     * @return SelectItem[] type
     */
    private SelectItem[] convertListParamEntity2SelectItem_hasEmpty(List<ParamlistitemEntity> listParam) {
        int iItem = 1;
        SelectItem[] selectParamItem = new SelectItem[listParam.size() + 1];
        selectParamItem[0] = new SelectItem(0, " ");
        for (ParamlistitemEntity eParamEntity : listParam) {
            selectParamItem[iItem] = new SelectItem(eParamEntity.getID(), eParamEntity.getName());
            iItem++;
        }
        return selectParamItem;
    }

    /**
     * convert List Integer to SelectItem[]
     * @param intList
     * @return
     */
    public SelectItem[] convertListInteger2ListParamItem(List<Integer> intList) {
        List<ParamlistitemEntity> listParam = new ArrayList<ParamlistitemEntity>();
        for (Integer key : intList) {
            ParamlistitemEntity param = new ParamlistitemEntity();
            try {
                param = paramEAO.find(key);
                if (param != null) {
                    listParam.add(param);
                }
            } catch (Exception ex) {
            }
        }
        return convertListParamEntity2SelectItem_hasEmpty(listParam);
    }

    /**
     * find ParamlistitemEntity with param ID
     * @author Vinh
     * @param id int type
     * @return ParamlistitemEntity type
     */
    public ParamlistitemEntity findParamEntity(int id) {
        eParam = paramEAO.find(id);
        return eParam;
    }

    public String[] LoadHour() {
        String[] oList;
        oList = new String[13];
        String a = new String();
        for (int i = 0; i < 12; i++) {
            a = Integer.toString(i + 1);
            oList[i + 1] = a;
        }
        oList[0] = new String(" ");
        return oList;
    }

    public String[] LoadMinute() {
        String[] oList;
        oList = new String[5];
        oList[0] = new String(" ");
        oList[1] = new String("00");
        oList[2] = new String("15");
        oList[3] = new String("30");
        oList[4] = new String("45");
        return oList;
    }

    public String[] LoadAPM() {
        String[] oList;
        oList = new String[3];
        oList[0] = new String(" ");
        oList[1] = new String("AM");
        oList[2] = new String("PM");
        return oList;
    }
}
