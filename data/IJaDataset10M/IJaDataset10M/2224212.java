package base.module.intrface;

import base.CGraphics;
import base.module.CModule;

public class CSetBaseImage extends CModule {

    @Override
    public void Init() {
        AssertParam(1);
    }

    @Override
    public void Execute() {
        CGraphics.SetBaseImage(Param[0].ToString());
    }
}
