package com.testonica.kickelhahn.core.elements.simple;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Resistor extends DualPinComponent {

    public Resistor(String name) {
        super(name);
        setSize(40, 40);
    }

    public void paint(Graphics g) {
        if (!getBoard().isVisible()) return;
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(getLocation().x + 10, getLocation().y, getSize().width - 20, getSize().height - 20);
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, g.getFont().getSize()));
        g.drawRect(getLocation().x + 10, getLocation().y, getSize().width - 20, getSize().height - 20);
        g.drawString("Tr", getLocation().x, getLocation().y);
    }
}
