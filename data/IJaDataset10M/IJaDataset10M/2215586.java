package com.plato.etoh.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.plato.etoh.client.model.MonthlyStat;
import com.plato.etoh.server.model.Application;

public class UpdateCurrentMonthTotalCronServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        try {
            response.setContentType("text/html;charset=UTF-8");
            long start = System.currentTimeMillis();
            PersistenceManager pm = PMF.get().getPersistenceManager();
            String queryString = "select from " + MonthlyStat.class.getName();
            Query query = pm.newQuery(queryString);
            String filterString = "";
            Date date = new Date();
            filterString += "month == " + date.getMonth() + " && ";
            filterString += "year == " + date.getYear() + "";
            query.setFilter(filterString);
            List<Application> greetings = null;
            int resultCount = 0;
            int size = 0;
            try {
                date.setDate(1);
                Query q = pm.newQuery("select from " + Application.class.getName());
                q.setFilter("basvuruTarihi >= tarih");
                q.declareImports("import java.util.Date");
                q.declareParameters("Date tarih");
                greetings = (List<Application>) q.execute(date);
                for (Iterator iterator = greetings.iterator(); iterator.hasNext(); ) {
                    Application application = (Application) iterator.next();
                    Date basvuruTarihi = application.getBasvuruTarihi();
                    if (basvuruTarihi.getMonth() == date.getMonth() && basvuruTarihi.getYear() == date.getYear()) {
                        resultCount++;
                    }
                }
                long end = System.currentTimeMillis();
                List<MonthlyStat> results = (List<MonthlyStat>) query.execute();
                if (results.size() == 0) {
                    MonthlyStat m = new MonthlyStat();
                    m.setHowMany(String.valueOf(resultCount));
                    m.setMonth(date.getMonth());
                    m.setYear(date.getYear());
                    pm.makePersistent(m);
                } else {
                    MonthlyStat monthlyStat = results.get(0);
                    monthlyStat.setHowMany(String.valueOf(resultCount));
                }
            } finally {
                pm.close();
            }
            long end = System.currentTimeMillis();
            out.println("Execution time in getBareApplications was " + (end - start) + " ms.");
        } finally {
            out.close();
        }
    }
}
