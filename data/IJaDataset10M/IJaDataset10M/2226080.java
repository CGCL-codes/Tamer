package Redes.IPv4.ARP;

import Redes.Direccion;
import java.util.Vector;

/**
 * Cach� ARP
 */
public class CacheARP {

    /**
	 * Tipos de las direcciones de protocolo de red (IPv, IPv6, ...)
	 */
    Vector Protocolos;

    /**
	 * Listas de direcciones del protocolo (IP)
	 */
    Vector DireccionesProtocolo;

    /**
	 * Lista de direcciones asociadas a las direcciones IP
	 */
    Vector DireccionesFisicas;

    /**
	 * Constructor
	 */
    public CacheARP() {
        Protocolos = new Vector();
        DireccionesProtocolo = new Vector();
        DireccionesFisicas = new Vector();
    }

    /**
     * Devuelve una direccion fisica asociada a una direccion de protocolo de red
     * @param dirProtocolo Direccion de protocolo
     * @return Direccion asociada o null si no existe
     */
    public Direccion getDireccionFisica(Direccion dirProtocolo) {
        Direccion dirAux = null;
        Direccion dirRetorno = null;
        boolean encontrada = false;
        if (dirProtocolo != null) {
            for (int i = 0; i < DireccionesProtocolo.size() && !encontrada; i++) {
                dirAux = (Direccion) DireccionesProtocolo.get(i);
                if (dirProtocolo != null && dirProtocolo.equals(dirAux)) {
                    if (dirProtocolo.Tipo() == dirAux.Tipo()) {
                        dirRetorno = (Direccion) DireccionesFisicas.get(i);
                        encontrada = true;
                    }
                }
            }
        }
        return (dirRetorno);
    }

    /**
     * Inserta una entrada en la cache ARP
     * @param protocolo Tipo de protocolo usado por la direccion de red
     * @param dirProtocolo Direccion del nivel de red
     * @param dirFisica Direccion ficica asociada a la direccion de red
     */
    public void NuevaEntrada(int protocolo, Direccion dirProtocolo, Direccion dirFisica) {
        Protocolos.add(new Integer(protocolo));
        DireccionesProtocolo.add(dirProtocolo);
        DireccionesFisicas.add(dirFisica);
    }

    /**
     * Elimina una entrada de la cache ARP
     * @param numEntrada
     */
    public void EliminaEntrada(int numEntrada) {
        if (numEntrada >= 0 && numEntrada < DireccionesProtocolo.size()) {
            Protocolos.remove(numEntrada);
            DireccionesProtocolo.remove(numEntrada);
            DireccionesFisicas.remove(numEntrada);
        }
    }

    /**
     * Comprueba si la pareja protocolo/dirProtocolo existe en la cache
     * @param protocolo Identificador de protocolo
     * @param dirProtocolo Direccion del protocolo
     * @return Cierto si la pareja indicada existe en la tabla
     */
    public boolean Existe(int protocolo, Direccion dirProtocolo) {
        boolean existe = false;
        int protoAux;
        Direccion dirAux;
        for (int i = 0; i < Protocolos.size() && !existe; i++) {
            protoAux = ((Integer) Protocolos.get(i)).intValue();
            if (protoAux == protocolo) {
                dirAux = (Direccion) DireccionesProtocolo.get(i);
                if (dirAux.equals(dirProtocolo)) existe = true;
            }
        }
        return (existe);
    }

    /**
     * Actualiza la direccion fisica asociada a la pareja protocolo/dirProtocolo
     * @param protocolo Identificador del protocolo
     * @param dirProtocolo Direccion del protocolo
     * @param dirFisica Direccion fisica
     */
    public void Actualiza(int protocolo, Direccion dirProtocolo, Direccion dirFisica) {
        boolean actualizada = false;
        int protoAux;
        Direccion dirAux;
        for (int i = 0; i < Protocolos.size() && !actualizada; i++) {
            protoAux = ((Integer) Protocolos.get(i)).intValue();
            if (protoAux == protocolo) {
                dirAux = (Direccion) DireccionesProtocolo.get(i);
                if (dirAux.equals(dirProtocolo)) {
                    DireccionesFisicas.set(i, dirFisica);
                    actualizada = true;
                }
            }
        }
    }
}
