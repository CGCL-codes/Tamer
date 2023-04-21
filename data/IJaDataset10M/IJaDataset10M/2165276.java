package edu.cmu.cs.bungee.javaExtensions.psk.cmdline;

public class ApplicationSettings {

    static final int optDoNotIgnoreUnknown = 1;

    static final int optIgnoreUnknown = 2;

    public ApplicationSettings() {
        this(optDoNotIgnoreUnknown);
    }

    public ApplicationSettings(int aApplicationSettingsOptions) {
        m_argDescriptions = new java.util.Vector<Token>();
        m_flags = aApplicationSettingsOptions;
    }

    public void addToken(Token argum) {
        for (int i = 0; i < m_argDescriptions.size(); i++) {
            Token argDesc = m_argDescriptions.elementAt(i);
            if (argDesc.name().compareTo(argum.name()) == 0) {
                System.err.print("ApplicationSettings ERROR: option \"");
                System.err.print(argum.name());
                System.err.println("\"is used more than once");
                System.exit(-1);
            }
            if (!argDesc.isSwitch() && !argum.isSwitch()) {
                System.err.print("ApplicationSettings ERROR: arguments defined in both '");
                System.err.print(argum.name());
                System.err.print("' and '");
                System.err.println(argDesc.name() + "'");
                System.err.println("Arguments should be defined only once.");
                System.exit(-2);
            }
        }
        m_argDescriptions.addElement(argum);
    }

    public boolean parseArgs(String[] args) {
        m_cmdLineArgs = new StringArrayIterator(args);
        setEnvironmentValues();
        try {
            parseInternal();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void printUsage(String reason) throws Exception {
        System.err.print("Error: ");
        System.err.println(reason + "\n");
        System.err.print("Usage: ");
        System.err.print(m_programName + " ");
        for (int i = 0; i < m_argDescriptions.size(); i++) {
            Token arg = m_argDescriptions.elementAt(i);
            arg.printUsage(System.err);
        }
        System.err.println("\n");
        for (int i = 0; i < m_argDescriptions.size(); i++) {
            Token arg = m_argDescriptions.elementAt(i);
            arg.printUsageExtended(System.err);
        }
        throw new Exception(reason);
    }

    protected void parseNonSwitch() throws Exception {
        for (int i = 0; i < m_argDescriptions.size(); i++) {
            Token argDesc = m_argDescriptions.elementAt(i);
            if (!argDesc.parseArgument(m_cmdLineArgs)) continue;
            if (!m_cmdLineArgs.EOF()) {
                this.printUsage("too many commeand line arguments.");
            }
            return;
        }
        String str = "Unexepected argument ";
        str += m_cmdLineArgs.get();
        if (!ignoreUnknownSwitches()) this.printUsage(str);
    }

    protected void parseInternal() throws Exception {
        m_programName = "program";
        while (!m_cmdLineArgs.EOF()) {
            try {
                if (Token.isASwitch(m_cmdLineArgs.get())) {
                    this.parseSwitch();
                } else {
                    this.parseNonSwitch();
                    break;
                }
            } catch (Exception ex) {
                String str = ex.getMessage();
                if (ex.getClass() == NumberFormatException.class) {
                    str = str + " ";
                    str = str + " wrong argument type";
                }
                this.printUsage(str);
            }
            m_cmdLineArgs.moveNext();
        }
        for (int i = 0; i < m_argDescriptions.size(); i++) {
            Token argDesc = m_argDescriptions.elementAt(i);
            if (!argDesc.isUsed() && argDesc.isRequired()) {
                String str;
                str = "missing required argument. Name: ";
                str += argDesc.extendedName();
                this.printUsage(str);
            }
        }
    }

    protected void parseSwitch() throws Exception {
        int i = 0;
        for (i = 0; i < m_argDescriptions.size(); i++) {
            Token argDesc = m_argDescriptions.elementAt(i);
            if (argDesc.ParseSwitch(m_cmdLineArgs)) return;
        }
        if (i >= m_argDescriptions.size()) {
            String str = new String("Unknown option ");
            str += m_cmdLineArgs.get();
            if (!ignoreUnknownSwitches()) this.printUsage(str);
        }
    }

    protected void setEnvironmentValues() {
    }

    protected boolean ignoreUnknownSwitches() {
        return (m_flags & optIgnoreUnknown) != 0;
    }

    protected StringArrayIterator m_cmdLineArgs;

    protected String m_programName;

    protected java.util.Vector<Token> m_argDescriptions;

    protected int m_flags;
}
