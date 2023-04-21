package com.sitescape.team.remoting.ws.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.Date;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;
import net.fortuna.ical4j.data.ParserException;
import com.sitescape.team.domain.Event;
import com.sitescape.team.module.ical.IcalModule;
import com.sitescape.team.module.shared.InputDataAccessor;
import com.sitescape.team.survey.Survey;
import com.sitescape.team.util.DateUtil;

/**
 * An implementation of <code>InputDataAccessor</code> based on element-only
 * flat dom tree.
 * 
 * @author jong
 *
 */
public class DomInputData implements InputDataAccessor {

    private Document doc;

    private Element root;

    private IcalModule icalModule;

    public DomInputData(Document doc, IcalModule icalModule) {
        this(doc.getRootElement(), icalModule);
        this.doc = doc;
    }

    public DomInputData(Element root, IcalModule icalModule) {
        this.root = root;
        this.icalModule = icalModule;
    }

    public String getSingleValue(String key) {
        Element elem = (Element) root.selectSingleNode("attribute[@name='" + key + "']");
        Element valueElem = (Element) root.selectSingleNode("attribute[@name='" + key + "']/value");
        if (valueElem != null) {
            return valueElem.getText();
        } else if (elem != null) {
            return elem.getText();
        } else {
            return null;
        }
    }

    public Date getDateValue(String key) {
        String textVal = getSingleValue(key);
        if (textVal != null) {
            return DateUtil.parseDate(textVal);
        }
        return null;
    }

    public Event getEventValue(String key, boolean hasDuration, boolean hasRecurrence) {
        Element eventElem = (Element) root.selectSingleNode("attribute[@name='" + key + "']");
        if (eventElem != null) {
            try {
                List<Event> events = icalModule.parseEvents(new StringReader(eventElem.getText()));
                return events.get(0);
            } catch (IOException e) {
            } catch (ParserException e) {
            } catch (IndexOutOfBoundsException e) {
            }
        }
        return null;
    }

    public Survey getSurveyValue(String key) {
        String textVal = getSingleValue(key);
        if (textVal != null) {
            return new Survey(textVal);
        }
        return null;
    }

    public String[] getValues(String key) {
        List nodes = root.selectNodes("attribute[@name='" + key + "']/value");
        if (nodes.size() == 0) {
            nodes = root.selectNodes("attribute[@name='" + key + "']");
        }
        int size = nodes.size();
        if (size > 0) {
            String[] values = new String[size];
            for (int i = 0; i < size; i++) {
                values[i] = ((Element) nodes.get(i)).getText();
            }
            return values;
        } else {
            return null;
        }
    }

    public boolean exists(String key) {
        if (root.selectSingleNode("attribute[@name='" + key + "']") != null) return true; else return false;
    }

    public Object getSingleObject(String key) {
        return getSingleValue(key);
    }

    public int getCount() {
        return root.nodeCount();
    }
}
