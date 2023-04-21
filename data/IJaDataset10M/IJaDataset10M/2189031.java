package gov.lanl.Database;

/**
 * Class declaration
 * 
 * 
 * @author <a href="mailto:Sascha@Koenig.net">Sascha A. Koenig<a>
 * @version $Revision: 924 $ $Date: 2001-10-22 15:46:13 -0400 (Mon, 22 Oct 2001) $
 */
public abstract class Sequence__DB extends gov.lanl.Database.Sequence_ {

    /**
	 * Method declaration
	 * 
	 * 
	 * @param classData
	 * 
	 * @see
	 */
    public static void save(org.storedobjects.db.DBClassData classData) {
        gov.lanl.Database.Sequence_ obj = (gov.lanl.Database.Sequence_) classData.instance;
        classData.writeInt("chunkSize", obj.chunkSize);
        classData.writeLong("sequence", obj.sequence);
    }

    /**
	 * Method declaration
	 * 
	 * 
	 * @param classData
	 * 
	 * @see
	 */
    public static void restore(org.storedobjects.db.DBClassData classData) {
        gov.lanl.Database.Sequence_ obj = (gov.lanl.Database.Sequence_) classData.instance;
        obj.chunkSize = classData.readInt("chunkSize");
        obj.sequence = classData.readLong("sequence");
    }
}
