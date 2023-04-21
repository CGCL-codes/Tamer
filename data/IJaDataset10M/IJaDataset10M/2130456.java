package vm;

public class CVMClassDataType extends CVMDataType {

    public CVMpkg pkg;

    public String classInPackage;

    CVMClassDataType(CVMpkg p, String cname) {
        super();
        pkg = p;
        classInPackage = cname;
    }
}
