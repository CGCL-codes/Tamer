package laborhazi;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class LemmingAnim extends Animation {

    private Viewer viewer;

    private Image image;

    private ImageIcon im;

    public LemmingAnim(Viewer v) {
        viewer = v;
        im = new ImageIcon("lemming.gif");
    }

    public void animate(Lemming lemming, Graphics g, int phase) {
        image = im.getImage();
        int[] position;
        int[] posField;
        int[] oldPosField;
        position = new int[2];
        posField = new int[2];
        oldPosField = new int[2];
        position = viewer.fieldToView(lemming.getPosition());
        posField = lemming.getPosition();
        oldPosField = lemming.getOldPos();
        if (oldPosField[0] == posField[0]) {
            if (oldPosField[1] != posField[1]) {
                position[1] = position[1] - 20 + phase;
            }
        } else if (oldPosField[0] < posField[0]) {
            if (oldPosField[1] == posField[1]) {
                position[0] = position[0] - 20 + phase;
            } else {
                position[0] = position[0] - 20 + phase;
                position[1] = position[1] + 20 - phase;
            }
        } else if (oldPosField[0] > posField[0]) {
            if (oldPosField[1] == posField[1]) {
                position[0] = position[0] + 20 - phase;
            } else {
                position[0] = position[0] + 20 - phase;
                position[1] = position[1] + 20 - phase;
            }
        }
        if (lemming.isErecting()) {
            Image erectIm;
            ImageIcon imageIc;
            if (lemming.getDir() == 1) {
                imageIc = new ImageIcon("build_to_right.gif");
            } else imageIc = new ImageIcon("build_to_left.gif");
            erectIm = imageIc.getImage();
            g.drawImage(erectIm, position[0], position[1], 18, 20, viewer);
        } else if (lemming.isDrilling()) {
            Image drillIm;
            ImageIcon imageIc;
            if (lemming.getDir() == 1) {
                imageIc = new ImageIcon("drill_to_right.gif");
            } else imageIc = new ImageIcon("drill_to_left.gif");
            drillIm = imageIc.getImage();
            g.drawImage(drillIm, position[0], position[1] + 1, 20, 19, viewer);
        } else if (lemming.isParachuting()) {
            Image parachuteIm;
            ImageIcon imageIc;
            imageIc = new ImageIcon("parachute.gif");
            parachuteIm = imageIc.getImage();
            g.drawImage(parachuteIm, position[0], position[1] + 4, 19, 20, viewer);
        } else if (lemming.getDir() == -1) {
            Image barricadeIm;
            ImageIcon imageIc;
            imageIc = new ImageIcon("barricade.gif");
            barricadeIm = imageIc.getImage();
            g.drawImage(barricadeIm, position[0], position[1] + 4, 19, 16, viewer);
        } else {
            Image walkIm;
            ImageIcon imageIc;
            if (lemming.getDir() == 1) imageIc = new ImageIcon("walk_to_right.gif"); else imageIc = new ImageIcon("walk_to_left.gif");
            walkIm = imageIc.getImage();
            g.drawImage(walkIm, position[0], position[1] + 4, 14, 16, viewer);
        }
        if (lemming.isExploding()) {
            Image explodeIm;
            ImageIcon imageIc;
            imageIc = new ImageIcon("expl_sign.gif");
            explodeIm = imageIc.getImage();
            g.drawImage(explodeIm, position[0], position[1], 10, 10, viewer);
        }
    }

    private void stepRigh() {
    }

    private void stepRighUp() {
    }

    private void stepLeft() {
    }

    private void stepLeftUp() {
    }
}
