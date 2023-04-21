package org.larOzanam.arquitetura.relatorio;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import org.larOzanam.arquitetura.dao.Parametro;

/**
 *
 * @author celio@celiosilva.com
 */
public class Relatorio {

    private String nomeRelatorio;

    private Parametro parametros;

    private String nomeArquivoRelatorio;

    /** Creates a new instance of Relatorio */
    public Relatorio(String nomeRelatorio, String nomeArquivoRelatorio, Parametro parametros) {
        if (nomeRelatorio == null) throw new NullPointerException("O argumento nomeRelatorio passado como par�metro possui valor nulo");
        if (nomeArquivoRelatorio == null) throw new NullPointerException("O argumento nomeArquivoRelatorio passado como par�metro possui valor nulo");
        if (parametros == null) throw new NullPointerException("O argumento parametros passado como parametro possui valor nulo");
        this.nomeRelatorio = nomeRelatorio;
        this.nomeArquivoRelatorio = nomeArquivoRelatorio;
        this.parametros = parametros;
        this.parametros.put(GerenciadorRelatorio.NOME_RELATORIO, nomeRelatorio);
    }

    public Relatorio(String nomeRelatorio, String nomeArquivoRelatorio) {
        this(nomeRelatorio, nomeArquivoRelatorio, new Parametro());
    }

    public Parametro getParametros() {
        return parametros;
    }

    public void setParametros(Parametro parametros) {
        this.parametros.putAll(parametros);
    }

    public String getNomeArquivoRelatorio() {
        return nomeArquivoRelatorio;
    }

    public String getNomeRelatorio() {
        return nomeRelatorio;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Relatorio \n[+]\n");
        sb.append("   --[NomeRelatorio=" + this.nomeRelatorio + "]\n");
        sb.append("   --[NomeArquivoRelatorio=" + this.nomeArquivoRelatorio + "]\n");
        for (Map.Entry<String, Object> entry : parametros.entrySet()) {
            sb.append("   --[" + entry.getKey() + "=" + entry.getValue() + "]\n");
        }
        sb.append("[-]");
        return sb.toString();
    }
}
