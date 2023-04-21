package ru.dpelevin.gddc.gui.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HotkeyChangeListner;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.HotkeyChangedEvent;
import org.eclipse.swt.widgets.HotkeyText;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import ru.dpelevin.gddc.command.UpdateApplicationPropertiesCommand;
import ru.dpelevin.gddc.model.ApplicationProperties;
import ru.dpelevin.gddc.service.facade.ApplicationPropertiesManagerStaticFacade;
import ru.dpelevin.gddc.service.facade.ResourceManagerStaticFacade;
import ru.dpelevin.gddc.service.facade.WidgetFactoryStaticFacade;
import ru.dpelevin.gddc.service.facade.WindowManagerStaticFacade;
import ru.dpelevin.service.facade.ApplicationCommandPublisherStaticFacade;

/**
 * Dialog for preferences management.
 * 
 * @author Dmitry Pelevin
 */
public class PreferencesDialog extends Shell {

    private Button hotkeyModifierShift;

    private Button hotkeyModifierCtrl;

    private HotkeyText hotkeyKeyText;

    private Button hotkeyModifierAlt;

    private FormData hotkeyModifierAltFormData;

    private FormData hotkeyModifierCtrlFormData;

    private Group hotkeyGroup;

    private FormData hotkeyKeyTextFormData;

    private FormData hotkeyGroupFormData;

    private Button okButton;

    private Button cancelButton;

    private TabFolder tabFolder;

    private FormData okButtonFormData;

    private FormData hotkeyModifierShiftFormData;

    private ApplicationProperties applicationProperties;

    private Label hotkeyKeyLable;

    private Button translateByDoubleCtrlInsert;

    private Button translateByDoubleCtrlC;

    private EscKeyListner escKeyListner;

    /**
	 * Launch the application.
	 * 
	 * @param args
	 *            the args
	 */
    public static void main(final String[] args) {
        try {
            Display display = Display.getDefault();
            PreferencesDialog shell = new PreferencesDialog(display, true, true);
            shell.open();
            shell.layout();
            while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Create the shell.
	 * 
	 * @param display
	 *            the display
	 * @param enableTranslateByDoubleCtrlInsert
	 *            the enable translate by double ctrl insert
	 * @param enableTranslateByDoubleCtrlC
	 *            the enable translate by double ctrl c
	 */
    public PreferencesDialog(final Display display, final boolean enableTranslateByDoubleCtrlInsert, final boolean enableTranslateByDoubleCtrlC) {
        super(display, SWT.SHELL_TRIM);
        escKeyListner = new EscKeyListner();
        display.addFilter(SWT.KeyUp, escKeyListner);
        setMinimumSize(new Point(400, 366));
        setImage(ResourceManagerStaticFacade.getImage(PreferencesDialog.class, "/tray_icon.png"));
        setLayout(new FormLayout());
        {
            tabFolder = new TabFolder(this, SWT.NONE);
            {
                FormData formData = new FormData();
                formData.bottom = new FormAttachment(100, -45);
                formData.right = new FormAttachment(100, -10);
                formData.top = new FormAttachment(0, 10);
                formData.left = new FormAttachment(0, 10);
                tabFolder.setLayoutData(formData);
            }
            {
                TabItem generalTab = new TabItem(tabFolder, SWT.NONE);
                generalTab.setText("Hotkeys");
                {
                    Composite generalTabComposite = new Composite(tabFolder, SWT.NONE);
                    generalTabComposite.setLayout(new FormLayout());
                    generalTab.setControl(generalTabComposite);
                    {
                        hotkeyGroup = new Group(generalTabComposite, SWT.NONE);
                        {
                            hotkeyGroupFormData = new FormData();
                            hotkeyGroupFormData.left = new FormAttachment(0, 10);
                            hotkeyGroupFormData.right = new FormAttachment(100, -10);
                            hotkeyGroupFormData.top = new FormAttachment(0);
                            hotkeyGroupFormData.height = 86;
                            hotkeyGroupFormData.width = 409;
                            hotkeyGroup.setLayoutData(hotkeyGroupFormData);
                        }
                        hotkeyGroup.setLayout(new FormLayout());
                        hotkeyGroup.setText("Show main window");
                        {
                            hotkeyModifierShift = new Button(hotkeyGroup, SWT.CHECK);
                            {
                                hotkeyModifierShiftFormData = new FormData();
                                hotkeyModifierShiftFormData.top = new FormAttachment(0, 30);
                                hotkeyModifierShiftFormData.left = new FormAttachment(0, 21);
                                hotkeyModifierShift.setLayoutData(hotkeyModifierShiftFormData);
                            }
                            hotkeyModifierShift.setText("Shift");
                        }
                        {
                            hotkeyModifierCtrl = new Button(hotkeyGroup, SWT.CHECK);
                            {
                                hotkeyModifierCtrlFormData = new FormData();
                                hotkeyModifierCtrlFormData.top = new FormAttachment(0, 30);
                                hotkeyModifierCtrlFormData.left = new FormAttachment(hotkeyModifierShift, 21);
                                hotkeyModifierCtrl.setLayoutData(hotkeyModifierCtrlFormData);
                            }
                            hotkeyModifierCtrl.setText("Ctrl");
                        }
                        {
                            hotkeyModifierAlt = new Button(hotkeyGroup, SWT.CHECK);
                            {
                                hotkeyModifierAltFormData = new FormData();
                                hotkeyModifierAltFormData.top = new FormAttachment(0, 30);
                                hotkeyModifierAltFormData.left = new FormAttachment(hotkeyModifierCtrl, 21);
                                hotkeyModifierAlt.setLayoutData(hotkeyModifierAltFormData);
                            }
                            hotkeyModifierAlt.setText("Alt");
                        }
                        {
                            hotkeyKeyLable = new Label(hotkeyGroup, SWT.NONE);
                            {
                                FormData formData = new FormData();
                                formData.left = new FormAttachment(hotkeyModifierAlt, 21);
                                formData.top = new FormAttachment(0, 31);
                                hotkeyKeyLable.setLayoutData(formData);
                            }
                            hotkeyKeyLable.setText("Key:");
                        }
                        {
                            hotkeyKeyText = WidgetFactoryStaticFacade.createHotkeyText(hotkeyGroup, SWT.BORDER);
                            hotkeyKeyText.addHotkeyChangeListener(new HotkeyChangeListner() {

                                public void hotkeyChanged(HotkeyChangedEvent event) {
                                    HotkeyText t;
                                    t = (HotkeyText) event.widget;
                                    t.setData(event.keyCode);
                                }
                            });
                            {
                                hotkeyKeyTextFormData = new FormData();
                                hotkeyKeyTextFormData.left = new FormAttachment(hotkeyKeyLable, 21);
                                hotkeyKeyTextFormData.top = new FormAttachment(0, 28);
                                hotkeyKeyTextFormData.right = new FormAttachment(100, -25);
                                hotkeyKeyText.setLayoutData(hotkeyKeyTextFormData);
                            }
                        }
                    }
                    {
                        Group clipboardTranslationHotkeyGroup = new Group(generalTabComposite, SWT.NONE);
                        clipboardTranslationHotkeyGroup.setLayout(new FormLayout());
                        clipboardTranslationHotkeyGroup.setText("Clipboard translation");
                        {
                            FormData formData = new FormData();
                            formData.top = new FormAttachment(hotkeyGroup, 6);
                            formData.bottom = new FormAttachment(100, -10);
                            formData.left = new FormAttachment(0, 10);
                            formData.right = new FormAttachment(100, -10);
                            clipboardTranslationHotkeyGroup.setLayoutData(formData);
                        }
                        {
                            translateByDoubleCtrlInsert = new Button(clipboardTranslationHotkeyGroup, SWT.CHECK);
                            translateByDoubleCtrlInsert.setEnabled(enableTranslateByDoubleCtrlInsert);
                            {
                                FormData translateByDoubleCtrlInsertFormData = new FormData();
                                translateByDoubleCtrlInsertFormData = new FormData();
                                translateByDoubleCtrlInsertFormData.top = new FormAttachment(25, 0);
                                translateByDoubleCtrlInsertFormData.left = new FormAttachment(0, 21);
                                translateByDoubleCtrlInsert.setLayoutData(translateByDoubleCtrlInsertFormData);
                            }
                            translateByDoubleCtrlInsert.setText("Translate by \"Ctrl+Ins+Ins\"");
                        }
                        {
                            translateByDoubleCtrlC = new Button(clipboardTranslationHotkeyGroup, SWT.CHECK);
                            translateByDoubleCtrlC.setEnabled(enableTranslateByDoubleCtrlC);
                            {
                                FormData translateByDoubleCtrlCFormData = new FormData();
                                translateByDoubleCtrlCFormData = new FormData();
                                translateByDoubleCtrlCFormData.top = new FormAttachment(55, 0);
                                translateByDoubleCtrlCFormData.left = new FormAttachment(0, 21);
                                translateByDoubleCtrlC.setLayoutData(translateByDoubleCtrlCFormData);
                            }
                            translateByDoubleCtrlC.setText("Translate by \"Ctrl+C+C\"");
                        }
                    }
                }
            }
        }
        {
            okButton = new Button(this, SWT.NONE);
            okButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    PreferencesDialog.this.submit();
                }
            });
            {
                okButtonFormData = new FormData();
                okButtonFormData.bottom = new FormAttachment(100, -10);
                okButtonFormData.width = 92;
                okButton.setLayoutData(okButtonFormData);
            }
            okButton.setText("Ok");
        }
        {
            cancelButton = new Button(this, SWT.NONE);
            cancelButton.addSelectionListener(new SelectionAdapter() {

                @Override
                public void widgetSelected(final SelectionEvent e) {
                    PreferencesDialog.this.dispose();
                }
            });
            okButtonFormData.right = new FormAttachment(cancelButton, -6);
            {
                FormData formData = new FormData();
                formData.bottom = new FormAttachment(okButton, 0, SWT.BOTTOM);
                formData.right = new FormAttachment(tabFolder, 0, SWT.RIGHT);
                formData.width = 92;
                cancelButton.setLayoutData(formData);
            }
            cancelButton.setText("Cancel");
        }
        createContents();
        applicationProperties = ApplicationPropertiesManagerStaticFacade.getProperties();
        hotkeyModifierShift.setSelection(applicationProperties.isHotkeyModifierShift());
        hotkeyModifierCtrl.setSelection(applicationProperties.isHotkeyModifierCtrl());
        hotkeyModifierAlt.setSelection(applicationProperties.isHotkeyModifierAlt());
        hotkeyKeyText.setText(WindowManagerStaticFacade.getTextForKeyCode(applicationProperties.getHotkeyVirtualKeyCode()));
        hotkeyKeyText.setData(applicationProperties.getHotkeyVirtualKeyCode());
        translateByDoubleCtrlInsert.setSelection(applicationProperties.isTranslateByDoubleCtrlInsert());
        translateByDoubleCtrlC.setSelection(applicationProperties.isTranslateByDoubleCtrlC());
    }

    /**
	 * Create contents of the shell.
	 */
    protected final void createContents() {
        setText("Preferences");
        setSize(400, 366);
    }

    /**
	 * Check subclass.
	 */
    @Override
    protected void checkSubclass() {
    }

    /**
	 * Submit.
	 */
    protected final void submit() {
        applicationProperties.setHotkeyModifierShift(hotkeyModifierShift.getSelection());
        applicationProperties.setHotkeyModifierCtrl(hotkeyModifierCtrl.getSelection());
        applicationProperties.setHotkeyModifierAlt(hotkeyModifierAlt.getSelection());
        applicationProperties.setHotkeyVirtualKeyCode((Integer) hotkeyKeyText.getData());
        applicationProperties.setTranslateByDoubleCtrlInsert(translateByDoubleCtrlInsert.getSelection());
        applicationProperties.setTranslateByDoubleCtrlC(translateByDoubleCtrlC.getSelection());
        ApplicationCommandPublisherStaticFacade.publishCommand(new UpdateApplicationPropertiesCommand(this, applicationProperties));
        PreferencesDialog.this.dispose();
    }

    /**
	 * Dispose.
	 */
    @Override
    public final void dispose() {
        getDisplay().removeFilter(SWT.KeyUp, escKeyListner);
        super.dispose();
    }

    /**
	 * The Class EscKeyListner.
	 */
    protected class EscKeyListner implements Listener {

        /**
		 * Handle event.
		 * 
		 * @param e
		 *            the e
		 */
        public final void handleEvent(final Event e) {
            if (e.keyCode == SWT.ESC) {
                PreferencesDialog.this.dispose();
            }
        }
    }
}
