package components;

public class MemberNameTriple {

    public String clazz;

    public String name;

    public String type;

    public MemberNameTriple(String cls, String nm, String typ) {
        clazz = cls;
        name = nm;
        type = typ;
    }

    public boolean sameMember(String classname, String membername, String typestring) {
        if (!clazz.equals(classname)) return false;
        if (!name.equals(membername)) return false;
        if (type == typestring) return true;
        return type.equals(typestring);
    }

    public String toString() {
        StringBuffer result = new StringBuffer(clazz);
        result.append('.');
        result.append(name);
        if (type != null) {
            result.append(type);
        }
        return result.toString();
    }
}
