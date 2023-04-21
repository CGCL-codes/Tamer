package org.extwind.osgi.tapestry;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author donf.yang
 * 
 */
public final class TapestryConstants {

    public static final Collection<String> TAPESTRY_PAGES_CLASSES = new ArrayList<String>(3);

    public static final Collection<String> TAPESTRY_COMPONENTS_CLASSES = new ArrayList<String>(44);

    public static final Collection<String> TAPESTRY_MIXINS_CLASSES = new ArrayList<String>(70);

    public static final String APPLICATION_ROOT_PACKAGE = "org.apache.tapestry5.corelib";

    public static final String PATH_PREFIX = "core";

    public static final String EXTENSION_SUBPACKAGE = "extension";

    public static final String EXTENSIONPOINT_SUBPACKAGE = "extensionpoint";

    public static final String TAPESTRY_VERSION = "5.1.0.5";

    public static final String SUPPORTED_LOCALES = "en,zh_CN";

    public static final String START_PAGE = "Main";

    static {
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.ActionLink");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.AddRowLink");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.AjaxFormLoop");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.Any");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.BeanDisplay");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.BeanEditForm");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.BeanEditor");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.Checkbox");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.DateField");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.Delegate");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.Errors");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.EventLink");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.ExceptionDisplay");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.Form");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.FormFragment");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.FormInjector");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.Grid");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.GridCell");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.GridColumns");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.GridPager");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.GridRows");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.Hidden");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.If");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.Label");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.LinkSubmit");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.Loop");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.Output");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.OutputRaw");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.PageLink");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.Palette");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.PasswordField");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.ProgressiveDisplay");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.PropertyDisplay");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.PropertyEditor");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.Radio");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.RadioGroup");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.RemoveRowLink");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.RenderObject");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.Select");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.Submit");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.SubmitNotifier");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.TextArea");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.TextField");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.TextOutput");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.Unless");
        TAPESTRY_COMPONENTS_CLASSES.add("org.apache.tapestry5.corelib.components.Zone");
        TAPESTRY_PAGES_CLASSES.add("org.apache.tapestry5.corelib.pages.ExceptionReport");
        TAPESTRY_PAGES_CLASSES.add("org.apache.tapestry5.corelib.pages.PropertyDisplayBlocks");
        TAPESTRY_PAGES_CLASSES.add("org.apache.tapestry5.corelib.pages.PropertyEditBlocks");
        TAPESTRY_PAGES_CLASSES.add("org.apache.tapestry5.corelib.pages.ServiceStatus");
        TAPESTRY_MIXINS_CLASSES.add("org.apache.tapestry5.corelib.mixins.Autocomplete");
        TAPESTRY_MIXINS_CLASSES.add("org.apache.tapestry5.corelib.mixins.DiscardBody");
        TAPESTRY_MIXINS_CLASSES.add("org.apache.tapestry5.corelib.mixins.NotEmpty");
        TAPESTRY_MIXINS_CLASSES.add("org.apache.tapestry5.corelib.mixins.RenderDisabled");
        TAPESTRY_MIXINS_CLASSES.add("org.apache.tapestry5.corelib.mixins.RenderInformals");
        TAPESTRY_MIXINS_CLASSES.add("org.apache.tapestry5.corelib.mixins.TriggerFragment");
    }
}
