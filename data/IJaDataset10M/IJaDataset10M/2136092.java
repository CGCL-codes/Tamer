package ebiCRM.data.control;

import ebiCRM.gui.dialogs.EBICRMAddContactAddressType;
import ebiCRM.gui.dialogs.EBIDialogProperties;
import ebiCRM.gui.panels.EBICRMCampaign;
import ebiCRM.utils.EBICRMHistoryDataUtil;
import ebiNeutrinoSDK.EBIOutlookToNeutrino;
import ebiNeutrinoSDK.EBIPGFactory;
import ebiNeutrinoSDK.gui.component.EBINeutrinoEMailFunction;
import ebiNeutrinoSDK.gui.dialogs.EBIChooserDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIExceptionDialog;
import ebiNeutrinoSDK.gui.dialogs.EBIImageViewer;
import ebiNeutrinoSDK.gui.dialogs.EBIMessage;
import ebiNeutrinoSDK.model.hibernate.*;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import java.awt.*;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

public class EBIDataControlCampaign {

    public Crmcampaign campaign = null;

    private EBICRMCampaign campaignPane = null;

    public int id = 0;

    public Timestamp lockTime = null;

    public String lockUser = "";

    public String lockModuleName = "";

    public int lockStatus = 0;

    public int lockId = -1;

    public EBIDataControlCampaign(EBICRMCampaign campaignPane) {
        campaign = new Crmcampaign();
        this.campaignPane = campaignPane;
    }

    public boolean dataStore(boolean isEdit) {
        try {
            campaignPane.ebiModule.ebiContainer.showInActionStatus("Campaign", campaignPane.ebiModule.guiRenderer.getButton("saveCampagin", "Campaign"), true);
            if (id != -1) {
                if (checkIslocked(id, true)) {
                    return false;
                }
            }
            campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("CAMPAIGN_SESSION").begin();
            if (isEdit == false) {
                this.campaign.setCreateddate(new Date());
                campaignPane.isEdit = true;
            } else {
                Query query = campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("CAMPAIGN_SESSION").createQuery("from Crmcampaign where campaignid=? ").setInteger(0, id);
                Crmcampaign crmCmp;
                Iterator iter = query.iterate();
                if (iter.hasNext()) {
                    crmCmp = (Crmcampaign) iter.next();
                    campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("CAMPAIGN_SESSION").refresh(crmCmp);
                    createHistory(crmCmp);
                }
                this.campaign.setChangeddate(new Date());
                this.campaign.setChangedfrom(EBIPGFactory.ebiUser);
            }
            this.campaign.setCreatedfrom(campaignPane.ebiModule.guiRenderer.getVisualPanel("Campaign").getCreatedFrom());
            campaign.setValidfrom(campaignPane.ebiModule.guiRenderer.getTimepicker("campaignValidFromText", "Campaign").getDate());
            campaign.setValidto(campaignPane.ebiModule.guiRenderer.getTimepicker("campaingValidToText", "Campaign").getDate());
            campaign.setName(campaignPane.ebiModule.guiRenderer.getTextfield("CampaignNameText", "Campaign").getText());
            campaign.setCampaignnr(campaignPane.ebiModule.guiRenderer.getTextfield("CampaignNrTex", "Campaign").getText());
            campaign.setStatus(campaignPane.ebiModule.guiRenderer.getComboBox("CampaignStatusText", "Campaign").getSelectedItem().toString());
            if (!campaign.getCrmcampaignprops().isEmpty()) {
                Iterator itrpr = campaign.getCrmcampaignprops().iterator();
                while (itrpr.hasNext()) {
                    Crmcampaignprop crmprops = (Crmcampaignprop) itrpr.next();
                    crmprops.setCrmcampaign(campaign);
                    if (crmprops.getPropertiesid() < 0) {
                        crmprops.setPropertiesid(null);
                    }
                    campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("CAMPAIGN_SESSION").saveOrUpdate(crmprops);
                }
            }
            if (!campaign.getCrmcampaigndocses().isEmpty()) {
                Iterator itrpr = campaign.getCrmcampaigndocses().iterator();
                while (itrpr.hasNext()) {
                    Crmcampaigndocs crmdocs = (Crmcampaigndocs) itrpr.next();
                    crmdocs.setCrmcampaign(campaign);
                    if (crmdocs.getDocid() < 0) {
                        crmdocs.setDocid(null);
                    }
                    campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("CAMPAIGN_SESSION").saveOrUpdate(crmdocs);
                }
            }
            if (!campaign.getCrmcampaignpositions().isEmpty()) {
                Iterator itrpr = campaign.getCrmcampaignpositions().iterator();
                while (itrpr.hasNext()) {
                    Crmcampaignposition crmpos = (Crmcampaignposition) itrpr.next();
                    crmpos.setCrmcampaign(campaign);
                    if (crmpos.getPositionid() < 0) {
                        crmpos.setPositionid(null);
                    }
                    campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("CAMPAIGN_SESSION").saveOrUpdate(crmpos);
                }
            }
            if (!campaign.getCrmcampaignreceivers().isEmpty()) {
                Iterator itrpr = campaign.getCrmcampaignreceivers().iterator();
                while (itrpr.hasNext()) {
                    Crmcampaignreceiver crmrec = (Crmcampaignreceiver) itrpr.next();
                    crmrec.setCrmcampaign(campaign);
                    if (crmrec.getReceiverid() < 0) {
                        crmrec.setReceiverid(null);
                    }
                    campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("CAMPAIGN_SESSION").saveOrUpdate(crmrec);
                }
            }
            campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("CAMPAIGN_SESSION").saveOrUpdate(campaign);
            campaignPane.ebiModule.ebiPGFactory.getDataStore("Campaign", "ebiSave");
            campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("CAMPAIGN_SESSION").commit();
            this.dataShow();
            dataShowDoc();
            dataShowProduct();
            dataShowProperties();
            dataShowReciever();
            campaignPane.ebiModule.ebiContainer.showInActionStatus("Campaign", campaignPane.ebiModule.guiRenderer.getButton("saveCampagin", "Campaign"), false);
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void dataEdit(int id) {
        campaignPane.ebiModule.guiRenderer.getVisualPanel("Campaign").setCursor(new Cursor(Cursor.WAIT_CURSOR));
        dataNew();
        Query query;
        try {
            campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("CAMPAIGN_SESSION").begin();
            query = campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("CAMPAIGN_SESSION").createQuery("from Crmcampaign where campaignid=? ").setInteger(0, id);
            Iterator iter = query.iterate();
            if (iter.hasNext()) {
                this.id = id;
                campaignPane.ebiModule.viewDialog.setLockId(id);
                campaignPane.ebiModule.viewDialog.setModuleName("CRMCampaign");
                this.campaign = (Crmcampaign) iter.next();
                campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("CAMPAIGN_SESSION").refresh(this.campaign);
                campaignPane.ebiModule.guiRenderer.getVisualPanel("Campaign").setCreatedDate(campaignPane.ebiModule.ebiPGFactory.getDateToString(campaign.getCreateddate()));
                campaignPane.ebiModule.guiRenderer.getVisualPanel("Campaign").setCreatedFrom(campaign.getCreatedfrom());
                campaignPane.ebiModule.guiRenderer.getTimepicker("campaignValidFromText", "Campaign").updateUI();
                campaignPane.ebiModule.guiRenderer.getTimepicker("campaignValidFromText", "Campaign").setDate(campaign.getValidfrom());
                campaignPane.ebiModule.guiRenderer.getTimepicker("campaignValidFromText", "Campaign").setFormats(EBIPGFactory.DateFormat);
                campaignPane.ebiModule.guiRenderer.getTimepicker("campaingValidToText", "Campaign").updateUI();
                campaignPane.ebiModule.guiRenderer.getTimepicker("campaingValidToText", "Campaign").setDate(campaign.getValidto());
                campaignPane.ebiModule.guiRenderer.getTimepicker("campaingValidToText", "Campaign").setFormats(EBIPGFactory.DateFormat);
                if (this.campaign.getChangeddate() != null) {
                    campaignPane.ebiModule.guiRenderer.getVisualPanel("Campaign").setChangedDate(campaignPane.ebiModule.ebiPGFactory.getDateToString(campaign.getChangeddate()));
                    campaignPane.ebiModule.guiRenderer.getVisualPanel("Campaign").setChangedFrom(campaign.getChangedfrom());
                } else {
                    campaignPane.ebiModule.guiRenderer.getVisualPanel("Campaign").setChangedDate("");
                    campaignPane.ebiModule.guiRenderer.getVisualPanel("Campaign").setChangedFrom("");
                }
                campaignPane.ebiModule.guiRenderer.getTextfield("CampaignNrTex", "Campaign").setText(campaign.getCampaignnr());
                campaignPane.ebiModule.guiRenderer.getTextfield("CampaignNameText", "Campaign").setText(campaign.getName());
                campaignPane.ebiModule.guiRenderer.getComboBox("CampaignStatusText", "Campaign").setSelectedItem(campaign.getStatus());
                campaignPane.ebiModule.ebiPGFactory.getDataStore("Campaign", "ebiEdit");
                campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("CAMPAIGN_SESSION").commit();
                this.dataShowDoc();
                this.dataShowProduct();
                this.dataShowProperties();
                this.dataShowReciever();
                checkIslocked(id, false);
            } else {
                EBIExceptionDialog.getInstance(EBIPGFactory.getLANG("EBI_LANG_C_RECORD_NOT_FOUND")).Show(EBIMessage.INFO_MESSAGE);
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            campaignPane.ebiModule.guiRenderer.getVisualPanel("Campaign").setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    public void dataDelete(int id) {
        dataNew();
        try {
            campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("CAMPAIGN_SESSION").begin();
            Query query = campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("CAMPAIGN_SESSION").createQuery("from Crmcampaign where campaignid=? ").setInteger(0, id);
            Iterator iter = query.iterate();
            if (iter.hasNext()) {
                Crmcampaign cam = (Crmcampaign) iter.next();
                campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("CAMPAIGN_SESSION").delete(cam);
                campaignPane.ebiModule.ebiPGFactory.getDataStore("Campaign", "ebiDelete");
                campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("CAMPAIGN_SESSION").commit();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        dataShow();
    }

    public void dataShow() {
        ResultSet set = null;
        int srow = campaignPane.ebiModule.guiRenderer.getTable("companyCampaignTable", "Campaign").getSelectedRow();
        try {
            PreparedStatement ps1 = campaignPane.ebiModule.ebiPGFactory.getIEBIDatabase().initPreparedStatement("SELECT * FROM CRMCAMPAIGN");
            set = campaignPane.ebiModule.ebiPGFactory.getIEBIDatabase().executePreparedQuery(ps1);
            if (set != null) {
                set.last();
                campaignPane.tabModelCampaign.data = new Object[set.getRow()][5];
                if (set.getRow() > 0) {
                    set.beforeFirst();
                    int i = 0;
                    while (set.next()) {
                        campaignPane.tabModelCampaign.data[i][0] = set.getString("NAME") == null ? "" : set.getString("NAME");
                        campaignPane.tabModelCampaign.data[i][1] = set.getString("STATUS") == null ? "" : set.getString("STATUS");
                        campaignPane.tabModelCampaign.data[i][2] = set.getDate("VALIDFROM") == null ? "" : campaignPane.ebiModule.ebiPGFactory.getDateToString(set.getDate("VALIDFROM"));
                        campaignPane.tabModelCampaign.data[i][3] = set.getDate("VALIDTO") == null ? "" : campaignPane.ebiModule.ebiPGFactory.getDateToString(set.getDate("VALIDTO"));
                        campaignPane.tabModelCampaign.data[i][4] = set.getString("CAMPAIGNID");
                        i++;
                    }
                } else {
                    campaignPane.tabModelCampaign.data = new Object[][] { { EBIPGFactory.getLANG("EBI_LANG_PLEASE_SELECT"), "", "", "" } };
                }
            } else {
                campaignPane.tabModelCampaign.data = new Object[][] { { EBIPGFactory.getLANG("EBI_LANG_PLEASE_SELECT"), "", "", "" } };
            }
            campaignPane.tabModelCampaign.fireTableDataChanged();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (set != null) {
                try {
                    set.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        campaignPane.ebiModule.guiRenderer.getTable("companyCampaignTable", "Campaign").changeSelection(srow, 0, false, false);
    }

    public String dataShowReport(int id, boolean showWindow) {
        String fileName = "";
        Query query;
        try {
            query = campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("CAMPAIGN_SESSION").createQuery("from Crmcampaign where campaignid=? ").setInteger(0, id);
            Iterator iter = query.iterate();
            while (iter.hasNext()) {
                Crmcampaign camp = (Crmcampaign) iter.next();
                campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("CAMPAIGN_SESSION").refresh(camp);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("ID", camp.getCampaignid());
                fileName = campaignPane.ebiModule.ebiPGFactory.getIEBIReportSystemInstance().useReportSystem(map, campaignPane.ebiModule.ebiPGFactory.convertReportCategoryToIndex(EBIPGFactory.getLANG("EBI_LANG_C_CAMPAIGN")), camp.getName(), showWindow);
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public void dataShowReport(int id) {
        Query query;
        try {
            query = campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("CAMPAIGN_SESSION").createQuery("from Crmcampaign where campaignid=? ").setInteger(0, id);
            Iterator iter = query.iterate();
            while (iter.hasNext()) {
                this.campaign = (Crmcampaign) iter.next();
                campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("CAMPAIGN_SESSION").refresh(this.campaign);
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("ID", this.campaign.getCampaignid());
                campaignPane.ebiModule.ebiPGFactory.getIEBIReportSystemInstance().useReportSystem(map, campaignPane.ebiModule.ebiPGFactory.convertReportCategoryToIndex(EBIPGFactory.getLANG("EBI_LANG_C_CAMPAIGN")), this.campaign.getName());
            }
        } catch (HibernateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dataNew() {
        campaignPane.ebiModule.guiRenderer.getVisualPanel("Campaign").setCreatedDate(campaignPane.ebiModule.ebiPGFactory.getDateToString(new java.util.Date()));
        campaignPane.ebiModule.guiRenderer.getVisualPanel("Campaign").setCreatedFrom(EBIPGFactory.ebiUser);
        campaignPane.ebiModule.guiRenderer.getVisualPanel("Campaign").setChangedDate("");
        campaignPane.ebiModule.guiRenderer.getVisualPanel("Campaign").setChangedFrom("");
        campaignPane.ebiModule.guiRenderer.getTextfield("CampaignNrTex", "Campaign").setText("");
        campaignPane.ebiModule.guiRenderer.getTextfield("CampaignNameText", "Campaign").setText("");
        campaignPane.ebiModule.guiRenderer.getComboBox("CampaignStatusText", "Campaign").setSelectedIndex(0);
        campaignPane.ebiModule.guiRenderer.getTimepicker("campaignValidFromText", "Campaign").getEditor().setText("");
        campaignPane.ebiModule.guiRenderer.getTimepicker("campaingValidToText", "Campaign").getEditor().setText("");
        campaignPane.ebiModule.unlockCompanyRecord(id, lockUser, "CRMCampaign");
        lockId = -1;
        lockModuleName = "";
        lockUser = "";
        lockStatus = 0;
        lockTime = null;
        id = -1;
        campaignPane.ebiModule.ebiPGFactory.getDataStore("Campaign", "ebiNew");
        campaign = new Crmcampaign();
        this.dataShowDoc();
        this.dataShowProduct();
        this.dataShowReciever();
        this.dataShowProperties();
    }

    public boolean mailCampaignTo(int id, String fileName) {
        EBINeutrinoEMailFunction mail = null;
        String subject = "";
        String bodyText = "";
        if (!EBIPGFactory.USE_OUTLOOK) {
            mail = campaignPane.ebiModule.ebiPGFactory.getEBIEMail();
            subject = campaignPane.ebiModule.guiRenderer.getTextfield("SubjectText", "sendEMailMessage").getText();
            bodyText = campaignPane.ebiModule.guiRenderer.getEditor("MessageAreaText", "sendEMailMessage").getText();
        }
        try {
            campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("CAMPAIGN_SESSION").begin();
            Query query = campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("CAMPAIGN_SESSION").createQuery("from Crmcampaign where campaignid=? ").setInteger(0, id);
            Iterator iter = query.iterate();
            if (iter.hasNext()) {
                Crmcampaign cam = (Crmcampaign) iter.next();
                campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("CAMPAIGN_SESSION").refresh(cam);
                if (cam.getCampaignid() == id) {
                    Iterator it = cam.getCrmcampaignreceivers().iterator();
                    boolean haveMoreRec = cam.getCrmcampaignreceivers().size() > 1 ? true : false;
                    if (cam.getCrmcampaignreceivers().size() == 0) {
                        EBIExceptionDialog.getInstance(EBIPGFactory.getLANG("EBI_LANG_NO_RECEIVER_WAS_FOUND")).Show(EBIMessage.ERROR_MESSAGE);
                        return false;
                    }
                    String to = "";
                    boolean noRecFound = false;
                    boolean isRecFound = false;
                    while (it.hasNext()) {
                        Crmcampaignreceiver rec = (Crmcampaignreceiver) it.next();
                        if (!EBIPGFactory.USE_OUTLOOK) {
                            if (haveMoreRec && rec.getCnum() == 1) {
                                mail.sendEMailMessage(rec.getEmail(), "", subject, bodyText, new String[] { fileName });
                                campaignPane.ebiModule.getstorangeVPanel().updateFolder();
                                noRecFound = false;
                                isRecFound = true;
                                break;
                            } else if (haveMoreRec && rec.getCnum() == 0 && !isRecFound) {
                                noRecFound = true;
                            }
                            if (!haveMoreRec) {
                                mail.sendEMailMessage(rec.getEmail(), "", subject, bodyText, new String[] { fileName });
                                campaignPane.ebiModule.getstorangeVPanel().updateFolder();
                                break;
                            }
                        } else {
                            if (!"".equals(to)) {
                                to += ";";
                            }
                            to += rec.getEmail();
                        }
                    }
                    if (!"".equals(to)) {
                        Hashtable<String, Object> obj = new Hashtable<String, Object>();
                        obj.put("To", to);
                        if (!"-1".equals(fileName)) {
                            obj.put("Attachments", fileName);
                        }
                        campaignPane.ebiModule.ebiPGFactory.getJCOBInstance().newItemFolderContents(EBIOutlookToNeutrino.INBOXNAME, obj);
                    }
                    if (noRecFound) {
                        EBIExceptionDialog.getInstance(EBIPGFactory.getLANG("EBI_LANG_NO_RECEIVER_IS_SET_MAIN_CANNOT_SEND")).Show(EBIMessage.INFO_MESSAGE);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            EBIExceptionDialog.getInstance(EBIPGFactory.printStackTrace(ex)).Show(EBIMessage.ERROR_MESSAGE);
        }
        return true;
    }

    public void dataShowReciever() {
        if (this.campaign.getCrmcampaignreceivers().size() > 0) {
            campaignPane.tabModReceiver.data = new Object[this.campaign.getCrmcampaignreceivers().size()][13];
            Iterator itr = this.campaign.getCrmcampaignreceivers().iterator();
            int i = 0;
            while (itr.hasNext()) {
                Crmcampaignreceiver obj = (Crmcampaignreceiver) itr.next();
                campaignPane.tabModReceiver.data[i][0] = obj.getReceivervia() == null ? "" : obj.getReceivervia();
                campaignPane.tabModReceiver.data[i][1] = obj.getCompanynumber() == null ? "" : obj.getCompanynumber();
                campaignPane.tabModReceiver.data[i][2] = obj.getCompanyname() == null ? "" : obj.getCompanyname();
                campaignPane.tabModReceiver.data[i][3] = obj.getGender() == null ? "" : obj.getGender();
                campaignPane.tabModReceiver.data[i][4] = obj.getSurname() == null ? "" : obj.getSurname();
                campaignPane.tabModReceiver.data[i][5] = obj.getName() == null ? "" : obj.getName();
                campaignPane.tabModReceiver.data[i][6] = obj.getPosition() == null ? "" : obj.getPosition();
                campaignPane.tabModReceiver.data[i][7] = obj.getStreet() == null ? "" : obj.getStreet();
                campaignPane.tabModReceiver.data[i][8] = obj.getZip() == null ? "" : obj.getZip();
                campaignPane.tabModReceiver.data[i][9] = obj.getLocation() == null ? "" : obj.getLocation();
                campaignPane.tabModReceiver.data[i][10] = obj.getPbox() == null ? "" : obj.getPbox();
                campaignPane.tabModReceiver.data[i][11] = obj.getCountry() == null ? "" : obj.getCountry();
                if (obj.getReceiverid() == null || obj.getReceiverid() < 0) {
                    obj.setReceiverid(((i + 1) * (-1)));
                }
                campaignPane.tabModReceiver.data[i][12] = obj.getReceiverid();
                i++;
            }
        } else {
            campaignPane.tabModReceiver.data = new Object[][] { { EBIPGFactory.getLANG("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", "", "", "", "", "", "" } };
        }
        campaignPane.tabModReceiver.fireTableDataChanged();
    }

    public void dataShowProduct() {
        if (this.campaign.getCrmcampaignpositions().size() > 0) {
            campaignPane.tabModProduct.data = new Object[this.campaign.getCrmcampaignpositions().size()][9];
            Iterator itr = this.campaign.getCrmcampaignpositions().iterator();
            int i = 0;
            NumberFormat currency = NumberFormat.getCurrencyInstance();
            while (itr.hasNext()) {
                Crmcampaignposition obj = (Crmcampaignposition) itr.next();
                campaignPane.tabModProduct.data[i][0] = obj.getQuantity();
                campaignPane.tabModProduct.data[i][1] = obj.getProductnr();
                campaignPane.tabModProduct.data[i][2] = obj.getProductname() == null ? "" : obj.getProductname();
                campaignPane.tabModProduct.data[i][3] = obj.getCategory() == null ? "" : obj.getCategory();
                campaignPane.tabModProduct.data[i][4] = obj.getTaxtype() == null ? "" : obj.getTaxtype();
                campaignPane.tabModProduct.data[i][5] = currency.format(campaignPane.ebiModule.dynMethod.calculatePreTaxPrice(obj.getNetamount(), String.valueOf(obj.getQuantity()), String.valueOf(obj.getDeduction()))) == null ? "" : currency.format(campaignPane.ebiModule.dynMethod.calculatePreTaxPrice(obj.getNetamount(), String.valueOf(obj.getQuantity()), String.valueOf(obj.getDeduction())));
                campaignPane.tabModProduct.data[i][6] = obj.getDeduction().equals("") ? "" : obj.getDeduction() + "%";
                campaignPane.tabModProduct.data[i][7] = obj.getDescription() == null ? "" : obj.getDescription();
                if (obj.getPositionid() == null || obj.getPositionid() < 0) {
                    obj.setPositionid(((i + 1) * (-1)));
                }
                campaignPane.tabModProduct.data[i][8] = obj.getPositionid();
                i++;
            }
        } else {
            campaignPane.tabModProduct.data = new Object[][] { { EBIPGFactory.getLANG("EBI_LANG_PLEASE_SELECT"), "", "", "", "", "", "", "" } };
        }
        campaignPane.tabModProduct.fireTableDataChanged();
    }

    private void createHistory(Crmcampaign com) {
        List<String> list = new ArrayList<String>();
        list.add(EBIPGFactory.getLANG("EBI_LANG_ADDED") + ": " + campaignPane.ebiModule.ebiPGFactory.getDateToString(campaign.getCreateddate()));
        list.add(EBIPGFactory.getLANG("EBI_LANG_ADDED_FROM") + ": " + campaign.getCreatedfrom());
        if (campaign.getChangeddate() != null) {
            list.add(EBIPGFactory.getLANG("EBI_LANG_CHANGED") + ": " + campaignPane.ebiModule.ebiPGFactory.getDateToString(campaign.getChangeddate()));
            list.add(EBIPGFactory.getLANG("EBI_LANG_CHANGED_FROM") + ": " + campaign.getChangedfrom());
        }
        list.add(EBIPGFactory.getLANG("EBI_LANG_C_CAMPAIGN_NR") + ": " + (campaign.getCampaignnr().equals(campaignPane.ebiModule.guiRenderer.getTextfield("CampaignNrTex", "Campaign").getText()) == true ? campaign.getCampaignnr() : campaign.getCampaignnr() + "$"));
        list.add(EBIPGFactory.getLANG("EBI_LANG_VALID_FROM") + ": " + (campaignPane.ebiModule.ebiPGFactory.getDateToString(campaign.getValidfrom()).equals(campaignPane.ebiModule.guiRenderer.getTimepicker("campaignValidFromText", "Campaign").getEditor().getText()) == true ? campaignPane.ebiModule.ebiPGFactory.getDateToString(campaign.getValidfrom()) : campaignPane.ebiModule.ebiPGFactory.getDateToString(campaign.getValidfrom()) + "$"));
        list.add(EBIPGFactory.getLANG("EBI_LANG_VALID_TO") + ": " + (campaignPane.ebiModule.ebiPGFactory.getDateToString(campaign.getValidto()).equals(campaignPane.ebiModule.guiRenderer.getTimepicker("campaingValidToText", "Campaign").getEditor().getText()) == true ? campaignPane.ebiModule.ebiPGFactory.getDateToString(campaign.getValidto()) : campaignPane.ebiModule.ebiPGFactory.getDateToString(campaign.getValidto()) + "$"));
        list.add(EBIPGFactory.getLANG("EBI_LANG_NAME") + ": " + (campaign.getName().equals(campaignPane.ebiModule.guiRenderer.getTextfield("CampaignNameText", "Campaign").getText()) == true ? campaign.getName() : campaign.getName() + "$"));
        list.add(EBIPGFactory.getLANG("EBI_LANG_C_STATUS") + ": " + (campaign.getStatus().equals(campaignPane.ebiModule.guiRenderer.getComboBox("CampaignStatusText", "Campaign").getSelectedItem().toString()) == true ? campaign.getStatus() : campaign.getStatus() + "$"));
        list.add("*EOR*");
        if (!campaign.getCrmcampaigndocses().isEmpty()) {
            Iterator iter = campaign.getCrmcampaigndocses().iterator();
            while (iter.hasNext()) {
                Crmcampaigndocs obj = (Crmcampaigndocs) iter.next();
                list.add(obj.getName() == null ? EBIPGFactory.getLANG("EBI_LANG_FILENAME") + ": " : EBIPGFactory.getLANG("EBI_LANG_FILENAME") + ": " + obj.getName());
                list.add(campaignPane.ebiModule.ebiPGFactory.getDateToString(obj.getCreateddate()) == null ? EBIPGFactory.getLANG("EBI_LANG_C_ADDED_DATE") + ": " : EBIPGFactory.getLANG("EBI_LANG_C_ADDED_DATE") + ": " + campaignPane.ebiModule.ebiPGFactory.getDateToString(obj.getCreateddate()));
                list.add(obj.getCreatedfrom() == null ? EBIPGFactory.getLANG("EBI_LANG_ADDED_FROM") + ": " : EBIPGFactory.getLANG("EBI_LANG_ADDED_FROM") + ": " + obj.getCreatedfrom());
                list.add("*EOR*");
            }
        }
        if (!campaign.getCrmcampaignpositions().isEmpty()) {
            Iterator iter = campaign.getCrmcampaignpositions().iterator();
            while (iter.hasNext()) {
                Crmcampaignposition obj = (Crmcampaignposition) iter.next();
                list.add(EBIPGFactory.getLANG("EBI_LANG_QUANTITY") + ": " + String.valueOf(obj.getQuantity()));
                list.add(EBIPGFactory.getLANG("EBI_LANG_PRODUCT_NUMBER") + ": " + obj.getProductnr());
                list.add(obj.getProductname() == null ? EBIPGFactory.getLANG("EBI_LANG_NAME") + ":" : EBIPGFactory.getLANG("EBI_LANG_NAME") + ": " + obj.getProductname());
                list.add(obj.getCategory() == null ? EBIPGFactory.getLANG("EBI_LANG_CATEGORY") + ":" : EBIPGFactory.getLANG("EBI_LANG_CATEGORY") + ": " + obj.getCategory());
                list.add(obj.getTaxtype() == null ? EBIPGFactory.getLANG("EBI_LANG_TAX") + ":" : EBIPGFactory.getLANG("EBI_LANG_TAX") + ": " + obj.getTaxtype());
                list.add(String.valueOf(obj.getPretax()) == null ? EBIPGFactory.getLANG("EBI_LANG_PRICE") + ":" : EBIPGFactory.getLANG("EBI_LANG_PRICE") + ": " + String.valueOf(obj.getPretax()));
                list.add(String.valueOf(obj.getDeduction()) == null ? EBIPGFactory.getLANG("EBI_LANG_DEDUCTION") + ":" : EBIPGFactory.getLANG("EBI_LANG_DEDUCTION") + ": " + String.valueOf(obj.getDeduction()));
                list.add(obj.getDescription() == null ? EBIPGFactory.getLANG("EBI_LANG_DESCRIPTION") + ":" : EBIPGFactory.getLANG("EBI_LANG_DESCRIPTION") + ": " + obj.getDescription());
                list.add("*EOR*");
            }
        }
        if (!campaign.getCrmcampaignreceivers().isEmpty()) {
            Iterator iter = campaign.getCrmcampaignreceivers().iterator();
            while (iter.hasNext()) {
                Crmcampaignreceiver obj = (Crmcampaignreceiver) iter.next();
                list.add(obj.getReceivervia() == null ? EBIPGFactory.getLANG("EBI_LANG_C_SEND_TYPE") + ":" : EBIPGFactory.getLANG("EBI_LANG_C_SEND_TYPE") + ": " + obj.getReceivervia());
                list.add(obj.getGender() == null ? EBIPGFactory.getLANG("EBI_LANG_C_GENDER") + ":" : EBIPGFactory.getLANG("EBI_LANG_C_GENDER") + ": " + obj.getGender());
                list.add(obj.getSurname() == null ? EBIPGFactory.getLANG("EBI_LANG_NAME") + ":" : EBIPGFactory.getLANG("EBI_LANG_NAME") + ": " + obj.getSurname());
                list.add(obj.getName() == null ? EBIPGFactory.getLANG("EBI_LANG_C_CNAME") + ":" : EBIPGFactory.getLANG("EBI_LANG_C_CNAME") + ": " + obj.getName());
                list.add(obj.getPosition() == null ? EBIPGFactory.getLANG("EBI_LANG_CONTACT_POSITION") + ":" : EBIPGFactory.getLANG("EBI_LANG_CONTACT_POSITION") + ": " + obj.getPosition());
                list.add(obj.getStreet() == null ? EBIPGFactory.getLANG("EBI_LANG_C_STREET_NR") + ":" : EBIPGFactory.getLANG("EBI_LANG_C_STREET_NR") + ": " + obj.getStreet());
                list.add(obj.getZip() == null ? EBIPGFactory.getLANG("EBI_LANG_C_ZIP") + ":" : EBIPGFactory.getLANG("EBI_LANG_C_ZIP") + ": " + obj.getZip());
                list.add(obj.getLocation() == null ? EBIPGFactory.getLANG("EBI_LANG_C_LOCATION") + ":" : EBIPGFactory.getLANG("EBI_LANG_C_LOCATION") + ": " + obj.getLocation());
                list.add(obj.getPbox() == null ? EBIPGFactory.getLANG("EBI_LANG_C_POST_CODE") + ":" : EBIPGFactory.getLANG("EBI_LANG_C_POST_CODE") + ": " + obj.getPbox());
                list.add(obj.getCountry() == null ? EBIPGFactory.getLANG("EBI_LANG_C_COUNTRY") + ":" : EBIPGFactory.getLANG("EBI_LANG_C_COUNTRY") + ": " + obj.getCountry());
                list.add("*EOR*");
            }
        }
        if (!campaign.getCrmcampaignprops().isEmpty()) {
            Iterator iter = campaign.getCrmcampaignprops().iterator();
            while (iter.hasNext()) {
                Crmcampaignprop dim = (Crmcampaignprop) iter.next();
                list.add(EBIPGFactory.getLANG("EBI_LANG_NAME") + ": " + dim.getName());
                list.add(EBIPGFactory.getLANG("EBI_LANG_VALUE") + ": " + dim.getValue());
                list.add("*EOR*");
            }
        }
        try {
            campaignPane.ebiModule.hcreator.setDataToCreate(new EBICRMHistoryDataUtil(com.getCampaignid(), "CRMCampaign", list));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dataAddProperties() {
        try {
            if (checkIslocked(id, true)) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        EBIDialogProperties dim = new EBIDialogProperties(campaignPane, campaign.getCrmcampaignprops(), null);
        dim.setVisible();
        if (!dim.cancel) {
            dataShowProperties();
        }
    }

    public void dataEditPoperties(int id) {
        try {
            if (checkIslocked(id, true)) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Iterator iter = this.campaign.getCrmcampaignprops().iterator();
        while (iter.hasNext()) {
            Crmcampaignprop properties = (Crmcampaignprop) iter.next();
            if (id == properties.getPropertiesid()) {
                EBIDialogProperties dim = new EBIDialogProperties(campaignPane, campaign.getCrmcampaignprops(), properties);
                dim.setVisible();
                if (!dim.cancel) {
                    dataShowProperties();
                }
                break;
            }
        }
    }

    public void dataDeleteProperties(int id) {
        try {
            if (checkIslocked(id, true)) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Iterator iter = this.campaign.getCrmcampaignprops().iterator();
        while (iter.hasNext()) {
            Crmcampaignprop properties = (Crmcampaignprop) iter.next();
            if (id == properties.getPropertiesid()) {
                if (id >= 0) {
                    try {
                        campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("CAMPAIGN_SESSION").begin();
                        campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("CAMPAIGN_SESSION").delete(properties);
                        campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("CAMPAIGN_SESSION").commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                this.campaign.getCrmcampaignprops().remove(properties);
                dataShowProperties();
                break;
            }
        }
    }

    public void dataShowProperties() {
        if (this.campaign.getCrmcampaignprops().size() > 0) {
            campaignPane.tabModProperties.data = new Object[this.campaign.getCrmcampaignprops().size()][3];
            Iterator iter = this.campaign.getCrmcampaignprops().iterator();
            int i = 0;
            while (iter.hasNext()) {
                Crmcampaignprop dim = (Crmcampaignprop) iter.next();
                campaignPane.tabModProperties.data[i][0] = dim.getName() == null ? "" : dim.getName();
                campaignPane.tabModProperties.data[i][1] = dim.getValue() == null ? "" : dim.getValue();
                if (dim.getPropertiesid() == null || dim.getPropertiesid() < 0) {
                    dim.setPropertiesid(((i + 1) * (-1)));
                }
                campaignPane.tabModProperties.data[i][2] = dim.getPropertiesid();
                i++;
            }
        } else {
            campaignPane.tabModProperties.data = new Object[][] { { EBIPGFactory.getLANG("EBI_LANG_PLEASE_SELECT"), "", "" } };
        }
        campaignPane.tabModProperties.fireTableDataChanged();
    }

    public void dataNewDoc() {
        try {
            if (checkIslocked(id, true)) {
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
                Crmcampaigndocs docs = new Crmcampaigndocs();
                docs.setCrmcampaign(campaign);
                docs.setName(selFile.getName());
                docs.setCreateddate(new java.sql.Date(new java.util.Date().getTime()));
                docs.setCreatedfrom(EBIPGFactory.ebiUser);
                docs.setFiles(file);
                this.campaign.getCrmcampaigndocses().add(docs);
                dataShowDoc();
            } else {
                EBIExceptionDialog.getInstance(EBIPGFactory.getLANG("EBI_LANG_ERROR_FILE_CANNOT_READING")).Show(EBIMessage.ERROR_MESSAGE);
                return;
            }
        }
    }

    public void dataShowDoc() {
        if (this.campaign.getCrmcampaigndocses().size() > 0) {
            campaignPane.tabModDoc.data = new Object[this.campaign.getCrmcampaigndocses().size()][4];
            Iterator itr = this.campaign.getCrmcampaigndocses().iterator();
            int i = 0;
            while (itr.hasNext()) {
                Crmcampaigndocs obj = (Crmcampaigndocs) itr.next();
                campaignPane.tabModDoc.data[i][0] = obj.getName() == null ? "" : obj.getName();
                campaignPane.tabModDoc.data[i][1] = campaignPane.ebiModule.ebiPGFactory.getDateToString(obj.getCreateddate()) == null ? "" : campaignPane.ebiModule.ebiPGFactory.getDateToString(obj.getCreateddate());
                campaignPane.tabModDoc.data[i][2] = obj.getCreatedfrom() == null ? "" : obj.getCreatedfrom();
                if (obj.getDocid() == null || obj.getDocid() < 0) {
                    obj.setDocid(((i + 1) * (-1)));
                }
                campaignPane.tabModDoc.data[i][3] = obj.getDocid();
                i++;
            }
        } else {
            campaignPane.tabModDoc.data = new Object[][] { { EBIPGFactory.getLANG("EBI_LANG_PLEASE_SELECT"), "", "" } };
        }
        campaignPane.tabModDoc.fireTableDataChanged();
    }

    public void dataViewDoc(int id) {
        String FileName;
        String FileType;
        OutputStream fos;
        try {
            Iterator iter = this.campaign.getCrmcampaigndocses().iterator();
            while (iter.hasNext()) {
                Crmcampaigndocs docs = (Crmcampaigndocs) iter.next();
                if (id == docs.getDocid()) {
                    String file = docs.getName().replaceAll(" ", "_");
                    byte buffer[] = docs.getFiles();
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

    public void dataDeleteDoc(int id) {
        try {
            if (checkIslocked(id, true)) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Iterator iter = this.campaign.getCrmcampaigndocses().iterator();
        while (iter.hasNext()) {
            Crmcampaigndocs doc = (Crmcampaigndocs) iter.next();
            if (id == doc.getDocid()) {
                if (id >= 0) {
                    try {
                        campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("CAMPAIGN_SESSION").begin();
                        campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("CAMPAIGN_SESSION").delete(doc);
                        campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("CAMPAIGN_SESSION").commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                this.campaign.getCrmcampaigndocses().remove(doc);
                this.dataShowDoc();
                break;
            }
        }
    }

    public void dataDeleteReceiver(int id) {
        try {
            if (checkIslocked(id, true)) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Iterator iter = this.campaign.getCrmcampaignreceivers().iterator();
        while (iter.hasNext()) {
            Crmcampaignreceiver campRec = (Crmcampaignreceiver) iter.next();
            if (campRec.getReceiverid() == id) {
                if (id >= 0) {
                    try {
                        campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("CAMPAIGN_SESSION").begin();
                        campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("CAMPAIGN_SESSION").delete(campRec);
                        campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("CAMPAIGN_SESSION").commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                campaign.getCrmcampaignreceivers().remove(campRec);
                this.dataShowReciever();
                break;
            }
        }
    }

    public void dataEditReceiver(int id) {
        try {
            if (checkIslocked(id, true)) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Iterator iter = this.campaign.getCrmcampaignreceivers().iterator();
        while (iter.hasNext()) {
            Crmcampaignreceiver campRec = (Crmcampaignreceiver) iter.next();
            if (campRec.getReceiverid() == id) {
                EBICRMAddContactAddressType addCo = new EBICRMAddContactAddressType(campaignPane.ebiModule, this, campRec);
                addCo.setVisible();
                this.dataShowReciever();
                break;
            }
        }
    }

    public void dataDeleteProduct(int id) {
        try {
            if (checkIslocked(id, true)) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Iterator iter = this.campaign.getCrmcampaignpositions().iterator();
        while (iter.hasNext()) {
            Crmcampaignposition camPro = (Crmcampaignposition) iter.next();
            if (camPro.getPositionid() == id) {
                if (id >= 0) {
                    try {
                        campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("CAMPAIGN_SESSION").begin();
                        campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateSession("CAMPAIGN_SESSION").delete(camPro);
                        campaignPane.ebiModule.ebiPGFactory.hibernate.getHibernateTransaction("CAMPAIGN_SESSION").commit();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                campaign.getCrmcampaignpositions().remove(camPro);
                this.dataShowProduct();
                break;
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
        type = type.toLowerCase();
        if (".jpg".equals(type) || ".gif".equals(type) || ".png".equals(type)) {
            EBIImageViewer view = new EBIImageViewer(new javax.swing.ImageIcon(fileName));
            view.setVisible(true);
        } else if (".pdf".equals(type)) {
            campaignPane.ebiModule.ebiPGFactory.openPDFReportFile(fileName);
        } else if (".doc".equals(type)) {
            campaignPane.ebiModule.ebiPGFactory.openTextDocumentFile(fileName);
        } else {
            campaignPane.ebiModule.ebiPGFactory.openTextDocumentFile(fileName);
        }
    }

    /**
     * Check if a loaded record is locked
     * @param compNr
     * @param showMessage
     * @throws Exception
     */
    public boolean checkIslocked(int compNr, boolean showMessage) throws Exception {
        boolean ret = false;
        PreparedStatement ps = campaignPane.ebiModule.ebiPGFactory.database.initPreparedStatement("SELECT * FROM EBIPESSIMISTIC WHERE RECORDID=? AND MODULENAME=?  ");
        ps.setInt(1, compNr);
        ps.setString(2, "CRMCampaign");
        ResultSet rs = campaignPane.ebiModule.ebiPGFactory.database.executePreparedQuery(ps);
        rs.last();
        if (rs.getRow() <= 0) {
            lockId = compNr;
            lockModuleName = "CRMCampaign";
            lockUser = EBIPGFactory.ebiUser;
            lockStatus = 1;
            lockTime = new Timestamp(new Date().getTime());
            campaignPane.ebiModule.lockCompanyRecord(compNr, "CRMCampaign", lockTime);
            activateLockedInfo(false);
        } else {
            rs.beforeFirst();
            rs.next();
            lockId = rs.getInt("RECORDID");
            lockModuleName = rs.getString("MODULENAME");
            lockUser = rs.getString("USER");
            lockStatus = rs.getInt("STATUS");
            lockTime = rs.getTimestamp("LOCKDATE");
            if (!lockUser.equals(EBIPGFactory.ebiUser)) {
                activateLockedInfo(true);
            }
            if (showMessage && !lockUser.equals(EBIPGFactory.ebiUser)) {
                ret = true;
            }
        }
        campaignPane.ebiModule.guiRenderer.getLabel("userx", "pessimisticViewDialog").setText(lockUser);
        campaignPane.ebiModule.guiRenderer.getLabel("statusx", "pessimisticViewDialog").setText(EBIPGFactory.getLANG("EBI_LANG_LOCKED"));
        if (lockTime != null) {
            campaignPane.ebiModule.guiRenderer.getLabel("timex", "pessimisticViewDialog").setText(lockTime.toString());
        }
        return ret;
    }

    /**
     * Activate Pessimistic Lock for the GUI
     * @param enabled
     */
    public void activateLockedInfo(boolean enabled) {
        campaignPane.ebiModule.guiRenderer.getVisualPanel("Campaign").showLockIcon(enabled);
        campaignPane.ebiModule.guiRenderer.getButton("saveCampagin", "Campaign").setVisible(enabled ? false : true);
        campaignPane.ebiModule.guiRenderer.getButton("newCampaignDoc", "Campaign").setVisible(enabled ? false : true);
        campaignPane.ebiModule.guiRenderer.getButton("deleteCampaignDoc", "Campaign").setVisible(enabled ? false : true);
        campaignPane.ebiModule.guiRenderer.getButton("newCampaingProperties", "Campaign").setVisible(enabled ? false : true);
        campaignPane.ebiModule.guiRenderer.getButton("editCampaingProperties", "Campaign").setVisible(enabled ? false : true);
        campaignPane.ebiModule.guiRenderer.getButton("deleteCampaingProperties", "Campaign").setVisible(enabled ? false : true);
        campaignPane.ebiModule.guiRenderer.getButton("newCampaignProduct", "Campaign").setVisible(enabled ? false : true);
        campaignPane.ebiModule.guiRenderer.getButton("deleteCampaignProduct", "Campaign").setVisible(enabled ? false : true);
        campaignPane.ebiModule.guiRenderer.getButton("newCampaignReceiver", "Campaign").setVisible(enabled ? false : true);
        campaignPane.ebiModule.guiRenderer.getButton("editCampaignReceiver", "Campaign").setVisible(enabled ? false : true);
        campaignPane.ebiModule.guiRenderer.getButton("deleteCampaignReceiver", "Campaign").setVisible(enabled ? false : true);
        campaignPane.ebiModule.guiRenderer.getButton("deleteCampaign", "Campaign").setVisible(enabled ? false : true);
        campaignPane.ebiModule.guiRenderer.getButton("deleteCampaign", "Campaign").setVisible(enabled ? false : true);
    }

    public Crmcampaign getCampaign() {
        return campaign;
    }

    public void setCampaign(Crmcampaign campaign) {
        this.campaign = campaign;
    }

    public Set<Crmcampaigndocs> getCampaignDocList() {
        return campaign.getCrmcampaigndocses();
    }

    public Set<Crmcampaignposition> getCampaignPosList() {
        return campaign.getCrmcampaignpositions();
    }

    public Set<Crmcampaignreceiver> getCampaignReceiverList() {
        return campaign.getCrmcampaignreceivers();
    }

    public Set<Crmcampaignprop> getPropertiesList() {
        return campaign.getCrmcampaignprops();
    }
}
