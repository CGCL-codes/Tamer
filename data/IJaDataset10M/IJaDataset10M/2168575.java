package neon.tools.editors;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.util.*;
import javax.swing.border.*;
import javax.swing.text.MaskFormatter;
import neon.objects.ResourceManager;
import neon.objects.resources.RSpell.SpellType;
import neon.tools.*;
import neon.tools.help.HelpLabels;
import neon.objects.resources.RItem;
import neon.objects.resources.RSpell;
import neon.util.ColorFactory;

public class ScrollEditor implements ObjectEditor {

    private JDialog frame;

    private JPanel itemProps;

    private JTextField nameField;

    private JFormattedTextField costField, weightField, charField;

    private JComboBox<String> colorBox;

    private JComboBox<String> spellBox;

    private JTextArea textArea;

    private RItem.Book data;

    private Vector<String> spells;

    public ScrollEditor(JFrame parent, RItem.Book data) {
        frame = new JDialog(parent, "Scroll Editor: " + data.id);
        JPanel content = new JPanel(new BorderLayout());
        frame.setContentPane(content);
        this.data = data;
        JPanel buttons = new JPanel();
        content.add(buttons, BorderLayout.PAGE_END);
        JButton ok = new JButton("Ok");
        ok.addActionListener(this);
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        JButton apply = new JButton("Apply");
        apply.addActionListener(this);
        buttons.add(ok);
        buttons.add(cancel);
        buttons.add(apply);
        itemProps = new JPanel();
        GroupLayout layout = new GroupLayout(itemProps);
        itemProps.setLayout(layout);
        layout.setAutoCreateGaps(true);
        JLabel nameLabel = new JLabel("Name: ");
        JLabel costLabel = new JLabel("Cost: ");
        JLabel colorLabel = new JLabel("Color: ");
        JLabel charLabel = new JLabel("Character: ");
        JLabel weightLabel = new JLabel("Weight: ");
        JLabel spellLabel = new JLabel("Spell: ");
        JLabel textLabel = new JLabel("Text: ");
        nameField = new JTextField(15);
        costField = new JFormattedTextField(NeonFormat.getIntegerInstance());
        colorBox = new JComboBox<String>(ColorFactory.getColorNames());
        colorBox.setBackground(Color.black);
        colorBox.setRenderer(new ColorCellRenderer());
        colorBox.addActionListener(this);
        charField = new JFormattedTextField(getMaskFormatter("*"));
        weightField = new JFormattedTextField(NeonFormat.getFloatInstance());
        loadSpells();
        spellBox = new JComboBox<String>(spells);
        textArea = new JTextArea();
        JLabel nameHelpLabel = HelpLabels.getNameHelpLabel();
        JLabel costHelpLabel = HelpLabels.getCostHelpLabel();
        JLabel colorHelpLabel = HelpLabels.getColorHelpLabel();
        JLabel charHelpLabel = HelpLabels.getCharHelpLabel();
        JLabel weightHelpLabel = HelpLabels.getWeightHelpLabel();
        JLabel spellHelpLabel = HelpLabels.getSpellHelpLabel();
        JLabel textHelpLabel = HelpLabels.getScrollTextHelpLabel();
        layout.setVerticalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(nameLabel).addComponent(nameField).addComponent(nameHelpLabel)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(costLabel).addComponent(costField).addComponent(costHelpLabel)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(colorLabel).addComponent(colorBox).addComponent(colorHelpLabel)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(charLabel).addComponent(charField).addComponent(charHelpLabel)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(weightLabel).addComponent(weightField).addComponent(weightHelpLabel)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(spellLabel).addComponent(spellBox).addComponent(spellHelpLabel)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(textLabel).addComponent(textArea).addComponent(textHelpLabel)));
        layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(nameLabel).addComponent(costLabel).addComponent(colorLabel).addComponent(charLabel).addComponent(weightLabel).addComponent(spellLabel).addComponent(textLabel)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(nameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(costField).addComponent(colorBox).addComponent(charField).addComponent(weightField).addComponent(spellBox).addComponent(textArea)).addGap(10).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false).addComponent(nameHelpLabel).addComponent(costHelpLabel).addComponent(colorHelpLabel).addComponent(charHelpLabel).addComponent(weightHelpLabel).addComponent(spellHelpLabel).addComponent(textHelpLabel)));
        JScrollPane propScroller = new JScrollPane(itemProps);
        propScroller.setBorder(new TitledBorder("Properties"));
        content.add(propScroller, BorderLayout.CENTER);
        initProps();
    }

    public void show() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void save() {
        data.name = nameField.getText();
        try {
            costField.commitEdit();
            data.cost = ((Long) costField.getValue()).intValue();
        } catch (ParseException e) {
            data.cost = 0;
        }
        data.color = colorBox.getSelectedItem().toString();
        data.text = charField.getText();
        data.weight = Float.parseFloat(weightField.getText());
        data.content = textArea.getText();
        if (spellBox.getSelectedItem() != null) {
            data.spell = spellBox.getSelectedItem().toString();
        } else {
            data.spell = null;
        }
        data.setPath(Editor.getStore().getActive().get("id"));
    }

    private void initProps() {
        nameField.setText(data.name);
        costField.setValue(data.cost);
        colorBox.setSelectedItem(data.color);
        charField.setValue(data.text);
        weightField.setValue(data.weight);
        textArea.setText(data.content);
        spellBox.setSelectedItem(data.spell);
    }

    private void loadSpells() {
        spells = new Vector<String>();
        for (RSpell spell : ResourceManager.getResources(RSpell.class)) {
            if (spell.type.equals(SpellType.SPELL)) {
                spells.add(spell.id);
            }
        }
    }

    private MaskFormatter getMaskFormatter(String s) {
        MaskFormatter formatter = null;
        try {
            formatter = new MaskFormatter(s);
            formatter.setPlaceholderCharacter('X');
        } catch (java.text.ParseException e) {
        }
        return formatter;
    }

    public void actionPerformed(ActionEvent e) {
        if ("Ok".equals(e.getActionCommand())) {
            save();
            frame.dispose();
        } else if ("Cancel".equals(e.getActionCommand())) {
            frame.dispose();
        } else if ("Apply".equals(e.getActionCommand())) {
            save();
        } else {
            colorBox.setForeground(neon.util.ColorFactory.getColor((String) colorBox.getSelectedItem()));
        }
    }
}
