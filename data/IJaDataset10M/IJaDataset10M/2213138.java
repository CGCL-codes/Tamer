package spellcast.server;

import java.awt.*;
import java.beans.*;
import java.net.*;
import java.util.*;
import java.io.*;
import org.log4j.*;
import spellcast.beings.*;
import spellcast.event.*;
import spellcast.game.*;
import spellcast.model.*;
import spellcast.net.*;

public class sengine extends senginei implements PropertyChangeListener {

    private Game theGame;

    private ServerConnectionHandler serverConnectionHandler;

    private GameEvent currentGameEvent;

    private static final Category cat = Category.getInstance("server.engine");

    public sengine(ServerConnectionHandler serverConnectionHandler, Game theGame) {
        this.serverConnectionHandler = serverConnectionHandler;
        this.theGame = theGame;
    }

    public int sengine_execute(String args[]) {
        int feedback;
        feedback = execute();
        return (feedback);
    }

    public void initialise_the_program() {
        the_next_event = ok_event;
    }

    public void get_external_event() {
        currentGameEvent = serverConnectionHandler.receive();
        the_next_event = mapGameEventToStateMachineEvent();
    }

    public int mapGameEventToStateMachineEvent() {
        int result = terminate_event;
        if (currentGameEvent instanceof ConnectionRequestEvent) {
            result = connect_event;
        } else if (currentGameEvent instanceof DisconnectionRequestEvent) {
            result = disconnect_event;
        } else if (currentGameEvent instanceof MessageEvent) {
            result = message_event;
        } else if (currentGameEvent instanceof OkEvent) {
            result = ok_event;
        }
        return result;
    }

    public void add_observer() {
    }

    /**
     * Checks for duplicate IDs (fatal) and duplicate names (add uniquifier)
     * before adding the wizard to the system.
     * Sends the ConnectionEvent for this wizard as well as sending one to the
     * connecting host for every wizard already connected.
     */
    public void add_wizard() {
        ConnectionRequestEvent event = (ConnectionRequestEvent) currentGameEvent;
        Wizard duplicate = theGame.getWizard(event.getOriginator());
        if (duplicate != null) {
            cat.error("Can not add wizard with duplicate wizard id " + duplicate.getID());
            return;
        }
        String originalWizardName = event.getWizardName().trim();
        String wizardName = originalWizardName;
        int uniquifier = 1;
        boolean nameMatched = false;
        do {
            nameMatched = false;
            Iterator wizardIterator = theGame.getWizards().listIterator();
            while (wizardIterator.hasNext()) {
                Wizard w = (Wizard) wizardIterator.next();
                if (w.getName().equals(wizardName)) {
                    wizardName = originalWizardName + " (" + uniquifier + ")";
                    uniquifier++;
                    nameMatched = true;
                    break;
                }
            }
        } while (nameMatched);
        Gender gender = Gender.getGender(event.getGender());
        if (gender == null) {
            cat.error("Unknown gender specified (" + event.getGender() + ")");
            return;
        }
        Wizard newWizard = new Wizard(event.getOriginator(), wizardName, gender);
        serverConnectionHandler.send(new ConnectionEvent(event.getOriginator(), ID.EVERYONE, newWizard));
        Iterator wizardIterator = theGame.getWizards().iterator();
        while (wizardIterator.hasNext()) {
            Wizard existingWizard = (Wizard) wizardIterator.next();
            serverConnectionHandler.send(new ConnectionEvent(ID.NO_ONE, event.getOriginator(), existingWizard));
        }
        theGame.addWizard(newWizard);
        newWizard.addPropertyChangeListener(this);
    }

    public void game_over() {
    }

    public void remove_wizard() {
        DisconnectionRequestEvent event = (DisconnectionRequestEvent) currentGameEvent;
        Wizard w = theGame.getWizard(event.getOriginator());
        if (w != null) {
            theGame.removeWizard(w);
            serverConnectionHandler.send(new DisconnectionEvent(w));
        } else {
            cat.warn("Could not find wizard to remove.");
        }
    }

    public void send_message() {
        MessageEvent event = (MessageEvent) currentGameEvent;
        Wizard w = theGame.getWizard(event.getOriginator());
        MessageEvent response = new MessageEvent(event.getOriginator(), event.getTarget(), event.getTarget(), "You say: " + event.getMessageForOriginator(), w.getName() + " says: " + event.getMessageForTarget());
        serverConnectionHandler.send(response);
    }

    public void start_game() {
    }

    public void terminate_the_program() {
    }

    public void toggle_wizard_ready() {
        OkEvent event = (OkEvent) currentGameEvent;
        Wizard w = theGame.getWizard(event.getOriginator());
        w.toggleReady();
    }

    public void check_game_start() {
    }

    /**
     * All property change events are forwarded to all clients
     */
    public void propertyChange(PropertyChangeEvent pce) {
        GameUpdateEvent response = new GameUpdateEvent(ID.EVERYONE, pce);
        cat.error("Received in sengine " + pce.getSource() + "\n" + "Property (" + pce.getPropertyName() + ") = " + pce.getNewValue());
        serverConnectionHandler.send(response);
    }
}
