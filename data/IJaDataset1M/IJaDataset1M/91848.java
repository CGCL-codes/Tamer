package ar.fi.uba.celdas.ambiente.modelo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import ar.fi.uba.celdas.ambiente.ILaberinto;
import ar.fi.uba.celdas.ambiente.ILaberinto.TIPO_CUBO;
import ar.fi.uba.celdas.interfaces.ambiente.IAccion;
import ar.fi.uba.celdas.interfaces.ambiente.IInfoRobot;
import ar.fi.uba.celdas.interfaces.bc.IBaseConocimientos;
import ar.fi.uba.celdas.interfaces.bc.Sensor;

public class Robot implements IRobot, IInfoRobot {

    private static final ArrayList<IAccion> acciones = new ArrayList<IAccion>();

    private static final HashMap<PCARD, Point> vectoresDireccion = new HashMap<Robot.PCARD, Point>(4);

    public static final String NOMBRE_SENSOR_POS_Y = "POS_Y";

    public static final String NOMBRE_SENSOR_POS_X = "POS_X";

    public static final String NOMBRE_SENSOR_DIST_IZQUIERDA = "DIST_IZQUIERDA";

    public static final String NOMBRE_SENSOR_DIST_ATRAS = "DIST_ATRAS";

    public static final String NOMBRE_SENSOR_DIST_DERECHA = "DIST_DERECHA";

    public static final String NOMBRE_SENSOR_DIST_ADELANTE = "DIST_ADELANTE";

    public static final String NOMBRE_ACCION_AVANZAR = "AVANZAR";

    public static final String NOMBRE_ACCION_ROTAR90 = "ROTAR90";

    public static final String NOMBRE_ACCION_ROTAR180 = "ROTAR180";

    public static final String NOMBRE_ACCION_ROTAR270 = "ROTAR270";

    private static enum PCARD {

        N, E, S, O
    }

    ;

    private static PCARD[] rotacionCoord = { PCARD.N, PCARD.E, PCARD.S, PCARD.O, PCARD.N, PCARD.E, PCARD.S };

    static {
        acciones.add(new Avanzar());
        acciones.add(new Rotar90());
        acciones.add(new Rotar180());
        acciones.add(new Rotar270());
        vectoresDireccion.put(PCARD.N, new Point(0, 1));
        vectoresDireccion.put(PCARD.E, new Point(1, 0));
        vectoresDireccion.put(PCARD.S, new Point(0, -1));
        vectoresDireccion.put(PCARD.O, new Point(-1, 0));
    }

    private final IBaseConocimientos bc;

    private final List<IRobotListener> listeners = new ArrayList<IRobotListener>();

    private ILaberinto laberinto;

    private final ArrayList<Sensor> estado = new ArrayList<Sensor>();

    private final ArrayList<Sensor> objetivo = new ArrayList<Sensor>();

    private final LinkedList<IAccion> colaAccionesAEjecutar = new LinkedList<IAccion>();

    private final Sensor distanciaAdelante = new Sensor(NOMBRE_SENSOR_DIST_ADELANTE, 0);

    private final Sensor distanciaDerecha = new Sensor(NOMBRE_SENSOR_DIST_DERECHA, 0);

    private final Sensor distanciaAtras = new Sensor(NOMBRE_SENSOR_DIST_ATRAS, 0);

    private final Sensor distanciaIzquierda = new Sensor(NOMBRE_SENSOR_DIST_IZQUIERDA, 0);

    private final Sensor posicionX = new Sensor(NOMBRE_SENSOR_POS_X, 0);

    private final Sensor posicionY = new Sensor(NOMBRE_SENSOR_POS_Y, 0);

    private final Sensor objetivoX = new Sensor(NOMBRE_SENSOR_POS_X, 0);

    private final Sensor objetivoY = new Sensor(NOMBRE_SENSOR_POS_Y, 0);

    private boolean ultimasAccionesExitosas = true;

    private boolean corriendo = false;

    private int indiceOrientacion = 0;

    private int distanciaVisible;

    private int cantAvances = 0;

    private int cantGiros = 0;

    private int cantAciertos = 0;

    private int cantErrores = 0;

    public Robot(IBaseConocimientos bc, ILaberinto laberinto, int distanciaVisible) {
        this.bc = bc;
        this.laberinto = laberinto;
        this.distanciaVisible = distanciaVisible;
        posicionX.setValor(laberinto.getEntrada().x);
        posicionY.setValor(laberinto.getEntrada().y);
        objetivoX.setValor(laberinto.getSalida().x);
        objetivoY.setValor(laberinto.getSalida().y);
        estado.add(distanciaAdelante);
        estado.add(distanciaDerecha);
        estado.add(distanciaAtras);
        estado.add(distanciaIzquierda);
        estado.add(posicionX);
        estado.add(posicionY);
        objetivo.add(objetivoX);
        objetivo.add(objetivoY);
        actualizarDistancias();
    }

    private void actualizarDistancias() {
        distanciaAdelante.setValor(calcularDistancia(vectoresDireccion.get(rotacionCoord[indiceOrientacion])));
        distanciaDerecha.setValor(calcularDistancia(vectoresDireccion.get(rotacionCoord[(indiceOrientacion + 1) % 4])));
        distanciaAtras.setValor(calcularDistancia(vectoresDireccion.get(rotacionCoord[(indiceOrientacion + 2) % 4])));
        distanciaIzquierda.setValor(calcularDistancia(vectoresDireccion.get(rotacionCoord[(indiceOrientacion + 3) % 4])));
    }

    /**
	 * @param direccion a calcular distancia
	 * @return distancia hasta obstáculo en la dirección pasada por parámetro 
	 */
    private int calcularDistancia(Point direccion) {
        int distancia = 0;
        for (int i = 1; i <= distanciaVisible; ++i) {
            int posX = posicionX.getValor() + direccion.x * i;
            int posY = posicionY.getValor() + direccion.y * i;
            if ((!validarLimitesLaberinto(posX, posY)) || laberinto.getCubo(posX, posY) == TIPO_CUBO.OBSTACULO) return distancia; else ++distancia;
        }
        return distancia;
    }

    /**
	 * @return true si el punto posX, posY se encuentra dentro del laberinto;
	 */
    private boolean validarLimitesLaberinto(int posX, int posY) {
        final Point tamanio = laberinto.getTamanio();
        return posX >= 0 && posY >= 0 && posX < tamanio.x && posY < tamanio.y;
    }

    @Override
    public Collection<Sensor> getEstado() {
        return new ArrayList<Sensor>(estado);
    }

    @Override
    public Collection<Sensor> getObjetivo() {
        return new ArrayList<Sensor>(objetivo);
    }

    @Override
    public boolean ultimasAccionesExitosas() {
        return ultimasAccionesExitosas;
    }

    @Override
    public Collection<IAccion> getAccionesDisponibles() {
        return new ArrayList<IAccion>(acciones);
    }

    @Override
    public synchronized void mover() {
        if (!corriendo || objetivoCumplido()) return;
        if (colaAccionesAEjecutar.isEmpty()) pedirNuevasAccionesABC();
        if (!colaAccionesAEjecutar.isEmpty() && !((IAccionInterna) colaAccionesAEjecutar.pop()).ejecutar(this)) {
            ultimasAccionesExitosas = false;
            colaAccionesAEjecutar.clear();
        }
    }

    private void pedirNuevasAccionesABC() {
        if (ultimasAccionesExitosas) ++cantAciertos; else ++cantErrores;
        notificarMovimiento();
        colaAccionesAEjecutar.addAll(bc.procesarSiguientesAcciones(this));
        ultimasAccionesExitosas = true;
    }

    public boolean objetivoCumplido() {
        return posicionX.getValor() == objetivoX.getValor() && posicionY.getValor() == objetivoY.getValor();
    }

    @Override
    public Point getPosicion() {
        return new Point(posicionX.getValor(), posicionY.getValor());
    }

    private PCARD getOrientacion() {
        return rotacionCoord[indiceOrientacion];
    }

    @Override
    public Point getVectorDireccion() {
        return vectoresDireccion.get(getOrientacion());
    }

    private static interface IAccionInterna extends IAccion {

        /**
		 * Ejecuta la acción en el robot pasado por parámetro 
		 */
        boolean ejecutar(Robot robot);
    }

    private static class Avanzar implements IAccionInterna {

        @Override
        public String getNombre() {
            return NOMBRE_ACCION_AVANZAR;
        }

        @Override
        public boolean ejecutar(Robot robot) {
            Point direccion = robot.getVectorDireccion();
            int posX = robot.posicionX.getValor() + direccion.x;
            int posY = robot.posicionY.getValor() + direccion.y;
            if (robot.distanciaAdelante.getValor() > 0 && robot.validarLimitesLaberinto(posX, posY)) {
                robot.posicionX.setValor(posX);
                robot.posicionY.setValor(posY);
                robot.actualizarDistancias();
                ++robot.cantAvances;
                return true;
            }
            return false;
        }
    }

    private abstract static class RotarGenerico implements IAccionInterna {

        @Override
        public final boolean ejecutar(Robot robot) {
            robot.indiceOrientacion = (robot.indiceOrientacion + getOffsetVectorOrientacion()) % 4;
            robot.actualizarDistancias();
            ++robot.cantGiros;
            return true;
        }

        public abstract int getOffsetVectorOrientacion();
    }

    private static class Rotar90 extends RotarGenerico {

        @Override
        public String getNombre() {
            return NOMBRE_ACCION_ROTAR90;
        }

        @Override
        public int getOffsetVectorOrientacion() {
            return 3;
        }
    }

    private static class Rotar180 extends RotarGenerico {

        @Override
        public String getNombre() {
            return NOMBRE_ACCION_ROTAR180;
        }

        @Override
        public int getOffsetVectorOrientacion() {
            return 2;
        }
    }

    private static class Rotar270 extends RotarGenerico {

        @Override
        public String getNombre() {
            return NOMBRE_ACCION_ROTAR270;
        }

        @Override
        public int getOffsetVectorOrientacion() {
            return 1;
        }
    }

    @Override
    public int getMaxValor(Sensor sensor) {
        if (sensor.getNombre().equals(NOMBRE_SENSOR_DIST_ADELANTE) || sensor.getNombre().equals(NOMBRE_SENSOR_DIST_ATRAS) || sensor.getNombre().equals(NOMBRE_SENSOR_DIST_DERECHA) || sensor.getNombre().equals(NOMBRE_SENSOR_DIST_IZQUIERDA)) return distanciaVisible; else if (sensor.getNombre().equals(NOMBRE_SENSOR_POS_X)) return laberinto.getTamanio().x - 1; else if (sensor.getNombre().equals(NOMBRE_SENSOR_POS_Y)) return laberinto.getTamanio().y - 1; else return 0;
    }

    @Override
    public int getMinValor(Sensor sensor) {
        return 0;
    }

    @Override
    public void addListener(IRobotListener listener) {
        listeners.add(listener);
    }

    private void notificarMovimiento() {
        for (IRobotListener l : listeners) l.notificarMovimientoRobot();
    }

    public int getCantAciertos() {
        return cantAciertos;
    }

    public int getCantAvances() {
        return cantAvances;
    }

    public int getCantGiros() {
        return cantGiros;
    }

    public int getCantErrores() {
        return cantErrores;
    }

    public void setCorriendo(boolean corriendo) {
        this.corriendo = corriendo;
    }

    public boolean getCorriendo() {
        return corriendo;
    }
}
