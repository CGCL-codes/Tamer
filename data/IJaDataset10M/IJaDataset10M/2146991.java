package bot.defend;

import gameinfo.SendArmyInfo;
import gameinfo.SendArmyInfo.SendArmyType;
import gameinfo.SendArmyInfo.SpyType;
import http.pages.*;
import java.awt.Point;
import java.io.IOException;
import java.net.UnknownHostException;
import bot.attack.AttackTask.CouldNotSendException;

public class DodgeTask {

    private final String villageId;

    private final Point pTarget;

    private SendArmyInfo movementInfo;

    private enum DodgeStage {

        GET, POST, CONFIRM, WAIT, CANCEL, DONE
    }

    private DodgeStage dodgeStage = DodgeStage.GET;

    private long timeDidSend = -1;

    private long timeTryReturn = -1;

    private Page_a2b sendpage;

    public DodgeTask(String aVillageId, Point aPTarget) throws UnknownHostException, IOException {
        villageId = aVillageId;
        pTarget = aPTarget;
        StartSend();
    }

    private void StartSend() throws UnknownHostException, IOException {
        sendpage = new Page_a2b(villageId);
        sendpage.get();
        dodgeStage = DodgeStage.POST;
        movementInfo = new SendArmyInfo(SendArmyType.REINFORCEMENT, SpyType.NONE, villageId, pTarget, sendpage.getMaxArmySize());
    }

    public void Run() throws UnknownHostException, IOException {
        try {
            switch(dodgeStage) {
                case POST:
                    if (sendpage.tryPost(movementInfo)) {
                        dodgeStage = DodgeStage.CONFIRM;
                    }
                    break;
                case CONFIRM:
                    if (sendpage.tryConfirmPost()) {
                        timeDidSend = sendpage.getLastServerAccess();
                        dodgeStage = DodgeStage.WAIT;
                    }
                    break;
                case WAIT:
                case CANCEL:
                case DONE:
                default:
                    dodgeStage = DodgeStage.DONE;
            }
        } catch (CouldNotSendException e) {
            dodgeStage = DodgeStage.DONE;
        }
    }

    public boolean isDone() {
        return dodgeStage == DodgeStage.DONE;
    }
}
