package org.oboedit.gui.actions;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import org.bbop.framework.GUIManager;
import org.obo.datamodel.Link;
import org.obo.datamodel.LinkDatabase;
import org.obo.datamodel.LinkedObject;
import org.obo.datamodel.OBOProperty;
import org.obo.datamodel.ObsoletableObject;
import org.obo.datamodel.PathCapable;
import org.obo.datamodel.impl.OBOClassImpl;
import org.obo.filters.EqualsComparison;
import org.obo.filters.Filter;
import org.obo.filters.LinkFilter;
import org.obo.filters.LinkFilterFactory;
import org.obo.filters.NameSearchCriterion;
import org.obo.filters.ObjectFilter;
import org.obo.filters.ObjectFilterFactory;
import org.obo.history.DeleteLinkHistoryItem;
import org.obo.history.DestroyObjectHistoryItem;
import org.obo.history.HistoryItem;
import org.obo.history.ObsoleteObjectHistoryItem;
import org.obo.history.RemoveConsiderHistoryItem;
import org.obo.history.RemoveReplacementHistoryItem;
import org.obo.history.TermMacroHistoryItem;
import org.obo.util.TermUtil;
import org.oboedit.controller.SessionManager;
import org.oboedit.gui.ClickMenuAction;
import org.oboedit.gui.EditAction;
import org.oboedit.gui.GestureTarget;
import org.oboedit.gui.Preferences;
import org.oboedit.gui.Selection;
import org.apache.log4j.*;

/**
 * <p>
 * @author Jennifer I Deegan and John Day-Richter
 * </p><p>
 * Enables the user to destroy or obsolete terms. Methods are included to ensure that if terms to be obsoleted are mentioned in 
 * consider or replaced_by tags then this information will be displayed in the dialogue box, along a warning that the 
 * the user is deleting the last relationship between the term and any parent and that obsoletion will occur.</p>
 *</p>
 */
public class DeleteAction implements ClickMenuAction {

    protected static final Logger logger = Logger.getLogger(DeleteAction.class);

    protected boolean isLegal = false;

    protected KeyStroke keyStroke;

    protected boolean shouldDestroy = false;

    protected boolean deleteTerm = false;

    protected boolean legacyMode = false;

    protected List<PathCapable> deleteThese = new ArrayList<PathCapable>();

    protected int lastInstanceCount = 0;

    protected String instanceString = "";

    protected String wontDelete = "";

    boolean usedInReplacementTag = false;

    public boolean getUsedInReplacementTag() {
        return usedInReplacementTag;
    }

    public void setUsedInReplacementTag(boolean usedInReplacementTag) {
        this.usedInReplacementTag = usedInReplacementTag;
    }

    HashMap<String, Collection<Object>> obsoleteTermsLinkedToAllTerms;

    LinkFilterFactory lff = new LinkFilterFactory();

    ObjectFilterFactory off = new ObjectFilterFactory();

    protected Comparator<PathCapable> pcComparator = new Comparator<PathCapable>() {

        public int compare(PathCapable o1, PathCapable o2) {
            if (o1 instanceof Link) return -1; else if (o2 instanceof Link) return 1; else return 0;
        }
    };

    public DeleteAction(boolean shouldDestroy) {
        setShouldDestroy(shouldDestroy);
    }

    public void setShouldDestroy(boolean shouldDestroy) {
        this.shouldDestroy = shouldDestroy;
        keyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, (shouldDestroy ? KeyEvent.SHIFT_DOWN_MASK : 0));
    }

    public void clickInit(Selection selection, GestureTarget destItem) {
        instanceString = "";
        wontDelete = "";
        lastInstanceCount = 0;
        if (selection == null || selection.isEmpty()) {
            isLegal = false;
            return;
        }
        if (destItem != null && destItem.getTerm() != null && !(selection.getAllSelectedObjects().contains(destItem.getTerm()))) {
            isLegal = false;
            return;
        }
        deleteTerm = false;
        isLegal = getDeletionItems(selection, deleteThese);
        for (Object o : deleteThese) {
            if (o instanceof Link) {
                Link l = (Link) o;
                if (TermUtil.isImplied(l)) {
                    deleteThese.remove(o);
                }
            }
        }
        if (deleteThese.size() < 1) {
            isLegal = false;
        }
        if (shouldDestroy && !deleteTerm) {
            isLegal = false;
        }
    }

    protected boolean getDeletionItems(Selection selection, Collection<PathCapable> out) {
        deleteThese.clear();
        legacyMode = selection.getSelector() != null && selection.getSelector().hasCombinedTermsAndLinks();
        boolean warnBeforeDelete = Preferences.getPreferences().getWarnBeforeDelete();
        deleteTerm = false;
        for (PathCapable pc : selection.getAllSelectedObjects()) {
            if (pc instanceof LinkedObject) {
                LinkedObject lo = (LinkedObject) pc;
                if (hasNonObsoleteChildren(lo)) {
                    wontDelete += lo.getName() + " (" + lo.getID() + ")" + " will not be deleted because it still has children.\n";
                } else {
                    if (!legacyMode || lo.getParents().size() <= 1) {
                        deleteTerm = true;
                        lastInstanceCount++;
                        instanceString += lo.getName() + " (" + lo.getID() + ")\n";
                        for (Link parentLink : lo.getParents()) {
                            if (!out.contains(parentLink)) {
                                out.add(parentLink);
                            }
                        }
                    }
                    out.add(lo);
                }
            } else if (pc instanceof Link) {
                Link link = (Link) pc;
                if (!TermUtil.isImplied(link) && link.getType() != null) {
                    if (link.getChild().getParents().size() > 1) {
                        out.add(pc);
                    }
                }
            }
        }
        return true;
    }

    private boolean hasNonObsoleteChildren(LinkedObject lo) {
        if (lo.getChildren().size() == 0) return false;
        for (Link childLink : lo.getChildren()) {
            LinkedObject child = childLink.getChild();
            if (TermUtil.isObsolete(child)) ; else {
                return true;
            }
        }
        return false;
    }

    public HistoryItem execute() {
        if (shouldDestroy) {
            if (JOptionPane.showConfirmDialog(GUIManager.getManager().getFrame(), wontDelete + "\nThese terms will be permanently destroyed:\n" + instanceString + "They will be entirely removed from the ontology\n" + "and will not appear as obsolete terms.\nAre " + "you sure you want to proceed?", "Destroy warning", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return null;
        } else if (Preferences.getPreferences().getWarnBeforeDelete()) {
            setUsedInReplacementTag(false);
            String replacedMessage = getObsoletesLinkedToThese().toString();
            replacedMessage = replacedMessage.replace("{", "\n\n ");
            replacedMessage = replacedMessage.replace("=[", ": ");
            replacedMessage = replacedMessage.replace("]}", "\n");
            replacedMessage = replacedMessage.replace(",", "\n");
            replacedMessage = replacedMessage.replace("]", "");
            if (lastInstanceCount > 0 && !getUsedInReplacementTag()) {
                String deleteQuestion = wontDelete + ((lastInstanceCount > 1) ? "\nThese are the last appearances of the following terms:" : "\nThis is the last appearance of the following term:") + "\n" + instanceString + "\nAre you sure you want to make " + ((lastInstanceCount > 1) ? "these terms" : "this term") + " permanently obsolete?";
                logger.info(deleteQuestion);
                int answer = JOptionPane.showConfirmDialog(GUIManager.getManager().getFrame(), deleteQuestion, "Delete warning", JOptionPane.YES_NO_OPTION);
                if (answer != JOptionPane.YES_OPTION) {
                    logger.info("User decided not to delete " + instanceString);
                    setUsedInReplacementTag(false);
                    return null;
                }
            }
            if (lastInstanceCount > 0 && getUsedInReplacementTag()) {
                logger.debug("DeleteAction: execute: deleteThese = " + deleteThese);
                String considerTagQuestion = ((lastInstanceCount > 1) ? "These are the last appearances of the selected terms. " + "Please update the replaced_by or consider tags that reference these terms:" : "This is the last appearance of the selected term. " + "Please update the replaced_by or consider tags that reference this term:") + replacedMessage + "\nAre you sure you want to make " + ((lastInstanceCount > 1) ? "these terms" : "this term") + " permanently obsolete?";
                logger.info(considerTagQuestion);
                int answer = JOptionPane.showConfirmDialog(GUIManager.getManager().getFrame(), considerTagQuestion, "Delete warning", JOptionPane.YES_NO_OPTION);
                if (answer != JOptionPane.YES_OPTION) {
                    logger.info("User decided not to delete " + instanceString);
                    setUsedInReplacementTag(false);
                    return null;
                }
            }
        }
        Collections.sort(deleteThese, pcComparator);
        logger.info("Candidates for " + (shouldDestroy ? "destruction:" : "deletion: ") + deleteThese);
        TermMacroHistoryItem out = new TermMacroHistoryItem("No items to delete");
        for (PathCapable pc : deleteThese) {
            if (pc instanceof Link) {
                Link link = (Link) pc;
                if (link.getType() == null) out.addItem(new DeleteLinkHistoryItem((Link) pc)); else if (link.getType().equals(OBOProperty.CONSIDER)) {
                    out.addItem(new RemoveConsiderHistoryItem(link.getParent().getID(), link.getChild().getID()));
                } else if (link.getType().equals(OBOProperty.REPLACES)) {
                    out.addItem(new RemoveReplacementHistoryItem(link.getParent().getID(), link.getChild().getID()));
                } else out.addItem(new DeleteLinkHistoryItem((Link) pc));
            } else if (pc instanceof LinkedObject && (!legacyMode || (legacyMode && deleteTerm))) {
                if (shouldDestroy) out.addItem(new DestroyObjectHistoryItem((LinkedObject) pc)); else out.addItem(new ObsoleteObjectHistoryItem((LinkedObject) pc));
            }
        }
        if (out.size() == 0) {
            return null;
        } else if (out.size() == 1) {
            return out.getItemAt(0);
        } else {
            return out;
        }
    }

    /**
	 * <p>Finds a list of previously obsoleted terms referencing those terms being obsoleted by the user. 
	 * </p><p>
	 * This method finds the names of each of the terms chosen by the user for obsoletion. 
	 *  </p><p>
	 * It loops through these terms calling the getObjectFilter and filterLinks methods for each. 
	 * </p><p>
	 * The getObjectFilter method makes a filter that will check any term name to see if it corresponds to 
	 *  selected term (in this case a term name that the user selected for obsoletion). 
	 * 
	 * </p><p>
	 * The filterLinks method is then called.
	 * 
	 *  </p><p>
	 * In it, the SessionManager is used to get the linkDatabase, and then TermUtil is used
	 * to get the complete set of obsolete terms. 
	 * </p><p>
	 * The method 
	 * iterates through each of the total set of previously obsoleted terms. For each previously 
	 * obsoleted term it gets the set of consider and replaced_by tags and checks to see if the child attached to 
	 * this tag matches the filter 
	 * made in getObjectFilter. That is, it checks to see if any of the term names referenced in a pre-existing 
	 * consider or replaced_by tag matches the name of the term that the user is trying to obsolete.
	 * </p><p>
	 * If the term slated for obsoletion is attached to a consider or replaced_by tag,
	 *  then the name of the previously obsoleted term or terms that carries the
	 * consider or replaced_by tag will be returned to this current method 
	 * in a Hashset. Back in the calling method, each HashSet is then entered as a value in a 
	 * HashMap with the key being the name of the term that the user is obsoleting. 
	 * </p><p>
	 * HashMap:<br>
	 * key = name of term the user is trying to obsolete,
	 * value = list of previously obsoleted terms that reference this term via
	 * a consider or replaced_by tag
	 * </p><p>
	 * The HashMap is returned so that the toString return value can be formatted for display in a dialog box.
	 * </p><p>
	 * For reference, the deleteThese collection may contain multiple terms and links, and is like this:</p><p>
	 * [Child term name A --OBO_REL:is_a--> Parent term name B, Child term name A]
	 * </p><p>
	 * This collection is copied and the copy is modified to get the list of terms the user wishes to obsolete. </p>
	 */
    public HashMap<String, Collection<Object>> getObsoletesLinkedToThese() {
        List<PathCapable> modifiedDeleteThese = new ArrayList<PathCapable>();
        modifiedDeleteThese.addAll(deleteThese);
        obsoleteTermsLinkedToAllTerms = new HashMap<String, Collection<Object>>();
        for (Object termToObsolete : modifiedDeleteThese) {
            if (termToObsolete.getClass().getSimpleName().equals("OBOClassImpl")) {
                String termToBeObsoleted = termToObsolete.toString();
                ObjectFilter ofilter = getObjectFilter(termToBeObsoleted);
                Collection<Object> obsoleteTermsLinkedToOneTerm = filterLinks(ofilter);
                if (!obsoleteTermsLinkedToOneTerm.isEmpty()) {
                    obsoleteTermsLinkedToAllTerms.put(termToBeObsoleted, obsoleteTermsLinkedToOneTerm);
                }
            }
        }
        return obsoleteTermsLinkedToAllTerms;
    }

    /**<p>
	 * Creates a custom filter object to find terms whose name matches the name of the term that the user has selected.
	 * </p><p>
	 * Filter produced looks like this: Name equals "[name of selected term ]"</p>
	 */
    public ObjectFilter getObjectFilter(String id) {
        ObjectFilter ofilter = (ObjectFilter) off.createNewFilter();
        EqualsComparison c = new EqualsComparison();
        ofilter.setComparison(c);
        ofilter.setValue(id);
        ofilter.setCriterion(new NameSearchCriterion());
        logger.debug("FilterTest: getLinkFilter: ofilter = " + ofilter);
        LinkFilter lfilter = (LinkFilter) lff.createNewFilter();
        lfilter.setFilter(ofilter);
        return ofilter;
    }

    /**
	 * See docs for getObsoletesLinkedToThese() from which this method is called. 
	 * @param filter
	 * @return matches
	 */
    public Collection<Object> filterLinks(Filter filter) {
        Collection<Object> matches = new HashSet<Object>();
        LinkDatabase ldb = SessionManager.getManager().getSession().getLinkDatabase();
        Collection<ObsoletableObject> allObsoletes = TermUtil.getObsoletes(ldb);
        for (Object obsoleteo : allObsoletes) {
            if (!obsoleteo.getClass().getSimpleName().equals("OBOPropertyImpl")) {
                OBOClassImpl obsoleteObject = (OBOClassImpl) obsoleteo;
                Set<?> replacedBySet = obsoleteObject.getReplacedBy();
                for (Object replacementTerm : replacedBySet) {
                    if (filter.satisfies(replacementTerm)) {
                        setUsedInReplacementTag(true);
                        System.out.println(usedInReplacementTag);
                        matches.add(obsoleteObject);
                    }
                }
                Set<?> considerSet = obsoleteObject.getConsiderReplacements();
                for (Object considerTerm : considerSet) {
                    if (filter.satisfies(considerTerm)) {
                        setUsedInReplacementTag(true);
                        matches.add(obsoleteObject);
                    }
                }
            }
        }
        return matches;
    }

    public KeyStroke getKeyStroke() {
        return keyStroke;
    }

    public String getName() {
        if (shouldDestroy) {
            if (deleteTerm) return "Destroy term"; else return "Destroy";
        } else return "Delete " + ((deleteTerm) ? "term (make obsolete)" : "relationship");
    }

    public String getDesc() {
        if (shouldDestroy) return "Destroying"; else return "Deleting";
    }

    public List<EditAction> getSubActions() {
        return null;
    }

    public boolean isLegal() {
        return isLegal;
    }
}
