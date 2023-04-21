package fr.univ_tln.inf9.exaplanning.controleur.administration;

import java.util.List;
import fr.univ_tln.inf9.exaplanning.api.administration.Administrator;
import fr.univ_tln.inf9.exaplanning.api.administration.Ticket;

/**
 * 
 * @author pivi
 *
 */
public class AdminController {

    public AdminView listView = null;

    private Administrator a = null;

    public AdminController(Administrator a) {
        this.a = a;
        addToListTicketListener();
    }

    private void addToListTicketListener() {
        a.addListTicketListener(listView);
    }

    public void displayViews() {
        listView.display();
    }

    public void closeViews() {
        listView.close();
    }

    public void notifyListTicketChanged(List<String> ticketsAdmin) {
        a.setTicketsAdmin(ticketsAdmin);
    }

    public void notifyTicketAddedToList(Ticket t) {
        a.add(t);
    }

    public void notifyRoomRemovedToList(Ticket t) {
        a.remove(t);
    }
}
