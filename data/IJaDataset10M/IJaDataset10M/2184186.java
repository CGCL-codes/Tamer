package com.peterhi.player.actions;

import java.awt.event.ActionEvent;
import com.peterhi.player.Whiteboard;
import com.peterhi.player.editors.PenEditor;
import com.peterhi.player.editors.Editor;
import com.peterhi.player.Elements;
import com.peterhi.net.messages.PolylineMessage;
import com.peterhi.client.SocketClient;
import com.peterhi.player.Window;
import com.peterhi.player.shapes.Polyline;

public class PenAction extends BaseAction {

    private static final PenAction instance = new PenAction();

    public static PenAction getInstance() {
        return instance;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Whiteboard.getWhiteboard().setEditor(new PenEditor(new Editor.Callback() {

            public void finished(Editor editor) {
                Polyline polyline = (Polyline) editor.create();
                Elements.getElements().getModel().add(polyline);
                Elements.getElements().updateTreeUI();
                PolylineMessage message = new PolylineMessage();
                message.sender = Window.getWindow().id;
                message.name = polyline.name;
                message.xs = polyline.xs;
                message.ys = polyline.ys;
                try {
                    SocketClient.getInstance().send(message);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            public void cancelled(Editor editor) {
            }
        }));
    }
}
