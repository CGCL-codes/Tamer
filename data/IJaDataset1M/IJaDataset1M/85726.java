package br.com.ita.rentacar.dao;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

public class Dao<T> {

    private final Session session;

    private final Class classe;

    public Dao(Session session, Class classe) {
        this.session = session;
        this.classe = classe;
    }

    public void cadastrar(T t) {
        this.session.save(t);
    }

    public void excluir(T t) {
        this.session.delete(t);
    }

    public void atualizar(T t) {
        this.session.update(t);
    }

    public List<T> listarTudo() {
        return this.session.createCriteria(this.classe).list();
    }

    public T consultar(Integer id) {
        return (T) session.load(this.classe, id);
    }

    public List<T> consultar(String campo, String valor) {
        Query query = this.session.createQuery("from " + this.classe.getName() + " as entity where entity." + campo + " like '%" + valor + "%'");
        return (List<T>) query.list();
    }
}
