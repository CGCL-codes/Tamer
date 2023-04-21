package logic;

import java.util.HashMap;
import domain.StackHolder;
import domain.StorageNode;

public class PreferenceFactory extends ItemFactory implements Observer {

    public PreferenceFactory(Property property) {
        super(property);
        m_ItemHandler = new PreferenceHandler();
        m_ItemHandler.addObserver(this);
    }

    public void update() {
    }

    public void update(KeyAction keyAction, HashMap<String, Object> keyValuePairs) {
        StorageNode storageServer;
        StackHolder recipient, sender;
        storageServer = new StorageNode((String) keyValuePairs.get("IP"), (String) keyValuePairs.get("OS"), (String) keyValuePairs.get("BackupsMountPoint"), (String) keyValuePairs.get("ClientsMountPoint"));
        sender = (StackHolder) keyValuePairs.get(PreferenceEnum.Sender.name());
        recipient = (StackHolder) keyValuePairs.get(PreferenceEnum.Recipient.name());
        m_Objects.put(PreferenceEnum.StorageNode.name(), storageServer);
        m_Objects.put(PreferenceEnum.Recipient.name(), recipient);
        m_Objects.put(PreferenceEnum.Sender.name(), sender);
        m_Objects.put(PreferenceEnum.BackUpNode.name(), keyValuePairs.get(PreferenceEnum.BackUpNode.name()));
    }
}
