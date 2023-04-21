package src.estrategias;

import java.util.*;
import com.gameloftProgrammersCup.client.ClientCommand;
import com.gameloftProgrammersCup.clientInterfaces.Command;
import com.gameloftProgrammersCup.clientInterfaces.Point;
import src.*;
import src.estadisticas.Estadisticas;

public abstract class EstrategiaGeneral {

    /**
	 * Actualiza las estrategias de cada unidad, verificando condiciones de fin, estad�sticas, etc.
	 * @param listaUnidades: todas las unidades de mi equipo o un subgrupo
	 * @author Fernando Rivas @version 1.0
	 *
	 */
    protected int ultimoTurnoDetecctionAtaqueMasivo = 0;

    protected boolean hayEnemigosEnRangoSeguridad = false;

    /**
	 * en el create, nullea todas las estrategias de todas las unidades, despu�s decidir� cu�l asigna a cada una
	 * 
	 */
    public EstrategiaGeneral(List unidades) {
        if (unidades == null) return;
        Iterator it = unidades.iterator();
        while (it.hasNext()) {
            UnidadBasica ub = (UnidadBasica) it.next();
            ub.setEstrategiaUnidad(null);
        }
    }

    public abstract void actualizarEstrategias(List listaUnidades, src.estadisticas.Estadisticas estadisticas);

    /**
 * Decide qu� unidad fabricar a continuacion, lleva a cabo la accion y devuelve el comando
 * @param f
 * @return comando a llevar a cabo
 * @author Fernando Rivas @version 1.0
 */
    public abstract ClientCommand fabricarUnidad(Fortaleza f);

    /**
 * funciones b�sicas de actualizaci�n que se llaman en cada ciclo para cada unidad
 * la idea es que controle si es necesario un cambio de estrategia
 */
    public abstract void actualizarUnidad(Asesino asesino);

    public abstract void actualizarUnidad(Guardia guardia);

    public abstract void actualizarUnidad(ColectorInicial colector);

    public abstract void actualizarUnidad(ColectorNormal colector);

    public abstract void actualizarUnidad(Explorador explorador);

    /**
 * @return the hayEnemigosEnRangoSeguridad
 */
    public boolean isHayEnemigosEnRangoSeguridad() {
        return hayEnemigosEnRangoSeguridad;
    }

    /**
 * @param hayEnemigosEnRangoSeguridad the hayEnemigosEnRangoSeguridad to set
 */
    public void setHayEnemigosEnRangoSeguridad(boolean hayEnemigosEnRangoSeguridad) {
        this.hayEnemigosEnRangoSeguridad = hayEnemigosEnRangoSeguridad;
    }

    /**
 * 
 * @return Enemigo: si hay, el que est� m�s cerca de la fortaleza. Sino, null
 */
    public Enemigo chequearEnemigosEnRangoSeguridad() {
        Enemigo enemigo = null;
        for (int y = Fortaleza.instanciaFortaleza.getPosition().getY() - Fortaleza.instanciaFortaleza.getRadioSeguridad(); y <= Fortaleza.instanciaFortaleza.getPosition().getY() + Fortaleza.instanciaFortaleza.getRadioSeguridad(); y++) {
            for (int x = Fortaleza.instanciaFortaleza.getPosition().getX() - Fortaleza.instanciaFortaleza.getRadioSeguridad(); x <= Fortaleza.instanciaFortaleza.getPosition().getX() + Fortaleza.instanciaFortaleza.getRadioSeguridad(); x++) {
                Point pEvaluado = new Point(x, y);
                if (!Fortaleza.isPointAfuera(pEvaluado) && pEvaluado.distance(Fortaleza.instanciaFortaleza.getPosition()) <= Fortaleza.instanciaFortaleza.getRadioSeguridad()) {
                    if (Fortaleza.mapa[x][y] != null && Fortaleza.mapa[x][y].getClass().equals(Enemigo.class) && ((Enemigo) Fortaleza.mapa[x][y]).getUltimoTurnoVisto() == Fortaleza.instanciaFortaleza.getJugador().turno) if (enemigo == null) enemigo = (Enemigo) Fortaleza.mapa[x][y]; else if (((Enemigo) Fortaleza.mapa[x][y]).getPosition().distance(Fortaleza.instanciaFortaleza.getPosition()) < enemigo.getPosition().distance(Fortaleza.instanciaFortaleza.getPosition())) enemigo = (Enemigo) Fortaleza.mapa[x][y];
                }
            }
        }
        return enemigo;
    }

    /**
 * devuelve una posici�n cualquiera dentro de rango seguridad. sirve para guardiaPatrullando
 * @return
 */
    public static Point getPosicionRandomEnRangoSeguridad() {
        int maxPuntosEvaluados = 5;
        Point pEvaluado = null;
        int porcentaje = 50;
        int GradoRandomizacion = (int) Math.round(Math.random() * (Fortaleza.instanciaFortaleza.getRadioSeguridad()));
        for (int y = Fortaleza.instanciaFortaleza.getPosition().getY() - Fortaleza.instanciaFortaleza.getRadioSeguridad(); y <= Fortaleza.instanciaFortaleza.getPosition().getY() + Fortaleza.instanciaFortaleza.getRadioSeguridad(); y++) {
            for (int x = Fortaleza.instanciaFortaleza.getPosition().getX() - Fortaleza.instanciaFortaleza.getRadioSeguridad(); x <= Fortaleza.instanciaFortaleza.getPosition().getX() + Fortaleza.instanciaFortaleza.getRadioSeguridad(); x++) {
                pEvaluado = new Point(x, y);
                if (!Fortaleza.isPointAfuera(pEvaluado) && pEvaluado.distance(Fortaleza.instanciaFortaleza.getPosition()) <= Fortaleza.instanciaFortaleza.getRadioSeguridad()) {
                    if (Math.random() * 100 < porcentaje) {
                        GradoRandomizacion--;
                        porcentaje = porcentaje / 2;
                        if (GradoRandomizacion < 0) return pEvaluado;
                    }
                }
            }
        }
        return pEvaluado;
    }

    public boolean hayAtaqueMasivoNOW() {
        if (Fortaleza.instanciaFortaleza.fortalezaEnemiga == null) return false;
        List enemigosFueraRango = new ArrayList();
        List enemigosTurno = Fortaleza.instanciaFortaleza.enemigosAtacantesDelTurno();
        Iterator it = enemigosTurno.iterator();
        while (it.hasNext()) {
            Enemigo en = (Enemigo) it.next();
            if (en.getPosition().distance(Fortaleza.instanciaFortaleza.fortalezaEnemiga.getPosition()) > EstrategiaExploradorAcecharColector.perimetro) enemigosFueraRango.add(en);
        }
        System.out.println("enemigosFueraRango.size()" + enemigosFueraRango.size() + 1 + " < " + Fortaleza.instanciaFortaleza.getJugador().turno / 60);
        if (enemigosFueraRango.size() < Fortaleza.instanciaFortaleza.getJugador().turno / 50 && Fortaleza.instanciaFortaleza.getJugador().turno >= 50) return false;
        int sumaDistanciasMenores = 0;
        it = enemigosFueraRango.iterator();
        Enemigo[] enArray = new Enemigo[enemigosFueraRango.size()];
        int i = 0;
        while (it.hasNext()) {
            Enemigo en = (Enemigo) it.next();
            enArray[i] = en;
            i++;
        }
        for (int j = 0; j < enemigosFueraRango.size(); j++) {
            Point pActual = enArray[j].getPosition();
            int minDistancia = 9999;
            for (int k = 1; k < enemigosFueraRango.size(); k++) {
                if (k == j) continue;
                if (pActual.distance(enArray[k].getPosition()) < minDistancia) minDistancia = pActual.distance(enArray[k].getPosition());
            }
            sumaDistanciasMenores += minDistancia;
        }
        if (enemigosFueraRango.size() == 0) return false;
        System.out.println("if" + sumaDistanciasMenores / enemigosFueraRango.size() + " < 10");
        if (enemigosFueraRango.size() > 0 && sumaDistanciasMenores / enemigosFueraRango.size() < 10) return true;
        return false;
    }
}
