package com.g2d.studio.rpg;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import com.cell.CIO;
import com.cell.rpg.ability.Abilities;
import com.g2d.editor.property.CellEditAdapter;
import com.g2d.editor.property.ObjectPropertyEdit;
import com.g2d.editor.property.PropertyCellEdit;
import com.g2d.studio.res.Res;
import com.g2d.awt.util.*;

/**
 * @author WAZA
 * 可编辑多个能力的窗口
 */
public class AbilityForm extends AbstractDialog implements PropertyCellEdit<Abilities> {

    private static final long serialVersionUID = 1L;

    ObjectPropertyEdit property_panel;

    JButton button;

    final AbilityPanel ability_panel;

    final Abilities abilities;

    JButton btn_ok = new JButton("OK");

    Abilities result;

    public AbilityForm(ObjectPropertyEdit owner, Abilities abilities, CellEditAdapter<?>... adapters) {
        super(owner.getComponent());
        super.setLayout(new BorderLayout());
        this.abilities = CIO.cloneObject(abilities);
        this.result = abilities;
        this.ability_panel = new AbilityPanel(this.abilities, adapters);
        this.add(ability_panel, BorderLayout.CENTER);
        this.setTitle(this.abilities.toString());
        this.setIconImage(Res.icon_edit);
        this.setSize(700, 400);
        this.setCenter();
        JPanel south = new JPanel(new FlowLayout());
        south.add(btn_ok);
        btn_ok.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                result = ability_panel.abilities;
                AbilityForm.this.setVisible(false);
            }
        });
        this.add(south, BorderLayout.SOUTH);
        button = new JButton(this.abilities.toString());
        button.setActionCommand("ok");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                AbilityForm.this.setVisible(true);
            }
        });
    }

    public Component getComponent(ObjectPropertyEdit panel) {
        property_panel = panel;
        button.setText(abilities.toString());
        return button;
    }

    public Abilities getValue() {
        return result;
    }
}
