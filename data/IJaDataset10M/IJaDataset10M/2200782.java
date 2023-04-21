package com.companyname.common.sysframe.view.foreveryone;

import java.util.List;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.companyname.common.base.model.BaseObject;
import com.companyname.common.base.web.view.AddFuncButton;
import com.companyname.common.base.web.view.BaseObjectListTemplet;
import com.companyname.common.base.web.view.ConfigColumnFuncButton;
import com.companyname.common.base.web.view.DelFuncButton;
import com.companyname.common.base.web.view.ExportFuncButton;
import com.companyname.common.base.web.view.FuncItem;
import com.companyname.common.base.web.view.Item4CheckBox;
import com.companyname.common.base.web.view.PrintFuncButton;
import com.companyname.common.base.web.view.QueryFuncButton;
import com.companyname.common.base.web.view.Title4CheckBox;
import com.companyname.common.base.web.view.UpdateFuncButton;
import com.companyname.common.sysframe.model.Post2User;

/**
 * <p>Title: 岗位2人员结果列表 </p>
 * <p>Description: 岗位2人员结果列表 Post2User </p>
 * <p>Copyright: Copyright (c) 2004-2006</p>
 * <p>Company: 公司名</p>
 * @ $Author: 作者名 $
 * @ $Date: 创建日期 $
 * @ $Revision: 1.0 $
 * @ created in 创建日期
 *
 */
public class Post2UserList4EveryOne extends BaseObjectListTemplet {

    static Logger logger = Logger.getLogger(Post2UserList4EveryOne.class);

    /** 功能按钮区 */
    public List getButtons(String contextPath) {
        List fbs = new Vector();
        fbs.add(new AddFuncButton());
        fbs.add(new UpdateFuncButton());
        fbs.add(new DelFuncButton());
        fbs.add(new QueryFuncButton());
        fbs.add(new ExportFuncButton());
        fbs.add(new PrintFuncButton());
        fbs.add(new ConfigColumnFuncButton());
        return fbs;
    }

    /** 功能切换区 */
    public List getChanges(String contextPath) {
        List fis = new Vector();
        fis.add(new FuncItem("岗位2人员", true));
        return fis;
    }

    /** 明细标题区 */
    public List getTitles(String contextPath) {
        List titles = new Vector();
        titles.add(new Title4CheckBox());
        return titles;
    }

    /** 明细区 */
    public List getItems(String contextPath, BaseObject model) {
        Post2User post2User = (Post2User) model;
        List items = new Vector();
        items.add(new Item4CheckBox(post2User.getId()));
        return items;
    }
}
