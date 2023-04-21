package JavaOrc.diagram;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import JavaOrc.ui.FlatTextArea;

/**
 * @class InterfaceComponent
 *
 * @date 08-20-2001
 * @author Eric Crahen
 * @version 1.0
 *
 */
public class InterfaceComponent extends CustomComponent {

    protected static final CustomUI interfaceUI = new CustomUI("interface");

    protected static final Insets margin = new Insets(1, 1, 1, 1);

    protected JLabel label = new JLabel("<< interface >>", JLabel.CENTER);

    protected JTextField title = new JTextField();

    protected FlatTextArea members = new FlatTextArea(true);

    static {
        UIManager.put("interface.background", new Color(0xFF, 0xFF, 0xDD));
        UIManager.put("interface.foreground", Color.black);
        UIManager.put("interface.border", BorderFactory.createLineBorder(Color.black, 1));
    }

    /**
   * Create a new Component for painting interfaces
   */
    public InterfaceComponent() {
        this.setLayout(null);
        label.setOpaque(true);
        this.add(label);
        title.setOpaque(true);
        title.setHorizontalAlignment(JTextField.CENTER);
        title.setMargin(margin);
        title.setBorder(null);
        this.add(title);
        members.setBorder(BorderFactory.createLineBorder(Color.black, 1));
        members.setMargin(margin);
        this.add(members);
        setUI(interfaceUI);
        setFont(title.getFont());
    }

    public void setTitle(String s) {
        title.setText(s);
    }

    public String getTitle() {
        return title.getText();
    }

    public void setMembers(String s) {
        members.setText(s);
    }

    public String getMembers() {
        return members.getText();
    }

    public void setFont(Font font) {
        super.setFont(font);
        font = font.deriveFont(Font.ITALIC | Font.PLAIN, font.getSize() - 2.0f);
        label.setFont(font);
    }

    public void doLayout() {
        Insets insets = this.getInsets();
        int w = this.getWidth() - (insets.left + insets.right);
        int h = this.getHeight() - (insets.top + insets.bottom);
        int x = insets.left;
        int y = insets.top;
        int componentHeight = label.getPreferredSize().height + 2;
        label.reshape(x + 1, y + 1, w - 2, componentHeight);
        y += componentHeight + 1;
        h -= componentHeight + 1;
        componentHeight = title.getPreferredSize().height + 2;
        title.setBounds(x + 1, y, w - 2, componentHeight);
        y += componentHeight + 1;
        h -= componentHeight + 1;
        componentHeight = h;
        members.setBounds(x, y, w, componentHeight);
    }

    /**
   * Paint the normal border, and the border around the label & the text field
   */
    public void paintBorder(Graphics g) {
        super.paintBorder(g);
        Insets insets = this.getInsets();
        int x = insets.left;
        int y = insets.top;
        int w = label.getWidth() + 1;
        int h = label.getHeight() + title.getHeight() + 1;
        g.setColor(Color.black);
        g.drawRect(x, y, w, h);
    }
}
