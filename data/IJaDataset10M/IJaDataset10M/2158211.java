package net.nothinginteresting.ylib.widgets;

import java.util.List;

public interface YContainer<T extends YWidget> {

    public List<T> getChildren();

    public <C extends YWidget> C findChild(String id, Class<C> childClass);
}
