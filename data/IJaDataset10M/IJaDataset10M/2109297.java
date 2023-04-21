package casdadm.core.common;

import casdadm.core.GenericJPADAO;
import casdadm.domain.common.Telefone;

/**
 *
 * @author Anderson Aiziro
 */
public class TelefoneDAO extends GenericJPADAO<Telefone> {

    public TelefoneDAO() {
        super(Telefone.class);
    }
}
