package name.emu.webapp.kos.domain;

import javax.persistence.Entity;
import javax.persistence.Lob;

/**
 * Users and other groups who have an access key for this group through, are the group's members.
 * Members inherit all the group's access keys. Cyclic access key relationships are not allowed.
 * @author emu
 *
 */
@Entity
public class KosGroup extends SecurityEntity {

    private String name;

    @Lob
    private byte[] encryptedPrivateKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getEncryptedPrivateKey() {
        return encryptedPrivateKey;
    }

    public void setEncryptedPrivateKey(byte[] encryptedPrivateKey) {
        this.encryptedPrivateKey = encryptedPrivateKey;
    }
}
