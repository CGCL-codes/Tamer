package net.sourceforge.ondex.util.metadata;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.text.JTextComponent;
import net.sourceforge.ondex.core.AttributeName;
import net.sourceforge.ondex.core.MetaData;
import net.sourceforge.ondex.core.ONDEXGraphMetaData;
import net.sourceforge.ondex.core.RelationType;
import net.sourceforge.ondex.core.Unit;
import net.sourceforge.ondex.util.metadata.model.MetaDataType;
import net.sourceforge.ondex.util.metadata.ops.UpdateOperation;

public class EditorPanel<M extends MetaData> extends JPanel {

    private static final long serialVersionUID = -8233172191199019096L;

    private HashMap<Field, Component> components = new HashMap<Field, Component>();

    private M md;

    private MetaDataType mdt;

    private DelayTrigger delaytrigger;

    private JComboBox unitBox;

    private JPanel unitBoxPanel;

    private ONDEXGraphMetaData omd;

    public EditorPanel(M md, ONDEXGraphMetaData omd) {
        this.md = md;
        this.omd = omd;
        delaytrigger = new DelayTrigger();
        delaytrigger.start();
        setLayout(new BorderLayout());
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.PAGE_AXIS));
        p.add(makeComponent(Field.ID, new JTextField(), md.getId()));
        ((JTextField) components.get(Field.ID)).setEditable(false);
        p.add(makeComponent(Field.FULLNAME, new JTextField(), md.getFullname()));
        p.add(makeComponent(Field.DESCRIPTION, new JTextArea(), md.getDescription()));
        mdt = MetaDataType.fromClass(md);
        switch(mdt) {
            case RELATION_TYPE:
                RelationType rt = (RelationType) md;
                p.add(makeComponent(Field.INVERSE, new JTextField(), rt.getInverseName()));
                p.add(configureButtonPanel(Field.SYMMETRY, new ButtonPanel(), rt.isSymmetric(), rt.isAntisymmetric()));
                p.add(makeComponent(Field.REFLEXIVE, new JCheckBox(), rt.isReflexive()));
                p.add(makeComponent(Field.TRANSITIVE, new JCheckBox(), rt.isTransitiv()));
                break;
            case ATTRIBUTE_NAME:
                AttributeName an = (AttributeName) md;
                p.add(makeComponent(Field.DATATYPE, new JTextField(), an.getDataTypeAsString()));
                ((JTextField) components.get(Field.DATATYPE)).setEditable(false);
                p.add(makeUnitComboBox(an.getUnit()));
                break;
        }
        add(p, BorderLayout.NORTH);
        add(new JPanel(), BorderLayout.CENTER);
    }

    public void addActionListener(ActionListener l) {
        delaytrigger.addActionListener(l);
    }

    public void close() {
        delaytrigger.terminate();
    }

    private JPanel makeComponent(Field f, JCheckBox tc, boolean set) {
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(f.pretty()));
        tc.setSelected(set);
        tc.setName(f.name());
        tc.setText(f.pretty());
        tc.addActionListener(delaytrigger);
        components.put(f, tc);
        p.add(tc, BorderLayout.WEST);
        p.add(new JPanel(), BorderLayout.CENTER);
        return p;
    }

    private JPanel makeUnitComboBox(Unit selection) {
        unitBoxPanel = new JPanel();
        unitBoxPanel.setLayout(new BorderLayout());
        unitBoxPanel.setBorder(BorderFactory.createTitledBorder(Field.UNIT.pretty()));
        updateUnitBox();
        if (selection != null) {
            unitBox.setSelectedItem(selection);
        } else {
            unitBox.setSelectedIndex(0);
        }
        components.put(Field.UNIT, unitBox);
        return unitBoxPanel;
    }

    private void updateUnitBox() {
        Vector<Object> unitV = new Vector<Object>();
        unitV.add("");
        Iterator<Unit> it = omd.getUnits().iterator();
        while (it.hasNext()) {
            unitV.add(it.next());
        }
        Object selection = unitBox != null ? unitBox.getSelectedItem() : null;
        unitBox = new JComboBox(unitV);
        unitBox.setName(Field.UNIT.name());
        unitBox.addActionListener(delaytrigger);
        if (selection != null && unitV.contains(selection)) {
            unitBox.setSelectedItem(selection);
        }
        unitBoxPanel.removeAll();
        unitBoxPanel.add(unitBox, BorderLayout.WEST);
        unitBoxPanel.add(new JPanel(), BorderLayout.CENTER);
        unitBoxPanel.revalidate();
    }

    private JPanel configureButtonPanel(Field f, ButtonPanel tc, boolean symmetric, boolean antisymmetric) {
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder(f.pretty()));
        if (symmetric) {
            tc.setSymmetric();
        } else if (antisymmetric) {
            tc.setAntisymmetric();
        } else {
            tc.setAssymmetric();
        }
        tc.setName(f.name());
        tc.addActionListener(delaytrigger);
        components.put(f, tc);
        p.add(tc, BorderLayout.WEST);
        p.add(new JPanel(), BorderLayout.CENTER);
        return p;
    }

    private JPanel makeComponent(Field f, JTextComponent tc, String content) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.LINE_AXIS));
        p.setBorder(BorderFactory.createTitledBorder(f.pretty()));
        tc.setText(content);
        tc.setName(f.name());
        tc.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                delaytrigger.hitSensor();
            }
        });
        components.put(f, tc);
        if (tc instanceof JTextArea) {
            p.setPreferredSize(new Dimension(50, 100));
            p.add(new JScrollPane(tc));
        } else {
            p.add(tc);
        }
        return p;
    }

    public void setText(Field f, String text) {
        Component co = components.get(f);
        if (co != null && co instanceof JTextComponent) {
            JTextComponent c = (JTextComponent) co;
            c.setText(text);
        }
    }

    public void setIs(Field f, boolean is) {
        Component co = components.get(f);
        if (co != null && co instanceof JCheckBox) {
            JCheckBox c = (JCheckBox) co;
            c.setSelected(is);
        }
    }

    public void setChoice(Field f, Field val) {
        Component co = components.get(f);
        if (co != null && co instanceof EditorPanel<?>.ButtonPanel<?>) {
            ButtonPanel c = (ButtonPanel) co;
            switch(val) {
                case SYMMETRIC:
                    c.setSymmetric();
                    break;
                case ASSYMMETRIC:
                    c.setAssymmetric();
                    break;
                case ANTISYMMETRIC:
                    c.setAntisymmetric();
                    break;
            }
        }
    }

    public void setContents(M contents) {
        md = contents;
        setText(Field.ID, contents.getId());
        setText(Field.FULLNAME, contents.getFullname());
        setText(Field.DESCRIPTION, contents.getDescription());
        switch(mdt) {
            case RELATION_TYPE:
                RelationType rt = (RelationType) contents;
                setText(Field.INVERSE, rt.getInverseName());
                setIs(Field.REFLEXIVE, rt.isReflexive());
                setIs(Field.TRANSITIVE, rt.isTransitiv());
                if (rt.isSymmetric()) {
                    setChoice(Field.SYMMETRY, Field.SYMMETRIC);
                } else if (rt.isAntisymmetric()) {
                    setChoice(Field.SYMMETRY, Field.ANTISYMMETRIC);
                } else {
                    setChoice(Field.SYMMETRY, Field.ASSYMMETRIC);
                }
                break;
            case ATTRIBUTE_NAME:
                AttributeName an = (AttributeName) contents;
                setText(Field.DATATYPE, an.getDataTypeAsString());
                unitBox.setSelectedItem(an.getUnit() != null ? an.getUnit() : "");
                updateUnitBox();
                break;
        }
    }

    public M getContents() {
        return md;
    }

    public UpdateOperation<M> extractChangeAction() {
        String fullName = ((JTextComponent) components.get(Field.FULLNAME)).getText();
        if (!fullName.equals(md.getFullname())) {
            return new UpdateOperation<M>(md, Field.FULLNAME, md.getFullname(), fullName);
        }
        String description = ((JTextComponent) components.get(Field.DESCRIPTION)).getText();
        if (!description.equals(md.getDescription())) {
            return new UpdateOperation<M>(md, Field.DESCRIPTION, md.getDescription(), description);
        }
        switch(mdt) {
            case RELATION_TYPE:
                RelationType rt = (RelationType) md;
                String inverseName = ((JTextComponent) components.get(Field.INVERSE)).getText();
                if (!inverseName.equals(rt.getInverseName())) {
                    return new UpdateOperation<M>(md, Field.INVERSE, rt.getInverseName(), inverseName);
                }
                boolean isSymmetric = ((ButtonPanel) components.get(Field.SYMMETRY)).isSymmetric();
                if (isSymmetric != rt.isSymmetric()) {
                    return new UpdateOperation<M>(md, Field.SYMMETRIC, rt.isSymmetric(), isSymmetric);
                }
                boolean isAntisymmetric = ((ButtonPanel) components.get(Field.SYMMETRY)).isAntiSymmetric();
                if (isAntisymmetric != rt.isAntisymmetric()) {
                    return new UpdateOperation<M>(md, Field.ANTISYMMETRIC, rt.isAntisymmetric(), isAntisymmetric);
                }
                boolean isTransitive = ((JCheckBox) components.get(Field.TRANSITIVE)).isSelected();
                if (isTransitive != rt.isTransitiv()) {
                    return new UpdateOperation<M>(md, Field.TRANSITIVE, rt.isTransitiv(), isTransitive);
                }
                boolean isReflexive = ((JCheckBox) components.get(Field.REFLEXIVE)).isSelected();
                if (isReflexive != rt.isReflexive()) {
                    return new UpdateOperation<M>(md, Field.REFLEXIVE, rt.isReflexive(), isReflexive);
                }
                break;
            case ATTRIBUTE_NAME:
                AttributeName an = (AttributeName) md;
                Object o = ((JComboBox) components.get(Field.UNIT)).getSelectedItem();
                Unit unit = o instanceof Unit ? (Unit) o : null;
                if ((unit == null && an.getUnit() != null) || (unit != null && !unit.equals(an.getUnit()))) {
                    return new UpdateOperation<M>(md, Field.UNIT, an.getUnit(), unit);
                }
        }
        return null;
    }

    public void update() {
        setContents(md);
    }

    public enum Field {

        ID("Id"), FULLNAME("Full name"), DESCRIPTION("Description"), INVERSE("Inverse name"), SYMMETRIC("Symmetric"), ASSYMMETRIC("Asymmetric"), ANTISYMMETRIC("Antisymmetric"), REFLEXIVE("Reflexive"), TRANSITIVE("Transitive"), SYMMETRY("Symmetry"), UNIT("Unit"), DATATYPE("Data type");

        String s;

        Field(String s) {
            this.s = s;
        }

        public String pretty() {
            return s;
        }
    }

    private class DelayTrigger extends Thread implements ActionListener {

        private long lastChange = 0L;

        private boolean excited = false;

        private boolean terminate = false;

        private Vector<ActionListener> listeners = new Vector<ActionListener>();

        public DelayTrigger() {
            setDaemon(true);
            setPriority(1);
        }

        public void run() {
            while (!terminate) {
                if (excited) {
                    if (System.currentTimeMillis() - lastChange > 700) {
                        fire();
                        excited = false;
                    }
                    Thread.yield();
                } else {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }

        public void terminate() {
            terminate = true;
        }

        public void addActionListener(ActionListener l) {
            listeners.add(l);
        }

        public void hitSensor() {
            lastChange = System.currentTimeMillis();
            excited = true;
        }

        private void fire() {
            for (ActionListener l : listeners) {
                l.actionPerformed(new ActionEvent(this, 0, "KeyWatcher"));
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            for (ActionListener l : listeners) {
                l.actionPerformed(new ActionEvent(e, 0, "MouseAction"));
            }
        }
    }

    private class ButtonPanel extends JPanel {

        private static final long serialVersionUID = -8049542057687885259L;

        private JRadioButton b1, b2, b3;

        private ButtonGroup group;

        public ButtonPanel() {
            setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            b1 = new JRadioButton(Field.SYMMETRIC.pretty());
            b2 = new JRadioButton(Field.ASSYMMETRIC.pretty());
            b3 = new JRadioButton(Field.ANTISYMMETRIC.pretty());
            group = new ButtonGroup();
            group.add(b1);
            group.add(b2);
            group.add(b3);
            add(b1);
            add(b2);
            add(b3);
        }

        public boolean isSymmetric() {
            return b1.isSelected();
        }

        public boolean isAntiSymmetric() {
            return b3.isSelected();
        }

        public void setSymmetric() {
            b1.setSelected(true);
        }

        public void setAssymmetric() {
            b2.setSelected(true);
        }

        public void setAntisymmetric() {
            b3.setSelected(true);
        }

        public void addActionListener(ActionListener l) {
            b1.addActionListener(l);
            b2.addActionListener(l);
            b3.addActionListener(l);
        }
    }
}
