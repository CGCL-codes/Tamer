package minimundo;

/**
 *
 * @author Raphael
 */
public class Comercio {

    private double total;

    public void venda(String produto, double valor, int quant) {
    }

    /**
     * @param valor
     * @param quant
     */
    public void totalCompra(double valor, int quant) {
        total = valor * quant;
    }

    /**
     * método que retorna o valor total de uma compra
     * @return
     */
    public double getTotal() {
        return total;
    }
}
