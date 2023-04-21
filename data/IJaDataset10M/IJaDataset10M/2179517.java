package secd.utils;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import secd.Circuito;
import secd.componentes.Cable;
import secd.componentes.Componente;
import secd.componentes.TipoConexion;

/*
 *  Clase q representa el área copiada para poder 
 *  pegarla posteriormente.
 *  
 *  Guarda una lista de los componentes copiados
 *  Otra lista de las conexiones copiadas
 *  y la región en la q se encontraban estos elementos.
 * */
public class Portapapeles {

	ArrayList<Componente> componentes = null;

	ArrayList<Cable> conexiones = null;

	Rectangle area = null;

	Circuito circuito = null;

	// Constructor
	public Portapapeles(ArrayList<Componente> componentesSeleccionados,
			ArrayList<Cable> conexionesSeleccionadas, Rectangle areaOriginal,
			Circuito cir) {
		componentes = componentesSeleccionados;
		conexiones = conexionesSeleccionadas;
		area = new Rectangle(areaOriginal);
		circuito = cir;
	}

	public Rectangle getArea() {
		return area;
	}

	public void setArea(Rectangle area) {
		this.area = area;
	}

	public ArrayList<Componente> getComponentes() {
		return componentes;
	}

	public void setComponentes(ArrayList<Componente> componentes) {
		this.componentes = componentes;
	}

	public ArrayList<Cable> getConexiones() {
		return conexiones;
	}

	public void setConexiones(ArrayList<Cable> conexiones) {
		this.conexiones = conexiones;
	}

	// Método auxiliar para depurar
	public void muestraContenido() {
		System.out.println("Rectangulo: " + area);
		System.out.println("Hay " + conexiones.size() + " conexiones");
		System.out.println("Hay " + componentes.size() + " componentes");
	}

	/*
	 * Retorna la lista de elementos a copiar, pero situados en la posición de
	 * destino.
	 */
	public ArrayList<Componente> getCopiaElementosTrasladados(
			Point punto_destino) {
		ArrayList<Componente> lista_elementos_trasladados = 
			new ArrayList<Componente>();

		Componente c;
		Componente c_nuevo;
		Distancia d;
		Point nuevo_centro;
		Point centro_rect = getCentroArea();

		for (int i = 0; i < componentes.size(); i++) {
			c = componentes.get(i);

			// Offset del centro del componente al centro del rectangulo de
			// seleccion
			d = new Distancia(
					(int) (c.getCentro().getX() - centro_rect.getX()), (int) (c
							.getCentro().getY() - centro_rect.getY()));

			// Obtenemos una copia del componente
			c_nuevo = c.getCopiaComponente();

			// Calculamos la nueva posición del centro del componente
			nuevo_centro = new Point();
			nuevo_centro.x = punto_destino.x + d.x;
			nuevo_centro.y = punto_destino.y + d.y;

			nuevo_centro = circuito.redondeaPunto(nuevo_centro);

			// Movemos el nuevo componente hasta su posición de destino
			c_nuevo.setCentro(nuevo_centro);
			c_nuevo.posicionaPatillas(nuevo_centro);

			lista_elementos_trasladados.add(c_nuevo);
		}

		return lista_elementos_trasladados;
	}

	/*
	 * Método que restablece las conexiones entre los componentes trasladados,
	 * utilizando como patrón los componentes originales y el nuevo punto de
	 * destino.
	 */
	public ArrayList<Cable> getCopiaConexionesTrasladadas(
			ArrayList<Componente> componentesTrasladados, Point punto_destino) {

		ArrayList<Cable> nuevas_conexiones = new ArrayList<Cable>();

		Cable cable_original, cable_nuevo;
		Componente c_origen_original, c_destino_original, c_origen_nuevo, c_destino_nuevo;

		boolean error;
		for (int i = 0; i < conexiones.size(); i++) {
			cable_original = conexiones.get(i);

			c_origen_original = cable_original.getComponenteOrigen();
			c_destino_original = cable_original.getComponenteDestino();

			c_origen_nuevo = getComponenteEquivalente(c_origen_original,
					punto_destino, componentesTrasladados);
			c_destino_nuevo = getComponenteEquivalente(c_destino_original,
					punto_destino, componentesTrasladados);

			cable_nuevo = new Cable(this.circuito);

			Point origen = getPuntoEquivalente(cable_original.getOrigen(),
					punto_destino, false);
			Point destino = getPuntoEquivalente(cable_original.getDestino(),
					punto_destino, false);
			
			if ( c_origen_nuevo==null || c_destino_nuevo==null )
				continue;

			cable_nuevo.setOrigen(origen);
			cable_nuevo.setDestino(destino);
			cable_nuevo.setComponenteDestino(c_destino_nuevo);
			cable_nuevo.setComponenteOrigen(c_origen_nuevo);

			try {
				if (c_origen_nuevo != null)
					c_origen_nuevo.conectaExtremoCable(cable_nuevo,
							getPuntoEquivalente(cable_original.getOrigen(),
									punto_destino, false),
							TipoConexion.ENTRADA_BIESTADO);

				if (c_destino_nuevo != null)
					c_destino_nuevo.conectaExtremoCable(cable_nuevo,
							getPuntoEquivalente(cable_original.getDestino(),
									punto_destino, false),
							TipoConexion.SALIDA_BIESTADO);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(null, e.getMessage(),
						"Error al conectar componentes",
						JOptionPane.WARNING_MESSAGE);
			}

			/*
			 * System.out.println("---> Datos de la conexión trasladada <---");
			 * System.out.println( "\r\nPunto origen original:" +
			 * cable_original.getOrigen() + "\r\nPunto destino original:" +
			 * cable_original.getDestino() + "\r\nPunto origen trasladado:" +
			 * cable_nuevo.getOrigen() + "\r\nPunto destino trasladado:" +
			 * cable_nuevo.getDestino() );
			 */

			// Añadimos la conexión nueva y la redibujamos
			error = cable_nuevo.recalculaCamino(true);

			if (error) {
				System.out
						.println("Salgo de getCopiaConexionesTrasladadas() con ERROR. ");
				return nuevas_conexiones;
			}

			nuevas_conexiones.add(cable_nuevo);
			circuito.añadirConexion(cable_nuevo);
		}
		System.out.println("Salgo de getCopiaConexionesTrasladadas(). Tamaño: "
				+ nuevas_conexiones.size());

		return nuevas_conexiones;
	}

	/*
	 * Retorna el componente que es la "copia" del componente "c_original". Los
	 * busca dentro de los componentes pegados "componentes_trasladados". Para
	 * encontrarlo se basa en calcular la posición que ocuparía su centro.
	 */
	private Componente getComponenteEquivalente(Componente c_original,
			Point punto_destino, 
			ArrayList<Componente> componentes_trasladados) {
		Componente c;
		Point centro_rect = getCentroArea();

		// Offset del centro del componente al centro del rectangulo de
		// seleccion
		Distancia d;
		d = new Distancia((int) (c_original.getCentro().getX() - centro_rect
				.getX()), (int) (c_original.getCentro().getY() - centro_rect
				.getY()));

		// Calculamos la nueva posición del centro del componente
		Point nuevo_centro;
		nuevo_centro = new Point();
		nuevo_centro.x = punto_destino.x + d.x;
		nuevo_centro.y = punto_destino.y + d.y;

		nuevo_centro = circuito.redondeaPunto(nuevo_centro);

		for (int i = 0; i < componentes_trasladados.size(); i++) {

			c = componentes_trasladados.get(i);

			// Si el nuevo centro del componente coindice, es q es la copia.
			if (c.contains(nuevo_centro)) {
				return c;
			}
		}

		return null;
	}

	private Point getPuntoEquivalente(Point p_antiguo, Point punto_referencia,
			boolean redondeo) {

		Distancia d;
		Point centro_rect = getCentroArea();
		d = new Distancia((int) (p_antiguo.getX() - centro_rect.getX()),
				(int) (p_antiguo.getY() - centro_rect.getY()));

		// Calculamos la nueva posición del centro del componente
		Point nuevo_punto;
		nuevo_punto = new Point();
		nuevo_punto.x = punto_referencia.x + d.x;
		nuevo_punto.y = punto_referencia.y + d.y;

		if (redondeo)
			nuevo_punto = circuito.redondeaPunto(nuevo_punto);

		return nuevo_punto;
	}

	private Point getCentroArea() {
		Point p = new Point();

		p.x = (int) (area.getX() + area.getWidth() / 2);
		p.y = (int) (area.getY() + area.getHeight() / 2);

		p = circuito.redondeaPunto(p);

		return p;
	}

	// Para aumentar y reducir escala, lo unico q hay que cambiar es el área,
	// puesto que los otros
	// componentes ya la tendrán actualizada. (Los elementos copiados son una
	// referencia a los elementos
	// reales del circuito). Su copia (clon), no se obtiene hasta q no se "pega"
	public void aumentaEscala() {
		area.x = (int) (area.x * circuito.getFactor());
		area.y = (int) (area.y * circuito.getFactor());

		area.width = (int) (area.width * circuito.getFactor());
		area.height = (int) (area.height * circuito.getFactor());
	}

	public void reduceEscala() {
		area.x = (int) (area.x / circuito.getFactor());
		area.y = (int) (area.y / circuito.getFactor());

		area.width = (int) (area.width / circuito.getFactor());
		area.height = (int) (area.height / circuito.getFactor());
	}
}
