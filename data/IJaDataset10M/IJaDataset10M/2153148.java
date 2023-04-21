package de.huxhorn.sulky.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.*;
import java.util.Enumeration;
import java.util.Set;
import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

public class SwingLogging {

    public static void logInputMaps(JComponent component) {
        final Logger logger = LoggerFactory.getLogger(SwingLogging.class);
        if (logger.isDebugEnabled()) {
            final int[] conditions = { JComponent.WHEN_IN_FOCUSED_WINDOW, JComponent.WHEN_FOCUSED, JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT };
            final String[] conditionStrings = { "WHEN_IN_FOCUSED_WINDOW", "WHEN_FOCUSED", "WHEN_ANCESTOR_OF_FOCUSED_COMPONENT" };
            StringBuilder msg = new StringBuilder();
            for (int i = 0; i < conditions.length; i++) {
                InputMap inputMap = component.getInputMap(conditions[i]);
                msg.append("InputMap for '").append(conditionStrings[i]).append("':\n");
                for (; ; ) {
                    KeyStroke[] keyStrokes = inputMap.keys();
                    if (keyStrokes != null) {
                        for (KeyStroke ks : keyStrokes) {
                            msg.append("\tKeyStroke: ").append(ks).append("\n");
                            msg.append("\tActionMapKey: ").append(inputMap.get(ks)).append("\n\n");
                        }
                    }
                    msg.append("######################################\n");
                    inputMap = inputMap.getParent();
                    if (inputMap == null) {
                        msg.append("No parent.\n\n");
                        break;
                    } else {
                        msg.append("Parent:\n");
                    }
                }
            }
            ActionMap actionMap = component.getActionMap();
            msg.append("ActionMap:\n");
            for (; ; ) {
                Object[] keys = actionMap.keys();
                if (keys != null) {
                    for (Object key : keys) {
                        msg.append("\tKey: ").append(key).append("\n");
                        msg.append("\tAction: ").append(actionMap.get(key)).append("\n\n");
                    }
                }
                msg.append("######################################\n");
                actionMap = actionMap.getParent();
                if (actionMap == null) {
                    msg.append("No parent.\n\n");
                    break;
                } else {
                    msg.append("Parent:\n");
                }
            }
            if (logger.isDebugEnabled()) logger.debug(msg.toString());
        }
    }

    public static void logStyles(HTMLEditorKit htmlEditorKit) {
        final Logger logger = LoggerFactory.getLogger(SwingLogging.class);
        if (logger.isDebugEnabled()) {
            StringBuilder msg = new StringBuilder();
            msg.append("Primary:\n");
            StyleSheet styleSheet = htmlEditorKit.getStyleSheet();
            appendStyles(styleSheet, msg);
            logger.debug(msg.toString());
        }
    }

    private static void appendStyles(StyleSheet styleSheet, StringBuilder msg) {
        Enumeration<?> styleNames = styleSheet.getStyleNames();
        while (styleNames.hasMoreElements()) {
            Object styleName = styleNames.nextElement();
            Style style = styleSheet.getStyle("" + styleName);
            msg.append("StyleName: ").append(styleName).append("\nStyle: ").append(style).append("\n\n");
        }
        StyleSheet[] styleSheets = styleSheet.getStyleSheets();
        if (styleSheets != null && styleSheets.length > 0) {
            for (int i = 0; i < styleSheets.length; i++) {
                msg.append("Child #").append(i).append(":");
                StyleSheet ss = styleSheets[i];
                appendStyles(ss, msg);
            }
        }
    }

    public static void logTraversal(JComponent component) {
        final Logger logger = LoggerFactory.getLogger(SwingLogging.class);
        final int[] ids = { KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, KeyboardFocusManager.UP_CYCLE_TRAVERSAL_KEYS, KeyboardFocusManager.DOWN_CYCLE_TRAVERSAL_KEYS };
        final String[] names = { "FORWARD_TRAVERSAL_KEYS", "BACKWARD_TRAVERSAL_KEYS", "UP_CYCLE_TRAVERSAL_KEYS", "DOWN_CYCLE_TRAVERSAL_KEYS" };
        if (logger.isDebugEnabled()) {
            for (int i = 0; i < ids.length; i++) {
                Set<AWTKeyStroke> keys = component.getFocusTraversalKeys(ids[i]);
                logger.debug("ID: {}\nKeys:{}", names[i], keys);
            }
        }
    }
}
