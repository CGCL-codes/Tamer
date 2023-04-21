package fr.xebia.demo.wicket.blog.view.admin.post;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.util.tester.FormTester;
import org.junit.Test;
import fr.xebia.demo.wicket.blog.data.Post;
import fr.xebia.demo.wicket.blog.service.PostService;
import fr.xebia.demo.wicket.blog.service.ServiceException;

public class EditPostPageErrorTest extends ViewPostPageErrorTest {

    @Override
    protected PostService getPostService() {
        PostService postService = new PostService() {

            @Override
            public Post update(Post entity) throws ServiceException {
                throw new ServiceException(ERROR_MESSAGE);
            }
        };
        return postService;
    }

    @Override
    @Test
    public void testErrorRender() {
        tester.startPage(ListPostPage.class);
        tester.assertRenderedPage(ListPostPage.class);
        tester.assertNoErrorMessage();
        tester.assertComponent("posts:0:viewLink", Link.class);
        tester.clickLink("posts:0:viewLink");
        tester.assertRenderedPage(ViewPostPage.class);
        tester.assertComponent("editLink", Link.class);
        tester.clickLink("editLink");
        tester.assertRenderedPage(EditPostPage.class);
        tester.assertNoErrorMessage();
        FormTester form = tester.newFormTester("postForm");
        form.setValue("content", "Test category");
        form.submit();
        tester.assertErrorMessages(new String[] { ERROR_MESSAGE });
    }
}
