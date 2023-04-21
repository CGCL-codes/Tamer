package net.sf.iqser.plugin.msx.api.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import microsoft.exchange.webservices.data.Appointment;
import microsoft.exchange.webservices.data.Attachment;
import microsoft.exchange.webservices.data.AttachmentCollection;
import microsoft.exchange.webservices.data.BasePropertySet;
import microsoft.exchange.webservices.data.Contact;
import microsoft.exchange.webservices.data.DeleteMode;
import microsoft.exchange.webservices.data.EmailMessage;
import microsoft.exchange.webservices.data.ExchangeService;
import microsoft.exchange.webservices.data.ExchangeVersion;
import microsoft.exchange.webservices.data.FindFoldersResults;
import microsoft.exchange.webservices.data.FindItemsResults;
import microsoft.exchange.webservices.data.Folder;
import microsoft.exchange.webservices.data.FolderView;
import microsoft.exchange.webservices.data.Item;
import microsoft.exchange.webservices.data.ItemId;
import microsoft.exchange.webservices.data.ItemSchema;
import microsoft.exchange.webservices.data.ItemView;
import microsoft.exchange.webservices.data.PropertySet;
import microsoft.exchange.webservices.data.SearchFilter;
import microsoft.exchange.webservices.data.SearchFilter.IsGreaterThan;
import microsoft.exchange.webservices.data.ServiceLocalException;
import microsoft.exchange.webservices.data.Task;
import microsoft.exchange.webservices.data.WebCredentials;
import microsoft.exchange.webservices.data.WellKnownFolderName;
import net.sf.iqser.plugin.msx.api.IExchangeApi;
import net.sf.iqser.plugin.msx.api.model.MsxAppointment;
import net.sf.iqser.plugin.msx.api.model.MsxContact;
import net.sf.iqser.plugin.msx.api.model.MsxFileAttachment;
import net.sf.iqser.plugin.msx.api.model.MsxMail;
import net.sf.iqser.plugin.msx.api.model.MsxNote;
import net.sf.iqser.plugin.msx.api.model.MsxTask;
import org.apache.log4j.Logger;

/**
 * Implementation for IExchangeApi.
 */
public class ExchangeApi implements IExchangeApi {

    /**
	 * Log4j logger.
	 */
    private static Logger logger = Logger.getLogger(ExchangeApi.class);

    /**
	 * Exchange service injection.
	 */
    private ExchangeService es;

    /**
	 * Constructor.
	 * @param username username for server connection.
	 * @param password password for server connection.
	 * @param serverLocation server location url.
	 * @param version version of Exchange service.
	 */
    public ExchangeApi(String username, String password, String serverLocation, String version) {
        super();
        try {
            WebCredentials wc = new WebCredentials(username, password);
            if ("Exchange2007_SP1".equals(version)) {
                es = new ExchangeService(ExchangeVersion.Exchange2007_SP1);
            } else if ("Exchange2010_SP1".equals(version)) {
                es = new ExchangeService(ExchangeVersion.Exchange2010_SP1);
            } else if ("Exchange2010".equals(version)) {
                es = new ExchangeService(ExchangeVersion.Exchange2010);
            } else {
                es = new ExchangeService(ExchangeVersion.Exchange2007_SP1);
            }
            es.setTraceEnabled(false);
            es.setUrl(new URI(serverLocation));
            es.setEnableScpLookup(true);
            es.setCredentials(wc);
        } catch (URISyntaxException e) {
            logger.error("Could not initialize exchnage service");
        }
    }

    /**
	 * Check if an item exists in the Exchange server.
	 * @param id the unique id of the item.
	 * @return true if the item with given id is found in MS Exchange.
	 */
    public boolean itemExists(String id) {
        try {
            Item item = Item.bind(es, new ItemId(id));
            if (item != null) return true; else return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
	 * Delete an item from server.
	 * @param id the unique id of the item to be deleted.
	 * @throws Exception if operation fails.
	 */
    public void delete(String id) throws Exception {
        Item item = Item.bind(es, new ItemId(id));
        item.delete(DeleteMode.HardDelete);
    }

    /**
	 * Get a list of MsxTask objects from Exchange server.
	 * @param filter map with filters for fetch action.
	 * @return list of MsxTask objects.
	 * @throws Exception if operation fails.
	 */
    public List<MsxTask> fetchTasks(Map<String, Object> filter) throws Exception {
        List<MsxTask> list = new ArrayList<MsxTask>();
        List<String> ids = fetchIds(filter, WellKnownFolderName.Tasks);
        for (String id : ids) {
            MsxTask fetchTaskById = fetchTaskById(id);
            if (fetchTaskById != null) list.add(fetchTaskById);
        }
        return list;
    }

    /**
	 * Retrieve MsxTask from Exchange server.
	 * @param id unique id of the item to be fetched.
	 * @return corresponding MsxTask for the Exchange server object with given id.
	 * @throws Exception if operation fails.
	 */
    public MsxTask fetchTaskById(String id) throws Exception {
        Item item = Item.bind(es, new ItemId(id));
        if (item instanceof Task) {
            Task serverTask = (Task) item;
            TaskConvertor convertor = new TaskConvertor(es);
            return convertor.convertToTask(serverTask);
        }
        return null;
    }

    /**
	 *  Retrieve an Exchange item for a given unique id.
	 * @param id unique id of the item to be fetched.
	 * @return item from Exchange server.
	 * @throws Exception
	 */
    private Item getItemById(String id) throws Exception {
        return Item.bind(es, new ItemId(id));
    }

    /**
	 * Save an email to Exchange server.
	 * @param task MsxTask object.
	 * @return the unique id from Exchange server.
	 * @throws Exception if operation fails.
	 */
    public String saveTask(MsxTask task) throws Exception {
        Task serverTask = new Task(es);
        TaskConvertor c = new TaskConvertor(es);
        serverTask = c.convertToServerTask(task);
        serverTask.save(WellKnownFolderName.Tasks);
        return serverTask.getId().getUniqueId();
    }

    /**
	 * Get a list of MsxNote objects from Exchange server.
	 * @param filter map with filters for fetch action.
	 * @return list of MsxNote objects.
	 * @throws Exception if operation fails.
	 */
    public List<MsxNote> fetchNotes(Map<String, Object> filter) throws Exception {
        List<MsxNote> list = new ArrayList<MsxNote>();
        List<String> ids = fetchIds(filter, WellKnownFolderName.Notes);
        NoteConvertor convertor = new NoteConvertor(es);
        for (String id : ids) {
            ItemId itemid = new ItemId(id);
            Item item = Item.bind(es, itemid);
            MsxNote convertToNote = convertor.convertToNote(item);
            list.add(convertToNote);
        }
        return list;
    }

    /**
	 * Retrieve MsxNote from Exchange server.
	 * @param id unique id of the item to be fetched.
	 * @return corresponding MsxNote for the Exchange server object with given id.
	 * @throws Exception if operation fails.
	 */
    public MsxNote fetchNoteById(String id) throws Exception {
        Item serverNote = (Item) getItemById(id);
        NoteConvertor convertor = new NoteConvertor(es);
        return convertor.convertToNote(serverNote);
    }

    /**
	 * Save an email to Exchange server.
	 * @param note MsxMail object.
	 * @return the unique id from Exchange server.
	 * @throws Exception if operation fails.
	 */
    public String saveNote(MsxNote note) throws Exception {
        EmailMessage serverNote = new EmailMessage(es);
        NoteConvertor c = new NoteConvertor(es);
        serverNote = c.convertToServerNote(note);
        serverNote.save(WellKnownFolderName.Notes);
        return serverNote.getId().getUniqueId();
    }

    /**
	 * Get a list of MsxMail objects from Exchange server.
	 * @param filter map with filters for fetching.
	 * @return list of MsxMail objects.
	 * @throws Exception if operation fails.
	 */
    public List<MsxMail> fetchEmails(Map<String, Object> filter) throws Exception {
        List<MsxMail> emails = new ArrayList<MsxMail>();
        List<String> ids = fetchIds(filter, WellKnownFolderName.Inbox);
        MailConvertor convertor = new MailConvertor(es);
        AttachmentConvertor attchConvertor = new AttachmentConvertor(es);
        for (String id : ids) {
            ItemId itemid = new ItemId(id);
            EmailMessage serverEmailMessage = EmailMessage.bind(es, itemid);
            try {
                MsxMail msxMail = convertor.convertToMail(serverEmailMessage);
                AttachmentCollection attachments = serverEmailMessage.getAttachments();
                if (attachments != null && attachments.getCount() > 0) {
                    Iterator<Attachment> iterator = attachments.iterator();
                    while (iterator.hasNext()) {
                        Attachment att = (Attachment) iterator.next();
                        MsxFileAttachment msxAttachment = attchConvertor.convertToAttachment(att, msxMail);
                        msxMail.getAttachments().add(msxAttachment);
                    }
                }
                emails.add(msxMail);
            } catch (ServiceLocalException e) {
                e.printStackTrace();
            }
        }
        return emails;
    }

    /**
	 * Retrieve MsxMail from Exchange server.
	 * @param id unique id of the item to be fetched.
	 * @return corresponding MsxMail for the Exchange server object with given id.
	 * @throws Exception if operation fails.
	 */
    public MsxMail fetchEmailById(String id) throws Exception {
        Item item = Item.bind(es, new ItemId(id));
        if (item instanceof EmailMessage) {
            EmailMessage em = (EmailMessage) item;
            MailConvertor convertor = new MailConvertor(es);
            return convertor.convertToMail(em);
        }
        return null;
    }

    /**
	 * Save an email to Exchange server.
	 * @param email MsxMail object.
	 * @return the unique id from Exchange server.
	 * @throws Exception if operation fails.
	 */
    public String saveMail(MsxMail email) throws Exception {
        EmailMessage serverEmail = new EmailMessage(es);
        MailConvertor c = new MailConvertor(es);
        serverEmail = c.convertToServerTask(email);
        serverEmail.save(WellKnownFolderName.Inbox);
        return serverEmail.getId().getUniqueId();
    }

    /**
	 * Get a list of MsxContact objects from Exchange server.
	 * @param filter map with filters for fetch action.
	 * @return list of MsxContact objects.
	 * @throws Exception if operation fails.
	 */
    public List<MsxContact> fetchContacts(Map<String, Object> filter) throws Exception {
        List<MsxContact> list = new ArrayList<MsxContact>();
        ContactConvertor convertor = new ContactConvertor(es);
        List<String> ids = fetchIds(filter, WellKnownFolderName.Contacts);
        for (String id : ids) {
            ItemId itemid = new ItemId(id);
            Contact contact = Contact.bind(es, itemid);
            MsxContact convertToContact = convertor.convertToContact(contact);
            list.add(convertToContact);
        }
        return list;
    }

    /**
	 * Retrieve MsxContact from Exchange server.
	 * @param id unique id of the item to be fetched.
	 * @return corresponding MsxContact for the Exchange server object with given id.
	 * @throws Exception if operation fails.
	 */
    public MsxContact fetchContactById(String id) throws Exception {
        Item item = Item.bind(es, new ItemId(id));
        if (item instanceof Contact) {
            Contact serverContact = (Contact) item;
            ContactConvertor convertor = new ContactConvertor(es);
            return convertor.convertToContact(serverContact);
        }
        return null;
    }

    /**
	 * Save an email to Exchange server.
	 * @param contact MsxContact object.
	 * @return the unique id from Exchange server.
	 * @throws Exception if operation fails.
	 */
    public String saveContact(MsxContact contact) throws Exception {
        Contact serverContact = new Contact(es);
        ContactConvertor cv = new ContactConvertor(es);
        serverContact = cv.convertToServerTask(contact);
        serverContact.save(WellKnownFolderName.Contacts);
        return serverContact.getId().getUniqueId();
    }

    /**
	 * Get a list of MsxAppointment objects from Exchange server.
	 * @param filter map with filters for fetch action.
	 * @return list of MsxAppointment objects.
	 * @throws Exception if operation fails.
	 */
    public List<MsxAppointment> fetchAppointments(Map<String, Object> filter) throws Exception {
        List<MsxAppointment> list = new ArrayList<MsxAppointment>();
        AppointmentConvertor convertor = new AppointmentConvertor(es);
        List<String> ids = fetchIds(filter, WellKnownFolderName.Calendar);
        for (String id : ids) {
            ItemId itemid = new ItemId(id);
            Appointment appointment = Appointment.bind(es, itemid);
            MsxAppointment convertToAppointment = convertor.convertToAppointment(appointment);
            list.add(convertToAppointment);
        }
        return list;
    }

    /**
	 * Retrieve MsxAppointment from Exchange server.
	 * @param id unique id of the item to be fetched.
	 * @return corresponding MsxAppointment for the Exchange server object with given id.
	 * @throws Exception if operation fails.
	 */
    public MsxAppointment fetchAppointmentById(String id) throws Exception {
        Item item = Item.bind(es, new ItemId(id));
        if (item instanceof Appointment) {
            Appointment serverAppt = (Appointment) item;
            AppointmentConvertor convertor = new AppointmentConvertor(es);
            return convertor.convertToAppointment(serverAppt);
        }
        return null;
    }

    /**
	 * Save an email to Exchange server.
	 * @param appointment MsxAppointment object.
	 * @return the unique id from Exchange server.
	 * @throws Exception if operation fails.
	 */
    public String saveAppointment(MsxAppointment appointment) throws Exception {
        Appointment serverAppt = new Appointment(es);
        AppointmentConvertor c = new AppointmentConvertor(es);
        serverAppt = c.convertToServerAppointment(appointment);
        serverAppt.save(WellKnownFolderName.Calendar);
        return serverAppt.getId().getUniqueId();
    }

    /**
	 * Get a list of MsxMail objects from Exchange server.
	 * @param filter map with filters for fetching.
	 * @return list of MsxMail objects.
	 * @throws Exception if operation fails.
	 */
    public List<String> fetchEmails(SearchFilter filter) throws Exception {
        List<String> ids = new ArrayList<String>();
        Folder folder = Folder.bind(es, WellKnownFolderName.Inbox);
        ItemView view = new ItemView(Integer.MAX_VALUE);
        view.setPropertySet(new PropertySet(BasePropertySet.IdOnly));
        FindItemsResults<Item> findResults = folder.findItems(filter, view);
        for (Item item : findResults) {
            ids.add(item.getId().getUniqueId());
        }
        return ids;
    }

    /**
	 * Retrieve corresponding attachments for a given email.
	 * @param emailId id of the email.
	 * @return list of attachments.
	 * @throws Exception if operation fails.
	 */
    public List<MsxFileAttachment> loadAttachmentsForEmail(String emailId) throws Exception {
        List<MsxFileAttachment> msxAttachments = new ArrayList<MsxFileAttachment>();
        AttachmentConvertor c = new AttachmentConvertor(es);
        MailConvertor emailConvertor = new MailConvertor(es);
        ItemId itemId = new ItemId(emailId);
        EmailMessage emailMessage = EmailMessage.bind(es, itemId);
        MsxMail msxMail = emailConvertor.convertToMail(emailMessage);
        AttachmentCollection attachments = emailMessage.getAttachments();
        for (Attachment attachment : attachments) {
            MsxFileAttachment msxFileAttachment = c.convertToAttachment(attachment, msxMail);
            msxAttachments.add(msxFileAttachment);
        }
        return msxAttachments;
    }

    /**
	 * Retrieve a single attachment from server.
	 * @param emailId id of the parent email.
	 * @param attachmentId id of fetched attachment.
	 * @return the MsxFileAttachment object.
	 * @throws Exception if operation fails.
	 */
    public MsxFileAttachment loadAttachment(String emailId, String attachmentId) throws Exception {
        List<MsxFileAttachment> attachmentsForEmail = loadAttachmentsForEmail(emailId);
        for (MsxFileAttachment msxFileAttachment : attachmentsForEmail) {
            if (msxFileAttachment.getId().equalsIgnoreCase(attachmentId)) {
                return msxFileAttachment;
            }
        }
        return null;
    }

    /**
	 * Retrieve a list of id's from server.
	 * @param filter a map with filters for fetching.
	 * @param folderName source folder for fetching the id's .
	 * @return list of string id's.
	 * @throws Exception if operation fails.
	 */
    public List<String> fetchIds(Map<String, Object> filter, WellKnownFolderName folderName) throws Exception {
        List<String> ids = new ArrayList<String>();
        Folder folder = Folder.bind(es, folderName);
        ItemView view = new ItemView(Integer.MAX_VALUE);
        view.setPropertySet(new PropertySet(BasePropertySet.IdOnly));
        recursiveFolders(filter, folder, view, ids);
        return ids;
    }

    /**
	 * Retrieve a list of id's from server.
	 * @param filter a map with filters for fetching.
	 * @param folderNames list of folders for fetching.
	 * @return list of string id's.
	 * @throws Exception if operation fails.
	 */
    public List<String> fetchIds(Map<String, Object> filter, List<WellKnownFolderName> folderNames) throws Exception {
        List<String> ids = new ArrayList<String>();
        for (WellKnownFolderName wellKnownFolderName : folderNames) {
            List<String> fetchIds = fetchIds(filter, wellKnownFolderName);
            ids.addAll(fetchIds);
        }
        return ids;
    }

    /**
	 * Search folders recursively and fetch items found.
	 * @param filter map with filters.
	 * @param folder parent folder name.
	 * @param view ItemView object attached to folder binding.
	 * @param ids list of id's to be populated.
	 * @throws Exception if operation fails.
	 */
    private void recursiveFolders(Map<String, Object> filter, Folder folder, ItemView view, List<String> ids) throws Exception {
        FolderView fview = new FolderView(Integer.MAX_VALUE);
        FindItemsResults<Item> findResults;
        if (filter != null) {
            Date lastSynchDate = (Date) filter.get("lastSynchTime");
            IsGreaterThan isGreaterThan = new SearchFilter.IsGreaterThan(ItemSchema.DateTimeReceived, lastSynchDate);
            findResults = folder.findItems(isGreaterThan, view);
        } else {
            findResults = folder.findItems(view);
        }
        for (Item item : findResults) {
            ids.add(item.getId().getUniqueId());
        }
        FindFoldersResults frez = folder.findFolders(fview);
        if (frez != null && frez.getTotalCount() > 0) {
            for (Folder subfolder : frez.getFolders()) {
                recursiveFolders(filter, subfolder, view, ids);
            }
        }
    }
}
