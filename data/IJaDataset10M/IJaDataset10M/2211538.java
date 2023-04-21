package cn.icybear.orzjisp.orzvm;

import java.util.HashMap;
import cn.icybear.orzjisp.Identifier;
import cn.icybear.orzjisp.bytefile.CodeSection;

public class Closure {

    public HashMap<Identifier, Object> map = new HashMap<Identifier, Object>();

    public CodeSection codeSection;
}
