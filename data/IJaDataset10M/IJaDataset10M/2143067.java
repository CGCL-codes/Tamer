package com.fdaoud.rayures.action.admin;

import com.fdaoud.rayures.action.*;
import net.sourceforge.stripes.action.Resolution;

/**
 * @author Frederic Daoud
 */
public class AnotherActionBean extends TestActionBean {

    public Resolution view() {
        return new ForwardToDefaultView(this);
    }
}
