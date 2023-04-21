package br.ufrj.cad.model.orientacao.academica;

import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import br.ufrj.cad.fwk.exception.BaseRuntimeException;
import br.ufrj.cad.fwk.exception.Notification;
import br.ufrj.cad.fwk.model.BaseService;
import br.ufrj.cad.fwk.model.ObjetoPersistente;
import br.ufrj.cad.fwk.model.Transaction;
import br.ufrj.cad.fwk.model.TransactionReadOnly;
import br.ufrj.cad.fwk.model.hibernate.CriteriaUtils;
import br.ufrj.cad.model.bo.BancaProfessor;
import br.ufrj.cad.model.bo.OrientacaoAcademica;
import br.ufrj.cad.model.bo.OrientacaoAcademicaAluno;
import br.ufrj.cad.model.bo.Usuario;
import br.ufrj.cad.model.to.OrientacaoAcademicaTO;

public class OrientacaoAcademicaService extends BaseService {

    private static OrientacaoAcademicaService _instance;

    public static synchronized OrientacaoAcademicaService getInstance() {
        if (_instance == null) {
            _instance = new OrientacaoAcademicaService();
        }
        return _instance;
    }

    public OrientacaoAcademica obtemOrientacaoAcademicaPorId(Long id) {
        return (OrientacaoAcademica) transactionTemplate.execute(getTransactionObtemOrientacaoAcademicaPorid(id));
    }

    public List<OrientacaoAcademica> obtemOrientacaoAcademicaPorProfessor(Usuario professor) {
        return (List<OrientacaoAcademica>) transactionTemplate.execute(getTransactionObtemOrientacaoAcademicaPorProfessor(professor));
    }

    public void excluirOrientacaoAcademica(OrientacaoAcademica orientacaoAcademica) {
        transactionTemplate.execute(getTransactionExcluirOrientacaoAcademica(orientacaoAcademica));
    }

    private Transaction getTransactionObtemOrientacaoAcademicaPorid(final Long id) {
        return new TransactionReadOnly() {

            public Object doInTransaction(Session session) {
                return session.get(OrientacaoAcademica.class, id);
            }
        };
    }

    private Transaction getTransactionObtemOrientacaoAcademicaPorProfessor(final Usuario professor) {
        return new TransactionReadOnly() {

            public Object doInTransaction(Session session) {
                Criteria crit = session.createCriteria(OrientacaoAcademica.class);
                CriteriaUtils.adicionaCriterio(crit, CriteriaUtils.adicionaRestricaoIgual("professor", professor));
                return crit.list();
            }
        };
    }

    public ObjetoPersistente salvaOrientacaoAcademica(OrientacaoAcademicaTO novoOrientacaoAcademicaTO, Usuario usuario, boolean edicao) {
        OrientacaoAcademica orientacaoAcademica = new OrientacaoAcademica(novoOrientacaoAcademicaTO);
        Notification erros = new Notification();
        if (!edicao && orientacaoAcademicaJaExiste(orientacaoAcademica)) {
            throw new BaseRuntimeException("orientacaoAcademica.ja.existe");
        }
        if (erros.size() > 0) {
            throw new BaseRuntimeException(erros);
        }
        if (edicao) {
            OrientacaoAcademica orientacaoAcademicaNoBanco = new OrientacaoAcademica();
            if (orientacaoAcademicaJaExiste(orientacaoAcademicaNoBanco) && !orientacaoAcademica.getId().equals(orientacaoAcademicaNoBanco.find().getId())) {
                throw new BaseRuntimeException("orientacaoAcademica.ja.existe");
            }
            return alteraOrientacaoAcademica(orientacaoAcademica, usuario);
        } else {
            return insereOrientacaoAcademica(orientacaoAcademica, usuario);
        }
    }

    private boolean orientacaoAcademicaJaExiste(OrientacaoAcademica orientacaoAcademica) {
        return false;
    }

    private ObjetoPersistente alteraOrientacaoAcademica(ObjetoPersistente orientacaoAcademica, Usuario usuario) {
        return (ObjetoPersistente) transactionTemplate.execute(getTransactionAlteraOrientacaoAcademica(orientacaoAcademica));
    }

    private Transaction getTransactionAlteraOrientacaoAcademica(final ObjetoPersistente orientacaoAcademica) {
        return new Transaction() {

            public Object doInTransaction(Session session) {
                session.update(orientacaoAcademica);
                return orientacaoAcademica;
            }
        };
    }

    private ObjetoPersistente insereOrientacaoAcademica(ObjetoPersistente orientacaoAcademica, Usuario usuario) {
        return (ObjetoPersistente) transactionTemplate.execute(getTransactionInserirOrientacaoAcademica(orientacaoAcademica));
    }

    private Transaction getTransactionInserirOrientacaoAcademica(final ObjetoPersistente orientacaoAcademica) {
        return new Transaction() {

            public Object doInTransaction(Session session) {
                session.save(orientacaoAcademica);
                return orientacaoAcademica;
            }
        };
    }

    public List<OrientacaoAcademicaAluno> obtemOrientacaoAcademicaAlunos(final OrientacaoAcademica orientacaoAcademica) {
        return (List<OrientacaoAcademicaAluno>) transactionTemplate.execute(getTransactionOrientacaoAcademicaAlunos(orientacaoAcademica));
    }

    private Transaction getTransactionOrientacaoAcademicaAlunos(final ObjetoPersistente orientacaoAcademica) {
        return new Transaction() {

            public Object doInTransaction(Session session) {
                Criteria crit = session.createCriteria(OrientacaoAcademicaAluno.class);
                CriteriaUtils.adicionaCriterio(crit, CriteriaUtils.adicionaRestricaoIgual("orientacaoAcademica", orientacaoAcademica));
                return crit.list();
            }
        };
    }

    private Transaction getTransactionExcluirOrientacaoAcademica(final ObjetoPersistente orientacaoAcademica) {
        return new Transaction() {

            public Object doInTransaction(Session session) {
                session.delete(orientacaoAcademica);
                return orientacaoAcademica;
            }
        };
    }
}
