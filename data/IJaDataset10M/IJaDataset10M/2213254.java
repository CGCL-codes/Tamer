package org.javanuke.tests.controller;

import java.util.Date;
import junit.framework.TestCase;
import org.javanuke.articles.ArticleVo;
import org.javanuke.core.annotations.AnnotationValidator;
import org.javanuke.downloads.DownloadVo;
import org.javanuke.feedback.FeedbackVo;
import org.javanuke.news.NewsVo;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import com.jnuke.guestbook.GuestbookVo;

public class AnnotationValidatorTest extends TestCase {

    public void testSupports() {
        AnnotationValidator v = new AnnotationValidator();
        assertTrue(v.supports(NewsVo.class));
        assertTrue(v.supports(ArticleVo.class));
        assertTrue(v.supports(GuestbookVo.class));
        assertTrue(v.supports(DownloadVo.class));
        assertTrue(v.supports(FeedbackVo.class));
    }

    public void testValidate() {
        AnnotationValidator v = new AnnotationValidator();
        ArticleVo article = new ArticleVo();
        Errors errors = new BindException(article, "article");
        v.validate(article, errors);
        assertTrue(errors.hasFieldErrors());
        System.out.println(errors);
    }
}
