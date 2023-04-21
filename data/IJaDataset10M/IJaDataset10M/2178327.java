package net.randomice.gengmf.template;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a
 * create method for each non-abstract class of the model. <!-- end-user-doc -->
 * 
 * @see net.randomice.gengmf.template.TemplatePackage
 * @generated
 */
public interface TemplateFactory extends EFactory {

    /**
	 * The singleton instance of the factory. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
    TemplateFactory eINSTANCE = net.randomice.gengmf.template.impl.TemplateFactoryImpl.init();

    /**
	 * Returns a new object of class '<em>Edge Template</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Edge Template</em>'.
	 * @generated
	 */
    EdgeTemplate createEdgeTemplate();

    /**
	 * Returns a new object of class '<em>Compartment Template</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Compartment Template</em>'.
	 * @generated
	 */
    CompartmentTemplate createCompartmentTemplate();

    /**
	 * Returns a new object of class '<em>Node Template</em>'. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Node Template</em>'.
	 * @generated
	 */
    NodeTemplate createNodeTemplate();

    /**
	 * Returns the package supported by this factory. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
    TemplatePackage getTemplatePackage();
}
