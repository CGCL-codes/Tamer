package espider.network.filetransfert.download;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import espider.Config;
import espider.contact.ContactList;
import espider.contact.NoContactException;
import espider.database.Database;
import espider.network.message.InitTransfertMessage;
import espider.utils.Utils;

public class Downloader extends Thread {

    private final InitTransfertMessage message;

    private SocketChannel socketChannel;

    private final ByteBuffer buffer;

    private long totalNbByteRead = 0;

    private int nbBytesPerSecond = 1024;

    private volatile boolean alive = true;

    private Connection sqlConn;

    private PreparedStatement nextPartStmt, isFinishStmt, setStatePartStmt;

    private long idDownload;

    private File downloadingFile;

    /**
	 * Download
	 * 
	 * @param message
	 */
    public Downloader(InitTransfertMessage message) throws SQLException, IOException {
        this.message = message;
        buffer = java.nio.ByteBuffer.allocate(message.getPacketSize());
        sqlConn = Database.getInstance().getConnection();
        if (message.getIdDownload() == -1) {
            downloadingFile = new File(Config.getInstance().getProperty("income_directory") + "/" + getTmpFileName());
            downloadingFile.createNewFile();
            insertNewDownload();
        } else {
            this.idDownload = message.getIdDownload();
            downloadRecovery();
        }
    }

    /**
	 * 
	 * @see java.lang.Runnable#run()
	 */
    public void run() {
        int nbByteRead = 0;
        long indexPacketToDownload = 0;
        int nbBytesInSecond = 0;
        int bufferLimit;
        long startDownload = 0;
        try {
            FileChannel fileChannel = new FileOutputStream(downloadingFile).getChannel();
            socketChannel = SocketChannel.open(new InetSocketAddress(message.getIPSender(), message.getUploadPort()));
            socketChannel.configureBlocking(true);
            PreparedStatement pstmt = sqlConn.prepareStatement("UPDATE downloading SET nb_bytes_downloaded=?");
            while (!isDownloadFinish()) {
                indexPacketToDownload = getNextPart();
                System.out.println("\n\n-------------------------------------\nJe demande la partie : " + indexPacketToDownload);
                askIndexPart(indexPacketToDownload);
                buffer.clear();
                nbByteRead = 0;
                bufferLimit = 0;
                while (buffer.position() < buffer.capacity() && nbByteRead > -1 && totalNbByteRead < message.getFileSize()) {
                    bufferLimit = bufferLimit + (nbBytesPerSecond - nbBytesInSecond);
                    if (bufferLimit > buffer.capacity()) bufferLimit = buffer.capacity();
                    buffer.limit(bufferLimit);
                    if (startDownload == 0) startDownload = System.currentTimeMillis();
                    nbByteRead = 0;
                    while (buffer.hasRemaining() && nbByteRead > -1 && totalNbByteRead < message.getFileSize()) {
                        nbByteRead = socketChannel.read(buffer);
                        if (nbByteRead > -1) {
                            totalNbByteRead += nbByteRead;
                            nbBytesInSecond += nbByteRead;
                        }
                        System.out.println("J'ai lu nb bytes : " + nbByteRead);
                    }
                    System.out.println("nbBytesInSecond : " + nbBytesInSecond + " & nbBytesPerSecond : " + nbBytesPerSecond);
                    if (nbBytesInSecond >= nbBytesPerSecond) {
                        long time = System.currentTimeMillis() - startDownload;
                        long sleep = 1000 - time;
                        if (sleep > 0) {
                            try {
                                Thread.sleep(sleep);
                                System.out.println("Je viens de dormir : " + sleep + " ms");
                            } catch (InterruptedException ie) {
                                ie.printStackTrace();
                            }
                        } else System.out.println("J'ai mis trop de temps : " + Math.abs(sleep));
                        if (time > 0) nbBytesPerSecond = (int) ((nbBytesInSecond * 1000) / time); else nbBytesPerSecond *= 10;
                        System.out.println("nouveau taux : " + nbBytesPerSecond);
                        startDownload = 0;
                        nbBytesInSecond = 0;
                    }
                }
                System.out.println("Download de la partie " + indexPacketToDownload + " finie...\nTotal DL : " + totalNbByteRead + "/" + message.getFileSize());
                pstmt.setLong(1, totalNbByteRead);
                pstmt.execute();
                sqlConn.commit();
                buffer.flip();
                fileChannel.write(buffer);
                setStateOfPart(indexPacketToDownload, "D");
            }
            askIndexPart(-1);
            socketChannel.close();
            fileChannel.close();
            String RSQL;
            if (getNbPartsToDownload() == 0) {
                File newFile = new File(Config.getInstance().getProperty("income_directory") + "/" + message.getFilename());
                if (newFile.exists()) newFile.delete();
                downloadingFile.renameTo(newFile);
                RSQL = "DELETE FROM downloading WHERE id_download=" + idDownload;
            } else RSQL = "UPDATE downloading SET state='P' WHERE id_download=" + idDownload;
            System.out.println(RSQL);
            sqlConn.createStatement().execute(RSQL);
            sqlConn.commit();
            System.out.println("FIN du transfert!!");
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    /**
	 * 
	 *
	 */
    public void stopDownload() {
        this.alive = false;
    }

    /**
	 * 
	 * @param index
	 * @throws IOException
	 */
    private void askIndexPart(long index) throws IOException {
        buffer.clear();
        buffer.putLong(index);
        buffer.flip();
        socketChannel.write(buffer);
    }

    /**
	 * 
	 * @return
	 */
    public String[] getSummary() {
        String[] summary = new String[6];
        summary[0] = "Active";
        summary[1] = message.getFilename();
        summary[2] = Utils.formatNumer((totalNbByteRead * 100) / (double) message.getFileSize()).concat(" %");
        summary[3] = Utils.formatNumer(((double) message.getFileSize()) / (1024 * 1024)).concat(" Mo");
        summary[4] = Utils.formatNumer(((double) message.getFileSize() - totalNbByteRead) / (1024 * 1024)).concat(" Mo");
        try {
            summary[5] = ContactList.getContact(message.getIdSender()).getName();
        } catch (NoContactException nce) {
            summary[5] = "unknown user";
        }
        return summary;
    }

    /**
	 * 
	 * @return
	 */
    private String getTmpFileName() {
        StringBuilder sb = new StringBuilder();
        sb.append(Calendar.getInstance(Locale.getDefault()).getTimeInMillis());
        sb.append(message.getIdFileToDL());
        return Utils.computeMD5(sb.toString());
    }

    /**
	 * 
	 * @return
	 */
    private boolean isDownloadFinish() throws SQLException {
        if (alive) return getNbPartsToDownload() == 0; else return true;
    }

    /**
	 * 
	 * @return
	 * @throws SQLException
	 */
    private int getNbPartsToDownload() throws SQLException {
        if (isFinishStmt == null) isFinishStmt = sqlConn.prepareStatement("SELECT count(*) FROM part WHERE id_download=" + idDownload + " AND isDownloaded='N'");
        ResultSet rs = isFinishStmt.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    /**
	 * 
	 * @throws SQLException
	 */
    private void insertNewDownload() throws SQLException {
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
        String RSQL = "INSERT INTO downloading (start, file_name, tmp_name, file_size, nb_bytes_downloaded, part_size, state) " + "VALUES('" + now + "', " + "'" + message.getFilename() + "', " + "'" + downloadingFile.getAbsolutePath() + "', " + message.getFileSize() + ", " + totalNbByteRead + ", " + message.getPacketSize() + ", " + "'D')";
        Statement stmt = sqlConn.createStatement();
        stmt.execute(RSQL);
        RSQL = "SELECT MAX(id_download) FROM downloading";
        ResultSet rs = stmt.executeQuery(RSQL);
        rs.next();
        idDownload = rs.getLong(1);
        RSQL = "INSERT INTO who_has_file (id_contact, id_download, id_file_to_dl) VALUES('" + message.getIdSender() + "', " + idDownload + ", " + message.getIdFileToDL() + ")";
        stmt.execute(RSQL);
        int nbPartsToDownload = Math.round(message.getFileSize() / message.getPacketSize()) + 1;
        PreparedStatement pstmt = sqlConn.prepareStatement("INSERT INTO part (id_part, id_download, isDownloaded) VALUES(?, ?, 'N')");
        for (int i = 0; i < nbPartsToDownload; i++) {
            pstmt.setInt(1, i);
            pstmt.setLong(2, idDownload);
            pstmt.execute();
        }
        sqlConn.commit();
    }

    /**
	 * 
	 * @return
	 * @throws SQLException
	 */
    private long getNextPart() throws SQLException {
        if (nextPartStmt == null) {
            nextPartStmt = sqlConn.prepareStatement("SELECT id_part FROM part WHERE id_download=" + idDownload + " AND isDownloaded='N' ORDER BY id_part ASC");
            nextPartStmt.setMaxRows(1);
        }
        ResultSet rs = nextPartStmt.executeQuery();
        rs.next();
        long idPart = rs.getLong(1);
        setStateOfPart(idPart, "E");
        return idPart;
    }

    /**
	 * 
	 * @param idPart
	 * @param state
	 * @throws SQLException
	 */
    private void setStateOfPart(long idPart, String state) throws SQLException {
        if (setStatePartStmt == null) setStatePartStmt = sqlConn.prepareStatement("UPDATE part SET isDownloaded=? WHERE id_download=" + idDownload + " AND id_part=?");
        setStatePartStmt.setString(1, state);
        setStatePartStmt.setLong(2, idPart);
        setStatePartStmt.execute();
        sqlConn.commit();
    }

    /**
	 * 
	 * @throws SQLException
	 */
    private void downloadRecovery() throws SQLException {
        String RSQL = "SELECT tmp_name, nb_bytes_downloaded FROM downloading WHERE id_download=" + idDownload;
        ResultSet rs = sqlConn.createStatement().executeQuery(RSQL);
        rs.next();
        downloadingFile = new File(rs.getString("tmp_name"));
        totalNbByteRead = rs.getLong("nb_bytes_downloaded");
    }

    /**
	 * 
	 * @return
	 */
    public long getIdDownload() {
        return idDownload;
    }
}
