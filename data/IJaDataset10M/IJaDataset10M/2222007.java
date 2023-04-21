package org.ufacekit.ui.core.styling.usac;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;
import org.ufacekit.ui.core.styles.DynamicStyleSelection;
import org.ufacekit.ui.core.styles.DynamicStyleSelectionFactory;
import org.ufacekit.ui.core.styles.StyleEngine;
import org.ufacekit.ui.core.styles.StylePropertyResolver;
import org.ufacekit.ui.core.styles.UIStyle;
import org.ufacekit.ui.core.styles.DynamicStyleSelectionFactory.Attr;
import org.ufacekit.ui.core.styles.StyleEngine.Parser;
import org.ufacekit.ui.core.styles.internal.InternalUIStyle;
import org.ufacekit.ui.core.util.ColorUtils;
import org.w3c.css.sac.AttributeCondition;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CombinatorCondition;
import org.w3c.css.sac.Condition;
import org.w3c.css.sac.ConditionalSelector;
import org.w3c.css.sac.DocumentHandler;
import org.w3c.css.sac.ElementSelector;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.Selector;
import org.w3c.css.sac.SelectorList;
import org.w3c.css.sac.SimpleSelector;
import org.w3c.css.sac.helpers.ParserFactory;

/**
 * SAC Parser of UFace Style {@link Parser}.
 * 
 */
public class USACParser extends Parser implements DocumentHandler {

    protected List<UIStyle> styles = new ArrayList<UIStyle>();

    private Stack<InternalUIStyle> nodeStack;

    private InternalUIStyle EMPTY_STYLE = new InternalUIStyle(InternalUIStyle.CUSTOM_STYLE, null, null, null, null);

    private DynamicStyleSelectionFactory factory;

    private StylePropertyResolver propertyResolver;

    public USACParser(StyleEngine styleEngine) {
        this(styleEngine, styleEngine);
    }

    public USACParser(DynamicStyleSelectionFactory factory, StylePropertyResolver propertyResolver) {
        this.factory = factory;
        this.propertyResolver = propertyResolver;
    }

    @Override
    public Collection<UIStyle> parse(String value) throws Exception {
        ParserFactory parserFactory = SACParserFactory.newInstance();
        org.w3c.css.sac.Parser parser = parserFactory.makeParser();
        parser.setDocumentHandler(this);
        InputSource source = new InputSource();
        source.setCharacterStream(new StringReader(value));
        parser.parseStyleSheet(source);
        return styles;
    }

    public void startDocument(InputSource arg0) throws CSSException {
        styles.clear();
        this.nodeStack = new Stack<InternalUIStyle>();
    }

    public void comment(String arg0) throws CSSException {
    }

    public void endDocument(InputSource arg0) throws CSSException {
    }

    public void endFontFace() throws CSSException {
    }

    public void endMedia(SACMediaList arg0) throws CSSException {
    }

    public void endPage(String arg0, String arg1) throws CSSException {
    }

    public void endSelector(SelectorList selectorList) throws CSSException {
        if (nodeStack.isEmpty()) nodeStack.pop();
    }

    public void ignorableAtRule(String arg0) throws CSSException {
    }

    public void importStyle(String arg0, SACMediaList arg1, String arg2) throws CSSException {
    }

    public void namespaceDeclaration(String arg0, String arg1) throws CSSException {
    }

    public void property(String name, LexicalUnit value, boolean important) throws CSSException {
        if (nodeStack.isEmpty()) return;
        InternalUIStyle style = nodeStack.peek();
        if (style.equals(EMPTY_STYLE)) return;
        int id = propertyResolver.propertyNameToId(name);
        if (id != -1) {
            Object propertyValue = null;
            LexicalUnit lu = null;
            switch(value.getLexicalUnitType()) {
                case LexicalUnit.SAC_RGBCOLOR:
                    lu = value.getParameters();
                    int red = lu.getIntegerValue();
                    lu = lu.getNextLexicalUnit().getNextLexicalUnit();
                    int green = lu.getIntegerValue();
                    lu = lu.getNextLexicalUnit().getNextLexicalUnit();
                    int blue = lu.getIntegerValue();
                    propertyValue = ColorUtils.getHexaColorStringValue(red, green, blue);
                    break;
                case LexicalUnit.SAC_POINT:
                    String dimensionText = value.getDimensionUnitText();
                    float f = value.getFloatValue();
                    propertyValue = f + dimensionText;
                    break;
                case LexicalUnit.SAC_IDENT:
                    propertyValue = value.getStringValue();
                    break;
            }
            if (propertyValue != null) {
                style.set(id, propertyValue);
            }
        }
    }

    public void startFontFace() throws CSSException {
    }

    public void startMedia(SACMediaList arg0) throws CSSException {
    }

    public void startPage(String arg0, String arg1) throws CSSException {
    }

    private int styleType = -1;

    private String id = null;

    private String className = null;

    private String elementName = null;

    private List<String> pseudos;

    private List<Attr> attributes = null;

    ;

    public void startSelector(SelectorList selectorList) throws CSSException {
        styleType = -1;
        id = null;
        className = null;
        elementName = null;
        pseudos = null;
        attributes = null;
        for (int i = 0; i < selectorList.getLength(); i++) {
            Selector selector = selectorList.item(i);
            applySelector(selector);
        }
        if (styleType != -1) {
            DynamicStyleSelection styleSelection = factory.createDynamicStyleSelection(pseudos, attributes);
            InternalUIStyle style = new InternalUIStyle(styleType, id, className, elementName, styleSelection);
            styles.add(style);
            nodeStack.push(style);
        } else {
            nodeStack.push(EMPTY_STYLE);
        }
    }

    private void applyCondition(Condition condition) {
        switch(condition.getConditionType()) {
            case Condition.SAC_CLASS_CONDITION:
                styleType = InternalUIStyle.CLASS_STYLE;
                className = getClass(condition);
                break;
            case Condition.SAC_PSEUDO_CLASS_CONDITION:
                String pseudoClass = getPseudoClass(condition);
                if (pseudos == null) {
                    pseudos = new ArrayList<String>();
                }
                pseudos.add(pseudoClass);
                break;
            case Condition.SAC_AND_CONDITION:
                CombinatorCondition combinatorCondion = (CombinatorCondition) condition;
                Condition firstCondition = combinatorCondion.getFirstCondition();
                applyCondition(firstCondition);
                Condition secondCondition = combinatorCondion.getSecondCondition();
                applyCondition(secondCondition);
                break;
            case Condition.SAC_ATTRIBUTE_CONDITION:
                AttributeCondition attributeCondition = (AttributeCondition) condition;
                String attributeName = attributeCondition.getLocalName();
                String attributeValue = attributeCondition.getValue();
                if (attributes == null) attributes = new ArrayList<Attr>();
                attributes.add(new Attr(attributeName, attributeValue));
                break;
        }
    }

    private void applySelector(Selector selector) {
        switch(selector.getSelectorType()) {
            case Selector.SAC_CONDITIONAL_SELECTOR:
                ConditionalSelector conditionalSelector = (ConditionalSelector) selector;
                SimpleSelector simpleSelector = conditionalSelector.getSimpleSelector();
                Condition condition = conditionalSelector.getCondition();
                applySelector(simpleSelector);
                applyCondition(condition);
                break;
            case Selector.SAC_ELEMENT_NODE_SELECTOR:
                ElementSelector elementSelector = (ElementSelector) selector;
                elementName = elementSelector.getLocalName();
                if (elementName != null) styleType = InternalUIStyle.ELEMENT_STYLE;
                break;
        }
    }

    private String getClass(Condition condition) {
        if (condition.getConditionType() == Condition.SAC_CLASS_CONDITION) {
            AttributeCondition attributeCondion = (AttributeCondition) condition;
            return attributeCondion.getValue();
        }
        return null;
    }

    private String getPseudoClass(Condition condition) {
        if (condition.getConditionType() == Condition.SAC_PSEUDO_CLASS_CONDITION) {
            AttributeCondition attributeCondion = (AttributeCondition) condition;
            return attributeCondion.getValue();
        }
        return null;
    }
}
