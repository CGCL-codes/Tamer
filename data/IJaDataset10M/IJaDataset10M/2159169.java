package hu.gbalage.owl.cms.core.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import org.semanticweb.owl.io.OWLOntologyInputSource;
import org.semanticweb.owl.io.OWLOntologyOutputTarget;
import org.semanticweb.owl.io.PhysicalURIInputSource;
import org.semanticweb.owl.io.StreamOutputTarget;
import hu.gbalage.owl.cms.core.OntologyStorage;

/**
 * @author balage
 *
 */
public class FileOntologyStorage implements OntologyStorage {

    private final File dir;

    private final String sep = File.separator;

    private final Properties index = new Properties();

    private final File indexfile;

    public FileOntologyStorage(File dir) {
        this.dir = dir;
        if (!dir.isDirectory()) dir.mkdir();
        indexfile = new File(dir.getAbsolutePath() + sep + "index.properties");
        try {
            index.load(new FileInputStream(indexfile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFile(URI uri) {
        String path = dir.getAbsolutePath() + sep + uri.getHost() + sep + uri.getPath().replaceAll("/", sep);
        File file = new File(path);
        File d = file.getParentFile();
        if (!d.isDirectory()) d.mkdirs();
        return file;
    }

    @Override
    public OWLOntologyInputSource load(URI uri) {
        return new PhysicalURIInputSource(getFile(uri).toURI());
    }

    @Override
    public OWLOntologyOutputTarget save(URI uri) throws FileNotFoundException {
        File f = getFile(uri);
        index.setProperty(uri.toString(), System.currentTimeMillis() + "");
        try {
            index.store(new FileOutputStream(indexfile), "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream os = new FileOutputStream(f);
        return new StreamOutputTarget(os);
    }

    @Override
    public Collection<URI> getAll() {
        List<URI> list = new ArrayList<URI>();
        Enumeration<Object> es = index.keys();
        while (es.hasMoreElements()) {
            list.add(URI.create(es.nextElement().toString()));
        }
        return list;
    }
}
