package br.ufmg.lcc.pcollecta.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "TB_PING_ATUALIZACAO_PARAMETROS")
public class AtualizacaoParametros extends PCollectaOrderedDTO {

    private static final long serialVersionUID = -4158306600478027885L;

    /**
	 * Nome do par�metro que pode ser utilizado no script
	 */
    private String nomeParametro;

    /**
	 * Script para o mapeamento
	 */
    private String scriptMapeamento;

    /**
	 * Momento de execu��o Antes ou Depois
	 */
    private String momentoExecucao;

    @Id
    @Column(name = "CO_ATUALIZACAO_PARAMETROS")
    @SequenceGenerator(name = "SEQUENCE", sequenceName = "SE_PING_ATUALIZACAO_PARAMETROS")
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQUENCE")
    @Override
    public Long getId() {
        return super.getId();
    }

    @Column(name = "DS_NOME_PARAMETRO")
    public String getNomeParametro() {
        return nomeParametro;
    }

    public void setNomeParametro(String nomeParametro) {
        this.nomeParametro = nomeParametro;
    }

    @Column(name = "DS_SCRIPT_MAPEAMENTO")
    public String getScriptMapeamento() {
        return scriptMapeamento;
    }

    public void setScriptMapeamento(String scriptMapeamento) {
        this.scriptMapeamento = scriptMapeamento;
    }

    @Column(name = "TP_MOMENTO_EXEC")
    public String getMomentoExecucao() {
        return momentoExecucao;
    }

    public void setMomentoExecucao(String momentoExecucao) {
        this.momentoExecucao = momentoExecucao;
    }

    @Override
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CO_EXTRACAO")
    public ETC getMaster() {
        return (ETC) super.getMaster();
    }
}
