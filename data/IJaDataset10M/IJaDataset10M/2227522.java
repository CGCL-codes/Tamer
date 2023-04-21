package com.gever.goa.infoservice.action;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.gever.exception.DefaultException;
import com.gever.goa.infoservice.dao.IsCustomerFactory;
import com.gever.goa.infoservice.dao.IsDoTypeDao;
import com.gever.goa.infoservice.vo.IsDoTypeVO;
import com.gever.jdbc.BaseDAO;
import com.gever.struts.action.BaseAction;
import com.gever.struts.action.GoaActionConfig;
import com.gever.sysman.privilege.util.PermissionUtil;
import com.gever.util.StringUtils;
import com.gever.vo.BaseTreeVO;

/**
 * <p>Title: ��Ϣ��������Action</p>
 * <p>Description: KOBE OFFICE ��Ȩ���У�Υ�߱ؾ���</p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: KOBE OFFICE</p>
 * @author Hu.Walker
 * @version 1.0
 */
public class IsDoTypeAction extends BaseAction {

    private IsDoTypeDao isDoTypeDao = null;

    public IsDoTypeAction() {
    }

    protected void initData(GoaActionConfig cfg) throws DefaultException, Exception {
        IsDoTypesForm myForm = (IsDoTypesForm) cfg.getBaseForm();
        isDoTypeDao = IsCustomerFactory.getInstance().createIsDoType(super.dbData);
        cfg.setBaseDao((BaseDAO) isDoTypeDao);
        if (myForm.getVo() == null) {
            myForm.setVo(new IsDoTypeVO());
        }
        this.setVoSql(false);
    }

    protected String toList(GoaActionConfig cfg) throws DefaultException, Exception {
        HttpServletRequest request = cfg.getRequest();
        IsDoTypesForm form = (IsDoTypesForm) cfg.getBaseForm();
        String paraFlag = form.getParaFlag();
        String nodeid = form.getNodeid();
        if (nodeid.equals("null")) {
            nodeid = "";
            form.setNodeid(nodeid);
        }
        String paraSimpleQuery = ((IsDoTypesForm) cfg.getBaseForm()).getParaSimpleQuery();
        paraSimpleQuery = StringUtils.replaceText(paraSimpleQuery);
        if (!"".equals(paraFlag)) {
            form.setFmoduleflag(paraFlag);
            form.setParaSimpleQuery("");
            if (!StringUtils.isNull(nodeid)) {
                form.setSqlWhere(" and is_dotype.moduleflag= '" + paraFlag + "'" + " and is_dotype.parent_type='" + nodeid + "'");
            } else {
                form.setSqlWhere(" and is_dotype.moduleflag= '" + paraFlag + "'" + " and (is_dotype.parent_type='' or is_dotype.parent_type is null) ");
            }
        } else {
            if (paraSimpleQuery == null) {
                paraSimpleQuery = "";
            }
            form.setSqlWhere(" AND IS_DOTYPE.INFO_TYPE LIKE '%" + paraSimpleQuery + "%'");
        }
        return super.toList(cfg);
    }

    protected String toEdit(GoaActionConfig cfg) throws DefaultException, Exception {
        IsDoTypesForm form = (IsDoTypesForm) cfg.getBaseForm();
        String forword = super.toEdit(cfg);
        IsDoTypeVO vo = (IsDoTypeVO) form.getVo();
        String fmoduleflag = vo.getModuleflag();
        String parenttype = vo.getParent_type();
        if ("".equals(parenttype)) {
            vo.setParent_type(form.getNodeid());
        }
        if ("".equals(fmoduleflag)) {
            vo.setModuleflag(form.getParaFlag());
        }
        return forword;
    }

    protected String doInsert(GoaActionConfig cfg, boolean isBack) throws DefaultException, Exception {
        IsDoTypesForm form = (IsDoTypesForm) cfg.getBaseForm();
        IsDoTypeVO oldvo = (IsDoTypeVO) form.getVo();
        String forword = null;
        HttpServletRequest request = cfg.getRequest();
        if (!isBack) {
            IsDoTypeVO vo = (IsDoTypeVO) form.getVo();
            vo.setModuleflag(oldvo.getModuleflag());
            vo.setParent_type(oldvo.getParent_type());
            vo.setTypelevel(oldvo.getTypelevel());
        }
        String parenttype = oldvo.getParent_type();
        String paraFlag = oldvo.getModuleflag();
        log.showLog("paraFlag-----------------" + paraFlag);
        cfg.getSession().setAttribute("operate", "insert");
        if (parenttype.equals("")) {
            paraFlag = String.valueOf(Integer.parseInt(paraFlag) + 1);
            cfg.getSession().setAttribute("nodeid", paraFlag);
        } else {
            cfg.getSession().setAttribute("nodeid", parenttype);
        }
        try {
            forword = super.doInsert(cfg, isBack);
        } catch (DefaultException e) {
            if (e.getMessage().equals("PK repeat!")) {
                request.setAttribute("ErrorMsg", "�������Ѵ���,���������룡");
                return FORWORD_EDIT_PAGE;
            } else {
                throw e;
            }
        }
        return forword;
    }

    public String doTreeData(GoaActionConfig cfg) throws DefaultException, Exception {
        String paraFlag = ((IsDoTypesForm) cfg.getBaseForm()).getParaFlag();
        String nodeid = StringUtils.nullToString(cfg.getRequest().getParameter("nodeid"));
        cfg.getRequest().setAttribute("treeData", isDoTypeDao.getTreeData(paraFlag, nodeid));
        return TREE_PAGE;
    }

    public String doStaticTreeData(GoaActionConfig cfg) throws DefaultException, Exception {
        HttpSession session = cfg.getSession();
        List statictreeData = new ArrayList();
        if (PermissionUtil.checkPermissionByCode(session, "VIEW", "GGXX")) {
            BaseTreeVO staticVO1 = new BaseTreeVO();
            staticVO1.setNodeid("1");
            staticVO1.setNodename("��Դ����");
            staticVO1.setAction("/infoservice/typelist.do?paraFlag=0&#38;nodeid=null");
            staticVO1.setIsfolder("1");
            staticVO1.setSrc("/infoservice/typetree.do?action=doTreeData&#38;paraFlag=0");
            statictreeData.add(staticVO1);
        }
        if (PermissionUtil.checkPermissionByCode(session, "VIEW", "ZCFG")) {
            BaseTreeVO staticVO2 = new BaseTreeVO();
            staticVO2.setNodeid("2");
            staticVO2.setAction("/infoservice/typelist.do?paraFlag=1&#38;nodeid=null");
            staticVO2.setNodename("���߷���");
            staticVO2.setIsfolder("1");
            staticVO2.setSrc("/infoservice/typetree.do?action=doTreeData&#38;paraFlag=1");
            statictreeData.add(staticVO2);
        }
        if (PermissionUtil.checkPermissionByCode(session, "VIEW", "ZZXX")) {
            BaseTreeVO staticVO3 = new BaseTreeVO();
            staticVO3.setNodeid("3");
            staticVO3.setAction("/infoservice/typelist.do?paraFlag=2&#38;nodeid=null");
            staticVO3.setNodename("��˾��̬");
            staticVO3.setIsfolder("1");
            staticVO3.setSrc("/infoservice/typetree.do?action=doTreeData&#38;paraFlag=2");
            statictreeData.add(staticVO3);
        }
        if (PermissionUtil.checkPermissionByCode(session, "VIEW", "BMXX")) {
            BaseTreeVO staticVO4 = new BaseTreeVO();
            staticVO4.setNodeid("4");
            staticVO4.setAction("/infoservice/typelist.do?paraFlag=3&#38;nodeid=null");
            staticVO4.setNodename("������Ϣ");
            staticVO4.setIsfolder("1");
            staticVO4.setSrc("/infoservice/typetree.do?action=doTreeData&#38;paraFlag=3");
            statictreeData.add(staticVO4);
        }
        if (PermissionUtil.checkPermissionByCode(session, "VIEW", "BGGL")) {
            BaseTreeVO staticVO5 = new BaseTreeVO();
            staticVO5.setNodeid("5");
            staticVO5.setAction("/infoservice/typelist.do?paraFlag=4&#38;nodeid=null");
            staticVO5.setNodename("�������");
            staticVO5.setIsfolder("1");
            staticVO5.setSrc("/infoservice/typetree.do?action=doTreeData&#38;paraFlag=4");
            statictreeData.add(staticVO5);
        }
        if (PermissionUtil.checkPermissionByCode(session, "VIEW", "HYXW")) {
            BaseTreeVO staticVO6 = new BaseTreeVO();
            staticVO6.setNodeid("6");
            staticVO6.setAction("/infoservice/typelist.do?paraFlag=5&#38;nodeid=null");
            staticVO6.setNodename("��ҵ����");
            staticVO6.setIsfolder("1");
            staticVO6.setSrc("/infoservice/typetree.do?action=doTreeData&#38;paraFlag=5");
            statictreeData.add(staticVO6);
        }
        if (PermissionUtil.checkPermissionByCode(session, "VIEW", "GLZD")) {
            BaseTreeVO staticVO7 = new BaseTreeVO();
            staticVO7.setNodeid("7");
            staticVO7.setAction("/infoservice/typelist.do?paraFlag=6&#38;nodeid=null");
            staticVO7.setNodename("�����ƶ�");
            staticVO7.setIsfolder("1");
            staticVO7.setSrc("/infoservice/typetree.do?action=doTreeData&#38;paraFlag=6");
            statictreeData.add(staticVO7);
        }
        if (PermissionUtil.checkPermissionByCode(session, "VIEW", "WDZL")) {
            BaseTreeVO staticVO8 = new BaseTreeVO();
            staticVO8.setNodeid("8");
            staticVO8.setAction("/infoservice/typelist.do?paraFlag=7&#38;nodeid=null");
            staticVO8.setNodename("�ĵ�����");
            staticVO8.setIsfolder("1");
            staticVO8.setSrc("/infoservice/typetree.do?action=doTreeData&#38;paraFlag=7");
            statictreeData.add(staticVO8);
        }
        cfg.getRequest().setAttribute("treeData", statictreeData);
        return TREE_PAGE;
    }

    protected String doUpdate(GoaActionConfig cfg, boolean isBack) throws DefaultException, Exception {
        IsDoTypesForm form = (IsDoTypesForm) cfg.getBaseForm();
        IsDoTypeVO vo = (IsDoTypeVO) form.getVo();
        String sqlWhere = "";
        String forword = null;
        HttpServletRequest request = cfg.getRequest();
        String ptype = StringUtils.nullToString(cfg.getRequest().getParameter("info_type"));
        if (!"".equals(ptype)) {
            sqlWhere += ("and ptype='" + ptype + "'");
        }
        String parenttype = vo.getParent_type();
        String paraFlag = vo.getModuleflag();
        log.showLog("paraFlag-----------------" + paraFlag);
        cfg.getSession().setAttribute("operate", "update");
        if (parenttype.equals("")) {
            paraFlag = String.valueOf(Integer.parseInt(paraFlag) + 1);
            cfg.getSession().setAttribute("nodeid", paraFlag);
        } else {
            cfg.getSession().setAttribute("nodeid", parenttype);
        }
        try {
            forword = super.doUpdate(cfg, isBack);
        } catch (DefaultException e) {
            throw new DefaultException("�������Ѵ���,���������룡");
        }
        return forword;
    }

    public String doDelete(GoaActionConfig cfg) throws DefaultException, Exception {
        String forword = null;
        try {
            forword = super.doDelete(cfg);
        } catch (DefaultException e) {
            if (e.getMessage().equals("PK")) {
                cfg.getRequest().setAttribute("ErrorMsg", "��������Ѵ�����Ϣ������ɾ�������´��ڵ���Ϣ����ɾ������");
                return FORWORD_LIST_PAGE;
            }
        }
        IsDoTypesForm form = (IsDoTypesForm) cfg.getBaseForm();
        IsDoTypeVO vo = (IsDoTypeVO) form.getVo();
        String parenttype = vo.getParent_type();
        String paraFlag = form.getParaFlag();
        cfg.getSession().setAttribute("operate", "delete");
        if (parenttype.equals("")) {
            paraFlag = String.valueOf(Integer.parseInt(paraFlag) + 1);
            cfg.getSession().setAttribute("nodeid", paraFlag);
        } else {
            cfg.getSession().setAttribute("nodeid", parenttype);
        }
        return forword;
    }
}
