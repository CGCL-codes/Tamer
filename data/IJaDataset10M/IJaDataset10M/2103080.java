package name.emu.webapp.kos.dao;

import name.emu.webapp.kos.domain.SecretAccount;

public interface SecretAccountDao {

    public SecretAccount findById(long id);

    public void save(SecretAccount secretAccount);

    public void delete(SecretAccount secretAccount);
}
