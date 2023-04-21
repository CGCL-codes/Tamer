package server;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import client.shoppingCartFrame.shoppingCart.ShoppingCartItem;
import server.product.Tickets;
import server.product.Tickets.Ticket;
import server.Transaction.*;

public class BillSysMySQL {

    private Connection connect = null;

    private Statement statement = null;

    private PreparedStatement preparedStatement = null;

    private ResultSet resultSet = null;

    ResourceBundle bundle = ResourceBundle.getBundle("server/billsys");

    private List<Ticket> ticketList = new ArrayList<Ticket>();

    public Tickets ticketX = new Tickets();

    JAXBContext context;

    StringWriter writer;

    String test;

    ServerIF serverUI;

    public BillSysMySQL(ServerIF serv) {
        serverUI = serv;
    }

    public void connectMySQL() throws Exception {
        try {
            String url = bundle.getString("URL");
            Class.forName(bundle.getString("Driver"));
            connect = DriverManager.getConnection(url, bundle.getString("user"), bundle.getString("password"));
            if (connect.isValid(2)) {
                displayMsg("CONNECTED");
                System.out.println("CONNECTED");
            } else {
                displayMsg("NOT CONNECTED");
            }
        } catch (Exception e) {
            System.out.println("Unable to load the driver class!");
        }
    }

    public void commitTransaction(Transaction transaction) throws Exception {
        String query = ("insert into  billsys_db.ticket_order (transaction_nr, " + "userID, ticket_order_total) values (?, ?, ?)");
        try {
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1, transaction.getTransactionNr());
            preparedStatement.setInt(2, transaction.getSeller());
            preparedStatement.setInt(3, transaction.getTotal());
            preparedStatement.execute();
            for (int i = 0; i < transaction.getItem().toArray().length; i++) {
                String query2 = ("insert into billsys_db.ticket_item (" + "transaction_nr, ticketID, quantity) values (?, ?, ?)");
                preparedStatement = connect.prepareStatement(query2);
                preparedStatement.setString(1, transaction.getItem().get(i).getReference());
                preparedStatement.setInt(2, transaction.getItem().get(i).getProduct());
                preparedStatement.setInt(3, transaction.getItem().get(i).getQuantity());
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            displayMsg("SQLException: " + e.getMessage());
            displayMsg("SQLState: " + e.getSQLState());
            displayMsg("VendorError: " + e.getErrorCode());
        }
    }

    public void getMessagesMySQL() throws Exception {
        try {
            statement = connect.createStatement();
            resultSet = statement.executeQuery("select ticketID, tk_name, tk_description, tk_price" + ", tk_amount from billsys_db.ticket");
            sendResultSet(resultSet);
        } catch (SQLException e) {
            displayMsg("SQLException: " + e.getMessage());
            displayMsg("SQLState: " + e.getSQLState());
            displayMsg("VendorError: " + e.getErrorCode());
        }
    }

    private void sendResultSet(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            Ticket ticket = new Ticket();
            ticket.setTicketID(resultSet.getInt("ticketID"));
            ticket.setTkName(resultSet.getString("tk_name"));
            ticket.setTkDescription(resultSet.getString("tk_description"));
            ticket.setTkPrice(resultSet.getBigDecimal("tk_price"));
            ticket.setTkAmount(resultSet.getInt("tk_amount"));
            ticketList.add(ticket);
        }
        for (int i = 0; i < ticketList.size(); i++) {
            displayMsg("" + ticketList.get(i).getTicketID());
        }
        ticketX.getTicket().addAll(ticketList);
        try {
            printXML();
        } catch (JAXBException e) {
            displayMsg("printXML fail to print");
        }
        try {
            printXMLConvert();
        } catch (JAXBException e) {
            System.out.println("printXMLConvert failed to print");
            e.printStackTrace();
        }
    }

    private void printXML() throws JAXBException {
        writer = new StringWriter();
        context = JAXBContext.newInstance(Tickets.class);
        Marshaller m = context.createMarshaller();
        m.marshal(ticketX, writer);
        test = writer.toString();
        displayMsg(writer.toString());
    }

    public String getTest() {
        return test;
    }

    private void printXMLConvert() throws JAXBException {
        StringReader reader = new StringReader(test);
        displayMsg("Output from the XML");
        Unmarshaller um = context.createUnmarshaller();
        Tickets ticketY = (Tickets) um.unmarshal(reader);
        for (int i = 0; i < ticketY.getTicket().toArray().length; i++) {
            displayMsg("Ticket" + (i + 1) + " " + ticketY.getTicket().get(i).getTkName() + " " + ticketY.getTicket().get(i).getTkDescription() + "----" + ticketY.getTicket().get(i).getTkPrice());
        }
    }

    public void newTransaction(StringReader msg) throws JAXBException {
        System.out.println("MySQL" + msg.toString());
        StringReader leser = msg;
        JAXBContext contexto;
        contexto = JAXBContext.newInstance(Transaction.class);
        Unmarshaller um = contexto.createUnmarshaller();
        Transaction transaction = (Transaction) um.unmarshal(leser);
        System.out.println("A" + transaction.getTransactionNr());
        System.out.println("B" + transaction.getSeller());
        System.out.println("C" + transaction.getTotal());
        try {
            commitTransaction(transaction);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
		   * This method closes the connection to the database.
		   * 
		   */
    protected void closeConnection() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {
        }
    }

    public void displayMsg(String message) {
        serverUI.display(message);
    }
}
