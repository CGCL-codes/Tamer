package org.tidelaget.gui.tree;

import org.tidelaget.core.*;
import org.tidelaget.gui.*;
import org.tidelaget.gui.action.*;
import org.tidelaget.gui.panel.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;

public class ClassNode extends FileNode {

    protected Class m_class;

    public ClassNode(Object obj) {
        super(obj);
        m_class = (new TIDEClassLoader(null)).loadClass(((File) obj).getAbsolutePath(), null);
    }

    public TIDEPanel getPanel() {
        return null;
    }

    public ImageIcon getIcon(boolean expanded) {
        return m_docIcon;
    }

    public JPopupMenu getPopupMenu() {
        ActionMapper.getAction(ActionMapper.ACTION_RUNCLASS).setEnabled(getMainMethod() != null);
        return super.getPopupMenu();
    }

    protected Method getMainMethod() {
        Method main = null;
        if (m_class != null) {
            try {
                Method[] methods = m_class.getMethods();
                for (int i = 0; i < methods.length; i++) {
                    if (methods[i].getName().equals("main")) {
                        main = methods[i];
                    }
                }
            } catch (Throwable e) {
            }
        }
        return main;
    }

    public void run() {
        ProcessPanel processPanel = new ProcessPanel("Running " + m_class.getName(), m_class);
        IDEController.get().registerPanel(processPanel, getIcon(false));
        String javaPath = Initializer.getProperty("tomcatide.settings.javahome");
        String javaExecutable = "java";
        String javaClassPath = System.getProperty("java.class.path");
        String filePath = ((File) getUserObject()).getAbsolutePath();
        String className = "/" + m_class.getName() + ".class";
        String classPath = filePath.substring(0, filePath.length() - className.length());
        if (javaPath.length() != 0) {
            File f = new File(javaPath + File.separator + "bin");
            if (f.isDirectory()) {
                javaExecutable = javaPath + File.separator + "bin" + File.separator + javaExecutable;
            }
        }
        String cmd = javaExecutable + " -classpath " + '"' + javaClassPath + ";" + classPath + '"' + " " + m_class.getName();
        processPanel.start(cmd, null, null);
    }
}
