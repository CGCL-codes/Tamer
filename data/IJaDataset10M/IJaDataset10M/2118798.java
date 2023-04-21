package fiuba.algo3.juego.modelo;

import java.io.Serializable;
import java.util.Random;
import fiuba.algo3.juego.modelo.excepciones.ItemNoDisponibleError;
import fiuba.algo3.juego.modelo.excepciones.NaveDestruidaError;
import fiuba.algo3.juego.modelo.excepciones.SuperposicionNavesError;

public class Bombardero extends NaveNoOperable implements Serializable {

    private static final long serialVersionUID = -7505650596285382873L;

    double haciaDer, haciaIzq;

    static int cantidadMoverLateral = 10;

    static double cantidadPaso = 0.5;

    public Bombardero(Punto punto, Plano plano) throws SuperposicionNavesError, NaveDestruidaError {
        super();
        puntos = 30;
        energia = 50;
        haciaDer = 0;
        haciaIzq = 0;
        esOperable = false;
        rectangulo = new Rectangulo(44, 60, punto);
        estaDestruida = false;
        fueraDelPlano = false;
        this.determinarPlano(plano);
        if (this.seSuperponeConOtraNave()) {
            throw new SuperposicionNavesError("La posicion esta ocupada");
        }
        plano.agregarNave(this);
        plano.agregarObjetoNuevo(this);
    }

    public Item dejarArma() throws ItemNoDisponibleError {
        if (!this.estaDestruida) {
            throw new ItemNoDisponibleError("El bombardero aun no esta destruido, no puede dejar armas");
        }
        Item itemDejado = new ArmaAbandonada(this.devolverPunto(), this.plano);
        return itemDejado;
    }

    @Override
    public void modificarEnergia(int cantidad) {
        energia = (energia + cantidad);
        if (energia <= 0) {
            try {
                this.destruirse();
            } catch (Exception error) {
            }
            try {
                this.dejarArma();
            } catch (ItemNoDisponibleError error) {
                return;
            }
        }
    }

    public void mover() throws SuperposicionNavesError {
        if (haciaDer < cantidadMoverLateral) {
            Punto nuevoPunto = new Punto(this.devolverPunto().getX() + cantidadPaso, this.devolverPunto().getY() - cantidadPaso);
            this.cambiarPosicion(nuevoPunto);
            haciaDer = (haciaDer + cantidadPaso);
        } else {
            if (haciaIzq <= cantidadMoverLateral) {
                Punto nuevoPunto = new Punto(this.devolverPunto().getX() - cantidadPaso, this.devolverPunto().getY() - cantidadPaso);
                this.cambiarPosicion(nuevoPunto);
                haciaIzq = (haciaIzq + cantidadPaso);
            } else {
                haciaIzq = 0;
                haciaDer = 0;
            }
        }
        if (this.seSuperponeConOtraNave()) {
            throw new SuperposicionNavesError("La posicion ya esta ocupada.");
        }
        this.estaFueraDelPlano();
    }

    public void moverAlternativo() throws SuperposicionNavesError {
        if (haciaDer <= cantidadMoverLateral) {
            Punto nuevoPunto = new Punto(this.devolverPunto().getX() - cantidadPaso, this.devolverPunto().getY() - cantidadPaso);
            this.cambiarPosicion(nuevoPunto);
            haciaDer = cantidadMoverLateral + cantidadPaso;
            haciaIzq = cantidadPaso;
        } else {
            Punto nuevoPunto = new Punto(this.devolverPunto().getX() + cantidadPaso, this.devolverPunto().getY() - cantidadPaso);
            this.cambiarPosicion(nuevoPunto);
            haciaDer = cantidadPaso;
            haciaIzq = cantidadMoverLateral + cantidadPaso;
        }
        if (this.seSuperponeConOtraNave()) {
            throw new SuperposicionNavesError("La posicion ya esta ocupada");
        }
        this.estaFueraDelPlano();
    }

    public void disparar() {
        if (velocidadDisparoCont == velocidadDisparo) {
            Random generadorRandom = new Random();
            int i = generadorRandom.nextInt(9);
            if (i <= 2) {
                this.dispararCohete();
            } else if (i == 3) {
                this.dispararTorpedoRastreadorHacia(this.plano.getAlgo42());
            } else {
                this.dispararLaser();
            }
            velocidadDisparoCont = 0;
        }
    }

    public int devolverCantidadMoverLateral() {
        return cantidadMoverLateral;
    }
}
