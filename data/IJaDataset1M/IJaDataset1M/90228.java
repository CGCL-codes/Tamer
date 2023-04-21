package MainProg;

import MyForms.*;

/**
 * Instanzen dieser Klasse repräsentieren einen FormManager der die
 * verschiedenen Fenster die angezeigt werden können verwaltet.
 * @author Nico
 */
public class FormManager {

    /** Das Fenster für die Anmeldung. */
    private MainLogin fMainLogin;

    /** Das erste Fenster für die Registrierung. */
    private Registration fRegistration;

    /** Das erste Fenster für die Benachrichtigung in einem Fenster. */
    private FormMessage fFormMessage;

    /** Das zweite Fenster für die Registrierung. */
    private RegistrationPart2 fRegistration2;

    /** Das Fenster für die Auswahl eines Chatraumes. */
    private ChatroomSelection fChatroomSelection;

    /** Das Fenster für die Erstellung eines Chatraumes. */
    private ChatroomCreation fChatroomCreation;

    /** Das Fenster für den Chatraume. */
    private Chatroom fChatroom;

    /**
     * Erstellt eine neuen Formmanager zum verwalten der einzelen Fenster.
     */
    public FormManager() {
    }

    /**
     * Gibt das Fenster für den Chatraum zurück.
     * @return  das Fenster für den Chatraum.
     */
    public Chatroom getFormChatroom() {
        if (this.fChatroom == null) {
            System.out.println("fChatroom ist null");
            this.fChatroom = new Chatroom();
        }
        return this.fChatroom;
    }

    /**
     * Löscht das Fenster für den Chatraum.
     */
    public void deleteFormChatroom() {
        if (this.fChatroom != null) {
            this.fChatroom.stopThread();
            this.fChatroom = null;
        }
    }

    /**
     * Gibt das Fenster für die Chatraumerstellung zurück.
     * @return  das Fenster für die Chatraumerstellung.
     */
    public ChatroomCreation getFormChatroomCreation() {
        if (this.fChatroomCreation == null) {
            this.fChatroomCreation = new ChatroomCreation("Chatraum Erstellung");
        }
        return this.fChatroomCreation;
    }

    /**
     * Löscht das Fenster für die Chatraumerstellung.
     */
    public void deleteFormChatroomCreation() {
        if (this.fChatroomCreation != null) {
            this.fChatroomCreation.stopThread();
            this.fChatroomCreation = null;
        }
    }

    /**
     * Gibt das Fenster für die Chatraumauswahl zurück.
     * @return  das Fenster für die Chatraumauswahl.
     */
    public ChatroomSelection getFormChatroomSelection() {
        if (this.fChatroomSelection == null) {
            this.fChatroomSelection = new ChatroomSelection("Chatraum Erstellung/Auswahl");
        }
        return this.fChatroomSelection;
    }

    /**
     * Löscht das Fenster für die Chatraumauswahl.
     */
    public void deleteFormChatroomSelection() {
        if (this.fChatroomSelection != null) {
            this.fChatroomSelection.stopThread();
            this.fChatroomSelection = null;
        }
    }

    /**
     * Gibt das Fenster für die Anmeldung zurück.
     * @return  das Fenster für die Anmeldung.
     */
    public MainLogin getFormMainLogin() {
        if (this.fMainLogin == null) {
            this.fMainLogin = new MainLogin("Anmeldung");
        }
        return this.fMainLogin;
    }

    /**
     * Löscht das Fenster für die Anmeldung.
     */
    public void deleteFormMainLogin() {
        if (this.fMainLogin != null) {
            this.fMainLogin.stopThread();
            this.fMainLogin = null;
        }
    }

    /**
     * Gibt das Fenster für die Registrierung zurück.
     * @return  das Fenster für die Registrierung.
     */
    public Registration getFormRegistration() {
        if (this.fRegistration == null) {
            this.fRegistration = new Registration("Registrierung");
        }
        return this.fRegistration;
    }

    /**
     * Löscht das Fenster für die Registrierung.
     */
    public void deleteFormRegistration() {
        if (this.fRegistration != null) {
            this.fRegistration.stopThread();
            this.fRegistration = null;
        }
    }

    /**
     * Gibt das Fenster für die Registierung zweiter Teil zurück.
     * @return  das Fenster für die Registrierung zweiter Teil.
     */
    public RegistrationPart2 getFormRegistration2() {
        if (this.fRegistration2 == null) {
            this.fRegistration2 = new RegistrationPart2("Registrierung");
        }
        return this.fRegistration2;
    }

    /**
     * Löscht das Fenster für die Registrierung zweiter Teil.
     */
    public void deleteFormRegistration2() {
        if (this.fRegistration2 != null) {
            this.fRegistration2.stopThread();
            this.fRegistration2 = null;
        }
    }

    /**
     * Gibt das Fenster für die Formnachricht zurück.
     * @return  das Fenster für die Formnachricht.
     */
    public FormMessage getFormFormMessage() {
        if (this.fFormMessage == null) {
            this.fFormMessage = new FormMessage("Info", "ein infostring");
        }
        return this.fFormMessage;
    }

    /**
     * Löscht das Fenster für die Formnachricht.
     */
    public void deleteFormFormMessage() {
        if (this.fFormMessage != null) {
            this.fFormMessage = null;
        }
    }
}
