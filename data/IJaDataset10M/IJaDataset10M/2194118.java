package cn.vlabs.duckling.vwb.ui.accclb;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import cn.cnic.esac.clb.api.AppContext;
import cn.cnic.esac.clb.api.CLBException;
import cn.cnic.esac.clb.api.service.ServiceFactory;
import cn.cnic.esac.clb.util.Tag;
import cn.vlabs.clb.api.document.MetaInfo;
import cn.vlabs.duckling.util.Utility;
import cn.vlabs.duckling.vwb.VWBContext;

/** 
 * MyEclipse Struts
 * Creation date: 08-30-2007
 * 
 * XDoclet definition:
 * @struts.form name="updateCLBForm"
 */
public class UpdateCLBForm extends ActionForm {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** file property */
    private FormFile file;

    private String docid, comment;

    private String sysTagValues[], privateTagValues[], shareTagValues[];

    private String sysTagID[], privateTagID[], shareTagID[];

    private String sysTag, privateTag, shareTag;

    private String tagvalues[];

    private String tagvaluesID[];

    private String tags;

    private String comeFrom;

    private String attach;

    /** 
     * Method validate
     * @param mapping
     * @param request
     * @return ActionErrors
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        return null;
    }

    /** 
     * Method reset
     * @param mapping
     * @param request
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        VWBContext context = VWBContext.getContext(request);
        String docid = request.getParameter("docid");
        if (docid == null) return;
        MetaInfo meta;
        tags = "";
        try {
            meta = context.getSite().getAttachmentService().getDocumentService(context.getVWBSession()).getMeta(Integer.parseInt(docid));
            String[] tagSet = meta.tags;
            if ((tagSet == null) || (tagSet.length == 0)) {
                tags = "";
                return;
            }
            for (int i = 0; i < tagSet.length; i++) {
                tags += tagSet[i];
            }
            tags = tags.trim();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void resetTags(ActionMapping mapping, HttpServletRequest request, Tag[] tags, FormFile file) {
        Tag tagAdd = (Tag) request.getSession().getAttribute("tagAdd");
        int count = 0;
        if (tagAdd != null) {
            count = 1;
        }
        if ((tags == null) || (tags.length == 0)) {
            this.tagvalues = new String[count];
            this.tagvaluesID = new String[count];
            if (count == 1) {
                tagvalues[tagvalues.length - 1] = tagAdd.getTagname();
                tagvaluesID[tagvaluesID.length - 1] = Integer.toString(tagAdd.getId());
            }
        } else {
            this.tagvalues = new String[tags.length + count];
            for (int i = 0; i < tags.length; i++) {
                tagvalues[i] = tags[i].getTagname();
                tagvaluesID[i] = Integer.toString(tags[i].getId());
            }
            if (count == 1) {
                tagvalues[tagvalues.length - 1] = tagAdd.getTagname();
                tagvaluesID[tagvaluesID.length - 1] = Integer.toString(tagAdd.getId());
            }
        }
        this.file = file;
    }

    /** 
     * Returns the file.
     * @return String
     */
    public FormFile getFile() {
        return file;
    }

    /** 
     * Set the file.
     * @param file The file to set
     */
    public void setFile(FormFile file) {
        this.file = file;
    }

    public String getDocid() {
        return docid;
    }

    public void setDocid(String id) {
        this.docid = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    private String values[];

    private String selected;

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }

    public String[] getValues() {
        return values;
    }

    public void setValues(String[] values) {
        this.values = values;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String[] getTagvalues() {
        return tagvalues;
    }

    public void setTagvalues(String[] tagvalues) {
        this.tagvalues = tagvalues;
    }

    private String newtag;

    public String getNewtag() {
        return newtag;
    }

    public void setNewtag(String newtag) {
        this.newtag = newtag;
    }

    private String operate;

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    private String action;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getSysTag() {
        return sysTag;
    }

    public String getPrivateTag() {
        return privateTag;
    }

    public String getShareTag() {
        return shareTag;
    }

    public String[] getSysTagValues() {
        return sysTagValues;
    }

    public String[] getPrivateTagValues() {
        return privateTagValues;
    }

    public String[] getShareTagValues() {
        return shareTagValues;
    }

    public String[] getSysTagID() {
        return sysTagID;
    }

    public String[] getPrivateTagID() {
        return privateTagID;
    }

    public String[] getShareTagID() {
        return shareTagID;
    }

    public void setSysTag(String sysTag) {
        this.sysTag = sysTag;
    }

    public void setPrivateTag(String privateTag) {
        this.privateTag = privateTag;
    }

    public void setShareTag(String shareTag) {
        this.shareTag = shareTag;
    }

    public void setSysTagValues(String[] sysTagValues) {
        this.sysTagValues = sysTagValues;
    }

    public void setPrivateTagValues(String[] privateTagValues) {
        this.privateTagValues = privateTagValues;
    }

    public void setShareTagValues(String[] shareTagValues) {
        this.shareTagValues = shareTagValues;
    }

    public void setSysTagID(String[] sysTagID) {
        this.sysTagID = sysTagID;
    }

    public void setPrivateTagID(String[] privateTagID) {
        this.privateTagID = privateTagID;
    }

    public void setShareTagID(String[] shareTagID) {
        this.shareTagID = shareTagID;
    }

    public void setTagvaluesID(String[] tagvaluesID) {
        this.tagvaluesID = tagvaluesID;
    }

    public String[] getTagvaluesID() {
        return tagvaluesID;
    }

    public String getComeFrom() {
        return comeFrom;
    }

    public void setComeFrom(String comeFrom) {
        this.comeFrom = comeFrom;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }
}
