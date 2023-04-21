package scheme4j.gui.listeners;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import javax.swing.JTextField;

public class HistoryListener extends KeyAdapter {

    private static final int historyLength = 20;

    private int historyPointer = -1;

    private LinkedList historyList;

    private JTextField commandField;

    public HistoryListener(JTextField textField) {
        commandField = textField;
        historyList = new LinkedList();
    }

    /** metodo invocato quando un tasto viene premuto */
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()) {
            case KeyEvent.VK_UP:
                commandField.setText(getPrevious());
                break;
            case KeyEvent.VK_DOWN:
                commandField.setText(getNext());
                break;
            case KeyEvent.VK_ENTER:
                addCommand(commandField.getText());
                break;
        }
    }

    /** inserisce un comando in testa alla lista */
    private void addCommand(String text) {
        historyList.addFirst(text);
        if (historyList.size() > historyLength) historyList.removeLast();
        historyPointer = -1;
    }

    /** restituisce il comando precedente */
    private String getPrevious() {
        if (historyPointer + 1 < historyList.size()) return (String) historyList.get(++historyPointer); else if (historyPointer == -1) return ""; else return (String) historyList.getLast();
    }

    /** restituisce il comando successivo */
    private String getNext() {
        if (historyPointer - 1 >= 0) return (String) historyList.get(--historyPointer); else if (historyPointer == 0) --historyPointer;
        return "";
    }
}
