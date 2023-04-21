package com.googlecode.jumpnevolve.game;

import com.jdotsoft.jarloader.JarClassLoader;

/**
 * Lädt und startet die Anwendung aus dem bin-Verzeichnis oder einem Archiv.
 * 
 * @author Niklas Fiekas
 */
public class JumpnevolveLauncher {

    /**
	 * @param args
	 *            Kommandozeilenargumente werden an {@link Jumpnevolve}
	 *            weitergegeben.
	 */
    public static void main(String[] args) {
        try {
            new JarClassLoader().invokeMain("com.googlecode.jumpnevolve.game.Jumpnevolve", args);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
