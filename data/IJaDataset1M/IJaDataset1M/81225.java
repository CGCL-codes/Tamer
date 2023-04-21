package hambo.app.base;

/**
 * Class used to handle information 
 * from forms
 *
 */
public class AppRedirect extends RedirectorBase {

    public void processPage() throws Exception {
        String switchApp = getParameter("swapp");
        String switchSubApp = getParameter("swsub");
        String switchLang = getParameter("changelang");
        if (switchApp != null && !switchApp.equals("") && getParameter("swappval") != null && !getParameter("swappval").equals("")) {
            throwRedirect(getParameter("swappval"));
        } else if (switchSubApp != null && !switchSubApp.equals("") && getParameter("swsubval") != null && !getParameter("swsubval").equals("")) {
            throwRedirect(getParameter("swsubval"));
        }
        redirectFromHistory();
    }
}
