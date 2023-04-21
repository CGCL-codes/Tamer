package uk.ac.cam.caret.tagphage.parser.ruleparser;

import java.lang.reflect.*;
import java.util.*;
import org.apache.commons.beanutils.*;
import org.apache.log4j.Logger;
import uk.ac.cam.caret.tagphage.parser.*;

/** 
 * @exclude
 * @author Dan Sheppard <dan@caret.cam.ac.uk>
 *
 */
public class ActionInstance implements PatternOrAction {

    private String namespace, name;

    private Map<String, String> attrs = new HashMap<String, String>();

    private PatternOrAction parent = null;

    private ActionTag at = null;

    private static Logger log = Logger.getLogger(ActionInstance.class);

    private List<ActionInstance> children = new ArrayList<ActionInstance>();

    private ActionTag getActionTag(RuleSet rs) {
        String full_name = name;
        return rs.getActionTagByName(namespace, full_name);
    }

    private String packageQualify(String in) {
        String qackage = parent.getPackage();
        if (qackage != null && !"".equals(qackage)) return qackage + "." + in;
        return in;
    }

    private String nsQualify(String in, NamespaceResolver ns) throws BadConfigException {
        if (in == null) return null;
        return new ResolvedExpression(in, ns).toString();
    }

    private String qualify(String in, String qualification, boolean parse_time, NamespaceResolver ns) throws BadConfigException {
        if (parse_time) {
            if ("namespace".equals(qualification)) return nsQualify(in, ns);
        } else {
            if ("package".equals(qualification)) {
                return packageQualify(in);
            }
        }
        return in;
    }

    private String qualifyall(String in, String quals, boolean parse_time, NamespaceResolver ns) throws BadConfigException {
        if (!"".equals(quals)) for (String q : quals.split(",")) in = qualify(in, q, parse_time, ns);
        return in;
    }

    private boolean hasquality(String quals, String key) {
        if (!"".equals(quals)) for (String q : quals.split(",")) if (q.equals(key)) return true;
        return false;
    }

    private AnyAction makeAction() throws BadConfigException {
        String klass = at.getClassname();
        int num = at.getSize();
        Object[] args = new Object[num];
        Class[] classes = new Class[num];
        for (int i = 0; i < num; i++) {
            String q = at.getQualify(i);
            if (hasquality(q, "nested")) {
                ActionInstance[] ai = null;
                if (children != null) ai = children.toArray(new ActionInstance[0]);
                AnyAction[] actions = new AnyAction[ai.length];
                for (int j = 0; j < ai.length; j++) actions[j] = ai[j].makeAction();
                args[i] = actions;
                classes[i] = AnyAction[].class;
            } else {
                args[i] = attrs.get(at.getAttr(i));
                args[i] = qualifyall((String) args[i], q, false, null);
                if (args[i] == null && !hasquality(q, "optional")) throw new BadConfigException("Argument " + at.getAttr(i) + " missing");
                classes[i] = String.class;
            }
        }
        try {
            return (AnyAction) ConstructorUtils.invokeConstructor(Class.forName(klass), args, classes);
        } catch (InstantiationException x) {
            throw new BadConfigException("Cannot create instance of type " + klass, x);
        } catch (NoSuchMethodException x) {
            throw new BadConfigException("Cannot create instance of type " + klass, x);
        } catch (IllegalAccessException x) {
            throw new BadConfigException("Cannot create instance of type " + klass, x);
        } catch (InvocationTargetException x) {
            log.error("Constructor failed", x);
            throw new BadConfigException("Constructor failed creating " + klass, x);
        } catch (ClassNotFoundException x) {
            throw new BadConfigException("No such class " + klass);
        }
    }

    private void makeRule(String pattern, ParserFactory pf) throws BadConfigException {
        log.debug("action for " + pattern);
        pf.addAction(pattern, null, makeAction());
    }

    public void makeRules(String pattern_prefix, ParserFactory pf) throws BadConfigException {
        makeRule(pattern_prefix, pf);
    }

    public void setInfo(String namespace, String name, Map<String, String> attrs, NamespaceResolver map) throws BadConfigException {
        this.namespace = namespace;
        this.name = name;
        this.attrs = attrs;
        RuleSet rs = parent.getSchema().getRuleSet();
        at = getActionTag(rs);
        if (at == null) throw new BadConfigException("Must declare an action before using it (" + name + ")");
        for (int i = 0; i < at.getSize(); i++) {
            String key = at.getAttr(i);
            String value = attrs.get(at.getAttr(i));
            value = qualifyall(value, at.getQualify(i), true, map);
            attrs.put(key, value);
        }
    }

    public void setParent(PatternOrAction in) {
        parent = in;
    }

    public Schema getSchema() {
        return parent.getSchema();
    }

    public String getPackage() {
        return parent.getPackage();
    }

    public void addInstance(ActionInstance ai) {
        children.add(ai);
    }
}
