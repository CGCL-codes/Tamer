package jp.stk.tools.apt.testpage;

import org.t2framework.t2.annotation.core.Default;
import org.t2framework.t2.annotation.core.Page;
import org.t2framework.t2.navigation.Forward;
import org.t2framework.t2.spi.Navigation;

@Page("/comment")
public class CommentPage {

    @Default
    public Navigation index() {
        return Forward.to("aaavvvva");
    }
}
