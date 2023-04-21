package com.evaserver.rof.script;

/**
 * @author Max Antoni
 * @version $Revision: 163 $
 */
public class ObjectTest extends _TesterBase {

    public void testProperty() {
        eval("var o = new Object(); o.foo = 'bar';");
        jsAssert("bar", "o.foo");
        eval("o.hallo='welt';");
        jsAssert("welt", "o.hallo");
        eval("delete o.foo;");
        jsAssert(null, "o.foo");
        jsAssert("welt", "o.hallo");
        eval("o.hallo='bla';");
        jsAssert("bla", "o.hallo");
    }

    public void testMethodOverload1() {
        eval("Foo = function(){}; var a = new Foo();");
        jsAssert("[object Object]", "a.toString()");
        eval("Foo.prototype.toString = function(){ return '[object Foo]'; };" + "var b = new Foo();");
        jsAssert("[object Foo]", "b.toString()");
    }

    public void testMethodOverload2() {
        eval("Foo=function(){};Foo.prototype.test=function() {return 'Foo';}; var a=new Foo();");
        jsAssert("Foo", "a.test()");
        jsAssert("Foo", "Foo.prototype.test.call(a)");
        eval("Bar=function(){};Bar.prototype=new Foo();Bar.prototype.test=function(){return 'Bar';};" + "var b = new Bar();");
        jsAssert("Bar", "b.test()");
        jsAssert("Bar", "Bar.prototype.test.call(b)");
        eval("FooBar=function(){};FooBar.prototype=new Foo();FooBar.superclass=Foo.prototype;" + "FooBar.prototype.test=function(){return FooBar.superclass.test.call(this) + ' -> Bar';};" + "var c = new FooBar();");
        jsAssert("Foo -> Bar", "c.test()");
    }

    public void testPrototypeAccess() {
        eval("Foo=function(){}; Foo.prototype.toString = function(){ return \"[object Foo]\" }; var a = new Foo();");
        jsAssert("[object Object]", "Object.prototype.toString.call(a)");
        jsAssert("[object Foo]", "Foo.prototype.toString.call(a)");
    }

    public void testToStringWithArguments() {
        eval("function foo(){return Object.prototype.toString.apply(arguments);}");
        jsAssert("[object Arguments]", "foo()");
    }

    public void testToString() {
        jsAssert("[object Object]", "Object.prototype.toString.apply({})");
        jsAssert("[object Function]", "Object.prototype.toString.apply(function(){})");
        jsAssert("[object Date]", "Object.prototype.toString.apply(new Date())");
        jsAssert("[object Array]", "Object.prototype.toString.apply([])");
        jsAssert("[object Window]", "Object.prototype.toString.apply(null)");
        jsAssert("[object String]", "Object.prototype.toString.apply(\"some-string\")");
        jsAssert("[object Error]", "Object.prototype.toString.apply(new Error())");
        jsAssert("[object Number]", "Object.prototype.toString.apply(123)");
        jsAssert("[object Boolean]", "Object.prototype.toString.apply(true)");
    }

    public void testConstructor() {
        assertEquals(Boolean.TRUE, eval("({}).constructor === Object.prototype.constructor"));
    }

    public void testPrototype() {
        assertNotNull(eval("(new Object()).prototype"));
        assertNull(eval("(new Object()).prototype.prototype"));
    }
}
