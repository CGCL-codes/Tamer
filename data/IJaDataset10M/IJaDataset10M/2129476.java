package net.sf.parser4j.parser.service;

import net.sf.parser4j.parser.entity.parsestate.IParseState;
import org.apache.log4j.Logger;

/**
 * 
 * @author luc peuvrier
 * 
 */
public final class ParserStackLogger {

    private static final Logger STACK_LOGGER = Logger.getLogger(ParserStackLogger.class);

    /** for each state define if it is token recognition */
    private boolean[] tokenRecognition;

    private boolean[] stringTokenRecognition;

    private final ParsingToStringUtil parsingToStringUtil;

    public ParserStackLogger(final ParsingToStringUtil parsingToStringUtil) {
        super();
        this.parsingToStringUtil = parsingToStringUtil;
    }

    /**
	 * @return
	 * @see org.apache.log4j.Category#isDebugEnabled()
	 */
    public boolean isDebugEnabled() {
        return STACK_LOGGER.isDebugEnabled();
    }

    /**
	 * @param tokenRecognition
	 *            the tokenRecognition to set
	 */
    public void setTokenRecognition(final boolean[] tokenRecognition) {
        this.tokenRecognition = tokenRecognition;
    }

    public void setStringTokenRecognition(boolean[] stringTokenRecognition) {
        this.stringTokenRecognition = stringTokenRecognition;
    }

    /**
	 * 
	 * @param title
	 * @param parseState
	 */
    public void logParseState(final String title, final IParseState parseState) {
        final StringBuilder builder = new StringBuilder();
        builder.append(title);
        builder.append('\n');
        if (parseState == null) {
            builder.append("null\n");
        } else {
            builder.append(parsingToStringUtil.parseStateToString(parseState, tokenRecognition, stringTokenRecognition, false, true));
        }
        STACK_LOGGER.debug(builder.toString());
    }
}
