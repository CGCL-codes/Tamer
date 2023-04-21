package juego;

/**
 *
 * @author juan
 */
public class FabricaBloques {

    private static FabricaBloques instancia = new FabricaBloques();

    BloqueVibora[] bloquesPorDireccion;

    BloqueHuevo premio;

    private FabricaBloques() {
        bloquesPorDireccion = new BloqueVibora[4];
        bloquesPorDireccion[BloqueVibora.ABAJO - 1] = new BloqueVibora();
        bloquesPorDireccion[BloqueVibora.ABAJO - 1].setDireccion(BloqueVibora.ABAJO);
        bloquesPorDireccion[BloqueVibora.ARRIBA - 1] = new BloqueVibora();
        bloquesPorDireccion[BloqueVibora.ARRIBA - 1].setDireccion(BloqueVibora.ARRIBA);
        bloquesPorDireccion[BloqueVibora.DERECHA - 1] = new BloqueVibora();
        bloquesPorDireccion[BloqueVibora.DERECHA - 1].setDireccion(BloqueVibora.DERECHA);
        bloquesPorDireccion[BloqueVibora.IZQUIERDA - 1] = new BloqueVibora();
        bloquesPorDireccion[BloqueVibora.IZQUIERDA - 1].setDireccion(BloqueVibora.IZQUIERDA);
        premio = new BloqueHuevo();
    }

    public static FabricaBloques getInstancia() {
        return instancia;
    }

    /**
     * Obtener la instancia del bloque de vibora correspondiente segun la direccion
     * @return el bloque necesario.
     */
    public BloqueVibora fabricarBloqueVibora(int direccion) {
        return bloquesPorDireccion[direccion - 1];
    }

    public BloqueHuevo fabricarPremio() {
        return premio;
    }
}
