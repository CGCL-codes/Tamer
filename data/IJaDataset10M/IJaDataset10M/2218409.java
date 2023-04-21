package configurator.model.deployment;

import configurator.model.XmlElement;
import configurator.model.url.FileUrl;
import configurator.util.ElementCreator;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

/**
 * A root directory mapping in the archive. Maps a directory in the project to a
 * directory in the web archive.
 * <p/>
 * In the .iml file: <root url="file://$MODULE_DIR$/resources" relative="/" />
 *
 * @author Justin Tomich
 */
public class Root implements XmlElement {

    private FileUrl url;

    private String relative;

    public Root(FileUrl url, String relative) {
        this.url = url;
        this.relative = relative;
    }

    public void addToDom(Element parent) {
        Element root = new ElementCreator("root", parent).create();
        url.setAttribute(root);
        root.setAttribute("relative", relative);
    }

    public String toString() {
        return "Root{" + "url=" + url + ", relative='" + relative + '\'' + '}';
    }
}
