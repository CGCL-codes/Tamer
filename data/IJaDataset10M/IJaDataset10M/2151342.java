package com.loribel.tools.fa.vm.action;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.swing.JComponent;
import com.loribel.commons.abstraction.GB_ActionReport;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_ObjectActionReport;
import com.loribel.commons.abstraction.GB_ObjectActionReportOwner;
import com.loribel.commons.exception.GB_ConfigException;
import com.loribel.commons.gui.GB_ViewManagerAbstract;
import com.loribel.commons.gui.bo.GB_BOPanelSingleObject;
import com.loribel.commons.module.states.GB_StateMapAction;
import com.loribel.commons.util.FTools;
import com.loribel.commons.util.GB_LabelIconTools;
import com.loribel.commons.util.Info;
import com.loribel.commons.util.impl.GB_ActionReportImpl;
import com.loribel.tools.fa.bo.GB_FARenameByDateBO;
import com.loribel.tools.file.date.GB_FileRenameByDate;

/**
 * File Rename by date.
 * 
 * @author Gr�gory Borelli
 */
public class GB_FileJpgRenameByDateVM extends GB_ViewManagerAbstract implements GB_ObjectActionReportOwner, GB_ObjectActionReport {

    private GB_FARenameByDateBO config;

    private boolean flagSimulation = false;

    private List files;

    private Map mapRename;

    public GB_FileJpgRenameByDateVM(List a_files) {
        super();
        config = new GB_FARenameByDateBO();
        files = a_files;
    }

    public GB_LabelIcon getLabelIcon() {
        String l_label = "Renommer par date";
        return GB_LabelIconTools.newLabelIcon(l_label);
    }

    protected JComponent buildView() {
        return new MyView();
    }

    public boolean acceptSimulation() {
        return true;
    }

    public GB_ActionReport doActionRpt(Object a_file) {
        if (mapRename == null) {
            buildMapRename();
        }
        File l_file = (File) a_file;
        String l_path = l_file.getAbsolutePath();
        String l_newName = (String) mapRename.get(l_path);
        if (l_newName == null) {
            return new GB_ActionReportImpl(GB_StateMapAction.STATE_ERROR, "null name");
        }
        if (l_path.equals(l_newName)) {
            return new GB_ActionReportImpl(GB_StateMapAction.STATE_NOTHING_TO_DO);
        }
        if (flagSimulation) {
            return new GB_ActionReportImpl(GB_StateMapAction.STATE_TO_DO, l_newName);
        } else {
            File l_newFile = new File(l_newName);
            l_newFile.getParentFile().mkdirs();
            boolean b = l_file.renameTo(l_newFile);
            if (b) {
                String l_report = l_file.getName() + "\t" + l_newFile.getName() + "\n";
                File l_reportFile = new File(config.getDestinationDir(), "report.txt");
                try {
                    FTools.writeFile(l_reportFile, l_report, true);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return new GB_ActionReportImpl(GB_StateMapAction.STATE_SUCCESS, l_newName);
            } else {
                return new GB_ActionReportImpl(GB_StateMapAction.STATE_ERROR, l_newName);
            }
        }
    }

    private void buildMapRename() {
        Info.setInfo("Pr�paration pour renommer les fichiers...");
        GB_FileRenameByDate l_tools = new GB_FileRenameByDate(files, config);
        mapRename = l_tools.getRenameMap();
        Info.setInfo("...");
    }

    private class MyView extends GB_BOPanelSingleObject {

        MyView() {
            super(config);
            this.addAllProperties();
            this.addRowEnd();
        }
    }

    public GB_ObjectActionReport getAction(boolean a_useSimulation) throws GB_ConfigException {
        flagSimulation = a_useSimulation;
        return this;
    }

    public boolean isSimulationAvailable() {
        return true;
    }
}
