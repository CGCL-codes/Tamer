package ognl.webobjects;

import java.util.Map;
import ognl.ClassResolver;
import com.webobjects.foundation._NSUtilities;

public class NSClassResolver implements ClassResolver {

    protected static NSClassResolver _sharedInstance;

    public static NSClassResolver sharedInstance() {
        if (_sharedInstance == null) _sharedInstance = new NSClassResolver();
        return _sharedInstance;
    }

    public Class classForName(String className, Map context) throws ClassNotFoundException {
        Class c1 = _NSUtilities.classWithName(className);
        if (c1 == null) throw new ClassNotFoundException(className);
        return c1;
    }
}
