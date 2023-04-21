package trstudio.beansmetric.core.beansmetric.java;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.Tree.Kind;
import java.io.File;
import java.util.List;
import trstudio.beansmetric.core.TRBeansMetric;
import trstudio.beansmetric.core.beansmetric.ClassTypeElement;
import trstudio.beansmetric.core.beansmetric.PackageFragmentElement;
import trstudio.beansmetric.core.beansmetric.ProjectElement;
import trstudio.beansmetric.core.metricsource.MetricSource;
import trstudio.beansmetric.core.beansmetric.SourceElement;

/**
 * Source Java.
 *
 * @author Sebastien Villemain
 */
public class JavaSourceElement implements SourceElement {

    private final CompilationUnitTree tree;

    private final String projectSourcePath;

    private ClassTypeElement[] classTypeElements = null;

    private PackageFragmentElement parentPackage = null;

    private String[] importsList = null;

    public JavaSourceElement(CompilationUnitTree tree) {
        this.tree = tree;
        projectSourcePath = getProjectSourcePath();
    }

    public void compute(MetricSource source) {
        linkParentPackage();
        computeMainClass(source);
        computeImports();
    }

    /**
	 * Calcul la classe principal et toutes ses dépendances.
	 *
	 * @param source
	 */
    private void computeMainClass(MetricSource source) {
        List<? extends Tree> typeDecls = tree.getTypeDecls();
        if (!typeDecls.isEmpty()) {
            Tree t = typeDecls.get(0);
            if (t.getKind() == Kind.CLASS) {
                classTypeElements = new ClassTypeElement[1];
                classTypeElements[0] = new JavaClassTypeElement(this, (ClassTree) t);
                classTypeElements[0].compute(source);
            }
        }
    }

    /**
	 * Liste des importations.
	 */
    private void computeImports() {
        importsList = new String[tree.getImports().size()];
        for (int i = 0; i < importsList.length; i++) {
            if (TRBeansMetric.isInterrupted()) {
                break;
            }
            ImportTree importT = tree.getImports().get(i);
            importsList[i] = importT.getQualifiedIdentifier().toString();
        }
    }

    /**
	 * Création de la liaison avec les packages parents.
	 */
    private void linkParentPackage() {
        String parentPackageName = tree.getPackageName().toString();
        int index = 0;
        PackageFragmentElement packageElement = null;
        while ((index = parentPackageName.indexOf(".", index)) > 0) {
            String packageName = parentPackageName.substring(0, index);
            packageElement = getPackageElement(packageName, packageElement);
            index++;
        }
        parentPackage = getPackageElement(parentPackageName, packageElement);
    }

    /**
	 * Retourne l'élement package correspondant au nom du package précisé.
	 *
	 * @param packageName
	 * @param parent
	 * @return
	 */
    private PackageFragmentElement getPackageElement(String packageName, PackageFragmentElement parent) {
        PackageFragmentElement packageElement = null;
        File packageFolder = getPackageFolder(packageName);
        if (!packageName.contains(".")) {
            ProjectElement projectElement = new JavaProjectElement(new File(projectSourcePath));
            packageElement = new JavaPackageRootElement(projectElement, packageFolder);
        } else if (parent != null) {
            packageElement = new JavaPackageFragmentElement(parent, packageFolder);
        }
        return packageElement;
    }

    /**
	 * Retourne le dossier du package.
	 *
	 * @param packageName
	 * @return
	 */
    private File getPackageFolder(String packageName) {
        String packagePath = packageName.replace(".", System.getProperty("file.separator"));
        return new File(projectSourcePath + packagePath);
    }

    /**
	 * Retourne le chemin vers les fichiers sources du projet.
	 *
	 * @return
	 */
    private String getProjectSourcePath() {
        File sourceElementFile = new File(tree.getSourceFile().toUri().getPath());
        String projectSourcePath = sourceElementFile.getAbsolutePath().replace(sourceElementFile.getName(), "");
        String packageRootName = tree.getPackageName().toString();
        int packageRootIndex = packageRootName.indexOf(".");
        if (packageRootIndex > 0) {
            packageRootName = packageRootName.substring(0, packageRootIndex);
        }
        int projectFileIndex = projectSourcePath.lastIndexOf(packageRootName);
        if (projectFileIndex > 0) {
            projectSourcePath = projectSourcePath.substring(0, projectFileIndex);
        }
        return projectSourcePath;
    }

    public String getHandle() {
        return tree.getPackageName().toString();
    }

    public ClassTypeElement[] getClassTypeElements() {
        return classTypeElements;
    }

    public PackageFragmentElement getParentPackageElement() {
        return parentPackage;
    }

    public String[] getImports() {
        return importsList;
    }
}
