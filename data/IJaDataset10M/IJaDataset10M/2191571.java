package org.xmlsh.commands.stax;

import java.io.IOException;
import java.util.List;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import net.sf.saxon.s9api.SaxonApiException;
import org.xmlsh.core.BuiltinFunctionCommand;
import org.xmlsh.core.CoreException;
import org.xmlsh.core.XValue;
import org.xmlsh.sh.shell.Shell;

public class newStreamWriter extends BuiltinFunctionCommand {

    public newStreamWriter() {
        super("newEventWriter");
    }

    @Override
    public XValue run(Shell shell, List<XValue> args) throws CoreException, XMLStreamException, SaxonApiException, IOException {
        if (args.size() == 0) return new XValue(shell.getEnv().getStdout().asXMLStreamWriter(shell.getSerializeOpts())); else return new XValue(shell.getEnv().getOutput(args.get(0), false).asXMLStreamWriter(shell.getSerializeOpts()));
    }
}
