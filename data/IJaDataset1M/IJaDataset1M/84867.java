package net.rptools.maptool.client.ui.token;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import net.rptools.lib.swing.SwingUtil;
import net.rptools.maptool.client.AppPreferences;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.swing.AbeillePanel;
import net.rptools.maptool.client.swing.GenericDialog;
import net.rptools.maptool.language.I18N;
import net.rptools.maptool.model.Token;
import net.rptools.maptool.util.ImageManager;
import com.jeta.forms.components.image.ImageComponent;

/**
 * This dialog is used to display all of the token states and notes to the user.
 */
public class NewTokenDialog extends AbeillePanel<Token> {

    /**
	 * The size used to constrain the icon.
	 */
    public static final int SIZE = 64;

    private Token token;

    private boolean success;

    private int centerX;

    private int centerY;

    private GenericDialog dialog;

    /**
	 * Create a new token notes dialog.
	 * 
	 * @param token
	 *            The token being displayed.
	 */
    public NewTokenDialog(Token token, int x, int y) {
        super("net/rptools/maptool/client/ui/forms/newTokenDialog.jfrm");
        this.token = token;
        centerX = x;
        centerY = y;
        panelInit();
    }

    public void showDialog() {
        dialog = new GenericDialog("New Token", MapTool.getFrame(), this) {

            @Override
            protected void positionInitialView() {
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension size = getSize();
                int x = centerX - size.width / 2;
                int y = centerY - size.height / 2;
                if (x < 0) {
                    x = 0;
                }
                if (y < 0) {
                    y = 0;
                }
                if (x + size.width > screenSize.width) {
                    x = screenSize.width - size.width;
                }
                if (y + size.height > screenSize.height) {
                    y = screenSize.height - size.height;
                }
                setLocation(x, y);
            }
        };
        bind(token);
        getRootPane().setDefaultButton(getOKButton());
        dialog.showDialog();
    }

    public ImageComponent getTokenIconPanel() {
        return (ImageComponent) getComponent("tokenIcon");
    }

    public JTextField getNameTextField() {
        return (JTextField) getComponent("@name");
    }

    public JButton getOKButton() {
        return (JButton) getComponent("okButton");
    }

    public JButton getCancelButton() {
        return (JButton) getComponent("cancelButton");
    }

    public JCheckBox getShowDialogCheckbox() {
        return (JCheckBox) getComponent("showDialogCheckbox");
    }

    public void initTokenIconPanel() {
        getTokenIconPanel().setPreferredSize(new Dimension(100, 100));
        getTokenIconPanel().setMinimumSize(new Dimension(100, 100));
        getTokenIconPanel().setIcon(getTokenIcon());
    }

    public void initOKButton() {
        getOKButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                success = true;
                if (!getShowDialogCheckbox().isSelected()) {
                    AppPreferences.setShowDialogOnNewToken(false);
                }
                if (getNameTextField().getText().equals("")) {
                    MapTool.showError(I18N.getText("msg.error.emptyTokenName"));
                    return;
                }
                if (commit()) {
                    dialog.closeDialog();
                }
            }
        });
    }

    public void initCancelButton() {
        getCancelButton().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                success = false;
                dialog.closeDialog();
            }
        });
    }

    public boolean isSuccess() {
        return success;
    }

    /**
	 * Get and icon from the asset manager and scale it properly.
	 * 
	 * @return An icon scaled to fit within a cell.
	 */
    private Icon getTokenIcon() {
        BufferedImage assetImage = ImageManager.getImageAndWait(token.getImageAssetId());
        Dimension imgSize = new Dimension(assetImage.getWidth(), assetImage.getHeight());
        SwingUtil.constrainTo(imgSize, SIZE);
        BufferedImage image = new BufferedImage(imgSize.width, imgSize.height, Transparency.BITMASK);
        Graphics2D g = image.createGraphics();
        g.drawImage(assetImage, (SIZE - imgSize.width) / 2, (SIZE - imgSize.height) / 2, imgSize.width, imgSize.height, null);
        g.dispose();
        return new ImageIcon(image);
    }
}
