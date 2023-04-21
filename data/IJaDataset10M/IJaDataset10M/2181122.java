package fh.bfi.pit.ebanking.interfaces;

import fh.bfi.pit.ebanking.model.BankingException;
import fh.bfi.pit.ebanking.model.Ticket;
import fh.bfi.pit.ebanking.model.User;
import java.util.List;

/**
 *
 * @author hugo
 */
public interface TicketInterface {

    public Ticket getTicket(int ticket_id) throws BankingException;

    public List<Ticket> getAllTickets() throws BankingException;

    public List<Ticket> getAllTicketsforUserSender(User u) throws BankingException;

    public List<Ticket> getAllTicketsforUserReceiver(User u) throws BankingException;

    public boolean saveTicket(Ticket t) throws BankingException;
}
