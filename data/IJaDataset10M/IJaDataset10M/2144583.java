package br.com.mcampos.controller.anoto.util;

import br.com.mcampos.dto.anoto.AnotoResultList;
import br.com.mcampos.dto.anoto.FormDTO;
import br.com.mcampos.dto.anoto.PGCDTO;
import br.com.mcampos.dto.anoto.PgcFieldDTO;
import br.com.mcampos.dto.anoto.PgcPageDTO;
import br.com.mcampos.dto.security.AuthenticationDTO;
import br.com.mcampos.dto.system.FieldTypeDTO;
import br.com.mcampos.dto.system.MediaDTO;
import br.com.mcampos.ejb.cloudsystem.anode.facade.AnotoExportFacade;
import br.com.mcampos.exception.ApplicationException;
import br.com.mcampos.sysutils.SysUtils;
import br.com.mcampos.util.locator.ServiceLocator;
import br.com.mcampos.util.locator.ServiceLocatorException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Date;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class AnotoExport {

    private List<AnotoResultList> currentList = null;

    private AuthenticationDTO user;

    private Boolean exportImages = false;

    private FormDTO form = null;

    AnotoExportFacade session;

    private static final String PATH = "c:\\temp\\anoto_res\\export";

    public AnotoExport(AuthenticationDTO user) {
        super();
        setUser(user);
    }

    public AnotoExport(AuthenticationDTO user, FormDTO form) {
        super();
        setUser(user);
        setForm(form);
    }

    public AnotoExport(AuthenticationDTO user, List<AnotoResultList> currentList) {
        super();
        setUser(user);
        setCurrentList(currentList);
    }

    public Document exportToXML() throws ApplicationException {
        int pgcId = -1;
        int bookId = -1;
        Element form = null;
        Element book = null;
        Element root;
        Element pages = null;
        Date now = new Date();
        if (SysUtils.isEmpty(getCurrentList())) return null;
        root = new Element("Export");
        root.setAttribute("timestamp", now.toString());
        for (AnotoResultList item : getCurrentList()) {
            if (item.getPgcPage().getPgc().getId().equals(pgcId) == false) {
                pgcId = item.getPgcPage().getPgc().getId();
                form = createFormElement(item);
                root.addContent(form);
                bookId = -1;
            }
            if (item.getPgcPage().getBookId().equals(bookId) == false) {
                bookId = item.getPgcPage().getBookId();
                book = createBookElement(item);
                pages = new Element("Pages");
                book.addContent(pages);
                form.addContent(book);
            }
            exportToXML(item, pages);
        }
        return new Document(root);
    }

    public void exportToFile(FormDTO form) throws ApplicationException {
        setForm(form);
        exportToFile();
    }

    private void createFile(Element root, int pgc_id) {
        Document doc = new Document(root);
        XMLOutputter output = new XMLOutputter(Format.getPrettyFormat());
        String str = output.outputString(doc);
        try {
            String path = String.format("%s\\%d.xml", getPath(), pgc_id);
            File file = new File(path);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(str);
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Element exportAttach(PGCDTO pgc) throws ApplicationException {
        if (pgc == null) return null;
        List<MediaDTO> medias = getSession().getAttachments(getUser(), pgc);
        if (SysUtils.isEmpty(medias)) return null;
        Element attachs = new Element("Attachs");
        for (MediaDTO media : medias) {
            Element attach = new Element("Attach");
            attach.setAttribute("mime", media.getMimeType());
            String attachFileName = String.format("%d_%d_image_attach.%s", pgc.getId(), media.getId(), media.getFormat());
            attach.setText(attachFileName);
            attachs.addContent(attach);
            try {
                String imageFilename = String.format("%s\\%s", getPath(), attachFileName);
                File file = new File(imageFilename);
                FileOutputStream writer = new FileOutputStream(file);
                writer.write(media.getObject());
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return attachs;
    }

    private void createFile(PgcPageDTO item, MediaDTO media) {
        try {
            String imageFilename = String.format("%s\\%d_%d_%d_%d_page_image.jpg", getPath(), item.getPgc().getId(), item.getBookId() + 1, item.getPageId() + 1, media.getId());
            File file = new File(imageFilename);
            FileOutputStream writer = new FileOutputStream(file);
            writer.write(media.getObject());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getPath() {
        String path = PATH;
        File file = new File(path);
        if (file.exists() == false) file.mkdirs();
        return path;
    }

    public void exportToFile() throws ApplicationException {
        List<PgcPageDTO> list;
        int oldPgc = 0, oldBook = 0;
        Element root = null;
        Element form = null;
        Element book = null;
        int contador = 1;
        list = getSession().getPages(getUser(), getForm());
        if (SysUtils.isEmpty(list)) return;
        for (PgcPageDTO item : list) {
            if (oldPgc != item.getPgc().getId()) {
                if (root != null) {
                    System.out.println("[" + contador + "]" + "***  Gravando pgc: " + item.getPgc().getId());
                    createFile(root, oldPgc);
                    root = null;
                    contador++;
                }
                oldPgc = item.getPgc().getId();
                root = new Element("Export");
                Date now = new Date();
                root.setAttribute("timestamp", now.toString());
                form = createFormElement(item);
                root.addContent(form);
                Element attachs = exportAttach(item.getPgc());
                if (attachs != null) root.addContent(attachs);
                oldBook = 0;
            }
            if (oldBook != (item.getBookId() + 1)) {
                System.out.println("***  Abrindo novo book: " + item.getBookId() + 1);
                oldBook = (item.getBookId() + 1);
                book = new Element("Book");
                book.setAttribute("id", "" + oldBook);
                form.addContent(book);
            }
            System.out.println(String.format("Pgc: %d. Book: %d. Page: %d", item.getPgc().getId(), item.getBookId() + 1, item.getPageId() + 1));
            Element page = createPageElement(item);
            page.setAttribute("id", "" + (item.getPageId() + 1));
            book.addContent(page);
            List<MediaDTO> medias = getSession().getImages(user, item);
            if (SysUtils.isEmpty(medias) == false) {
                for (MediaDTO media : medias) {
                    Element image = new Element("Image");
                    String imageFilename = String.format("%d_%d_%d_%d_page_image.jpg", item.getPgc().getId(), item.getBookId() + 1, item.getPageId() + 1, media.getId());
                    System.out.println("\tExporting " + imageFilename);
                    image.setText(imageFilename);
                    page.addContent(image);
                    image.setAttribute("mime", "image/jpeg");
                    createFile(item, media);
                }
            } else System.out.println("\tNão existem imagems para esta página");
        }
        if (root != null) {
            createFile(root, oldPgc);
        }
    }

    public Element exportToXML(AnotoResultList item, Element root) throws ApplicationException {
        String xml = "";
        Element page = createPageElement(item.getPgcPage());
        root.addContent(page);
        return root;
    }

    protected Element createFormElement(AnotoResultList r) {
        Element root = new Element("Form");
        root.setAttribute("id", "" + r.getPgcPage().getPgc().getId());
        root.addContent(new Element("Application").setText(r.getForm().getApplication()));
        root.addContent(new Element("Description").setText(r.getForm().getDescription()));
        root.addContent(new Element("Pen").setText(r.getPen().getId()));
        return root;
    }

    protected Element createFormElement(PgcPageDTO r) {
        Element root = new Element("Form");
        root.setAttribute("id", "" + r.getPgc().getId());
        root.addContent(new Element("Application").setText(r.getAnotoPage().getPad().getForm().getApplication()));
        root.addContent(new Element("Description").setText(r.getAnotoPage().getPad().getForm().getDescription()));
        root.addContent(new Element("Pen").setText(r.getPgc().getPenId()));
        return root;
    }

    protected Element createBookElement(AnotoResultList r) {
        Element root = new Element("Book");
        int id = r.getPgcPage().getBookId();
        id++;
        root.setAttribute("id", "" + id);
        return root;
    }

    protected Element createPageElement(PgcPageDTO pgcPage) throws ApplicationException {
        Element page = new Element("Page");
        page.setAttribute("Address", pgcPage.getAnotoPage().getPageAddress());
        List<PgcFieldDTO> fields = getSession().getFields(user, pgcPage);
        Element xmlFields = new Element("Fields");
        page.addContent(xmlFields);
        if (SysUtils.isEmpty(fields)) return page;
        for (PgcFieldDTO field : fields) {
            xmlFields.addContent(createField(field));
        }
        return page;
    }

    private Element exportImage(Element page, PgcPageDTO pgcPage) throws ApplicationException {
        if (page == null) return page;
        if (pgcPage == null) return page;
        if (getExportImages() == false) return page;
        List<MediaDTO> medias = getSession().getImages(user, pgcPage);
        if (SysUtils.isEmpty(medias)) return page;
        for (MediaDTO media : medias) {
            try {
                String value = SysUtils.getHexString(media.getObject(), "UTF-8");
                Element eImage = new Element("Image");
                eImage.setAttribute("mime", "image/jpeg");
                eImage.setText("0x" + value);
                page.addContent(eImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return page;
    }

    protected Element createField(PgcFieldDTO field) {
        Element xmlField = new Element("Field");
        xmlField.addContent(new Element("Name").setText(field.getName()));
        if (field.getType() == null) field.setType(new FieldTypeDTO(1, "String"));
        xmlField.setAttribute("type", field.getType().getDescription());
        String value;
        if (field.getType().getId().equals(FieldTypeDTO.typeBoolean)) value = field.getHasPenstrokes() ? "true" : "false"; else value = SysUtils.isEmpty(field.getRevisedText()) ? field.getIrcText() : field.getRevisedText();
        Element xmlValue = new Element("value");
        xmlValue.setText(value);
        xmlField.addContent(xmlValue);
        xmlField.addContent(new Element("HasPenstrokes").setText(field.getHasPenstrokes() ? "true" : "false"));
        if (field.getHasPenstrokes()) {
            if (field.getStartTime() != null) xmlField.addContent(new Element("StartTime").setText(field.getStartTime().toString()));
            if (field.getEndTime() != null) xmlField.addContent(new Element("EndTime").setText(field.getEndTime().toString()));
        }
        return xmlField;
    }

    public void setCurrentList(List<AnotoResultList> currentList) {
        this.currentList = currentList;
    }

    public List<AnotoResultList> getCurrentList() {
        return currentList;
    }

    public void setUser(AuthenticationDTO user) {
        this.user = user;
    }

    public AuthenticationDTO getUser() {
        return user;
    }

    protected AnotoExportFacade getRemoteSession() {
        try {
            return (AnotoExportFacade) ServiceLocator.getInstance().getRemoteSession(AnotoExportFacade.class);
        } catch (ServiceLocatorException e) {
            throw new NullPointerException("Invalid EJB Session (possible null)");
        }
    }

    public AnotoExportFacade getSession() {
        if (session == null) session = getRemoteSession();
        return session;
    }

    public void setExportImages(Boolean exportImages) {
        this.exportImages = exportImages;
    }

    public Boolean getExportImages() {
        return exportImages;
    }

    public void setForm(FormDTO form) {
        this.form = form;
    }

    public FormDTO getForm() {
        return form;
    }
}
