package visSim;

import java.util.Vector;

/** Esta clase gestiona un conjunto de interfaces interfazVisual de cada equipo
 * Extiende un Vector para gestion de la lista*/
public class listaInterfaces extends Vector {

    /** Constructor de la clase */
    public listaInterfaces() {
    }

    /** Metodo para borrado de una interfaz de la lista
	 * La interfaz que se elimina sera aquella que coincida con todos los parametros recibidos
	 * @param nombre Nombre de la interfaz que se va a eliminar
	 * @param ip IP de la interfaz que se va a eliminar
	 * @param mascara Mascara de red de la interfaz que se va a eliminar
	 * @param dirEnlace Direccion de enlace de la interfaz que se va a eliminar
	 * @param conecta Nombre del equipo al que se encuentra conectado la interfaz que se va a eliminar
	 */
    public void borra(String nombre, String ip, String mascara, String dirEnlace, String conecta) {
        boolean encontrado = false;
        for (int i = 0; i < size() && !encontrado; i++) if (getNombre(i).compareTo(nombre) == 0 && getIP(i).compareTo(ip) == 0 && getMascara(i).compareTo(mascara) == 0 && getDirEnlace(i).compareTo(dirEnlace) == 0 && getconecta(i).compareTo(conecta) == 0) {
            removeElementAt(i);
            encontrado = true;
        }
        if (!encontrado) System.err.println("Error inesperado. Interfaz " + nombre + " no encontrada.");
    }

    /** Metodo para eliminar todas las interfaces conectadas al equipo de nombre recibido
	    Devuelve un Vector conteniendo las IPs de las interfaces eliminadas */
    public Vector borraConectado(String nombre) {
        Vector dev = new Vector();
        for (int i = 0; i < size(); i++) if (getconecta(i).compareTo(nombre) == 0) {
            dev.add(getIP(i));
            removeElementAt(i);
        }
        return dev;
    }

    /** Elimina todo el contenido de la lista de interfaces */
    public void borraLista() {
        this.clear();
    }

    /** Devuelve un Vector conteniendo las posiciones de las interfaces que estan conectadas al equipo
	 * de nombre recibido */
    public Vector buscaConectados(String nombre) {
        Vector dev = new Vector();
        for (int i = 0; i < size(); i++) if (getconecta(i).compareTo(nombre) == 0) dev.add(new Integer(i));
        return dev;
    }

    /** Metodo para modificar el nombre del equipo al que conectan las interfaces
	 * @param nombreViejo Nombre viejo del equipo conectado
	 * @param nombreNuevo Nombre nuevo del equipo conectado
	 */
    public void cambiaNombreEquipo(String nombreViejo, String nombreNuevo) {
        for (int i = 0; i < size(); i++) if (getconecta(i).compareTo(nombreViejo) == 0) setconecta(i, nombreNuevo);
    }

    /** Devuelve un objeto tipo listaInterfaces resultado de copiar la lista actual */
    public listaInterfaces copia() {
        listaInterfaces dev = new listaInterfaces();
        for (int i = 0; i < size(); i++) dev.add(getInterfaz(i));
        return dev;
    }

    /** Devuelve una direccion MAC no existente en la topologia ni en esta maquina
	 * Para saber las direcciones MAC del resto de la topologia la funcion recibe dichas
	 * direcciones en un Vector */
    public String dameMACNoExistente(Vector listaMAC) {
        boolean asignada = true;
        boolean encontrada;
        int i;
        int tamInicial = listaMAC.size() + 1;
        String MACnum, MACtxt = "";
        while (asignada) {
            MACnum = Integer.toHexString(tamInicial).toUpperCase();
            MACtxt = "";
            while (MACnum.length() != 12) MACnum = "0" + MACnum;
            for (i = 0; i < 5; i++) MACtxt = MACtxt + MACnum.substring(i * 2, (i * 2) + 2) + ":";
            MACtxt = MACtxt + MACnum.substring(10, 12);
            encontrada = false;
            for (i = 0; i < listaMAC.size() && !encontrada; i++) if (((String) listaMAC.elementAt(i)).compareTo(MACtxt) == 0) encontrada = true;
            if (!encontrada) for (i = 0; i < tam(); i++) if (getDirEnlace(i).compareTo(MACtxt) == 0) encontrada = true;
            if (encontrada) tamInicial++; else asignada = false;
        }
        return MACtxt;
    }

    /** Devuelve un vector de nombres de las interfaces.
	 * @param copioDatoActual boolean que indica si se incluye la interfaz con el nombre recibido
	 * @param nombre de la interfaz que se copiara o no dependiendo de copioDatoActual
	 * @return Vector con los nombres
	 */
    public Vector dameNombresInterfaz(boolean copioDatoActual, String nombre) {
        Vector dev = new Vector();
        for (int i = 0; i < tam(); i++) if (copioDatoActual || (!copioDatoActual && getNombre(i).compareTo(nombre) != 0)) dev.add(getNombre(i));
        return dev;
    }

    /** Devuelve el nombre del equipo al que se encuentra conectada la interfaz situada en pos */
    public String getconecta(int pos) {
        return getInterfaz(pos).getconecta();
    }

    /** Devuelve la direccion de enlace de la interfaz situada en pos */
    public String getDirEnlace(int pos) {
        return getInterfaz(pos).getDirEnlace();
    }

    /** Devuelve la interfaz situada en pos */
    public interfazVisual getInterfaz(int pos) {
        return (interfazVisual) elementAt(pos);
    }

    /** Devuelve IP de la interfaz situada en pos */
    public String getIP(int pos) {
        return getInterfaz(pos).getIP();
    }

    /** Devuelve la mascara de red de la interfaz situada en pos */
    public String getMascara(int pos) {
        return getInterfaz(pos).getMascara();
    }

    /** Devuelve el nombre de la interfaz situada en pos */
    public String getNombre(int pos) {
        return getInterfaz(pos).getNombre();
    }

    /** Insercion de una nueva interfaz en la lista a partir de un objeto interfazVisual */
    public void inserta(interfazVisual interfaz) {
        add(new interfazVisual(interfaz.getNombre(), interfaz.getIP(), interfaz.getMascara(), interfaz.getDirEnlace(), interfaz.getconecta()));
    }

    /** Metodo para insercion de una nueva interfaz en la lista a partir de todos sus parametros */
    public void inserta(String nombre, String ip, String mascara, String dirEnlace, String conecta) {
        add(new interfazVisual(nombre, ip, mascara, dirEnlace, conecta));
    }

    /** Metodo para modificar una interfaz de la lista
	 * La interfaz que se modificara sera la que coincida con todos los parametros recibidos */
    public void modifica(String nombre, String ip, String mascara, String dirEnlace, String conecta, String nombreN, String ipN, String mascaraN, String dirEnlaceN, String conectaN) {
        boolean encontrado = false;
        for (int i = 0; i < size() && !encontrado; i++) if (getNombre(i).compareTo(nombre) == 0 && getIP(i).compareTo(ip) == 0 && getMascara(i).compareTo(mascara) == 0 && getDirEnlace(i).compareTo(dirEnlace) == 0 && getconecta(i).compareTo(conecta) == 0) {
            removeElementAt(i);
            insertElementAt(new interfazVisual(nombreN, ipN, mascaraN, dirEnlaceN, conectaN), i);
            encontrado = true;
        }
        if (!encontrado) System.err.println("Error inesperado. Interfaz " + nombre + " no encontrada en listaInterfaces.modifica");
    }

    /** Las interfaces conectadas al nombre del equipo recibido dejan de estarlo */
    public void modificaConectado(String nombre) {
        for (int i = 0; i < size(); i++) if (getconecta(i).compareTo(nombre) == 0) setconecta(i, "N/A");
    }

    /** Establece el nombre del equipo al que se conecta la interfaz situada en pos */
    public void setconecta(int pos, String conecta) {
        getInterfaz(pos).setconecta(conecta);
    }

    /** Establece la direccion de enlace de la interfaz situada en pos */
    public void setDirEnlace(int pos, String dirEnlace) {
        getInterfaz(pos).setDirEnlace(dirEnlace);
    }

    /** Establece la IP de la interfaz situada en pos */
    public void setIP(int pos, String ip) {
        getInterfaz(pos).setIP(ip);
    }

    /** Establece la mascara de red de la interfaz situada en pos */
    public void setMascara(int pos, String mascara) {
        getInterfaz(pos).setMascara(mascara);
    }

    /** Establece el nombre de la interfaz situada en pos */
    public void setNombre(int pos, String nombre) {
        getInterfaz(pos).setNombre(nombre);
    }

    public int tam() {
        return size();
    }

    public boolean existeInterfaz(String nombre) {
        for (int i = 0; i < size(); i++) if (nombre.compareTo(this.getNombre(i)) == 0) return true;
        return false;
    }
}
