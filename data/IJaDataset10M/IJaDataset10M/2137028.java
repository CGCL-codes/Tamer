package com.googlecode.jumpnevolve.editor;

import java.io.IOException;
import com.googlecode.jumpnevolve.graphics.AbstractEngine;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.util.Parameter;

public class EditorLauncher {

    /**
	 * Startet den neuen Editor.
	 * 
	 * @param args
	 *            Kommandozeilenargumente
	 * @throws IOException
	 */
    public static void main(String[] args) throws IOException {
        AbstractEngine engine = Engine.getInstance();
        engine.setTargetFrameRate(Parameter.GAME_FPS_TARGET);
        engine.switchState(new Editor(100, 100, 1));
        engine.start();
    }
}
