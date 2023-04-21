package algoritmos.numericos.tipos;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import Jama.Matrix;
import algoritmos.Matriz;
import algoritmos.numericos.NoSupervisado;

public class LVQ extends NoSupervisado {

    /**
	 * Lista de muestras de cada centro
	 */
    private ArrayList<ArrayList<Matriz>> muestrascentros;

    /**
	 * Umbral
	 */
    private double umbral;

    public LVQ(JTextArea arg0, JProgressBar arg1, JButton arg2, JButton arg3, JButton arg4) {
        super();
        muestrascentros = new ArrayList<ArrayList<Matriz>>();
        umbral = 2;
        texto = arg0;
        progreso = arg1;
        botonNuevoDato = arg2;
        botonAprendizaje = arg3;
        botonVisualizaGraficos = arg4;
    }

    private void actualizaCentro(int indexClase, int indexMuestra) {
        ArrayList<Matriz> muestrasClase = muestrascentros.get(indexClase);
        muestrasClase.add(new Matriz(entrada.get(indexMuestra)));
        double[] centroActualizado = new double[dimension];
        for (int i = 0; i < dimension; i++) centroActualizado[i] = new Float(0);
        for (Iterator<Matriz> it = muestrasClase.iterator(); it.hasNext(); ) {
            double[] muestraActual = it.next().getRowPackedCopy();
            for (int i = 0; i < dimension; i++) centroActualizado[i] += muestraActual[i];
        }
        for (int i = 0; i < dimension; i++) centroActualizado[i] /= muestrasClase.size();
        Matrix temp = new Matrix(centroActualizado, 1);
        centros.set(indexClase, new Matriz(temp));
        conjuntos.get(indexClase).add(indexMuestra);
    }

    protected void aprendizaje() {
        inicializaComponentesAprendizaje();
        int indexMuestra = 0;
        for (Matrix temp : entrada) {
            Matriz dato = (Matriz) temp;
            int indexClase = muestraPerteneceAClase(dato, umbral);
            if (indexClase == -1) {
                centros.add(new Matriz(dato));
                ArrayList<Matriz> listamuestras = new ArrayList<Matriz>();
                listamuestras.add(new Matriz(dato));
                muestrascentros.add(listamuestras);
                Set<Integer> conjunto = new HashSet<Integer>();
                conjunto.add(indexMuestra);
                conjuntos.add(conjunto);
            } else {
                actualizaCentro(indexClase, indexMuestra);
            }
            indexMuestra++;
        }
        long milis = new Date().getTime() - startTime.getTime();
        String tmp;
        salida("Proceso de aprendizaje finalizado");
        if (milis > 1000) {
            tmp = (milis / 1000) + " segundos " + milis % 1000 + " ms.";
        } else {
            tmp = milis + " ms.";
        }
        salida("El proceso ha durado " + tmp);
        salida("Centros de las clases:");
        for (int i = 0; i < numeroDeClases(); i++) salida("Clase " + (i + 1) + "= " + centros.get(i));
        salida("");
        finalizaComponentesAprendizaje();
    }

    protected boolean convergencia() {
        return true;
    }

    private int muestraPerteneceAClase(Matriz muestra, double umbral) {
        if (centros.size() == 0) return -1;
        double minDistancia = Math.sqrt(muestra.distancia((Matriz) centros.get(0)));
        int index = 0;
        for (int i = 1; i < centros.size(); i++) {
            double distanciaActual = Math.sqrt(muestra.distancia((Matriz) centros.get(i)));
            if (distanciaActual < minDistancia) {
                minDistancia = distanciaActual;
                index = i;
            }
        }
        if (minDistancia > umbral) return -1;
        return index;
    }

    public void perteneceAClase(ArrayList<Number> lista) {
        int clase;
        Matriz dato = new Matriz(lista);
        clase = muestraPerteneceAClase(dato, umbral) + 1;
        if (clase == 0) salida("La muestra " + dato + " no se conoce clase contenedora."); else salida("La muestra " + dato + " pertenece a la clase " + clase + ".");
    }

    private void salida(String St) {
        final String mensaje = St;
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                texto.append(mensaje + "\n");
            }
        });
    }

    public double getUmbral() {
        return umbral;
    }

    public void setUmbral(double umbral) {
        this.umbral = umbral;
    }
}
