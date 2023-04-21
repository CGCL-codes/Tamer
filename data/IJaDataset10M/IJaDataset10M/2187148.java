package base.module.intrface;

import base.CGraphics;
import base.module.CModule;

public class CSetTexture extends CModule {

    @Override
    public void Init() {
        AssertParam(1);
    }

    @Override
    public void Execute() {
        CGraphics.Texture = CGraphics.LoadImage(Param[0].ToString());
    }
}
