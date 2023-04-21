package checkersGUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListCellRenderer;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import checkersMain.CheckersPlayerInterface;
import checkersMain.CheckersPlayerLoader;

/**
 * This class is a GUI dialog that allows the user to change different settings
 * in the GameCheckerager and CheckersGUI.
 * 
 * @author Amos Yuen
 * @version 1.20 - 30 July 2008
 */
@SuppressWarnings("serial")
public class PlayerSetupDialog extends JDialog implements ActionListener, KeyListener, ItemListener {

    public static final Font FONT = new Font("Arial", Font.BOLD, 20);

    private boolean accepted;

    private SimpleComboBox comboBox1, comboBox2;

    private JComponent component1, component2;

    private JTextArea description1, description2;

    private JButton ok, cancel;

    private JPanel panel;

    private CheckersPlayerInterface player1, player2;

    private JScrollPane scrollPane1, scrollPane2;

    public PlayerSetupDialog(JFrame parent, CheckersPlayerInterface player1, CheckersPlayerInterface player2, boolean useAutoSwitch) {
        super(parent, "Player Setup", true);
        this.player1 = player1;
        this.player2 = player2;
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(CheckersGUI.NEUTRAL_BG_COLOR);
        ListCellRenderer cellRenderer = new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JComponent c = (JComponent) super.getListCellRendererComponent(list, value, index, isSelected, false);
                if (isSelected) {
                    c.setForeground(CheckersGUI.NEUTRAL_BG_COLOR);
                    c.setBackground(list.getForeground());
                }
                return c;
            }
        };
        component1 = new JComponent() {
        };
        component2 = new JComponent() {
        };
        component1.setLayout(new GridBagLayout());
        component2.setLayout(new GridBagLayout());
        TitledBorder border = BorderFactory.createTitledBorder(CheckersGUI.BORDER, "Player 1");
        border.setTitleFont(FONT);
        border.setTitleColor(CheckersGUI.PLAYER1_COLOR);
        component1.setBorder(border);
        border = BorderFactory.createTitledBorder(CheckersGUI.BORDER, "Player 2");
        border.setTitleFont(FONT);
        border.setTitleColor(CheckersGUI.PLAYER2_COLOR);
        component2.setBorder(border);
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1;
        c.weighty = 1;
        c.gridx = 0;
        c.gridy = 0;
        String[] displayNames = CheckersPlayerLoader.getPlayerDisplayNames();
        comboBox1 = new SimpleComboBox(displayNames);
        comboBox1.setSelectedValue(player1.getName());
        comboBox1.addItemListener(this);
        comboBox1.setForeground(CheckersGUI.PLAYER1_COLOR);
        comboBox1.setBackground(CheckersGUI.NEUTRAL_BG_COLOR);
        comboBox1.setFocusForeground(CheckersGUI.NEUTRAL_BG_COLOR);
        comboBox1.setFocusBackground(CheckersGUI.PLAYER1_COLOR);
        comboBox1.getList().setCellRenderer(cellRenderer);
        comboBox1.getPopup().setBorder(CheckersGUI.BORDER);
        component1.add(comboBox1, c);
        comboBox2 = new SimpleComboBox(displayNames);
        comboBox2.setSelectedValue(player2.getName());
        comboBox2.addItemListener(this);
        comboBox2.setForeground(CheckersGUI.PLAYER2_COLOR);
        comboBox2.setBackground(CheckersGUI.NEUTRAL_BG_COLOR);
        comboBox2.setFocusForeground(CheckersGUI.NEUTRAL_BG_COLOR);
        comboBox2.setFocusBackground(CheckersGUI.PLAYER2_COLOR);
        comboBox2.getList().setCellRenderer(cellRenderer);
        comboBox2.getPopup().setBorder(CheckersGUI.BORDER);
        component2.add(comboBox2, c);
        c.gridy++;
        c.weighty = 7;
        description1 = new JTextArea(player1.getDescription());
        description1.setEditable(false);
        description1.setLineWrap(true);
        description1.setWrapStyleWord(true);
        description1.setBackground(CheckersGUI.NEUTRAL_BG_COLOR);
        description1.setForeground(CheckersGUI.PLAYER1_COLOR);
        scrollPane1 = new JScrollPane(description1);
        scrollPane1.setOpaque(false);
        border = BorderFactory.createTitledBorder(CheckersGUI.BORDER, "Description");
        border.setTitleFont(FONT);
        border.setTitleColor(CheckersGUI.PLAYER1_COLOR);
        scrollPane1.setBorder(border);
        component1.add(scrollPane1, c);
        description2 = new JTextArea(player2.getDescription());
        description2.setEditable(false);
        description2.setLineWrap(true);
        description2.setWrapStyleWord(true);
        description2.setBackground(CheckersGUI.NEUTRAL_BG_COLOR);
        description2.setForeground(CheckersGUI.PLAYER2_COLOR);
        scrollPane2 = new JScrollPane(description2);
        scrollPane2.setOpaque(false);
        border = BorderFactory.createTitledBorder(CheckersGUI.BORDER, "Description");
        border.setTitleFont(FONT);
        border.setTitleColor(CheckersGUI.PLAYER2_COLOR);
        scrollPane2.setBorder(border);
        component2.add(scrollPane2, c);
        c.insets = new Insets(5, 5, 5, 5);
        c.weightx = 1;
        c.weighty = 5;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(component1, c);
        c.gridx++;
        panel.add(component2, c);
        c.gridwidth = 1;
        c.weighty = 1;
        c.gridy++;
        ok = new JButton("OK");
        ok.addActionListener(this);
        panel.add(ok, c);
        c.gridx--;
        cancel = new JButton("Cancel");
        cancel.addActionListener(this);
        panel.add(cancel, c);
        add(panel);
        setFocusCycleRoot(true);
        description1.setFocusable(false);
        description2.setFocusable(false);
        comboBox1.addKeyListener(this);
        comboBox2.addKeyListener(this);
        scrollPane1.addKeyListener(this);
        scrollPane2.addKeyListener(this);
        cancel.addKeyListener(this);
        ok.addKeyListener(this);
        addKeyListener(this);
        setMinimumSize(new Dimension(400, 300));
        setSize(600, 400);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == ok) accepted = true;
        setVisible(false);
    }

    public CheckersPlayerInterface getPlayer1() {
        return player1;
    }

    public CheckersPlayerInterface getPlayer2() {
        return player2;
    }

    public boolean isAccepted() {
        return accepted;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        try {
            if (e.getSource() == comboBox1) {
                player1 = CheckersPlayerLoader.getPlayerClass(comboBox1.getSelectedIndex()).newInstance();
                description1.setText(player1.getDescription());
                description1.updateUI();
                scrollPane1.getViewport().setViewPosition(new Point());
            } else if (e.getSource() == comboBox2) {
                player2 = CheckersPlayerLoader.getPlayerClass(comboBox2.getSelectedIndex()).newInstance();
                description2.setText(player2.getDescription());
                description2.updateUI();
                scrollPane2.getViewport().setViewPosition(new Point());
            }
        } catch (Exception error) {
            JOptionPane.showMessageDialog(this, error, "ERROR", JOptionPane.ERROR_MESSAGE);
            error.printStackTrace();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) cancel.doClick();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
