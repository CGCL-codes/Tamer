package de.flaxen.jdvdslideshow.ui;

import java.awt.Component;
import java.awt.Container;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * @author lars
 * 
 */
public class Translator {

    public static ResourceBundle languageBundle = ResourceBundle.getBundle("jDVDSlideshow_" + Locale.getDefault().getLanguage());

    public void translate(Component component) {
        if (component instanceof AbstractButton) {
            AbstractButton button = (AbstractButton) component;
            String text = button.getText();
            if (isTextSetted(text)) button.setText(translate(text));
        } else if (component instanceof JLabel) {
            JLabel label = (JLabel) component;
            String text = label.getText();
            if (isTextSetted(text)) label.setText(translate(text));
        }
        if (component instanceof JComponent) {
            JComponent jComponent = (JComponent) component;
            String text = jComponent.getToolTipText();
            if (isTextSetted(text)) jComponent.setToolTipText(translate(text));
            translate(jComponent.getComponentPopupMenu());
        }
        if (component instanceof JFrame) {
            JFrame frame = (JFrame) component;
            String text = frame.getTitle();
            if (isTextSetted(text)) frame.setTitle(translate(text));
        }
        if (component instanceof JPanel) {
            JPanel panel = (JPanel) component;
            Border border = panel.getBorder();
            if (border instanceof TitledBorder) {
                TitledBorder title = (TitledBorder) border;
                String text = title.getTitle();
                if (isTextSetted(text)) title.setTitle(translate(text));
            }
        }
        Component[] components = null;
        if (component instanceof JMenu) {
            JMenu menu = (JMenu) component;
            components = menu.getMenuComponents();
        } else if (component instanceof Container) {
            Container container = (Container) component;
            components = container.getComponents();
        }
        if (components != null) {
            for (Component nextComponent : components) translate(nextComponent);
        }
    }

    boolean isTextSetted(String text) {
        return text != null && text.length() > 0;
    }

    StringBuilder builder = new StringBuilder();

    public String translate(String text) {
        String[] splitted = text.split(" ");
        builder.setLength(0);
        for (String word : splitted) {
            String normWord = normalizeWord(word);
            String translatedWord;
            if (normWord != null) {
                translatedWord = translateWord(normWord);
                translatedWord = word.replaceFirst(normWord, translatedWord);
            } else translatedWord = word;
            builder.append(translatedWord);
            builder.append(" ");
        }
        if (builder.length() > 0) builder.setLength(builder.length() - 1);
        return builder.toString();
    }

    String normalizeWord(String text) {
        StringBuilder builder = null;
        for (char ch : text.toCharArray()) {
            if (!Character.isDigit(ch) && !Character.isWhitespace(ch) && (ch == '_' || ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z')) {
                if (builder == null) builder = new StringBuilder();
                builder.append(ch);
            } else if (builder != null) break;
        }
        if (builder != null) return builder.toString(); else return null;
    }

    String translateWord(String word) {
        String translated;
        if (isTextSetted(word)) if (languageBundle.containsKey(word)) translated = languageBundle.getString(word); else translated = word; else translated = "";
        return translated;
    }
}
