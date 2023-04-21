package Tests.Loaders;

import harima.agamengine.loaders.IMapSerializer;
import harima.agamengine.loaders.JavaMapModelSerializer;
import harima.agamengine.loaders.XmlMapModelSerializer;
import harima.agamengine.mapmodel.MapModel;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import Tests.MapModel.MapModelFixture;
import com.thoughtworks.xstream.XStream;
import junit.framework.TestCase;

public class XmlMapModelSerializerFixture extends TestCase {

    IMapSerializer loader;

    String fileName = "test";

    protected void setUp() throws Exception {
        loader = new XmlMapModelSerializer();
    }

    public void test_CanCreateFileWithExtension() throws Exception {
        loader.createFile(fileName);
        File file = new File(fileName + loader.getFileExtension());
        assertEquals(file.getName(), fileName + loader.getFileExtension());
        assertTrue(file.exists());
        assertTrue(file.isFile());
        assertTrue(file.delete());
    }

    public void test_CanWriteToFile() throws Exception {
        File file = loader.createFile(fileName);
        loader.writeToFile(fileName, new MapModel(new Dimension(), ""));
        XStream reader = new XStream();
        FileReader fileReader = new FileReader(file);
        Object data = (MapModel) (reader.fromXML(fileReader));
        fileReader.close();
        assertNotNull(data);
        assertTrue(data instanceof MapModel);
        assertTrue(file.delete());
    }

    public void test_CanReadFromFile() throws Exception {
        File file = loader.createFile(fileName);
        FileWriter writer = new FileWriter(file);
        XStream xstream = new XStream();
        xstream.toXML(new MapModel(new Dimension(), ""), writer);
        writer.close();
        MapModel model = loader.readFromFile(fileName);
        assertNotNull(model);
        assertTrue(file.delete());
    }

    public void test_CanFindMapFilesInDirectory_FindMapsRequested_Always() throws Exception {
        File dirFile = new File("myDir");
        assertTrue(dirFile.mkdir());
        File myMap = new File(dirFile, "myMap" + loader.getFileExtension());
        assertTrue(myMap.createNewFile());
        File[] mapNames = loader.findMapsInDir(dirFile);
        assertNotNull(mapNames);
        assertEquals(1, mapNames.length);
        assertEquals("myMap" + loader.getFileExtension(), mapNames[0].getName());
        assertTrue(myMap.delete());
        assertTrue(dirFile.delete());
    }
}
