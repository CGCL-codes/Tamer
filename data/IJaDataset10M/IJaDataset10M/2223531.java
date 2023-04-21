package net.bpfurtado.tas.builder;

import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.text.JTextComponent;

public class BuilderSwingUtils {

    public static ScrollTextArea createTextAreaWidgets(IBuilder builder, Toolkit toolkit) {
        ScrollTextArea sta = TextAreaWidgetFactory.create(Builder.FONT, toolkit);
        BuilderSwingUtils.addTextEventHandlers(sta.textArea, builder);
        return sta;
    }

    public static void addTextEventHandlers(JTextComponent textComponent, final IBuilder builder) {
        textComponent.addKeyListener(new KeyAdapter() {

            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() != 19) {
                    builder.markAsDirty();
                }
            }
        });
        textComponent.addFocusListener(new FocusAdapter() {

            @Override
            public void focusLost(FocusEvent e) {
                builder.saveActualScene();
            }
        });
    }
}
