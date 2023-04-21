package carrancao.conexao;

import carrancao.entidades.Categoria;
import carrancao.entidades.Produto;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author lubni
 */
public class Conexao {

    private Connection con;

    public static Connection conectar() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/carrancao", "root", "");
        } catch (ClassNotFoundException cnfe) {
            JOptionPane.showMessageDialog(null, "Não foi possível encontrar o Driver!", "Driver", JOptionPane.ERROR_MESSAGE);
            cnfe.printStackTrace();
        } catch (SQLException sqle) {
            JOptionPane.showMessageDialog(null, "Não foi possível conectar ao banco de dados!", "Conexão", JOptionPane.ERROR_MESSAGE);
            sqle.printStackTrace();
        }
        return null;
    }

    public void desconectar() {
        try {
            con.close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Categoria categoria = new Categoria();
        categoria.setIdcategoria(4);
        Produto produto = new Produto();
        produto.setCategoria(categoria);
        produto.setIdproduto(45);
        produto.setDescricao("XYZ-BURGUER");
        produto.setObs("Para que?");
        produto.setValor(5.5f);
        produto.setStatus("ATIVO");
        atualizarProduto(produto);
    }

    public static void atualizarProduto(Produto p) {
        try {
            Connection conn = Conexao.conectar();
            final String sqlString = "UPDATE PRODUTO SET DESCRICAO=?, OBS=?, VALOR=?, STATUS=?, CATEGORIA_IDCATEGORIA=? WHERE IDPRODUTO=?";
            PreparedStatement pr = conn.prepareStatement(sqlString);
            pr.setString(1, p.getDescricao());
            pr.setString(2, p.getObs());
            pr.setDouble(3, p.getValor());
            pr.setString(4, p.getStatus());
            pr.setInt(5, p.getCategoria().getIdcategoria());
            pr.setInt(6, p.getIdproduto());
            int linhasAfetadas = pr.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("ATUALIZOU MEU CAMARADA!");
            } else {
                System.out.println("NÃO FEZ PN!");
            }
        } catch (SQLException sqlex) {
            System.out.println(sqlex.getMessage());
        }
    }

    public static void atualizarProdutoHasItem(Produto p) {
        try {
            Connection conn = Conexao.conectar();
            final String sqlString = "UPDATE PRODUTO_HAS_ITEM_PRODUTO SET DESCRICAO=?, OBS=?, VALOR=?, STATUS=?, CATEGORIA_IDCATEGORIA=? WHERE IDPRODUTO=?";
            PreparedStatement pr = conn.prepareStatement(sqlString);
            pr.setString(1, p.getDescricao());
            pr.setString(2, p.getObs());
            pr.setDouble(3, p.getValor());
            pr.setString(4, p.getStatus());
            pr.setInt(5, p.getCategoria().getIdcategoria());
            pr.setInt(6, p.getIdproduto());
            int linhasAfetadas = pr.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("ATUALIZOU MEU CAMARADA!");
            } else {
                System.out.println("NÃO FEZ PN!");
            }
        } catch (SQLException sqlex) {
            System.out.println(sqlex.getMessage());
        }
    }
}
