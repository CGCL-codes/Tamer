package es.realtimesystems.simplemulticast;

import java.util.TreeMap;
import java.util.Vector;
import java.util.Iterator;
import java.util.SortedMap;

/**
 * 1. Almacena <b>los ID_TPDU por los que est� esperando recibir asentimientos
 *    positivos.</b><br>
 * 2. Lleva el control de quien ha asentido (vecinos o hermanos --> ID_Socket)
 *    o (grupos locales --> IDGL)
 *
 * Esta clase no es thread-safe.
 *
 * @version  1.0
 * @author Antonio Berrocal Piris
 * <A HREF="mailto:AntonioBP@wanadoo.es">(AntonioBP@wanadoo.es)</A><p>
 * M. Alejandro Garc�a Dom�nguez
 * <A HREF="mailto:AlejandroGarcia@wanadoo.es">(AlejandroGarcia@wanadoo.es)</A><p>
 */
public class TablaAsentimientos {

    /**
   * ID_TPDU por los que se est� esperando asentimientos positivos.<br>
   * <table border=1>
   *  <tr>  <td><b>Key:</b></td>
   *	    <td>ID_TPDU</td>
   *  </tr>
   *  <tr>  <td><b>Value:</b></td>
   *	    <td>Instancia de RegistroAsentimientos</td>
   *  </tr>
   * </table>
   */
    private ListaOrdID_TPDU listaIDs_TPDUEnEsperaAsentimiento = null;

    /** Objeto datosThread */
    private DatosThread datosThread = null;

    /** Objeto CGLThread */
    private CGLThread cglThread = null;

    /**
   * Crea los estructuras de datos necesarias.
   * @param cglThread objeto CGLThread
   * @throws ParametroInvalidoExcepcion lanzada si cglThread es null
   */
    public TablaAsentimientos(DatosThread datosThread) throws ParametroInvalidoExcepcion {
        if (datosThread == null) throw new ParametroInvalidoExcepcion("Puntero a DatosThread NULO.");
        this.datosThread = datosThread;
        this.cglThread = this.datosThread.getCGLThread();
        if (this.cglThread == null) throw new ParametroInvalidoExcepcion("Puntero a cglThread NULO.");
        this.listaIDs_TPDUEnEsperaAsentimiento = new ListaOrdID_TPDU();
    }

    /**
   * A�ade un nuevo ID_TPDU para el cual se est� esperando recibir asentimientos.
   * Hace una copia de los asentimientos que ya se han recibido por su inmediato
   * superior, para la misma direcci�n fuente, si lo hay.
   * @param ID_TPDU identificador del TPDU
   * @param iNumeroRafaga n�mero de r�faga a la que pertenece el id_tdpu
   * @return true si no exist�a y ha sido a�adido, y false en caso contrario.
   */
    public boolean addID_TPDUEnEsperaAsentimiento(ID_TPDU id_TPDU, int iNumeroRafaga) {
        if (!this.listaIDs_TPDUEnEsperaAsentimiento.contiene(id_TPDU)) {
            SortedMap sortedMap = this.listaIDs_TPDUEnEsperaAsentimiento.getSublistaMayorIgual(id_TPDU.getID_Socket(), id_TPDU.getNumeroSecuencia());
            RegistroAsentimientos reg = null;
            if (sortedMap.size() > 0) {
                RegistroAsentimientos regSuperior = (RegistroAsentimientos) sortedMap.get(sortedMap.firstKey());
                reg = (RegistroAsentimientos) regSuperior.clone();
                reg.iNumeroRafaga = iNumeroRafaga;
            } else {
                reg = new RegistroAsentimientos();
                reg.iNumeroRafaga = iNumeroRafaga;
            }
            this.listaIDs_TPDUEnEsperaAsentimiento.put(id_TPDU, reg);
            return true;
        }
        return false;
    }

    /**
   * Elimina ID_TPDU para el cual se est� esperando recibir asentimientos.
   * Elimina todos los menores o iguales.
   * @param ID_TPDU identificador del TPDU
   * @return true si exist�a y ha sido eliminado, y false en caso contrario.
   */
    public void removeID_TPDUMenorIgualEnEsperaAsentimiento(ID_TPDU id_TPDU) {
        SortedMap sortedMap = this.listaIDs_TPDUEnEsperaAsentimiento.getSublistaMenorIgual(id_TPDU.getID_Socket(), id_TPDU.getNumeroSecuencia());
        sortedMap.clear();
    }

    /**
   * Anota como recibido de id_Socket un ACK para el id_tpdu indicado, as� como
   * para todos los menores, por ser los ACK acumulativos. Comprueba que id_Socket
   * pertenece al grupo local (es vecino).
   * @return true si se estaba esperando recibir asentimiento por id_tpdu o por
   * alguno menor.
   */
    public boolean addACK(ID_Socket id_Socket, ID_TPDU id_tpdu) {
        if (!this.cglThread.esVecino(id_Socket)) return false;
        SortedMap sortedMap = this.listaIDs_TPDUEnEsperaAsentimiento.getSublistaMenorIgual(id_tpdu.getID_Socket(), id_tpdu.getNumeroSecuencia());
        if (sortedMap == null) return false;
        Iterator iteradorRegistros = sortedMap.values().iterator();
        RegistroAsentimientos regNext = null;
        boolean bResult = false;
        while (iteradorRegistros.hasNext()) {
            regNext = (RegistroAsentimientos) iteradorRegistros.next();
            regNext.treeMapEnviadoACK.put(id_Socket, null);
            bResult = true;
        }
        return bResult;
    }

    /**
   * id_Socket ha mandado ACK para todos los ID_TPDU que estan en espera de
   * asentimiento.
   * @return true si hab�a alg�n ID_TPDU en espera.
   */
    public boolean addACKAID_TPDUEnEspera(ID_Socket id_Socket) {
        if (!this.cglThread.esVecino(id_Socket)) return false;
        Iterator iteradorRegistros = this.listaIDs_TPDUEnEsperaAsentimiento.iteradorObjetos();
        if (iteradorRegistros == null) return false;
        RegistroAsentimientos regNext = null;
        while (iteradorRegistros.hasNext()) {
            regNext = (RegistroAsentimientos) iteradorRegistros.next();
            regNext.treeMapEnviadoACK.put(id_Socket, null);
        }
        return true;
    }

    /**
   * Anota como recibido de idgl un HACK para el id_tpdu indicado, as� como
   * para todos los menores, por ser los HACK acumulativos. Comprueba que idgl
   * es hijo jer�rquico.
   * @return true si se estaba esperando recibir asentimiento por id_tpdu o por
   * alguno menor.
   */
    public boolean addHACK(IDGL idgl, ID_TPDU id_tpdu) {
        if ((idgl == null) || (id_tpdu == null)) return false;
        IDGL idglFuente = this.datosThread.getIDGL(id_tpdu.getID_Socket());
        if (idglFuente == null) return false;
        if (!this.cglThread.getCGHijos(idglFuente).containsKey(idgl)) return false;
        SortedMap sortedMap = this.listaIDs_TPDUEnEsperaAsentimiento.getSublistaMenorIgual(id_tpdu.getID_Socket(), id_tpdu.getNumeroSecuencia());
        if (sortedMap == null) return false;
        Iterator iteradorRegistros = sortedMap.values().iterator();
        RegistroAsentimientos regNext = null;
        boolean bResult = false;
        while (iteradorRegistros.hasNext()) {
            regNext = (RegistroAsentimientos) iteradorRegistros.next();
            regNext.treeMapEnviadoHSACK.remove(idgl);
            regNext.treeMapEnviadoHACK.put(idgl, null);
            bResult = true;
        }
        return bResult;
    }

    /**
   * Anota como recibido de idgl un HSACK para el id_tpdu indicado, as� como
   * para todos los menores, por ser los HSACK acumulativos. Comprueba que idgl
   * es hijo jer�rquico.
   * @return true si se estaba esperando recibir asentimiento por id_tpdu o por
   * alguno menor.
   */
    public boolean addHSACK(IDGL idgl, ID_TPDU id_tpdu) {
        final String mn = "TablaAsentimientos.addHSACK (idgl,id_tpdu)";
        if ((idgl == null) || (id_tpdu == null)) return false;
        IDGL idglFuente = this.datosThread.getIDGL(id_tpdu.getID_Socket());
        if (idglFuente == null) return false;
        if (!this.cglThread.getCGHijos(idglFuente).containsKey(idgl)) return false;
        SortedMap sortedMap = this.listaIDs_TPDUEnEsperaAsentimiento.getSublistaMenorIgual(id_tpdu.getID_Socket(), id_tpdu.getNumeroSecuencia());
        if (sortedMap == null) return false;
        Iterator iteradorRegistros = sortedMap.values().iterator();
        RegistroAsentimientos regNext = null;
        boolean bResult = false;
        while (iteradorRegistros.hasNext()) {
            regNext = (RegistroAsentimientos) iteradorRegistros.next();
            if (regNext.treeMapEnviadoHACK.containsKey(idgl)) continue;
            regNext.treeMapEnviadoHSACK.put(idgl, null);
            bResult = true;
        }
        return bResult;
    }

    /**
   * idgl ha mandado HSACK para todos los TPDU que est�n en espera de
   * asentimiento.
   * @return true si hab�a alg�n ID_TPDU en espera.
   */
    public boolean addHSACKAID_TPDUEnEspera(IDGL idgl) {
        if (idgl == null) return false;
        Iterator iteradorID_TPDU = this.listaIDs_TPDUEnEsperaAsentimiento.iteradorID_TPDU();
        RegistroAsentimientos regNext = null;
        ID_TPDU id_tpduNext = null;
        while (iteradorID_TPDU.hasNext()) {
            id_tpduNext = (ID_TPDU) iteradorID_TPDU.next();
            IDGL idglFuente = this.datosThread.getIDGL(id_tpduNext.getID_Socket());
            if (idglFuente == null) continue;
            if (!this.cglThread.getCGHijos(idglFuente).containsKey(idgl)) continue;
            regNext = (RegistroAsentimientos) this.listaIDs_TPDUEnEsperaAsentimiento.get(id_tpduNext);
            if (regNext.treeMapEnviadoHACK.containsKey(idgl)) continue;
            regNext.treeMapEnviadoHSACK.put(idgl, null);
        }
        return true;
    }

    /**
   * Devuelve una lista ordenada de ID_TPDU que han sido asentidos por
   * todos los vecinos e hijos jer�rquicos.
   * @return lista con los ID_TPDU asentidos o vac�a si no hay ninguno.
   */
    public ListaOrdID_TPDU getID_TPDUAsentidos() {
        ListaOrdID_TPDU listaResult = new ListaOrdID_TPDU();
        Iterator iterador = this.listaIDs_TPDUEnEsperaAsentimiento.iteradorID_TPDU();
        ID_TPDU id_tpduNext = null;
        RegistroAsentimientos reg = null;
        IDGL idglFuente = null;
        while (iterador.hasNext()) {
            id_tpduNext = (ID_TPDU) iterador.next();
            reg = (RegistroAsentimientos) this.listaIDs_TPDUEnEsperaAsentimiento.get(id_tpduNext);
            idglFuente = this.datosThread.getIDGL(id_tpduNext.getID_Socket());
            if (idglFuente == null) continue;
            if ((this.cglThread.numeroVecinos() == reg.treeMapEnviadoACK.size()) && (this.cglThread.getCGHijos(idglFuente).size() == reg.treeMapEnviadoHACK.size())) listaResult.put(id_tpduNext, null);
        }
        return listaResult;
    }

    /**
   * Devuelve una lista ordenada de los ID_TPDU que no han sido asentidos por
   * todos los vecinos e hijos jer�rquicos.
   * @return lista con los ID_TPDU no asentidos o vac�a si no hay ninguno.
   */
    public ListaOrdID_TPDU getID_TPDUNoAsentidos() {
        ListaOrdID_TPDU listaResult = new ListaOrdID_TPDU();
        Iterator iterador = this.listaIDs_TPDUEnEsperaAsentimiento.iteradorID_TPDU();
        ID_TPDU id_tpduNext = null;
        RegistroAsentimientos reg = null;
        IDGL idglFuente = null;
        while (iterador.hasNext()) {
            id_tpduNext = (ID_TPDU) iterador.next();
            reg = (RegistroAsentimientos) this.listaIDs_TPDUEnEsperaAsentimiento.get(id_tpduNext);
            idglFuente = this.datosThread.getIDGL(id_tpduNext.getID_Socket());
            if (idglFuente == null) continue;
            if ((this.cglThread.numeroVecinos() == reg.treeMapEnviadoACK.size()) && (this.cglThread.getCGHijos(idglFuente).size() == reg.treeMapEnviadoHACK.size())) {
            } else {
                listaResult.put(id_tpduNext, null);
                Log.debug(Log.TABLA_ASENTIMIENTOS, "TablaAsentimientos.getID_TPDUNoAsentidos", "ID_TDPU No asentido por todos: " + id_tpduNext);
                Log.debug(Log.TABLA_ASENTIMIENTOS, "", "N� Vecinos: " + this.cglThread.numeroVecinos());
                Log.debug(Log.TABLA_ASENTIMIENTOS, "", "N� ACKs recibidos: " + reg.treeMapEnviadoACK.size());
                Log.debug(Log.TABLA_ASENTIMIENTOS, "", "N� CG Hijos: " + this.cglThread.getCGHijos(idglFuente).size());
                Log.debug(Log.TABLA_ASENTIMIENTOS, "", "N� HACKs recibidos: " + reg.treeMapEnviadoHACK.size());
            }
        }
        return listaResult;
    }

    /**
   * Elimina los ID_TPDU cuyo id_socket coincida con el especificado y que
   * pertenezcan a la r�faga iNumeroRafaga.
   * @param id_socket al que tiene que pertenecer los ID_TPDU que elimine
   * @param iNumeroRafaga n�mero de la r�faga de los ID_TPDU a eliminar.
   * @return lista ordenada con los ID_TPDU eliminados, o vac�a si no se elimin�
   * ninguno.
   */
    public ListaOrdID_TPDU removeID_TPDUEnEsperaAsentimiento(ID_Socket id_socket, int iNumeroRafaga) {
        ListaOrdID_TPDU listaResult = new ListaOrdID_TPDU();
        Iterator iteradorID_TPDU = this.listaIDs_TPDUEnEsperaAsentimiento.getSublista(id_socket).keySet().iterator();
        ID_TPDU id_tpduNext = null;
        RegistroAsentimientos reg = null;
        while (iteradorID_TPDU.hasNext()) {
            id_tpduNext = (ID_TPDU) iteradorID_TPDU.next();
            reg = (RegistroAsentimientos) this.listaIDs_TPDUEnEsperaAsentimiento.get(id_tpduNext);
            if (reg == null) continue;
            if (reg.iNumeroRafaga == iNumeroRafaga) {
                iteradorID_TPDU.remove();
                listaResult.put(id_tpduNext, null);
            }
        }
        return listaResult;
    }

    /**
   * Comprueba si se est� esperando recibir asentimiento por id_tpdu
   * @param id_tpdu
   */
    public boolean contieneID_TPDU(ID_TPDU id_tpdu) {
        return this.listaIDs_TPDUEnEsperaAsentimiento.contiene(id_tpdu);
    }

    /**
   * Devuelve el menor ID_TPDU en espera de asentimiento, asociado al id_socket
   * indicado.
   * @param id_socket
   * @return id_tpdu menor en espera para id_socket
   */
    public ID_TPDU getID_TPDUMenorEnEsperaAsentimiento(ID_Socket id_socket) {
        return this.listaIDs_TPDUEnEsperaAsentimiento.getID_TPDUMenor(id_socket);
    }

    /**
   * Devuelve true si no se est� esperando asentimiento por ning�n TPDU.
   */
    public boolean estaVacia() {
        return (this.listaIDs_TPDUEnEsperaAsentimiento.size() == 0);
    }

    /**
   * Comprueba si id_TPDU ha sido asentido por todos los vecinos e hijos
   * jer�rquicos.
   * @return true si ha sido asentido y false <b>si no se estaba en espera de
   * asentimiento para dicho id_TPDU</b> o no ha sido asentido.
   */
    public boolean asentido(ID_TPDU id_TPDU) {
        RegistroAsentimientos reg = (RegistroAsentimientos) this.listaIDs_TPDUEnEsperaAsentimiento.get(id_TPDU);
        if (reg == null) return false;
        IDGL idglFuente = this.datosThread.getIDGL(id_TPDU.getID_Socket());
        if (idglFuente == null) return false;
        if ((this.cglThread.numeroVecinos() == reg.treeMapEnviadoACK.size()) && (this.cglThread.getCGHijos(idglFuente).size() == reg.treeMapEnviadoHACK.size())) {
            return true;
        }
        return false;
    }

    /**
   * Convierte los HSACK que hab�a recibido para id_tpdu en HACK.
   * @param id_tpdu
   */
    public void convertirHSACKaHACK(ID_TPDU id_tpdu) {
        if (!contieneID_TPDU(id_tpdu)) return;
        SortedMap sortedMap = this.listaIDs_TPDUEnEsperaAsentimiento.getSublistaMenorIgual(id_tpdu.getID_Socket(), id_tpdu.getNumeroSecuencia());
        if (sortedMap == null) return;
        Iterator iteradorRegistros = sortedMap.values().iterator();
        RegistroAsentimientos regNext = null;
        while (iteradorRegistros.hasNext()) {
            regNext = (RegistroAsentimientos) iteradorRegistros.next();
            regNext.treeMapEnviadoHACK.putAll(regNext.treeMapEnviadoHSACK);
            regNext.treeMapEnviadoHSACK.clear();
        }
    }

    /**
   * Comprueba si id_TPDU ha sido semiAsentido por todos los vecinos e hijos
   * jer�rquicos. Es decir, si han mandado un ACK todos los ID_Sockets vecinos
   * y un HACK o un HSACK todos los hijos jer�rquicos.
   * @param id_TPDU
   * @return true si ha sido semiasentido y false <b>si no se estaba en espera de
   * asentimiento para dicho id_TPDU</b> o no ha sido asentido.
   */
    public boolean semiAsentido(ID_TPDU id_TPDU) {
        RegistroAsentimientos reg = (RegistroAsentimientos) this.listaIDs_TPDUEnEsperaAsentimiento.get(id_TPDU);
        if (reg == null) return false;
        IDGL idglFuente = this.datosThread.getIDGL(id_TPDU.getID_Socket());
        if (idglFuente == null) return false;
        if ((this.cglThread.numeroVecinos() == reg.treeMapEnviadoACK.size()) && (this.cglThread.getCGHijos(idglFuente).size() == (reg.treeMapEnviadoHACK.size() + reg.treeMapEnviadoHSACK.size()))) return true;
        return false;
    }

    /**
   * Comprueba si id_TPDU ha sido asentido por alg�n vecino.<br>
   * Es decir, si alg�n vecino ha mandado un ACK.
   * @return id_TPDU
   */
    public boolean algunACKID_Socket(ID_TPDU id_TPDU) {
        RegistroAsentimientos reg = (RegistroAsentimientos) this.listaIDs_TPDUEnEsperaAsentimiento.get(id_TPDU);
        if (reg == null) return false;
        if ((reg.treeMapEnviadoACK.size() != 0)) return true;
        return false;
    }

    /**
  * Devuelve un treemap con los ID_Sockets que no han enviado ACK pora id_TPDU,
  * o el vector vac�o si no hay ninguno.
  * @param id_TPDU identificador de TPDU
  * @return un objeto treemap con los ID_Sockets o vac�o.
   * <table border=1>
   *  <tr>  <td><b>Key:</b></td>
   *	    <td>{@link ID_Socket}</td>
   *  </tr>
   *  <tr>  <td><b>Value:</b></td>
   *	    <td>NULL</td>
   *  </tr>
   * </table>
  */
    public TreeMap getTreeMapID_SocketsNoEnviadoACK(ID_TPDU id_TPDU) {
        TreeMap treeMapResult = new TreeMap();
        RegistroAsentimientos reg = (RegistroAsentimientos) this.listaIDs_TPDUEnEsperaAsentimiento.get(id_TPDU);
        if (reg != null) {
            int iDiferencia = this.cglThread.numeroVecinos() - reg.treeMapEnviadoACK.size();
            if (iDiferencia <= 0) return treeMapResult;
            Iterator iterador = this.cglThread.getTreeMapID_SocketVecinos().keySet().iterator();
            ID_Socket id_SocketNext = null;
            while (iterador.hasNext() || (iDiferencia > 0)) {
                id_SocketNext = (ID_Socket) iterador.next();
                if (!(reg.treeMapEnviadoACK.containsKey(id_SocketNext))) {
                    treeMapResult.put(id_SocketNext, null);
                    iDiferencia--;
                }
            }
        }
        return treeMapResult;
    }

    /**
  * Devuelve un treemap con los IDGLs que no han enviado HACK o HSACK
  * pora id_TPDU, o el vector vac�o si no hay ninguno.
  * @param id_TPDU identificador de TPDU
  * @return un objeto treemap con los IDGLs o vac�o.
   * <table border=1>
   *  <tr>  <td><b>Key:</b></td>
   *	    <td>{@link IDGL}</td>
   *  </tr>
   *  <tr>  <td><b>Value:</b></td>
   *	    <td>NULL</td>
   *  </tr>
   * </table>
  */
    public TreeMap getTreeMapIDGLNoEnviadoHACKoHSACK(ID_TPDU id_TPDU) {
        TreeMap treeMapResult = new TreeMap();
        RegistroAsentimientos reg = (RegistroAsentimientos) this.listaIDs_TPDUEnEsperaAsentimiento.get(id_TPDU);
        if (reg != null) {
            IDGL idglFuente = this.datosThread.getIDGL(id_TPDU.getID_Socket());
            if (idglFuente == null) return treeMapResult;
            int iDiferencia = this.cglThread.getCGHijos(idglFuente).size() - (reg.treeMapEnviadoHACK.size() + reg.treeMapEnviadoHSACK.size());
            if (iDiferencia <= 0) return treeMapResult;
            Iterator iterador = this.cglThread.getCGHijos(idglFuente).keySet().iterator();
            IDGL idglNext = null;
            while (iterador.hasNext() || (iDiferencia > 0)) {
                idglNext = (IDGL) iterador.next();
                if (!(reg.treeMapEnviadoHACK.containsKey(idglNext)) && !(reg.treeMapEnviadoHSACK.containsKey(idglNext))) {
                    treeMapResult.put(idglNext, null);
                    iDiferencia--;
                }
            }
        }
        return treeMapResult;
    }

    /**
  * Devuelve un treemap con los IDGLs que no han enviado HACK para id_TPDU,
  * o el vector vac�o si no hay ninguno.
  * @param id_TPDU identificador de TPDU
  * @return  un objeto treemap con los IDGLs o vac�o.
   * <table border=1>
   *  <tr>  <td><b>Key:</b></td>
   *	    <td>{@link IDGL}</td>
   *  </tr>
   *  <tr>  <td><b>Value:</b></td>
   *	    <td>NULL</td>
   *  </tr>
   * </table>
  */
    public TreeMap getTreeMapIDGLNoEnviadoHACK(ID_TPDU id_TPDU) {
        TreeMap treeMapResult = new TreeMap();
        RegistroAsentimientos reg = (RegistroAsentimientos) this.listaIDs_TPDUEnEsperaAsentimiento.get(id_TPDU);
        if (reg != null) {
            IDGL idglFuente = this.datosThread.getIDGL(id_TPDU.getID_Socket());
            if (idglFuente == null) return treeMapResult;
            int iDiferencia = this.cglThread.getCGHijos(idglFuente).size() - reg.treeMapEnviadoHACK.size();
            if (iDiferencia <= 0) return treeMapResult;
            Iterator iterador = this.cglThread.getCGHijos(idglFuente).keySet().iterator();
            IDGL idglNext = null;
            while (iterador.hasNext() || (iDiferencia > 0)) {
                idglNext = (IDGL) iterador.next();
                if (!(reg.treeMapEnviadoHACK.containsKey(idglNext))) {
                    treeMapResult.put(idglNext, null);
                    iDiferencia--;
                }
            }
        }
        return treeMapResult;
    }

    /**
   * Anota como enviado un HSACK para id_tpdu.
   * @param id_tpdu
   */
    public void setEnviadoHSACK(ID_TPDU id_tpdu) {
        if (id_tpdu == null) return;
        RegistroAsentimientos reg = (RegistroAsentimientos) this.listaIDs_TPDUEnEsperaAsentimiento.get(id_tpdu);
        if (reg == null) return;
        reg.bEnviadoHSACK = true;
    }

    /**
   * Devuelve true si para el id_tpdu ya se ha enviado un HSACK.
   * @param id_tpdu
   */
    public boolean enviadoHSACK(ID_TPDU id_tpdu) {
        if (id_tpdu == null) return false;
        RegistroAsentimientos reg = (RegistroAsentimientos) this.listaIDs_TPDUEnEsperaAsentimiento.get(id_tpdu);
        if (reg == null) return false;
        return (reg.bEnviadoHSACK);
    }

    /**
   * Implementaci�n de la interfaz ID_SocketListener. Elimina toda la
   * informaci�n sobre  id_socket.<br>
   * Este m�todo es ejecutado por el thread <b>"ThreadCGL"</b>
   * @param id_socket
   */
    public void removeID_Socket(ID_Socket id_socket) {
        listaIDs_TPDUEnEsperaAsentimiento.removeID_Socket(id_socket);
        Iterator iterador = listaIDs_TPDUEnEsperaAsentimiento.iteradorObjetos();
        RegistroAsentimientos regNext = null;
        while (iterador.hasNext()) {
            regNext = (RegistroAsentimientos) iterador.next();
            regNext.treeMapEnviadoACK.remove(id_socket);
        }
    }

    /**
   * Implementaci�n de la interfaz IDGLListener. Elimina toda la informaci�n
   * sobre idgl.<br>
   * Este m�todo es ejecutado por el thread <b>"ThreadCGL"</b>
   * @param idgl
   */
    public void removeIDGL(IDGL idgl) {
        Iterator iterador = listaIDs_TPDUEnEsperaAsentimiento.iteradorObjetos();
        RegistroAsentimientos regNext = null;
        while (iterador.hasNext()) {
            regNext = (RegistroAsentimientos) iterador.next();
            regNext.treeMapEnviadoHACK.remove(idgl);
            regNext.treeMapEnviadoHSACK.remove(idgl);
        }
    }

    /**
  * Devuelve una cadena informativa.
  */
    public String toString() {
        return this.listaIDs_TPDUEnEsperaAsentimiento.toString();
    }
}

/**
 * Clase que almacena informaci�n sobre los asentimientos recibidos para un
 * ID_TDPU.<br>
 * @see TablaAsentimientos#listaIDs_TPDUEnEsperaAsentimiento
 * Esta clase no es thread-safe.
 * @version  1.0
 * @author M. Alejandro Garc�a Dom�nguez
 * <A HREF="mailto:garcia@arconet.es">(garcia@arconet.es)</A><p>
 *			   Antonio Berrocal Piris
 */
class RegistroAsentimientos implements Cloneable {

    /**
    * Almacena la informaci�n sobre los id_socket que han enviado ACK.
    * <table border=1>
    *  <tr>  <td><b>Key:</b></td>
    *	    <td>{@link ID_Socket}</td>
    *  </tr>
    *  <tr>  <td><b>Value:</b></td>
    *	    <td>NULL</td>
    *  </tr>
    * </table>
    */
    TreeMap treeMapEnviadoACK = null;

    /**
    * Almacena la informaci�n sobre los IDGL que han enviado HACK
    * <table border=1>
    *  <tr>  <td><b>Key:</b></td>
    *	    <td>{@link IDGL}</td>
    *  </tr>
    *  <tr>  <td><b>Value:</b></td>
    *	    <td>NULL</td>
    *  </tr>
    * </table>
    */
    TreeMap treeMapEnviadoHACK = null;

    /**
    * Almacena la informaci�n sobre los IDGL que han enviado HSACK
    * <table border=1>
    *  <tr>  <td><b>Key:</b></td>
    *	    <td>{@link IDGL}</td>
    *  </tr>
    *  <tr>  <td><b>Value:</b></td>
    *	    <td>NULL</td>
    *  </tr>
    * </table>
    */
    TreeMap treeMapEnviadoHSACK = null;

    /** Indica si se ha enviado un HSACK para el tpdu */
    boolean bEnviadoHSACK = false;

    /** N�mero de r�faga a la que pertenece al id_tpdu asociado */
    int iNumeroRafaga = -1;

    /**
    * Crea las estructuras de datos necesarias.
    */
    public RegistroAsentimientos() {
        this.treeMapEnviadoACK = new TreeMap();
        this.treeMapEnviadoHACK = new TreeMap();
        this.treeMapEnviadoHSACK = new TreeMap();
    }

    /**
    * Constructor privado para usar en la clonaci�n.
    */
    private RegistroAsentimientos(int iNoUsado) {
    }

    /**
    * Devuelve una cadena informativa del registro.
    */
    public String toString() {
        return "ID_Socket han enviado ACK  : " + treeMapEnviadoACK + "\nIDGLs han enviado HACK : " + treeMapEnviadoHACK + "\nIDGLs han enviado HSACK: " + treeMapEnviadoHSACK + "\nEnviadoHSACK: " + bEnviadoHSACK + "\nNumero rafaga: " + iNumeroRafaga;
    }

    /**
    * Clona el registro
    */
    public Object clone() {
        RegistroAsentimientos regAsentResult = new RegistroAsentimientos(0);
        regAsentResult.treeMapEnviadoACK = (TreeMap) this.treeMapEnviadoACK.clone();
        regAsentResult.treeMapEnviadoHACK = (TreeMap) this.treeMapEnviadoHACK.clone();
        regAsentResult.treeMapEnviadoHSACK = (TreeMap) this.treeMapEnviadoHSACK.clone();
        regAsentResult.bEnviadoHSACK = this.bEnviadoHSACK;
        regAsentResult.iNumeroRafaga = this.iNumeroRafaga;
        return regAsentResult;
    }
}
