package org.jcompany.control.jsf.office;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipException;
import org.jcompany.commons.PlcBaseEntity;
import org.jcompany.commons.helper.PlcEntityHelper;
import org.jcompany.commons.io.PlcZip;
import org.jcompany.control.jsf.velocity.PlcParseVelocity;

public class PlcGenerateDocumentOffice {

    public void generateFileDocumentoOffice(InputStream template, String dirTemp, String fileName, Object entity) throws ZipException, IOException {
        unzipTemplate(template, dirTemp);
        manipulateDoc(dirTemp + "/word/document.xml");
        parseVelocity(dirTemp + "/word/document.xml", entity);
        zipDocx(fileName, dirTemp);
    }

    private void manipulateDoc(String file) throws IOException {
        PlcInterpretsDoc.manipulateDocParser(file);
    }

    private void parseVelocity(String template, Object entity) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(PlcEntityHelper.getInstance().getPropertyNamePlc(entity), entity);
        map.put("dataAtual", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        PlcParseVelocity.parse(template, template, map);
    }

    private void zipDocx(String fileName, String dirTemp) throws ZipException, IOException {
        PlcZip zip = new PlcZip(dirTemp + File.separator + fileName, false);
        zip.compactar(dirTemp);
    }

    private void unzipTemplate(InputStream input, String dirTemp) throws ZipException, IOException {
        File file = new File(dirTemp + File.separator + "file.tmp");
        FileOutputStream output = new FileOutputStream(file);
        int ch = 0;
        while ((ch = input.read()) > -1) {
            output.write(ch);
        }
        input.close();
        output.close();
        PlcZip zip = new PlcZip(dirTemp + File.separator + "file.tmp");
        zip.descompactar(dirTemp);
        zip.close();
        file.delete();
    }
}
