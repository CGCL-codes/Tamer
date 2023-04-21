package net.sf.karatasi.librarian;

import java.sql.SQLException;
import java.util.Date;
import net.sf.karatasi.database.DBValueException;
import net.sf.karatasi.database.Database;
import net.sf.karatasi.database.Database.DbHealthState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DatabaseStatus implements Cloneable, Comparable<DatabaseStatus> {

    /** The full name of the database. */
    private String fullName;

    /** The version number of this database. */
    private int dbVersion;

    /** The size of the database file. */
    private long fileSize;

    /** The time stamp of the database (stored in the database). */
    @Nullable
    private Date timeStamp;

    /** The card count of this database. */
    private int cardCount;

    /** The state of this database. */
    @NotNull
    private Database.DbHealthState dbHealth;

    /** The borrowing state of the database. */
    private boolean borrowed;

    /** Constructor setting values from a database.
     *
     * @param database the data source.
     * @throws SQLException In case of SQL problems.
     * @throws DBValueException if the database contains invalid data.
     */
    public DatabaseStatus(@NotNull final Database database) throws DBValueException, SQLException {
        this.borrowed = false;
        refreshFrom(database);
    }

    /** Refresh the database status.
     *
     * @param database the data source.
     * @throws SQLException In case of SQL problems.
     * @throws DBValueException if the database contains invalid data.
     */
    public void refreshFrom(@NotNull final Database database) throws DBValueException, SQLException {
        this.fullName = database.getFullName();
        this.dbHealth = database.getHealthState();
        if (database.isDead()) {
            this.dbVersion = 0;
            this.fileSize = 0;
            this.timeStamp = null;
            this.cardCount = 0;
        } else {
            this.dbVersion = database.getVersion();
            this.fileSize = database.getFile().length();
            this.timeStamp = database.getLastMod();
            this.cardCount = database.getCardCount();
        }
    }

    /** Getter function.
     * @return the full name of the database.
     */
    public String getFullName() {
        return fullName;
    }

    /** Getter function.
     * @return the version number of the database.
     */
    public int getDbVersion() {
        return dbVersion;
    }

    /** Determines whether the database has the current version.
     * @return true if current version / false else
     */
    public boolean isCurrentVersion() {
        return dbVersion == Database.DB_ACTIVE_VERSION;
    }

    /** Determines whether the database has an old version.
     * @return true if old version / false else
     */
    public boolean isOldVersion() {
        return dbVersion < Database.DB_ACTIVE_VERSION;
    }

    /** Determines whether the database has a future version.
     * @return true if future version / false else
     */
    public boolean isFutureVersion() {
        return dbVersion > Database.DB_ACTIVE_VERSION;
    }

    /** Returns the size of the database file.
     * @return The size of the database file.
     */
    public long getFileSize() {
        return fileSize;
    }

    /** Returns the time stamp of the database.
     * @note The time stamp is not a file time stamp, but a time stamp stored in the database.
     * @return The time stamp of the database.
     */
    public Date getTimeStamp() {
        if (timeStamp != null) {
            return (Date) timeStamp.clone();
        } else {
            return null;
        }
    }

    /** Getter function.
     * @return the card count of the database.
     */
    public int getCardCount() {
        return cardCount;
    }

    /** Getter function.
     * @return the health state of the database.
     */
    public Database.DbHealthState getDbHealth() {
        return dbHealth;
    }

    /** Returns whether the database is healthy.
     * @return true if healthy / false else.
     */
    public boolean isHealthy() {
        return dbHealth == DbHealthState.OKAY;
    }

    /** Determines whether the database is operational: healthy and of current version.
     * @return true if healthy and of current version / false else
     */
    public synchronized boolean isOperational() {
        return isHealthy() && isCurrentVersion();
    }

    /** Getter function.
     * @return the borrow state of the database.
     */
    public boolean isBorrowed() {
        return borrowed;
    }

    /** Setter function.
     * @param the borrow state of the database.
     */
    public void setBorrowed(final boolean borrowed) {
        this.borrowed = borrowed;
    }

    /** clone the database status. */
    @Override
    public DatabaseStatus clone() {
        final DatabaseStatus clonedStatus;
        try {
            clonedStatus = (DatabaseStatus) super.clone();
        } catch (final CloneNotSupportedException e) {
            return null;
        }
        clonedStatus.timeStamp = getTimeStamp();
        return clonedStatus;
    }

    public int compareTo(final DatabaseStatus o) {
        return Database.DISPLAY_ORDER_NAME.compare(fullName, o.fullName);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof DatabaseStatus)) {
            return false;
        }
        final DatabaseStatus other = (DatabaseStatus) obj;
        if (!fullName.equals(other.getFullName())) {
            return false;
        }
        if (dbHealth != other.getDbHealth()) {
            return false;
        }
        if (fileSize != other.getFileSize()) {
            return false;
        }
        if (isHealthy()) {
            if (dbVersion != other.getDbVersion()) {
                return false;
            }
            if (!timeStamp.equals(other.getTimeStamp())) {
                return false;
            }
            if (cardCount != other.getCardCount()) {
                return false;
            }
        }
        if (borrowed != other.isBorrowed()) {
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int hash = fullName.hashCode();
        hash = hash * 3 + dbHealth.ordinal();
        hash = hash * 3 + (borrowed ? 1 : 0);
        hash = hash * 31 + (int) fileSize;
        if (isHealthy()) {
            hash = hash * 5 + dbVersion;
            hash = hash * 31 + timeStamp.hashCode();
            hash = hash * 31 + cardCount;
        }
        return hash;
    }
}
