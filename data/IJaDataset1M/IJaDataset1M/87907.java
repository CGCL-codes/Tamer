package saf.analysis;

import java.util.*;
import saf.node.*;

public class ReversedDepthFirstAdapter extends AnalysisAdapter {

    public void inStart(Start node) {
        defaultIn(node);
    }

    public void outStart(Start node) {
        defaultOut(node);
    }

    public void defaultIn(@SuppressWarnings("unused") Node node) {
    }

    public void defaultOut(@SuppressWarnings("unused") Node node) {
    }

    @Override
    public void caseStart(Start node) {
        inStart(node);
        node.getEOF().apply(this);
        node.getPProgram().apply(this);
        outStart(node);
    }

    public void inAProgram(AProgram node) {
        defaultIn(node);
    }

    public void outAProgram(AProgram node) {
        defaultOut(node);
    }

    @Override
    public void caseAProgram(AProgram node) {
        inAProgram(node);
        if (node.getProgramFooter() != null) {
            node.getProgramFooter().apply(this);
        }
        if (node.getProgramBehaviour() != null) {
            node.getProgramBehaviour().apply(this);
        }
        if (node.getProgramPersonality() != null) {
            node.getProgramPersonality().apply(this);
        }
        if (node.getProgramHeader() != null) {
            node.getProgramHeader().apply(this);
        }
        outAProgram(node);
    }

    public void inAProgramHeader(AProgramHeader node) {
        defaultIn(node);
    }

    public void outAProgramHeader(AProgramHeader node) {
        defaultOut(node);
    }

    @Override
    public void caseAProgramHeader(AProgramHeader node) {
        inAProgramHeader(node);
        if (node.getLeftCurly() != null) {
            node.getLeftCurly().apply(this);
        }
        {
            List<TLetter> copy = new ArrayList<TLetter>(node.getLetter());
            Collections.reverse(copy);
            for (TLetter e : copy) {
                e.apply(this);
            }
        }
        outAProgramHeader(node);
    }

    public void inAProgramFooter(AProgramFooter node) {
        defaultIn(node);
    }

    public void outAProgramFooter(AProgramFooter node) {
        defaultOut(node);
    }

    @Override
    public void caseAProgramFooter(AProgramFooter node) {
        inAProgramFooter(node);
        if (node.getRightCurly() != null) {
            node.getRightCurly().apply(this);
        }
        outAProgramFooter(node);
    }

    public void inAProgramPersonality(AProgramPersonality node) {
        defaultIn(node);
    }

    public void outAProgramPersonality(AProgramPersonality node) {
        defaultOut(node);
    }

    @Override
    public void caseAProgramPersonality(AProgramPersonality node) {
        inAProgramPersonality(node);
        {
            List<PProgramCharacteristic> copy = new ArrayList<PProgramCharacteristic>(node.getProgramCharacteristic());
            Collections.reverse(copy);
            for (PProgramCharacteristic e : copy) {
                e.apply(this);
            }
        }
        outAProgramPersonality(node);
    }

    public void inAProgramCharacteristic(AProgramCharacteristic node) {
        defaultIn(node);
    }

    public void outAProgramCharacteristic(AProgramCharacteristic node) {
        defaultOut(node);
    }

    @Override
    public void caseAProgramCharacteristic(AProgramCharacteristic node) {
        inAProgramCharacteristic(node);
        if (node.getDigit() != null) {
            node.getDigit().apply(this);
        }
        if (node.getEquals() != null) {
            node.getEquals().apply(this);
        }
        {
            List<TLetter> copy = new ArrayList<TLetter>(node.getLetter());
            Collections.reverse(copy);
            for (TLetter e : copy) {
                e.apply(this);
            }
        }
        outAProgramCharacteristic(node);
    }

    public void inAProgramBehaviour(AProgramBehaviour node) {
        defaultIn(node);
    }

    public void outAProgramBehaviour(AProgramBehaviour node) {
        defaultOut(node);
    }

    @Override
    public void caseAProgramBehaviour(AProgramBehaviour node) {
        inAProgramBehaviour(node);
        {
            List<PProgramRule> copy = new ArrayList<PProgramRule>(node.getProgramRule());
            Collections.reverse(copy);
            for (PProgramRule e : copy) {
                e.apply(this);
            }
        }
        outAProgramBehaviour(node);
    }

    public void inAProgramRule(AProgramRule node) {
        defaultIn(node);
    }

    public void outAProgramRule(AProgramRule node) {
        defaultOut(node);
    }

    @Override
    public void caseAProgramRule(AProgramRule node) {
        inAProgramRule(node);
        if (node.getRightBracket() != null) {
            node.getRightBracket().apply(this);
        }
        if (node.getMove() != null) {
            node.getMove().apply(this);
        }
        if (node.getFight() != null) {
            node.getFight().apply(this);
        }
        if (node.getLeftBracket() != null) {
            node.getLeftBracket().apply(this);
        }
        {
            List<TLetter> copy = new ArrayList<TLetter>(node.getLetter());
            Collections.reverse(copy);
            for (TLetter e : copy) {
                e.apply(this);
            }
        }
        outAProgramRule(node);
    }

    public void inAProgramAction(AProgramAction node) {
        defaultIn(node);
    }

    public void outAProgramAction(AProgramAction node) {
        defaultOut(node);
    }

    @Override
    public void caseAProgramAction(AProgramAction node) {
        inAProgramAction(node);
        if (node.getRightParen() != null) {
            node.getRightParen().apply(this);
        }
        {
            List<TLetter> copy = new ArrayList<TLetter>(node.getActions());
            Collections.reverse(copy);
            for (TLetter e : copy) {
                e.apply(this);
            }
        }
        if (node.getLeftParen() != null) {
            node.getLeftParen().apply(this);
        }
        {
            List<TLetter> copy = new ArrayList<TLetter>(node.getChoose());
            Collections.reverse(copy);
            for (TLetter e : copy) {
                e.apply(this);
            }
        }
        outAProgramAction(node);
    }
}
