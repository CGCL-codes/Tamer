package sfeir.gwt.ergosoom.server;

import java.io.IOException;
import java.net.URL;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;
import sfeir.gwt.ergosoom.client.model.Person;
import sfeir.gwt.ergosoom.server.service.ProfileService;
import sfeir.gwt.ergosoom.server.util.VCardParser;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

@SuppressWarnings("serial")
@Singleton
public class ImportServlet extends HttpServlet {

    @Inject
    private Injector injector;

    private ProfileService getProfileService() {
        return injector.getInstance(ProfileService.class);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Person p = null;
        if (ServletFileUpload.isMultipartContent(req)) {
            ServletFileUpload upload = new ServletFileUpload();
            try {
                FileItemIterator it = upload.getItemIterator(req);
                URL requestUrl = null;
                while (it.hasNext()) {
                    FileItemStream item = it.next();
                    if ("uploadVCard".equals(item.getFieldName())) {
                        p = VCardParser.parse(Streams.asString(item.openStream()));
                        if (null != p) break;
                    } else if ("telDomain".equals(item.getFieldName())) requestUrl = new URL("http://pilot.webproxy.nic.tel/vcard.action?domain=" + Streams.asString(item.openStream())); else if ("webSite".equals(item.getFieldName())) requestUrl = new URL("http://technorati.com/contacts/contact/?url=" + Streams.asString(item.openStream())); else if ("photo".equals(item.getFieldName())) {
                        String url = "http://" + req.getHeader("Host") + "/picture/" + getProfileService().saveProfilePicture(item.getContentType(), IOUtils.toByteArray(item.openStream()));
                        resp.getWriter().print(url);
                        return;
                    }
                    if (null != requestUrl) {
                        p = VCardParser.parse(Streams.asString(requestUrl.openStream()));
                        if (null != p) break;
                    }
                }
            } catch (FileUploadException e) {
                e.printStackTrace();
            }
            if (null != p) {
                if (req.getHeader("user-agent").indexOf("Chrome") >= 0) resp.setCharacterEncoding("UTF-8");
                resp.getWriter().print("@ERGOSOOM@PERSON!#@" + Person.blob(p) + "@ERGOSOOM@PERSON!#@");
            }
        }
    }
}
