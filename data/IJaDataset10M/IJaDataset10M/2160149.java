package free.robotsamurai.andesta.data;

import java.util.Date;

/**
 * @author pjamar
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class AFile {

    public static final int TYPE_FILE = 1000;

    public static final int TYPE_DIR = 1001;

    public static final int TYPE_PACKAGE = 1002;

    private Integer UID;

    private Integer fatherUID;

    private String name;

    private Integer type;

    private Long size;

    private Date creation;

    private Date lastUpdate;

    /**
	 * @return Returns the creation.
	 */
    public Date getCreation() {
        return creation;
    }

    /**
	 * @param creation The creation to set.
	 */
    public void setCreation(Date creation) {
        this.creation = creation;
    }

    /**
	 * @return Returns the fatherUID.
	 */
    public Integer getFatherUID() {
        return fatherUID;
    }

    /**
	 * @param fatherUID The fatherUID to set.
	 */
    public void setFatherUID(Integer fatherUID) {
        this.fatherUID = fatherUID;
    }

    /**
	 * @return Returns the lastUpdate.
	 */
    public Date getLastUpdate() {
        return lastUpdate;
    }

    /**
	 * @param lastUpdate The lastUpdate to set.
	 */
    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name The name to set.
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return Returns the size.
	 */
    public Long getSize() {
        return size;
    }

    /**
	 * @param size The size to set.
	 */
    public void setSize(Long size) {
        this.size = size;
    }

    /**
	 * @return Returns the type.
	 */
    public Integer getType() {
        return type;
    }

    /**
	 * @param type The type to set.
	 */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
 * @return Returns the uID.
 */
    public Integer getUID() {
        return UID;
    }

    /**
 * @param uid The uID to set.
 */
    public void setUID(Integer uid) {
        UID = uid;
    }
}
