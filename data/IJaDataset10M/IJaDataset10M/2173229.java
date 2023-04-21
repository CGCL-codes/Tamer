package daisy;

import java.util.List;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EstruturaNCX extends EstruturaBase {

    private RegNavPoint navMap;

    public EstruturaNCX() {
        setNavMap(null);
        setNomeArquivo(null);
    }

    public void setNavMap(RegNavPoint navMap) {
        this.navMap = navMap;
    }

    public RegNavPoint getNavMap() {
        return navMap;
    }

    public boolean abrir(String nomeArq) {
        setNomeArquivo(nomeArq);
        if (!parseFile(nomeArq)) return false;
        montaNavMap();
        return true;
    }

    private void montaNavMap() {
        Element elem;
        elem = inicioEstrutura();
        if (elem != null) setNavMap(extraiNavPoint(elem));
    }

    public boolean extraiAtributos(Element elem, RegNavPoint novo) {
        Node noTexto;
        NodeList lisFilhos, lisTexto;
        Element elemNavLabel, elemContent;
        LivroDTB livro;
        livro = LivroDTB.instancia();
        novo.setId(elem.getAttribute("id"));
        novo.setPlayOrder(Integer.parseInt(elem.getAttribute("playOrder")));
        lisFilhos = elem.getElementsByTagName("navLabel");
        if (lisFilhos == null || lisFilhos.getLength() == 0) {
            System.err.println("Arquivo invÃ¡lido, navPoint sem navLabel!");
            return false;
        }
        elemNavLabel = (Element) lisFilhos.item(0);
        lisFilhos = elemNavLabel.getElementsByTagName("text");
        if (lisFilhos != null && lisFilhos.getLength() > 0) {
            lisTexto = lisFilhos.item(0).getChildNodes();
            if (lisTexto != null && lisTexto.getLength() > 0) {
                noTexto = (Node) lisTexto.item(0);
                if (noTexto.getNodeType() == Node.TEXT_NODE) novo.setTextoLabel(noTexto.getNodeValue());
            }
        }
        lisFilhos = elemNavLabel.getElementsByTagName("audio");
        if (lisFilhos != null && lisFilhos.getLength() > 0) {
            novo.setAudioLabel(extraiRegAudio((Element) lisFilhos.item(0)));
        }
        if (novo.getTextoLabel() == null && novo.getAudioLabel() == null) {
            System.err.println("Arquivo inválido, navLabel sem audio e sem texto!");
            return false;
        }
        lisFilhos = elem.getElementsByTagName("content");
        if (lisFilhos == null || lisFilhos.getLength() == 0) {
            System.err.println("Arquivo inválido, navPoint sem content!");
            return false;
        }
        elemContent = (Element) lisFilhos.item(0);
        novo.setContent(livro.getRegPAR(elemContent.getAttribute("src")));
        return true;
    }

    public RegNavPoint extraiNavPoint(Element elem) {
        RegNavPoint novo;
        NodeList lisFilhos;
        if (elem == null) return null;
        novo = new RegNavPoint();
        if (elem.getNodeName().equals("navPoint")) {
            if (!extraiAtributos(elem, novo)) return null;
        }
        lisFilhos = elem.getChildNodes();
        if (lisFilhos != null && lisFilhos.getLength() > 0) {
            Node noFilho;
            RegNavPoint filho;
            for (int i = 0; i < lisFilhos.getLength(); i++) {
                noFilho = lisFilhos.item(i);
                if (noFilho.getNodeType() == Node.ELEMENT_NODE && noFilho.getNodeName().equals("navPoint")) {
                    filho = extraiNavPoint((Element) noFilho);
                    if (filho != null) novo.addNavFilhos(filho);
                }
            }
        }
        return novo;
    }

    public Element inicioEstrutura() {
        Element ncxElem;
        NodeList lstNavMap;
        ncxElem = dom.getDocumentElement();
        lstNavMap = ncxElem.getElementsByTagName("navMap");
        if (lstNavMap == null || lstNavMap.getLength() <= 0) {
            System.err.println("Arquivo inválido, sem navMap!");
            return null;
        }
        return (Element) lstNavMap.item(0);
    }

    private static String montaNivelLista(RegNavPoint navPoint) {
        String lista, nome;
        List<RegNavPoint> lisNP;
        nome = navPoint.getTextoLabel();
        lista = (nome != null) ? nome : "";
        lisNP = navPoint.getNavFilhos();
        if (lisNP.size() > 0) {
            lista += "<ol>";
            for (int i = 0; i < lisNP.size(); i++) {
                lista += "<li>" + montaNivelLista(lisNP.get(i)) + "</li>";
            }
            lista += "</ol>";
        }
        return lista;
    }

    public static String montaListaHTML(RegNavPoint nav) {
        return (nav != null) ? montaNivelLista(nav) : "";
    }
}
