package net.etherstorm.jopenrpg.swing.nodehandlers.newd20tool.d20tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import net.etherstorm.jopenrpg.swing.nodehandlers.newd20tool.D20CharacterTool;
import org.jdom.Element;

/** 
 * 
 * 
 * @author Ted Berg (<a href="mailto:tedberg@users.sourceforge.net">tedberg@users.sourceforge.net</a>)
 * @version $Revision: 1.8 $
 */
public class ClassesData extends D20ChildNode {

    public ClassesData() {
    }

    /** 
	 * 
	 * 
	 * @param e 
	 * @//throws UnsupportedVersionException 
	 */
    public ClassesData(Element e) {
        super(e);
    }

    public ClassesData(D20CharacterTool handler) {
        setHandler(handler);
    }

    /** 
	 * 
	 * 
	 * @return 
	 */
    public Element toXML() {
        Element e = super.toXML("classes");
        e.setAttribute("level", String.valueOf(getCharacterLevel()));
        try {
            Iterator iter = getClasses().iterator();
            while (iter.hasNext()) e.addContent(((CharacterClass) iter.next()).toXML());
        } catch (Exception ex) {
        }
        return e;
    }

    /** 
	 * 
	 * 
	 * @param e 
	 * @//throws UnsupportedVersionException 
	 */
    public void fromXML(Element e) {
        super.fromXML(e);
        Element elem = handleVersioning(e);
        try {
            setCharacterLevel(Integer.parseInt(elem.getAttributeValue("level", "0")));
        } catch (Exception ex) {
        }
        Iterator iter = elem.getChildren("class").iterator();
        while (iter.hasNext()) addClass(new CharacterClass((Element) iter.next()));
    }

    /**
	 * @param class1
	 */
    public void addClass(CharacterClass class1) {
        class1.setHandler(getHandler());
        getClasses().add(class1);
    }

    protected int _characterLevel;

    public static final String PROPERTY_CHARACTERLEVEL = "characterLevel";

    public void setCharacterLevel(int val) {
        if (val == _characterLevel) return;
        int oldval = _characterLevel;
        _characterLevel = val;
        firePropertyChange(PROPERTY_CHARACTERLEVEL, oldval, _characterLevel);
    }

    protected ArrayList<CharacterClass> _classes;

    public static final String PROPERTY_CLASSES = "classes";

    public void setClasses(ArrayList<CharacterClass> val) {
        try {
            if (val.equals(_classes)) return;
        } catch (Exception ex) {
            return;
        }
        ArrayList<CharacterClass> oldval = _classes;
        _classes = val;
        firePropertyChange(PROPERTY_CLASSES, oldval, _classes);
    }

    public ArrayList<CharacterClass> getClasses() {
        if (_classes == null) setClasses(new ArrayList<CharacterClass>());
        return _classes;
    }

    public int getEffectiveCharacterLevel() {
        try {
            return getCharacterLevel() + Integer.parseInt(getHandler().getGeneralData().get(GeneralData.LEVEL_ADJUSTMENT, "0"));
        } catch (NumberFormatException e) {
            return getCharacterLevel();
        }
    }

    public int getCharacterLevel() {
        Iterator iter = getClasses().iterator();
        int level = 0;
        while (iter.hasNext()) {
            CharacterClass cc = (CharacterClass) iter.next();
            level += cc.getLevelAdjustment();
        }
        return level;
    }

    public String[] getClassNames() {
        Iterator<CharacterClass> iter = getClasses().iterator();
        ArrayList<String> result = new ArrayList<String>();
        while (iter.hasNext()) {
            CharacterClass cc = iter.next();
            System.out.println("Adding class " + cc.getClassName());
            if (cc.getLevelAdjustment() > 0 && result.indexOf(cc.getClassName()) == -1) result.add(cc.getClassName());
        }
        System.out.println(result);
        return result.toArray(new String[] {});
    }

    public int[] getClassLevels() {
        String[] classNames = getClassNames();
        int[] result = new int[classNames.length];
        Iterator iter = getClasses().iterator();
        while (iter.hasNext()) {
            CharacterClass cc = (CharacterClass) iter.next();
            int index = Arrays.asList(classNames).indexOf(cc.getClassName());
            if (index > -1) result[index] += cc.getLevelAdjustment();
        }
        return result;
    }

    public int getTotalHitPoints() {
        int result = 0;
        int realLevels = 0;
        Iterator iter = getClasses().iterator();
        while (iter.hasNext()) {
            CharacterClass cl = (CharacterClass) iter.next();
            result += cl.getHitPoints();
            realLevels += cl.getLevelAdjustment() * (cl.getHitPoints() > 0 ? 1 : 0);
        }
        return result + realLevels * getHandler().getAbilityData().getFromAbbrev("con").getEffectiveModifier();
    }

    public int getTotalSkillPoints() {
        int result = 0;
        Iterator iter = getClasses().iterator();
        while (iter.hasNext()) result += ((CharacterClass) iter.next()).getSkillPoints();
        return result;
    }
}
