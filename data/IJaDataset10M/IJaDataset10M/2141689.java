package carga;

import java.util.Vector;

class HtmlToken {

    public String tag;

    public String contenido;

    public boolean tagCierra;

    private void parseTag() {
        int x = 0;
        char c;
        String tmp = tag;
        tag = "";
        while (x < tmp.length()) {
            c = tmp.charAt(x);
            if (Character.isDigit(c) || Character.isLetter(c)) tag += c; else break;
            x++;
        }
        contenido = tmp.substring(x).trim();
        tag = tag.toLowerCase();
    }

    public HtmlToken(String tag, boolean tagCierra) {
        this.tag = tag;
        this.contenido = "";
        this.tagCierra = tagCierra;
        parseTag();
    }

    public HtmlToken(String contenido) {
        this.tag = "";
        this.contenido = contenido.trim();
        this.tagCierra = false;
    }
}

interface HtmlParserListener {

    public void progreso(String estado, int progreso);
}

public class HtmlParser {

    private Vector tokens;

    int estado;

    String buffer;

    int indice;

    HtmlParserListener eventos;

    private void filtrarContenido(HtmlTag n) {
        for (int x = 0; x < n.getSubCont(); x++) {
            HtmlTag tmp = (HtmlTag) (n.getSubTag(x));
            filtrarContenido(tmp);
            if (tmp.contenido.equals("") && tmp.getSubCont() == 0) {
                n.remover(x);
                x--;
            }
        }
    }

    private void filtrarEtiquetas(HtmlTag nodo) {
        for (int x = 0; x < nodo.getSubCont(); x++) {
            HtmlTag tmp = (HtmlTag) (nodo.getSubTag(x));
            filtrarEtiquetas(tmp);
            if (tmp.etiqueta.equals("font") || tmp.etiqueta.equals("a") || tmp.etiqueta.equals("b")) {
                nodo.contenido += tmp.contenido;
                for (int y = 0; y < tmp.getSubCont(); y++) {
                    nodo.insertar(tmp.getSubTag(y));
                }
                nodo.remover(x);
                x--;
            }
        }
    }

    private void leerRecursivo(String tag, HtmlTag info) {
        while (indice < tokens.size()) {
            if (eventos != null) eventos.progreso("An�lisis sint�ctico...", indice * 100 / tokens.size());
            HtmlToken actual = (HtmlToken) tokens.get(indice);
            if (!actual.tag.equals("")) {
                if (actual.tagCierra) {
                    if (actual.tag.equals(tag)) {
                        return;
                    } else {
                        indice--;
                        return;
                    }
                } else {
                    if (actual.tag.equals("br") || actual.tag.equals("img") || actual.tag.equals("hr") || actual.tag.equals("input")) {
                    } else {
                        HtmlTag tmp = new HtmlTag();
                        tmp.etiqueta = actual.tag;
                        tmp.parametros = actual.contenido;
                        info.insertar(tmp);
                        indice++;
                        leerRecursivo(actual.tag, tmp);
                    }
                }
            } else {
                info.contenido += actual.contenido;
            }
            indice++;
        }
    }

    public void setHtmlParserListener(HtmlParserListener e) {
        eventos = e;
    }

    private int estado0(char c) {
        if (c == '\n' || c == '\t' || c == ' ') return 0; else if (c == '<') {
            return 1;
        } else {
            buffer += c;
            return 4;
        }
    }

    private int estado1(char c) {
        if (c == '/') return 3; else {
            buffer += c;
            return 2;
        }
    }

    private int estado2(char c) {
        if (c == '>') {
            tokens.add(new HtmlToken(buffer, false));
            buffer = "";
            return 0;
        } else if (c == ' ' || c == '\t' || c == '\n') {
            buffer += ' ';
            return 5;
        } else buffer += c;
        return 2;
    }

    private int estado3(char c) {
        buffer += c;
        return 6;
    }

    private int estado4(char c) {
        if (c == '<') {
            tokens.add(new HtmlToken(buffer));
            buffer = "";
            return 1;
        } else {
            buffer += c;
            return 4;
        }
    }

    private int estado5(char c) {
        if (c == '>') {
            tokens.add(new HtmlToken(buffer, false));
            buffer = "";
            return 0;
        } else {
            buffer += c;
            return 5;
        }
    }

    private int estado6(char c) {
        if (c == '>') {
            tokens.add(new HtmlToken(buffer, true));
            buffer = "";
            return 0;
        } else if (c == ' ' || c == '\n' || c == '\t') {
            buffer += ' ';
            return 7;
        } else {
            buffer += c;
            return 6;
        }
    }

    private int estado7(char c) {
        if (c == '>') {
            tokens.add(new HtmlToken(buffer, true));
            buffer = "";
            return 0;
        } else {
            buffer += c;
            return 7;
        }
    }

    public HtmlParser() {
        eventos = null;
    }

    public HtmlTag parse(String s) {
        tokens = new Vector(10, 10);
        estado = 0;
        buffer = "";
        for (indice = 0; indice < s.length(); indice++) {
            switch(estado) {
                case 0:
                    estado = estado0(s.charAt(indice));
                    break;
                case 1:
                    estado = estado1(s.charAt(indice));
                    break;
                case 2:
                    estado = estado2(s.charAt(indice));
                    break;
                case 3:
                    estado = estado3(s.charAt(indice));
                    break;
                case 4:
                    estado = estado4(s.charAt(indice));
                    break;
                case 5:
                    estado = estado5(s.charAt(indice));
                    break;
                case 6:
                    estado = estado6(s.charAt(indice));
                    break;
                case 7:
                    estado = estado7(s.charAt(indice));
                    break;
            }
            if (eventos != null) eventos.progreso("An�lisis l�xico", (indice + 1) * 100 / s.length());
        }
        tokens.add(new HtmlToken("fin", true));
        indice = 0;
        HtmlTag n = new HtmlTag();
        leerRecursivo("fin", n);
        if (eventos != null) eventos.progreso("An�lisis Global", 90);
        filtrarEtiquetas(n);
        filtrarContenido(n);
        if (eventos != null) eventos.progreso("An�lisis Global", 100);
        return n;
    }
}
