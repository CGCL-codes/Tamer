package de.internnetz.pluto.lineuptool.types.tactics;

import static de.internnetz.pluto.playertool.enumerations.PositionEnum.CD;
import static de.internnetz.pluto.playertool.enumerations.PositionEnum.CM;
import static de.internnetz.pluto.playertool.enumerations.PositionEnum.SM;
import static de.internnetz.pluto.playertool.enumerations.PositionEnum.ST;
import static de.internnetz.pluto.playertool.enumerations.PositionEnum.WB;
import de.internnetz.pluto.lineuptool.types.TacticPositionHolder;
import de.internnetz.pluto.playertool.enumerations.PositionEnum;

public class Tactic442Normal extends TacticPositionHolder {

    public Tactic442Normal() {
        super("4-4-2 Normal");
        this.setPosOffensive(new PositionEnum[] { ST, ST });
        this.setPosMidfield(new PositionEnum[] { SM, CM, CM, SM });
        this.setPosDefensive(new PositionEnum[] { WB, CD, CD, WB });
    }
}
