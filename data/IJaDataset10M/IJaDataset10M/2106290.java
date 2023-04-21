package cn.shining365.webclips.clipper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import cn.shining365.webclips.clipper.serializer.Serializer;

/**
 * 注意：这个servlet不经登录就可以访问到。必须保证它所支持的所有操作都是只读的。
 * */
public class AllClipperServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static Logger logger = Logger.getLogger(AllClipperServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Clipper> clippers = ClipperDao.getInstance().findAll();
        Serializer htmlSerializer = Serializer.getHtmlTableSerializer(false, null, null);
        StringBuffer sb = new StringBuffer();
        String prefix = request.getParameter("prefix");
        if (prefix == null) {
            prefix = "";
        } else {
            prefix = prefix.trim();
        }
        for (Clipper clipper : clippers) {
            if (!clipper.getName().trim().startsWith(prefix)) {
                continue;
            }
            try {
                sb.append("<h3>").append(clipper.getName()).append("</h3>").append(new ClipperRunner(clipper).extract((Map<String, String>) null, htmlSerializer)).append("<br/>\n");
                logger.info("Finished: " + clipper.getName());
            } catch (DocumentException e) {
                logger.warn("", e);
            }
        }
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().print(sb);
    }
}
