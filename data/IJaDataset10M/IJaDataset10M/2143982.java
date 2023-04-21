package org.javanuke.tests.model;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.javanuke.comments.CommentVo;
import org.javanuke.core.annotations.AnnotationValidator;
import org.javanuke.tests.utils.TestsConfig;
import org.javanuke.users.UserVo;
import org.javanuke.users.UsersManagerIntf;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class CommentsTest extends TestCase {

    private CommentVo comment = null;

    private Validator validator = null;

    private UsersManagerIntf usersManager = null;

    public CommentsTest(String arg0) {
        super(arg0);
        validator = (AnnotationValidator) TestsConfig.getBean("annotationValidator");
        usersManager = (UsersManagerIntf) TestsConfig.getBean("usersManager");
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        comment = new CommentVo();
        comment.setAuthor(null);
        comment.setContent("This is a comment!");
        comment.setTitle("This is a comment title!");
    }

    public void testValidComment() {
        BindException be = new BindException(comment, "Comment");
        ValidationUtils.invokeValidator(validator, comment, be);
        Assert.assertFalse(be.hasFieldErrors());
    }

    public void testInvalidComment() {
        comment.setAuthor(null);
        comment.setContent(null);
        BindException be = new BindException(comment, "Comment");
        ValidationUtils.invokeValidator(validator, comment, be);
        Assert.assertTrue(be.hasFieldErrors());
        Assert.assertNotNull(be.getFieldError("author"));
        Assert.assertNotNull(be.getFieldError("content"));
    }
}
