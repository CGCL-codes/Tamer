package actions;

import gui.MainWindow;
import gui.MapComponent;
import i18n.I18NHelper;
import io.ImageFileFilter;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import tools.FileUtilities;
import tools.ImageUtilities;

public class SaveMapExtract extends ApplicationAction {

    private static final long serialVersionUID = 1L;

    private MainWindow mainWindow;

    public SaveMapExtract(MainWindow mainWindow) {
        super(I18NHelper.getInstance().getString("action.savemapextract"), "save_map_extract", KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK);
        this.mainWindow = mainWindow;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        performAction();
    }

    private void performAction() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(I18NHelper.getInstance().getString("action.savemapextract.fctitle"));
        fileChooser.setFileFilter(new ImageFileFilter());
        int returnValue = fileChooser.showSaveDialog(mainWindow.getJFrame());
        switch(returnValue) {
            case JFileChooser.APPROVE_OPTION:
                saveImage(fileChooser.getSelectedFile());
                break;
            case JFileChooser.CANCEL_OPTION:
                break;
            case JFileChooser.ERROR_OPTION:
                JOptionPane.showMessageDialog(null, I18NHelper.getInstance().getString("action.savemapextract.filedialogerror.msg"), I18NHelper.getInstance().getString("generic.error"), JOptionPane.ERROR_MESSAGE);
                break;
        }
    }

    private void saveImage(File outputFile) {
        String fileSuffix = FileUtilities.getSuffix(outputFile);
        if (ImageUtilities.isWriteSupportedFormat(fileSuffix)) {
            Robot robot = null;
            try {
                robot = new Robot();
            } catch (AWTException e) {
                JOptionPane.showMessageDialog(null, I18NHelper.getInstance().getString("action.savemapextract.creationerror"), I18NHelper.getInstance().getString("generic.error"), JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return;
            }
            MapComponent mapComponent = mainWindow.getMapComponent();
            Point componentLocation = mapComponent.getLocationOnScreen();
            Dimension dimension = new Dimension(mapComponent.getWidth(), mapComponent.getHeight() - mapComponent.STATUS_BAR_HEIGHT);
            Rectangle rectangle = new Rectangle(componentLocation, dimension);
            BufferedImage image = robot.createScreenCapture(rectangle);
            try {
                ImageIO.write(image, fileSuffix, outputFile);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, I18NHelper.getInstance().getString("action.savemapextract.ioerror"), I18NHelper.getInstance().getString("generic.error"), JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, I18NHelper.getInstance().getString("action.savemapextract.unsupportedtype.msg"), I18NHelper.getInstance().getString("action.savemapextract.unsupportedtype.title"), JOptionPane.ERROR_MESSAGE);
            performAction();
        }
    }
}
