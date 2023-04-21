package org.tranche.gui.add.monitor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import org.tranche.add.AddFileToolEvent;
import org.tranche.gui.GenericScrollPane;
import org.tranche.gui.GenericTable;
import org.tranche.gui.SortableTableModel;
import org.tranche.gui.Styles;
import org.tranche.hash.BigHash;

/**
 *
 * @author James "Augie" Hill - augman85@gmail.com
 */
public class ChunkPanel extends JPanel {

    public static final int MAX_NUM_ROWS = 50;

    private ChunkTableModel model = new ChunkTableModel();

    private GenericTable table;

    public ChunkPanel() {
        setName("Chunks");
        setLayout(new BorderLayout());
        table = new GenericTable(model, ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        GenericScrollPane scrollPane = new GenericScrollPane(table);
        scrollPane.setBackground(Color.GRAY);
        scrollPane.setHorizontalScrollBarPolicy(GenericScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(Styles.BORDER_NONE);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void handleChunkEvent(AddFileToolEvent event) {
        model.handleChunkEvent(event);
    }

    private class ChunkTableModel extends SortableTableModel {

        private final String[] headers = new String[] { "File", "Chunk", "Started", "Finished", "Tries", "Servers", "Status" };

        private final Map<String, ChunkRow> rows = new HashMap<String, ChunkRow>();

        private final LinkedList<String> order = new LinkedList<String>();

        private final LinkedList<String> orderReceived = new LinkedList<String>();

        public void handleChunkEvent(AddFileToolEvent event) {
            boolean isNew = true;
            synchronized (rows) {
                isNew = !rows.containsKey(makeUniqueID(event));
            }
            if (isNew) {
                synchronized (rows) {
                    while (rows.size() > MAX_NUM_ROWS) {
                        String uniqueId;
                        synchronized (orderReceived) {
                            uniqueId = orderReceived.removeFirst();
                        }
                        synchronized (order) {
                            order.remove(uniqueId);
                        }
                        rows.remove(uniqueId);
                    }
                }
                ChunkRow cr = new ChunkRow(event);
                synchronized (rows) {
                    rows.put(cr.uniqueId, cr);
                }
                synchronized (order) {
                    order.add(cr.uniqueId);
                }
                synchronized (orderReceived) {
                    orderReceived.add(cr.uniqueId);
                }
            } else {
                ChunkRow cr;
                synchronized (rows) {
                    cr = rows.get(makeUniqueID(event));
                }
                cr.handleChunkEvent(event);
            }
            resort();
            fireTableDataChanged();
        }

        public int getColumnCount() {
            return headers.length;
        }

        @Override
        public Class getColumnClass(int c) {
            try {
                return getValueAt(0, c).getClass();
            } catch (Exception e) {
                return String.class;
            }
        }

        @Override
        public String getColumnName(int column) {
            if (column < getColumnCount()) {
                return headers[column];
            } else {
                return "";
            }
        }

        public ChunkRow getRow(int row) {
            String uniqueID;
            synchronized (order) {
                uniqueID = order.get(row);
            }
            synchronized (rows) {
                return rows.get(uniqueID);
            }
        }

        public int getRowOf(ChunkRow cr) {
            synchronized (order) {
                return order.indexOf(cr);
            }
        }

        public int getRowCount() {
            synchronized (order) {
                return order.size();
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public Object getValueAt(int row, int column) {
            try {
                ChunkRow cr = getRow(row);
                if (cr == null) {
                    return null;
                }
                switch(column) {
                    case 0:
                        return cr.fileHash.toString();
                    case 1:
                        return cr.chunkHash.toString();
                    case 2:
                        return String.valueOf(cr.timeStarted);
                    case 3:
                        return String.valueOf(cr.timeFinished);
                    case 4:
                        return String.valueOf(cr.tries);
                    case 5:
                        return cr.getServersTriedList();
                    case 6:
                        return cr.status;
                    default:
                        return null;
                }
            } catch (Exception e) {
                return null;
            }
        }

        public void sort(int column) {
            if (column >= headers.length || column < 0) {
                return;
            }
            table.setPressedColumn(column);
            synchronized (order) {
                Collections.sort(order, new SSComparator(column));
            }
        }

        public void resort() {
            sort(table.getPressedColumn());
        }

        private class SSComparator implements Comparator {

            private int column;

            public SSComparator(int column) {
                this.column = column;
            }

            public int compare(Object o1, Object o2) {
                if (table.getDirection()) {
                    Object temp = o1;
                    o1 = o2;
                    o2 = temp;
                }
                if (o1 == null && o2 == null) {
                    return 0;
                } else if (o1 == null) {
                    return 1;
                } else if (o1 instanceof String && o2 instanceof String) {
                    ChunkRow cr1, cr2;
                    synchronized (rows) {
                        cr1 = rows.get((String) o1);
                        cr2 = rows.get((String) o2);
                    }
                    if (column == table.getColumnModel().getColumnIndex("File")) {
                        return cr1.fileHash.toString().compareTo(cr2.fileHash.toString().toLowerCase());
                    } else if (column == table.getColumnModel().getColumnIndex("Chunk")) {
                        return cr1.chunkHash.toString().compareTo(cr2.chunkHash.toString().toLowerCase());
                    } else if (column == table.getColumnModel().getColumnIndex("Started")) {
                        if (cr1.timeStarted == cr2.timeStarted) {
                            return 0;
                        } else if (cr1.timeStarted > cr2.timeStarted) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } else if (column == table.getColumnModel().getColumnIndex("Finished")) {
                        if (cr1.timeFinished == cr2.timeFinished) {
                            return 0;
                        } else if (cr1.timeFinished > cr2.timeFinished) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } else if (column == table.getColumnModel().getColumnIndex("Tries")) {
                        if (cr1.tries == cr2.tries) {
                            return 0;
                        } else if (cr1.tries > cr2.tries) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } else if (column == table.getColumnModel().getColumnIndex("Servers")) {
                        if (cr1.serversTried.size() == cr2.serversTried.size()) {
                            return 0;
                        } else if (cr1.serversTried.size() > cr2.serversTried.size()) {
                            return 1;
                        } else {
                            return -1;
                        }
                    } else if (column == table.getColumnModel().getColumnIndex("Status")) {
                        return cr1.status.toLowerCase().compareTo(cr2.status.toLowerCase());
                    } else {
                        return 0;
                    }
                } else {
                    return 1;
                }
            }
        }
    }

    public static String makeUniqueID(String fileRelativeName, BigHash hash) {
        return fileRelativeName + hash.toString();
    }

    public static String makeUniqueID(AddFileToolEvent event) {
        try {
            return event.getFileName() + event.getChunkHash().toString();
        } catch (Exception e) {
            try {
                return event.getChunkHash().toString();
            } catch (Exception ee) {
                return "";
            }
        }
    }

    private class ChunkRow {

        public String uniqueId = "";

        public BigHash fileHash, chunkHash;

        public String fileRelativeName;

        public long timeStarted = 0;

        public long timeFinished = 0;

        public Set<String> serversTried = new HashSet<String>(), serversUploaded = new HashSet<String>(), serversSkipped = new HashSet<String>();

        public String status = "";

        public int tries = 0;

        public ChunkRow(AddFileToolEvent event) {
            uniqueId = makeUniqueID(event);
            fileRelativeName = event.getFileName();
            fileHash = event.getFileHash();
            chunkHash = event.getChunkHash();
            handleChunkEvent(event);
        }

        public void handleChunkEvent(AddFileToolEvent event) {
            if (event.getAction() == AddFileToolEvent.ACTION_STARTING || event.getAction() == AddFileToolEvent.ACTION_STARTED) {
                timeStarted = event.getTimestamp();
            } else if (event.getAction() == AddFileToolEvent.ACTION_FAILED || event.getAction() == AddFileToolEvent.ACTION_FINISHED) {
                timeFinished = event.getTimestamp();
            }
            if (event.getServer() != null && !event.getServer().equals("")) {
                if (event.getAction() == AddFileToolEvent.ACTION_TRYING) {
                    serversTried.add(event.getServer());
                    tries++;
                } else if (event.getAction() == AddFileToolEvent.ACTION_UPLOADED) {
                    serversUploaded.add(event.getServer());
                }
            }
            status = event.getActionString();
        }

        public String getServersTriedList() {
            String servers = "";
            for (String url : this.serversTried) {
                if (servers.equals("")) {
                    servers = servers + url;
                } else {
                    servers = servers + ", " + url;
                }
            }
            return servers;
        }

        public String getServersUploadedList() {
            String servers = "";
            for (String url : this.serversUploaded) {
                if (servers.equals("")) {
                    servers = servers + url;
                } else {
                    servers = servers + ", " + url;
                }
            }
            return servers;
        }
    }
}
