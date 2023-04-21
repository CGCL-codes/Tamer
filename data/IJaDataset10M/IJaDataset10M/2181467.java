package net.sf.jregression;

import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import junit.framework.Test;
import junit.framework.TestFailure;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import net.sf.jregression.exception.JRegressionStartUpException;
import net.sf.jregression.reglas.vo.Bateria;
import net.sf.jregression.reglas.vo.Prueba;

/**
 * @author mblasi
 * mailto: matias.blasi@gmail.com
 *
 * DynamicTestSuite
 */
public class DynamicTestSuite extends TestSuite implements Test {

    /**
	 * Cantidad de veces que deseo reiterar la ejecucion de la bateria (default 1)
	 */
    private int repeticiones = 1;

    /**
	 * Intervalo de tiempo en ms entre ejecución y ejecución de bateria
	 */
    private int descanso = 0;

    /**
	 * Bateria a la que pertenece esta suite, necesaria para centralizar el manejo de errores ocurridos en los tests
	 */
    private Bateria padre = null;

    /**
	 * @param nombre
	 */
    public DynamicTestSuite(String nombre, Bateria bateria) {
        super(nombre);
        this.padre = bateria;
        String userRepe = System.getProperty(Ejecutador.REPETICIONES);
        if (userRepe != null) {
            try {
                this.repeticiones = Integer.parseInt(userRepe);
            } catch (NumberFormatException e1) {
                throw new JRegressionStartUpException("Formato de n�mero incorrecto: " + userRepe);
            }
        }
        String userDescanso = System.getProperty(Ejecutador.DESCANSO);
        if (userDescanso != null) {
            try {
                this.descanso = userDescanso != null ? Integer.parseInt(userDescanso) : 0;
            } catch (NumberFormatException e) {
                throw new JRegressionStartUpException("Formato de n�mero incorrecto: " + userDescanso);
            }
        }
    }

    /**
	 * Ejecuta la suite tantas veces como el valor de repeticiones lo indique
	 */
    public void run(TestResult result) {
        int cantidad = padre.getPruebas().size();
        JRegressionReporter reporter = JRegressionReporter.getInstance(repeticiones, cantidad);
        reporter.setFecha(new Date(System.currentTimeMillis()));
        for (int i = 0; i < repeticiones; i++) {
            reporter.addBateria(i, super.getName());
            super.run(result);
            Enumeration t = super.tests();
            int k = 0;
            while (t.hasMoreElements()) {
                Object o = t.nextElement();
                if (o instanceof JRegressionTest) {
                    JRegressionTest test = (JRegressionTest) o;
                    reporter.addPrueba(k, test.getIdPrueba());
                    reporter.addTiempo(i, k, test.getTiempo());
                    reporter.addTiempoSetup(i, k, test.getTiempoSetup());
                    if (Ejecutador.ENTRADA_SI.equals(System.getProperty(Ejecutador.ENTRADA))) if (test.getLote() != null) reporter.addEntrada(i, k, test.getLote().toString());
                    if (!Ejecutador.SALIDA_NO.equals(System.getProperty(Ejecutador.SALIDA))) {
                        if (test.getResultado() != null) reporter.addResultado(i, k, test.getResultado().toString());
                    }
                    k++;
                }
            }
            if (padre.huboErrores()) {
                int cantidadPruebasFallidas = 0;
                Iterator e = padre.getErrores();
                while (e.hasNext()) {
                    cantidadPruebasFallidas++;
                    TestFailure error = (TestFailure) e.next();
                    reporter.addError(i, error.trace());
                }
                Iterator f = padre.getFallas();
                while (f.hasNext()) {
                    cantidadPruebasFallidas++;
                    TestFailure falla = (TestFailure) f.next();
                    reporter.addFalla(i, falla.trace());
                }
                String plural = cantidadPruebasFallidas > 1 ? "s" : "";
                reporter.log("BATERIA '" + super.getName() + "' --> " + cantidadPruebasFallidas + " (de " + cantidad + ") prueba" + plural + " fallida" + plural);
            } else {
                String plural = super.testCount() > 1 ? "s" : "";
                reporter.log("BATERIA '" + super.getName() + "' --> " + cantidad + " prueba" + plural + " exitosa" + plural + "!");
            }
            reporter.log("\n");
            padre.resetErrors();
            long partida = System.currentTimeMillis();
            if (i + 1 < repeticiones) while (System.currentTimeMillis() < partida + descanso) ;
        }
        reporter.generarResumen();
    }

    /**
     * Permite obtener el valor de la propiedad descanso
     * @return retorna el valor de descanso
     */
    public int getDescanso() {
        return descanso;
    }

    /**
     * Permite modificar el valor de descanso
     * @param descanso es el nuevo valor de  descanso
     */
    public void setDescanso(int descanso) {
        this.descanso = descanso;
    }

    /**
     * Permite obtener el valor de la propiedad repeticiones
     * @return retorna el valor de repeticiones
     */
    public int getRepeticiones() {
        return repeticiones;
    }

    /**
     * Permite modificar el valor de repeticiones
     * @param repeticiones es el nuevo valor de  repeticiones
     */
    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }

    /**
     * Creo una nueva Suite, le agrego sus tests clonados, y sus suites clonadas
     * @see java.lang.Object#clone()
     */
    public Object clone() throws CloneNotSupportedException {
        DynamicTestSuite ret = new DynamicTestSuite(getName(), this.padre);
        Enumeration t = this.tests();
        while (t.hasMoreElements()) {
            Test test = (Test) t.nextElement();
            if (test instanceof JRegressionTest) {
                JRegressionTest jregressionTest = (JRegressionTest) test;
                this.padre.setSuite(ret);
                Prueba pruebaMulata = null;
                Iterator pruebasMulatas = this.padre.getPruebas().iterator();
                boolean encontrada = false;
                while (pruebasMulatas.hasNext()) {
                    pruebaMulata = (Prueba) pruebasMulatas.next();
                    if (jregressionTest.getIdPrueba().equals(pruebaMulata.getId())) {
                        encontrada = true;
                        break;
                    }
                }
                Prueba prueba = this.padre.getVo().getPrueba(jregressionTest.getIdPrueba());
                prueba = (Prueba) prueba.clone();
                if (encontrada && pruebaMulata != null) prueba.merge(pruebaMulata);
                jregressionTest = this.padre.getVo().prueba2JRegressionTest(prueba);
                jregressionTest.setPadre(this.padre);
                ret.addTest(jregressionTest);
            } else if (test instanceof DynamicTestSuite) {
                DynamicTestSuite anidada = (DynamicTestSuite) test;
                Bateria duplicado = (Bateria) anidada.getPadre().clone();
                duplicado.setPadre(this.padre);
                ret.setPadre(duplicado.getPadre());
                ret.addTest(duplicado.getSuite());
            }
        }
        ret.setRepeticiones(1);
        return ret;
    }

    /**
     * Permite obtener el valor de la propiedad padre
     * @return retorna el valor de padre
     */
    public Bateria getPadre() {
        return padre;
    }

    /**
     * Permite modificar el valor de padre
     * @param padre es el nuevo valor de  padre
     */
    public void setPadre(Bateria padre) {
        this.padre = padre;
    }

    /**
     * Permite obtener el primer test de una suite
     * @param s suite a obtener el primer test
     */
    public JRegressionTest getFirstTest(DynamicTestSuite s) {
        JRegressionTest ret = null;
        if (s.testCount() > 0) {
            Test t = s.testAt(0);
            if (t instanceof JRegressionTest) {
                ret = ((JRegressionTest) t);
            } else if (t instanceof TestSuite) {
                ret = getFirstTest((DynamicTestSuite) t);
            }
        }
        return ret;
    }

    /**
     * Permite obtener el ultimo test de una suite
     * @param s suite a obtener el ultimo test
     */
    public JRegressionTest getLastTest(DynamicTestSuite s) {
        JRegressionTest ret = null;
        if (s.testCount() > 0) {
            Test t = s.testAt(s.testCount() - 1);
            if (t instanceof JRegressionTest) {
                ret = ((JRegressionTest) t);
            } else if (t instanceof TestSuite) {
                ret = getLastTest((DynamicTestSuite) t);
            }
        }
        return ret;
    }
}
