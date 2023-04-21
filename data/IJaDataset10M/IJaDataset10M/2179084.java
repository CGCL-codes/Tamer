package info.javafans.cdn.web.action;

import info.javafans.cdn.dao.MemberDao;
import info.javafans.cdn.domain.Member;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Project: CollegeDatingNetwork <br />
 * ClassName: RegisterAction <br />
 * Description:  <br />
 */
public class RegisterAction extends Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String repassword = request.getParameter("repassword");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        System.out.println("info:" + username + password + repassword + phone + email);
        if (username != null && password != null && repassword != null && phone != null && email != null) {
            if (password.equals(repassword)) {
                System.out.println("yes");
                MemberDao.doRegister(username, password, phone, email);
                Member member = new Member();
                member.setName(username);
                member.setPassword(repassword);
                member.setEmail(email);
                member.setPhone(phone);
                request.setAttribute("member", member);
                return "success";
            } else {
                request.setAttribute("error", "两次输入的密码不一致");
                return "error";
            }
        } else {
            request.setAttribute("error", "不允许留空");
            return "error";
        }
    }
}
