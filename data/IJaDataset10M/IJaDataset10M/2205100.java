package org.grandtestauto.test;

import org.grandtestauto.*;

public class NameTest {

    public boolean camelCaseNameTest() {
        assert Name.camelCaseName("nohumps").toString().equals("nohumps");
        assert Name.camelCaseName("ALLHUMPS").toString().equals("ALLHUMPS");
        assert Name.camelCaseName("BactrianCamel").toString().equals("BactrianCamel");
        assert Name.camelCaseName("Dromedary").toString().equals("Dromedary");
        assert Name.camelCaseName("WhatAHumpyCamel").toString().equals("WhatAHumpyCamel");
        return true;
    }

    public boolean toStringTest() {
        assert Name.dotSeparatedName("").toString().equals("") : "Got: '" + Name.dotSeparatedName("") + "'";
        assert Name.dotSeparatedName("a").toString().equals("a") : "Got: '" + Name.dotSeparatedName("a") + "'";
        assert Name.dotSeparatedName("a.b").toString().equals("a.b") : "Got:' " + Name.dotSeparatedName("a.b") + "'";
        return true;
    }

    public boolean compareToTest() {
        assert n("").compareTo(n("")) == 0;
        assert n("a").compareTo(n("a")) == 0;
        assert n("a.b").compareTo(n("a.b")) == 0;
        assert n("a.bcde.fg").compareTo(n("a.bcde.fg")) == 0;
        assert n("").compareTo(n("a")) < 0;
        assert n("a").compareTo(n("a.b")) < 0;
        assert n("a.b").compareTo(n("a.bcde.fg")) < 0;
        assert n("a.bcde.fg").compareTo(n("a.bcde.fg.hi")) < 0;
        assert n("a").compareTo(n("")) > 0;
        assert n("a.b").compareTo(n("a")) > 0;
        assert n("a.bcde.fg").compareTo(n("a.bcde")) > 0;
        assert n("a.bcde.fg.hi").compareTo(n("a.bcde.fg")) > 0;
        assert n("a").compareTo(n("b")) < 0;
        assert n("abc").compareTo(n("abd")) < 0;
        assert n("a.bc.de").compareTo(n("a.bc.df")) < 0;
        assert n("a.bcdef.fg").compareTo(n("a.bcdeg.fg")) < 0;
        assert n("ab").compareTo(n("aa")) > 0;
        assert n("a.b").compareTo(n("a.a")) > 0;
        return true;
    }

    public boolean dotSeparatedNameTest() {
        Name nEmpty = Name.dotSeparatedName("");
        Name.dotSeparatedName("jfsf");
        Name.dotSeparatedName(".");
        Name.dotSeparatedName("a.b");
        return true;
    }

    public boolean matchesTest() {
        Name n1 = Name.dotSeparatedName("animal");
        Name n2 = Name.dotSeparatedName("");
        assert n2.matches(n1);
        assert !n1.matches(n2);
        n2 = Name.dotSeparatedName("a");
        assert n2.matches(n1);
        assert !n1.matches(n2);
        n2 = Name.dotSeparatedName("an");
        assert n2.matches(n1);
        assert !n1.matches(n2);
        n2 = Name.dotSeparatedName("ani");
        assert n2.matches(n1);
        assert !n1.matches(n2);
        n2 = Name.dotSeparatedName("animal");
        assert n2.matches(n1);
        assert n1.matches(n2);
        n2 = Name.dotSeparatedName("animals");
        assert !n2.matches(n1);
        assert n1.matches(n2);
        n2 = Name.dotSeparatedName("animus");
        assert !n2.matches(n1);
        assert !n1.matches(n2);
        n1 = Name.dotSeparatedName("animal.mammal.ungulate");
        n2 = Name.dotSeparatedName("");
        assert n2.matches(n1);
        assert !n1.matches(n2);
        n2 = Name.dotSeparatedName("a");
        assert n2.matches(n1);
        assert !n1.matches(n2);
        n2 = Name.dotSeparatedName("a.m");
        assert n2.matches(n1);
        assert !n1.matches(n2);
        n2 = Name.dotSeparatedName("a.ma");
        assert n2.matches(n1);
        assert !n1.matches(n2);
        n2 = Name.dotSeparatedName("a.ma.u");
        assert n2.matches(n1);
        assert !n1.matches(n2);
        n2 = Name.dotSeparatedName("a.m.u");
        assert n2.matches(n1);
        assert !n1.matches(n2);
        n2 = Name.dotSeparatedName("a.m.u.t");
        assert !n2.matches(n1);
        assert !n1.matches(n2);
        return true;
    }

    private Name n(String s) {
        return Name.dotSeparatedName(s);
    }
}
