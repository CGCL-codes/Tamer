package net.sf.ofx4j.domain.data.profile.info.banking;

import net.sf.ofx4j.meta.Aggregate;
import net.sf.ofx4j.meta.Element;

/**
 * Email Profile
 * @author Scott Priddy
 * @see "Section 11.13.2.3 OFX Spec"
 */
@Aggregate("EMAILPROF")
public class EmailProfile {

    private Boolean canEmail;

    private Boolean canNotify;

    @Element(name = "CANEMAIL", required = true, order = 10)
    public Boolean getCanEmail() {
        return canEmail;
    }

    public void setCanEmail(Boolean canEmail) {
        this.canEmail = canEmail;
    }

    @Element(name = "CANNOTIFY", required = true, order = 20)
    public Boolean getCanNotify() {
        return canNotify;
    }

    public void setCanNotify(Boolean canNotify) {
        this.canNotify = canNotify;
    }
}
