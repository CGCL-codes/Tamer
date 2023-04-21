package com.g2d.studio.gameedit;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import com.cell.rpg.template.TEffect;
import com.g2d.studio.Studio.ProgressForm;
import com.g2d.studio.gameedit.dynamic.DEffect;
import com.g2d.studio.gameedit.entity.ObjectGroup;
import com.g2d.studio.swing.G2DTreeNodeGroup;
import com.g2d.studio.swing.G2DTreeNodeGroup.GroupMenu;

public class EffectTreeView extends ObjectTreeViewTemplateDynamic<DEffect, TEffect> {

    private static final long serialVersionUID = 1L;

    public EffectTreeView(String title, com.g2d.studio.io.File objects_dir, ProgressForm progress) {
        super(title, DEffect.class, TEffect.class, objects_dir, progress);
    }

    @Override
    protected EffectGroup createTreeRoot(String title) {
        return new EffectGroup(title);
    }

    public class EffectGroup extends ObjectGroup<DEffect, TEffect> {

        private static final long serialVersionUID = 1L;

        public EffectGroup(String name) {
            super(name, EffectTreeView.this.list_file, EffectTreeView.this.node_type, EffectTreeView.this.data_type);
        }

        @Override
        protected G2DTreeNodeGroup<?> createGroupNode(String name) {
            return new EffectGroup(name);
        }

        @Override
        protected boolean createObjectNode(String key, TEffect data) {
            try {
                addNode(this, new DEffect(EffectTreeView.this, data));
                return true;
            } catch (Exception err) {
                err.printStackTrace();
            }
            return false;
        }

        @Override
        public void onClicked(JTree tree, MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3) {
                new EffectRootMenu(this).show(getTree(), e.getX(), e.getY());
            }
        }
    }

    class EffectRootMenu extends GroupMenu {

        private static final long serialVersionUID = 1L;

        EffectGroup root;

        JMenuItem add_effect = new JMenuItem("添加Effect");

        public EffectRootMenu(EffectGroup root) {
            super(root, getTree(), getTree());
            this.root = root;
            add_effect.addActionListener(this);
            add(add_effect);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == add_effect) {
                String name = JOptionPane.showInputDialog(getTree(), "输入Effect名字");
                if (name.length() > 0) {
                    DEffect effect = new DEffect(EffectTreeView.this, name);
                    addNode(root, effect);
                    getTree().reload(root);
                } else {
                    JOptionPane.showMessageDialog(getTree(), "名字不能为空");
                }
            } else {
                super.actionPerformed(e);
            }
        }
    }
}
