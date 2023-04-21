package Modelo;

/**
 *
 * @author LOSILVA
 */
public class Produto {

    private int intIdProduto;

    private String strDescricao;

    private int intQtdMin;

    private int intQtdMax;

    private String strUnMedida;

    private double dblPrecoUnit;

    public void setIdProduto(int idProduto) {
        this.intIdProduto = idProduto;
    }

    public void setDescricao(String descricao) {
        this.strDescricao = descricao;
    }

    public void setQtdMin(int qtdMin) {
        this.intQtdMin = qtdMin;
    }

    public void setQtdMax(int QtdMax) {
        this.intQtdMax = QtdMax;
    }

    public void setUnMedida(String unMedida) {
        this.strUnMedida = unMedida;
    }

    public void setPrecoUnit(double precoUnit) {
        this.dblPrecoUnit = precoUnit;
    }

    public int getIdProduto() {
        return this.intIdProduto;
    }

    public String getDescricao() {
        return this.strDescricao;
    }

    public int getQtdMin() {
        return this.intQtdMin;
    }

    public int getQtdMax() {
        return this.intQtdMax;
    }

    public String getUnMedida() {
        return this.strUnMedida;
    }

    public double getPrecoUnit() {
        return this.dblPrecoUnit;
    }
}
