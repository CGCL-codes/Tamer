package groovy.lang.webobjects;

import groovy.lang.Binding;
import groovy.lang.webobjects.components.WOTranscript;
import com.webobjects.appserver.WOApplication;
import com.webobjects.eoaccess.EOModelGroup;
import com.webobjects.eocontrol.EOObjectStoreCoordinator;
import com.webobjects.eocontrol.EOSharedEditingContext;
import com.webobjects.foundation.NSDictionary;
import com.webobjects.foundation.NSNotificationCenter;

public class WOGroovyBinding extends Binding {

    private static WOGroovyBinding defaultBinding;

    public static String TranscriptKey = "transcript";

    public static String ObjectStoreCoordinatorKey = "osc";

    public static String ResourceManagerKey = "rm";

    public static String ModelGroupKey = "mgroup";

    public static String NotificationCenterKey = "nc";

    public static String SharedEditingContextKey = "sec";

    public static String EditingContextKey = "ec";

    public static WOGroovyBinding defaultBinding() {
        if (defaultBinding == null) {
            defaultBinding = new WOGroovyBinding();
            initializeBinding(defaultBinding);
        }
        return defaultBinding;
    }

    public static void initializeBinding(WOGroovyBinding binding) {
        binding.setObjectForKey(WOTranscript.defaultTranscript, TranscriptKey);
        binding.setObjectForKey(EOObjectStoreCoordinator.defaultCoordinator(), ObjectStoreCoordinatorKey);
        binding.setObjectForKey(WOApplication.application().resourceManager(), ResourceManagerKey);
        binding.setObjectForKey(EOModelGroup.defaultGroup(), ModelGroupKey);
        binding.setObjectForKey(NSNotificationCenter.defaultCenter(), NotificationCenterKey);
        binding.setObjectForKey(EOSharedEditingContext.defaultSharedEditingContext(), SharedEditingContextKey);
    }

    public static void setDefaultBinding(WOGroovyBinding binding) {
        defaultBinding = binding;
    }

    public WOGroovyBinding() {
        super();
    }

    public WOGroovyBinding(NSDictionary<String, Object> dictionary) {
        super(dictionary);
    }

    public Object propertyForKey(String key) {
        return getProperty(key);
    }

    public void setPropertyForKey(Object value, String key) {
        setProperty(key, value);
    }

    public Object objectForKey(String key) {
        return getVariable(key);
    }

    public void setObjectForKey(Object value, String key) {
        setVariable(key, value);
    }
}
