package org.openmobster.device.agent.frameworks.mobileObject;

/**
 * @author openmobster@gmail.com
 */
final class ArrayMetaData {

    private long id;

    private String arrayUri;

    private String arrayLength;

    private String arrayClass;

    ArrayMetaData() {
    }

    long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }

    String getArrayUri() {
        return arrayUri;
    }

    void setArrayUri(String arrayUri) {
        this.arrayUri = arrayUri;
    }

    String getArrayLength() {
        return arrayLength;
    }

    void setArrayLength(String arrayLength) {
        this.arrayLength = arrayLength;
    }

    String getArrayClass() {
        return arrayClass;
    }

    void setArrayClass(String arrayClass) {
        this.arrayClass = arrayClass;
    }
}
