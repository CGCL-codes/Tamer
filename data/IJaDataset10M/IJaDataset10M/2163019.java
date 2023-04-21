package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Classe permettant l'affichage et la gestion de l'�diteur interne � la note.
 * L'�diteur interne permet d'entrer du texte directement dans la note sans passer par une fenetre externe.
 * @author Antoine Blanchet
 */
public class InternalEditor extends JPanel {

    private static final long serialVersionUID = 1L;

    private boolean networkNote;

    private JTextArea textArea;

    private DrawingPanel panel;

    private JScrollPane scrollPane;

    private JPanel moverContainer;

    private JPanel mover;

    private int width;

    private int height;

    private JPanel placementPanel;

    private Point initialPoint;

    private Point newPoint;

    /**
	 * Constructeur de la classe InternalEditor
	 * @author Antoine Blanchet
	 */
    public InternalEditor() {
        super();
        initialize();
    }

    /**
	 * M�thode d'initialisation de l'InternalEditor
	 * Permet de g�nerer l'UI et de lui donner des valeurs par d�faut
	 * @author Antoine Blanchet
	 */
    private void initialize() {
        this.width = 300;
        this.height = 150;
        this.initialPoint = new Point();
        this.newPoint = new Point(-1, -1);
        this.setLayout(new BorderLayout());
        this.add(getPlacementPanel(), BorderLayout.NORTH);
        this.add(getScrollPane(), BorderLayout.CENTER);
        this.add(getMoverContainer(), BorderLayout.EAST);
    }

    /**
	 * M�thode d'affichage de l'�diteur interne
	 * @param dp drawing panel sur lequel l'�diteur va s'afficher
	 * @param position la position � laquelle il va s'afficher
	 * @author Antoine Blanchet
	 */
    public void show(DrawingPanel dp, Point position) {
        this.panel = dp;
        dp.add(this);
        this.setBounds(position.x, position.y, this.width, this.height);
        this.newPoint = position;
        getTextArea().requestFocus();
        dp.updateUI();
    }

    /**
	 * M�thode d'affichage de l'�diteur interne
	 * @param dp drawing panel sur lequel l'�diteur va s'afficher
	 * @param position la position � laquelle il va s'afficher
	 * @param text texte qui remplit l'�diteur interne par d�faut
	 * @author Antoine Blanchet
	 */
    public void show(DrawingPanel dp, Point position, String text) {
        getTextArea().setText(text);
        show(dp, position);
    }

    public void hide() {
        this.panel.remove(this);
        getTextArea().setText("");
        this.panel.updateUI();
    }

    /**
	 * Permet de changer la typographie du texte entr�e dans l'�diteur interne
	 * @param font police du texte
	 * @param color couleur du texte
	 * @author Antoine Blanchet
	 */
    public void changeTypo(Font font, Color color) {
        getTextArea().setFont(font);
        getTextArea().setForeground(color);
    }

    /**
	 * Pr�pare la JTextArea de l'�diteur interne
	 * @return la zone de texte dans laquelle on pourra entrer du texte
	 * @author Antoine Blanchet
	 */
    public JTextArea getTextArea() {
        if (this.textArea == null) {
            this.textArea = new JTextArea();
            this.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        }
        return this.textArea;
    }

    /**
	 * Pr�pare le JScrollPane
	 * @return JScrollPane
	 * @author Antoine Blanchet
	 */
    public JScrollPane getScrollPane() {
        if (this.scrollPane == null) {
            this.scrollPane = new JScrollPane(getTextArea());
            this.scrollPane.setBorder(null);
        }
        return this.scrollPane;
    }

    /**
	 * Retourne vrai si la note en �dition est la network note, false sinon
	 * @return true/false en fonction de la networknote
	 * @author Antoine Blanchet
	 */
    public boolean isNetworkNote() {
        return networkNote;
    }

    /**
	 * D�fini si la note active est la networknote
	 * @param networkNote true/false
	 * @author Antoine Blanchet
	 */
    public void setNetworkNote(boolean networkNote) {
        this.networkNote = networkNote;
    }

    /**
	 * G�nere le JPanel qui contiendra le composant pour changer la taille de l'�diteur interne
	 * @return un jpanel
	 * @author Antoine Blanchet
	 */
    public JPanel getMoverContainer() {
        if (this.moverContainer == null) {
            this.moverContainer = new JPanel();
            this.moverContainer.setPreferredSize(new Dimension(10, 10));
            this.moverContainer.setBackground(Color.WHITE);
            this.moverContainer.setLayout(new BorderLayout());
            this.moverContainer.add(getMover(), BorderLayout.SOUTH);
        }
        return moverContainer;
    }

    /**
	 * Composant permettant de modifier la taille de l'�diteur interne
	 * @return JPanel
	 * @author Antoine Blanchet
	 */
    public JPanel getMover() {
        if (this.mover == null) {
            this.mover = new JPanel();
            this.mover.setPreferredSize(new Dimension(10, 10));
            this.mover.setBackground(new Color(220, 220, 220));
            this.mover.setBorder(BorderFactory.createEmptyBorder(-5, 0, 0, 0));
            this.mover.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    mover.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
                }
            });
            this.mover.addMouseMotionListener(new MouseMotionListener() {

                @Override
                public void mouseDragged(MouseEvent e) {
                    updateSize(e.getPoint());
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                }
            });
        }
        return mover;
    }

    /**
	 * M�thode appell�e au moment de la modification de la taille de l'�diteur interne
	 * @param fin point en bas � droite de la fenetre, permet de calculer la nouvelle taille et de l'afficher
	 * @author Antoine Blanchet
	 */
    public void updateSize(Point fin) {
        Dimension size = this.getSize();
        double width = size.getWidth();
        double height = size.getHeight();
        double newWidht = width + fin.getX();
        double newHeight = height + fin.getY();
        if (newWidht >= 10 && newHeight >= 10) {
            this.width = (int) newWidht;
            this.height = (int) newHeight;
            this.setBounds((int) this.getLocation().getX(), (int) this.getLocation().getY(), (int) newWidht, (int) newHeight);
            this.updateUI();
        }
    }

    /**
	 * Panel contenant le composant pour d�placer l'�diteur interne
	 * @return JPanel
	 * @author Antoine Blanchet
	 */
    public JPanel getPlacementPanel() {
        if (this.placementPanel == null) {
            this.placementPanel = new JPanel();
            this.placementPanel.setPreferredSize(new Dimension(10, 10));
            this.placementPanel.setBackground(new Color(220, 220, 220));
            this.placementPanel.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    placementPanel.setCursor(new Cursor(Cursor.MOVE_CURSOR));
                }

                @Override
                public void mousePressed(MouseEvent arg0) {
                    super.mousePressed(arg0);
                    initialPoint = arg0.getPoint();
                }
            });
            this.placementPanel.addMouseMotionListener(new MouseMotionListener() {

                @Override
                public void mouseDragged(MouseEvent e) {
                    updatePlacement(e.getPoint());
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                }
            });
        }
        return placementPanel;
    }

    /**
	 * M�thode appell�e au moment du d�placement de l'�diteur interne
	 * @param position Nouvelle position de l'�diteur, permet de calculer les coordon�es de l'�diteur et de l'afficher
	 * @author Antoine Blanchet
	 */
    public void updatePlacement(Point position) {
        double newX = this.getLocation().getX() + position.getX() - this.initialPoint.getX();
        double newY = this.getLocation().getY() + position.getY();
        this.setLocation(new Point((int) newX, (int) newY));
        this.newPoint = new Point((int) newX, (int) newY);
        this.updateUI();
    }

    /**
	 * Retourne le nouveau point
	 * @return Point
	 * @author Antoine Blanchet
	 */
    public Point getNewPoint() {
        return newPoint;
    }

    /**
	 * permet de vider le coordon�es du point initial
	 * @author Antoine Blanchet
	 */
    public void removeInitialPoint() {
        this.newPoint = new Point(-1, -1);
    }
}
