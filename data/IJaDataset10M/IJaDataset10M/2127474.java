package planspiel.panels.subpanels;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import planspiel.panels.GameScreen;
import admintool.imp03_data.IMP03_Platform;

/**
 *
 * @author Administrator
 */
public class GameContentPlatform extends JPanel {

    private static final long serialVersionUID = -4498352971543812783L;

    private GameScreen gameScreen = null;

    private JPanel platformParameters = null;

    private JComboBox cbPlatform = null;

    private IMP03_Platform selectedPlatform = null;

    private Image image = null;

    /**
     * 
     * @param gameScreen
     */
    public GameContentPlatform(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        initDisplay();
        try {
            BufferedImage tmpImage = new BufferedImage(1000, 800, BufferedImage.TYPE_INT_RGB);
            tmpImage = ImageIO.read(this.getClass().getResourceAsStream("/planspiel/resources/BackgroundPlatform.jpg"));
            this.image = new ImageIcon(tmpImage.getScaledInstance(720, 580, Image.SCALE_SMOOTH)).getImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @return
     */
    public IMP03_Platform getSelectedPlatform() {
        return selectedPlatform;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }

    private void initDisplay() {
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        final JComboBox cbPlatform = new JComboBox(IMP03_Platform.getAllAvailablePlatformsFromDB(true).toArray());
        JLabel test = new JLabel();
        Dimension cbPlatformDimension = new Dimension(250, test.getHeight());
        cbPlatform.setMaximumSize(cbPlatformDimension);
        cbPlatform.setPreferredSize(cbPlatformDimension);
        platformParameters = new JPanel();
        platformParameters.setOpaque(false);
        cbPlatform.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                selectedPlatform = (IMP03_Platform) (cbPlatform.getSelectedItem());
                platformParameters.removeAll();
                GroupLayout layout = new GroupLayout(platformParameters);
                layout.setAutoCreateGaps(true);
                platformParameters.setLayout(layout);
                JLabel label1 = new JLabel("Name: ");
                JLabel label2 = new JLabel("Desc: ");
                JLabel text1 = new JLabel(selectedPlatform.getPlatformName());
                JTextArea text2 = new JTextArea(selectedPlatform.getPlatformDesc());
                text2.setLineWrap(true);
                text2.setEditable(false);
                text2.setEnabled(false);
                text2.setWrapStyleWord(true);
                JScrollPane sp2 = new JScrollPane(text2);
                layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(label1).addComponent(label2)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(text1).addComponent(sp2))));
                layout.setVerticalGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(label1).addComponent(text1)).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(label2).addComponent(sp2)));
            }
        });
        if (gameScreen.getGame().getCurrentGameRoundController().getSelectedPlatform() != null) {
            IMP03_Platform tmpPlatform = gameScreen.getGame().getCurrentGameRoundController().getSelectedPlatform();
            for (int i = 0; i < cbPlatform.getItemCount(); i++) {
                if (tmpPlatform.getPlatformID() == ((IMP03_Platform) (cbPlatform.getItemAt(i))).getPlatformID()) {
                    cbPlatform.setSelectedIndex(i);
                    cbPlatform.setEnabled(false);
                    break;
                }
            }
        } else if (cbPlatform.getItemCount() > 0) {
            cbPlatform.setSelectedIndex(0);
        }
        layout.setHorizontalGroup(layout.createSequentialGroup().addGap(10).addComponent(cbPlatform).addGap(20).addComponent(platformParameters, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGap(10));
        layout.setVerticalGroup(layout.createSequentialGroup().addGap(10).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(cbPlatform).addComponent(platformParameters, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGap(10));
    }
}
