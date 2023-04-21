package interfaz.vistas;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.TimerTask;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import threading.ThreadVisualizacion;
import util.MyJSONReader;
import visualizaciones.GraphVizGrupos;
import visualizaciones.Visualizacion;

/**
 * @author Fernando
 *
 */
public class VistaGVizGrupos extends Vista {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String urlImagenes = MyJSONReader.getInstance().readConfigStr("TempDirImgPath");

    private File archivoImagen;

    private JLabel imagen;

    public VistaGVizGrupos(Visualizacion v) {
        super(v);
        this.setLayout(new GridBagLayout());
        GridBagConstraints g1 = new GridBagConstraints();
        g1.gridx = 0;
        g1.gridy = 0;
        g1.gridwidth = 1;
        imagen = new JLabel();
        this.add(imagen, g1);
        this.setPreferredSize(new Dimension(600, 600));
    }

    @Override
    public void borraArchivosAnteriores() {
        File f1 = new File(MyJSONReader.getInstance().readConfigStr("TempDirImgPath"));
        File[] files = f1.listFiles();
        for (int i = 0; i < files.length; i++) {
            files[i].delete();
        }
    }

    protected void pinta() {
        String nombreArchivo = ((GraphVizGrupos) vistaMostrada).getNombreArchivo();
        int instante = vistaMostrada.getInstante();
        archivoImagen = new File(urlImagenes + "/" + nombreArchivo + String.format("%03d", instante) + ".png");
        while (!archivoImagen.exists()) {
        }
        ImageIcon icon = new ImageIcon(urlImagenes + "/" + nombreArchivo + String.format("%03d", instante) + ".png");
        imagen.setIcon(icon);
        this.validate();
    }

    @Override
    public void empezar() {
        try {
            vistaMostrada.setInstante(0);
            timer.schedule(new RemindTask(), 1000, 1000);
            hilo.start();
            hilo.setEjecutar(true);
            ejecutando = true;
        } catch (Exception e) {
            System.err.println("La visualización ya está empezada");
        }
    }

    @Override
    public void empezarEnInstante(int numInstante) {
        try {
            timer.schedule(new RemindTask(), 500, 500);
            vistaMostrada.setInstante(numInstante);
            hilo.start();
            hilo.setEjecutar(true);
            ejecutando = true;
        } catch (Exception e) {
            System.err.println("La visualización ya está empezada");
        }
    }

    @Override
    public void reiniciar() {
        pausar();
        vistaMostrada.setInstante(0);
        timer.schedule(new RemindTask(), 500, 500);
        hilo.start();
        hilo.setEjecutar(true);
        ejecutando = true;
    }

    @Override
    public void continuar() {
        if (ejecutando == false) {
            hilo = new ThreadVisualizacion(vistaMostrada);
            timer.schedule(new RemindTask(), 500, 500);
            hilo.start();
            hilo.setEjecutar(true);
            ejecutando = true;
        }
    }

    private class RemindTask extends TimerTask {

        @Override
        public void run() {
            pinta();
        }
    }
}
