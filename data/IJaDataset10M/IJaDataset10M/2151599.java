package com.res.java.translation.engine;

import java.util.ArrayList;
import java.util.Stack;
import com.res.cobol.Main;
import com.res.common.RESConfig;
import com.res.java.lib.CobolSymbol;
import com.res.java.lib.Constants;
import com.res.java.lib.FieldFormat;
import com.res.java.lib.RunTimeUtil;
import com.res.java.translation.symbol.SymbolConstants;
import com.res.java.translation.symbol.SymbolProperties;
import com.res.java.translation.symbol.SymbolTable;
import com.res.java.translation.symbol.SymbolUtil;
import com.res.java.translation.symbol.Visitor;
import com.res.java.util.FieldAttributes;

public class CalculateSymbolLength implements Visitor {

    @Override
    public void visitPreprocess(SymbolProperties props) {
        if (Main.getContext().getTraceLevel() >= 2) {
            System.out.println("Doing CalculateSymbolLength symbol " + props.getDataName());
        }
        if (props.isOccurs() && props.getType() != SymbolConstants.PROGRAM) {
            if (props.getLevelNumber() == 1 || props.getLevelNumber() == 66 || props.getLevelNumber() == 88 || props.getLevelNumber() == 77) props.setOccurs(false); else {
                subscriptParents.push(props);
            }
        }
        if (props.getRedefines() != null) {
            if (props.isGroupData()) {
                offset.push(props.getRedefines().getOffset());
                unAdjustedOffset.push(props.getRedefines().getUnAdjustedOffset());
            }
        }
        if (props.getType() != SymbolConstants.PROGRAM) props.setAParentInOccurs(props.getParent().isOccurs() || props.getParent().isAParentInOccurs());
        if (props.is01Group() && props.hasRenames()) {
            alwaysCobolBytes = true;
        }
        if (props.getDataUsage() == CobolSymbol.INDEX) {
            props.setDataUsage((short) CobolSymbol.BINARY);
            isIndexSetToBinary = true;
        } else isIndexSetToBinary = false;
    }

    private boolean isIndexSetToBinary = false;

    @Override
    public void visitPostprocess(SymbolProperties props) {
        if (alwaysNoFormat) props.setIsFormat(false);
        if (alwaysCobolBytes) props.setUsedNativeJavaTypes(false); else if (props.getType() != SymbolConstants.PROGRAM && RESConfig.getInstance().getOptimizeAlgorithm() == 1) ;
        if (props.isOccurs()) subscriptParents.pop();
        if (props.getType() == SymbolConstants.PROGRAM || props.is01Group()) {
            if (props.is01Group() && props.getDataName().equalsIgnoreCase("SQLCA")) {
                props.setLength(100);
            }
            offset.pop();
            unAdjustedOffset.pop();
        }
        if (props.getRedefines() != null) {
            if (props.getJavaName2().equalsIgnoreCase("Grp_1seqRecord_3b")) System.out.println("Grp_1seqRecord_3b");
            int maxLen = props.getLength();
            int maxAdjLen = props.getAdjustedLength();
            SymbolProperties props2 = props;
            while (props2.getRedefines() != null) {
                maxLen = Math.max(maxLen, props2.getRedefines().getLength());
                maxAdjLen = Math.max(maxAdjLen, props2.getRedefines().getAdjustedLength());
                if (props2.getLength() > props2.getRedefines().getLength()) {
                    if (props.getParent().isIndexedFile() && props.getRedefines().getParent().isIndexedFile()) {
                        if (props.getParent().getDataName().equalsIgnoreCase(props.getRedefines().getParent().getDataName())) {
                            props.setIndexedFileRecord(true);
                            props.getRedefines().setIndexedFileRecord(false);
                        } else {
                            props.getRedefines().setIndexedFileRecord(true);
                        }
                    }
                } else {
                    if (props2.getParent().isIndexedFile() && props2.getRedefines().getParent().isIndexedFile()) {
                        if (props2.getParent().getDataName().equalsIgnoreCase(props2.getRedefines().getParent().getDataName())) {
                            props2.setIndexedFileRecord(false);
                            props2.getRedefines().setIndexedFileRecord(true);
                        } else {
                            props2.setIndexedFileRecord(true);
                        }
                    }
                }
                props2 = props2.getRedefines();
            }
            props2 = props;
            props2.setLength(maxLen);
            props2.setAdjustedLength(maxAdjLen);
            if (props2.getRedefinedBy() != null) {
                for (SymbolProperties r : props2.getRedefinedBy()) {
                    r.setLength(maxLen);
                    r.setAdjustedLength(maxAdjLen);
                }
            }
            while (props2.getRedefines() != null) {
                props2.getRedefines().setLength(maxLen);
                props2.getRedefines().setAdjustedLength(maxAdjLen);
                props2 = props2.getRedefines();
                if (props2.getRedefinedBy() != null) {
                    for (SymbolProperties r : props2.getRedefinedBy()) {
                        r.setLength(maxLen);
                        r.setAdjustedLength(maxAdjLen);
                    }
                }
            }
        } else {
            if (props.getParent() != null && props.getParent().isIndexedFile()) {
                props.setIndexedFileRecord(true);
            }
        }
        if (props.is01Group() && props.hasRenames()) {
            alwaysCobolBytes = false;
        }
        if (isIndexSetToBinary && props.getDataUsage() == Constants.BINARY) props.setDataUsage((short) Constants.INDEX);
    }

    private boolean alwaysCobolBytes = false;

    private boolean alwaysNoFormat = false;

    @Override
    public void visitChildPreprocess(SymbolProperties props) {
    }

    @Override
    public void visitChildPostprocess(SymbolProperties props) {
    }

    @Override
    public void visit01Element(SymbolProperties props) {
        calculateElementLength(props);
    }

    @Override
    public void visit01Group(SymbolProperties props) {
        if (props.getDataName().equalsIgnoreCase("sqlca")) alwaysNoFormat = true;
        calculateGroupLength(props);
        alwaysNoFormat = false;
    }

    @Override
    public void visit77Element(SymbolProperties props) {
        calculateElementLength(props);
    }

    @Override
    public void visit88Element(SymbolProperties props) {
        props.setJavaType(new CobolSymbol());
        props.getJavaType().type = props.getParent().getJavaType().type;
    }

    @Override
    public void visitInnerElement(SymbolProperties props) {
        calculateElementLength(props);
    }

    @Override
    public void visitInnerGroup(SymbolProperties props) {
        calculateGroupLength(props);
    }

    @Override
    public void visitProgram(SymbolProperties props) {
        calculateGroupLength(props);
    }

    private static Stack<SymbolProperties> subscriptParents = new Stack<SymbolProperties>();

    private static Stack<Integer> offset = new Stack<Integer>();

    private static Stack<Integer> unAdjustedOffset = new Stack<Integer>();

    private static Stack<Integer> noLiveFillers = new Stack<Integer>();

    private void calculateGroupLength(SymbolProperties props) {
        int leng;
        props.setFormat(false);
        props.setJavaType(new CobolSymbol());
        props.getJavaType().type = (byte) CobolSymbol.GROUP;
        if (props.getLevelNumber() == 66) {
            processRenames(props);
            return;
        }
        if (props.getType() == SymbolConstants.PROGRAM || (props.getLevelNumber() == 1 && props.isGroupData())) {
            offset.push(0);
            unAdjustedOffset.push(0);
            noLiveFillers.push(0);
        }
        int prevOffset = unAdjustedOffset.peek();
        int prevAdjustedOffset = offset.peek();
        props.setOffset(offset.peek());
        props.setUnAdjustedOffset(unAdjustedOffset.peek());
        switch(RESConfig.getInstance().getOptimizeAlgorithm()) {
            case 0:
                props.setUsedNativeJavaTypes(false);
                break;
            case 1:
                props.setUsedNativeJavaTypes((props.getRef() || props.getMod()) && (props.getLevelNumber() == 1 || props.getType() == SymbolConstants.PROGRAM));
                props.setUsedNativeJavaTypes(props.isUsedNativeJavaTypes() && !alwaysCobolBytes);
                break;
            case 2:
                SymbolUtil.setCheckUseNativeJavaTypes(props, alwaysCobolBytes);
        }
        if (props.hasChildren()) {
            SymbolTable.visit(props.getChildren(), this);
        }
        if (props.isOccurs() || props.isAParentInOccurs()) {
            ArrayList<SymbolProperties> b = new ArrayList<SymbolProperties>();
            b.addAll(subscriptParents);
            props.setOccursParents(b);
            props.setNoOccursSubscripts(b.size());
        }
        leng = unAdjustedOffset.peek() - prevOffset;
        int adjustedLength = offset.peek() - prevAdjustedOffset;
        props.setLength(leng);
        props.setAdjustedLength(adjustedLength);
        if (props.isOccurs() && props.getMaxOccursInt() > 0) leng *= (props.getMaxOccursInt() - 1); else leng = 0;
        if (props.getRedefines() != null) {
            offset.pop();
            unAdjustedOffset.pop();
        } else {
            unAdjustedOffset.push(unAdjustedOffset.pop() + leng);
            if (!props.isUsedNativeJavaTypes() && adjustedLength > 0) {
                offset.push(offset.pop() + leng);
            }
        }
    }

    private void processRenames(SymbolProperties props) {
        SymbolProperties from = props.getRedefinedBy().get(0);
        if (from == null) RunTimeUtil.getInstance().reportError("Invalid RENAMES clause in symbol: " + props.getDataName(), true);
        SymbolProperties thru = from;
        if (props.getRedefinedBy().size() > 1) thru = props.getRedefinedBy().get(1);
        if (thru == null) RunTimeUtil.getInstance().reportError("Invalid RENAMES clause in symbol: " + props.getDataName(), true);
        if (thru.getUnAdjustedOffset() - from.getUnAdjustedOffset() + thru.getLength() <= 0) RunTimeUtil.getInstance().reportError("Invalid RENAMES clause in symbol: " + props.getDataName(), true);
        props.setOffset(from.getOffset());
        props.setUnAdjustedOffset(from.getUnAdjustedOffset());
        props.setAdjustedLength(thru.getUnAdjustedOffset() - from.getUnAdjustedOffset() + thru.getLength());
        props.setLength(thru.getUnAdjustedOffset() - from.getUnAdjustedOffset() + thru.getLength());
    }

    private void calculateElementLength(SymbolProperties props) {
        int leng;
        Boolean isSuppressed = (Boolean) props.getIsSuppressed();
        if (isSuppressed != null && isSuppressed) return;
        CobolSymbol sym = new CobolSymbol();
        sym.pic = (String) props.getPictureString();
        String u = sym.pic.toUpperCase();
        sym.usage = (byte) props.getDataUsage();
        if (FieldFormat.verifyCobolPicture(u) == CobolSymbol.BIGDECIMAL) {
            FieldAttributes.processDecimal(u, sym);
            props.setSigned(sym.isSigned);
        } else if (FieldFormat.verifyCobolPicture(u) == CobolSymbol.INTEGER) {
            FieldAttributes.processDecimal(u, sym);
            props.setSigned(sym.isSigned);
        } else {
            if (FieldFormat.verifyCobolPicture(u) == CobolSymbol.STRING) {
                FieldAttributes.processAlpha(u, sym);
            } else {
                SymbolUtil.getInstance().reportError("Error In Usage or Picture of: " + props.getDataName() + ((props.getParent() != null) ? " IN " + props.getParent().getDataName() : "") + ((props.getPictureString() != null) ? " PICTURE " + props.getPictureString() : ""));
            }
        }
        if (props.getDataUsage() == CobolSymbol.COMPUTATIONAL1) sym.type = CobolSymbol.FLOAT; else if (props.getDataUsage() == CobolSymbol.COMPUTATIONAL2) sym.type = CobolSymbol.DOUBLE;
        props.setJavaType(sym);
        leng = FieldAttributes.calculateLength(props);
        if (props.getLevelNumber() == 78) return;
        if (props.isOccurs() || props.isAParentInOccurs()) {
            ArrayList<SymbolProperties> b = new ArrayList<SymbolProperties>();
            b.addAll(subscriptParents);
            props.setOccursParents(b);
            props.setNoOccursSubscripts(b.size());
        }
        props.setLength(leng);
        boolean b = SymbolUtil.setCheckUseNativeJavaTypes(props, alwaysCobolBytes);
        if (props.getRedefines() == null) {
            props.setOffset(offset.peek());
            props.setUnAdjustedOffset(unAdjustedOffset.peek());
            if (props.isOccurs() && props.getMaxOccursInt() > 1) leng *= (props.getMaxOccursInt());
            if (b) {
                unAdjustedOffset.push(unAdjustedOffset.pop() + leng);
                leng = 0;
            } else {
                offset.push(offset.pop() + leng);
                unAdjustedOffset.push(unAdjustedOffset.pop() + leng);
            }
        } else {
            props.setOffset(props.getRedefines().getOffset());
            props.setUnAdjustedOffset(props.getRedefines().getUnAdjustedOffset());
            leng = Math.max(leng, props.getRedefines().getLength());
        }
        SymbolTable.visit(props.getChildren(), this);
        return;
    }

    @Override
    public void visitParagraph(SymbolProperties props) {
    }

    @Override
    public void visitSection(SymbolProperties props) {
    }

    @Override
    public void visitFile(SymbolProperties props) {
        alwaysCobolBytes = true;
        SymbolTable.visit(props.getChildren(), this);
        alwaysCobolBytes = false;
        if (props.hasChildren()) {
            for (SymbolProperties child : props.getChildren()) {
                props.setLength(Math.max(props.getLength(), child.getLength()));
            }
            if (props.getAdjustedLength() < props.getLength()) props.setAdjustedLength(props.getLength());
        }
    }
}
