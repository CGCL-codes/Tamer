package view;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Item;
import javax.microedition.lcdui.ItemCommandListener;
import javax.microedition.lcdui.StringItem;
import model.PaukerCore;
import util.Callback;
import view.alert.ConfirmationAlert;
import view.event.ModelListener;
import view.reusable.CardEditButtons;
import beans.Card;
import controller.learn.LearningController;
import de.enough.polish.ui.UiAccess;

/**
 * Display both sides of a card for learning it.
 * <p>
 * About buttons and form commands: To be able to have the [Edit] etc. commands
 * we had to remove the default command, Next, from the form and put it to the
 * first side of the card because normally the form focuses the first active item 
 * - which would be the button - and displays its actions (Select) and, if there
 * are any form actions then together with them in a options menu. But we want to
 * have   
 *
 * @author Markus Brosch (markus[at]brosch[dot]net)
 */
public class LearningUI extends CardDisplayBase {

    private static final Command _nextCommand = new Command("Next", Command.OK, 0);

    private final CardEditButtons _editbuttonsHelper;

    private Card _card;

    public LearningUI(LearningController learningController, PaukerCore model) {
        super(learningController, model);
        _editbuttonsHelper = new CardEditButtons(learningController, model, _form);
    }

    protected ModelListener createStopLearningConfirmationListener() {
        return new ModelListener() {

            public void modelChanged(Object model) {
                final ConfirmationAlert confirmationAlert = (ConfirmationAlert) model;
                if (confirmationAlert.isConfirmed()) {
                    _controller.stop();
                } else {
                    LearningUI.this.showScreen();
                }
            }
        };
    }

    protected void updateView() {
        _card = _model.getCurrentCard();
        if (_card != null) {
            _form.deleteAll();
            appendCardSide(_card, true, true);
            appendCardSide(_card, false, false);
            _editbuttonsHelper.reset();
            _editbuttonsHelper.appendCustomButton("Stop", new Callback() {

                public void execute() {
                    _confirmStopLearning.show();
                }
            });
            _editbuttonsHelper.appendEditButton(this);
            _editbuttonsHelper.appendDeleteButton(new Callback() {

                public void execute() {
                    _controller.learnNext();
                }
            });
        }
    }

    private StringItem appendCardSide(final Card card, final boolean questionSide, final boolean addMainActions) {
        final boolean questionOnSideA = _model.getPreferences().isDirectionAB();
        final boolean shownSideA = (questionSide && questionOnSideA) || (!questionSide && !questionOnSideA);
        final String text = shownSideA ? card.getA() : card.getB();
        final StringItem answerItem = new StringItem(null, text);
        if (addMainActions) {
            addMainActionsToItem(answerItem);
        }
        if (shownSideA) {
            UiAccess.setStyle(answerItem);
        } else {
            UiAccess.setStyle(answerItem);
        }
        _form.append(answerItem);
        return answerItem;
    }

    private void addMainActionsToItem(final StringItem answerItem) {
        answerItem.setDefaultCommand(_nextCommand);
        answerItem.setItemCommandListener(new ItemCommandListener() {

            public void commandAction(final Command command, final Item source) {
                if (command == _nextCommand) {
                    _controller.learnNext();
                }
            }
        });
    }

    public void commandAction(Command c, Displayable s) {
    }

    protected String getUiTitle() {
        return "Learning ...";
    }
}
