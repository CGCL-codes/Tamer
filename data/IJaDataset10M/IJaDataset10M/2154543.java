package dao;

import java.util.Collection;
import modelo.Coordenador;
import org.hibernate.HibernateException;

public class CoordenadorDAO extends DAOMenor {

    public void save(Coordenador coordenador) {
        super.save(coordenador);
        super.closeSession();
    }

    public Coordenador validarObjeto(Coordenador coordenador) throws HibernateException {
        return (Coordenador) this.validarObjeto(coordenador, coordenador.getNome());
    }

    @SuppressWarnings("unchecked")
    public Collection<Coordenador> getAll() throws org.hibernate.HibernateException {
        return this.find("FROM " + Coordenador.class.getName() + " as coordenador ORDER BY coordenador.nome");
    }
}
