package codebush.web.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import codebush.domain.Article;
import codebush.service.Rss;

/**
 * 
 * @author Fution Bai
 * @since 1.0
 */
@Controller
public class RssController {

    private final Rss rss;

    @Autowired
    public RssController(Rss rss) {
        this.rss = rss;
    }

    /**
	 * index request for rss
	 * 
	 * @return
	 */
    @RequestMapping("/rss")
    public ModelAndView rss(@RequestParam(value = "uid") String userId, HttpServletResponse response) {
        ModelAndView mav = new ModelAndView();
        List<Article> list = (List<Article>) this.rss.rss(Integer.parseInt(userId));
        mav.addObject("articles", list);
        mav.setViewName("/support/rss");
        response.setContentType("application/xml");
        return mav;
    }
}
