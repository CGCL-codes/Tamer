package cn.myapps.mobile.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import cn.myapps.base.action.ParamsTable;
import cn.myapps.base.dao.DataPackage;
import cn.myapps.constans.Web;
import cn.myapps.core.dynaform.activity.ejb.Activity;
import cn.myapps.core.dynaform.activity.ejb.ActivityType;
import cn.myapps.core.dynaform.document.ejb.Document;
import cn.myapps.core.dynaform.form.constants.MobileConstant;
import cn.myapps.core.dynaform.form.ejb.Form;
import cn.myapps.core.dynaform.view.action.ViewAction;
import cn.myapps.core.dynaform.view.ejb.Column;
import cn.myapps.core.dynaform.view.ejb.View;
import cn.myapps.core.macro.runner.IRunner;
import cn.myapps.core.macro.runner.JavaScriptFactory;
import cn.myapps.core.resource.ejb.ResourceType;
import cn.myapps.core.user.action.WebUser;
import cn.myapps.util.HtmlEncoder;
import com.opensymphony.webwork.ServletActionContext;

public class MbViewAction extends ViewAction {

    private static final long serialVersionUID = 1876974711850496098L;

    private String _currpage;

    private String _mapStr;

    public String get_mapStr() {
        return _mapStr;
    }

    public void set_mapStr(String str) {
        _mapStr = str;
    }

    public MbViewAction() throws ClassNotFoundException {
        super();
    }

    public String doDisplaySearchForm() throws Exception {
        try {
            view = (View) process.doView(_viewid);
            if (view != null) {
                setContent(view);
                toSearchFormXml();
            } else {
                throw new Exception("View id is null!");
            }
        } catch (Exception e) {
            this.addFieldError("SystemError", e.getMessage());
            e.printStackTrace();
            return ERROR;
        }
        return SUCCESS;
    }

    private void toSearchFormXml() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        WebUser user = getUser();
        ParamsTable params = new ParamsTable();
        params.setParameter("_viewid", _viewid);
        params.setParameter("_mapStr", _mapStr);
        params.setParameter("parentid", parent);
        Document tdoc = parent != null ? parent : new Document();
        IRunner runner = JavaScriptFactory.getInstance(request.getSession().getId(), getApplication());
        runner.initBSFManager(tdoc, params, user, new ArrayList());
        Form searchForm = view.getSearchForm();
        if (searchForm != null) {
            Document searchDoc = searchForm.createDocument(params, user);
            String xmlText = searchForm.toMbXML(searchDoc, params, user, new ArrayList(), getEnvironment());
            System.out.println("XmlText --> " + xmlText);
            session.setAttribute("toXml", xmlText);
        }
    }

    public String doDialogView() throws Exception {
        return doDisplayView(true);
    }

    public String doDisplayView() throws Exception {
        return doDisplayView(false);
    }

    private String doDisplayView(boolean isDialogView) throws Exception {
        try {
            ParamsTable params = getParams();
            view = (View) process.doView(_viewid);
            if (view != null) {
                view.setPagination(true);
                setContent(view);
                setParent(null);
                changeOrderBy(params);
                DataPackage datas = getViewDatas(view, params, 5, getApplication());
                setDatas(datas);
                toViewListXml(isDialogView);
            } else {
                throw new Exception("View id is null or view is not exist!");
            }
        } catch (Exception e) {
            this.addFieldError("SystemError", e.getMessage());
            e.printStackTrace();
            return ERROR;
        }
        return SUCCESS;
    }

    private void toViewListXml(boolean isDialogView) throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        HttpSession session = request.getSession();
        WebUser user = getUser();
        Collection errors = new HashSet();
        ParamsTable params = getParams();
        String refresh = params.getParameterAsString("refresh");
        boolean isRefresh = refresh != null && refresh.trim().equals("true");
        IRunner jsrun = JavaScriptFactory.getInstance(params.getSessionid(), getApplication());
        Collection columns = view.getColumns();
        DataPackage datas = getDatas();
        String title = view.getDescription();
        if (title == null || title.trim().length() <= 0 || title.trim().equals("null")) title = view.getName();
        title = " (" + datas.pageNo + "/" + datas.getPageCount() + ")  " + title;
        StringBuffer buffer = new StringBuffer();
        buffer.append("<").append(MobileConstant.TAG_VIEW).append(" ").append(MobileConstant.ATT_TITLE).append("='" + HtmlEncoder.encode(title) + "' ");
        if (isRefresh) {
            buffer.append(MobileConstant.ATT_REFRESH).append("='true' ");
        }
        if (view.getReadonly().booleanValue()) {
            buffer.append(MobileConstant.ATT_READONLY).append("='true' ");
        }
        buffer.append(">");
        if (isRefresh) buffer.append("<").append(MobileConstant.TAG_HIDDENFIELD).append(" ").append(MobileConstant.ATT_NAME).append("='refresh'>true</").append(MobileConstant.TAG_HIDDENFIELD).append(">");
        buffer.append("<").append(MobileConstant.TAG_HIDDENFIELD).append(" ").append(MobileConstant.ATT_NAME).append("='_viewid'>" + _viewid + "</").append(MobileConstant.TAG_HIDDENFIELD).append(">");
        if (isDialogView) {
            String mapStr = params.getParameterAsString("_mapStr");
            buffer.append("<").append(MobileConstant.TAG_HIDDENFIELD).append(" ").append(MobileConstant.ATT_NAME).append("='_mapStr'>" + mapStr + "</").append(MobileConstant.TAG_HIDDENFIELD).append(">");
        }
        if (parent != null && parent.getId() != null) {
            buffer.append("<").append(MobileConstant.TAG_HIDDENFIELD).append(" ").append(MobileConstant.ATT_NAME).append("='parentid'>" + parent.getId() + "</").append(MobileConstant.TAG_HIDDENFIELD).append(">");
        }
        String isRelate = params.getParameterAsString("isRelate");
        if (isRelate != null && isRelate.trim().equals("true")) {
            buffer.append("<").append(MobileConstant.TAG_HIDDENFIELD).append(" ").append(MobileConstant.ATT_NAME).append("='isRelate'>true</").append(MobileConstant.TAG_HIDDENFIELD).append(">");
        }
        Document tdoc = parent != null ? parent : new Document();
        boolean flag = false;
        IRunner runner = JavaScriptFactory.getInstance(session.getId(), user.getApplicationid());
        runner.initBSFManager(tdoc, params, user, new ArrayList());
        Collection activities = view.getActivitys();
        if (activities == null) {
            activities = new ArrayList();
        }
        Iterator aiter = activities.iterator();
        while (aiter.hasNext()) {
            Activity act = (Activity) aiter.next();
            boolean isHidden = false;
            if ((act.getHiddenScript()) != null && (act.getHiddenScript()).trim().length() > 0) {
                StringBuffer label = new StringBuffer();
                label.append("View").append("." + view.getName()).append(".Activity(").append(act.getId()).append(act.getName() + ")").append(".runHiddenScript");
                Object result = runner.run(label.toString(), act.getHiddenScript());
                if (result != null && result instanceof Boolean) {
                    isHidden = ((Boolean) result).booleanValue();
                }
            }
            boolean isStateToHidden = false;
            if (parent != null) {
                isStateToHidden = act.isStateToHidden(parent);
            }
            flag = (isHidden || isStateToHidden);
            if (!flag) {
                if (act.getType() != ActivityType.EXPTOEXCEL) {
                    buffer.append("<").append(MobileConstant.TAG_ACTION).append(" ").append(MobileConstant.ATT_TYPE).append("='");
                    buffer.append(act.getType());
                    buffer.append("' ").append(MobileConstant.ATT_NAME).append("='{*[" + HtmlEncoder.encode(act.getName()) + "]*}' ");
                    buffer.append("").append(MobileConstant.ATT_ID).append("='");
                    buffer.append(act.getId());
                    buffer.append("'>");
                    buffer.append("</").append(MobileConstant.TAG_ACTION).append(">");
                }
            }
        }
        if (isDialogView) {
            buffer.append("<").append(MobileConstant.TAG_ACTION).append(" ").append(MobileConstant.ATT_TYPE).append("='link' ").append(MobileConstant.ATT_NAME).append("='{*[OK]*}' ");
            buffer.append(">");
            buffer.append("</").append(MobileConstant.TAG_ACTION).append(">");
        }
        buffer.append("<").append(MobileConstant.TAG_TH).append(">");
        if (view != null) {
            if (view.getSearchForm() != null) {
                buffer.append("<").append(MobileConstant.TAG_ACTION).append(" ").append(MobileConstant.ATT_TYPE).append("='23'");
                buffer.append(" ").append(MobileConstant.ATT_NAME).append("='{*[Search]*}' ");
                buffer.append("").append(MobileConstant.ATT_ID).append("=''>");
                buffer.append("</").append(MobileConstant.TAG_ACTION).append(">");
            }
            Collection col = view.getColumns();
            if (col != null) {
                Iterator its = col.iterator();
                if (its != null) {
                    while (its.hasNext()) {
                        Column column = (Column) its.next();
                        buffer.append("<").append(MobileConstant.TAG_TD).append(" ").append(MobileConstant.ATT_NAME).append(" = '' ").append(MobileConstant.ATT_WIDTH).append(" = '" + column.getWidth() + "'>");
                        buffer.append(HtmlEncoder.encode(column.getName()) + "</").append(MobileConstant.TAG_TD).append(">");
                    }
                }
            }
        }
        buffer.append("</").append(MobileConstant.TAG_TH).append(">");
        if (datas != null) {
            int pages = datas.rowCount / datas.linesPerPage;
            if (datas.rowCount % datas.linesPerPage > 0) pages++;
            if (datas.pageNo < (pages)) {
                buffer.append("<").append(MobileConstant.TAG_ACTION).append(" ").append(MobileConstant.ATT_TYPE).append("='" + ResourceType.RESOURCE_TYPE_MOBILE + "'");
                buffer.append(" ").append(MobileConstant.ATT_NAME).append("='{*[NextPage]*}'>");
                buffer.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME).append("='_viewid'>" + HtmlEncoder.encode(view.getId()) + "</").append(MobileConstant.TAG_PARAMETER).append(">");
                if (parent != null && parent.getId() != null && parent.getId().trim().length() > 0) {
                    buffer.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME).append("='parentid'>" + HtmlEncoder.encode(parent.getId()) + "</").append(MobileConstant.TAG_PARAMETER).append(">");
                }
                buffer.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME).append(" ='_currpage'>" + (datas.pageNo + 1) + "</").append(MobileConstant.TAG_PARAMETER).append(">");
                buffer.append("</").append(MobileConstant.TAG_ACTION).append(">");
                buffer.append("<").append(MobileConstant.TAG_ACTION).append(" ").append(MobileConstant.ATT_TYPE).append("='" + ResourceType.RESOURCE_TYPE_MOBILE + "'");
                buffer.append(" ").append(MobileConstant.ATT_NAME).append("='{*[EndPage]*}'>");
                buffer.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME).append("='_viewid'>" + HtmlEncoder.encode(view.getId()) + "</").append(MobileConstant.TAG_PARAMETER).append(">");
                if (parent != null && parent.getId() != null && parent.getId().trim().length() > 0) {
                    buffer.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME).append("='parentid'>" + HtmlEncoder.encode(parent.getId()) + "</").append(MobileConstant.TAG_PARAMETER).append(">");
                }
                buffer.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME).append(" ='_currpage'>" + (pages) + "</").append(MobileConstant.TAG_PARAMETER).append(">");
                buffer.append("</").append(MobileConstant.TAG_ACTION).append(">");
            }
            if (datas.pageNo > 1) {
                buffer.append("<").append(MobileConstant.TAG_ACTION).append(" ").append(MobileConstant.ATT_TYPE).append("='" + ResourceType.RESOURCE_TYPE_MOBILE + "'");
                buffer.append(" ").append(MobileConstant.ATT_NAME).append("='{*[FirstPage]*}'>");
                buffer.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME).append("='_viewid'>" + HtmlEncoder.encode(view.getId()) + "</").append(MobileConstant.TAG_PARAMETER).append(">");
                if (parent != null && parent.getId() != null && parent.getId().trim().length() > 0) {
                    buffer.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME).append("='parentid'>" + HtmlEncoder.encode(parent.getId()) + "</").append(MobileConstant.TAG_PARAMETER).append(">");
                }
                buffer.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME).append(" ='_currpage'>1</").append(MobileConstant.TAG_PARAMETER).append(">");
                buffer.append("</").append(MobileConstant.TAG_ACTION).append(">");
                buffer.append("<").append(MobileConstant.TAG_ACTION).append(" ").append(MobileConstant.ATT_TYPE).append("='" + ResourceType.RESOURCE_TYPE_MOBILE + "'");
                buffer.append(" ").append(MobileConstant.ATT_NAME).append("='{*[PrevPage]*}'>");
                buffer.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME).append("='_viewid'>" + HtmlEncoder.encode(view.getId()) + "</").append(MobileConstant.TAG_PARAMETER).append(">");
                if (parent != null && parent.getId() != null && parent.getId().trim().length() > 0) {
                    buffer.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME).append("='parentid'>" + HtmlEncoder.encode(parent.getId()) + "</").append(MobileConstant.TAG_PARAMETER).append(">");
                }
                buffer.append("<").append(MobileConstant.TAG_PARAMETER).append(" ").append(MobileConstant.ATT_NAME).append(" ='_currpage'>" + (datas.pageNo - 1) + "</").append(MobileConstant.TAG_PARAMETER).append(">");
                buffer.append("</").append(MobileConstant.TAG_ACTION).append(">");
            }
            Collection col = datas.getDatas();
            if (col != null) {
                Iterator its = col.iterator();
                while (its.hasNext()) {
                    Document doc = (Document) its.next();
                    jsrun.initBSFManager(doc, params, user, errors);
                    Iterator iter = columns.iterator();
                    if (doc != null && doc.getId() != null) {
                        try {
                            buffer.append("<").append(MobileConstant.TAG_TR).append(" ").append(MobileConstant.ATT_ID).append(" = '" + doc.getId() + "'>");
                            while (iter.hasNext()) {
                                buffer.append("<").append(MobileConstant.TAG_TD).append(" ");
                                Column column = (Column) iter.next();
                                Object result = null;
                                if (column.getType() != null && column.getType().equals(Column.COLUMN_TYPE_SCRIPT)) {
                                    StringBuffer label = new StringBuffer();
                                    label.append("DisplayView.").append(view.getId()).append(".Column.").append(column.getId()).append("runValueScript");
                                    result = jsrun.run(label.toString(), column.getValueScript());
                                } else if (column.getType() != null && column.getType().equals(Column.COLUMN_TYPE_FIELD)) {
                                    if (column.getFieldName().startsWith("$")) {
                                        String propName = column.getFieldName().substring(1, column.getFieldName().length());
                                        result = doc.getValueByPropertyName(propName);
                                    } else {
                                        result = doc.getItemValueAsString(column.getFieldName());
                                    }
                                }
                                if (column != null && column.getId() != null) {
                                    buffer.append(" ").append(MobileConstant.ATT_NAME).append(" = '" + HtmlEncoder.encode(column.getId()) + "' >");
                                } else {
                                    buffer.append(" ").append(MobileConstant.ATT_NAME).append(" ='' >");
                                }
                                if (result == null) {
                                    result = "";
                                }
                                buffer.append(HtmlEncoder.encode((String) result));
                                buffer.append("</").append(MobileConstant.TAG_TD).append(">");
                            }
                            buffer.append("</").append(MobileConstant.TAG_TR).append(">");
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw e;
                        }
                    }
                }
            }
        }
        buffer.append("</").append(MobileConstant.TAG_VIEW).append(">");
        session.setAttribute("toXml", buffer.toString());
        System.out.println("XmlText --> " + buffer.toString());
    }

    public String get_currpage() {
        return _currpage;
    }

    public void set_currpage(String _currpage) {
        this._currpage = _currpage;
    }

    public String getApplication() {
        if (application != null && application.trim().length() > 0) {
            return application;
        } else {
            return (String) getContext().getSession().get(Web.SESSION_ATTRIBUTE_APPLICATION);
        }
    }

    private Map fieldErrors;

    public void addFieldError(String fieldname, String message) {
        List thisFieldErrors = (List) getFieldErrors().get(fieldname);
        if (thisFieldErrors == null) {
            thisFieldErrors = new ArrayList();
            this.fieldErrors.put(fieldname, thisFieldErrors);
        }
        thisFieldErrors.add(message);
    }

    public Map getFieldErrors() {
        if (fieldErrors == null) fieldErrors = new HashMap();
        return fieldErrors;
    }

    public void setFieldErrors(Map fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
