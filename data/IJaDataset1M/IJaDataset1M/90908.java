package client.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import client.Client;
import commands.MoveCardCommand;
import core.Card;
import core.GameObjectObserver;
import core.Location;
import core.Visibility;

/**
 * The concrete View of a Card.
 */
public class CardView extends AbstractView {

    /**
	 * Create a new CardView for the given Card in the given ZoneView.
	 */
    public CardView(Card card, Client client) {
        super(card, client);
        this.card = card;
        final CardPanel cp = new CardPanel(card, client);
        setComponent(cp);
        cp.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent me) {
                if (me.getButton() == 1 && me.getModifiersEx() == MouseEvent.SHIFT_DOWN_MASK) {
                    SelectionRegistry.toggle(CardView.this.card);
                    cp.repaint(true);
                }
            }
        });
        component.addMouseListener(new ZOrderer());
        JMenu visibilityMenu = new JMenu("Visibility");
        visibilityMenu.add(new SetVisibilityAction("All", Visibility.ALL, card, client));
        visibilityMenu.add(new SetVisibilityAction("None", Visibility.NONE, card, client));
        visibilityMenu.add(new SetVisibilityAction("Default", card.getZone().getDefaultVisibility(), card, client));
        popupMenu.add(visibilityMenu);
        JMenu rotateCard = new JMenu("Rotation");
        rotateCard.add(new RotateCardAction(0));
        rotateCard.add(new RotateCardAction(0.5));
        rotateCard.add(new RotateCardAction(1));
        rotateCard.add(new RotateCardAction(1.5));
        popupMenu.add(rotateCard);
        popupMenu.add(new ShowInspectorAction());
    }

    /**
	 * Action to rotate a card by a specified number of degrees
	 */
    private class RotateCardAction extends AbstractAction {

        public RotateCardAction(double radiansOverPi) {
            super(Double.toString(radiansOverPi * 180) + (char) 0x00B0);
            rotation = radiansOverPi;
        }

        public void actionPerformed(ActionEvent arg0) {
            SelectionRegistry.add(card);
            try {
                for (Card card : SelectionRegistry.getSelectedCards()) {
                    Location newLoc = card.getLocation();
                    newLoc.setRotation(rotation);
                    client.sendCommand(new MoveCardCommand(card, card.getZone(), newLoc));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            SelectionRegistry.clear();
        }

        private final double rotation;
    }

    private class ShowInspectorAction extends javax.swing.AbstractAction {

        private ShowInspectorAction() {
            super("Show details");
        }

        public void actionPerformed(ActionEvent arg0) {
            javax.swing.JFrame cardFrame = new javax.swing.JFrame(card.getName());
            cardFrame.getContentPane().setLayout(new javax.swing.BoxLayout(cardFrame.getContentPane(), javax.swing.BoxLayout.Y_AXIS));
            for (core.Attribute<?> attribute : card.getBase().getAttributes()) cardFrame.getContentPane().add(new javax.swing.JLabel(attribute.getName() + ": " + attribute.getValue()));
            cardFrame.pack();
            cardFrame.setVisible(true);
        }
    }

    private final Card card;
}

/**
 * The visual representation of a Card object.  Paints the image of a Card
 * on a JPanel.  This component supports drag-and-drop operations (it can
 * be dragged to and from ZoneViews).
 */
class CardPanel extends JPanel {

    private final Card card;

    private Image image;

    private final Client client;

    /**
	 * Creates a visual representation of the given Card in the given ZoneView.
	 */
    public CardPanel(Card c, Client client) {
        card = c;
        this.client = client;
        image = createScaledImage(card, 1f);
        setSize(image.getWidth(null), image.getHeight(null));
        final DragSource dragSource = new DragSource();
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_MOVE, new DragGestureListener() {

            public void dragGestureRecognized(DragGestureEvent dge) {
                SelectionRegistry.add(card);
                dragSource.startDrag(dge, java.awt.Toolkit.getDefaultToolkit().createCustomCursor(createScaledImage(card, 0.5f), new java.awt.Point(0, 0), "Custom card pointer"), new CardMoveTransferable(new CardMoveTransferable.CardMove(SelectionRegistry.getSelectedCards())), null);
            }
        });
        card.addObserver(new GameObjectObserver() {

            public void update() {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        image = createScaledImage(card, 1f);
                        setSize(image.getWidth(null), image.getHeight(null));
                        setLocation(card.getLocation().getPosition());
                        repaint();
                    }
                });
            }
        });
    }

    public void repaint(boolean recalculateImage) {
        if (recalculateImage) image = createScaledImage(card, 1f);
        super.repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    private static String getImagePath(core.AbstractCard base, String module) {
        return "images/" + module + "/" + (base.getSet() == null ? "back.gif" : base.getSet() + "/" + base.getImage());
    }

    private java.awt.image.BufferedImage createScaledImage(Card card, float alpha) {
        Image image = new ImageIcon(getImagePath(card.getBase(), client.getModule())).getImage();
        double rotation = (card.getLocation() == null ? 0 : card.getLocation().getRotation() * Math.PI);
        AffineTransform at = AffineTransform.getRotateInstance(rotation);
        Point2D corners[] = { at.transform(new Point2D.Double(0, 0), null), at.transform(new Point2D.Double(100, 0), null), at.transform(new Point2D.Double(0, 133), null), at.transform(new Point2D.Double(100, 133), null) };
        double minX = 0, minY = 0, maxX = 0, maxY = 0;
        for (Point2D corner : corners) {
            if (corner.getX() < minX) minX = corner.getX();
            if (corner.getX() > maxX) maxX = corner.getX();
            if (corner.getY() < minY) minY = corner.getY();
            if (corner.getY() > maxY) maxY = corner.getY();
        }
        at.scale(100.0 / image.getWidth(null), 133.0 / image.getHeight(null));
        at.preConcatenate(AffineTransform.getTranslateInstance(-1 * minX, -1 * minY));
        java.awt.image.BufferedImage ghost = new java.awt.image.BufferedImage((int) (maxX - minX), (int) (maxY - minY), java.awt.image.BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D g = ghost.createGraphics();
        g.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC, alpha));
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(image, at, null);
        if (SelectionRegistry.contains(card)) {
            g.setStroke(new BasicStroke(2f));
            g.setColor(Color.RED);
            g.drawRect(1, 1, (int) (maxX - minX - 1), (int) (maxY - minY - 1));
        }
        g.dispose();
        return ghost;
    }
}
