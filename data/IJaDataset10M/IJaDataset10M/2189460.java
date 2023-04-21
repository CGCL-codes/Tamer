package hci;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.PopupMenu;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import logic.Fighter;
import logic.Item;
import main.SourceOfAllThings;
import main.constants.HCIConstants;
import main.constants.TextConstants;
import mlooks.MScrollPane;

/**
 * @author  Matheus
 */
public class FighterManagement extends UmbraPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 13031986L;

    Fighter who = null;

    private JPanel inside;

    private JPanel attributes;

    private JPanel details;

    private JPanel thename;

    private JPanel playeritens;

    private JPanel belt;

    private JPanel equiped;

    private JLabel[] item;

    private JButton changeclass;

    private JPanel mini;

    public FighterManagement() {
        super(800, 600);
        super.setTitle(TextConstants.fightermanagement);
        who = new Fighter();
        super.getContentsPane().add(getInside(), BorderLayout.CENTER);
        this.setVisible(true);
    }

    /**
	 * @return  Returns the inside.
	 */
    private JPanel getInside() {
        if (inside == null) {
            inside = new JPanel();
            inside.setLayout(null);
            inside.setBackground(super.getBackground());
            inside.add(getAttributes(), null);
            inside.add(getThename(), null);
            inside.add(getDetails(), null);
            inside.add(getPlayerItens(), null);
            inside.add(getBelt(), null);
            inside.add(getEquipped(), null);
            inside.add(getChangeClass(), null);
            inside.add(getMini(), null);
        }
        return inside;
    }

    private JPanel getMini() {
        if (mini == null) {
            mini = new JPanel();
            mini.setLayout(new BorderLayout());
            mini.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(HCIConstants.borderyellow, 1), TextConstants.miniature, javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            ((TitledBorder) mini.getBorder()).setTitleFont(HCIConstants.normalfont);
            ((TitledBorder) mini.getBorder()).setTitleColor(HCIConstants.borderyellow);
            mini.setBounds(640, 60, 130, 190);
            mini.setBackground(super.getBackground());
            JLabel inner = new JLabel();
            inner.setIcon(new ImageIcon(who.getDefaultFace().getImage().getScaledInstance(who.getDefaultFace().getIconWidth() / 2, who.getDefaultFace().getIconHeight() / 2, Image.SCALE_SMOOTH)));
            inner.setHorizontalAlignment(JLabel.CENTER);
            inner.setVerticalAlignment(JLabel.BOTTOM);
            mini.add(inner, BorderLayout.CENTER);
        }
        return mini;
    }

    private JButton getChangeClass() {
        if (changeclass == null) {
            changeclass = new JButton();
            changeclass.setText("Change Class");
            changeclass.setBackground(super.getBackground());
            changeclass.setToolTipText("Changes the class based on the choice made in the right bottom corner");
            changeclass.setFont(HCIConstants.normalfont);
            changeclass.setForeground(Color.white);
            changeclass.setBounds(640, 8, 130, 50);
        }
        return changeclass;
    }

    private JPanel getEquipped() {
        if (equiped == null) {
            equiped = new JPanel();
            equiped.setLayout(new BorderLayout());
            equiped.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(HCIConstants.borderyellow, 1), TextConstants.equipped, javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            ((TitledBorder) equiped.getBorder()).setTitleFont(HCIConstants.normalfont);
            ((TitledBorder) equiped.getBorder()).setTitleColor(HCIConstants.borderyellow);
            equiped.setBounds(290, 250, 480, 210);
            equiped.setBackground(super.getBackground());
            JPanel inner = new JPanel();
            inner.setLayout(new FlowLayout());
            ((FlowLayout) inner.getLayout()).setVgap(4);
            ((FlowLayout) inner.getLayout()).setHgap(0);
            inner.setBackground(super.getBackground());
            inner.setPreferredSize(new Dimension(300, 5000));
            item = new JLabel[TextConstants.slots.length];
            for (int i = 0; i < TextConstants.slots.length; i++) {
                item[i] = new JLabel(TextConstants.slots[i]);
                item[i].setPreferredSize(new Dimension(235, 40));
                item[i].setFont(HCIConstants.normalfont);
                item[i].setForeground(Color.white);
                item[i].setIcon(HCIConstants.armor);
                inner.add(item[i]);
            }
            equiped.add(inner, BorderLayout.CENTER);
        }
        Item[] equips = who.getItens();
        for (int i = 0; i < equips.length; i++) {
            if (equips[i] != null) {
                item[i].setText(equips[i].getName());
                item[i].setIcon(equips[i].getMiniature());
            }
        }
        return equiped;
    }

    private Component getBelt() {
        if (belt == null) {
            belt = new JPanel();
            belt.setLayout(new BorderLayout());
            belt.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(HCIConstants.borderyellow, 1), TextConstants.belt, javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            ((TitledBorder) belt.getBorder()).setTitleFont(HCIConstants.normalfont);
            ((TitledBorder) belt.getBorder()).setTitleColor(HCIConstants.borderyellow);
            belt.setBounds(290, 460, 350, 70);
            belt.setBackground(super.getBackground());
            JPanel inner = new JPanel();
            inner.setLayout(new FlowLayout());
            ((FlowLayout) inner.getLayout()).setVgap(0);
            ((FlowLayout) inner.getLayout()).setHgap(0);
            inner.setBackground(super.getBackground());
            Item[] beltitens = who.getBelt();
            for (int i = 0; i < 8; i++) {
                if (beltitens[i] != null) {
                    JLabel item = new JLabel();
                    item.setPreferredSize(new Dimension(40, 40));
                    item.setFont(HCIConstants.normalfont);
                    item.setForeground(Color.white);
                    item.setIcon(beltitens[i].getMiniature());
                    item.setToolTipText(beltitens[i].getName());
                    inner.add(item);
                } else {
                }
            }
            belt.add(inner, BorderLayout.CENTER);
        }
        return belt;
    }

    private JPanel getPlayerItens() {
        if (playeritens == null) {
            playeritens = new JPanel();
            playeritens.setLayout(new BorderLayout());
            playeritens.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(HCIConstants.borderyellow, 1), TextConstants.itens, javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            ((TitledBorder) playeritens.getBorder()).setTitleFont(HCIConstants.normalfont);
            ((TitledBorder) playeritens.getBorder()).setTitleColor(HCIConstants.borderyellow);
            playeritens.setBounds(0, 250, 290, 280);
            playeritens.setBackground(super.getBackground());
            MScrollPane scrolled = new MScrollPane();
            scrolled.setScrollPolicy(MScrollPane.NEEDEDVERTICAL);
            JPanel inner = new JPanel();
            inner.setLayout(new FlowLayout());
            ((FlowLayout) inner.getLayout()).setVgap(0);
            inner.setBackground(super.getBackground());
            scrolled.setContent(inner);
            inner.setPreferredSize(new Dimension(300, 5000));
            Item[] equips = SourceOfAllThings.getActualPlayer().getAvaiableItens();
            for (int i = 0; i < equips.length; i++) {
                JLabel item = new JLabel(equips[i].getName());
                item.setPreferredSize(new Dimension(280, 50));
                item.setFont(HCIConstants.normalfont);
                item.setForeground(Color.white);
                item.setIcon(equips[i].getMiniature());
                inner.add(item);
            }
            playeritens.add(scrolled, BorderLayout.CENTER);
        }
        return playeritens;
    }

    /**
	 * @return  Returns the thename.
	 */
    private JPanel getThename() {
        if (thename == null) {
            thename = new JPanel();
            thename.setLayout(new BorderLayout());
            thename.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(HCIConstants.borderyellow, 1), TextConstants.name, javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            ((TitledBorder) thename.getBorder()).setTitleFont(HCIConstants.normalfont);
            ((TitledBorder) thename.getBorder()).setTitleColor(HCIConstants.borderyellow);
            thename.setBounds(290, 0, 350, 60);
            thename.setBackground(super.getBackground());
            JLabel name = new JLabel(who.getName());
            name.setFont(HCIConstants.titlefont);
            name.setForeground(Color.white);
            name.setHorizontalAlignment(JLabel.CENTER);
            thename.add(name, BorderLayout.CENTER);
        }
        return thename;
    }

    /**
	 * @return  Returns the details.
	 */
    private JPanel getDetails() {
        if (details == null) {
            details = new JPanel();
            details.setLayout(null);
            details.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(HCIConstants.borderyellow, 1), TextConstants.details, javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            ((TitledBorder) details.getBorder()).setTitleFont(HCIConstants.normalfont);
            ((TitledBorder) details.getBorder()).setTitleColor(HCIConstants.borderyellow);
            details.setBackground(super.getBackground());
            details.setBounds(290, 60, 350, 190);
            JLabel icon = new JLabel("Life Points");
            icon.setFont(HCIConstants.normalfont);
            icon.setForeground(Color.white);
            icon.setIcon(HCIConstants.heart);
            icon.setBounds(10, 20, 150, 40);
            details.add(icon, null);
            icon = new JLabel("Skill Points");
            icon.setFont(HCIConstants.normalfont);
            icon.setForeground(Color.white);
            icon.setIcon(HCIConstants.skillpoints);
            icon.setBounds(180, 20, 150, 40);
            details.add(icon, null);
            icon = new JLabel(TextConstants.armor);
            icon.setFont(HCIConstants.normalfont);
            icon.setForeground(Color.white);
            icon.setIcon(HCIConstants.armor);
            icon.setBounds(10, 73, 150, 40);
            details.add(icon, null);
            icon = new JLabel(TextConstants.attack);
            icon.setFont(HCIConstants.normalfont);
            icon.setForeground(Color.white);
            icon.setIcon(HCIConstants.attack);
            icon.setBounds(10, 126, 150, 40);
            details.add(icon, null);
            icon = new JLabel(TextConstants.evasion);
            icon.setFont(HCIConstants.normalfont);
            icon.setForeground(Color.white);
            icon.setIcon(HCIConstants.armor);
            icon.setBounds(180, 73, 150, 40);
            details.add(icon, null);
            icon = new JLabel(TextConstants.movement);
            icon.setFont(HCIConstants.normalfont);
            icon.setForeground(Color.white);
            icon.setIcon(HCIConstants.movement);
            icon.setBounds(180, 126, 150, 40);
            details.add(icon, null);
        }
        return details;
    }

    /**
	 * @return  Returns the attributes.
	 */
    private JPanel getAttributes() {
        if (attributes == null) {
            attributes = new JPanel();
            attributes.setLayout(null);
            attributes.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(HCIConstants.borderyellow, 1), TextConstants.attributes, javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            ((TitledBorder) attributes.getBorder()).setTitleFont(HCIConstants.normalfont);
            ((TitledBorder) attributes.getBorder()).setTitleColor(HCIConstants.borderyellow);
            attributes.setBackground(super.getBackground());
            attributes.setBounds(0, 0, 290, 250);
            JLabel names = new JLabel(TextConstants.attributenames);
            names.setFont(HCIConstants.normalfont);
            names.setForeground(Color.white);
            names.setBounds(10, attributes.getHeight() - 40, attributes.getWidth(), 20);
            attributes.add(names, null);
            JPanel attribute;
            JPanel shadow;
            int block = 3;
            attribute = new JPanel();
            attribute.setBackground(new Color(0, 100, 0));
            attribute.setBounds(20, attributes.getHeight() - 40 - who.getSpeed() * block, 20, who.getSpeed() * block);
            attribute.setToolTipText("- " + who.getSpeed() + " -");
            attribute.setVisible(true);
            attributes.add(attribute, null);
            shadow = new JPanel();
            shadow.setBackground(new Color(40, 40, 40));
            shadow.setBounds(23, attributes.getHeight() - 40 - who.getSpeed() * block - 3, 20, who.getSpeed() * block);
            attributes.add(shadow, null);
            attribute = new JPanel();
            attribute.setBackground(new Color(150, 0, 0));
            attribute.setBounds(90, attributes.getHeight() - 40 - who.getVitality() * block, 20, who.getVitality() * block);
            attribute.setToolTipText("- " + who.getVitality() + " -");
            attribute.setVisible(true);
            attributes.add(attribute, null);
            shadow = new JPanel();
            shadow.setBackground(new Color(40, 40, 40));
            shadow.setBounds(93, attributes.getHeight() - 40 - who.getVitality() * block - 3, 20, who.getVitality() * block);
            attributes.add(shadow, null);
            attribute = new JPanel();
            attribute.setBackground(new Color(150, 150, 0));
            attribute.setBounds(175, attributes.getHeight() - 40 - who.getStrenght() * block, 20, who.getStrenght() * block);
            attribute.setToolTipText("- " + who.getStrenght() + " -");
            attribute.setVisible(true);
            attributes.add(attribute, null);
            shadow = new JPanel();
            shadow.setBackground(new Color(40, 40, 40));
            shadow.setBounds(178, attributes.getHeight() - 40 - who.getStrenght() * block - 3, 20, who.getStrenght() * block);
            attributes.add(shadow, null);
            attribute = new JPanel();
            attribute.setBackground(new Color(0, 0, 150));
            attribute.setBounds(245, attributes.getHeight() - 40 - who.getSkill() * block, 20, who.getSkill() * block);
            attribute.setToolTipText("- " + who.getSkill() + " -");
            attribute.setVisible(true);
            attributes.add(attribute, null);
            shadow = new JPanel();
            shadow.setBackground(new Color(40, 40, 40));
            shadow.setBounds(248, attributes.getHeight() - 40 - who.getSkill() * block - 3, 20, who.getSkill() * block);
            attributes.add(shadow, null);
        }
        return attributes;
    }
}
