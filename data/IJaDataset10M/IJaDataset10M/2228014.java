package com.sos.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.sos.vo.*;
import com.sos.exception.PersistenceException;

public class ClienteDAO {

    public static final String SELECT_CLIENTES = "SELECT * " + "FROM Clientes " + "ORDER BY identificacao";

    public static final String SELECT_CLIENTE_POR_COD_CLIENTE = "SELECT * " + "FROM Clientes " + "WHERE codCliente = ?";

    public static final String SELECT_CLIENTE_POR_COD_PESSOA = "SELECT * " + "FROM Clientes " + "WHERE codPessoa = ?";

    public Cliente obterCliente(Cliente cliente) throws PersistenceException {
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;
        try {
            conn = ConnectionManager.getInstance().getConnection();
            if (cliente.getCodPessoa() > 0) {
                pstm = conn.prepareStatement(SELECT_CLIENTE_POR_COD_PESSOA);
                pstm.setInt(1, cliente.getCodPessoa());
            } else {
                pstm = conn.prepareStatement(SELECT_CLIENTE_POR_COD_CLIENTE);
                pstm.setInt(1, cliente.getCodCliente());
            }
            rset = pstm.executeQuery();
            cliente = null;
            if (rset.next()) {
                cliente = new Cliente();
                cliente.setCodCliente(rset.getInt("codCliente"));
                cliente.setCodPessoa(rset.getInt("codPessoa"));
                cliente.setTipoPessoa(rset.getString("tipoPessoa").charAt(0));
                cliente.setCep(rset.getString("cep"));
                cliente.setEndereco(rset.getString("endereco"));
                cliente.setBairro(rset.getString("bairro"));
                Cidade cidade = new Cidade();
                cidade.setCodCidade(rset.getInt("codCidade"));
                cidade.setCidade(rset.getString("cidade"));
                Estado estado = new Estado();
                estado.setCodEstado(rset.getInt("codEstado"));
                estado.setEstado(rset.getString("estado"));
                estado.setSigla(rset.getString("sigla"));
                cidade.setEstado(estado);
                cliente.setCidade(cidade);
                cliente.setEmail(rset.getString("email"));
                cliente.setObservacoes(rset.getString("observacoes"));
                cliente.setAutorizaNotificacoes(rset.getString("autorizaNotificacoes").charAt(0) == '1');
                cliente.setCadastroAtivo(rset.getString("cadastroAtivo").charAt(0) == '1');
                cliente.setDataRegistro(rset.getTimestamp("dataRegistro"));
                cliente.setDataModificacao(rset.getTimestamp("dataModificacao"));
                cliente.setDataUltimoLogin(rset.getTimestamp("dataUltimoLogin"));
                if (cliente.getTipoPessoa() == 'F') {
                    cliente.setNome(rset.getString("nome"));
                    cliente.setApelido(rset.getString("apelido"));
                    cliente.setSexo(rset.getString("sexo").charAt(0));
                    cliente.setDataNascimento(rset.getDate("dataNascimento"));
                    cliente.setCpf(rset.getString("cpf"));
                    cliente.setTelefoneResidencial(rset.getString("telefoneResidencial"));
                    cliente.setTelefoneCelular(rset.getString("telefoneCelular"));
                    cliente.setTelefoneFax(rset.getString("telefoneFax"));
                } else if (cliente.getTipoPessoa() == 'J') {
                    cliente.setRazaoSocial(rset.getString("razaoSocial"));
                    cliente.setNomeFantasia(rset.getString("nomeFantasia"));
                    cliente.setNomeResponsavel(rset.getString("nomeResponsavel"));
                    cliente.setCnpj(rset.getString("cnpj"));
                    cliente.setTelefoneComercial(rset.getString("telefoneComercial"));
                    cliente.setTelefoneCelular(rset.getString("telefoneCelular"));
                    cliente.setTelefoneFax(rset.getString("telefoneFax"));
                }
            }
            return cliente;
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao obter cliente: " + e.getMessage());
        } finally {
            try {
                if (rset != null) {
                    rset.close();
                }
                if (pstm != null) {
                    pstm.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    public List<Cliente> obterClientes() throws PersistenceException {
        List<Cliente> clientes = new ArrayList<Cliente>();
        Connection conn = null;
        PreparedStatement pstm = null;
        ResultSet rset = null;
        try {
            conn = ConnectionManager.getInstance().getConnection();
            pstm = conn.prepareStatement(SELECT_CLIENTES);
            rset = pstm.executeQuery();
            while (rset.next()) {
                Cliente cliente = new Cliente();
                cliente.setCodCliente(rset.getInt("codCliente"));
                cliente.setCodPessoa(rset.getInt("codPessoa"));
                cliente.setTipoPessoa(rset.getString("tipoPessoa").charAt(0));
                cliente.setCep(rset.getString("cep"));
                cliente.setEndereco(rset.getString("endereco"));
                cliente.setBairro(rset.getString("bairro"));
                Cidade cidade = new Cidade();
                cidade.setCodCidade(rset.getInt("codCidade"));
                cidade.setCidade(rset.getString("cidade"));
                Estado estado = new Estado();
                estado.setCodEstado(rset.getInt("codEstado"));
                estado.setEstado(rset.getString("estado"));
                estado.setSigla(rset.getString("sigla"));
                cidade.setEstado(estado);
                cliente.setCidade(cidade);
                cliente.setEmail(rset.getString("email"));
                cliente.setObservacoes(rset.getString("observacoes"));
                cliente.setAutorizaNotificacoes(rset.getString("autorizaNotificacoes").charAt(0) == '1');
                cliente.setCadastroAtivo(rset.getString("cadastroAtivo").charAt(0) == '1');
                cliente.setDataRegistro(rset.getTimestamp("dataRegistro"));
                cliente.setDataModificacao(rset.getTimestamp("dataModificacao"));
                cliente.setDataUltimoLogin(rset.getTimestamp("dataUltimoLogin"));
                if (cliente.getTipoPessoa() == 'F') {
                    cliente.setNome(rset.getString("nome"));
                    cliente.setApelido(rset.getString("apelido"));
                    cliente.setSexo(rset.getString("sexo").charAt(0));
                    cliente.setDataNascimento(rset.getDate("dataNascimento"));
                    cliente.setCpf(rset.getString("cpf"));
                    cliente.setTelefoneResidencial(rset.getString("telefoneResidencial"));
                    cliente.setTelefoneCelular(rset.getString("telefoneCelular"));
                    cliente.setTelefoneFax(rset.getString("telefoneFax"));
                } else if (cliente.getTipoPessoa() == 'J') {
                    cliente.setRazaoSocial(rset.getString("razaoSocial"));
                    cliente.setNomeFantasia(rset.getString("nomeFantasia"));
                    cliente.setNomeResponsavel(rset.getString("nomeResponsavel"));
                    cliente.setCnpj(rset.getString("cnpj"));
                    cliente.setTelefoneComercial(rset.getString("telefoneComercial"));
                    cliente.setTelefoneCelular(rset.getString("telefoneCelular"));
                    cliente.setTelefoneFax(rset.getString("telefoneFax"));
                }
                clientes.add(cliente);
            }
            return clientes;
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao obter listagem de clientes: " + e.getMessage());
        } finally {
            try {
                if (rset != null) {
                    rset.close();
                }
                if (pstm != null) {
                    pstm.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    public void inserirCliente(Cliente cliente, String senha) throws PersistenceException {
        Connection conn = null;
        StatementManager stmm = null;
        CallableStatement cstm = null;
        try {
            conn = ConnectionManager.getInstance().getConnection();
            stmm = StatementManager.getInstance();
            if (cliente.getTipoPessoa() == 'F') {
                cstm = (CallableStatement) conn.prepareCall(stmm.getStatement("PROCEDURE_INSERIR_CLIENTE_PF"));
                cstm.setString("_cpf", cliente.getCpf());
                cstm.setString("_nome", cliente.getNome());
                if (cliente.getApelido() != null) {
                    cstm.setString("_apelido", cliente.getApelido());
                } else {
                    cstm.setNull("_apelido", java.sql.Types.NULL);
                }
                cstm.setString("_sexo", String.valueOf(cliente.getSexo()));
                cstm.setDate("_dataNascimento", new java.sql.Date(cliente.getDataNascimento().getTime()));
                if (cliente.getTelefoneResidencial() != null) {
                    cstm.setString("_telefoneResidencial", cliente.getTelefoneResidencial());
                } else {
                    cstm.setNull("_telefoneResidencial", java.sql.Types.NULL);
                }
            } else {
                cstm = (CallableStatement) conn.prepareCall(stmm.getStatement("PROCEDURE_INSERIR_CLIENTE_PJ"));
                cstm.setString("_cnpj", cliente.getCnpj());
                cstm.setString("_razaoSocial", cliente.getRazaoSocial());
                if (cliente.getNomeFantasia() != null) {
                    cstm.setString("_nomeFantasia", cliente.getNomeFantasia());
                } else {
                    cstm.setNull("_nomeFantasia", java.sql.Types.NULL);
                }
                cstm.setString("_nomeResponsavel", cliente.getNomeResponsavel());
                if (cliente.getTelefoneComercial() != null) {
                    cstm.setString("_telefoneComercial", cliente.getTelefoneComercial());
                } else {
                    cstm.setNull("_telefoneComercial", java.sql.Types.NULL);
                }
            }
            cstm.setString("_cep", cliente.getCep());
            cstm.setString("_endereco", cliente.getEndereco());
            cstm.setString("_bairro", cliente.getBairro());
            cstm.setInt("_codCidade", cliente.getCidade().getCodCidade());
            if (cliente.getTelefoneCelular() != null) {
                cstm.setString("_telefoneCelular", cliente.getTelefoneCelular());
            } else {
                cstm.setNull("_telefoneCelular", java.sql.Types.NULL);
            }
            if (cliente.getTelefoneFax() != null) {
                cstm.setString("_telefoneFax", cliente.getTelefoneFax());
            } else {
                cstm.setNull("_telefoneFax", java.sql.Types.NULL);
            }
            cstm.setString("_email", cliente.getEmail());
            cstm.setString("_senha", senha);
            if (cliente.getObservacoes() != null) {
                cstm.setString("_observacoes", cliente.getObservacoes());
            } else {
                cstm.setNull("_observacoes", java.sql.Types.NULL);
            }
            cstm.setString("_autorizaNotificacoes", (cliente.getAutorizaNotificacoes()) ? "1" : "0");
            cstm.registerOutParameter("_codCliente", java.sql.Types.SMALLINT);
            if (cstm.executeUpdate() == 0) {
                throw new SQLException("falha na inserção de dados");
            }
            cliente.setCodCliente(cstm.getInt("_codCliente"));
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao inserir cliente: " + e.getMessage());
        } finally {
            try {
                if (cstm != null) {
                    cstm.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
    }

    public void atualizarCliente(Cliente cliente, String senha) throws PersistenceException {
        Connection conn = null;
        StatementManager stmm = null;
        CallableStatement cstm = null;
        try {
            conn = ConnectionManager.getInstance().getConnection();
            stmm = StatementManager.getInstance();
            if (cliente.getTipoPessoa() == 'F') {
                cstm = (CallableStatement) conn.prepareCall(stmm.getStatement("PROCEDURE_ATUALIZAR_CLIENTE_PF"));
                cstm.setString("_nome", cliente.getNome());
                if (cliente.getApelido() != null) {
                    cstm.setString("_apelido", cliente.getApelido());
                } else {
                    cstm.setNull("_apelido", java.sql.Types.NULL);
                }
                cstm.setString("_sexo", String.valueOf(cliente.getSexo()));
                cstm.setDate("_dataNascimento", new java.sql.Date(cliente.getDataNascimento().getTime()));
                if (cliente.getTelefoneResidencial() != null) {
                    cstm.setString("_telefoneResidencial", cliente.getTelefoneResidencial());
                } else {
                    cstm.setNull("_telefoneResidencial", java.sql.Types.NULL);
                }
            } else {
                cstm = (CallableStatement) conn.prepareCall(stmm.getStatement("PROCEDURE_ATUALIZAR_CLIENTE_PJ"));
                cstm.setString("_razaoSocial", cliente.getRazaoSocial());
                if (cliente.getNomeFantasia() != null) {
                    cstm.setString("_nomeFantasia", cliente.getNomeFantasia());
                } else {
                    cstm.setNull("_nomeFantasia", java.sql.Types.NULL);
                }
                cstm.setString("_nomeResponsavel", cliente.getNomeResponsavel());
                if (cliente.getTelefoneComercial() != null) {
                    cstm.setString("_telefoneComercial", cliente.getTelefoneComercial());
                } else {
                    cstm.setNull("_telefoneComercial", java.sql.Types.NULL);
                }
            }
            cstm.setInt("_codCliente", cliente.getCodCliente());
            cstm.setString("_cep", cliente.getCep());
            cstm.setString("_endereco", cliente.getEndereco());
            cstm.setString("_bairro", cliente.getBairro());
            cstm.setInt("_codCidade", cliente.getCidade().getCodCidade());
            if (cliente.getTelefoneCelular() != null) {
                cstm.setString("_telefoneCelular", cliente.getTelefoneCelular());
            } else {
                cstm.setNull("_telefoneCelular", java.sql.Types.NULL);
            }
            if (cliente.getTelefoneFax() != null) {
                cstm.setString("_telefoneFax", cliente.getTelefoneFax());
            } else {
                cstm.setNull("_telefoneFax", java.sql.Types.NULL);
            }
            cstm.setString("_email", cliente.getEmail());
            cstm.setString("_senha", senha);
            if (cliente.getObservacoes() != null) {
                cstm.setString("_observacoes", cliente.getObservacoes());
            } else {
                cstm.setNull("_observacoes", java.sql.Types.NULL);
            }
            cstm.setString("_autorizaNotificacoes", (cliente.getAutorizaNotificacoes()) ? "1" : "0");
            cstm.setString("_cadastroAtivo", (cliente.getCadastroAtivo()) ? "1" : "0");
            if (cstm.executeUpdate() == 0) {
                throw new SQLException("falha na atualização dos dados");
            }
        } catch (SQLException e) {
            throw new PersistenceException("Erro ao atualizar cliente: " + e.getMessage());
        } finally {
            try {
                if (cstm != null) {
                    cstm.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
            }
        }
    }
}
