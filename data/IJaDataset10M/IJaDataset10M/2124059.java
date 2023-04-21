package org.xmlsh.marklogic;

import java.util.List;
import org.xmlsh.core.Options;
import org.xmlsh.core.OutputPort;
import org.xmlsh.core.XValue;
import org.xmlsh.marklogic.util.MLCommand;
import org.xmlsh.sh.shell.SerializeOpts;
import com.marklogic.xcc.ContentSource;
import com.marklogic.xcc.Request;
import com.marklogic.xcc.ResultSequence;
import com.marklogic.xcc.Session;
import com.marklogic.xcc.ValueFactory;
import com.marklogic.xcc.types.XName;
import com.marklogic.xcc.types.XSString;
import com.marklogic.xcc.types.XdmVariable;

public class invoke extends MLCommand {

    @Override
    public int run(List<XValue> args) throws Exception {
        Options opts = new Options("c=connect:,v,t=text,binary", SerializeOpts.getOptionDefs());
        opts.parse(args);
        args = opts.getRemainingArgs();
        boolean asText = opts.hasOpt("t");
        boolean bBinary = opts.hasOpt("binary");
        SerializeOpts serializeOpts = getSerializeOpts(opts);
        mContentSource = getConnection(opts);
        String module = args.remove(0).toString();
        OutputPort out = getStdout();
        mSession = mContentSource.newSession();
        Request request = mSession.newModuleInvoke(module);
        if (opts.hasOpt("v")) {
            for (int i = 0; i < args.size() / 2; i++) {
                String name = args.get(i * 2).toString();
                XValue value = args.get(i * 2 + 1);
                XdmVariable var = newVariable(name, value, serializeOpts);
                request.setVariable(var);
            }
        }
        ResultSequence rs = mSession.submitRequest(request);
        writeResult(rs, out, serializeOpts, asText, bBinary);
        mSession.close();
        return 0;
    }
}
