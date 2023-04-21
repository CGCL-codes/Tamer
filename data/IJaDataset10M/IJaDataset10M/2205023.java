package proyecto.twemoi;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Almacena información para la comunicación entre una actividad y otra. Es
 * implementado como un Bundle propio en Android.
 * 
 * @author moises
 * 
 */
public class MensajeIntercambio implements Serializable {

    private static final long serialVersionUID = 3940646209959943718L;

    private ArrayList<Tweet> listaTweet;

    private twimoipro actividad;

    /**
	 * Constructor de un mensaje de intercambio
	 * @param lista
	 * @param a
	 */
    public MensajeIntercambio(ArrayList<Tweet> lista, twimoipro a) {
        this.setListaTweet(lista);
        this.setActividad(a);
    }

    /**
	 * Establece la lista de los tweets
	 * @param listaTweet
	 */
    public void setListaTweet(ArrayList<Tweet> listaTweet) {
        this.listaTweet = listaTweet;
    }

    /**
	 * Devuelve la lista de Tweets
	 * @return
	 */
    public ArrayList<Tweet> getListaTweet() {
        return listaTweet;
    }

    /**
	 * Establece una actividad
	 * @param actividad
	 */
    public void setActividad(twimoipro actividad) {
        this.actividad = actividad;
    }

    /**
	 * Devuelve la actividad
	 * @return
	 */
    public twimoipro getActividad() {
        return actividad;
    }
}
