package pooproyecto01grupo092011;

import java.util.ArrayList;

/**
 * La clase Barco provee los atributos y metodos necesarios para trabajar con
 * objetos de tipo Barco.Todos los barcos son implementados como instancias de
 * esta clase.
 * @author Grupo No. 9, Seccion:10
 * @version 1.1
 */
public class Barco {

    private String naviera_Propietaria, pais_Naviera, nombre_barco;

    private int capacidad_Contenedores, cantidad_Contenedores_Actual;

    private float capacidad_Carga, carga_Actual;

    private ArrayList<Contenedor> contenedor = new ArrayList<Contenedor>();

    private String pais_actual;

    private Ruta ruta;

    /**
     * Contructor, inicizaliza los valores de las variables de instancia.
     * @param  nombre : Nombre del barco.
     * @param nombreNavierax : Nombre de la la naviera propietaria.
     * @param paisNavierax : Nombre del pais de la naviera.
     * @param capacidadCargax : Capacidad maxima en toneladas metricas que puede
     *        transportar el barco.
     * @param capacidadContenedoresx : Cantidad maxima de contenedores que puede
     *        transportar el barco.
     */
    public Barco(String nombre, String nombreNavierax, String paisNavierax, float capacidadCargax, int capacidadContenedoresx) {
        naviera_Propietaria = nombreNavierax;
        pais_Naviera = paisNavierax;
        capacidad_Carga = capacidadCargax;
        capacidad_Contenedores = capacidadContenedoresx;
        nombre_barco = nombre;
        cantidad_Contenedores_Actual = 0;
        pais_actual = paisNavierax;
        carga_Actual = 0;
    }

    /**
     * Retorna el nombre del barco.
     * @return nombre_barco : El nombre que identifica al barco.
     */
    public String getNombre_barco() {
        return nombre_barco;
    }

    /**
     * Retorna un contenedor en una posicion especifica.
     * @param index el indicador de la posicion del contenedor.
     * @return contenedor.get(index) : retorna el contenedor que se encuentra en tal posicion.
     */
    public Contenedor getContenedor(int index) {
        return contenedor.get(index);
    }

    /**
     * Retorna la lista completa de contenedores que hay en un barco.
     * @return contenedor.get(index) : retorna el contenedor que se encuentra en tal posicion.
     */
    public ArrayList<Contenedor> getContenedor() {
        return contenedor;
    }

    /**
     * Retorna el peso de la carga actual que hay en el barco.
     * @return carga_Actual : retorna la carga actual que contiene el barco.
     */
    public float getCargaActual() {
        return carga_Actual;
    }

    /**
     * Elimina un contenedor de la lista de contenedores y modifica el peso de la carga actual del barco.
     * @param x : recibe la posicion en la que se encuentra el contenedor a descargar
     */
    public void descargarContenedor(int x) {
        cantidad_Contenedores_Actual = cantidad_Contenedores_Actual - 1;
        carga_Actual = carga_Actual - contenedor.get(x).getPesoActual();
        contenedor.remove(x);
    }

    /**
     * Retorna la cantidad de contenedores que hay en el barco.
     * @return cantidad_Contenedores_Actual : retorna la cantidad de contenedores cargados.
     */
    public int getCantidad_Contenedores_Actual() {
        return cantidad_Contenedores_Actual;
    }

    /**
     * Retorna el nombre de la naviera propietaria.
     * @return naviera_Propietaria : Nombre de la naviera propietaria.
     */
    public String getNaviera_propietaria() {
        return naviera_Propietaria;
    }

    /**
     * Retorna el pais de la naviera del barco.
     * @return pais_Naviera : Nombre del pais de la naviera del barco.
     */
    public String getPaisNaviera() {
        return pais_Naviera;
    }

    /**
     * Retorna el peso disponible que tiene el barco.
     * @return peso_disponible : retorna espacio disponible para agregar contenedores de cierto peso..
     */
    public float getPesoDisponible() {
        float peso_disponible;
        peso_disponible = capacidad_Carga - carga_Actual;
        return peso_disponible;
    }

    /**
     * Retorna la capacidad de contenedores que tiene el barco.
     * @return peso_disponible : retorna espacio disponible para agregar contenedores.
     */
    public int getEspacioDisponibleContenedores() {
        int toReturn;
        toReturn = capacidad_Contenedores - cantidad_Contenedores_Actual;
        return toReturn;
    }

    /**
     * Agrega una ruta a un barco especifico.
     * @param name_capitan nombre del capitán del barco de ruta.
     */
    public void agregar_ruta(String name_capitan) {
        ruta = new Ruta(name_capitan);
    }

    /**
     * Se agrega un contenedor al barco y se  modifica el peso del barco. 
     * @param x Contenedor que se agregara.
     */
    public void agregar_contenedor(Contenedor x) {
        contenedor.add(x);
        cantidad_Contenedores_Actual += 1;
        carga_Actual += x.getPesoActual();
    }

    /**
     * Se obtiene el objeto ruta. 
     * @return ruta ruta de un barco.
     */
    public Ruta getRuta() {
        return ruta;
    }

    /**
     * Se cambia el pais actual del barco. 
     * @param pais : nombre del pais a asignar al barco.
     */
    public void setPais_Actual(String pais) {
        pais_actual = pais;
    }

    /**
     * Retorna el pais actual del barco.
     * @return pais_actual retorna el pais actual del barco
     */
    public String getPaisActual() {
        return pais_actual;
    }

    /**
      * Retorna la capacidad de contenedores que tiene el barco.
     * @return capacidad_contenedores devuelve la capacidad de contenedores que tiene el barco
     */
    public int getCapacidadContenedores() {
        return capacidad_Contenedores;
    }

    /**
     *Se demuestra el estado actual del os objetos de la clase. 
     * Devuelve el estado del barco.
     * @return toReturn : Datos generales del Barco.
     */
    @Override
    public String toString() {
        String toReturn;
        toReturn = "Nombre del barco: " + nombre_barco;
        toReturn += "\nNaviera Propietaria: " + naviera_Propietaria;
        toReturn += "\nPais de la Naviera: " + pais_Naviera;
        toReturn += "\nPais en el que se encuentra: " + pais_actual;
        toReturn += "\nCapacidad de Carga: " + capacidad_Carga;
        toReturn += "\nCantidad Máxima de Contenedores" + capacidad_Contenedores;
        toReturn += "\nCantida de Contenedores Cargados" + cantidad_Contenedores_Actual;
        return toReturn;
    }
}
