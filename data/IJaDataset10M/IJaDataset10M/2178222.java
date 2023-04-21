package com.ibm.wala.cast.js.test;

import java.io.File;
import java.io.IOException;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import com.ibm.wala.cast.js.ipa.callgraph.correlations.extraction.ClosureExtractor;
import com.ibm.wala.cast.js.ipa.callgraph.correlations.extraction.ForInBodyExtractionPolicy;
import com.ibm.wala.cast.tree.CAstEntity;
import com.ibm.wala.cast.tree.impl.CAstImpl;
import com.ibm.wala.classLoader.SourceFileModule;
import com.ibm.wala.classLoader.SourceModule;
import com.ibm.wala.util.io.FileUtil;

public abstract class TestForInBodyExtraction {

    public void testRewriter(String in, String out) {
        testRewriter(null, in, out);
    }

    public void testRewriter(String testName, String in, String out) {
        File tmp = null;
        try {
            tmp = File.createTempFile("test", ".js");
            FileUtil.writeFile(tmp, in);
            CAstImpl ast = new CAstImpl();
            String actual = new CAstDumper().dump(new ClosureExtractor(ast, ForInBodyExtractionPolicy.FACTORY).rewrite(parseJS(tmp, ast)));
            FileUtil.writeFile(tmp, out);
            String expected = new CAstDumper().dump(parseJS(tmp, ast));
            Assert.assertEquals(testName, expected, actual);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (tmp != null && tmp.exists()) tmp.delete();
        }
    }

    protected CAstEntity parseJS(File tmp, CAstImpl ast) throws IOException {
        String moduleName = tmp.getName();
        SourceFileModule module = new SourceFileModule(tmp, moduleName);
        return parseJS(ast, module);
    }

    protected abstract CAstEntity parseJS(CAstImpl ast, SourceModule module) throws IOException;

    @Test
    public void test1() {
        testRewriter("function extend(dest, src) {" + "  for(var p in src) {" + "    dest[p] = src[p];" + "  }" + "}", "function extend(dest, src) {" + "  for(var p in src) {" + "    (function _forin_body_0(p) {" + "      dest[p] = src[p];" + "     })(p);" + "  }" + "}");
    }

    @Test
    public void test2() {
        testRewriter("function extend(dest, src) {" + "  for(var p in src)" + "    dest[p] = src[p];" + "}", "function extend(dest, src) {" + "  for(var p in src)" + "    (function _forin_body_0(p) {" + "      dest[p] = src[p];" + "     })(p);" + "}");
    }

    @Test
    public void test3() {
        testRewriter("function extend(dest, src) {" + "  for(p in src)" + "    dest[p] = src[p];" + "}", "function extend(dest, src) {" + "  for(p in src)" + "    (function _forin_body_0(p) {" + "      dest[p] = src[p];" + "     })(p);" + "}");
    }

    @Test
    public void test4() {
        testRewriter("function extend(dest, src) {" + "  var p;" + "  for(p in src)" + "    dest[p] = src[p];" + "}", "function extend(dest, src) {" + "  var p;" + "  for(p in src)" + "    (function _forin_body_0(p) {" + "      dest[p] = src[p];" + "     })(p);" + "}");
    }

    @Test
    public void test5() {
        testRewriter("function extend(dest, src) {" + "  for(p in src) {" + "    var p;" + "    dest[p] = src[p];" + "  }" + "}", "function extend(dest, src) {" + "  for(p in src) {" + "    var p;" + "    (function _forin_body_0(p) {" + "      dest[p] = src[p];" + "     })(p);" + "  }" + "}");
    }

    @Test
    public void test6() {
        testRewriter("function extend(dest, src) {" + "  for(p in src) {" + "    dest[p] = src[p];" + "    var p;" + "  }" + "}", "function extend(dest, src) {" + "  for(p in src) {" + "    var p;" + "    (function _forin_body_0(p) {" + "      dest[p] = src[p];" + "     })(p);" + "  }" + "}");
    }

    @Test
    @Ignore
    public void test7() {
        testRewriter("function extend(dest, src) {" + "  for(var p in src) {" + "    dest[p] = src[p];" + "    p = true;" + "  }" + "  return p;" + "}", "function extend(dest, src) {" + "  for(var p in src)" + "    (function _let_0(_let_parm_0) {" + "      (function _forin_body_0(p) {" + "        try {" + "          dest[p] = src[p];" + "          p = true;" + "        } finally {" + "          _let_parm_0 = p;" + "        }" + "       })(p);" + "       p = _let_parm_0;" + "     })(p);" + "  return p;" + "}");
    }

    @Test
    public void test8() {
        testRewriter("Object.prototype.extend = function(src) {" + "  for(var p in src)" + "    this[p] = src[p];" + "}", "Object.prototype.extend = function(src) {" + "  for(var p in src)" + "    (function _forin_body_0(p, thi$) {" + "      thi$[p] = src[p];" + "    })(p, this);" + "}");
    }

    @Test
    public void test9() {
        testRewriter("function defglobals(globals) {" + "  for(var p in globals) {" + "    (function() {" + "      this[p] = globals[p];" + "    })();" + "  }" + "}", "function defglobals(globals) {" + "  for(var p in globals) {" + "    (function _forin_body_0(p) {" + "      (function() {" + "        this[p] = globals[p];" + "      })()" + "    })(p);" + "  }" + "}");
    }

    @Test
    public void test10() {
        testRewriter("function extend(dest, src) {" + "  for(var p in src) {" + "    if(p == \"stop\")" + "      break;" + "    dest[p] = src[p];" + "  }" + "}", "function extend(dest, src) {" + "  for(var p in src) {" + "    re$ = (function _forin_body_0(p) {" + "      if(p == \"stop\")" + "        return {type: 'goto', target: 0};" + "      dest[p] = src[p];" + "    })(p);" + "    if(re$) {" + "      if(re$.type == 'goto') {" + "        if(re$.target == 0)" + "          break;" + "      }" + "    }" + "  }" + "}");
    }

    @Test
    public void test11() {
        testRewriter("function extend(dest, src) {" + "  for(var p in src) {" + "    while(true) {" + "      dest[p] = src[p];" + "      break;" + "    }" + "  }" + "}", "function extend(dest, src) {" + "  for(var p in src) {" + "    (function _forin_body_0(p) {" + "      while(true) {" + "        dest[p] = src[p];" + "        break;" + "      }" + "    })(p);" + "  }" + "}");
    }

    @Test
    public void test12() {
        testRewriter("function extend(dest, src) {" + "  outer: for(var p in src) {" + "    while(true) {" + "      dest[p] = src[p];" + "      break outer;" + "    }" + "  }" + "}", "function extend(dest, src) {" + "  outer: for(var p in src) {" + "    re$ = (function _forin_body_0(p) {" + "      while(true) {" + "        dest[p] = src[p];" + "        return {type: 'goto', target: 0};" + "      }" + "    })(p);" + "    if(re$) {" + "      if(re$.type == 'goto') {" + "        if(re$.target == 0)" + "          break outer;" + "      }" + "    }" + "  }" + "}");
    }

    @Test
    public void test13() {
        testRewriter("function extend(dest, src) {" + "  for(var p in src) {" + "    if(p == '__proto__')" + "      throw new Exception('huh?');" + "    dest[p] = src[p];" + "  }" + "}", "function extend(dest, src) {" + "  for(var p in src) {" + "    (function _forin_body_0(p) {" + "      if(p == '__proto__')" + "        throw new Exception('huh?');" + "      dest[p] = src[p];" + "     })(p);" + "  }" + "}");
    }

    @Test
    @Ignore
    public void test14() {
        testRewriter("x = 23;" + "function foo() {" + "  x = 42;" + "  for(var p in {toString : 23}) {" + "    var x = 56;" + "    alert(x);" + "  }" + "  alert(x);" + "}" + "foo();" + "alert(x);", "x = 23;" + "function foo() {" + "  x = 42;" + "  for(var p in {toString : 23}) {" + "    var x;" + "    (function _forin_body_0(p) {" + "      x = 56;" + "      alert(x);" + "    })(p);" + "  }" + "  alert(x);" + "}" + "foo();" + "alert(x);");
    }

    @Test
    public void test15() {
        testRewriter("x = 23;" + "function foo() {" + "  x = 42;" + "  for(var p in {toString : 23}) {" + "    (function() {" + "      var x = 56;" + "      alert(x);" + "    })();" + "  }" + "  alert(x);" + "}" + "foo();" + "alert(x);", "x = 23;" + "function foo() {" + "  x = 42;" + "  for(var p in {toString : 23}) {" + "    (function _forin_body_0(p) {" + "      (function() {" + "        var x = 56;" + "        alert(x);" + "      })();" + "    })(p);" + "  }" + "  alert(x);" + "}" + "foo();" + "alert(x);");
    }

    @Test
    public void test16() {
        testRewriter("function extend(dest, src) {" + "  var o = { dest: dest };" + "  with(o) {" + "    for(var p in src) {" + "      dest[p] = src[p];" + "    }" + "  }" + "}", "function extend(dest, src) {" + "  var o = { dest: dest };" + "  with(o) {" + "    for(var p in src) {" + "      (function _forin_body_0(p) {" + "        dest[p] = src[p];" + "      })(p);" + "    }" + "  }" + "}");
    }

    @Test
    public void test17() {
        testRewriter("var o = {x:23};" + "for(x in o) {" + "  o[x] += 19;" + "}", "var o = {x:23};" + "for(x in o) {" + "  (function _forin_body_0(x) {" + "    o[x] += 19;" + "  })(x);" + "}");
    }

    @Test
    public void test18() {
        testRewriter("var o = {x:{y:23}};" + "for(x in o) {" + "  for(y in o[x]) {" + "    o[x][y] += 19;" + "  }" + "}", "var o = {x:{y:23}};" + "for(x in o) {" + "  (function _forin_body_0(x) {" + "    for(y in o[x]) {" + "      (function _forin_body_1(y) {" + "        o[x][y] += 19;" + "      })(y);" + "    }" + "  })(x);" + "}");
    }

    @Test
    public void test19() {
        testRewriter("function foo(x) {" + "  for(var p in x) {" + "    if(p == 'ret')" + "      return x[p];" + "    x[p]++;" + "  }" + "}", "function foo(x) {" + "  for(var p in x) {" + "    re$ = (function _forin_body_0(p) {" + "      if(p == 'ret')" + "        return {type: 'return', value: x[p]};" + "      x[p]++;" + "    })(p);" + "    if(re$) {" + "      if(re$.type == 'return')" + "        return re$.value;" + "    }" + "  }" + "}");
    }

    @Test
    public void test20() {
        testRewriter("function extend(dest, src) {" + "  for(var p in src)" + "    dest[p] = src[p];" + "}" + "function foo() {" + "  extend({}, {});" + "}" + "foo();", "function extend(dest, src) {" + "  for(var p in src)" + "    (function _forin_body_0(p) {" + "      dest[p] = src[p];" + "    })(p);" + "}" + "function foo() {" + "  extend({}, {});" + "}" + "foo();");
    }

    @Test
    public void test21() {
        testRewriter("function foo() {" + "  var result = [];" + "  for(var style in Element.ShortStyles) {" + "    for(var s in Element.ShortStyles[style]) {" + "      result.push(this.getStyle(s));" + "    }" + "  }" + "}", "function foo() {" + "  var result = [];" + "  for(var style in Element.ShortStyles) {" + "    var s;" + "    (function _forin_body_0(style, thi$) {" + "      for(s in Element.ShortStyles[style]) {" + "        (function _forin_body_1(s) {" + "          result.push(thi$.getStyle(s));" + "        })(s);" + "      }" + "    })(style, this);" + "  }" + "}");
    }

    @Test
    public void test22() {
        testRewriter("function foo(property) {" + "  var result = [];" + "  for(var style in Element.ShortStyles) {" + "    if(property != style) continue; " + "    for(var s in Element.ShortStyles[style]) {" + "      ;" + "    }" + "  }" + "}", "function foo(property) {" + "  var result = [];" + "  for(var style in Element.ShortStyles) {" + "    var s;" + "    re$ = (function _forin_body_0(style) {" + "      if(property != style) return {type:'goto', target:0}; " + "      for(s in Element.ShortStyles[style]) {" + "        (function _forin_body_1(s) {" + "          ;" + "        })(s);" + "      }" + "    })(style);" + "    if(re$) {" + "      if(re$.type == 'goto') {" + "        if(re$.target == 0)" + "          continue;" + "      }" + "    }" + "  }" + "}");
    }

    @Test
    public void test23() {
        testRewriter("function foo(obj) {" + "  for(var p in obj) {" + "    if(p != 'bar')" + "      continue;" + "    return obj[p];" + "  }" + "}", "function foo(obj) {" + "  for(var p in obj) {" + "    re$ = (function _forin_body_0(p) {" + "      if(p != 'bar')" + "        return {type:'goto', target:0};" + "      return {type:'return', value:obj[p]};" + "    })(p);" + "    if(re$) {" + "      if(re$.type == 'return')" + "        return re$.value;" + "      if(re$.type == 'goto') {" + "        if(re$.target == 0)" + "          continue;" + "      }" + "    }" + "  }" + "}");
    }

    @Test
    public void test24() {
        testRewriter("var addSlickPseudos = function() {" + "  for(var name in pseudos)" + "    if(pseudos.hasOwnProperty(name)) {" + "      ;" + "    }" + "}", "var addSlickPseudos = function() {" + "  for(var name in pseudos)" + "    (function _forin_body_0(name) {" + "      if(pseudos.hasOwnProperty(name)) {" + "        ;" + "      }" + "    })(name);" + "}");
    }

    @Test
    public void test25() {
        testRewriter("function ext(dest, src) {" + "  for(var p in src)" + "    do_ext(dest, p, src);" + "}" + "function do_ext(x, p, y) { x[p] = y[p]; }", "function ext(dest, src) {" + "  for(var p in src)" + "    (function _forin_body_0(p) {" + "      do_ext(dest, p, src);" + "    })(p);" + "}" + "function do_ext(x, p, y) { x[p] = y[p]; }");
    }

    @Test
    public void test26() {
        testRewriter("function foo(x) {" + "  for(p in x) {" + "    for(q in p[x]) {" + "      if(b)" + "        return 23;" + "    }" + "  }" + "}", "function foo(x) {" + "  for(p in x) {" + "    re$ = (function _forin_body_0(p) {" + "      for(q in p[x]) {" + "        re$ = (function _forin_body_1(q) {" + "          if(b)" + "            return { type: 'return', value: 23 };" + "        })(q);" + "        if(re$) {" + "          return re$;" + "        }" + "      }" + "    })(p);" + "    if(re$) {" + "      if(re$.type == 'return')" + "        return re$.value;" + "    }" + "  }" + "}");
    }

    @Test
    public void test27() {
        testRewriter("function foo(property) {" + "  var result = [];" + "  outer: for(var style in Element.ShortStyles) {" + "    for(var s in Element.ShortStyles[style]) {" + "      if(s != style) continue outer;" + "    }" + "  }" + "}", "function foo(property) {" + "  var result = [];" + "  outer: for(var style in Element.ShortStyles) {" + "    var s;" + "    re$ = (function _forin_body_0(style) {" + "      for(s in Element.ShortStyles[style]) {" + "        re$ = (function _forin_body_1(s) {" + "          if(s != style) return {type:'goto', target:0};" + "        })(s);" + "        if(re$) {" + "          return re$;" + "        }" + "      }" + "    })(style);" + "    if(re$) {" + "      if(re$.type == 'goto') {" + "        if(re$.target == 0)" + "          continue outer;" + "      }" + "    }" + "  }" + "}");
    }

    @Test
    public void test28() {
        testRewriter("function foo(property) {" + "  var result = [];" + "  outer: for(var style in Element.ShortStyles) {" + "    for(var s in Element.ShortStyles[style]) {" + "      if(s != style) continue;" + "    }" + "  }" + "}", "function foo(property) {" + "  var result = [];" + "  outer: for(var style in Element.ShortStyles) {" + "    var s;" + "    (function _forin_body_0(style) {" + "      for(s in Element.ShortStyles[style]) {" + "        re$ = (function _forin_body_1(s) {" + "          if(s != style) return {type:'goto', target:0};" + "        })(s);" + "        if(re$) {" + "          if(re$.type == 'goto') {" + "            if(re$.target == 0)" + "              continue;" + "          }" + "        }" + "      }" + "    })(style);" + "  }" + "}");
    }

    @Test
    public void test29() {
        testRewriter("Element.addMethods = function(methods) {" + "  function copy() {" + "    for (var property in methods) {" + "    }" + "  }" + "  for (var tag in methods) {" + "  }" + "};", "Element.addMethods = function(methods) {" + "  function copy() {" + "    for (var property in methods) {" + "      (function _forin_body_1(property) {" + "      })(property);" + "    }" + "  }" + "  for (var tag in methods) {" + "    (function _forin_body_0(tag){ })(tag);" + "  }" + "};");
    }

    @Test
    public void test30() {
        testRewriter("try {" + "  for(var i in {}) {" + "    f();" + "  }" + "} catch(_) {}", "try {" + "  for(var i in {}) {" + "    (function _forin_body_0(i) {" + "      f();" + "    })(i);" + "  }" + "} catch(_) {}");
    }

    @Test
    public void test31() {
        testRewriter("function extend(dest, src) {" + "  for(var p in src) {" + "    arguments[0][p] = src[p];" + "  }" + "}", "function extend(dest, src) {" + "  for(var p in src) {" + "    arguments[0][p] = src[p];" + "  }" + "}");
    }
}
