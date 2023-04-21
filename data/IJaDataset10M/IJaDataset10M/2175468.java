package lichen.render;

import java.util.Locale;
import org.apache.tapestry5.internal.services.PageTemplateLocator;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.model.ComponentModel;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.test.TapestryTestCase;
import org.testng.annotations.Test;

/**
 * @author jcai
 * @version $Revision: 225 $
 * @since 0.0.3
 */
public class ContextRootTemplateLocatorTest extends TapestryTestCase {

    @Test
    public void not_a_page_class() {
        ComponentModel model = mockComponentModel();
        Resource root = mockResource();
        ComponentClassResolver resolver = mockComponentClassResolver();
        train_getComponentClassName(model, "foo.bar.Baz");
        replay();
        PageTemplateLocator locator = new ContextRootTemplateLocator(root, resolver);
        assertNull(locator.findPageTemplateResource(model, Locale.FRENCH));
        verify();
    }

    @Test
    public void template_found() {
        ComponentModel model = mockComponentModel();
        Resource root = mockResource();
        Resource withExtension = mockResource();
        Resource forLocale = mockResource();
        Locale locale = Locale.FRENCH;
        String className = "myapp.pages.Foo";
        ComponentClassResolver resolver = mockComponentClassResolver();
        train_getComponentClassName(model, className);
        train_resolvePageClassNameToPageName(resolver, className, "Foo");
        train_forFile(root, "Foo.html", withExtension);
        train_forLocale(withExtension, locale, forLocale);
        replay();
        PageTemplateLocator locator = new ContextRootTemplateLocator(root, resolver);
        assertSame(locator.findPageTemplateResource(model, locale), forLocale);
        verify();
    }

    /**
	 * Because of how Tapestry maps class names to logical page names, part of
	 * the name may be have been stripped off and we want to make sure we get it
	 * back.
	 */
    @Test
    public void uses_simple_class_name_in_folders() {
        ComponentModel model = mockComponentModel();
        Resource root = mockResource();
        Resource withExtension = mockResource();
        Resource forLocale = mockResource();
        Locale locale = Locale.FRENCH;
        String className = "myapp.pages.foo.CreateFoo";
        ComponentClassResolver resolver = mockComponentClassResolver();
        train_getComponentClassName(model, className);
        train_resolvePageClassNameToPageName(resolver, className, "foo/Create");
        train_forFile(root, "foo/CreateFoo.html", withExtension);
        train_forLocale(withExtension, locale, forLocale);
        replay();
        PageTemplateLocator locator = new ContextRootTemplateLocator(root, resolver);
        assertSame(locator.findPageTemplateResource(model, locale), forLocale);
        verify();
    }

    @Test
    public void template_not_found() {
        ComponentModel model = mockComponentModel();
        Resource root = mockResource();
        Resource withExtension = mockResource();
        Locale locale = Locale.GERMAN;
        String className = "myapp.pages.bar.Baz";
        ComponentClassResolver resolver = mockComponentClassResolver();
        train_getComponentClassName(model, className);
        train_resolvePageClassNameToPageName(resolver, className, "bar/Baz");
        train_forFile(root, "bar/Baz.html", withExtension);
        train_forLocale(withExtension, locale, null);
        replay();
        PageTemplateLocator locator = new ContextRootTemplateLocator(root, resolver);
        assertNull(locator.findPageTemplateResource(model, locale));
        verify();
    }

    protected final void train_resolvePageClassNameToPageName(ComponentClassResolver resolver, String pageClassName, String pageName) {
        expect(resolver.resolvePageClassNameToPageName(pageClassName)).andReturn(pageName);
    }
}
