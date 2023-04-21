package gov.ornl.nice.niceclient.test.eclipseuiwidgets;

import static org.junit.Assert.*;
import org.junit.Test;
import gov.ornl.nice.niceclient.eclipseuiwidgets.EclipseErrorBoxWidget;
import gov.ornl.nice.niceclient.eclipseuiwidgets.EclipseExtraInfoWidget;
import gov.ornl.nice.niceclient.eclipseuiwidgets.EclipseFormWidget;
import gov.ornl.nice.niceclient.eclipseuiwidgets.EclipseTextEditor;
import gov.ornl.nice.niceclient.eclipseuiwidgets.EclipseUIWidgetFactory;
import gov.ornl.nice.niceclient.iniceclient.uiwidgets.IErrorBox;
import gov.ornl.nice.niceclient.iniceclient.uiwidgets.IExtraInfoWidget;
import gov.ornl.nice.niceclient.iniceclient.uiwidgets.IFormWidget;
import gov.ornl.nice.niceclient.iniceclient.uiwidgets.ITextEditor;

/** 
 * <!-- begin-UML-doc -->
 * <p>This class is responsible for testing the EclipseUIWidgetFactory. It only checks the instance type of the widgets and makes sure that they are not null.</p>
 * <!-- end-UML-doc -->
 * @author bkj
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class EclipseUIWidgetFactoryTester {

    /** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private EclipseUIWidgetFactory eclipseUIWidgetFactory;

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation checks the Widget types and makes sure that they are not null.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    @Test
    public void checkWidgetTypes() {
        eclipseUIWidgetFactory = new EclipseUIWidgetFactory();
        IErrorBox errorBox = eclipseUIWidgetFactory.getErrorBox();
        assertNotNull(errorBox);
        assertTrue(errorBox instanceof EclipseErrorBoxWidget);
        IExtraInfoWidget extraInfo = eclipseUIWidgetFactory.getExtraInfoWidget();
        assertNotNull(extraInfo);
        assertTrue(extraInfo instanceof EclipseExtraInfoWidget);
        IFormWidget formWidget = eclipseUIWidgetFactory.getFormWidget();
        assertNotNull(formWidget);
        assertTrue(formWidget instanceof EclipseFormWidget);
        ITextEditor editor = eclipseUIWidgetFactory.getTextEditor();
        assertNotNull(editor);
        assertTrue(editor instanceof EclipseTextEditor);
        return;
    }
}
