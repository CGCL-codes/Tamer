package com.aionemu.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import com.aionemu.gameserver.skillengine.model.Effect;

/**
 * @author ATracer
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BoostHealEffect")
public class BoostHealEffect extends BufEffect {

    @Override
    public void calculate(Effect effect) {
        effect.addSucessEffect(this);
    }
}
