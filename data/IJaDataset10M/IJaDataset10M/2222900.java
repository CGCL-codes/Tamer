package tp2.vista.modelo.objetosVivos;

import tp2.modelo.especificaciones.ValoresDeNaves;
import tp2.vista.modelo.ArchivosDeImagenes;
import tp2.vista.modelo.FactoresDeImagenes;
import ar.uba.fi.algo3.titiritero.vista.Imagen;

public class ImagenHelicoptero extends Imagen {

    private static ImagenHelicoptero imagenPrincipal;

    private static ImagenEscalable imagenEscalable;

    private ImagenHelicoptero() {
        this.setNombreArchivoImagen(ArchivosDeImagenes.IMAGEN_HELICOPTERO_FEDERAL);
    }

    private ImagenHelicoptero(Imagen imagen) {
        super(imagen);
    }

    private static void crearImagenHelicoptero() {
        imagenPrincipal = new ImagenHelicoptero();
        imagenEscalable = new ImagenEscalable(imagenPrincipal);
    }

    public static ImagenHelicoptero nuevaImagen(double escala) {
        if (imagenPrincipal == null) {
            crearImagenHelicoptero();
        }
        int tamanioImagen = (int) (FactoresDeImagenes.FACTOR_HELICOPTERO_FEDERAL * 2 * ValoresDeNaves.helicopteroFederalTamanio * escala);
        return new ImagenHelicoptero(imagenEscalable.getImagenEscalada(tamanioImagen));
    }
}
