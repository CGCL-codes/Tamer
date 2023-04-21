package clinicapoo.services;

import clinicapoo.dao.Transaction;
import clinicapoo.exceptions.ApplicationException;
import clinicapoo.factory.Business;
import clinicapoo.factory.DAO;
import clinicapoo.model.Paciente;
import java.util.List;

/**
 * @author Christiano
 */
public class PacienteServices extends BaseServices {

    public void cadastrar(Paciente p) throws ApplicationException {
        Transaction tx = null;
        try {
            tx = new Transaction();
            Business.paciente.cadastrar(p);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw new ApplicationException(e.getMessage(), e);
        }
    }

    public List<Paciente> listar() {
        log.debug("Listando pacientes");
        return DAO.paciente.listar();
    }

    public void alterar(String cpf, Paciente p) {
        log.debug("alterando dados de paciente");
        DAO.paciente.alterar(cpf, p);
    }
}
