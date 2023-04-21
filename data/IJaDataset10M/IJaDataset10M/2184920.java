package br.com.arsmachina.introducaotapestry.pages.ajax;

import java.util.ArrayList;
import java.util.List;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import br.com.arsmachina.introducaotapestry.Usuario;

/**
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class Autocomplete {

    @SuppressWarnings("unused")
    @Property
    private String banda;

    private List<String> palavras;

    @OnEvent(component = "banda", value = "providecompletions")
    public List<String> autocompletar(String string) {
        List<String> list = new ArrayList<String>();
        string = string.toLowerCase();
        for (String palavra : getPalavras()) {
            if (palavra.toLowerCase().indexOf(string.toLowerCase()) >= 0) {
                list.add(palavra);
            }
        }
        return list;
    }

    private List<String> getPalavras() {
        if (palavras == null) {
            palavras = new ArrayList<String>();
            palavras.add("Beatles");
            palavras.add("Gogol Bordello");
            palavras.add("Rolling Stones");
            palavras.add("U2");
            palavras.add("Nação Zumbi");
            palavras.add("Raul Seixas");
        }
        return palavras;
    }
}
