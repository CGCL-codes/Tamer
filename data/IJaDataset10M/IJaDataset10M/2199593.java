package at.ac.tuwien.law.yaplaf.plugin.input.dummyReader;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.Logger;
import at.ac.tuwien.law.yaplaf.entities.Paper;
import at.ac.tuwien.law.yaplaf.exceptions.MissingPropertyException;
import at.ac.tuwien.law.yaplaf.interfaces.Input;

public class DummyReader implements Input {

    Logger logger = Logger.getLogger(DummyReader.class);

    private Properties props;

    @Override
    public List<Paper> execute() throws MissingPropertyException {
        List<Paper> papers = new LinkedList<Paper>();
        for (Object o : this.getPropertiesDescription().keySet()) {
            logger.info(o);
            if (!this.props.containsKey(o)) {
                logger.error("Schl�ssel fehlt: " + o.toString());
                throw new MissingPropertyException();
            }
        }
        for (int i = 0; i < 3; i++) {
            Paper p = new Paper();
            p.setNachname(props.getProperty("name" + i));
            p.setMatriculationNumber(String.valueOf(i));
            p.setPaperText(props.getProperty("text" + i));
            papers.add(p);
        }
        return papers;
    }

    @Override
    public Properties getProperties() {
        return props;
    }

    @Override
    public Map<String, String> getPropertiesDescription() {
        Map<String, String> desc = new HashMap<String, String>();
        desc.put("text0", "Der erste DummyText");
        desc.put("name0", "Name0");
        desc.put("text1", "Der zweite DummyText");
        desc.put("name1", "Name1");
        desc.put("text2", "Der dritte DummyText");
        desc.put("name1", "Name1");
        return desc;
    }

    @Override
    public void setProperties(Properties props) throws MissingPropertyException {
        this.props = props;
    }
}
