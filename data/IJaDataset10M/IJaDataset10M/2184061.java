package paolomind.multitalk.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import paolomind.multitalk.plugin.Module;
import paolomind.multitalk.plugin.ModuleManager;

/**
 * Un ModuleManager che implementa l' AWT ActionListener.
 * Associando l'oggetto ad un Button o AbstractButton, cattura l'evento d'azione
 * per selezionare un tool in base al ActionCommand
 * associato all'evento scatenato
 * @author paolo
 *
 */
public class ActionCommandModule extends ModuleManager implements ActionListener {

    /**
   * Catturato l'evento, seleziona il modulol.
   * @param e evento catturato,
   *                    tale evento deve aver specificato l'ActionCommand
   *                    con l' id del tool da selezionare
   * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
   */
    public final void actionPerformed(final ActionEvent e) {
        if (this.select(e.getActionCommand())) {
            this.activate();
        }
    }

    /**
   * Metodo che si occupa di assegnare l'ActionListner ad un java.awt.Button e
   * inizializzare l'ActionCommand.
   * @param m modulo da associare al bottone
   * @param b il bottone da inizializzare
   * @return il bottone inizializzato (non &egrave; necessario reperirlo)
   */
    public final java.awt.Button setupAction(final Module m, final java.awt.Button b) {
        this.register(m);
        b.setActionCommand(m.getSelfId());
        b.addActionListener(this);
        return b;
    }

    /**
   * Metodo che si occupa di assegnare l'ActionListener
   * ad un javax.swing.AbstractButton e
   * inizializzare l'ActionCommand.
   * @param m modulo da associare al bottone
   * @param b il bottone da inizializzare
   * @return il bottone inizializzato (non &egrave; necessario reperirlo)
   */
    public final javax.swing.AbstractButton setupAction(final Module m, final javax.swing.AbstractButton b) {
        this.register(m);
        b.setActionCommand(m.getSelfId());
        b.addActionListener(this);
        return b;
    }
}
