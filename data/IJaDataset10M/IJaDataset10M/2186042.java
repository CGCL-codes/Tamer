package totalpos;

import java.util.List;

/**
 *
 * @author Saul Hidalgo
 */
public interface Services extends java.rmi.Remote {

    public void initialize(String myDay, String storeName) throws java.rmi.RemoteException, java.sql.SQLException;

    public void deleteDataFrom() throws java.rmi.RemoteException, java.sql.SQLException;

    public void sendCreditNotesReceipt(List<ReceiptSap> creditNotes, List<ReceiptSap> receipts, List<Client> clients) throws java.rmi.RemoteException, java.sql.SQLException;

    public void createDummySeller() throws java.rmi.RemoteException, java.sql.SQLException;
}
