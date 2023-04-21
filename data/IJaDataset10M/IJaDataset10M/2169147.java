package com.tapina.robe.swi.clib.stdlib;

import com.tapina.robe.runtime.Environment;
import com.tapina.robe.swi.clib.Stub;

/**
 * Created by IntelliJ IDEA.
 * User: gareth
 * Date: 21-May-2005
 * Time: 18:52:06
 * To change this template use File | Settings | File Templates.
 */
public class SRand extends Stub {

    private RandomState state;

    public SRand(RandomState state) {
        this.state = state;
    }

    public void executeStub(Environment environment) {
        state.getRandom().setSeed(environment.getCpu().R[0]);
    }
}
