package fr.itris.glips.svgeditor.display.canvas.rulers;

import fr.itris.glips.svgeditor.*;
import fr.itris.glips.svgeditor.display.handle.*;
import fr.itris.glips.svgeditor.resources.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;

/**
 * the class used to display the rulers
 * @author ITRIS, Jordi SUC
 */
public class RulersModule extends ModuleAdapter {

    /**
	 * the id
	 */
    private String id = "Rulers";

    /**
	 * the labels
	 */
    private String rulersHiddenLabel = "", rulersShownLabel = "";

    /**
	 * the menu item used for the rulers
	 */
    private final JMenuItem displayRulers = new JMenuItem();

    /**
	 * the constructor of the class
	 * @param editor the editor
	 */
    public RulersModule(Editor editor) {
        ResourceBundle bundle = ResourcesManager.bundle;
        if (bundle != null) {
            try {
                rulersHiddenLabel = bundle.getString("RulersHidden");
                rulersShownLabel = bundle.getString("RulersShown");
            } catch (Exception ex) {
            }
        }
        final HandlesListener svgHandlesListener = new HandlesListener() {

            @Override
            public void handleChanged(SVGHandle currentHandle, Set<SVGHandle> handles) {
                if (currentHandle != null) {
                    displayRulers.setEnabled(true);
                } else {
                    displayRulers.setEnabled(false);
                }
            }
        };
        editor.getHandlesManager().addHandlesListener(svgHandlesListener);
        ImageIcon icon = ResourcesManager.getIcon(id, false);
        ImageIcon disabledIcon = ResourcesManager.getIcon(id, true);
        displayRulers.setText(rulersShownLabel);
        displayRulers.setIcon(icon);
        displayRulers.setDisabledIcon(disabledIcon);
        displayRulers.setEnabled(false);
        displayRulers.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                SVGHandle handle = Editor.getEditor().getHandlesManager().getCurrentHandle();
                if (handle != null) {
                    RulersParametersManager manager = Editor.getEditor().getHandlesManager().getRulersParametersHandler();
                    if (!manager.areRulersEnabled()) {
                        manager.setRulersEnabled(true);
                        displayRulers.setText(rulersShownLabel);
                    } else {
                        manager.setRulersEnabled(false);
                        displayRulers.setText(rulersHiddenLabel);
                    }
                }
            }
        });
    }

    @Override
    public HashMap<String, JMenuItem> getMenuItems() {
        HashMap<String, JMenuItem> menuItems = new HashMap<String, JMenuItem>();
        menuItems.put(id, displayRulers);
        return menuItems;
    }
}
