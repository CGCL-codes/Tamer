package br.com.caelum.stella.nfe.modelo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TProtNFe", propOrder = { "infProt", "signature" })
public class TProtNFe {

    @XmlElement(required = true)
    protected InfProt infProt;

    @XmlElement(name = "Signature", namespace = "http://www.w3.org/2000/09/xmldsig#")
    protected SignatureType signature;

    @XmlAttribute(required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String versao;

    public InfProt getInfProt() {
        return infProt;
    }

    public void setInfProt(InfProt value) {
        this.infProt = value;
    }

    public SignatureType getSignature() {
        return signature;
    }

    public void setSignature(SignatureType value) {
        this.signature = value;
    }

    public String getVersao() {
        return versao;
    }

    public void setVersao(String value) {
        this.versao = value;
    }
}
