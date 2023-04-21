package cliente.dibujos;

import java.awt.*;
import java.awt.image.*;
import java.net.URL;
import javax.swing.JPanel;
import cliente.RotateFilter;

/**
 * Implementaci�n del patr�n AbstractFactory para fabricar cartas. Las verdaderas f�bricas, que sirven para fabricar
 * las cartas de las distintas barajas, heredan de esta clase.
 * @author usuario
 *
 */
public abstract class FabricaCartas {

    protected JPanel panel;

    protected ImageProducer ip;

    protected MediaTracker tracker;

    public int ANCHO;

    public int ALTO;

    protected FabricaCartas() {
    }

    public FabricaCartas(JPanel panel) {
        super();
        this.CambiarTamano();
        this.panel = panel;
        URL url = ClassLoader.getSystemResource(DarArchivoCartas());
        Image imagen = Toolkit.getDefaultToolkit().getImage(url);
        tracker = new MediaTracker(panel);
        tracker.addImage(imagen, 0);
        try {
            tracker.waitForID(0);
        } catch (Exception e) {
            System.out.println(e);
        }
        ip = imagen.getSource();
    }

    /**
	 * Cambia el tama�o de la carta, cambiando el valor de las variables ALTO y ANCHO. 
	 */
    protected abstract void CambiarTamano();

    /**
	 * Da el archivo donde est� guardada la imagen de la carta. La idea es que en el constructor est� todo el c�digo
	 * para crear los gr�ficos de las cartas. La �nica diferencia entre una carta espa�ola y una francesa es el archivo.
	 * Lo mismo pasa al producir la imagen del reverso.
	 * @return archivo donde est� la carta.
	 */
    protected abstract String DarArchivoCartas();

    /**
	 * Devuelve la imagen del frente de una carta espa&ntilde;ola
	 * 
	 * @param quecarta c�digo de la carta que hay que crear
	 * @return imagen de la carta creada, para que la carta se la guarde.
	 */
    protected Image ProducirImagen(int quecarta) {
        int nx = (quecarta % 10) * ANCHO;
        int ny = (quecarta / 10) * ALTO;
        Image nuevaCarta = panel.createImage(new FilteredImageSource(ip, new CropImageFilter(nx, ny, ANCHO, ALTO)));
        tracker.addImage(nuevaCarta, 1);
        return nuevaCarta;
    }

    /**
	 * Devuelve la imagen rotada del frente de una carta espa&ntilde;ola.
	 * 
	 * @param quecarta
	 * @return Imagen rotada de la carta pedida (por n�mero)
	 */
    protected Image ProducirImagenRotada(int quecarta) {
        Image imagenOrigen = ProducirImagen(quecarta);
        Image ImagenResultado = panel.createImage(new FilteredImageSource(imagenOrigen.getSource(), new RotateFilter(Math.PI / 2)));
        return ImagenResultado;
    }

    /**
	 * Produce la imagen rotada del reverso de una carta espa&ntilde;ola.
	 * 
	 * @return Imagen del reverso rotado
	 */
    protected Image ProducirReversoRotado() {
        Image imagenOrigen = ProducirReverso();
        Image imagenResultado = panel.createImage(new FilteredImageSource(imagenOrigen.getSource(), new RotateFilter(Math.PI / 2)));
        return imagenResultado;
    }

    /**
	 * Produce la imagen del reverso de una carta.
	 * 
	 * @return la imagen del reverso de la carta
	 */
    public Image ProducirReverso() {
        int nx = this.DarXReverso();
        int ny = this.DarYReverso();
        Image nuevaCarta = panel.createImage(new FilteredImageSource(ip, new CropImageFilter(nx, ny, ANCHO, ALTO)));
        tracker.addImage(nuevaCarta, 1);
        return nuevaCarta;
    }

    /**
	 * Da la X del reverso de la carta. La idea es que se calcule el X y el Y del reverso que necesita la funci�n para
	 * producir el reverso.
	 * @return coordenada X donde tiene que estar la imagen que representa al reverso dentro de la imagen que contiene todas las cartas
	 */
    protected abstract int DarXReverso();

    /**
	 * Da la Y del reverso de la carta. La idea es que se calcule el X y el Y del reverso que necesita la funci�n para
	 * producir el reverso.
	 * @return coordenada Y donde tiene que estar la imagen que representa al reverso dentro de la imagen que contiene todas las cartas.
	 */
    protected abstract int DarYReverso();
}
