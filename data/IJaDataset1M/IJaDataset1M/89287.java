package samples;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.util.*;
import com.adobe.acrobat.util.*;

public class NailsApplet extends Applet {

    private Frame getFrame() {
        Component c = this;
        while ((c = c.getParent()) != null) {
            if (c instanceof Frame) {
                return (Frame) c;
            }
        }
        return null;
    }

    public class InsetPanel extends Container {

        private Insets insets = new Insets(10, 10, 10, 10);

        public Insets getInsets() {
            return insets;
        }
    }

    public void init() {
        try {
            Session theSession = Session.getTheSession();
            if (theSession.isIE()) {
                com.ms.security.PolicyEngine.assertPermission(com.ms.security.PermissionID.SYSTEM);
            } else if (theSession.isNetscape()) {
                netscape.security.PrivilegeManager.enablePrivilege("UniversalTopLevelWindow");
            }
            setBackground(Color.white);
            ScrollPane spanel = new ScrollPane();
            InsetPanel panel = new InsetPanel();
            panel.setLayout(new GridLayout(3, 3, 10, 10));
            spanel.add(panel);
            int w = 79;
            int h = 103;
            Frame f = getFrame();
            String url = getURL();
            Vector files = getPdfs();
            for (int i = 0; i < files.size(); i++) {
                Nails n = new Nails(w, h, f, false);
                n.setURL(url + (String) (files.elementAt(i)));
                panel.add(n);
            }
            setLayout(new BorderLayout());
            add(spanel, BorderLayout.CENTER);
        } catch (Exception x) {
            setLayout(new BorderLayout());
            add(new Label("Error...Unable to create applet. Reason:" + x));
        }
    }

    private String getURL() throws Exception {
        String url = this.getParameter("url");
        if (url == null) throw new Exception("Missing URL parameter");
        return url;
    }

    private Vector getPdfs() throws Exception {
        StringTokenizer st = new StringTokenizer(this.getParameter("pdfs"));
        Vector v = new Vector();
        while (st.hasMoreTokens()) {
            String f = st.nextToken() + ".pdf";
            v.addElement(f);
        }
        return v;
    }
}
