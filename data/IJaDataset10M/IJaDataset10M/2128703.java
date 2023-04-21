package trabajos;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Encapsulación de un trabajo para el congreso
 * 
 * @author alvaro
 */
@SuppressWarnings("serial")
public class Trabajo implements Serializable {

    private String cabecera;

    private String resumen;

    private String categoria;

    private String subcategorias;

    private String fichero;

    public Trabajo() {
        cabecera = new String();
        resumen = new String();
        fichero = new String();
        categoria = new String();
        subcategorias = new String();
    }

    /**
	 * Constructor base del trabajo. Genera la instacia a partir de los datos de
	 * los campos en bruto.
	 * 
	 * @param cabecera
	 * @param resumen
	 * @param fichero
	 * @param categoria
	 * @param subcategorias
	 * @throws Exception
	 */
    public Trabajo(String cabecera, String resumen, String fichero, String categoria, String subcategorias) throws Exception {
        setCabecera(cabecera);
        setResumen(resumen);
        setFichero(fichero);
        setCategoria(categoria);
        this.subcategorias = subcategorias;
    }

    /**
	 * Permite crear la instacia de Trabajo a partir de cada uno de los
	 * conjuntos de datos que forman sus atributos, y no con estos directamente
	 */
    public Trabajo(String titulo, ArrayList<String> autores, ArrayList<String> afiliacion, ArrayList<String> direccion, ArrayList<String> correo, String resumen, String fichero, String categoria, ArrayList<String> subcategorias, ArrayList<String> DNI) throws Exception {
        setCabecera(titulo, autores, afiliacion, direccion, correo, DNI);
        setResumen(resumen);
        setFichero(fichero);
        setCategoria(categoria);
        setSubcategorias(subcategorias);
    }

    /**
	 * Devuelve la cabecera del trabajo
	 */
    public String getCabecera() {
        return cabecera;
    }

    /**
	 * Establece la cabecera del trabajo
	 * 
	 * @param cabecera
	 * @throws Exception
	 */
    public void setCabecera(String cabecera) throws Exception {
        setCabecera(getDataofToken(cabecera, 0, '/'), parsetoArrayList(getDataofToken(cabecera, 1, '/')), parsetoArrayList(getDataofToken(cabecera, 2, '/')), parsetoArrayList(getDataofToken(cabecera, 3, '/')), parsetoArrayList(getDataofToken(cabecera, 4, '/')), parsetoArrayList(getDataofToken(cabecera, 5, '/')));
    }

    /**
	 * Devuelve la categoría del trabajo
	 * 
	 * @return
	 */
    public String getCategoria() {
        return categoria;
    }

    /**
	 * Permite establecer la categoría del trabajo. En caso de error lanza una
	 * excepción con la causa del mismo
	 * 
	 * @param categoria
	 * @throws Exception
	 */
    public void setCategoria(String categoria) throws Exception {
        CategoriasTrabajos categoriatrabajos = new CategoriasTrabajos();
        if (categoriatrabajos.GetCategorias().contains(categoria)) this.categoria = categoria; else throw new Exception("Categoría desconocida");
    }

    /**
	 * Devuelve el resumen del trabajo
	 * 
	 * @return
	 */
    public String getResumen() {
        return resumen;
    }

    /**
	 * Permite establecer el contenido del trabajo
	 * 
	 * @param resumen
	 */
    public void setResumen(String resumen) {
        this.resumen = resumen;
    }

    /**
	 * Devuelve el contenido del trabajo
	 * 
	 * @return
	 */
    public String getFichero() {
        return fichero;
    }

    /**
	 * Permite establecer el contenido del trabajo
	 * 
	 * @param fichero
	 */
    public void setFichero(String fichero) {
        this.fichero = fichero;
    }

    /**
	 * Permite establecer la cabecera del trabajo a partir de los datos de los
	 * autores
	 * 
	 * @param titulo
	 *            Título del trabajo
	 * @param autores
	 *            Nombre de los autores
	 * @param afiliacion
	 *            Afiliación de los autores
	 * @param direccion
	 *            Dirección de los autores
	 * @param correo
	 *            E-mail de los autores
	 * @param DNI
	 *            DNI de los autores
	 * @throws Exception
	 */
    public void setCabecera(String titulo, ArrayList<String> autores, ArrayList<String> afiliacion, ArrayList<String> direccion, ArrayList<String> correo, ArrayList<String> DNI) throws Exception {
        if (aredifferent(autores) && aredifferent(correo) && aredifferent(DNI)) this.cabecera = new String(titulo) + "/" + encodetoString(autores) + "/" + encodetoString(afiliacion) + "/" + encodetoString(DNI) + "/" + encodetoString(direccion) + "/" + encodetoString(correo); else throw new Exception("Existen datos repetidos en los argumentos");
    }

    /**
	 * Establece las subcategorías del trabajo. En caso de no ser correctas
	 * lanza una excepción con la causa del error.
	 * 
	 * @param subcategorias
	 * @throws Exception
	 */
    public void setSubcategorias(ArrayList<String> subcategorias) throws Exception {
        boolean invalid = false;
        CategoriasTrabajos categoriatrabajos = new CategoriasTrabajos();
        if (aredifferent(subcategorias)) {
            for (int i = 0; i < subcategorias.size(); i++) {
                if (!categoriatrabajos.GetSubcategorias(categoria).contains(subcategorias.get(i))) {
                    invalid = true;
                    break;
                }
            }
            if (invalid) throw new Exception("Alguna subcategoría no se enuentra dentro del rango"); else this.subcategorias = encodetoString(subcategorias);
        } else throw new Exception("Subcategorías repetidas");
    }

    /**
	 * Retorna el título del trabajo
	 */
    public String getTitulo() {
        if (checkcabecera()) return getDataofToken(cabecera, 0, '/');
        return null;
    }

    /**
	 * Devuelve los nombres de los autores tras convertirlos a un ArrayList de
	 * String
	 * 
	 * @return
	 */
    public ArrayList<String> getAutores() {
        if (checkcabecera()) {
            return parsetoArrayList(getDataofToken(this.cabecera, 1, '/'));
        }
        return null;
    }

    /**
	 * Devuelve los DNIs de los autores tras convertirlos a un ArrayList de
	 * String
	 * 
	 * @return
	 */
    public ArrayList<String> getDNIs() {
        if (checkcabecera()) {
            return parsetoArrayList(getDataofToken(this.cabecera, 3, '/'));
        }
        return null;
    }

    /**
	 * Devuelve las subcategorías del trabajo tras convertirlas a un ArrayList
	 * de String.
	 * 
	 * @return
	 */
    public ArrayList<String> getSubcategorias() {
        return parsetoArrayList(subcategorias);
    }

    /**
	 * Convierte el trabajo a su representación como cadena de texto para
	 * permitir su escritura en disco. Sería incluso posible añadir encriptación
	 * o compresión modificando este punto y parsefromraw()
	 * 
	 * @return
	 */
    public String rawdata() {
        String ret = new String();
        ret = cabecera + (char) 3 + resumen + (char) 3 + fichero + (char) 3 + categoria + (char) 3 + subcategorias;
        return ret;
    }

    /**
	 * Comprueba la integridad del formato de la cabecera del trabajo para
	 * evitar corrupción en los datos
	 */
    private boolean checkcabecera() {
        int totalseparators = 0;
        for (int i = 0; i < cabecera.length(); i++) {
            if (cabecera.charAt(i) == '/') totalseparators++;
        }
        return totalseparators == 5;
    }

    private boolean aredifferent(ArrayList<String> arraylist) {
        boolean ret = true;
        ArrayList<String> aux = new ArrayList<String>();
        for (int i = 0; i < arraylist.size(); i++) {
            if (aux.contains(arraylist.get(i))) {
                ret = false;
                break;
            } else {
                aux.add(arraylist.get(i));
            }
        }
        return ret;
    }

    private ArrayList<String> parsetoArrayList(String string) {
        ArrayList<String> ret = new ArrayList<String>();
        int leftmargin = 0, rightmargin = 0, pos = 0;
        while (pos < string.length()) {
            if (string.charAt(pos) == ',') {
                rightmargin = pos;
                ret.add(string.substring(leftmargin, rightmargin));
                leftmargin = ++pos;
            } else pos++;
        }
        ret.add(string.substring(leftmargin, string.length()));
        return ret;
    }

    private String encodetoString(ArrayList<String> arraylist) {
        String ret = new String();
        for (int i = 0; i < arraylist.size(); i++) {
            if (i < arraylist.size() - 1) ret += arraylist.get(i) + ","; else ret += arraylist.get(i);
        }
        return ret;
    }

    public static String parseTitulodeRaw(String string) {
        return string.substring(0, string.indexOf('/') - 1);
    }

    public static Trabajo buildfromRaw(String string) {
        try {
            return new Trabajo(parseCabeceradeRaw(string), parseResumendeRaw(string), parseFicherodeRaw(string), parseCategoriadeRaw(string), parseSubcategoriasdeRaw(string));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String parseCabeceradeRaw(String string) {
        return getDataofToken(string, 0, (char) 3);
    }

    public static String parseResumendeRaw(String string) {
        return getDataofToken(string, 1, (char) 3);
    }

    public static String parseFicherodeRaw(String string) {
        return getDataofToken(string, 2, (char) 3);
    }

    public static String parseCategoriadeRaw(String string) {
        return getDataofToken(string, 3, (char) 3);
    }

    public static String parseSubcategoriasdeRaw(String string) {
        return getDataofToken(string, 4, (char) 3);
    }

    private static String getDataofToken(String string, int token, char target) {
        int currenttoken = 0;
        int leftmargin = 0;
        int rightmargin = 0;
        int i = 0;
        for (i = 0; currenttoken < token; i++) {
            if (string.charAt(i) == target) {
                currenttoken++;
            }
        }
        leftmargin = i;
        if (!string.substring(leftmargin).contains(Character.toString(target))) rightmargin = string.length(); else {
            currenttoken = 0;
            for (i = leftmargin; currenttoken < 1; i++) {
                if (string.charAt(i) == target) {
                    currenttoken++;
                }
            }
            rightmargin = i - 1;
        }
        return string.substring(leftmargin, rightmargin);
    }
}
