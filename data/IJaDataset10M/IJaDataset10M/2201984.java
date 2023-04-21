package cl.coretech.openbravo.translator.translation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.List;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.load.Persister;
import org.simpleframework.xml.stream.Format;

/**
 * @author Jose Ignacio Santa Cruz G. (Core Technologies Ltda.)
 *
 */
public class TranslationSerialization {

    private File workFile;

    /**
	 * @param workFile
	 */
    public TranslationSerialization(File workFile) {
        super();
        this.workFile = workFile;
    }

    public TranslationSerialization(String workFilePath) {
        super();
        this.workFile = new File(workFilePath);
    }

    public void serialize(Translation translation) throws Exception {
        Serializer serializer = new Persister(new Format(4, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        Translation trans = translation;
        File result = workFile;
        FileOutputStream outputStream = new FileOutputStream(result);
        serializer.write(trans, outputStream, "UTF-8");
    }

    public byte[] serializeBytes(Translation translation) throws Exception {
        Serializer serializer = new Persister(new Format(4, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"));
        Translation trans = translation;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        serializer.write(trans, byteStream, "UTF-8");
        return byteStream.toByteArray();
    }

    public Translation deSerialize() throws Exception {
        Serializer serializer = new Persister();
        File source = workFile;
        Translation trans = serializer.read(Translation.class, source);
        return trans;
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        File file = new File("/Desarrollos/OpenbravoERP/AppsOpenbravo/attach/lang/es_CL/M_PRODUCT_TRL_es_CL.xml");
        TranslationSerialization translationSerialization = new TranslationSerialization(file);
        try {
            Translation translation = translationSerialization.deSerialize();
            String language = translation.getLanguage();
            String table = translation.getTable();
            System.out.println("Language: " + language);
            System.out.println("Table: " + table);
            List<Row> rows = translation.getRows();
            for (Iterator<Row> iterator = rows.iterator(); iterator.hasNext(); ) {
                Row row = iterator.next();
                int id = row.getId();
                String trl = row.getTrl();
                System.out.println("\n-Id: " + id);
                System.out.println("-Trl: " + trl);
                List<Value> values = row.getValues();
                for (Iterator<Value> iterator2 = values.iterator(); iterator2.hasNext(); ) {
                    Value value = iterator2.next();
                    String column = value.getColumn();
                    String original = value.getOriginal();
                    String val = value.getValue();
                    System.out.println("--\n--Column: " + column);
                    System.out.println("--Original: " + original);
                    System.out.println("--Value: " + val);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * @return the workFile
	 */
    public File getWorkFile() {
        return workFile;
    }

    /**
	 * @param workFile the workFile to set
	 */
    public void setWorkFile(File workFile) {
        this.workFile = workFile;
    }
}
