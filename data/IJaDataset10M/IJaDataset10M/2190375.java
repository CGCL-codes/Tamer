package ru.yandex.strictweb.example.sampleajax;

import java.util.Date;
import java.util.Map;
import ru.yandex.strictweb.ajaxtools.annotation.AjaxServiceHelper;
import ru.yandex.strictweb.ajaxtools.annotation.Arguments;

@AjaxServiceHelper(description = "")
public class SampleHelperBean {

    @Arguments
    public Date getServerDate() {
        return new Date();
    }

    @Arguments
    public String getHelloWorldString() {
        return "Hello, World!";
    }

    @Arguments
    public Map<Object, Object> getSystemProperties() {
        return System.getProperties();
    }

    @Arguments
    public SomeModel getObject(Long id) {
        SomeModel o = new SomeModel();
        o.id = System.currentTimeMillis();
        o.name = "Hoota";
        o.email = "hoota@yandex-team.ru";
        return o;
    }

    @Arguments
    public String saveObject(SomeModel o) {
        if (o.name == null || o.name.isEmpty()) throw new RuntimeException("Bad Object: empty name");
        return o.id + " :: " + o.name + " :: " + o.email;
    }
}
