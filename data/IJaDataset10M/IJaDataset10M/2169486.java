package medimagesuite.workspace2d.tools.rotate;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.util.Hashtable;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import medimagesuite.development.DevelopmentConstants;
import medimagesuite.plugins.workspaces.workspace2D.Workspace2D;
import medimagesuite.plugins.workspaces.workspace2D.extensionpoints.Tool2D;
import medimagesuite.plugins.workspaces.workspace2D.extensionpoints.UnableToRunTool2DException;
import medimagesuite.plugins.workspaces.workspace2D.ui.MyJInternalFrame;
import medimagesuite.plugins.workspaces.workspace2D.ui.UserImage;
import medimagesuite.util.messages.MessageProvider;
import org.java.plugin.Plugin;

/**
 * @author Augusto Breno - Ufcg
 * 		   augustobreno@gmail.com	
 *
 */
public class Rotate extends Plugin implements Tool2D, ChangeListener, ActionListener {

    public static final String PLUGIN_ID = "medimagesuite.workspace2d.tools.rotate";

    private static final String LANGUAGES_FOLDER = "languages";

    private MyJInternalFrame frame;

    private JSlider slider;

    private JCheckBox interactive;

    private JButton apply;

    private JButton cancel;

    private UserImage userImage;

    private String applyStr;

    private String cancelStr;

    private JPanel controlPanel;

    private float centerX;

    private float centerY;

    @Override
    protected void doStart() throws Exception {
        MessageProvider.getMessageProvider().addMessagesFromPlugin(this.getClass(), Rotate.PLUGIN_ID, LANGUAGES_FOLDER);
        applyStr = MessageProvider.getMessageProvider().getMessage(Messages.APPLY_BUTTON);
        cancelStr = MessageProvider.getMessageProvider().getMessage(Messages.CANCEL_BUTTON);
    }

    @Override
    protected void doStop() throws Exception {
    }

    /** 
     * @see medimagesuite.plugins.workspaces.workspace2D.extensionpoints.Tool2D#run(medimagesuite.plugins.workspaces.workspace2D.Workspace2D)
     */
    public void run(Workspace2D workspace2D) throws UnableToRunTool2DException {
        frame = workspace2D.getActiveFrame();
        if (frame == null) {
            throw new UnableToRunTool2DException(MessageProvider.getMessageProvider().getMessage(Messages.IMAGE_MUST_BE_SELECTED_ERROR_MESSAGE));
        }
        userImage = workspace2D.getActiveImage();
        centerX = userImage.getImage().getWidth() / 2f;
        centerY = userImage.getImage().getHeight() / 2f;
        initRotate();
    }

    /**
     * Starts Rotation
     */
    private void initRotate() {
        if (DevelopmentConstants.DEBUG_ENABLED) System.out.println("[DEBUG] Rotate initialized!");
        createControlPanel();
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                if (DevelopmentConstants.DEBUG_ENABLED) e.printStackTrace();
            }
        }
    }

    /**
     * Creates the control panel to contains rotate functions.
     *
     */
    private void createControlPanel() {
        controlPanel = new JPanel(new BorderLayout());
        String interactiveStr = MessageProvider.getMessageProvider().getMessage(Messages.INTERACTIVE);
        interactive = new JCheckBox(interactiveStr, false);
        apply = new JButton(applyStr);
        apply.addActionListener(this);
        cancel = new JButton(cancelStr);
        cancel.addActionListener(this);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        panel.add(interactive);
        panel.add(apply);
        panel.add(cancel);
        createSlide();
        controlPanel.add(slider, BorderLayout.NORTH);
        controlPanel.add(panel, BorderLayout.SOUTH);
        frame.getContentPane().add(controlPanel, BorderLayout.NORTH);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Creates a slide to control the angle to make rotation.
     * @return TODO
     */
    private void createSlide() {
        slider = new JSlider(0, 360, 0);
        Hashtable<Integer, JLabel> sliderLabels = new Hashtable<Integer, JLabel>();
        for (int label = 0; label <= 360; label += 30) {
            if ((label == 0) || (label % 90) == 0) sliderLabels.put(new Integer(label), new JLabel("" + label)); else sliderLabels.put(new Integer(label), new JLabel("|"));
        }
        slider.setLabelTable(sliderLabels);
        slider.setPaintLabels(true);
        slider.addChangeListener(this);
    }

    /**
     * It Executes the rotation
     * @param image The image to rotate
     * @param angle The angle (in degrees) for rotation
     * @param interType Interpolation Type. This type is one of the Interpolation Type Constants of
     * the <code>Interpolation</code> class
     * @return The PlanarImage with the rotation done
     */
    private PlanarImage rotate(float angle, int interType) {
        double radianAngle = Math.toRadians(angle);
        Interpolation interpolation = Interpolation.getInstance(interType);
        AffineTransform at = AffineTransform.getRotateInstance(radianAngle, centerX, centerY);
        PlanarImage newView = (PlanarImage) JAI.create("affine", frame.getImage().getImage().createSnapshot(), at, interpolation);
        return newView;
    }

    /**
     * @see ChangeListener#stateChanged(javax.swing.event.ChangeEvent)
     */
    public void stateChanged(ChangeEvent e) {
        if (slider.getValueIsAdjusting() && !interactive.isSelected()) return;
        PlanarImage imageRotated = rotate(slider.getValue(), Interpolation.INTERP_NEAREST);
        frame.setView(imageRotated);
        if (DevelopmentConstants.DEBUG_ENABLED) System.out.println("[DEBUG] Rotate done!");
    }

    /**
     * @see ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        frame.getContentPane().remove(controlPanel);
        frame.pack();
        String buttonCmd = e.getActionCommand();
        if (buttonCmd.equals(applyStr)) {
            userImage.setImage(rotate(slider.getValue(), Interpolation.INTERP_BICUBIC));
            userImage.setModified(true);
        }
        frame.setImage(userImage);
        frame.applyImageAsBufferedImage();
        cancelRotate();
    }

    /**
     * Remove this from listeners list
     */
    private void cancelRotate() {
        apply.removeActionListener(this);
        cancel.removeActionListener(this);
        slider.removeChangeListener(this);
        synchronized (this) {
            notify();
        }
    }
}
