package dao;

import java.awt.Color;
import operating.Strings;
import model.Receita;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Responsavel;
import util.ConsoleX;
import util.Data;
import view.componentes.JComboBoxModelX;

/**
 *
 * @author aluno
 */
public class ReceitaDAO {

    /**
     * 
     * @param codigo
     * @return 
     */
    public Receita getReceita(String codigo) {
        Connection conexao = Conexao.getSqlConnection();
        Receita receita = new Receita();
        try {
            Statement statement = conexao.createStatement();
            statement.executeQuery("SELECT * FROM tabreceita WHERE rctCodigo = " + codigo + ";");
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                receita.setRctCodigo(resultSet.getInt("rctCodigo"));
                receita.setRctDescricao(resultSet.getString("rctDescricao"));
                receita.setRctValor(resultSet.getDouble("rctValor"));
                receita.setRctObservacao(resultSet.getString("rctObservacao"));
                receita.setRctAtivo(resultSet.getBoolean("rctAtivo"));
                receita.setRctSelecionado(resultSet.getBoolean("rctSelecionado"));
                receita.setRctTipo(resultSet.getString("rctTipo"));
                receita.setRctPeriodicidade(resultSet.getString("rctPeriodicidade"));
                receita.setRctPermanente(resultSet.getBoolean("rctPermanente"));
                receita.setRctDia(resultSet.getInt("rctDia"));
                Responsavel responsavel = new Responsavel();
                responsavel.setRspCadastro(new Data().converteDataMysqlBr(resultSet.getString("rctCadastro")));
                responsavel.setRspAlteracao(new Data().converteDataMysqlBr(resultSet.getString("rctAlteracao")));
                responsavel.setRspUsuario(resultSet.getString("rctUsuario"));
                responsavel.setRspResponsavel(resultSet.getString("rctCadastro"));
                receita.setRctResponsavel(responsavel);
            }
            statement.close();
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroRecuperacao("Receita", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
        }
        return receita;
    }

    /**
     * 
     * @param codigo
     * @return 
     */
    public Receita getAnterior(String codigo) {
        Connection conexao = Conexao.getSqlConnection();
        Receita receita = new Receita();
        int intCodigo = 0;
        try {
            Statement statement = conexao.createStatement();
            String sql = ("SELECT MAX(rctCodigo) AS codigo FROM tabreceita WHERE rctCodigo < " + codigo + ";");
            statement.executeQuery(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                intCodigo = resultSet.getInt("codigo");
            }
            resultSet.close();
            statement.close();
            if (intCodigo > 0) {
                receita = getReceita(String.valueOf(intCodigo));
            } else {
                receita.setRctCodigo(intCodigo);
            }
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroNovoCodigo("Receita", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
        }
        return receita;
    }

    /**
     * 
     * @param codigo
     * @return 
     */
    public Receita getProximo(String codigo) {
        Connection conexao = Conexao.getSqlConnection();
        Receita receita = new Receita();
        int intCodigo = 0;
        try {
            Statement statement = conexao.createStatement();
            String sql = ("SELECT MIN(rctCodigo) AS codigo FROM tabreceita WHERE rctCodigo > " + codigo + ";");
            statement.executeQuery(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                intCodigo = resultSet.getInt("codigo");
            }
            resultSet.close();
            statement.close();
            if (intCodigo > 0) {
                receita = getReceita(String.valueOf(intCodigo));
            } else {
                receita.setRctCodigo(intCodigo);
            }
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroNovoCodigo("Receita", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
        }
        return receita;
    }

    /**
     * 
     * @return 
     */
    public Receita getPrimeiro() {
        Connection conexao = Conexao.getSqlConnection();
        Receita receita = new Receita();
        int intCodigo = 0;
        try {
            Statement statement = conexao.createStatement();
            String sql = ("SELECT MIN(rctCodigo) AS codigo FROM tabreceita;");
            statement.executeQuery(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                intCodigo = resultSet.getInt("codigo");
            }
            resultSet.close();
            statement.close();
            if (intCodigo > 0) {
                receita = getReceita(String.valueOf(intCodigo));
            } else {
                receita.setRctCodigo(intCodigo);
            }
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroNovoCodigo("Receita", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
        }
        return receita;
    }

    /**
     * 
     * @return 
     */
    public Receita getUltimo() {
        Connection conexao = Conexao.getSqlConnection();
        Receita receita = new Receita();
        int intCodigo = 0;
        try {
            Statement statement = conexao.createStatement();
            String sql = ("SELECT MAX(rctCodigo) AS codigo FROM tabreceita;");
            statement.executeQuery(sql);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                intCodigo = resultSet.getInt("codigo");
            }
            resultSet.close();
            statement.close();
            if (intCodigo > 0) {
                receita = getReceita(String.valueOf(intCodigo));
            } else {
                receita.setRctCodigo(intCodigo);
            }
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroNovoCodigo("Receita", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
        }
        return receita;
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
            statement.executeQuery("SELECT MAX(rctCodigo) AS codigo FROM tabreceita;");
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                intCodigo = resultSet.getInt("codigo");
            }
            resultSet.close();
            statement.close();
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroNovoCodigo("Receita", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
        }
        return intCodigo;
    }

    /**
     * 
     * @param pReceita
     * @return
     */
    public boolean salvar(Receita pReceita) {
        Connection conexao = Conexao.getSqlConnection();
        try {
            Statement statement = conexao.createStatement();
            statement.execute("INSERT INTO tabreceita VALUES " + "(" + pReceita.getRctCodigo() + ", '" + pReceita.getRctDescricao() + "'" + ", " + pReceita.getRctValor() + ", '" + pReceita.getRctObservacao() + "'" + ", " + pReceita.isRctAtivo() + ", " + pReceita.isRctSelecionado() + ", '" + pReceita.getRctTipo() + "'" + ", '" + pReceita.getRctPeriodicidade() + "'" + ", " + pReceita.isRctPermanente() + ", " + pReceita.getRctDia() + ", '" + pReceita.getRctResponsavel().getRspCadastro() + "'" + ", '" + pReceita.getRctResponsavel().getRspAlteracao() + "'" + ", '" + pReceita.getRctResponsavel().getRspUsuario() + "'" + ", '" + pReceita.getRctResponsavel().getRspResponsavel() + "');");
            conexao.commit();
            statement.close();
            return true;
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroPersitencia("Receita", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * 
     * @param pReceita
     * @return
     */
    public boolean atualizar(Receita pReceita) {
        Connection conexao = Conexao.getSqlConnection();
        try {
            Statement statement = conexao.createStatement();
            statement.execute("UPDATE tabreceita SET " + "rctDescricao = '" + pReceita.getRctDescricao() + "'" + ", rctValor = " + pReceita.getRctValor() + ", rctObservacao = '" + pReceita.getRctObservacao() + "'" + ", rctAtivo = " + pReceita.isRctAtivo() + ", rctSelecionado = " + pReceita.isRctSelecionado() + ", rctTipo = '" + pReceita.getRctTipo() + "'" + ", rctPeriodicidade = '" + pReceita.getRctPeriodicidade() + "'" + ", rctPermanente = " + pReceita.isRctPermanente() + ", rctDia = " + pReceita.getRctDia() + ", rctAlteracao = '" + pReceita.getRctResponsavel().getRspAlteracao() + "'" + ", rctResponsavel = '" + pReceita.getRctResponsavel().getRspResponsavel() + "' " + "WHERE rctCodigo = " + pReceita.getRctCodigo() + ";");
            conexao.commit();
            statement.close();
            return true;
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroEdicao("Receita", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
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
            statement.execute("DELETE FROM tabreceita WHERE rctCodigo = " + codigo + ";");
            conexao.commit();
            statement.close();
            return true;
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroExclusao("Receita", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * 
     * @param jTable
     */
    public void getTabelaReceitas(JTable jTable) {
        Connection conexao = Conexao.getSqlConnection();
        DefaultTableModel modeloTabela;
        Vector<String> elementosDaTabela = new Vector<String>();
        elementosDaTabela.addElement("X");
        elementosDaTabela.addElement("Código");
        elementosDaTabela.addElement("Descrição");
        elementosDaTabela.addElement("Valor");
        elementosDaTabela.addElement("Saldo");
        elementosDaTabela.addElement("Observação");
        elementosDaTabela.addElement("Tipo");
        elementosDaTabela.addElement("Permanente");
        elementosDaTabela.addElement("Periodicidade");
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
            statement.executeQuery("SELECT * FROM tabreceita;");
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                elementosDaTabela = new Vector<String>();
                elementosDaTabela.addElement(Strings.selecionadoOf(resultSet.getBoolean("rctSelecionado")));
                elementosDaTabela.addElement(resultSet.getString("rctCodigo"));
                elementosDaTabela.addElement(resultSet.getString("rctDescricao"));
                elementosDaTabela.addElement(ConsoleX.valueOf(resultSet.getDouble("rctValor")));
                elementosDaTabela.addElement(ConsoleX.valueOf(getSaldoReceita(resultSet.getString("rctCodigo"))));
                elementosDaTabela.addElement(resultSet.getString("rctObservacao"));
                elementosDaTabela.addElement(resultSet.getString("rctTipo"));
                elementosDaTabela.addElement(Strings.getResolucao(resultSet.getBoolean("rctPermanente"), "Permanente", "Temporário"));
                elementosDaTabela.addElement(resultSet.getString("rctPeriodicidade"));
                elementosDaTabela.addElement(Strings.ativoOf(resultSet.getBoolean("rctAtivo")));
                elementosDaTabela.addElement(new Data().converteDataMysqlBr(resultSet.getString("rctCadastro")));
                elementosDaTabela.addElement(new Data().converteDataMysqlBr(resultSet.getString("rctAlteracao")));
                elementosDaTabela.addElement(resultSet.getString("rctUsuario"));
                elementosDaTabela.addElement(resultSet.getString("rctResponsavel"));
                modeloTabela.addRow(elementosDaTabela);
            }
            resultSet.close();
            statement.close();
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroRecuperacao("Receitas", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
        }
        DefaultTableCellRenderer crSelect = new DefaultTableCellRenderer();
        crSelect.setBackground(new Color(243, 243, 241));
        crSelect.setForeground(new Color(33, 140, 33));
        DefaultTableCellRenderer crRight = new DefaultTableCellRenderer();
        crRight.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
        jTable.setModel(modeloTabela);
        jTable.getColumnModel().getColumn(0).setCellRenderer(crSelect);
        jTable.getColumnModel().getColumn(3).setCellRenderer(crRight);
        jTable.getColumnModel().getColumn(4).setCellRenderer(crRight);
        jTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        jTable.getColumnModel().getColumn(1).setPreferredWidth(50);
        jTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        jTable.getColumnModel().getColumn(3).setPreferredWidth(50);
        jTable.getColumnModel().getColumn(4).setPreferredWidth(50);
        jTable.getColumnModel().getColumn(5).setPreferredWidth(100);
        jTable.getColumnModel().getColumn(6).setPreferredWidth(70);
        jTable.getColumnModel().getColumn(7).setPreferredWidth(80);
        jTable.getColumnModel().getColumn(8).setPreferredWidth(80);
        jTable.getColumnModel().getColumn(9).setPreferredWidth(50);
        jTable.getColumnModel().getColumn(10).setPreferredWidth(70);
        jTable.getColumnModel().getColumn(11).setPreferredWidth(70);
        jTable.getColumnModel().getColumn(12).setPreferredWidth(70);
        jTable.getColumnModel().getColumn(13).setPreferredWidth(80);
    }

    /**
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    public JComboBoxModelX getReceitaJCModel() {
        Connection conexao = Conexao.getSqlConnection();
        JComboBoxModelX receitaJCmodel = new JComboBoxModelX();
        receitaJCmodel.getCodigos().add(0);
        List<String> descricoes = new ArrayList<String>();
        descricoes.add("-");
        try {
            Statement statement = conexao.createStatement();
            statement.executeQuery("SELECT * FROM tabreceita ORDER BY rctDescricao;");
            ResultSet resultSetLocal = statement.getResultSet();
            while (resultSetLocal.next()) {
                receitaJCmodel.getCodigos().add(resultSetLocal.getInt("rctCodigo"));
                descricoes.add(resultSetLocal.getString("rctDescricao"));
            }
            resultSetLocal.close();
            statement.close();
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroRecuperacao("JComboBoxModel de Receita", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
        }
        String[] vetor = new String[descricoes.size()];
        for (int i = 0; i < descricoes.size(); i++) {
            vetor[i] = descricoes.get(i);
        }
        receitaJCmodel.setDescricoes(vetor);
        return receitaJCmodel;
    }

    /**
     * 
     * @param codigo
     * @return
     */
    public double getSaldoReceita(String codigo) {
        Connection conexao = Conexao.getSqlConnection();
        double saldo = 0;
        try {
            Statement statement = conexao.createStatement();
            statement.executeQuery("SELECT SUM(recValorRecebido) AS saldo FROM tabtitulosreceber " + "INNER JOIN tabreceita ON (recCodReceita = rctCodigo) " + "WHERE recCodReceita = " + codigo + ";");
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                saldo = resultSet.getDouble("saldo");
            }
            resultSet.close();
            statement.close();
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroNovoCodigo("Receita", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
        }
        return saldo;
    }

    /**
     * 
     * @param codigo
     * @return
     */
    public boolean selecionar(String codigo) {
        Connection conexao = Conexao.getSqlConnection();
        try {
            Receita receita = getReceita(codigo);
            boolean selecionado = !receita.isRctSelecionado();
            Statement statement = conexao.createStatement();
            statement.execute("UPDATE tabreceita SET " + "rctSelecionado = " + selecionado + " " + "WHERE rctCodigo = " + codigo + ";");
            conexao.commit();
            statement.close();
            return true;
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroEdicao("Receita", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
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
            statement.execute("UPDATE tabreceita SET " + "rctSelecionado = " + selecionado + ";");
            conexao.commit();
            statement.close();
            return true;
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(null, Strings.getMensagemErroEdicao("Receita", erro.getMessage()), Strings.tituloMessageErro, JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
