package saf.presentation;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import saf.simulation.FighterState;
import saf.structure.FightActionType;
import saf.structure.MoveActionType;

public class View extends JPanel {

    private JProgressBar leftProgressBar;

    private JProgressBar rightProgressBar;

    private ImagePanel leftPanel;

    private ImagePanel rightPanel;

    /**
	 * Create the panel.
	 */
    public View() {
        setLayout(null);
        leftProgressBar = new JProgressBar();
        leftProgressBar.setBounds(6, 6, 146, 20);
        add(leftProgressBar);
        rightProgressBar = new JProgressBar();
        rightProgressBar.setBounds(848, 6, 146, 20);
        add(rightProgressBar);
        leftPanel = new ImagePanel(new ImageIcon("input/images/stand_left.png").getImage());
        leftPanel.setBounds(100, 419, 125, 275);
        add(leftPanel);
        rightPanel = new ImagePanel(new ImageIcon("input/images/stand_right.png").getImage());
        rightPanel.setBounds(775, 419, 125, 275);
        add(rightPanel);
        this.setVisible(true);
    }

    public void updateFighterPresentation(FighterState fighterState) {
        ImagePanel panelToUpdate = leftPanel;
        JProgressBar progressToUpdate = leftProgressBar;
        ;
        String leftOrRight = "_left";
        switch(fighterState.getStartingPosition()) {
            case right:
                panelToUpdate = rightPanel;
                progressToUpdate = rightProgressBar;
                leftOrRight = "_right";
                break;
        }
        progressToUpdate.setValue(fighterState.getHealth());
        panelToUpdate.setLocation((int) fighterState.getFighterX() * 10, panelToUpdate.getY());
        String imageLoc = "";
        String fighting = "";
        if (fighterState.isActionPerform()) fighting = "_" + fighterState.getCurrentFightActionType().toString();
        switch(fighterState.getCurrentMoveActionType()) {
            case crouch:
                imageLoc = "crouch" + fighting + leftOrRight;
                break;
            case jump:
                if (fighterState.isActionPerform()) imageLoc = "jump" + fighting + leftOrRight; else imageLoc = "stand" + fighting + leftOrRight;
                break;
            case stand:
            case run_away:
            case run_towards:
            case walk_away:
            case walk_towards:
                imageLoc = "stand" + fighting + leftOrRight;
                break;
        }
        File imageFile = new File("input/images/" + imageLoc + ".png");
        Image image = getToolkit().getImage(imageFile.getPath());
        panelToUpdate.setImage(image);
    }
}

class ImagePanel extends JPanel {

    private Image img;

    public ImagePanel(String img) {
        this(new ImageIcon(img).getImage());
    }

    public ImagePanel(Image img) {
        this.img = img;
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
    }

    public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }

    public void setImage(Image image) {
        this.img = image;
    }
}
