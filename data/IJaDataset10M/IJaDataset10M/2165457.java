package configurator.model.rootmgr;

import configurator.model.XmlElement;
import configurator.model.url.FileUrl;
import configurator.model.url.Url;
import configurator.util.ElementCreator;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

/**
 * Folder's contents to exclude.
 *
 * @author Justin Tomich
 */
public class ExcludeFolder implements XmlElement {

    private FileUrl url;

    ExcludeFolder(FileUrl url) {
        this.url = url;
    }

    public Url getUrl() {
        return url;
    }

    public String toString() {
        return "ExcludeFolder{" + "exclude=" + url + "}";
    }

    public void addToDom(Element parent) {
        Element e = new ElementCreator("excludeFolder", parent).create();
        url.setAttribute(e);
    }
}
