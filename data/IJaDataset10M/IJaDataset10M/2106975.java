package org.deved.antlride.internal.ui.views;

import org.deved.antlride.core.parser.ast.AntlrGrammarDeclarationAST;
import org.deved.antlride.core.parser.ast.AntlrRuleDeclarationAST;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class AntlrSyntaxDiagram extends Canvas {

    private AntlrGrammarDeclarationAST fGrammarAST;

    private String fRuleName;

    public AntlrSyntaxDiagram(Composite parent) {
        super(parent, SWT.NONE);
        setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        addPaintListener(new SyntaxDiagramPainter());
    }

    public void update(String ruleName, AntlrGrammarDeclarationAST grammarDeclarationAST) {
        fRuleName = ruleName;
        fGrammarAST = grammarDeclarationAST;
        redraw();
    }

    public void paintSyntaxDiagram(PaintEvent e) {
        AntlrRuleDeclarationAST ruleAST = fGrammarAST.findRuleByName(fRuleName);
        if (ruleAST == null) return;
        Rectangle clientArea = getClientArea();
        Image image = new Image(getDisplay(), clientArea.width, clientArea.height);
        GC gc = new GC(image);
        AntlrSyntaxDiagramPainter painter = new AntlrSyntaxDiagramPainter(gc);
        painter.draw(ruleAST);
        gc.dispose();
        e.gc.drawImage(image, 0, 0);
        gc.dispose();
        e.gc.drawImage(image, 0, 0);
    }

    private class SyntaxDiagramPainter implements PaintListener {

        public void paintControl(PaintEvent e) {
            if (fGrammarAST != null) {
                paintSyntaxDiagram(e);
            }
        }
    }
}
