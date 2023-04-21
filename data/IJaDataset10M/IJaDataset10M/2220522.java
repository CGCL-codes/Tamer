package colibri.io.relation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import colibri.lib.Relation;

/**
 * Class for exporting a binary relation to an .xml file.
 * @author Daniel N. Goetzmann
 * @version 1.0
 */
public class RelationWriterXML {

    /**
	 * writes the relation to a file in .xml format
	 * @param relation the relation to be written
	 * @param file the file to which the relation shall be written. The file is closed by this method.
	 * @throws IOException
	 */
    public void write(Relation relation, File file) throws IOException {
        final String beginDocument = "<?xml version=\"1.0\" ?>\n<context>\n";
        final String endDocument = "</context>";
        FileWriter writer = new FileWriter(file);
        try {
            writer.write(beginDocument);
            Iterator<Comparable> objects = relation.getAllObjects().iterator();
            while (objects != null && objects.hasNext()) {
                Comparable object = objects.next();
                writer.write("\t<object name=\"" + object.toString() + "\">\n");
                Iterator<Comparable> attributes = relation.getAttributes(object);
                while (attributes != null && attributes.hasNext()) {
                    Comparable attribute = attributes.next();
                    writer.write("\t\t<attribute name=\"" + attribute.toString() + "\"></attribute>\n");
                }
                writer.write("\t</object>\n");
            }
            objects = null;
            Iterator<Comparable> attributes = relation.getAllAttributes().iterator();
            while (attributes != null && attributes.hasNext()) {
                Comparable attribute = attributes.next();
                objects = relation.getObjects(attribute);
                if (!objects.hasNext()) writer.write("\t<attribute name=\"" + attribute.toString() + "\"></attribute>");
            }
            writer.write(endDocument);
        } finally {
            writer.close();
        }
    }
}
