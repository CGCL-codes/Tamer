package photospace.meta;

import java.io.*;
import java.util.*;
import org.apache.commons.logging.*;
import com.drew.metadata.*;
import com.hp.hpl.jena.rdf.model.*;
import com.hp.hpl.jena.vocabulary.*;
import junit.framework.*;
import photospace.meta.rdf.*;
import photospace.vfs.FileSystem;
import photospace.vfs.*;

public class TranslatorTest extends TestCase {

    private static final Log log = LogFactory.getLog(TranslatorTest.class);

    public void testTranslateFromAsset() throws Exception {
        Translator translator = new Translator();
        PhotoMeta photo = new PhotoMeta();
        photo.setDevice("test device");
        photo.setTags(new String[] { "k1", "k2" });
        Model rdf = translator.toRdf(photo);
        assertEquals(photo.getDevice(), rdf.getProperty(null, TechVocab.CAMERA).getString());
    }

    public void testRoundTrip() throws Exception {
        Translator translator = new Translator();
        PhotoMeta photo = new PhotoMeta();
        photo.setTags(new String[] { "k1", "k2" });
        Model rdf = translator.toRdf(photo);
        PhotoMeta fromRdf = (PhotoMeta) translator.fromRdf(rdf);
        assertEquals(Arrays.asList(photo.getTags()), Arrays.asList(fromRdf.getTags()));
        assertEquals(photo, fromRdf);
    }

    public void testTranslateFromExif() throws Exception {
        PersisterImpl persister = new PersisterImpl();
        Translator translator = new Translator();
        persister.setTranslator(translator);
        File jpeg = new File(System.getProperty("project.root"), "build/test/exif-nordf.jpg");
        Metadata exif = persister.getExif(jpeg);
        PhotoMeta photo = translator.fromExif(exif);
        assertTrue(photo.getWidth() > 0);
        assertTrue(photo.getHeight() > 0);
        Model rdf = translator.toRdf(photo);
        PhotoMeta fromRdf = (PhotoMeta) translator.fromRdf(rdf);
        assertEquals(photo, fromRdf);
        assertEquals(photo.getDevice(), fromRdf.getDevice());
        assertEquals(photo.getWidth(), fromRdf.getWidth());
        assertEquals(photo.getHeight(), fromRdf.getHeight());
    }

    public void testFolderMeta() throws Exception {
        PersisterImpl persister = new PersisterImpl();
        Translator translator = new Translator();
        persister.setTranslator(translator);
        FileSystem filesystem = new FileSystemImpl();
        filesystem.setRoot(new File(System.getProperty("project.root"), "build/test/"));
        persister.setFilesystem(filesystem);
        File folder = new File(System.getProperty("project.root"), "build/test/").getCanonicalFile();
        FolderMeta meta = (FolderMeta) persister.getMeta(folder);
        assertEquals("/", meta.getPath());
        assertNotNull(meta);
        Model rdf = translator.toRdf(meta);
        assertNotNull(rdf);
        Meta fromRdf = translator.fromRdf(rdf);
        fromRdf.setName(meta.getName());
        assertEquals(meta.getPath(), fromRdf.getPath());
        assertEquals(meta, fromRdf);
    }

    public void testDeepToRdf() throws Exception {
        PhotoMeta photo = new PhotoMeta();
        photo.setPath("/foo/photo.jpg");
        photo.setTitle("foo photo");
        FolderMeta folder = new FolderMeta();
        folder.setPath("/foo/boo/");
        folder.setTitle("boo folder");
        folder.addFile(photo);
        Translator translator = new Translator();
        Model rdf = translator.toRdf(folder, 1);
        assertNotNull(rdf);
        ResIterator subjects = rdf.listSubjects();
        assertTrue(subjects.hasNext());
        Resource r1 = subjects.nextResource();
        assertTrue(subjects.hasNext());
        Resource r2 = subjects.nextResource();
        assertFalse(subjects.hasNext());
    }

    public void testRss() throws Exception {
        PhotoMeta photo = new PhotoMeta();
        photo.setPath("/foo/photo.jpg");
        photo.setTitle("foo photo");
        Model rdf = ModelFactory.createDefaultModel();
        Resource channel = rdf.createResource(RSS.channel);
        channel.addProperty(RSS.title, "channel title");
        channel.addProperty(RSS.link, "http://foo.bar/channel/");
        channel.addProperty(RSS.description, "channel description");
        Seq items = rdf.createSeq();
        channel.addProperty(RSS.items, items);
        for (int i = 0; i < 6; i++) {
            Resource item = rdf.createResource(RSS.item);
            items.add(item);
            item.addProperty(RSS.title, "item title " + i);
            item.addProperty(RSS.link, "http://foo.bar/channel/" + i);
            item.addProperty(RSS.description, "channel description " + i);
        }
        log.info(RdfTools.toString(rdf));
    }
}
