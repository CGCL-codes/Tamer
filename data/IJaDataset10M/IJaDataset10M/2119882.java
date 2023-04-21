package org.dspace.app.webui.servlet.admin;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.dspace.app.webui.servlet.DSpaceServlet;
import org.dspace.app.webui.util.FileUploadRequest;
import org.dspace.app.webui.util.JSPManager;
import org.dspace.app.webui.util.UIUtil;
import org.dspace.authorize.AuthorizeException;
import org.dspace.authorize.AuthorizeManager;
import org.dspace.content.*;
import org.dspace.content.Collection;
import org.dspace.core.Constants;
import org.dspace.core.Context;
import org.dspace.core.LogManager;
import org.dspace.core.ConfigurationManager;
import org.dspace.handle.HandleManager;
import org.dspace.license.CreativeCommons;

/**
 * Servlet for editing and deleting (expunging) items
 *
 * @author Robert Tansley
 * @version $Revision: 2642 $
 */
public class EditItemServlet extends DSpaceServlet {

    /**
     * User wants to delete (expunge) an item
     */
    public static final int START_DELETE = 1;

    /**
     * User confirms delete (expunge) of item
     */
    public static final int CONFIRM_DELETE = 2;

    /**
     * User updates item
     */
    public static final int UPDATE_ITEM = 3;

    /**
     * User starts withdrawal of item
     */
    public static final int START_WITHDRAW = 4;

    /**
     * User confirms withdrawal of item
     */
    public static final int CONFIRM_WITHDRAW = 5;

    /**
     * User reinstates a withdrawn item
     */
    public static final int REINSTATE = 6;

    /**
     * User starts the movement of an item
     */
    public static final int START_MOVE_ITEM = 7;

    /**
     * User confirms the movement of the item
     */
    public static final int CONFIRM_MOVE_ITEM = 8;

    /**
     * Logger
     */
    private static Logger log = Logger.getLogger(EditCommunitiesServlet.class);

    protected void doDSGet(Context context, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {
        int internalID = UIUtil.getIntParameter(request, "item_id");
        String handle = request.getParameter("handle");
        boolean showError = false;
        Item itemToEdit = null;
        if (internalID > 0) {
            itemToEdit = Item.find(context, internalID);
            showError = (itemToEdit == null);
        } else if ((handle != null) && !handle.equals("")) {
            DSpaceObject dso = HandleManager.resolveToObject(context, handle.trim());
            if ((dso != null) && (dso.getType() == Constants.ITEM)) {
                itemToEdit = (Item) dso;
                showError = false;
            } else {
                showError = true;
            }
        }
        if (itemToEdit != null) {
            checkEditAuthorization(context, itemToEdit);
            showEditForm(context, request, response, itemToEdit);
        } else {
            if (showError) {
                request.setAttribute("invalid.id", new Boolean(true));
            }
            JSPManager.showJSP(request, response, "/tools/get-item-id.jsp");
        }
    }

    protected void doDSPost(Context context, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {
        String contentType = request.getContentType();
        if ((contentType != null) && (contentType.indexOf("multipart/form-data") != -1)) {
            processUploadBitstream(context, request, response);
            return;
        }
        if (request.getParameter("submit_cancel") != null) {
            JSPManager.showJSP(request, response, "/tools/get-item-id.jsp");
            return;
        }
        int action = UIUtil.getIntParameter(request, "action");
        Item item = Item.find(context, UIUtil.getIntParameter(request, "item_id"));
        String handle = HandleManager.findHandle(context, item);
        checkEditAuthorization(context, item);
        request.setAttribute("item", item);
        request.setAttribute("handle", handle);
        switch(action) {
            case START_DELETE:
                JSPManager.showJSP(request, response, "/tools/confirm-delete-item.jsp");
                break;
            case CONFIRM_DELETE:
                Collection[] collections = item.getCollections();
                for (int i = 0; i < collections.length; i++) {
                    collections[i].removeItem(item);
                }
                JSPManager.showJSP(request, response, "/tools/get-item-id.jsp");
                context.complete();
                break;
            case UPDATE_ITEM:
                processUpdateItem(context, request, response, item);
                break;
            case START_WITHDRAW:
                JSPManager.showJSP(request, response, "/tools/confirm-withdraw-item.jsp");
                break;
            case CONFIRM_WITHDRAW:
                item.withdraw();
                JSPManager.showJSP(request, response, "/tools/get-item-id.jsp");
                context.complete();
                break;
            case REINSTATE:
                item.reinstate();
                JSPManager.showJSP(request, response, "/tools/get-item-id.jsp");
                context.complete();
                break;
            case START_MOVE_ITEM:
                if (AuthorizeManager.isAdmin(context)) {
                    Collection[] notLinkedCollections = item.getCollectionsNotLinked();
                    Collection[] linkedCollections = item.getCollections();
                    request.setAttribute("linkedCollections", linkedCollections);
                    request.setAttribute("notLinkedCollections", notLinkedCollections);
                    JSPManager.showJSP(request, response, "/tools/move-item.jsp");
                } else {
                    throw new ServletException("You must be an administrator to move an item");
                }
                break;
            case CONFIRM_MOVE_ITEM:
                if (AuthorizeManager.isAdmin(context)) {
                    Collection fromCollection = Collection.find(context, UIUtil.getIntParameter(request, "collection_from_id"));
                    Collection toCollection = Collection.find(context, UIUtil.getIntParameter(request, "collection_to_id"));
                    if (fromCollection == null || toCollection == null) {
                        throw new ServletException("Missing or incorrect collection IDs for moving item");
                    }
                    item.move(fromCollection, toCollection);
                    showEditForm(context, request, response, item);
                    context.complete();
                } else {
                    throw new ServletException("You must be an administrator to move an item");
                }
                break;
            default:
                log.warn(LogManager.getHeader(context, "integrity_error", UIUtil.getRequestLogInfo(request)));
                JSPManager.showIntegrityError(request, response);
        }
    }

    /**
     * Throw an exception if user isn't authorized to edit this item
     *
     * @param context
     * @param item
     */
    private void checkEditAuthorization(Context c, Item item) throws AuthorizeException, java.sql.SQLException {
        if (!item.canEdit()) {
            int userID = 0;
            if (c.getCurrentUser() != null) {
                userID = c.getCurrentUser().getID();
            }
            throw new AuthorizeException("EditItemServlet: User " + userID + " not authorized to edit item " + item.getID());
        }
    }

    /**
     * Show the item edit form for a particular item
     *
     * @param context  DSpace context
     * @param request  the HTTP request containing posted info
     * @param response the HTTP response
     * @param item     the item
     */
    private void showEditForm(Context context, HttpServletRequest request, HttpServletResponse response, Item item) throws ServletException, IOException, SQLException, AuthorizeException {
        if (request.getParameter("cc_license_url") != null) {
            CreativeCommons.setLicense(context, item, request.getParameter("cc_license_url"));
            context.commit();
        }
        String handle = HandleManager.findHandle(context, item);
        Collection[] collections = item.getCollections();
        MetadataField[] types = MetadataField.findAll(context);
        HashMap metadataFields = new HashMap();
        MetadataSchema[] schemas = MetadataSchema.findAll(context);
        for (int i = 0; i < schemas.length; i++) {
            String schemaName = schemas[i].getName();
            MetadataField[] fields = MetadataField.findAllInSchema(context, schemas[i].getSchemaID());
            for (int j = 0; j < fields.length; j++) {
                Integer fieldID = new Integer(fields[j].getFieldID());
                String displayName = "";
                displayName = schemaName + "." + fields[j].getElement() + (fields[j].getQualifier() == null ? "" : "." + fields[j].getQualifier());
                metadataFields.put(fieldID, displayName);
            }
        }
        List<Reference> refs = item.getReferences();
        Map<String, String> refsMap = new LinkedHashMap<String, String>();
        if ((refs != null) && (refs.size() > 0)) {
            for (Reference ref : refs) {
                String refString = ref.getFirstMetadataValue(ConfigurationManager.getProperty("ref.citationString"));
                String uriString = ref.getFirstMetadataValue(ConfigurationManager.getProperty("ref.citationUri"));
                refsMap.put(refString, uriString);
            }
        }
        request.setAttribute("item", item);
        request.setAttribute("handle", handle);
        request.setAttribute("collections", collections);
        request.setAttribute("dc.types", types);
        request.setAttribute("metadataFields", metadataFields);
        request.setAttribute("refs", refsMap);
        JSPManager.showJSP(request, response, "/tools/edit-item-form.jsp");
    }

    /**
     * Process input from the edit item form
     *
     * @param context  DSpace context
     * @param request  the HTTP request containing posted info
     * @param response the HTTP response
     * @param item     the item
     */
    private void processUpdateItem(Context context, HttpServletRequest request, HttpServletResponse response, Item item) throws ServletException, IOException, SQLException, AuthorizeException {
        String button = UIUtil.getSubmitButton(request, "submit");
        item.clearMetadata(Item.ANY, Item.ANY, Item.ANY, Item.ANY);
        Enumeration unsortedParamNames = request.getParameterNames();
        List sortedParamNames = new LinkedList();
        while (unsortedParamNames.hasMoreElements()) {
            sortedParamNames.add(unsortedParamNames.nextElement());
        }
        Collections.sort(sortedParamNames);
        Iterator iterator = sortedParamNames.iterator();
        while (iterator.hasNext()) {
            String p = (String) iterator.next();
            if (p.startsWith("value")) {
                StringTokenizer st = new StringTokenizer(p, "_");
                st.nextToken();
                String schema = st.nextToken();
                String element = st.nextToken();
                String qualifier = null;
                if (st.countTokens() == 2) {
                    qualifier = st.nextToken();
                }
                String sequenceNumber = st.nextToken();
                String key = MetadataField.formKey(schema, element, qualifier);
                String language = request.getParameter("language_" + key + "_" + sequenceNumber);
                if ((language != null) && language.equals("")) {
                    language = null;
                }
                String value = request.getParameter(p).trim();
                if (!(value.equals("") || button.equals("submit_remove_" + key + "_" + sequenceNumber))) {
                    item.addMetadata(schema, element, qualifier, language, value);
                }
            } else if (p.startsWith("bitstream_name") && AuthorizeManager.isAdmin(context)) {
                StringTokenizer st = new StringTokenizer(p, "_");
                st.nextToken();
                st.nextToken();
                int bundleID = Integer.parseInt(st.nextToken());
                int bitstreamID = Integer.parseInt(st.nextToken());
                Bundle bundle = Bundle.find(context, bundleID);
                Bitstream bitstream = Bitstream.find(context, bitstreamID);
                String key = String.valueOf(bundleID) + "_" + bitstreamID;
                if (button.equals("submit_delete_bitstream_" + key)) {
                    bundle.removeBitstream(bitstream);
                    if (bundle.getBitstreams().length == 0) {
                        item.removeBundle(bundle);
                    }
                } else {
                    String name = request.getParameter(p);
                    String source = request.getParameter("bitstream_source_" + key);
                    String desc = request.getParameter("bitstream_description_" + key);
                    int formatID = UIUtil.getIntParameter(request, "bitstream_format_id_" + key);
                    String userFormatDesc = request.getParameter("bitstream_user_format_description_" + key);
                    int primaryBitstreamID = UIUtil.getIntParameter(request, bundleID + "_primary_bitstream_id");
                    if (source.equals("")) {
                        source = null;
                    }
                    if (desc.equals("")) {
                        desc = null;
                    }
                    if (userFormatDesc.equals("")) {
                        userFormatDesc = null;
                    }
                    bitstream.setName(name);
                    bitstream.setSource(source);
                    bitstream.setDescription(desc);
                    bitstream.setFormat(BitstreamFormat.find(context, formatID));
                    if (primaryBitstreamID > 0) {
                        bundle.setPrimaryBitstreamID(primaryBitstreamID);
                    }
                    if (userFormatDesc != null) {
                        bitstream.setUserFormatDescription(userFormatDesc);
                    }
                    bitstream.update();
                    bundle.update();
                }
            }
        }
        if (button.equals("submit_addfield")) {
            int dcTypeID = UIUtil.getIntParameter(request, "addfield_dctype");
            String value = request.getParameter("addfield_value").trim();
            String lang = request.getParameter("addfield_language");
            if (lang.equals("")) {
                lang = null;
            }
            MetadataField field = MetadataField.find(context, dcTypeID);
            MetadataSchema schema = MetadataSchema.find(context, field.getSchemaID());
            item.addMetadata(schema.getName(), field.getElement(), field.getQualifier(), lang, value);
        }
        item.deleteReferences();
        String total = request.getParameter("totalRefs");
        if ((total != null) && (total.length() > 0)) {
            int totalInt = Integer.valueOf(total);
            int i = 1;
            while (i <= totalInt) {
                String inputRef = request.getParameter("ref_value_" + i);
                if ((inputRef != null) && (inputRef.length() > 0) && (!button.equals("submit_ref_remove_" + i))) {
                    org.dspace.content.Reference ref = addReference(context, item);
                    addMetadataRecord(context, ref, inputRef, ConfigurationManager.getProperty("ref.citationString"));
                    String inputUrl = request.getParameter("ref_url_" + i);
                    if ((inputUrl != null) && (inputUrl.length() > 0)) {
                        addMetadataRecord(context, ref, inputRef, ConfigurationManager.getProperty("ref.citationUri"));
                    }
                }
                i++;
            }
        }
        if (button.equals("submit_ref_add")) {
            String inputRef = request.getParameter("ref_value");
            if ((inputRef != null) && (inputRef.length() > 0)) {
                org.dspace.content.Reference ref = addReference(context, item);
                addMetadataRecord(context, ref, inputRef, ConfigurationManager.getProperty("ref.citationString"));
                String inputUrl = request.getParameter("ref_url");
                if ((inputUrl != null) && (inputUrl.length() > 0)) {
                    addMetadataRecord(context, ref, inputRef, ConfigurationManager.getProperty("ref.citationUri"));
                }
            }
        }
        item.update();
        if (button.equals("submit_addcc")) {
            request.setAttribute("item", item);
            JSPManager.showJSP(request, response, "/tools/creative-commons-edit.jsp");
        }
        if (button.equals("submit_addbitstream")) {
            request.setAttribute("item", item);
            JSPManager.showJSP(request, response, "/tools/upload-bitstream.jsp");
        } else {
            showEditForm(context, request, response, item);
        }
        context.complete();
    }

    /**
     * Process the input from the upload bitstream page
     *
     * @param context  current DSpace context
     * @param request  current servlet request object
     * @param response current servlet response object
     */
    private void processUploadBitstream(Context context, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException, AuthorizeException {
        FileUploadRequest wrapper = new FileUploadRequest(request);
        Bitstream b = null;
        Item item = Item.find(context, UIUtil.getIntParameter(wrapper, "item_id"));
        File temp = wrapper.getFile("file");
        InputStream is = new BufferedInputStream(new FileInputStream(temp));
        checkEditAuthorization(context, item);
        Bundle[] bundles = item.getBundles("ORIGINAL");
        if (bundles.length < 1) {
            b = item.createSingleBitstream(is, "ORIGINAL");
        } else {
            b = bundles[0].createBitstream(is);
        }
        String noPath = wrapper.getFilesystemName("file");
        while (noPath.indexOf('/') > -1) {
            noPath = noPath.substring(noPath.indexOf('/') + 1);
        }
        while (noPath.indexOf('\\') > -1) {
            noPath = noPath.substring(noPath.indexOf('\\') + 1);
        }
        b.setName(noPath);
        b.setSource(wrapper.getFilesystemName("file"));
        BitstreamFormat bf = FormatIdentifier.guessFormat(context, b);
        b.setFormat(bf);
        b.update();
        item.update();
        showEditForm(context, request, response, item);
        temp.delete();
        context.complete();
    }

    private org.dspace.content.Reference addReference(Context context, Item item) throws SQLException, IOException, AuthorizeException {
        org.dspace.content.Reference ref = new org.dspace.content.Reference(context);
        ref.setItemId(item.getItemId());
        item.addReference(ref);
        return ref;
    }

    private void addMetadataRecord(Context context, org.dspace.content.Reference ref, String value, String qName) throws SQLException, AuthorizeException {
        MetadataValueRef mvr;
        mvr = new MetadataValueRef(context);
        mvr.setReferenceId(ref.getReferenceId());
        ref.addMetadata(mvr);
        int fieldId = ref.findMetadataType(qName);
        mvr.setFieldId(fieldId);
        mvr.setValue(value);
    }
}
