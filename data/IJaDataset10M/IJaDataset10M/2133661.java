package dao;

import java.util.Collection;
import modelo.Local_resgistro_ocorrencia;
import org.hibernate.HibernateException;

public class Local_registro_ocorrenciaDAO extends DAOMenor {

    public void save(Local_resgistro_ocorrencia local_resgistro_ocorrencia) {
        super.save(local_resgistro_ocorrencia);
        super.closeSession();
    }

    public Local_resgistro_ocorrencia validarObjeto(Local_resgistro_ocorrencia local_resgistro_ocorrencia) throws HibernateException {
        return (Local_resgistro_ocorrencia) this.validarObjeto(local_resgistro_ocorrencia, local_resgistro_ocorrencia.getNome());
    }

    @SuppressWarnings("unchecked")
    public Collection getAll() throws org.hibernate.HibernateException {
        return this.find("SELECT distinct(local_resgistro_ocorrencia.nome) FROM " + Local_resgistro_ocorrencia.class.getName() + " as local_resgistro_ocorrencia ORDER BY local_resgistro_ocorrencia.nome");
    }
}
