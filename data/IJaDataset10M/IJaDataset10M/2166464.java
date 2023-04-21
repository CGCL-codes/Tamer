package com.g2d.studio.old.actor;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import com.cell.CIO;
import com.cell.game.CSprite;
import com.cell.rpg.entity.Actor;
import com.g2d.Tools;
import com.g2d.cell.CellSetResource;
import com.g2d.cell.game.SceneSprite;
import com.g2d.display.DisplayObjectContainer;
import com.g2d.display.Stage;
import com.g2d.editor.DisplayObjectEditor;
import com.g2d.studio.Version;
import com.g2d.studio.old.AFormDisplayObjectViewer;
import com.g2d.studio.old.ATreeNodeLeaf;
import com.g2d.studio.old.swing.AbilityPanel;
import com.g2d.studio.old.swing.RPGUnitPanel;
import com.g2d.util.Drawing;

public class FormActorViewer extends AFormDisplayObjectViewer<SceneSprite> {

    private static final long serialVersionUID = Version.VersionGS;

    /**
	 * 为单位绑定RPG属性
	 */
    private Actor rpg_actor;

    JButton tool_info = new JButton("编辑属性");

    public FormActorViewer(ATreeNodeLeaf<FormActorViewer> leaf, CellSetResource set, String sprID) {
        super(leaf, new SceneSprite(set, sprID));
        canvas.getCanvasAdapter().setStage(new ActorStage());
        setSize(view_object.getWidth() * 2, view_object.getHeight() * 2);
        this.setTitle("actor " + sprID);
        this.rpg_actor = new Actor(getCpjName(), getCpjObjectID());
        ButtonGroup tool_group = new ButtonGroup();
        {
            super.addToolButton(tool_info, "单位绑定的属性", tool_group);
            tool_info.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    DisplayObjectEditor<SceneSprite> form = new DisplayObjectEditor<SceneSprite>(getViewObject(), new RPGUnitPanel(rpg_actor), new AbilityPanel(null, rpg_actor));
                    form.setLocation(getX() + getWidth(), getY());
                    form.setVisible(true);
                }
            });
        }
    }

    public Actor createRPGActor() {
        return CIO.cloneObject(rpg_actor);
    }

    @Override
    public void loadObject(ObjectInputStream is, File file) throws Exception {
        this.rpg_actor = (Actor) is.readObject();
    }

    @Override
    public void saveObject(ObjectOutputStream os, File file) throws Exception {
        os.writeObject(rpg_actor);
    }

    @Override
    public ImageIcon getSnapshot(boolean update) {
        if (snapshot == null || update) {
            try {
                CSprite spr = view_object.getSprite();
                Image img = spr.getFrameImage(0, 0, 0).getSrc();
                img = img.getScaledInstance(32, 32, Image.SCALE_FAST);
                snapshot = Tools.createIcon(img);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return snapshot;
    }

    class ActorStage extends Stage {

        public ActorStage() {
            addChild(view_object);
        }

        public void added(DisplayObjectContainer parent) {
        }

        public void removed(DisplayObjectContainer parent) {
        }

        public void render(Graphics2D g) {
            g.setColor(Color.GREEN);
            g.fill(local_bounds);
            g.setColor(Color.WHITE);
            Drawing.drawStringBorder(g, "Animate: " + view_object.getSprite().getCurrentAnimate() + "/" + view_object.getSprite().getAnimateCount(), 1, 1, 0);
            Drawing.drawStringBorder(g, "Frame: " + view_object.getSprite().getCurrentFrame() + "/" + view_object.getSprite().getFrameCount(view_object.getSprite().getCurrentAnimate()), 1, 20, 0);
        }

        public void update() {
            if (getRoot().isMouseWheelUP()) {
                view_object.getSprite().nextAnimate(-1);
            }
            if (getRoot().isMouseWheelDown()) {
                view_object.getSprite().nextAnimate(1);
            }
            if (getRoot().isMouseDown(MouseEvent.BUTTON3)) {
            }
            view_object.getSprite().nextCycFrame();
            view_object.setLocation(getWidth() / 2, getHeight() / 2);
        }
    }
}
