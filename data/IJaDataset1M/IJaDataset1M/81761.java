package control.server;

import Excessoes.ExcessaoDAO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import model.bean.EnderecoJB;
import model.dao.ConexoesBD;
import model.dao.EnderecoDAO;

/**
 *
 * @author Alessandro
 */
public class EnderecoControl {

    Connection conn = null;

    EnderecoDAO dao = null;

    public EnderecoControl() {
        this.dao = new EnderecoDAO();
    }

    /**Método que insere os atributos do objeto endereco na tabela endereco do banco tccSmartLAundry
     *
     * @param endereco
     * @param conn
     * @throws ExcessaoDAO
     */
    public void setInsereEndereco(EnderecoJB endereco) {
        try {
            this.conn = new ConexoesBD().getPostgres();
            this.dao.insereEndereco(endereco, this.conn);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                this.conn.close();
            } catch (Exception e) {
            }
        }
    }

    /**Método que retorna um objeto enderecojb com informacoes do banco
     * 
     * @param endereco
     * @return
     * @throws ExcessaoDAO
     */
    public EnderecoJB getEnderecoJB(EnderecoJB endereco) throws ExcessaoDAO {
        EnderecoJB ende = null;
        try {
            this.conn = new ConexoesBD().getPostgres();
            ende = new EnderecoJB();
            ende = this.dao.retornaEnderecoJB(this.conn, endereco);
        } catch (Exception e) {
            throw new ExcessaoDAO("Não foi possivel retornar um enderecojb");
        } finally {
            try {
                this.conn.close();
            } catch (Exception e) {
            }
        }
        return ende;
    }

    /**Método que verifica a existência de determinado endereço no banco de dados
     *
     * @param conn
     * @param endereco
     * @return
     * @throws ExcessaoDAO
     */
    public boolean getEnderecoCadastrado(EnderecoJB endereco, Connection conn) {
        boolean cad = true;
        try {
            this.conn = new ConexoesBD().getPostgres();
            cad = this.dao.enderecoCadastrado(this.conn, endereco);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                this.conn.close();
            } catch (Exception e) {
            }
        }
        return cad;
    }

    /**Retorna todos os cep´s cadastrados no servidor
     *
     * @param conn
     * @return
     * @throws Exception
     */
    public List<String> cepsCadastrados() throws Exception {
        List<String> lista = null;
        try {
            this.conn = new ConexoesBD().getPostgres();
            lista = this.dao.cepsCadastrados(this.conn);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                this.conn.close();
            } catch (Exception e) {
            }
        }
        return lista;
    }
}
