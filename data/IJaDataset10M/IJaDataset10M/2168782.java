package org.nexopenframework.context.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import org.jets3t.service.model.S3Object;
import org.junit.Test;
import org.nexopenframework.context.framework.Contexts;
import org.nexopenframework.context.framework.Scope;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Easy Test case for {@link ContextsNamespaceHandler} and derived strategies for support
 * of <code>Amazons3Context</code></p>
 * 
 * @see org.nexopenframework.context.config.ContextsNamespaceHandler
 * @see org.nexopenframework.context.framework.Contexts
 * @author Francesc Xavier Magdaleno
 * @version $Revision $,$Date: 2009-06-20 18:18:44 +0100 $ 
 * @since 2.0.0.GA
 */
@ContextConfiguration(locations = "/openfrwk-modules-amazons3-context.xml")
public class ContextsNamespaceHandlerAmazonS3Test extends AbstractJUnit4SpringContextTests {

    @Test
    public void existenceOfContexts() {
        assertNotNull(applicationContext.getBean("openfrwk.context.contexts_manager"));
        assertTrue(Contexts.isInitialized());
    }

    @Test
    public void existenceOfAmazonS3Contexts() {
        assertEquals(2, Contexts.getContexts(Scope.APPLICATION_AWS_S3).length);
    }

    @Test
    public void processContextsApplicationAmazonS3() {
        Contexts.setAttribute("key.test", "value", Scope.APPLICATION_AWS_S3);
        assertNotNull(Contexts.getAttribute("key.test", Scope.APPLICATION_AWS_S3));
        assertTrue(Contexts.getAttribute("key.test", Scope.APPLICATION_AWS_S3) instanceof S3Object);
    }

    @Test
    public void processContextsApplicationAmazonS3SpecificBucket() throws NoSuchAlgorithmException, IOException {
        final S3Object s3Object = new S3Object("key.test0", "value");
        s3Object.setBucketName("test1");
        Contexts.setAttribute("key.test0", s3Object, Scope.APPLICATION_AWS_S3);
        assertNotNull(Contexts.getAttribute("key.test0", Scope.APPLICATION_AWS_S3));
        assertTrue(Contexts.getAttribute("key.test0", Scope.APPLICATION_AWS_S3) instanceof S3Object);
    }
}
