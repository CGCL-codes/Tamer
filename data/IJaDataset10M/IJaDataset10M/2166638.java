package org.jtools.tmplc.item;

import javax.tools.Diagnostic.Kind;
import org.jtools.tmpl.compiler.api.Arguments;
import org.jtools.tmpl.compiler.api.CompilerException;
import org.jtools.tmpl.compiler.api.Identifier;
import org.jtools.tmpl.compiler.api.Template;
import org.jtools.tmplc.CompilerItemCallback;
import org.jtools.tmplc.CompilerItemVisitor;
import org.jtools.tmplc.InfoSupport;
import org.jtools.tmplc.MacroResourcesImpl;
import org.jtools.tmplc.MacroStatement;
import org.jtools.tmplc.ParsedStatement;
import org.jtools.tmplc.SimpleMacroDescriptor;
import org.jtools.tmplc.parser.Parser;
import org.jtools.util.SimpleNamedValue;

public class MacroItem extends _MultiLineMacroItem implements MacroStatement {

    protected SimpleMacroDescriptor macro;

    protected Identifier macroid;

    protected Parser parser = null;

    protected Template template = null;

    public MacroItem(CompilerItem parent, Parser parser, InfoSupport support, char[][] txt, Arguments arguments, SimpleMacroDescriptor macro, Identifier macroid) {
        super(parent, support, txt, arguments);
        this.parser = parser;
        this.macro = macro;
        this.macroid = macroid;
    }

    public MacroItem(CompilerItem parent, Parser parser, InfoSupport support, String txt[], Arguments arguments, SimpleMacroDescriptor macro, Identifier macroid) {
        super(parent, support, txt, arguments);
        this.parser = parser;
        this.macro = macro;
        this.macroid = macroid;
    }

    public CompilerException createArgumentException(String message, int argIndex) {
        return getSupport().createException(message);
    }

    public BeginItem createBegin() {
        return new BeginItem(this, support);
    }

    public CodeItem createCode(String code) {
        return new CodeItem(this, support, code);
    }

    public CommentItem createComment(String txt) {
        return new CommentItem(this, support, txt);
    }

    public EndItem createEnd() {
        return new EndItem(this, support);
    }

    public CompilerException createException(String message) {
        return getSupport().createException(message);
    }

    public ExpressionItem createExpression(String txt) {
        return new ExpressionItem(this, support, txt);
    }

    @SuppressWarnings("unchecked")
    public ExtendsItem createExtends(String classname) {
        return new ExtendsItem(this, support, new SimpleNamedValue<String, String>(null, classname));
    }

    public GlobalItem createGlobal(String code) {
        return new GlobalItem(this, support, code);
    }

    public HeaderItem createHeader(String code) {
        return new HeaderItem(this, support, code);
    }

    public ImageItem createImage(String txt) {
        return new ImageItem(this, support, txt);
    }

    @SuppressWarnings("unchecked")
    public ImplementsItem createImplements(String classname) {
        return new ImplementsItem(this, support, new SimpleNamedValue<String, String>(null, classname));
    }

    @SuppressWarnings("unchecked")
    public IncludeItem createInclude(String filename) {
        return new IncludeItem(this, support, new SimpleNamedValue<String, String>(null, filename));
    }

    public CompilerException createLineException(String message, int lineIndex) {
        return getSupport().createException(message);
    }

    public CompilerException createLineException(String message, int lineIndex, int colIndex) {
        return getSupport().createException(message);
    }

    public MacroItem createMacro(Parser parser, Identifier macroid, Arguments arguments, String txt[]) {
        return new MacroItem(this, parser, support, txt, arguments, null, macroid);
    }

    @Override
    public void execute(CompilerItemCallback cb) {
        this.template = cb.getTemplate();
        ParsedStatement parsedStatement = new ParsedStatement(macroid, arguments);
        if (macro == null) macro = cb.findMacro(parsedStatement);
        macro.call(new MacroResourcesImpl(cb, this), arguments, getImages());
    }

    public Identifier getMacroID() {
        return macroid;
    }

    public Parser getParser() {
        return parser;
    }

    public Template getTemplate() {
        return template;
    }

    public <R, D> R accept(CompilerItemVisitor<R, D> visitor, D... data) {
        return visitor.visitMacroItem(this, data);
    }

    public void report(Kind kind, String code, String msg) {
        support.report(kind, code, msg);
    }

    @SuppressWarnings("unchecked")
    public ImportItem createImport(String classname) {
        return new ImportItem(this, support, new SimpleNamedValue<String, String>(null, classname));
    }

    public JavaDocItem createJavaDoc(String text) {
        return new JavaDocItem(this, support, text);
    }

    public MetaItem createMeta(String line) {
        return new MetaItem(this, support, line);
    }
}
