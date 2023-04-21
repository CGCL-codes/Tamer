package jaguar.grammar.jgrammar;

import jaguar.structures.*;
import jaguar.machine.util.jutil.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/** 
 * La interface para reemplazar s�mbolos
 * 
 * @author Ivan Hern�ndez Serrano <ivanx@users.sourceforge.net>
 * @version 0.1
 */
public class JGrammarSymbolReplacer extends JPanel {

    /**
     * El alfabeto de no terminales de entrada
     */
    protected Alphabet N;

    /**
     * funcion de acceso para obtener el valor de N
     * @return el valor actual de N
     * @see #N
     */
    public Alphabet getN() {
        return N;
    }

    /**
     * funcion de acceso para modificar N
     * @param new_N el nuevo valor para N
     * @see #N
     */
    public void setN(Alphabet new_N) {
        N = new_N;
    }

    /**
     * El alfabeto de  terminales de entrada
     */
    protected Alphabet T;

    /**
     * funcion de acceso para obtener el valor de T
     * @return el valor actual de T
     * @see #T
     */
    public Alphabet getT() {
        return T;
    }

    /**
     * funcion de acceso para modificar T
     * @param new_T el nuevo valor para T
     * @see #T
     */
    public void setT(Alphabet new_T) {
        T = new_T;
    }

    /**
     * Constructor sin par�metros.
     * Inicializa el objeto usando los valores por omision.
     */
    public JGrammarSymbolReplacer(Alphabet N, Alphabet T) {
        super();
        this.N = N;
        this.T = T;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        createContent();
    }

    public void createContent() {
        JPanel symbolsList = new JPanel();
        JPanel NPanel = new JPanel(), TPanel = new JPanel();
        symbolsList.setLayout(new BoxLayout(symbolsList, BoxLayout.Y_AXIS));
        NPanel.setLayout(new BoxLayout(NPanel, BoxLayout.Y_AXIS));
        TPanel.setLayout(new BoxLayout(TPanel, BoxLayout.Y_AXIS));
        pairInfo = new Hashtable();
        CheckBoxListener myListener = new CheckBoxListener();
        JCheckBox currentCheckB;
        JTextField currentTextF;
        JPanel pairPanel;
        Vector vInfo;
        Object nA[] = getN().toArray();
        for (int i = 0; i < nA.length; i++) {
            currentCheckB = new JCheckBox(nA[i].toString(), false);
            currentCheckB.addItemListener(myListener);
            currentTextF = new JTextField(20);
            currentTextF.setEnabled(false);
            vInfo = new Vector();
            vInfo.add(nA[i]);
            vInfo.add(currentTextF);
            pairInfo.put(currentCheckB, vInfo);
            pairPanel = new JPanel();
            pairPanel.setLayout(new BoxLayout(pairPanel, BoxLayout.X_AXIS));
            pairPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            pairPanel.add(currentCheckB);
            pairPanel.add(Box.createHorizontalGlue());
            pairPanel.add(currentTextF);
            pairPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            NPanel.add(pairPanel);
        }
        Object tA[] = getT().toArray();
        for (int i = 0; i < tA.length; i++) {
            currentCheckB = new JCheckBox(tA[i].toString(), false);
            currentCheckB.addItemListener(myListener);
            currentTextF = new JTextField(20);
            currentTextF.setEnabled(false);
            vInfo = new Vector();
            vInfo.add(tA[i]);
            vInfo.add(currentTextF);
            pairInfo.put(currentCheckB, vInfo);
            pairPanel = new JPanel();
            pairPanel.setLayout(new BoxLayout(pairPanel, BoxLayout.X_AXIS));
            pairPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            pairPanel.add(currentCheckB);
            pairPanel.add(Box.createHorizontalGlue());
            pairPanel.add(currentTextF);
            pairPanel.add(Box.createRigidArea(new Dimension(10, 0)));
            TPanel.add(pairPanel);
        }
        symbolsList.add(JMachineFrame.createJPanelBorder(NPanel, "N"));
        symbolsList.add(Box.createVerticalGlue());
        symbolsList.add(JMachineFrame.createJPanelBorder(TPanel, "T"));
        add(symbolsList);
    }

    /** 
     * Regresa una tabla de hash donde en la primer entrada est� el s�mbolo a reemplazar y en la segunda entrada esta el nuevo s�mbolo .
     *
     */
    public Hashtable getPairsToReplace() {
        Symbol currNewSymbol;
        Vector currPair;
        Hashtable result = new Hashtable();
        Object keys[] = pairInfo.keySet().toArray();
        for (int i = 0; i < keys.length; i++) if (((JCheckBox) keys[i]).isSelected()) {
            currPair = (Vector) pairInfo.get(keys[i]);
            currNewSymbol = new Symbol(((JTextField) currPair.get(1)).getText());
            result.put(currPair.get(0), currNewSymbol);
        }
        return result;
    }

    public void cancelSelections() {
        setPairinfo(new Hashtable());
    }

    /**
     * Aqui estan todos los simbolos con la info asociada para ver si tenemos que cambiar alguno 
     */
    protected Hashtable pairInfo;

    /**
     * funcion de acceso para obtener el valor de pairInfo
     * @return el valor actual de pairInfo
     * @see #pairInfo
     */
    public Hashtable getPairinfo() {
        return pairInfo;
    }

    /**
     * funcion de acceso para modificar pairInfo
     * @param new_pairInfo el nuevo valor para pairInfo
     * @see #pairInfo
     */
    public void setPairinfo(Hashtable new_pairInfo) {
        pairInfo = new_pairInfo;
    }

    /**
     * Regresa una cadena con una representaci�n del objeto.
     * Toma los campos y los imprime en una lista junto con sus valores.
     *
     * @return una cadena con los valores del objeto.
     */
    public String toString() {
        String salida = "";
        return salida;
    }

    class CheckBoxListener implements ItemListener {

        public void itemStateChanged(ItemEvent e) {
            Object source = e.getItemSelectable();
            Vector vpair = (Vector) pairInfo.get(source);
            if (e.getStateChange() == ItemEvent.SELECTED) ((JTextField) vpair.get(1)).setEnabled(true); else ((JTextField) vpair.get(1)).setEnabled(false);
        }
    }
}
