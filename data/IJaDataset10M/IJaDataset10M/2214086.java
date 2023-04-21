package bones.doc.file;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import bones.doc.Archive;
import bones.doc.Context;
import bones.doc.ContextFactory;
import bones.doc.Doc;
import bones.doc.Field;
import bones.doc.Resource;
import bones.process.BonesSystemException;

/**
 * class FileDoc.java.
 * ie. a file insinde a jar.
 */
public class FileDoc implements Doc {

    private transient Archive parent;

    private long id;

    private String name;

    private Map fields = new HashMap();

    public FileDoc() {
    }

    /**
	 * construct an empty Doc to be saved
	 */
    public FileDoc(Archive parent, String name) throws BonesSystemException {
        this.parent = parent;
        this.name = name;
    }

    /**
	 * Constructor for FileDoc.
	 */
    public FileDoc(Context superparent, String url) throws BonesSystemException {
        super();
        try {
            URL url2 = new URL(url);
            name = url2.getFile();
            parent = new FileArchive(superparent, url2.getPath());
        } catch (MalformedURLException e) {
            throw new BonesSystemException("Incorrect url:" + url);
        }
        id = generateId();
    }

    private long generateId() {
        return (long) ((long) getUrl().hashCode()) + System.currentTimeMillis() & ((long) 0xFFFFF000);
    }

    public Resource parent() {
        return parent;
    }

    public String getUrl() {
        return parent.getUrl() + ContextFactory.getUrlSeparator() + name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setFieldValues(String name, Object[][] values) {
        fields.put(name, values);
    }

    public Object[][] getFieldValues(String name) {
        return (Object[][]) fields.get(name);
    }

    public Field getField(String name) {
        return null;
    }

    public boolean save() {
        return parent.save();
    }
}
