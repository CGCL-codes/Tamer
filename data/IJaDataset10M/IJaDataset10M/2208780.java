package net.taylor.hibernate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import org.hibernate.AssertionFailure;
import org.hibernate.cfg.DefaultComponentSafeNamingStrategy;
import org.jboss.seam.log.Log;
import org.jboss.seam.log.Logging;

/**
 * @author Chunyun Zhao
 * @author jgilbert
 */
public class NamingStrategy extends DefaultComponentSafeNamingStrategy {

    private static final long serialVersionUID = 1L;

    private static Log log = Logging.getLog(NamingStrategy.class);

    protected static String addUnderscores(String name) {
        return name.replace('.', '_');
    }

    @Override
    public String collectionTableName(String ownerEntity, String ownerEntityTable, String associatedEntity, String associatedEntityTable, String propertyName) {
        return checkTableName(getAbbreviation(tableName(new StringBuilder(ownerEntityTable).append("_").append(associatedEntityTable != null ? associatedEntityTable : addUnderscores(propertyName)).toString())));
    }

    @Override
    public String foreignKeyColumnName(String propertyName, String propertyEntityName, String propertyTableName, String referencedColumnName) {
        String header = propertyName != null ? addUnderscores(propertyName) : propertyTableName;
        if (header == null) throw new AssertionFailure("NamingStrategy not properly filled");
        return checkColumnName(getAbbreviation(columnName(header + "_" + referencedColumnName)));
    }

    @Override
    public String logicalCollectionColumnName(String columnName, String propertyName, String referencedColumn) {
        return getAbbreviation(super.logicalCollectionColumnName(columnName, propertyName, referencedColumn));
    }

    @Override
    public String logicalCollectionTableName(String tableName, String ownerEntityTable, String associatedEntityTable, String propertyName) {
        return getAbbreviation(super.logicalCollectionTableName(tableName, ownerEntityTable, associatedEntityTable, propertyName));
    }

    @Override
    public String classToTableName(String className) {
        return checkTableName(getAbbreviation(super.classToTableName(className)));
    }

    @Override
    public String columnName(String columnName) {
        return checkColumnName(getAbbreviation(super.columnName(columnName)));
    }

    @Override
    public String joinKeyColumnName(String joinedColumn, String joinedTable) {
        return checkColumnName(getAbbreviation(super.joinKeyColumnName(joinedColumn, joinedTable)));
    }

    protected String checkColumnName(String name) {
        if (name.length() > 26) {
            throw new RuntimeException("Column Name greater than 26 characters: " + name + " : " + name.length());
        }
        checkForUncap(name);
        return name;
    }

    protected String checkTableName(String name) {
        if (name.length() > 21) {
            throw new RuntimeException("Table Name greater than 21 characters: " + name + " : " + name.length());
        }
        checkForUncap(name);
        return name;
    }

    protected void checkForUncap(String name) {
        char[] chars = name.toCharArray();
        for (char c : chars) {
            if (Character.isLowerCase(c)) {
                throw new RuntimeException("Uncapitalized name: " + name);
            }
        }
    }

    @Override
    public String tableName(String tableName) {
        return checkTableName(getAbbreviation(super.tableName(tableName)));
    }

    @Override
    public String logicalColumnName(String columnName, String propertyName) {
        return getAbbreviation(super.logicalColumnName(columnName, propertyName));
    }

    @Override
    public String propertyToColumnName(String propertyName) {
        return checkColumnName(getAbbreviation(addUnderscores(propertyName)));
    }

    protected String getAbbreviation(String name) {
        return getProperty(getAbbreviations(name));
    }

    protected String getAbbreviations(String name) {
        String abbrv = getProperty(name);
        if (!abbrv.equals(name)) {
            return abbrv;
        }
        List<String> parts = parseName(name);
        StringBuffer result = new StringBuffer();
        for (Iterator<String> nameIter = parts.iterator(); nameIter.hasNext(); ) {
            String nameComponent = nameIter.next();
            nameComponent = getProperty(nameComponent);
            result.append(nameComponent);
            if (nameIter.hasNext() && nameComponent.length() > 1) {
                result.append("_");
            }
        }
        return result.toString();
    }

    protected List<String> parseName(String sourceName) {
        char sourceSeparator = '_';
        List<String> parts = new ArrayList<String>();
        StringBuffer currentWord = new StringBuffer();
        int length = sourceName.length();
        boolean lastIsLower = false;
        for (int index = 0; index < length; index++) {
            char curChar = sourceName.charAt(index);
            if (Character.isUpperCase(curChar) || (!lastIsLower && Character.isDigit(curChar)) || curChar == sourceSeparator) {
                if (lastIsLower || curChar == sourceSeparator) {
                    parts.add(currentWord.toString());
                    currentWord = new StringBuffer();
                }
                lastIsLower = false;
            } else {
                if (!lastIsLower) {
                    int currentWordLength = currentWord.length();
                    if (currentWordLength > 1) {
                        char lastChar = currentWord.charAt(--currentWordLength);
                        currentWord.setLength(currentWordLength);
                        parts.add(currentWord.toString());
                        currentWord = new StringBuffer();
                        currentWord.append(lastChar);
                    }
                }
                lastIsLower = true;
            }
            if (curChar != sourceSeparator) {
                currentWord.append(curChar);
            }
        }
        parts.add(currentWord.toString());
        return parts;
    }

    protected String getProperty(String word) {
        String abbrv = getProperties().getProperty(word);
        if (abbrv == null) {
            return word;
        } else {
            return abbrv;
        }
    }

    protected static Properties abbrvProperties;

    protected Properties getProperties() {
        if (abbrvProperties == null) {
            try {
                abbrvProperties = new Properties();
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                Enumeration<URL> files = classLoader.getResources("abbreviation.properties");
                while (files.hasMoreElements()) {
                    URL url = (URL) files.nextElement();
                    log.debug("ABBRV URL: {0}", url);
                    InputStream resourceAsStream = url.openStream();
                    try {
                        Properties temp = new Properties();
                        temp.load(resourceAsStream);
                        Set<?> names = sort(temp);
                        for (Object name : names) {
                            if (abbrvProperties.containsKey(name)) {
                                Object value1 = abbrvProperties.get(name);
                                Object value2 = temp.get(name);
                                if (!value1.equals(value2)) {
                                    throw new AssertionFailure("Duplicate Abbreviation Found: " + name + "=" + value2 + ", in: " + url);
                                } else {
                                    log.debug("Duplicate Abbreviation Found: {0}={1}", name, value2);
                                }
                            } else {
                                abbrvProperties.put(name, temp.get(name));
                            }
                        }
                    } finally {
                        resourceAsStream.close();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Unable to load abbreviation properties, ignore it.", e);
            }
        }
        return abbrvProperties;
    }

    protected Set<Object> sort(Properties properties) {
        return new TreeSet<Object>(properties.keySet());
    }

    protected void print(Properties properties) {
        Set<Object> names = sort(properties);
        for (Object name : names) {
            System.out.print(name);
            System.out.print("=");
            System.out.println(properties.get(name));
        }
    }
}
