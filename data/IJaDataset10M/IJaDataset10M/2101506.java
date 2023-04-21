package src;

import src.estadisticas.EstadisticasTipoUnidad;
import src.estrategias.EstrategiaUnidad;

public class ColectorNormal extends Colector{
	static private EstadisticasTipoUnidad estadisticasTipoUnidad=new EstadisticasTipoUnidad(ColectorNormal.class);
	public ColectorNormal(int id,Cliente jugador,EstrategiaUnidad estrategia){
				super(COLECTOR_ACTIONRANGE,COLECTOR_ACTIONSPEED, COLECTOR_LIFE,COLECTOR_MOVEMENTSPEED, id, jugador, COLECTOR_VIEWRANGE,COLECTOR_MAXPAYLOAD,estrategia);
				//estadisticasTipoUnidad.setUnidadesFabricadas(estadisticasTipoUnidad.getUnidadesFabricadas()+1);
			}
		public static final int COLECTOR_LIFE = 30; //*2 (60)
		public static final int COLECTOR_ACTIONSPEED = 2 ; //*20 (40)
		public static final int COLECTOR_VIEWRANGE = 1	; //*20 (20)
		public static final int COLECTOR_ACTIONRANGE = 1 ; //*30 (30)
		public static final int COLECTOR_MOVEMENTSPEED = 4 ; //*20 (80)
		public static final int COLECTOR_MAXPAYLOAD = 20 ; //*4 (80)

		public int getTipoInternoUnidad(){
			return UnidadBasica.TIPO_COLECTORNORMAL;
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
		static public void setEstadisticasTipoUnidad(EstadisticasTipoUnidad estadisticasTipoUnidad) {
			estadisticasTipoUnidad = estadisticasTipoUnidad;
		}
		/**
		 * @override para estadisticas
		 */
		 public  void addPosicionesDescubiertas(int iCantidad){
			 estadisticasTipoUnidad.setCasillerosDescubiertos(estadisticasTipoUnidad.getCasillerosDescubiertos()+iCantidad);
		 }
		 
		 /**
		  * @override para estadisticas
		  */
			public void setResources(int recursos) {
				if (recursos<load);//asumo que está depositando
					estadisticasTipoUnidad.setRecursosColectados(estadisticasTipoUnidad.getRecursosColectados()+ (load - recursos ));
				super.setResources(recursos);

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
