package net.etherstorm.jopenrpg.net;

/**
 * 
 * 
 * @author Ted Berg
 * @author $Author: tedberg $
 * @version $Revision: 1.5 $
 * $Date: 2002/03/09 08:37:10 $
 */
public class CreateGroupMessage extends AbstractGroupMessage {

    /**
	 *
	 */
    public CreateGroupMessage() {
        super("create_group");
    }

    /**
	 *
	 */
    public void clear() {
        super.clear();
        setBootPwd("");
        setName("");
    }

    /**
	 *
	 */
    public String getBootPwd() {
        return get("boot_pw");
    }

    /**
	 *
	 */
    public void setBootPwd(String bootPwd) {
        set("boot_pw", bootPwd);
    }

    /**
	 *
	 */
    public String getName() {
        return get("name");
    }

    /**
	 *
	 */
    public void setName(String name) {
        set("name", name);
    }

    /**
	 *
	 */
    public void sendRemote() {
        referenceManager.getCore().sendRemote(this);
    }

    /**
	 *
	 */
    public void sendLocal() {
        referenceManager.getCore().sendLocal(this);
    }
}
