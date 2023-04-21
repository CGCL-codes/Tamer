package momi;

public class ObjectType implements MoMiType {

    /**
     * 
     */
    private static final long serialVersionUID = -1846644987286997931L;

    protected RecordType methods = new RecordType();

    public ObjectType() {
    }

    public ObjectType(RecordType type) {
        methods = type;
    }

    public void addMethod(MethodType meth) {
        methods.addMethod(meth);
    }

    public boolean equals(MoMiType type) {
        return (type instanceof ObjectType && methods.equals(((ObjectType) type).methods));
    }

    public boolean subtype(MoMiType type) {
        return (type instanceof ObjectType && methods.subtype(((ObjectType) type).methods));
    }

    /**
     * Convert this object type into a string
     *
     * @return the string representation of this object type
     */
    public String toString() {
        return _toString(0);
    }

    /**
     * Convert this object type to a string with indentation
     *
     * @param indent the indentation level
     *
     * @return the string representation of this object type
     */
    String _toString(int indent) {
        String indent_str = MoMiPrinter.build_indent(indent);
        String indent_str2 = MoMiPrinter.build_indent(indent + 2);
        return indent_str + "object<\n" + methods._toString(indent + 4) + "\n" + indent_str2 + ">";
    }
}
