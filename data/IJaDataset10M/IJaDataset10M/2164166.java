package tudresden.ocl20.pivot.ocl2parser.testcasegenerator.abstractsyntax.impl;

import java.io.File;
import tudresden.ocl20.pivot.ocl2parser.testcasegenerator.abstractsyntax.IModelNode;
import tudresden.ocl20.pivot.ocl2parser.testcasegenerator.abstractsyntax.IPackageDeclaration;
import tudresden.ocl20.pivot.ocl2parser.testcasegenerator.abstractsyntax.ITest;
import tudresden.ocl20.pivot.ocl2parser.testcasegenerator.abstractsyntax.ITokenAS;
import tudresden.ocl20.pivot.ocl2parser.testcasegenerator.util.BuildingASMException;
import tudresden.ocl20.pivot.ocl2parser.testcasegenerator.util.Environment;

public abstract class Test implements ITest {

    protected ITokenAS token;

    protected IPackageDeclaration packageDecl;

    protected File file;

    public Test() {
    }

    public Test(ITokenAS token) {
        this.token = token;
    }

    public ITokenAS getToken() {
        return token;
    }

    public void setToken(ITokenAS token) {
        this.token = token;
    }

    public IPackageDeclaration getPackageDeclaration() {
        return packageDecl;
    }

    public void setPackageDeclaration(IPackageDeclaration decl) {
        this.packageDecl = decl;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
