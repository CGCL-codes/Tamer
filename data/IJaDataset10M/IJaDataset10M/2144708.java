package domain;

import java.util.Arrays;
import javax.swing.text.BadLocationException;

/**
 * Met de klasse IRCChannel wordt een kanaal van de chat voorgesteld.
 * De klasse is de subklasse van de klasse IRC.
 * Methoden zijn protected.
 * Het bevat methoden die zaken toevoegt aan het StyledDocument en zaken in de DefaultListModel wijzigt.
 * @author David Covemaeker, Maarten Minnebo, Tim Van Thuyne, Toon Kint
 */
public class IRCChannel extends IRC {

    /**
	 * De constructor roept de constructor van IRC (superklasse) op.
	 * @param login De naam van degene die ingelogd is
	 */
    protected IRCChannel(String login) {
        super(login);
    }

    /**
	 * Loginnaam wordt toegevoegd aan de DefaultListModel.
	 * Maakt nieuwe array bestaande uit de oude array plus de nieuwe user.
	 * Roept addUsersToListModel op om nieuwe array door oude te vervangen.
	 * --> Na een JOIN message...
	 * @param login 
	 */
    protected void addUserToListModel(String login) {
        Object[] oldarray = listModel.toArray();
        String[] newarray = new String[oldarray.length + 1];
        for (int i = 0; i < oldarray.length; i++) {
            newarray[i] = (String) oldarray[i];
        }
        newarray[newarray.length - 1] = login;
        addUsersToListModel(newarray);
    }

    /**
	 * Loginnaam wordt verwijdert uit de DefaultListModel.
	 * --> Na een PART, KICK of QUIT message...
	 * @param login User dat moet verwijdert worden
	 */
    protected void removeUserFromListModel(String login) {
        if (listModel.contains(login)) listModel.removeElement(login);
        if (listModel.contains("@" + login)) listModel.removeElement("@" + login);
        if (listModel.contains("+" + login)) listModel.removeElement("+" + login);
    }

    /**
	 * Voegt bericht toe aan het StyledDocument dat meldt dat iemand op het kanaal is toegevoegd.
	 * --> Na een JOIN message...
	 * @param login De client die is toegevoegd aan het kanaal
	 */
    protected void addClientJoinedChannelMessage(String login) {
        try {
            styledDocument.insertString(styledDocument.getLength(), giveTime() + login + " is toegevoegd op het kanaal.\n", style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Voegt bericht toe aan het StyledDocument dat meldt dat iemand het kanaal verlaten heeft.
	 * --> Na een PART message...
	 * @param login Persoon die kanaal verlaat
	 * @param reason Reden van vertrek uit het kanaal
	 */
    protected void addClientPartChannelMessage(String login, String reason) {
        String r;
        r = (reason == "") ? "." : (": " + reason);
        try {
            styledDocument.insertString(styledDocument.getLength(), giveTime() + login + " heeft het kanaal verlaten" + r + "\n", style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Voegt bericht toe aan het StyledDocument dat meldt dat iemand uit het kanaal is gesmeten.
	 * --> Na een KICK message...
	 * @param kicker Degene die iemand anders uit het kanaal smijt
	 * @param victim Degene die uit het kanaal wordt gesmeten door de kicker
	 * @param reason De reden dat er iemand eruit gesmeten wordt
	 */
    protected void addClientKickedOutMessage(String kicker, String victim, String reason) {
        String r;
        r = (reason == "") ? "." : (": " + reason);
        try {
            styledDocument.insertString(styledDocument.getLength(), giveTime() + victim + " werd buitengekickt door " + kicker + ": " + r + ".\n", style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
	 * Zorgt ervoor dat een boodschap wordt toegevoegd aan het StyledDocument als iemand uit het kanaal zijn loginnaam veranderd heeft.
	 * --> Na een NICK message...
	 * @param oldName De oude loginnaam
	 * @param newName De nieuwe loginnaam
	 */
    @Override
    protected void addNickChangeMessage(String oldName, String newName) {
        if (listModel.contains(oldName) || listModel.contains("@" + oldName) || listModel.contains("+" + oldName)) {
            try {
                styledDocument.insertString(styledDocument.getLength(), giveTime() + " " + oldName + " heeft zijn nickname veranderd naar " + newName + ".\n", style);
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * De loginnamen van de users worden allemaal aan de DefaultListModel toegevoegd.
	 * Oude array wordt telkens vervangen door nieuwe na het toevoegen van een nieuwe user.
	 * --> Na de userListControl in de observer update in de IRCController
	 * @param arrayLogin De lijst met loginnamen
	 */
    protected void addUsersToListModel(String[] arrayLogin) {
        listModel.removeAllElements();
        Arrays.sort(arrayLogin);
        for (int i = 0; i < arrayLogin.length; i++) {
            listModel.addElement(arrayLogin[i]);
        }
    }

    /**
	 * In de DefaultListModel met de users de loginnaam van de bepaalde user veranderen.
	 * Controleren op de drie naam varianten:
	 * De gewone user, voiced of operator.
	 * --> Na een NICK message...
	 * @param oldName De oude loginnaam
	 * @param newName De nieuwe loginnaam
	 */
    @Override
    protected void changeLoginName(String oldName, String newName) {
        if (listModel.contains(oldName)) {
            listModel.set(listModel.indexOf(oldName), newName);
        }
        if (listModel.contains("@" + oldName)) {
            listModel.set(listModel.indexOf("@" + oldName), "@" + newName);
        }
        if (listModel.contains("+" + oldName)) {
            listModel.set(listModel.indexOf("+" + oldName), "+" + newName);
        }
    }
}
