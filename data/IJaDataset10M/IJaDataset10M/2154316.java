package tp2.vista.modelo.objetosVivos;

import tp2.modelo.especificaciones.ValoresDeNaves;
import tp2.vista.modelo.ArchivosDeImagenes;
import tp2.vista.modelo.FactoresDeImagenes;
import ar.uba.fi.algo3.titiritero.vista.Imagen;

public class ImagenBombardero extends Imagen {

    private static ImagenBombardero imagenPrincipal;

    private static ImagenEscalable imagenEscalable;

    private ImagenBombardero() {
        this.setNombreArchivoImagen(ArchivosDeImagenes.IMAGEN_BOMBARDERO);
    }

    private ImagenBombardero(Imagen imagen) {
        super(imagen);
    }

    private static void crearImagenBombardero() {
        imagenPrincipal = new ImagenBombardero();
        imagenEscalable = new ImagenEscalable(imagenPrincipal);
    }

    public static ImagenBombardero nuevaImagen(double escala) {
        if (imagenPrincipal == null) {
            crearImagenBombardero();
        }
        int tamanioImagen = (int) (FactoresDeImagenes.FACTOR_BOMBARDERO * 2 * ValoresDeNaves.bombarderoTamanio * escala);
        return new ImagenBombardero(imagenEscalable.getImagenEscalada(tamanioImagen));
    }
}
