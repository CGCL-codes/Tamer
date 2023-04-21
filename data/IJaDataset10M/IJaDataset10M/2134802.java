package ru.adv.repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import ru.adv.db.DBUser;
import ru.adv.io.UnknownIOSourceException;
import ru.adv.logger.TLogger;
import ru.adv.util.ErrorCodeException;
import ru.adv.util.InputOutput;
import ru.adv.util.XmlUtils;
import ru.adv.xml.parser.IncludableParser;
import ru.adv.xml.parser.ParserException;

/**
 * 
 * Parse config file with {@link DBUser} list 
 * 
 * @author vic
 *
 */
public class DbUserConfig {

    private TLogger LOGGER = new TLogger(DbUserConfig.class);

    private InputOutput configIO;

    private Map<String, DBUser> dbUsersMap;

    private long lastModified = -1;

    public DbUserConfig(InputOutput configIO) {
        super();
        this.configIO = configIO;
        dbUsersMap = Collections.emptyMap();
    }

    /**
	 * find {@link DBUser} to connect to database
	 * @param dbName
	 * @return
	 */
    public DBUser getDbUserForDatabse(String dbName, String adapterName) {
        readUserList();
        DBUser result = dbUsersMap.get(mapKey(adapterName, dbName));
        if (result == null) {
            result = dbUsersMap.get(mapKey("*", dbName));
        }
        if (result == null) {
            result = dbUsersMap.get(mapKey(adapterName, "*"));
        }
        if (result == null) {
            result = dbUsersMap.get(mapKey("*", "*"));
        }
        if (result == null) {
            result = new DBUser("mozart", "mozart");
        }
        return result;
    }

    private synchronized void readUserList() throws UnknownIOSourceException, ParserException {
        try {
            long fileModificationTime = configIO.lastModified();
            if (lastModified == -1 || fileModificationTime > lastModified) {
                dbUsersMap = parse(configIO);
                lastModified = fileModificationTime;
            }
        } catch (ErrorCodeException e) {
            LOGGER.logErrorStackTrace("Can't read database user config file", e);
        }
    }

    private Map<String, DBUser> parse(InputOutput io) throws ParserException {
        Map<String, DBUser> dbUsers = new HashMap<String, DBUser>();
        final Document doc = new IncludableParser().parse(io).getDocument();
        final List<Element> list = XmlUtils.findAllElements(doc, "dbuser");
        for (Element e : list) {
            String db = e.getAttribute("database");
            String user = e.getAttribute("name");
            String pwd = e.getAttribute("password");
            String adapter = e.getAttribute("adapter");
            String key = mapKey(adapter, db);
            if (dbUsers.containsKey(key)) {
                throw new ParserException(String.format("Double DBUser definition for adapter '%1$s' and database '%2$s' ", adapter, db));
            }
            dbUsers.put(key, new DBUser(user, pwd));
        }
        return dbUsers;
    }

    private static String mapKey(String adapterName, String databaseName) {
        return (StringUtils.hasText(adapterName) ? adapterName.trim() : "*") + "_" + databaseName;
    }
}
