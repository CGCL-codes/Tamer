package objetoVisual;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;
import javax.swing.JLabel;
import objetoVisual.accionVisual.accionVisual;
import util.nomiconos;
import util.simuGrafico;
import util.utilLinea;
import util.utilTexto;
import visSim.listaInterfaces;
import visSim.listaRutas;

public class listaObjetos extends listaObjetosVisuales {

    /** Borra todas las conexiones que van a/desde una maquina dado su nombre y modifica las interfaces 
	    conectadas. */
    private void borraConexiones(String nombre) {
        int tam = size();
        for (int i = 0; i < tam; i++) {
            getConexiones(i).borraConexionesHacia(nombre);
            getInterfaces(i).modificaConectado(nombre);
        }
    }

    /** Elimina las conexiones del equipo con un origen y destino especificados
	 * @param nombreOrigen Equipo origen de la conexion
	 * @param nombreDestino Equipo destino de la conexion
	 */
    public void borraConexionesDesdeHacia(String nombreOrigen, String nombreDestino) {
        getConexiones(buscaEquipo(nombreOrigen)).borraConexionesHacia(nombreDestino);
    }

    /** Busqueda de un equipo por su nombre
	 * @param nombre Nombre del equipo que se pretende buscar 
	 * @return int con la posicion dentro de la lista. -1 si no se ha encontrado.
	 */
    public int buscaEquipo(String nombre) {
        int tam = size();
        for (int i = 0; i < tam; i++) if (getNombre(i).compareTo(nombre) == 0) return i;
        return -1;
    }

    /** Busqueda de un equipo por su nombre en una posicion distinta a pos
	 * @param nombre Nombre del equipo que se pretende buscar
	 * @param pos posicion donde no se debe buscar el equipo
	 * @return int con la posicion dentro de la lista. -1 si no se ha encontrado.
	 */
    public int buscaEquipo(String nombre, int pos) {
        int tam = size();
        for (int i = 0; i < tam; i++) if (i != pos && getNombre(i).compareTo(nombre) == 0) return i;
        return -1;
    }

    /** Cuando se va a pegar uno o varios equipos, esta funcion cambia las
	 * coordenadas de los que se van a pegar con relacion a las coordenadas
	 * del raton. Para ello lo que se hace es localizar el objeto mas cercano
	 * al origen de coordenadas y a partir de el desplazar el resto.
	 * @param x Coordenada x del raton
	 * @param y Coordenada y del raton
	 */
    public void cambiaCoord(int x, int y) {
        double distMin = Float.MAX_VALUE;
        double distancia;
        int i, indice;
        int xTras = 0;
        int yTras = 0;
        int tam = size();
        indice = 0;
        for (i = 0; i < tam; i++) {
            distancia = Math.sqrt(getX(i) * getX(i) + getY(i) * getY(i));
            if (distancia < distMin) {
                distMin = distancia;
                indice = i;
                xTras = getX(i);
                yTras = getY(i);
            }
        }
        for (i = 0; i < tam; i++) if (i == indice) ((objetoVisual) elementAt(i)).setCoord(x, y); else ((objetoVisual) elementAt(i)).setCoord(getX(i) - xTras + x, getY(i) - yTras + y);
    }

    /** Con esta funcion se cambian los nombres de aquellos objetos que van
	 * a ser copiados en una topologia.
	 * @param listaOriginal Lista donde se van a pegar los equipos
	 */
    public void cambiaNombres(listaObjetos listaOriginal) {
        String nombreNuevo, nombreViejo;
        int i, j, k, contador;
        int tam = size();
        if (listaOriginal.tam() == 0) return;
        Vector nuevos, viejos, tmpConexion;
        objetoVisual eq;
        Vector controlCambios = new Vector();
        Vector t1, t2;
        boolean encontrado;
        int ctd;
        nuevos = new Vector();
        viejos = new Vector();
        for (i = 0; i < tam; i++) {
            ctd = -1;
            encontrado = false;
            for (j = 0; j < controlCambios.size() && !encontrado; j++) {
                t1 = new Vector((Vector) controlCambios.elementAt(j));
                if (((String) t1.elementAt(0)).compareTo(getNomIcono(i)) == 0) {
                    encontrado = true;
                    ctd = ((Integer) (t1.elementAt(1))).intValue() + 1;
                    t2 = new Vector();
                    t2.add(getNomIcono(i));
                    t2.add(new Integer(ctd));
                    controlCambios.remove(j);
                    controlCambios.add(j, t2);
                }
            }
            if (ctd == -1) {
                ctd = 0;
                t2 = new Vector();
                t2.add(getNomIcono(i));
                t2.add(new Integer(ctd));
                controlCambios.add(t2);
            }
            contador = listaOriginal.devMaximo(getNomIcono(i)) + ctd;
            nombreNuevo = (new nomiconos()).getNombre(getNomIcono(i)) + contador;
            nombreViejo = getNombre(i);
            nuevos.add(nombreNuevo);
            viejos.add(nombreViejo);
            eq = (objetoVisual) getObjeto(i).clone();
            eq.setNombre(nombreNuevo);
            eq.setConexiones(getConexiones(i));
            eq.cambiaNombresConexiones(nombreViejo, nombreNuevo);
            setElementAt(eq, i);
        }
        for (i = 0; i < tam; i++) {
            tmpConexion = new Vector(getConexiones(i));
            for (j = 0; j < tmpConexion.size(); j++) for (k = 0; k < viejos.size(); k++) if (((String) tmpConexion.elementAt(j)).compareTo((String) viejos.elementAt(k)) == 0) tmpConexion.set(j, nuevos.elementAt(k));
            setConexiones(i, new Vector(tmpConexion));
        }
    }

    /** Cuando se cambia el nombre de cualquier equipo, este metodo se encarga de cambiar
	 * el nombre en las conexiones de la lista y los de la interfaz
	 * @param nombreViejo Nombre viejo del equipo
	 * @param nombreNuevo Nombre nuevo que tiene el equipo
	 */
    public void cambiaNombresConexiones(String nombreViejo, String nombreNuevo) {
        for (int i = 0; i < size(); i++) {
            getObjeto(i).cambiaNombresConexiones(nombreViejo, nombreNuevo);
            getObjeto(i).cambiaConexionesInterfaz(nombreViejo, nombreNuevo);
        }
    }

    /** Centrado de los equipos con respecto a los ejes vertical y horizontal
	 * @param altura del panel donde se encuentra la topologia
	 * @param anchura del panel donde se encuentra la topologia
	 */
    public void centrarAmbos(int altura, int anchura) {
        centrarVertical(altura, anchura);
        centrarHorizontal(altura, anchura);
    }

    /** Centrado de los equipos con respecto al eje horizontal
	 * @param altura del panel donde se encuentra la topologia
	 * @param anchura del panel donde se encuentra la topologia
	 */
    public void centrarHorizontal(int altura, int anchura) {
        int tam = size();
        int dif = anchura / 2 - (int) (new Point(getCM(altura, anchura))).getX();
        for (int i = 0; i < tam; i++) if (getSeleccionado(i)) setX(i, getX(i) + dif);
    }

    /** Centrado de los equipos con respecto al eje vertical
	 * @param altura del panel donde se encuentra la topologia
	 * @param anchura del panel donde se encuentra la topologia
	 */
    public void centrarVertical(int altura, int anchura) {
        int tam = size();
        int dif = altura / 2 - (int) (new Point(getCM(altura, anchura))).getY();
        for (int i = 0; i < tam; i++) if (getSeleccionado(i)) setY(i, getY(i) + dif);
    }

    /** Comprueba si dos equipos son compatibles para poder conectarlos
	 * @return boolean indicando la posibilidad de conectarlos
	 */
    public boolean compatiblesConexion() {
        int ind1 = getSeleccionadoConexion();
        int ind2 = -1;
        int tam = size();
        boolean encontrado = false;
        for (int i = ind1 + 1; i < tam && !encontrado; i++) if (getConecta(i)) {
            encontrado = true;
            ind2 = i;
        }
        if (encontrado) {
            String id1 = getID(ind1);
            String id2 = getID(ind2);
            return puedeConectarse(ind1, id2) && puedeConectarse(ind2, id1);
        }
        return false;
    }

    /** Funcion que comprueba posibles errores antes de la simulacion.
	 * @return Vector conteniendo los avisos al usuario
	 */
    public Vector compruebaSimulacion() {
        int i, j;
        Vector dev = new Vector();
        objetoVisual eqtemp;
        int tam = size();
        listaConexiones conexiones = new listaConexiones();
        listaInterfaces interfaces = new listaInterfaces();
        listaRutas rutas = new listaRutas();
        for (i = 0; i < tam; i++) {
            eqtemp = getObjeto(i);
            if (eqtemp instanceof pcVisual || eqtemp instanceof routerVisual) {
                conexiones.copia(eqtemp.getConexiones());
                interfaces = eqtemp.getInterfaces().copia();
                rutas = eqtemp.getRutas().copia();
                for (j = 0; j < conexiones.tam(); j++) if ((new Vector(interfaces.buscaConectados((String) conexiones.elementAt(j)))).size() == 0) dev.add(getNombre(i) + " - No existen interfaces para la conexion con " + (String) conexiones.elementAt(j));
                if (rutas.tam() > 0 && interfaces.tam() == 0 && conexiones.tam() == 0) dev.add(getNombre(i) + " - Existen entradas en la tabla de rutas que no corresponden a ninguna conexion ni interfaz.");
            } else if (eqtemp instanceof puenteVisual || eqtemp instanceof modemVisual || eqtemp instanceof wanVisual || eqtemp instanceof anilloVisual) dev.add(getNombre(i) + " - No se ha implementado la simulacion para este tipo de equipo");
        }
        return dev;
    }

    /** Funcion que devuelve una copia de la lista
	 * @return listaObjetos
	 */
    public listaObjetos copiaLista() {
        listaObjetos dev = new listaObjetos();
        dev.copialistaObjetos(this);
        return dev;
    }

    /** Constructor de copia de listaObjetos */
    public void copialistaObjetos(listaObjetos listatemp) {
        for (int i = 0; i < listatemp.tam(); i++) insertaObjeto((objetoVisual) listatemp.elementAt(i));
    }

    /** Metodo que hace una copia de los objetos seleccionados */
    public Vector copiaSeleccionados() {
        int tam = size();
        Vector dev = new Vector();
        for (int i = 0; i < tam; i++) if (getSeleccionado(i) && getNomIcono(i).compareTo(nomiconos.nomVacio) != 0) dev.addElement(getNombre(i));
        return dev;
    }

    /** Conecta dos equipos que han sido previamente conectados con una linea */
    public void creaConexion() {
        int i, j;
        boolean conectados = false;
        String nombreConexion;
        int tam = size();
        boolean sePuede = true;
        for (i = 0; i < tam && !conectados && sePuede; i++) if (getConecta(i)) for (j = i + 1; j < tam && !conectados && sePuede; j++) if (getConecta(j)) {
            if (getID(i).compareTo("mo") == 0) {
                if (getConexiones(i).tam() == 2 || (getID(j).compareTo("mo") == 0 && getConexiones(j).tam() == 2)) sePuede = false; else if (getConexiones(i).tam() == 1) {
                    nombreConexion = getConexiones(i).getNombre(0);
                    if (getID(buscaEquipo(nombreConexion)).compareTo("mo") == 0 && getID(j).compareTo("mo") == 0) sePuede = false;
                    if (sePuede && getID(buscaEquipo(nombreConexion)).compareTo("mo") != 0 && getID(j).compareTo("mo") != 0) sePuede = false;
                    if (sePuede && getConexiones(j).contains(nombreConexion)) sePuede = false;
                }
            }
            if (sePuede && getID(j).compareTo("mo") == 0) {
                if (getConexiones(j).tam() == 2 || (getID(i).compareTo("mo") == 0 && getConexiones(j).tam() == 2)) sePuede = false; else if (getConexiones(j).tam() == 1) {
                    nombreConexion = getConexiones(j).getNombre(0);
                    if (getID(buscaEquipo(nombreConexion)).compareTo("mo") == 0 && getID(i).compareTo("mo") == 0) sePuede = false;
                    if (sePuede && getID(buscaEquipo(nombreConexion)).compareTo("mo") != 0 && getID(i).compareTo("mo") != 0) sePuede = false;
                    if (sePuede && getConexiones(i).contains(nombreConexion)) sePuede = false;
                }
            }
            if (sePuede) {
                getObjeto(i).insertaConexion(getNombre(j));
                getObjeto(j).insertaConexion(getNombre(i));
                int pos1 = getObjeto(i).primeraInterfazLibre();
                int pos2 = getObjeto(j).primeraInterfazLibre();
                if (pos1 != -1) getObjeto(i).setInterfazConecta(pos1, getNombre(j));
                if (pos2 != -1 && !getCadenaSimulador(j).startsWith("Ethernet")) getObjeto(j).setInterfazConecta(pos2, getNombre(i));
            }
            setConecta(i, false);
            setConecta(j, false);
            conectados = true;
        }
    }

    /** Crea un vector de lineas para luego representar graficamente las conexiones */
    public Vector creaLineas() {
        int i, j, k;
        int tam = size();
        listaConexiones conexiones = new listaConexiones();
        Vector lineas = new Vector();
        for (i = 0; i < tam; i++) {
            conexiones.copia(getConexiones(i));
            for (j = i + 1; j < tam; j++) for (k = 0; k < conexiones.tam(); k++) if ((conexiones.getNombre(k)).compareTo(getNombre(j)) == 0) lineas.add(getutilLinea(i, j));
        }
        return new Vector(lineas);
    }

    /** Selecciona los objetos que caigan dentro del area del rectangulo */
    public void creaSelecciones(utilLinea rectangulo) {
        int x, y;
        int tam = size();
        int x1 = rectangulo.getX(2);
        int x2 = rectangulo.getX(1);
        if (x1 > x2) {
            x1 = x2;
            x2 = rectangulo.getX(2);
        }
        int y1 = rectangulo.getY(2);
        int y2 = rectangulo.getY(1);
        if (y1 > y2) {
            y1 = y2;
            y2 = rectangulo.getY(2);
        }
        for (int i = 0; i < tam; i++) {
            if ((getNombre(i).length() >= 5 && getNombre(i).substring(0, 5).compareTo("*tex*") != 0) || (getNombre(i).length() < 5)) {
                x = getX(i);
                y = getY(i);
                if (x >= x1 && y >= y1 && x <= x2 && y <= y2) setSeleccionado(i, true); else {
                    x = x + getWidth(i);
                    if (x >= x1 && y >= y1 && x <= x2 && y <= y2) setSeleccionado(i, true); else {
                        x = getX(i);
                        y = y + getHeight(i);
                        if (x >= x1 && y >= y1 && x <= x2 && y <= y2) setSeleccionado(i, true); else {
                            x = x + getWidth(i);
                            if (x >= x1 && y >= y1 && x <= x2 && y <= y2) setSeleccionado(i, true);
                        }
                    }
                }
            }
        }
    }

    /** Devuelve el indice maximo que ha sido utilizado en la lista de equipos
	 * de un tipo de eqipo para crear un equipo nuevo */
    public int devMaximo(String nomicono) {
        int tam = size();
        int ctd, i;
        boolean asignado, encontrado;
        String nombre;
        String cadena = (new nomiconos()).getNombre(nomicono);
        String nombreClase = "objetoVisual." + cadena.toLowerCase() + "Visual";
        ctd = 0;
        for (i = 0; i < tam; i++) if (elementAt(i).getClass().getName().compareTo(nombreClase) == 0) ctd++;
        ctd++;
        asignado = false;
        encontrado = false;
        nombre = cadena + ctd;
        while (!asignado) {
            encontrado = false;
            for (i = 0; i < tam && !encontrado; i++) if (getNombre(i).compareTo(nombre) == 0) encontrado = true;
            if (encontrado) {
                ctd++;
                nombre = cadena + ctd;
            } else asignado = true;
        }
        return ctd;
    }

    /** Funcion utilizada para eliminar las conexiones hacia equipos no existentes
	 * en la lista. Se utiliza al copiar varios equipos seleccionados
	 * @param nombres
	 */
    public void eliminaNoExistentes(Vector nombres) {
        int tam = size();
        for (int i = 0; i < tam; i++) getConexiones(i).eliminaNoExistentes(nombres);
    }

    /** Elimina los equipos seleccionados en la lista */
    public void eliminaSeleccionados() {
        int j;
        String nombre;
        for (int i = 0; i < size(); ) if (getSeleccionado(i)) {
            nombre = getNombre(i);
            borraConexiones(nombre);
            removeElementAt(i);
            for (j = 0; j < listaAcciones.size(); ) if (((accionVisual) listaAcciones.elementAt(j)).getEquipo().startsWith(nombre)) listaAcciones.removeElementAt(j); else j += 1;
        } else i++;
    }

    /** Devuelve la cadena con que el simulador identifica al objeto con el nombre recibido */
    public String getCadenaSimulador(String nombre) {
        return getObjeto(buscaEquipo(nombre)).getCadenaSimulador();
    }

    /** Devuelve el centro de masas del rectangulo formado por todos los equipos seleccionados */
    private Point getCM(int altura, int anchura) {
        Point dcha = new Point(0, 0);
        Point izq = new Point(anchura, 0);
        Point alto = new Point(0, altura);
        Point bajo = new Point(0, 0);
        int tam = size();
        int anch = 0, alt = 0;
        for (int i = 0; i < tam; i++) if (getSeleccionado(i)) {
            if (getX(i) < izq.getX()) izq.setLocation(getX(i), getY(i));
            if (getX(i) > dcha.getX()) {
                dcha.setLocation(getX(i), getY(i));
                anch = getWidth(i);
            }
            if (getY(i) < alto.getY()) alto.setLocation(getX(i), getY(i));
            if (getY(i) > bajo.getY()) {
                bajo.setLocation(getX(i), getY(i));
                alt = getHeight(i);
            }
        }
        return new Point((int) (izq.getX() + dcha.getX() + anch) / 2, (int) (alto.getY() + bajo.getY() + alt) / 2);
    }

    /** Devuelve un vector conteniendo todas las direcciones MAC de la topologia */
    public Vector getDirecsMac(listaInterfaces interfaces, int pos) {
        int j;
        int tam = size();
        Vector dev = new Vector();
        listaInterfaces tempInter;
        for (int i = 0; i < tam; i++) if (i != pos) {
            tempInter = getInterfaces(i);
            for (j = 0; j < tempInter.tam(); j++) dev.add(tempInter.getDirEnlace(j));
        }
        for (j = 0; j < interfaces.tam(); j++) dev.add(interfaces.getDirEnlace(j));
        return dev;
    }

    /** Metodo que devuelve un Vector con las IPs de un objeto y las coordenadas que le corresponden
	    en el panel de dibujo */
    public Vector getIPVisual(int pos) {
        listaInterfaces interfaces = getInterfaces(pos);
        utilLinea lineaConexion = new utilLinea();
        Vector dev = new Vector();
        int pos2, a, b, c, d, x, y, h;
        FontMetrics metrica = (new JLabel()).getFontMetrics(new Font("Dialog.plain", -1, 4));
        for (int i = 0; i < interfaces.tam(); i++) if (interfaces.getconecta(i).compareTo("N/A") != 0) {
            pos2 = buscaEquipo(interfaces.getconecta(i));
            if (pos2 != -1) {
                lineaConexion = getutilLinea(pos, pos2);
                a = lineaConexion.getX(1);
                b = lineaConexion.getY(1);
                c = lineaConexion.getX(2);
                d = lineaConexion.getY(2);
                h = (int) Math.sqrt((c - a) * (c - a) + (d - b) * (d - b));
                if (h == 0) h++;
                x = a + nomiconos.tam * (c - a) / h;
                y = b + nomiconos.tam * (d - b) / h;
                dev.add(new utilTexto(interfaces.getIP(i), x, y, metrica.stringWidth(interfaces.getIP(i)) + 30, metrica.getAscent() + 5));
            }
        }
        return dev;
    }

    /** Devuelve la lista de IPs que conectan a las redes indicadas 
	 * @param nombresRedes Nombres de la redes de las que se copian las IPs
	 * @param noCopiar IP que no se va a devolver en el vector
	 * @return Vector
	 */
    public Vector getListaIPConecta(Vector nombresRedes, String noCopiar) {
        int i, j;
        int tam = size();
        Vector dev = new Vector();
        listaInterfaces interfaces = new listaInterfaces();
        for (i = 0; i < tam; i++) if (getID(i).compareTo("pc") == 0 || getID(i).compareTo("ro") == 0 || getID(i).compareTo("mc") == 0) {
            interfaces = getInterfaces(i).copia();
            for (j = 0; j < interfaces.tam(); j++) if (noCopiar.compareTo(interfaces.getIP(j)) != 0 && nombresRedes.contains(interfaces.getconecta(j))) dev.add((interfaces.getIP(j) + " - " + interfaces.getconecta(j)));
        }
        return dev;
    }

    /** Devuelve un vector conteniendo los nombres de todos los equipos de la topologia */
    public Vector getNombresEquipos() {
        Vector dev = new Vector();
        for (int i = 0; i < tam(); i++) if (getID(i).compareTo("pc") == 0 || getID(i).compareTo("ro") == 0 || getID(i).compareTo("mc") == 0 || getID(i).compareTo("hu") == 0 || getID(i).compareTo("mo") == 0 || getID(i).compareTo("pu") == 0 || getID(i).compareTo("sw") == 0) dev.add(getNombre(i));
        return dev;
    }

    /** Devuelve un vector conteniendo los nombres de todos los equipos de la topologia */
    public Vector getNombres() {
        Vector dev = new Vector();
        for (int i = 0; i < tam(); i++) if (getCadenaSimulador(i).length() > 0) dev.add(getNombre(i));
        return dev;
    }

    /** Devuelve un vector conteniendo los nombres de todos los ordenadores junto con cada una de sus IP */
    public Vector getNombresOrdenadores() {
        int j;
        Vector dev = new Vector();
        listaInterfaces interfaces;
        for (int i = 0; i < tam(); i++) {
            interfaces = getInterfaces(i);
            for (j = 0; j < interfaces.tam(); j++) if (interfaces.getconecta(j).compareTo("N/A") != 0) dev.add(getNombre(i) + " (" + interfaces.getIP(j) + ")");
        }
        return dev;
    }

    /** Metodo que devuelve el numero de objetos seleccionados en la lista */
    public int getNumSeleccionados() {
        int dev = 0;
        int tam = size();
        for (int i = 0; i < tam; i++) if (getSeleccionado(i)) dev++;
        return dev;
    }

    /** Devuelve la posicion en la lista del objeto situado mas a la izquierda */
    public int getPosXMin() {
        int min = Integer.MAX_VALUE;
        int dev = -1;
        for (int i = 0; i < tam(); i++) if (getX(i) < min) {
            min = getX(i);
            dev = i;
        }
        return dev;
    }

    /** Devuelve la posicion en la lista del objeto situado mas alto */
    public int getPosYMin() {
        int min = Integer.MAX_VALUE;
        int dev = -1;
        int tam = size();
        for (int i = 0; i < tam; i++) if (getY(i) < min) {
            min = getY(i);
            dev = i;
        }
        return dev;
    }

    /** Devuelve la posicion del primer equipo seleccionado */
    public int getPrimerSeleccionado() {
        int tam = size();
        for (int i = 0; i < tam; i++) if (getSeleccionado(i)) return i;
        return -1;
    }

    /** Devuelve los tipos de maquinas a los que estan conectadas las conexiones recibidas */
    public Vector getTipos(Vector conexiones) {
        Vector dev = new Vector();
        for (int i = 0; i < conexiones.size(); i++) if (buscaEquipo((String) conexiones.elementAt(i)) != -1) dev.add(getID(buscaEquipo((String) conexiones.elementAt(i))));
        return dev;
    }

    /** Devuelve un objeto utilLinea representando la conexion entre dos equipos situados en las
	 * posiciones de la lista i y j
	 **/
    public utilLinea getutilLinea(int i, int j) {
        Point punto;
        utilLinea dev = new utilLinea();
        punto = new Point(getCoordenadasConexion(i, getX(j), getY(j), getWidth(j), getHeight(j)));
        dev.setX(punto.x, 1);
        dev.setY(punto.y, 1);
        punto = new Point(getCoordenadasConexion(j, getX(i), getY(i), getWidth(i), getHeight(i)));
        dev.setX(punto.x, 2);
        dev.setY(punto.y, 2);
        return dev;
    }

    /** Metodo que actualiza un objeto Graphics conteniendo la topologia para posteriormente
	 * imprimirla */
    public void imprimir(Graphics g, int ancho, int alto) {
        simuGrafico.dibuja(g, ancho, alto, this, null, null, null, true);
    }

    /** Desplaza todos los equipos segun su coordenada actual y las nuevas recibidas */
    public void mueveEquipos(int x, int y) {
        int tam = size();
        for (int i = 0; i < tam; i++) ((objetoVisual) elementAt(i)).setCoord(getX(i) + x, getY(i) + y);
    }

    /** Cambia las coordenadas de los equipos seleccionados*/
    public void mueveEquiposSeleccionados(int xRaton, int yRaton) {
        int tam = size();
        for (int i = 0; i < tam; i++) if (getSeleccionado(i)) ((objetoVisual) elementAt(i)).setCoord(getX(i) - incDistanx + xRaton, getY(i) - incDistany + yRaton);
    }

    /** Metodo que pega los objetos previamente seleccionados y copiados */
    public void pega(listaObjetos original, Vector nombres) {
        int pos;
        for (int i = 0; i < nombres.size(); i++) {
            pos = original.buscaEquipo((String) nombres.elementAt(i));
            if (pos != -1) insertaObjeto((objetoVisual) original.getObjeto(pos).clone());
        }
    }

    /** Funcion que comprueba la posibilidad de poder simular con la configuracion
	 * actual de la topologia
	 * @return boolean indicando si es posible la simulacion
	 */
    public boolean posibleSimular() {
        boolean dev = false;
        for (int i = 0; i < size() && !dev; i++) if (getID(i).compareTo("pc") == 0) if (getConexiones(i).size() > 0) dev = true;
        return dev;
    }

    public void setInterfaces(listaObjetos lista) {
    }
}
