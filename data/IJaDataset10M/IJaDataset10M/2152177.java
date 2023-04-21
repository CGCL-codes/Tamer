package br.org.acessobrasil.processoacessibilidade.vo;

import java.io.Serializable;
import java.net.URL;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import br.org.acessobrasil.padrao.PadraoNumerico;

@Entity
@Table(name = "sitio")
public class SitioPro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "urlReger", length = 255)
    private String urlRegex;

    @Column(name = "urlInicial", length = 255)
    private String urlInicial;

    @Column(name = "nomePasta", length = 255)
    private String nomePasta;

    public String getUrlInicial() {
        return urlInicial;
    }

    public void setUrlInicial(String urlInicial) {
        this.urlInicial = urlInicial;
    }

    public String getUrlRegex() {
        return urlRegex;
    }

    public void setUrlRegex(String urlRegex) {
        this.urlRegex = urlRegex;
    }

    public String getNomePasta() {
        if (nomePasta == null) this.setNomePasta(urlInicial);
        return nomePasta;
    }

    /**
	 * Pega a url inicial(no padrão http://www.sitio.xxx.xxx) e cria o nome apartir dela mais o id
	 * @param urlInicial
	 */
    public void setNomePasta(String urlInicial) {
        try {
            URL url = new URL(urlInicial);
            this.nomePasta = PadraoNumerico.completarZeros(getId(), 4) + "_" + url.getHost();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
