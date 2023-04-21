package nakayo.gameserver.dataholders;

import nakayo.gameserver.model.templates.stats.SummonStatsTemplate;
import gnu.trove.TIntObjectHashMap;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ATracer
 */
@XmlRootElement(name = "summon_stats_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class SummonStatsData {

    @XmlElement(name = "summon_stats", required = true)
    private List<SummonStatsType> summonTemplatesList = new ArrayList<SummonStatsType>();

    private final TIntObjectHashMap<SummonStatsTemplate> summonTemplates = new TIntObjectHashMap<SummonStatsTemplate>();

    void afterUnmarshal(Unmarshaller u, Object parent) {
        for (SummonStatsType st : summonTemplatesList) {
            int code1 = makeHash(st.getNpcIdDark(), st.getRequiredLevel());
            summonTemplates.put(code1, st.getTemplate());
            int code2 = makeHash(st.getNpcIdLight(), st.getRequiredLevel());
            summonTemplates.put(code2, st.getTemplate());
        }
    }

    /**
     * @param npcId
     * @param level
     * @return
     */
    public SummonStatsTemplate getSummonTemplate(int npcId, int level) {
        SummonStatsTemplate template = summonTemplates.get(makeHash(npcId, level));
        if (template == null) template = summonTemplates.get(makeHash(201022, 10));
        return template;
    }

    /**
     * Size of summon templates
     *
     * @return
     */
    public int size() {
        return summonTemplates.size();
    }

    @XmlRootElement(name = "summonStatsTemplateType")
    private static class SummonStatsType {

        @XmlAttribute(name = "npc_id_dark", required = true)
        private int npcIdDark;

        @XmlAttribute(name = "npc_id_light", required = true)
        private int npcIdLight;

        @XmlAttribute(name = "level", required = true)
        private int requiredLevel;

        @XmlElement(name = "stats_template")
        private SummonStatsTemplate template;

        /**
         * @return the npcIdDark
         */
        public int getNpcIdDark() {
            return npcIdDark;
        }

        /**
         * @return the npcIdLight
         */
        public int getNpcIdLight() {
            return npcIdLight;
        }

        /**
         * @return requiredLevel
         */
        public int getRequiredLevel() {
            return requiredLevel;
        }

        /**
         * @return template
         */
        public SummonStatsTemplate getTemplate() {
            return template;
        }
    }

    /**
     * Note:<br>
     * max level is 255
     *
     * @param npcId
     * @param level
     * @return
     */
    private static int makeHash(int npcId, int level) {
        return npcId << 8 | level;
    }
}
