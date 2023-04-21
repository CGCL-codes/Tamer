package jaguar.machine.dfa.jdfa.jconverters;

import jaguar.machine.dfa.jdfa.*;
import jaguar.machine.dfa.converters.*;
import java.util.*;
import jaguar.structures.Symbol;
import jaguar.machine.util.*;
import jaguar.machine.util.jutil.*;
import jaguar.util.*;
import jaguar.util.jutil.*;
import jaguar.grammar.jgrammar.*;
import jaguar.grammar.tipo3.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import jaguar.machine.dfa.*;
import jaguar.machine.dfa.jdfa.*;
import java.io.*;
import java.awt.event.ComponentEvent;

/** 
 * El frame para la clase JDfa2Gtipo3
 * 
 * @author Ivan Hern�ndez Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class JDfa2Gtipo3Frame extends JConverterFrame {

    /**
     * El motor para la conversion
     */
    protected JDfa2Gtipo3 engineConverter;

    /**
     * funcion de acceso para obtener el valor de engineConverter
     * @return el valor actual de engineConverter
     * @see #engineConverter
     */
    public JDfa2Gtipo3 getEngineConverter() {
        return engineConverter;
    }

    /**
     * funcion de acceso para modificar engineConverter
     * @param new_engineConverter el nuevo valor para engineConverter
     * @see #engineConverter
     */
    public void setEngineConverter(JDfa2Gtipo3 new_engineConverter) {
        engineConverter = new_engineConverter;
    }

    /**
     * El JDFA  que vamos a minimizar
     */
    protected JDFA jdfaOrig;

    /**
     * funcion de acceso para obtener el valor de jdfaOrig
     * @return el valor actual de jdfaOrig
     * @see #jdfaOrig
     */
    public JDFA getJdfaorig() {
        return jdfaOrig;
    }

    /**
     * funcion de acceso para modificar jdfaOrig
     * @param new_jdfaOrig el nuevo valor para jdfaOrig
     * @see #jdfaOrig
     */
    public void setJdfaorig(JDFA new_jdfaOrig) {
        jdfaOrig = new_jdfaOrig;
    }

    /**
      * La gram�tica tipo3 equivalente al JDFA 
      */
    protected Gtipo3 rGrammar;

    /**
     * funcion de acceso para obtener el valor de rGrammar
     * @return el valor actual de rGrammar
     * @see #rGrammar
     */
    public Gtipo3 getRgrammar() {
        return rGrammar;
    }

    /**
     * funcion de acceso para modificar rGrammar
     * @param new_rGrammar el nuevo valor para rGrammar
     * @see #rGrammar
     */
    public void setRgrammar(Gtipo3 new_rGrammar) {
        rGrammar = new_rGrammar;
    }

    /**
     * El canvas donde solo dibujaremos el DFA original que minimizaremos 
     */
    protected JDfaCanvas jdfacanvasOrig;

    /**
     * funcion de acceso para obtener el valor de jdfacanvasOrig
     * @return el valor actual de jdfacanvasOrig
     * @see #jdfacanvasOrig
     */
    public JDfaCanvas getJdfacanvasorig() {
        return jdfacanvasOrig;
    }

    /**
     * funcion de acceso para modificar jdfacanvasOrig
     * @param new_jdfacanvasOrig el nuevo valor para jdfacanvasOrig
     * @see #jdfacanvasOrig
     */
    public void setJdfacanvasorig(JDfaCanvas new_jdfacanvasOrig) {
        jdfacanvasOrig = new_jdfacanvasOrig;
    }

    /**
     * El canvas donde solo dibujaremos la Gram�tica equivalente
     */
    protected JGrammarCanvas jgrammarCanvas;

    /**
     * funcion de acceso para obtener el valor de jgrammarCanvas
     * @return el valor actual de jgrammarCanvas
     * @see #jgrammarCanvas
     */
    public JGrammarCanvas getJgrammarcanvas() {
        return jgrammarCanvas;
    }

    /**
     * funcion de acceso para modificar jgrammarCanvas
     * @param new_jgrammarCanvas el nuevo valor para jgrammarCanvas
     * @see #jgrammarCanvas
     */
    public void setJgrammarcanvas(JGrammarCanvas new_jgrammarCanvas) {
        jgrammarCanvas = new_jgrammarCanvas;
    }

    /**
     * El frame donde se mostrar� el ndfa
     */
    protected JInternalFrame jorigframe;

    /**
     * funcion de acceso para obtener el valor de jorigframe
     * @return el valor actual de jorigframe
     * @see #jorigframe
     */
    public JInternalFrame getJorigframe() {
        return jorigframe;
    }

    /**
     * funcion de acceso para modificar jorigframe
     * @param new_jorigframe el nuevo valor para jorigframe
     * @see #jorigframe
     */
    public void setJorigframe(JInternalFrame new_jorigframe) {
        jorigframe = new_jorigframe;
    }

    /**
     * El frame donde pondremos el canvas de la gram�tica resultante
     */
    protected JInternalFrame jgrammarframe;

    /**
     * funcion de acceso para obtener el valor de jgrammarframe
     * @return el valor actual de jgrammarframe
     * @see #jgrammarframe
     */
    public JInternalFrame getJgrammarframe() {
        return jgrammarframe;
    }

    /**
     * funcion de acceso para modificar jgrammarframe
     * @param new_jgrammarframe el nuevo valor para jgrammarframe
     * @see #jgrammarframe
     */
    public void setJgrammarframe(JInternalFrame new_jgrammarframe) {
        jgrammarframe = new_jgrammarframe;
    }

    JSplitPane splitPane;

    public JDfa2Gtipo3Frame(String title) {
        super(title, "Waiting for a DFA to transform... ");
    }

    public JDfa2Gtipo3Frame() {
        this("DFA 2 Gram�tica T3");
    }

    protected void setControls() {
        setControls("Transform DFA");
    }

    /**
     ** Crea el men� con las configuraciones b�sicas de este Frame
     **/
    protected JMenuBar createMenu() {
        return createMenu("Load DFA to Tranform...", "Loads a new DFA to transform into a T3 Grammar ", "Transform", "Transform the loaded DFA to an equivalent T3 Grammar", "Save  Grammar...", "Save the resulting Grammar");
    }

    public void saveResultFromConvertion() {
        saveResultFromConvertion("Save Resulting Grammar ");
    }

    public boolean doConvertion() {
        setEngineConverter(new JDfa2Gtipo3(getJdfaorig(), detailsArea));
        if (getEngineConverter() == null) return false;
        engineConverter.doConvertion();
        setRgrammar(engineConverter.getGt3());
        detailsArea.append("\n\nResulting T3 Grammar:\n" + getRgrammar());
        jgrammarCanvas = new JGrammarCanvas(MACHINE_SIZE, getRgrammar());
        jgrammarframe = new JInternalGrammarFrame(jgrammarCanvas, "T3 Grammar " + getCurrentobjecttoconvert());
        jgrammarframe.getContentPane().add(new JScrollPane(jgrammarCanvas, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        jgrammarframe.setSize(MACHINE_SIZE);
        jgrammarframe.setLocation(new Point((int) MACHINE_SIZE.getWidth(), 0));
        desktop.add(jgrammarframe);
        jgrammarframe.show();
        jgrammarframe.setVisible(true);
        saveResultMenuItem.setText("Save T3 Grammar " + getCurrentobjecttoconvert());
        return true;
    }

    protected boolean loadOrigFromFile() {
        return loadOrigFromFile("Load DFA to minimize");
    }

    protected void loadOrigFromFile(File file) throws Exception {
        jdfaOrig = new JDFA(file);
        jdfacanvasOrig = new JDfaCanvas(MACHINE_SIZE, jdfaOrig);
        jdfaOrig.initStatesPosition(MACHINE_SIZE);
        currentObjectToConvert++;
        jorigframe = new JInternalFrame("DFA Orig " + getCurrentobjecttoconvert(), true, true, true, true);
        jorigframe.getContentPane().add(new JScrollPane(jdfacanvasOrig, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
        jorigframe.setSize(MACHINE_SIZE);
        desktop.add(jorigframe);
        jorigframe.show();
        jorigframe.setVisible(true);
    }

    public void toFile(FileWriter fw) {
        getRgrammar().toFile(fw);
    }

    public static void main(String[] argv) {
        JDfa2Gtipo3Frame f = new JDfa2Gtipo3Frame();
        f.show();
    }

    public class JInternalGrammarFrame extends JInternalFrame implements ActionListener {

        JGrammarCanvas gCanvas;

        public JInternalGrammarFrame(JGrammarCanvas gCanvas, String title) {
            this(gCanvas, title, true, true, true, true);
        }

        public JInternalGrammarFrame(JGrammarCanvas gCanvas, String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable) {
            super(title, resizable, closable, maximizable, iconifiable);
            this.gCanvas = gCanvas;
            JMenuBar menuBar = new JMenuBar();
            JMenu utilMenu = new JMenu("Util");
            JMenuItem replaceSymbol = new JMenuItem("Replace Symbols...", KeyEvent.VK_R);
            replaceSymbol.getAccessibleContext().setAccessibleDescription("Reemplaza s�mbolos de la gram�tica");
            replaceSymbol.addActionListener(this);
            replaceSymbol.setActionCommand("replace");
            replaceSymbol.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F7, 0));
            utilMenu.add(replaceSymbol);
            JMenu fileMenu = new JMenu("File");
            JMenuItem saveCurrent = new JMenuItem("Save Grammar....", KeyEvent.VK_S);
            saveCurrent.getAccessibleContext().setAccessibleDescription("Guarda esta gram�tica");
            saveCurrent.addActionListener(this);
            saveCurrent.setActionCommand("save");
            saveCurrent.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F6, 0));
            fileMenu.add(saveCurrent);
            menuBar.add(fileMenu);
            menuBar.add(utilMenu);
            setJMenuBar(menuBar);
        }

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("replace")) {
                replaceSymbols();
            }
            if (e.getActionCommand().equals("save")) {
                saveResultFromConvertion("Save " + getTitle());
            }
        }

        protected void saveResultFromConvertion(String dialogTitlePrefix) {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle(dialogTitlePrefix + getCurrentobjecttoconvert());
            int returnVal = fc.showSaveDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try {
                    gCanvas.getGrammar().toFile(new FileWriter(file));
                    JOptionPane.showInternalMessageDialog(this, "The result has been saved!", "Saving status", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ouch) {
                    System.err.println("[" + (new java.util.Date()).toString() + "]" + this.getClass().getName() + " Not saved: ");
                    ouch.printStackTrace();
                    JOptionPane.showInternalMessageDialog(this, "El resultado NO fue guardado!", "Saving status", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        protected void replaceSymbols() {
            JGrammarSymbolReplacer jgsr = new JGrammarSymbolReplacer(gCanvas.getGrammar().getN(), gCanvas.getGrammar().getT());
            JScrollPane sp = new JScrollPane(jgsr, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            Debug.println("\nAntes: " + gCanvas.getGrammar());
            Object[] options = { "OK", "CANCEL" };
            int response = JOptionPane.showInternalOptionDialog(this, sp, "Replacer " + getTitle(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (response == JOptionPane.OK_OPTION) {
                Hashtable symbolsToReplace = jgsr.getPairsToReplace();
                Object a[] = symbolsToReplace.keySet().toArray();
                for (int i = 0; i < a.length; i++) gCanvas.getGrammar().replaceSymbol((Symbol) a[i], (Symbol) symbolsToReplace.get(a[i]));
                gCanvas.displayGrammar();
                getContentPane().validate();
            }
            Debug.println("\nDespu�s: " + gCanvas.getGrammar());
        }
    }
}
