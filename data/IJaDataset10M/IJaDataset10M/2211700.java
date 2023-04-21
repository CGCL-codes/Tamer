package org.eclipse.swt.examples.controls;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;

class SpinnerTab extends RangeTab {

    Spinner spinner1;

    Group spinnerGroup;

    Button readOnlyButton, wrapButton;

    Spinner incrementSpinner, pageIncrementSpinner, digitsSpinner;

    /**
	 * Creates the Tab within a given instance of ControlExample.
	 */
    SpinnerTab(ControlExample instance) {
        super(instance);
    }

    /**
	 * Creates the "Control" widget children.
	 */
    void createControlWidgets() {
        super.createControlWidgets();
        createIncrementGroup();
        createPageIncrementGroup();
        createDigitsGroup();
    }

    /**
	 * Creates the "Example" group.
	 */
    void createExampleGroup() {
        super.createExampleGroup();
        spinnerGroup = new Group(exampleGroup, SWT.NONE);
        spinnerGroup.setLayout(new GridLayout());
        spinnerGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        spinnerGroup.setText("Spinner");
    }

    /**
	 * Creates the "Example" widgets.
	 */
    void createExampleWidgets() {
        int style = getDefaultStyle();
        if (readOnlyButton.getSelection()) style |= SWT.READ_ONLY;
        if (borderButton.getSelection()) style |= SWT.BORDER;
        if (wrapButton.getSelection()) style |= SWT.WRAP;
        spinner1 = new Spinner(spinnerGroup, style);
    }

    /**
	 * Create a group of widgets to control the increment
	 * attribute of the example widget.
	 */
    void createIncrementGroup() {
        Group incrementGroup = new Group(controlGroup, SWT.NONE);
        incrementGroup.setLayout(new GridLayout());
        incrementGroup.setText(ControlExample.getResourceString("Increment"));
        incrementGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        incrementSpinner = new Spinner(incrementGroup, SWT.BORDER);
        incrementSpinner.setMaximum(100000);
        incrementSpinner.setSelection(getDefaultIncrement());
        incrementSpinner.setPageIncrement(100);
        incrementSpinner.setIncrement(1);
        incrementSpinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        incrementSpinner.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                setWidgetIncrement();
            }
        });
    }

    /**
	 * Create a group of widgets to control the page increment
	 * attribute of the example widget.
	 */
    void createPageIncrementGroup() {
        Group pageIncrementGroup = new Group(controlGroup, SWT.NONE);
        pageIncrementGroup.setLayout(new GridLayout());
        pageIncrementGroup.setText(ControlExample.getResourceString("Page_Increment"));
        pageIncrementGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        pageIncrementSpinner = new Spinner(pageIncrementGroup, SWT.BORDER);
        pageIncrementSpinner.setMaximum(100000);
        pageIncrementSpinner.setSelection(getDefaultPageIncrement());
        pageIncrementSpinner.setPageIncrement(100);
        pageIncrementSpinner.setIncrement(1);
        pageIncrementSpinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        pageIncrementSpinner.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent event) {
                setWidgetPageIncrement();
            }
        });
    }

    /**
	 * Create a group of widgets to control the digits
	 * attribute of the example widget.
	 */
    void createDigitsGroup() {
        Group digitsGroup = new Group(controlGroup, SWT.NONE);
        digitsGroup.setLayout(new GridLayout());
        digitsGroup.setText(ControlExample.getResourceString("Digits"));
        digitsGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        digitsSpinner = new Spinner(digitsGroup, SWT.BORDER);
        digitsSpinner.setMaximum(100000);
        digitsSpinner.setSelection(getDefaultDigits());
        digitsSpinner.setPageIncrement(100);
        digitsSpinner.setIncrement(1);
        digitsSpinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
        digitsSpinner.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                setWidgetDigits();
            }
        });
    }

    /**
	 * Creates the tab folder page.
	 *
	 * @param tabFolder org.eclipse.swt.widgets.TabFolder
	 * @return the new page for the tab folder
	 */
    Composite createTabFolderPage(TabFolder tabFolder) {
        super.createTabFolderPage(tabFolder);
        tabFolderPage.addControlListener(new ControlAdapter() {

            public void controlResized(ControlEvent e) {
                setExampleWidgetSize();
            }
        });
        return tabFolderPage;
    }

    /**
	 * Creates the "Style" group.
	 */
    void createStyleGroup() {
        orientationButtons = false;
        super.createStyleGroup();
        readOnlyButton = new Button(styleGroup, SWT.CHECK);
        readOnlyButton.setText("SWT.READ_ONLY");
        wrapButton = new Button(styleGroup, SWT.CHECK);
        wrapButton.setText("SWT.WRAP");
    }

    /**
	 * Gets the "Example" widget children.
	 */
    Control[] getExampleWidgets() {
        return new Control[] { spinner1 };
    }

    /**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
    String[] getMethodNames() {
        return new String[] { "Selection", "ToolTipText" };
    }

    /**
	 * Gets the text for the tab folder item.
	 */
    String getTabText() {
        return "Spinner";
    }

    /**
	 * Sets the state of the "Example" widgets.
	 */
    void setExampleWidgetState() {
        super.setExampleWidgetState();
        readOnlyButton.setSelection((spinner1.getStyle() & SWT.READ_ONLY) != 0);
        wrapButton.setSelection((spinner1.getStyle() & SWT.WRAP) != 0);
        if (!instance.startup) {
            setWidgetIncrement();
            setWidgetPageIncrement();
            setWidgetDigits();
        }
    }

    /**
	 * Gets the default maximum of the "Example" widgets.
	 */
    int getDefaultMaximum() {
        return spinner1.getMaximum();
    }

    /**
	 * Gets the default minimim of the "Example" widgets.
	 */
    int getDefaultMinimum() {
        return spinner1.getMinimum();
    }

    /**
	 * Gets the default selection of the "Example" widgets.
	 */
    int getDefaultSelection() {
        return spinner1.getSelection();
    }

    /**
	 * Gets the default increment of the "Example" widgets.
	 */
    int getDefaultIncrement() {
        return spinner1.getIncrement();
    }

    /**
	 * Gets the default page increment of the "Example" widgets.
	 */
    int getDefaultPageIncrement() {
        return spinner1.getPageIncrement();
    }

    /**
	 * Gets the default digits of the "Example" widgets.
	 */
    int getDefaultDigits() {
        return spinner1.getDigits();
    }

    /**
	 * Sets the increment of the "Example" widgets.
	 */
    void setWidgetIncrement() {
        spinner1.setIncrement(incrementSpinner.getSelection());
    }

    /**
	 * Sets the minimim of the "Example" widgets.
	 */
    void setWidgetMaximum() {
        spinner1.setMaximum(maximumSpinner.getSelection());
    }

    /**
	 * Sets the minimim of the "Example" widgets.
	 */
    void setWidgetMinimum() {
        spinner1.setMinimum(minimumSpinner.getSelection());
    }

    /**
	 * Sets the page increment of the "Example" widgets.
	 */
    void setWidgetPageIncrement() {
        spinner1.setPageIncrement(pageIncrementSpinner.getSelection());
    }

    /**
	 * Sets the digits of the "Example" widgets.
	 */
    void setWidgetDigits() {
        spinner1.setDigits(digitsSpinner.getSelection());
    }

    /**
	 * Sets the selection of the "Example" widgets.
	 */
    void setWidgetSelection() {
        spinner1.setSelection(selectionSpinner.getSelection());
    }
}
