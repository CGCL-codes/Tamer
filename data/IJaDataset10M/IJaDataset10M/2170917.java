package br.com.insight.consultoria.negocio.bo;

import java.util.List;
import br.com.insight.consultoria.entity.Area;
import br.com.insight.consultoria.entity.ConhecimentoInformatica;
import br.com.insight.consultoria.entity.ConhecimentoLinguaEstrangeira;
import br.com.insight.consultoria.entity.Tipo;
import br.com.insight.consultoria.entity.Usuario;
import br.com.insight.consultoria.erro.exception.InsightConstants;
import br.com.insight.consultoria.erro.exception.InsightException;
import br.com.insight.consultoria.erro.exception.InsightNegocioException;
import br.com.insight.consultoria.erro.exception.SistemaException;
import br.com.insight.consultoria.integracao.dao.interfacedao.ConhecimentoInformaticaDAO;
import br.com.insight.consultoria.integracao.dao.interfacedao.ConhecimentoLinguaEstrangeiraDAO;
import br.com.insight.consultoria.negocio.bo.interfacebo.ConhecimentoInformaticaBO;
import br.com.insight.consultoria.negocio.bo.interfacebo.ConhecimentoLinguaEstrangeiraBO;

public class ConhecimentoInformaticaImpBO implements ConhecimentoInformaticaBO {

    private ConhecimentoInformaticaDAO conhecimentoUsuarioDAO;

    @Override
    public void inserir(ConhecimentoInformatica conhecimentoUsuario) throws InsightException {
        try {
            getConhecimentoUsuarioDAO().inserir(conhecimentoUsuario);
        } catch (InsightException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new SistemaException(InsightConstants.MENSAGEM_ERRO_NAO_TRATADO, exception);
        }
    }

    @Override
    public void alterar(ConhecimentoInformatica conhecimentoUsuario) throws InsightException {
        try {
            getConhecimentoUsuarioDAO().alterar(conhecimentoUsuario);
        } catch (InsightException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new SistemaException(InsightConstants.MENSAGEM_ERRO_NAO_TRATADO, exception);
        }
    }

    @Override
    public void excluir(ConhecimentoInformatica conhecimentoUsuario) throws InsightException {
        try {
            getConhecimentoUsuarioDAO().excluir(conhecimentoUsuario);
        } catch (InsightException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new SistemaException(InsightConstants.MENSAGEM_ERRO_NAO_TRATADO, exception);
        }
    }

    @Override
    public ConhecimentoInformatica getConhecimentoUsuario(ConhecimentoInformatica.IdInformaticaUsuario id) throws InsightException {
        ConhecimentoInformatica conhecimentoUsuario = null;
        try {
            conhecimentoUsuario = getConhecimentoUsuarioDAO().getConhecimentoUsuario(id);
        } catch (InsightException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new SistemaException(InsightConstants.MENSAGEM_ERRO_NAO_TRATADO, exception);
        }
        return conhecimentoUsuario;
    }

    @Override
    public List<ConhecimentoInformatica> pesquisar(Usuario usuario) throws InsightException {
        List<ConhecimentoInformatica> conhecimentoUsuario = null;
        try {
            conhecimentoUsuario = getConhecimentoUsuarioDAO().pesquisar(usuario);
        } catch (InsightException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new SistemaException(InsightConstants.MENSAGEM_ERRO_NAO_TRATADO, exception);
        }
        return conhecimentoUsuario;
    }

    @Override
    public List<ConhecimentoInformatica> pesquisar(Usuario usuario, Tipo tipo) throws InsightException {
        List<ConhecimentoInformatica> conhecimentoUsuario = null;
        try {
            conhecimentoUsuario = getConhecimentoUsuarioDAO().pesquisar(usuario, tipo);
        } catch (InsightException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new SistemaException(InsightConstants.MENSAGEM_ERRO_NAO_TRATADO, exception);
        }
        return conhecimentoUsuario;
    }

    @Override
    public List<ConhecimentoInformatica> pesquisar(Usuario usuario, Area area) throws InsightException {
        List<ConhecimentoInformatica> conhecimentoUsuario = null;
        try {
            conhecimentoUsuario = getConhecimentoUsuarioDAO().pesquisar(usuario, area);
        } catch (InsightException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new SistemaException(InsightConstants.MENSAGEM_ERRO_NAO_TRATADO, exception);
        }
        return conhecimentoUsuario;
    }

    public boolean isCadastrado(ConhecimentoInformatica.IdInformaticaUsuario id) throws InsightException {
        ConhecimentoInformatica conhecimentoUsuario = getConhecimentoUsuario(id);
        if (conhecimentoUsuario != null) return true;
        return false;
    }

    public ConhecimentoInformaticaDAO getConhecimentoUsuarioDAO() {
        return conhecimentoUsuarioDAO;
    }

    public void setConhecimentoUsuarioDAO(ConhecimentoInformaticaDAO conhecimentoUsuarioDAO) {
        this.conhecimentoUsuarioDAO = conhecimentoUsuarioDAO;
    }
}
