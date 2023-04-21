package tenant;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import perInfo.UserCookie;
import hibernate.*;
import java.util.*;

public class VisitRequestController implements Controller {

    private VisitRequestDAO userVisit;

    private String nextPage;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String houseId = request.getParameter("houseId");
        String message = null;
        String userName = UserCookie.getUserCookie(request, "HLSSUSER_NAMECOOKIE");
        if (userName == null) {
            message = "���뿴��ʧ�ܣ����ȵ�¼";
            Map<String, String> model = new HashMap<String, String>();
            model.put("msg", message);
            model.put("title", "����ʧ��");
            model.put("url", "login.jsp");
            return new ModelAndView(getNextPage(), model);
        } else {
            if (userVisit.findById(userName + houseId) != null) {
                message = "�������뿴������ȴ�����Ӧ";
                Map<String, String> model = new HashMap<String, String>();
                model.put("msg", message);
                model.put("title", "�ظ�����");
                model.put("url", "front.jsp");
                return new ModelAndView(getNextPage(), model);
            } else {
                Session session = HibernateSessionFactory.getSession();
                session.beginTransaction();
                HouseInfo houseInfo = (HouseInfo) session.get(HouseInfo.class, houseId);
                if (userName.equals(houseInfo.getUserInfo().getUserName())) {
                    session.close();
                    message = "���뿴��ʧ�ܣ����������Լ��ķ���";
                    Map<String, String> model = new HashMap<String, String>();
                    model.put("msg", message);
                    model.put("title", "����ʧ��");
                    model.put("url", "front.jsp");
                    return new ModelAndView(getNextPage(), model);
                }
                VisitRequest visit = new VisitRequest();
                visit.setVisitId(userName + houseId);
                visit.setHouseInfo(houseInfo);
                UserInfo userInfo = (UserInfo) session.get(UserInfo.class, userName);
                visit.setUserInfo(userInfo);
                visit.setVisitState("����δȷ��");
                Date visitDate = new Date();
                SimpleDateFormat matter = new SimpleDateFormat("yyyy-MM-dd");
                matter.format(visitDate);
                visit.setVisitDate(visitDate);
                userVisit.save(visit);
                session.close();
                message = "���뿴���ɹ�����ȴ�����Ӧ";
                Map<String, String> model = new HashMap<String, String>();
                model.put("msg", message);
                model.put("title", "����ɹ�");
                model.put("url", "front.jsp");
                return new ModelAndView(getNextPage(), model);
            }
        }
    }

    public void setUserVisit(VisitRequestDAO userVisit) {
        this.userVisit = userVisit;
    }

    public VisitRequestDAO getUserVisit() {
        return userVisit;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public String getNextPage() {
        return nextPage;
    }
}
