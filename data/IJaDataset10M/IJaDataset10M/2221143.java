package org.jaffa.applications.jaffa.modules.admin.components.menunavigation.ui;

import java.io.*;
import java.net.MalformedURLException;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.jaffa.presentation.portlet.component.Component;
import org.jaffa.presentation.portlet.FormKey;
import org.jaffa.exceptions.ApplicationExceptions;
import org.jaffa.exceptions.FrameworkException;
import org.jaffa.util.URLHelper;
import org.jaffa.util.StringHelper;
import org.jaffa.applications.jaffa.modules.admin.components.menunavigation.ui.exceptions.MenuNavigationException;
import org.jaffa.components.navigation.NavAccessor;
import org.jaffa.components.navigation.NavCache;
import javax.servlet.http.HttpSession;

/** This is the component controller for the MenuNavigation Editor.
 *
 * @author  Maheshd
 * @version 1.0
 */
public class MenuNavigationComponent extends Component {

    private static Logger log = Logger.getLogger(MenuNavigationComponent.class);

    /** Holds value of property fileContents. */
    private String m_fileContents;

    /** Holds value of property fileContentsFormKey. */
    private FormKey m_fileContentsFormKey;

    /** Getter for property fileContents.
     * @return Value of property fileContents.
     *
     */
    public String getFileContents() {
        return m_fileContents;
    }

    /** Setter for property fileContents.
     * @param fileContents New value of property fileContents.
     *
     */
    public void setFileContents(String fileContents) {
        m_fileContents = fileContents;
    }

    /** Getter for property fileContentsFormKey.
     * @return Value of property fileContentsFormKey.
     *
     */
    public FormKey getMenuNavigationFormKey() {
        if (m_fileContentsFormKey == null) m_fileContentsFormKey = new FormKey(MenuNavigationForm.NAME, getComponentId());
        return m_fileContentsFormKey;
    }

    /** This will retrieve the file contents of the navigation.xml file  and return the FormKey for rendering the component.
     * @return the FormKey for rendering the component.
     * @throws FrameworkException if any error ocurrs in obtaining the contents of the navigation.xml file.
     */
    public FormKey display() throws FrameworkException, ApplicationExceptions {
        retrieveFileContents();
        return getMenuNavigationFormKey();
    }

    /** This retrieves the the file contents of the navigation.xml file .
     * @throws FrameworkException if any error occurs.
     */
    protected void retrieveFileContents() throws FrameworkException, ApplicationExceptions {
        ApplicationExceptions appExps = new ApplicationExceptions();
        String prop = NavCache.getFileLocation();
        BufferedReader reader = null;
        try {
            String absoluteFileName = URLHelper.newExtendedURL(prop).getPath();
            getUserSession().getWidgetCache(getComponentId()).clear();
            reader = new BufferedReader(new FileReader(absoluteFileName));
            StringBuffer buf = new StringBuffer();
            String str = null;
            while ((str = reader.readLine()) != null) {
                buf.append(str);
                buf.append("\r\n");
            }
            m_fileContents = buf.toString();
        } catch (MalformedURLException e) {
            appExps.add(new MenuNavigationException(MenuNavigationException.PROP_BADURL_ERROR, prop, StringHelper.convertToHTML(e.getMessage())));
            throw appExps;
        } catch (IOException e) {
            appExps.add(new MenuNavigationException(MenuNavigationException.PROP_FILEREAD_ERROR, StringHelper.convertToHTML(e.getMessage())));
            throw appExps;
        } finally {
            if (reader != null) try {
                reader.close();
            } catch (IOException e) {
                String str = "Exception thrown while closing the Reader Stream";
                log.error(str, e);
                appExps.add(new MenuNavigationException(MenuNavigationException.PROP_FILEREAD_ERROR, StringHelper.convertToHTML(e.getMessage())));
                throw appExps;
            }
        }
    }

    /** This will perform the following tasks.
     * - Saves the contents to navigation.xml.
     * - Clears the cache and refreshes the menu.
     * @param request The request we are processing.
     * @throws FrameworkException , ApplicationExceptions if any error occurs.
     */
    protected void performSave(HttpServletRequest request) throws FrameworkException, ApplicationExceptions {
        ApplicationExceptions appExps = new ApplicationExceptions();
        String prop = NavCache.getFileLocation();
        BufferedWriter writer = null;
        try {
            String absoluteFileName = URLHelper.newExtendedURL(prop).getPath();
            writer = new BufferedWriter(new FileWriter(absoluteFileName));
            writer.write(getFileContents());
            writer.flush();
            clearCacheAndRefreshMenu(request);
        } catch (MalformedURLException e) {
            appExps.add(new MenuNavigationException(MenuNavigationException.PROP_BADURL_ERROR, prop, StringHelper.convertToHTML(e.getMessage())));
            throw appExps;
        } catch (IOException e) {
            appExps.add(new MenuNavigationException(MenuNavigationException.PROP_FILEREAD_ERROR, StringHelper.convertToHTML(e.getMessage())));
            throw appExps;
        } finally {
            if (writer != null) try {
                writer.close();
            } catch (IOException e) {
                String str = "Exception thrown while closing the Writer Stream";
                log.error(str, e);
                appExps.add(new MenuNavigationException(MenuNavigationException.PROP_FILEREAD_ERROR, StringHelper.convertToHTML(e.getMessage())));
                throw appExps;
            }
        }
    }

    /** This will perform the following tasks.
     * - Clears the cache
     * - Refreshes the menu for current User session.
     */
    private void clearCacheAndRefreshMenu(HttpServletRequest request) {
        NavCache.getInstance().clearCache();
        HttpSession session = (HttpSession) getUserSession().getHttpSession();
        NavAccessor.getNavAccessor(request).clearSession(session);
    }
}
