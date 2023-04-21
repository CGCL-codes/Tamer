package com.arjuna.blue.bluefrontend.faces;

import java.util.List;

public abstract class BlueConfigFileStore {

    public abstract String addObject(BlueObject blueObject, int objectType);

    public abstract String addTemplate(BlueObject blueObject, int objectType);

    public abstract String modifyObject(BlueObject blueObject, int objectType);

    public abstract String deleteObject(int objectId, int objectType);

    public abstract List<BlueObject> searchByObjectName(String objectName, int objectType);

    public abstract int getObjectCount(int objectType);

    public abstract List<String> getObjectNames(int objectType);

    public abstract BlueObject loadObjectById(int objectId, int objectType);

    public abstract boolean checkObjectExists(int objectId, int objectType);

    public abstract boolean checkObjectNameExists(String objectName, int objectType);

    public abstract List<BlueObject> getStoredObjects(int objectType);

    public abstract int getTemplateCount(int objectType);

    public abstract List<BlueObject> getStoredTemplates(int objectType);

    public abstract String storeMainBlueConfig(BlueConfig blueConfig);

    public abstract boolean loadMainBlueConfig(BlueConfig blueConfig);
}
