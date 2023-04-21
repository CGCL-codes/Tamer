package cn.webwheel.tutorials.helloworld;

import cn.webwheel.FilterGroup;
import cn.webwheel.Main;

public class WebMain extends Main {

    protected void init() {
        System.out.println("WebMain启动");
        registerResultType(String.class, new StringResultInterpreter());
        FilterGroup filterGroup = root.append(new CountFilter());
        filterGroup.bindAction("/HelloWorld", HelloWorldAction.class);
    }

    protected void destroy() {
        System.out.println("WebMain停止");
    }
}
