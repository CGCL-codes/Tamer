package net.sf.rmoffice.meta.internal;

import net.sf.rmoffice.meta.SkillCategory;
import net.sf.rmoffice.meta.enums.SpellUserType;
import net.sf.rmoffice.meta.enums.SpelllistPart;

/**
 * Compound object used as key in a map for spelllist costs.
 */
public class SkillcategorySpelllistPartKey {

    private final SpelllistPart part;

    private final SkillCategory category;

    private final SpellUserType type;

    /**
	 * @param category the skill category
	 * @param part the spell list part
	 * @param type the spell user type
	 * 
	 */
    public SkillcategorySpelllistPartKey(SkillCategory category, SpelllistPart part, SpellUserType type) {
        this.category = category;
        this.part = part;
        this.type = type;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + ((part == null) ? 0 : part.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SkillcategorySpelllistPartKey other = (SkillcategorySpelllistPartKey) obj;
        if (category == null) {
            if (other.category != null) return false;
        } else if (!category.equals(other.category)) return false;
        if (part != other.part) return false;
        if (type != other.type) return false;
        return true;
    }
}
