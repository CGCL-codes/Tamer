package plugin.communicationdata.taggedtypes;

import java.util.Stack;
import plugin.communicationdata.tags.Tag;

public abstract class TaggedData<T> {

    private T data;

    private Stack<Tag> tags;

    public TaggedData() {
    }

    protected TaggedData(T data, Stack<Tag> tags) {
        this.data = data;
        this.tags = tags;
    }

    public Stack<Tag> getTags() {
        return this.tags;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setTags(Stack<Tag> tags) {
        this.tags = tags;
    }
}
