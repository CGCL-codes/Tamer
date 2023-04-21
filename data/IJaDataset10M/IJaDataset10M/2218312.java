package net.minecraft.src;

class J_JsonTrueNodeBuilder implements J_JsonNodeBuilder {

    J_JsonTrueNodeBuilder() {
    }

    public J_JsonNode buildNode() {
        return J_JsonNodeFactories.aJsonTrue();
    }
}
