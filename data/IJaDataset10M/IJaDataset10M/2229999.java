package cn.sharezoo.struts.action.usermanager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.struts.ActionSupport;
import cn.sharezoo.domain.Student;
import cn.sharezoo.service.UserService;
import cn.sharezoo.utils.WrapperStudentInfoToRtf;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.PageSize;
import com.lowagie.text.rtf.RtfWriter2;

public class StudentInfoExport extends ActionSupport {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        WebApplicationContext ctx = getWebApplicationContext();
        UserService userService = (UserService) ctx.getBean("userService");
        Long id = new Long(0);
        if (StringUtils.isNumeric(request.getParameter("id"))) {
            id = new Long(request.getParameter("id"));
        }
        Student stu = (Student) userService.getUserById(id, "student");
        response.setContentType("text/rtf");
        response.setHeader("Content-disposition", "attachment; filename=" + stu.getEnglishName().replaceAll(" ", "_") + ".rtf");
        Document document = new Document();
        document.setPageSize(PageSize.A4);
        document.setMargins(40, 40, 40, 40);
        try {
            RtfWriter2.getInstance(document, response.getOutputStream());
            document.open();
            document.add(WrapperStudentInfoToRtf.WrapperStudentInfo(stu));
            document.close();
        } catch (DocumentException de) {
            de.printStackTrace();
            System.err.println("document: " + de.getMessage());
        }
        return null;
    }
}
