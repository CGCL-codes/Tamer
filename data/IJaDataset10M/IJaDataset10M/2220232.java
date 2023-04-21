package backend.parser.gramene;

import java.io.File;
import org.apache.log4j.Level;
import backend.core.AbstractONDEXGraph;
import backend.core.security.Session;
import backend.event.type.StatisticalOutput;
import backend.param.args.ArgumentDefinition;
import backend.parser.AbstractONDEXParser;
import backend.parser.ParserArguments;

/**
 * Gramene Parser
 * 
 * Info: http://www.gramene.org/
 * ftp: ftp://ftp.gramene.org/pub/gramene/CURRENT_RELEASE/data/database_dump/mysql-dumps/
 * (downloading during daytime is slow at night the speed is a lot better)
 * 
 * 
 * @author hoekmanb
 */
public class Parser extends AbstractONDEXParser {

    private ParserArguments pa;

    /**
	 * Parser for GO.
	 * 
	 */
    public Parser(Session s) {
        super(s);
    }

    @Override
    public String getName() {
        return new String("GO");
    }

    @Override
    public ParserArguments getParserArguments() {
        return this.pa;
    }

    @Override
    public String getVersion() {
        return new String("02.05.2007");
    }

    @Override
    public ArgumentDefinition[] getArgumentDefinitions() {
        return new ArgumentDefinition[0];
    }

    @Override
    public void setONDEXGraph(AbstractONDEXGraph graph) {
        StatisticalOutput so = new StatisticalOutput("Starting gramene parsing...");
        so.setLog4jLevel(Level.INFO);
        fireEventOccurred(so);
        String inFilesDir = pa.getInputDir();
        File tempDir = new File(inFilesDir + File.separator + "tab" + File.separator);
        tempDir.mkdir();
        String tempDirLoc = tempDir.getAbsolutePath();
        GenesDBParser geneDBParser = new GenesDBParser(s, graph);
        geneDBParser.parseGenes(inFilesDir, tempDirLoc);
        so = new StatisticalOutput("Gramene parsing finished!");
        so.setLog4jLevel(Level.INFO);
        fireEventOccurred(so);
    }

    @Override
    public void setParserArguments(ParserArguments pa) {
        this.pa = pa;
    }
}
