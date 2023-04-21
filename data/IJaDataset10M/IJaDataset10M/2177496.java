package nakayo.gameserver.model.templates;

import nakayo.gameserver.model.templates.gather.Materials;
import javax.xml.bind.annotation.*;

/**
 * @author ATracer
 */
@XmlRootElement(name = "gatherable_template")
@XmlAccessorType(XmlAccessType.FIELD)
public class GatherableTemplate extends VisibleObjectTemplate {

    @XmlElement(required = true)
    protected Materials materials;

    @XmlAttribute
    protected int aerialAdj = 100;

    @XmlAttribute
    protected int failureAdj = 100;

    @XmlAttribute
    protected int successAdj = 100;

    @XmlAttribute
    protected int harvestSkill = 30002;

    @XmlAttribute
    protected int skillLevel;

    @XmlAttribute
    protected int harvestCount = 3;

    @XmlAttribute
    protected String sourceType;

    @XmlAttribute
    protected int nameId;

    @XmlAttribute
    protected String name;

    @XmlAttribute
    protected String desc;

    @XmlAttribute
    protected int id;

    /**
     * Gets the value of the materials property.
     *
     * @return possible object is
     *         {@link Materials }
     */
    public Materials getMaterials() {
        return materials;
    }

    /**
     * Gets the value of the id property.
     */
    @Override
    public int getTemplateId() {
        return id;
    }

    /**
     * Gets the value of the aerialAdj property.
     *
     * @return possible object is
     *         {@link Integer }
     */
    public int getAerialAdj() {
        return aerialAdj;
    }

    /**
     * Gets the value of the failureAdj property.
     *
     * @return possible object is
     *         {@link Integer }
     */
    public int getFailureAdj() {
        return failureAdj;
    }

    /**
     * Gets the value of the successAdj property.
     *
     * @return possible object is
     *         {@link Integer }
     */
    public int getSuccessAdj() {
        return successAdj;
    }

    /**
     * Gets the value of the harvestSkill property.
     *
     * @return possible object is
     *         {@link Integer }
     */
    public int getHarvestSkill() {
        return harvestSkill;
    }

    /**
     * Gets the value of the skillLevel property.
     *
     * @return possible object is
     *         {@link Integer }
     */
    public int getSkillLevel() {
        return skillLevel;
    }

    /**
     * Gets the value of the harvestCount property.
     *
     * @return possible object is
     *         {@link Integer }
     */
    public int getHarvestCount() {
        return harvestCount;
    }

    /**
     * Gets the value of the sourceType property.
     *
     * @return possible object is
     *         {@link String }
     */
    public String getSourceType() {
        return sourceType;
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     *         {@link String }
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * @return the nameId
     */
    @Override
    public int getNameId() {
        return nameId;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }
}
