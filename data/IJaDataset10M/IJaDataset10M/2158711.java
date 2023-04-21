package solidbase.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import solidbase.core.Delimiter.Type;
import solidbase.util.Assert;
import solidstack.io.Resource;
import solidstack.io.SourceReader;

/**
 * Processes commands, maintains state, triggers the listeners.
 *
 * @author Ren� M. de Bloois
 * @since May 2010
 */
public abstract class CommandProcessor {

    /**
	 * Pattern for ENCODING.
	 */
    protected static final Pattern encodingPattern = Pattern.compile("ENCODING\\s+\"(.*)\"", Pattern.CASE_INSENSITIVE);

    /**
	 * Pattern for IGNORE SQL ERROR.
	 */
    protected static final Pattern ignoreSqlErrorPattern = Pattern.compile("IGNORE\\s+SQL\\s+ERROR\\s+(\\w+(\\s*,\\s*\\w+)*)", Pattern.CASE_INSENSITIVE);

    /**
	 * Pattern for /IGNORE SQL ERROR.
	 */
    protected static final Pattern ignoreEnd = Pattern.compile("END\\s+IGNORE|/IGNORE\\s+SQL\\s+ERROR", Pattern.CASE_INSENSITIVE);

    /**
	 * Pattern for SET USER.
	 */
    protected static final Pattern setUserPattern = Pattern.compile("SET\\s+USER\\s+(\\w+)\\s*", Pattern.CASE_INSENSITIVE);

    protected static final Pattern selectConnectionPattern = Pattern.compile("SELECT\\s+CONNECTION\\s+(\\w+)", Pattern.CASE_INSENSITIVE);

    /**
	 * Pattern for DELIMITER.
	 */
    protected static final Pattern delimiterPattern = Pattern.compile("(?:SET\\s+DELIMITER|DELIMITER\\s+IS)(?:\\s+(ISOLATED)|\\s+(TRAILING))?\\s+(\\S+)(?:\\sOR(?:\\s+(ISOLATED)|\\s+(TRAILING))?\\s+(\\S+))?", Pattern.CASE_INSENSITIVE);

    /**
	 * Pattern for SECTION.
	 */
    protected static final Pattern sectionPattern = Pattern.compile("SECTION(?:\\.(\\d))?\\s+\"(.*)\"", Pattern.CASE_INSENSITIVE);

    /**
	 * Pattern for SKIP.
	 */
    protected static final Pattern skipPattern = Pattern.compile("SKIP", Pattern.CASE_INSENSITIVE);

    /**
	 * Pattern for /SKIP.
	 */
    protected static final Pattern skipEnd = Pattern.compile("END\\s+SKIP|/SKIP", Pattern.CASE_INSENSITIVE);

    /**
	 * Pattern for JDBC ESCAPE PROCESSING
	 */
    protected static final Pattern JDBC_ESCAPING = Pattern.compile("JDBC\\s+ESCAPE\\s+PROCESSING\\s+(ON|OFF)", Pattern.CASE_INSENSITIVE);

    protected static final Pattern setVariablePattern = Pattern.compile("SET\\s+VARIABLE\\s+(\\w+)\\s*=\\s*((SELECT|VALUES)\\s+.*)", Pattern.CASE_INSENSITIVE);

    /**
	 * Pattern for IF VARIABLE.
	 */
    protected static final Pattern ifVariablePattern = Pattern.compile("IF\\s+VARIABLE\\s+(\\w+)\\s+IS\\s+(NOT\\s+)?NULL", Pattern.CASE_INSENSITIVE);

    /**
	 * Pattern for ELSE.
	 */
    protected static Pattern elsePattern = Pattern.compile("ELSE", Pattern.CASE_INSENSITIVE);

    /**
	 * Pattern for /IF.
	 */
    protected static Pattern ifEndPattern = Pattern.compile("END\\s+IF|/IF", Pattern.CASE_INSENSITIVE);

    protected static Pattern runPattern = Pattern.compile("\\s*RUN\\s+\"(.*)\"", Pattern.CASE_INSENSITIVE);

    protected static Pattern placeHolderPattern = Pattern.compile("&(([A-Za-z\\$_][A-Za-z0-9\\$_]*)|\\{([A-Za-z\\$_][A-Za-z0-9\\$_]*)\\})");

    /**
	 * Current execution context.
	 */
    protected CommandContext context;

    /**
	 * The progress listener.
	 */
    protected ProgressListener progress;

    /**
	 * Constructor.
	 *
	 * @param listener Listens to the progress.
	 */
    public CommandProcessor(ProgressListener listener) {
        this.progress = listener;
    }

    /**
	 * Execute the given command.
	 *
	 * @param command The command to be executed.
	 * @return Whenever an {@link SQLException} is ignored.
	 * @throws SQLExecutionException Whenever an {@link SQLException} occurs during the execution of a command.
	 */
    protected SQLExecutionException executeWithListeners(Command command, boolean skip) throws SQLExecutionException {
        substituteVariables(command);
        if (command.isPersistent()) if (!skip) this.progress.executing(command);
        SQLExecutionException result = null;
        try {
            if (!executeListeners(command, skip)) if (!skip) if (command.isPersistent()) executeJdbc(command); else throw new CommandFileException("Unknown command " + command.getCommand(), command.getLocation());
        } catch (SQLException e) {
            SQLExecutionException newException = new SQLExecutionException(command.getCommand(), command.getLocation(), e);
            String error = e.getSQLState();
            if (!this.context.ignoreSQLError(error)) {
                this.progress.exception(newException);
                throw newException;
            }
            result = newException;
        }
        if (command.isPersistent()) if (!skip) this.progress.executed(); else this.progress.skipped(command);
        return result;
    }

    /**
	 * Substitutes place holders in the command with the values from the variables.
	 *
	 * @param command The command.
	 */
    protected void substituteVariables(Command command) {
        if (!this.context.hasVariables()) return;
        if (!command.getCommand().contains("&")) return;
        Matcher matcher = placeHolderPattern.matcher(command.getCommand());
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String name = matcher.group(2);
            if (name == null) name = matcher.group(3);
            name = name.toUpperCase();
            if (this.context.hasVariable(name)) {
                String value = this.context.getVariableValue(name);
                if (value == null) throw new CommandFileException("Variable '" + name + "' is null", command.getLocation());
                matcher.appendReplacement(sb, value);
            }
        }
        matcher.appendTail(sb);
        command.setCommand(sb.toString());
    }

    /**
	 * Give the listeners a chance to react to the given command.
	 *
	 * @param command The command to be executed.
	 * @return True if a listener has processed the command, false otherwise.
	 * @throws SQLException If the database throws an exception.
	 */
    protected boolean executeListeners(Command command, boolean skip) throws SQLException {
        String sql = command.getCommand();
        Matcher matcher;
        if (command.isTransient()) {
            if ((matcher = sectionPattern.matcher(sql)).matches()) {
                section(matcher.group(1), matcher.group(2), command);
                return true;
            }
            if ((matcher = delimiterPattern.matcher(sql)).matches()) {
                setDelimiters(parseDelimiters(matcher));
                return true;
            }
            if ((matcher = ignoreSqlErrorPattern.matcher(sql)).matches()) {
                this.context.pushIgnores(matcher.group(1));
                return true;
            }
            if (ignoreEnd.matcher(sql).matches()) {
                this.context.popIgnores();
                return true;
            }
            if ((matcher = selectConnectionPattern.matcher(sql)).matches()) {
                selectConnection(matcher.group(1), command);
                return true;
            }
            if ((matcher = setVariablePattern.matcher(sql)).matches()) {
                setVariableFromSelect(matcher.group(1), matcher.group(2));
                return true;
            }
            if ((matcher = ifVariablePattern.matcher(sql)).matches()) {
                ifVariableIsNull(matcher.group(1), matcher.group(2), command);
                return true;
            }
            if (elsePattern.matcher(sql).matches()) {
                this.context.doElse(command.getLocation());
                return true;
            }
            if (ifEndPattern.matcher(sql).matches()) {
                this.context.endIf(command.getLocation());
                return true;
            }
            if ((matcher = setUserPattern.matcher(sql)).matches()) {
                setUser(matcher.group(1));
                return true;
            }
            if (skipPattern.matcher(sql).matches()) {
                this.context.skip(true);
                return true;
            }
            if (skipEnd.matcher(sql).matches()) {
                this.context.endSkip(command.getLocation());
                return true;
            }
            if ((matcher = JDBC_ESCAPING.matcher(sql)).matches()) {
                this.context.setJdbcEscaping(matcher.group(1).equalsIgnoreCase("ON"));
                return true;
            }
            if (encodingPattern.matcher(sql).matches()) {
                return true;
            }
        } else if (!skip) {
            if ((matcher = runPattern.matcher(sql)).matches()) {
                run(matcher.group(1));
                return true;
            }
        }
        for (CommandListener listener : PluginManager.listeners) if (listener.execute(this, command, skip)) return true;
        return false;
    }

    public Statement createStatement() throws SQLException {
        Connection connection = getCurrentDatabase().getConnection();
        Assert.isFalse(connection.getAutoCommit(), "Autocommit should be false");
        Statement statement = connection.createStatement();
        statement.setEscapeProcessing(this.context.getJdbcEscaping());
        return statement;
    }

    /**
	 * Prepares a new statement from the current connection.

	 * @param sql The SQL for the statement.
	 * @return The prepared statement.
	 * @throws SQLException Whenever JDBC throws an SQLException.
	 */
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        Connection connection = getCurrentDatabase().getConnection();
        Assert.isFalse(connection.getAutoCommit(), "Autocommit should be false");
        PreparedStatement statement = connection.prepareStatement(sql);
        return statement;
    }

    /**
	 * Closes the given statement and commits or rollbacks if the command processor is in auto commit mode.
	 *
	 * @param statement The statement to close.
	 * @param commitOrRollback If the command processor is in auto commit mode, this boolean indicates if commit or
	 *        rollback should be called on the statement's connection. If the command processor is not in auto commit
	 *        mode, this boolean is ignored.
	 */
    public void closeStatement(Statement statement, boolean commitOrRollback) {
        try {
            if (autoCommit()) {
                Connection connection = statement.getConnection();
                if (commitOrRollback) connection.commit(); else connection.rollback();
            }
            statement.close();
        } catch (SQLException e) {
            throw new SystemException(e);
        }
    }

    /**
	 * Execute the given command.
	 *
	 * @param command The command to be executed.
	 * @throws SQLException Whenever an {@link SQLException} occurs during the execution of a command.
	 */
    protected void executeJdbc(Command command) throws SQLException {
        Assert.isTrue(command.isPersistent());
        String sql = command.getCommand();
        if (sql.length() == 0) return;
        Statement statement = createStatement();
        boolean commit = false;
        try {
            statement.execute(sql);
            commit = true;
        } finally {
            closeStatement(statement, commit);
        }
    }

    /**
	 * Sets the current database and initializes it.
	 *
	 * @param database The database to make current.
	 */
    protected void setConnection(Database database) {
        this.context.setCurrentDatabase(database);
        if (database != null) database.init();
    }

    /**
	 * Changes the current user on the current database.
	 *
	 * @param user The user to make current.
	 */
    protected void setUser(String user) {
        this.context.getCurrentDatabase().setCurrentUser(user);
    }

    /**
	 * Starts a new section.
	 *
	 * @param level The level of the section.
	 * @param message The message to be shown.
	 * @param command The command that started this.
	 */
    protected void section(String level, String message, Command command) {
        int l = level != null ? Integer.parseInt(level) : 1;
        if (l < 0 || l > 9) throw new CommandFileException("Section level must be 0..9", command.getLocation());
        if (l > this.context.getSectionLevel() + 1) throw new CommandFileException("Section levels can't be skipped, current section level is " + this.context.getSectionLevel(), command.getLocation());
        this.context.setSectionLevel(l);
        startSection(l, message);
    }

    /**
	 * Starts a new section.
	 *
	 * @param level The level of the section.
	 * @param message The message to be shown.
	 */
    protected void startSection(int level, String message) {
        this.progress.startSection(level, message);
    }

    /**
	 * Runs a different SQL file.
	 *
	 * @param url The path of the SQL file.
	 */
    protected void run(String url) {
        SQLFile file = Factory.openSQLFile(getResource().resolve(url), this.progress);
        SQLProcessor processor = new SQLProcessor(this.progress);
        processor.setContext(new SQLContext(this.context, file.getSource()));
        processor.process();
    }

    /**
	 * Returns the progress listener.
	 *
	 * @return The progress listener.
	 */
    public ProgressListener getProgressListener() {
        return this.progress;
    }

    /**
	 * Sets the progress listener.
	 *
	 * @param callBack The progress listener.
	 */
    public void setCallBack(ProgressListener callBack) {
        this.progress = callBack;
    }

    /**
	 * Closes open files and closes connections.
	 */
    public abstract void end();

    /**
	 * Makes current another configured connection.
	 *
	 * @param name The name of the connection to select.
	 * @param command The command that started this.
	 */
    protected void selectConnection(String name, Command command) {
        name = name.toLowerCase();
        Database database = this.context.getDatabase(name);
        if (database == null) throw new CommandFileException("Database '" + name + "' not configured", command.getLocation());
        setConnection(database);
    }

    /**
	 * Execute the SELECT and set the variable with the result from the SELECT.
	 *
	 * @param name Name of the variable.
	 * @param select The SELECT SQL statement.
	 * @throws SQLException Whenever the database throws one.
	 */
    protected void setVariableFromSelect(String name, String select) throws SQLException {
        Statement statement = createStatement();
        Object value = null;
        try {
            ResultSet result = statement.executeQuery(select);
            if (result.next()) value = result.getObject(1);
        } finally {
            closeStatement(statement, true);
        }
        this.context.setVariable(name.toUpperCase(), value);
    }

    /**
	 * Process the IF VARIABLE IS [NOT] NULL annotation.
	 *
	 * @param name The name of the variable.
	 * @param not Is NOT part of the annotation?
	 * @param command The command itself needed for the line number if an exception is thrown.
	 */
    protected void ifVariableIsNull(String name, String not, Command command) {
        if (!this.context.hasVariable(name)) throw new CommandFileException("Variable '" + name + "' is not defined", command.getLocation());
        this.context.skip(this.context.getVariableValue(name) == null != (not == null));
    }

    /**
	 * Parses delimiters.
	 *
	 * @param matcher The matcher.
	 * @return The parsed delimiters.
	 */
    protected static Delimiter[] parseDelimiters(Matcher matcher) {
        Delimiter[] delimiters = new Delimiter[matcher.group(6) != null ? 2 : 1];
        for (int i = 0; i < delimiters.length; i++) {
            int j = i * 3 + 3;
            String delimiter = matcher.group(j);
            j -= 2;
            Delimiter.Type type = Type.FREE;
            if (matcher.group(j++) != null) type = Type.ISOLATED; else if (matcher.group(j) != null) type = Type.TRAILING;
            delimiters[i] = new Delimiter(delimiter, type);
        }
        return delimiters;
    }

    /**
	 * Overrides the current delimiters.
	 *
	 * @param delimiters The delimiters.
	 */
    protected abstract void setDelimiters(Delimiter[] delimiters);

    /**
	 * Returns the current database.
	 *
	 * @return The current database.
	 */
    public Database getCurrentDatabase() {
        return this.context.getCurrentDatabase();
    }

    /**
	 * Returns the default database.
	 *
	 * @return The default database.
	 */
    public Database getDefaultDatabase() {
        return this.context.getDatabase("default");
    }

    /**
	 * Returns the {@link SourceReader} that is the source of the commands.
	 *
	 * @return the {@link SourceReader} that is the source of the commands.
	 */
    public abstract SourceReader getReader();

    /**
	 * Returns the underlying resource.
	 *
	 * @return The underlying resource.
	 */
    public abstract Resource getResource();

    /**
	 * If true ({@link UpgradeProcessor}), commands get committed automatically, and rolled back when an {@link SQLException} occurs.
	 * If false ({@link SQLProcessor}), commit/rollback should be in the command source.
	 *
	 * @return True if commands get committed or rollbacked automatically, false otherwise.
	 */
    public abstract boolean autoCommit();
}
