package nemo;

/**
 * Metodos que calculan lo calculable del programa
 * Limpian y ponen datos en el GUI
 * Llaman al bean de las bases y recoje los datos, los procesa y los envia al
 * GUI listos para mostrarlos
 * @author jmmgarcia
 */
public class ControlBean {

    BasesBean conex;

    public void LimpiaGuiCalidad(FCalidad gui) {
        gui.setCFecha("");
        gui.setCImo("");
        gui.setCNib("");
        gui.setCNombre("");
        gui.setCOrden("");
    }

    public void PierdeFoco(FCalidad gui) {
        int nume;
        nume = Integer.valueOf(gui.getCNib());
        conex = new BasesBean();
        conex.SetNib(nume);
        conex.BuscaNib();
        Actualiza(gui);
    }

    public void Actualiza(FCalidad gui) {
        gui.setCImo(Integer.toString(conex.GetImo()));
        gui.setCMat(conex.GetMatricula());
        gui.setCNombre(conex.GetNombre());
    }
}
