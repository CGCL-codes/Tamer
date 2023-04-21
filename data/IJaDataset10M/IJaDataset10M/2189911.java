package org.hl7.rim;

import org.hl7.types.CE;

/**<p>A subtype of Entity that is inanimate and locationally independent. </p>
<p><i>Examples:</i> Pharmaceutical substances (including active vaccines containing retarded virus), disposable supplies, durable equipment,
   implantable devices, food items (including meat or plant products), waste, traded goods, etc.
</p>
<p><i>Discussion:</i> Manufactured or processed products are considered material, even if they originate as living matter. Materials come in a
   wide variety of physical forms and can pass through different states (ie. Gas, liquid, solid) while still retaining their
   physical composition and material characteristics.
</p>
<p><i>Rationale:</i> There are entities that have attributes in addition to the Entity class, yet cannot be classified as either LivingSubject
   or Place.
</p>
*/
public interface Material extends Entity {

    /**<p>A value representing the state (solid, liquid, gas) and nature of the material. </p>
<p><i>Examples:</i> For therapeutic substances, the dose form, such as tablet, ointment, gel, etc.
</p>
  */
    CE getFormCode();

    /** Sets the property formCode.
      @see #getFormCode
  */
    void setFormCode(CE formCode);
}
