package ui.swing.pipeline2;

import ui.swing.CompilerListener;
import ui.swing.CompilerModel;
import javax.swing.*;
import java.awt.*;

/**
 * Displays source code currently assembled into processor.
 * <p/>
 * At the current, binding is directly between view and CompilerModel.
 */
public class SourceArea extends JTextArea implements CompilerListener {

    protected final CompilerModel compilerModel = CompilerModel.getModel();

    public SourceArea(String content) {
        super(content);
        this.setTabSize(4);
        Font font = Font.getFont(Font.MONOSPACED);
        this.setFont(font);
        compilerModel.addCompilerListener(this);
    }

    @Override
    public void newSource(String newSource) {
        this.setText(newSource);
    }
}
