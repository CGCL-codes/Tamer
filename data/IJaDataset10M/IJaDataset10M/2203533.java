package cz.cuni.mff.ksi.jinfer.xsdimporter;

import cz.cuni.mff.ksi.jinfer.base.interfaces.Processor;
import cz.cuni.mff.ksi.jinfer.base.objects.FolderType;
import cz.cuni.mff.ksi.jinfer.base.objects.nodes.Element;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDException;
import cz.cuni.mff.ksi.jinfer.xsdimporter.utils.XSDImportSettings;
import cz.cuni.mff.ksi.jinfer.xsdimporter.interfaces.XSDParser;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

/**
 * Class providing logic for IG retrieval from XSD schemas.
 * Implementation of {@link cz.cuni.mff.ksi.jinfer.base.interfaces.Processor}
 * @author reseto
 */
@ServiceProvider(service = Processor.class)
public class XSDProcessor implements Processor<Element> {

    private static final Logger LOG = Logger.getLogger(XSDProcessor.class);

    @Override
    public FolderType getFolder() {
        return FolderType.SCHEMA;
    }

    @Override
    public String getExtension() {
        return "xsd";
    }

    @Override
    public List<Element> process(final InputStream stream) throws InterruptedException {
        LOG.setLevel(XSDImportSettings.getLogLevel());
        final XSDParser parser = XSDImportSettings.getParser();
        try {
            if (parser != null) {
                LOG.info(NbBundle.getMessage(XSDProcessor.class, "Info.ParsingMethod", parser.getDisplayName()));
                final List<Element> rules = parser.parse(stream);
                printDebugInfo(rules, "AfterParsing");
                return rules;
            } else {
                LOG.error(NbBundle.getMessage(XSDProcessor.class, "Error.NoParser"));
                return Collections.emptyList();
            }
        } catch (final XSDException e) {
            if (XSDImportSettings.isStopOnError()) {
                throw new RuntimeException(NbBundle.getMessage(XSDProcessor.class, "Exception.Parsing"), e);
            } else {
                LOG.error(NbBundle.getMessage(XSDProcessor.class, "Error.IgnoreParsing"), e);
                return Collections.emptyList();
            }
        }
    }

    /**
   * Prints information about number of rules after a specified stage of execution.
   * Either only the number of rules is displayed, or if verbose setting is enabled,
   * full rules are printed to log output.
   * @param settings Current settings of the module, toggling the verbose option.
   * @param rules List of rules to be displayed.
   * @param stateMessageName Part of the name of the message that defines current execution stage
   * (values "AfterParsing" and "AfterExpanding" are defined in bundle).
   */
    private void printDebugInfo(final List<Element> rules, final String stateMessageName) {
        if (XSDImportSettings.isVerbose()) {
            LOG.info(NbBundle.getMessage(XSDProcessor.class, "Debug.Rules." + stateMessageName + ".FullMsg", rules.size()));
            for (Element elem : rules) {
                LOG.debug(elem.toString());
            }
        } else {
            LOG.info(NbBundle.getMessage(XSDProcessor.class, "Debug.Rules." + stateMessageName + ".ShortMsg", rules.size()));
        }
    }

    @Override
    public boolean processUndefined() {
        return false;
    }

    @Override
    public Class<?> getResultType() {
        return Element.class;
    }
}
