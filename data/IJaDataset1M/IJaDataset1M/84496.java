package tab;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rooms")
public class Room {

    private int mId;

    private boolean mAvailable;

    private String mName;

    /**
	 * @return Returns the available.
	 */
    public boolean isAvailable() {
        return mAvailable;
    }

    /**
	 * @param pAvailable The available to set.
	 */
    public void setAvailable(boolean pAvailable) {
        mAvailable = pAvailable;
    }

    /**
	 * @return Returns the id.
	 */
    @Id
    @Column(name = "room")
    public int getId() {
        return mId;
    }

    /**
	 * @param pId The id to set.
	 */
    public void setId(int pId) {
        mId = pId;
    }

    /**
	 * @return Returns the name.
	 */
    public String getName() {
        return mName;
    }

    /**
	 * @param pName The name to set.
	 */
    public void setName(String pName) {
        mName = pName;
    }
}
