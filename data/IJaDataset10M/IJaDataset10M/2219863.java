package cn.ac.ntarl.umt.action.user;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import cn.ac.ntarl.umt.CLBException;
import cn.ac.ntarl.umt.action.AbstractAction;
import cn.ac.ntarl.umt.actions.profile.DBCreateProfile;
import cn.ac.ntarl.umt.actions.user.DBDeleteProfile;
import cn.ac.ntarl.umt.actions.user.DBEditProfile;
import cn.ac.ntarl.umt.actions.user.DBListProfile;
import cn.ac.ntarl.umt.database.AlreadyExist;
import cn.ac.ntarl.umt.database.Database;
import cn.ac.ntarl.umt.form.AddProfileForm;
import cn.ac.ntarl.umt.profile.define.AttributeInfo;
import cn.ac.ntarl.umt.security.Sessions;
import cn.ac.ntarl.umt.utils.MessageBean;

public class AddProfileAction extends AbstractAction {

    /**
	 * Method execute
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
    @Override
    public ActionForward exec(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, Sessions sm) {
        AddProfileForm addprofile = (AddProfileForm) form;
        if (Sessions.isLogin(request)) {
            if (!Sessions.IsCurrentPosition(request, 1, "�����Ա")) {
                return MessageBean.findMessageForward(request, mapping, "��ǰ�û�Ȩ�޲��㣬�޷���������������µ�¼��");
            }
        } else {
            return MessageBean.findMessageForward(request, mapping, "��ǰ�û�û�е�¼�����¼��");
        }
        try {
            if (addprofile.isInit()) {
                List profiles = (List) Database.perform(new DBListProfile());
                request.getSession().setAttribute("existprofile", profiles);
                log.info("init list profile successful!");
                return mapping.getInputForward();
            } else {
                String action = addprofile.getType();
                if (action != null && action.equals("addprofile")) {
                    String newname = addprofile.getNewProfileName();
                    if (newname == null || newname.equals("")) {
                        log.error("no profile input");
                        return MessageBean.findMessageForward(request, mapping, "û�������µ�Profile��ƣ����Profileʧ��");
                    }
                    ActionErrors errors = new ActionErrors();
                    try {
                        AttributeInfo p = new AttributeInfo();
                        p.setName(newname);
                        p.setDescription(addprofile.getNewProfileDescription());
                        p.setDatatype(addprofile.getEditProfileType());
                        Integer profileid = (Integer) Database.perform(new DBCreateProfile(p));
                        p.setProfileid(profileid.toString());
                        List profiles = (List) request.getSession().getAttribute("existprofile");
                        if (profiles == null) profiles = new ArrayList<AttributeInfo>();
                        profiles.add(p);
                        request.getSession().setAttribute("existprofile", profiles);
                        log.info("profile " + newname + " is created successful");
                        errors.add("username", new ActionError("info.createprofile"));
                        saveErrors(request, errors);
                        return mapping.getInputForward();
                    } catch (AlreadyExist e) {
                        log.warn("profile " + newname + " exists and cannot be created successful!");
                        errors.add("username", new ActionError("errors.createprofile"));
                        saveErrors(request, errors);
                        return mapping.getInputForward();
                    } catch (CLBException e) {
                        log.error("create profile " + newname + " fail with reason:" + e.getMessage());
                        return MessageBean.findExceptionForward(request, mapping, e);
                    }
                }
                if (action != null && action.equals("editprofile")) {
                    String editname = addprofile.getEditProfileName();
                    ActionErrors errors = new ActionErrors();
                    if (editname == null || editname.equals("")) {
                        errors.add("username", new ActionError("errors.editprofile"));
                        saveErrors(request, errors);
                        return mapping.getInputForward();
                    }
                    try {
                        AttributeInfo p = new AttributeInfo();
                        p.setName(editname);
                        p.setDescription(addprofile.getEditProfileDescription());
                        p.setProfileid(Integer.toString(addprofile.getEditProfileID()));
                        p.setDatatype(addprofile.getEditProfileType());
                        Database.perform(new DBEditProfile(p));
                        List profiles = (List) request.getSession().getAttribute("existprofile");
                        if (profiles != null && profiles.size() > 0) {
                            String pid = Integer.toString(addprofile.getEditProfileID());
                            for (int i = 0; i < profiles.size(); i++) {
                                AttributeInfo temp = (AttributeInfo) profiles.get(i);
                                if (temp.getProfileid().equals(pid)) {
                                    temp.setName(editname);
                                    temp.setDescription(addprofile.getEditProfileDescription());
                                    temp.setDatatype(addprofile.getEditProfileType());
                                    break;
                                }
                            }
                        }
                        request.getSession().setAttribute("existprofile", profiles);
                        log.info("profile " + editname + " is modified successful");
                        errors.add("username", new ActionError("info.editprofile"));
                        saveErrors(request, errors);
                        return mapping.getInputForward();
                    } catch (AlreadyExist e) {
                        log.warn("profile " + editname + " exists and cannot be created successful!");
                        errors.add("username", new ActionError("errors.createprofile"));
                        saveErrors(request, errors);
                        return mapping.getInputForward();
                    } catch (CLBException e) {
                        log.error("create profile " + editname + " fail with reason:" + e.getMessage());
                        return MessageBean.findExceptionForward(request, mapping, e);
                    }
                }
                if (action != null && action.equals("deleteprofile")) {
                    int profileid = addprofile.getEditProfileID();
                    ActionErrors errors = new ActionErrors();
                    if (profileid <= 0) {
                        errors.add("username", new ActionError("errors.deleteprofile"));
                        saveErrors(request, errors);
                        return mapping.getInputForward();
                    }
                    try {
                        AttributeInfo p = new AttributeInfo();
                        p.setProfileid(Integer.toString(addprofile.getEditProfileID()));
                        Database.perform(new DBDeleteProfile(p));
                        List profiles = (List) request.getSession().getAttribute("existprofile");
                        if (profiles != null && profiles.size() > 0) {
                            for (int i = 0; i < profiles.size(); i++) {
                                AttributeInfo temp = (AttributeInfo) profiles.get(i);
                                if (temp.getProfileid().equals(Integer.toString(addprofile.getEditProfileID()))) {
                                    profiles.remove(i);
                                    break;
                                }
                            }
                        }
                        request.getSession().setAttribute("existprofile", profiles);
                        log.info("profile is deleted successful");
                        errors.add("username", new ActionError("info.deleteprofile"));
                        saveErrors(request, errors);
                        return mapping.getInputForward();
                    } catch (AlreadyExist e) {
                        log.warn("profile can not be deleted as it's used");
                        errors.add("username", new ActionError("errors.profileused"));
                        saveErrors(request, errors);
                        return mapping.getInputForward();
                    } catch (CLBException e) {
                        log.error("delete profile " + profileid + " fail with reason:" + e.getMessage());
                        return MessageBean.findExceptionForward(request, mapping, e);
                    }
                }
                log.warn("unrecognized operation is fired!");
                return MessageBean.findMessageForward(request, mapping, "�޷�ʶ��Ĳ���");
            }
        } catch (CLBException e) {
            log.error("add profile fail with reason:" + e.getMessage());
            return MessageBean.findExceptionForward(request, mapping, e);
        }
    }

    private static Logger log;

    static {
        log = Logger.getLogger(AddProfileAction.class);
    }
}
