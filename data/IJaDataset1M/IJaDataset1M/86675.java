package hu.cubussapiens.modembed.model.highlevelprogram;

import hu.cubussapiens.modembed.model.infrastructure.PackageElement;
import hu.cubussapiens.modembed.model.platform.PlatformDefinition;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>High Level Program</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link hu.cubussapiens.modembed.model.highlevelprogram.HighLevelProgram#getPlatform <em>Platform</em>}</li>
 *   <li>{@link hu.cubussapiens.modembed.model.highlevelprogram.HighLevelProgram#getVariables <em>Variables</em>}</li>
 *   <li>{@link hu.cubussapiens.modembed.model.highlevelprogram.HighLevelProgram#getSteps <em>Steps</em>}</li>
 * </ul>
 * </p>
 *
 * @see hu.cubussapiens.modembed.model.highlevelprogram.HighLevelProgramPackage#getHighLevelProgram()
 * @model
 * @generated
 */
public interface HighLevelProgram extends PackageElement {

    /**
	 * Returns the value of the '<em><b>Platform</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Platform</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Platform</em>' reference.
	 * @see #setPlatform(PlatformDefinition)
	 * @see hu.cubussapiens.modembed.model.highlevelprogram.HighLevelProgramPackage#getHighLevelProgram_Platform()
	 * @model
	 * @generated
	 */
    PlatformDefinition getPlatform();

    /**
	 * Sets the value of the '{@link hu.cubussapiens.modembed.model.highlevelprogram.HighLevelProgram#getPlatform <em>Platform</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Platform</em>' reference.
	 * @see #getPlatform()
	 * @generated
	 */
    void setPlatform(PlatformDefinition value);

    /**
	 * Returns the value of the '<em><b>Variables</b></em>' containment reference list.
	 * The list contents are of type {@link hu.cubussapiens.modembed.model.highlevelprogram.Variable}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Variables</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Variables</em>' containment reference list.
	 * @see hu.cubussapiens.modembed.model.highlevelprogram.HighLevelProgramPackage#getHighLevelProgram_Variables()
	 * @model containment="true"
	 * @generated
	 */
    EList<Variable> getVariables();

    /**
	 * Returns the value of the '<em><b>Steps</b></em>' containment reference list.
	 * The list contents are of type {@link hu.cubussapiens.modembed.model.highlevelprogram.HLStep}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Steps</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Steps</em>' containment reference list.
	 * @see hu.cubussapiens.modembed.model.highlevelprogram.HighLevelProgramPackage#getHighLevelProgram_Steps()
	 * @model containment="true"
	 * @generated
	 */
    EList<HLStep> getSteps();
}
