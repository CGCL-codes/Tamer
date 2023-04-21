package amoeba.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author shawn.lassiter
 */
@XmlRootElement(name = "node")
public class StorageEdge extends Marshallable {

    private static int currentID = 0;

    public static String getNextID() {
        currentID++;
        return "edge" + Integer.toString(currentID);
    }

    @XmlIDREF
    @XmlAttribute(name = Consts.END_ATT)
    public StorageNode end;

    @XmlIDREF
    @XmlAttribute(name = Consts.START_ATT)
    public StorageNode start;

    @XmlIDREF
    @XmlAttribute(name = Consts.FROM_ATT)
    public StorageIdentity from;

    @XmlID
    @XmlAttribute(name = Consts.ID_ATT)
    public String id;

    public StorageEdge() {
        this("", null, null, null);
    }

    public StorageEdge(String id, StorageIdentity from, StorageNode start, StorageNode end) {
        this.id = id;
        this.from = from;
        this.start = start;
        this.end = end;
    }

    /**
	 * @param fileName
	 * @return
	 */
    public boolean writeToFile(String fileName) {
        return writeToFile(new File(fileName));
    }

    /**
	 * @param file
	 * @return
	 */
    public boolean writeToFile(File file) {
        try {
            JAXBContext jc;
            jc = JAXBContext.newInstance(getClass());
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            OutputStream out = new FileOutputStream(file);
            m.marshal(this, out);
            return true;
        } catch (JAXBException e) {
            e.printStackTrace();
            return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
	 * @param string
	 */
    public static StorageEdge unmarshal(String fileName) {
        return unmarshal(new File(fileName));
    }

    /**
	 * @param file
	 * @return
	 */
    public static StorageEdge unmarshal(File file) {
        try {
            JAXBContext jc;
            jc = JAXBContext.newInstance(StorageEdge.class);
            Unmarshaller m = jc.createUnmarshaller();
            Object o = m.unmarshal(file);
            return (StorageEdge) o;
        } catch (JAXBException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != getClass()) return false;
        if (!((StorageEdge) obj).id.equals(id)) return false;
        if (!((StorageEdge) obj).from.equals(from)) return false;
        if (!((StorageEdge) obj).start.equals(start)) return false;
        if (!((StorageEdge) obj).end.equals(end)) return false;
        return true;
    }

    @Override
    protected Class<StorageEdge> getClassContext() {
        return StorageEdge.class;
    }
}
