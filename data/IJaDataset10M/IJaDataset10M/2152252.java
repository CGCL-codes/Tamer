package net.sf.iqser.plugin.mail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import net.sf.iqser.plugin.file.parser.FileParser;
import net.sf.iqser.plugin.file.parser.FileParserException;
import net.sf.iqser.plugin.file.parser.FileParserFactory;
import net.sf.iqser.plugin.mail.content.Utils;
import com.iqser.core.model.Attribute;
import com.iqser.core.model.Content;

/**
 * zip attachment creator.
 * @author alexandru.galos
 *
 */
public class ZipAttachment {

    /**
	 * creates zip attachments.
	 * @param zipIS the input stream of the zip file
	 * @param mailCont   the mail content object
	 * @param fileName   the zip file name
	 * @param index      the index of the entry in a zip attachment
	 * @return   a collection of attachment contents
	 * @throws IOException io exception if exception occurs while reading the file
	 * @throws FileParserException  exception occurs while parsing the file
	 */
    public Collection<Content> createZipAttachments(InputStream zipIS, Content mailCont, String fileName, int index) throws IOException, FileParserException {
        String url = mailCont.getContentUrl() + fileName;
        ZipInputStream zip = new ZipInputStream(zipIS);
        ZipEntry zipEntry = null;
        Collection<Content> contents = new ArrayList<Content>();
        int off = 0;
        while ((zipEntry = zip.getNextEntry()) != null) {
            byte[] b = new byte[2048];
            int size = 0;
            if (zip.available() == 1) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                while ((size = zip.read(b, off, b.length)) != -1) bos.write(b, 0, size);
                bos.flush();
                bos.close();
                if (!zipEntry.isDirectory()) {
                    String urlZipEntry = zipEntry.getName();
                    Content content = null;
                    byte[] byteArray = bos.toByteArray();
                    InputStream is = new ByteArrayInputStream(byteArray);
                    FileParserFactory parserFactory = FileParserFactory.getInstance();
                    FileParser parser = parserFactory.getFileParser(is);
                    if (parser != null) {
                        String contentURL = "zip://" + url + "!/" + urlZipEntry;
                        content = Utils.parseFileContent(urlZipEntry, parser, byteArray);
                        content.setContentUrl(contentURL);
                        contents.add(content);
                        Attribute attribute = new Attribute();
                        attribute.setKey(false);
                        attribute.setName("ATTACHED_TO");
                        attribute.setValue(content.getContentUrl());
                        attribute.setType(Attribute.ATTRIBUTE_TYPE_TEXT);
                        content.addAttribute(attribute);
                        attribute = new Attribute();
                        attribute.setKey(true);
                        attribute.setName("MESSAGE_ATTACHMENTS_NAME_" + index);
                        attribute.setValue(contentURL);
                        attribute.setType(Attribute.ATTRIBUTE_TYPE_TEXT);
                        content.addAttribute(attribute);
                    }
                }
            }
        }
        return contents;
    }
}
