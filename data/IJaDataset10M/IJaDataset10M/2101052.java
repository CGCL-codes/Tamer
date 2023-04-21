package no.uia.sudoku;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cell</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link no.uia.sudoku.Cell#getICellValue <em>ICell Value</em>}</li>
 *   <li>{@link no.uia.sudoku.Cell#getField <em>Field</em>}</li>
 *   <li>{@link no.uia.sudoku.Cell#getIPossibleValues <em>IPossible Values</em>}</li>
 * </ul>
 * </p>
 *
 * @see no.uia.sudoku.SudokuPackage#getCell()
 * @model
 * @generated
 */
public interface Cell extends EObject {

    /**
	 * Returns the value of the '<em><b>ICell Value</b></em>' attribute.
	 * The default value is <code>"0"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>ICell Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>ICell Value</em>' attribute.
	 * @see #setICellValue(int)
	 * @see no.uia.sudoku.SudokuPackage#getCell_ICellValue()
	 * @model default="0"
	 * @generated
	 */
    int getICellValue();

    /**
	 * Sets the value of the '{@link no.uia.sudoku.Cell#getICellValue <em>ICell Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>ICell Value</em>' attribute.
	 * @see #getICellValue()
	 * @generated
	 */
    void setICellValue(int value);

    /**
	 * Returns the value of the '<em><b>Field</b></em>' reference list.
	 * The list contents are of type {@link no.uia.sudoku.Field}.
	 * It is bidirectional and its opposite is '{@link no.uia.sudoku.Field#getCells <em>Cells</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Field</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Field</em>' reference list.
	 * @see no.uia.sudoku.SudokuPackage#getCell_Field()
	 * @see no.uia.sudoku.Field#getCells
	 * @model opposite="cells" required="true"
	 * @generated
	 */
    EList<Field> getField();

    /**
	 * Returns the value of the '<em><b>IPossible Values</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.Integer}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>IPossible Values</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>IPossible Values</em>' attribute list.
	 * @see no.uia.sudoku.SudokuPackage#getCell_IPossibleValues()
	 * @model default="0"
	 * @generated
	 */
    EList<Integer> getIPossibleValues();
}
