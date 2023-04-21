package net.sf.gilead.annotations;

import junit.framework.TestCase;
import net.sf.gilead.core.annotations.AnnotationsManager;
import net.sf.gilead.test.domain.annotated.Message;
import net.sf.gilead.test.domain.annotated.User;
import net.sf.gilead.test.domain.misc.Configuration;

/**
 * Test case for annotation manager.
 * @author bruno.marchesson
 *
 */
public class AnnotationsManagerTest extends TestCase {

    /**
	 * Test method for {@link net.sf.gilead.core.annotations.AnnotationsManager#hasServerOnlyAnnotations(java.lang.Class)}.
	 */
    public final void testHasServerOnlyAnnotations() {
        assertTrue(AnnotationsManager.hasServerOnlyAnnotations(Message.class));
        assertTrue(AnnotationsManager.hasServerOnlyAnnotations(User.class));
        assertFalse(AnnotationsManager.hasServerOnlyAnnotations(Configuration.class));
    }

    /**
	 * Test method for {@link net.sf.gilead.core.annotations.AnnotationsManager#isServerOnly(java.lang.Class, java.lang.String)}.
	 */
    public final void testIsServerOnly() {
        Message message = new Message();
        assertTrue(AnnotationsManager.isServerOnly(message, "version"));
        assertFalse(AnnotationsManager.isServerOnly(message, "author"));
        assertFalse(AnnotationsManager.isServerOnly(message, "doesNotExist"));
        User user = new User();
        assertTrue(AnnotationsManager.isServerOnly(user, "password"));
        assertFalse(AnnotationsManager.isServerOnly(user, "login"));
        assertFalse(AnnotationsManager.isServerOnly(user, "doesNotExist"));
    }

    /**
	 * Test method for {@link net.sf.gilead.core.annotations.AnnotationsManager#hasReadOnlyAnnotations(java.lang.Class)}.
	 */
    public final void testHasReadOnlyAnnotations() {
        assertTrue(AnnotationsManager.hasReadOnlyAnnotations(Message.class));
        assertTrue(AnnotationsManager.hasReadOnlyAnnotations(User.class));
        assertFalse(AnnotationsManager.hasReadOnlyAnnotations(Configuration.class));
    }

    /**
	 * Test method for {@link net.sf.gilead.core.annotations.AnnotationsManager#isReadOnly(java.lang.Class, java.lang.String)}.
	 */
    public final void testIsReadOnly() {
        Message message = new Message();
        assertFalse(AnnotationsManager.isReadOnly(message, "comment"));
        assertFalse(AnnotationsManager.isReadOnly(message, "message"));
        assertFalse(AnnotationsManager.isReadOnly(message, "doesNotExist"));
        User user = new User();
        assertTrue(AnnotationsManager.isReadOnly(user, "login"));
        assertFalse(AnnotationsManager.isReadOnly(user, "password"));
        assertFalse(AnnotationsManager.isReadOnly(user, "doesNotExist"));
    }

    /**
	 * Test access manager handling
	 */
    public final void testAccessManager() {
        TestAccessManager accessManager = new TestAccessManager();
        AnnotationsManager.setAccessManager(accessManager);
        accessManager.setRole(TestAccessManager.Role.user);
        Message message = new Message();
        assertTrue(AnnotationsManager.isReadOnly(message, "comment"));
        accessManager.setRole(TestAccessManager.Role.admin);
        assertFalse(AnnotationsManager.isReadOnly(message, "comment"));
    }
}
