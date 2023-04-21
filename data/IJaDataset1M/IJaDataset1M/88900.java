package tirateima.gui.variaveis;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Writer;

/**
 * Representa uma variável do tipo string.
 * 
 * @author felipe.lessa
 *
 */
@SuppressWarnings("serial")
public class VarString extends VarLinha {

    private String valor = "";

    /**
	 * Cria uma nova variável do tipo string. 
	 * @param nome  nome da variável.
	 */
    public VarString(String nome) {
        super(nome, "TAMANHO NORMAL DE STRING!!! =)");
    }

    /**
	 * Cria uma nova variável do tipo string. 
	 * @param nome   nome da variável.
	 * @param valor  valor inicial.
	 */
    public VarString(String nome, String valor) {
        this(nome);
        setValor(valor);
    }

    @Override
    public VarString criarCopia() {
        VarString ret = new VarString(nome);
        ret.valor = valor;
        ret.setTexto(valor);
        ret.lixo = lixo;
        ret.modificado = modificado;
        modificado = false;
        return ret;
    }

    @Override
    public String typeName() {
        return "string";
    }

    @Override
    public Color getCorTitulo() {
        return new Color(1.0f, 1.0f, 0.8f, 1.0f);
    }

    @Override
    public String getValor() {
        return valor;
    }

    @Override
    public void setValor(Object valor) {
        if (valor == null) lixo = true; else {
            lixo = false;
            this.valor = (String) valor;
            String v = new String(this.valor);
            v = v.replace("\\", "\\\\").replace("'", "\\'");
            v = v.replace("\n", "\\n").replace("\r", "\\r");
            v = v.replace("\t", "\\t");
            v = "'" + v + "'";
            this.setTexto(v);
        }
    }

    public int readData(BufferedReader buffer) throws IOException {
        try {
            String valor = null;
            String linha = buffer.readLine();
            if (linha == null) return -1;
            int tam = Integer.valueOf(linha);
            if (tam >= 0) {
                char temp[] = new char[tam];
                if (buffer.read(temp) < temp.length) throw new IOException("Não foi possível ler a string no arquivo!");
                if (buffer.readLine() == null) throw new IllegalArgumentException("Arquivo no formato incorreto!");
                valor = new String(temp);
            }
            setValor(valor);
            return 0;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Não foi possível ler valor no arquivo!");
        }
    }

    public void writeData(Writer buffer) throws IOException {
        String valor = lixo ? getValor() + "\n" : "";
        int tam = lixo ? getValor().length() : -1;
        buffer.write(Integer.toString(tam) + "\n");
        buffer.write(valor);
    }
}
