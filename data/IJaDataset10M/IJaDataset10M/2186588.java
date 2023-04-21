package org.vexi.core;

import java.io.IOException;
import org.ibex.js.Backtraceable;
import org.ibex.js.JS;
import org.ibex.js.JSExn;
import org.ibex.js.JSU;
import org.ibex.js.Parser;
import org.ibex.js.Thread;
import org.ibex.util.Basket;
import org.ibex.util.Tree;
import org.ibex.util.Vec;
import org.ibex.util.XML;

/**
 *  Encapsulates a template node 
 *  <p>
 *  <h4>General Structure of a template file </h4>
 *  <pre>
 *  &lt;vexi>
 *  	// 'Static' JS code
 *  	
 *  	&lt;preapply1/>
 *  	...
 *  	&lt;preapplyN/>
 *  	&lt;principal>
 *  		&lt;anon1>
 *  			&lt;anon1_1/>
 *  			...
 *  			&lt;anon1_N/>
 *  		&lt;/anonA>
 *  		...
 *  		&lt;anonN>
 *  			&lt;anonNN/>
 *  			...
 *  			&lt;anonNN/>
 *  		&lt;/anonA>
 *  	&lt;/principal>
 *  &lt;/vexi&gt
 *  </pre>
 *  <h3> Functional Notes </h3>
 *  <p>
 *  'preapplyX', 'principal' and 'anonX' are all template nodes.
 *  Each template node may contain JS code in the text part, which is executed
 *  when the template is applied. 
 *  </p>
 *  <p>
 *  The element of a name either resolves to the uri <i>vexi://ui.box</i> or it 
 *  is resolved to another template, which will be applied first. If this is the
 *  case we are subclassing the resolved template node.
 *  </p>
 *  <p>
 *  Preapplies allow multiple inheritance (in root nodes only).
 *  </p>
 *  <p>
 *  Templates are analogous to classes and are static. There purpose is to create
 *  structures of boxes + associated JS code. It is possible to achieve the same
 *  effect by programmatically, in JS, building up a box hierarchy and adding JS
 *  traps/functions to the boxes.
 *  </p>

 *  <h3> Implementation Notes </h3>
 *  <p>
 *  Each template node is represented by an instance of org.vexi.core.Template 
 *  </p>
 *  <p>
 *  Some of the instance members are not meaningful on non-root
 *  Template instances. We refer to these non-root instances as
 *  <i>anonymous templates</i>.
 *  </p>
 *  @author mgoodwin
 *  @author amegacz
 *  @author dcrawshaw
 */
public class Template extends CodeBlock implements Constants, Backtraceable {

    String id = null;

    JS[] keys;

    JS[] vals;

    Prefixes uriPrefixes;

    Vec children = new Vec();

    JS script = null;

    Template preapply = null;

    Template principal = null;

    Template parent = null;

    Static staticPart = null;

    class Static {

        String sourceName;

        JS staticObject;

        Prefixes uriPrefixes;

        public Static(String sourceName, Prefixes staticPrefixes) {
            staticObject = new JS.Obj();
            uriPrefixes = staticPrefixes;
            this.sourceName = sourceName;
        }
    }

    public JS getStatic() {
        if (staticPart == null) return null;
        return staticPart.staticObject;
    }

    JS vexi;

    Parser.GlobalsChecker pisParserParam = null;

    Template(Template t, int startLine) {
        preapply = t;
        this.vexi = t.vexi;
        this.startLine = startLine;
    }

    Template(JS vexi, int startLine) {
        this.vexi = vexi;
        this.startLine = startLine;
    }

    Template(JS vexi, int startLine, Template parent) {
        this(vexi, startLine);
        this.parent = parent;
    }

    /** Applies the template to Box b
     *  @param pboxes a vector of all box parents on which to put $-references
     *  @param ptemplates a vector of the fileNames to recieve private references on the pboxes
     */
    public void apply(Box b) throws JSExn {
        try {
            apply(b, null);
        } catch (IOException e) {
            throw new JSExn(e.toString());
        } catch (JSExn e) {
            throw e;
        }
    }

    private static final JS[] callempty = new JS[0];

    private void apply(Box b, PerInstantiationScope parentPis) throws JSExn, IOException {
        Thread.getCurrentInterpreter().enterNonJSCall(this);
        try {
            if (preapply != null) preapply.apply(b, null);
            if (principal != null) principal.apply(b, null);
            if (id != null && parentPis != null) parentPis.putDollar(id, b);
            if (parentPis != null) staticPart = parentPis.getStaticPart();
            PerInstantiationScope pis = new PerInstantiationScope(b, parentPis);
            for (int i = 0; children != null && i < children.size(); i++) {
                Box kid = new Box();
                ((Template) children.elementAt(i)).apply(kid, pis);
                b.putAndTriggerTraps(b.get(SC_numchildren), kid);
            }
            if (script != null) JSU.cloneWithNewGlobalScope(script, pis).call(null, callempty);
            for (int i = 0; keys != null && i < keys.length; i++) {
                if (keys[i] == null) continue;
                JS key = keys[i];
                JS val = vals[i];
                if ("null".equals(val)) val = null;
                if (JSU.isString(val) && (JSU.toString(val).length() > 0)) {
                    switch(JSU.toString(val).charAt(0)) {
                        case '$':
                            val = pis.get(val);
                            if (val == null) throw new JSExn("unknown box id '" + JSU.str(vals[i]) + "' referenced in XML attribute");
                            break;
                        case '.':
                            val = Vexi.resolveString(vexi, JSU.toString(val).substring(1), false);
                    }
                }
                b.putAndTriggerTraps(key, val);
            }
        } finally {
            Thread.getCurrentInterpreter().exitNonJSCall();
        }
    }

    public String traceLine() {
        return fileName() + ":" + startLine + "(apply)";
    }

    ;

    private String fileName() {
        if (staticPart != null) {
            return staticPart.sourceName;
        }
        return parent.fileName();
    }

    /**
     * Contains list of xml namespace prefixes. The &lt;vexi/> and
     * each template node has its xml namespace converted into JS objects
     * (blessings) giving them an equivalent sense in JS code.
     * <p>
     * Initially stores a string for the value. Resolves and converts it to 
     * a blessing on first access.
     * 
     * @author mike
     *
     */
    static class Prefixes {

        static final Object NULL_PLACEHOLDER = new Object();

        private Basket.Hash map = new Basket.Hash();

        Prefixes parent;

        JS vexi;

        Prefixes(JS vexi, Prefixes parent, Tree.Element e) {
            this.parent = parent;
            this.vexi = vexi;
            Tree.Prefixes prefixes = e.getPrefixes();
            for (int i = 0; i < prefixes.pfxSize(); i++) {
                String key = prefixes.getPrefixKey(i);
                String val = prefixes.getPrefixVal(i);
                if (val.equals("vexi://ui")) continue;
                if (val.equals("vexi://meta")) continue;
                if (val.length() > 0 && val.charAt(0) == '.') val = val.substring(1);
                map.put(JSU.S(key), val);
            }
        }

        public boolean isPrefix(String key) {
            try {
                return get(JSU.S(key), false) != null;
            } catch (JSExn e) {
                e.printStackTrace();
                return false;
            }
        }

        public Object get(JS key, boolean resolve) throws JSExn {
            Object r = map.get(key);
            if (r == NULL_PLACEHOLDER) return null;
            if (resolve && r instanceof String) {
                r = Vexi.resolveString(vexi, (String) r, true);
                map.put(key, r);
            }
            if (r == null && parent != null) {
                r = parent.get(key, resolve);
                if (r == null) {
                    if ("".equals(JSU.toString(key))) r = vexi.get(key); else map.put(key, NULL_PLACEHOLDER);
                }
            }
            return r;
        }
    }

    private JS commonGlobals(String s) throws JSExn {
        if (s.equals("vexi")) return vexi;
        if (s.equals("static")) return staticPart.staticObject;
        if (s.equals("")) return vexi.getAndTriggerTraps(SC_);
        return null;
    }

    /** 
     * The StaticScope is the global scope for the static javascript code 
     *  @author mike
     */
    class StaticScope extends JS.Obj {

        public JS get(JS key) throws JSExn {
            JS r;
            if (JSU.isString(key)) {
                String s = JSU.toString(key);
                if (super.get(key) != null) return super.get(key);
                r = (JS) staticPart.uriPrefixes.get(key, true);
                if (r != null) return r;
                r = commonGlobals(s);
                if (r != null) return r;
            }
            return staticPart.staticObject.getAndTriggerTraps(key);
        }

        public void put(JS key, JS val) throws JSExn {
            staticPart.staticObject.putAndTriggerTraps(key, val);
        }

        public void addTrap(JS key, JS f) throws JSExn {
            staticPart.staticObject.addTrap(key, f);
        }

        public void delTrap(JS key, JS f) throws JSExn {
            staticPart.staticObject.delTrap(key, f);
        }

        public Trap getTrap(JS key) throws JSExn {
            return staticPart.staticObject.getTrap(key);
        }
    }

    /** 
     * <p>
     * The PIS is the global scope for the javascript inside a template instantiation 
     * (box described by its xml element).
     * </p><p>
     * There is also a hierarchy of PISs. This serves two purposes:
     * <ol>
     * <li> child -> parent<br> 
     * Propagate the id of the box to all parent PISs, so that they can refer
     * 	    to the interior templates id.</li>
     * <li> parent-> child<br>
     * Propagate xml namespace prefix JS objects. Respecting the xml sense, that child
     * elements inherit from parent elements</li> 
     * </ol>
     * </p> 
     * <p>
     * The challenge is that the Global scope corresponds to the box properties + 
     * $x for child boxes + uri prefix objects + global + static object
     * </p>
     */
    class PerInstantiationScope extends JS.Obj {

        PerInstantiationScope parentBoxPis = null;

        JS box;

        void putDollar(String key, Box target) throws JSExn {
            if (parentBoxPis != null) parentBoxPis.putDollar(key, target);
            JS jskey = JSU.S("$" + key);
            sput(jskey, target);
        }

        Static getStaticPart() {
            return staticPart;
        }

        void sput(JS key, JS val) throws JSExn {
            super.put(key, val);
        }

        public PerInstantiationScope(JS box, PerInstantiationScope parentBoxPis) {
            this.parentBoxPis = parentBoxPis;
            this.box = box;
        }

        public JS get(JS key) throws JSExn {
            JS r;
            if (JSU.isString(key)) {
                String s = JSU.toString(key);
                if (super.get(key) != null) return super.get(key);
                r = (JS) uriPrefixes.get(key, true);
                if (r != null) return r;
                r = commonGlobals(s);
                if (r != null) return r;
            }
            return box.getAndTriggerTraps(key);
        }

        public void put(JS key, JS val) throws JSExn {
            box.putAndTriggerTraps(key, val);
        }

        public void addTrap(JS key, JS f) throws JSExn {
            box.addTrap(key, f);
        }

        public void delTrap(JS key, JS f) throws JSExn {
            box.delTrap(key, f);
        }

        public Trap getTrap(JS key) throws JSExn {
            return box.getTrap(key);
        }
    }
}

class CodeBlock {

    StringBuffer content = null;

    int content_start = 0;

    int startLine = -1;
}
