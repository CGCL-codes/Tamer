package view;

import controller.UIController;
import de.enough.polish.ui.*;
import util.Toolbox;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.rms.RecordStore;

/**
 * The Quit Dialog let the user to choose whether to save the current lesson or not
 * and to select the lesson to save into or to save it as a new lesson.
 *
 * @author Markus Brosch (markus[at]brosch[dot]net)
 */
public class SaveAndQuitUI extends UIComponent implements ItemStateListener, ItemCommandListener {

    private static final Command _yesCommand = new Command("Yes", Command.OK, 0);

    private static final Command _noCommand = new Command("No", Command.CANCEL, 1);

    private static final Command _okCommand = new Command("Save & Quit", Command.OK, 10);

    private static final Command _cancelCommand = new Command("Cancel", Command.CANCEL, 0);

    private Form _form;

    private ChoiceGroup _choiceGroup;

    private String _originalLessonName;

    private static final int NOSELECTION = -1;

    private int _indexOfLastSelection = NOSELECTION;

    private final UIController _controller;

    private TextField _tf;

    public SaveAndQuitUI(UIController controller, String defaultName) {
        _controller = controller;
        _originalLessonName = defaultName;
        _tf = new TextField("", _originalLessonName, 50, TextField.ANY);
        this.createView();
        this.showScreen();
    }

    protected void createView() {
        _form = new Form("Quitting: MiniPauker says bye bye!");
        _form.append("Do you want to save the current lesson before quitting?");
        _form.addCommand(_yesCommand);
        _form.addCommand(_noCommand);
        _form.setCommandListener(this);
    }

    protected void updateView() {
    }

    public Displayable getScreen() {
        return _form;
    }

    public synchronized void itemStateChanged(Item item) {
        if (item == _choiceGroup) {
            boolean selections[] = new boolean[_choiceGroup.size()];
            boolean anySelection = false;
            _choiceGroup.getSelectedFlags(selections);
            for (int i = 0; i < selections.length; i++) {
                boolean selection = selections[i];
                if (selection) {
                    anySelection = true;
                    if (i != _indexOfLastSelection) {
                        if (_indexOfLastSelection != NOSELECTION) _choiceGroup.setSelectedIndex(_indexOfLastSelection, false);
                        _indexOfLastSelection = i;
                        break;
                    }
                } else {
                    _choiceGroup.setSelectedIndex(i, false);
                }
            }
            if (anySelection) {
                _tf.setString(_choiceGroup.getString(_indexOfLastSelection));
            } else {
                _indexOfLastSelection = NOSELECTION;
                _tf.setString(_originalLessonName);
            }
        } else if (item == _tf) {
            this.makeSelection();
        }
    }

    public void commandAction(Command c, Displayable s) {
        if (c == _noCommand) {
            _controller.quit();
        } else if (c == _yesCommand) {
            _form.deleteAll();
            _form.removeCommand(_yesCommand);
            _form.removeCommand(_noCommand);
            _form.setTitle("Save and Exit");
            UiAccess.setSubtitle(_form, "(Press any key to start typing)");
            _form.append(_tf);
            _form.addCommand(_cancelCommand);
            _form.addCommand(_okCommand);
            String[] stores = RecordStore.listRecordStores();
            if (stores != null) {
                _choiceGroup = new ChoiceGroup(" Overwrite existing lessons:", Choice.MULTIPLE);
                _choiceGroup.setFitPolicy(Choice.TEXT_WRAP_OFF);
                for (int i = 0; i < stores.length; i++) {
                    String store = stores[i];
                    if (store.endsWith(Toolbox.PAUKEREXT)) {
                        final String lessonName = store.substring(0, store.length() - Toolbox.PAUKEREXT.length());
                        _choiceGroup.append(lessonName, null);
                    }
                }
                this.makeSelection();
                System.out.println("commandAction:appending lesson selection...");
                _form.append(_choiceGroup);
                _form.setItemStateListener(this);
            } else {
                _form.append("Sorry, the phone doesn't support lesson saving. (No record stores available.)");
            }
        } else if (c == _cancelCommand) {
            _controller.showMainMenuUI();
        } else if (c == _okCommand) {
            _controller.saveTo(_tf.getString());
        }
    }

    public void commandAction(Command command, Item item) {
    }

    private void makeSelection() {
        final String textFieldEntry = _tf.getString();
        boolean allDeSelected = true;
        for (int i = 0; i < _choiceGroup.size(); i++) {
            String item = _choiceGroup.getString(i);
            if (item.equals(textFieldEntry)) {
                _choiceGroup.setSelectedIndex(i, true);
                _indexOfLastSelection = i;
                allDeSelected = false;
            } else {
                _choiceGroup.setSelectedIndex(i, false);
            }
        }
        if (allDeSelected) _indexOfLastSelection = NOSELECTION;
    }
}
