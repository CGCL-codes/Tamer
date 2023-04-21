package cn.mmbook.platform.action.tag;

import static javacommon.util.extjs.Struts2JsonHelper.outJson;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javacommon.util.StringTool;
import javacommon.base.BaseStruts2Action;
import javacommon.util.extjs.ExtJsPageHelper;
import javacommon.util.extjs.ListRange;
import cn.mmbook.platform.model.tag.CmsTagCategory;
import cn.mmbook.platform.model.tag.CmsTemplet;
import cn.mmbook.platform.service.tag.CmsTagCategoryManager;
import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * 
 * @author Felix 
 *
 */
public class CmsTagCategoryAction extends BaseStruts2Action implements Preparable, ModelDriven {

    protected static final String DEFAULT_SORT_COLUMNS = null;

    private CmsTagCategoryManager cmsTagCategoryManager;

    private CmsTagCategory cmsTagCategory;

    private CmsTemplet cmsTemplet;

    java.lang.String id = null;

    private String[] items;

    public void prepare() throws Exception {
        if (isNullOrEmptyString(id)) {
            cmsTagCategory = new CmsTagCategory();
        } else {
            cmsTagCategory = (CmsTagCategory) cmsTagCategoryManager.getById(id);
        }
    }

    /** 通过spring自动注入 */
    public void setCmsTagCategoryManager(CmsTagCategoryManager manager) {
        this.cmsTagCategoryManager = manager;
    }

    public Object getModel() {
        return cmsTagCategory;
    }

    public void setId(java.lang.String val) {
        this.id = val;
    }

    public void setItems(String[] items) {
        this.items = items;
    }

    /**
	 * ExtGrid使用
	 * 列表
	 * @throws IOException
	 */
    public void list() throws IOException {
        PageRequest<Map> pr = ExtJsPageHelper.createPageRequestForExtJs(getRequest(), DEFAULT_SORT_COLUMNS);
        String versionId = this.getRequest().getParameter("versionId");
        String tagSort = this.getRequest().getParameter("tagSort");
        if (StringTool.isNull(versionId)) {
            pr.getFilters().put("versionId", versionId);
        }
        if (StringTool.isNull(tagSort)) {
            pr.getFilters().put("tagSort", tagSort);
        }
        Page page = cmsTagCategoryManager.findByPageRequest(pr);
        List<CmsTagCategory> CmsTagCategorylist = (List) page.getResult();
        ListRange<CmsTagCategory> resultList = new ListRange<CmsTagCategory>();
        resultList.setList(CmsTagCategorylist);
        resultList.setTotalSize(page.getTotalCount());
        resultList.setMessage("ok");
        resultList.setSuccess(true);
        outJson(resultList);
    }

    /**
	 * extGrid保存
	 * @throws IOException
	 */
    public void save() throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            String cids = this.getRequest().getParameter("cids");
            System.out.println(cids);
            cmsTagCategory.setTagName(cmsTagCategory.getTagNameNew());
            if (null != cmsTagCategory.getTagName()) {
                cmsTagCategoryManager.saveCmsTagCategory(cmsTagCategory);
                result.put("success", true);
                result.put("msg", "添加栏目标签成功!");
            } else {
                result.put("failure", true);
                result.put("msg", "添加栏目标签失败!");
            }
        } catch (Exception e) {
            result.put("failure", true);
            result.put("msg", "添加栏目标签失败!");
            e.printStackTrace();
        }
        outJson(result);
    }

    /**
	 * extGrid修改
	 * @throws IOException
	 */
    public void update() throws IOException {
        System.out.println("update");
        Map<String, Object> result = new HashMap<String, Object>();
        if (null != cmsTagCategory) {
            try {
                cmsTagCategory.setTagName(cmsTagCategory.getTagNameNew());
                if (null != cmsTagCategory.getTagName()) {
                    cmsTagCategoryManager.updateCmsTagCategory(cmsTagCategory);
                    result.put("success", true);
                    result.put("msg", "修改栏目标签成功!");
                } else {
                    result.put("failure", true);
                    result.put("msg", "修改栏目标签失败!");
                }
            } catch (Exception e) {
                result.put("failure", true);
                result.put("msg", "修改栏目标签失败!");
                e.printStackTrace();
            }
        } else {
            result.put("failure", true);
            result.put("msg", "数据不存在!");
        }
        outJson(result);
    }

    /**
	 * extGrid删除
	 * @throws IOException
	 */
    public void delete() throws IOException {
        String ids = getRequest().getParameter("ids");
        String[] idarray = ids.split(",");
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            for (int i = 0; i < idarray.length; i++) {
                java.lang.String id = new java.lang.String((String) idarray[i]);
                cmsTagCategoryManager.removeById(id);
            }
            result.put("success", true);
            result.put("msg", "删除成功");
        } catch (Exception e) {
            result.put("failure", true);
            result.put("msg", "删除失败!");
            e.printStackTrace();
        }
        outJson(result);
    }

    /**
	 * 验证文本输入重复信息
	 * @author qiongguo
	 * @throws IOException
	 */
    public void repetitionText() throws IOException {
        String textvalue = "444444";
        textvalue = (null == this.getRequest().getParameter("textvalue")) ? "" : this.getRequest().getParameter("textvalue");
        Map<String, Object> result = new HashMap<String, Object>();
        if (textvalue.length() > 0) {
            try {
                CmsTagCategory obj_info = new CmsTagCategory();
                obj_info.setTagName(textvalue);
                Map<String, Object> parameter = new HashMap<String, Object>();
                List list = cmsTagCategoryManager.getList(obj_info);
                if (list != null && list.size() > 0) {
                    result.put("failure", true);
                    result.put("msg", "已经被使用");
                } else {
                    result.put("success", true);
                    result.put("msg", "验证通过成功");
                }
            } catch (Exception e) {
                result.put("failure", true);
                result.put("msg", e.getMessage());
                e.printStackTrace();
            }
        }
        outJson(result);
    }

    /**
	 * 返回栏目标签所有数据
	 * @author qiongguo
	 * @throws IOException
	 */
    public void getList() throws IOException {
        CmsTagCategory obj_info = new CmsTagCategory();
        List<CmsTagCategory> cmsTagCategorylist = (List) cmsTagCategoryManager.getList(obj_info);
        ListRange<CmsTagCategory> resultList = new ListRange<CmsTagCategory>();
        resultList.setList(cmsTagCategorylist);
        resultList.setTotalSize(cmsTagCategorylist.size());
        resultList.setMessage("ok");
        resultList.setSuccess(true);
        outJson(resultList);
    }

    /**
	 * ExtGrid使用
	 * 列表
	 * @author qiongguo
	 * @throws IOException
	 */
    public void listNew() throws IOException {
        PageRequest<Map> pr = ExtJsPageHelper.createPageRequestForExtJs(getRequest(), DEFAULT_SORT_COLUMNS);
        Page page = cmsTagCategoryManager.findByPageRequestNew(pr);
        List<CmsTagCategory> CmsTagCategorylist = (List) page.getResult();
        ListRange<CmsTagCategory> resultList = new ListRange<CmsTagCategory>();
        resultList.setList(CmsTagCategorylist);
        resultList.setTotalSize(page.getTotalCount());
        resultList.setMessage("ok");
        resultList.setSuccess(true);
        outJson(resultList);
    }

    public java.lang.String getId() {
        return id;
    }

    public CmsTemplet getCmsTemplet() {
        return cmsTemplet;
    }

    public void setCmsTemplet(CmsTemplet cmsTemplet) {
        this.cmsTemplet = cmsTemplet;
    }

    /**
	 * 新增Action  取不同的分类数据
	 * ['channel','频道标签'],['category','栏目标签'],['sort','分类标签']
	 * @author qiongguo
	 * @throws IOException
	 */
    public String turn() throws IOException {
        String v = (null == this.getRequest().getParameter("v")) ? "" : this.getRequest().getParameter("v");
        String u = (null == this.getRequest().getParameter("u")) ? "" : this.getRequest().getParameter("u");
        String tagType = (null == this.getRequest().getParameter("tagType")) ? "" : this.getRequest().getParameter("tagType");
        String url = "";
        System.out.println("###@^&*(" + tagType);
        if ("channel".equals(tagType)) {
            return "manage/SiteChannels/getComboBox.do?v=" + v + "&u=" + u;
        } else if ("category".equals(tagType)) {
            return "manage/SitePart/getSynTree.do?v=" + v + "&u=1151204";
        }
        if ("sort".equals(tagType)) {
            return "manage/SiteContentSort/getTreeCombox.do?v=" + v + "&u=" + u;
        } else {
            return "/admin/tag/CmsTagCategory/index.jsp?v=" + v + "&u=" + u;
        }
    }
}
