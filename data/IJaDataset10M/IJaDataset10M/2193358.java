package clinicapoo.dao;

import clinicapoo.model.Paciente;
import java.util.List;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Christiano
 */
public class PacienteDAO extends BaseDAO {

    public void cadastrar(Paciente p) {
        getEntityManager().persist(p);
    }

    public void atualizar(Paciente p) {
        getEntityManager().merge(p);
    }

    public void alterar(String s, Paciente p) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PacienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        String url = "jdbc:postgresql://localhost:5432/teste?user=postgres&password=chrtn";
        String sql = "update paciente set cpf = ?, nome = ?, address = ?, telefone = ?, " + "celular = ? where cpf = ?;";
        Connection con = null;
        try {
            con = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            Logger.getLogger(PacienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, p.getCpf());
            st.setString(2, p.getNome());
            st.setString(3, p.getAddress());
            st.setString(4, p.getTelefone());
            st.setString(5, p.getCelular());
            st.setString(6, s);
            st.executeUpdate();
            atualizar(p);
            System.out.println("dados atualizados");
            st.close();
        } catch (SQLException ex) {
            Logger.getLogger(PacienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void excluir(Paciente p) {
        log.debug(p.toString());
        getEntityManager().remove(p);
    }

    public Paciente procurarPorCpf(String cpf) {
        log.debug("cpf: " + cpf);
        List<Paciente> pacientes = getEntityManager().createQuery("from Paciente p where p.cpf = :cpf").setParameter("cpf", cpf).getResultList();
        if (pacientes.isEmpty()) return null; else return pacientes.get(0);
    }

    public List<Paciente> listar() {
        log.debug("Recuperando lista de pacientes");
        return getEntityManager().createQuery("select object(p) from Paciente p").getResultList();
    }
}
