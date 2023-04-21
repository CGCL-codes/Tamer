package net.bpfurtado.tas.builder;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;
import net.bpfurtado.tas.Conf;
import net.bpfurtado.tas.Workspace;
import net.bpfurtado.tas.model.Scene;
import net.bpfurtado.tas.view.Util;
import org.apache.log4j.Logger;

public class ImagePanelBuilder {

    @SuppressWarnings("unused")
    private static final Logger logger = Logger.getLogger(ImagePanelBuilder.class);

    private static final Conf conf = Conf.builder();

    private static final String SCENE_IMAGE_LAST_USED_FOLDER = "sceneImageLastUsedFolder";

    private static final JFileChooser fc = new JFileChooser();

    static {
        fc.setFileFilter(new FileFilter() {

            @Override
            public String getDescription() {
                return "Image";
            }

            @Override
            public boolean accept(File f) {
                String n = f.getName().toLowerCase();
                return f.isDirectory() || n.endsWith(".png") || n.endsWith(".jpg") || n.endsWith(".gif");
            }
        });
    }

    private JPanel mainPanel;

    private JPanel centralPn;

    private JLabel imageLb;

    private ImageReceiver imageReceiver;

    public ImagePanelBuilder(ImageReceiver imageReceiver, Scene scene) {
        this.imageReceiver = imageReceiver;
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(createTopPanel(), BorderLayout.PAGE_START);
        this.centralPn = createCentralPanel(scene);
        mainPanel.add(new JScrollPane(centralPn), BorderLayout.CENTER);
    }

    protected void updateImage(String imageId) {
        imageLb.setIcon(new ImageIcon(imageReceiver.getWorkspace().imagePathFrom(imageId)));
        imageReceiver.fireNewImageSelectedAction(imageId);
    }

    public void update(Scene currentScene) {
        Icon image = null;
        if (currentScene.getImageId() == null) {
            image = Util.getImage("chest.JPG");
        } else {
            image = imageReceiver.getWorkspace().imageFrom(currentScene);
        }
        this.imageLb = new JLabel(image);
        centralPn.removeAll();
        centralPn.add(imageLb);
    }

    private void chooseImageButtonAction(Workspace workspace) {
        File lastFolder = new File(conf.get(SCENE_IMAGE_LAST_USED_FOLDER, System.getProperty("user.home")));
        if (lastFolder.exists()) {
            fc.setCurrentDirectory(lastFolder);
        }
        fc.showOpenDialog(mainPanel);
        File imageFile = fc.getSelectedFile();
        String imageId = imageReceiver.getWorkspace().copy(imageFile);
        conf.set(SCENE_IMAGE_LAST_USED_FOLDER, imageFile.getParentFile().getAbsolutePath());
        conf.save();
        updateImage(imageId);
    }

    private JPanel createCentralPanel(Scene scene) {
        JPanel centralPn = new JPanel();
        centralPn.setBorder(BorderFactory.createRaisedBevelBorder());
        if (scene != null) {
            update(scene);
        } else {
            this.imageLb = new JLabel(Util.getImage("chest.JPG"));
        }
        centralPn.add(imageLb);
        return centralPn;
    }

    private JPanel createTopPanel() {
        JPanel top = new JPanel();
        JButton chooseBt = new JButton("Choose...");
        chooseBt.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                chooseImageButtonAction(imageReceiver.getWorkspace());
            }
        });
        top.add(chooseBt);
        return top;
    }

    JPanel getPanel() {
        return mainPanel;
    }
}
