package bee.entities;

import bee.core.Core;
import bee.core.Vec2dInt;
import bee.gui.Button;
import bee.gui.ElementEventListener;
import bee.gui.GUI;
import bee.gui.Sheet;
import bee.gui.TextStatic;
import java.awt.Color;

/**
 *
 * @author boto
 */
public class DlgDead {

    private Sheet mainSheet;

    private Player player;

    public DlgDead(Player player) {
        this.player = player;
    }

    public void setup() {
        Vec2dInt pos = new Vec2dInt();
        Vec2dInt size = new Vec2dInt(275, 130);
        Vec2dInt sdim = Core.get().getAppState().getSceneManager().getViewDimension();
        pos.x = (sdim.x - size.x) / 2;
        pos.y = (sdim.y - size.y) / 2;
        Sheet sheet = new Sheet(size);
        sheet.setText("Ooops");
        sheet.setPosition(new Vec2dInt(pos.x, pos.y));
        sheet.setTextColor(Color.white);
        sheet.setBackgroundColor(Color.orange);
        TextStatic st = new TextStatic();
        String text = "Sorry, you are too weak for collecting\nhoney. Get some sleep and try it again.";
        st.setText(text);
        st.setSize(new Vec2dInt(size.x - 6, 50));
        st.setPosition(new Vec2dInt(3, 16));
        st.setTextColor(Color.black);
        st.setEnableBackground(false);
        st.setEnableBorder(false);
        sheet.addChild(st);
        Button btnretry = new Button("Retry");
        btnretry.setPosition(new Vec2dInt((size.x / 2 - btnretry.getSize().x) / 2, size.y - btnretry.getSize().y - 10));
        sheet.addChild(btnretry);
        btnretry.addElementListener(new ElementEventListener() {

            @Override
            public void onClicked() {
                player.onClickedRetry();
            }
        });
        Button btnmenu = new Button("Menu");
        btnmenu.setPosition(new Vec2dInt((size.x / 2) + (size.x / 2 - btnmenu.getSize().x) / 2, size.y - btnmenu.getSize().y - 10));
        sheet.addChild(btnmenu);
        btnmenu.addElementListener(new ElementEventListener() {

            @Override
            public void onClicked() {
                player.onClickedGiveUp();
            }
        });
        mainSheet = sheet;
        GUI.get().add(mainSheet);
    }

    public void destroy() {
        GUI.get().remove(mainSheet);
        mainSheet.destroy();
        player = null;
    }

    public void show(boolean show) {
        mainSheet.setVisible(show);
        mainSheet.setEnabled(show);
        GUI.get().showCursor(show);
    }
}
