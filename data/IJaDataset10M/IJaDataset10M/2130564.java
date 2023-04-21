package santa.jpaint.gui.filterdialogs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import santa.jpaint.kernel.JPaintUtility;
import santa.jpaint.resource.Resources;
import santa.nice.imaging.PaperTexture;

public class PaperTextureDialog extends FilterDialog {

    private static final long serialVersionUID = -7034434337229885523L;

    public static PaperTextureDialog instance = new PaperTextureDialog();

    protected BufferedImage texture = Resources.paperTexture;

    protected JFileChooser fileChooser = new JFileChooser();

    protected PaperTextureDialog() {
        this.setTitle("Paper Texture");
        this.setSize(480, 400);
        content.setLayout(new BorderLayout());
        JPanel temp = new JPanel();
        JButton btnSelect = new JButton("Select texture...");
        JButton btnDefault = new JButton("Default texture");
        temp.add(btnDefault);
        temp.add(btnSelect);
        content.add(temp, BorderLayout.NORTH);
        final JLabel textureLabel = new JLabel();
        textureLabel.setHorizontalAlignment(JLabel.CENTER);
        textureLabel.setVerticalAlignment(JLabel.CENTER);
        textureLabel.setIcon(new ImageIcon(texture));
        content.add(new JScrollPane(textureLabel), BorderLayout.CENTER);
        btnSelect.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ev) {
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    try {
                        texture = ImageIO.read(fileChooser.getSelectedFile());
                        textureLabel.setIcon(new ImageIcon(texture));
                        updateParams();
                    } catch (IOException e) {
                        JPaintUtility.handleException(e);
                    }
                }
            }
        });
        btnDefault.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                texture = Resources.paperTexture;
                textureLabel.setIcon(new ImageIcon(texture));
                updateParams();
            }
        });
    }

    @Override
    public void updateParams() {
        filter = new PaperTexture(texture);
        super.updateParams();
    }
}
