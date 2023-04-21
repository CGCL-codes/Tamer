package tp2.modelo;

import java.util.LinkedList;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import tp2.auxiliares.Point;
import tp2.modelo.excepciones.ComposicionIncompleta;
import tp2.modelo.excepciones.ObjetoDesconocido;
import tp2.modelo.excepciones.ValorInvalido;
import tp2.modelo.excepciones.VueloNoIniciado;
import tp2.persistencia.GeneradorXml;
import tp2.persistencia.IGuardable;
import tp2.persistencia.ReconstructorDesdeXml;

public class VueloCompuesto extends Vuelo {

    private LinkedList<Vuelo> vuelos;

    private LinkedList<Double> trayectoriasDeVuelo;

    private Vuelo vueloActual;

    private Double trayectoriaDeVueloActual;

    public VueloCompuesto(ObjetoVolador objetoVolador) {
        super(objetoVolador);
        vuelos = new LinkedList<Vuelo>();
        trayectoriasDeVuelo = new LinkedList<Double>();
        vueloActual = null;
        trayectoriaDeVueloActual = null;
    }

    public VueloCompuesto() {
        super();
    }

    @Override
    public Point avanzarDurante(double unTiempo) {
        if (!this.estaIniciado()) {
            throw new VueloNoIniciado("El vuelo no ha iniciado.");
        }
        double velocidad = this.getObjetoVolador().getVelocidad();
        double tiempoRestante = unTiempo;
        Point desplazamiento = new Point(0, 0);
        double tiempoParcial;
        while (tiempoRestante > 0) {
            if ((velocidad * tiempoRestante < this.trayectoriaDeVueloActual) | (this.vuelos.isEmpty())) {
                tiempoParcial = tiempoRestante;
            } else {
                tiempoParcial = this.trayectoriaDeVueloActual / velocidad;
            }
            desplazamiento = desplazamiento.sumarCon(vueloActual.avanzarDurante(tiempoParcial));
            tiempoRestante -= tiempoParcial;
            this.trayectoriaDeVueloActual -= velocidad * tiempoParcial;
            if (tiempoRestante > 0) {
                this.vueloActual = this.vuelos.poll();
                this.trayectoriaDeVueloActual = this.trayectoriasDeVuelo.poll();
                this.vueloActual.iniciar();
            }
        }
        return desplazamiento;
    }

    @Override
    public void iniciar() {
        if (this.vueloActual == null) {
            throw new ComposicionIncompleta("No se defini� ning�n vuelo.");
        }
        super.iniciar();
        this.vueloActual.iniciar();
    }

    public void agregarVuelo(Vuelo vuelo, double longitudTrayectoria) {
        if (longitudTrayectoria <= 0) {
            throw new ValorInvalido("La trayectoria de vuelo tiene que ser mayor que cero.");
        }
        if (this.getObjetoVolador() != vuelo.getObjetoVolador()) {
            throw new ObjetoDesconocido("El vuelo recibido pertenece a un objeto volador desconocido.");
        }
        if (this.vueloActual == null) {
            this.vueloActual = vuelo;
            this.trayectoriaDeVueloActual = longitudTrayectoria;
        } else {
            this.vuelos.offer(vuelo);
            this.trayectoriasDeVuelo.offer(longitudTrayectoria);
        }
    }

    @Override
    public Element guardar(Element contenedor) {
        super.guardar(contenedor);
        contenedor.appendChild(GeneradorXml.generarElementoDe(vuelos, "vuelos"));
        contenedor.appendChild(GeneradorXml.generarElementoDe(trayectoriasDeVuelo, "trayectoriasDeVuelo"));
        contenedor.appendChild(GeneradorXml.generarElementoDe(vueloActual, "vueloActual"));
        contenedor.appendChild(GeneradorXml.generarElementoDe(trayectoriaDeVueloActual, "trayectoriaDeVueloActual"));
        return contenedor;
    }

    @SuppressWarnings("unchecked")
    @Override
    public IGuardable cargar(Map<String, Node> atributos) {
        super.cargar(atributos);
        this.vuelos = (LinkedList<Vuelo>) ReconstructorDesdeXml.generarObjeto(atributos.get("vuelos"));
        this.trayectoriasDeVuelo = (LinkedList<Double>) ReconstructorDesdeXml.generarObjeto(atributos.get("trayectoriasDeVuelo"));
        this.vueloActual = (Vuelo) ReconstructorDesdeXml.generarObjeto(atributos.get("vueloActual"));
        this.trayectoriaDeVueloActual = (Double) ReconstructorDesdeXml.generarObjeto(atributos.get("trayectoriaDeVueloActual"));
        return this;
    }
}
