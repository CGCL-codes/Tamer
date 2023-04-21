package com.isa.jump.plugin;

import bsh.EvalError;
import bsh.Interpreter;
import com.vividsolutions.jump.I18N;
import com.vividsolutions.jump.workbench.plugin.EnableCheck;
import com.vividsolutions.jump.workbench.plugin.PlugInContext;
import com.vividsolutions.jump.workbench.ui.MenuNames;
import com.vividsolutions.jump.workbench.ui.plugin.FeatureInstaller;
import com.vividsolutions.jump.workbench.ui.task.TaskMonitorManager;
import com.vividsolutions.jump.workbench.ui.toolbox.ToolboxDialog;
import com.vividsolutions.jump.workbench.plugin.AbstractPlugIn;
import java.io.File;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * This OpenJUMP PlugIn adds the capability to launch a scripted file from the menu.
 * The original design is from ISA (Larry Becker - 2005)
 * Modified by Micha&euml;l Michaud in order to make hirarchical menus possible.
 */
public class BeanToolsPlugIn extends AbstractPlugIn {

    private String lastcmd = "";

    private String beanShellDirName;

    private TaskMonitorManager taskMonitorManager;

    private FeatureInstaller featureInstaller;

    private boolean scriptsExist = false;

    public void initialize(PlugInContext context) throws Exception {
        File plugInDirectory = context.getWorkbenchContext().getWorkbench().getPlugInManager().getPlugInDirectory();
        if (null == plugInDirectory || !plugInDirectory.exists()) {
            System.out.print("BeanTools plugin has not been initialized : the plugin directory is missing");
            return;
        }
        beanShellDirName = plugInDirectory.getPath() + File.separator + "BeanTools";
        File beanShellDir = new File(beanShellDirName);
        featureInstaller = context.getFeatureInstaller();
        taskMonitorManager = new TaskMonitorManager();
        if (beanShellDir.exists()) {
            scanBeanShellDir(beanShellDir, context);
        }
    }

    /**
	 * Extracts the filepath as a String from dir to file
	 */
    private String ancestors(File dir, File file) throws IOException {
        String path = file.getCanonicalPath();
        return path.substring(path.lastIndexOf(dir.getName()), path.lastIndexOf(file.getName()));
    }

    /**
	 * Scan beanShellDir iteratively and makes a script menu-item from each
	 * .bsh file.
	 */
    private void scanBeanShellDir(final File file, final PlugInContext context) throws IOException {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                scanBeanShellDir(f, context);
            }
        } else if (file.getName().endsWith(".bsh")) {
            File beanShellDir = new File(beanShellDirName);
            String ancestors = ancestors(beanShellDir, file);
            String shellName = file.getName().substring(0, file.getName().length() - 4);
            JMenu menu = featureInstaller.menuBarMenu(MenuNames.TOOLS);
            if (menu == null) {
                menu = (JMenu) FeatureInstaller.installMnemonic(new JMenu(I18N.get(MenuNames.TOOLS)), featureInstaller.menuBar());
                featureInstaller.menuBar().add(menu);
            }
            JMenu parent = featureInstaller.createMenusIfNecessary(menu, ancestors.split(File.separator.replace("\\", "\\\\")));
            final JMenuItem menuItem = featureInstaller.installMnemonic(new JMenuItem(shellName), parent);
            final ActionListener listener = AbstractPlugIn.toActionListener(this, context.getWorkbenchContext(), taskMonitorManager);
            menuItem.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (e != null) lastcmd = file.getPath();
                    listener.actionPerformed(e);
                }
            });
            parent.add(menuItem);
        } else ;
    }

    public String getName() {
        return "Bean Tools";
    }

    public boolean execute(final PlugInContext context) throws Exception {
        ToolboxDialog toolbox = new ToolboxDialog(context.getWorkbenchContext());
        try {
            Interpreter interpreter = new Interpreter();
            interpreter.setClassLoader(toolbox.getContext().getWorkbench().getPlugInManager().getClassLoader());
            interpreter.set("wc", toolbox.getContext());
            interpreter.eval("setAccessibility(true)");
            interpreter.eval("import com.vividsolutions.jts.geom.*");
            interpreter.eval("import com.vividsolutions.jump.feature.*");
            interpreter.source(lastcmd);
        } catch (EvalError e) {
            toolbox.getContext().getErrorHandler().handleThrowable(e);
        }
        return true;
    }
}
