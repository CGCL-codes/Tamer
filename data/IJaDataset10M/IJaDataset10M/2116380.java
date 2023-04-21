package org.corrib.s3b.recommendations.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.corrib.s3b.recommendations.RecommendationUtils;
import org.corrib.s3b.recommendations.UserRecommendationPrefs;
import org.corrib.s3b.recommendations.types.RecommendationName;
import org.foafrealm.manage.Person;
import org.foafrealm.tools.FoafManageHelper;

public class RecommendationsPrefsServlet extends HttpServlet {

    private static final long serialVersionUID = -6659512304629823134L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Person person = FoafManageHelper.getFMHInstance().getLoggedPerson(req);
        if (req.getParameter("changed") == null) {
            UserRecommendationPrefs[] urp = RecommendationUtils.getUserRecommendationPrefs(person.getMbox().toString());
            for (int i = 0; i < urp.length; i++) {
                req.setAttribute("chb_" + urp[i].getName().getAbbrName(), "checked");
                req.setAttribute("sel1_" + urp[i].getName().getAbbrName(), String.valueOf(urp[i].getLimit()));
                if (!urp[i].getName().getAbbrName().equals(RecommendationName.OVERALL.getAbbrName())) req.setAttribute("sel2_" + urp[i].getName().getAbbrName(), String.valueOf(urp[i].getWeight()));
            }
            req.getRequestDispatcher("profile/recommendationsPrefs.jsp").forward(req, resp);
        } else {
            RecommendationUtils.removeUserRecommendationPrefs(person.getMbox().toString());
            RecommendationName[] names = RecommendationName.values();
            for (int i = 0; i < names.length; i++) {
                if (req.getParameter("chb_" + names[i].getAbbrName()) != null) {
                    int limit = Integer.parseInt(req.getParameter("sel1_" + names[i].getAbbrName()));
                    int weight = 0;
                    if (!names[i].getAbbrName().equals(RecommendationName.OVERALL.getAbbrName())) weight = Integer.parseInt(req.getParameter("sel2_" + names[i].getAbbrName()));
                    UserRecommendationPrefs urp = new UserRecommendationPrefs(names[i], limit, weight);
                    urp.saveUserRecommendationPrefs(person.getMbox().toString());
                }
            }
            resp.sendRedirect(((req.getContextPath() != null) ? req.getContextPath() : "") + "/profile/");
        }
    }
}
