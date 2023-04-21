package uk.ac.reload.straker.datamodel.learningdesign.types;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jdom.Element;
import uk.ac.reload.straker.datamodel.DataComponent;
import uk.ac.reload.straker.datamodel.DataModel;
import uk.ac.reload.straker.datamodel.learningdesign.LD_CheckList;
import uk.ac.reload.straker.datamodel.learningdesign.LD_CheckListItem;
import uk.ac.reload.straker.datamodel.learningdesign.LD_DataComponent;
import uk.ac.reload.straker.datamodel.learningdesign.expressions.WhenPropertyValueIsSetType;

/**
 * Complete Activity type
 * 
 * @author Phillip Beauvoir
 * @version $Id: AbstractCompleteType.java,v 1.6 2006/07/10 11:50:38 phillipus Exp $
 */
public abstract class AbstractCompleteType extends LD_DataComponent {

    public static String TIME_LIMIT = "time-limit";

    public static String WHEN_PROPERTY_VALUE_IS_SET = "when-property-value-is-set";

    /**
     * Choices to set
     */
    private String _choice;

    /**
     * Time Limit
     */
    private TimeLimitType _timeLimitType;

    /**
     * When Property Value Is Set Type Array
     */
    private ArrayList _propertyValueTypeList = new ArrayList();

    /**
     * Default constructor
     */
    protected AbstractCompleteType(DataModel dataModel) {
        super(dataModel);
    }

    /**
     * Set the Choice or null for not set
     * @param s
     */
    public void setChoice(String s) {
        _choice = s;
    }

    /**
     * @return The Choice
     */
    public String getChoice() {
        return _choice;
    }

    /**
     * @return The Time Limit Type
     */
    public TimeLimitType getTimeLimitType() {
        if (_timeLimitType == null) {
            _timeLimitType = new TimeLimitType(getDataModel());
        }
        return _timeLimitType;
    }

    /**
     * @return The When Property Value Is Set Types
     */
    public WhenPropertyValueIsSetType[] getWhenPropertyValueIsSetTypes() {
        return (WhenPropertyValueIsSetType[]) _propertyValueTypeList.toArray(new WhenPropertyValueIsSetType[_propertyValueTypeList.size()]);
    }

    /**
     * Add a WhenPropertyValueIsSetType DataComponent
     * @param propertyValueType
     */
    public void addWhenPropertyValueIsSetType(WhenPropertyValueIsSetType propertyValueType) {
        if (propertyValueType != null && !_propertyValueTypeList.contains(propertyValueType)) {
            _propertyValueTypeList.add(propertyValueType);
            propertyValueType.setParent(this);
        }
    }

    /**
     * Remove a WhenPropertyValueIsSetType DataComponent
     * @param propertyValueType
     */
    public void removeWhenPropertyValueIsSetType(WhenPropertyValueIsSetType propertyValueType) {
        if (propertyValueType != null && _propertyValueTypeList.contains(propertyValueType)) {
            _propertyValueTypeList.remove(propertyValueType);
            propertyValueType.setParent(null);
        }
    }

    public void addCheckListItems(LD_CheckList checkList, String category) {
        if (TIME_LIMIT.equals(getChoice())) {
            getTimeLimitType().addCheckListItems(checkList, category);
        } else if (WHEN_PROPERTY_VALUE_IS_SET.equals(getChoice())) {
            String level = getLearningDesign().getLevel();
            if ("A".equals(level)) {
                LD_CheckListItem item = new LD_CheckListItem(category + ": Should be level B or C if using Properties", true);
                checkList.addCheckListItem(item);
            }
            for (int i = 0; i < _propertyValueTypeList.size(); i++) {
                ((LD_DataComponent) _propertyValueTypeList.get(i)).addCheckListItems(checkList, category);
            }
        }
    }

    /**
     * Destroy this Component
     */
    public void dispose() {
        super.dispose();
        if (_timeLimitType != null) {
            _timeLimitType.dispose();
            _timeLimitType = null;
        }
        for (int i = 0; i < _propertyValueTypeList.size(); i++) {
            ((DataComponent) _propertyValueTypeList.get(i)).dispose();
        }
        _propertyValueTypeList.clear();
        _propertyValueTypeList = null;
    }

    public Element marshall2XML(Element parentElement) {
        if (_choice == null) {
            return null;
        }
        Element element = new Element(getXMLElementName());
        parentElement.addContent(element);
        if (TIME_LIMIT.equals(_choice)) {
            getTimeLimitType().marshall2XML(element);
        } else if (WHEN_PROPERTY_VALUE_IS_SET.equals(_choice)) {
            for (int i = 0; i < _propertyValueTypeList.size(); i++) {
                ((DataComponent) _propertyValueTypeList.get(i)).marshall2XML(element);
            }
        }
        return element;
    }

    public void unmarshallXML(Element element) {
        Element timelimit = element.getChild(TIME_LIMIT);
        if (timelimit != null) {
            setChoice(TIME_LIMIT);
            getTimeLimitType().unmarshallXML(timelimit);
            return;
        }
        List list = element.getChildren(WHEN_PROPERTY_VALUE_IS_SET);
        if (!list.isEmpty()) {
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                WhenPropertyValueIsSetType propertyValueType = new WhenPropertyValueIsSetType(getDataModel());
                propertyValueType.unmarshallXML(e);
                addWhenPropertyValueIsSetType(propertyValueType);
            }
            setChoice(WHEN_PROPERTY_VALUE_IS_SET);
            return;
        }
        setChoice(null);
    }
}
