package com.google.clearsilver.jsilver.functions.bundles;

import com.google.clearsilver.jsilver.functions.escape.*;
import com.google.clearsilver.jsilver.functions.html.CssUrlValidateFunction;
import com.google.clearsilver.jsilver.functions.html.HtmlStripFunction;
import com.google.clearsilver.jsilver.functions.html.HtmlUrlValidateFunction;
import com.google.clearsilver.jsilver.functions.html.TextHtmlFunction;
import com.google.clearsilver.jsilver.functions.numeric.AbsFunction;
import com.google.clearsilver.jsilver.functions.numeric.MaxFunction;
import com.google.clearsilver.jsilver.functions.numeric.MinFunction;
import com.google.clearsilver.jsilver.functions.string.CrcFunction;
import com.google.clearsilver.jsilver.functions.string.FindFunction;
import com.google.clearsilver.jsilver.functions.string.LengthFunction;
import com.google.clearsilver.jsilver.functions.string.SliceFunction;
import com.google.clearsilver.jsilver.functions.structure.FirstFunction;
import com.google.clearsilver.jsilver.functions.structure.LastFunction;
import com.google.clearsilver.jsilver.functions.structure.SubcountFunction;

/**
 * Set of functions required to allow JSilver to be compatible with ClearSilver.
 */
public class ClearSilverCompatibleFunctions extends CoreOperators {

    @Override
    protected void setupDefaultFunctions() {
        super.setupDefaultFunctions();
        registerFunction("subcount", new SubcountFunction());
        registerFunction("first", new FirstFunction());
        registerFunction("last", new LastFunction());
        registerFunction("len", new SubcountFunction());
        registerFunction("abs", new AbsFunction());
        registerFunction("max", new MaxFunction());
        registerFunction("min", new MinFunction());
        registerFunction("string.slice", new SliceFunction());
        registerFunction("string.find", new FindFunction());
        registerFunction("string.length", new LengthFunction());
        registerFunction("string.crc", new CrcFunction());
        registerFunction("url_escape", new UrlEscapeFunction("UTF-8"), true);
        registerEscapeMode("url", new UrlEscapeFunction("UTF-8"));
        registerFunction("html_escape", new HtmlEscapeFunction(false), true);
        registerEscapeMode("html", new HtmlEscapeFunction(false));
        registerFunction("js_escape", new JsEscapeFunction(false), true);
        registerEscapeMode("js", new JsEscapeFunction(false));
        registerEscapeMode("html_unquoted", new HtmlEscapeFunction(true));
        registerEscapeMode("js_attr_unquoted", new JsEscapeFunction(true));
        registerEscapeMode("js_check_number", new JsValidateUnquotedLiteral());
        registerEscapeMode("url_validate_unquoted", new HtmlUrlValidateFunction(true));
        registerEscapeMode("css", new StyleEscapeFunction(false));
        registerEscapeMode("css_unquoted", new StyleEscapeFunction(true));
        registerFunction("html_strip", new HtmlStripFunction());
        registerFunction("text_html", new TextHtmlFunction());
        registerFunction("url_validate", new HtmlUrlValidateFunction(false), true);
        registerEscapeMode("url_validate", new HtmlUrlValidateFunction(false));
        registerFunction("css_url_validate", new CssUrlValidateFunction(), true);
        registerFunction("null_escape", new NullEscapeFunction(), true);
    }
}
