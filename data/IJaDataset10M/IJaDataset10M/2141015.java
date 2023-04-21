package action;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import manage.ManagePermissionCode;
import manage.ManageTeacher;
import manage.ManageUser;
import model.PermissionCode;
import model.Teacher;
import model.User;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

/**
 *
 * @author dani
 */
public class TeacherAction extends DispatchAction {

    ManageTeacher manageTeacher = new ManageTeacher();

    ManageUser manageUser = new ManageUser();

    ManagePermissionCode managePermissionCode = new ManagePermissionCode();

    String forward = null;

    public ActionForward registerTeacher(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            String name = request.getParameter("name");
            String login = request.getParameter("login");
            String password = request.getParameter("password");
            String address = request.getParameter("address");
            String tel = request.getParameter("tel");
            String email = request.getParameter("email");
            String cpf = request.getParameter("cpf");
            String subject = request.getParameter("subject");
            String code = request.getParameter("code");
            User studentVeri = manageUser.getByLogin(login);
            PermissionCode codeVeri = managePermissionCode.getByCode(code);
            if (studentVeri != null) {
                request.setAttribute("loginExists", true);
            } else {
                if (codeVeri == null) {
                    request.setAttribute("grupoInvalido", true);
                } else {
                    Teacher teacher = new Teacher(name, login, password, address, email, tel, cpf, subject);
                    manageTeacher.save(teacher);
                    PermissionCode permissionCode = managePermissionCode.getByCode(code);
                    String role = permissionCode.getRole();
                    manageUser.addRole(login, role);
                }
            }
            forward = "success";
        } catch (IllegalArgumentException ex) {
            forward = "error";
        }
        return mapping.findForward(forward);
    }

    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String tel = request.getParameter("tel");
        String email = request.getParameter("email");
        String cpf = request.getParameter("cpf");
        String subject = request.getParameter("subject");
        Long id = Long.parseLong(request.getParameter("userId"));
        Teacher teacher = manageTeacher.getById(id);
        teacher.setName(name);
        teacher.setAddress(address);
        teacher.setTelephone(tel);
        teacher.setEmail(email);
        teacher.setCpf(cpf);
        teacher.setSubject(subject);
        manageTeacher.update(teacher);
        return mapping.findForward("home");
    }

    public ActionForward listAll(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Teacher> teacher = manageTeacher.getAll();
        request.setAttribute("users", teacher);
        return mapping.findForward("listAll");
    }

    public ActionForward seeMyProfile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String remoteUser = request.getRemoteUser();
        Teacher teacher = manageTeacher.getByLogin(remoteUser);
        request.setAttribute("user", teacher);
        return mapping.findForward("seeProfile");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long id = Long.parseLong(request.getParameter("userId"));
        Teacher teacher = manageTeacher.getById(id);
        manageTeacher.delete(teacher);
        return mapping.findForward("home");
    }

    public ActionForward getByLogin(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String login = request.getParameter("login");
        Teacher teacher = manageTeacher.getByLogin(login);
        request.setAttribute("teacher", teacher);
        return mapping.findForward("teacherProfile");
    }
}
