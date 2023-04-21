package jaguar.machine.turing.jturing;

import java.awt.Graphics;
import java.awt.Dimension;
import jaguar.machine.turing.*;
import jaguar.machine.JMachine;
import jaguar.machine.turing.exceptions.*;
import jaguar.util.*;
import jaguar.machine.util.*;
import jaguar.machine.util.jutil.*;
import jaguar.util.jutil.*;
import jaguar.machine.structures.*;
import jaguar.machine.structures.exceptions.*;
import jaguar.structures.*;
import jaguar.structures.exceptions.*;
import jaguar.structures.jstructures.*;
import jaguar.machine.turing.structures.*;
import jaguar.machine.turing.jturing.jstructures.*;
import jaguar.machine.turing.structures.exceptions.*;
import java.util.Vector;
import java.util.Iterator;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import java.io.File;
import java.io.IOException;
import java.lang.Math;

public class JTuring extends Turing implements JMachine {

    /**
     * La cadena sobre la que estamos ejecutando la TM, est� puede cambiar conforme vamos ejecutando la TM
     */
    protected JStr strToTest;

    /**
     * funcion de acceso para obtener el valor de strToTest
     * @return el valor actual de strToTest
     */
    public JStr getStrToTest() {
        return strToTest;
    }

    /**
    nm * funcion de acceso para modificar strToTest
     * @param new_strToTest el nuevo valor para strToTest
     */
    public void setStrToTest(JStr new_strToTest) {
        strToTest = new_strToTest;
    }

    /**
     * La cadena a checar si pertenece a L(TM) 
     */
    protected JStr strToTestOrig;

    /**
     * funcion de acceso para obtener el valor de strToTestOrig
     * @return el valor actual de strToTestOrig
     * @see #strToTestOrig
     */
    public JStr getStrToTestOrig() {
        return strToTestOrig;
    }

    /**
     * funcion de acceso para modificar strToTestOrig
     * @param new_strToTestOrig el nuevo valor para strToTestOrig
     * @see #strToTestOrig
     */
    public void setStrToTestOrig(JStr new_strToTestOrig) {
        strToTestOrig = new_strToTestOrig;
    }

    /**
     * El frame donde se mostrar� est� TM
     */
    protected JTuringFrame turingFrame;

    /**

    /**
     * funcion de acceso para obtener el valor de turingFrame
     * @return el valor actual de turingFrame
     * @see #turingFrame
     */
    public JMachineFrame getMachineFrame() {
        return turingFrame;
    }

    /**
     * funcion de acceso para modificar turingFrame
     * @param new_turingFrame el nuevo valor para turingFrame
     * @see #turingFrame
     */
    public void setJMachineFrame(JMachineFrame new_turingFrame) {
        this.turingFrame = (JTuringFrame) new_turingFrame;
    }

    /**
     * El contexto gr�fico sobre el cual se dibujar�
     */
    protected Graphics g;

    /**
     * funcion de acceso para obtener el valor de g
     * @return el valor actual de g
     */
    public Graphics getG() {
        return g;
    }

    /**
     * funcion de acceso para modificar g
     * @param new_g el nuevo valor para g
     */
    public void setG(Graphics new_g) {
        g = new_g;
    }

    /**
     * La subcadena de la cadena de entrada que ya ha sido probada
     */
    JStr subStrTested;

    /**
       * Get the value of subStrTested.
       * @return Value of subStrTested.
       */
    public JStr getSubStrTested() {
        return subStrTested;
    }

    /**
       * Set the value of subStrTested.
       * @param v  Value to assign to subStrTested.
       */
    public void setSubStrTested(JStr v) {
        this.subStrTested = v;
    }

    /**
      * El estado anterior antes de ser nulo, esto nos sirve para dar un �ltimo estado y checar si esta en los finales, en caso de tener que hacer este chequeo
      */
    protected State previousNotNullCurrentState;

    /**
     * funcion de acceso para obtener el valor de previousNotNullCurrentState
     * @return el valor actual de previousNotNullCurrentState
     * @see #previousNotNullCurrentState
     */
    public State getPreviousNotNullCurrentState() {
        return previousNotNullCurrentState;
    }

    /**
     * funcion de acceso para modificar previousNotNullCurrentState
     * @param new_previousNotNullCurrentState el nuevo valor para previousNotNullCurrentState
     * @see #previousNotNullCurrentState
     */
    public void setPreviousNotNullCurrentState(State new_previousNotNullCurrentState) {
        previousNotNullCurrentState = new_previousNotNullCurrentState;
    }

    /**
     * Reinicializa la JTuring a los valores de inicio<br>
     * Los valores reinicializados son:<br>
     * <ul>
     * <li>Al estado actual le asigna <b>q0</b></li>
     * <li>A la cadena para probar le asigna la cadena original a probar asignada por medio de
     *       <code>setStrToTestOrig(JStr)</code></li>
     */
    public void resetMachine() {
        ((JTuringDelta) delta).setCurrent_p(new JState("resetP"));
        ((JTuringDelta) delta).setCurrent_q(new JState("resetQ"));
        setCurrentState(getQ0());
        setPreviousNotNullCurrentState(getQ0());
        setStrToTest(getStrToTestOrig());
        setSubStrTested(new JStr());
        ((JTuringDelta) delta).setCurrent_sym(new Symbol("reset"));
        ((JTuringFrame) getMachineFrame()).getJdc().repaint();
        initializeMachine(getStrToTestOrig());
    }

    public JTuring(StateSet _Q, Alphabet _Sigma, Alphabet _Gamma, TuringDelta _delta, State _q0, Symbol _blanco, StateSet _F, Graphics _g) {
        this(_Q, _Sigma, _Gamma, _delta, _q0, _blanco, _F);
        setG(_g);
    }

    public JTuring(Turing turing) {
        this(new JStateSet(turing.getQ()), turing.getSigma(), turing.getGamma(), (TuringDelta) turing.getDelta(), new JState(turing.getQ0()), turing.getBlanco(), turing.getF());
        setDelta(new JTuringDelta((TuringDelta) getDelta(), (JStateSet) getQ()));
    }

    public JTuring(StateSet _Q, Alphabet _Sigma, Alphabet _Gamma, TuringDelta _delta, State _q0, Symbol _blanco, StateSet _F) {
        super(_Q, _Sigma, _Gamma, _delta, _q0, _blanco, _F);
        setCurrentState(getQ0());
        setPreviousNotNullCurrentState(getQ0());
    }

    protected JTuring() {
        super();
    }

    /**
     * Constructora que construye un JTuring a partir del nombre de un archivo que es valido segun el DTD de las M�quinas de Turing
     * @see <a href="http://ijaguar.sourceforge.net/DTD/tm.dtd">tm.dtd</a>
     */
    public JTuring(String filename) throws Exception {
        this(new File(filename));
    }

    /**
     * Constructora que construye un JTuring a partir del nombre de un archivo que es valido segun el las M�quinas de Turing
     * @param jturingframe asociado listo para inicializar las posiciones de los estados, si estos no fueroninicializados, y listo para mostrarse.
     * @see <a href="http://ijaguar.sourceforge.net/DTD/tm.dtd">tm.dtd</a>
     */
    public JTuring(String filename, JTuringFrame jturingframe) throws Exception {
        this(new File(filename), jturingframe);
    }

    /**
     * Constructora que construye un JTuring a partir del nombre de un archivo que es valido segun el DTD de las M�quinas de Turing
     * @param jturingframe asociado listo para inicializar las posiciones de los estados, si estos no fueroninicializados, y listo para mostrarse.
     * @see <a href="http://ijaguar.sourceforge.net/DTD/tm.dtd">tm.dtd</a>
     */
    public JTuring(File file, JTuringFrame jturingframe) throws Exception {
        this(file);
        setJMachineFrame(jturingframe);
        initStatesPosition(turingFrame.getJScrollPaneCanvas().getViewport().getViewSize());
        turingFrame.getJdc().repaint();
    }

    /**
     * Constructora que construye un JTuring a partir del nombre de un archivo que es valido segun el DTD de las M�quinas de Turing
     * @see <a href="http://ijaguar.sourceforge.net/DTD/tm.dtd">tm.dtd</a>
     */
    public JTuring(File file) throws Exception {
        super(file);
        setQ(new JStateSet(getQ()));
        setQ0(new JState(getQ0()));
        setF(new JStateSet(getF()));
        setDelta(new JTuringDelta((TuringDelta) getDelta(), (JStateSet) getQ()));
        setCurrentState(getQ0());
        setPreviousNotNullCurrentState(getQ0());
        makeStateReferences();
    }

    public void print(Graphics g) {
        turingFrame.getJdc().paint(g);
    }

    /**
     * Esta funci�n se usa para asignar posiciones a los centros de
     * los JStates.  Estas posiciones, est�n alrededor de un circulo
     * de radio <code>r</code>, dividiendo y encontramos la posici�n
     * de cada estado por medio de coordenadas polares (<code>(x,y) =
     * (r*cos*theta, r*sin*theta)</code>).  Donde la theta es cada uno
     * de los intervalos de dividir 360 entre la cardinalidad de Q y r
     * es el minimo entre el ancho y alto del canvas entre dos.
     */
    public void initStatesPosition(Dimension d) {
        int cardinalidadQ = getQ().size();
        if (cardinalidadQ > 0) {
            double radio;
            if (d.getWidth() != 300 && d.getHeight() != 300) radio = Math.min((d.getWidth() - 250) / 2, (d.getHeight() - 250) / 2); else radio = Math.min((d.getWidth() - 75) / 2, (d.getHeight() - 75) / 2);
            radio += 20;
            JState current;
            double intervalo = 360 / cardinalidadQ;
            double currentIntervalo = 0;
            for (Iterator i = getQ().iterator(); i.hasNext(); ) {
                current = (JState) i.next();
                current.setLocation(radio * Math.cos(Math.toRadians(currentIntervalo)) + radio, radio * Math.sin(Math.toRadians(currentIntervalo)) + radio);
                currentIntervalo += intervalo;
            }
        }
    }

    /**
     * Regresa verdadero si podemo hacer un paso m�s o falso si no podemos
     * @return <code>true</code> - si podemos seguir aplicando la funci�n de transici�n delte, i.e. si la cadena a checar
     * no es <epsilon>  y si el estado en el que estamos es distinto de <code>null</code>. <br>
     * <code>false</code> - en otro caso.
     */
    public boolean nextStep() {
        setPreviousNotNullCurrentState(currentState);
        doTransition();
        ((JTuringDelta) delta).setCurrent_p(previousNotNullCurrentState);
        ((JTuringDelta) delta).setCurrent_sym(getLastTransitionSymbol());
        ((JTuringDelta) delta).setCurrent_q(currentState);
        turingFrame.getJdc().repaint();
        return !getHalt();
    }

    /**
     * Despliega el resultado de la ejecuci�n del aut�mata como un cuadrito en el <code>turingframe</code>
     * asociado
     */
    public void displayResult() {
        if (currentState == null) JOptionPane.showMessageDialog((JFrame) turingFrame, "The TM DOES NOT accepts the string " + getStrToTestOrig(), "TM Result", JOptionPane.INFORMATION_MESSAGE); else JOptionPane.showMessageDialog((JFrame) turingFrame, "The TM " + (getCurrentState().getIsInF() ? " accepts " : " DOES NOT accept ") + " the string " + getStrToTestOrig(), "TM Result", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * La representaci�n en vector de la funci�n de transici�n delta
     */
    protected Vector tableVector = DEFAULT_TABLEVECTOR;

    /**
     * El valor por omisi�n para tableVector
     */
    public static final Vector DEFAULT_TABLEVECTOR = null;

    /**
     * funcion de acceso para obtener el valor de tableVector
     * @return el valor actual de tableVector, donde la entrada tableVector.get(0) es el header y tableVector.get(1) es un vector que contiene los renglones 
     * @see #tableVector
     */
    public Vector getTableVector() {
        if (tableVector == DEFAULT_TABLEVECTOR) {
            Object aGamma[] = getGamma().toArray();
            Object aQ[] = getQ().toArray();
            Vector header = new Vector(), currentRow, data = new Vector();
            Object entry;
            for (int i = 0; i < aQ.length; i++) {
                currentRow = new Vector();
                for (int j = 0; j < aGamma.length; j++) {
                    entry = ((TuringDelta) getDelta()).apply((State) aQ[i], (Symbol) aGamma[j]);
                    currentRow.add((entry != null) ? entry.toString() : null);
                }
                currentRow.add(0, aQ[i]);
                data.add(currentRow);
            }
            for (int j = 0; j < aGamma.length; j++) header.add(((Symbol) aGamma[j]).getSym());
            header.add(0, "Q");
            tableVector = new Vector();
            tableVector.add(header);
            tableVector.add(data);
        }
        return tableVector;
    }

    /**
     ** Dado un estado dice si es o no es un estado inicial
     ** @param p el estado sobre el cual preguntaremos si es o no inicial en �sta m�quina
     ** @return true si <code>p</code> es estado inicial
     **/
    public boolean esInicial(State p) {
        return p.equals(getQ0());
    }
}
