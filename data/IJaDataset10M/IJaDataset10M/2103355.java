package harima.agamengine.loaders;

import harima.agamengine.mapmodel.MapModel;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class JavaMapModelSerializer implements IMapSerializer {

    final String FileExtension = ".xmap";

    public String getFileExtension() {
        return FileExtension;
    }

    public File createFile(String fileName) throws IOException {
        File file = new File(fileName + FileExtension);
        file.createNewFile();
        return file;
    }

    public void writeToFile(String file, MapModel model) throws FileNotFoundException, IOException {
        file += FileExtension;
        ObjectOutputStream stream = new ObjectOutputStream(new FileOutputStream(new File(file)));
        stream.writeObject(model);
        stream.flush();
        stream.close();
    }

    public MapModel readFromFile(String fileName) throws IOException, ClassNotFoundException {
        File file = new File(fileName + FileExtension);
        FileInputStream stream = new FileInputStream(file);
        ObjectInputStream reader = new ObjectInputStream(stream);
        MapModel model = (MapModel) (reader.readObject());
        reader.close();
        return model;
    }

    public File[] findMapsInDir(File direcory) {
        File[] files = direcory.listFiles(new FileFilter() {

            public boolean accept(File suspect) {
                return suspect.getName().endsWith(FileExtension);
            }
        });
        return files;
    }
}
