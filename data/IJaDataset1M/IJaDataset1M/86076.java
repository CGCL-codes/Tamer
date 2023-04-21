package fr.xebia.demo.wicket.blog.view.admin.category;

import org.junit.BeforeClass;
import fr.xebia.demo.wicket.blog.service.CategoryService;
import fr.xebia.demo.wicket.blog.view.WicketPageTest;

public abstract class CategoryPageTest extends WicketPageTest {

    @BeforeClass
    public static void setUpAppContext() {
        CategoryService categoryService = new CategoryService();
        categoryService.setEntityManagerFactory(entityManagerFactory);
        appContext.putBean("categoryService", categoryService);
    }
}
