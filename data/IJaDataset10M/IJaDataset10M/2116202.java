package net.tileeditor.keybindings;

import net.engine.file.xml.XMLTag;
import net.engine.file.xml.XMLBody;

public class ActionKeyBinding extends KeyBinding {

    protected String action;

    public ActionKeyBinding(String action, int keyCode, int modifiers) {
        super(keyCode, modifiers);
        this.action = action;
    }

    public ActionKeyBinding(ActionKeyBinding binding) {
        super(binding);
        action = binding.action;
    }

    public ActionKeyBinding(String action, String binding) {
        keyBindingFromString(binding);
        this.action = action;
    }

    public ActionKeyBinding(XMLTag tag) {
        XMLBody keyBindingBody = (XMLBody) tag.getData();
        String action = (String) keyBindingBody.getData("Action");
        String binding = (String) keyBindingBody.getData("Key");
        keyBindingFromString(binding);
        this.action = action;
    }

    public void save(XMLBody body) {
        XMLTag tag = body.put("KeyBinding");
        XMLBody keyBindingBody = tag.setData();
        keyBindingBody.put("Action", action);
        keyBindingBody.put("Key", asKeyString());
    }

    public String getAction() {
        return action;
    }
}
