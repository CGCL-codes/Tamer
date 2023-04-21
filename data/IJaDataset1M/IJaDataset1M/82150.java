package crafty.metamodel;

import java.lang.reflect.Modifier;
import java.util.*;
import crafty.util.CopyUtil;

public abstract class MetaModelElement implements Comparable, Copyable {

    private MetaModelElement parent = null;

    private String name = "";

    private String annotation = "";

    private Map tags = new LinkedHashMap();

    private Map typeToMemberMap = new LinkedHashMap();

    private CodeBlock impl = new CodeBlock();

    public MetaModelElement() {
    }

    public MetaModelElement(String name) {
        this(name, new Class[0]);
    }

    public MetaModelElement(String name, Class memberType) {
        this(name, new Class[] { memberType });
    }

    public MetaModelElement(String name, Class[] memberTypes) {
        assert (name != null);
        assert (memberTypes != null);
        this.name = name;
        processMemberTypes(memberTypes);
    }

    public MetaModelElement(Class memberType) {
        this(new Class[] { memberType });
    }

    public MetaModelElement(Class[] memberTypes) {
        assert (memberTypes != null);
        processMemberTypes(memberTypes);
    }

    public String toString() {
        return getQName();
    }

    public Object copy() {
        MetaModelElement other = null;
        try {
            other = (MetaModelElement) getClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        other.parent = null;
        other.name = (String) CopyUtil.copy(name);
        other.annotation = (String) CopyUtil.copy(annotation);
        other.impl = (CodeBlock) CopyUtil.copy(impl);
        other.tags = CopyUtil.copy(tags);
        Collection memberList = getAllMembers();
        for (Iterator it = memberList.iterator(); it.hasNext(); ) {
            other.addMember((MetaModelElement) CopyUtil.copy(it.next()));
        }
        return other;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MetaModelElement)) {
            return false;
        }
        final MetaModelElement element = (MetaModelElement) o;
        if (!name.equals(element.name)) {
            return false;
        }
        if (!annotation.equals(element.annotation)) {
            return false;
        }
        if (!impl.equals(element.impl)) {
            return false;
        }
        if (!tags.equals(element.tags)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result;
        result = name.hashCode();
        result = 29 * result + annotation.hashCode();
        result = 29 * result + tags.hashCode();
        result = 29 * result + typeToMemberMap.hashCode();
        result = 29 * result + impl.hashCode();
        return result;
    }

    public int compareTo(Object o) {
        MetaModelElement el = (MetaModelElement) o;
        return getName().compareTo(el.getName());
    }

    public String getMetaType() {
        String type = getClass().getName();
        return type.substring((type.lastIndexOf("Meta") + 4), type.length());
    }

    public String getName() {
        return name;
    }

    public MetaModelElement setName(String name) {
        this.name = name;
        return this;
    }

    public String getAnnotation() {
        return annotation;
    }

    public MetaModelElement setAnnotation(String annotation) {
        this.annotation = annotation;
        return this;
    }

    public String getQName() {
        if (getParent() != null) {
            return getParent().getQName() + "." + getName();
        }
        return getName();
    }

    public MetaModelElement addTag(Object name, Object value) {
        tags.put(name, value);
        return this;
    }

    public Object getTag(Object name) {
        if (!tags.containsKey(name)) {
            return "";
        }
        return tags.get(name);
    }

    public Object getInheritedTag(Object name) {
        if (!tags.containsKey(name)) {
            MetaModelElement p = getParent();
            if (p != null) {
                return p.getInheritedTag(name);
            }
            return "";
        }
        return tags.get(name);
    }

    public Map getTags() {
        return tags;
    }

    public MetaModelElement getParent() {
        return parent;
    }

    public CodeBlock getImpl() {
        return impl;
    }

    /**
   * Accept the meta model visitor.
   * @param v the meta model visitor.
   */
    public abstract void accept(MetaModelVisitor v);

    protected MetaModelElement addMember(MetaModelElement member) {
        Class type = member.getClass();
        assert (isValidMemberType(type)) : "Unsupported member type '" + type + "'.";
        Set set = (Set) typeToMemberMap.get(type);
        assert (!set.contains(member)) : "Already contains member '" + member.getName() + "' = '" + member + "'";
        set.add(member);
        member.setParent(this);
        return this;
    }

    protected Collection getMembers(Class type) {
        assert (type != null);
        return (Set) typeToMemberMap.get(type);
    }

    protected Collection getAllMembers() {
        ArrayList l = new ArrayList();
        Collection typeCol = typeToMemberMap.values();
        for (Iterator it = typeCol.iterator(); it.hasNext(); ) {
            Set s = (Set) it.next();
            l.addAll(s);
        }
        return Collections.unmodifiableCollection(l);
    }

    private boolean isValidMemberType(Class type) {
        return typeToMemberMap.containsKey(type);
    }

    private MetaModelElement setParent(MetaModelElement parent) {
        this.parent = parent;
        return this;
    }

    private void processMemberTypes(Class[] memberTypes) {
        for (int i = 0; i < memberTypes.length; i++) {
            Class type = memberTypes[i];
            assert (!Modifier.isAbstract(type.getModifiers())) : "Member types cannot be abstract.";
            typeToMemberMap.put(type, new LinkedHashSet());
        }
    }
}
