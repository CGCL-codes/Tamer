package CamposDePotencial;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.vecmath.Point3d;
import estrategia.ParametrosPotenciales;
import estrategia.Predictor;
import estrategia.Utiles;
import estrategia.Vector;

public class CampoAtractorObjetivo extends CampoPotencial {

    private double Katraccion;

    private double radioAtraccionConica;

    private Objetivo objetivo;

    public CampoAtractorObjetivo(Objetivo objetivo, Utiles u, Predictor p) {
        super(objetivo.getIdObjetivo(), u, p);
        this.objetivo = objetivo;
    }

    public void cargarConstantes() {
        Katraccion = ParametrosPotenciales.KCampoAtraccionCircular;
        radioAtraccionConica = ParametrosPotenciales.radioCampoAtractor;
    }

    public double hallarPendienteRecta(Point3d mipos, Point3d P) {
        double m = 0;
        if ((P.x - mipos.x) == 0) m = (Math.signum(P.y - mipos.y) * Double.MAX_VALUE); else m = (P.y - mipos.y) / (P.x - mipos.x);
        return m;
    }

    public double evaluarRecta(double m, Point3d posRobot) {
        return (m * posRobot.x - posRobot.y);
    }

    public double evaluarCircunferencia(Point3d posObjeto, Point3d posRobot) {
        double valor = Math.pow((posRobot.x - posObjeto.x), 2) + Math.pow((posRobot.y - posObjeto.y), 2);
        return Math.sqrt(valor);
    }

    public double getMagnitudFuerza(Point3d mipos, Point3d posObj) {
        double distancia = utiles.distanciaEuclidea(mipos, posObj);
        double magnitud = Katraccion * (Math.pow(distancia, 2));
        double direccionP1 = 0;
        double direccionP2 = 0;
        if (posObj.z - (3.0 / 8.0) * Math.PI < -Math.PI) direccionP2 = Math.PI + (posObj.z - (3 / 8) * Math.PI + Math.PI); else direccionP2 = posObj.z - (3.0 / 8.0) * Math.PI;
        if (posObj.z + (3.0 / 8.0) * Math.PI > Math.PI) direccionP1 = -Math.PI - (posObj.z - (3.0 / 8.0) * Math.PI - Math.PI); else direccionP1 = posObj.z + (3.0 / 8.0) * Math.PI;
        Point3d P1 = new Point3d();
        P1.x = Math.cos(direccionP1) * radioAtraccionConica + posObj.x;
        P1.y = Math.sin(direccionP1) * radioAtraccionConica + posObj.y;
        Point3d P2 = new Point3d();
        P2.x = Math.cos(direccionP2) * radioAtraccionConica + posObj.x;
        P2.y = Math.sin(direccionP2) * radioAtraccionConica + posObj.y;
        double m1 = hallarPendienteRecta(posObj, P1);
        double m2 = hallarPendienteRecta(posObj, P2);
        if (evaluarCircunferencia(posObj, mipos) <= radioAtraccionConica) {
            if (m2 < 0) {
                if (Math.signum(evaluarRecta(m1, mipos) - (m1 * posObj.x - posObj.y)) != Math.signum(evaluarRecta(m2, mipos) - (m2 * posObj.x - posObj.y))) {
                    magnitud = 0;
                }
            } else {
                if (Math.signum(evaluarRecta(m1, mipos) - (m1 * posObj.x - posObj.y)) == Math.signum(evaluarRecta(m2, mipos) - (m2 * posObj.x - posObj.y))) {
                    magnitud = 0;
                }
            }
        }
        return magnitud;
    }

    public Vector obtenerFuerza(Point3d mipos) throws IOException {
        cargarConstantes();
        Point3d puntoCampo = new Point3d();
        puntoCampo = objetivo.getPosicionObjetivo();
        double direccion = utiles.getAnguloTheta(mipos, puntoCampo);
        double magnitud = getMagnitudFuerza(mipos, puntoCampo);
        Vector resultante = new Vector(magnitud, direccion);
        resultante.setMagnitud(Math.abs(magnitud));
        return resultante;
    }
}
