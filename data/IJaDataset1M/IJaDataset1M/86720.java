package view;

import model.AcolyteCard;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.util.StringTokenizer;
import java.util.List;
import java.util.LinkedList;

public class CardTemplate {

    private int windowHeight, windowWidth;

    private List<TemplateElement> elements;

    public CardTemplate(String templateFile) {
        try {
            setup(templateFile);
        } catch (XMLStreamException e) {
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public CardTemplate() {
    }

    public void save() {
    }

    private void setup(String filename) throws XMLStreamException, FileNotFoundException {
        FileInputStream in = new FileInputStream("templates/" + filename);
        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(in);
        while ("window" != reader.getLocalName()) reader.nextTag();
        windowHeight = Integer.parseInt(reader.getAttributeValue(0));
        windowWidth = Integer.parseInt(reader.getAttributeValue(1));
        elements = new LinkedList<TemplateElement>();
        while (reader.hasNext()) {
            reader.nextTag();
            if (reader.isStartElement()) {
                if (0 < reader.getAttributeCount()) {
                    String name = reader.getAttributeValue(0);
                    int x = Integer.parseInt(reader.getAttributeValue(1));
                    int y = Integer.parseInt(reader.getAttributeValue(2));
                    int height = Integer.parseInt(reader.getAttributeValue(3));
                    int width = Integer.parseInt(reader.getAttributeValue(4));
                    elements.add(new TemplateElement(name, x, y, height, width));
                }
            }
        }
    }

    public void templateShow(AcolyteCard toShow) {
    }

    public void show(AcolyteCard toShow) {
        JFrame f = new JFrame(toShow.get("name"));
        Dimension dim = new Dimension(windowWidth, windowHeight);
        System.out.println(dim);
        JPanel content = new JPanel(new GridLayout(0, 1));
        content.setBackground(java.awt.Color.BLACK);
        JLabel cost = new JLabel(toShow.get("cost"), JLabel.RIGHT);
        cost.setForeground(java.awt.Color.WHITE);
        content.add(cost);
        JPanel rulebox = new JPanel(new GridLayout(0, 1));
        String rulesText = toShow.get("ability");
        StringTokenizer abilities = new StringTokenizer(rulesText, "|");
        while (abilities.hasMoreTokens()) {
            JLabel rules = new JLabel(abilities.nextToken());
            rules.setForeground(java.awt.Color.BLACK);
            rulebox.add(rules);
        }
        rulebox.setBackground(java.awt.Color.GRAY);
        content.add(rulebox);
        String typeline = toShow.get("type");
        String subtype = toShow.get("subtype");
        if ("" != subtype) {
            typeline += "-" + subtype;
        }
        typeline = typeline.replace("|", " ");
        JLabel type = new JLabel(typeline);
        type.setForeground(java.awt.Color.WHITE);
        content.add(type);
        JLabel flavor = new JLabel(toShow.get("flavortext"));
        flavor.setForeground(java.awt.Color.WHITE);
        content.add(flavor);
        JPanel footer = new JPanel(new GridLayout(0, 2));
        JLabel set = new JLabel(toShow.get("set") + "-" + toShow.get("rarity"), JLabel.LEFT);
        set.setForeground(java.awt.Color.WHITE);
        footer.add(set);
        footer.setBackground(Color.BLACK);
        String power = toShow.get("power");
        if ("" != power) {
            String toughness = toShow.get("toughness");
            String print = power + "/" + toughness;
            JLabel powerLabel = new JLabel(print, JLabel.RIGHT);
            powerLabel.setForeground(java.awt.Color.WHITE);
            footer.add(powerLabel);
        }
        content.add(footer);
        f.setContentPane(content);
        f.pack();
        f.setSize(dim);
        f.setVisible(true);
    }

    public static void main(String[] args) {
        AcolyteCard card = new AcolyteCard("cardTest.xml");
        new CardTemplate("templateExample.xml").show(card);
        return;
    }
}
