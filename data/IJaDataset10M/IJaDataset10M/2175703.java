package com.nhncorp.cubridqa.editor.xmleditor;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.swt.SWT;

/**
 * 
 * The RuleBasedPartitionScanner of XML editor.
 * @ClassName: XMLScanner
 * @date 2009-9-7
 * @version V1.0 Copyright (C) www.nhn.com
 */
public class XMLScanner extends RuleBasedScanner {

    private TextAttribute keywords;

    private TextAttribute string;

    private TextAttribute object;

    private TextAttribute comment;

    public XMLScanner(ColorManager manager) {
        keywords = new TextAttribute(manager.getColor(IXMLColorConstants.TAG), null, SWT.BOLD);
        string = new TextAttribute(manager.getColor(IXMLColorConstants.STRING), null, SWT.BOLD);
        comment = new TextAttribute(manager.getColor(IXMLColorConstants.XML_COMMENT), null, SWT.BOLD);
        IToken procInstr = new Token(new TextAttribute(manager.getColor(IXMLColorConstants.PROC_INSTR)));
        List<IRule> l = new ArrayList();
        l.add(new SingleLineRule("<?", "?>", procInstr));
        l.add(new WhitespaceRule(new XMLWhitespaceDetector()));
        l.add(new MultiLineRule("<!--", "-->", new Token(comment)));
        l.add(new SingleLineRule("<", ">", new Token(keywords)));
        l.add(new MultiLineRule(">", "</", new Token(string)));
        setRules(l.toArray(new IRule[0]));
    }
}
