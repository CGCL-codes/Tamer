package org.peaseplate.demo.scriptengine;

import org.peaseplate.scriptengine.ScriptEngineSingleton;
import org.peaseplate.utils.exception.CompileException;
import org.peaseplate.utils.exception.ExecuteException;

public class GameOfLifeScript {

    public static void main(final String[] args) throws ExecuteException, CompileException {
        ScriptEngineSingleton.SCRIPT_ENGINE.load("org/peaseplate/demo/scriptengine/GameOfLifeScript.script").execute();
    }
}
