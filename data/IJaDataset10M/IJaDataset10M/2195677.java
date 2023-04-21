package com.itextpdf.tool.xml.parser;

import com.itextpdf.tool.xml.parser.state.*;

/**
 * Switches the current state in the {@link XMLParser}.
 * @author redlab_b
 *
 */
public class StateController {

    private final State unknown;

    private final State tagEncountered;

    private final State tagAttributes;

    private final State inTag;

    private final State attrValue;

    private final State singleQuoted;

    private final State doubleQuoted;

    private final State selfClosing;

    private final State specialChar;

    private final State closingTag;

    private final State comment;

    private final State closeComment;

    private final State cdata;

    private final State xml;

    private final State doctype;

    private final State unquoted;

    private final State processingInstruction;

    private final XMLParser parser;

    private State currentState;

    private State previousState;

    /**
	 * Constructs a StateController with the given parser.
	 * @param parser the Parser
	 * @param isHTML true if this parser is going to parse HTML, this results in different whitespace handling.
	 */
    public StateController(final XMLParser parser, boolean isHTML) {
        this.parser = parser;
        unknown = new UnknownState(parser);
        tagEncountered = new TagEncounteredState(parser);
        tagAttributes = new TagAttributeState(parser);
        inTag = (isHTML) ? new InsideTagHTMLState(parser) : new InsideTagState(parser);
        attrValue = new AttributeValueState(parser);
        singleQuoted = new SingleQuotedAttrValueState(parser);
        doubleQuoted = new DoubleQuotedAttrValueState(parser);
        selfClosing = new SelfClosingTagState(parser);
        specialChar = new SpecialCharState(parser);
        closingTag = new ClosingTagState(parser);
        comment = new CommentState(parser);
        closeComment = new CloseCommentState(parser);
        cdata = new CdataState(parser);
        xml = new XmlState(parser);
        doctype = new DocTypeState(parser);
        unquoted = new UnquotedAttrState(parser);
        processingInstruction = new ProcessingInstructionEncounteredState(parser);
        previousState = null;
        currentState = null;
    }

    /**
	 *	Changes the state.
	 * @param state the state to set as current state.
	 * @return the Parser
	 */
    public XMLParser setState(final State state) {
        previousState = currentState;
        currentState = state;
        parser.setState(state);
        return parser;
    }

    /**
	 * Returns to the previous state.
	 * @return Parser
	 */
    public XMLParser previousState() {
        parser.setState(previousState);
        return parser;
    }

    /**
	 * set Parser state to {@link UnknownState}.
	 * @return Parser
	 */
    public XMLParser unknown() {
        return setState(unknown);
    }

    /**
	 * set Parser state to {@link TagEncounteredState}.
	 * @return Parser
	 */
    public XMLParser tagEncountered() {
        return setState(tagEncountered);
    }

    /**
	 * set Parser state to {@link TagAttributeState}.
	 * @return Parser
	 */
    public XMLParser tagAttributes() {
        return setState(tagAttributes);
    }

    /**
	 * set Parser state to {@link InsideTagState}.
	 * @return Parser
	 */
    public XMLParser inTag() {
        return setState(inTag);
    }

    /**
	 * set Parser state to {@link AttributeValueState}.
	 * @return Parser
	 */
    public XMLParser attributeValue() {
        return setState(attrValue);
    }

    /**
	 * set Parser state to {@link SingleQuotedAttrValueState}.
	 * @return Parser
	 */
    public XMLParser singleQuotedAttr() {
        return setState(singleQuoted);
    }

    /**
	 * set Parser state to {@link DoubleQuotedAttrValueState}.
	 * @return Parser
	 */
    public XMLParser doubleQuotedAttr() {
        return setState(doubleQuoted);
    }

    /**
	 * set Parser state to {@link ProcessingInstructionEncounteredState}.
	 * @return Parser
	 */
    public XMLParser processingInstructions() {
        return setState(processingInstruction);
    }

    /**
	 * set Parser state to {@link SelfClosingTagState}.
	 * @return Parser
	 */
    public XMLParser selfClosing() {
        return setState(selfClosing);
    }

    /**
	 *set Parser state to {@link SpecialCharState}.
	 * @return Parser
	 */
    public XMLParser specialChar() {
        return setState(this.specialChar);
    }

    /**
	 * set Parser state to {@link ClosingTagState}.
	 * @return Parser
	 */
    public XMLParser closingTag() {
        return setState(this.closingTag);
    }

    /**
	 * set Parser state to {@link CommentState}.
	 * @return Parser
	 */
    public XMLParser comment() {
        return setState(this.comment);
    }

    /**
	 * set Parser state to {@link CloseCommentState}.
	 * @return Parser
	 */
    public XMLParser closeComment() {
        return setState(closeComment);
    }

    /**
	 * set Parser state to {@link CdataState}.
	 * @return Parser
	 */
    public XMLParser cdata() {
        return setState(cdata);
    }

    /**
	 * set Parser state to {@link DocTypeState}.
	 * @return Parser
	 */
    public XMLParser doctype() {
        return setState(doctype);
    }

    /**
	 * set Parser state to {@link UnquotedAttrState}.
	 * @return Parser
	 *
	 */
    public XMLParser unquotedAttr() {
        return setState(unquoted);
    }
}
