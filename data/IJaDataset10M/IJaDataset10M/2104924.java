package opt.base.specific;

import java.awt.Color;
import javax.swing.JFrame;
import opt.base.*;
import dtm.*;

public class OptionsStandard extends OptionGroup {

    private String[] databaseNames;

    public OptionsStandard(DataTypeManager dtMgr) {
        super("DEFAULT", "Standard Options");
        setDatatypeManager(dtMgr);
        databaseNames = dtMgr.getDBMSNames();
        initialize();
    }

    private void initialize() {
        addOption(new SingleStringOption("sqlCmdSeparator", "SQL Command Separator", ";"));
        addOption(new SingleBoolOption("PKFKinsideTable", "PK/FK Inside Create Table", "On = enclosed, off = Alter Table", true));
        addOption(new SingleBoolOption("GenerateIdentityColumns", "Generate Identity Columns", "On = Sequences, off = no sequences", false));
        addOption(new SingleBoolOption("GenFTMCompressed", "Generate Cross-Ref Compressed", "On = Compressed, off = not compressed", false));
        addOption(new SingleBoolOption("GenDropCommands", "Generate DROP Commands", "On = Drop [table,view,sequence], Off = Not generated", true));
        SingleComboBoxOption modelCBO = new SingleComboBoxOption("SourceModelClass", "Source Model Default Class", "Normalized");
        modelCBO.setStrings(",", "Normalized,ODS,Staging,Data Vault,Star Schema,Exploration Mart,Master Data Model");
        addOption(modelCBO);
        SingleComboBoxOption srcCBO = new SingleComboBoxOption("SourceDBMS", "Source DBMS Type", "Oracle");
        srcCBO.setStrings(databaseNames);
        addOption(srcCBO);
        SingleComboBoxOption tgtCBO = new SingleComboBoxOption("TargetDBMS", "Target DBMS Type", "Oracle");
        tgtCBO.setStrings(databaseNames);
        addOption(tgtCBO);
        SingleComboBoxOption conCBO = new SingleComboBoxOption("DDLConnectionType", "DDL Connection Type", "Flat File");
        conCBO.setStrings(",", "Flat File,Database");
        addOption(conCBO);
        SingleIntOption debugOption = new SingleIntOption("vdbg", "", 0, 0);
        debugOption.setIsWritable(false);
        addOption(debugOption);
        SingleBoolOption nkeyOpt = new SingleBoolOption("UseNaturalKeys", "", false);
        nkeyOpt.setIsWritable(false);
        addOption(nkeyOpt);
        addOption(new SingleStringOption("FontName", "", "Monospaced"));
        addOption(new SingleIntOption("FontSize", "", 12, 12));
        addOption(new SingleColorOption("FontColor", Color.black, Color.black));
        addOption(new SingleIntOption("WindowX", "", 10, 10));
        addOption(new SingleIntOption("WindowY", "", 10, 10));
        addOption(new SingleIntOption("WindowHeight", "", 500, 500));
        addOption(new SingleIntOption("WindowWidth", "", 600, 600));
        addOption(new SingleIntOption("vDividerPosition", "", 100, 100));
        addOption(new SingleIntOption("hDividerPosition", "", 200, 200));
        addOption(new SingleIntOption("winExtendedOpts", "", JFrame.NORMAL, JFrame.NORMAL));
    }
}
