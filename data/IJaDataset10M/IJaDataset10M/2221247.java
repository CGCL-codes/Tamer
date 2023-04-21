package com.peterhi.player;

import javax.swing.JPopupMenu;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.ButtonGroup;
import com.peterhi.player.actions.SmiliesAction;
import com.peterhi.player.actions.ChoicesAction;
import com.peterhi.player.actions.GoodAction;
import com.peterhi.player.actions.HandAction;
import com.peterhi.player.actions.NoChoiceAction;
import com.peterhi.player.actions.TrueAction;
import com.peterhi.player.actions.FalseAction;
import com.peterhi.player.actions.ChoiceAAction;
import com.peterhi.player.actions.ChoiceBAction;
import com.peterhi.player.actions.ChoiceCAction;
import com.peterhi.player.actions.ChoiceDAction;
import com.peterhi.State;

public class ActionsPopupMenu extends JPopupMenu implements StateMachine {

    private static final ActionsPopupMenu actionsPopupMenu = new ActionsPopupMenu();

    public static ActionsPopupMenu getActionsPopupMenu() {
        return actionsPopupMenu;
    }

    private JCheckBoxMenuItem miGood = new JCheckBoxMenuItem();

    private JCheckBoxMenuItem miHand = new JCheckBoxMenuItem();

    private JMenu mSmilies = new JMenu();

    private JMenu mChoices = new JMenu();

    private ButtonGroup choiceGroup = new ButtonGroup();

    private JRadioButtonMenuItem miNoChoice = new JRadioButtonMenuItem();

    private JRadioButtonMenuItem miTrue = new JRadioButtonMenuItem();

    private JRadioButtonMenuItem miFalse = new JRadioButtonMenuItem();

    private JRadioButtonMenuItem miChoiceA = new JRadioButtonMenuItem();

    private JRadioButtonMenuItem miChoiceB = new JRadioButtonMenuItem();

    private JRadioButtonMenuItem miChoiceC = new JRadioButtonMenuItem();

    private JRadioButtonMenuItem miChoiceD = new JRadioButtonMenuItem();

    public ActionsPopupMenu() {
        super();
        miGood.setAction(GoodAction.getInstance());
        miHand.setAction(HandAction.getInstance());
        mSmilies.setAction(SmiliesAction.getInstance());
        mChoices.setAction(ChoicesAction.getInstance());
        miNoChoice.setAction(NoChoiceAction.getInstance());
        miTrue.setAction(TrueAction.getInstance());
        miFalse.setAction(FalseAction.getInstance());
        miChoiceA.setAction(ChoiceAAction.getInstance());
        miChoiceB.setAction(ChoiceBAction.getInstance());
        miChoiceC.setAction(ChoiceCAction.getInstance());
        miChoiceD.setAction(ChoiceDAction.getInstance());
        mChoices.add(miNoChoice);
        mChoices.add(miTrue);
        mChoices.add(miFalse);
        mChoices.add(miChoiceA);
        mChoices.add(miChoiceB);
        mChoices.add(miChoiceC);
        mChoices.add(miChoiceD);
        miNoChoice.setSelected(true);
        choiceGroup.add(miNoChoice);
        choiceGroup.add(miTrue);
        choiceGroup.add(miFalse);
        choiceGroup.add(miChoiceA);
        choiceGroup.add(miChoiceB);
        choiceGroup.add(miChoiceC);
        choiceGroup.add(miChoiceD);
        add(miGood);
        add(miHand);
        addSeparator();
        add(mSmilies);
        add(mChoices);
    }

    public void stateChanged(int state, int changedBits) {
        miHand.setState(State.isSet(state, State.RAISING_HAND));
        miGood.setState(State.isSet(state, State.APPLAUSING));
        int choice = State.getChoice(state);
        switch(choice) {
            case State.A:
                miChoiceA.setSelected(true);
                break;
            case State.B:
                miChoiceB.setSelected(true);
                break;
            case State.C:
                miChoiceC.setSelected(true);
                break;
            case State.D:
                miChoiceD.setSelected(true);
                break;
            case State.TRUE:
                miTrue.setSelected(true);
                break;
            case State.FALSE:
                miFalse.setSelected(true);
                break;
            default:
                miNoChoice.setSelected(true);
                break;
        }
    }
}
