package org.iqual.chaplin.example.variableInPath;

import static org.iqual.chaplin.DynaCastUtils.$;

/**
 * Created by IntelliJ IDEA.
 * User: zslajchrt
 * Date: Jan 4, 2010
 * Time: 7:40:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class Composite {

    public static Processor create() {
        Processor proc = $(new LeftHandler(), new RightHandler(), new Core());
        return proc;
    }
}
