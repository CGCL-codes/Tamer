package net.etherstorm.jopenrpg.net;

import org.jdom.Document;

/** 
 * Message that propagates role changes.
 * <p>
 * Available roles are :
 * <ul>
 *	 <li>Lurker</li>
 *	 <li>Player</li>
 *	 <li>GM</li>
 * </ul>
 * 
 * @author Ted Berg (<a href="mailto:tedberg@users.sourceforge.net">tedberg@users.sourceforge.net</a>)
 * @version $Revision: 1.2 $
 */
public class RoleMessage extends AbstractMessage {

    /** 
	 * 
	 */
    public RoleMessage() {
        super("role");
    }

    public void setPlayer(String player) {
        set("player", player);
    }

    public String getPlayer() {
        return get("player");
    }

    public void setAction(String action) {
        set("action", action);
    }

    public String getAction() {
        return get("action");
    }

    public void setPassword(String password) {
        set("boot_pw", password);
    }

    public String getPassword() {
        return get("boot_pw");
    }

    /** 
	 * Not implemented.
	 * 
	 * @param doc 
	 */
    public void fromXML(Document doc) {
    }

    public void sendLocal() {
    }

    public void sendRemote() {
        referenceManager.getCore().sendRemote((AbstractMessage) this);
    }

    public void setRole(String role) {
        set("role", role);
    }

    public String getRole() {
        return get("role");
    }
}
