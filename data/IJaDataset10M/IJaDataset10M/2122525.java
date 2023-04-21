package com.oldportal.objectsbuilder.document;

/**
 * Serialization class data to XML document node.
 */
public interface XMLSerializable {

    /**
   * Load class data (and parent classes data) from XML node (element).
   * Загружает данные класса и его предков из XML элемента.
   */
    public void load(org.jdom.Element element);

    /**
   * Save class data (and parent classes data) to XML node (element).
   * Сохраняет данные класса и его предков в XML элемент, который можно встроить в какой-либо документ.
   */
    public org.jdom.Element save();

    /**
   * Restore references to shared classe via search class by class GUID.
   * Восстанавливает ссылки на разделяемые классы через поиск по их GUID.
   */
    public void resolveDependencies();
}
