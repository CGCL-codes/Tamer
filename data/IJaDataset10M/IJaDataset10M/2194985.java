package fr.xebia.demo.wicket.blog.service;

import java.util.Date;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import fr.xebia.demo.wicket.blog.data.Category;
import fr.xebia.demo.wicket.blog.data.Post;

@Component
public class SampleDataInitializer {

    private static final Logger logger = Logger.getLogger(SampleDataInitializer.class);

    @Resource(name = "categoryService")
    private CategoryService categoryService;

    @Resource(name = "postService")
    private PostService postService;

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void setPostService(PostService postService) {
        this.postService = postService;
    }

    @PostConstruct
    public void initSampleData() {
        logger.debug("Creating sample data");
        Category category = createSampleCategory();
        Post post = createSamplePost(category);
        try {
            categoryService.save(category);
            postService.save(post);
            logger.debug("Sample category and post added");
        } catch (ServiceException e) {
            logger.error("Error storing defaults objects in database", e);
        }
    }

    private Post createSamplePost(Category category) {
        Post post = new Post();
        post.setTitle("Welcome to the Wicket Blog Demo");
        post.setAuthor("Manuel@Xebia");
        post.setCategory(category);
        post.setDate(new Date());
        post.setModified(new Date());
        post.setCommentsAllowed(true);
        post.setStatus(Post.STATUS_PUBLISHED);
        post.setContent("Voici une application demonstratrice du framework web appel� Wicket !!\n" + "Vous pouvez retrouver l'ensemble du code source � l'adresse suivante :\n" + "http://xebia-france.googlecode.com/svn/trunk/web/wicket-blog-demo/");
        return post;
    }

    private Category createSampleCategory() {
        Category category = new Category();
        category.setName("divers");
        category.setNicename("Divers");
        category.setDescription("Articles divers");
        return category;
    }
}
