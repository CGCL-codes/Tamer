package org.sergioveloso.spott.command.testsuiteitem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import org.hibernate.Query;
import org.sergioveloso.spott.command.Command;
import org.sergioveloso.spott.model.entity.*;
import org.sergioveloso.spott.model.db.*;
import org.sergioveloso.spott.config.DispatchProperties;

public class ListTestSuiteItemCategoryCommand extends Command {

    @SuppressWarnings("unchecked")
    @Override
    public int execute(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        try {
            Query q = HibernateUtil.getSession().createQuery("from TestSuiteItemCategory order by categoryName");
            List<TestSuiteItemCategory> cats = (List<TestSuiteItemCategory>) q.list();
            req.setAttribute("categoryList", cats);
        } catch (Throwable t) {
            log("ERROR: " + t.getMessage());
            throw new ServletException("ERROR", t);
        }
        return DispatchProperties.RET_TSI_CATEGORIES;
    }
}
