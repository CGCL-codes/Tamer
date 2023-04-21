package ist.ac.simulador.nucleo;

import javax.swing.JComponent;
import java.util.*;
import ist.ac.simulador.application.Gui;
import ist.ac.simulador.confguis.ConfigGui;

/**
 * Elemento abstracto do simulador. Todos os elementos tratados pelos
 * simulador derivam desta classe. Contem m�todos para manipula��o de
 * sub-elementos atrav�s de um registo.
 *
 * @author Carlos Martinho and Carlos Ribeiro
 * @version 1.1
 * @see SElementRegistry
 */
public abstract class SElement implements java.io.Serializable, Comparable {

    public static int Z = 0xFFFFFFFF;

    public static int MAX_WORD_SIZE = 24;

    /**
     * Refer�ncia para o m�dulo no qual o elemento est� inserido.
     */
    private SModule fModule = null;

    /**
     * Prioridade do elemento no ciclo do simulador.
     */
    private int fPriority = 0;

    /**
     * Identifica��o do elemento.
     */
    private String fId = null;

    private String fName = null;

    /**
     * Flag indicando se o elemento necessita de se processar.
     */
    protected boolean fModified;

    /** Flag indicando se o elemento est� activo.
     */
    protected boolean fEnable;

    /**
     * Refer�ncia para o simulador.
     */
    protected transient Simulator fSimulator = null;

    /**
     * Registo de sub-elementos.
     */
    protected SElementRegistry fRegistry = null;

    /**
     * Array contendo os sub-elementos do registo aquando da �ltima
     * invoca��o do m�todo <code>lockConfig</code>.
     *
     * @see #lockConfig 
     */
    protected Object[] aRegistry = null;

    /**
     * Refer�ncia para os objectos gr�fico com vistas sobre o objecto.
     */
    private transient Object fGUI = null;

    private transient ConfigGui configGui = null;

    private transient Object fGUIContainer = null;

    private static long lastCreationTime = 0;

    private static long count = 0;

    /**
     * Cria um novo elemento com uma determinada prioridade.  Um
     * elemento com valor de prioridade mais alto � tratado primeiro
     * no ciclo do simulador. Isto �, o m�todo
     * <code>updateCheck</code> � invocado antes de ser invocado nos
     * elementos de prioridade mais baixa.
     *
     * @param priority prioridade do elemento no ciclo do simulador.
     * @param id identifica��o do elemento.
     * @see #updateCheck
     */
    public SElement(int priority, String id) {
        init(priority, id);
    }

    protected void init(int priority, String id) {
        fPriority = priority;
        long time = System.currentTimeMillis();
        fId = Long.toHexString(time);
        if (time == lastCreationTime) {
            fId += "-" + count++;
        } else {
            lastCreationTime = time;
            count = 0;
        }
        fName = id;
        fModified = false;
        fEnable = true;
        fRegistry = new SElementRegistry();
    }

    /**     * Devolve a prioridade do elemento.
     *
     * @return prioridade do elemento.
     */
    public int getPriority() {
        return fPriority;
    }

    /**
     * Devolve a identifica��o do elemento.
     *
     * @return identifica��o do elemento.
     */
    public String getId() {
        if (fModule == null) return fId;
        return fModule.getId() + ":" + fId;
    }

    /**
     * Devolve a identifica��o do elemento.
     *
     * @return identifica��o do elemento.
     */
    public String getStrictId() {
        return fId;
    }

    /**
     * Renomeia a identifica��o do elemento.
     *
     * @param id a nova identifica��o
     */
    protected void setId(String id) {
        fId = id;
    }

    /**
     * Devolve o nome do elemento.
     *
     * @return nome do elemento.
     */
    public final String getName() {
        return fName;
    }

    /**
     * Renomeia o elemento.
     *
     *
     * @param name nome do elemento
     */
    public void setName(String name) {
        fName = name;
        if (getGUI() != null) ((ist.ac.simulador.application.Gui) getGUI()).setTitle(fName);
        if (getConfigGui() != null) getConfigGui().setTitle(fName + " Properties");
    }

    /**
     * Devolve a refer�ncia para o simulador.
     *
     * @return refer�ncia para o simulador.
     */
    public Simulator getSimulator() {
        return fSimulator;
    }

    /**
     * Actualiza a refer�ncia para o simulador. Propagando-a a todos
     * os sub-elementos contidos no registo. 
     *
     * @param s refer�ncia para o simulador.
     */
    public void setSimulator(Simulator s) {
        fSimulator = s;
        if (fModified && fSimulator != null) {
            fSimulator.addModifiedElement(this);
        }
        fRegistry.setSimulator(s);
    }

    /**
     * Devolve um array com todos os sub-elementos do elemento.
     * @return os subelementos do elemento.
     */
    public Object[] getSubElements() {
        return fRegistry.getConfig();
    }

    /**
     * Tira uma imagem do registo de sub-elementos. Guarda-a em
     * <code>aRegistry</code> de forma a permitir a execu��o mais
     * eficiente de opera��es sobre todos os sub-elementos do registo.
     *
     * @see #aRegistry 
     */
    public void lockConfig() {
        aRegistry = fRegistry.getConfig();
    }

    /**
     * Reinicia o elemento.
     */
    public void reset() {
        if (fSimulator != null && fModified) fSimulator.removeModifiedElement(this);
        fModified = false;
        fEnable = true;
        if (aRegistry != null) for (int i = 0; i < aRegistry.length; i++) ((SElement) aRegistry[i]).reset();
        fRegistry.reset();
        Gui gui = (Gui) getGUI();
        if (gui != null) gui.reset();
        ConfigGui configGui = getConfigGui();
        if (configGui != null) configGui.setVisible(false);
    }

    /**
     * Devolve o objecto gr�fico que representa o elemento.
     *
     * @return refer�ncia para o objecto gr�fico interface com o
     * elemento.
     */
    public Object getGUI() {
        return fGUI;
    }

    /** Posiciona o objecto gr�fico que representa o elemento.
     *
     * @param fGUI refer�ncia para o objecto gr�fico interface com o
     * elemento.
     */
    public void setGUI(Object fGUI) {
        this.fGUI = fGUI;
    }

    /**
	 * Define a GUI que � respons�vel pela configura?�o do elemento.
	 * @param configGui GUI.
	 */
    public void setConfigGui(ConfigGui configGui) {
        this.configGui = configGui;
    }

    /**
	 * Devolve a GUI respons�vel pela configura?�o do elemento.
	 * @return GUI.
	 */
    public ConfigGui getConfigGui() {
        return configGui;
    }

    /** Define que dois elementos s�o iguais se se imprimirem da mesma forma.
     * @param obj elemento a comparar
     * @return true se os elementos forem iguais.
     */
    public boolean equals(Object obj) {
        if (obj == null || !SElement.class.isInstance(obj)) return false;
        return ((SElement) obj).getId().equals(getId());
    }

    public int compareTo(Object obj) {
        if (obj == null || !SElement.class.isInstance(obj)) return -1;
        return ((SElement) obj).getId().compareTo(getId());
    }

    /** Posiciona o m�dulo a que pertence o elemento.
     * @param m o m�dulo a que o elemento pertence.
     */
    public void setModule(SModule m) {
        fModule = m;
    }

    /** Devolve o m�dulo a que o elemento pertence.
     * @return o m�dulo
     */
    public SModule getModule() {
        return fModule;
    }

    /** Posiciona o objecto gr�fico que representa o elemento na �rvore de constru��o da arquitectura.
     * @param obj o objecto gr�fico.
     */
    public void setGUIContainer(Object obj) {
        fGUIContainer = obj;
    }

    /** Devolve o objecto gr�fico que representa o elemento na �rvore de constru��o da arquitectura.
     * @return o objecto gr�fico.
     */
    public Object getGUIContainer() {
        return fGUIContainer;
    }

    /** Activa/Desactiva o elemento. Este m�todo � utilizado para evitar que um elemento
     * seja processado quando o se encontra inactivo. Tipicamente o elemento � colocado
     * inactivo por um {@link SEnPort porto de enable} , mas pode ser colocado inactivo de outras formas.
     * @param enable true - activa o elemento
     * false - desactiva o elemento
     */
    public void setEnable(boolean enable) {
        fEnable = enable;
    }

    /** Verifica se o elemento est� activo.
     * @return true se o elemento est� activo.
     */
    public boolean isEnable() {
        return fEnable;
    }

    /**
     * For�a o processamento deste elemento. Da pr�xima vez que o
     * m�todo <code>updateCheck</code> for invocado, o processamento
     * ser� desencadeado. De forma a que este m�todo se execute ainda
     * no mesmo ciclo de simula��o, este verifica que a prioridade do
     * elemento invocador � superior � sua pr�pria prioridade. Caso
     * contr�rio, lan�a uma excep��o.
     *
     * @param caller o elemento que invoca a fun��o.
     * @exception SInsufficientPriorityException lan�ada caso o elemento
     *     invocando este m�todo n�o tenha prioridade superior.
     * @see #updateCheck
     */
    public synchronized void setModified(SElement caller) throws SInsufficientPriorityException {
        if (caller != null) {
            if (caller.getPriority() > fPriority) {
                if (fSimulator != null && !fModified) fSimulator.addModifiedElement(this);
                fModified = true;
            } else throw new SInsufficientPriorityException();
        }
    }

    /**
     * Used by realizing classes to mark this module dirty.
     * This method is potentialy invoked by another thread so it should be 
     * sincronized. However, to improve performance the method is not sinchronized
     * and we rely on the atomicity of integer update.
     *
     */
    protected synchronized void setModified() {
        if (fSimulator != null && !fModified) fSimulator.addModifiedElement(this);
        fModified = true;
    }

    /**
     * Invoca o processamento deste elemento caso tenha sido
     * notificado anteriormente para tal atrav�s do m�todo
     * <code>setModified</code>.  Caso contr�rio, nada faz. O
     * processamento � contido no m�todo <code>update</code>.  Este
     * m�todo � invocado a cada ciclo do simulador.
     *
     * @exception SException reencaminha qualquer excep��o que tenha sido 
     *     lan�ada durante a execu��o do m�todo <code>update</code>.
     * @see #update 
     * @see #setModified
     */
    public synchronized void updateCheck() throws SException {
        if (fModified) {
            fModified = false;
            update();
        }
    }

    /**
     * M�todo abstracto contendo o processamento do elemento.
     * Reencaminha qualquer excep��o.
     *
     * @exception SException eventualmente lan�ada durante a execu��o do m�todo. 
     */
    public abstract void update() throws SException;

    /**
     * Trata o sinal desencadeado por um evento do simulador. Por omiss�o,
     * este m�todo tem uma implementa��o vazia, pois nem todos os elementos
     * recebem eventos. Cabe aos que recebem redefinir este m�todo.
     *
     * @param signalValue valor do sinal do evento.
     */
    public void handleEvent(int signalValue) {
    }

    /**
     * Classe interna privada usada para ordenar elementos pela sua
     * prioridade.  
     */
    private static class CompareElementPriority implements Comparator {

        /**
         */
        public int compare(Object o1, Object o2) {
            int p1 = ((SElement) o1).fPriority;
            int p2 = ((SElement) o2).fPriority;
            return (p1 > p2 ? -1 : (p1 == p2 ? 0 : 1));
        }
    }

    /**
     * Devolve um comparador para ordenar elementos por prioridades.
     * Devolve uma classe implementando a interface
     * <code>java.util.Comparator</code> permitindo a ordena��o de
     * elementos do mais priorit�rio ao menos priorit�rio.
     *
     * @return comparador por prioridades
     * @see java.util.Comparator
     */
    public static Comparator comparePriority() {
        return new CompareElementPriority();
    }

    /**
     * Converte o objecto elemento numa <code>String</code>.
     *
     * @return String representando o elemento.
     */
    public String toString() {
        return "E<" + fId + ">";
    }

    /**
     * Escreve uma mensagem de texto na interface de simula��o.  Serve
     * para centralizar a interface de mensagens.
     *
     * @param s mensagem de texto a imprimir.  
     */
    public void dbgMsg(String s) {
        if (fSimulator == null) System.out.println("> " + s); else fSimulator.dbgMsg(s);
    }

    /**
     * Escreve uma mensagem de erro na interface de simula��o.  Serve
     * para centralizar a interface de mensagens.
     *
     * @param s mensagem de erro a imprimir.  
     */
    public void dbgErrorMsg(String s) {
        if (fSimulator == null) System.out.println("> " + s); else fSimulator.dbgErrorMsg(s);
    }
}
