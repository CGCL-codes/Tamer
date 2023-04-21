package org.opennms.netmgt.dao.castor;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.opennms.netmgt.config.ackd.AckdConfiguration;
import org.opennms.netmgt.config.ackd.Parameter;
import org.opennms.netmgt.config.ackd.Reader;
import org.opennms.netmgt.config.ackd.ReaderSchedule;
import org.opennms.netmgt.dao.AckdConfigurationDao;
import org.springframework.dao.DataAccessResourceFailureException;

/**
 * Default implementation of <code>AckdConfiguration</code> containing utility methods for manipulating
 * the <code>Ackd</code> and <code>AckdReader</code>s.
 * 
 * @author <a href="mailto:david@opennms.org">David Hustace</a>
 */
public class DefaultAckdConfigurationDao extends AbstractCastorConfigDao<AckdConfiguration, AckdConfiguration> implements AckdConfigurationDao {

    public DefaultAckdConfigurationDao() {
        super(AckdConfiguration.class, "Ackd Configuration");
    }

    public AckdConfiguration getConfig() {
        return getContainer().getObject();
    }

    @Override
    public AckdConfiguration translateConfig(AckdConfiguration castorConfig) {
        return castorConfig;
    }

    public Boolean acknowledgmentMatch(List<String> messageText) {
        String expression = getConfig().getAckExpression();
        return matcher(messageText, expression);
    }

    public Boolean clearMatch(List<String> messageText) {
        String expression = getConfig().getClearExpression();
        return matcher(messageText, expression);
    }

    public Boolean escalationMatch(List<String> messageText) {
        String expression = getConfig().getEscalateExpression();
        return matcher(messageText, expression);
    }

    public Boolean unAcknowledgmentMatch(List<String> messageText) {
        String expression = getConfig().getUnackExpression();
        return matcher(messageText, expression);
    }

    private Boolean matcher(List<String> messageText, String expression) {
        Boolean matches = new Boolean(false);
        Pattern p;
        if (expression.startsWith("~")) {
            expression = (expression.startsWith("~") ? expression.substring(1) : expression);
            p = Pattern.compile(expression);
            for (String text : messageText) {
                Matcher m = p.matcher(text);
                matches = m.matches();
                if (matches) {
                    break;
                }
            }
        } else {
            for (String text : messageText) {
                matches = expression.equalsIgnoreCase(text);
            }
        }
        return matches;
    }

    public Reader getReader(String readerName) {
        Reader readerByName = null;
        List<Reader> readers = getConfig().getReaders().getReaderCollection();
        for (Reader reader : readers) {
            if (readerName.equals(reader.getReaderName())) {
                readerByName = reader;
            }
        }
        return readerByName;
    }

    public ReaderSchedule getReaderSchedule(String readerName) {
        ReaderSchedule schedule = null;
        Reader reader = getReader(readerName);
        if (reader != null) {
            schedule = reader.getReaderSchedule();
        }
        return schedule;
    }

    public boolean isReaderEnabled(String readerName) {
        boolean enabled = false;
        Reader reader = getReader(readerName);
        if (reader != null) {
            enabled = reader.isEnabled();
        }
        return enabled;
    }

    /**
     * The exception boils up from the container class  The container class should
     * indicate this.
     * 
     */
    public void reloadConfiguration() throws DataAccessResourceFailureException {
        getContainer().reload();
    }

    public int getEnabledReaderCount() {
        int cnt = 0;
        Iterator<Reader> it = getConfig().getReaders().getReaderCollection().iterator();
        while (it.hasNext()) {
            Reader reader = (Reader) it.next();
            if (reader.isEnabled()) {
                cnt++;
            }
        }
        return cnt;
    }

    public List<Parameter> getParametersForReader(String name) {
        return getReader(name).getParameterCollection();
    }
}
