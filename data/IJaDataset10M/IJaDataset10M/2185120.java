package client.gui.replay;

import client.ClientAction;
import client.filter.ClientFilter;
import client.gui.util.FieldPanel;
import java.awt.*;
import java.util.Vector;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import utils.Position;

public class ReplayFieldPanel extends FieldPanel {

    private ClientFilter filter = null;

    public ReplayFieldPanel(String hostName, String guestName, Vector<Vector<ClientAction>> hostActions, Vector<Vector<ClientAction>> guestActions) {
        super(hostName, guestName, hostActions, guestActions);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        float image_ratio = (float) field.getWidth() / field.getHeight();
        float panel_ratio = (float) this.getWidth() / this.getHeight();
        float ratio = 0.0f;
        if (image_ratio > panel_ratio) ratio = this.getWidth() / (float) field.getWidth(); else ratio = this.getHeight() / (float) field.getHeight();
        xSize = (int) (ratio * field.getWidth());
        ySize = (int) (ratio * field.getHeight());
        int xPos = (this.getWidth() - xSize) / 2;
        int yPos = (this.getHeight() - ySize) / 2;
        g2.drawImage(this.field, xPos, yPos, xSize, ySize, this);
        int totalSet = hostActions.size();
        for (int j = 0; j < totalSet; j++) {
            Vector<ClientAction> hostAc = hostActions.get(j);
            Vector<ClientAction> guestAc = guestActions.get(j);
            int nbHostSetActions = hostAc.size();
            int nbGuestSetActions = guestAc.size();
            int hPos = 0;
            int gPos = 0;
            while (hPos < nbHostSetActions || gPos < nbGuestSetActions) {
                int hID = (nbHostSetActions <= hPos) ? MAX_ACTION_ID : hostAc.get(hPos).getID();
                int gID = (nbGuestSetActions <= gPos) ? MAX_ACTION_ID : guestAc.get(gPos).getID();
                ClientAction action = null;
                BufferedImage im = null;
                if (hID < gID) {
                    action = hostAc.get(hPos);
                    im = hostImages.get(action.getTypeOfAction());
                    hPos++;
                } else {
                    action = guestAc.get(gPos);
                    im = guestImages.get(action.getTypeOfAction());
                    gPos++;
                }
                if (filter == null || filter.filter(action)) {
                    Position position = action.getPosition();
                    g2.drawImage(im, xPos + (int) (position.x() * xSize - ACTION_SIZE / 2.0), yPos + (int) (position.y() * ySize - ACTION_SIZE / 2.0), ACTION_SIZE, ACTION_SIZE, this);
                }
            }
        }
    }

    public void setClientFilter(ClientFilter filter) {
        this.filter = filter;
        repaint();
    }

    public void filterUpdated() {
        repaint();
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}
