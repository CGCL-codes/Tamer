package com.g2d.studio.gameedit.dynamic;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import com.cell.CIO;
import com.cell.rpg.template.TEffect;
import com.g2d.awt.util.Tools;
import com.g2d.display.particle.Layer;
import com.g2d.studio.gameedit.EffectEditor;
import com.g2d.studio.gameedit.EffectTreeView;
import com.g2d.studio.gameedit.ObjectViewer;
import com.g2d.studio.gameedit.EffectTreeView.EffectGroup;
import com.g2d.studio.res.Res;

public final class DEffect extends DynamicNode<TEffect> {

    final EffectTreeView factory;

    public DEffect(IDynamicIDFactory<DEffect> f, String name) {
        super(f, name);
        this.factory = (EffectTreeView) f;
    }

    public DEffect(IDynamicIDFactory<DEffect> f, TEffect effect) {
        super(effect, Integer.parseInt(effect.id), effect.name);
        this.factory = (EffectTreeView) f;
    }

    @Override
    protected TEffect newData(IDynamicIDFactory<?> f, String name, Object... args) {
        return new TEffect(getIntID(), name);
    }

    @Override
    public boolean setName(String name) {
        if (super.setName(name)) {
            getData().name = name;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ImageIcon getIcon(boolean update) {
        return Tools.createIcon(Res.icon_res_8);
    }

    @Override
    public void onRightClicked(JTree tree, MouseEvent e) {
        new EffectMenu().show(tree, e.getX(), e.getY());
    }

    @Override
    public ObjectViewer<?> getEditComponent() {
        onOpenEdit();
        if (edit_component == null) {
            edit_component = new EffectViewer();
        }
        return edit_component;
    }

    @Override
    public void onSave() {
        super.onSave();
        if (edit_component instanceof EffectViewer) {
            ((EffectViewer) edit_component).editor.getData();
        }
    }

    public class EffectMenu extends DynamicNodeMenu {

        protected JMenuItem clone = new JMenuItem("复制");

        public EffectMenu() {
            super(DEffect.this);
            super.add(clone);
            super.remove(delete);
            super.add(delete);
            clone.addActionListener(this);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == clone) {
                if (node.getParent() instanceof EffectGroup) {
                    EffectGroup root = (EffectGroup) node.getParent();
                    TEffect src = CIO.cloneObject(DEffect.this.getData());
                    DEffect effect = new DEffect(factory, src.name + " Copy");
                    effect.getData().particles.clear();
                    for (Layer layer : src.particles) {
                        effect.getData().particles.addLayer(layer);
                    }
                    factory.addNode(root, effect);
                    factory.getTree().reload(root);
                }
            } else {
                super.actionPerformed(e);
            }
        }
    }

    public class EffectViewer extends ObjectViewer<DEffect> {

        private static final long serialVersionUID = 1L;

        EffectEditor editor;

        public EffectViewer() {
            super(DEffect.this);
            editor = new EffectEditor(getData());
            table.remove(page_object_panel);
            table.remove(page_abilities);
            table.addTab("粒子编辑器", editor);
        }

        @Override
        protected void appendPages(JTabbedPane table) {
        }
    }
}
