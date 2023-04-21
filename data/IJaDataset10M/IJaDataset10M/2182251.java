package ca.ucalgary.cpsc.ase.productLineDesigner.refactoring.VariationPoint.addVariant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.CompilationUnit;
import ca.ucalgary.cpsc.ase.productLineDesigner.exceptions.RefactoringException;
import ca.ucalgary.cpsc.ase.productLineDesigner.exceptions.VariationPointSearchException;
import ca.ucalgary.cpsc.ase.productLineDesigner.refactoring.RefactoringUtils;
import ca.ucalgary.cpsc.ase.productLineDesigner.refactoring.VariationPoint.Variant;
import ca.ucalgary.cpsc.ase.productLineDesigner.refactoring.VariationPoint.VariationPointInfo;
import ca.ucalgary.cpsc.ase.productLineDesigner.refactoring.VariationPoint.annotations.VPAnnotationUtil;
import ca.ucalgary.cpsc.ase.productLineDesigner.refactoring.visitors.TypeDeclarationFinder;
import ca.ucalgary.cpsc.ase.productLineDesigner.search.VariationPoint.VariationPoint;

public class AddVariantInfo extends VariationPointInfo {

    private List<Variant> newVariants;

    private IType ennum;

    public AddVariantInfo(IMethod testMethod) throws RefactoringException {
        super(testMethod);
        String nameOfSelectedMethod = testMethod.getElementName();
        if (nameOfSelectedMethod.contains("_")) {
            originalTestMethodName = testMethod.getElementName().substring(0, testMethod.getElementName().lastIndexOf('_'));
        } else {
            originalTestMethodName = "test";
        }
    }

    public void initialize(IProgressMonitor pm) throws RefactoringException {
        pm.beginTask("initialize", 1);
        try {
            IAnnotation vpAnnotation = null;
            for (IAnnotation anno : testMethod.getAnnotations()) {
                if (VPAnnotationUtil.isValidVariationAnnotation(anno)) {
                    vpAnnotation = anno;
                    break;
                }
            }
            if (vpAnnotation == null) {
                throw new RefactoringException("");
            }
            variationPoint = VPAnnotationUtil.getVPID(vpAnnotation);
            VariationPoint vp = VariationPoint.createVariationPointObject(project, variationPoint);
            ennum = vp.getVariantEnum();
            enumPath = ennum.getPath().removeFirstSegments(1);
            enumName = ennum.getElementName();
            qualifiedEnumName = ennum.getFullyQualifiedName();
            variants = vp.getVariants();
            IType factory = vp.getFactory();
            factoryName = factory.getElementName();
            qualifiedFactoryName = factory.getFullyQualifiedName();
            factoryPath = factory.getPath().removeFirstSegments(1);
            IType abstractProduct = vp.getAbstractProduct();
            qualifiedProductName = abstractProduct.getFullyQualifiedName();
            productName = abstractProduct.getElementName();
            productPath = abstractProduct.getPath().removeFirstSegments(1);
            productPackagePath = abstractProduct.getPackageFragment().getPath().removeFirstSegments(1);
            IField field = vp.getConfigurationField();
            enumInstanceName = field.getElementName();
            CompilationUnit cu = RefactoringUtils.createCompilationUnitFor(abstractProduct.getCompilationUnit());
            TypeDeclarationFinder tdf = new TypeDeclarationFinder(abstractProduct.getElementName());
            cu.accept(tdf);
            setProductConstructorParametersFor(tdf.getTypeDeclaration().resolveBinding());
            pm.worked(1);
        } catch (CoreException e) {
            throw new RefactoringException("");
        } catch (VariationPointSearchException e) {
            throw new RefactoringException(e.getMessage());
        } finally {
            pm.done();
        }
    }

    public List<Variant> getNewVariants() {
        return Collections.unmodifiableList(newVariants);
    }

    public void setNewVariants(List<String> newVariants) {
        this.newVariants = new ArrayList<Variant>();
        for (String variant : newVariants) {
            String concreteProductName = RefactoringUtils.createConcreteProductName(variant, productName);
            String constant = RefactoringUtils.createVariantConstantName(variant);
            this.newVariants.add(new Variant(variant, concreteProductName, constant));
        }
    }
}
