package gebruikersInterfaceLaag.listeners;

import java.util.EventListener;
import javax.swing.event.EventListenerList;

/** 
 * <!-- begin-UML-doc -->
 * <!-- end-UML-doc -->
 * @author Wilco
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public abstract class ListenerList {

    /** 
	 * <!-- begin-UML-doc -->
	 * <HTML><HEAD>
	 * <META name=GENERATOR content="MSHTML 8.00.7600.16722"></HEAD>
	 * <BODY>Abstracte klasse ListenerList</BODY></HTML>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    protected EventListenerList eventListenerList = new EventListenerList();

    /** 
	 * <!-- begin-UML-doc -->
	 * <HTML><HEAD>
	 * <META content="MSHTML 6.00.6000.17095" name=GENERATOR></HEAD>
	 * <BODY>Deze methode voegt een listener toe aan de listenerlist</BODY></HTML>
	 * <!-- end-UML-doc -->
	 * @param l
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public void addListener(EventListener l) {
        eventListenerList.add(EventListener.class, l);
    }

    /** 
	 * <!-- begin-UML-doc -->
	 * <HTML><HEAD>
	 * <META content="MSHTML 6.00.6000.17095" name=GENERATOR></HEAD>
	 * <BODY>Deze methode verwijdert alle listeners uit de listenerlist</BODY></HTML>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public void removeAllListeners() {
        for (EventListener l : eventListenerList.getListeners(EventListener.class)) eventListenerList.remove(EventListener.class, l);
    }
}
