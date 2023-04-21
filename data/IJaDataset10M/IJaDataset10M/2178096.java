package it.page;

import org.t2framework.t2.annotation.core.ActionPath;
import org.t2framework.t2.annotation.core.Default;
import org.t2framework.t2.annotation.core.Page;
import org.t2framework.t2.annotation.core.Var;
import org.t2framework.t2.contexts.Request;
import org.t2framework.t2.navigation.SimpleText;
import org.t2framework.t2.spi.Navigation;

@Page("/issue218/{hoge}")
public class Issue218 {

    @Default
    public Navigation index(@Var("hoge") String hoge, final Request request) {
        return SimpleText.out("hoge=" + hoge);
    }

    @ActionPath("/{fuga}")
    public Navigation fuga(@Var("hoge") String hoge, @Var("fuga") String fuga, final Request request) {
        return SimpleText.out("hoge=" + hoge + ", fuga=" + fuga);
    }
}
