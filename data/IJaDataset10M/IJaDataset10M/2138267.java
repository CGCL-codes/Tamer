package com.exedosoft.plat.ui.jquery.grid;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.exedosoft.plat.bo.BOInstance;
import com.exedosoft.plat.bo.DOService;
import com.exedosoft.plat.ui.DOFormModel;
import com.exedosoft.plat.ui.DOGridModel;
import com.exedosoft.plat.ui.DOIModel;
import com.exedosoft.plat.ui.DOPaneModel;
import com.exedosoft.plat.ui.DOViewTemplate;
import com.exedosoft.plat.util.DOGlobals;
import com.exedosoft.plat.util.StringUtil;

/**
 * @author lenovo
 *  
 *  �������Ϊ��ӡ��˵Ŀؼ����ָ�ΪCMS����ͼƬչʾ�ؼ�
 *  չʾԭ�?
 *  ����exedo/webv3/template/cms/themeĿ¼�µ��ļ��У�û���ļ���Ϊһ������
 *
 */
public class GridListImage extends DOViewTemplate {

    private static Log log = LogFactory.getLog(GridListImage.class);

    public GridListImage() {
        this.templateFile = "grid/GridListImage.ftl";
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> putData(DOIModel doimodel) {
        int pageNo = 1;
        int pageNum = 0;
        List ztl = getThemesDir();
        Map<String, Object> data = new HashMap<String, Object>();
        DOGridModel gm = (DOGridModel) doimodel;
        if (gm.getRowSize() != null) {
            pageNum = gm.getRowSize().intValue();
        }
        data.put("rowSize", pageNum);
        int resultSize = ztl.size();
        int pageSize = StringUtil.getPageSize(resultSize, pageNum);
        data.put("pageSize", pageSize);
        data.put("resultSize", resultSize);
        data.put("pageNo", pageNo);
        data.put("cms", ztl);
        data.put("model", gm);
        data.put("contextPath", DOGlobals.PRE_FULL_FOLDER);
        if (gm.getContainerPane() != null) {
            data.put("pmlName", gm.getContainerPane().getName());
        }
        data.put("formName", "a" + gm.getObjUid());
        int datarowSize;
        String rowTmp = DOGlobals.getInstance().getSessoinContext().getFormInstance().getValue("rowSize");
        if (rowTmp != null && !"".equals(rowTmp)) {
            datarowSize = Integer.parseInt(rowTmp);
        } else {
            datarowSize = 3;
        }
        List list = gm.getAllGridFormLinks();
        DOFormModel fm;
        for (int i = 0; i < list.size(); i++) {
            fm = (DOFormModel) list.get(i);
            if (fm.getL10n().equals("startusing")) {
                data.put("fm", fm);
                break;
            } else {
                data.put("fm", "");
            }
        }
        data.put("datarowSize", datarowSize);
        return data;
    }

    @SuppressWarnings("unchecked")
    public List getThemesDir() {
        String path = this.getClass().getResource("/").getPath();
        File current_dir = new File(path);
        File root_dir = new File(new File(current_dir.getParent()).getParent() + "/exedo/webv3/template/cms/theme/");
        File[] listDirs = root_dir.listFiles();
        DOService service = DOService.getService("cms_options_list");
        BOInstance bo = new BOInstance();
        List<BOInstance> l = service.invokeSelect(bo);
        String current_theme = "";
        if (!l.isEmpty()) {
            for (BOInstance b : l) {
                System.out.println(b.getValue("opt_key"));
                if (b.getValue("opt_key").equals("themes_dir")) {
                    current_theme = b.getValue("opt_value");
                }
            }
        }
        List cl = new ArrayList();
        for (File f : listDirs) {
            if (f.isDirectory()) {
                Map map = new HashMap();
                map.put("theme_dir", f.getName());
                if (f.getName().equals(current_theme)) {
                    map.put("current_theme", current_theme);
                }
                cl.add(map);
            }
        }
        return cl;
    }
}
