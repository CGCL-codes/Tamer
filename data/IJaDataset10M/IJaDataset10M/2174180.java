package cn.mmbook.platform.action.site;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javacommon.util.extjs.ExtJsPageHelper;
import javacommon.util.extjs.ListRange;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;
import cn.mmbook.platform.model.site.SiteInfomation;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.ModelDriven;
import static javacommon.util.extjs.Struts2JsonHelper.*;
import java.util.*;
import javacommon.base.*;
import javacommon.util.*;
import cn.org.rapid_framework.util.*;
import cn.org.rapid_framework.web.util.*;
import cn.org.rapid_framework.page.*;
import cn.org.rapid_framework.page.impl.*;
import cn.mmbook.platform.model.site.*;
import cn.mmbook.platform.dao.site.*;
import cn.mmbook.platform.service.site.impl.*;
import cn.mmbook.platform.service.site.*;

/**
 * <p> SiteInfomationAction<br> 
 * <p>   <br>
 * @author admin,
 * @version 1.0. 2010-07-08
 *
 */
public class SiteInfomationAction extends BaseStruts2Action implements Preparable, ModelDriven {

    protected static final String DEFAULT_SORT_COLUMNS = null;

    private SiteInfomationManager siteInfomationManager;

    private SiteInfomation siteInfomation;

    java.lang.Integer id = null;

    private String[] items;

    /**
	 * 自动获取对象
	 * @exception Exception
	 */
    public void prepare() throws Exception {
        if (isNullOrEmptyString(id)) {
            siteInfomation = new SiteInfomation();
        } else {
            siteInfomation = (SiteInfomation) siteInfomationManager.getById(id);
        }
    }

    /**
	 * 对象通过spring自动注入
	 * @param manager SiteInfomationManager
	 *  
	 */
    public void setSiteInfomationManager(SiteInfomationManager manager) {
        this.siteInfomationManager = manager;
    }

    /**
	 * struts 自动把页面提交数据转换成数据类对象赋值
	 * @return SiteInfomation siteInfomation
	 */
    public Object getModel() {
        return siteInfomation;
    }

    public void setId(java.lang.Integer val) {
        this.id = val;
    }

    public void setItems(String[] items) {
        this.items = items;
    }

    /**
	 * 分页显示数据,通过JSON串返回给页面
     * @author admin,
     * @version 1.0. 2010-07-08
	 * @throws IOException
	 */
    public void list() throws IOException {
        PageRequest<Map> pageRequest = ExtJsPageHelper.createPageRequestForExtJs(getRequest(), DEFAULT_SORT_COLUMNS);
        Page pages = siteInfomationManager.findByPageRequest(pageRequest);
        List<SiteInfomation> SiteInfomationlist = (List) pages.getResult();
        ListRange<SiteInfomation> resultList = new ListRange<SiteInfomation>();
        resultList.setList(SiteInfomationlist);
        resultList.setTotalSize(pages.getTotalCount());
        resultList.setMessage("ok");
        resultList.setSuccess(true);
        outJson(resultList);
    }

    /**
	 * 返回所有数据
     * @author admin,
     * @version 1.0. 2010-07-08
	 * @throws IOException
	 */
    public void findAll() throws IOException {
        ArrayList<SiteInfomation> SiteInfomationlist = (ArrayList) siteInfomationManager.findAll();
        ListRange<SiteInfomation> resultList = new ListRange<SiteInfomation>();
        resultList.setList(SiteInfomationlist);
        resultList.setMessage("ok");
        resultList.setSuccess(true);
        outJson(resultList);
    }

    /**
	 * 保存数据
	 * 系统自动把页面提交数据，封装到 SiteInfomation 对象
     * @author admin,
     * @version 1.0. 2010-07-08
	 * @throws IOException
	 */
    public void save() throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            siteInfomationManager.save(siteInfomation);
            result.put("success", true);
            result.put("msg", "数据添加成功!");
        } catch (Exception e) {
            result.put("failure", true);
            result.put("msg", "数据添加失败!");
            e.printStackTrace();
        }
        outJson(result);
    }

    /**
	 * 修改数据
	 * 系统自动把页面提交数据，封装到 SiteInfomation 对象
     * @author admin,
     * @version 1.0. 2010-07-08
	 * @throws IOException
	 */
    public void update() throws IOException {
        Map<String, Object> result = new HashMap<String, Object>();
        if (null != siteInfomation) {
            try {
                siteInfomationManager.update(siteInfomation);
                result.put("success", true);
                result.put("msg", "数据修改成功!");
            } catch (Exception e) {
                result.put("failure", true);
                result.put("msg", "数据修改失败!");
                e.printStackTrace();
            }
        } else {
            result.put("failure", true);
            result.put("msg", "数据不存在!");
        }
        outJson(result);
    }

    /**
	 * 删除单条或多条数据
	 * 页面把需要删除的数据的ID存放在　参数 ids 中。
	 * Action 把IDS 分割,得到要删除对象主键ID进行删除
     * @author admin,
     * @version 1.0. 2010-07-08
	 * @throws IOException
	 */
    public void delete() throws IOException {
        String ids = getRequest().getParameter("ids");
        String[] idarray = ids.split(",");
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            for (int i = 0; i < idarray.length; i++) {
                java.lang.Integer id = new java.lang.Integer((String) idarray[i]);
                siteInfomationManager.removeById(id);
            }
            result.put("success", true);
            result.put("msg", "数据删除成功");
        } catch (Exception e) {
            result.put("failure", true);
            result.put("msg", "数据删除失败!");
            e.printStackTrace();
        }
        outJson(result);
    }

    /**
	 * 下拉框所用
	 * 返回格式: ['1','name1'],['2','name2'],['3','kep']
     * @author admin,
     * @version 1.0. 2010-07-08
	 * @throws IOException
	 */
    public void getComboBox() throws IOException {
        String result = null;
        List list = siteInfomationManager.getList(siteInfomation);
        for (int i = 0; list != null && i < list.size(); i++) {
            siteInfomation = (SiteInfomation) list.get(i);
            if (i == 0) {
                result = "[['" + siteInfomation.getId() + "','" + siteInfomation.getSiteName() + "']";
            } else {
                result += ",['" + siteInfomation.getId() + "','" + siteInfomation.getSiteName() + "']";
            }
        }
        if (!"".equals(result)) {
            result += "]";
        }
        outJsonString(result);
    }

    /**
	 * 多表关联查询
	 * 分页显示数据
	 * 列表
     * @author admin,
     * @version 1.0. 2010-07-08
	 * @throws IOException
	 */
    public void listAnyTable() throws IOException {
        PageRequest<Map> pr = ExtJsPageHelper.createPageRequestForExtJs(getRequest(), DEFAULT_SORT_COLUMNS);
        Page page = siteInfomationManager.listPageAnyTable(pr);
        List<SiteInfomation> SiteInfomationlist = (List) page.getResult();
        ListRange<SiteInfomation> resultList = new ListRange<SiteInfomation>();
        resultList.setList(SiteInfomationlist);
        resultList.setTotalSize(page.getTotalCount());
        resultList.setMessage("ok");
        resultList.setSuccess(true);
        outJson(resultList);
    }
}
