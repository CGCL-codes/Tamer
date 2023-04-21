package br.com.hs.nfe.common.to;

import br.com.hs.nfe.common.anotation.Element;
import br.com.hs.nfe.common.anotation.Node;
import br.com.hs.nfe.common.anotation.TypeEnum;
import br.com.hs.nfe.common.validation.Validable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe de detalhamento específico de medicamentos e de matérias-primas farmacêuticas.
 * @author Ranlive Hrysyk
 */
@Node(id = "K", name = "Medicamento")
public class MedicamentoTO extends BaseTO implements Validable {

    @Element(publicName = "Número do Lote", type = TypeEnum.CARACTER, min = 1, max = 20)
    private String nLote;

    @Element(publicName = "Qtde. Produtos no Lote", type = TypeEnum.DECIMAL, decimalCount = 3)
    private BigDecimal qLote;

    @Element(publicName = "Data de Fabricação", type = TypeEnum.DATE, formart = "yyyy-MM-dd")
    private Date dFab;

    @Element(publicName = "Data de Validade", type = TypeEnum.DATE, formart = "yyyy-MM-dd")
    private Date dVal;

    @Element(publicName = "Preço Máximo Consumidor", type = TypeEnum.DECIMAL, decimalCount = 2)
    private BigDecimal vPMC;

    /**
     * Número do Lote.
     */
    public String getNLote() {
        return nLote;
    }

    /**
     * Informar o número do lote de medicamentos ou de matérias-primas farmacêuticas.
     * @param nLote Número do Lote
     */
    public void setNLote(String nLote) {
        this.nLote = nLote;
    }

    /**
     * Quantidade de Produtos no Lote.
     */
    public BigDecimal getQLote() {
        return qLote;
    }

    /**
     * Informar a quantidade de produtos no lote de medicamentos ou de matérias-primas farmacêuticas.
     * @param qLote Quantidade de Produtos no Lote
     */
    public void setQLote(BigDecimal qLote) {
        this.qLote = qLote;
    }

    /**
     * Data de Fabricação.
     */
    public Date getDFab() {
        return dFab;
    }

    /**
     * Informar a data de fabricação.
     * <p><b>Formato: "AAAA-MM-DD"</b></p>
     * @param dFab Data de Fabricação
     */
    public void setDFab(Date dFab) {
        this.dFab = dFab;
    }

    /**
     * Data de Validade.
     */
    public Date getDVal() {
        return dVal;
    }

    /**
     * Informar a data de validade.
     * <p><b>Formato: "AAAA-MM-DD"</b></p>
     * @param dVal Data de Validade
     */
    public void setDVal(Date dVal) {
        this.dVal = dVal;
    }

    /**
     * Preço Máximo Consumidor.
     */
    public BigDecimal getVPMC() {
        return vPMC;
    }

    /**
     * Informar o preço máximo ao consumidor.
     * @param vPMC Preço Máximo Consumidor
     */
    public void setVPMC(BigDecimal vPMC) {
        this.vPMC = vPMC;
    }
}
