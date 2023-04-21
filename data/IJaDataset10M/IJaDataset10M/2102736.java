package tirateima.gui.variaveis;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;

/**
 * Representa uma variável do tipo real.
 * 
 * @author felipe.lessa
 *
 */
@SuppressWarnings("serial")
public class VarReal extends VarLinha {

    private double valor = -0.1;

    /**
	 * Cria uma nova variável do tipo real. 
	 * @param nome  nome da variável.
	 */
    public VarReal(String nome) {
        super(nome, "000.000000");
    }

    /**
	 * Cria uma nova variável do tipo real. 
	 * @param nome   nome da variável.
	 * @param valor  valor inicial.
	 */
    public VarReal(String nome, double valor) {
        this(nome);
        setValor(valor);
    }

    @Override
    public VarReal criarCopia() {
        VarReal ret;
        if (lixo) ret = new VarReal(nome); else ret = new VarReal(nome, valor);
        ret.modificado = modificado;
        modificado = false;
        return ret;
    }

    @Override
    public String typeName() {
        return "real";
    }

    @Override
    public Color getCorTitulo() {
        return new Color(1.0f, 0.5f, 1.0f, 1.0f);
    }

    @Override
    public Double getValor() {
        return new Double(valor);
    }

    @Override
    public void setValor(Object valor) {
        if (valor == null) lixo = true; else {
            lixo = false;
            this.valor = (Double) valor;
            this.setTexto(String.valueOf(this.valor));
        }
    }

    public int readData(BufferedReader buffer) throws IOException {
        String linha = buffer.readLine();
        if (linha == null) return -1;
        try {
            setValor(Double.valueOf(linha));
        } catch (NumberFormatException e) {
            throw new IOException("O valor lido não representa " + "um real válido!");
        }
        return 0;
    }

    public void writeData(Writer buffer) throws IOException {
        String valor = lixo ? ((Double) getValor()).toString() : "";
        valor += "\n";
        buffer.write(valor);
    }
}
