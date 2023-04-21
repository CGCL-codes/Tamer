package net.sourceforge.seqware.webservice.resources.tables;

import java.io.IOException;
import net.sf.beanlib.hibernate3.Hibernate3DtoCopier;
import net.sourceforge.seqware.common.business.FileService;
import net.sourceforge.seqware.common.factory.BeanFactory;
import net.sourceforge.seqware.common.model.File;
import net.sourceforge.seqware.common.util.xmltools.JaxbObject;
import net.sourceforge.seqware.common.util.xmltools.XmlTools;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Put;
import org.restlet.resource.ResourceException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author mtaschuk
 */
public class FileIDResource extends DatabaseIDResource {

    public FileIDResource() {
        super("fileId");
    }

    @Get
    public void getXml() {
        FileService ss = BeanFactory.getFileServiceBean();
        File file = (File) testIfNull(ss.findBySWAccession(Integer.parseInt(getId())));
        Hibernate3DtoCopier copier = new Hibernate3DtoCopier();
        JaxbObject<File> jaxbTool = new JaxbObject<File>();
        File dto = copier.hibernate2dto(File.class, file);
        Document line = XmlTools.marshalToDocument(jaxbTool, dto);
        getResponse().setEntity(XmlTools.getRepresentation(line));
    }

    @Override
    @Put
    public Representation put(Representation entity) {
        authenticate();
        Representation representation = null;
        String text = null;
        try {
            JaxbObject<File> jo = new JaxbObject<File>();
            text = entity.getText();
            File newFile = (File) XmlTools.unMarshal(jo, new File(), text);
            FileService fs = BeanFactory.getFileServiceBean();
            File file = fs.findByID(newFile.getFileId());
            file.setDescription(newFile.getDescription());
            file.setFilePath(newFile.getFilePath());
            file.setFileType(newFile.getFileType());
            file.setIsSelected(newFile.getIsSelected());
            file.setMd5sum(newFile.getMd5sum());
            file.setMetaType(newFile.getMetaType());
            if (newFile.getOwner() != null) {
                file.setOwner(newFile.getOwner());
            } else if (file.getOwner() == null) {
                file.setOwner(registration);
            }
            file.setSwAccession(newFile.getSwAccession());
            file.setType(newFile.getType());
            file.setUrl(newFile.getUrl());
            file.setUrlLabel(newFile.getUrlLabel());
            fs.update(registration, file);
            Hibernate3DtoCopier copier = new Hibernate3DtoCopier();
            File detachedFile = copier.hibernate2dto(File.class, file);
            Document line = XmlTools.marshalToDocument(jo, detachedFile);
            representation = XmlTools.getRepresentation(line);
            getResponse().setEntity(representation);
            getResponse().setLocationRef(getRequest().getRootRef() + "/files/" + detachedFile.getSwAccession());
            getResponse().setStatus(Status.SUCCESS_CREATED);
        } catch (SecurityException e) {
            getResponse().setStatus(Status.CLIENT_ERROR_FORBIDDEN, e.getMessage());
        } catch (SAXException ex) {
            ex.printStackTrace();
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "XML Parsing exception");
        } catch (IOException e) {
            e.printStackTrace();
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "IO Exception");
        } catch (Exception e) {
            e.printStackTrace();
            getResponse().setStatus(Status.SERVER_ERROR_INTERNAL, e.getMessage());
        }
        return representation;
    }
}
