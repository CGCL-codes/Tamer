package br.com.insight.consultoria.negocio.bo;

import java.util.List;
import br.com.insight.consultoria.entity.ExperienciaProfissional;
import br.com.insight.consultoria.entity.Usuario;
import br.com.insight.consultoria.erro.exception.InsightConstants;
import br.com.insight.consultoria.erro.exception.InsightException;
import br.com.insight.consultoria.erro.exception.SistemaException;
import br.com.insight.consultoria.integracao.dao.interfacedao.ExperienciaProfissionalDAO;
import br.com.insight.consultoria.negocio.bo.interfacebo.ExperienciaProfissionalBO;

public class ExperienciaProfissionalImpBO implements ExperienciaProfissionalBO {

    private ExperienciaProfissionalDAO experienciaProfissionalDAO;

    public void inserir(ExperienciaProfissional profissional) throws InsightException {
        try {
            getExperienciaProfissionalDAO().inserir(profissional);
        } catch (InsightException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new SistemaException(InsightConstants.MENSAGEM_ERRO_NAO_TRATADO, exception);
        }
    }

    public void alterar(ExperienciaProfissional profissional) throws InsightException {
        try {
            getExperienciaProfissionalDAO().alterar(profissional);
        } catch (InsightException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new SistemaException(InsightConstants.MENSAGEM_ERRO_NAO_TRATADO, exception);
        }
    }

    public void excluir(ExperienciaProfissional profissional) throws InsightException {
        try {
            getExperienciaProfissionalDAO().excluir(profissional);
        } catch (InsightException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new SistemaException(InsightConstants.MENSAGEM_ERRO_NAO_TRATADO, exception);
        }
    }

    public List<ExperienciaProfissional> pesquisar(Usuario usuario) throws InsightException {
        try {
            return getExperienciaProfissionalDAO().pesquisar(usuario);
        } catch (InsightException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new SistemaException(InsightConstants.MENSAGEM_ERRO_NAO_TRATADO, exception);
        }
    }

    public ExperienciaProfissional getExperienciaProfissional(Long id) throws InsightException {
        try {
            return getExperienciaProfissionalDAO().getExperienciaProfissional(id);
        } catch (InsightException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new SistemaException(InsightConstants.MENSAGEM_ERRO_NAO_TRATADO, exception);
        }
    }

    public ExperienciaProfissionalDAO getExperienciaProfissionalDAO() {
        return experienciaProfissionalDAO;
    }

    public void setExperienciaProfissionalDAO(ExperienciaProfissionalDAO experienciaProfissionalDAO) {
        this.experienciaProfissionalDAO = experienciaProfissionalDAO;
    }
}
