package org.vosao.webdav.sysfile.global;

import java.util.Date;
import org.vosao.business.Business;
import org.vosao.webdav.AbstractFileResource;

public class MessagesFileResource extends AbstractFileResource {

    public MessagesFileResource(Business aBusiness, String name) {
        super(aBusiness, name, new Date());
        setContentType("text/xml");
        setData(getBusiness().getImportExportBusiness().getExporterFactory().getMessagesExporter().createMessagesXML());
    }
}
