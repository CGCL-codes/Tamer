package com.loribel.tools.java.tree;

import java.io.File;
import javax.swing.Icon;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.swing.GB_ViewManager;
import com.loribel.commons.abstraction.swing.GB_ViewManagerOwner;
import com.loribel.commons.gui.GB_ErrorVM;
import com.loribel.commons.gui.GB_TabbedVM;
import com.loribel.commons.io.GB_DirTools;
import com.loribel.commons.swing.tree.GB_TreeNodeWithChildren;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.GB_IconTools;
import com.loribel.commons.util.GB_LabelIconTools;
import com.loribel.commons.util.STools;
import com.loribel.commons.util.comparator.GB_ComparatorTools;
import com.loribel.tools.java.GB_JavaProjectTools;
import com.loribel.tools.java.bo.GB_JavaProjectBO;
import com.loribel.tools.java.vm.GB_JavaPackageDependVM;
import com.loribel.tools.java.vm.GB_JavaPackageDocVM;
import com.loribel.tools.java.vm.GB_JavaPackageTreeVM;

/**
 * Tree Node to represent package.
 */
public class GB_JavaPackageTN extends GB_TreeNodeWithChildren implements GB_ViewManagerOwner {

    private GB_ViewManager vm;

    private GB_JavaProjectBO javaProject;

    private String packageName;

    private String shortPackageName;

    private File dirPackage;

    public GB_JavaPackageTN(GB_JavaProjectBO a_javaProject, String a_packageName) {
        super(a_packageName);
        packageName = a_packageName;
        shortPackageName = packageName.substring(packageName.lastIndexOf(".") + 1);
        javaProject = a_javaProject;
        dirPackage = GB_JavaProjectTools.toPackageFile(javaProject, packageName);
    }

    public boolean buildChildren2(boolean a_flagDeep) {
        boolean retour = false;
        File[] l_subDirs = GB_DirTools.listDirectories(dirPackage);
        GB_ComparatorTools.sort(l_subDirs, File.class);
        int len = CTools.getSize(l_subDirs);
        for (int i = 0; i < len; i++) {
            File l_subDir = l_subDirs[i];
            String l_packageName = packageName;
            if (!STools.isNull(l_packageName)) {
                l_packageName += ".";
            }
            l_packageName += l_subDir.getName();
            this.add(new GB_JavaPackageTN(javaProject, l_packageName));
            retour = true;
        }
        return retour;
    }

    public GB_LabelIcon getLabelIcon() {
        Icon l_icon = GB_IconTools.get(AA.ICON.JAVA_X16_PACKAGE);
        String l_label = shortPackageName;
        if (STools.isNull(l_label)) {
            l_label = "[src]";
        }
        return GB_LabelIconTools.newLabelIcon(l_label, l_icon);
    }

    public GB_ViewManager getViewManager() {
        if (vm == null) {
            try {
                GB_TabbedVM l_vm = new GB_TabbedVM();
                String l_projectName = javaProject.getProjectName();
                l_vm.addTab(new GB_JavaPackageDocVM(l_projectName, packageName));
                l_vm.addTab(new GB_JavaPackageTreeVM(l_projectName, packageName));
                l_vm.addTab(new GB_JavaPackageDependVM(l_projectName, packageName));
                vm = l_vm;
            } catch (Exception ex) {
                return new GB_ErrorVM(this, ex);
            }
        }
        return vm;
    }
}
