package jp.hpl.map.selectprop.texture;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import com.sun.media.sound.Toolkit;
import jp.hpl.common.data.StaticData;
import jp.hpl.common.gui.UITool;
import jp.hpl.map.data.MapStaticData;
import jp.hpl.map.mouseevent.MapCanvas;
import src.backend.ShapeDescriptor;
import src.backend.wad.WadTool;

public class TextureEditPanel extends JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private static final String IMAGE_UNKNOWN_TEXTURE_FILE_PATH = StaticData.IMG_DIR_PATH + "UnknownTexture.png";

    MapCanvas canvas;

    Frame parentFrame;

    private JTextField textureCollectionText;

    private JTextField textureCLUTText;

    private JTextField textureShapeText;

    private Image unknownTextureImage;

    public ShapeDescriptor getTextureDescriptor() {
        ShapeDescriptor descriptor = new ShapeDescriptor(0);
        try {
            descriptor.setColl(Short.parseShort(textureCollectionText.getText()));
            descriptor.setClut(Short.parseShort(textureCLUTText.getText()));
            descriptor.setBmp(Short.parseShort(textureShapeText.getText()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return descriptor;
    }

    public void setTextureDescriptor(ShapeDescriptor descriptor) {
        this.textureCollectionText.setText("" + descriptor.getColl());
        this.textureCLUTText.setText("" + descriptor.getClut());
        this.textureShapeText.setText("" + descriptor.getBmp());
    }

    public TextureEditPanel(String label, MapCanvas canvas, Frame parentFrame) {
        super();
        LineBorder lnBorder = new LineBorder(Color.black);
        TitledBorder tBorder = new TitledBorder(lnBorder, label);
        this.setBorder(tBorder);
        this.canvas = canvas;
        this.parentFrame = parentFrame;
        GridBagLayout gbl = new GridBagLayout();
        this.setLayout(gbl);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.WEST;
        this.unknownTextureImage = java.awt.Toolkit.getDefaultToolkit().getImage(IMAGE_UNKNOWN_TEXTURE_FILE_PATH);
        int y = 0;
        UITool.addToGridBagLayoutPanel(this, gbc, 0, y, 1, 1, new JLabel(" Collection:"), gbl);
        textureCollectionText = UITool.createDefaultTextField();
        UITool.addToGridBagLayoutPanel(this, gbc, 1, y, 1, 1, textureCollectionText, gbl);
        textureCollectionText.setEditable(false);
        y++;
        UITool.addToGridBagLayoutPanel(this, gbc, 0, y, 1, 1, new JLabel(" CLUT"), gbl);
        textureCLUTText = UITool.createDefaultTextField();
        textureCLUTText.setEditable(false);
        UITool.addToGridBagLayoutPanel(this, gbc, 1, y, 1, 1, textureCLUTText, gbl);
        y++;
        UITool.addToGridBagLayoutPanel(this, gbc, 0, y, 1, 1, new JLabel(" Shape"), gbl);
        textureShapeText = UITool.createDefaultTextField();
        textureShapeText.setEditable(false);
        UITool.addToGridBagLayoutPanel(this, gbc, 1, y, 1, 1, textureShapeText, gbl);
        y++;
        JButton editButton = new JButton(new ImageIcon(unknownTextureImage));
        editButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                doEditTexture(e);
            }
        });
        UITool.addToGridBagLayoutPanel(this, gbc, 1, y, 1, 1, editButton, gbl);
    }

    private void doEditTexture(ActionEvent e) {
        TextureDialog textureDialog = new TextureDialog(parentFrame);
        textureDialog.setVisible(true);
    }
}
