package utilidades;

import excepciones.ExcepcionCargaFicheroDeMusica;
import excepciones.ExcepcionErrorEnBusqueda;
import ficherosMusicales.FicheroMusicalAbstracto;
import java.io.File;
import javax.swing.JTable;
import javax.swing.JTextField;
import vista.escritorio.componentes.modelos.ModeloParaGFMTableDirectorio;
import vista.escritorio.componentes.modelos.ModeloParaGFMTableRenombrador;

/**
 * Utilidades con cadenas.
 * 
 * @author Rodrigo Villamil Pérez 
 */
public class UtilComponentes {

    private UtilComponentes() {
    }

    /**
     * Aplica el patron del JTextField en la lista de ficheros que muestra 
     * el componente jTablaRenombradorCanciones.
     */
    public static void aplicaPatron(JTable jTablaListaCanciones, JTable jTablaRenombradorCanciones, JTextField jTextoPatron) {
        FicheroMusicalAbstracto fMusical = null;
        File ficheroNuevo = null;
        Patron unPatron = null;
        String rutaPadre = "";
        ModeloParaGFMTableDirectorio modelo = (ModeloParaGFMTableDirectorio) jTablaListaCanciones.getModel();
        ModeloParaGFMTableRenombrador modeloRenombre = (ModeloParaGFMTableRenombrador) jTablaRenombradorCanciones.getModel();
        modeloRenombre.vacia();
        int[] filasSeleccionadas = jTablaListaCanciones.getSelectedRows();
        for (int i = 0; i < filasSeleccionadas.length; ++i) {
            fMusical = modelo.getFicheroMusical(filasSeleccionadas[i]);
            unPatron = new Patron(jTextoPatron.getText(), fMusical);
            if (fMusical.getFile().getParentFile() != null) {
                rutaPadre = fMusical.getFile().getParentFile().getAbsolutePath() + File.separator;
            }
            try {
                ficheroNuevo = new File(rutaPadre + unPatron.getValor() + "." + fMusical.getExtension());
                modeloRenombre.aniadeNombresFichero(fMusical.getFile().getName(), ficheroNuevo.getName());
            } catch (ExcepcionErrorEnBusqueda ex) {
            } catch (ExcepcionCargaFicheroDeMusica ex) {
            }
        }
    }
}
