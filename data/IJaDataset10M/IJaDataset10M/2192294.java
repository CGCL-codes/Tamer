package org.xmlsh.commands.internal;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.XQueryCompiler;
import net.sf.saxon.s9api.XQueryEvaluator;
import net.sf.saxon.s9api.XQueryExecutable;
import net.sf.saxon.s9api.XdmAtomicValue;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;
import org.xmlsh.core.IXdmValueOutputStream;
import org.xmlsh.core.InputPort;
import org.xmlsh.core.Namespaces;
import org.xmlsh.core.Options;
import org.xmlsh.core.OutputPort;
import org.xmlsh.core.XCommand;
import org.xmlsh.core.XValue;
import org.xmlsh.core.Options.OptionValue;
import org.xmlsh.sh.shell.SerializeOpts;
import org.xmlsh.sh.shell.Shell;
import org.xmlsh.sh.shell.ShellModuleURIResolver;
import org.xmlsh.util.Util;
import org.xmlsh.xpath.EvalDefinition;

public class xquery extends XCommand {

    @Override
    public int run(List<XValue> args) throws Exception {
        Options opts = new Options("c=context:,cf=context-file:,f=file:,i=input:,n,q:,v,nons,ns:+,s=string,b=bool,baseuri:,noxmlsh", SerializeOpts.getOptionDefs());
        opts.parse(args);
        Processor processor = Shell.getProcessor();
        XQueryCompiler compiler = processor.newXQueryCompiler();
        compiler.setModuleURIResolver(new ShellModuleURIResolver(getEnv().getShell()));
        XdmItem context = null;
        SerializeOpts serializeOpts = getSerializeOpts(opts);
        InputPort in = null;
        boolean bString = opts.hasOpt("s");
        boolean bBool = opts.hasOpt("b");
        boolean bNoXmlsh = opts.hasOpt("noxmlsh");
        String baseURI = opts.getOptString("baseuri", null);
        if (baseURI != null) baseURI = getAbsoluteURI(baseURI);
        if (!opts.hasOpt("n")) {
            if (opts.hasOpt("c")) context = opts.getOptValue("c").asXdmItem(); else if (opts.hasOpt("cf")) context = (in = getInput(new XValue(opts.getOptString("cf", "-")))).asXdmItem(serializeOpts); else if (opts.hasOpt("i")) context = (in = getInput(opts.getOptValue("i"))).asXdmItem(serializeOpts); else context = (in = getStdin()).asXdmItem(serializeOpts);
        }
        String query = null;
        if (opts.hasOpt("q")) query = opts.getOpt("q").getValue().toString();
        XQueryExecutable expr = null;
        List<XValue> xvargs = opts.getRemainingArgs();
        OptionValue ov = opts.getOpt("f");
        if (ov != null) {
            if (query != null) throwInvalidArg("Cannot specifify both -q and -f");
            InputPort qin = getInput(ov.getValue());
            InputStream is = qin.asInputStream(serializeOpts);
            query = Util.readString(is, serializeOpts.getInputTextEncoding());
            if (baseURI == null) {
                String sysid = qin.getSystemId();
                if (!Util.isBlank(sysid)) {
                    String uri = getAbsoluteURI(sysid);
                    baseURI = uri;
                }
            }
            is.close();
            qin.close();
        }
        if (baseURI == null) compiler.setBaseURI(getShell().getEnv().getBaseURI()); else compiler.setBaseURI(new URI(baseURI));
        if (query == null) {
            if (xvargs.size() < 1) throwInvalidArg("No query specified");
            query = xvargs.remove(0).toString();
        }
        Namespaces ns = null;
        if (!opts.hasOpt("nons")) ns = getEnv().getNamespaces();
        if (opts.hasOpt("ns")) {
            Namespaces ns2 = new Namespaces();
            if (ns != null) ns2.putAll(ns);
            for (XValue v : opts.getOpt("ns").getValues()) ns2.declare(v);
            ns = ns2;
        }
        if (ns != null) {
            for (String prefix : ns.keySet()) {
                String uri = ns.get(prefix);
                compiler.declareNamespace(prefix, uri);
            }
        }
        if (!bNoXmlsh) compiler.declareNamespace("xmlsh", EvalDefinition.kXMLSH_EXT_NAMESPACE);
        expr = compiler.compile(query);
        XQueryEvaluator eval = expr.load();
        if (context != null) eval.setContextItem(context);
        if (opts.hasOpt("v")) {
            for (int i = 0; i < xvargs.size() / 2; i++) {
                String name = xvargs.get(i * 2).toString();
                XValue value = xvargs.get(i * 2 + 1);
                QName vqname = Util.resolveQName(name, ns);
                eval.setExternalVariable(vqname, value.asXdmValue());
            }
        }
        if (in != null) in.close();
        if (bBool) {
            XValue value = new XValue(eval.evaluate());
            return value.toBoolean() ? 0 : 1;
        }
        OutputPort stdout = getStdout();
        IXdmValueOutputStream ser = stdout.asXdmValueOutputStream(serializeOpts);
        boolean bFirst = true;
        boolean bAnyOut = false;
        for (XdmItem item : eval) {
            bAnyOut = true;
            if (!bFirst) stdout.writeSequenceSeperator(serializeOpts);
            bFirst = false;
            if (item instanceof XdmNode) {
                XdmNode node = (XdmNode) item;
                if (bString) item = new XdmAtomicValue(node.getStringValue());
            }
            ser.write(item);
        }
        if (bAnyOut) stdout.writeSequenceTerminator(serializeOpts);
        return 0;
    }
}
