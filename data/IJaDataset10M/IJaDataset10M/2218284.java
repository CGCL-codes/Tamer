package freestyleLearning.setupCore.installAnywhere;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.xml.bind.Dispatcher;
import javax.xml.bind.DuplicateIdentifierException;
import com.zerog.ia.api.pub.CustomCodeAction;
import com.zerog.ia.api.pub.InstallException;
import com.zerog.ia.api.pub.InstallerProxy;
import com.zerog.ia.api.pub.UninstallerProxy;
import freestyleLearning.homeCore.learningUnitsManager.data.xmlBinding.LearningUnitDescriptor;
import freestyleLearning.homeCore.learningUnitsManager.data.xmlBinding.LearningUnitsDescriptor;
import freestyleLearning.homeCore.learningUnitsManager.data.xmlBindingSubclasses.FSLLearningUnitDescriptor;
import freestyleLearning.homeCore.learningUnitsManager.data.xmlBindingSubclasses.FSLLearningUnitsDescriptor;
import freestyleLearningGroup.independent.gui.FLGMediaFileChooser;
import freestyleLearningGroup.independent.gui.FLGOptionPane;
import freestyleLearningGroup.independent.util.FLGInternationalization;

/**
 * FSLLearningUnitLinkWorker.java This class implements CustomCode executed by the ZeroG InstallAnywhere environment
 * Tasks are: 1) Modify LearningUnitsDescriptor entries to enable linking of Learning Units
 * which data resides on intall medium
 */
public class FSLLearningUnitLinkAction extends CustomCodeAction {

    private FLGInternationalization internationalization;

    private String luString = "LearningUnits";

    private String luInstallDirName = "learningUnits";

    private String descriptorString = "learningUnitsDescriptor.xml";

    private String setupString = "Setup";

    private File descriptorFile;

    private FSLLearningUnitsDescriptor descriptor;

    public FSLLearningUnitLinkAction() {
        internationalization = new FLGInternationalization("freestyleLearning.setupCore.installAnywhere.internationalization", getClass().getClassLoader());
    }

    /**
     * This is the method that is called at install-time.  The InstallerProxy
     * instance provides methods to access information in the installer, set status, and control flow.
     */
    public void install(InstallerProxy ip) throws InstallException {
        try {
            boolean learningUnitsFound = false;
            String userInstallDir = ip.substitute("$USER_INSTALL_DIR$");
            String executingFrom = ip.substitute("$EXTRACTOR_EXECUTABLE$");
            String fileSeparator = ip.substitute("$prop.file.separator$");
            String installDriveRoot;
            int setupDirIndex = executingFrom.indexOf(fileSeparator + setupString);
            if (setupDirIndex >= 0) {
                installDriveRoot = executingFrom.substring(0, setupDirIndex + 1);
            } else {
                installDriveRoot = executingFrom.substring(0, executingFrom.indexOf(fileSeparator) + 1);
            }
            File learningUnitsSourceDirectory = new File(installDriveRoot + luString);
            boolean selectionAlive = true;
            if (learningUnitsSourceDirectory.exists()) {
                try {
                    descriptorFile = new File(learningUnitsSourceDirectory.getAbsolutePath() + fileSeparator + descriptorString);
                    descriptor = loadLearningUnitsDescriptor(descriptorFile);
                    learningUnitsFound = true;
                    selectionAlive = false;
                } catch (Exception e) {
                    System.out.println("***error loading descriptor***\n\t" + e);
                    JOptionPane.showMessageDialog(null, "Error Loading Descriptor File:\n" + e, "Error loading Descriptor File!", JOptionPane.INFORMATION_MESSAGE);
                }
            }
            while (selectionAlive) {
                String title_luNotFound = internationalization.getString("message.title.luDirNotFound");
                String label_luNotFound = internationalization.getString("message.label.luDirNotFound");
                if ((FLGOptionPane.showConfirmDialog(label_luNotFound, title_luNotFound, FLGOptionPane.YES_NO_OPTION, FLGOptionPane.QUESTION_MESSAGE)) == FLGOptionPane.YES_OPTION) {
                    FLGMediaFileChooser fileChooser = new FLGMediaFileChooser(FLGMediaFileChooser.XML);
                    if (fileChooser.showDialog(new File(installDriveRoot))) {
                        if (fileChooser.getSelectedFile() != null) {
                            File descriptorFile = fileChooser.getSelectedFile();
                            try {
                                descriptor = loadLearningUnitsDescriptor(descriptorFile);
                                learningUnitsSourceDirectory = descriptorFile.getParentFile();
                                learningUnitsFound = true;
                                selectionAlive = false;
                            } catch (Exception e) {
                                System.out.println("***error loading descriptor***\n\t" + e);
                                JOptionPane.showMessageDialog(null, "Error Loading Descriptor File:\n" + e, "Error loading Descriptor File!", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                    } else selectionAlive = false;
                } else selectionAlive = false;
            }
            if (learningUnitsFound) {
                File installLearningUnitsDirectory = new File(userInstallDir + fileSeparator + luInstallDirName);
                java.util.List learningUnitDescriptors = descriptor.getLearningUnitsDescriptors();
                File installedUnitsDescriptorFile = new File(userInstallDir + fileSeparator + luInstallDirName + fileSeparator + "learningUnitsDescriptor.xml");
                FSLLearningUnitsDescriptor installedUnitsDescriptor;
                try {
                    installedUnitsDescriptor = loadLearningUnitsDescriptor(installedUnitsDescriptorFile);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                for (int i = 0; i < learningUnitDescriptors.size(); i++) {
                    FSLLearningUnitDescriptor learningUnitDescriptor = (FSLLearningUnitDescriptor) learningUnitDescriptors.get(i);
                    String learningUnitPath = learningUnitDescriptor.getPath();
                    int lastSeparatorIndex = learningUnitPath.lastIndexOf(fileSeparator);
                    if (lastSeparatorIndex < 0) lastSeparatorIndex = learningUnitPath.lastIndexOf("/");
                    if (lastSeparatorIndex < 0) lastSeparatorIndex = 0;
                    String learningUnitDirectoryName = learningUnitPath.substring(lastSeparatorIndex + 1, learningUnitPath.length());
                    try {
                        File learningUnitSourceDirectory = new File(learningUnitsSourceDirectory.getAbsolutePath() + fileSeparator + learningUnitDirectoryName);
                        String installedLearningUnitPath = learningUnitSourceDirectory.getAbsolutePath();
                        learningUnitDescriptor.setPath(installedLearningUnitPath);
                        installedUnitsDescriptor.getLearningUnitsDescriptors().add(learningUnitDescriptor);
                        saveLearningUnitsDescriptor(installedUnitsDescriptor, installedUnitsDescriptorFile);
                    } catch (DuplicateIdentifierException e) {
                        System.out.println(e);
                        JOptionPane.showMessageDialog(new JLabel(internationalization.getString("message.title.dublicateUnit")), internationalization.getString("message.label.dublicateUnit1") + "\n\n" + learningUnitDescriptor.getTitle() + "\n\n" + internationalization.getString("message.label.dublicateUnit2"), internationalization.getString("message.title.dublicateUnit"), JOptionPane.ERROR_MESSAGE);
                        installedUnitsDescriptor.getLearningUnitsDescriptors().remove(learningUnitDescriptor);
                        try {
                            saveLearningUnitsDescriptor(installedUnitsDescriptor, installedUnitsDescriptorFile);
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private FSLLearningUnitsDescriptor loadLearningUnitsDescriptor(File learningUnitsDescriptorFile) {
        FSLLearningUnitsDescriptor learningUnitsDescriptor = null;
        Dispatcher dispatcher = LearningUnitsDescriptor.newDispatcher();
        dispatcher.register(LearningUnitsDescriptor.class, FSLLearningUnitsDescriptor.class);
        dispatcher.register(LearningUnitDescriptor.class, FSLLearningUnitDescriptor.class);
        FileInputStream learningUnitsDescriptorFileInputStream = null;
        try {
            learningUnitsDescriptorFileInputStream = new FileInputStream(learningUnitsDescriptorFile);
            learningUnitsDescriptor = (FSLLearningUnitsDescriptor) dispatcher.unmarshal(learningUnitsDescriptorFileInputStream);
            learningUnitsDescriptorFileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            learningUnitsDescriptor = new FSLLearningUnitsDescriptor();
        }
        return learningUnitsDescriptor;
    }

    private boolean saveLearningUnitsDescriptor(FSLLearningUnitsDescriptor installedUnitsDescriptor, File installedUnitsDescriptorFile) throws DuplicateIdentifierException {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(installedUnitsDescriptorFile);
            installedUnitsDescriptor.validate();
            installedUnitsDescriptor.marshal(fileOutputStream);
            fileOutputStream.close();
            return true;
        } catch (DuplicateIdentifierException die) {
            throw die;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *  This is the method that is called at uninstall-time.  The DataInput
     * instance provides access to any information written at install-time
     * with the instance of DataOutput provided by UninstallerProxy.getLogOutput().
     */
    public void uninstall(UninstallerProxy up) throws InstallException {
    }

    /** This method will be called to display a status message during the installation. */
    public String getInstallStatusMessage() {
        return internationalization.getString("install.statusMessage");
    }

    /** This method will be called to display a status message during the uninstall. */
    public String getUninstallStatusMessage() {
        return null;
    }
}
