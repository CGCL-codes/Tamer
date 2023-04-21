package uk.ac.ebi.intact.apt.mockable;

import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.InterfaceDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.Modifier;
import com.sun.mirror.type.InterfaceType;
import uk.ac.ebi.intact.annotation.Mockable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * TODO comment this
*
* @author Bruno Aranda (baranda@ebi.ac.uk)
* @version $Id: MockInfo.java 8764 2007-06-22 08:34:32Z baranda $
*/
public class MockInfo {

    private static final String MOCK_CLASS_PREFIX = "Mock";

    private String simpleName;

    private String packageName;

    private List<String> methodSignatures;

    private InterfaceDeclaration interfaceDeclaration;

    private String genericType;

    private String genericTypeVar;

    public MockInfo(InterfaceDeclaration interfaceDeclaration, String packageName) {
        this.interfaceDeclaration = interfaceDeclaration;
        this.packageName = packageName;
        String typeParams = declarationsToCommaList(interfaceDeclaration.getFormalTypeParameters());
        if (typeParams != null && typeParams.length() > 0) {
            genericType = "<" + typeParams + ">";
            genericTypeVar = "<T>";
        }
        this.simpleName = MOCK_CLASS_PREFIX + interfaceDeclaration.getSimpleName();
        this.methodSignatures = new ArrayList<String>();
    }

    public String getPackageName() {
        return packageName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public String getQualifiedName() {
        return packageName + "." + simpleName;
    }

    public InterfaceDeclaration getInterfaceDeclaration() {
        return interfaceDeclaration;
    }

    public String getGenericType() {
        return genericType;
    }

    public String getGenericTypeVar() {
        return genericTypeVar;
    }

    public void setGenericTypeVar(String genericTypeVar) {
        this.genericTypeVar = genericTypeVar;
    }

    public List<String> getMethodSignatures() {
        return methodSignatures;
    }

    public void addMethodSignature(MethodDeclaration methodDeclaration) {
        if (methodSignatures == null) {
            methodSignatures = new ArrayList<String>();
        }
        methodSignatures.add(getMethodSignatureFromDeclaration(methodDeclaration));
    }

    protected String getMethodSignatureFromDeclaration(MethodDeclaration md) {
        StringBuilder sb = new StringBuilder();
        for (Modifier modifier : md.getModifiers()) {
            if (!modifier.equals(Modifier.ABSTRACT)) {
                sb.append(modifier).append(" ");
            }
        }
        String formalTypeParams = declarationsToCommaList(md.getFormalTypeParameters());
        if (formalTypeParams != null && formalTypeParams.length() > 0) {
            sb.append("<").append(formalTypeParams).append("> ");
        }
        sb.append(md.getReturnType()).append(" ").append(md.getSimpleName()).append("(");
        sb.append(declarationsToCommaList(md.getParameters()));
        sb.append(")");
        return sb.toString();
    }

    protected String declarationsToCommaList(Collection<? extends Declaration> declarations) {
        StringBuilder sb = new StringBuilder();
        for (Iterator<? extends Declaration> iterator = declarations.iterator(); iterator.hasNext(); ) {
            Declaration pd = iterator.next();
            sb.append(pd);
            if (iterator.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    public String getSuperMock() {
        for (InterfaceType superInterface : interfaceDeclaration.getSuperinterfaces()) {
            if (superInterface.getDeclaration().getAnnotation(Mockable.class) != null) {
                String superQualName = superInterface.getDeclaration().getQualifiedName();
                String superQualNameWithType = superInterface.toString();
                String superInterfaceSimpleName = superQualNameWithType.substring(superQualName.lastIndexOf('.') + 1, superQualNameWithType.length());
                return MOCK_CLASS_PREFIX + superInterfaceSimpleName;
            }
        }
        return null;
    }

    public File getFileName(File resourcesDir) throws IOException {
        File parentDir = createGeneratedPackageDir(resourcesDir);
        File file = new File(parentDir, simpleName + ".java");
        file.getParentFile().mkdirs();
        return file;
    }

    private File createGeneratedPackageDir(File resourcesDir) throws IOException {
        String strFile = packageName.replaceAll("\\.", "/");
        File file = new File(resourcesDir, strFile + "/");
        file.mkdirs();
        return file;
    }
}
