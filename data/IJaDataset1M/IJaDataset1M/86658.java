package xgi.mclip.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import xgi.mclip.control.interfaces.ConfigController;
import xgi.mclip.factory.MClipSingletonFactory;
import xgi.mclip.model.Preferences;

/** This is the class that implements the view used to select the hot key to be pressed in order to show ui */
public class ConfigView extends Composite {

    /** check box that specifies whether or not duplicates should be popped on top of the stack */
    private Button popOnTop;

    /** check box that specifies whether or not duplicates should be popped on top of the stack */
    private Button showOnStart;

    /** check box that specifies CTRL as modifier */
    private Button ctrl;

    /** check box that specifies ALT as modifier */
    private Button alt;

    /** check box that specifies WINDOWS as modifier */
    private Button win;

    /** check box that specifies SHIFT as modifier */
    private Button shift;

    /** Text field in which to type the character to be pressed */
    private Text letter;

    /** The controller */
    private final ConfigController controller;

    /**
	 * Default constructor
	 * @param parent the parent widget
	 * @param style the SWT style
	 */
    public ConfigView(ConfigController controller, Composite parent, int style) {
        super(parent, style);
        this.controller = controller;
        createUI();
        initializeFromProperties();
        bindUIEvents();
    }

    /** initialize the gui with values from properties */
    private void initializeFromProperties() {
        ctrl.setSelection(Boolean.parseBoolean(MClipSingletonFactory.getPreferences().get(Preferences.HOTKEY_CTRL)));
        alt.setSelection(Boolean.parseBoolean(MClipSingletonFactory.getPreferences().get(Preferences.HOTKEY_ALT)));
        shift.setSelection(Boolean.parseBoolean(MClipSingletonFactory.getPreferences().get(Preferences.HOTKEY_SHIFT)));
        win.setSelection(Boolean.parseBoolean(MClipSingletonFactory.getPreferences().get(Preferences.HOTKEY_WIN)));
        letter.setText(MClipSingletonFactory.getPreferences().get(Preferences.HOTKEY_LETTER));
        popOnTop.setSelection(Boolean.parseBoolean(MClipSingletonFactory.getPreferences().get(Preferences.GENERAL_POP_ON_TOP)));
        showOnStart.setSelection(Boolean.parseBoolean(MClipSingletonFactory.getPreferences().get(Preferences.GENERAL_SHOW_ON_START)));
    }

    /** binds the buttons events to listener that 'll modify the properties */
    private void bindUIEvents() {
        Listener actionListener = new Listener() {

            @Override
            public void handleEvent(Event event) {
                if (event.widget == ctrl) {
                    controller.ctrlChanged(ctrl.getSelection());
                }
                if (event.widget == alt) {
                    controller.altChanged(alt.getSelection());
                }
                if (event.widget == shift) {
                    controller.shiftChanged(shift.getSelection());
                }
                if (event.widget == win) {
                    controller.winChanged(win.getSelection());
                }
                if (event.widget == letter) {
                    controller.letterChanged(letter.getText());
                }
                if (event.widget == popOnTop) {
                    controller.popOnTopChanged(popOnTop.getSelection());
                }
                if (event.widget == showOnStart) {
                    controller.showOnStartChanged(showOnStart.getSelection());
                }
            }
        };
        ctrl.addListener(SWT.Selection, actionListener);
        alt.addListener(SWT.Selection, actionListener);
        win.addListener(SWT.Selection, actionListener);
        shift.addListener(SWT.Selection, actionListener);
        letter.addListener(SWT.FocusOut, actionListener);
        popOnTop.addListener(SWT.Selection, actionListener);
        showOnStart.addListener(SWT.Selection, actionListener);
    }

    /** 
	 * Instanciates all widgets and lay them out. This method should be called only once !!
	 */
    private void createUI() {
        setLayout(new GridLayout());
        setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        Group historyPanel = new Group(this, SWT.NONE);
        GridData layoutInfo = new GridData(GridData.FILL_HORIZONTAL);
        layoutInfo.heightHint = 70;
        historyPanel.setLayoutData(layoutInfo);
        historyPanel.setLayout(new FillLayout(SWT.VERTICAL));
        historyPanel.setText("General options");
        popOnTop = new Button(historyPanel, SWT.CHECK);
        popOnTop.setText("Move duplicate entry to the top");
        showOnStart = new Button(historyPanel, SWT.CHECK);
        showOnStart.setText("Show main window on start");
        Group hotkeyPanel = new Group(this, SWT.NONE);
        layoutInfo = new GridData(GridData.FILL_HORIZONTAL);
        layoutInfo.heightHint = 70;
        hotkeyPanel.setLayoutData(layoutInfo);
        hotkeyPanel.setLayout(new GridLayout(6, true));
        hotkeyPanel.setText("Hotkey");
        Label keyComboMessage = new Label(hotkeyPanel, SWT.NONE);
        keyComboMessage.setForeground(new Color(getDisplay(), 255, 0, 0));
        keyComboMessage.setText("AltGr = Ctrl + Alt");
        layoutInfo = new GridData(SWT.BEGINNING, SWT.CENTER, true, true);
        layoutInfo.horizontalSpan = 6;
        keyComboMessage.setLayoutData(layoutInfo);
        ctrl = new Button(hotkeyPanel, SWT.CHECK);
        ctrl.setText("Ctrl");
        ctrl.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, true));
        alt = new Button(hotkeyPanel, SWT.CHECK);
        alt.setText("Alt");
        alt.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, true));
        win = new Button(hotkeyPanel, SWT.CHECK);
        win.setText("Win");
        win.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, true));
        shift = new Button(hotkeyPanel, SWT.CHECK);
        shift.setText("Shift");
        shift.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, true, true));
        Label label = new Label(hotkeyPanel, SWT.NULL);
        label.setText("Char");
        label.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, true));
        letter = new Text(hotkeyPanel, SWT.SINGLE | SWT.BORDER);
        layoutInfo = new GridData(SWT.BEGINNING, SWT.CENTER, true, true);
        layoutInfo.widthHint = 40;
        letter.setLayoutData(layoutInfo);
    }
}
