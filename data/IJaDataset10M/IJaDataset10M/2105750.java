package ro.codemart.tutorial.v3.cdmanager.operation;

import ro.codemart.installer.core.InstallerException;
import ro.codemart.installer.core.InstallerContext;
import ro.codemart.installer.core.I18nKey;
import ro.codemart.tutorial.v2.cdmanager.AudioCD_V2;
import ro.codemart.tutorial.v2.cdmanager.Play_V2;
import java.util.List;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import ro.codemart.commons.sql.ConnectionInfo;
import ro.codemart.commons.sql.SQLCommand;
import org.apache.log4j.Logger;

/**
 * Writes the information related to audio CDs and plays into a DB tables
 */
public class WriteCDs_V3ToDBOperation extends DBAction {

    public static final String WRITE_V3_MESSAGE = "WriteV3Message";

    public static final String WRITE_V3_DEFAULT_MESSAGE = "Writing data to database";

    private static final Logger logger = Logger.getLogger(WriteCDs_V3ToDBOperation.class);

    private static final String INSERT_PLAY_SQL = "INSERT INTO PLAYS(CD_ID, AUTHOR, TITLE, LENGTH) VALUES (?,?,?,?)";

    private static final String INSERT_CD_SQL = "INSERT INTO AUDIO_CDS(ID, NR_PLAYS, PROVIDER_NAME, TOTAL_LENGTH) VALUES(?,?,?,?)";

    private List<AudioCD_V2> audioCDs;

    /**
     * Creates a new operation for writing the list of audio CDs into DB tables based on a id, and a list of audio CDs
     *
     * @param id       the unique id for this operation
     * @param audioCDs the list of audio CDs to be written to the DB tables
     * @param context  installer context
     */
    public WriteCDs_V3ToDBOperation(String id, List<AudioCD_V2> audioCDs, InstallerContext context) {
        super(id, context);
        this.audioCDs = audioCDs;
    }

    /**
     * Executes this operation.
     *
     * @throws InstallerException if any error occurs while performing the operation
     */
    public void execute() throws InstallerException {
        ConnectionInfo connectionInfo = getConnectionInfo(context);
        SQLCommand command = null;
        PreparedStatement prepStmtCD = null;
        PreparedStatement prepStmtPlay = null;
        setDescription(new I18nKey(WRITE_V3_MESSAGE, WRITE_V3_DEFAULT_MESSAGE), getLanguage());
        try {
            logger.info("Begin to move audio CDs and plays to Database...");
            setDescription("Moving audio CDs and plays to database");
            command = new SQLCommand(connectionInfo);
            prepStmtPlay = command.getConnection().prepareStatement(INSERT_PLAY_SQL);
            prepStmtCD = command.getConnection().prepareStatement(INSERT_CD_SQL);
            List<Play_V2> plays;
            for (AudioCD_V2 audioCD : audioCDs) {
                plays = audioCD.getPlays();
                prepStmtCD.setLong(1, audioCD.getId());
                prepStmtCD.setInt(2, plays.size());
                prepStmtCD.setString(3, audioCD.getProviderName());
                prepStmtCD.setDouble(4, audioCD.getTotalLength());
                prepStmtCD.execute();
                for (Play_V2 play : plays) {
                    prepStmtPlay.setLong(1, audioCD.getId());
                    prepStmtPlay.setString(2, play.getAuthor());
                    prepStmtPlay.setString(3, play.getTitle());
                    prepStmtPlay.setDouble(4, play.getPlayLength());
                    prepStmtPlay.execute();
                }
                logger.info("Moving data to database successfully completed.");
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new InstallerException(e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
            throw new InstallerException(e.getMessage(), e);
        } finally {
            if (command != null) {
                command.close();
            }
        }
    }
}
