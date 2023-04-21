package dao.pessoa;

import java.util.List;
import javax.persistence.Query;
import dao.base.BaseDao;
import entidades.aluno.Aluno;
import entidades.pessoa.Pessoa;

public class PessoaDao extends BaseDao<Aluno> {

    public static final String PARAMETRO_UNICO = "parametro";

    @SuppressWarnings("unchecked")
    public Pessoa buscarPorCpf(String login) {
        String sql = "select pessoa from " + Pessoa.class.getName() + " as pessoa " + "Where (pessoa.cpf=" + ":" + PARAMETRO_UNICO + ")";
        Query query = this.getEntityManager().createQuery(sql);
        query.setParameter(PARAMETRO_UNICO, login);
        List<Pessoa> resultado = query.getResultList();
        if (resultado.size() < 1) {
            return null;
        }
        return resultado.get(0);
    }
}
