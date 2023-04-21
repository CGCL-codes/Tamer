package de.olypedia;

import de.olypedia.ImagingSetting.ImagingSettingException;
import de.olypedia.resources.i18n.LocalizationSupport;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.ImageItem;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.ItemStateListener;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.*;
import org.netbeans.microedition.lcdui.SimpleTableModel;
import org.netbeans.microedition.lcdui.TableItem;

/**
 * This is the actual MIDlet for the DoF calculator
 *
 * @author khb
 */
public class DofCalculator extends MIDlet implements CommandListener, ItemCommandListener, ImagingChangeListener, ItemStateListener {

    /** The model responsible for managing changes to the underlying data */
    private ImagingSettingModel _model;

    /** A proxy managing persistent storage of preferences */
    private RecordStoreHandler _store;

    /** Marker for whether or not the midlet is paused */
    private boolean midletPaused = false;

    /**
     * Reference to the main form. The thing here is, that we need to create
     * new forms whenever the calculation target ChoiceGroup is changed,
     * because we need to make the selected TextField uneditable. Due to a
     * bug in MIDP, making it editable again throws an exception. Therefore,
     * we completely recreate the form and all field every time a different
     * target is selected.
     * For that reason, we also keep references to the currently displayed
     * fields here.
     *
     * @todo: maybe refactor this to always use getters from the DofFormFactory
     * and add a switch to the getter for the main form to specify whether or
     * not the form and fields should be recreated.
     */
    private Form form;

    /** Reference to the currently used ChoiceGroup for the calculation target */
    private ChoiceGroup choiceGroupCalculate;

    /** Reference to the currently used TextField for focal length */
    private TextField textFieldFocalLength;

    /** Reference to the currently used TextField for aperture */
    private TextField textFieldAperture;

    /** Reference to the currently used TextField for subject distance */
    private TextField textFieldDistance;

    /** Reference to the currently used TextField for depth-of-field */
    private TextField textFieldDepthOfField;

    /** Reference to the currently used TableItem for the results */
    private TableItem tableResult;

    /**
     * Reference to the currently used SimpleTableModel used by the
     * TableItem above
     */
    private SimpleTableModel tableModelResult;

    /** 
     * Form to return to from all Alerts or Lists which need to return to 
     * the main form.
     * Other Displayables may open from well-defined locations as the
     * preferences screen and return there directly. The Online help can
     * be opened from anywhere and therefore needs its own return pointer
     * to not interfere with the return path if called from e. g. the 
     * preferences UI.
     */
    private Displayable returnTo;

    /** The Item to activate/select after returning, if any */
    private Item returnToItem;

    /** Form to return to when dismissing the online help */
    private Displayable helpReturnTo;

    /** The Item to activate/select after returning from the online help, if any */
    private Item helpReturnToItem;

    /** The List display for aperture values to chose from */
    private List aperturesList;

    /** The List display for circle of confusion values to chose from */
    private List cocsList;

    /** Reference to the preferences form */
    private Form prefForm;

    /** Reference to the TextField for coc preference */
    private TextField textFieldPrefCoC;

    /** Reference to the TextField for default calculation target preference */
    private ChoiceGroup choiceGroupPrefDefCalcTarget;

    /** Reference to the TextField for aperture stop scale preference */
    private ChoiceGroup choiceGroupPrefStopScale;

    /** The number of TextFields present on the main display */
    private static final int NUM_TEXT_FIELDS = 4;

    /** Valid choices for the coc list */
    private static final String[][] CIRCLE_OF_CONFUSION_CHOICES = new String[][] { { LocalizationSupport.getMessage("CHOICE_CIRCLE_OF_CONFUSION_35_TRAD"), "0.02884" }, { LocalizationSupport.getMessage("CHOICE_CIRCLE_OF_CONFUSION_APSC_TRAD"), "0.01803" }, { LocalizationSupport.getMessage("CHOICE_CIRCLE_OF_CONFUSION_FT_TRAD"), "0.0147" }, { LocalizationSupport.getMessage("CHOICE_CIRCLE_OF_CONFUSION_FT_5MP"), "0.01341" }, { LocalizationSupport.getMessage("CHOICE_CIRCLE_OF_CONFUSION_FT_8MP"), "0.01060" }, { LocalizationSupport.getMessage("CHOICE_CIRCLE_OF_CONFUSION_FT_10MP"), "0.00948" }, { LocalizationSupport.getMessage("CHOICE_CIRCLE_OF_CONFUSION_FT_12MP"), "0.00855" } };

    /** User wants a full-stop scale in the aperture list */
    public static final int APERTURE_FULL_STOPS = 0;

    /** User wants a one-half-stop scale in the aperture list */
    public static final int APERTURE_HALF_STOPS = 1;

    /** User wants a one-third-stop scale in the aperture list */
    public static final int APERTURE_THRID_STOPS = 2;

    public void startApp() {
        if (midletPaused) {
            resumeMIDlet();
        } else {
            startMIDlet();
        }
        midletPaused = false;
    }

    public void pauseApp() {
        midletPaused = true;
    }

    public void destroyApp(boolean unconditional) {
    }

    public void startMIDlet() {
        Alert error = null;
        try {
            _store = new RecordStoreHandler();
            _model = new ImagingSettingModel(_store.getDefaultCalculationTarget(), _store.getCircleOfConfusion(), 54, 3.5, 1000, 33);
        } catch (ImagingSettingException ie) {
            error = new Alert(LocalizationSupport.getMessage("EXCEPTION_IMAGING_SETTING_TITLE"), LocalizationSupport.getMessage("EXCEPTION_IMAGING_SETTING_TXT"), null, AlertType.ERROR);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (_model == null) {
            try {
                _model = new ImagingSettingModel(ImagingSettingModel.CALCULATE_DEPTH_OF_FIELD, ImagingSetting.DEFAULT_CIRCLE_OF_CONFUSION, 54, 3.5, 1000, 0);
            } catch (ImagingSettingException ex) {
                ex.printStackTrace();
            }
        }
        _model.addChangeListener(this);
        switchDisplayable(error, DofFormFactory.getForm(this));
    }

    public void resumeMIDlet() {
    }

    public void exitMIDlet() {
        switchDisplayable(null, null);
        destroyApp(true);
        notifyDestroyed();
    }

    /**
     * Switches a current displayable in a display. The <code>display</code>
     * instance is taken from <code>getDisplay</code> method. This method is
     * used by all actions in the design for switching displayable.
     * @param alert the Alert which is temporarily set to the display; if
     * <code>null</code>, then <code>nextDisplayable</code> is set immediately
     * @param nextDisplayable the Displayable to be set
     */
    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {
        Display display = getDisplay();
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }
    }

    /**
     * Returns a display instance.
     * @return the display instance.
     */
    public Display getDisplay() {
        return Display.getDisplay(this);
    }

    /**
     * Called by a system to indicated that a command has been invoked on a
     * particular displayable.
     * @param command the Command that was invoked
     * @param displayable the Displayable where the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {
        if (command == DofFormFactory.getHelpCommandSingleton()) {
            helpReturnTo = getDisplay().getCurrent();
            helpReturnToItem = null;
            switchDisplayable(null, DofFormFactory.getHelpScreen(this, displayable));
        } else if (displayable == prefForm) {
            if (command == DofFormFactory.getPrefOKCommandSingleton()) {
                Alert error = null;
                boolean reset = false;
                final double coc = Double.parseDouble(textFieldPrefCoC.getString());
                int defCalcTarget = getCalculatedModelFromIndex(choiceGroupPrefDefCalcTarget.getSelectedIndex());
                int stopScale = choiceGroupPrefStopScale.getSelectedIndex();
                try {
                    _store.updateRecords(coc, defCalcTarget, stopScale);
                } catch (Exception ex) {
                    error = new Alert(LocalizationSupport.getMessage("EXCEPTION_UPDATE_STORE_GENERAL_TITLE"), LocalizationSupport.getMessage("EXCEPTION_UPDATE_STORE_GENERAL_TXT", new String[] { Double.toString(coc), choiceGroupPrefDefCalcTarget.getString(choiceGroupPrefDefCalcTarget.getSelectedIndex()), ex.getMessage() }), null, AlertType.ERROR);
                }
                try {
                    _model.setCircleOfConfusion(coc);
                } catch (ImagingSettingException ex) {
                    error = new Alert(LocalizationSupport.getMessage("EXCEPTION_UPDATE_STORE_IMAGING_SETTING_TITLE"), LocalizationSupport.getMessage("EXCEPTION_UPDATE_STORE_IMAGING_SETTING_TXT", new String[] { Double.toString(coc) }), null, AlertType.ERROR);
                    reset = true;
                }
                switchDisplayable(error, returnTo);
                if (reset) {
                    getDisplay().callSerially(new Runnable() {

                        public void run() {
                            try {
                                _model = new ImagingSettingModel(ImagingSettingModel.CALCULATE_DEPTH_OF_FIELD, coc, 54, 3.5, 1000, 0);
                            } catch (ImagingSettingException ex) {
                            }
                            _model.addChangeListener(DofCalculator.this);
                        }
                    });
                }
            } else if (command == DofFormFactory.getPrefCancelCommandSingleton()) {
                switchDisplayable(null, returnTo);
            }
        } else if (displayable == aperturesList && command == List.SELECT_COMMAND) {
            textFieldAperture.setString(aperturesList.getString(aperturesList.getSelectedIndex()));
            switchDisplayable(null, returnTo);
            getDisplay().callSerially(new Runnable() {

                public void run() {
                    getDisplay().setCurrentItem(textFieldAperture);
                }
            });
        } else if (displayable == cocsList && command == List.SELECT_COMMAND) {
            int selected = cocsList.getSelectedIndex();
            if (selected >= 0 && selected < CIRCLE_OF_CONFUSION_CHOICES.length) {
                textFieldPrefCoC.setString(CIRCLE_OF_CONFUSION_CHOICES[selected][1]);
            }
            switchDisplayable(null, prefForm);
            getDisplay().callSerially(new Runnable() {

                public void run() {
                    getDisplay().setCurrentItem(textFieldPrefCoC);
                }
            });
        } else if (command == DofFormFactory.getHelpOKCommandSingleton() && displayable == DofFormFactory.getHelpScreen()) {
            switchDisplayable(null, helpReturnTo);
            if (helpReturnToItem != null) {
                getDisplay().callSerially(new Runnable() {

                    public void run() {
                        getDisplay().setCurrentItem(helpReturnToItem);
                    }
                });
            }
        } else if (command == DofFormFactory.getHelpNextCommandSingleton() && displayable == DofFormFactory.getHelpScreen()) {
            switchDisplayable(null, DofFormFactory.nextHelpPage(this));
        } else if (command == DofFormFactory.getHelpPrevCommandSingleton() && displayable == DofFormFactory.getHelpScreen()) {
            switchDisplayable(null, DofFormFactory.previousHelpPage(this));
        }
    }

    /**
     * Called by a system to indicated that a command has been invoked on a
     * particular item.
     * @param command the Command that was invoked
     * @param displayable the Item where the command was invoked
     */
    public void commandAction(Command command, Item item) {
        if (command == DofFormFactory.getHelpCommandSingleton()) {
            helpReturnTo = getDisplay().getCurrent();
            helpReturnToItem = item;
            switchDisplayable(null, DofFormFactory.getHelpScreen(this, item));
        } else if (command == DofFormFactory.getExitCommandSingleton()) {
            exitMIDlet();
        }
        if (item == textFieldAperture) {
            if (command == DofFormFactory.getSelectApertureCommandSingleton()) {
                returnTo = getDisplay().getCurrent();
                aperturesList = DofFormFactory.getApertureList(this);
                switchDisplayable(null, aperturesList);
            }
        } else if (item == choiceGroupCalculate) {
            if (command == DofFormFactory.getPreferencesCommandSingleton()) {
                returnTo = getDisplay().getCurrent();
                switchDisplayable(null, DofFormFactory.getPreferencesForm(this));
            }
        } else if (item == textFieldPrefCoC) {
            if (command == DofFormFactory.getSelectCoCCommandSingleton()) {
                cocsList = DofFormFactory.getCoCList(this);
                switchDisplayable(null, cocsList);
            }
        }
        if (command == DofFormFactory.getCalculateCommandSingleton()) {
            double fl = Double.parseDouble(textFieldFocalLength.getString());
            double a = Double.parseDouble(textFieldAperture.getString());
            double g = Double.parseDouble(textFieldDistance.getString());
            double dof = Double.parseDouble(textFieldDepthOfField.getString());
            try {
                _model.updateState(fl, a, g, dof);
            } catch (ImagingSetting.FocalLengthTooSmallException e) {
                Alert msg = DofFormFactory.getFocalLengthRangeWarning();
                switchDisplayable(msg, getDisplay().getCurrent());
                getDisplay().setCurrentItem(item);
            } catch (ImagingSetting.ApertureTooSmallException e) {
                Alert msg = DofFormFactory.getApertureMinWarning();
                switchDisplayable(msg, getDisplay().getCurrent());
                getDisplay().setCurrentItem(item);
            } catch (ImagingSetting.ApertureTooHighException e) {
                Alert msg = DofFormFactory.getApertureMaxWarning();
                switchDisplayable(msg, getDisplay().getCurrent());
                getDisplay().setCurrentItem(item);
            } catch (ImagingSetting.InfiniteDofNotAchievableException e) {
                Alert msg = DofFormFactory.getApertureMaxWarning();
                switchDisplayable(msg, getDisplay().getCurrent());
                getDisplay().setCurrentItem(item);
            } catch (ImagingSetting.ImagingSettingException e) {
            }
            form.delete(0);
            form.insert(0, choiceGroupCalculate);
        }
    }

    /**
     * Called by a system to indicated that an Item has changed.
     * @param item the Item that changed.
     */
    public void itemStateChanged(Item item) {
        if (item == choiceGroupCalculate) {
            int selected = choiceGroupCalculate.getSelectedIndex();
            switch(selected) {
                case 0:
                    try {
                        _model.setFieldToCalculate(getCalculatedModelFromIndex(selected));
                    } catch (ImagingSetting.FocalLengthTooSmallException e) {
                        Alert msg = DofFormFactory.getFocalLengthRangeWarning();
                        setCalculatedIndexFromModel(_model.getFieldToCalculate());
                        switchDisplayable(msg, getDisplay().getCurrent());
                        getDisplay().setCurrentItem(item);
                    } catch (ImagingSetting.InfiniteDofNotAchievableException e) {
                        Alert msg = DofFormFactory.getInfinityWarning();
                        setCalculatedIndexFromModel(_model.getFieldToCalculate());
                        switchDisplayable(msg, getDisplay().getCurrent());
                        getDisplay().setCurrentItem(item);
                    } catch (ImagingSetting.ImagingSettingException e) {
                    }
                    break;
                case 1:
                    try {
                        _model.setFieldToCalculate(getCalculatedModelFromIndex(selected));
                    } catch (ImagingSetting.ApertureTooSmallException e) {
                        Alert msg = DofFormFactory.getApertureMinWarning();
                        setCalculatedIndexFromModel(_model.getFieldToCalculate());
                        switchDisplayable(msg, getDisplay().getCurrent());
                        getDisplay().setCurrentItem(item);
                    } catch (ImagingSetting.ApertureTooHighException e) {
                        Alert msg = DofFormFactory.getApertureMaxWarning();
                        setCalculatedIndexFromModel(_model.getFieldToCalculate());
                        switchDisplayable(msg, getDisplay().getCurrent());
                        getDisplay().setCurrentItem(item);
                    } catch (ImagingSetting.InfiniteDofNotAchievableException e) {
                        Alert msg = DofFormFactory.getInfinityWarning();
                        setCalculatedIndexFromModel(_model.getFieldToCalculate());
                        switchDisplayable(msg, getDisplay().getCurrent());
                        getDisplay().setCurrentItem(item);
                    } catch (ImagingSetting.ImagingSettingException e) {
                    }
                    break;
                case 2:
                    try {
                        _model.setFieldToCalculate(getCalculatedModelFromIndex(selected));
                    } catch (ImagingSetting.ImagingSettingException e) {
                    }
                    break;
                case 3:
                default:
                    try {
                        _model.setFieldToCalculate(getCalculatedModelFromIndex(selected));
                    } catch (ImagingSetting.ImagingSettingException e) {
                    }
            }
        } else if (item == textFieldFocalLength || item == textFieldAperture || item == textFieldDistance || item == textFieldDepthOfField) {
            form.delete(0);
            form.insert(0, DofFormFactory.getChoiceGroupUneditable(choiceGroupCalculate));
        }
    }

    /**
     * Sets the ChoiceGroup for the calculation target to the right selected
     * value given a target in the data model's terminology.
     * @param fieldToCalculate an integer constant
     *          from ImagingSettingMode.CALCULATE*
     */
    private void setCalculatedIndexFromModel(int fieldToCalculate) {
        int select = getCalculatedIndexFromModel(fieldToCalculate);
        for (int i = 0; i < NUM_TEXT_FIELDS; i++) {
            if (i == select) {
                choiceGroupCalculate.setSelectedIndex(i, true);
            } else {
                choiceGroupCalculate.setSelectedIndex(i, false);
            }
        }
    }

    /**
     * Converts between selection indexes of the ChoiceGroup for the
     * calculation target and targets in the data model's terminology.
     * @param fieldToCalculate an integer constant
     *          from ImagingSettingModel.CALCULATE*
     * @return the ChoiceGroup selection that matches the model value.
     */
    private int getCalculatedIndexFromModel(int fieldToCalculate) {
        switch(fieldToCalculate) {
            case ImagingSettingModel.CALCULATE_FOCAL_LENGTH:
                return 0;
            case ImagingSettingModel.CALCULATE_APERTURE:
                return 1;
            case ImagingSettingModel.CALCULATE_DISTANCE:
                return 2;
            case ImagingSettingModel.CALCULATE_DEPTH_OF_FIELD:
            default:
                return 3;
        }
    }

    /**
     * Converts between selection indexes of the ChoiceGroup for the
     * calculation target and targets in the data model's terminology.
     * @param selected the selection index of the ChoiceGroup
     * @return the model value, one of ImagingSettingModel.CALCULATE*
     */
    private int getCalculatedModelFromIndex(int selected) {
        switch(selected) {
            case 0:
                return ImagingSettingModel.CALCULATE_FOCAL_LENGTH;
            case 1:
                return ImagingSettingModel.CALCULATE_APERTURE;
            case 2:
                return ImagingSettingModel.CALCULATE_DISTANCE;
            case 3:
            default:
                return ImagingSettingModel.CALCULATE_DEPTH_OF_FIELD;
        }
    }

    /**
     * Updates a given TextField, only if the new value is different from the
     * current one.
     * @param f the field to update
     * @param val the new value
     */
    private void updateTextFieldIfDifferent(TextField f, String val) {
        if (!f.getString().equals(val)) {
            f.setString(val);
        }
    }

    /**
     * This is a callback for the model to trigger once it has done it's
     * job on an update. The UI can now set its state accordingly.
     * @param oldSetting the model before the change
     * @param newSetting the new model
     */
    public void changedImagingSetting(ImagingSettingModel oldSetting, ImagingSettingModel newSetting) {
        if (oldSetting.getFieldToCalculate() != newSetting.getFieldToCalculate()) {
            form = DofFormFactory.getForm(this);
            switchDisplayable(null, form);
        } else {
            updateTextFieldIfDifferent(textFieldFocalLength, _model.getFocalLengthAsString());
            updateTextFieldIfDifferent(textFieldAperture, _model.getApertureAsString());
            updateTextFieldIfDifferent(textFieldDistance, _model.getDistanceAsString());
            updateTextFieldIfDifferent(textFieldDepthOfField, _model.getDepthOfFieldAsString());
            int n = _model.getNearEdge();
            if (n == 0) {
                tableModelResult.setValue(1, 0, LocalizationSupport.getMessage("LBL_INFINITY"));
            } else {
                tableModelResult.setValue(1, 0, _model.getNearEdgeAsString());
            }
            int f = _model.getFarEdge();
            if (f == 0) {
                tableModelResult.setValue(1, 1, LocalizationSupport.getMessage("LBL_INFINITY"));
            } else {
                tableModelResult.setValue(1, 1, _model.getFarEdgeAsString());
            }
            tableResult.setModel(tableModelResult);
        }
    }

    private static class DofFormFactory {

        private static final double[] fullStopScale = new double[] { 1.0, 1.4, 2, 2.8, 4, 5.6, 8, 11, 16, 22 };

        private static final double[] oneHalfStopScale = new double[] { 1.0, 1.2, 1.4, 1.7, 2, 2.4, 2.8, 3.3, 4, 4.8, 5.6, 6.7, 8, 9.5, 11, 13, 16, 19, 22 };

        private static final double[] oneThirdStopScale = new double[] { 1.0, 1.1, 1.2, 1.4, 1.6, 1.8, 2, 2.2, 2.5, 2.8, 3.2, 3.6, 4, 4.5, 5.0, 5.6, 6.3, 7.1, 8, 9, 10, 11, 13, 14, 16, 18, 20, 22 };

        private static Alert apertureMinWarning;

        private static Alert apertureMaxWarning;

        private static Alert focalLengthWarning;

        private static Alert infinityWarning;

        private static Command exitCommand;

        private static Command calculateCommand;

        private static Command selectApertureCommand;

        private static Command selectCoCCommand;

        private static Command helpCommand;

        private static Command preferencesCommand;

        private static Command prefOKCommand;

        private static Command prefCancelCommand;

        private static Displayable helpScreen;

        private static int currentHelpPage;

        private static Command helpOKCommand;

        private static Command helpNextCommand;

        private static Command helpPrevCommand;

        /**
         * Retrieves a ChoiceGroup for calculation target selection. This
         * does not follow the singleton pattern, i. e. it creates a new
         * Item every time.
         * @param app the midlet
         * @return the ChoiceGroup Item
         */
        private static ChoiceGroup getChoiceGroup(DofCalculator app) {
            int selected = app.getCalculatedIndexFromModel(app._model.getFieldToCalculate());
            ChoiceGroup choiceGroup = new ChoiceGroup(LocalizationSupport.getMessage("CHOICE_TARGET_TITLE"), Choice.POPUP);
            choiceGroup.append(LocalizationSupport.getMessage("CHOICE_TARGET_FOCAL_LENGTH"), null);
            choiceGroup.append(LocalizationSupport.getMessage("CHOICE_TARGET_APERTURE"), null);
            choiceGroup.append(LocalizationSupport.getMessage("CHOICE_TARGET_DISTANCE"), null);
            choiceGroup.append(LocalizationSupport.getMessage("CHOICE_TARGET_DOF"), null);
            choiceGroup.setLayout(ImageItem.LAYOUT_RIGHT | Item.LAYOUT_EXPAND);
            for (int i = 0; i < choiceGroup.size(); i++) {
                choiceGroup.setSelectedIndex(i, (i == selected ? true : false));
            }
            choiceGroup.addCommand(getHelpCommandSingleton());
            choiceGroup.addCommand(getExitCommandSingleton());
            choiceGroup.addCommand(getPreferencesCommandSingleton());
            choiceGroup.setItemCommandListener(app);
            return choiceGroup;
        }

        /**
         * Retrieves a TextField for focal length input. This
         * does not follow the singleton pattern, i. e. it creates a new
         * Item every time.
         * @param app the midlet
         * @return the TextField Item
         */
        private static TextField getTextFieldFocalLength(DofCalculator app) {
            String label = LocalizationSupport.getMessage("LBL_TF_FOCAL_LENGTH");
            TextField textField;
            if (app._model.getFieldToCalculate() == ImagingSettingModel.CALCULATE_FOCAL_LENGTH) {
                textField = new TextField(label, null, 32, TextField.NUMERIC | TextField.UNEDITABLE);
            } else {
                textField = new TextField(label, null, 32, TextField.NUMERIC);
            }
            textField.setString(app._model.getFocalLengthAsString());
            textField.setLayout(ImageItem.LAYOUT_RIGHT | Item.LAYOUT_EXPAND);
            textField.addCommand(getHelpCommandSingleton());
            textField.addCommand(getExitCommandSingleton());
            textField.addCommand(getCalculateCommandSingleton());
            textField.setItemCommandListener(app);
            return textField;
        }

        /**
         * Retrieves a TextField for aperture input. This
         * does not follow the singleton pattern, i. e. it creates a new
         * Item every time.
         * @param app the midlet
         * @return the TextField Item
         */
        private static TextField getTextFieldAperture(DofCalculator app) {
            String label = LocalizationSupport.getMessage("LBL_TF_APERTURE");
            TextField textField;
            if (app._model.getFieldToCalculate() == ImagingSettingModel.CALCULATE_APERTURE) {
                textField = new TextField(label, null, 32, TextField.DECIMAL | TextField.UNEDITABLE);
            } else {
                textField = new TextField(label, null, 32, TextField.DECIMAL);
                textField.addCommand(getSelectApertureCommandSingleton());
                textField.setItemCommandListener(app);
            }
            textField.setString(app._model.getApertureAsString());
            textField.setLayout(ImageItem.LAYOUT_RIGHT | Item.LAYOUT_EXPAND);
            textField.addCommand(getHelpCommandSingleton());
            textField.addCommand(getExitCommandSingleton());
            textField.addCommand(getCalculateCommandSingleton());
            textField.setItemCommandListener(app);
            return textField;
        }

        /**
         * Retrieves a TextField for distance input. This
         * does not follow the singleton pattern, i. e. it creates a new
         * Item every time.
         * @param app the midlet
         * @return the TextField Item
         */
        private static TextField getTextFieldDistance(DofCalculator app) {
            String label = LocalizationSupport.getMessage("LBL_TF_DISTANCE");
            TextField textField;
            if (app._model.getFieldToCalculate() == ImagingSettingModel.CALCULATE_DISTANCE) {
                textField = new TextField(label, null, 32, TextField.NUMERIC | TextField.UNEDITABLE);
            } else {
                textField = new TextField(label, null, 32, TextField.NUMERIC);
            }
            textField.setString(app._model.getDistanceAsString());
            textField.setLayout(ImageItem.LAYOUT_RIGHT | Item.LAYOUT_EXPAND);
            textField.addCommand(getHelpCommandSingleton());
            textField.addCommand(getExitCommandSingleton());
            textField.addCommand(getCalculateCommandSingleton());
            textField.setItemCommandListener(app);
            return textField;
        }

        /**
         * Retrieves a TextField for depth-of-field input. This
         * does not follow the singleton pattern, i. e. it creates a new
         * Item every time.
         * @param app the midlet
         * @return the TextField Item
         */
        private static TextField getTextFieldDepthOfField(DofCalculator app) {
            String label = LocalizationSupport.getMessage("LBL_TF_DOF");
            TextField textField;
            if (app._model.getFieldToCalculate() == ImagingSettingModel.CALCULATE_DEPTH_OF_FIELD) {
                textField = new TextField(label, null, 32, TextField.NUMERIC | TextField.UNEDITABLE);
            } else {
                textField = new TextField(label, null, 32, TextField.NUMERIC);
            }
            textField.setString(app._model.getDepthOfFieldAsString());
            textField.setLayout(ImageItem.LAYOUT_RIGHT | Item.LAYOUT_EXPAND);
            textField.addCommand(getHelpCommandSingleton());
            textField.addCommand(getExitCommandSingleton());
            textField.addCommand(getCalculateCommandSingleton());
            textField.setItemCommandListener(app);
            return textField;
        }

        /**
         * Retrieves a TableItem to display the results in. This
         * does not follow the singleton pattern, i. e. it creates a new
         * Item every time.
         * @param app the midlet
         * @return the TableItem Item
         */
        private static TableItem getResultsTable(DofCalculator app) {
            app.tableModelResult = new SimpleTableModel(new java.lang.String[][] { new java.lang.String[] { LocalizationSupport.getMessage("LBL_ROW_DOF_NEAR"), app._model.getNearEdgeAsString() }, new java.lang.String[] { LocalizationSupport.getMessage("LBL_ROW_DOF_FAR"), app._model.getFarEdgeAsString() } }, null);
            TableItem tableItem = new TableItem(app.getDisplay(), LocalizationSupport.getMessage("LBL_TABLE_RESULT"));
            tableItem.setLayout(ImageItem.LAYOUT_DEFAULT | Item.LAYOUT_EXPAND);
            tableItem.setModel(app.tableModelResult);
            tableItem.addCommand(getHelpCommandSingleton());
            tableItem.addCommand(getExitCommandSingleton());
            tableItem.setItemCommandListener(app);
            return tableItem;
        }

        /**
         * Retrieves a List Displayable with a list of aperture values. This
         * does not follow the singleton pattern, i. e. it creates a new
         * Item every time.
         * @param app the midlet
         * @return the List Displayable
         */
        private static List getApertureList(DofCalculator app) {
            double[] apertureOptions;
            switch(app._store.getStopScale()) {
                case APERTURE_FULL_STOPS:
                    apertureOptions = fullStopScale;
                    break;
                case APERTURE_HALF_STOPS:
                    apertureOptions = oneHalfStopScale;
                    break;
                case APERTURE_THRID_STOPS:
                default:
                    apertureOptions = oneThirdStopScale;
            }
            List apertures = new List(LocalizationSupport.getMessage("LIST_TITLE_APERTURES"), Choice.IMPLICIT);
            double currentApertureVal = Double.parseDouble(app.textFieldAperture.getString());
            boolean setSelected = false;
            for (int i = 0; i < apertureOptions.length; i++) {
                apertures.append(Double.toString(apertureOptions[i]), null);
                if (apertureOptions[i] >= currentApertureVal && !setSelected) {
                    setSelected = true;
                    apertures.setSelectedIndex(i, true);
                } else {
                    apertures.setSelectedIndex(i, false);
                }
            }
            apertures.addCommand(getHelpCommandSingleton());
            apertures.setCommandListener(app);
            return apertures;
        }

        /**
         * Singleton accessor for the \"Calculate\" command
         * @return the Command
         */
        public static Command getCalculateCommandSingleton() {
            if (calculateCommand == null) {
                calculateCommand = new Command(LocalizationSupport.getMessage("LBL_CMD_CALCULATE"), Command.ITEM, 0);
            }
            return calculateCommand;
        }

        /**
         * Singleton accessor for the \"Help\" command
         * @return the Command
         */
        public static Command getHelpCommandSingleton() {
            if (helpCommand == null) {
                helpCommand = new Command(LocalizationSupport.getMessage("LBL_CMD_HELP"), Command.ITEM, 7);
            }
            return helpCommand;
        }

        /**
         * Singleton accessor for the \"Preferences\" command
         * @return the Command
         */
        public static Command getPreferencesCommandSingleton() {
            if (preferencesCommand == null) {
                preferencesCommand = new Command(LocalizationSupport.getMessage("LBL_CMD_PREFS"), Command.ITEM, 3);
            }
            return preferencesCommand;
        }

        /**
         * Singleton accessor for the \"Select\" command for aperture values
         * @return the Command
         */
        public static Command getSelectApertureCommandSingleton() {
            if (selectApertureCommand == null) {
                selectApertureCommand = new Command(LocalizationSupport.getMessage("LBL_CMD_SELECT_APERTURE"), Command.ITEM, 3);
            }
            return selectApertureCommand;
        }

        /**
         * Singleton accessor for the \"Exit\" command
         * @return the Command
         */
        public static Command getExitCommandSingleton() {
            if (exitCommand == null) {
                exitCommand = new Command(LocalizationSupport.getMessage("LBL_CMD_EXIT"), Command.ITEM, 9);
            }
            return exitCommand;
        }

        /**
         * Creates an uneditable StringItem to replace the ChoiceGroup for
         * calculation target selection. This does not follow the singleton
         * pattern and creates a new StringItem every time.
         * @param g the ChoiceGroup to replace
         * @return the StringItem
         */
        public static Item getChoiceGroupUneditable(ChoiceGroup g) {
            return new StringItem(LocalizationSupport.getMessage("CHOICE_TARGET_TITLE"), g.getString(g.getSelectedIndex()));
        }

        /**
         * Retrieves a form to display as the main form. This does not follow
         * the singleton pattern and creates a new one every time.
         * @param app
         * @return
         */
        public static Form getForm(DofCalculator app) {
            app.choiceGroupCalculate = getChoiceGroup(app);
            app.textFieldFocalLength = getTextFieldFocalLength(app);
            app.textFieldAperture = getTextFieldAperture(app);
            app.textFieldDistance = getTextFieldDistance(app);
            app.textFieldDepthOfField = getTextFieldDepthOfField(app);
            app.tableResult = getResultsTable(app);
            app.form = new Form(LocalizationSupport.getMessage("FORM_MAIN_TITLE"), new Item[] { app.choiceGroupCalculate, app.textFieldFocalLength, app.textFieldAperture, app.textFieldDistance, app.textFieldDepthOfField, app.tableResult });
            app.form.setCommandListener(app);
            app.form.setItemStateListener(app);
            return app.form;
        }

        /**
         * Retrieves a TextField for input of the circle-of-confusion 
         * preference. This does not follow the singleton pattern, i. e.
         * it creates a new Item every time.
         * @param app the midlet
         * @return the TextField Item
         */
        private static TextField getTextFieldCoCPref(DofCalculator app) {
            String label = LocalizationSupport.getMessage("LBL_TF_PREF_COC");
            TextField textField = new TextField(label, null, 32, TextField.DECIMAL);
            textField.setString(Double.toString(app._store.getCircleOfConfusion()));
            textField.setLayout(ImageItem.LAYOUT_RIGHT | Item.LAYOUT_EXPAND);
            textField.addCommand(getSelectCoCCommandSingleton());
            textField.addCommand(getHelpCommandSingleton());
            textField.setItemCommandListener(app);
            return textField;
        }

        /**
         * Retrieves a ChoiceGroup for default calculation target selection.
         * This does not follow the singleton pattern, i. e. it creates a new
         * Item every time.
         * @param app the midlet
         * @return the ChoiceGroup Item
         */
        private static ChoiceGroup getChoiceGroupDefCalcTargetPref(DofCalculator app) {
            int selected = app.getCalculatedIndexFromModel(app._store.getDefaultCalculationTarget());
            ChoiceGroup choiceGroup = new ChoiceGroup(LocalizationSupport.getMessage("CHOICE_DEF_TARGET_TITLE"), Choice.POPUP);
            choiceGroup.append(LocalizationSupport.getMessage("CHOICE_DEF_TARGET_FOCAL_LENGTH"), null);
            choiceGroup.append(LocalizationSupport.getMessage("CHOICE_DEF_TARGET_APERTURE"), null);
            choiceGroup.append(LocalizationSupport.getMessage("CHOICE_DEF_TARGET_DISTANCE"), null);
            choiceGroup.append(LocalizationSupport.getMessage("CHOICE_DEF_TARGET_DOF"), null);
            choiceGroup.setLayout(ImageItem.LAYOUT_RIGHT | Item.LAYOUT_EXPAND);
            for (int i = 0; i < choiceGroup.size(); i++) {
                choiceGroup.setSelectedIndex(i, (i == selected ? true : false));
            }
            choiceGroup.addCommand(getHelpCommandSingleton());
            choiceGroup.setItemCommandListener(app);
            return choiceGroup;
        }

        /**
         * Retrieves a ChoiceGroup for stop scale preference selection. This
         * does not follow the singleton pattern, i. e. it creates a new
         * Item every time.
         * @param app the midlet
         * @return the ChoiceGroup Item
         */
        private static ChoiceGroup getChoiceGroupStopScale(DofCalculator app) {
            int selected = app._store.getStopScale();
            ChoiceGroup choiceGroup = new ChoiceGroup(LocalizationSupport.getMessage("CHOICE_STOP_SCALE_TITLE"), Choice.POPUP);
            choiceGroup.append(LocalizationSupport.getMessage("CHOICE_STOP_SCALE_FULL"), null);
            choiceGroup.append(LocalizationSupport.getMessage("CHOICE_STOP_SCALE_HALF"), null);
            choiceGroup.append(LocalizationSupport.getMessage("CHOICE_STOP_SCALE_THIRD"), null);
            choiceGroup.setLayout(ImageItem.LAYOUT_RIGHT | Item.LAYOUT_EXPAND);
            for (int i = 0; i < choiceGroup.size(); i++) {
                choiceGroup.setSelectedIndex(i, (i == selected ? true : false));
            }
            choiceGroup.addCommand(getHelpCommandSingleton());
            choiceGroup.setItemCommandListener(app);
            return choiceGroup;
        }

        /**
         * Singleton accessor for the \"OK\" command for the preferences form
         * @return the Command
         */
        public static Command getPrefOKCommandSingleton() {
            if (prefOKCommand == null) {
                prefOKCommand = new Command(LocalizationSupport.getMessage("LBL_CMD_PREF_OK"), Command.OK, 0);
            }
            return prefOKCommand;
        }

        /**
         * Singleton accessor for the \"Cancel\" command for the preferences
         * form
         * @return the Command
         */
        public static Command getPrefCancelCommandSingleton() {
            if (prefCancelCommand == null) {
                prefCancelCommand = new Command(LocalizationSupport.getMessage("LBL_CMD_PREF_CANCEL"), Command.CANCEL, 0);
            }
            return prefCancelCommand;
        }

        /**
         * Retrieves a List Displayable with sample circle of confusion presets.
         * This does not follow the singleton pattern, i. e. it creates a new
         * Item every time.
         * @param l the midlet
         * @return the List Displayable
         */
        private static List getCoCList(CommandListener l) {
            List cocs = new List(LocalizationSupport.getMessage("LIST_TITLE_COC"), Choice.IMPLICIT);
            for (int i = 0; i < CIRCLE_OF_CONFUSION_CHOICES.length; i++) {
                cocs.append(CIRCLE_OF_CONFUSION_CHOICES[i][0], null);
                cocs.setSelectedIndex(i, false);
            }
            cocs.addCommand(getHelpCommandSingleton());
            cocs.setCommandListener(l);
            return cocs;
        }

        /**
         * Singleton accessor for the \"Select\" command for the circle of
         * confusion in the preferences form
         * @return the Command
         */
        public static Command getSelectCoCCommandSingleton() {
            if (selectCoCCommand == null) {
                selectCoCCommand = new Command(LocalizationSupport.getMessage("LBL_CMD_PREF_SELECT_COC"), Command.ITEM, 10);
            }
            return selectCoCCommand;
        }

        /**
         * Retrieves a form to display as the preferences form. This does not
         * follow the singleton pattern and creates a new one every time.
         * @param app
         * @return
         */
        public static Form getPreferencesForm(DofCalculator app) {
            app.textFieldPrefCoC = getTextFieldCoCPref(app);
            app.choiceGroupPrefDefCalcTarget = getChoiceGroupDefCalcTargetPref(app);
            app.choiceGroupPrefStopScale = getChoiceGroupStopScale(app);
            app.prefForm = new Form(LocalizationSupport.getMessage("FORM_PREFS_TITLE"), new Item[] { app.textFieldPrefCoC, app.choiceGroupPrefDefCalcTarget, app.choiceGroupPrefStopScale });
            app.prefForm.addCommand(getPrefOKCommandSingleton());
            app.prefForm.addCommand(getPrefCancelCommandSingleton());
            app.prefForm.setCommandListener(app);
            return app.prefForm;
        }

        /** The help texts in sections with titles */
        private static String[][] helpTexts = new String[][] { { LocalizationSupport.getMessage("HELP_TEXT_OVERVIEW_TITLE"), LocalizationSupport.getMessage("HELP_TEXT_OVERVIEW_TXT") }, { LocalizationSupport.getMessage("HELP_TEXT_OPERATION_TITLE"), LocalizationSupport.getMessage("HELP_TEXT_OPERATION_TXT") }, { LocalizationSupport.getMessage("HELP_TEXT_PREFS_TITLE"), LocalizationSupport.getMessage("HELP_TEXT_PREFS_TXT") } };

        /**
         * Maps an arbitrary object to a help section to display. This is
         * used to implement the context sensitivity of the online help.
         * It is typically passed an Item or a Displayable off which the
         * help command is executed.
         * @param app the midlet
         * @param helpAbout the object for which help is requsted
         * @return an index into the helpTexts array to the right help section
         */
        private static int getHelpPageForObject(DofCalculator app, Object helpAbout) {
            if (helpAbout == app.textFieldPrefCoC || helpAbout == app.choiceGroupPrefDefCalcTarget || helpAbout == app.choiceGroupPrefStopScale || helpAbout == app.prefForm) {
                return 2;
            } else if (helpAbout == app.choiceGroupCalculate) {
                return 0;
            } else {
                return 1;
            }
        }

        /**
         * Singleton accessor for the \"OK\" command in the online help.
         * @return the Command
         */
        public static Command getHelpOKCommandSingleton() {
            if (helpOKCommand == null) {
                helpOKCommand = new Command(LocalizationSupport.getMessage("LBL_CMD_HELP_OK"), Command.OK, 0);
            }
            return helpOKCommand;
        }

        /**
         * Singleton accessor for the \"Next\" command in the online help.
         * @return the Command
         */
        public static Command getHelpNextCommandSingleton() {
            if (helpNextCommand == null) {
                helpNextCommand = new Command(LocalizationSupport.getMessage("LBL_CMD_HELP_NEXT"), Command.ITEM, 0);
            }
            return helpNextCommand;
        }

        /**
         * Singleton accessor for the \"Previous\" command in the online help.
         * @return the Command
         */
        public static Command getHelpPrevCommandSingleton() {
            if (helpPrevCommand == null) {
                helpPrevCommand = new Command(LocalizationSupport.getMessage("LBL_CMD_HELP_PREV"), Command.ITEM, 5);
            }
            return helpPrevCommand;
        }

        /**
         * Accessor to the help screen that does not trigger a change,
         * just retrieves the current screen.
         * @return the Command
         */
        public static Displayable getHelpScreen() {
            return helpScreen;
        }

        /**
         * Accessor to the help screen for a given object. This creates a
         * new help screen for every call.
         * @param app the midlet
         * @param helpAbout the object for which help is requested
         * @return the help screen
         */
        public static Displayable getHelpScreen(DofCalculator app, Object helpAbout) {
            currentHelpPage = getHelpPageForObject(app, helpAbout);
            refreshHelpPage(app);
            return helpScreen;
        }

        private static Displayable nextHelpPage(DofCalculator app) {
            currentHelpPage++;
            refreshHelpPage(app);
            return helpScreen;
        }

        private static Displayable previousHelpPage(DofCalculator app) {
            currentHelpPage--;
            refreshHelpPage(app);
            return helpScreen;
        }

        private static void refreshHelpPage(DofCalculator app) {
            helpScreen = new Form(LocalizationSupport.getMessage("FORM_HELP_TITLE", new String[] { helpTexts[currentHelpPage][0] }), new Item[] { new StringItem(null, helpTexts[currentHelpPage][1]) });
            helpScreen.addCommand(getHelpOKCommandSingleton());
            helpScreen.setCommandListener(app);
            helpScreen.removeCommand(getHelpNextCommandSingleton());
            helpScreen.removeCommand(getHelpPrevCommandSingleton());
            if (currentHelpPage > 0) {
                helpScreen.addCommand(getHelpPrevCommandSingleton());
            }
            if (currentHelpPage < (helpTexts.length - 1)) {
                helpScreen.addCommand(getHelpNextCommandSingleton());
            }
        }

        /**
         * Singleton accessor for the warning about too small an aperture
         * @return the Alert
         */
        public static Alert getApertureMinWarning() {
            if (apertureMinWarning == null) {
                apertureMinWarning = new Alert(LocalizationSupport.getMessage("EXCEPTION_MIN_APERTURE_TITLE"), LocalizationSupport.getMessage("EXCEPTION_MIN_APERTURE_TXT"), null, AlertType.WARNING);
            }
            return apertureMinWarning;
        }

        /**
         * Singleton accessor for the warning about too large an aperture
         * @return the Alert
         */
        public static Alert getApertureMaxWarning() {
            if (apertureMaxWarning == null) {
                apertureMaxWarning = new Alert(LocalizationSupport.getMessage("EXCEPTION_MAX_APERTURE_TITLE"), LocalizationSupport.getMessage("EXCEPTION_MAX_APERTURE_TXT"), null, AlertType.WARNING);
            }
            return apertureMaxWarning;
        }

        /**
         * Singleton accessor for the warning about an invalid focal length
         * @return the Alert
         */
        public static Alert getFocalLengthRangeWarning() {
            if (focalLengthWarning == null) {
                focalLengthWarning = new Alert(LocalizationSupport.getMessage("EXCEPTION_INVALID_FOCAL_LENGTH_TITLE"), LocalizationSupport.getMessage("EXCEPTION_INVALID_FOCAL_LENGTH_TXT"), null, AlertType.WARNING);
            }
            return focalLengthWarning;
        }

        /**
         * Singleton accessor for the warning about unachievable infinite dof
         * @return the Alert
         */
        public static Alert getInfinityWarning() {
            if (infinityWarning == null) {
                infinityWarning = new Alert(LocalizationSupport.getMessage("EXCEPTION_INFINITY_IMPOSSIBLE_TITLE"), LocalizationSupport.getMessage("EXCEPTION_INFINITY_IMPOSSIBLE_TXT"), null, AlertType.WARNING);
            }
            return infinityWarning;
        }
    }
}
