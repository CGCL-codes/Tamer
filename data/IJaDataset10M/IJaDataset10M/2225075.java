package wendyeq.iweb.blog.service;

import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wendyeq.iweb.blog.dao.TagsDAO;
import wendyeq.iweb.blog.pojo.Category;
import wendyeq.iweb.blog.pojo.Tags;

@Service
public class TagsService {

    private static final Logger logger = Logger.getLogger(TagsService.class);

    @Autowired
    private TagsDAO tagsDAO;

    @Autowired
    private ArticleService articleService;

    public void insert(String tag) {
        if (this.tagsDAO.findByTag(tag) == null) {
            this.tagsDAO.saveOrUpdate(new Tags(tag));
            logger.warn("TagsService -- do insert!");
        } else {
            logger.warn("TagsService -- do insert false!");
        }
    }

    public void update(String oldName, String newName) {
        this.articleService.updateTags(oldName, newName);
        Tags tag = this.tagsDAO.findByTag(oldName);
        tag.setName(newName);
        this.tagsDAO.saveOrUpdate(tag);
    }

    public void delete(String id) {
        Tags tag = this.tagsDAO.findById(id);
        Set<String> aids = tag.getArticle();
        for (String aid : aids) {
            this.articleService.updateByTags(aid, tag.getName());
        }
        this.tagsDAO.delete(id);
    }

    public List<Tags> findAll() {
        return this.tagsDAO.findAll();
    }

    public Tags findByTag(String t) {
        return this.tagsDAO.findByTag(t);
    }
}
