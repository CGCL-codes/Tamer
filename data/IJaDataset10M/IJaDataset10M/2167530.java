package net.innig.macker.structure;

/**
    A reference from one class to another.
*/
public final class Reference {

    public Reference(ClassInfo from, ClassInfo to, ReferenceType type, String memberName, AccessModifier memberAccess) {
        if (from == null) throw new IllegalArgumentException("from is null");
        if (to == null) throw new IllegalArgumentException("to is null");
        if (type == null) throw new IllegalArgumentException("type is null");
        if ((memberName == null) != (memberAccess == null)) throw new IllegalArgumentException("memberName and memberAccess must both be present or both be absent");
        this.from = from;
        this.to = to;
        this.type = type;
        this.memberName = memberName;
        this.memberAccess = memberAccess;
    }

    public ClassInfo getFrom() {
        return from;
    }

    public ClassInfo getTo() {
        return to;
    }

    public ReferenceType getType() {
        return type;
    }

    public String getMemberName() {
        return memberName;
    }

    public AccessModifier getMemberAccess() {
        return memberAccess;
    }

    public boolean equals(Object that) {
        if (this == that) return true;
        if (that == null) return false;
        if (this.getClass() != that.getClass()) return false;
        Reference thatRef = (Reference) that;
        if (this.memberName == null && thatRef.memberName != null) return false;
        if (this.memberAccess == null && thatRef.memberAccess != null) return false;
        return this.from.equals(thatRef.from) && this.to.equals(thatRef.to) && this.type.equals(thatRef.type);
    }

    public int hashCode() {
        return from.hashCode() ^ to.hashCode() * 17 ^ type.hashCode() * 103 ^ (memberName == null ? 0 : memberName.hashCode() * 23) ^ (memberAccess == null ? 0 : memberAccess.hashCode() * 5);
    }

    public String toString() {
        return "Ref(" + from + " -> " + to + ", " + type + (memberAccess == null ? "" : ": " + memberAccess + " " + memberName) + ')';
    }

    private final ClassInfo from, to;

    private final ReferenceType type;

    private final String memberName;

    private final AccessModifier memberAccess;
}
