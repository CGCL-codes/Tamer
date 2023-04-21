package totalpos;

import java.rmi.RemoteException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author shidalgo
 */
public class ServiceImplement extends java.rmi.server.UnicastRemoteObject implements Services {

    private String myDay;

    private String storeName;

    public ServiceImplement() throws java.rmi.RemoteException {
        super();
    }

    @Override
    public void deleteDataFrom() throws RemoteException, java.sql.SQLException {
        System.out.println(Shared.now() + "Iniciando eliminacion de " + myDay + " - " + storeName);
        PreparedStatement stmt = Constants.connection.prepareStatement("delete from " + Constants.dbHeader + ".ZSDS_POS_FACT where WERKS = ? and FKDAT = ? ");
        stmt.setString(1, storeName);
        stmt.setString(2, myDay);
        stmt.executeUpdate();
        stmt = Constants.connection.prepareStatement("delete from " + Constants.dbHeader + ".ZSDS_CAB_FACT where WERKS = ? and FKDAT = ? ");
        stmt.setString(1, storeName);
        stmt.setString(2, myDay);
        stmt.executeUpdate();
        stmt = Constants.connection.prepareStatement("delete from " + Constants.dbHeader + ".ZSDS_POS_DEV where WERKS = ? and FKDAT = ? ");
        stmt.setString(1, storeName);
        stmt.setString(2, myDay);
        stmt.executeUpdate();
        stmt = Constants.connection.prepareStatement("delete from " + Constants.dbHeader + ".ZSDS_CAB_DEV where WERKS = ? and FKDAT = ? ");
        stmt.setString(1, storeName);
        stmt.setString(2, myDay);
        stmt.executeUpdate();
        stmt = Constants.connection.prepareStatement("delete from " + Constants.dbHeader + ".ZSDS_VEND_FACT where WERKS = ? and FKDAT = ? ");
        stmt.setString(1, storeName);
        stmt.setString(2, myDay);
        stmt.executeUpdate();
        System.out.println(Shared.now() + " Eliminado " + myDay + " - " + storeName);
    }

    @Override
    public void initialize(String myDay, String storeName) throws RemoteException, java.sql.SQLException {
        try {
            System.out.println(Shared.now() + " Creando Conexion... ");
            this.myDay = myDay.replace("-", "");
            this.storeName = storeName;
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String url = "jdbc:sqlserver://" + Constants.serverSQLServerAdd + ":" + Constants.portSqlServer + ";DatabaseName=" + Constants.dbRMIName;
            Constants.connection = DriverManager.getConnection(url, Constants.dbRMIUser, Constants.dbRMIPass);
            System.out.println(Shared.now() + " Conexion creada!");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceImplement.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void sendCreditNotesReceipt(List<ReceiptSap> creditNotes, List<ReceiptSap> receipts, List<Client> clients) throws RemoteException, SQLException {
        System.out.println(Shared.now() + " Iniciando envío de ventas...");
        PreparedStatement stmt;
        for (ReceiptSap receiptSap : creditNotes) {
            stmt = Constants.connection.prepareStatement("insert into " + Constants.dbHeader + ".ZSDS_CAB_DEV ( MANDT , FKDAT , VBELN , WERKS , ZTIPV , " + "KUNNR , RANGO , REPOZ , IMPRE , WAERS ) values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ) ");
            stmt.setString(1, Constants.mant);
            stmt.setString(2, myDay);
            stmt.setString(3, "D" + receiptSap.getId());
            stmt.setString(4, storeName);
            stmt.setString(5, receiptSap.getKind());
            stmt.setString(6, receiptSap.getClient());
            stmt.setString(7, receiptSap.getMinFiscalId() + "-" + receiptSap.getMaxFiscalId());
            stmt.setString(8, receiptSap.getZ());
            stmt.setString(9, receiptSap.getPrinterId());
            stmt.setString(10, Constants.waerks);
            stmt.executeUpdate();
            int position = 1;
            for (Receipt receipt : receiptSap.receipts) {
                for (Item2Receipt item2Receipt : receipt.getItems()) {
                    stmt = Constants.connection.prepareStatement("insert into " + Constants.dbHeader + ".ZSDS_POS_DEV ( MANDT , FKDAT , VBELN , POSNR , EAN11 , " + "KWMENG , VRKME , CHARG , KBETP , KBETD , PERNR , WERKS ) values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ) ");
                    stmt.setString(1, Constants.mant);
                    stmt.setString(2, myDay);
                    stmt.setString(3, "D" + receiptSap.getId());
                    stmt.setString(4, Constants.df2intSAP.format(position++));
                    stmt.setString(5, item2Receipt.getItem().getBarcodes().get(0));
                    stmt.setString(6, item2Receipt.getQuant().toString());
                    stmt.setString(7, item2Receipt.getItem().getSellUnits());
                    stmt.setString(8, "");
                    stmt.setDouble(9, item2Receipt.getSellPrice());
                    stmt.setDouble(10, (item2Receipt.getSellDiscount() / 100.0) * item2Receipt.getSellPrice());
                    stmt.setString(11, Constants.pernr);
                    stmt.setString(12, storeName);
                    stmt.executeUpdate();
                }
            }
            System.out.println(Shared.now() + " Agregada Nota de Crédito " + receiptSap.getMinFiscalId() + "-" + receiptSap.getMaxFiscalId());
        }
        Double gDisc = .0;
        for (ReceiptSap receiptSap : receipts) {
            stmt = Constants.connection.prepareStatement("insert into " + Constants.dbHeader + ".ZSDS_CAB_FACT ( MANDT , FKDAT , VBELN , WERKS , ZTIPV , " + "KUNNR , RANGO , REPOZ , IMPRE , WAERS ) values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ) ");
            stmt.setString(1, Constants.mant);
            stmt.setString(2, myDay);
            stmt.setString(3, "F" + receiptSap.getId());
            stmt.setString(4, storeName);
            stmt.setString(5, receiptSap.getKind());
            stmt.setString(6, receiptSap.getClient());
            stmt.setString(7, receiptSap.getMinFiscalId() + "-" + receiptSap.getMaxFiscalId());
            stmt.setString(8, receiptSap.getZ());
            stmt.setString(9, receiptSap.getPrinterId());
            stmt.setString(10, Constants.waerks);
            stmt.executeUpdate();
            int position = 1;
            for (Receipt receipt : receiptSap.receipts) {
                gDisc = receipt.getGlobalDiscount();
                for (Item2Receipt item2Receipt : receipt.getItems()) {
                    stmt = Constants.connection.prepareStatement("insert into " + Constants.dbHeader + ".ZSDS_POS_FACT ( MANDT , FKDAT , VBELN , POSNR , EAN11 , " + "KWMENG , VRKME , CHARG , KBETP , KBETD , PERNR , WERKS ) values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ) ");
                    stmt.setString(1, Constants.mant);
                    stmt.setString(2, myDay);
                    stmt.setString(3, "F" + receiptSap.getId());
                    stmt.setString(4, Constants.df2intSAP.format(position++));
                    stmt.setString(5, item2Receipt.getItem().getBarcodes().get(0));
                    stmt.setString(6, item2Receipt.getQuant().toString());
                    stmt.setString(7, item2Receipt.getItem().getSellUnits());
                    stmt.setString(8, "");
                    stmt.setDouble(9, item2Receipt.getSellPrice());
                    Double tmpD = (item2Receipt.getSellDiscount() / 100.0) * item2Receipt.getSellPrice();
                    stmt.setDouble(10, tmpD + gDisc * (item2Receipt.getSellPrice() - tmpD));
                    stmt.setString(11, Constants.pernr);
                    stmt.setString(12, storeName);
                    stmt.executeUpdate();
                }
            }
            System.out.println(Shared.now() + " Agregada la factura " + receiptSap.getMinFiscalId() + "-" + receiptSap.getMaxFiscalId());
        }
        for (Client client : clients) {
            stmt = Constants.connection.prepareStatement("delete from " + Constants.dbHeader + ".ZSDS_CLIENT where FKDAT = ? and KUNNR = ? ");
            stmt.setString(1, myDay);
            stmt.setString(2, client.getId());
            stmt.executeUpdate();
            stmt = Constants.connection.prepareStatement("insert into " + Constants.dbHeader + ".ZSDS_CLIENT ( MANDT , FKDAT , KUNNR , NAME1 , ADRNR , WAERS )" + " values ( ? , ? , ? , ? , ? , ? ) ");
            stmt.setString(1, Constants.mant);
            stmt.setString(2, myDay);
            stmt.setString(3, client.getId());
            stmt.setString(4, client.getName());
            stmt.setString(5, client.getAddress() + " Telf: " + client.getPhone());
            stmt.setString(6, Constants.waerks);
            stmt.executeUpdate();
        }
        System.out.println(Shared.now() + " Envío Finalizado Satisfactoriamente!");
    }

    @Override
    public void createDummySeller() throws RemoteException, SQLException {
        PreparedStatement stmt = Constants.connection.prepareStatement("insert into " + Constants.dbHeader + ".ZSDS_VEND_FACT ( MANDT , VBELN , POSNR , FKDAT , WERKS , " + "PARTN_ROLE , PARTN_NUMB ) values ( ? , ? , ? , ? , ? , ? , ? )");
        stmt.setString(1, Constants.mant);
        stmt.setString(2, Constants.vbeln);
        stmt.setString(3, "000001");
        stmt.setString(4, myDay);
        stmt.setString(5, storeName);
        stmt.setString(6, "99");
        stmt.setString(7, Constants.vbeln);
        stmt.executeUpdate();
    }
}
