package com.jaspec;

import static com.jaspec.Jaspec.*;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TestJaspec {

    @Test
    public void types() {
        $(2).should().be_instanceof(Integer.class);
        $(2).should_not().be_instanceof(String.class);
    }

    @Test
    public void equality() {
        $(2).should().be(2);
        $(2).should_not().be(3);
    }

    @Test
    public void booleans() {
        $(false).should().be_false();
        $(true).should().be_true();
        $(true).should_not().be_false();
        $(false).should_not().be_true();
    }

    @Test
    public void nulls() {
        Object nill = null;
        $(nill).should().be_null();
        $(1).should_not().be_null();
    }

    @Test
    public void within() {
        $(2).should().be_within(1, 3);
        $(2).should_not().be_within(10, 12);
    }

    @Test
    public void expect() {
        $expect(new Code() {

            @Override
            public void exec() throws Exception {
                throw new RuntimeException();
            }
        }).to_throw_exception();
        $expect(new Code() {

            @Override
            public void exec() throws Exception {
                throw new RuntimeException();
            }
        }).to_throw(RuntimeException.class);
    }

    @Test
    public void collections() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4);
        $(list).should().be(Arrays.asList(1, 2, 3, 4));
        $(list).should().have(4).items();
        $(list).should().have_at_least(3).items();
        $(list).should().have_at_most(7).items();
        $(list).should_not().have(3).items();
        $(list).should_not().have_at_least(5).items();
        $(list).should_not().have_at_most(2).items();
        $(list).should().include(2, 3);
        $(list).should_not().include(5, 6, 10);
        $(list).should_not().be_empty();
        $(new ArrayList<Object>()).should().be_empty();
    }

    @Test
    public void maps() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("1", 1);
        map.put("2", 2);
        map.put("3", 3);
        map.put("4", 4);
        $(map).should().be(map);
        $(map).should().have(4).items();
        $(map).should().have_at_least(3).items();
        $(map).should().have_at_most(7).items();
        $(map).should_not().have(3).items();
        $(map).should_not().have_at_least(5).items();
        $(map).should_not().have_at_most(2).items();
        $(map).should().include_key("2", "3");
        $(map).should_not().include_key("5", "6", "10");
        $(map).should().include_value(2, 3);
        $(map).should_not().include_value(5, 6, 10);
        $(map).should_not().be_empty();
        $(new HashMap<Object, Object>()).should().be_empty();
    }

    @Test
    public void strings() {
        $("jaspec").should().be("jaspec");
        String s = "hello world, this's jaspec!";
        $(s).should().be("hello world, this's jaspec!");
        $(s).should().have(4).words();
        $(s).should().have_at_least(3).words();
        $(s).should().have_at_most(7).words();
        $(s).should_not().have(3).words();
        $(s).should_not().have_at_least(5).words();
        $(s).should_not().have_at_most(2).words();
        $(s).should().have_at_least(10).letters();
        $(s).should().include("ello", "this'");
        $(s).should_not().include("pla", "app", "lls");
        $(s).should().match("^hello.*", ".*jasp.*");
        $(s).should_not().match("pla", "app", "lls");
        $("jaspec").should().start_with("jas");
        $("jaspec").should().end_with("ec");
        $("jaspec").should_not().start_with("as");
        $("jaspec").should_not().end_with("e");
    }

    @Test
    public void numbers() {
        $(3).should().be(3);
        $(3).should().bigger_than(2);
        $(3).should().smaller_than(4);
        $(3).should().bigger_or_equal_than(2L);
        $(3).should().smaller_or_equal_than(4);
        $(1).should().be_1();
        $(2).should().be_2();
        $(4).should().be_multiple_of(2);
        $(4).should_not().be_multiple_of(3);
        $(6).should().be_pair();
        $(7).should_not().be_pair();
    }
}
