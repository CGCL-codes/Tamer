package arqueologia.view.PanelesComandos.ComandoComprar.ProgressBarCompra;

import arqueologia.controller.GestorHerramienta;
import arqueologia.model.Equipo;
import arqueologia.model.EquipoCubo;
import arqueologia.model.TrabajadorObrero;
import arqueologia.model.Transaccion;
import arqueologia.view.BarraSuperior.JPTransaccion;
import arqueologia.view.juego.Juego;

/**
 *
 * @author arturo.gamarra
 */
public class JPBCObreros extends JProgressBarCompra {

    public JPBCObreros(GestorHerramienta herramientasExistentes, Equipo equipo, int min, int max) {
        super(herramientasExistentes, equipo, min, max);
    }

    @Override
    protected void finalizarCompra() {
        EquipoCubo equipoCubo = equipo.getEquipoCubo();
        boolean isExcavando = equipoCubo.isExcavando();
        if (isExcavando) equipoCubo.detener();
        try {
            TrabajadorObrero obrero = new TrabajadorObrero();
            equipo.addTrabajador(obrero);
            JPTransaccion.addTransaccion(new Transaccion((float) (obrero.getSueldo() * -1), "Contratar un Obrero "), JPTransaccion.TipoTransaccion.BOLSA);
            int cantidadPacHerramientas = super.getCantidadHerramientas();
            if (super.equipo.getTrabajadores().getSizeTrabajadores() <= cantidadPacHerramientas) {
                if (equipo.getTrabajador(0).getBalde() != null) {
                    super.asignarHerramineta(obrero, equipo.getTrabajador(0).getBalde().getID(), true);
                }
                if (equipo.getTrabajador(0).getHerramienta() != null) {
                    super.asignarHerramineta(obrero, equipo.getTrabajador(0).getHerramienta().getID(), false);
                }
            }
        } catch (Exception ex) {
        }
        if (isExcavando) equipoCubo.excavar();
    }
}
