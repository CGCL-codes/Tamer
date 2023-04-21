package src;

import com.gameloftProgrammersCup.client.UnitStats;
import com.gameloftProgrammersCup.clientInterfaces.Command;

import src.estadisticas.EstadisticasTipoUnidad;
import src.estrategias.EstrategiaUnidad;

public class Explorador extends Colector{
	static private EstadisticasTipoUnidad estadisticasTipoUnidad=new EstadisticasTipoUnidad(Explorador.class);
	public Explorador(int id,Cliente jugador,EstrategiaUnidad estrategia){
		super(EXPLORADOR_ACTIONRANGE,EXPLORADOR_ACTIONSPEED, EXPLORADOR_LIFE,EXPLORADOR_MOVEMENTSPEED, id, jugador, EXPLORADOR_VIEWRANGE,EXPLORADOR_MAXPAYLOAD,estrategia);
		//estadisticasTipoUnidad.setUnidadesFabricadas(estadisticasTipoUnidad.getUnidadesFabricadas()+1);
	}
public static final int EXPLORADOR_LIFE = 65; //*2 (130)
public static final int EXPLORADOR_ACTIONSPEED = 0 ; //*20 (0)
public static final int EXPLORADOR_VIEWRANGE = 5 ; //*20 (100)
public static final int EXPLORADOR_ACTIONRANGE = 0 ; //*30 (0)
public static final int EXPLORADOR_MOVEMENTSPEED = 4 ; //*20 (80)
public static final int EXPLORADOR_MAXPAYLOAD = 0 ; //*4 (0)
public int turnosSiguiendoColector=0;
public static final int MAXIMO_TURNOS_SIGUIENDO_EXPLORADOR=6;
public int turnoInicioPerimetro=0;

	public int getTipoInternoUnidad(){
		return UnidadBasica.TIPO_EXPLORADOR;
	}

	/**
	 * @return the estadisticasTipoUnidad
	 */
	static public EstadisticasTipoUnidad getEstadisticasTipoUnidad() {
		return estadisticasTipoUnidad;
	}

	/**
	 * @param estadisticasTipoUnidad the estadisticasTipoUnidad to set
	 */
	static public void setEstadisticasTipoUnidad(
			EstadisticasTipoUnidad estadisticasTipoUnidad) {
		estadisticasTipoUnidad = estadisticasTipoUnidad;
	}
	
	/**
	 * @override
	 */
	 public  void addPosicionesDescubiertas(int iCantidad){
		 estadisticasTipoUnidad.setCasillerosDescubiertos(estadisticasTipoUnidad.getCasillerosDescubiertos()+iCantidad);
	 }
	 
	 /**
	  * @override para estadisticas
	  */
	 public void setLife(int life) {
		 if(life < this.life)
			  estadisticasTipoUnidad.setDañoRecibido(this.life - life);
		 super.setLife(life); 
	 }
	 
	 /**
	  * @override
	  */
	 public void addMuerteDeEsteTipoDeUnidad(){
		 estadisticasTipoUnidad.setMuertes(estadisticasTipoUnidad.getMuertes()+1);
	 }

}

