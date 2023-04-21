package br.com.insight.consultoria.integracao.dao;

import java.util.List;
import org.springframework.dao.DataAccessException;
import br.com.insight.consultoria.entity.EstadoCivil;
import br.com.insight.consultoria.erro.exception.InsightConstants;
import br.com.insight.consultoria.erro.exception.InsightDaoException;
import br.com.insight.consultoria.erro.exception.InsightException;
import br.com.insight.consultoria.erro.exception.SistemaException;
import br.com.insight.consultoria.integracao.dao.genericdao.GenericDAO;
import br.com.insight.consultoria.integracao.dao.interfacedao.EstadoCivilDAO;

public class EstadoCivilImpDAO extends GenericDAO implements EstadoCivilDAO {

    public List<EstadoCivil> listaEstadoCivil() throws InsightException {
        List findAll = null;
        try {
            findAll = findAll(EstadoCivil.class);
        } catch (DataAccessException sql) {
            throw new InsightDaoException(InsightConstants.MENSAGEM_ERRO_ACESSO_BANCO, sql);
        } catch (Exception ex) {
            throw new SistemaException(InsightConstants.MENSAGEM_ERRO_NAO_TRATADO, ex);
        }
        return findAll;
    }

    @Override
    public EstadoCivil getEstadoCivil(Long id) throws InsightException {
        EstadoCivil estadoCivil = null;
        try {
            estadoCivil = (EstadoCivil) getEntityById(EstadoCivil.class, id);
        } catch (DataAccessException sql) {
            throw new InsightDaoException(InsightConstants.MENSAGEM_ERRO_ACESSO_BANCO, sql);
        } catch (Exception ex) {
            throw new SistemaException(InsightConstants.MENSAGEM_ERRO_NAO_TRATADO, ex);
        }
        return estadoCivil;
    }
}
