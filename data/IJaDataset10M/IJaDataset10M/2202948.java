package jorgan.swing.font;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import jorgan.swing.StandardDialog;
import bias.Configuration;

/**
 * Selector of a font.
 */
public class FontSelector extends JPanel {

    private static Configuration config = Configuration.getRoot().get(FontSelector.class);

    /**
	 * The button used to edit the selected font.
	 */
    private JButton button = new JButton();

    private FontPanel panel = new FontPanel();

    /**
	 * Create a new selector.
	 */
    public FontSelector() {
        super(new BorderLayout());
        button.setHorizontalAlignment(JButton.LEFT);
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                StandardDialog dialog = StandardDialog.create(FontSelector.this);
                dialog.addCancelAction();
                dialog.addOKAction();
                dialog.setBody(panel);
                config.get("dialog").read(dialog);
                dialog.setVisible(true);
                config.get("dialog").write(dialog);
                dialog.setBody(null);
                dialog.dispose();
                if (!dialog.wasCancelled()) {
                    setSelectedFont(getSelectedFont());
                }
            }
        });
        add(button, BorderLayout.CENTER);
        setSelectedFont(new Font("Arial", Font.PLAIN, 12));
    }

    @Override
    public void setEnabled(boolean enabled) {
        button.setEnabled(enabled);
    }

    /**
	 * Set the selected font.
	 * 
	 * @param font
	 *            the font to select
	 */
    public void setSelectedFont(Font font) {
        panel.setSelectedFont(font);
        String text;
        if (font == null) {
            text = "-";
        } else {
            String name = font.getName();
            int size = font.getSize();
            String style = panel.formatStyle(font.getStyle());
            text = (name + " " + size + " " + style);
        }
        button.setText(text);
    }

    /**
	 * Get the selected font.
	 * 
	 * @return the selected font
	 */
    public Font getSelectedFont() {
        return panel.getSelectedFont();
    }
}
