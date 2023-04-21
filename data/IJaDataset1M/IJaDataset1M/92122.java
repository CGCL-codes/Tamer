package org.eclipse.uml2.uml.internal.operations;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.UniqueEList;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.uml2.common.util.UnionEObjectEList;
import org.eclipse.uml2.uml.Artifact;
import org.eclipse.uml2.uml.DeployedArtifact;
import org.eclipse.uml2.uml.Deployment;
import org.eclipse.uml2.uml.DeploymentTarget;
import org.eclipse.uml2.uml.Manifestation;
import org.eclipse.uml2.uml.PackageableElement;
import org.eclipse.uml2.uml.UMLPackage;

/**
 * <!-- begin-user-doc -->
 * A static utility class that provides operations related to '<em><b>Deployment Target</b></em>' model objects.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following operations are supported:
 * <ul>
 *   <li>{@link org.eclipse.uml2.uml.DeploymentTarget#getDeployedElements() <em>Get Deployed Elements</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DeploymentTargetOperations extends NamedElementOperations {

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
    protected DeploymentTargetOperations() {
        super();
    }

    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * result = ((self.deployment->collect(deployedArtifact))->collect(manifestation))->collect(utilizedElement)
	 * @param deploymentTarget The receiving '<em><b>Deployment Target</b></em>' model object.
	 * <!-- end-model-doc -->
	 * @generated NOT
	 */
    public static EList<PackageableElement> getDeployedElements(DeploymentTarget deploymentTarget) {
        EList<PackageableElement> deployedElements = new UniqueEList.FastCompare<PackageableElement>();
        for (Deployment deployment : deploymentTarget.getDeployments()) {
            for (DeployedArtifact deployedArtifact : deployment.getDeployedArtifacts()) {
                if (deployedArtifact instanceof Artifact) {
                    for (Manifestation manifestation : ((Artifact) deployedArtifact).getManifestations()) {
                        PackageableElement utilizedElement = (PackageableElement) manifestation.eGet(UMLPackage.Literals.MANIFESTATION__UTILIZED_ELEMENT, false);
                        if (utilizedElement != null) {
                            deployedElements.add(utilizedElement);
                        }
                    }
                }
            }
        }
        return new UnionEObjectEList<PackageableElement>((InternalEObject) deploymentTarget, UMLPackage.Literals.DEPLOYMENT_TARGET__DEPLOYED_ELEMENT, deployedElements.size(), deployedElements.toArray());
    }
}
