package com.g2d.studio.scene.script;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import com.cell.CUtil;
import com.cell.rpg.scene.Actor;
import com.cell.rpg.scene.Scene;
import com.cell.rpg.scene.SceneTrigger;
import com.cell.rpg.scene.SceneTriggerEditable;
import com.cell.rpg.scene.SceneTriggerScriptable;
import com.cell.rpg.scene.SceneUnit;
import com.cell.rpg.scene.TriggerGenerator;
import com.cell.rpg.scene.Triggers;
import com.cell.rpg.scene.script.Scriptable;
import com.cell.rpg.scene.script.anno.EventType;
import com.cell.rpg.scene.script.entity.Player;
import com.cell.rpg.scene.script.trigger.Event;
import com.g2d.display.ui.TreeView;
import com.g2d.studio.res.Res;
import com.g2d.studio.scene.editor.SceneEditor;
import com.g2d.studio.scene.units.SceneActor;
import com.g2d.studio.swing.G2DDefaultTreeNode;
import com.g2d.studio.swing.G2DTree;
import com.g2d.studio.swing.G2DTreeNode;

@SuppressWarnings("serial")
public class TriggerGenerateTreeNode extends G2DTreeNode<G2DTreeNode<?>> {

    final TriggerGenerator root_object;

    final Class<? extends Scriptable> trigger_object_type;

    public TriggerGenerateTreeNode(SceneEditor se) {
        this(se.getSceneNode().getData(), com.cell.rpg.scene.script.entity.Scene.class);
        ArrayList<SceneUnit> sub_nodes = se.getRuntimeUnits();
        if (!sub_nodes.isEmpty()) {
            Collections.sort(sub_nodes, new Comparator<SceneUnit>() {

                @Override
                public int compare(SceneUnit o1, SceneUnit o2) {
                    return CUtil.getStringCompare().compare(o2.name, o1.name);
                }
            });
            G2DDefaultTreeNode group = new G2DDefaultTreeNode(new ImageIcon(Res.icons_bar[4]), "场景物体");
            for (SceneUnit su : sub_nodes) {
                TriggerGenerateTreeNode tn = new TriggerGenerateTreeNode(su, su.getTriggerObjectType());
                group.add(tn);
            }
            this.insert(group, 0);
        }
        {
            G2DDefaultTreeNode players = new G2DDefaultTreeNode(new ImageIcon(Res.icons_bar[4]), "场景玩家");
            TriggerGenerateTreeNode tn = new TriggerGenerateTreeNode(se.getSceneNode().getData().getPlayerTriggers(), Player.class);
            players.add(tn);
            this.insert(players, 1);
        }
    }

    public TriggerGenerateTreeNode(SceneUnit su) {
        this(su, su.getTriggerObjectType());
    }

    protected TriggerGenerateTreeNode(TriggerGenerator su, Class<? extends Scriptable> tot) {
        this.root_object = su;
        this.trigger_object_type = tot;
        for (SceneTrigger st : su.getTriggers()) {
            TriggerNode en = new TriggerNode(st);
            this.add(en);
        }
    }

    @Override
    protected ImageIcon createIcon() {
        if (root_object instanceof Scene) {
            return new ImageIcon(Res.icon_scene);
        }
        if (root_object instanceof SceneUnit) {
            return new ImageIcon(Res.icon_hd);
        }
        if (Player.class.isAssignableFrom(trigger_object_type)) {
            return new ImageIcon(Res.icon_res_2);
        }
        return null;
    }

    @Override
    public String getName() {
        return root_object.getTriggerObjectName();
    }

    @Override
    public void onRightClicked(JTree tree, MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON3) {
            new GenerateMenu((G2DTree) tree).show(tree, e.getX(), e.getY());
        }
    }

    private void addTrigger(TriggerNode tn) {
        if (this.root_object.addTrigger(tn.trigger)) {
            this.add(tn);
        }
    }

    private void removeTrigger(TriggerNode tn) {
        if (this.root_object.removeTrigger(tn.trigger)) {
            this.remove(tn);
        }
    }

    class GenerateMenu extends JPopupMenu implements ActionListener {

        G2DTree tree;

        JMenuItem item_add_scriptable_trigger = new JMenuItem("添加触发器(脚本)");

        JMenuItem item_add_editable_trigger = new JMenuItem("添加触发器(编辑)");

        public GenerateMenu(G2DTree tree) {
            this.tree = tree;
            item_add_scriptable_trigger.addActionListener(this);
            item_add_editable_trigger.addActionListener(this);
            add(item_add_scriptable_trigger);
            add(item_add_editable_trigger);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == item_add_scriptable_trigger) {
                Object res = JOptionPane.showInputDialog(tree, "输入名字", "UnamedScript");
                if (res != null) {
                    try {
                        SceneTriggerScriptable sts = new SceneTriggerScriptable(root_object, res.toString());
                        TriggerNode trigger = new TriggerNode(sts);
                        addTrigger(trigger);
                        tree.reload(TriggerGenerateTreeNode.this);
                    } catch (Exception err) {
                        JOptionPane.showMessageDialog(tree, "重复的名字!");
                    }
                }
            }
            if (e.getSource() == item_add_editable_trigger) {
                Object res = JOptionPane.showInputDialog(tree, "输入名字", "UnamedEditor");
                if (res != null) {
                    try {
                        SceneTriggerEditable sts = new SceneTriggerEditable(root_object, res.toString());
                        TriggerNode trigger = new TriggerNode(sts);
                        addTrigger(trigger);
                        tree.reload(TriggerGenerateTreeNode.this);
                    } catch (Exception err) {
                        JOptionPane.showMessageDialog(tree, "重复的名字!");
                    }
                }
            }
        }
    }

    /**
	 * 编辑一个触发器
	 * @author WAZA
	 */
    public class TriggerNode extends G2DTreeNode<G2DTreeNode<?>> {

        SceneTrigger trigger;

        TriggerPanel<?> edit_page;

        public TriggerNode(SceneTrigger trigger) {
            this.trigger = trigger;
            if (trigger instanceof SceneTriggerScriptable) {
                edit_page = new TriggerPanelScriptable((SceneTriggerScriptable) trigger, trigger_object_type, root_object);
            } else {
                edit_page = new TriggerPanelEditable((SceneTriggerEditable) trigger, trigger_object_type, root_object);
            }
        }

        @Override
        protected ImageIcon createIcon() {
            if (trigger instanceof SceneTriggerScriptable) {
                return new ImageIcon(Res.icon_action);
            } else {
                return new ImageIcon(Res.icon_condition);
            }
        }

        @Override
        public String getName() {
            if (trigger != null) {
                return trigger.getName();
            }
            return "null";
        }

        @Override
        public void onRightClicked(JTree tree, MouseEvent e) {
            new TriggerNodeMenu((G2DTree) tree).show(tree, e.getX(), e.getY());
        }

        TriggerPanel<?> getEditPage() {
            return edit_page;
        }

        class TriggerNodeMenu extends JPopupMenu implements ActionListener {

            G2DTree tree;

            JMenuItem rename = new JMenuItem("重命名");

            JMenuItem delete = new JMenuItem("删除");

            public TriggerNodeMenu(G2DTree tree) {
                this.tree = tree;
                rename.addActionListener(this);
                delete.addActionListener(this);
                add(rename);
                add(delete);
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == delete) {
                    if (JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(tree, "确定 ?")) {
                        removeTrigger(TriggerNode.this);
                        tree.reload(TriggerGenerateTreeNode.this);
                    }
                } else if (e.getSource() == rename) {
                    Object res = JOptionPane.showInputDialog(tree, "输入名字", TriggerNode.this.getName());
                    if (res != null) {
                        if (TriggerNode.this.trigger.setName(root_object, res.toString())) {
                            tree.reload(TriggerNode.this);
                            tree.repaint();
                        } else {
                            JOptionPane.showMessageDialog(tree, "重复的名字!");
                        }
                    }
                }
            }
        }
    }
}
