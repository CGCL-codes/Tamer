package dao;

import modelo.Quem_era_acusado;
import org.hibernate.HibernateException;

public class Quem_era_acusadoDAO extends DAOMenor {

    public Quem_era_acusado validarObjeto(Quem_era_acusado quem_era_acusado) throws HibernateException {
        return (Quem_era_acusado) this.validarObjeto(quem_era_acusado, quem_era_acusado.getNome());
    }
}
