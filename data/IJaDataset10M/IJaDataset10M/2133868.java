package fbpwn.plugins.core;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import fbpwn.core.AuthenticatedAccount;
import fbpwn.core.FacebookAccount;
import fbpwn.ui.FacebookGUI;
import fbpwn.core.FacebookTask;
import fbpwn.core.FacebookTaskState;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 * Represents the dump images task 
 */
public class DumpImagesTask extends FacebookTask {

    /**
     * Creates a new dump images task 
     * @param FacebookGUI The GUI used for updating the task's progress
     * @param facebookProfile The victim's profile
     * @param authenticatedProfile The attacker's profile
     * @param workingDirectory The directory used to save all the dumped data
     */
    public DumpImagesTask(FacebookGUI FacebookGUI, FacebookAccount facebookProfile, AuthenticatedAccount authenticatedProfile, File workingDirectory) {
        super(FacebookGUI, facebookProfile, authenticatedProfile, workingDirectory);
    }

    /**
     * Initialize this task, called once the task is added to the queue 
     */
    @Override
    public void init() {
    }

    /**
     * Runs this task
     * @return true if completed, false if error occurred so that it will be rerun after a small delay.
     */
    @Override
    public boolean run() {
        setTaskState(FacebookTaskState.Running);
        setMessage("Getting Album List");
        getFacebookGUI().updateTaskProgress(this);
        ArrayList<String> albumsURL = new ArrayList<String>();
        try {
            HtmlPage photosPage = getAuthenticatedProfile().getBrowser().getPage(getFacebookTargetProfile().getTaggedPhotosURL().replace("www", "m").replace("sk", "v"));
            while (true) {
                List<HtmlAnchor> anchors = photosPage.getAnchors();
                for (int i = 0; i < anchors.size(); i++) {
                    if (anchors.get(i).getHrefAttribute().contains("/media/set")) {
                        if (!anchors.get(i).getHrefAttribute().contains("m.facebook.com")) {
                            albumsURL.add("http://m.facebook.com" + anchors.get(i).getHrefAttribute());
                        } else {
                            albumsURL.add(anchors.get(i).getHrefAttribute());
                        }
                    }
                }
                try {
                    photosPage = getAuthenticatedProfile().getBrowser().getPage("http://m.facebook.com" + photosPage.getElementById("m_more_item").getElementsByTagName("a").get(0).getAttribute("href"));
                } catch (Exception ex) {
                    break;
                }
                if (checkForCancel()) {
                    return true;
                }
            }
            for (int i = 0; i < albumsURL.size(); i++) {
                setMessage("Dumping Albums " + (i + 1) + "/" + albumsURL.size());
                getFacebookGUI().updateTaskProgress(this);
                HtmlPage album = getAuthenticatedProfile().getBrowser().getPage(albumsURL.get(i));
                processAlbum(album, i + 1);
                if (checkForCancel()) {
                    return true;
                }
            }
            setMessage((albumsURL.isEmpty()) ? "No Albums or Albums are not accesible" : "Finished");
            setTaskState(FacebookTaskState.Finished);
            setPercentage(100.0);
            getFacebookGUI().updateTaskProgress(this);
        } catch (IOException ex) {
            Logger.getLogger(DumpImagesTask.class.getName()).log(Level.SEVERE, "Exception in thread: " + Thread.currentThread().getName(), ex);
        } catch (FailingHttpStatusCodeException ex) {
            Logger.getLogger(DumpImagesTask.class.getName()).log(Level.SEVERE, "Exception in thread: " + Thread.currentThread().getName(), ex);
        }
        return true;
    }

    /**
     * Dump Album Images with Comments
     * @param albumPage Mobile album page containing Images with comments
     * @param albumIndex Album index that identify it's folder
     */
    private void processAlbum(HtmlPage albumPage, int albumIndex) throws FileNotFoundException, UnsupportedEncodingException, IOException {
        ArrayList<String> photos = new ArrayList<String>();
        DomNodeList<HtmlElement> anchors;
        while (true) {
            HtmlElement body = albumPage.getElementById("root");
            anchors = body.getElementsByTagName("a");
            for (int j = 0; j < anchors.size(); j++) {
                if (anchors.get(j).getAttribute("href").contains("fbid")) {
                    photos.add("http://m.facebook.com" + anchors.get(j).getAttribute("href"));
                }
            }
            try {
                albumPage = getAuthenticatedProfile().getBrowser().getPage("http://m.facebook.com" + albumPage.getElementById("m_more_item").getElementsByTagName("a").get(0).getAttribute("href"));
            } catch (Exception ex) {
                break;
            }
            if (checkForCancel()) {
                return;
            }
        }
        for (int j = 0; j < photos.size(); j++) {
            HtmlPage photoPage = getAuthenticatedProfile().getBrowser().getPage(photos.get(j));
            FileUtils.copyURLToFile(new URL(photoPage.getElementsByTagName("img").get(1).getAttribute("src")), new File(getDirectory().getAbsolutePath() + System.getProperty("file.separator") + "Album-" + albumIndex + System.getProperty("file.separator") + "Image-" + (j + 1) + ".jpg"));
            PrintWriter commentWriter = new PrintWriter(new File(getDirectory().getAbsolutePath() + System.getProperty("file.separator") + "Album-" + albumIndex + System.getProperty("file.separator") + "Comments-on-Image-" + (j + 1) + ".html"), "UTF-8");
            commentWriter.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
            commentWriter.println("<!DOCTYPE html PUBLIC \"-//WAPFORUM//DTD XHTML Mobile 1.0//EN\" \"http://www.wapforum.org/DTD/xhtml-mobile10.dtd\">");
            commentWriter.println(albumPage.getElementsByTagName("head").get(0).asXml());
            try {
                dumpComments((HtmlPage) getAuthenticatedProfile().getBrowser().getPage("http://m.facebook.com" + photoPage.getElementById("see_prev").getElementsByTagName("a").get(0).getAttribute("href")), commentWriter);
            } catch (Exception ex) {
            }
            dumpComments(photoPage, commentWriter);
            commentWriter.flush();
            commentWriter.close();
            setPercentage((double) (j + 1) / photos.size() * 100);
            getFacebookGUI().updateTaskProgress(this);
            if (checkForCancel()) {
                return;
            }
        }
        PrintWriter nameWriter = new PrintWriter(new File(getDirectory().getAbsolutePath() + System.getProperty("file.separator") + "Album-" + albumIndex + System.getProperty("file.separator") + "Album Name.txt"), "UTF-8");
        nameWriter.println(albumPage.getTitleText());
        nameWriter.flush();
        nameWriter.close();
    }

    /**
     * Dump comments on Album images
     * @param photoPage Mobile photo page containing comments
     * @param commentWriter Writer used to dump comments
     */
    private void dumpComments(HtmlPage photoPage, PrintWriter commentWriter) {
        DomNodeList<HtmlElement> divisions = photoPage.getElementsByTagName("div");
        for (int i = 0; i < divisions.size(); i++) {
            if (divisions.get(i).getAttribute("class").equals("ufi")) {
                commentWriter.println(divisions.get(i).asXml());
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "Dump Album's photos with comments";
    }
}
