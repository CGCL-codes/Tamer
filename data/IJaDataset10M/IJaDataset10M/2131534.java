package com.javable.cese.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import javax.swing.JFileChooser;
import javax.swing.beanbuilder.CustomObjectInputStream;
import javax.swing.beanbuilder.JarClassLoader;
import com.javable.cese.CESE;
import com.javable.cese.ModelManager;
import com.javable.cese.ResourceManager;
import com.javable.utils.ActionSupport;
import com.javable.utils.ExceptionDialog;
import com.javable.utils.ExtensionFileFilter;

public class OpenParamsAction extends ActionSupport implements java.beans.PropertyChangeListener {

    private File dir, file;

    private ModelManager manager;

    /** Creates new OpenParamsAction */
    public OpenParamsAction(ModelManager m) {
        this();
        manager = m;
        manager.addPropertyChangeListener(this);
    }

    /** Creates new OpenParamsAction */
    public OpenParamsAction() {
        super(ResourceManager.getResource("Open_Parameters..."), ResourceManager.getResource("Open_parameters"), ResourceManager.getResource("open_params_image"));
        dir = new File(this.getClass().getResource(ResourceManager.getResource("params_dir")).getFile());
    }

    public void actionPerformed(ActionEvent evt) {
        ObjectInputStream in = null;
        try {
            JFileChooser filechooser = new JFileChooser(dir);
            filechooser.setDialogTitle(ResourceManager.getResource("Load_Model_Parameters"));
            ExtensionFileFilter preferredFilter = new ExtensionFileFilter(ResourceManager.getResource("mdl_extension"), ResourceManager.getResource("Model_Parameters_Files"));
            filechooser.addChoosableFileFilter(preferredFilter);
            filechooser.setFileFilter(preferredFilter);
            int retVal = filechooser.showOpenDialog(CESE.getEnvironment());
            if (retVal == JFileChooser.APPROVE_OPTION) {
                file = filechooser.getSelectedFile();
                dir = filechooser.getCurrentDirectory();
                if (file.getPath() == null) return;
                in = new CustomObjectInputStream(new FileInputStream(file.getPath()), JarClassLoader.getJarClassLoader());
                Object lm = in.readObject();
                manager.loadModel(lm);
                manager.setTabName(file.getName());
            }
        } catch (Exception e) {
            ExceptionDialog.showExceptionDialog(ResourceManager.getResource("Error"), ResourceManager.getResource("Error_loading_parameters_file."), e);
        } finally {
            try {
                in.close();
            } catch (Exception ex) {
            }
        }
    }

    public void propertyChange(java.beans.PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("model_control")) this.setEnabled(((Boolean) evt.getNewValue()).booleanValue());
    }
}
