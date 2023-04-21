package dao;

import java.awt.Color;
import java.awt.Font;
import operating.Strings;
import model.TitulosReceber;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Responsavel;
import util.ConsoleX;
import util.Data;

/**
 *
 * @author aluno
 */
public class TitulosReceberDAO {

    /**
     * 
     * @param codigo
     * @return 
     */
    public TitulosReceber getTitulosReceber(String codigo) {
        Connection conexao = Conexao.getSqlConnection();
        TitulosReceber titulosReceber = new TitulosReceber();
        try {
            Statement statement = conexao.createStatement();
            String sql = "SELECT * FROM tabtitulosreceber " + "WHERE recCodigo = " + codigo + ";";
            statement.executeQuery(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                titulosReceber.setRecCodigo(resultSet.getInt("recCodigo"));
                titulosReceber.setRecDataEmissao(new Data().converteDataMysqlBr(resultSet.getString("recDataEmissao")));
                titulosReceber.setRecDataVencimento(new Data().converteDataMysqlBr(resultSet.getString("recDataVencimento")));
                titulosReceber.setRecDataRecebimento(new Data().converteDataMysqlBr(resultSet.getString("recDataRecebimento")));
                titulosReceber.setRecCliente((new ClienteDAO()).getCliente(resultSet.getString("recCodCliente")));
                titulosReceber.setRecConta((new ContaDAO()).getConta(resultSet.getString("recCodConta")));
                titulosReceber.setRecValorTitulo(resultSet.getDouble("recValorTitulo"));
                titulosReceber.setRecValorTotal(resultSet.getDouble("recValorTotal"));
                titulosReceber.setRecValorRecebido(resultSet.getDouble("recValorRecebido"));
                titulosReceber.setRecEncargos(resultSet.getDouble("recEncargos"));
                titulosReceber.setRecDesconto(resultSet.getDouble("recDesconto"));
                titulosReceber.setRecCodigoBarras(resultSet.getString("recCodigoBarras"));
                titulosReceber.setRecSelecionado(resultSet.getBoolean("recSelecionado"));
                titulosReceber.setRecRecebido(resultSet.getBoolean("recRecebido"));
                titulosReceber.setRecAtivo(resultSet.getBoolean("recAtivo"));
                titulosReceber.setRecTipo(resultSet.getString("recTipo"));
                titulosReceber.setRecObservacao(resultSet.getString("recObservacao"));
                Responsavel responsavel = new Responsavel();
                responsavel.setRspCadastro(new Data().converteDataMysqlBr(resultSet.getString("recCadastro")));
                responsavel.setRspAlteracao(new Data().converteDataMysqlBr(resultSet.getString("recAlteracao")));
                responsavel.setRspUsuario(resultSet.getString("recUsuario"));
                responsavel.setRspResponsavel(resultSet.getString("recResponsavel"));
                titulosReceber.setRecResponsavel(responsavel);
            }
            statement.close();
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroRecuperacao("Titulos à Receber", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
        }
        return titulosReceber;
    }

    /**
     * 
     * @param codigo
     * @return 
     */
    public TitulosReceber getAnterior(String codigo) {
        Connection conexao = Conexao.getSqlConnection();
        TitulosReceber titulosReceber = new TitulosReceber();
        int intCodigo = 0;
        try {
            Statement statement = conexao.createStatement();
            String sql = "SELECT MAX(recCodigo) AS codigo FROM tabtitulosreceber WHERE recCodigo < " + codigo + ";";
            statement.executeQuery(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                intCodigo = resultSet.getInt("codigo");
            }
            resultSet.close();
            statement.close();
            if (intCodigo > 0) {
                titulosReceber = getTitulosReceber(String.valueOf(intCodigo));
            } else {
                titulosReceber.setRecCodigo(intCodigo);
            }
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroNovoCodigo("Titulos à Receber", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
        }
        return titulosReceber;
    }

    /**
     * 
     * @param codigo
     * @return 
     */
    public TitulosReceber getProximo(String codigo) {
        Connection conexao = Conexao.getSqlConnection();
        TitulosReceber titulosReceber = new TitulosReceber();
        int intCodigo = 0;
        try {
            Statement statement = conexao.createStatement();
            String sql = "SELECT MIN(recCodigo) AS codigo FROM tabtitulosreceber WHERE recCodigo > " + codigo + ";";
            statement.executeQuery(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                intCodigo = resultSet.getInt("codigo");
            }
            resultSet.close();
            statement.close();
            if (intCodigo > 0) {
                titulosReceber = getTitulosReceber(String.valueOf(intCodigo));
            } else {
                titulosReceber.setRecCodigo(intCodigo);
            }
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroNovoCodigo("Titulos à Receber", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
        }
        return titulosReceber;
    }

    /**
     * 
     * @return 
     */
    public TitulosReceber getPrimeiro() {
        Connection conexao = Conexao.getSqlConnection();
        TitulosReceber titulosreceber = new TitulosReceber();
        int intCodigo = 0;
        try {
            Statement statement = conexao.createStatement();
            String sql = ("SELECT MIN(recCodigo) AS codigo FROM tabtitulosreceber;");
            statement.executeQuery(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                intCodigo = resultSet.getInt("codigo");
            }
            resultSet.close();
            statement.close();
            if (intCodigo > 0) {
                titulosreceber = getTitulosReceber(String.valueOf(intCodigo));
            } else {
                titulosreceber.setRecCodigo(intCodigo);
            }
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroNovoCodigo("TitulosReceber", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
        }
        return titulosreceber;
    }

    /**
     * 
     * @return 
     */
    public TitulosReceber getUltimo() {
        Connection conexao = Conexao.getSqlConnection();
        TitulosReceber titulosreceber = new TitulosReceber();
        int intCodigo = 0;
        try {
            Statement statement = conexao.createStatement();
            String sql = ("SELECT MAX(recCodigo) AS codigo FROM tabtitulosreceber;");
            statement.executeQuery(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                intCodigo = resultSet.getInt("codigo");
            }
            resultSet.close();
            statement.close();
            if (intCodigo > 0) {
                titulosreceber = getTitulosReceber(String.valueOf(intCodigo));
            } else {
                titulosreceber.setRecCodigo(intCodigo);
            }
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroNovoCodigo("TitulosReceber", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
        }
        return titulosreceber;
    }

    /**
     * 
     * @return
     */
    public int getMaiorCodigo() {
        Connection conexao = Conexao.getSqlConnection();
        int intCodigo = 0;
        try {
            Statement statement = conexao.createStatement();
            statement.executeQuery("SELECT MAX(recCodigo) AS codigo FROM tabtitulosreceber;");
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                intCodigo = resultSet.getInt("codigo");
            }
            resultSet.close();
            statement.close();
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroNovoCodigo("Titulos à Receber", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
        }
        return intCodigo;
    }

    /**
     * 
     * @param titulosReceber
     * @return
     */
    public boolean salvar(TitulosReceber titulosReceber) {
        Connection conexao = Conexao.getSqlConnection();
        try {
            Statement statement = conexao.createStatement();
            String campos = ("recCodigo, recDataEmissao, recDataVencimento, " + "recCodCliente, recCodConta, recValorTitulo, recValorTotal, " + "recValorRecebido, recEncargos, recDesconto, recCodigoBarras, " + "recSelecionado, recRecebido, recAtivo, recTipo, recObservacao, " + "recCadastro, recAlteracao, recUsuario, recResponsavel");
            String sql = ("INSERT INTO tabtitulosreceber (" + campos + ") VALUES " + "(" + titulosReceber.getRecCodigo() + ", '" + titulosReceber.getRecDataEmissao() + "'" + ", '" + titulosReceber.getRecDataVencimento() + "'" + ", " + titulosReceber.getRecCliente().getCliCodigo() + ", " + titulosReceber.getRecConta().getConCodigo() + ", " + titulosReceber.getRecValorTitulo() + ", " + titulosReceber.getRecValorTotal() + ", " + titulosReceber.getRecValorRecebido() + ", " + titulosReceber.getRecEncargos() + ", " + titulosReceber.getRecDesconto() + ", '" + titulosReceber.getRecCodigoBarras() + "'" + ", " + titulosReceber.isRecSelecionado() + ", " + titulosReceber.isRecRecebido() + ", " + titulosReceber.isRecAtivo() + ", '" + titulosReceber.getRecTipo() + "'" + ", '" + titulosReceber.getRecObservacao() + "'" + ", '" + titulosReceber.getRecResponsavel().getRspCadastro() + "'" + ", '" + titulosReceber.getRecResponsavel().getRspAlteracao() + "'" + ", '" + titulosReceber.getRecResponsavel().getRspUsuario() + "'" + ", '" + titulosReceber.getRecResponsavel().getRspResponsavel() + "');");
            ConsoleX.escrever(sql);
            statement.execute(sql);
            conexao.commit();
            statement.close();
            return true;
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroPersitencia("Titulos à Receber", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * 
     * @param titulosReceber
     * @return
     */
    public boolean atualizar(TitulosReceber titulosReceber) {
        Connection conexao = Conexao.getSqlConnection();
        try {
            Statement statement = conexao.createStatement();
            String sql = ("UPDATE tabtitulosReceber SET " + "recDataEmissao = '" + titulosReceber.getRecDataEmissao() + "'" + ", recDataVencimento = '" + titulosReceber.getRecDataVencimento() + "'");
            if (titulosReceber.isRecRecebido()) {
                sql += (", recDataRecebimento = '" + titulosReceber.getRecDataRecebimento() + "'");
            } else {
                sql += (", recDataRecebimento = " + titulosReceber.getRecDataRecebimento());
            }
            sql += (", recCodCliente = " + titulosReceber.getRecCliente().getCliCodigo() + ", recCodConta = " + titulosReceber.getRecConta().getConCodigo() + ", recValorTitulo = " + titulosReceber.getRecValorTitulo() + ", recValorTotal = " + titulosReceber.getRecValorTotal() + ", recValorRecebido = " + titulosReceber.getRecValorRecebido() + ", recEncargos = " + titulosReceber.getRecEncargos() + ", recDesconto = '" + titulosReceber.getRecDesconto() + "'" + ", recCodigoBarras = '" + titulosReceber.getRecCodigoBarras() + "'" + ", recSelecionado = " + titulosReceber.isRecSelecionado() + ", recRecebido = " + titulosReceber.isRecRecebido() + ", recAtivo = " + titulosReceber.isRecAtivo() + ", recTipo = '" + titulosReceber.getRecTipo() + "'" + ", recObservacao = '" + titulosReceber.getRecObservacao() + "'" + ", recAlteracao = '" + titulosReceber.getRecResponsavel().getRspAlteracao() + "'" + ", recResponsavel = '" + titulosReceber.getRecResponsavel().getRspResponsavel() + "'" + " WHERE recCodigo = " + titulosReceber.getRecCodigo() + ";");
            statement.execute(sql);
            conexao.commit();
            statement.close();
            return true;
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroEdicao("Titulos à Receber", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * 
     * @param codigo
     * @return
     */
    public boolean excluir(String codigo) {
        Connection conexao = Conexao.getSqlConnection();
        try {
            Statement statement = conexao.createStatement();
            statement.execute("DELETE FROM tabtitulosreceber WHERE recCodigo = " + codigo + ";");
            conexao.commit();
            statement.close();
            return true;
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroExclusao("Titulos à Receber", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * 
     * @param jTable
     */
    public void getTabelaTitulosReceber(JTable jTable) {
        Connection conexao = Conexao.getSqlConnection();
        DefaultTableModel modeloTabela;
        Vector<String> elementosDaTabela = new Vector<String>();
        elementosDaTabela.addElement("X");
        elementosDaTabela.addElement("Código");
        elementosDaTabela.addElement("Status");
        elementosDaTabela.addElement("Emissão");
        elementosDaTabela.addElement("Vencimento");
        elementosDaTabela.addElement("Recebimento");
        elementosDaTabela.addElement("Receita");
        elementosDaTabela.addElement("Conta");
        elementosDaTabela.addElement("Valor");
        elementosDaTabela.addElement("Encargos");
        elementosDaTabela.addElement("Descontos");
        elementosDaTabela.addElement("Recebido");
        elementosDaTabela.addElement("Observação");
        elementosDaTabela.addElement("Ativo");
        elementosDaTabela.addElement("Cadastro");
        elementosDaTabela.addElement("Alteração");
        elementosDaTabela.addElement("Usuário");
        elementosDaTabela.addElement("Responsável");
        modeloTabela = new DefaultTableModel(elementosDaTabela, 0) {

            private static final long serialVersionUID = 1;

            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        try {
            Statement statement = conexao.createStatement();
            statement.executeQuery("SELECT * FROM tabtitulosreceber;");
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                elementosDaTabela = new Vector<String>();
                elementosDaTabela.addElement(Strings.selecionadoOf(resultSet.getBoolean("recSelecionado")));
                elementosDaTabela.addElement(resultSet.getString("recCodigo"));
                elementosDaTabela.addElement(Strings.getResolucao(resultSet.getBoolean("recRecebido"), "Recebido", "Em Aberto"));
                elementosDaTabela.addElement(new Data().converteDataMysqlBr(resultSet.getString("recDataEmissao")));
                elementosDaTabela.addElement(new Data().converteDataMysqlBr(resultSet.getString("recDataVencimento")));
                elementosDaTabela.addElement(new Data().converteDataMysqlBr(resultSet.getString("recDataRecebimento")));
                elementosDaTabela.addElement((new ReceitaDAO()).getReceita(resultSet.getString("recCodCliente")).getRctDescricao());
                elementosDaTabela.addElement((new ContaDAO()).getConta(resultSet.getString("recCodConta")).getConNome());
                elementosDaTabela.addElement(ConsoleX.valueOf(resultSet.getDouble("recValorTitulo")));
                elementosDaTabela.addElement(ConsoleX.valueOf(resultSet.getDouble("recEncargos")));
                elementosDaTabela.addElement(ConsoleX.valueOf(resultSet.getDouble("recDesconto")));
                elementosDaTabela.addElement(ConsoleX.valueOf(resultSet.getDouble("recValorRecebido")));
                elementosDaTabela.addElement(resultSet.getString("recObservacao"));
                elementosDaTabela.addElement(Strings.ativoOf(resultSet.getBoolean("recAtivo")));
                elementosDaTabela.addElement(new Data().converteDataMysqlBr(resultSet.getString("recCadastro")));
                elementosDaTabela.addElement(new Data().converteDataMysqlBr(resultSet.getString("recAlteracao")));
                elementosDaTabela.addElement(resultSet.getString("recUsuario"));
                elementosDaTabela.addElement(resultSet.getString("recResponsavel"));
                modeloTabela.addRow(elementosDaTabela);
            }
            resultSet.close();
            statement.close();
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroRecuperacao("TitulosReceberes", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
        }
        DefaultTableCellRenderer crSelect = new DefaultTableCellRenderer();
        crSelect.setBackground(new Color(243, 243, 241));
        crSelect.setForeground(new Color(33, 140, 33));
        DefaultTableCellRenderer crRight = new DefaultTableCellRenderer();
        crRight.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
        jTable.setModel(modeloTabela);
        jTable.getColumnModel().getColumn(0).setCellRenderer(crSelect);
        jTable.getColumnModel().getColumn(8).setCellRenderer(crRight);
        jTable.getColumnModel().getColumn(9).setCellRenderer(crRight);
        jTable.getColumnModel().getColumn(10).setCellRenderer(crRight);
        jTable.getColumnModel().getColumn(11).setCellRenderer(crRight);
        jTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        jTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        jTable.getColumnModel().getColumn(2).setPreferredWidth(60);
        jTable.getColumnModel().getColumn(3).setPreferredWidth(70);
        jTable.getColumnModel().getColumn(4).setPreferredWidth(70);
        jTable.getColumnModel().getColumn(5).setPreferredWidth(75);
        jTable.getColumnModel().getColumn(6).setPreferredWidth(150);
        jTable.getColumnModel().getColumn(7).setPreferredWidth(150);
        jTable.getColumnModel().getColumn(8).setPreferredWidth(60);
        jTable.getColumnModel().getColumn(9).setPreferredWidth(60);
        jTable.getColumnModel().getColumn(10).setPreferredWidth(60);
        jTable.getColumnModel().getColumn(11).setPreferredWidth(60);
        jTable.getColumnModel().getColumn(12).setPreferredWidth(100);
        jTable.getColumnModel().getColumn(13).setPreferredWidth(50);
        jTable.getColumnModel().getColumn(14).setPreferredWidth(70);
        jTable.getColumnModel().getColumn(15).setPreferredWidth(70);
        jTable.getColumnModel().getColumn(16).setPreferredWidth(70);
        jTable.getColumnModel().getColumn(17).setPreferredWidth(80);
    }

    /**
     * 
     * @param codigo
     * @return
     */
    public boolean selecionar(String codigo) {
        Connection conexao = Conexao.getSqlConnection();
        try {
            TitulosReceber titulosreceber = getTitulosReceber(codigo);
            boolean selecionado = !titulosreceber.isRecSelecionado();
            Statement statement = conexao.createStatement();
            statement.execute("UPDATE tabtitulosreceber SET " + "recSelecionado = " + selecionado + " " + "WHERE recCodigo = " + codigo + ";");
            conexao.commit();
            statement.close();
            return true;
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroEdicao("TitulosReceber", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * 
     * @param selecionado
     * @return
     */
    public boolean selecao(boolean selecionado) {
        Connection conexao = Conexao.getSqlConnection();
        try {
            Statement statement = conexao.createStatement();
            statement.execute("UPDATE tabtitulosreceber SET " + "recSelecionado = " + selecionado + ";");
            conexao.commit();
            statement.close();
            return true;
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroEdicao("TitulosReceber", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
