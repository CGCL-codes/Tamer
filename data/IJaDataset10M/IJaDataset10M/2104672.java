package gov.ornl.nice.niceclient.eclipseuiwidgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Combo;
import java.util.ArrayList;
import org.eclipse.swt.widgets.Button;
import org.eclipse.ui.forms.widgets.FormToolkit;
import gov.ornl.nice.nicedatastructures.form.AllowedValueType;
import gov.ornl.nice.nicedatastructures.form.Entry;

/** 
 * <!-- begin-UML-doc -->
 * <p>This is an subclass of SWT's Composite class made specifically to work with NiCE Entries.</p><p>Changes to this class are broadcasted using SWT's event system. Marking the FormEditor as dirty, for example, should be handled by registering an event listener with instances of this class and catching the signal.</p>
 * <!-- end-UML-doc -->
 * @author bkj
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class EntryComposite extends Composite {

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>A label that describes the Entry.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private Label label;

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>A text field that is used if the Entry type is unspecified.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private Text text;

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>A drop-down menu for the Entry.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private Combo dropDown;

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>A set of buttons for the Entry.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private ArrayList<Button> buttons;

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>The Entry that is displayed by the EntryComposite.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private Entry entry;

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>The Constructor</p>
	 * <!-- end-UML-doc -->
	 * @param parent <p>The parent Composite.</p>
	 * @param style <p>The style of the EntryComposite.</p>
	 * @param refEntry <p>An Entry that should be used to create the widget, to update when changed by the user and to be updated from when changed internally by NiCE.</p>
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public EntryComposite(Composite parent, int style, Entry refEntry) {
        super(parent, style);
        if (refEntry != null) entry = refEntry; else throw new RuntimeException("Entry passed to EntryComposite " + "constructor cannot be null!");
        buttons = new ArrayList<Button>();
        render();
        return;
    }

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation creates buttons on the Composite.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private void createButtons() {
        Button tmpButton = null;
        for (String i : entry.getAllowedValues()) {
            tmpButton = new Button(this, SWT.RADIO);
            tmpButton.setText(i);
            tmpButton.setToolTipText(entry.getDescription());
            if (i.equals(entry.getValue())) tmpButton.setSelection(true);
            tmpButton.addSelectionListener(new SelectionListener() {

                @Override
                public void widgetSelected(SelectionEvent e) {
                    EntryComposite.this.notifyListeners(SWT.Selection, new Event());
                    entry.setValue(((Button) e.item).getText());
                }

                @Override
                public void widgetDefaultSelected(SelectionEvent e) {
                    EntryComposite.this.notifyListeners(SWT.Selection, new Event());
                    entry.setValue(((Button) e.item).getText());
                }
            });
            buttons.add(tmpButton);
        }
        return;
    }

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation creates a drop-down menu on the Composite.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private void createDropdown() {
        dropDown = new Combo(this, SWT.DROP_DOWN | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL);
        for (String i : entry.getAllowedValues()) dropDown.add(i);
        dropDown.select(dropDown.indexOf(entry.getValue()));
        dropDown.addSelectionListener(new SelectionListener() {

            @Override
            public void widgetSelected(SelectionEvent e) {
                EntryComposite.this.notifyListeners(SWT.Selection, new Event());
                entry.setValue(dropDown.getItem(dropDown.getSelectionIndex()));
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
                EntryComposite.this.notifyListeners(SWT.Selection, new Event());
                entry.setValue(dropDown.getItem(dropDown.getSelectionIndex()));
            }
        });
        return;
    }

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation creates a textfield on the Composite.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private void createTextfield() {
        if (!entry.isSecret()) text = new Text(this, SWT.LEFT | SWT.BORDER); else text = new Text(this, SWT.LEFT | SWT.BORDER | SWT.PASSWORD);
        text.setToolTipText(entry.getDescription());
        text.setText(entry.getValue());
        text.setLayoutData(new GridData(255, SWT.DEFAULT));
        text.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
            }

            ;

            @Override
            public void focusLost(FocusEvent e) {
                EntryComposite.this.notifyListeners(SWT.Selection, new Event());
                entry.setValue(text.getText());
            }

            ;
        });
        this.addListener(SWT.DefaultSelection, new Listener() {

            public void handleEvent(Event e) {
                EntryComposite.this.notifyListeners(SWT.Selection, new Event());
                entry.setValue(text.getText());
                System.out.println("Default selection selected!");
            }
        });
        return;
    }

    /** 
	 * <!-- begin-UML-doc -->
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private void createLabel() {
        label = new Label(this, 0);
        label.setText(entry.getName() + ":");
        label.setToolTipText(entry.getDescription());
        return;
    }

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation renders the SWT widgets for the Entry.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    private void render() {
        int numAllowedValues = 0, maxValueLength = 12, maxShortValues = 8;
        boolean shortValues = true;
        AllowedValueType valueType = null;
        createLabel();
        valueType = entry.getValueType();
        numAllowedValues = entry.getAllowedValues().size();
        for (String i : entry.getAllowedValues()) {
            if (i.length() > maxValueLength) {
                shortValues = false;
                break;
            }
        }
        setLayout(new GridLayout());
        if (valueType == AllowedValueType.Discrete) {
            if (numAllowedValues <= maxShortValues && shortValues) {
                createButtons();
            } else {
                createDropdown();
            }
        } else {
            createTextfield();
        }
        return;
    }

    /** 
	 * <!-- begin-UML-doc -->
	 * <p>This operation directs the EntryComposite to refresh its view of the Entry. This should be called in the event that the Entry has changed on the file system and the view needs to be updated.</p>
	 * <!-- end-UML-doc -->
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
    public void refresh() {
        if (dropDown != null) dropDown.dispose();
        if (label != null) label.dispose();
        if (text != null) text.dispose();
        for (Button i : buttons) {
            if (!i.isDisposed()) i.dispose();
        }
        render();
        layout();
        return;
    }
}
