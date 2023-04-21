package net.sourceforge.picdev.components;

import net.sourceforge.picdev.annotations.EComponent;
import net.sourceforge.picdev.annotations.EComponentProperty;
import net.sourceforge.picdev.core.Project;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@EComponent(name = "Togglebuttons", description = "Toggle buttons")
public class Togglebuttons extends SimpleComponent implements ActionListener {

    Project parent;

    private static final int PADDING_LEFT = 5;

    private static final int PADDING_TOP = 14;

    private static final int SPACING = 10;

    private static final int BUTTON_WIDTH = 30;

    private static final int BUTTON_HEIGHT = 30;

    private static final int START_COUNT = 8;

    private IO input;

    JButton buttons[] = new JButton[START_COUNT];

    public Togglebuttons() {
        createIOs(START_COUNT);
        setLocation(280, 400);
        addButtons(START_COUNT);
    }

    private void addButtons(int count) {
        this.getContentPane().removeAll();
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
        setSize(2 * PADDING_LEFT + BUTTON_WIDTH * count + SPACING * (count + 1), BUTTON_HEIGHT + 4 * PADDING_TOP);
        buttons = new JButton[count];
        for (int i = 0; i < count; i++) {
            buttons[i] = new JButton(String.valueOf(i), new ImageIcon("images/switchoff.gif"));
            buttons[i].setBorder(BorderFactory.createEmptyBorder());
            buttons[i].setBackground(Color.white);
            buttons[i].setContentAreaFilled(false);
            buttons[i].setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
            buttons[i].setMaximumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
            buttons[i].setLocation(PADDING_LEFT + i * BUTTON_WIDTH + (i - 1) * SPACING, PADDING_TOP);
            buttons[i].addActionListener(this);
            this.getContentPane().add(Box.createHorizontalStrut(SPACING));
            this.getContentPane().add(buttons[i]);
        }
        revalidate();
        setSize(2 * PADDING_LEFT + BUTTON_WIDTH * count + SPACING * (count + 1), BUTTON_HEIGHT + 4 * PADDING_TOP);
    }

    private void createIOs(int count) {
        input = new IO();
        input.setWidth(count);
        input.setDirection(0x00000000);
    }

    public void init(Project parent) {
        this.parent = parent;
        this.setVisible(true);
        this.getContentPane().setBackground(Color.WHITE);
    }

    @EComponentProperty(description = "Number of Buttons")
    public void setCount(int count) {
        createIOs(count);
        addButtons(count);
    }

    ;

    public void actionPerformed(ActionEvent evt) {
        for (int i = 0; i < getCount(); i++) if (evt.getSource() == buttons[i]) {
            input.state[i] = !input.state[i];
            if (input.state[i]) buttons[i].setIcon(new ImageIcon("images/switchon.gif")); else buttons[i].setIcon(new ImageIcon("images/switchoff.gif"));
            parent.postPinUpdate(this);
        }
    }

    public int getCount() {
        return input.getWidth();
    }

    public String[] getPinNames() {
        String[] names = new String[getCount()];
        for (int i = 0; i < names.length; i++) {
            names[i] = "Button " + i;
        }
        return names;
    }

    public boolean getOutput(int index) {
        return input.state[index];
    }

    public void setInput(int index, boolean state) {
        return;
    }
}
