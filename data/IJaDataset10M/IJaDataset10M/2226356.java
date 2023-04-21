package ces.platform.system.ui.resource.action;

import java.io.IOException;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import ces.coral.log.Logger;
import ces.platform.system.common.Constant;
import ces.platform.system.common.IdGenerator;
import ces.platform.system.common.SelectList;
import ces.platform.system.common.Translate;
import ces.platform.system.dbaccess.Resource;
import ces.platform.system.ui.resource.form.AddResourceForm;

public class AddResourceAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String strForward = "";
        AddResourceForm objForm = (AddResourceForm) form;
        try {
            if (objForm.getActionFlag() != null && objForm.getActionFlag().equals("btnSave")) {
                Resource objRes = new Resource();
                int intId = (int) (IdGenerator.getInstance().getId(IdGenerator.GEN_ID_SYS_RESOURCE));
                logger.debug("intId == " + intId);
                logger.debug("resourceName == " + objForm.getResourceName());
                objRes.setId(intId);
                objRes.setName(Translate.translate(objForm.getResourceName(), Constant.PARAM_FORMBEAN));
                objRes.setOperateTypeID(objForm.getOperateType());
                objRes.setTypeID(objForm.getResourceType());
                objRes.setParentID(objForm.getParentId());
                objRes.setResourceID(Translate.translate(objForm.getResorceId(), Constant.PARAM_FORMBEAN));
                objRes.setLevelNum(objForm.getLevel() + 1);
                objRes.setRemark(Translate.translate(objForm.getRemark(), Constant.PARAM_FORMBEAN));
                objRes.setPath(objForm.getPath() + "_" + objForm.getResorceId());
                objRes.setPathName(Translate.translate(objForm.getPathName() + "/" + objForm.getResourceName(), Constant.PARAM_FORMBEAN));
                objRes.doNew();
                request.setAttribute("status", Constant.SUCCESS_TO_UPANDTREE);
                request.setAttribute("messageKey", "system.resource.addResource.alert.success");
                strForward = "alert";
            } else {
                String strParentId = request.getParameter("strId");
                String strLevel = request.getParameter("level");
                String strPath = request.getParameter("path");
                String strPathName = Translate.translate(request.getParameter("pathName"), Constant.PARAM_GET);
                logger.debug("AddResourceAction:strPathName == " + strPathName);
                if (strParentId != null && strParentId.trim().length() > 0) {
                    logger.debug("strId.length > 0");
                    Vector vcChildObj = null;
                    Resource objRes = new Resource(Integer.parseInt(strParentId));
                    vcChildObj = objRes.getNextLevel();
                    if (vcChildObj != null && vcChildObj.size() > 0) {
                        vcChildObj.remove(0);
                    }
                    request.setAttribute("childObj", vcChildObj);
                    logger.debug("vcChildObj.size() == " + vcChildObj.size());
                }
                objForm.setParentId(Integer.parseInt(strParentId.trim()));
                objForm.setPath(strPath);
                objForm.setPathName(strPathName);
                objForm.setLevel(Integer.parseInt(strLevel.trim()));
                setSelectList(objForm);
                Vector vResTypeID = objForm.getResourceValue();
                Vector vResTypeName = objForm.getResourceLabel();
                Vector vOpTypeID = objForm.getOperateValue();
                Vector vOpTypeName = objForm.getOperateLabel();
                request.setAttribute("vResTypeID", vResTypeID);
                request.setAttribute("vResTypeName", vResTypeName);
                request.setAttribute("vOpTypeID", vOpTypeID);
                request.setAttribute("vOpTypeName", vOpTypeName);
                strForward = "success";
            }
        } catch (Exception ex) {
            logger.error("AddResourceAction error:" + ex);
            if (objForm.getActionFlag() != null && objForm.getActionFlag().equals("btnSave")) {
                request.setAttribute("status", Constant.FAILED_TO_SELF);
                request.setAttribute("messageKey", "system.resource.addResource.alert.failed");
                strForward = "alert";
            } else {
                request.setAttribute("status", Constant.FAILED_TO_UP);
                request.setAttribute("messageKey", "system.resource.addResource.alert.newPageError");
                strForward = "alert";
            }
        }
        return (mapping.findForward(strForward));
    }

    /**
     * ����ҳ���������б�ֵ
     * @param objForm ҳ���Ӧ��formBean
     */
    public void setSelectList(AddResourceForm objForm) {
        SelectList objSel = SelectList.getResTypeLst();
        objForm.setResourceValue(objSel.getValues());
        objForm.setResourceLabel(objSel.getNames());
        objSel = SelectList.getOperateTypeLst();
        objForm.setOperateValue(objSel.getValues());
        objForm.setOperateLabel(objSel.getNames());
    }

    static Logger logger = new Logger(ces.platform.system.ui.resource.action.AddResourceAction.class);
}
