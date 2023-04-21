package org.fudaa.dodico.crue.metier.emh;

/** @pdOid 4da4c4ab-b0aa-4d5f-acad-e03e74ec0fda */
public class RelationEMHSectionDansBranche extends RelationEMH<CatEMHSection> {

    /** @pdOid 976a6b2d-60bb-4d47-a36e-732b33c7a21b */
    private double xp;

    /** @pdOid 0906b00a-6643-4707-9077-37a1887f7571 */
    private EnumPosSection pos;

    /**
   * @return the pos
   */
    public EnumPosSection getPos() {
        return pos;
    }

    /**
   * @param pos the pos to set
   */
    public void setPos(final EnumPosSection pos) {
        this.pos = pos;
    }

    /** @pdOid a1909c6f-b4b5-4153-932b-ee7d864dfe17 */
    public double getXp() {
        return xp;
    }

    /**
   * @param newXp
   * @pdOid b3334013-cda6-4aea-83b9-72aba5a21c8d
   */
    public void setXp(final double newXp) {
        xp = newXp;
    }
}
