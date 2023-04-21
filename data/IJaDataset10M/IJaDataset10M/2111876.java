package ch.oois.web.speaker;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.math.NumberUtils;
import org.codehaus.jackson.map.ObjectMapper;
import ch.oois.infocore.ejb.InfoManagerBean;
import ch.oois.infocore.exception.IDNotFoundException;
import ch.oois.infocore.to.RaceMetaTO;

/**
 * CategoryData (sync): Retrieves all categories for a specific race.
 */
@WebServlet("/CategoryData")
public class CategoryDataServlet extends HttpServlet {

    private static final long serialVersionUID = 7715210710648502268L;

    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    @EJB
    private InfoManagerBean m_infoManagerBean;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("ISO-8859-1");
        Integer raceId = NumberUtils.toInt(request.getParameter("raceId"));
        RaceMetaTO raceMeta;
        Map<String, Integer> categoriesAndLegs = null;
        try {
            raceMeta = m_infoManagerBean.getRaceMetaInfo(raceId);
            categoriesAndLegs = raceMeta.getCategoriesAndLegs();
        } catch (IDNotFoundException e) {
            e.printStackTrace();
        }
        PrintWriter writer = response.getWriter();
        JSON_MAPPER.writeValue(writer, categoriesAndLegs);
        writer.flush();
        writer.close();
    }
}
