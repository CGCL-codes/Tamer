package com.br.fsm.projectdelivery.basica;

import java.util.Date;
import android.provider.BaseColumns;

/**
 * Endereco
 */
public class Endereco {

    private Integer enderecoId;

    private String enderecoLogradouro;

    private Integer enderecoNumero;

    private String enderecoComplemento;

    private String enderecoBairro;

    private String enderecoCidade;

    private String enderecoEstado;

    private String enderecoCEP;

    private Date enderecoUltimaAlteracao;

    public Endereco() {
    }

    public Integer getEnderecoId() {
        return enderecoId;
    }

    public void setEnderecoId(Integer enderecoId) {
        this.enderecoId = enderecoId;
    }

    public String getEnderecoLogradouro() {
        return enderecoLogradouro;
    }

    public void setEnderecoLogradouro(String enderecoLogradouro) {
        this.enderecoLogradouro = enderecoLogradouro;
    }

    public Integer getEnderecoNumero() {
        return enderecoNumero;
    }

    public void setEnderecoNumero(Integer enderecoNumero) {
        this.enderecoNumero = enderecoNumero;
    }

    public String getEnderecoComplemento() {
        return enderecoComplemento;
    }

    public void setEnderecoComplemento(String enderecoComplemento) {
        this.enderecoComplemento = enderecoComplemento;
    }

    public String getEnderecoBairro() {
        return enderecoBairro;
    }

    public void setEnderecoBairro(String enderecoBairro) {
        this.enderecoBairro = enderecoBairro;
    }

    public String getEnderecoCidade() {
        return enderecoCidade;
    }

    public void setEnderecoCidade(String enderecoCidade) {
        this.enderecoCidade = enderecoCidade;
    }

    public String getEnderecoEstado() {
        return enderecoEstado;
    }

    public void setEnderecoEstado(String enderecoEstado) {
        this.enderecoEstado = enderecoEstado;
    }

    public String getEnderecoCEP() {
        return enderecoCEP;
    }

    public void setEnderecoCEP(String enderecoCEP) {
        this.enderecoCEP = enderecoCEP;
    }

    public Date getEnderecoUltimaAlteracao() {
        return enderecoUltimaAlteracao;
    }

    public void setEnderecoUltimaAlteracao(Date enderecoUltimaAlteracao) {
        this.enderecoUltimaAlteracao = enderecoUltimaAlteracao;
    }

    private static String[] columns = new String[] { Enderecos.ID, Enderecos.LOGRADOURO, Enderecos.NUMERO, Enderecos.COMPLEMENTO, Enderecos.BAIRRO, Enderecos.CIDADE, Enderecos.ESTADO, Enderecos.CEP, Enderecos.ULTIMAALTERACAO };

    public static String[] getColumns() {
        return columns;
    }

    public static final class Enderecos implements BaseColumns {

        public static final String ID = "EDRC_ID";

        public static final String LOGRADOURO = "EDRC_DSLOGRADOURO";

        public static final String NUMERO = "EDRC_NUMERO";

        public static final String COMPLEMENTO = "EDRC_DSCOMPLEMENTO";

        public static final String BAIRRO = "EDRC_DSBAIRRO";

        public static final String CIDADE = "EDRC_DSCIDADE";

        public static final String ESTADO = "EDRC_DSESTADO";

        public static final String CEP = "EDRC_DSCEP";

        public static final String ULTIMAALTERACAO = "EDRC_TMULTIMAALTERACAO";
    }
}
