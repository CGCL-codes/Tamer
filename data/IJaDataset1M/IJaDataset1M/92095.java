package org.parallelj.jmx.client;

import org.parallelj.mirror.Element;
import com.google.gson.JsonObject;

/**
 * Represents an implementation of an element
 * 
 * @author Laurent Legrand
 * 
 */
abstract class ElementProxy implements Element {

    /**
	 * The content of the element
	 */
    private JsonObject content;

    ClientReflection reflection;

    ElementProxy() {
    }

    public String getId() {
        return this.content.get("Id").getAsString();
    }

    JsonObject getContent() {
        return this.content;
    }

    void setContent(JsonObject content) {
        this.content = content;
    }

    ClientReflection getReflection() {
        return reflection;
    }

    void setReflection(ClientReflection reflection) {
        this.reflection = reflection;
    }
}
