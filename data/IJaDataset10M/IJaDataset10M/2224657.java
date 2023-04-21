package mx.unam.fciencias.balpox.expression;

import mx.unam.fciencias.balpox.gui.core.style.enumeration.STYLE;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * <br><br>Fecha: Jun 13, 2006&nbsp;&nbsp;&nbsp;&nbsp;Hora:6:34:19 PM
 *
 * @author <a href="mailto:balpo@gmx.net?subject=mx.unam.fciencias.balpox.expression.Fecha">Rodrigo Poblanno Balp</a>
 */
public class Fecha extends ExpresionTemporal {

    protected Fecha() {
    }

    /**
	 *
	 */
    public Fecha(String fecha) {
        tokens = new Vector<String>(5);
        tokens.add(fecha.trim());
    }

    public Fecha(String fecha, int line, int col) {
        this(fecha);
        super.linea = line;
        super.columna = col;
    }

    public Fecha(String fecha, int line, int col, int caract) {
        this(fecha, line, col);
        this.caracter = caract;
    }

    /**
	 * Agrega una partícula a esta expresión temporal.
	 *
	 * @param token La partícula o enunciado a agregar a esta expresión temporal
	 */
    public void add(String token) {
        addSentence(token.trim());
    }

    /**
	 * Agrega un enunciado a esta expresión temporal. Los elementos de este enunciado
	 * deben ser separados en subpartículas.
	 *
	 * @param sentence La oración que conforma esta expresión temporal.
	 */
    public void addSentence(String sentence) {
        if (tokens == null) {
            tokens = new Vector<String>(5);
        }
        StringTokenizer st = new StringTokenizer(sentence, " ", false);
        while (st.hasMoreTokens()) {
            tokens.add(st.nextToken());
        }
    }

    /**
	 * Obtiene el tipo de expresión temporal que ésta representa.
	 *
	 * @return Uno de los cinco tipos posibles de expresiones temporales.
	 *
	 * @see mx.unam.fciencias.balpox.expression.ExpresionTemporal.Tipo
	 */
    public final Tipo getTipo() {
        return Tipo.FECHA;
    }

    /**
	 * Obtiene la representación de esta expresión temporal como cadena.
	 *
	 * @return Una cadena que debe representar la expresión temporal.
	 */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(tokens.get(0));
        sb.append("(");
        sb.append(getLinea());
        sb.append(", ");
        sb.append(getColumna());
        sb.append(")");
        sb.append("\t\n\t");
        for (int i = 1; i < tokens.size(); i++) {
            sb.append(tokens.elementAt(i));
            sb.append("/");
        }
        return sb.toString();
    }

    /**
	 * Obtiene el estilo asociado para este objeto.
	 *
	 * @return El estilo que deberá utilizar este objeto.
	 *
	 * @see mx.unam.fciencias.balpox.gui.core.style.Style
	 * @see mx.unam.fciencias.balpox.gui.core.style.Styler
	 */
    public final STYLE getStyle() {
        return STYLE.STYLE_FECHA;
    }
}
