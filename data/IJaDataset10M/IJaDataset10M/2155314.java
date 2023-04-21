package br.com.caelum.stella.nfe.modelo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ICMS51 ", propOrder = { "orig", "cst", "modBC", "pRedBC", "vbc", "picms", "vicms" })
public class ICMS51 {

    @XmlElement(name = "orig", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String origem;

    @XmlElement(name = "CST", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String tipoTributacao;

    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlElement(name = "modBC")
    protected String modalidadeBaseCalculo;

    @XmlElement(name = "pRedBC")
    protected String percentualReducaoBaseCalculo;

    @XmlElement(name = "vBC")
    protected String valorDaBaseDeCalculo;

    @XmlElement(name = "pICMS")
    protected String aliquota;

    @XmlElement(name = "vICMS")
    protected String valor;

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(final String value) {
        origem = value;
    }

    public String getCST() {
        return tipoTributacao;
    }

    public void setCST(final String value) {
        tipoTributacao = value;
    }

    public String getModalidadeBaseCalculo() {
        return modalidadeBaseCalculo;
    }

    public void setModalidadeBaseCalculo(final String value) {
        modalidadeBaseCalculo = value;
    }

    public String getPercentualReducaoBaseCalculo() {
        return percentualReducaoBaseCalculo;
    }

    public void setPercentualReducaoBaseCalculo(final String value) {
        percentualReducaoBaseCalculo = value;
    }

    public String getVBC() {
        return valorDaBaseDeCalculo;
    }

    public void setVBC(final String value) {
        valorDaBaseDeCalculo = value;
    }

    public String getPICMS() {
        return aliquota;
    }

    public void setPICMS(final String value) {
        aliquota = value;
    }

    public String getVICMS() {
        return valor;
    }

    public void setVICMS(final String value) {
        valor = value;
    }
}
