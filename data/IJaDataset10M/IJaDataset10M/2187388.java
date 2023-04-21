package pierre.webApplication;

import pierre.reports.*;
import pierre.api.PierreService;
import pierre.model.*;
import pedro.soa.security.User;
import java.util.ArrayList;
import java.io.IOException;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.struts.action.Action;
import org.apache.struts.util.MessageResources;
import org.apache.struts.taglib.html.*;
import java.util.Locale;
import java.io.*;
import java.net.URL;

public final class PrintStyleSheetReference extends TagSupport {

    private HTMLUtility htmlUtility;

    public PrintStyleSheetReference() {
        htmlUtility = new HTMLUtility();
    }

    public int doStartTag() throws JspException {
        JspWriter out = pageContext.getOut();
        HttpSession session = pageContext.getSession();
        try {
            PierreService service = (PierreService) session.getAttribute(WebApplicationConstants.SERVICE);
            BrowserConfigurationModel browserConfigurationModel = service.getBrowserConfigurationModel();
            GeneralInformationModel generalInformationModel = browserConfigurationModel.getGeneralInformationModel();
            URL styleSheet = generalInformationModel.getStyleSheet();
            if (styleSheet != null) {
                String urlText = styleSheet.toString();
                int slashIndex = urlText.lastIndexOf("/");
                if (slashIndex != -1) {
                    TomcatProperties tomcatProperties = new TomcatProperties();
                    String styleSheetName = urlText.substring(slashIndex + 1);
                    String srcPath = tomcatProperties.getWebPagesPath(styleSheetName);
                    StringBuffer styleSheetLink = new StringBuffer();
                    styleSheetLink.append("<link rel=\"stylesheet\" href=\"");
                    styleSheetLink.append(srcPath);
                    styleSheetLink.append("\" ");
                    styleSheetLink.append("type=\"text/css\">");
                    System.out.println(styleSheetLink.toString());
                    out.print(styleSheetLink.toString());
                }
            }
            out.flush();
        } catch (Exception err) {
            err.printStackTrace(System.out);
        }
        return SKIP_BODY;
    }

    public int doEndTag() throws JspException {
        return SKIP_BODY;
    }

    public void release() {
        super.release();
    }
}
