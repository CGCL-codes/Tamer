package fiuba.algo3.juego.vista;

import fiuba.algo3.titiritero.ObjetoVivo;
import fiuba.algo3.titiritero.vista.Imagen;

public class VistaNaveExplosion extends Imagen implements ObjetoVivo {

    int frameCont;

    static int velocidadCambio = 3;

    int velocidadCambioCont;

    public VistaNaveExplosion() {
        this.frameCont = 0;
        this.velocidadCambioCont = 0;
        this.setNombreArchivoImagen("recursos/Nave/Explosion/Explosion0.png");
    }

    @Override
    public void vivir() {
        pasaUnTiempo();
        actualizarImagen();
    }

    public void actualizarImagen() {
        if (velocidadCambioCont == velocidadCambio) {
            if (frameCont < 7) {
                frameCont++;
                this.setNombreArchivoImagen("recursos/Nave/Explosion/Explosion" + frameCont + ".png");
            } else {
                this.setNombreArchivoImagen("recursos/Nave/Explosion/Explosion6.png");
            }
            velocidadCambioCont = 0;
        }
    }

    public void pasaUnTiempo() {
        if (velocidadCambioCont < velocidadCambio) {
            velocidadCambioCont++;
        }
    }
}
