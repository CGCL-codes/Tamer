package plugin.lsttokens;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
import pcgen.base.util.TripleKeyMapToList;
import pcgen.cdom.base.AssociatedPrereqObject;
import pcgen.cdom.base.CDOMList;
import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.base.Constants;
import pcgen.cdom.base.PrereqObject;
import pcgen.cdom.enumeration.AssociationKey;
import pcgen.cdom.list.ClassSpellList;
import pcgen.core.prereq.Prerequisite;
import pcgen.core.spell.Spell;
import pcgen.rules.context.LoadContext;
import pcgen.rules.persistence.token.AbstractSpellListToken;
import pcgen.rules.persistence.token.CDOMPrimaryToken;
import pcgen.util.Logging;

/**
 * The Class <code>SpellknownLst</code> is responsible for parsing and 
 * unparsing the SPELLKNOWN tag. This class is heavily based on the 
 * SpelllevelLst class. <p>
 * Syntax is:
 * <pre>
 * SPELLKNOWN:CLASS|Name1,Name2=Level1|Spell1,Spell2,Spell3|Name3=Level2|Spell4,Spell5|PRExxx|PRExxx
 * </pre>
 * 
 * Last Editor: $Author: $
 * Last Edited: $Date:  $
 * 
 * @author James Dempsey <jdempsey@users.sourceforge.net>
 * @version $Revision:  $
 */
public class SpellknownLst extends AbstractSpellListToken implements CDOMPrimaryToken<CDOMObject> {

    @Override
    public String getTokenName() {
        return "SPELLKNOWN";
    }

    public boolean parse(LoadContext context, CDOMObject obj, String value) {
        if (isEmpty(value) || hasIllegalSeparator('|', value)) {
            return false;
        }
        String workingValue = value;
        List<Prerequisite> prereqs = new ArrayList<Prerequisite>();
        while (true) {
            int lastPipeLoc = workingValue.lastIndexOf('|');
            if (lastPipeLoc == -1) {
                Logging.log(Logging.LST_ERROR, "Invalid " + getTokenName() + " not enough tokens: " + value);
                return false;
            }
            String lastToken = workingValue.substring(lastPipeLoc + 1);
            if (lastToken.startsWith("PRE") || lastToken.startsWith("!PRE")) {
                workingValue = workingValue.substring(0, lastPipeLoc);
                prereqs.add(getPrerequisite(lastToken));
            } else {
                break;
            }
        }
        StringTokenizer tok = new StringTokenizer(workingValue, "|");
        if (tok.countTokens() < 3) {
            Logging.errorPrint("Insufficient values in SPELLKNOWN tag: " + value);
            return false;
        }
        String tagType = tok.nextToken();
        while (tok.hasMoreTokens()) {
            String tokString = tok.nextToken();
            String spellString = tok.nextToken();
            if (tagType.equalsIgnoreCase("CLASS")) {
                if (!subParse(context, obj, ClassSpellList.class, tokString, spellString, prereqs)) {
                    Logging.log(Logging.LST_ERROR, "  " + getTokenName() + " error - entire token was " + value);
                    return false;
                }
            } else {
                Logging.errorPrint("First token of " + getTokenName() + " must be CLASS: " + value);
                return false;
            }
        }
        return true;
    }

    /**
	 * Parse the tag contents after the SPELLKNOWN:CLASS| section.
	 * 
	 * @param context the context under which the tag is being parsed.
	 * @param obj the obj The object owning the tag.
	 * @param tagType the type of object the tag creates 
	 * @param tokString the tok string The string defining the caster type/class and spell level.
	 * @param spellString the spell string The string containing the spell name(s)
	 * @param prereqs the prereqs The prerequisites to be applied.
	 * 
	 * @return true, if successful
	 */
    private <CL extends CDOMObject & CDOMList<Spell>> boolean subParse(LoadContext context, CDOMObject obj, Class<CL> tagType, String tokString, String spellString, List<Prerequisite> prereqs) {
        int equalLoc = tokString.indexOf(Constants.EQUALS);
        if (equalLoc == -1) {
            Logging.errorPrint("Expected an = in SPELLKNOWN " + "definition: " + tokString);
            return false;
        }
        String casterString = tokString.substring(0, equalLoc);
        String spellLevel = tokString.substring(equalLoc + 1);
        Integer splLevel;
        try {
            splLevel = Integer.decode(spellLevel);
        } catch (NumberFormatException nfe) {
            Logging.errorPrint("Expected a number for SPELLKNOWN, found: " + spellLevel);
            return false;
        }
        if (isEmpty(casterString) || hasIllegalSeparator(',', casterString)) {
            return false;
        }
        StringTokenizer clTok = new StringTokenizer(casterString, Constants.COMMA);
        List<CDOMReference<? extends CDOMList<Spell>>> slList = new ArrayList<CDOMReference<? extends CDOMList<Spell>>>();
        while (clTok.hasMoreTokens()) {
            String classString = clTok.nextToken();
            CDOMReference<CL> ref;
            if (classString.startsWith("SPELLCASTER.")) {
                ref = context.ref.getCDOMTypeReference(tagType, classString.substring(12));
            } else {
                ref = context.ref.getCDOMReference(tagType, classString);
            }
            slList.add(ref);
        }
        if (hasIllegalSeparator(',', spellString)) {
            return false;
        }
        StringTokenizer spTok = new StringTokenizer(spellString, ",");
        while (spTok.hasMoreTokens()) {
            String spellName = spTok.nextToken();
            CDOMReference<Spell> sp = context.ref.getCDOMReference(Spell.class, spellName);
            for (CDOMReference<? extends CDOMList<Spell>> sl : slList) {
                AssociatedPrereqObject tpr = context.getListContext().addToList(getTokenName(), obj, sl, sp);
                tpr.setAssociation(AssociationKey.SPELL_LEVEL, splLevel);
                tpr.setAssociation(AssociationKey.KNOWN, Boolean.TRUE);
                tpr.addAllPrerequisites(prereqs);
            }
        }
        return true;
    }

    public String[] unparse(LoadContext context, CDOMObject obj) {
        Set<String> set = new TreeSet<String>();
        Collection<CDOMReference<? extends CDOMList<? extends PrereqObject>>> changedClassLists = context.getListContext().getChangedLists(obj, ClassSpellList.class);
        TripleKeyMapToList<String, Integer, CDOMReference<? extends CDOMList<? extends PrereqObject>>, CDOMReference<Spell>> classMap = getMap(context, obj, changedClassLists, true);
        for (String prereqs : classMap.getKeySet()) {
            set.add(processUnparse("CLASS", classMap, prereqs).toString());
        }
        if (set.isEmpty()) {
            return null;
        }
        return set.toArray(new String[set.size()]);
    }

    public Class<CDOMObject> getTokenClass() {
        return CDOMObject.class;
    }
}
