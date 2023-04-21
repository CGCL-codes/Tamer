package blue.scripting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import blue.utility.ListUtil;
import electric.xml.Element;
import electric.xml.Elements;

/**
 * @author Steven Yi
 * 
 * ScriptCategory holds Scripts and other ScriptCategories
 */
public class ScriptCategory implements Serializable {

    private String categoryName = "New Script Category";

    private ArrayList subCategories = new ArrayList();

    private ArrayList scripts = new ArrayList();

    private boolean isRoot = false;

    public String toString() {
        return this.getCategoryName();
    }

    public boolean removeScript(Script effect) {
        int index = ListUtil.indexOfByRef(scripts, effect);
        if (index >= 0) {
            scripts.remove(index);
            return true;
        }
        for (Iterator iter = subCategories.iterator(); iter.hasNext(); ) {
            ScriptCategory category = (ScriptCategory) iter.next();
            if (category.removeScript(effect)) {
                return true;
            }
        }
        return false;
    }

    public boolean removeScriptCategory(ScriptCategory category) {
        int index = ListUtil.indexOfByRef(subCategories, category);
        if (index >= 0) {
            subCategories.remove(index);
            return true;
        }
        for (Iterator iter = subCategories.iterator(); iter.hasNext(); ) {
            ScriptCategory tempCategory = (ScriptCategory) iter.next();
            if (tempCategory.removeScriptCategory(category)) {
                return true;
            }
        }
        return false;
    }

    public void addScriptCategory(ScriptCategory category) {
        subCategories.add(category);
    }

    public void addScript(int index, Script effect) {
        scripts.add(index, effect);
    }

    public void addScript(Script effect) {
        scripts.add(effect);
    }

    /**
     * @return Returns the categoryName.
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * @param categoryName
     *            The categoryName to set.
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * @return Returns the instruments.
     */
    public ArrayList getScripts() {
        return scripts;
    }

    /**
     * @param scripts
     *            The scripts to set.
     */
    public void setScripts(ArrayList scripts) {
        this.scripts = scripts;
    }

    /**
     * @return Returns the subCategories.
     */
    public ArrayList getSubCategories() {
        return subCategories;
    }

    /**
     * @param subCategories
     *            The subCategories to set.
     */
    public void setSubCategories(ArrayList subCategories) {
        this.subCategories = subCategories;
    }

    /**
     * @return Returns the isRoot.
     */
    public boolean isRoot() {
        return isRoot;
    }

    /**
     * @param isRoot
     *            The isRoot to set.
     */
    public void setRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    public static ScriptCategory loadFromXML(Element data) throws Exception {
        ScriptCategory scriptCat = new ScriptCategory();
        scriptCat.setCategoryName(data.getAttributeValue("categoryName"));
        scriptCat.setRoot(Boolean.valueOf(data.getAttributeValue("isRoot")).booleanValue());
        Elements subCatNodes = data.getElements("scriptCategory");
        while (subCatNodes.hasMoreElements()) {
            scriptCat.addScriptCategory(ScriptCategory.loadFromXML(subCatNodes.next()));
        }
        Elements scripts = data.getElements("script");
        while (scripts.hasMoreElements()) {
            scriptCat.addScript(Script.loadFromXML(scripts.next()));
        }
        return scriptCat;
    }

    public Element saveAsXML() {
        Element retVal = new Element("scriptCategory");
        retVal.setAttribute("categoryName", this.getCategoryName());
        retVal.setAttribute("isRoot", Boolean.toString(this.isRoot()));
        for (Iterator iter = subCategories.iterator(); iter.hasNext(); ) {
            ScriptCategory tempCat = (ScriptCategory) iter.next();
            retVal.addElement(tempCat.saveAsXML());
        }
        for (Iterator iter = scripts.iterator(); iter.hasNext(); ) {
            Script effect = (Script) iter.next();
            retVal.addElement(effect.saveAsXML());
        }
        return retVal;
    }
}
