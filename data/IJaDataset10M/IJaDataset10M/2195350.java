package jmud.jgp;

import jgp.functor.UnaryFunction;
import jmud.util.StrUtil;
import jmud.UniqueId;

public class IdTitleFormatter implements UnaryFunction {

    private TitleExtractor te = new TitleExtractor();

    private int width;

    private char fill;

    public IdTitleFormatter(int idWidth, char pad) {
        width = idWidth;
        fill = pad;
    }

    public Object execute(Object obj) {
        return StrUtil.formatNumber(((UniqueId) obj).getId(), width, fill) + " " + te.execute(obj);
    }
}
