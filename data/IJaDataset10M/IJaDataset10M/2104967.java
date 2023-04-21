package pcgen.persistence.lst;

import java.util.Map;
import java.util.StringTokenizer;
import pcgen.core.Globals;
import pcgen.core.PObject;
import pcgen.core.Skill;
import pcgen.persistence.PersistenceLayerException;
import pcgen.persistence.SystemLoader;
import pcgen.util.Logging;

/**
 * @author  David Rice <david-pcgen@jcuz.com>
 * @version $Revision: 1.44 $
 */
public final class SkillLoader extends LstObjectFileLoader {

    /** Creates a new instance of SkillLoader */
    public SkillLoader() {
        super();
    }

    /**
	 * @see pcgen.persistence.lst.LstObjectFileLoader#parseLine(pcgen.core.PObject, java.lang.String, pcgen.persistence.lst.CampaignSourceEntry)
	 */
    public PObject parseLine(PObject target, String lstLine, CampaignSourceEntry source) throws PersistenceLayerException {
        Skill skill = (Skill) target;
        if (skill == null) {
            skill = new Skill();
            skill.setSourceCampaign(source.getCampaign());
            skill.setSourceFile(source.getFile());
        }
        final StringTokenizer colToken = new StringTokenizer(lstLine, SystemLoader.TAB_DELIM);
        skill.setName(colToken.nextToken());
        Map tokenMap = TokenStore.inst().getTokenMap(SkillLstToken.class);
        while (colToken.hasMoreTokens()) {
            final String colString = colToken.nextToken().trim();
            final int idxColon = colString.indexOf(':');
            String key = "";
            try {
                key = colString.substring(0, idxColon);
            } catch (Exception e) {
            }
            SkillLstToken token = (SkillLstToken) tokenMap.get(key);
            if ("REQ".equals(colString)) {
                skill.setRequired(true);
            } else if (token != null) {
                final String value = colString.substring(idxColon + 1).trim();
                LstUtils.deprecationCheck(token, skill, value);
                if (!token.parse(skill, value)) {
                    Logging.errorPrint("Error parsing skill " + skill.getName() + ':' + source.getFile() + ':' + colString + "\"");
                }
            } else if (PObjectLoader.parseTag(skill, colString)) {
                continue;
            } else {
                Logging.errorPrint("Illegal skill info '" + lstLine + "' in " + source.toString());
            }
        }
        finishObject(skill);
        return null;
    }

    /**
	 * @see pcgen.persistence.lst.LstObjectFileLoader#getObjectNamed(java.lang.String)
	 */
    protected PObject getObjectNamed(String baseName) {
        return Globals.getSkillNamed(baseName);
    }

    /**
	 * @see pcgen.persistence.lst.LstObjectFileLoader#finishObject(pcgen.core.PObject)
	 */
    protected void finishObject(PObject target) {
        if (includeObject(target)) {
            Skill skill = (Skill) target;
            final Skill aSkill = Globals.getSkillKeyed(skill.getKeyName());
            if (aSkill == null) {
                Globals.getSkillList().add(skill);
            }
        }
    }

    /**
	 * @see pcgen.persistence.lst.LstObjectFileLoader#performForget(pcgen.core.PObject)
	 */
    protected void performForget(PObject objToForget) {
        Globals.getSkillList().remove(objToForget);
    }
}
