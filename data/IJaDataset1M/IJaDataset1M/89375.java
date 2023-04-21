package managedBean;

import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import ejb.Alternativa;
import ejb.Pergunta;

public class LinhaChecklist {

    private Pergunta pergunta;

    private String resposta;

    private List<SelectItem> alternativas = new ArrayList<SelectItem>();

    public LinhaChecklist() {
        super();
    }

    public LinhaChecklist(Pergunta pergunta, String resposta) {
        super();
        this.pergunta = pergunta;
        this.resposta = resposta;
    }

    public Pergunta getPergunta() {
        return pergunta;
    }

    public void setPergunta(Pergunta pergunta) {
        this.pergunta = pergunta;
        List<Alternativa> listaAlternativa = new ArrayList<Alternativa>(pergunta.getAlternativas());
        for (Alternativa a : listaAlternativa) {
            SelectItem si = new SelectItem();
            si.setLabel(a.getNome());
            si.setValue(a.getIdAlternativa());
        }
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public List<SelectItem> getAlternativas() {
        return alternativas;
    }

    public void setAlternativas(List<SelectItem> alternativas) {
        this.alternativas = alternativas;
    }
}
