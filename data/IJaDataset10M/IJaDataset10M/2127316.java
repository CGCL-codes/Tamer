package whf.framework.workflow.assign.parser;

import java.util.Collection;
import java.util.Map;
import org.jdom.Element;
import whf.framework.util.Utils;

/**
 * @author king
 *
 */
public class AssignmentParserFactory {

    private static Map<String, Class<? extends AssignmentParser>> cache = Utils.newHashMap();

    static {
        cache.put("org", DeptAssignmentParser.class);
        cache.put("role", RoleAssignmentParser.class);
        cache.put("user", UserAssignmentParser.class);
        cache.put("hql", HqlAssignmentParser.class);
    }

    public static AssignmentParser getAssignmentParser(Element source, String roleName, Collection<String> deptCodes) {
        String type = source.getName();
        Class<? extends AssignmentParser> cls = cache.get(type);
        if (cls == null) return null;
        try {
            AssignmentParser parser = cls.newInstance();
            if (parser == null) return null;
            parser.setSource(source);
            if (parser instanceof RoleableAssignmentParser) {
                ((RoleableAssignmentParser) parser).setRoleName(roleName);
            }
            if (parser instanceof DeptableAssignmentParser) {
                ((DeptableAssignmentParser) parser).setDeptCodes(deptCodes);
            }
            return parser;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
