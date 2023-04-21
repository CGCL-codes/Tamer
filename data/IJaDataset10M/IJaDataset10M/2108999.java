package net.randomice.gengmf.template;

import org.eclipse.gmf.gmfgraph.Connection;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Edge Template</b></em>'. <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link net.randomice.gengmf.template.EdgeTemplate#getConnection <em>
 * Connection</em>}</li>
 * </ul>
 * </p>
 * 
 * @see net.randomice.gengmf.template.TemplatePackage#getEdgeTemplate()
 * @model
 * @generated
 */
public interface EdgeTemplate extends FigureTemplate {

    /**
	 * Returns the value of the '<em><b>Connection</b></em>' containment
	 * reference. <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Connection</em>' containment reference isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Connection</em>' containment reference.
	 * @see #setConnection(Connection)
	 * @see net.randomice.gengmf.template.TemplatePackage#getEdgeTemplate_Connection()
	 * @model containment="true" required="true"
	 * @generated
	 */
    Connection getConnection();

    /**
	 * Sets the value of the '
	 * {@link net.randomice.gengmf.template.EdgeTemplate#getConnection
	 * <em>Connection</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Connection</em>' containment
	 *            reference.
	 * @see #getConnection()
	 * @generated
	 */
    void setConnection(Connection value);
}
