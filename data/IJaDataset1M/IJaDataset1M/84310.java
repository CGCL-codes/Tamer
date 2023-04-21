package org.systemsbiology.apps.transitiongenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.systemsbiology.aebersoldlab.core.commandline.CommandLineArgument;
import org.systemsbiology.aebersoldlab.core.commandline.CommandLineArgumentException;
import org.systemsbiology.aebersoldlab.core.commandline.CommandLineOption;
import org.systemsbiology.aebersoldlab.core.commandline.ICommandLineArgument;
import org.systemsbiology.aebersoldlab.core.commandline.ICommandLineOption;
import org.systemsbiology.lib.commonobj.ModificationBean;
import org.systemsbiology.lib.commonobj.importdata.ModificationManager;

public class DecoyTransitionGeneratorAppArgument {

    public static final ICommandLineArgument<File> INPUT_FILE_ARG = new CommandLineArgument<File>("-INPUT_FILE", "Transition files in csv format", true) {

        @Override
        public File parse(String s) throws CommandLineArgumentException {
            File f;
            if (s.isEmpty()) throw new CommandLineArgumentException("transition files cannot be empty.");
            f = new File(s);
            if (!f.exists()) throw new CommandLineArgumentException("file " + s + " does not exist"); else if (!(f.isFile() && (s.toLowerCase().endsWith(".csv") || s.toLowerCase().endsWith(".abi")))) throw new CommandLineArgumentException(s + " is not a file"); else return f;
        }
    };

    public static final ICommandLineArgument<String> OUTPUT_FILE_ARG = new CommandLineArgument<String>("-OUTPUT_FILE", "output file location", true) {

        @Override
        public String parse(String s) throws CommandLineArgumentException {
            if (s.isEmpty()) throw new CommandLineArgumentException("output file cannot be empty.");
            return s;
        }
    };

    public static final ICommandLineOption<ITransitionParser> PARSER_FULL = new CommandLineOption<ITransitionParser>("full", "parser for full csv format", FullFormatTransitionParser.getInstance());

    private static final List<ICommandLineOption<ITransitionParser>> PARSER_OPTS = new ArrayList<ICommandLineOption<ITransitionParser>>();

    static {
        PARSER_OPTS.add(PARSER_FULL);
    }

    public static final ICommandLineArgument<ITransitionParser> PARSER_ARG = new CommandLineArgument<ITransitionParser>("-PARSER", "parser type", PARSER_FULL, PARSER_OPTS, false) {

        @Override
        public ITransitionParser parse(String str) throws CommandLineArgumentException {
            if (str.equalsIgnoreCase(PARSER_FULL.getName())) return PARSER_FULL.getVal(); else throw new CommandLineArgumentException("invalid option for parser arg:" + str);
        }
    };

    public static final ICommandLineOption<IDecoyAlgorithm> ALGORITHM_SIMPLE_OPT = new CommandLineOption<IDecoyAlgorithm>("simple", SimpleDecoyAlgorithm.ALGORITHM_NAME, SimpleDecoyAlgorithm.getInstance());

    public static final ICommandLineOption<IDecoyAlgorithm> ALGORITHM_SIMPLENOOVERLAP_OPT = new CommandLineOption<IDecoyAlgorithm>("simpleNoOverlap", SimpleNoOverlapDecoyAlgorithm.ALGORITHM_NAME, SimpleNoOverlapDecoyAlgorithm.getInstance());

    private static final List<ICommandLineOption<IDecoyAlgorithm>> ALGORITHM_OPTS = new ArrayList<ICommandLineOption<IDecoyAlgorithm>>();

    static {
        ALGORITHM_OPTS.add(ALGORITHM_SIMPLE_OPT);
        ALGORITHM_OPTS.add(ALGORITHM_SIMPLENOOVERLAP_OPT);
    }

    public static final ICommandLineArgument<IDecoyAlgorithm> ALGORITHM_ARG = new CommandLineArgument<IDecoyAlgorithm>("-ALGORITHM", "algorithm type", ALGORITHM_SIMPLE_OPT, ALGORITHM_OPTS, false) {

        @Override
        public IDecoyAlgorithm parse(String str) throws CommandLineArgumentException {
            if (str.equalsIgnoreCase(ALGORITHM_SIMPLE_OPT.getName())) return ALGORITHM_SIMPLE_OPT.getVal(); else if (str.equalsIgnoreCase(ALGORITHM_SIMPLENOOVERLAP_OPT.getName())) return ALGORITHM_SIMPLENOOVERLAP_OPT.getVal(); else throw new CommandLineArgumentException("invalid option for algorithm arg:" + str);
        }
    };

    public static final ICommandLineOption<ExportType> EXPORT_SIMPLE_OPT = new CommandLineOption<ExportType>("simple", "write result to readable file", ExportType.FULL);

    public static final ICommandLineOption<ExportType> EXPORT_ATAQS_OPT = new CommandLineOption<ExportType>("ataqs", "write result for ATAQS transition list generator step", ExportType.ATAQS);

    private static final List<ICommandLineOption<ExportType>> EXPORT_OPTS = new ArrayList<ICommandLineOption<ExportType>>();

    static {
        EXPORT_OPTS.add(EXPORT_SIMPLE_OPT);
        EXPORT_OPTS.add(EXPORT_ATAQS_OPT);
    }

    public static final ICommandLineArgument<ExportType> EXPORT_ARG = new CommandLineArgument<ExportType>("-WRITER", "writer type", EXPORT_SIMPLE_OPT, EXPORT_OPTS, false) {

        @Override
        public ExportType parse(String str) throws CommandLineArgumentException {
            if (str.equalsIgnoreCase(EXPORT_SIMPLE_OPT.getName())) return EXPORT_SIMPLE_OPT.getVal(); else if (str.equalsIgnoreCase(EXPORT_ATAQS_OPT.getName())) return EXPORT_ATAQS_OPT.getVal(); else throw new CommandLineArgumentException("invalid option for writer arg:" + str);
        }
    };

    public static final ICommandLineArgument<File> MODIFICATION_FILE_ARG = new CommandLineArgument<File>("-MOD_FILE", "Modification file in xml format", true) {

        @Override
        public File parse(String s) throws CommandLineArgumentException {
            if (s.isEmpty()) throw new CommandLineArgumentException("modification file cannot be empty.");
            File f = new File(s);
            if (!f.exists()) throw new CommandLineArgumentException("file " + s + " does not exist"); else if (!(f.isFile() && s.toLowerCase().endsWith(".xml"))) throw new CommandLineArgumentException(s + " is not a xml file");
            return f;
        }
    };

    public static final ICommandLineArgument<ModificationBean> TARGET_MODIFICIATION_ARG = new CommandLineArgument<ModificationBean>("-TARGET_MODIFICIATION", "generate target modification transition before run", new ArrayList<ICommandLineOption<ModificationBean>>(), false) {

        @Override
        public List<ModificationBean> parse(String[] str) throws CommandLineArgumentException {
            List<ModificationBean> targetModificationBeans = new ArrayList<ModificationBean>();
            Map<String, ModificationBean> modMap = ModificationManager.getModificationMap();
            for (String target_modification : str) {
                if (modMap.get(target_modification) != null) targetModificationBeans.add(modMap.get(target_modification)); else throw new CommandLineArgumentException(target_modification + " is not found in modification file");
            }
            return targetModificationBeans;
        }
    };

    public static final void updateTargetModificationOption(List<ModificationBean> beans) {
        for (ModificationBean bean : beans) {
            CommandLineOption<ModificationBean> option = new CommandLineOption<ModificationBean>(bean.getFullyQualifiedName(), bean.getName() + ",AverageMassDelta:" + bean.getAverageMassDelta(), bean);
            DecoyTransitionGeneratorAppArgument.TARGET_MODIFICIATION_ARG.getOptions().add(option);
        }
    }

    public static final ICommandLineArgument<Integer> NUM_DECOY_PEPTIDE_GENERATE_ARG = new CommandLineArgument<Integer>("-NUM_DECOY_PEPTIDE_GENERATE", "set number of random decoy peptide will be generated", false) {

        @Override
        public Integer parse(String str) throws CommandLineArgumentException {
            Integer i = null;
            try {
                i = Integer.parseInt(str);
            } catch (NumberFormatException e) {
                throw new CommandLineArgumentException(e);
            }
            return i;
        }
    };

    public static final ICommandLineArgument<String> HELP_ARG = new CommandLineArgument<String>("--help", "additional information", false);
}
