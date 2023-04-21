package com.amac;

import ViewerBlackBoxViews.ViewerContext.Viewer;
import DomainWhiteBoxViews.DomainArchitectures.DomainLogicalArchitecture.NiCECorePackage.NiCECore;

/** 
 * <!-- begin-UML-doc -->
 * The Launcher class is called to bootstrap NiCE. All methods on this class must be static since it is called by main().
 * <!-- end-UML-doc -->
 * @author jaybilly
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class AndroidLauncher {

    /** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private Viewer viewer;

    /** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private static NiCECore niCECore;

    /** 
	 * <!-- begin-UML-doc -->
	 * The launch operation starts NiCE.<br />
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public void launch() {
        viewer.open();
        niCECore.start();
    }

    /** 
	 * <!-- begin-UML-doc -->
	 * This is the default constructor.
	 * <!-- end-UML-doc -->
	 */
    public AndroidLauncher() {
        viewer = new Viewer(new AndroidWindow());
        niCECore = new AndroidNiCECore();
        niCECore.registerViewer(viewer);
        viewer.setNiCECore(niCECore);
    }

    /** 
	 * <!-- begin-UML-doc -->
	 * This is an alternative constructor that allows dependencies to be overridden and it is used primarily for testing.
	 * <!-- end-UML-doc -->
	 * @param altViewer
	 * @param altCore
	 */
    public AndroidLauncher(Viewer altViewer, NiCECore altCore) {
        viewer = altViewer;
        niCECore = altCore;
        viewer.setNiCECore(niCECore);
        niCECore.registerViewer(viewer);
    }

    public void launchNewItem(String item) {
        viewer.createNewItemFromString(item);
        return;
    }
}
