package org.ironrhino.common.action;

import javax.inject.Inject;
import org.apache.commons.lang3.StringUtils;
import org.ironrhino.common.Constants;
import org.ironrhino.common.model.Page;
import org.ironrhino.common.service.PageManager;
import org.ironrhino.common.support.SettingControl;
import org.ironrhino.core.metadata.AutoConfig;
import org.ironrhino.core.model.ResultPage;
import org.ironrhino.core.struts.BaseAction;
import org.ironrhino.core.struts.RequestDecoratorMapper;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.interceptor.annotations.Before;

@AutoConfig(namespace = ColumnPageAction.NAMESPACE, actionName = ColumnPageAction.ACTION_NAME)
public class ColumnPageAction extends BaseAction {

    private static final long serialVersionUID = -7189565572156313486L;

    public static final String NAMESPACE = "/";

    public static final String ACTION_NAME = "_column_page_";

    @Inject
    protected PageManager pageManager;

    @Inject
    protected SettingControl settingControl;

    protected String column;

    protected String[] columns;

    protected ResultPage<Page> resultPage;

    private String name;

    protected Page page;

    public ResultPage<Page> getResultPage() {
        return resultPage;
    }

    public void setResultPage(ResultPage<Page> resultPage) {
        this.resultPage = resultPage;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String[] getColumns() {
        return columns;
    }

    public Page getPage() {
        return page;
    }

    public void setName(String name) {
        this.name = name;
        ActionContext.getContext().setName(getName());
    }

    public String getName() {
        if (name == null) name = ActionContext.getContext().getName();
        return name;
    }

    @Override
    public String execute() {
        return list();
    }

    @Override
    public String list() {
        columns = settingControl.getStringArray(getName() + Constants.SETTING_KEY_COLUMN_SUFFIX);
        column = getUid();
        if (StringUtils.isBlank(column)) {
            page = pageManager.getByPath("/" + getName() + "/preface");
            if (page != null) return "columnpage";
        }
        if (resultPage == null) resultPage = new ResultPage<Page>();
        if (StringUtils.isBlank(column)) resultPage = pageManager.findResultPageByTag(resultPage, getName()); else resultPage = pageManager.findResultPageByTag(resultPage, new String[] { getName(), column });
        return "columnlist";
    }

    public String p() {
        columns = settingControl.getStringArray(getName() + Constants.SETTING_KEY_COLUMN_SUFFIX);
        String path = getUid();
        if (StringUtils.isNotBlank(path)) {
            path = "/" + path;
            page = pageManager.getByPath(path);
        }
        return "columnpage";
    }

    @Before
    public void setDecorator() {
        RequestDecoratorMapper.setDecorator(getName());
    }
}
