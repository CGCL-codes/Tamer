package xslt;

import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import com.yands.HTTPUtil;

public class HomeController extends AbstractController {

    static Logger log = Logger.getLogger(HomeController.class);

    public HomeController() {
        log.info("HomeController was initialised");
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String pageName = HTTPUtil.getRequestName(request);
        Map map = new HashMap();
        List wordList = new ArrayList();
        wordList.add("hello");
        wordList.add("world");
        map.put("wordList", wordList);
        return new ModelAndView("words", map);
    }
}
