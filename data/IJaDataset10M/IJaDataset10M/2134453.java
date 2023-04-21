package codebush.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import codebush.domain.Article;
import codebush.service.Tag;

/**
 * 
 * @author Fution Bai
 * @since 1.0
 */
@Controller
public class TagController {

    private final Tag tag;

    @Autowired
    public TagController(Tag tag) {
        this.tag = tag;
    }

    /**
	 * the index request for subject/tag/?ti=
	 * show all tags or show the specified tag's(ti=?) all topics 
	 * @return
	 */
    @RequestMapping("/tag")
    public ModelAndView index(@RequestParam(value = "ti", required = false) String tagId) {
        if (tagId != null) {
            return showTagTopics(Integer.parseInt(tagId));
        }
        List<String> tags = (List<String>) this.getTag().index();
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/tag");
        Map<String, List<String>> model = new HashMap<String, List<String>>();
        model.put("tags", tags);
        mav.addAllObjects(model);
        return mav;
    }

    /**
	 * show the specified tag's all topics(articles,) 
	 * @param name
	 * @return
	 */
    private ModelAndView showTagTopics(int tagId) {
        List<Article> list = (List<Article>) this.getTag().showTagArticles(tagId);
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/tag");
        Map<String, List<Article>> model = new HashMap<String, List<Article>>();
        model.put("tagArticles", list);
        mav.addAllObjects(model);
        return mav;
    }

    public Tag getTag() {
        return tag;
    }
}
