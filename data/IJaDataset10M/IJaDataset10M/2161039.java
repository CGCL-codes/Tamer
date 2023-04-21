package rubbish.db.dao.parser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import rubbish.db.util.ClassUtils;
import rubbish.db.util.RegexUtils;
import rubbish.db.util.StringUtils;

/**
 * ����
 * 
 * @author $Author: winebarrel $
 * @version $Revision: 1.1 $
 */
public class FormalArgs {

    protected Map methods = new HashMap();

    public void register(String classname, String methodname, String prmsdesc) {
        Map class_methods = (Map) methods.get(classname);
        if (class_methods == null) {
            class_methods = new HashMap();
            methods.put(classname, class_methods);
        }
        Map overload_methods = (Map) class_methods.get(methodname);
        if (overload_methods == null) {
            overload_methods = new HashMap();
            class_methods.put(methodname, overload_methods);
        }
        if (RegexUtils.matches("\\s*", Pattern.MULTILINE, prmsdesc)) {
            overload_methods.put("", new String[0]);
            return;
        }
        String[] prmsets = RegexUtils.split("\\s*,\\s*", Pattern.MULTILINE, prmsdesc);
        List types = new ArrayList();
        List names = new ArrayList();
        for (int i = 0; i < prmsets.length; i++) {
            String[] prmset = RegexUtils.split("\\s+", Pattern.MULTILINE, prmsets[i], 2);
            types.add(prmset[0]);
            names.add(prmset[1]);
        }
        String typesdesc = StringUtils.join(types.toArray(), ", ");
        overload_methods.put(typesdesc, names.toArray(new String[names.size()]));
    }

    public String[] getFormalArg(Class clazz, Method method) {
        Map class_methods = (Map) methods.get(clazz.getName());
        if (class_methods == null) return null;
        Map overload_methods = (Map) class_methods.get(method.getName());
        if (overload_methods == null) return null;
        Class[] types = method.getParameterTypes();
        if (types.length < 1) return (String[]) overload_methods.get("");
        String[] classnames = new String[types.length];
        for (int i = 0; i < types.length; i++) classnames[i] = ClassUtils.getShortName(types[i]);
        String typesdesc = StringUtils.join(classnames, ", ");
        return (String[]) overload_methods.get(typesdesc);
    }
}
