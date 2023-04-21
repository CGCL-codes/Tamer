package org.wikiup.modules.fileupload;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.wikiup.core.inf.Document;
import org.wikiup.core.util.Assert;
import org.wikiup.core.util.Documents;
import org.wikiup.core.util.FileUtil;
import org.wikiup.core.util.ValueUtil;
import org.wikiup.servlet.ServletProcessorContext;

public class UploadUtil {

    public static List parseUploadParameters(ServletProcessorContext context, ServletFileUpload upload) {
        List items = null;
        try {
            int i;
            items = upload.parseRequest(context.getServletRequest());
            for (i = 0; i < items.size(); i++) {
                FileItem item = (FileItem) items.get(i);
                if (item.isFormField()) context.getExtraParameters().put(item.getFieldName(), convert(item.getString(), "iso8859-1", "utf-8"));
            }
        } catch (Exception ex) {
            Assert.fail(ex);
        }
        return items;
    }

    public static void loadUploadConfig(Document node, ServletFileUpload upload) {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(ValueUtil.toInteger(Documents.getDocumentValue(node, "size-threshold", null), 4096));
        factory.setRepository(getTemporaryPath(Documents.getDocumentValue(node, "temporary-path", "C:\\WINDOWS\\Temp;/tmp")));
        upload.setSizeMax(ValueUtil.toInteger(Documents.getDocumentValue(node, "max-size", null), 10000000));
        upload.setFileItemFactory(factory);
    }

    private static File getTemporaryPath(String path) {
        String list[] = path.split(";");
        int i;
        for (i = 0; i < list.length; i++) if (FileUtil.isDirectory(list[i])) return new File(list[i]);
        return null;
    }

    private static String convert(String value, String fromCharset, String toCharset) {
        try {
            return new String(value.getBytes(fromCharset), toCharset);
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }
}
