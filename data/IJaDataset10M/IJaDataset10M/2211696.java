package org.archive.crawler.restlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import org.apache.commons.lang.StringEscapeUtils;
import org.restlet.data.CharacterSet;
import org.restlet.data.MediaType;
import org.restlet.data.Reference;
import org.restlet.resource.CharacterRepresentation;
import org.restlet.resource.FileRepresentation;

/**
 * Representation wrapping a FileRepresentation, displaying its contents
 * in a TextArea for editting. 
 * 
 * @contributor gojomo
 */
public class EditRepresentation extends CharacterRepresentation {

    FileRepresentation fileRepresentation;

    EnhDirectoryResource dirResource;

    public EditRepresentation(FileRepresentation representation, EnhDirectoryResource resource) {
        super(MediaType.TEXT_HTML);
        fileRepresentation = representation;
        dirResource = resource;
        setCharacterSet(CharacterSet.UTF_8);
    }

    @Override
    public Reader getReader() throws IOException {
        StringWriter writer = new StringWriter((int) fileRepresentation.getSize() + 100);
        write(writer);
        return new StringReader(writer.toString());
    }

    @Override
    public void write(Writer writer) throws IOException {
        PrintWriter pw = new PrintWriter(writer);
        pw.println("<head><title>" + fileRepresentation.getFile().getName() + "</title></head>");
        Flash.renderFlashesHTML(pw, dirResource.getRequest());
        pw.println("<form method='POST'>");
        pw.println("<input type='submit' value='save changes'/>");
        pw.println(fileRepresentation.getFile());
        Reference viewRef = dirResource.getRequest().getOriginalRef().clone();
        viewRef.setQuery(null);
        pw.println("<a href='" + viewRef + "'>view</a>");
        pw.println("<br/>");
        pw.println("<textarea style='width:100%;height:94%;' name='contents'>");
        StringEscapeUtils.escapeHtml(pw, fileRepresentation.getText());
        pw.println("</textarea></form>");
        pw.close();
    }

    public FileRepresentation getFileRepresentation() {
        return fileRepresentation;
    }
}
