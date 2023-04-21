package org.soqqo.vannitator.vmodel;

public class VObjectType extends VValueType {

    public VObjectType(String packageName, String simpleClassName) {
        super(new VNameSimpleClass(simpleClassName));
        this.packageName = new VNamePackage(packageName);
        this.simpleClassName = (VNameSimpleClass) getName();
        this.qualifiedName = new VNameQualified(simpleClassName, packageName);
    }

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private VNamePackage packageName;

    private VNameSimpleClass simpleClassName;

    private VNameQualified qualifiedName;

    public VNamePackage getPackageName() {
        return packageName;
    }

    public VNameSimpleClass getSimpleClassName() {
        return simpleClassName;
    }

    public VNameQualified getQualifiedName() {
        return qualifiedName;
    }
}
