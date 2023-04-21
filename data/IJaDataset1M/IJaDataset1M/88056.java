package listaspesa.shared.dto;

import java.util.Map;
import listaspesa.server.model.IData;
import listaspesa.shared.identifiers.NegozioIdentifier;

public class NegozioDTO implements IData {

    private static final long serialVersionUID = 6522616794144837915L;

    private String id;

    private String nome;

    private IndirizzoDTO indirizzo;

    private Map<String, Double> prodotti;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public IndirizzoDTO getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(IndirizzoDTO indirizzo) {
        this.indirizzo = indirizzo;
    }

    public Map<String, Double> getProdotti() {
        return prodotti;
    }

    public void setProdotti(Map<String, Double> prodotti) {
        this.prodotti = prodotti;
    }

    @Override
    public String getTableType() {
        return NegozioIdentifier.token;
    }
}
