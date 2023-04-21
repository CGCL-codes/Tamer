package ipcards.gui;

import ipcards.events.*;
import ipcards.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.InputEvent;
import java.awt.geom.AffineTransform;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class TablePanel extends JPanel implements Scrollable, PlayerActionListener {

    private static final int MOVE_DELAY = 5;

    private int delay = 0;

    private Point initial;

    private static final int STATE_NORMAL = 0;

    private static final int STATE_SELECT_RECT = 1;

    private static final int STATE_SELECT_RECT_SHIFT = 2;

    private static final int onmask = InputEvent.SHIFT_DOWN_MASK | InputEvent.BUTTON1_DOWN_MASK;

    private int state = STATE_NORMAL;

    private SelectionBox selectRectangle;

    private CardSet savedSelectedCards;

    private Color green = new Color(0.0f, 0.5f, 0.0f);

    private Color red = new Color(0.5f, 0.0f, 0.0f);

    private Color blue = new Color(0.0f, 0.0f, 0.5f);

    private Color purple = new Color(0.25f, 0.0f, 0.25f);

    private Color colour = Color.black;

    private Color turnindicator = Color.white;

    private Image backgroundImage = null;

    private PlayerActionPerformer performer;

    private Room room;

    private View view;

    private Dimension size = new Dimension(800, 600);

    private double zoomAmount = 1.0;

    private boolean zoomEnable = true;

    private AffineTransform at = new AffineTransform();

    public TablePanel(PlayerModel model, View v) {
        super();
        view = v;
        Dbg.println("TablePanel Constructor");
        changeBackgroundColour("green");
        MIL mil = new MIL();
        this.addMouseListener(mil);
        this.addMouseMotionListener(mil);
        setPlayer(model);
        updatePanelSize();
        repaint();
    }

    private void updatePanelSize() {
        Dimension tablesize = room.table.size;
        size = new Dimension((int) (tablesize.width * zoomAmount), (int) (tablesize.height * zoomAmount));
        this.setPreferredSize(size);
        revalidate();
        Dbg.println("Table Size: " + size.getWidth() + ", " + size.getHeight());
        repaint();
    }

    public void playerActionPerformed(PlayerAction pa) {
        if (pa.getAction() == PlayerAction.NEW_GAME) {
            Game g = room.table.game;
            view.updateButtonPanel(g.createButtonPanel());
        }
        repaint();
    }

    public void repaint() {
        Dbg.println("TablePanel.repaint");
        super.repaint();
    }

    public void paintComponent(Graphics g) {
        Dbg.println("TablePanel.paintComponent");
        if (backgroundImage == null) {
            super.paintComponent(g);
        } else {
            g.drawImage(backgroundImage, 0, 0, this);
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.transform(at);
        if (room == null) return;
        CardSet cards = room.table.cards;
        if (room.table.drawingList.isEmpty()) {
            for (Card card : room.table.cards) {
                CardView.drawCard(g2d, card, this);
            }
        }
        for (int i = 0; i < room.table.drawingList.size(); ++i) {
            CardView.drawCard(g2d, room.table.drawingList.get(i), this);
        }
        if (room.table.allPlayers.size() > 0) {
            int turn = room.table.turn;
            int numPlayers = room.table.allPlayers.size();
            double angle = 360 / (numPlayers);
            double radius = 300 + (numPlayers - 1) * 3;
            int x = (int) (radius * Math.cos(Math.toRadians(turn * angle + 90)));
            int y = (int) (radius * Math.sin(Math.toRadians(turn * angle + 90)));
            g2d.setColor(turnindicator);
            g2d.fillOval(x + 465 - 15, y + 465 - 15, 30, 30);
        }
        if (state == STATE_SELECT_RECT || state == STATE_SELECT_RECT_SHIFT) {
            g2d.setColor(Color.black);
            g2d.draw(selectRectangle);
        }
    }

    public void setPlayer(PlayerModel player) {
        performer = player;
        room = player.room;
        room.localPlayer = room.localPlayer;
        room.localPlayer.addPlayerActionListener(this);
        CardView.setRoom(room);
    }

    public class MIL extends MouseInputAdapter {

        private Point prev = new Point();

        public void mousePressed(MouseEvent e) {
            translateMouse(e);
            if ((e.getModifiersEx() & (onmask)) == onmask) {
                savedSelectedCards = new CardSet();
                savedSelectedCards.addAll(room.localPlayer.selectedCards);
                Card selectedCard = null;
                int z = -1;
                Point p = e.getPoint();
                prev = p;
                for (Card card : room.table.cards) {
                    if (card.getRectangle().contains(p)) {
                        if (card.getZ() > z) {
                            z = card.getZ();
                            selectedCard = card;
                            initial = (Point) p.clone();
                        }
                    }
                }
                state = STATE_NORMAL;
                if (selectedCard == null) {
                    state = STATE_SELECT_RECT_SHIFT;
                    selectRectangle = new SelectionBox();
                    selectRectangle.setStart(e.getX(), e.getY());
                    selectRectangle.setEnd(e.getX(), e.getY());
                } else {
                    if (!room.localPlayer.isSelectedCard(selectedCard)) {
                        performer.performPlayerAction(PlayerAction.newSelectionAdd(this, new CardSet(selectedCard)));
                    } else {
                        performer.performPlayerAction(PlayerAction.newSelectionRemove(this, new CardSet(selectedCard)));
                    }
                    if (e.isPopupTrigger()) {
                        showContextMenu(e.getX(), e.getY());
                    } else {
                        performer.performPlayerAction(PlayerAction.newHold(this, null));
                    }
                }
            } else {
                savedSelectedCards = new CardSet();
                Card selectedCard = null;
                int z = -1;
                Point p = e.getPoint();
                prev = p;
                for (Card card : room.table.cards) {
                    if (card.getRectangle().contains(p)) {
                        if (card.getZ() > z) {
                            z = card.getZ();
                            selectedCard = card;
                            initial = (Point) p.clone();
                        }
                    }
                }
                state = STATE_NORMAL;
                if (selectedCard == null) {
                    performer.performPlayerAction(PlayerAction.newSelectionRemove(this, null));
                    state = STATE_SELECT_RECT;
                    selectRectangle = new SelectionBox();
                    selectRectangle.setStart(e.getX(), e.getY());
                    selectRectangle.setEnd(e.getX(), e.getY());
                } else {
                    if (!room.localPlayer.isSelectedCard(selectedCard)) {
                        performer.performPlayerAction(PlayerAction.newSelectionRemove(this, null));
                        performer.performPlayerAction(PlayerAction.newSelectionAdd(this, new CardSet(selectedCard)));
                    }
                    if (e.isPopupTrigger()) {
                        showContextMenu(e.getX(), e.getY());
                    } else {
                        performer.performPlayerAction(PlayerAction.newHold(this, null));
                    }
                }
            }
        }

        public void mouseDragged(MouseEvent e) {
            translateMouse(e);
            Point curr = e.getPoint();
            Point diff = new Point(curr.x - prev.x, curr.y - prev.y);
            prev = curr;
            CardSet selCards;
            switch(state) {
                case STATE_NORMAL:
                    for (Card c : room.localPlayer.selectedCards) {
                        c.moveBy(diff);
                    }
                    repaint();
                    if (delay++ >= MOVE_DELAY) {
                        performer.performPlayerAction(PlayerAction.newMove(this, null, new Point(curr.x - initial.x, curr.y - initial.y)));
                        delay = 0;
                        initial = (Point) curr.clone();
                    }
                    break;
                case STATE_SELECT_RECT:
                    selectRectangle.setEnd(e.getX(), e.getY());
                    selCards = new CardSet();
                    for (Card card : room.table.cards) {
                        if (card.getRectangle().intersects(selectRectangle)) {
                            selCards.add(card);
                        }
                    }
                    performer.performPlayerAction(PlayerAction.newSelectionRemove(this, null));
                    performer.performPlayerAction(PlayerAction.newSelectionAdd(this, selCards));
                    repaint();
                    break;
                case STATE_SELECT_RECT_SHIFT:
                    selectRectangle.setEnd(e.getX(), e.getY());
                    selCards = new CardSet();
                    for (Card card : room.table.cards) {
                        if (card.getRectangle().intersects(selectRectangle)) {
                            selCards.add(card);
                        }
                    }
                    performer.performPlayerAction(PlayerAction.newSelectionRemove(this, null));
                    performer.performPlayerAction(PlayerAction.newSelectionAdd(this, savedSelectedCards));
                    performer.performPlayerAction(PlayerAction.newSelectionAdd(this, selCards));
                    repaint();
                    break;
            }
        }

        public void mouseReleased(MouseEvent e) {
            translateMouse(e);
            switch(state) {
                case STATE_NORMAL:
                    if ((e.getModifiersEx() & (onmask)) == onmask) {
                        Card selectedCard = null;
                        int z = -1;
                        Point p = e.getPoint();
                        prev = p;
                        for (Card card : room.table.cards) {
                            if (card.getRectangle().contains(p)) {
                                if (card.getZ() > z) {
                                    z = card.getZ();
                                    selectedCard = card;
                                    initial = (Point) p.clone();
                                }
                            }
                        }
                        performer.performPlayerAction(PlayerAction.newSelectionRemove(this, new CardSet(selectedCard)));
                    } else {
                    }
                    performer.performPlayerAction(PlayerAction.newRelease(this, null));
                    break;
                case STATE_SELECT_RECT:
                case STATE_SELECT_RECT_SHIFT:
                    state = STATE_NORMAL;
                    performer.performPlayerAction(PlayerAction.newHold(this, room.localPlayer.selectedCards));
                    repaint();
                    break;
            }
        }

        public void translateMouse(MouseEvent e) {
            e.translatePoint((int) (-e.getX() + e.getX() / at.getScaleX()), (int) (-e.getY() + e.getY() / at.getScaleY()));
        }
    }

    public void changeBackgroundColour(String colourstr) {
        if (colourstr.equals("green")) {
            colour = green;
            turnindicator = purple;
        } else if (colourstr.equals("red")) {
            colour = red;
            turnindicator = blue;
        } else if (colourstr.equals("blue")) {
            colour = blue;
            turnindicator = red;
        } else if (colourstr.equals("purple")) {
            colour = purple;
            turnindicator = green;
        }
        this.setBackground(colour);
    }

    /**
	 * @param zoom the at to set
	 */
    public void zoom(int zoom) {
        double zin = 0.95;
        double zout = 1. / zin;
        double zoomMax = 1.0;
        double zoomMin = 0.1;
        if (zoom > 0) {
            zoomAmount *= zin;
        }
        if (zoom < 0) {
            zoomAmount *= zout;
        }
        if (zoomEnable == true) {
            zoomAmount = Math.min(zoomMax, zoomAmount);
            zoomAmount = Math.max(zoomMin, zoomAmount);
        }
        at = AffineTransform.getScaleInstance(zoomAmount, zoomAmount);
        updatePanelSize();
    }

    public void setZoom(boolean z) {
        zoomEnable = z;
    }

    public Dimension getPreferredScrollableViewportSize() {
        return new Dimension(800, 600);
    }

    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 100;
    }

    public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 10;
    }

    private void showContextMenu(int x, int y) {
        view.cardPopupMenu.getPopupMenu().show(this, x, y);
    }

    public void getImage(String filePath) throws InterruptedException, IOException {
        if (filePath == "none") {
            backgroundImage = null;
        } else {
            FileInputStream in = new FileInputStream(filePath);
            byte[] b = new byte[in.available()];
            in.read(b);
            in.close();
            backgroundImage = Toolkit.getDefaultToolkit().createImage(b);
            MediaTracker mt = new MediaTracker(this);
            mt.addImage(backgroundImage, 0);
            mt.waitForAll();
        }
    }
}
