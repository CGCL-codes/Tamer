package org.apache.webbeans.test.component.intercept.webbeans;

import javax.webbeans.Production;
import javax.webbeans.RequestScoped;
import org.apache.webbeans.test.component.intercept.webbeans.bindings.Action;

@Production
@Action
@RequestScoped
public class WMetaInterceptorComponent {

    public static int s = 0;

    public static int sWithMeta = 0;

    public int hello() {
        return s;
    }

    public int hello2() {
        return sWithMeta;
    }
}
