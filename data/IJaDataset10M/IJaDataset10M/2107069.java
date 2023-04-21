package de.jmda.generator.java.naming;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ImportManager {

    /**
	 * stores the target package name for java types to be generated.
	 */
    private String targetTypePackageName;

    /**
	 * Stores pairs of simple type names and their corresponding package name. The
	 * simple type names can be used unqualified in the java type to be generated
	 * because an appropriate type import statement will be returned by {@link
	 * #generateImportStatements()}.
	 */
    private Map<String, String> typeImports = new HashMap<String, String>();

    /**
	 * Stores static imports.
	 */
    private Set<String> staticImports = new TreeSet<String>();

    public ImportManager(String targetTypePackageName) {
        super();
        this.targetTypePackageName = targetTypePackageName;
    }

    /**
	 * @return import statements, containing static and non static imports for the
	 *         java type to be generated
	 */
    public StringBuffer generateImportStatements() {
        StringBuffer result = new StringBuffer();
        for (String staticImport : staticImports) {
            result.append("import static " + staticImport + ";\n");
        }
        Set<String> importClasses = new TreeSet<String>();
        for (String simpleTypeName : typeImports.keySet()) {
            importClasses.add(typeImports.get(simpleTypeName) + "." + simpleTypeName);
        }
        for (String importClass : importClasses) {
            result.append("import " + importClass + ";\n");
        }
        return result;
    }

    /**
	 * Updates internal data as necessary and calculates string for type
	 * information in generated field or parameter declarations. For types from
	 * package <code>java.lang</code> and for types from the same package as
	 * {@link #targetTypePackageName} this method will calculate the according
	 * simple name of <code>qualifiedImportTypeName</code>.
	 *
	 * @param qualifiedImportTypeName
	 * @return <code>qualifiedImportTypeName</code> if it has to be used
	 *         qualified in generated code, simple type name if it does not have
	 *         to be used qualified.
	 */
    public String useType(String qualifiedImportTypeName) {
        if (qualifiedImportTypeName.startsWith("java.lang")) {
            return getSimpleNameFromQualifiedTypeName(qualifiedImportTypeName);
        }
        String packageName = getPackageNameFromQualifiedTypeName(qualifiedImportTypeName);
        if (packageName.equals(targetTypePackageName)) {
            return getSimpleNameFromQualifiedTypeName(qualifiedImportTypeName);
        }
        String simpleName = getSimpleNameFromQualifiedTypeName(qualifiedImportTypeName).replace('$', '.');
        String typeImportPackageName = typeImports.get(simpleName);
        if (typeImportPackageName != null) {
            if (typeImportPackageName.equals(packageName)) {
                return simpleName;
            } else {
                return qualifiedImportTypeName;
            }
        }
        typeImports.put(simpleName, packageName);
        return simpleName;
    }

    public String useType(Class<?> clazz) {
        return useType(clazz.getName());
    }

    public void addStaticImport(String staticImport) {
        if (staticImport.endsWith("*")) {
            staticImports.add(staticImport);
        } else {
            int index = staticImport.lastIndexOf('.');
            if (index > 0) {
                String wildcardImport = staticImport.substring(0, index) + ".*";
                if (staticImports.contains(wildcardImport)) {
                } else {
                    staticImports.add(staticImport);
                }
            }
        }
    }

    private String getSimpleNameFromQualifiedTypeName(String qualifiedTypeName) {
        return qualifiedTypeName.substring(qualifiedTypeName.lastIndexOf(".") + 1);
    }

    private String getPackageNameFromQualifiedTypeName(String qualifiedTypeName) {
        return qualifiedTypeName.substring(0, qualifiedTypeName.lastIndexOf("."));
    }
}
