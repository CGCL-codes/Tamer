package html.form;

import html.basic.HTMLParameters;
import html.gadget.HTMLLayoutHorizontal;
import html.gadget.HTMLLayoutVertical;
import html.gadget.HTMLPanel;
import java.util.Enumeration;
import java.util.Vector;
import validaciones.Validar;

class HTMLFormAssist extends AbstractHTMLFormWithButtons {

    HTMLPanel m_panelFase;

    HTMLParameters m_parameters;

    String m_textoBoton;

    public HTMLFormAssist(HTMLPanel panelFase, String url, String textoBoton, HTMLParameters parameters) {
        super(url);
        m_panelFase = panelFase;
        m_textoBoton = textoBoton;
        m_parameters = parameters;
    }

    protected HTMLPanel buttons() {
        HTMLLayoutHorizontal botones;
        botones = new HTMLLayoutHorizontal();
        botones.add(new HTMLButtonSubmit(m_textoBoton));
        return new HTMLPanel(botones);
    }

    static HTMLPanel i_panelHidden(HTMLParameters parameters) {
        HTMLLayoutVertical layout;
        Enumeration claves, valores;
        claves = parameters.keys();
        valores = parameters.values();
        layout = new HTMLLayoutVertical();
        while (claves.hasMoreElements() == true) {
            String clave, valor;
            HTMLHidden hidden;
            clave = (String) claves.nextElement();
            valor = (String) valores.nextElement();
            hidden = new HTMLHidden(clave, valor);
            layout.add(hidden);
        }
        Validar.assertion(valores.hasMoreElements() == false, "valores.hasMoreElements() == false");
        return new HTMLPanel(layout);
    }

    static HTMLPanel i_panelConCamposHidden(HTMLPanel panel, HTMLParameters parameters) {
        HTMLLayoutVertical layout;
        HTMLPanel hiddens;
        hiddens = i_panelHidden(parameters);
        layout = new HTMLLayoutVertical();
        layout.add(panel);
        layout.add(hiddens);
        return new HTMLPanel(layout);
    }

    protected HTMLPanel panel() {
        return i_panelConCamposHidden(m_panelFase, m_parameters);
    }
}

public class HTMLAssitent {

    Vector m_fases;

    String m_title, m_urlFinal;

    public HTMLAssitent(String title, String urlFinal) {
        super();
        m_title = title;
        m_urlFinal = urlFinal;
        m_fases = new Vector();
    }

    public void add(HTMLPanel panel) {
        m_fases.add(panel);
    }

    public final String getAttribs() {
        return "";
    }

    public final String getElements() {
        return "";
    }

    static int i_obtenFase(HTMLParameters parameters) {
        String strFase;
        strFase = parameters.getParameterNullSiNotExist("fase");
        if (strFase == null) return 0; else return Integer.parseInt(strFase);
    }

    private static String i_textoFase(int indFase, int numFases) {
        String strFase, strNumFases;
        strFase = Integer.toString(indFase + 1);
        strNumFases = Integer.toString(numFases);
        return "( " + strFase + " de " + strNumFases + " )";
    }

    private static HTMLFormAssist i_formFase(Vector fases, String url, int faseActual, HTMLParameters parameters) {
        HTMLPanel panelFaseActual;
        String textoBoton;
        HTMLParameters camposConSiguienteFase;
        String strSiguinteFase;
        strSiguinteFase = Integer.toString(faseActual + 1);
        camposConSiguienteFase = new HTMLParameters(parameters);
        camposConSiguienteFase.setParameter("fase", strSiguinteFase);
        panelFaseActual = (HTMLPanel) fases.elementAt(faseActual);
        if (faseActual == fases.size() - 1) textoBoton = "Terminar"; else textoBoton = "Siguiente";
        return new HTMLFormAssist(panelFaseActual, url, textoBoton, camposConSiguienteFase);
    }

    private static String i_obtenUrl(int faseActual, int numFases, String urlFinal, HTMLParameters parameters) {
        if (faseActual == numFases - 1) return urlFinal; else {
            return parameters.getParameter("url");
        }
    }

    public HTMLDialog getDialog(HTMLParameters parameters) {
        HTMLFormAssist formAsistente;
        String title;
        String url;
        int faseActual, numFases;
        numFases = m_fases.size();
        faseActual = i_obtenFase(parameters);
        url = i_obtenUrl(faseActual, numFases, m_urlFinal, parameters);
        formAsistente = i_formFase(m_fases, url, faseActual, parameters);
        title = m_title + i_textoFase(faseActual, numFases);
        return new HTMLDialog(title, formAsistente);
    }
}
