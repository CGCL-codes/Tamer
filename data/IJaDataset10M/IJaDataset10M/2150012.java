package com.peterhi.player.action;

import static com.peterhi.player.Application.*;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import com.peterhi.net.message.PolylineMessage;
import com.peterhi.client.SocketClient;
import com.peterhi.player.*;
import com.peterhi.player.component.*;
import com.peterhi.player.view.*;

public class DoDrawPolylineAction extends AbstractAction {

    private static final Action instance = new DoDrawPolylineAction();

    public static Action getInstance() {
        return instance;
    }

    public DoDrawPolylineAction() {
        super();
        putValue(SMALL_ICON, Application.getPolylineIcon());
    }

    public void actionPerformed(ActionEvent evt) {
        JPlayerWindow playerWindow = Application.getWindow();
        final JBasicWhiteboardElementView basicWhiteboardElementView = playerWindow.getView(JBasicWhiteboardElementView.class);
        final JBasicWhiteboardView basicWhiteboardView = playerWindow.getView(JBasicWhiteboardView.class);
        final JBasicWhiteboard basicWhiteboard = basicWhiteboardView.getWhiteboard();
        final JBasicWhiteboardElementView.Model model = basicWhiteboardElementView.getModel();
        basicWhiteboard.setEditor(new JBasicWhiteboard.JPolylineEditor(new JBasicWhiteboard.JShapeEditor.Callback() {

            public void finished(JBasicWhiteboard.JShapeEditor editor) {
                JBasicWhiteboard.JPolyline polyline = (JBasicWhiteboard.JPolyline) editor.create();
                basicWhiteboardElementView.getModel().addShape(editor.create());
                basicWhiteboardElementView.updateTreeUI();
                PolylineMessage message = new PolylineMessage();
                message.sender = get(KEY_ID, Integer.class);
                message.name = polyline.getName();
                message.xs = polyline.getXs();
                message.ys = polyline.getYs();
                try {
                    SocketClient.getInstance().send(message);
                } catch (Exception ex) {
                    Application.shutdown(ex);
                }
            }

            public void cancelled(JBasicWhiteboard.JShapeEditor editor) {
            }
        }));
    }
}
