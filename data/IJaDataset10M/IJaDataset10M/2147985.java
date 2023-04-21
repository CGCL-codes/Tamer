package org.jazzteam.snipple.action.article;

import java.util.ArrayList;
import java.util.List;
import org.jazzteam.snipple.action.Action;
import org.jazzteam.snipple.data.ArticleDataService;
import org.jazzteam.snipple.data.HierarchyDataService;
import org.jazzteam.snipple.data.TagDataService;
import org.jazzteam.snipple.model.Article;
import org.jazzteam.snipple.model.Hierarchy;
import org.jazzteam.snipple.model.Tag;
import org.jazzteam.snipple.model.TreeBuilder;
import org.jazzteam.snipple.model.User;
import com.jgeppert.struts2.jquery.tree.result.TreeNode;
import com.opensymphony.xwork2.ActionContext;

public class CreateArticleAction extends Action {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5384560298240037793L;

    private String title;

    private String tags;

    private String content;

    private ArticleDataService articleDataService;

    private TagDataService tagDataService;

    private TreeNode tree;

    private HierarchyDataService hierarchyDataService;

    private List<Hierarchy> hierarchies = new ArrayList<Hierarchy>();

    public CreateArticleAction(ArticleDataService articleDataService, TagDataService tagDataService, HierarchyDataService hierarchyDataService) {
        this.articleDataService = articleDataService;
        this.tagDataService = tagDataService;
        this.hierarchyDataService = hierarchyDataService;
    }

    public String index() {
        tree = new TreeBuilder().buidTreeNodeByHierarchies(hierarchyDataService.findAll());
        return SUCCESS;
    }

    public String create() {
        User user = (User) ActionContext.getContext().getSession().get("user");
        List<Tag> tagz = new ArrayList<Tag>();
        String[] stringTags = tags.split(" ");
        for (String name : stringTags) {
            if (!(name.trim().length() == 0)) {
                Tag tag = tagDataService.findByName(name);
                boolean exist = false;
                for (Tag t : tagz) {
                    if (t != null && t.getName().equalsIgnoreCase(name)) {
                        exist = true;
                        break;
                    }
                }
                if (tag == null && !exist) {
                    tagz.add(new Tag(name));
                } else {
                    tagz.add(tag);
                }
            }
        }
        Article article = new Article(title, content, user, tagz);
        for (Tag t : tagz) {
            if (t != null) tagDataService.save(t);
        }
        articleDataService.save(article);
        return SUCCESS;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TreeNode getTree() {
        return tree;
    }

    public void setTree(TreeNode tree) {
        this.tree = tree;
    }

    public HierarchyDataService getHierarchyDataService() {
        return hierarchyDataService;
    }

    public void setHierarchyDataService(HierarchyDataService hierarchyDataService) {
        this.hierarchyDataService = hierarchyDataService;
    }

    public List<Hierarchy> getHierarchies() {
        return hierarchies;
    }

    public void setHierarchies(List<Hierarchy> hierarchies) {
        this.hierarchies = hierarchies;
    }
}
