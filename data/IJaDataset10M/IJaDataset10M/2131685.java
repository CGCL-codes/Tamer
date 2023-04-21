package org.spockframework.runtime.model;

import java.util.List;

/**
 * Runtime information about a block in a method of a Spock specification.
 *
 * @author Peter Niederwieser
 */
public class BlockInfo {

    private BlockKind kind;

    private List<String> texts;

    public BlockKind getKind() {
        return kind;
    }

    public void setKind(BlockKind kind) {
        this.kind = kind;
    }

    public List<String> getTexts() {
        return texts;
    }

    public void setTexts(List<String> texts) {
        this.texts = texts;
    }
}
