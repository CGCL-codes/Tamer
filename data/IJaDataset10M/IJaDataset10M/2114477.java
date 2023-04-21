package org.neblipedia.wiki;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ProcesarAbstract {

    /**
	 * |
	 */
    public static final Pattern PATTERN_BARRA_VERTICAL = Pattern.compile("\\|");

    /**
	 * :
	 */
    public static final Pattern PATTERN_DOSPUNTOS = Pattern.compile(":");

    /**
	 * 
	 */
    public static final Pattern PATTERN_ESPACIO = Pattern.compile("\\s");

    /**
	 * =
	 */
    public static final Pattern PATTERN_IGUAL = Pattern.compile("=");

    /**
	 * _
	 */
    public static final Pattern patternGuionPiso = Pattern.compile("_");

    /**
	 * \n
	 */
    public static final Pattern patternSaltoLinea = Pattern.compile("\n");

    public static String escapar(String txt) {
        try {
            return Matcher.quoteReplacement(txt);
        } catch (NullPointerException e) {
            return "";
        }
    }

    public static String reemplazar(Pattern p, String text, String reemplazo) {
        return p.matcher(text).replaceAll(reemplazo);
    }

    public static void reemplazar(Pattern p, StringBuffer sb, String reemplazo) {
        reemplazar(p, sb, reemplazo, true);
    }

    public static void reemplazar(Pattern p, StringBuffer sb, String reemplazo, boolean escape) {
        Matcher m = p.matcher(sb.toString());
        if (m.find()) {
            sb.delete(0, sb.length());
            do {
                m.appendReplacement(sb, escape ? escapar(reemplazo) : reemplazo);
            } while (m.find());
            m.appendTail(sb);
        }
    }

    public static void reemplazar(Pattern p, StringBuffer sb, String reemplazo, int i) {
        int j = 0;
        boolean escape = true;
        Matcher m = p.matcher(sb.toString());
        if (m.find()) {
            sb.delete(0, sb.length());
            do {
                m.appendReplacement(sb, escape ? escapar(reemplazo) : reemplazo);
                j++;
            } while (m.find() && j < i);
            m.appendTail(sb);
        }
    }

    public static String[] split(Pattern pattern, String text) {
        return split(pattern, text, 0);
    }

    public static String[] split(Pattern pattern, String text, int c) {
        return pattern.split(text, c);
    }

    /**
	 * divide una cadena en 2 en la posicion especificada.
	 * 
	 * @param str
	 * @param index
	 * @return
	 */
    public static String[] split(String str, int index) {
        String[] arr = new String[2];
        arr[0] = str.substring(0, index);
        arr[1] = str.substring(index);
        return arr;
    }

    /**
	 * divide la cadena de texto usando "|" como patron
	 * 
	 * @param text
	 * @return
	 */
    public static String[] splitBarraVertical(String text) {
        return split(PATTERN_BARRA_VERTICAL, text);
    }

    /**
	 * divide la cadena de texto usando "|" como patron
	 * 
	 * @param text
	 * @param c
	 * @return
	 */
    public static String[] splitBarraVertical(String text, int c) {
        return split(PATTERN_BARRA_VERTICAL, text, c);
    }

    /**
	 * divide la cadena de texto usando "\n" como patron
	 * 
	 * @param text
	 * @return
	 */
    public static String[] splitSaltoLinea(String text) {
        return split(patternSaltoLinea, text);
    }

    /**
	 * delegado de articulo.getContenido()
	 */
    protected StringBuffer contenido;

    protected ProcesarAbstract() {
        contenido = new StringBuffer();
    }

    public ProcesarAbstract(ArticuloAbstract articulo) {
        this.contenido = articulo.contenido;
    }

    public abstract void procesar();

    /**
	 * retorna el contenido procesado del articulo, es igual a invocar el metodo
	 * toString() del articulo
	 */
    @Override
    public abstract String toString();
}
