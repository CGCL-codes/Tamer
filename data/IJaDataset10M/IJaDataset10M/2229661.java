package org.compiere.www;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import org.apache.ecs.*;
import org.apache.ecs.xhtml.*;
import org.compiere.model.*;
import org.compiere.util.*;

/**
 *	HTML UI Attachment
 *	
 *  @author Jorg Janke
 *  @version $Id: WAttachment.java,v 1.2 2006/07/30 00:53:21 jjanke Exp $
 */
public class WAttachment extends HttpServlet {

    /**	Logger			*/
    private static CLogger log = CLogger.getCLogger(WAttachment.class);

    /**
	 * Initialize global variables
	 */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        if (!WebEnv.initWeb(config)) throw new ServletException("WAttachment.init");
    }

    public static final String P_Attachment_ID = "AD_Attachment_ID";

    public static final String P_ATTACHMENT_INDEX = "AttachmentIndex";

    public static final String P_TEXTMSG = "TextMsg";

    /**
	 * 	Process the HTTP Get request.
	 * 	Initial display and streaming 
	 *  @param request request
	 *  @param response response
	 */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("From " + request.getRemoteHost() + " - " + request.getRemoteAddr());
        HttpSession session = request.getSession(false);
        WWindowStatus ws = WWindowStatus.get(request);
        WebDoc doc = null;
        if (session == null || ws == null) {
            doc = WebDoc.createPopup("No Context");
            doc.addPopupClose(ws.ctx);
        } else {
            String error = null;
            int AD_Attachment_ID = WebUtil.getParameterAsInt(request, P_Attachment_ID);
            if (AD_Attachment_ID != 0) {
                int attachmentIndex = WebUtil.getParameterAsInt(request, P_ATTACHMENT_INDEX);
                if (attachmentIndex != 0) {
                    error = streamAttachment(AD_Attachment_ID, attachmentIndex, response, ws);
                    if (error == null) return;
                }
            }
            doc = createPage(ws.ctx, ws.curTab.getAD_AttachmentID(), ws.curTab.getAD_Table_ID(), ws.curTab.getRecord_ID(), error);
        }
        WebUtil.createResponse(request, response, this, null, doc, false);
    }

    /**
	 *  Process the HTTP Post request.
	 *  Update Attachment
	 *  @param request request
	 *  @parem response response
	 */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("From " + request.getRemoteHost() + " - " + request.getRemoteAddr());
        HttpSession sess = request.getSession(false);
        WWindowStatus ws = WWindowStatus.get(request);
        WebDoc doc = null;
        if (ws == null) doc = WebDoc.create("Help - No Context"); else {
            String error = processPost(request, response, ws);
            doc = createPage(ws.ctx, ws.curTab.getAD_AttachmentID(), ws.curTab.getAD_Table_ID(), ws.curTab.getRecord_ID(), error);
        }
        WebUtil.createResponse(request, response, this, null, doc, false);
    }

    /**
	 * 	Create Attachment Page
	 * 	@param ctx context
	 *	@param AD_Attachment_ID id for existing attachment
	 *	@param AD_Table_ID table for new attachment
	 *	@param Record_ID record for new attachment
	 *	@param error optional error message
	 *	@return WebDoc
	 */
    private WebDoc createPage(Properties ctx, int AD_Attachment_ID, int AD_Table_ID, int Record_ID, String error) {
        WebDoc doc = WebDoc.createPopup(Msg.translate(ctx, "AD_Attachment_ID"));
        table table = doc.getTable();
        if (error != null) {
            table.addElement(new tr().addElement(new td("popupHeader", AlignType.RIGHT, AlignType.TOP, false, null)).addElement(new td("popupHeader", AlignType.LEFT, AlignType.TOP, false, new p(error, AlignType.LEFT).setClass("Cerror"))));
        }
        MAttachment attachment = null;
        if (AD_Attachment_ID != 0) attachment = new MAttachment(ctx, AD_Attachment_ID, null); else attachment = new MAttachment(ctx, AD_Table_ID, Record_ID, null);
        tr tr = new tr();
        td left = new td("popupCenter", AlignType.LEFT, AlignType.TOP, false, new label("TextMsg", "T", Msg.translate(ctx, "TextMsg")));
        td right = new td("popupCenter", AlignType.LEFT, AlignType.TOP, false);
        form textMsg = new form("WAttachment");
        textMsg.addElement(new input(input.TYPE_HIDDEN, P_Attachment_ID, AD_Attachment_ID));
        textMsg.addElement(new input(input.TYPE_HIDDEN, "AD_Table_ID", AD_Table_ID));
        textMsg.addElement(new input(input.TYPE_HIDDEN, "Record_ID", Record_ID));
        textarea msg = new textarea(P_TEXTMSG, 5, 40);
        msg.addElement(attachment.getTextMsg());
        textMsg.addElement(msg);
        textMsg.addElement(new br());
        right.addElement(textMsg);
        p p = new p();
        MAttachmentEntry[] entries = attachment.getEntries();
        for (int i = 0; i < entries.length; i++) {
            MAttachmentEntry entry = entries[i];
            if (i > 0) p.addElement(" - ");
            String url = "WAttachment?" + P_Attachment_ID + "=" + AD_Attachment_ID + "&" + P_ATTACHMENT_INDEX + "=" + entry.getIndex();
            p.addElement(new a(url, null, a.TARGET_BLANK, entry.getName()));
        }
        right.addElement(p);
        form upload = FileUpload.createForm("WAttachment");
        upload.addElement(new input(input.TYPE_HIDDEN, P_Attachment_ID, AD_Attachment_ID));
        upload.addElement(new input(input.TYPE_HIDDEN, "AD_Table_ID", AD_Table_ID));
        upload.addElement(new input(input.TYPE_HIDDEN, "Record_ID", Record_ID));
        right.addElement(upload);
        tr.addElement(left);
        tr.addElement(right);
        table.addElement(tr);
        doc.addPopupClose(ctx);
        return doc;
    }

    /**
	 * 	Stream Attachment Entry  
	 *	@param AD_Attachment_ID attachment
	 *	@param attachmentIndex index
	 *	@param response response
	 *	@param ws status
	 *	@return error message
	 */
    private String streamAttachment(int AD_Attachment_ID, int attachmentIndex, HttpServletResponse response, WWindowStatus ws) {
        log.info("AD_Attachment_ID=" + AD_Attachment_ID + ", AttachmentIndex=" + attachmentIndex);
        MAttachment attachment = new MAttachment(ws.ctx, AD_Attachment_ID, null);
        if (attachment.get_ID() == 0) {
            log.fine("No Attachment AD_Attachment_ID=" + AD_Attachment_ID);
            return "Attachment not found";
        }
        if (ws.curTab.getAD_AttachmentID() != AD_Attachment_ID) {
            log.warning("Tab AD_Attachment_ID=" + ws.curTab.getAD_AttachmentID() + " <> " + AD_Attachment_ID);
            return "Your Attachment not found";
        }
        return WebUtil.streamAttachment(response, attachment, attachmentIndex);
    }

    /**
	 * 	Process Post.
	 * 	Update / Create Attachment
	 * 	Upload Attachment Entry  
	 *	@param request request
	 *	@param response response
	 *	@return error message
	 */
    private String processPost(HttpServletRequest request, HttpServletResponse response, WWindowStatus ws) {
        int AD_Attachment_ID = 0;
        int AD_Table_ID = 0;
        int Record_ID = 0;
        String textMsg = null;
        FileUpload upload = null;
        if (request.getContentType().equals(form.ENC_DEFAULT)) {
            AD_Attachment_ID = WebUtil.getParameterAsInt(request, P_Attachment_ID);
            AD_Table_ID = WebUtil.getParameterAsInt(request, "AD_Table_ID");
            Record_ID = WebUtil.getParameterAsInt(request, "Record_ID");
            textMsg = WebUtil.getParameter(request, P_TEXTMSG);
        } else {
            upload = new FileUpload(request);
            String error = upload.getError();
            if (error != null) {
                log.warning("pocessPost - " + error);
                return error;
            }
            AD_Attachment_ID = upload.getParameterAsInt(P_Attachment_ID);
            AD_Table_ID = upload.getParameterAsInt("AD_Table_ID");
            Record_ID = upload.getParameterAsInt("Record_ID");
        }
        log.info("processPost - AD_Attachment_ID=" + AD_Attachment_ID + ", AD_Table_ID=" + AD_Table_ID + ", Record_ID=" + Record_ID + " - Upload=" + upload);
        if (ws.curTab.getAD_AttachmentID() != AD_Attachment_ID) return "Your Attachment not found";
        if (AD_Attachment_ID != 0 && Record_ID == 0) return "Need to save record first";
        MAttachment attachment = null;
        if (AD_Attachment_ID == 0) attachment = new MAttachment(ws.ctx, AD_Table_ID, Record_ID, null); else attachment = new MAttachment(ws.ctx, AD_Attachment_ID, null);
        if (textMsg != null) attachment.setTextMsg(textMsg);
        if (upload != null) attachment.addEntry(upload.getFileName(), upload.getData());
        if (attachment.save()) ws.curTab.loadAttachments(); else return "Attachment not saved";
        return null;
    }
}
