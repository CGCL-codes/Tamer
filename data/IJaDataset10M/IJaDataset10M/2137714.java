package org.enterprise.rhtutorial.entity.folhapagamento;

import java.util.Date;
import javax.persistence.TemporalType;
import javax.persistence.Temporal;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.jcompany.commons.PlcBaseEntity;

@MappedSuperclass
public class FolhaPagamento extends PlcBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_FOLHA_PAGAMENTO")
    @Column(name = "ID_FOLHA_PAGAMENTO", nullable = false, length = 5)
    private Long id;

    @Version
    @Column(name = "VERSION", nullable = false, length = 5)
    private int versao;

    @Column(name = "DATE_LAST_UPDATE", nullable = false, length = 11)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataUltAlteracao = new Date();

    @Column(name = "USER_LAST_UPDATE", nullable = false)
    private String usuarioUltAlteracao = "";

    @Column(name = "ANO_MES_ULTIMO_FECHAMENTO", nullable = false, length = 8)
    @Temporal(TemporalType.TIMESTAMP)
    private Date anoMesUltimoFechamento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getAnoMesUltimoFechamento() {
        return anoMesUltimoFechamento;
    }

    public void setAnoMesUltimoFechamento(Date anoMesUltimoFechamento) {
        this.anoMesUltimoFechamento = anoMesUltimoFechamento;
    }

    public int getVersao() {
        return versao;
    }

    public void setVersao(int versao) {
        this.versao = versao;
    }

    public Date getDataUltAlteracao() {
        return dataUltAlteracao;
    }

    public void setDataUltAlteracao(Date dataUltAlteracao) {
        this.dataUltAlteracao = dataUltAlteracao;
    }

    public String getUsuarioUltAlteracao() {
        return usuarioUltAlteracao;
    }

    public void setUsuarioUltAlteracao(String usuarioUltAlteracao) {
        this.usuarioUltAlteracao = usuarioUltAlteracao;
    }
}
