package org.tripcom.query.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import com.profium.sir.sparql.core.ParseException;
import com.profium.sir.sparql.core.SPARQLParser;

public class ParseQueryServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain");
        if (ServletFileUpload.isMultipartContent(request)) {
            ServletFileUpload servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
            try {
                List<FileItem> fileItemsList = servletFileUpload.parseRequest(request);
                Iterator it = fileItemsList.iterator();
                while (it.hasNext()) {
                    FileItem fileItem = (FileItem) it.next();
                    if (fileItem.isFormField()) {
                        System.out.println("?");
                    } else {
                        byte[] data = fileItem.get();
                        String query = new String(data, "utf-8");
                        String validationResult = validateQuery(query);
                        if (validationResult != null) query = validationResult + "\n\n" + query;
                        System.out.println("jee" + query);
                        PrintWriter out = response.getWriter();
                        out.print(query);
                    }
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
            }
        }
    }

    private String validateQuery(String query) {
        SPARQLParser parser = new SPARQLParser(new StringReader(query.toString()));
        try {
            parser.Query();
            return null;
        } catch (ParseException e) {
            return e.getMessage();
        }
    }
}
