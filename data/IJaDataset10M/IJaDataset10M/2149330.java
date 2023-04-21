package net.sourceforge.java_stratego.stratego.player.editor;

public class EditorDriver {

    public static void main(String[] args) {
        if (args.length > 0) new WEditor(args[0]); else new WEditor("");
    }
}
