package paolomind.multitalk.plugin;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Properties;
import paolomind.commons.NamedObject;
import paolomind.multitalk.netmessage.IMessage;
import paolomind.multitalk.netmessage.MessageException;
import paolomind.multitalk.netmessage.Sender;

/**
 * classe per gestire un insieme di tool.
 *
 * @author paolo
 */
public class ToolManager implements paolomind.multitalk.plugin.ToolInterface, paolomind.commons.ObjectContainer, paolomind.multitalk.netmessage.Sender {

    /** */
    private String name;

    /** */
    private ToolInterface current;

    /** */
    private java.util.Map toolmap;

    /** */
    private Sender psender;

    /**
   * costruttore che inizializza l'id del manager.
   *
   * @param pname
   *            id del manager
   */
    public ToolManager(final String pname) {
        this(pname, null, new java.util.HashMap());
    }

    /**
   * costruttore che inizializza l'id del manager ed il sender.
   *
   * @param pname
   *            id del manager
   * @param s
   *            il sender
   */
    public ToolManager(final String pname, final Sender s) {
        this(pname, s, new java.util.HashMap());
    }

    /**
   * costruttore che inizializza il sender.
   *
   * @param s
   *            il sender.
   */
    public ToolManager(final Sender s) {
        this(null, s);
    }

    /**
   * costruttore che inizializza l'id del manager, il sender e la mappa di tool.
   *
   * @param pname
   *            id del manager
   * @param s
   *            il sender
   * @param m
   *            mappa dei tool
   */
    public ToolManager(final String pname, final Sender s, final java.util.Map m) {
        toolmap = m;
        psender = s;
        this.name = pname;
    }

    /**
   * rimanda l'azione al tool corrente selezionato.
   *
   * @see paolomind.multitalk.plugin.ToolInterface#select()
   */
    public final void select() {
        if (current != null) {
            current.select();
        }
    }

    /**
   * restituisce il proprio id.
   * @return  il proprio id
   * @see paolomind.commons.NamedObject#getSelfId()
   */
    public final String getSelfId() {
        return name;
    }

    /**
   * rimanda l'azione al tool corrente selezionato.
   *
   * @param e
   *            evento
   * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
   */
    public final void mouseClicked(final MouseEvent e) {
        if (current != null) {
            current.mouseClicked(e);
        }
    }

    /**
   * rimanda l'azione al tool corrente selezionato.
   *
   * @param e
   *            evento
   * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
   */
    public final void mouseEntered(final MouseEvent e) {
        if (current != null) {
            current.mouseEntered(e);
        }
    }

    /**
   * rimanda l'azione al tool corrente selezionato.
   *
   * @param e
   *            evento
   * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
   */
    public final void mouseExited(final MouseEvent e) {
        if (current != null) {
            current.mouseExited(e);
        }
    }

    /**
   * rimanda l'azione al tool corrente selezionato.
   *
   * @param e
   *            evento
   * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
   */
    public final void mousePressed(final MouseEvent e) {
        if (current != null) {
            current.mousePressed(e);
        }
    }

    /**
   * rimanda l'azione al tool corrente selezionato.
   *
   * @param e
   *            evento
   * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
   */
    public final void mouseReleased(final MouseEvent e) {
        if (current != null) {
            current.mouseReleased(e);
        }
    }

    /**
   * rimanda l'azione al tool corrente selezionato.
   *
   * @param e
   *            evento
   * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
   */
    public final void mouseDragged(final MouseEvent e) {
        if (current != null) {
            current.mouseDragged(e);
        }
    }

    /**
   * rimanda l'azione al tool corrente selezionato.
   *
   * @param e
   *            evento
   * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
   */
    public final void mouseMoved(final MouseEvent e) {
        if (current != null) {
            current.mouseMoved(e);
        }
    }

    /**
   * rimanda l'azione al tool corrente selezionato.
   *
   * @param e
   *            evento
   * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
   */
    public final void keyPressed(final KeyEvent e) {
        if (current != null) {
            current.keyPressed(e);
        }
    }

    /**
   * rimanda l'azione al tool corrente selezionato.
   *
   * @param e
   *            evento
   * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
   */
    public final void keyReleased(final KeyEvent e) {
        if (current != null) {
            current.keyReleased(e);
        }
    }

    /**
   * rimanda l'azione al tool corrente selezionato.
   *
   * @param e
   *            evento
   * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
   */
    public final void keyTyped(final KeyEvent e) {
        if (current != null) {
            current.keyTyped(e);
        }
    }

    /**
   * rinvia il messagio al tool specificato nel messaggio o al tool corrente.
   *
   * @param m
   *            messaggio ricevuto
   * @throws MessageException
   *             rilancia l'eccezzione se il messaggio non è riconosciuto
   * @see paolomind.multitalk.netmessage.Receiver#receive(paolomind.multitalk.netmessage.IMessage)
   */
    public final void receive(final IMessage m) throws MessageException {
        Object info = m.getInfo();
        if (info != null) {
            ToolInterface t = (ToolInterface) toolmap.get(info);
            if (t != null) {
                t.receive(m);
            } else {
                this.receive(m);
            }
        } else {
            throw new MessageException();
        }
    }

    /**
   * invia il messaggio al sender aggiungendo il proprio nome se specificato.
   *
   * @param m
   *            il messaggio da reinviare
   * @see paolomind.multitalk.netmessage.Sender#send(paolomind.multitalk.netmessage.IMessage)
   */
    public final void send(final IMessage m) {
        String s = this.getSelfId();
        if (s != null) {
            m.addInfo(s);
        }
        if (psender != null) {
            psender.send(m);
        }
    }

    /**
   * registra un ogetto.
   *
   * @param element
   *            il tool da registrare con il suo nome
   * @see paolomind.commons.ObjectContainer#register(paolomind.commons.NamedObject)
   */
    public final void register(final NamedObject element) {
        try {
            this.register((ToolInterface) element);
        } catch (ClassCastException e) {
            throw new UnsupportedOperationException("non è possibile inserire oggetti che non siano ToolInterface", e);
        }
    }

    /**
   * funzione di registrazione specifica per ToolManager.
   *
   * @param n
   *            identificativo del tool
   * @param t
   *            il ToolInterface
   */
    public final void register(final String n, final ToolInterface t) {
        toolmap.put(n, t);
        t.setSelfId(n);
    }

    /**
   * funzione di registrazione specifica per ToolManager.
   *
   * @param t
   *            il ToolInterface
   */
    public final void register(final ToolInterface t) {
        String s = t.getSelfId();
        if (s != null) {
            toolmap.put(s, t);
        } else {
            throw new NullPointerException("nome nullo per il NamedObject");
        }
    }

    /**
   * seleziona un tool registrato. il manager prende il comportamento di tale
   * oggetto
   *
   * @param pname
   *            identificativo dell'ogetto
   * @return true se l'oggetto esiste ed è stato selezionato, false altrimenti
   * @see paolomind.commons.ObjectContainer#select(java.lang.String)
   */
    public final boolean select(final String pname) {
        current = (ToolInterface) toolmap.get(pname);
        return (current != null);
    }

    /**
   * Propriet&agrave; del tool selezionato.<br/>
   *
   * @return Informazioni sul tool selezionato
   * @see paolomind.multitalk.plugin.ToolInterface#getPropertes()
   */
    public final Properties getPropertes() {
        if (current != null) {
            return current.getPropertes();
        }
        return null;
    }

    /**
   * setta l'id.
   *
   * @param pname
   *            l'id con cui sarà identificato il manager
   * @see paolomind.commons.NamedObject#setSelfId(java.lang.String)
   */
    public final void setSelfId(final String pname) {
        this.name = pname;
    }

    /**
   * reperisce un toolregistrato.
   *
   * @param pname
   *            nome del tool registrato
   * @return restituisce un ogetto registrato oppure null
   * @see paolomind.commons.ObjectContainer#get(java.lang.String)
   */
    public final Object get(final String pname) {
        return toolmap.get(name);
    }

    /**
   * restituisce tutti gli elementi.
   *
   * @return tutti gli elementi registrati
   * @see paolomind.commons.ObjectContainer#getAll()
   */
    public final Iterator getAll() {
        return toolmap.values().iterator();
    }
}
