package com.sitescape.team.servlet.administration;

import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.activation.FileTypeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.dom4j.Document;
import org.springframework.web.servlet.ModelAndView;
import com.sitescape.team.util.XmlFileUtil;
import com.sitescape.team.web.servlet.SAbstractController;
import com.sitescape.util.Validator;

public abstract class ZipDownloadController extends SAbstractController {

    protected abstract String getFilename();

    protected abstract NamedDocument getDocumentForId(String defId);

    private FileTypeMap mimeTypes;

    protected FileTypeMap getFileTypeMap() {
        return mimeTypes;
    }

    public void setFileTypeMap(FileTypeMap mimeTypes) {
        this.mimeTypes = mimeTypes;
    }

    protected ModelAndView handleRequestAfterValidation(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String filename = getFilename();
        response.setContentType(mimeTypes.getContentType(filename));
        response.setHeader("Cache-Control", "private");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream());
        Map formData = request.getParameterMap();
        Iterator itFormData = formData.entrySet().iterator();
        while (itFormData.hasNext()) {
            Map.Entry me = (Map.Entry) itFormData.next();
            if (((String) me.getKey()).startsWith("id_")) {
                String defId = ((String) me.getKey()).substring(3);
                if (defId.startsWith("%")) defId = defId.substring(1);
                if (Validator.isNotNull(defId)) {
                    try {
                        NamedDocument doc = getDocumentForId(defId);
                        zipOut.putNextEntry(new ZipEntry(Validator.replacePathCharacters(doc.name) + ".xml"));
                        XmlFileUtil.writeFile(doc.doc, zipOut);
                    } catch (Exception ex) {
                    }
                }
            }
        }
        zipOut.finish();
        return null;
    }

    protected class NamedDocument {

        public String name;

        public Document doc;

        public NamedDocument(String name, Document doc) {
            this.name = name;
            this.doc = doc;
        }
    }
}
