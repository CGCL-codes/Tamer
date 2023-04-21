package com.magicpwd.__a.maoc;

import com.magicpwd.__a.AAction;
import com.magicpwd.__i.maoc.IMaocAction;
import com.magicpwd.v.app.maoc.MaocPtn;

/**
 *
 * @author Amon
 */
public abstract class AMaocAction extends AAction implements IMaocAction {

    protected MaocPtn maocPtn;

    @Override
    public void setMaocPtn(MaocPtn maocPtn) {
        this.maocPtn = maocPtn;
    }
}
