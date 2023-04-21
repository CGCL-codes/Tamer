package org.vosao.webdav.sysfile.local;

import java.util.Date;
import java.util.List;
import org.vosao.business.Business;
import org.vosao.entity.PageEntity;
import org.vosao.webdav.AbstractFileResource;

public class ContentFileResource extends AbstractFileResource {

    private List<PageEntity> pages;

    public ContentFileResource(Business aBusiness, List<PageEntity> aPages) {
        super(aBusiness, "_content.xml", new Date());
        setContentType("text/xml");
        pages = aPages;
        setData(getBusiness().getImportExportBusiness().getExporterFactory().getPageExporter().createPageContentXML(pages.get(0)));
    }
}
