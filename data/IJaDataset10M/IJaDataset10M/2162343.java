package ant.famix;

import ant.famix.cdif.Output;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * @author mzeibig
 * @created 08.04.2004 22:00:00
 * @since
 * @version $Revision: 1.2 $
 */
final class HeaderWriter extends AbstractTaskLevelWriter {

    HeaderWriter(final FileWriter fw, final Set ignoreSet) {
        super(fw, ignoreSet);
        packagecount = 0;
        classcount = 0;
        methodcount = 0;
        fieldcount = 0;
        invokecount = 0;
        inheritcount = 0;
        accesscount = 0;
        localvarcount = 0;
    }

    int write(final int counter) throws IOException {
        if (registry.isXMI()) {
            return writeXMI(counter);
        } else {
            return writeCDIF(counter);
        }
    }

    int writeXMI(final int counter) throws IOException {
        ant.famix.model.Model model = createModel();
        fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + "<XMI xmi.version=\"1.0\" timestamp=\"November 23, 2000 8:39:52.000\">\n" + "    <XMI.header>\n" + "        <XMI.documentation>\n" + "            <XMI.exporter>JAVA2FAMIX</XMI.exporter>\n" + "            <XMI.exporterVersion>0.1</XMI.exporterVersion>\n" + "        </XMI.documentation>\n" + "        <XMI.metamodel xmi.name=\"FAMIX\" xmi.version=\"" + (registry.getFamixVersion() == 20 ? "2.0" : "2.2") + "\"/>\n" + "    </XMI.header>\n\n" + "    <XMI.content>\n" + ant.famix.xmi.Output.getAsString(model, 0));
        return counter;
    }

    int writeCDIF(final int counter) throws IOException {
        ant.famix.model.Model model = createModel();
        fw.write("CDIF, SYNTAX \"SYNTAX.1\" \"02.00.00\", ENCODING \"ENCODING.1\" \"02.00.00\"\n" + "#| This file contains a transfer with information according to the\n" + "   FAMOOS Information Exchange (FAMIX) Model, see\n" + "	 http://www.iam.unibe.ch/~famoos/FAMIX/\n" + "   using the CDIF standard for information exchange, see\n" + "	 http://www.eigroup.org/cdif/index.html\n" + "|#\n" + "\n" + "\n" + "(:HEADER\n" + "	(:SUMMARY\n" + "		(ExporterName  \"JAVA2FAMIX\")\n" + "		(ExporterVersion  \"0.1\")\n" + "		(ExporterDate  \"" + new SimpleDateFormat("yyyy/MM/dd").format(new Date()) + "\")\n" + "		(ExporterTime  \"" + new SimpleDateFormat("hh:mm:ss").format(new Date()) + "\")\n" + "		(PublisherName \"" + System.getProperties().getProperty("user.name", "unknown") + "\")\n" + "		(ParsedSystemName \"unknown\")\n" + "		(ReificationLevel \"" + registry.getLevel() + "\")\n" + "		(SourceLanguage   \"Java\")\n" + "	)\n" + ")\n" + "\n" + "(:META-MODEL\n" + "\n" + "	(:SUBJECTAREAREFERENCE Foundation\n" + "		(:VERSIONNUMBER \"01.00\")\n" + "	)\n" + "\n" + "	(:SUBJECTAREAREFERENCE FAMIX\n" + (registry.getFamixVersion() == 20 ? "       (:VERSIONNUMBER \"2.0\")\n" : "       (:VERSIONNUMBER \"2.2\")\n") + "	)\n" + ")\n" + "\n" + "\n" + "(:MODEL\n\n" + Output.getAsString(model, 0) + "\n");
        return counter;
    }

    private ant.famix.model.Model createModel() {
        ant.famix.model.Model model;
        if (registry.getFamixVersion() == 20) {
            model = new ant.famix.model.model20.Model("JAVA2FAMIX", "0.1", new SimpleDateFormat("yyyy/MM/dd").format(new Date()), new SimpleDateFormat("hh:mm:ss").format(new Date()), System.getProperties().getProperty("user.name", "unknown"), Integer.toString(registry.getLevel()), "Java");
        } else {
            model = new ant.famix.model.model22.Model("JAVA2FAMIX", "0.1", new SimpleDateFormat("yyyy/MM/dd").format(new Date()), new SimpleDateFormat("hh:mm:ss").format(new Date()), System.getProperties().getProperty("user.name", "unknown"), Integer.toString(registry.getLevel()), "Java");
        }
        model.setParsedSystemName("unknown");
        model.setSourceDialect("1.4.x");
        return model;
    }
}
