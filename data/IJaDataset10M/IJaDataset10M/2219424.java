package pl.omtt.lang.model.types;

public interface IType {

    IType getEffective();

    boolean isSequence();

    IType setSequence();

    IType unsetSequence();

    boolean isNotNull();

    IType setNotNull();

    IType unsetNotNull();

    public boolean isFrozen();

    public void freeze();

    boolean isSubtypeOf(IType type);

    boolean essentiallyEquals(IType type);

    boolean isSingleData();

    boolean isBoolean();

    boolean isNumeric();

    boolean isGeneral();

    boolean isNull();

    boolean isFunction();

    boolean isGeneric();

    boolean isError();

    Class<?> getAssociatedClass();

    IType dup();

    String singleToString();

    String toDiagnosticString();

    String toEssentialString();

    IType getEffectiveLowerBound();

    String getName();

    void setName(String name);
}
