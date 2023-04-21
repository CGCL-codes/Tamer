package ebiCRM.data.control;

import ebiCRM.gui.panels.EBICRMCompanyActivity;
import ebiCRM.utils.EBICRMHistoryDataUtil;
import ebiNeutrinoSDK.EBIPGFactory;
import ebiNeutrinoSDK.gui.dialogs.EBIChooserDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIExceptionDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIImageViewer;
import ebiNeutrinoSDK.gui.dialogs.EBIMessage;
import ebiNeutrinoSDK.model.hibernate.Company;
import ebiNeutrinoSDK.model.hibernate.Companyactivities;
import ebiNeutrinoSDK.model.hibernate.Companyactivitiesdocs;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class EBIDataControlActivity {

    private Companyactivities companyActivity = null;

    private EBICRMCompanyActivity activityPane = null;

    public EBIDataControlActivity(EBICRMCompanyActivity activityPane) {
        this.activityPane = activityPane;
        companyActivity = new Companyactivities();
    }

    public boolean dataStore(boolean isEdit) {
        try {
            activityPane.ebiModule.ebiContainer.showInActionStatus("Activity", activityPane.ebiModule.guiRenderer.getButton("saveActivity", "Activity"), true);
            if (activityPane.ebiModule.checkIslocked(activityPane.ebiModule.companyID, true)) {
                return false;
            }
            activityPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("EBICRM_SESSION").begin();
            if (isEdit == true) {
                createHistory(activityPane.ebiModule.ebiPGFactory.company);
                companyActivity.setChangeddate(new Date());
                companyActivity.setChangedfrom(EBIPGFactory.ebiUser);
            } else {
                companyActivity.setCreateddate(new Date());
                activityPane.isEdit = true;
            }
            companyActivity.setCreatedfrom(activityPane.ebiModule.guiRenderer.getVisualPanel("Activity").getCreatedFrom());
            companyActivity.setCompany(activityPane.ebiModule.ebiPGFactory.company);
            companyActivity.setActivityname(activityPane.ebiModule.guiRenderer.getTextfield("activityNameText", "Activity").getText());
            companyActivity.setActivitytype(activityPane.ebiModule.guiRenderer.getComboBox("activityTypeText", "Activity").getSelectedItem().toString());
            if (activityPane.ebiModule.guiRenderer.getComboBox("activityStatusText", "Activity").getSelectedIndex() != 0) {
                companyActivity.setActivitystatus(activityPane.ebiModule.guiRenderer.getComboBox("activityStatusText", "Activity").getSelectedItem().toString());
            }
            if (activityPane.ebiModule.guiRenderer.getTimepicker("activityTODOText", "Activity").getDate() != null) {
                Calendar eDate = new GregorianCalendar();
                eDate.setTime(activityPane.ebiModule.guiRenderer.getTimepicker("activityTODOText", "Activity").getDate());
                eDate.set(Calendar.HOUR_OF_DAY, Integer.parseInt(activityPane.ebiModule.guiRenderer.getSpinner("dueH", "Activity").getValue().toString()));
                eDate.set(Calendar.MINUTE, Integer.parseInt(activityPane.ebiModule.guiRenderer.getSpinner("dueMin", "Activity").getValue().toString()));
                eDate.set(Calendar.SECOND, 0);
                eDate.set(Calendar.MILLISECOND, 0);
                companyActivity.setDuedate(eDate.getTime());
            }
            companyActivity.setDuration(Integer.parseInt(activityPane.ebiModule.guiRenderer.getTextfield("durationText", "Activity").getText()));
            StringBuffer color = new StringBuffer();
            color.append(activityPane.ebiModule.guiRenderer.getPanel("colorPanel", "Activity").getBackground().getRed());
            color.append(",");
            color.append(activityPane.ebiModule.guiRenderer.getPanel("colorPanel", "Activity").getBackground().getGreen());
            color.append(",");
            color.append(activityPane.ebiModule.guiRenderer.getPanel("colorPanel", "Activity").getBackground().getBlue());
            companyActivity.setAcolor(color.toString());
            companyActivity.setActivitydescription(activityPane.ebiModule.guiRenderer.getTextarea("activityDescription", "Activity").getText());
            if (!companyActivity.getCompanyactivitiesdocses().isEmpty()) {
                Iterator iter = companyActivity.getCompanyactivitiesdocses().iterator();
                while (iter.hasNext()) {
                    Companyactivitiesdocs docs = (Companyactivitiesdocs) iter.next();
                    if (docs.getActivitydocid() < 0) {
                        docs.setActivitydocid(null);
                    }
                    activityPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("EBICRM_SESSION").saveOrUpdate(docs);
                }
            }
            activityPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("EBICRM_SESSION").saveOrUpdate(companyActivity);
            activityPane.ebiModule.ebiPGFactory.getDataStore("Activity", "ebiSave");
            activityPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("EBICRM_SESSION").commit();
            activityPane.ebiModule.ebiPGFactory.company.getCompanyactivitieses().add(companyActivity);
            dataShow(true);
            dataShowDoc();
            activityPane.ebiModule.ebiContainer.showInActionStatus("Activity", activityPane.ebiModule.guiRenderer.getButton("saveActivity", "Activity"), false);
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void dataEdit(int id) {
        dataNew();
        if (this.activityPane.ebiModule.ebiPGFactory.company.getCompanyactivitieses() == null) {
            EBIExceptionDialog.getInstance(EBIPGFactory.getLANG("EBI_LANG_C_RECORD_NOT_FOUND")).Show(EBIMessage.INFO_MESSAGE);
            return;
        }
        Iterator iter = this.activityPane.ebiModule.ebiPGFactory.company.getCompanyactivitieses().iterator();
        while (iter.hasNext()) {
            this.companyActivity = (Companyactivities) iter.next();
            if (this.companyActivity.getActivityid() == id) {
                activityPane.ebiModule.guiRenderer.getVisualPanel("Activity").setCreatedDate(activityPane.ebiModule.ebiPGFactory.getDateToString(companyActivity.getCreateddate()));
                activityPane.ebiModule.guiRenderer.getVisualPanel("Activity").setCreatedFrom(companyActivity.getCreatedfrom());
                if (companyActivity.getChangeddate() != null) {
                    activityPane.ebiModule.guiRenderer.getVisualPanel("Activity").setChangedDate(activityPane.ebiModule.ebiPGFactory.getDateToString(companyActivity.getChangeddate()));
                    activityPane.ebiModule.guiRenderer.getVisualPanel("Activity").setChangedFrom(companyActivity.getChangedfrom());
                } else {
                    activityPane.ebiModule.guiRenderer.getVisualPanel("Activity").setChangedDate("");
                    activityPane.ebiModule.guiRenderer.getVisualPanel("Activity").setChangedFrom("");
                }
                activityPane.ebiModule.guiRenderer.getTextfield("activityNameText", "Activity").setText(companyActivity.getActivityname());
                activityPane.ebiModule.guiRenderer.getTimepicker("activityTODOText", "Activity").updateUI();
                activityPane.ebiModule.guiRenderer.getTimepicker("activityTODOText", "Activity").setFormats(EBIPGFactory.DateFormat);
                activityPane.ebiModule.guiRenderer.getTimepicker("activityTODOText", "Activity").setDate(companyActivity.getDuedate());
                activityPane.ebiModule.guiRenderer.getTextfield("durationText", "Activity").setText(String.valueOf(companyActivity.getDuration()));
                int r;
                int g;
                int b;
                String[] splCol = companyActivity.getAcolor().split(",");
                r = Integer.parseInt(splCol[0]);
                g = Integer.parseInt(splCol[1]);
                b = Integer.parseInt(splCol[2]);
                activityPane.ebiModule.guiRenderer.getPanel("colorPanel", "Activity").setBackground(new Color(r, g, b));
                GregorianCalendar startDate = new GregorianCalendar();
                startDate.setTime(companyActivity.getDuedate());
                startDate.set(Calendar.SECOND, 0);
                startDate.set(Calendar.MILLISECOND, 0);
                activityPane.ebiModule.guiRenderer.getSpinner("dueH", "Activity").setValue(startDate.get(Calendar.HOUR_OF_DAY));
                activityPane.ebiModule.guiRenderer.getSpinner("dueMin", "Activity").setValue(startDate.get(Calendar.MINUTE));
                if (companyActivity.getActivitytype() != null) {
                    activityPane.ebiModule.guiRenderer.getComboBox("activityTypeText", "Activity").setSelectedItem(companyActivity.getActivitytype());
                }
                if (companyActivity.getActivitystatus() != null) {
                    activityPane.ebiModule.guiRenderer.getComboBox("activityStatusText", "Activity").setSelectedItem(companyActivity.getActivitystatus());
                }
                activityPane.ebiModule.guiRenderer.getTextarea("activityDescription", "Activity").setText(companyActivity.getActivitydescription());
                try {
                    activityPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("EBICRM_SESSION").begin();
                    activityPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("EBICRM_SESSION").refresh(companyActivity);
                    activityPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("EBICRM_SESSION").commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                activityPane.ebiModule.ebiPGFactory.getDataStore("Activity", "ebiEdit");
                this.dataShowDoc();
                break;
            }
        }
    }

    public void dataDelete(int id) {
        try {
            if (activityPane.ebiModule.checkIslocked(activityPane.ebiModule.companyID, true)) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Iterator iter = activityPane.ebiModule.ebiPGFactory.company.getCompanyactivitieses().iterator();
        while (iter.hasNext()) {
            Companyactivities act = (Companyactivities) iter.next();
            if (act.getActivityid() == id) {
                try {
                    activityPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("EBICRM_SESSION").begin();
                    activityPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("EBICRM_SESSION").delete(act);
                    activityPane.ebiModule.ebiPGFactory.getDataStore("Activity", "ebiDelete");
                    activityPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("EBICRM_SESSION").commit();
                } catch (HibernateException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                activityPane.ebiModule.ebiPGFactory.company.getCompanyactivitieses().remove(act);
                this.dataShow(false);
                break;
            }
        }
        dataNew();
    }

    public void dataShow(boolean reload) {
        try {
            boolean isEmpty = false;
            Iterator itr;
            int srow = activityPane.ebiModule.guiRenderer.getTable("tableActivity", "Activity").getSelectedRow();
            if (!reload) {
                itr = activityPane.ebiModule.ebiPGFactory.company.getCompanyactivitieses().iterator();
                if (activityPane.ebiModule.ebiPGFactory.company.getCompanyactivitieses().size() == 0) {
                    isEmpty = true;
                }
                activityPane.tabModel.data = new Object[activityPane.ebiModule.ebiPGFactory.company.getCompanyactivitieses().size()][8];
            } else {
                Query query = activityPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("EBICRM_SESSION").createQuery("from Companyactivities as act where act.company.companyid=? ").setInteger(0, activityPane.ebiModule.companyID);
                if (activityPane.ebiModule.ebiPGFactory.company != null) {
                    activityPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("EBICRM_SESSION").begin();
                    activityPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("EBICRM_SESSION").refresh(activityPane.ebiModule.ebiPGFactory.company);
                    activityPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("EBICRM_SESSION").commit();
                }
                itr = query.iterate();
                if (query.list().size() == 0) {
                    isEmpty = true;
                }
                activityPane.tabModel.data = new Object[query.list().size()][8];
            }
            if (!isEmpty) {
                int i = 0;
                while (itr.hasNext()) {
                    Companyactivities obj = (Companyactivities) itr.next();
                    if (reload) {
                        activityPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("EBICRM_SESSION").begin();
                        activityPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("EBICRM_SESSION").refresh(obj);
                        activityPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("EBICRM_SESSION").commit();
                    }
                    activityPane.tabModel.data[i][0] = obj.getActivityname();
                    activityPane.tabModel.data[i][1] = obj.getActivitytype() == null ? "" : obj.getActivitytype();
                    GregorianCalendar startDate = new GregorianCalendar();
                    startDate.setTime(obj.getDuedate());
                    startDate.set(Calendar.SECOND, 0);
                    startDate.set(Calendar.MILLISECOND, 0);
                    String min;
                    if (startDate.get(Calendar.MINUTE) < 10) {
                        min = startDate.get(Calendar.MINUTE) + "0";
                    } else {
                        min = startDate.get(Calendar.MINUTE) + "";
                    }
                    String hour;
                    if (startDate.get(Calendar.HOUR_OF_DAY) < 10) {
                        hour = "0" + startDate.get(Calendar.HOUR_OF_DAY);
                    } else {
                        hour = "" + startDate.get(Calendar.HOUR_OF_DAY);
                    }
                    activityPane.tabModel.data[i][2] = obj.getDuedate() == null ? "" : (activityPane.ebiModule.ebiPGFactory.getDateToString(obj.getDuedate()) + " " + hour + ":" + min);
                    activityPane.tabModel.data[i][3] = obj.getDuration() == null ? "" : String.valueOf(obj.getDuration());
                    activityPane.tabModel.data[i][4] = obj.getAcolor() == null ? "" : obj.getAcolor();
                    activityPane.tabModel.data[i][5] = obj.getActivitystatus() == null ? "" : obj.getActivitystatus();
                    activityPane.tabModel.data[i][6] = obj.getActivitydescription() == null ? "" : obj.getActivitydescription();
                    activityPane.tabModel.data[i][7] = obj.getActivityid();
                    i++;
                }
                if (activityPane.ebiModule.calendarPane != null) {
                    activityPane.ebiModule.calendarPane.refresh();
                }
            } else {
                activityPane.tabModel.data = new Object[][] { { EBIPGFactory.getLANG("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", "" } };
            }
            activityPane.tabModel.fireTableDataChanged();
            activityPane.ebiModule.guiRenderer.getTable("tableActivity", "Activity").changeSelection(srow, 0, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createHistory(Company com) {
        List<String> list = new ArrayList<String>();
        list.add(EBIPGFactory.getLANG("EBI_LANG_ADDED") + ": " + activityPane.ebiModule.ebiPGFactory.getDateToString(companyActivity.getCreateddate()));
        list.add(EBIPGFactory.getLANG("EBI_LANG_ADDED_FROM") + ": " + companyActivity.getCreatedfrom());
        if (companyActivity.getChangeddate() != null) {
            list.add(EBIPGFactory.getLANG("EBI_LANG_CHANGED") + ": " + activityPane.ebiModule.ebiPGFactory.getDateToString(companyActivity.getChangeddate()));
            list.add(EBIPGFactory.getLANG("EBI_LANG_CHANGED_FROM") + ": " + companyActivity.getChangedfrom());
        }
        list.add(EBIPGFactory.getLANG("EBI_LANG_NAME") + ": " + (companyActivity.getActivityname().equals(activityPane.ebiModule.guiRenderer.getTextfield("activityNameText", "Activity").getText()) == true ? companyActivity.getActivityname() : companyActivity.getActivityname() + "$"));
        GregorianCalendar startDate = new GregorianCalendar();
        startDate.setTime(companyActivity.getDuedate());
        startDate.set(Calendar.SECOND, 0);
        startDate.set(Calendar.MILLISECOND, 0);
        String min;
        if (startDate.get(Calendar.MINUTE) < 10) {
            min = startDate.get(Calendar.MINUTE) + "0";
        } else {
            min = startDate.get(Calendar.MINUTE) + "";
        }
        String hour;
        if (startDate.get(Calendar.HOUR_OF_DAY) < 10) {
            hour = "0" + startDate.get(Calendar.HOUR_OF_DAY);
        } else {
            hour = "" + startDate.get(Calendar.HOUR_OF_DAY);
        }
        list.add(EBIPGFactory.getLANG("EBI_LANG_DUE_DATE") + ": " + (activityPane.ebiModule.ebiPGFactory.getDateToString(companyActivity.getDuedate()).equals(activityPane.ebiModule.guiRenderer.getTimepicker("activityTODOText", "Activity").getEditor().getText()) == true ? activityPane.ebiModule.ebiPGFactory.getDateToString(companyActivity.getDuedate()) + hour + ":" + min : activityPane.ebiModule.ebiPGFactory.getDateToString(companyActivity.getDuedate()) + hour + ":" + min + "$"));
        list.add(EBIPGFactory.getLANG("EBI_LANG_C_DESCRIPTION") + ": " + (companyActivity.getActivitydescription().equals(activityPane.ebiModule.guiRenderer.getTextarea("activityDescription", "Activity").getText()) == true ? companyActivity.getActivitydescription() : companyActivity.getActivitydescription() + "$"));
        if (companyActivity.getActivitystatus() != null) {
            list.add(EBIPGFactory.getLANG("EBI_LANG_C_STATUS") + ": " + (companyActivity.getActivitystatus().equals(activityPane.ebiModule.guiRenderer.getComboBox("activityStatusText", "Activity").getSelectedItem().toString()) == true ? companyActivity.getActivitystatus() : companyActivity.getActivitystatus() + "$"));
        }
        if (companyActivity.getActivitytype() != null) {
            list.add(EBIPGFactory.getLANG("EBI_LANG_TYPE") + ": " + (companyActivity.getActivitytype().equals(activityPane.ebiModule.guiRenderer.getComboBox("activityTypeText", "Activity").getSelectedItem().toString()) == true ? companyActivity.getActivitytype() : companyActivity.getActivitytype() + "$"));
        }
        list.add(EBIPGFactory.getLANG("EBI_LANG_DURATION") + ": " + (("" + companyActivity.getDuration()).equals(activityPane.ebiModule.guiRenderer.getTextfield("durationText", "Activity").getText()) == true ? companyActivity.getDuration() : companyActivity.getDuration() + "$"));
        list.add("*EOR*");
        if (!companyActivity.getCompanyactivitiesdocses().isEmpty()) {
            Iterator iter = companyActivity.getCompanyactivitiesdocses().iterator();
            while (iter.hasNext()) {
                Companyactivitiesdocs obj = (Companyactivitiesdocs) iter.next();
                list.add(obj.getName() == null ? EBIPGFactory.getLANG("EBI_LANG_FILENAME") + ": " : EBIPGFactory.getLANG("EBI_LANG_FILENAME") + ": " + obj.getName());
                list.add(activityPane.ebiModule.ebiPGFactory.getDateToString(obj.getCreateddate()) == null ? EBIPGFactory.getLANG("EBI_LANG_C_ADDED_DATE") + ": " : EBIPGFactory.getLANG("EBI_LANG_C_ADDED_DATE") + ": " + activityPane.ebiModule.ebiPGFactory.getDateToString(obj.getCreateddate()));
                list.add(obj.getCreatedfrom() == null ? EBIPGFactory.getLANG("EBI_LANG_ADDED_FROM") + ": " : EBIPGFactory.getLANG("EBI_LANG_ADDED_FROM") + ": " + obj.getCreatedfrom());
                list.add("*EOR*");
            }
        }
        try {
            activityPane.ebiModule.hcreator.setDataToCreate(new EBICRMHistoryDataUtil(com.getCompanyid(), "Activities", list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dataNew() {
        this.companyActivity = new Companyactivities();
        activityPane.tabActDoc.data = new Object[][] { { EBIPGFactory.getLANG("EBI_LANG_PLEASE_SELECT"), "", "", "" } };
        activityPane.tabActDoc.fireTableDataChanged();
        activityPane.ebiModule.guiRenderer.getVisualPanel("Activity").setCreatedDate(activityPane.ebiModule.ebiPGFactory.getDateToString(new java.util.Date()));
        activityPane.ebiModule.guiRenderer.getVisualPanel("Activity").setCreatedFrom(EBIPGFactory.ebiUser);
        activityPane.ebiModule.guiRenderer.getVisualPanel("Activity").setChangedDate("");
        activityPane.ebiModule.guiRenderer.getVisualPanel("Activity").setChangedFrom("");
        activityPane.ebiModule.guiRenderer.getTextfield("activityNameText", "Activity").setText("");
        activityPane.ebiModule.guiRenderer.getComboBox("activityTypeText", "Activity").setSelectedIndex(0);
        activityPane.ebiModule.guiRenderer.getComboBox("activityStatusText", "Activity").setSelectedIndex(0);
        activityPane.ebiModule.guiRenderer.getTimepicker("activityTODOText", "Activity").setDate(new java.util.Date());
        activityPane.ebiModule.guiRenderer.getTimepicker("activityTODOText", "Activity").getEditor().setText("");
        activityPane.ebiModule.guiRenderer.getTextarea("activityDescription", "Activity").setText("");
        activityPane.ebiModule.guiRenderer.getSpinner("dueH", "Activity").setValue(0);
        activityPane.ebiModule.guiRenderer.getSpinner("dueMin", "Activity").setValue(0);
        activityPane.ebiModule.guiRenderer.getTextfield("durationText", "Activity").setText("");
        activityPane.ebiModule.guiRenderer.getPanel("colorPanel", "Activity").setBackground(new Color(5, 125, 255));
        activityPane.ebiModule.guiRenderer.getTable("tableActivity", "Activity").setOpaque(true);
        activityPane.ebiModule.ebiPGFactory.getDataStore("Activity", "ebiNew");
    }

    public void dataDeleteDoc(int id) {
        try {
            if (activityPane.ebiModule.checkIslocked(activityPane.ebiModule.companyID, true)) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Iterator iter = this.companyActivity.getCompanyactivitiesdocses().iterator();
        while (iter.hasNext()) {
            Companyactivitiesdocs doc = (Companyactivitiesdocs) iter.next();
            if (id == doc.getActivitydocid()) {
                if (id >= 0) {
                    try {
                        activityPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("EBICRM_SESSION").begin();
                        activityPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("EBICRM_SESSION").delete(doc);
                        activityPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("EBICRM_SESSION").commit();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                this.companyActivity.getCompanyactivitiesdocses().remove(doc);
                this.dataShowDoc();
                break;
            }
        }
    }

    public void dataShowDoc() {
        if (this.companyActivity.getCompanyactivitiesdocses().size() > 0) {
            activityPane.tabActDoc.data = new Object[this.companyActivity.getCompanyactivitiesdocses().size()][4];
            Iterator itr = this.companyActivity.getCompanyactivitiesdocses().iterator();
            int i = 0;
            while (itr.hasNext()) {
                Companyactivitiesdocs obj = (Companyactivitiesdocs) itr.next();
                activityPane.tabActDoc.data[i][0] = obj.getName() == null ? "" : obj.getName();
                activityPane.tabActDoc.data[i][1] = activityPane.ebiModule.ebiPGFactory.getDateToString(obj.getCreateddate()) == null ? "" : activityPane.ebiModule.ebiPGFactory.getDateToString(obj.getCreateddate());
                activityPane.tabActDoc.data[i][2] = obj.getCreatedfrom() == null ? "" : obj.getCreatedfrom();
                if (obj.getActivitydocid() == null || obj.getActivitydocid() < 0) {
                    obj.setActivitydocid(((i + 1) * (-1)));
                }
                activityPane.tabActDoc.data[i][3] = obj.getActivitydocid();
                i++;
            }
        } else {
            activityPane.tabActDoc.data = new Object[][] { { EBIPGFactory.getLANG("EBI_LANG_PLEASE_SELECT"), "", "" } };
        }
        activityPane.tabActDoc.fireTableDataChanged();
    }

    public void dataViewDoc(int id) {
        String FileName;
        String FileType;
        OutputStream fos;
        try {
            Iterator iter = this.companyActivity.getCompanyactivitiesdocses().iterator();
            while (iter.hasNext()) {
                Companyactivitiesdocs doc = (Companyactivitiesdocs) iter.next();
                if (id == doc.getActivitydocid()) {
                    String file = doc.getName().replaceAll(" ", "_");
                    byte buffer[] = doc.getFiles();
                    FileName = "tmp/" + file;
                    FileType = file.substring(file.lastIndexOf("."));
                    fos = new FileOutputStream(FileName);
                    fos.write(buffer, 0, buffer.length);
                    fos.close();
                    resolverType(FileName, FileType);
                    break;
                }
            }
        } catch (FileNotFoundException exx) {
            EBIExceptionDialog.getInstance(EBIPGFactory.getLANG("EBI_LANG_ERROR_FILE_NOT_FOUND")).Show(EBIMessage.INFO_MESSAGE);
        } catch (IOException exx1) {
            EBIExceptionDialog.getInstance(EBIPGFactory.getLANG("EBI_LANG_ERROR_LOADING_FILE")).Show(EBIMessage.INFO_MESSAGE);
        }
    }

    public void dataNewDoc() {
        try {
            if (activityPane.ebiModule.checkIslocked(activityPane.ebiModule.companyID, true)) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        EBIChooserDialog fc = new EBIChooserDialog();
        fc.fc.setFileSelectionMode(fc.fc.FILES_ONLY);
        fc.setTitle(EBIPGFactory.getLANG("EBI_LANG_SELECT_FILE"));
        fc.setVisible(true);
        if (fc.FileName != null) {
            File selFile = fc.FileName;
            byte[] file = readFileToByte(selFile);
            if (file != null) {
                Companyactivitiesdocs docs = new Companyactivitiesdocs();
                docs.setCompanyactivities(this.companyActivity);
                docs.setName(selFile.getName());
                docs.setCreateddate(new java.sql.Date(new java.util.Date().getTime()));
                docs.setCreatedfrom(EBIPGFactory.ebiUser);
                docs.setFiles(file);
                this.companyActivity.getCompanyactivitiesdocses().add(docs);
                this.dataShowDoc();
            } else {
                EBIExceptionDialog.getInstance(EBIPGFactory.getLANG("EBI_LANG_ERROR_FILE_CANNOT_READING")).Show(EBIMessage.ERROR_MESSAGE);
                return;
            }
        }
    }

    private byte[] readFileToByte(File selFile) {
        InputStream st = readFileGetBlob(selFile);
        byte inBuf[];
        try {
            int inBytes = st.available();
            inBuf = new byte[inBytes];
            st.read(inBuf, 0, inBytes);
        } catch (java.io.IOException ex) {
            return null;
        }
        return inBuf;
    }

    private InputStream readFileGetBlob(File file) {
        InputStream is;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException ex) {
            EBIExceptionDialog.getInstance(EBIPGFactory.getLANG("EBI_LANG_ERROR_FILE_NOT_FOUND")).Show(EBIMessage.INFO_MESSAGE);
            return null;
        }
        return is;
    }

    private void resolverType(String fileName, String type) {
        if (".jpg".equals(type) || ".gif".equals(type) || ".png".equals(type)) {
            EBIImageViewer view = new EBIImageViewer(new javax.swing.ImageIcon(fileName));
            view.setVisible(true);
        } else if (".pdf".equals(type)) {
            activityPane.ebiModule.ebiPGFactory.openPDFReportFile(fileName);
        } else if (".doc".equals(type)) {
            activityPane.ebiModule.ebiPGFactory.openTextDocumentFile(fileName);
        } else {
            activityPane.ebiModule.ebiPGFactory.openTextDocumentFile(fileName);
        }
    }

    public Set<Companyactivities> getActivitiesesList() {
        return activityPane.ebiModule.ebiPGFactory.company.getCompanyactivitieses();
    }

    public Set<Companyactivitiesdocs> getActivitiesDocList() {
        return companyActivity.getCompanyactivitiesdocses();
    }

    public Companyactivities getCompanyActivity() {
        return companyActivity;
    }

    public void setCompanyActivity(Companyactivities companyActivity) {
        this.companyActivity = companyActivity;
    }
}
