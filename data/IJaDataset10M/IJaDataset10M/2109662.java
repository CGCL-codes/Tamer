package octopus.presentation;

import org.w3c.dom.Element;
import java.io.UnsupportedEncodingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import hambo.util.StringUtil;
import hambo.app.util.DOMUtil;
import hambo.app.base.PortalPage;
import com.lutris.appserver.server.httpPresentation.HttpPresentationException;
import octopus.OctopusApplication;
import octopus.tools.Objects.ObjectTranslation;
import octopus.requests.OctopusRequest;
import octopus.requests.OctopusRequestFactory;
import octopus.tools.Messages.OctopusErrorMessages;

/**
 * Page used to Signup in Octopus
 *
 */
public class signup extends octoPage {

    /**
     * Default constructor. Does nothing except calling the constructor
     * of the superclass with the page id as an argument. The page id 
     * is set in the page_id variable that is inherited from PortalPage.
     */
    public signup() {
        super("signup");
    }

    /**
    * The over-ridden method that is called automatically 
    * by {@link PortalPage}.run().
    */
    public void processPage() throws Exception {
        DOMUtil.setFirstNodeText(getElement(skeleton, "pagetitle"), "Register");
        DOMUtil.setAttribute(getElement(skeleton, "SkeletonForm"), "ACTION", "signupredir.po");
        DOMUtil.setAttribute(getElement(skeleton, "SkeletonForm"), "METHOD", "POST");
        String newlogin = comms.request.getParameter("newlogin");
        if (newlogin != null && !newlogin.trim().equals("")) {
            newlogin = new String(newlogin.getBytes(), OctopusApplication.ENCODING);
            DOMUtil.setAttribute(getElement("newlogin"), "VALUE", newlogin);
        }
        DOMUtil.removeElement(getElement(skeleton, "navitem1"));
        DOMUtil.removeElement(getElement(skeleton, "navitem2"));
        DOMUtil.removeElement(getElement(skeleton, "navitem3"));
        DOMUtil.removeElement(getElement(skeleton, "navitem4"));
        DOMUtil.removeElement(getElement(skeleton, "navitem5"));
        DOMUtil.removeElement(getElement(skeleton, "navitem6"));
        DOMUtil.removeElement(getElement(skeleton, "navitem7"));
        DOMUtil.removeElement(getElement(skeleton, "navitem8"));
        DOMUtil.removeElement(getElement(skeleton, "sepadmin"));
        DOMUtil.removeElement(getElement(skeleton, "sepadmin2"));
        File help_file = new File(OctopusApplication.HELP_FILES_LOCATION + "signup.help");
        if (help_file.exists()) {
            StringBuffer help_text = new StringBuffer();
            FileInputStream fin = new FileInputStream(OctopusApplication.HELP_FILES_LOCATION + page_id + ".help");
            BufferedReader myInput = new BufferedReader(new InputStreamReader(fin));
            String thisLine;
            while ((thisLine = myInput.readLine()) != null) {
                help_text.append(thisLine);
            }
            if (!help_text.toString().trim().equals("")) {
                DOMUtil.setFirstNodeText(getElement(skeleton, "help_text"), help_text.toString());
            } else {
                DOMUtil.removeElement(getElement(skeleton, "div_help"));
            }
        } else {
            DOMUtil.removeElement(getElement(skeleton, "div_help"));
        }
    }
}
