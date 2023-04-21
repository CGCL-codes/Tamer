package gov.ornl.nice.niceclient.eclipseuiwidgets;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import gov.ornl.nice.niceclient.iniceclient.uiwidgets.IExtraInfoWidget;
import gov.ornl.nice.nicedatastructures.form.DataComponent;
import gov.ornl.nice.nicedatastructures.form.Form;
import java.util.ArrayList;
import gov.ornl.nice.niceclient.iniceclient.uiwidgets.IWidgetClosedListener;

/** 
 * <!-- begin-UML-doc -->
 * <p>This class implements the IExtraInfoWidget interface for Eclipse. It creates an ExtraInfoDialog in sync with the Eclipse workbench and it delegates all listener management to the dialog.</p>
 * <!-- end-UML-doc -->
 * @author bkj
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class EclipseExtraInfoWidget implements IExtraInfoWidget {

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>The Form containing the extra information request.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private Form niceForm;

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>The JFace dialog to use for rendering the DataComponent.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private ExtraInfoDialog infoDialog;

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>The IWidgetClosedListeners who have subscribed to this widget for updates. This list is maintained because the ExtraInfoDialog is created on a separate thread and the listeners can not be automatically forwarded to it.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private ArrayList<IWidgetClosedListener> listeners;

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>The constructor</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public EclipseExtraInfoWidget() {
        listeners = new ArrayList<IWidgetClosedListener>();
        return;
    }

    /** 
	 * (non-Javadoc)
	 * @see IExtraInfoWidget#display()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public void display() {
        PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

            public void run() {
                Display display;
                Shell shell;
                display = PlatformUI.getWorkbench().getDisplay();
                shell = display.getActiveShell();
                infoDialog = new ExtraInfoDialog(shell);
                infoDialog.setDataComponent((DataComponent) niceForm.getComponents().get(0));
                for (IWidgetClosedListener i : listeners) infoDialog.setCloseListener(i);
                infoDialog.open();
            }
        });
    }

    /** 
	 * (non-Javadoc)
	 * @see IExtraInfoWidget#setForm(Form form)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public void setForm(Form form) {
        niceForm = form;
        return;
    }

    /** 
	 * (non-Javadoc)
	 * @see IExtraInfoWidget#getForm()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public Form getForm() {
        return niceForm;
    }

    /** 
	 * (non-Javadoc)
	 * @see IExtraInfoWidget#setCloseListener(IWidgetClosedListener listener)
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public void setCloseListener(IWidgetClosedListener listener) {
        if (listener != null) listeners.add(listener);
        return;
    }
}
