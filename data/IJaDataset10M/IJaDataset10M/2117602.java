package delivery;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

/**
 * This class represents the <em>Who Is Where Agent</em> in the Delivery Agent
 * Community. This agent registers with the DF as a service provider and then
 * activates its GUI. The agent listens for query_ref and subscribe ACL messages
 * and sends information about the position of people in the building.
 * 
 * @author Ferenc Husz�r
 */
public class WhoIsWhereAgent extends Agent {

    /**
	 * Serial version unique identifier.
	 */
    static final long serialVersionUID = 200000001L;

    /**
	 * Sole constructor (typically implicit). Invokes super constructor.
	 */
    public WhoIsWhereAgent() {
        super();
    }

    /**
	 * Starts the graphical user interface and registers with the <em>DF</em>
	 * agent as a service provider.
	 * 
	 * @see jade.core.Agent#setup()
	 */
    protected void setup() {
        addBehaviour(new MessageProcessingBehaviour(this));
        DFAgentDescription dfd = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType("WhoIsWhereAgent");
        sd.setName(getName());
        sd.setOwnership("Feri");
        dfd.setName(getAID());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            System.err.println(getLocalName() + " registration with DF unsucceeded. Reason: " + e.getMessage());
            doDelete();
        }
        restoreGUI();
        System.out.println("Hi! WhoIsWhereAgent " + getAID().getName() + " is ready.");
    }

    /**
	 * Deregisters agent with the <em>DF</em> agent and disposes the graphical
	 * user interface.
	 * 
	 * @see jade.core.Agent#takeDown()
	 */
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            System.err.println(getLocalName() + " DF deregistration unsucceeded. Reason: " + e.getMessage());
        }
        disposeGUI();
    }

    /**
	 * Releases local resources including the graphical user interface and local
	 * resources before the original agent instance on the source container is
	 * stopped.
	 */
    protected void beforeMove() {
        disposeGUI();
    }

    /**
	 * Restarts the graphical user interface after arriving to the destination
	 * agent container for a migration.
	 * 
	 * @see jade.core.Agent#afterMove()
	 */
    protected void afterMove() {
        restoreGUI();
    }

    /**
	 * Releases local resources including the graphical user interface before
	 * copying an agent to another agent container.
	 * 
	 * @see jade.core.Agent#beforeClone()
	 */
    protected void beforeClone() {
        super.beforeClone();
        disposeGUI();
    }

    /**
	 * Restores the graphical user interface after copying an agent to another
	 * agent container.
	 * 
	 * @see jade.core.Agent#afterClone()
	 */
    protected void afterClone() {
        super.afterClone();
        restoreGUI();
    }

    /**
	 * Restores the graphical user interface.
	 */
    private void restoreGUI() {
        myGui = new WhoIsWhereGUI(this);
        myGui.setVisible(true);
    }

    /**
	 * Closes the graphical user interface.
	 */
    private void disposeGUI() {
        if (myGui != null) {
            final WhoIsWhereGUI gui = myGui;
            myGui = null;
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    gui.setVisible(false);
                    gui.dispose();
                }
            });
        }
    }

    /**
	 * Notifies all subscribers, that the position of a person has changed.
	 * 
	 * @param personID
	 *            the identifier of the person, who moved.
	 * @param placeID
	 *            the identifier of the place, where the person has moved.
	 */
    protected void notifySubscribers(String personID, String placeID) {
        if (subscriptions.containsKey(personID)) {
            try {
                Set<AID> subscribers = subscriptions.get(personID);
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                Iterator<AID> subscriberIterator = subscribers.iterator();
                while (subscriberIterator.hasNext()) msg.addReceiver(subscriberIterator.next());
                msg.setContent("(" + personID + "," + placeID + ")");
                send(msg);
            } catch (Exception e) {
            }
        }
    }

    /**
	 * This class is responsible for the cyclic message processing behavior of a
	 * Who Is Where agent. Subscriptions, subscription cancellations and single
	 * queries of appropriate form are accepted and replied by the agent with
	 * this behavior.
	 * 
	 * @author Ferenc Husz�r
	 */
    class MessageProcessingBehaviour extends CyclicBehaviour {

        /**
		 * Serial version unique identifier.
		 */
        static final long serialVersionUID = 300000001L;

        /**
		 * Sole constructor. Calls the respective constructor in the superclass.
		 * 
		 * @param agent
		 *            the agent to whom the behaviour belongs to
		 */
        public MessageProcessingBehaviour(Agent agent) {
            super(agent);
        }

        /**
		 * Continually processes and replies the first <em>ACL</em> message in
		 * the message queue. Subscriptions, subscription cancellations and
		 * single queries of appropriate form are accepted, and replies are
		 * generated and sent if required. In case of improper message act, or
		 * unrecognizable communicative act a not_understood ACL message is
		 * sent.
		 * 
		 * @see jade.core.behaviours.Behaviour#action()
		 */
        public void action() {
            ACLMessage msg = blockingReceive();
            if (msg != null) {
                ACLMessage reply = msg.createReply();
                switch(msg.getPerformative()) {
                    case ACLMessage.SUBSCRIBE:
                        if (msg.getContent() != null) {
                            Scanner scanner = new Scanner(msg.getContent());
                            scanner.useDelimiter(Pattern.compile("[()]"));
                            if (scanner.hasNext()) {
                                if (scanner.next().equals("WHERE_IS")) {
                                    if (scanner.hasNext()) {
                                        String personID = scanner.next();
                                        if (subscriptions.containsKey(personID)) {
                                            subscriptions.get(personID).add(msg.getSender());
                                        } else {
                                            Set<AID> subscribers = new TreeSet<AID>();
                                            subscribers.add(msg.getSender());
                                            subscriptions.put(personID, subscribers);
                                        }
                                        String placeID = DeliveryMap.NOWHERE;
                                        reply.setPerformative(ACLMessage.INFORM);
                                        for (int i = 0; i < whoIsWhereTable.getRowCount(); i++) {
                                            if (whoIsWhereTable.getValueAt(i, 0).equals(personID)) {
                                                placeID = whoIsWhereTable.getValueAt(i, 1).toString();
                                                break;
                                            }
                                        }
                                        reply.setContent("(" + personID + "," + placeID + ")");
                                        send(reply);
                                        break;
                                    }
                                }
                            }
                        }
                        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                        send(reply);
                        break;
                    case ACLMessage.CANCEL:
                        if (msg.getContent() != null) {
                            Scanner scanner = new Scanner(msg.getContent());
                            scanner.useDelimiter(Pattern.compile("[()]"));
                            if (scanner.hasNext()) {
                                if (scanner.next().equals("WHERE_IS")) {
                                    if (scanner.hasNext()) {
                                        String personID = scanner.next();
                                        if (subscriptions.containsKey(personID)) {
                                            subscriptions.get(personID).remove(msg.getSender());
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                        send(reply);
                        break;
                    case ACLMessage.QUERY_REF:
                        reply.setPerformative(ACLMessage.INFORM);
                        Scanner scanner = new Scanner(msg.getContent());
                        scanner.useDelimiter(Pattern.compile("[()]"));
                        if (scanner.hasNext()) {
                            if (scanner.next().equals("WHERE_IS")) {
                                if (scanner.hasNext()) {
                                    String personID = scanner.next();
                                    String placeID = DeliveryMap.NOWHERE;
                                    reply.setPerformative(ACLMessage.INFORM);
                                    for (int i = 0; i < whoIsWhereTable.getRowCount(); i++) {
                                        if (whoIsWhereTable.getValueAt(i, 0).equals(personID)) {
                                            placeID = whoIsWhereTable.getValueAt(i, 1).toString();
                                            break;
                                        }
                                    }
                                    reply.setContent("(" + personID + "," + placeID + ")");
                                    send(reply);
                                    break;
                                }
                            }
                        }
                        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                        send(reply);
                        break;
                    default:
                        reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                        send(reply);
                        break;
                }
            }
        }
    }

    /**
	 * Lookup table model for the Who Is Where Agent. PersonID - placeID pairs
	 * are stored in an array, and subscribers are notified in case of a change
	 * in the position of a person.
	 * 
	 * @author Ferenc Husz�r
	 */
    protected class WhoIsWhereTableModel extends AbstractTableModel {

        /**
		 * Serial version unique identifier.
		 */
        static final long serialVersionUID = 300000001L;

        private String[] columnNames = { "Person", "Location" };

        private Object[][] data = new Object[0][2];

        /**
		 * Sole class constructor. Tries to load a default Who is Where table.
		 */
        public WhoIsWhereTableModel() {
            try {
                File file = new File("resources/default.wiw");
                readFromFile(file);
            } catch (Exception e) {
            }
        }

        /**
		 * Reads the contents of the table from a file.
		 * 
		 * @param file
		 *            the file containing a Who is where lookup table.
		 */
        public void readFromFile(File file) {
            for (int i = 0; i < data.length; i++) notifySubscribers(data[i][0].toString(), DeliveryMap.NOWHERE);
            data = new Object[0][2];
            try {
                RandomAccessFile raFile = new RandomAccessFile(file, "r");
                String currentLine = raFile.readLine();
                for (int i = 0; currentLine != null; i++) {
                    Scanner scanner = new Scanner(currentLine);
                    scanner.useDelimiter(Pattern.compile(";"));
                    if (scanner.hasNext()) {
                        setValueAt(scanner.next(), i, 0);
                        if (scanner.hasNext()) {
                            setValueAt(scanner.next(), i, 1);
                        }
                    }
                    currentLine = raFile.readLine();
                }
                raFile.close();
            } catch (Exception e) {
                System.out.println("catch");
            }
            fireTableDataChanged();
        }

        /**
		 * Returns the number of columns in the table, that is 2.
		 * 
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
        public int getColumnCount() {
            return 2;
        }

        /**
		 * Returns the number of entries in the table.
		 * 
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
        public int getRowCount() {
            return data.length;
        }

        /**
		 * Returns the name of the column.
		 * 
		 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
		 * @param col
		 *            the index of the column
		 */
        public String getColumnName(int col) {
            return columnNames[col];
        }

        /**
		 * Returns the object in the table at a specified position. If the
		 * indexes are inconsistent with the table, null is returned.
		 * 
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 * @param row
		 *            the index of the row
		 * @param col
		 *            the index of the column
		 */
        public Object getValueAt(int row, int col) {
            try {
                if (row < 0 || col < 0 || row >= data.length || col >= columnNames.length) return null;
                return data[row][col];
            } catch (Exception e) {
                System.out.println("exception: ");
                e.printStackTrace();
                return null;
            }
        }

        /**
		 * Returns true if <code>col</code> equals to one and false otherwise.
		 * 
		 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
		 * @param row
		 *            the row in which the cell is
		 * @param col
		 *            the column in which the cell is
		 */
        public boolean isCellEditable(int row, int col) {
            return (col == 1);
        }

        /**
		 * Modifies the value at a specified position in the table. If the place
		 * of a person changes, the subscribers are notified.
		 * 
		 * @see javax.swing.table.AbstractTableModel#setValueAt(java.lang.Object,
		 *      int, int)
		 * @param value -
		 *            the new value of the cell
		 * @param row
		 *            the row in which the cell is
		 * @param col
		 *            the column in which the cell is
		 */
        public void setValueAt(Object value, int row, int col) {
            if ((col >= 2) || (col < 0) || (row < 0)) return;
            if (col == 0) for (int i = 0; i < data.length; i++) if (value.equals(data[i][0])) return;
            if (row >= data.length) {
                Object[][] temp = data;
                data = new Object[row + 1][2];
                for (int i = 0; i < temp.length; i++) {
                    data[i][0] = temp[i][0];
                    data[i][1] = temp[i][1];
                }
                for (int i = temp.length + 1; i < data.length; i++) {
                    data[i][0] = "";
                    data[i][1] = "";
                }
                fireTableRowsInserted(temp.length, row);
            }
            if (data[row][col] != value.toString()) {
                data[row][col] = value.toString();
                fireTableCellUpdated(row, col);
                if (col == 1) notifySubscribers(data[row][0].toString(), data[row][1].toString());
            }
        }

        public void deleteRow(int row) {
            if ((row < 0) || (row >= data.length)) return; else {
                notifySubscribers(data[row][0].toString(), DeliveryMap.NOWHERE);
                Object[][] temp = new Object[data.length - 1][2];
                for (int i = 0; i < row; i++) {
                    temp[i][0] = data[i][0];
                    temp[i][1] = data[i][1];
                }
                for (int i = row + 1; i < data.length; i++) {
                    temp[i - 1][0] = data[i][0];
                    temp[i - 1][1] = data[i][1];
                }
                data = temp;
                fireTableRowsDeleted(row, row);
            }
        }
    }

    /**
	 * Maps person identifiers to sets of active subscribers (agents).
	 */
    protected Map<String, Set<AID>> subscriptions = new TreeMap<String, Set<AID>>();

    /**
	 * The map of the building.
	 */
    protected DeliveryMap map = new DeliveryMap();

    /**
	 * A table containing the current person to position mapping.
	 */
    protected WhoIsWhereTableModel whoIsWhereTable = new WhoIsWhereTableModel();

    /**
	 * The graphical user interface of the agent.
	 */
    protected transient WhoIsWhereGUI myGui;
}
