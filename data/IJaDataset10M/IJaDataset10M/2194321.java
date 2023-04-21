package br.com.lopes.gci.manager;

import static br.com.lopes.gci.util.Constants.FINALIZANDO_METODO;
import static br.com.lopes.gci.util.Constants.INICIANDO_METODO;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import br.com.lopes.gci.dao.impl.EnderecoHibernateDAO;
import br.com.lopes.gci.dao.impl.FuncionarioHibernateDAO;
import br.com.lopes.gci.dao.impl.PerfilHibernateDAO;
import br.com.lopes.gci.exception.DAOException;
import br.com.lopes.gci.exception.GCIException;
import br.com.lopes.gci.exception.ManagerException;
import br.com.lopes.gci.model.Dominio;
import br.com.lopes.gci.model.Funcionario;
import br.com.lopes.gci.model.Perfil;
import br.com.lopes.gci.util.Constants;
import br.com.lopes.gci.util.Criptografia;

@Service
public class FuncionarioManager {

    private static final Logger LOGGER = Logger.getLogger(FuncionarioManager.class);

    @Autowired
    private FuncionarioHibernateDAO funcionarioHibernateDAO;

    @Autowired
    private PerfilHibernateDAO perfilHibernateDAO;

    @Autowired
    private EnderecoHibernateDAO enderecoHibernateDAO;

    public List<Funcionario> pesquisarFuncionario(Funcionario funcionario) throws ManagerException {
        LOGGER.debug(INICIANDO_METODO + "pesquisarFuncionario(Funcionario)");
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Funcionario.class);
            if (funcionario.getStatus().getCodigo() != null && funcionario.getStatus().getCodigo() > 0) {
                criteria.setFetchMode("status", FetchMode.JOIN);
                criteria.createAlias("status", "st");
                criteria.add(Restrictions.eq("st.codigo", funcionario.getStatus().getCodigo()));
            }
            if (funcionario.getNome() != null && !funcionario.getNome().equals("")) {
                criteria.add(Restrictions.like("nome", funcionario.getNome(), MatchMode.ANYWHERE));
            }
            if (funcionario.getTelefoneCelular() != null && !funcionario.getTelefoneCelular().equals("")) {
                criteria.add(Restrictions.like("telefoneCelular", funcionario.getTelefoneCelular(), MatchMode.ANYWHERE));
            }
            if (funcionario.getCpf() != null && !funcionario.getCpf().equals("")) {
                criteria.add(Restrictions.like("cpf", funcionario.getCpf(), MatchMode.ANYWHERE));
            }
            if (funcionario.getEmail() != null && !funcionario.getEmail().equals("")) {
                criteria.add(Restrictions.like("email", funcionario.getEmail(), MatchMode.ANYWHERE));
            }
            if (funcionario.getPerfil().getCodigo() != null && funcionario.getPerfil().getCodigo() > 0) {
                criteria.setFetchMode("perfil", FetchMode.JOIN);
                criteria.createAlias("perfil", "pr");
                criteria.add(Restrictions.eq("pr.codigo", funcionario.getPerfil().getCodigo()));
            }
            return funcionarioHibernateDAO.findByCriteria(criteria);
        } catch (DAOException e) {
            throw new ManagerException(e);
        } finally {
            LOGGER.debug(FINALIZANDO_METODO + "pesquisarFuncionario(Funcionario)");
        }
    }

    @Transactional(rollbackFor = { DAOException.class })
    public void incluirFuncionario(Funcionario funcionario) throws ManagerException {
        LOGGER.debug(INICIANDO_METODO + "incluirFuncionario(Funcionario)");
        try {
            funcionario.setSenha(Criptografia.encripta(Constants.SENHA_INICIAL));
            enderecoHibernateDAO.insert(funcionario.getEndereco());
            funcionarioHibernateDAO.insert(funcionario);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage());
            throw new ManagerException(e);
        } catch (GCIException ge) {
            LOGGER.error(ge.getMessage());
            throw new ManagerException(ge);
        } finally {
            LOGGER.debug(FINALIZANDO_METODO + "incluirFuncionario(Funcionario)");
        }
    }

    @Transactional(rollbackFor = { DAOException.class })
    public void alterarFuncionario(Funcionario funcionario) throws ManagerException {
        LOGGER.debug(INICIANDO_METODO + "alterarfuncionario(ClienteVO)");
        try {
            funcionarioHibernateDAO.update(funcionario);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage());
            throw new ManagerException(e);
        } finally {
            LOGGER.debug(FINALIZANDO_METODO + "alterarFuncionario(funcionario)");
        }
    }

    @Transactional(rollbackFor = { DAOException.class })
    public void excluirFuncionario(Funcionario funcionario) throws ManagerException {
        LOGGER.debug(INICIANDO_METODO + "excluirFuncionario(funcionario)");
        try {
            funcionario.setStatus(new Dominio(Constants.STATUS_INATIVO));
            funcionarioHibernateDAO.update(funcionario);
        } catch (DAOException e) {
            LOGGER.error(e.getMessage());
            throw new ManagerException(e);
        } finally {
            LOGGER.debug(FINALIZANDO_METODO + "excluirFuncionario(funcionario)");
        }
    }

    public Funcionario getFuncionarioById(Funcionario funcionario) throws ManagerException {
        LOGGER.debug(INICIANDO_METODO + "getFuncionarioById(Id)");
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Funcionario.class);
            criteria.add(Restrictions.eq("codigo", funcionario.getCodigo()));
            List<Funcionario> funcionarios = funcionarioHibernateDAO.findByCriteria(criteria);
            if (!funcionarios.isEmpty()) {
                return funcionarios.get(0);
            } else {
                return null;
            }
        } catch (DAOException e) {
            throw new ManagerException(e);
        } finally {
            LOGGER.debug(FINALIZANDO_METODO + "getFuncionarioById(Id)");
        }
    }

    public List<Funcionario> findFuncionarioByGerente(Funcionario funcionario) throws ManagerException {
        LOGGER.debug(INICIANDO_METODO + "findFuncionarioByGerente(Funcionario)");
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Funcionario.class);
            criteria.setFetchMode("gerente", FetchMode.JOIN);
            criteria.createAlias("gerente", "ger");
            criteria.add(Restrictions.eq("ger.codigo", funcionario.getCodigo()));
            return funcionarioHibernateDAO.findByCriteria(criteria);
        } catch (DAOException e) {
            throw new ManagerException(e);
        } finally {
            LOGGER.debug(FINALIZANDO_METODO + "findFuncionarioByGerente(Funcionario)");
        }
    }

    public List<Funcionario> findFuncionarioByDiretor(Funcionario funcionario) throws ManagerException {
        LOGGER.debug(INICIANDO_METODO + "findFuncionarioByDiretor(Funcionario)");
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Funcionario.class);
            criteria.setFetchMode("diretor", FetchMode.JOIN);
            criteria.createAlias("diretor", "dir");
            criteria.add(Restrictions.eq("dir.codigo", funcionario.getCodigo()));
            return funcionarioHibernateDAO.findByCriteria(criteria);
        } catch (DAOException e) {
            throw new ManagerException(e);
        } finally {
            LOGGER.debug(FINALIZANDO_METODO + "findFuncionarioByDiretor(Funcionario)");
        }
    }

    public List<Funcionario> getFuncionarioByPerfil(Integer idPerfil) throws ManagerException {
        LOGGER.debug(INICIANDO_METODO + "getFuncionarioByPerfil()");
        try {
            DetachedCriteria criteria = DetachedCriteria.forClass(Funcionario.class);
            criteria.add(Restrictions.eq("status.codigo", Constants.STATUS_ATIVO));
            criteria.add(Restrictions.eq("perfil.codigo", idPerfil));
            return funcionarioHibernateDAO.findByCriteria(criteria);
        } catch (DAOException e) {
            throw new ManagerException(e);
        } finally {
            LOGGER.debug(FINALIZANDO_METODO + "getFuncionarioByPerfil()");
        }
    }

    public Perfil getPerfilById(Integer codigo) throws ManagerException {
        LOGGER.debug(INICIANDO_METODO + "getPerfilById(codigo)");
        try {
            try {
                DetachedCriteria criteria = DetachedCriteria.forClass(Perfil.class);
                criteria.add(Restrictions.eq("codigo", codigo));
                List<Perfil> perfis = perfilHibernateDAO.findByCriteria(criteria);
                if (!perfis.isEmpty()) {
                    return perfis.get(0);
                } else {
                    return null;
                }
            } catch (DAOException e) {
                throw new ManagerException(e);
            }
        } finally {
            LOGGER.debug(FINALIZANDO_METODO + "getPerfilById(codigo)");
        }
    }

    public List<Perfil> getPerfis() throws ManagerException {
        LOGGER.debug(INICIANDO_METODO + "getPerfis()");
        try {
            try {
                return perfilHibernateDAO.findAll();
            } catch (DAOException e) {
                throw new ManagerException(e);
            }
        } finally {
            LOGGER.debug(FINALIZANDO_METODO + "getPerfis()");
        }
    }
}
