package uit.upis.action;

import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import uit.comm.Constants;
import uit.comm.util.BoardUtil;
import uit.comm.util.RequestUtil;
import uit.comm.util.StringUtil;
import uit.upis.manager.CommonManager;
import uit.upis.manager.UrbanManager;
import uit.upis.model.DefEntity;
import uit.upis.model.Gosi;
import uit.upis.model.UrbanResult;

public class UrbanAction extends BaseAction {

    private CommonManager commonManager;

    private UrbanManager urbanManager;

    /**
	 * @return Returns the commonManager.
	 */
    public CommonManager getCommonManager() {
        return commonManager;
    }

    /**
	 * @param commonManager The commonManager to set.
	 */
    public void setCommonManager(CommonManager commonManager) {
        this.commonManager = commonManager;
    }

    /**
	 * @return Returns the urbanManager.
	 */
    public UrbanManager getUrbanManager() {
        return urbanManager;
    }

    /**
	 * @param urbanManager The urbanManager to set.
	 */
    public void setUrbanManager(UrbanManager urbanManager) {
        this.urbanManager = urbanManager;
    }

    /**
	 * ���ð�ȹ�ü� �˻�
	 * setCharacterEncoding �߰� - 20071025
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ActionForward getSearch(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("UrbanAction getSearch()");
        request.setCharacterEncoding("utf-8");
        RequestUtil.showRequestParam(request);
        String pageIndex = StringUtil.nvl(request.getParameter("pageIndex"), "1");
        String upCode = StringUtil.nvl(request.getParameter("upCode"));
        String upName = StringUtil.nvl(request.getParameter("upName"));
        String entMain = StringUtil.nvl(request.getParameter("entMain"));
        String entMid = StringUtil.nvl(request.getParameter("entMid"));
        String entSub = StringUtil.nvl(request.getParameter("entSub"));
        String entDtl = StringUtil.nvl(request.getParameter("entDtl"));
        String searchValue = StringUtil.nvl(request.getParameter("searchValue"));
        try {
            DefEntity defEntity = new DefEntity();
            defEntity.setUpCode(upCode);
            defEntity.setUpName(upName);
            defEntity.setEntMain(entMain);
            defEntity.setEntMid(entMid);
            defEntity.setEntSub(entSub);
            defEntity.setEntDtl(entDtl);
            defEntity.setSearchValue(searchValue);
            defEntity.setStartIndex(BoardUtil.getStartIndex(pageIndex, Constants.SEARCH_LIST_PAGE_SIZE));
            defEntity.setEndIndex(BoardUtil.getEndIndex(pageIndex, Constants.SEARCH_LIST_PAGE_SIZE));
            DefEntity entity = commonManager.getDefEntityByClassify(defEntity);
            defEntity.setTblName(entity.getTblName());
            defEntity.setLyrName(entity.getLyrName() + "_DCN");
            defEntity.setCode(entity.getCode());
            log.debug("defEntity.getTblName()=" + defEntity.getTblName());
            log.debug("defEntity.getLyrName()=" + defEntity.getLyrName());
            log.debug("defEntity.getCode()=" + defEntity.getCode());
            HashMap hashMap = urbanManager.getSearch(defEntity);
            request.setAttribute("pageIndex", pageIndex);
            request.setAttribute("defEntity", defEntity);
            request.setAttribute("data", hashMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mapping.findForward("success");
    }

    /**
	 * �ü��������� ������ �̵�
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ActionForward getJoseoInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("UrbanAction getJoseoInfo");
        request.setCharacterEncoding("utf-8");
        RequestUtil.showRequestParam(request);
        request.setAttribute("objectId", StringUtil.nvl(request.getParameter("objectId")));
        request.setAttribute("code", StringUtil.nvl(request.getParameter("code")));
        request.setAttribute("cdName", StringUtil.nvl(request.getParameter("cdName")));
        request.setAttribute("upCode", StringUtil.nvl(request.getParameter("upCode")));
        request.setAttribute("tblName", StringUtil.nvl(request.getParameter("tblName")));
        request.setAttribute("lyrName", StringUtil.nvl(request.getParameter("lyrName")));
        request.setAttribute("dcnGId", StringUtil.nvl(request.getParameter("dcnGId")));
        request.setAttribute("ownGId", StringUtil.nvl(request.getParameter("ownGId")));
        request.setAttribute("dcnJId", StringUtil.nvl(request.getParameter("dcnJId")));
        request.setAttribute("ownJId", StringUtil.nvl(request.getParameter("ownJId")));
        request.setAttribute("grKey", StringUtil.nvl(request.getParameter("grKey")));
        if ("A".equals(StringUtil.nvl(request.getParameter("type")))) return mapping.findForward("all"); else return mapping.findForward("joseo");
    }

    /**
	 * �ü��������� �� ��ȸ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ActionForward getJoseo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("UrbanAction getJoseo");
        request.setCharacterEncoding("utf-8");
        RequestUtil.showRequestParam(request);
        String tblName = StringUtil.nvl(request.getParameter("tblName"));
        String dcnGId = StringUtil.nvl(request.getParameter("dcnGId"));
        String ownGId = StringUtil.nvl(request.getParameter("ownGId"));
        String dcnJId = StringUtil.nvl(request.getParameter("dcnJId"));
        String ownJId = StringUtil.nvl(request.getParameter("ownJId"));
        try {
            Gosi dcnGosi = urbanManager.getGosi(StringUtil.parseLong(dcnGId));
            Gosi ownGosi = urbanManager.getGosi(StringUtil.parseLong(ownGId));
            DefEntity defEntity = new DefEntity();
            defEntity.setObjectId(StringUtil.parseLong(dcnJId));
            defEntity.setTblName(tblName);
            UrbanResult joseo = urbanManager.getJoseo(defEntity);
            String[] str = StringUtil.getTokenArray(tblName, "_");
            if (str != null) joseo.setType(str[str.length - 1]);
            request.setAttribute("dcnGosi", dcnGosi);
            request.setAttribute("ownGosi", ownGosi);
            request.setAttribute("joseo", joseo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ("A".equals(StringUtil.nvl(request.getParameter("type")))) return mapping.findForward("all"); else return mapping.findForward("joseo");
    }

    /**
	 * �ü������̷����� ��ȸ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ActionForward getHistList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("UrbanAction getHistList");
        request.setCharacterEncoding("utf-8");
        RequestUtil.showRequestParam(request);
        String tblName = StringUtil.nvl(request.getParameter("tblName"));
        String grKey = StringUtil.nvl(request.getParameter("grKey"));
        try {
            List list = urbanManager.getHistList(tblName, grKey);
            request.setAttribute("list", list);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ("A".equals(StringUtil.nvl(request.getParameter("type")))) return mapping.findForward("all"); else return mapping.findForward("joseo");
    }

    /**
	 * �ü������̷����� �� ��ȸ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ActionForward getHistDetail(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("UrbanAction getHistDetail");
        request.setCharacterEncoding("utf-8");
        RequestUtil.showRequestParam(request);
        String tblName = StringUtil.nvl(request.getParameter("tblName"));
        long objectId = StringUtil.parseLong(StringUtil.nvl(request.getParameter("")));
        if ("A".equals(StringUtil.nvl(request.getParameter("type")))) return mapping.findForward("all"); else return mapping.findForward("joseo");
    }

    /**
	 * ������������ ��ȸ
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
    public ActionForward getEntryParcel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("UrbanAction getEntryParcel");
        request.setCharacterEncoding("utf-8");
        RequestUtil.showRequestParam(request);
        String objectId = StringUtil.nvl(request.getParameter("objectId"));
        String code = StringUtil.nvl(request.getParameter("code"));
        String cdName = StringUtil.nvl(request.getParameter("cdName"));
        String upCode = StringUtil.nvl(request.getParameter("upCode"));
        String tblName = StringUtil.nvl(request.getParameter("tblName"));
        String lyrName = StringUtil.nvl(request.getParameter("lyrName"));
        String dcnGId = StringUtil.nvl(request.getParameter("dcnGId"));
        String ownGId = StringUtil.nvl(request.getParameter("ownGId"));
        String dcnJId = StringUtil.nvl(request.getParameter("dcnJId"));
        String ownJId = StringUtil.nvl(request.getParameter("ownJId"));
        String grKey = StringUtil.nvl(request.getParameter("grKey"));
        List parcel = null;
        List parcellist = urbanManager.getEntryParcelList(code, dcnJId);
        request.setAttribute("parcel", parcel);
        request.setAttribute("parcellist", parcellist);
        if ("A".equals(StringUtil.nvl(request.getParameter("type")))) return mapping.findForward("all"); else return mapping.findForward("joseo");
    }
}
