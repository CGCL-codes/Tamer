package tudresden.ocl20.pivot.ocl2parser.testcasegenerator.abstractsyntax;

public interface IListElement extends IComplexElement {

    public String getListTypename();

    public void setListTypename(String typename);

    public String getFullQualifiedListTypeName();

    public String getPackageListName();

    public boolean isArray();

    public void setArray(boolean arrayType);
}
