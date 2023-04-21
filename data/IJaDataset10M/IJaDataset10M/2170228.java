package calclipse.mcomp.cntxt;

import calclipse.lib.math.mp.Defrag;
import calclipse.lib.math.mp.MPConfig;
import calclipse.lib.math.mp.MPConstants;
import calclipse.lib.math.mp.SymbolTable;
import calclipse.lib.math.rpn.RPNParserConfig;
import calclipse.lib.math.rpn.RPNParserStep;

/**
 * The parser configuration used by the context class.
 * @author T. Sommerland
 */
public class Config implements RPNParserConfig<Defrag<SymbolTable>> {

    private static final RPNParserConfig<Defrag<SymbolTable>> SUPER_CONFIG = new MPConfig();

    public Config() {
    }

    @Override
    public Defrag<SymbolTable> getScanner() {
        final Defrag<SymbolTable> defrag = SUPER_CONFIG.getScanner();
        final SymbolTable symTab = defrag.getScanner();
        symTab.add(Constants.DO);
        symTab.add(Constants.THEN);
        symTab.add(Constants.UNTIL);
        symTab.add(Constants.ELSE);
        symTab.add(Constants.ELIF);
        symTab.add(Constants.END);
        symTab.add(Constants.SEMICOLON);
        symTab.add(Constants.RETURN);
        symTab.add(Constants.WHILE);
        symTab.add(Constants.REPEAT);
        symTab.add(Constants.FOR);
        symTab.add(Constants.IF);
        symTab.add(Constants.WAIT);
        symTab.add(Constants.PRINT);
        symTab.add(Constants.PROMPT);
        symTab.add(Constants.EXECUTE);
        symTab.add(Constants.TRY);
        symTab.add(Constants.RAISE);
        symTab.add(Constants.EXIT);
        symTab.add(Constants.IMPORT);
        return defrag;
    }

    @Override
    public RPNParserStep[] getSteps() {
        return new RPNParserStep[] { MPConstants.ARG_PROCESSOR, MPConstants.INDEX_PROCESSOR, MPConstants.IMPLICIT_MULTIPLICATION, MPConstants.SIGN, Constants.IGNORE_LAST_SEMICOLON, MPConstants.CHECK, MPConstants.POSTFIX };
    }
}
