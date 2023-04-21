package hokutonorogue.character.quest;

import java.io.*;
import java.util.*;
import com.golden.gamedev.util.*;
import hokutonorogue.character.*;
import hokutonorogue.game.*;
import hokutonorogue.level.*;
import hokutonorogue.object.*;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: </p>
 *
 * @author Alessio Carotenuto
 * @version 1.0
 */
public abstract class Quest implements Serializable, Hintable {

    public static final int AVAILABLE_STATUS = 0;

    public static final int IN_PROGRESS_STATUS = 1;

    public static final int COMPLETED_STATUS = 2;

    public static final int SUCCEEDED_STATUS = 3;

    public static final int FAILED_STATUS = 4;

    public static final int ENDED_STATUS = 5;

    public static final String ACCEPT_CHOICE = "ACCEPT";

    public static final int MAX_QUESTS = 15;

    public enum GenericQuests {

        HEAL_QUEST(HealQuest.class), THIRST_QUEST(ThirstQuest.class), ESCORT_TO_WELL_QUEST(EscortToWellQuest.class), BLIND_QUEST(BlindQuest.class), WEAPONS_QUEST(WeaponsQuest.class);

        private final Class questClass;

        GenericQuests(Class questClass) {
            this.questClass = questClass;
        }

        public Class questClass() {
            return questClass;
        }

        public Quest generateQuest() throws IllegalAccessException, InstantiationException {
            return (Quest) questClass.newInstance();
        }

        public static Quest random() throws IllegalAccessException, InstantiationException {
            int i = Utility.getRandom(0, GenericQuests.values().length - 1);
            return (Quest) GenericQuests.values()[i].questClass.newInstance();
        }
    }

    public enum UniqueQuests {

        KILL_ZEED_QUEST(KillZeedQuest.class);

        private final Class questClass;

        UniqueQuests(Class questClass) {
            this.questClass = questClass;
        }

        public Class questClass() {
            return questClass;
        }

        public Quest generateQuest() throws IllegalAccessException, InstantiationException {
            return (Quest) questClass.newInstance();
        }
    }

    protected CharacterModel questGiver = null;

    protected int status = AVAILABLE_STATUS;

    public Quest() {
    }

    public Quest(CharacterModel questGiver) {
        this.questGiver = questGiver;
    }

    public abstract void init();

    public abstract String getName();

    public abstract String getDescription();

    public abstract int getLevel();

    public abstract int getXPReward();

    public abstract List<HokutoObject> getObjectsReward();

    public void onQuestGiverTalk() {
        if (status == AVAILABLE_STATUS) {
            handleRequest();
        } else if (status == IN_PROGRESS_STATUS) {
            handleProgress();
        } else if (status == COMPLETED_STATUS) {
            handleSuccess();
        } else if (status == FAILED_STATUS) {
            handleFailure();
        }
    }

    public void handleRequest() {
        _handleRequest();
        ChoiceMenu choiceMenu = new ChoiceMenu(MainGame.getInstance().parent, MainGame.getInstance());
        choiceMenu.setMessage("CHOOSE YOUR ANSWER:");
        for (SimpleChoice choice : getChoices()) {
            choiceMenu.addChoice(choice);
        }
        choiceMenu.start();
        Choice choice = choiceMenu.getSelectedChoice();
        if (choice != SimpleChoice.CANCEL_CHOICE) {
            if (choice.getUserObject().equals(ACCEPT_CHOICE)) {
                onAccept();
            } else {
                onRefuse();
            }
        }
    }

    protected abstract void _handleRequest();

    protected abstract List<SimpleChoice> getChoices();

    public void onAccept() {
        CharacterModel hero = MainGame.getInstance().getHero();
        if (hero.getNotCompletedQuestNumber() < MAX_QUESTS) {
            _onAccept();
            status = IN_PROGRESS_STATUS;
            hero.getAcceptedQuests().add(0, this);
            LogMessage message = new LogMessage("QUEST ACCEPTED!", LogMessage.POSITIVE);
            Log.getInstance().addMessage(message);
        } else {
            LogMessage message = new LogMessage("YOU CAN'T ACCEPT MORE THAN " + MAX_QUESTS + " QUESTS", LogMessage.NEGATIVE);
            Log.getInstance().addMessage(message);
        }
    }

    protected abstract void _onAccept();

    public abstract void onRefuse();

    public void updateGame(double elapsedTime) {
        if (status == IN_PROGRESS_STATUS) {
            if (isSuccess()) {
                status = COMPLETED_STATUS;
                LogMessage message = new LogMessage("QUEST COMPLETED!", LogMessage.POSITIVE);
                Log.getInstance().addMessage(message);
                MainGame.getInstance().bsSound.play("resources/sound/success.wav");
            } else if (isFailure()) {
                LogMessage message = new LogMessage("QUEST FAILED!", LogMessage.NEGATIVE);
                Log.getInstance().addMessage(message);
                if (questGiver.isDead()) {
                    handleFailure();
                } else {
                    status = FAILED_STATUS;
                }
            }
        } else if (status == COMPLETED_STATUS) {
            if (!isSuccess()) {
                status = IN_PROGRESS_STATUS;
            }
        }
    }

    public abstract boolean isSuccess();

    public boolean isFailure() {
        boolean ret = false;
        if (questGiver.isDead()) {
            ret = true;
        } else {
            ret = _isFailure();
        }
        return ret;
    }

    public abstract boolean _isFailure();

    public void handleSuccess() {
        _handleSuccess();
        int xpValue = getXPReward();
        List<HokutoObject> objectsReward = getObjectsReward();
        CharacterModel hero = MainGame.getInstance().getHero();
        hero.addXp(xpValue);
        LogMessage message = new LogMessage("QUEST SUCCEEDED!", LogMessage.POSITIVE);
        Log.getInstance().addMessage(message);
        MainGame.getInstance().bsSound.play("resources/sound/reward.wav");
        message = new LogMessage(hero.getName().toUpperCase() + " HAS GAINED " + xpValue + " XP", LogMessage.POSITIVE);
        Log.getInstance().addMessage(message);
        if (objectsReward != null && !objectsReward.isEmpty()) {
            for (HokutoObject reward : objectsReward) {
                if (!hero.getInventory().isFull()) {
                    hero.getInventory().addObject(reward);
                } else {
                    hero.getTile().getGround().addObject(reward);
                }
                message = new LogMessage(hero.getName().toUpperCase() + " HAS RECEIVED A " + reward.getName(), LogMessage.POSITIVE);
                Log.getInstance().addMessage(message);
            }
        }
        status = SUCCEEDED_STATUS;
        hero.getStatistics().increaseCompletedQuests();
    }

    public void handleFailure() {
        _handleFailure();
        status = ENDED_STATUS;
    }

    protected abstract void _handleSuccess();

    protected abstract void _handleFailure();

    public abstract void handleProgress();

    public int getStatus() {
        return status;
    }

    public String getStatusString() {
        String ret = "";
        switch(status) {
            case AVAILABLE_STATUS:
                ret = "AVAILABLE";
                break;
            case IN_PROGRESS_STATUS:
                ret = "IN PROGRESS";
                break;
            case COMPLETED_STATUS:
                ret = "COMPLETED";
                break;
            case FAILED_STATUS:
                ret = "FAILED";
                break;
            case SUCCEEDED_STATUS:
                ret = "SUCCEEDED";
                break;
            case ENDED_STATUS:
                ret = "ENDED";
                break;
            default:
                break;
        }
        return ret;
    }

    public int getStatusIndex() {
        int ret = 0;
        switch(status) {
            case AVAILABLE_STATUS:
                ret = 2;
                break;
            case IN_PROGRESS_STATUS:
                ret = 3;
                break;
            case COMPLETED_STATUS:
                ret = 9;
                break;
            case FAILED_STATUS:
                ret = 6;
                break;
            default:
                break;
        }
        return ret;
    }

    public CharacterModel getQuestGiver() {
        return questGiver;
    }

    public void setQuestGiver(CharacterModel questGiver) {
        this.questGiver = questGiver;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isTerminated() {
        return status == SUCCEEDED_STATUS || status == ENDED_STATUS;
    }

    public String getHint() {
        return getDescription();
    }

    public void onAbandon() {
        if (status != SUCCEEDED_STATUS) {
            CharacterModel hero = MainGame.getInstance().getHero();
            _onAbandon();
            status = AVAILABLE_STATUS;
            hero.getAcceptedQuests().remove(this);
            LogMessage message = new LogMessage("QUEST ABANDONED!", LogMessage.NEGATIVE);
            Log.getInstance().addMessage(message);
        }
    }

    protected abstract void _onAbandon();

    public abstract boolean isQuestItem(HokutoObject object);
}
