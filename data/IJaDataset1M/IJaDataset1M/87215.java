package org.xmlsh.commands.internal;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;
import org.xmlsh.core.InvalidArgumentException;
import org.xmlsh.core.Options;
import org.xmlsh.core.OutputPort;
import org.xmlsh.core.XCommand;
import org.xmlsh.core.XValue;
import org.xmlsh.sh.shell.SerializeOpts;
import org.xmlsh.util.Util;

public class xurlencode extends XCommand {

    public int run(List<XValue> args) throws Exception {
        Options opts = new Options("n,p=port:", SerializeOpts.getOptionDefs());
        opts.parse(args);
        boolean nolf = opts.hasOpt("n");
        String port = opts.getOptString("p", null);
        OutputPort stdout = port != null ? getEnv().getOutputPort(port) : getEnv().getStdout();
        if (stdout == null) throw new InvalidArgumentException("Output port not found: " + port);
        SerializeOpts serializeOpts = getSerializeOpts(opts);
        OutputStream out = stdout.asOutputStream(serializeOpts);
        args = opts.getRemainingArgs();
        if (args.size() > 0) {
            args = Util.expandSequences(args);
            boolean bFirst = true;
            for (XValue arg : args) {
                if (!bFirst) out.write('&');
                bFirst = false;
                String value = arg.toString();
                value = URLEncoder.encode(value, serializeOpts.getInputXmlEncoding());
                out.write(value.getBytes(serializeOpts.getOutputTextEncoding()));
            }
            if (!nolf) out.write(Util.getNewline(serializeOpts));
        } else {
            InputStream is = getStdin().asInputStream(serializeOpts);
            String data = Util.readString(is, serializeOpts.getInputTextEncoding());
            String value = URLEncoder.encode(data, serializeOpts.getInputTextEncoding());
            out.write(value.getBytes(serializeOpts.getOutputTextEncoding()));
            is.close();
        }
        out.close();
        return 0;
    }
}
