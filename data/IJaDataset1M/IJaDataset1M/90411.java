package org.rakiura.evm.grid.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import org.rakiura.evm.grid.Parameters;

/**
 * A panel allowing to set the parameters.
 * Parameters of type String are not set via ParametersGUI. They are set directly
 * through file choosers in InitializationFrame.
 * 
 * TODO: implements comments
 * 
 * @author Lucien Epiney
 *
 */
public class ParametersGUI extends JPanel implements ActionListener, MouseListener {

    /**
	 * Comment for <code>serialVersionUID</code>
	 */
    private static final long serialVersionUID = 1L;

    public final Parameters parameters;

    private final JTextField[] intFields, doubleFields;

    private final JCheckBox[] checkBoxes;

    private final HashMap<String, String> comments;

    private final JTextArea commentsLabel;

    private JLabel l;

    public ParametersGUI(Parameters parameters) {
        super(new GridLayout(1, 3));
        this.parameters = parameters;
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 5, 0, 5);
        int m = 0;
        Map<String, Object> intParameters = parameters.getParameters(int.class);
        int n = intParameters.size();
        int i = 0;
        this.intFields = new JTextField[n];
        JLabel l;
        for (String name : intParameters.keySet()) {
            this.intFields[i] = new JTextField(6);
            this.intFields[i].addMouseListener(this);
            this.intFields[i].setName(name);
            this.intFields[i].setHorizontalAlignment(SwingConstants.RIGHT);
            this.intFields[i].setText(((Integer) intParameters.get(name)).toString());
            c.gridy = m++;
            c.gridx = 0;
            c.gridwidth = 1;
            panel.add(this.intFields[i], c);
            l = new JLabel(makeReadable(name), SwingConstants.LEFT);
            c.gridx = 1;
            c.gridwidth = 3;
            panel.add(l, c);
            i++;
        }
        Map<String, Object> doubleParameters = parameters.getParameters(double.class);
        n = doubleParameters.size();
        i = 0;
        this.doubleFields = new JTextField[n];
        for (String name : doubleParameters.keySet()) {
            this.doubleFields[i] = new JTextField(6);
            this.doubleFields[i].addMouseListener(this);
            this.doubleFields[i].setName(name);
            this.doubleFields[i].setHorizontalAlignment(SwingConstants.RIGHT);
            this.doubleFields[i].setText(((Double) doubleParameters.get(name)).toString());
            c.gridy = m++;
            c.gridx = 0;
            c.gridwidth = 1;
            panel.add(this.doubleFields[i], c);
            l = new JLabel(makeReadable(name), SwingConstants.LEFT);
            c.gridx = 1;
            c.gridwidth = 3;
            panel.add(l, c);
            i++;
        }
        Map<String, Object> boolParameters = parameters.getParameters(boolean.class);
        n = boolParameters.size();
        i = 0;
        this.checkBoxes = new JCheckBox[n];
        c.gridx = 0;
        c.gridwidth = 4;
        for (String name : boolParameters.keySet()) {
            this.checkBoxes[i] = new JCheckBox(makeReadable(name));
            this.checkBoxes[i].addMouseListener(this);
            this.checkBoxes[i].setName(name);
            this.checkBoxes[i].setSelected(((Boolean) boolParameters.get(name)).booleanValue());
            c.gridy = m++;
            panel.add(this.checkBoxes[i], c);
            i++;
        }
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setBorder(new EmptyBorder(20, 10, 20, 10));
        Color inside1 = new Color(Color.HSBtoRGB(0.05f, 0.6f, 1f));
        Color inside2 = new Color(Color.HSBtoRGB(0.15f, 0.6f, 1f));
        Color outside1 = Color.GRAY;
        Color outside2 = Color.GRAY;
        Font textFont = new Font("SansSherif", Font.BOLD, 12);
        Font titleFont = new Font("SansSherif", Font.BOLD, 16);
        String classname = this.parameters.getClass().getName();
        classname = classname.substring(classname.lastIndexOf("Parameters"));
        classname = classname.replaceAll("Parameters", "");
        JLabel l1 = new JLabel(classname + " parameters", SwingConstants.RIGHT);
        l1.setFont(titleFont);
        l1.setForeground(outside1);
        l1.setOpaque(true);
        l1.setVerticalAlignment(SwingConstants.BOTTOM);
        p.add(l1);
        JTextArea gcl = new JTextArea();
        gcl.setLineWrap(true);
        gcl.setWrapStyleWord(true);
        gcl.setBackground(inside1);
        gcl.setForeground(outside1);
        gcl.setFont(textFont);
        gcl.setPreferredSize(new Dimension(200, 100));
        gcl.setBorder(new CompoundBorder(new LineBorder(outside1, 2), new EmptyBorder(10, 10, 0, 10)));
        JPanel p2 = new JPanel(new BorderLayout());
        p2.add(l1, BorderLayout.NORTH);
        p2.add(gcl, BorderLayout.CENTER);
        p2.add(new JLabel(" "), BorderLayout.SOUTH);
        p.add(p2);
        try {
            Field f = this.parameters.getClass().getDeclaredField("comments");
            gcl.setText(f.get(this.parameters).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.l = new JLabel(" ", SwingConstants.CENTER);
        this.l.setFont(titleFont);
        this.l.setForeground(outside2);
        this.l.setOpaque(true);
        this.l.setHorizontalAlignment(SwingConstants.RIGHT);
        this.l.setVerticalAlignment(SwingConstants.BOTTOM);
        this.comments = this.parameters.createComments();
        this.commentsLabel = new JTextArea();
        this.commentsLabel.setLineWrap(true);
        this.commentsLabel.setWrapStyleWord(true);
        this.commentsLabel.setBackground(inside2);
        this.commentsLabel.setForeground(outside2);
        this.commentsLabel.setFont(textFont);
        this.commentsLabel.setBorder(new CompoundBorder(new LineBorder(outside2, 2), new EmptyBorder(10, 10, 0, 10)));
        JPanel p1 = new JPanel(new BorderLayout());
        p1.add(this.l, BorderLayout.NORTH);
        p1.add(this.commentsLabel, BorderLayout.CENTER);
        p.add(p1);
        add(new JLabel(parameters.getBigIcon()));
        add(panel);
        add(p);
    }

    public void update() {
        if (this.parameters == null) return;
        final Map<String, Object> intParameters = this.parameters.getParameters(int.class);
        for (int i = 0; i < this.intFields.length; i++) this.intFields[i].setText(((Integer) intParameters.get(this.intFields[i].getName())).toString());
        final Map<String, Object> doubleParameters = this.parameters.getParameters(double.class);
        for (int i = 0; i < this.doubleFields.length; i++) this.doubleFields[i].setText(((Double) doubleParameters.get(this.doubleFields[i].getName())).toString());
        final Map<String, Object> boolParameters = this.parameters.getParameters(boolean.class);
        for (int i = 0; i < this.checkBoxes.length; i++) this.checkBoxes[i].setSelected(((Boolean) boolParameters.get(this.checkBoxes[i].getName())).booleanValue());
    }

    private static String makeReadable(String ugly) {
        StringBuffer nice = new StringBuffer();
        nice.append(Character.toUpperCase(ugly.charAt(0)));
        for (int i = 1; i < ugly.length(); i++) {
            char c = ugly.charAt(i);
            if (Character.isUpperCase(c)) {
                nice.append(" " + Character.toLowerCase(c));
            } else nice.append(c);
        }
        return nice.toString();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton source = (JButton) (e.getSource());
            if (source.getText().equalsIgnoreCase("Apply")) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 0; i < this.intFields.length; i++) {
                    try {
                        map.put(this.intFields[i].getName(), new Integer(this.intFields[i].getText()));
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(this, "Non valid value for " + this.parameters.getClass().getFields()[i].getName());
                        return;
                    }
                }
                for (int i = 0; i < this.doubleFields.length; i++) {
                    try {
                        map.put(this.doubleFields[i].getName(), new Double(this.doubleFields[i].getText()));
                    } catch (Exception exception) {
                        JOptionPane.showMessageDialog(this, "Non valid value for " + this.parameters.getClass().getFields()[i].getName());
                        return;
                    }
                }
                for (int i = 0; i < this.checkBoxes.length; i++) {
                    map.put(this.checkBoxes[i].getName(), new Boolean(this.checkBoxes[i].isSelected()));
                }
                this.parameters.set(map);
                this.getTopLevelAncestor().setVisible(false);
            }
        }
    }

    public static JFrame createParametersFrame(Parameters[] parameters) {
        ParametersGUI[] paramPanels = new ParametersGUI[parameters.length];
        JTabbedPane tabbedPane = new JTabbedPane();
        JButton b = new JButton("Apply");
        for (int i = 0; i < parameters.length; i++) {
            paramPanels[i] = new ParametersGUI(parameters[i]);
            String packageName = parameters[i].getClass().getPackage().getName();
            String name = parameters[i].getClass().getName().replaceFirst(packageName + ".Parameters", "");
            tabbedPane.addTab(name, paramPanels[i].parameters.getIcon(), paramPanels[i]);
            b.addActionListener(paramPanels[i]);
        }
        JFrame frame = new JFrame("Parameters");
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
        frame.getContentPane().add(b, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(false);
        return frame;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        String name = ((JComponent) e.getSource()).getName();
        this.l.setText(makeReadable(name));
        this.commentsLabel.setText(this.comments.get(name));
    }

    public void mouseExited(MouseEvent e) {
    }
}
