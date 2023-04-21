package Utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Barco implements Serializable {

    private float capacidadCarga, espacioLibre;

    private int capacidadContenedores, espacioContenedores;

    private String naviera;

    private String pais;

    private Ruta miRuta;

    private boolean finalizadoRuta;

    private ArrayList<Contenedor> misContenedores;

    /**
     * Constructor: Inicializa una instancia del objeto Barco con los datos recibidos como parámetros.
     * Dicho barco se en el mar y está descargado.
     * @param capacidadCarga Toneladas que puede soportar el barco. (Float)
     * @param capacidadContenedores Número entero de contenedores que puede llevar el barco.
     * @param naviera String con el nombre de la naviera al cual pertenece el barco.
     * @param pais String con el nombre del país de donde proviene la naviera.
     * @param miRuta Ruta que sigue el barco.
     */
    public Barco(float capacidadCarga, int capacidadContenedores, String naviera, String pais, Ruta miRuta) {
        this.capacidadCarga = capacidadCarga;
        this.capacidadContenedores = capacidadContenedores;
        this.naviera = naviera;
        this.pais = pais;
        this.miRuta = miRuta;
        this.misContenedores = new ArrayList<Contenedor>();
        this.espacioContenedores = capacidadContenedores;
        this.espacioLibre = capacidadCarga;
        this.finalizadoRuta = false;
    }

    /**
     * getMiRuta
     * Devuelve la ruta del barco.
     * @return miRuta Instancia de la clase Ruta, la cual representa la ruta del barco.
     */
    public Ruta getMiRuta() {
        return miRuta;
    }

    /**
     * getNaviera
     * Devuelve la naviera del barco.
     * @return naviera String con el nombre de la naviera dueña del barco.
     */
    public String getNaviera() {
        return naviera;
    }

    /**
     * getPais
     * Devuelve el país del cual viene la naviera dueña del barco.
     * @return pais Pais de donde es la naviera dueña del barco.
     */
    public String getPais() {
        return pais;
    }

    public boolean isFinalizadoRuta() {
        return finalizadoRuta;
    }

    public void haFinalizadoRuta() {
        this.finalizadoRuta = true;
    }

    /**
     * descargar: Permite al barco descargar todos los contenedores que tiene como destino el puerto al cual se dirige.
     * @param puerto Puerto al cual arribará el barco.
     */
    private void descargar(Puerto puerto) {
        ArrayList<Contenedor> contenedoresADescargar = new ArrayList<Contenedor>();
        if (misContenedores.size() > 0) {
            for (Contenedor contenedor : misContenedores) {
                if (contenedor.getPuertoDestino().equals(puerto)) {
                    contenedoresADescargar.add(contenedor);
                    espacioLibre += contenedor.getPeso();
                    espacioContenedores++;
                }
            }
            for (Contenedor contenedor : contenedoresADescargar) {
                this.misContenedores.remove(contenedor);
                puerto.getMisContenedores().add(contenedor);
            }
        }
    }

    /**
     * cargar: Permite que un barco cargue todos los contenedores que pueda
     * según su capacidad dentro de sí mismo, siempre y cuando, el destino de
     * dichos contenedores se encuentren sobre la ruta restante del barco.
     * @param puerto Puerto en el cual se encuentra el barco al momento de cargar los contenedores.
     */
    private void cargar(Puerto puerto) {
        ArrayList<Contenedor> contenedoresPorCargar = new ArrayList<Contenedor>();
        if (!puerto.getMisContenedores().isEmpty()) {
            for (int numContenedor = 0; numContenedor < puerto.getMisContenedores().size(); numContenedor++) {
                Contenedor contenedor = puerto.getMisContenedores().get(numContenedor);
                for (int numEscala = miRuta.getActualEscala() + 1; numEscala < miRuta.getEscalas().size(); numEscala++) {
                    Escala escala = miRuta.getEscalas().get(numEscala);
                    if ((escala.getPuerto().equals(contenedor.getPuertoDestino())) && espacioLibre >= contenedor.getPeso() && espacioContenedores > 0) {
                        contenedoresPorCargar.add(contenedor);
                        espacioLibre -= contenedor.getPeso();
                        espacioContenedores -= 1;
                    }
                }
            }
            for (Contenedor contenedor : contenedoresPorCargar) {
                this.misContenedores.add(contenedor);
                puerto.getMisContenedores().remove(contenedor);
            }
        }
    }

    /**
     * arriboSiguientePuerto: Hace que el barco llegue al siguiente Puerto sobre su ruta, descargue
     * y luego cargue en dicho puerto, para luego retirarse de él en dirección del siguiente puerto.
     * Asimismo, se registra la fecha real de llegada a dicho puerto.
     * @param fechaReal Fecha en la cual llegó el barco al puerto.
     * @return true Si se completo el arribo al siguienete puerto
     * @return false Si no se completó el arribo al siguiente puerto, por estar el barco en el final de su ruta.
     */
    public boolean arriboSiguientePuerto(Date fechaReal) {
        if (miRuta.getActualEscala() < miRuta.getEscalas().size()) {
            this.descargar(miRuta.getEscalas().get(miRuta.getActualEscala()).getPuerto());
            this.cargar(miRuta.getEscalas().get(miRuta.getActualEscala()).getPuerto());
            this.miRuta.getEscalas().get(miRuta.getActualEscala()).setFechaReal(fechaReal);
            this.miRuta.setActualEscala(miRuta.getActualEscala() + 1);
            return true;
        } else {
            return false;
        }
    }

    /**
     * contenedoresDestinadosA: Devuelve un ArrayList con los contenedores que se encuentran en un barco y que tienen como destino al puerto recibido como parámetro.
     * @param puerto Puerto sobre el cual se desea realizar la consulta.
     * @return losContenedores ArrayList de contenedores que se encuentran dentro del barco dirigidos hacia el puerto ingresado como parámetro.
     */
    private ArrayList<Contenedor> contenedoresDestinadosA(Puerto puerto) {
        ArrayList<Contenedor> losContenedores = new ArrayList<Contenedor>();
        for (Contenedor contenedor : misContenedores) {
            if (contenedor.getPuertoDestino().equals(puerto)) {
                losContenedores.add(contenedor);
            }
        }
        return losContenedores;
    }

    /**
     * numContenedoresDestinadosA
     * Devuelve el número de contenedores ya cargados en un barco cuyo destino
     * es un puerto específico.
     * @param puerto Puerto sobre el cual se desea consultar el número de contenedores
     * destinados a él.
     * @return numero Número de contenedores destinados al puerto.
     */
    public int numContenedoresDestinadosA(Puerto puerto) {
        int numero;
        numero = contenedoresDestinadosA(puerto).size();
        return numero;
    }

    /**
     * contenedoresSegunRuta: Muestra todos los puertos que se encuentran dentro de la ruta por recorrer
     * del barco, así como el nombre de todos los contenedores que se desacargarán en ellos.
     * @return msj String con los puertos restantes y los contenedores ha descargar en cada uno de ellos.
     */
    public String contenedoresSegunRuta() {
        String msj;
        if (miRuta.getActualEscala() < miRuta.getEscalas().size()) {
            msj = "Ruta restante:";
            for (int i = miRuta.getActualEscala(); i < miRuta.getEscalas().size(); i++) {
                int ordinal = i + 1;
                msj += "\n" + ordinal + ") Puerto: " + miRuta.getEscalas().get(i).getPuerto().getNombre();
                if (!contenedoresDestinadosA(miRuta.getEscalas().get(i).getPuerto()).isEmpty()) {
                    msj += "\n\tContenedores a descargar:";
                    for (Contenedor contenedor : contenedoresDestinadosA(miRuta.getEscalas().get(i).getPuerto())) {
                        msj += "\n\t" + contenedor.getNombre();
                    }
                } else {
                    msj += "\n\tNo hay contenedores por descargar en este puerto.";
                }
                msj += "\n";
            }
        } else {
            msj = "El barco del capitán " + miRuta.getCapitan() + ", perteneciente a " + this.naviera + ", " + this.pais + " ha llegado al final de su ruta.";
        }
        return msj;
    }

    /**
     * toString
     * Devuelve un String con datos importantes del barco.
     * @return mensaje String con la información básica de la identificación del barco.
     */
    @Override
    public String toString() {
        String mensaje = "Barco de " + naviera + ", " + pais + " dirigido por el capitán " + miRuta.getCapitan();
        return mensaje;
    }
}
