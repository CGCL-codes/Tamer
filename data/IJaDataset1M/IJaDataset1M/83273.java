package configurator.model.rootmgr;

import configurator.model.XmlElement;
import configurator.model.url.FileUrl;
import configurator.model.url.Url;
import configurator.util.ElementCreator;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

/**
 * Base behavior for SourceFolder and TestFolder.
 *
 * @author Justin Tomich
 */
public abstract class BaseSource implements XmlElement {

    static final String ELEMENT_TAG = "sourceFolder";

    protected final FileUrl fileUrl;

    protected final boolean isTest;

    protected BaseSource(FileUrl file, boolean test) {
        this.fileUrl = file;
        isTest = test;
    }

    public Url getFileUrl() {
        return fileUrl;
    }

    public boolean isTest() {
        return isTest;
    }

    public void addToDom(Element parent) {
        Element source = new ElementCreator(ELEMENT_TAG, parent).create();
        fileUrl.setAttribute(source);
        source.setAttribute("isTestSource", String.valueOf(isTest));
    }

    public String toString() {
        return "BaseSource{" + "file=" + fileUrl + ", isTest=" + isTest + "}";
    }
}
