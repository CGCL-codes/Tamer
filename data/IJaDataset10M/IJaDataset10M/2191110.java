package com.wxz.sanguo.game.general;

import java.util.List;
import com.wxz.sanguo.game.soldier.SoldierType;

public class GeneralCommand {

    private Command[] commands;

    private GroupCommand groupCommand;

    private int currentPositon = 0;

    private int len = 5;

    /***
	 * 
	 * @param tacticPoint
	 * @param tacticList
	 *            ��܊ ���������ս��������ȡ� ���׵�ս�����ǿ�����ʱ���ӵġ�
	 * 
	 */
    public GeneralCommand(int tacticPoint, List<Tactic> tacticList) {
        this.groupCommand = new GroupCommand(tacticPoint, tacticList);
        commands = new Command[len];
        commands[0] = this.groupCommand;
        commands[1] = new SoldierTypeCommand(SoldierType.WU);
        commands[2] = new SoldierTypeCommand(SoldierType.QI);
        commands[3] = new SoldierTypeCommand(SoldierType.GO);
        commands[4] = new SoldierTypeCommand(SoldierType.BU);
    }

    public void beforeCommand() {
        if (currentPositon >= 1) {
            currentPositon--;
        } else {
            currentPositon = len - 1;
        }
    }

    public void nextCommand() {
        if (currentPositon < len - 1) {
            currentPositon++;
        } else {
            currentPositon = 0;
        }
    }

    public void nextTactic() {
        commands[currentPositon].nextTactic();
    }

    public void beforeTactic() {
        commands[currentPositon].beforeTactic();
    }

    public int getTacticPoint() {
        return this.groupCommand.getAllTacticPoint();
    }

    public Command[] getCommands() {
        return commands;
    }

    public int getCurrentPositon() {
        return currentPositon;
    }

    /****
	 * ��ý���ļ�����ս����
	 * 
	 * @return
	 */
    public List<Tactic> getValidTactics() {
        return this.groupCommand.getValidTacticsList();
    }

    /***
	 * 
	 * 
	 * ���⼼�ܵ���
	 */
    public void executeCommand() {
        this.groupCommand.execute();
        List<Tactic> validList = this.groupCommand.getValidTacticsList();
        for (Tactic tactic : validList) {
            if (tactic.getType() == TacticType.CHONGFENG) {
                SoldierTypeCommand qiCommand = (SoldierTypeCommand) commands[2];
                qiCommand.setLockTacticType(TacticType.FORWARD);
                qiCommand.lock();
            }
            if (tactic.getType() == TacticType.LONGARROW) {
                SoldierTypeCommand goCommand = (SoldierTypeCommand) commands[3];
                goCommand.setLockTacticType(TacticType.WAIT);
                goCommand.lock();
            }
        }
    }

    /***
	 * ����ʱ��Ƭ�����ս�������ս����
	 */
    public void executeTurn() {
        this.groupCommand.executeTurn();
    }

    /****
	 * 
	 * @param soldierType
	 * @return
	 */
    public TacticType getCommonTacticBySoldierType(SoldierType soldierType) {
        for (int i = 1; i < 5; i++) {
            SoldierTypeCommand stc = (SoldierTypeCommand) commands[i];
            if (stc.getSoldierType() == soldierType) {
                return stc.getCurrentTacticType();
            }
        }
        return null;
    }

    public Tactic popCurrentGroupTactic() {
        return this.groupCommand.popCurrentTactic();
    }
}
