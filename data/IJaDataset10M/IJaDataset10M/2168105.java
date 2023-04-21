package org.onemind.swingweb.mapinput.peerdelegate.java.awt;

import java.awt.*;
import java.awt.Frame;
import java.util.Map;
import java.util.logging.Logger;
import org.onemind.awtbridge.input.BridgeInputContext;
import org.onemind.awtbridge.input.InputException;
import org.onemind.awtbridge.peer.BridgeComponentPeer;
import org.onemind.swingweb.mapinput.peerdelegate.MapInputDelegate;

/**
 * The frame input delegate
 * @author TiongHiang Lee (thlee@onemindsoft.org)
 * 
 */
public class FrameInputDelegate extends WindowInputDelegate {

    /** the logger **/
    private static final Logger _logger = Logger.getLogger(FrameInputDelegate.class.getName());

    /** the instance **/
    public static MapInputDelegate INSTANCE = new FrameInputDelegate();

    /**
     * {@inheritDoc}
     */
    public void processInput(BridgeComponentPeer peer, BridgeInputContext context, Map inputForm) throws InputException {
        Frame f = (Frame) peer.getComponent();
        if (f.getMenuBar() != null) {
            context.handleInput(f.getMenuBar(), inputForm);
        }
        super.processInput(peer, context, inputForm);
    }

    public void processWindowEvent(BridgeComponentPeer peer, BridgeInputContext context, Map inputForm) {
        Frame f = (Frame) peer.getComponent();
        String value = (String) inputForm.get(peer.getId());
        if ("maximize".equals(value)) {
            if (f.getMaximizedBounds() == null) {
                Dimension screenDim = context.getContext().getScreenSize();
                f.setMaximizedBounds(new Rectangle(0, 0, (int) screenDim.getWidth(), (int) screenDim.getHeight()));
            }
            Rectangle current = f.getBounds();
            f.setBounds(f.getMaximizedBounds());
            f.setMaximizedBounds(current);
            if (f.getExtendedState() == Frame.NORMAL) {
                f.setExtendedState(Frame.MAXIMIZED_BOTH);
            } else {
                f.setExtendedState(Frame.NORMAL);
            }
            f.validate();
        } else if ("minimize".equals(value)) {
            if (f.getExtendedState() == Frame.ICONIFIED) {
                f.setExtendedState(Frame.NORMAL);
            } else {
                f.setExtendedState(Frame.ICONIFIED);
            }
        } else {
            super.processWindowEvent(peer, context, inputForm);
        }
        return;
    }
}
