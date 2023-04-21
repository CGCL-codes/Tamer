package interfaces.hud;

import gameStates.GameTimeListener;
import gameStates.absGamesStates.AbsIngameState;
import interfaces.GUISource;
import interfaces.hud.pilot.PilotHUD;
import interfaces.hud.spectate.SpectateHUD;
import interfaces.superWidgets.InterfaceLabel;
import interfaces.superWidgets.InterfaceWindow;
import interfaces.superWidgets.MoneyListener;
import org.fenggui.layout.StaticLayout;
import org.fenggui.util.Color;

public class GameTimeWindow extends InterfaceWindow implements MoneyListener {

    private InterfaceLabel timeLabel, moneyLabel;

    public GameTimeWindow(BasicHUD hud) {
        super(false, "");
        content.setLayoutManager(new StaticLayout());
        int width = hud.getHeight() / 9;
        int height = hud.getHeight() / 13;
        setSize(width, height);
        setXY(hud.getWidth() - width, hud.getHeight() / 5 * 3);
        layout();
        int labelHeight = content.getHeight() / 2;
        int labelWidth = content.getWidth();
        timeLabel = new InterfaceLabel(GUISource.bigFont);
        timeLabel.setSize(labelWidth, labelHeight);
        timeLabel.setXY(0, labelHeight);
        GUISource.setTextColor(timeLabel, Color.BLACK);
        content.addWidget(timeLabel);
        AbsIngameState ingameState = hud.getIngameState();
        ingameState.addGameTimeListener(new GameTimeListener() {

            @Override
            public void gameTimeUpdate(Integer newGameTime) {
                updateTime(newGameTime.intValue());
            }

            @Override
            public void endTimeUpdate(Integer newEndTime) {
            }

            @Override
            public void respawnTimeUpdate() {
            }
        });
        moneyLabel = new InterfaceLabel(" " + ingameState.getPlayer().getMoney(), GUISource.bigFont);
        moneyLabel.setSize(labelWidth, labelHeight);
        moneyLabel.setXY(0, 0);
        GUISource.setTextColor(moneyLabel, GUISource.green);
        if (hud instanceof SpectateHUD || hud instanceof PilotHUD) content.addWidget(moneyLabel);
        layout();
    }

    private void updateTime(int seconds) {
        int minutes = seconds / 60;
        seconds = seconds % 60;
        timeLabel.setText(" " + minutes + ":" + seconds);
        layout();
    }

    @Override
    public void updateMoney(int newMoney) {
        moneyLabel.setText(" " + newMoney);
        content.layout();
    }
}
