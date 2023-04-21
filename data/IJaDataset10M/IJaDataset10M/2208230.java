package cliente;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.Semaphore;

/**
 * Ancestro de todos los elementos que forman la mesa: los puestos y la mesa
 * propiamente dicha.
 * 
 * @author Agust&iacute;n Villafa&ntildee
 * 
 */
public abstract class ElementoMesa {

    protected int esquinaSuperiorX;

    protected int esquinaSuperiorY;

    protected int tamanoX;

    protected int tamanoY;

    protected int esquinaInferiorX;

    protected int esquinaInferiorY;

    protected final int ALTO = 96;

    protected final int ANCHO = 60;

    protected int Xclick;

    protected int Yclick;

    protected LinkedList<CartaEspanolaGrafica> misCartas;

    protected boolean hiceclick;

    protected CartaGrafica cartaArrastrada;

    protected int lugarCartaArrastrada;

    protected Semaphore semaforo;

    public ElementoMesa() {
        semaforo = new Semaphore(1);
        misCartas = new LinkedList<CartaEspanolaGrafica>();
    }

    public ElementoMesa(int esquinaSuperiorX, int esquinaSuperiorY, int tamanoX, int tamanoY) {
        super();
        this.esquinaSuperiorX = esquinaSuperiorX;
        this.esquinaSuperiorY = esquinaSuperiorY;
        this.esquinaInferiorX = esquinaSuperiorX + tamanoX;
        this.esquinaInferiorY = esquinaSuperiorY + tamanoY;
    }

    /**
	 * Detecta si hicieron click en este elemento: devuelve True si las
	 * coordenadas informadas est&aacute;n dentro del recuadro de este elemento.
	 * 
	 * @param dondeX
	 * @param dondeY
	 * @return true si se hizo click sobre esta carta, para las coordenadas especificadas.
	 */
    public boolean Click(int dondeX, int dondeY) {
        if (dondeX >= esquinaSuperiorX && dondeX <= esquinaInferiorX && dondeY >= esquinaSuperiorY && dondeY <= esquinaInferiorY) return true; else return false;
    }

    /**
	 * Devuelve el numero de carta sobre la que se hizo click. Es necesario
	 * porque no se sabe si se le hizo click en una carta de la mesa o no. Si se
	 * hizo click en ese sector de la pantalla, lo que se sabe con la funcion
	 * click, se pregunta cual es la carta que se hizo click. Como cero es un
	 * valor valido, se optara por devolver -1
	 * 
	 * @param dondeX
	 *            donde hizo click, en X
	 * @param dondeY
	 *            donde hizo click, en Y
	 * @return n�mero de la carta donde se hizo click que est� en las coordenadas especificadas
	 */
    public int CartaClick(int dondeX, int dondeY) {
        try {
            semaforo.acquire();
            int cartaClick = -1;
            ListIterator<CartaEspanolaGrafica> cartas = misCartas.listIterator();
            while (cartas.hasNext()) {
                int cualcarta = cartas.nextIndex();
                CartaGrafica c = cartas.next();
                if (c.Click(dondeX, dondeY)) cartaClick = cualcarta;
            }
            semaforo.release();
            return cartaClick;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
	 * Invocado cuando se arrastra una carta, asigna el lugar a la carta cuando se arrastra, 
	 * as�, si no es entregada, vuelve al lugar que le corresponde.
	 * @param numero
	 */
    public void cambiarOrigenArrastre(int numero) {
        lugarCartaArrastrada = numero;
    }

    /**
	 * Implementado por los ancestros para reaccionar ante la llegada de una
	 * carta.
	 * 
	 * @param c
	 * @param posicion
	 */
    public abstract void RecibirCarta(CartaEspanolaGrafica c, int posicion);

    /**
	 * Recibe una carta que se estaba arrastrando pero se solt� sin llevar
	 * a ninguna parte.
	 * @param c
	 */
    public void RecibirCartaArrastrada(CartaEspanolaGrafica c) {
        RecibirCarta(c, lugarCartaArrastrada);
    }

    /**
	 * Entrega una carta y la remueve de la lista.
	 * 
	 * @param cual
	 *            n&uacute;mero de carta a entregar.
	 * @return
	 *            carta que se va a entregar a quien la pida.
	 */
    public CartaEspanolaGrafica EntregarCarta(int cual) {
        try {
            semaforo.acquire();
            CartaEspanolaGrafica c = misCartas.remove(cual);
            semaforo.release();
            return c;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
	 * Devuelve la cantidad de cartas que se tienen en la mano.
	 * 
	 * @return cantidad de cartas de la mano
	 */
    public int CantidadCartas() {
        return misCartas.size();
    }

    /**
	 * Borra una carta del puesto.
	 * @param cual
	 */
    public void BorrarCarta(int cual) {
        try {
            semaforo.acquire();
            misCartas.remove(cual);
            semaforo.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
