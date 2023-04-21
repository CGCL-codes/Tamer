package daisy;

import java.util.List;
import java.util.ArrayList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EstruturaXML extends EstruturaBase {

    private int ordem;

    private List<RegParteTexto> lstSentencas;

    public EstruturaXML() {
        setNomeArquivo(null);
        lstSentencas = new ArrayList<RegParteTexto>();
    }

    public List<RegParteTexto> getLstSentencas() {
        return lstSentencas;
    }

    public void setLstSentencas(List<RegParteTexto> l) {
        lstSentencas = l;
    }

    public int getNumSentencas() {
        return lstSentencas.size();
    }

    public RegParteTexto getSentenca(int p) {
        if (p < lstSentencas.size()) return lstSentencas.get(p);
        return null;
    }

    public RegParteTexto getSentenca(String id) {
        for (int i = 0; i < lstSentencas.size(); i++) if (lstSentencas.get(i).getId().equals(id)) return lstSentencas.get(i);
        return null;
    }

    public boolean estaCarregado(String nome) {
        return (nomeArquivo != null) && (nomeArquivo.equals(nome));
    }

    public boolean abrir(String nomeArq) {
        setNomeArquivo(null);
        if (!parseFile(nomeArq)) return false;
        if (!montaListaTexto()) return false;
        setNomeArquivo(nomeArq);
        return true;
    }

    public String extraiTexto(Node no) {
        String paragrafo;
        NodeList lisFilhos;
        if (no == null) return "";
        paragrafo = "";
        lisFilhos = no.getChildNodes();
        if (lisFilhos != null && lisFilhos.getLength() > 0) {
            Node noFilho;
            for (int i = 0; i < lisFilhos.getLength(); i++) {
                noFilho = lisFilhos.item(i);
                switch(noFilho.getNodeType()) {
                    case Node.TEXT_NODE:
                        paragrafo += noFilho.getNodeValue();
                        break;
                    case Node.ELEMENT_NODE:
                        paragrafo += extraiTexto(noFilho);
                        break;
                }
            }
        }
        return paragrafo;
    }

    private boolean trataSent(Node elem, int tipo, int nivel) {
        Node noFilho;
        String str, nomeNo;
        NodeList lstNodes;
        RegParteTexto novo;
        lstNodes = elem.getChildNodes();
        if (lstNodes != null && lstNodes.getLength() > 0) {
            for (int i = 0; i < lstNodes.getLength(); i++) {
                noFilho = lstNodes.item(i);
                if (noFilho.getNodeType() == Node.ELEMENT_NODE) {
                    nomeNo = noFilho.getNodeName();
                    if (nomeNo.equals("sent")) {
                        Element e = (Element) noFilho;
                        str = e.getAttribute("id");
                        novo = new RegParteTexto(tipo, str, nivel, ordem, extraiTexto(noFilho));
                        lstSentencas.add(novo);
                    }
                } else if (noFilho.getNodeType() == Node.TEXT_NODE) {
                    novo = new RegParteTexto(tipo, "", nivel, ordem, noFilho.getNodeValue());
                    lstSentencas.add(novo);
                }
            }
        }
        return true;
    }

    private boolean trataLevel(Node elem, int nivel) {
        Node noFilho;
        String nomeNo;
        NodeList lstNodes;
        lstNodes = elem.getChildNodes();
        if (lstNodes != null && lstNodes.getLength() > 0) {
            for (int i = 0; i < lstNodes.getLength(); i++) {
                noFilho = lstNodes.item(i);
                if (noFilho.getNodeType() == Node.ELEMENT_NODE) {
                    nomeNo = noFilho.getNodeName();
                    if (nomeNo.length() > 4 && nomeNo.substring(0, 5).equals("level")) {
                        trataLevel(noFilho, nivel + 1);
                    } else if (nivel > 1) {
                        String str;
                        str = "h" + (nivel - 1);
                        if (nomeNo.equals("p")) {
                            ordem++;
                            trataSent(noFilho, RegParteTexto.TXT_PARAG, nivel);
                        } else if (nomeNo.equals("hd") || nomeNo.equals(str)) {
                            ordem++;
                            trataSent(noFilho, RegParteTexto.TXT_CABEC, nivel);
                        } else {
                            System.out.println("Elemento " + nomeNo + " inesperado no nÃ­vel " + nivel);
                            trataLevel(noFilho, nivel);
                        }
                    } else {
                        if (nomeNo.equals("doctitle")) {
                            ordem++;
                            trataSent(noFilho, RegParteTexto.TXT_CABEC, nivel);
                        } else if (nomeNo.equals("convertitle") || nomeNo.equals("docauthor")) {
                            ordem++;
                            trataSent(noFilho, RegParteTexto.TXT_CABEC, nivel + 1);
                        } else System.out.println("Elemento " + nomeNo + " inesperado no nÃ­vel " + nivel);
                    }
                }
            }
        }
        return true;
    }

    private boolean montaListaTexto() {
        Element dtBook;
        NodeList lstMatter;
        boolean saida = false;
        ordem = 0;
        dtBook = dom.getDocumentElement();
        lstMatter = dtBook.getElementsByTagName("frontmatter");
        if (lstMatter != null && lstMatter.getLength() > 0) {
            saida = trataLevel((Element) lstMatter.item(0), 1);
        }
        lstMatter = dtBook.getElementsByTagName("bodymatter");
        if (lstMatter != null && lstMatter.getLength() > 0) {
            saida = trataLevel((Element) lstMatter.item(0), 1);
        }
        lstMatter = dtBook.getElementsByTagName("rearmatter");
        if (lstMatter != null && lstMatter.getLength() > 0) {
            saida = trataLevel((Element) lstMatter.item(0), 1);
        }
        return saida;
    }
}
