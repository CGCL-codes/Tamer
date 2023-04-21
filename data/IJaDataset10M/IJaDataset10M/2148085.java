package cz.vse.gebz;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Kompozicionalni Pravidlo</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link cz.vse.gebz.KompozicionalniPravidlo#getVahaPravidla <em>Vaha Pravidla</em>}</li>
 *   <li>{@link cz.vse.gebz.KompozicionalniPravidlo#getDolniHraniceVahyPravidla <em>Dolni Hranice Vahy Pravidla</em>}</li>
 *   <li>{@link cz.vse.gebz.KompozicionalniPravidlo#getHorniHraniceVahyPravidla <em>Horni Hranice Vahy Pravidla</em>}</li>
 * </ul>
 * </p>
 *
 * @see cz.vse.gebz.GebzPackage#getKompozicionalniPravidlo()
 * @model extendedMetaData="kind='elementOnly'"
 * @generated
 */
public interface KompozicionalniPravidlo extends Pravidlo {

    /**
	 * Returns the value of the '<em><b>Vaha Pravidla</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Vaha Pravidla</em>' containment reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Vaha Pravidla</em>' containment reference.
	 * @see #setVahaPravidla(Vaha)
	 * @see cz.vse.gebz.GebzPackage#getKompozicionalniPravidlo_VahaPravidla()
	 * @model containment="true" required="true" ordered="false"
	 * @generated
	 */
    Vaha getVahaPravidla();

    /**
	 * Sets the value of the '{@link cz.vse.gebz.KompozicionalniPravidlo#getVahaPravidla <em>Vaha Pravidla</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Vaha Pravidla</em>' containment reference.
	 * @see #getVahaPravidla()
	 * @generated
	 */
    void setVahaPravidla(Vaha value);

    /**
	 * Returns the value of the '<em><b>Dolni Hranice Vahy Pravidla</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dolni Hranice Vahy Pravidla</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dolni Hranice Vahy Pravidla</em>' attribute.
	 * @see #setDolniHraniceVahyPravidla(double)
	 * @see cz.vse.gebz.GebzPackage#getKompozicionalniPravidlo_DolniHraniceVahyPravidla()
	 * @model transient="true" volatile="true" derived="true"
	 * @generated
	 */
    double getDolniHraniceVahyPravidla();

    /**
	 * Sets the value of the '{@link cz.vse.gebz.KompozicionalniPravidlo#getDolniHraniceVahyPravidla <em>Dolni Hranice Vahy Pravidla</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dolni Hranice Vahy Pravidla</em>' attribute.
	 * @see #getDolniHraniceVahyPravidla()
	 * @generated
	 */
    void setDolniHraniceVahyPravidla(double value);

    /**
	 * Returns the value of the '<em><b>Horni Hranice Vahy Pravidla</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Horni Hranice Vahy Pravidla</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Horni Hranice Vahy Pravidla</em>' attribute.
	 * @see #setHorniHraniceVahyPravidla(double)
	 * @see cz.vse.gebz.GebzPackage#getKompozicionalniPravidlo_HorniHraniceVahyPravidla()
	 * @model transient="true" volatile="true" derived="true"
	 * @generated
	 */
    double getHorniHraniceVahyPravidla();

    /**
	 * Sets the value of the '{@link cz.vse.gebz.KompozicionalniPravidlo#getHorniHraniceVahyPravidla <em>Horni Hranice Vahy Pravidla</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Horni Hranice Vahy Pravidla</em>' attribute.
	 * @see #getHorniHraniceVahyPravidla()
	 * @generated
	 */
    void setHorniHraniceVahyPravidla(double value);
}
