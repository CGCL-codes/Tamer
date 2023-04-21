package org.sableccsupport.sccparser.analysis;

import java.util.*;
import org.sableccsupport.sccparser.node.*;

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
        node.getPGrammar().apply(this);
        outStart(node);
    }

    public void inAGrammar(AGrammar node) {
        defaultIn(node);
    }

    public void outAGrammar(AGrammar node) {
        defaultOut(node);
    }

    @Override
    public void caseAGrammar(AGrammar node) {
        inAGrammar(node);
        if (node.getAst() != null) {
            node.getAst().apply(this);
        }
        if (node.getProductions() != null) {
            node.getProductions().apply(this);
        }
        if (node.getIgnTokens() != null) {
            node.getIgnTokens().apply(this);
        }
        if (node.getTokens() != null) {
            node.getTokens().apply(this);
        }
        if (node.getStates() != null) {
            node.getStates().apply(this);
        }
        if (node.getHelpers() != null) {
            node.getHelpers().apply(this);
        }
        if (node.getPackage() != null) {
            node.getPackage().apply(this);
        }
        outAGrammar(node);
    }

    public void inAPackage(APackage node) {
        defaultIn(node);
    }

    public void outAPackage(APackage node) {
        defaultOut(node);
    }

    @Override
    public void caseAPackage(APackage node) {
        inAPackage(node);
        if (node.getPkgName() != null) {
            node.getPkgName().apply(this);
        }
        if (node.getPackage() != null) {
            node.getPackage().apply(this);
        }
        outAPackage(node);
    }

    public void inAPkgName(APkgName node) {
        defaultIn(node);
    }

    public void outAPkgName(APkgName node) {
        defaultOut(node);
    }

    @Override
    public void caseAPkgName(APkgName node) {
        inAPkgName(node);
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        {
            List<PPkgNameTail> copy = new ArrayList<PPkgNameTail>(node.getPkgIds());
            Collections.reverse(copy);
            for (PPkgNameTail e : copy) {
                e.apply(this);
            }
        }
        if (node.getPkgId() != null) {
            node.getPkgId().apply(this);
        }
        outAPkgName(node);
    }

    public void inAPkgNameTail(APkgNameTail node) {
        defaultIn(node);
    }

    public void outAPkgNameTail(APkgNameTail node) {
        defaultOut(node);
    }

    @Override
    public void caseAPkgNameTail(APkgNameTail node) {
        inAPkgNameTail(node);
        if (node.getPkgId() != null) {
            node.getPkgId().apply(this);
        }
        if (node.getDot() != null) {
            node.getDot().apply(this);
        }
        outAPkgNameTail(node);
    }

    public void inAHelpers(AHelpers node) {
        defaultIn(node);
    }

    public void outAHelpers(AHelpers node) {
        defaultOut(node);
    }

    @Override
    public void caseAHelpers(AHelpers node) {
        inAHelpers(node);
        {
            List<PHelperDef> copy = new ArrayList<PHelperDef>(node.getHelperDefs());
            Collections.reverse(copy);
            for (PHelperDef e : copy) {
                e.apply(this);
            }
        }
        if (node.getHelpers() != null) {
            node.getHelpers().apply(this);
        }
        outAHelpers(node);
    }

    public void inAHelperDef(AHelperDef node) {
        defaultIn(node);
    }

    public void outAHelperDef(AHelperDef node) {
        defaultOut(node);
    }

    @Override
    public void caseAHelperDef(AHelperDef node) {
        inAHelperDef(node);
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        if (node.getRegExp() != null) {
            node.getRegExp().apply(this);
        }
        if (node.getEqual() != null) {
            node.getEqual().apply(this);
        }
        if (node.getId() != null) {
            node.getId().apply(this);
        }
        outAHelperDef(node);
    }

    public void inAStates(AStates node) {
        defaultIn(node);
    }

    public void outAStates(AStates node) {
        defaultOut(node);
    }

    @Override
    public void caseAStates(AStates node) {
        inAStates(node);
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        if (node.getIdList() != null) {
            node.getIdList().apply(this);
        }
        if (node.getStates() != null) {
            node.getStates().apply(this);
        }
        outAStates(node);
    }

    public void inAIdList(AIdList node) {
        defaultIn(node);
    }

    public void outAIdList(AIdList node) {
        defaultOut(node);
    }

    @Override
    public void caseAIdList(AIdList node) {
        inAIdList(node);
        {
            List<PIdListTail> copy = new ArrayList<PIdListTail>(node.getIds());
            Collections.reverse(copy);
            for (PIdListTail e : copy) {
                e.apply(this);
            }
        }
        if (node.getId() != null) {
            node.getId().apply(this);
        }
        outAIdList(node);
    }

    public void inAIdListTail(AIdListTail node) {
        defaultIn(node);
    }

    public void outAIdListTail(AIdListTail node) {
        defaultOut(node);
    }

    @Override
    public void caseAIdListTail(AIdListTail node) {
        inAIdListTail(node);
        if (node.getId() != null) {
            node.getId().apply(this);
        }
        if (node.getComma() != null) {
            node.getComma().apply(this);
        }
        outAIdListTail(node);
    }

    public void inATokens(ATokens node) {
        defaultIn(node);
    }

    public void outATokens(ATokens node) {
        defaultOut(node);
    }

    @Override
    public void caseATokens(ATokens node) {
        inATokens(node);
        {
            List<PTokenDef> copy = new ArrayList<PTokenDef>(node.getTokenDefs());
            Collections.reverse(copy);
            for (PTokenDef e : copy) {
                e.apply(this);
            }
        }
        if (node.getTokens() != null) {
            node.getTokens().apply(this);
        }
        outATokens(node);
    }

    public void inATokenDef(ATokenDef node) {
        defaultIn(node);
    }

    public void outATokenDef(ATokenDef node) {
        defaultOut(node);
    }

    @Override
    public void caseATokenDef(ATokenDef node) {
        inATokenDef(node);
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        if (node.getLookAhead() != null) {
            node.getLookAhead().apply(this);
        }
        if (node.getRegExp() != null) {
            node.getRegExp().apply(this);
        }
        if (node.getEqual() != null) {
            node.getEqual().apply(this);
        }
        if (node.getId() != null) {
            node.getId().apply(this);
        }
        if (node.getStateList() != null) {
            node.getStateList().apply(this);
        }
        outATokenDef(node);
    }

    public void inAStateList(AStateList node) {
        defaultIn(node);
    }

    public void outAStateList(AStateList node) {
        defaultOut(node);
    }

    @Override
    public void caseAStateList(AStateList node) {
        inAStateList(node);
        if (node.getRBrace() != null) {
            node.getRBrace().apply(this);
        }
        {
            List<PStateListTail> copy = new ArrayList<PStateListTail>(node.getStateLists());
            Collections.reverse(copy);
            for (PStateListTail e : copy) {
                e.apply(this);
            }
        }
        if (node.getTransition() != null) {
            node.getTransition().apply(this);
        }
        if (node.getId() != null) {
            node.getId().apply(this);
        }
        if (node.getLBrace() != null) {
            node.getLBrace().apply(this);
        }
        outAStateList(node);
    }

    public void inAStateListTail(AStateListTail node) {
        defaultIn(node);
    }

    public void outAStateListTail(AStateListTail node) {
        defaultOut(node);
    }

    @Override
    public void caseAStateListTail(AStateListTail node) {
        inAStateListTail(node);
        if (node.getTransition() != null) {
            node.getTransition().apply(this);
        }
        if (node.getId() != null) {
            node.getId().apply(this);
        }
        if (node.getComma() != null) {
            node.getComma().apply(this);
        }
        outAStateListTail(node);
    }

    public void inATransition(ATransition node) {
        defaultIn(node);
    }

    public void outATransition(ATransition node) {
        defaultOut(node);
    }

    @Override
    public void caseATransition(ATransition node) {
        inATransition(node);
        if (node.getId() != null) {
            node.getId().apply(this);
        }
        if (node.getArrow() != null) {
            node.getArrow().apply(this);
        }
        outATransition(node);
    }

    public void inAIgnTokens(AIgnTokens node) {
        defaultIn(node);
    }

    public void outAIgnTokens(AIgnTokens node) {
        defaultOut(node);
    }

    @Override
    public void caseAIgnTokens(AIgnTokens node) {
        inAIgnTokens(node);
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        if (node.getIdList() != null) {
            node.getIdList().apply(this);
        }
        if (node.getTokens() != null) {
            node.getTokens().apply(this);
        }
        if (node.getIgnored() != null) {
            node.getIgnored().apply(this);
        }
        outAIgnTokens(node);
    }

    public void inALookAhead(ALookAhead node) {
        defaultIn(node);
    }

    public void outALookAhead(ALookAhead node) {
        defaultOut(node);
    }

    @Override
    public void caseALookAhead(ALookAhead node) {
        inALookAhead(node);
        if (node.getRegExp() != null) {
            node.getRegExp().apply(this);
        }
        if (node.getSlash() != null) {
            node.getSlash().apply(this);
        }
        outALookAhead(node);
    }

    public void inARegExp(ARegExp node) {
        defaultIn(node);
    }

    public void outARegExp(ARegExp node) {
        defaultOut(node);
    }

    @Override
    public void caseARegExp(ARegExp node) {
        inARegExp(node);
        {
            List<PRegExpTail> copy = new ArrayList<PRegExpTail>(node.getConcats());
            Collections.reverse(copy);
            for (PRegExpTail e : copy) {
                e.apply(this);
            }
        }
        if (node.getConcat() != null) {
            node.getConcat().apply(this);
        }
        outARegExp(node);
    }

    public void inARegExpTail(ARegExpTail node) {
        defaultIn(node);
    }

    public void outARegExpTail(ARegExpTail node) {
        defaultOut(node);
    }

    @Override
    public void caseARegExpTail(ARegExpTail node) {
        inARegExpTail(node);
        if (node.getConcat() != null) {
            node.getConcat().apply(this);
        }
        if (node.getBar() != null) {
            node.getBar().apply(this);
        }
        outARegExpTail(node);
    }

    public void inAConcat(AConcat node) {
        defaultIn(node);
    }

    public void outAConcat(AConcat node) {
        defaultOut(node);
    }

    @Override
    public void caseAConcat(AConcat node) {
        inAConcat(node);
        {
            List<PUnExp> copy = new ArrayList<PUnExp>(node.getUnExps());
            Collections.reverse(copy);
            for (PUnExp e : copy) {
                e.apply(this);
            }
        }
        outAConcat(node);
    }

    public void inAUnExp(AUnExp node) {
        defaultIn(node);
    }

    public void outAUnExp(AUnExp node) {
        defaultOut(node);
    }

    @Override
    public void caseAUnExp(AUnExp node) {
        inAUnExp(node);
        if (node.getUnOp() != null) {
            node.getUnOp().apply(this);
        }
        if (node.getBasic() != null) {
            node.getBasic().apply(this);
        }
        outAUnExp(node);
    }

    public void inACharBasic(ACharBasic node) {
        defaultIn(node);
    }

    public void outACharBasic(ACharBasic node) {
        defaultOut(node);
    }

    @Override
    public void caseACharBasic(ACharBasic node) {
        inACharBasic(node);
        if (node.getChar() != null) {
            node.getChar().apply(this);
        }
        outACharBasic(node);
    }

    public void inASetBasic(ASetBasic node) {
        defaultIn(node);
    }

    public void outASetBasic(ASetBasic node) {
        defaultOut(node);
    }

    @Override
    public void caseASetBasic(ASetBasic node) {
        inASetBasic(node);
        if (node.getSet() != null) {
            node.getSet().apply(this);
        }
        outASetBasic(node);
    }

    public void inAStringBasic(AStringBasic node) {
        defaultIn(node);
    }

    public void outAStringBasic(AStringBasic node) {
        defaultOut(node);
    }

    @Override
    public void caseAStringBasic(AStringBasic node) {
        inAStringBasic(node);
        if (node.getString() != null) {
            node.getString().apply(this);
        }
        outAStringBasic(node);
    }

    public void inAIdBasic(AIdBasic node) {
        defaultIn(node);
    }

    public void outAIdBasic(AIdBasic node) {
        defaultOut(node);
    }

    @Override
    public void caseAIdBasic(AIdBasic node) {
        inAIdBasic(node);
        if (node.getId() != null) {
            node.getId().apply(this);
        }
        outAIdBasic(node);
    }

    public void inARegExpBasic(ARegExpBasic node) {
        defaultIn(node);
    }

    public void outARegExpBasic(ARegExpBasic node) {
        defaultOut(node);
    }

    @Override
    public void caseARegExpBasic(ARegExpBasic node) {
        inARegExpBasic(node);
        if (node.getRPar() != null) {
            node.getRPar().apply(this);
        }
        if (node.getRegExp() != null) {
            node.getRegExp().apply(this);
        }
        if (node.getLPar() != null) {
            node.getLPar().apply(this);
        }
        outARegExpBasic(node);
    }

    public void inACharChar(ACharChar node) {
        defaultIn(node);
    }

    public void outACharChar(ACharChar node) {
        defaultOut(node);
    }

    @Override
    public void caseACharChar(ACharChar node) {
        inACharChar(node);
        if (node.getChar() != null) {
            node.getChar().apply(this);
        }
        outACharChar(node);
    }

    public void inADecChar(ADecChar node) {
        defaultIn(node);
    }

    public void outADecChar(ADecChar node) {
        defaultOut(node);
    }

    @Override
    public void caseADecChar(ADecChar node) {
        inADecChar(node);
        if (node.getDecChar() != null) {
            node.getDecChar().apply(this);
        }
        outADecChar(node);
    }

    public void inAHexChar(AHexChar node) {
        defaultIn(node);
    }

    public void outAHexChar(AHexChar node) {
        defaultOut(node);
    }

    @Override
    public void caseAHexChar(AHexChar node) {
        inAHexChar(node);
        if (node.getHexChar() != null) {
            node.getHexChar().apply(this);
        }
        outAHexChar(node);
    }

    public void inAOperationSet(AOperationSet node) {
        defaultIn(node);
    }

    public void outAOperationSet(AOperationSet node) {
        defaultOut(node);
    }

    @Override
    public void caseAOperationSet(AOperationSet node) {
        inAOperationSet(node);
        if (node.getRBkt() != null) {
            node.getRBkt().apply(this);
        }
        if (node.getRight() != null) {
            node.getRight().apply(this);
        }
        if (node.getBinOp() != null) {
            node.getBinOp().apply(this);
        }
        if (node.getLeft() != null) {
            node.getLeft().apply(this);
        }
        if (node.getLBkt() != null) {
            node.getLBkt().apply(this);
        }
        outAOperationSet(node);
    }

    public void inAIntervalSet(AIntervalSet node) {
        defaultIn(node);
    }

    public void outAIntervalSet(AIntervalSet node) {
        defaultOut(node);
    }

    @Override
    public void caseAIntervalSet(AIntervalSet node) {
        inAIntervalSet(node);
        if (node.getRBkt() != null) {
            node.getRBkt().apply(this);
        }
        if (node.getRight() != null) {
            node.getRight().apply(this);
        }
        if (node.getDDot() != null) {
            node.getDDot().apply(this);
        }
        if (node.getLeft() != null) {
            node.getLeft().apply(this);
        }
        if (node.getLBkt() != null) {
            node.getLBkt().apply(this);
        }
        outAIntervalSet(node);
    }

    public void inAStarUnOp(AStarUnOp node) {
        defaultIn(node);
    }

    public void outAStarUnOp(AStarUnOp node) {
        defaultOut(node);
    }

    @Override
    public void caseAStarUnOp(AStarUnOp node) {
        inAStarUnOp(node);
        if (node.getStar() != null) {
            node.getStar().apply(this);
        }
        outAStarUnOp(node);
    }

    public void inAQMarkUnOp(AQMarkUnOp node) {
        defaultIn(node);
    }

    public void outAQMarkUnOp(AQMarkUnOp node) {
        defaultOut(node);
    }

    @Override
    public void caseAQMarkUnOp(AQMarkUnOp node) {
        inAQMarkUnOp(node);
        if (node.getQMark() != null) {
            node.getQMark().apply(this);
        }
        outAQMarkUnOp(node);
    }

    public void inAPlusUnOp(APlusUnOp node) {
        defaultIn(node);
    }

    public void outAPlusUnOp(APlusUnOp node) {
        defaultOut(node);
    }

    @Override
    public void caseAPlusUnOp(APlusUnOp node) {
        inAPlusUnOp(node);
        if (node.getPlus() != null) {
            node.getPlus().apply(this);
        }
        outAPlusUnOp(node);
    }

    public void inAPlusBinOp(APlusBinOp node) {
        defaultIn(node);
    }

    public void outAPlusBinOp(APlusBinOp node) {
        defaultOut(node);
    }

    @Override
    public void caseAPlusBinOp(APlusBinOp node) {
        inAPlusBinOp(node);
        if (node.getPlus() != null) {
            node.getPlus().apply(this);
        }
        outAPlusBinOp(node);
    }

    public void inAMinusBinOp(AMinusBinOp node) {
        defaultIn(node);
    }

    public void outAMinusBinOp(AMinusBinOp node) {
        defaultOut(node);
    }

    @Override
    public void caseAMinusBinOp(AMinusBinOp node) {
        inAMinusBinOp(node);
        if (node.getMinus() != null) {
            node.getMinus().apply(this);
        }
        outAMinusBinOp(node);
    }

    public void inAProductions(AProductions node) {
        defaultIn(node);
    }

    public void outAProductions(AProductions node) {
        defaultOut(node);
    }

    @Override
    public void caseAProductions(AProductions node) {
        inAProductions(node);
        {
            List<PProd> copy = new ArrayList<PProd>(node.getProds());
            Collections.reverse(copy);
            for (PProd e : copy) {
                e.apply(this);
            }
        }
        if (node.getProductions() != null) {
            node.getProductions().apply(this);
        }
        outAProductions(node);
    }

    public void inAProd(AProd node) {
        defaultIn(node);
    }

    public void outAProd(AProd node) {
        defaultOut(node);
    }

    @Override
    public void caseAProd(AProd node) {
        inAProd(node);
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        if (node.getAlts() != null) {
            node.getAlts().apply(this);
        }
        if (node.getEqual() != null) {
            node.getEqual().apply(this);
        }
        if (node.getProdTransform() != null) {
            node.getProdTransform().apply(this);
        }
        if (node.getId() != null) {
            node.getId().apply(this);
        }
        outAProd(node);
    }

    public void inAProdTransform(AProdTransform node) {
        defaultIn(node);
    }

    public void outAProdTransform(AProdTransform node) {
        defaultOut(node);
    }

    @Override
    public void caseAProdTransform(AProdTransform node) {
        inAProdTransform(node);
        if (node.getRBrace() != null) {
            node.getRBrace().apply(this);
        }
        {
            List<PElem> copy = new ArrayList<PElem>(node.getElems());
            Collections.reverse(copy);
            for (PElem e : copy) {
                e.apply(this);
            }
        }
        if (node.getArrow() != null) {
            node.getArrow().apply(this);
        }
        if (node.getLBrace() != null) {
            node.getLBrace().apply(this);
        }
        outAProdTransform(node);
    }

    public void inAAlts(AAlts node) {
        defaultIn(node);
    }

    public void outAAlts(AAlts node) {
        defaultOut(node);
    }

    @Override
    public void caseAAlts(AAlts node) {
        inAAlts(node);
        {
            List<PAltsTail> copy = new ArrayList<PAltsTail>(node.getAlts());
            Collections.reverse(copy);
            for (PAltsTail e : copy) {
                e.apply(this);
            }
        }
        if (node.getAlt() != null) {
            node.getAlt().apply(this);
        }
        outAAlts(node);
    }

    public void inAAltsTail(AAltsTail node) {
        defaultIn(node);
    }

    public void outAAltsTail(AAltsTail node) {
        defaultOut(node);
    }

    @Override
    public void caseAAltsTail(AAltsTail node) {
        inAAltsTail(node);
        if (node.getAlt() != null) {
            node.getAlt().apply(this);
        }
        if (node.getBar() != null) {
            node.getBar().apply(this);
        }
        outAAltsTail(node);
    }

    public void inAAlt(AAlt node) {
        defaultIn(node);
    }

    public void outAAlt(AAlt node) {
        defaultOut(node);
    }

    @Override
    public void caseAAlt(AAlt node) {
        inAAlt(node);
        if (node.getAltTransform() != null) {
            node.getAltTransform().apply(this);
        }
        {
            List<PElem> copy = new ArrayList<PElem>(node.getElems());
            Collections.reverse(copy);
            for (PElem e : copy) {
                e.apply(this);
            }
        }
        if (node.getAltName() != null) {
            node.getAltName().apply(this);
        }
        outAAlt(node);
    }

    public void inAAltTransform(AAltTransform node) {
        defaultIn(node);
    }

    public void outAAltTransform(AAltTransform node) {
        defaultOut(node);
    }

    @Override
    public void caseAAltTransform(AAltTransform node) {
        inAAltTransform(node);
        if (node.getRBrace() != null) {
            node.getRBrace().apply(this);
        }
        {
            List<PTerm> copy = new ArrayList<PTerm>(node.getTerms());
            Collections.reverse(copy);
            for (PTerm e : copy) {
                e.apply(this);
            }
        }
        if (node.getArrow() != null) {
            node.getArrow().apply(this);
        }
        if (node.getLBrace() != null) {
            node.getLBrace().apply(this);
        }
        outAAltTransform(node);
    }

    public void inANewTerm(ANewTerm node) {
        defaultIn(node);
    }

    public void outANewTerm(ANewTerm node) {
        defaultOut(node);
    }

    @Override
    public void caseANewTerm(ANewTerm node) {
        inANewTerm(node);
        if (node.getRPar() != null) {
            node.getRPar().apply(this);
        }
        if (node.getParams() != null) {
            node.getParams().apply(this);
        }
        if (node.getLPar() != null) {
            node.getLPar().apply(this);
        }
        if (node.getProdName() != null) {
            node.getProdName().apply(this);
        }
        if (node.getNew() != null) {
            node.getNew().apply(this);
        }
        outANewTerm(node);
    }

    public void inAListTerm(AListTerm node) {
        defaultIn(node);
    }

    public void outAListTerm(AListTerm node) {
        defaultOut(node);
    }

    @Override
    public void caseAListTerm(AListTerm node) {
        inAListTerm(node);
        if (node.getRBkt() != null) {
            node.getRBkt().apply(this);
        }
        if (node.getListOfListTerm() != null) {
            node.getListOfListTerm().apply(this);
        }
        if (node.getLBkt() != null) {
            node.getLBkt().apply(this);
        }
        outAListTerm(node);
    }

    public void inASimpleTerm(ASimpleTerm node) {
        defaultIn(node);
    }

    public void outASimpleTerm(ASimpleTerm node) {
        defaultOut(node);
    }

    @Override
    public void caseASimpleTerm(ASimpleTerm node) {
        inASimpleTerm(node);
        if (node.getSimpleTermTail() != null) {
            node.getSimpleTermTail().apply(this);
        }
        if (node.getId() != null) {
            node.getId().apply(this);
        }
        if (node.getSpecifier() != null) {
            node.getSpecifier().apply(this);
        }
        outASimpleTerm(node);
    }

    public void inANullTerm(ANullTerm node) {
        defaultIn(node);
    }

    public void outANullTerm(ANullTerm node) {
        defaultOut(node);
    }

    @Override
    public void caseANullTerm(ANullTerm node) {
        inANullTerm(node);
        if (node.getNull() != null) {
            node.getNull().apply(this);
        }
        outANullTerm(node);
    }

    public void inAListOfListTerm(AListOfListTerm node) {
        defaultIn(node);
    }

    public void outAListOfListTerm(AListOfListTerm node) {
        defaultOut(node);
    }

    @Override
    public void caseAListOfListTerm(AListOfListTerm node) {
        inAListOfListTerm(node);
        {
            List<PListTermTail> copy = new ArrayList<PListTermTail>(node.getListTerms());
            Collections.reverse(copy);
            for (PListTermTail e : copy) {
                e.apply(this);
            }
        }
        if (node.getListTerm() != null) {
            node.getListTerm().apply(this);
        }
        outAListOfListTerm(node);
    }

    public void inANewListTerm(ANewListTerm node) {
        defaultIn(node);
    }

    public void outANewListTerm(ANewListTerm node) {
        defaultOut(node);
    }

    @Override
    public void caseANewListTerm(ANewListTerm node) {
        inANewListTerm(node);
        if (node.getRPar() != null) {
            node.getRPar().apply(this);
        }
        if (node.getParams() != null) {
            node.getParams().apply(this);
        }
        if (node.getLPar() != null) {
            node.getLPar().apply(this);
        }
        if (node.getProdName() != null) {
            node.getProdName().apply(this);
        }
        if (node.getNew() != null) {
            node.getNew().apply(this);
        }
        outANewListTerm(node);
    }

    public void inASimpleListTerm(ASimpleListTerm node) {
        defaultIn(node);
    }

    public void outASimpleListTerm(ASimpleListTerm node) {
        defaultOut(node);
    }

    @Override
    public void caseASimpleListTerm(ASimpleListTerm node) {
        inASimpleListTerm(node);
        if (node.getSimpleTermTail() != null) {
            node.getSimpleTermTail().apply(this);
        }
        if (node.getId() != null) {
            node.getId().apply(this);
        }
        if (node.getSpecifier() != null) {
            node.getSpecifier().apply(this);
        }
        outASimpleListTerm(node);
    }

    public void inAListTermTail(AListTermTail node) {
        defaultIn(node);
    }

    public void outAListTermTail(AListTermTail node) {
        defaultOut(node);
    }

    @Override
    public void caseAListTermTail(AListTermTail node) {
        inAListTermTail(node);
        if (node.getListTerm() != null) {
            node.getListTerm().apply(this);
        }
        if (node.getComma() != null) {
            node.getComma().apply(this);
        }
        outAListTermTail(node);
    }

    public void inASimpleTermTail(ASimpleTermTail node) {
        defaultIn(node);
    }

    public void outASimpleTermTail(ASimpleTermTail node) {
        defaultOut(node);
    }

    @Override
    public void caseASimpleTermTail(ASimpleTermTail node) {
        inASimpleTermTail(node);
        if (node.getId() != null) {
            node.getId().apply(this);
        }
        if (node.getDot() != null) {
            node.getDot().apply(this);
        }
        outASimpleTermTail(node);
    }

    public void inAProdName(AProdName node) {
        defaultIn(node);
    }

    public void outAProdName(AProdName node) {
        defaultOut(node);
    }

    @Override
    public void caseAProdName(AProdName node) {
        inAProdName(node);
        if (node.getProdNameTail() != null) {
            node.getProdNameTail().apply(this);
        }
        if (node.getId() != null) {
            node.getId().apply(this);
        }
        outAProdName(node);
    }

    public void inAProdNameTail(AProdNameTail node) {
        defaultIn(node);
    }

    public void outAProdNameTail(AProdNameTail node) {
        defaultOut(node);
    }

    @Override
    public void caseAProdNameTail(AProdNameTail node) {
        inAProdNameTail(node);
        if (node.getId() != null) {
            node.getId().apply(this);
        }
        if (node.getDot() != null) {
            node.getDot().apply(this);
        }
        outAProdNameTail(node);
    }

    public void inAParams(AParams node) {
        defaultIn(node);
    }

    public void outAParams(AParams node) {
        defaultOut(node);
    }

    @Override
    public void caseAParams(AParams node) {
        inAParams(node);
        {
            List<PParamsTail> copy = new ArrayList<PParamsTail>(node.getParams());
            Collections.reverse(copy);
            for (PParamsTail e : copy) {
                e.apply(this);
            }
        }
        if (node.getTerm() != null) {
            node.getTerm().apply(this);
        }
        outAParams(node);
    }

    public void inAParamsTail(AParamsTail node) {
        defaultIn(node);
    }

    public void outAParamsTail(AParamsTail node) {
        defaultOut(node);
    }

    @Override
    public void caseAParamsTail(AParamsTail node) {
        inAParamsTail(node);
        if (node.getTerm() != null) {
            node.getTerm().apply(this);
        }
        if (node.getComma() != null) {
            node.getComma().apply(this);
        }
        outAParamsTail(node);
    }

    public void inAAltName(AAltName node) {
        defaultIn(node);
    }

    public void outAAltName(AAltName node) {
        defaultOut(node);
    }

    @Override
    public void caseAAltName(AAltName node) {
        inAAltName(node);
        if (node.getRBrace() != null) {
            node.getRBrace().apply(this);
        }
        if (node.getId() != null) {
            node.getId().apply(this);
        }
        if (node.getLBrace() != null) {
            node.getLBrace().apply(this);
        }
        outAAltName(node);
    }

    public void inAElem(AElem node) {
        defaultIn(node);
    }

    public void outAElem(AElem node) {
        defaultOut(node);
    }

    @Override
    public void caseAElem(AElem node) {
        inAElem(node);
        if (node.getUnOp() != null) {
            node.getUnOp().apply(this);
        }
        if (node.getId() != null) {
            node.getId().apply(this);
        }
        if (node.getSpecifier() != null) {
            node.getSpecifier().apply(this);
        }
        if (node.getElemName() != null) {
            node.getElemName().apply(this);
        }
        outAElem(node);
    }

    public void inAElemName(AElemName node) {
        defaultIn(node);
    }

    public void outAElemName(AElemName node) {
        defaultOut(node);
    }

    @Override
    public void caseAElemName(AElemName node) {
        inAElemName(node);
        if (node.getColon() != null) {
            node.getColon().apply(this);
        }
        if (node.getRBkt() != null) {
            node.getRBkt().apply(this);
        }
        if (node.getId() != null) {
            node.getId().apply(this);
        }
        if (node.getLBkt() != null) {
            node.getLBkt().apply(this);
        }
        outAElemName(node);
    }

    public void inATokenSpecifier(ATokenSpecifier node) {
        defaultIn(node);
    }

    public void outATokenSpecifier(ATokenSpecifier node) {
        defaultOut(node);
    }

    @Override
    public void caseATokenSpecifier(ATokenSpecifier node) {
        inATokenSpecifier(node);
        if (node.getDot() != null) {
            node.getDot().apply(this);
        }
        if (node.getTokenSpecifier() != null) {
            node.getTokenSpecifier().apply(this);
        }
        outATokenSpecifier(node);
    }

    public void inAProductionSpecifier(AProductionSpecifier node) {
        defaultIn(node);
    }

    public void outAProductionSpecifier(AProductionSpecifier node) {
        defaultOut(node);
    }

    @Override
    public void caseAProductionSpecifier(AProductionSpecifier node) {
        inAProductionSpecifier(node);
        if (node.getDot() != null) {
            node.getDot().apply(this);
        }
        if (node.getProductionSpecifier() != null) {
            node.getProductionSpecifier().apply(this);
        }
        outAProductionSpecifier(node);
    }

    public void inAAst(AAst node) {
        defaultIn(node);
    }

    public void outAAst(AAst node) {
        defaultOut(node);
    }

    @Override
    public void caseAAst(AAst node) {
        inAAst(node);
        {
            List<PAstProd> copy = new ArrayList<PAstProd>(node.getProds());
            Collections.reverse(copy);
            for (PAstProd e : copy) {
                e.apply(this);
            }
        }
        if (node.getTree() != null) {
            node.getTree().apply(this);
        }
        if (node.getSyntax() != null) {
            node.getSyntax().apply(this);
        }
        if (node.getAbstract() != null) {
            node.getAbstract().apply(this);
        }
        outAAst(node);
    }

    public void inAAstProd(AAstProd node) {
        defaultIn(node);
    }

    public void outAAstProd(AAstProd node) {
        defaultOut(node);
    }

    @Override
    public void caseAAstProd(AAstProd node) {
        inAAstProd(node);
        if (node.getSemicolon() != null) {
            node.getSemicolon().apply(this);
        }
        if (node.getAlts() != null) {
            node.getAlts().apply(this);
        }
        if (node.getEqual() != null) {
            node.getEqual().apply(this);
        }
        if (node.getId() != null) {
            node.getId().apply(this);
        }
        outAAstProd(node);
    }

    public void inAAstAlts(AAstAlts node) {
        defaultIn(node);
    }

    public void outAAstAlts(AAstAlts node) {
        defaultOut(node);
    }

    @Override
    public void caseAAstAlts(AAstAlts node) {
        inAAstAlts(node);
        {
            List<PAstAltsTail> copy = new ArrayList<PAstAltsTail>(node.getAstAlts());
            Collections.reverse(copy);
            for (PAstAltsTail e : copy) {
                e.apply(this);
            }
        }
        if (node.getAstAlt() != null) {
            node.getAstAlt().apply(this);
        }
        outAAstAlts(node);
    }

    public void inAAstAltsTail(AAstAltsTail node) {
        defaultIn(node);
    }

    public void outAAstAltsTail(AAstAltsTail node) {
        defaultOut(node);
    }

    @Override
    public void caseAAstAltsTail(AAstAltsTail node) {
        inAAstAltsTail(node);
        if (node.getAstAlt() != null) {
            node.getAstAlt().apply(this);
        }
        if (node.getBar() != null) {
            node.getBar().apply(this);
        }
        outAAstAltsTail(node);
    }

    public void inAAstAlt(AAstAlt node) {
        defaultIn(node);
    }

    public void outAAstAlt(AAstAlt node) {
        defaultOut(node);
    }

    @Override
    public void caseAAstAlt(AAstAlt node) {
        inAAstAlt(node);
        {
            List<PElem> copy = new ArrayList<PElem>(node.getElems());
            Collections.reverse(copy);
            for (PElem e : copy) {
                e.apply(this);
            }
        }
        if (node.getAltName() != null) {
            node.getAltName().apply(this);
        }
        outAAstAlt(node);
    }
}
