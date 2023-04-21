package com.company.common.Hibernate.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.company.common.Constant;
import com.company.common.Hibernate.PageInfo;
import com.company.common.global.MessageTreat;
import com.company.common.util.JsonUtil;
import com.company.common.util.StringUtil;
import com.company.erp.right.dao.TBPermissionDAO;
import com.company.erp.right.dao.VwBRolePermissionDAO;
import com.company.erp.right.model.TBPermission;
import com.company.erp.right.model.VwBRolePermission;
import com.company.sys.dao.TSMenuDAO;
import com.company.sys.dao.TSParamsDAO;
import com.company.sys.dao.VwSRoleMenuDAO;
import com.company.sys.model.LoginUser;
import com.company.sys.model.TSMenu;
import com.company.sys.model.TSParams;
import com.company.sys.model.VwSRoleMenu;
import com.opensymphony.xwork2.ActionSupport;

public class BaseAction extends ActionSupport {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(BaseAction.class);

    private BaseContext context;

    public PageInfo pageInfo;

    private LoginUser loginUser;

    private MessageTreat messageTreat;

    private String menuId;

    private String parentMenu;

    private VwSRoleMenuDAO vwSRoleMenuDAO;

    private VwSRoleMenu vwSRoleMenu;

    private TSParamsDAO tsParamsDAO;

    private Map<String, String> rightMap;

    private VwBRolePermissionDAO vwBRolePermissionDAO;

    private TBPermissionDAO tbPermissionDAO;

    private TSMenuDAO tsMenuDAO;

    public Map<String, String> getRight() {
        if (StringUtil.isEmpty(getMenuId())) {
            menuId = (String) context.getSessionAttribute("currentMenu");
        } else {
            context.setSessionAttribute("currentMenu", menuId);
        }
        if (rightMap != null) rightMap.clear(); else {
            rightMap = new HashMap<String, String>();
        }
        List tsParamsList = tsParamsDAO.findByCode(Constant.RIGHT_CONTROL);
        if (tsParamsList.size() > 0) {
            TSParams tsParams = (TSParams) tsParamsList.get(0);
            TSMenu tsMenu = tsMenuDAO.findById(menuId);
            rightMap.put(Constant.MENU_NAME, tsMenu.getName());
            if (Constant.RIGHT_CONTROL_SYS.equals(tsParams.getValue())) {
                vwSRoleMenu = getVwSRoleMenuDAO().findByRoleIdAndMenuId(getLoginUser().getRoleId(), menuId);
                rightMap.put(Constant.RIGHT_ADD, Integer.toString(vwSRoleMenu.getAdd()));
                rightMap.put(Constant.RIGHT_DELETE, Integer.toString(vwSRoleMenu.getDelete()));
                rightMap.put(Constant.RIGHT_EDIT, Integer.toString(vwSRoleMenu.getEdit()));
                rightMap.put(Constant.RIGHT_VIEW, Integer.toString(vwSRoleMenu.getView()));
                List<TBPermission> list = tbPermissionDAO.findAll();
                for (int i = 0; i < list.size(); i++) {
                    TBPermission tbPermission = (TBPermission) list.get(i);
                    rightMap.put(tbPermission.getCode(), Constant.RIGHT_YES);
                }
            } else if (Constant.RIGHT_CONTROL_BUS.equals(tsParams.getValue())) {
                rightMap.put(Constant.RIGHT_ADD, Constant.RIGHT_YES);
                rightMap.put(Constant.RIGHT_DELETE, Constant.RIGHT_YES);
                rightMap.put(Constant.RIGHT_EDIT, Constant.RIGHT_YES);
                rightMap.put(Constant.RIGHT_VIEW, Constant.RIGHT_YES);
                List<VwBRolePermission> list = vwBRolePermissionDAO.findByRoleId(getLoginUser().getRoleId());
                for (int i = 0; i < list.size(); i++) {
                    VwBRolePermission vwBRolePermission = (VwBRolePermission) list.get(i);
                    rightMap.put(vwBRolePermission.getPcode(), vwBRolePermission.getStatus());
                }
            }
        }
        return rightMap;
    }

    public String getSuccessMsg(String msg) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("status", "success");
        map.put("message", msg);
        String temp = JsonUtil.map2Josn(map);
        logger.info(temp);
        getServletResponse().setContentType("text/html;charset=utf-8");
        try {
            PrintWriter write;
            write = getServletResponse().getWriter();
            write.print(temp);
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    public String getSuccessMsgByJson(String msg) {
        String result = msg;
        HttpServletResponse response = getServletResponse();
        try {
            logger.info(result);
            response.setHeader("ContentType", "text/json");
            response.setCharacterEncoding("utf-8");
            PrintWriter pw = response.getWriter();
            pw.write(result);
            pw.flush();
            pw.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    public String getErrorMsg(String msg) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("status", "error");
        map.put("message", msg);
        String temp = JsonUtil.map2Josn(map);
        logger.info(temp);
        getServletResponse().setContentType("text/html;charset=utf-8");
        try {
            PrintWriter write;
            write = getServletResponse().getWriter();
            write.print(temp);
        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    public String[] getUpdateProperties(String name) {
        Enumeration pNames = getServletRequest().getParameterNames();
        List updateProps = new ArrayList();
        while (pNames.hasMoreElements()) {
            String pName = (String) pNames.nextElement();
            if (pName.startsWith(name + ".")) {
                updateProps.add(pName.substring(name.length() + 1));
            }
        }
        String[] props = new String[updateProps.size()];
        updateProps.toArray(props);
        return props;
    }

    public void copyUpdatedProperties(String formAttr, Object source, Object target) {
        String[] updateProps = this.getUpdateProperties(formAttr);
        for (String prop : updateProps) {
            if (prop.indexOf(".") > 0) {
                prop = prop.substring(0, prop.indexOf("."));
            }
            Object value = null;
            try {
                Method getMethod = source.getClass().getMethod("get" + StringUtils.capitalize(prop), new Class[0]);
                value = getMethod.invoke(source, new Object[0]);
            } catch (Exception e) {
                try {
                    Field field = source.getClass().getField(prop);
                    value = field.get(source);
                } catch (Exception e1) {
                    this.logger.info("复制属性获取原值出错:" + prop);
                    e1.printStackTrace();
                }
            }
            try {
                BeanUtils.setProperty(target, prop, value);
            } catch (Exception e) {
                this.logger.info("复制属性出错:" + prop);
                e.printStackTrace();
            }
        }
    }

    public LoginUser getLoginUser() {
        loginUser = (LoginUser) context.getSessionAttribute("loginUser");
        return loginUser;
    }

    public void setLoginUser(LoginUser loginUser) {
        context.setSessionAttribute("loginUser", loginUser);
        this.loginUser = loginUser;
    }

    public MessageTreat getMessageTreat() {
        return messageTreat;
    }

    public void setMessageTreat(MessageTreat messageTreat) {
        this.messageTreat = messageTreat;
    }

    public HttpServletResponse getServletResponse() {
        return context.getServletResponse();
    }

    public HttpServletRequest getServletRequest() {
        return context.getServletRequest();
    }

    public PageInfo getPageInfo() {
        if (pageInfo == null) {
            pageInfo = new PageInfo();
        }
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    public BaseContext getContext() {
        return context;
    }

    public void setContext(BaseContext context) {
        this.context = context;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(String parentMenu) {
        this.parentMenu = parentMenu;
    }

    public VwSRoleMenuDAO getVwSRoleMenuDAO() {
        return vwSRoleMenuDAO;
    }

    public void setVwSRoleMenuDAO(VwSRoleMenuDAO vwSRoleMenuDAO) {
        this.vwSRoleMenuDAO = vwSRoleMenuDAO;
    }

    public VwSRoleMenu getVwSRoleMenu() {
        return vwSRoleMenu;
    }

    public void setVwSRoleMenu(VwSRoleMenu vwSRoleMenu) {
        this.vwSRoleMenu = vwSRoleMenu;
    }

    public TSParamsDAO getTsParamsDAO() {
        return tsParamsDAO;
    }

    public void setTsParamsDAO(TSParamsDAO tsParamsDAO) {
        this.tsParamsDAO = tsParamsDAO;
    }

    public Map<String, String> getRightMap() {
        return rightMap;
    }

    public void setRightMap(Map<String, String> rightMap) {
        this.rightMap = rightMap;
    }

    public VwBRolePermissionDAO getVwBRolePermissionDAO() {
        return vwBRolePermissionDAO;
    }

    public void setVwBRolePermissionDAO(VwBRolePermissionDAO vwBRolePermissionDAO) {
        this.vwBRolePermissionDAO = vwBRolePermissionDAO;
    }

    public TBPermissionDAO getTbPermissionDAO() {
        return tbPermissionDAO;
    }

    public void setTbPermissionDAO(TBPermissionDAO tbPermissionDAO) {
        this.tbPermissionDAO = tbPermissionDAO;
    }

    public TSMenuDAO getTsMenuDAO() {
        return tsMenuDAO;
    }

    public void setTsMenuDAO(TSMenuDAO tsMenuDAO) {
        this.tsMenuDAO = tsMenuDAO;
    }
}
