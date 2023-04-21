package com.nhncorp.cubridqa.schedule;

import java.io.File;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import com.nhncorp.cubridqa.model.ScheduleModel;
import com.nhncorp.cubridqa.schedule.composite.ChooseBuildComposite;
import com.nhncorp.cubridqa.schedule.composite.ChooseTestCaseComposite;
import com.nhncorp.cubridqa.schedule.composite.ConfigurationComposite;
import com.nhncorp.cubridqa.schedule.composite.CreateDbComposite;
import com.nhncorp.cubridqa.schedule.composite.IScheduleComposite;
import com.nhncorp.cubridqa.schedule.composite.PrePostWorkComposite;
import com.nhncorp.cubridqa.utils.FileUtil;
import com.nhncorp.cubridqa.utils.TreeUtil;

/**
 * A factory class.
 * Create schedule composite or update,delete schedule according your selection.
 * @ClassName: ScheduleCompositeFactory
 * @date 2009-9-4
 * @version V1.0 
 * Copyright (C) www.nhn.com
 */
public class ScheduleCompositeFactory {

    /**
	 * 
	 * @Title: getScheduleComposite
	 * @Description:Create composite.
	 * @param @param type
	 * @param @param parent
	 * @param @param scheduleModel
	 * @param @param treeItem
	 * @param @return
	 * @return IScheduleComposite
	 * @throws
	 */
    public static IScheduleComposite getScheduleComposite(String type, Composite parent, ScheduleModel scheduleModel, TreeItem treeItem) {
        if (type.equalsIgnoreCase("regular")) {
            ScheduleController ctrl = new ScheduleController();
            if (scheduleModel.getName().endsWith("/")) {
                return ctrl.newSchedule(scheduleModel.getName(), ScheduleNewDialog.SCHEDULE_TYPE_REGULAR, parent, treeItem);
            } else {
                return ctrl.newSchedule(scheduleModel.getName() + "/", ScheduleNewDialog.SCHEDULE_TYPE_REGULAR, parent, treeItem);
            }
        } else if (type.equalsIgnoreCase("irregular")) {
            ScheduleController ctrl = new ScheduleController();
            if (scheduleModel.getName().endsWith("/")) {
                return ctrl.newSchedule(scheduleModel.getName() + type, ScheduleNewDialog.SCHEDULE_TYPE_IRREGULAR, parent, treeItem);
            } else {
                return ctrl.newSchedule(scheduleModel.getName() + "/" + type, ScheduleNewDialog.SCHEDULE_TYPE_IRREGULAR, parent, treeItem);
            }
        } else if (type.equalsIgnoreCase("ChooseTestEngine")) {
            return new ChooseBuildComposite(parent, SWT.NONE, null, scheduleModel, treeItem, false);
        } else if (type.equalsIgnoreCase("ChooseTestCase")) {
            return new ChooseTestCaseComposite(parent, SWT.NONE, null, scheduleModel, treeItem, false);
        } else if (type.equalsIgnoreCase("ApplyConfiguration")) {
            return new ConfigurationComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL, null, scheduleModel, treeItem, false);
        } else if (type.equalsIgnoreCase("CreateDb")) {
            return new CreateDbComposite(parent, SWT.NONE, null, scheduleModel, treeItem, false);
        } else if (type.equalsIgnoreCase("Pre/PostWork")) {
            return new PrePostWorkComposite(parent, SWT.NONE, null, scheduleModel, treeItem, false);
        } else {
            MessageDialog.openError(parent.getShell(), "Create error", "Can not create anything here!");
            return null;
        }
    }

    /**
	 * 
	 * @Title: updateShedule
	 * @Description:Update composite.
	 * @param @param parent
	 * @param @param scheduleModel
	 * @param @return
	 * @return IScheduleComposite
	 * @throws
	 */
    public static IScheduleComposite updateShedule(Composite parent, ScheduleModel scheduleModel) {
        if ("regular".equalsIgnoreCase(scheduleModel.getParent().getId())) {
            ScheduleController ctrl = new ScheduleController();
            return ctrl.updateSchedule(scheduleModel.getName(), ScheduleNewDialog.SCHEDULE_TYPE_REGULAR, parent);
        } else if ("irregular".equalsIgnoreCase(scheduleModel.getParent().getId())) {
            ScheduleController ctrl = new ScheduleController();
            return ctrl.updateSchedule(scheduleModel.getName(), ScheduleNewDialog.SCHEDULE_TYPE_IRREGULAR, parent);
        } else if (scheduleModel.getParent().getId().equalsIgnoreCase("ChooseTestEngine")) {
            ChooseBuildComposite composite = new ChooseBuildComposite(parent, SWT.NONE, null, scheduleModel, null, true);
            composite.loadData(scheduleModel);
            return composite;
        } else if (scheduleModel.getParent().getId().equalsIgnoreCase("ChooseTestCase")) {
            ChooseTestCaseComposite composite = new ChooseTestCaseComposite(parent, SWT.NONE, null, scheduleModel, null, true);
            composite.loadData(scheduleModel);
            return composite;
        } else if (scheduleModel.getParent().getId().equalsIgnoreCase("ApplyConfiguration")) {
            return new ConfigurationComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL, null, scheduleModel, null, true);
        } else if (scheduleModel.getParent().getId().equalsIgnoreCase("CreateDb")) {
            CreateDbComposite composite = new CreateDbComposite(parent, SWT.NONE, null, scheduleModel, null, true);
            composite.loadData(scheduleModel);
            return composite;
        } else if (scheduleModel.getParent().getId().equalsIgnoreCase("Pre/PostWork")) {
            PrePostWorkComposite composite = new PrePostWorkComposite(parent, SWT.NONE, null, scheduleModel, null, true);
            composite.loadData(scheduleModel);
            return composite;
        } else {
            return null;
        }
    }

    /**
	 * 
	 * @Title: deleteShedule
	 * @Description:Delete schedule or procedure.
	 * @param @param scheduleModel
	 * @param @param treeItem
	 * @return void
	 * @throws
	 */
    public static void deleteShedule(ScheduleModel scheduleModel, TreeItem treeItem) {
        if (scheduleModel.getParent().getId().equalsIgnoreCase("regular") || scheduleModel.getParent().getId().equalsIgnoreCase("irregular") || scheduleModel.getParent().getId().equalsIgnoreCase("ChooseTestEngine") || scheduleModel.getParent().getId().equalsIgnoreCase("ChooseTestCase") || scheduleModel.getParent().getId().equalsIgnoreCase("ApplyConfiguration") || scheduleModel.getParent().getId().equalsIgnoreCase("CreateDb") || scheduleModel.getParent().getId().equalsIgnoreCase("Pre/PostWork")) {
            File file = new File(scheduleModel.getName());
            if (file.exists()) {
                file.delete();
                TreeUtil.removeItem(treeItem);
                FileUtil.createXml();
            }
        } else {
            MessageDialog.openError(treeItem.getParent().getShell(), "Delete error", "Can not delete anything here!");
        }
    }
}
