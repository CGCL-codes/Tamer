package de.jfruit.factory.demo;

import java.util.Properties;
import de.jfruit.jmapper.JMapper;
import de.jfruit.jmapper.adapter.Adapter;
import de.jfruit.jmapper.adapter.ILinkAdapter;
import de.jfruit.jmapper.link.ILinkParser;
import de.jfruit.jmapper.link.Link;

public class AdapterDemo {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("smode", true);
        props.put("user", 2);
        MyFace face = JMapper.createFromMap(props, MyFace.class);
        System.out.println("SMODE: " + face.isSingleUser());
        face.setCounter(12);
        System.out.println("Counter: " + face.counterToString());
        System.out.println("User:    " + face.userToString());
    }
}

interface MyFace {

    @Link(key = "counter")
    int getCounter();

    @Link(key = "counter")
    void setCounter(int i);

    @Link(key = "counter", parser = IntToStringParser.class)
    String counterToString();

    @Link(key = "user", parser = IntToStringParser.class)
    String userToString();

    @Adapter(MyAdapter.class)
    boolean isSingleUser();
}

class IntToStringParser implements ILinkParser<String, Integer> {

    @Override
    public String defValue() {
        return "NULL";
    }

    @Override
    public String parse(Integer o) {
        return String.valueOf(o);
    }
}

class MyAdapter implements ILinkAdapter<String, Boolean, Object> {

    @Override
    public String key() {
        return "smode";
    }

    @Override
    public Boolean defValue() {
        return false;
    }

    @Override
    public Boolean parse(Object o) {
        if (o instanceof String) {
            return ((String) o).equalsIgnoreCase("false") ? false : true;
        }
        if (o instanceof Boolean) {
            return (Boolean) o;
        }
        return false;
    }
}
