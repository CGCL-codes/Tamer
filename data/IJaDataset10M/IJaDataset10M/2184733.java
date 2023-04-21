package com.wwg.market.ui.dashboard.client.utils;

import com.google.gwt.user.client.ui.ListBox;
import com.mobileares.midp.widgets.client.utils.CodeModel;
import com.mobileares.midp.widgets.client.utils.ModelCallback;
import com.nexustar.gwt.widgets.client.asyn.FishAsyncCallback;
import com.wwg.market.ui.dashboard.client.Utils;
import com.wwg.market.ui.service.dto.GeneralCodeDto;
import com.wwg.market.ui.service.interfaces.CodeService;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Tom
 * Date: 2011-12-19
 * Time: 6:18:55
 * To change this template use File | Settings | File Templates.
 */
public class CacheCode {

    private static Map<String, List> cache = new HashMap();

    public static void initAppCodes() {
        UXMLCacheFactory.getInstance().load("codes/category-caches.xml");
    }

    public static void getCodesById(final String id, final ModelCallback callback) {
        if (cache.get(id) == null) Utils.invoke(CodeService.class.getName(), "getCodesByType", new Object[] { id }, new FishAsyncCallback() {

            public void onSuccess(Object result) {
                cache.put(id, convert((List) result));
                if (callback != null) callback.setModelElments(convert((List<GeneralCodeDto>) result));
            }
        }); else if (callback != null) callback.setModelElments(cache.get(id));
    }

    public static void cacheCodesById(final String id) {
        if (cache.get(id) == null) Utils.invoke(CodeService.class.getName(), "getCodesByType", new Object[] { id }, new FishAsyncCallback() {

            public void onSuccess(Object result) {
                cache.put(id, convert((List<GeneralCodeDto>) result));
            }
        });
    }

    private static List getCodesByXMLCache(String id) {
        if (cache.get(id) == null) {
            List<Map> maps = UXMLCacheFactory.getInstance().getGlobalCaches(id);
            if (maps == null) return null; else {
                List result = new ArrayList();
                for (Map map : maps) {
                    CodeModel dto = new CodeModel((String) map.get("name"), (String) map.get("value"));
                    result.add(dto);
                }
                cache.put(id, result);
                return result;
            }
        } else return cache.get(id);
    }

    public static void cacheCodesById(String id, List<CodeModel> codes) {
        cache.put(id, codes);
    }

    public static List getCodesById(String id) {
        return getCodesByXMLCache(id);
    }

    public static String getNameByCode(List codes, String code) {
        for (Iterator it = codes.iterator(); it.hasNext(); ) {
            CodeModel temp = (CodeModel) it.next();
            if (temp.getCode().equals(code)) return temp.getText();
        }
        return code;
    }

    public static void initGeneralCodesToListWidget(List<GeneralCodeDto> codes, ListBox listWidget, boolean isNull) {
        listWidget.clear();
        if (isNull) listWidget.addItem("-请选择-", "");
        if (codes != null && codes.size() > 0) {
            for (int i = 0; i < codes.size(); i++) {
                GeneralCodeDto category = codes.get(i);
                listWidget.addItem(category.getName(), category.getCode().toString());
            }
        }
    }

    private static List<CodeModel> convert(List<GeneralCodeDto> src) {
        List<CodeModel> des = new ArrayList<CodeModel>();
        for (GeneralCodeDto dto : src) {
            CodeModel model = new CodeModel(dto.getName(), dto.getCode());
            des.add(model);
        }
        return des;
    }
}
