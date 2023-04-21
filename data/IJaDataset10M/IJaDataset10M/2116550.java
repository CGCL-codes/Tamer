package basefbd;

import java.awt.Label;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import twoctable.ConditionInformationForTwoC;
import twoctable.CondsInformationforDraw;
import twoctable.GeneratePostfix;
import twoctable.TwoCInformationAutomata;
import y.view.Graph2D;
import y.view.NodeRealizer;
import blockInformation.CombinedSelBlock;
import blockInformation.GenericBlock;
import blockInformation.MoveBlock;
import blockInformation.MuxBlock;
import blockInformation.TwoInputBlock;

public class DrawBlocks {

    public Stack<Object> operatorStack = new Stack<Object>();

    Queue<CondsInformationforDraw> condsForDraw = new LinkedList<CondsInformationforDraw>();

    GeneratePostfix generatePostfix;

    Queue<TwoInputBlock> allblocks = new LinkedList<TwoInputBlock>();

    public int allBlocksCount = 1;

    public int rowcount = 0;

    public int totalRowCount = 0;

    ArrayList combinedSelBlocks = new ArrayList();

    String outputString = "";

    Graph2D graph_preFBD;

    Graph2D graph_outputFBD;

    Graph2D graph_stateFBD;

    Graph2D graph_totalFBD;

    public Queue<Label> labelCollection = new LinkedList<Label>();

    public Queue<String> outputsStatesForSel = new LinkedList<String>();

    boolean isSDT;

    public Graph2D graph;

    public boolean isSaving = false;

    public String savePath = "a.xml";

    public int localIdNumber = 1;

    public Document xmldoc;

    Element rootElement;

    public DrawBlocks(int localIdNumber, boolean isSaving, String savePath, Document doc, Element rootElement) {
        this.localIdNumber = localIdNumber;
        this.isSaving = isSaving;
        this.savePath = savePath;
        this.xmldoc = doc;
        this.rootElement = rootElement;
    }

    public void DrawPreProcessingBlock(Queue<CondInformation> condsQueueLast, Graph2D graph_preFBD, int totalRowCount, Graph2D graph_totalFBD, int allBlocksCount) {
        this.graph_preFBD = graph_preFBD;
        this.allBlocksCount = allBlocksCount;
        if (graph_totalFBD != null) {
            this.totalRowCount = totalRowCount;
            this.graph_totalFBD = graph_totalFBD;
        }
        for (CondInformation conds : condsQueueLast) {
            generatePostfix = new GeneratePostfix();
            CondsInformationforDraw c = new CondsInformationforDraw();
            c.name = conds.name;
            c.content = conds.content;
            generatePostfix.CreatePostfix(c.content);
            c.postfix = generatePostfix.postfix;
            condsForDraw.add(c);
        }
        if (graph_totalFBD == null) {
            SetBlockByPostfix(graph_preFBD);
            DrawBlocksByPostfix(graph_preFBD);
        } else {
            SetBlockByPostfix(graph_totalFBD);
            DrawBlocksByPostfix(graph_totalFBD);
        }
    }

    public void DrawOutputProcessingBlock(ArrayList<CombinedSelBlock> combinedSelBlocks, Graph2D graph_outputFBD, String outputString, int totalRowCount, Graph2D graph_totalFBD, int allBlocksCount) {
        this.outputString = outputString;
        isSDT = true;
        rowcount = 0;
        this.graph_outputFBD = graph_outputFBD;
        this.allBlocksCount = allBlocksCount;
        String s = "";
        if (graph_totalFBD != null) {
            this.totalRowCount = totalRowCount;
            this.graph_totalFBD = graph_totalFBD;
        }
        this.combinedSelBlocks = combinedSelBlocks;
        ArrayList<CombinedSelBlock> temp = new ArrayList<CombinedSelBlock>();
        for (int i = 0; i < combinedSelBlocks.size(); i++) temp.add(combinedSelBlocks.get(combinedSelBlocks.size() - i - 1));
        combinedSelBlocks = temp;
        for (CombinedSelBlock combined : combinedSelBlocks) {
            for (String condition : combined.conditions) s += condition + "|";
            s = s.substring(0, s.length() - 1);
            s += "#";
        }
        s = s.substring(0, s.length() - 1);
        if (s.charAt(s.length() - 1) == '|') s = s.substring(0, s.length() - 2);
        generatePostfix = new GeneratePostfix();
        CondsInformationforDraw c = new CondsInformationforDraw();
        c.name = "";
        c.content = s;
        generatePostfix.CreatePostfix(c.content);
        c.postfix = generatePostfix.postfix;
        condsForDraw.add(c);
        if (graph_totalFBD == null) {
            SetBlockByPostfix(graph_outputFBD);
            DrawBlocksByPostfix(graph_outputFBD);
        } else {
            SetBlockByPostfix(graph_totalFBD);
            DrawBlocksByPostfix(graph_totalFBD);
        }
    }

    public void DrawOutputProcessingBlock(String[] combinedStringForFBD, TwoCInformationAutomata twoCInformationAutomata, Graph2D graph_outputFBD, String outputString, int totalRowCount, Graph2D graph_totalFBD, int allBlocksCount, Queue<String> outputsForSel) {
        this.outputString = outputString;
        isSDT = false;
        rowcount = 0;
        this.graph_outputFBD = graph_outputFBD;
        this.allBlocksCount = allBlocksCount;
        if (outputsForSel != null) this.outputsStatesForSel = outputsForSel;
        int count = 0;
        if (graph_totalFBD != null) {
            this.totalRowCount = totalRowCount;
            this.graph_totalFBD = graph_totalFBD;
        }
        for (ConditionInformationForTwoC conditionInfos : twoCInformationAutomata.conditions) {
            String s = "";
            for (String[] conditionInformations : conditionInfos.conditionContents) {
                s += "(" + combinedStringForFBD[count++] + ")#";
            }
            s = s.substring(0, s.length() - 1);
            s = s.substring(0, s.lastIndexOf('#') + 1);
            s += "others's output";
            generatePostfix = new GeneratePostfix();
            CondsInformationforDraw c = new CondsInformationforDraw();
            c.name = "";
            c.content = s;
            generatePostfix.CreatePostfix(s);
            c.postfix = generatePostfix.postfix;
            if (c.postfix.size() == 1) {
                c.postfix.add("error");
                c.postfix.add("&");
            }
            condsForDraw.add(c);
        }
        if (graph_totalFBD == null) {
            SetBlockByPostfix(graph_outputFBD);
            DrawBlocksByPostfix(graph_outputFBD);
        } else {
            SetBlockByPostfix(graph_totalFBD);
            DrawBlocksByPostfix(graph_totalFBD);
        }
    }

    public void DrawStateProcessingBlock(String[] combinedStringForFBD, TwoCInformationAutomata twoCInformationAutomata, Graph2D graph_stateFBD, int totalRowCount, Graph2D graph_totalFBD, int allBlocksCount, Queue<String> statesForSel) {
        isSDT = false;
        rowcount = 0;
        this.graph_stateFBD = graph_stateFBD;
        this.allBlocksCount = allBlocksCount;
        if (statesForSel != null) this.outputsStatesForSel = statesForSel;
        int count = 0;
        if (graph_totalFBD != null) {
            this.totalRowCount = totalRowCount;
            this.graph_totalFBD = graph_totalFBD;
        }
        for (ConditionInformationForTwoC conditionInfos : twoCInformationAutomata.conditions) {
            String s = "";
            for (String[] conditionInformations : conditionInfos.conditionContents) {
                s += "(" + combinedStringForFBD[count++] + ")#";
            }
            s = s.substring(0, s.length() - 1);
            s = s.substring(0, s.lastIndexOf('#') + 1);
            s += "others's state";
            generatePostfix = new GeneratePostfix();
            CondsInformationforDraw c = new CondsInformationforDraw();
            c.name = "";
            c.content = s;
            generatePostfix.CreatePostfix(s);
            c.postfix = generatePostfix.postfix;
            if (c.postfix.size() == 1) {
                c.postfix.add("error");
                c.postfix.add("&");
            }
            condsForDraw.add(c);
        }
        if (graph_totalFBD == null) {
            SetBlockByPostfix(graph_stateFBD);
            DrawBlocksByPostfix(graph_stateFBD);
        } else {
            SetBlockByPostfix(graph_totalFBD);
            DrawBlocksByPostfix(graph_totalFBD);
        }
    }

    public void SetBlockByPostfix(Graph2D graph) {
        try {
            operatorStack = new Stack<Object>();
            for (CondsInformationforDraw conds : condsForDraw) {
                for (String content : conds.postfix) {
                    if (generatePostfix.IsOperator(content)) LinkingBlock(content, graph); else operatorStack.push(content);
                }
                NodeRealizer nr = graph.getRealizer(((TwoInputBlock) operatorStack.peek()).node_output);
                nr.getLabel().setText(conds.name);
                Numbering((TwoInputBlock) operatorStack.peek(), graph);
            }
        } catch (Exception e) {
        }
    }

    public void Numbering(TwoInputBlock block, Graph2D graph) {
        block.leafCount = rowcount;
        if (block.inputUpObject instanceof String && block.inputDownObject instanceof String) rowcount++;
        if (block.outputObject instanceof TwoInputBlock) block.level = ((TwoInputBlock) block.outputObject).level - 1;
        NodeRealizer nr = graph.getRealizer(block.node_Body);
        if (nr.getLabel().getText().equals("    SEL_")) {
            if (combinedSelBlocks.size() != 0) {
                block.SetSelLocation(new Point(block.level * 340 + 250, (block.leafCount + totalRowCount) * 130 + 20), ((CombinedSelBlock) combinedSelBlocks.get(0)).output.replace("\n", ""), graph);
                CombinedSelBlock combiendSelBlock = (CombinedSelBlock) combinedSelBlocks.get(0);
                combinedSelBlocks.remove(0);
                combinedSelBlocks.add(combiendSelBlock);
                nr = graph.getRealizer(block.node_inputDown);
                if (nr.getLabel().getText().equals("") == false) {
                    nr.getLabel().setText(((CombinedSelBlock) combinedSelBlocks.get(0)).output.replace("\n", ""));
                    CombinedSelBlock combiendSelBlock1 = (CombinedSelBlock) combinedSelBlocks.get(0);
                    combinedSelBlocks.remove(0);
                    combinedSelBlocks.add(combiendSelBlock1);
                }
            }
            if (outputsStatesForSel.size() != 0) {
                String outputState = "";
                outputState = outputsStatesForSel.remove();
                block.SetSelLocation(new Point(block.level * 340 + 250, (block.leafCount + totalRowCount) * 130 + 20), outputState.replace("\n", ""), graph);
                outputsStatesForSel.add(outputState);
                nr = graph.getRealizer(block.node_inputDown);
                if (nr.getLabel().getText().equals("") == false) {
                    outputState = outputsStatesForSel.remove();
                    nr.getLabel().setText(outputState.replace("\n", ""));
                    outputsStatesForSel.add(outputState);
                }
            }
        }
        if (block.inputUpObject instanceof TwoInputBlock) Numbering((TwoInputBlock) block.inputUpObject, graph);
        if (block.inputDownObject instanceof TwoInputBlock) Numbering((TwoInputBlock) block.inputDownObject, graph);
    }

    public void LinkingBlock(String op, Graph2D graph) {
        if (op.equals("|")) {
            MakeBlock("OR2_BOOL", graph);
        } else if (op.equals("&")) {
            MakeBlock("AND2_BOOL", graph);
        } else if (op.equals("<")) {
            MakeBlock2("GT_", graph);
        } else if (op.equals(">")) {
            MakeBlock2("LT_", graph);
        } else if (op.equals("<=")) {
            MakeBlock2("GE_", graph);
        } else if (op.equals(">=")) {
            MakeBlock2("LE_", graph);
        } else if (op.equals("=")) {
            MakeBlock("EQ_", graph);
        } else if (op.equals("!")) {
            MakeNot();
        } else if (op.equals("-")) {
            MakeBlock2("SUB_", graph);
        } else if (op.equals("+")) {
            MakeBlock("ADD2_", graph);
        } else if (op.equals("*")) {
            MakeBlock("MUL2_", graph);
        } else if (op.equals("/")) {
            MakeBlock2("DIV_", graph);
        } else if (op.equals("]")) {
            MakeBlock("    TOF", graph);
        } else if (op.equals("#")) {
            MakeBlock2("    SEL_", graph);
        }
    }

    public void MakeBlock(String blockName, Graph2D graph) {
        try {
            Object input1 = operatorStack.pop();
            Object input2 = operatorStack.pop();
            String inputString1 = "";
            String inputString2 = "";
            GenericBlock inputBlock1 = null;
            GenericBlock inputBlock2 = null;
            if (input1 instanceof String) inputString1 = (String) input1; else inputBlock1 = (GenericBlock) input1;
            if (input2 instanceof String) inputString2 = (String) input2; else inputBlock2 = (GenericBlock) input2;
            if (inputString1.equals("") == false && inputBlock2 != null) {
                TwoInputBlock twoinputBlock = new TwoInputBlock(blockName, "0", inputString1, inputBlock2, "", graph);
                twoinputBlock.level = inputBlock2.level + 1;
                if (inputBlock2 instanceof TwoInputBlock) ((TwoInputBlock) inputBlock2).outputObject = twoinputBlock;
                operatorStack.push(twoinputBlock);
                allblocks.add(twoinputBlock);
            } else if (inputString1.equals("") == false && inputString2.equals("") == false) {
                TwoInputBlock twoinputBlock = new TwoInputBlock(blockName, "0", inputString1, inputString2, "", graph);
                twoinputBlock.level = 0;
                operatorStack.push(twoinputBlock);
                allblocks.add(twoinputBlock);
            } else if (inputBlock1 != null && inputString2.equals("") == false) {
                TwoInputBlock twoinputBlock = new TwoInputBlock(blockName, "0", inputBlock1, inputString2, "", graph);
                twoinputBlock.level = inputBlock1.level + 1;
                if (inputBlock1 instanceof TwoInputBlock) ((TwoInputBlock) inputBlock1).outputObject = twoinputBlock;
                operatorStack.push(twoinputBlock);
                allblocks.add(twoinputBlock);
            } else {
                TwoInputBlock twoinputBlock = new TwoInputBlock(blockName, "0", inputBlock1, inputBlock2, "", graph);
                if (inputBlock1.level >= inputBlock2.level) twoinputBlock.level = inputBlock1.level + 1; else if (inputBlock1.level < inputBlock2.level) twoinputBlock.level = inputBlock2.level + 1;
                if (inputBlock2 instanceof TwoInputBlock) ((TwoInputBlock) inputBlock2).outputObject = twoinputBlock;
                if (inputBlock1 instanceof TwoInputBlock) ((TwoInputBlock) inputBlock1).outputObject = twoinputBlock;
                operatorStack.push(twoinputBlock);
                allblocks.add(twoinputBlock);
            }
        } catch (Exception e) {
        }
    }

    public void MakeBlock2(String blockName, Graph2D graph) {
        try {
            Object input2 = operatorStack.pop();
            Object input1 = operatorStack.pop();
            String inputString1 = "";
            String inputString2 = "";
            GenericBlock inputBlock1 = null;
            GenericBlock inputBlock2 = null;
            if (input1 instanceof String) inputString1 = (String) input1; else inputBlock1 = (GenericBlock) input1;
            if (input2 instanceof String) inputString2 = (String) input2; else inputBlock2 = (GenericBlock) input2;
            if (inputString1.equals("") == false && inputBlock2 != null) {
                TwoInputBlock twoinputBlock = new TwoInputBlock(blockName, "0", inputString1, inputBlock2, "", graph);
                twoinputBlock.level = inputBlock2.level + 1;
                if (inputBlock2 instanceof TwoInputBlock) ((TwoInputBlock) inputBlock2).outputObject = twoinputBlock;
                operatorStack.push(twoinputBlock);
                allblocks.add(twoinputBlock);
            } else if (inputString1.equals("") == false && inputString2.equals("") == false) {
                TwoInputBlock twoinputBlock = new TwoInputBlock(blockName, "0", inputString1, inputString2, "", graph);
                twoinputBlock.level = 0;
                operatorStack.push(twoinputBlock);
                allblocks.add(twoinputBlock);
            } else if (inputBlock1 != null && inputString2.equals("") == false) {
                TwoInputBlock twoinputBlock = new TwoInputBlock(blockName, "0", inputBlock1, inputString2, "", graph);
                twoinputBlock.level = inputBlock1.level + 1;
                if (inputBlock1 instanceof TwoInputBlock) ((TwoInputBlock) inputBlock1).outputObject = twoinputBlock;
                operatorStack.push(twoinputBlock);
                allblocks.add(twoinputBlock);
            } else {
                TwoInputBlock twoinputBlock = new TwoInputBlock(blockName, "0", inputBlock1, inputBlock2, "", graph);
                if (inputBlock1.level >= inputBlock2.level) twoinputBlock.level = inputBlock1.level + 1; else if (inputBlock1.level < inputBlock2.level) twoinputBlock.level = inputBlock2.level + 1;
                if (inputBlock2 instanceof TwoInputBlock) ((TwoInputBlock) inputBlock2).outputObject = twoinputBlock;
                if (inputBlock1 instanceof TwoInputBlock) ((TwoInputBlock) inputBlock1).outputObject = twoinputBlock;
                operatorStack.push(twoinputBlock);
                allblocks.add(twoinputBlock);
            }
        } catch (Exception e) {
        }
    }

    public void MakeNot() {
        Object o = operatorStack.pop();
        if (o instanceof String) {
            String s = (String) o;
            s = "!" + s;
            operatorStack.push(s);
        } else if (o instanceof GenericBlock) {
            GenericBlock b = (GenericBlock) o;
            b.isNot = true;
            operatorStack.push(b);
        }
    }

    public void DrawBlocksByPostfix(Graph2D graph) {
        int highestLevel = 0;
        int muxRow = totalRowCount;
        if (graph_outputFBD != null && isSDT == true) {
            NodeRealizer nr = graph.getRealizer(((TwoInputBlock) operatorStack.peek()).node_output);
            nr.getLabel().setText(outputString);
        }
        for (TwoInputBlock twoinputBlock : allblocks) {
            DrawBlock(twoinputBlock, graph);
            if (twoinputBlock.level > highestLevel) {
                highestLevel = twoinputBlock.level;
            }
        }
        if ((graph_outputFBD != null || graph_stateFBD != null) && isSDT == false) {
            MuxBlock muxblock = new MuxBlock();
            muxblock.level = highestLevel + 1;
            muxblock.leafCount = muxRow;
            if (operatorStack.peek() instanceof String) {
                if (((String) operatorStack.peek()).equals("(")) operatorStack.pop();
            }
            NodeRealizer nr = graph.getRealizer(((TwoInputBlock) operatorStack.peek()).node_output);
            if (graph_outputFBD != null) {
                nr.getLabel().setText(outputString);
                muxblock.string_output = outputString;
            } else if (graph_stateFBD != null) {
                nr.getLabel().setText("status");
                muxblock.string_output = "status";
            }
            DrawMuxBlock(muxblock, graph);
            operatorStack.push(muxblock);
        }
        if (graph_outputFBD != null && isSDT == false) {
            MoveBlock moveblock = new MoveBlock();
            moveblock.level = highestLevel + 2;
            moveblock.leafCount = muxRow;
            moveblock.string_output = outputString;
            DrawMoveBlock(moveblock, graph);
            operatorStack.push(moveblock);
        }
        allblocks.clear();
    }

    Element CreateInVariableXml(String expression, NodeRealizer nr) {
        Element inVariableElement = xmldoc.createElementNS(null, "inVariable");
        inVariableElement.setAttributeNS(null, "localId", Integer.toString(this.localIdNumber));
        inVariableElement.setAttribute("height", "16");
        inVariableElement.setAttribute("width", Integer.toString((expression.length() + 16) / 16 * 16));
        Element position = xmldoc.createElement("position");
        if (nr == null) {
            position.setAttribute("x", "0");
            position.setAttribute("y", "0");
        } else {
            position.setAttribute("x", Integer.toString((int) nr.getX() / 16 * 16));
            position.setAttribute("y", Integer.toString((int) nr.getY() / 16 * 16));
        }
        Element connectionPointOutElement = xmldoc.createElementNS(null, "connectionPointOut");
        Element relPosition = xmldoc.createElement("relPosition");
        relPosition.setAttribute("x", Integer.toString((expression.length() + 16) / 16 * 16));
        relPosition.setAttribute("y", "8");
        Element expressionElement = xmldoc.createElementNS(null, "expression");
        org.w3c.dom.Node expressionNode = xmldoc.createTextNode(expression);
        connectionPointOutElement.appendChild(relPosition);
        expressionElement.appendChild(expressionNode);
        inVariableElement.appendChild(position);
        inVariableElement.appendChild(connectionPointOutElement);
        inVariableElement.appendChild(expressionElement);
        this.localIdNumber++;
        return inVariableElement;
    }

    Element CreateOutVariableXml(String expression, NodeRealizer nr) {
        Element outVariableElement = xmldoc.createElementNS(null, "outVariable");
        outVariableElement.setAttributeNS(null, "localId", Integer.toString(this.localIdNumber));
        outVariableElement.setAttribute("height", "16");
        outVariableElement.setAttribute("width", Integer.toString((expression.length() + 16) / 16 * 16));
        Element position = xmldoc.createElement("position");
        if (nr == null) {
            position.setAttribute("x", "0");
            position.setAttribute("y", "0");
        } else {
            position.setAttribute("x", Integer.toString((int) nr.getX() / 16 * 16));
            position.setAttribute("y", Integer.toString((int) nr.getY() / 16 * 16));
        }
        Element connectionPointInElement = xmldoc.createElementNS(null, "connectionPointIn");
        Element relPosition = xmldoc.createElement("relPosition");
        relPosition.setAttribute("x", "0");
        relPosition.setAttribute("y", "8");
        Element connectionElement = xmldoc.createElementNS(null, "connection");
        connectionElement.setAttributeNS(null, "formalParameter", "OUT1");
        connectionElement.setAttributeNS(null, "refLocalId", Integer.toString(localIdNumber - 1));
        Element expressionElement = xmldoc.createElementNS(null, "expression");
        org.w3c.dom.Node expressionNode = xmldoc.createTextNode(expression);
        expressionElement.appendChild(expressionNode);
        connectionPointInElement.appendChild(relPosition);
        connectionPointInElement.appendChild(connectionElement);
        outVariableElement.appendChild(position);
        outVariableElement.appendChild(connectionPointInElement);
        outVariableElement.appendChild(expressionElement);
        this.localIdNumber++;
        return outVariableElement;
    }

    Element CreateTwoInputBlockXml(String typeName, int localUp, int localDown, int localSel, NodeRealizer nrbody, String upBlock, String downBlock, TwoInputBlock tblock) {
        typeName = typeName.replace(" ", "");
        Element block = xmldoc.createElementNS(null, "block");
        block.setAttributeNS(null, "localId", Integer.toString(this.localIdNumber));
        block.setAttributeNS(null, "typeName", typeName);
        block.setAttributeNS(null, "instanceName", typeName);
        Element position = xmldoc.createElement("position");
        String negated = "false";
        if (nrbody == null) {
            block.setAttribute("height", "0");
            block.setAttribute("width", "0");
            position.setAttribute("x", "0");
            position.setAttribute("y", "0");
        } else {
            block.setAttribute("height", Integer.toString((int) nrbody.getHeight() / 16 * 16));
            block.setAttribute("width", Integer.toString((int) nrbody.getWidth() / 16 * 16));
            position.setAttribute("x", Integer.toString((int) nrbody.getX() / 16 * 16));
            position.setAttribute("y", Integer.toString((int) nrbody.getY() / 16 * 16));
        }
        Element inputVariables = xmldoc.createElementNS(null, "inputVariables");
        Element variableUp = xmldoc.createElementNS(null, "variable");
        variableUp.setAttributeNS(null, "formalParameter", "IN1");
        if (tblock.inputUpNe == true) {
            negated = "true";
        }
        variableUp.setAttributeNS(null, "negated", negated);
        negated = "false";
        Element connectionPointInUp = xmldoc.createElementNS(null, "connectionPointIn");
        Element relPositionInUp = xmldoc.createElement("relPosition");
        relPositionInUp.setAttribute("x", "0");
        relPositionInUp.setAttribute("y", "8");
        Element connectionUp = xmldoc.createElementNS(null, "connection");
        if (upBlock.endsWith("OUT1")) connectionUp.setAttributeNS(null, "formalParameter", upBlock);
        connectionUp.setAttributeNS(null, "refLocalId", Integer.toString(localUp));
        Element variableDown = xmldoc.createElementNS(null, "variable");
        variableDown.setAttributeNS(null, "formalParameter", "IN2");
        if (tblock.inputDownNe == true) {
            negated = "true";
        }
        variableDown.setAttributeNS(null, "negated", negated);
        negated = "false";
        Element connectionPointInDown = xmldoc.createElementNS(null, "connectionPointIn");
        Element relPositionInDown = xmldoc.createElement("relPosition");
        relPositionInDown.setAttribute("x", "0");
        relPositionInDown.setAttribute("y", "24");
        Element connectionDown = xmldoc.createElementNS(null, "connection");
        if (downBlock.endsWith("OUT1")) connectionDown.setAttributeNS(null, "formalParameter", downBlock);
        connectionDown.setAttributeNS(null, "refLocalId", Integer.toString(localDown));
        Element variableSel = xmldoc.createElementNS(null, "variable");
        variableSel.setAttributeNS(null, "formalParameter", "IN3");
        Element connectionPointInSel = xmldoc.createElementNS(null, "connectionPointIn");
        Element relPositionInSel = xmldoc.createElement("relPosition");
        relPositionInSel.setAttribute("x", "0");
        relPositionInSel.setAttribute("y", "40");
        Element connectionSel = xmldoc.createElementNS(null, "connection");
        connectionSel.setAttributeNS(null, "refLocalId", Integer.toString(localSel));
        Element inOutVariables = xmldoc.createElementNS(null, "inOutVariables");
        Element outputVariables = xmldoc.createElementNS(null, "outputVariables");
        Element variableOut = xmldoc.createElementNS(null, "variable");
        variableOut.setAttributeNS(null, "formalParameter", "OUT1");
        Element connectionPointOut = xmldoc.createElementNS(null, "connectionPointOut");
        Element relPositionOut = xmldoc.createElement("relPosition");
        if (nrbody == null) relPositionOut.setAttribute("x", "0"); else relPositionOut.setAttribute("x", Integer.toString((int) nrbody.getWidth() / 16 * 16));
        relPositionOut.setAttribute("y", "8");
        connectionPointInUp.appendChild(relPositionInUp);
        connectionPointInUp.appendChild(connectionUp);
        variableUp.appendChild(connectionPointInUp);
        connectionPointInDown.appendChild(relPositionInDown);
        connectionPointInDown.appendChild(connectionDown);
        variableDown.appendChild(connectionPointInDown);
        connectionPointInSel.appendChild(relPositionInSel);
        connectionPointInSel.appendChild(connectionSel);
        variableSel.appendChild(connectionPointInSel);
        inputVariables.appendChild(variableUp);
        inputVariables.appendChild(variableDown);
        if (localSel != 0) {
            inputVariables.appendChild(variableSel);
        }
        connectionPointOut.appendChild(relPositionOut);
        variableOut.appendChild(connectionPointOut);
        outputVariables.appendChild(variableOut);
        block.appendChild(position);
        block.appendChild(inputVariables);
        block.appendChild(inOutVariables);
        block.appendChild(outputVariables);
        this.localIdNumber++;
        return block;
    }

    Element CreateMuxBlockXml(int[] localIds, NodeRealizer nrbody) {
        Element block = xmldoc.createElementNS(null, "block");
        int k = localIds.length;
        int i = 0;
        block.setAttributeNS(null, "localId", Integer.toString(this.localIdNumber));
        block.setAttributeNS(null, "typeName", "MUX");
        block.setAttributeNS(null, "instanceName", "MUX_INT");
        block.setAttribute("height", Integer.toString((int) nrbody.getHeight() / 16 * 16));
        block.setAttribute("width", Integer.toString((int) nrbody.getWidth() / 16 * 16));
        Element position = xmldoc.createElement("position");
        position.setAttribute("x", Integer.toString((int) nrbody.getX() / 16 * 16));
        position.setAttribute("y", Integer.toString((int) nrbody.getY() / 16 * 16));
        Element inputVariables = xmldoc.createElementNS(null, "inputVariables");
        Element variableK = xmldoc.createElementNS(null, "variable");
        variableK.setAttributeNS(null, "formalParameter", "K");
        Element connectionPointInK = xmldoc.createElementNS(null, "connectionPointIn");
        Element relPositionInK = xmldoc.createElement("relPosition");
        relPositionInK.setAttribute("x", "0");
        relPositionInK.setAttribute("y", "8");
        Element connectionK = xmldoc.createElementNS(null, "connection");
        connectionK.setAttributeNS(null, "refLocalId", Integer.toString(this.localIdNumber - 1));
        connectionPointInK.appendChild(relPositionInK);
        connectionPointInK.appendChild(connectionK);
        variableK.appendChild(connectionPointInK);
        inputVariables.appendChild(variableK);
        Element variable1 = xmldoc.createElementNS(null, "variable");
        variable1.setAttributeNS(null, "formalParameter", "IN1");
        Element connectionPointIn1 = xmldoc.createElementNS(null, "connectionPointIn");
        Element relPositionIn1 = xmldoc.createElement("relPosition");
        relPositionIn1.setAttribute("x", "0");
        relPositionIn1.setAttribute("y", "24");
        connectionPointIn1.appendChild(relPositionIn1);
        if (i < k) {
            Element connection1 = xmldoc.createElementNS(null, "connection");
            connection1.setAttributeNS(null, "refLocalId", Integer.toString(localIds[i]));
            connectionPointIn1.appendChild(connection1);
            i++;
            variable1.appendChild(connectionPointIn1);
            inputVariables.appendChild(variable1);
        }
        Element variable2 = xmldoc.createElementNS(null, "variable");
        variable2.setAttributeNS(null, "formalParameter", "IN2");
        Element connectionPointIn2 = xmldoc.createElementNS(null, "connectionPointIn");
        Element relPositionIn2 = xmldoc.createElement("relPosition");
        relPositionIn2.setAttribute("x", "0");
        relPositionIn2.setAttribute("y", "40");
        connectionPointIn2.appendChild(relPositionIn2);
        if (i < k) {
            Element connection2 = xmldoc.createElementNS(null, "connection");
            connection2.setAttributeNS(null, "refLocalId", Integer.toString(localIds[i]));
            connectionPointIn2.appendChild(connection2);
            i++;
            variable2.appendChild(connectionPointIn2);
            inputVariables.appendChild(variable2);
        }
        Element variable3 = xmldoc.createElementNS(null, "variable");
        variable3.setAttributeNS(null, "formalParameter", "IN3");
        Element connectionPointIn3 = xmldoc.createElementNS(null, "connectionPointIn");
        Element relPositionIn3 = xmldoc.createElement("relPosition");
        relPositionIn3.setAttribute("x", "0");
        relPositionIn3.setAttribute("y", "56");
        connectionPointIn3.appendChild(relPositionIn3);
        if (i < k) {
            Element connection3 = xmldoc.createElementNS(null, "connection");
            connection3.setAttributeNS(null, "refLocalId", Integer.toString(localIds[i]));
            connectionPointIn3.appendChild(connection3);
            i++;
            variable3.appendChild(connectionPointIn3);
            inputVariables.appendChild(variable3);
        }
        Element variable4 = xmldoc.createElementNS(null, "variable");
        variable4.setAttributeNS(null, "formalParameter", "IN4");
        Element connectionPointIn4 = xmldoc.createElementNS(null, "connectionPointIn");
        Element relPositionIn4 = xmldoc.createElement("relPosition");
        relPositionIn4.setAttribute("x", "0");
        relPositionIn4.setAttribute("y", "72");
        connectionPointIn4.appendChild(relPositionIn4);
        if (i < k) {
            Element connection4 = xmldoc.createElementNS(null, "connection");
            connection4.setAttributeNS(null, "refLocalId", Integer.toString(localIds[i]));
            connectionPointIn4.appendChild(connection4);
            i++;
            variable4.appendChild(connectionPointIn4);
            inputVariables.appendChild(variable4);
        }
        Element variable5 = xmldoc.createElementNS(null, "variable");
        variable5.setAttributeNS(null, "formalParameter", "IN5");
        Element connectionPointIn5 = xmldoc.createElementNS(null, "connectionPointIn");
        Element relPositionIn5 = xmldoc.createElement("relPosition");
        relPositionIn5.setAttribute("x", "0");
        relPositionIn5.setAttribute("y", "88");
        connectionPointIn5.appendChild(relPositionIn5);
        if (i < k) {
            Element connection5 = xmldoc.createElementNS(null, "connection");
            connection5.setAttributeNS(null, "refLocalId", Integer.toString(localIds[i]));
            connectionPointIn5.appendChild(connection5);
            i++;
            variable5.appendChild(connectionPointIn5);
            inputVariables.appendChild(variable5);
        }
        Element variable6 = xmldoc.createElementNS(null, "variable");
        variable6.setAttributeNS(null, "formalParameter", "IN6");
        Element connectionPointIn6 = xmldoc.createElementNS(null, "connectionPointIn");
        Element relPositionIn6 = xmldoc.createElement("relPosition");
        relPositionIn6.setAttribute("x", "0");
        relPositionIn6.setAttribute("y", "104");
        connectionPointIn6.appendChild(relPositionIn5);
        if (i < k) {
            Element connection6 = xmldoc.createElementNS(null, "connection");
            connection6.setAttributeNS(null, "refLocalId", Integer.toString(localIds[i]));
            connectionPointIn6.appendChild(connection6);
            i++;
            variable6.appendChild(connectionPointIn6);
            inputVariables.appendChild(variable6);
        }
        Element inOutVariables = xmldoc.createElementNS(null, "inOutVariables");
        Element outputVariables = xmldoc.createElementNS(null, "outputVariables");
        Element variableOut = xmldoc.createElementNS(null, "variable");
        variableOut.setAttributeNS(null, "formalParameter", "OUT1");
        Element connectionPointOut = xmldoc.createElementNS(null, "connectionPointOut");
        Element relPositionOut = xmldoc.createElement("relPosition");
        relPositionOut.setAttribute("x", Integer.toString((int) nrbody.getHeight() / 16 * 16));
        relPositionOut.setAttribute("y", "8");
        connectionPointOut.appendChild(relPositionOut);
        variableOut.appendChild(connectionPointOut);
        outputVariables.appendChild(variableOut);
        block.appendChild(position);
        block.appendChild(inputVariables);
        block.appendChild(inOutVariables);
        block.appendChild(outputVariables);
        this.localIdNumber++;
        return block;
    }

    Element CreateMoveBlockXml(NodeRealizer nrbody) {
        Element block = xmldoc.createElementNS(null, "block");
        block.setAttributeNS(null, "localId", Integer.toString(this.localIdNumber));
        block.setAttributeNS(null, "typeName", "MOVE_");
        block.setAttributeNS(null, "instanceName", "MOVE_");
        block.setAttribute("height", Integer.toString((int) nrbody.getHeight() / 16 * 16));
        block.setAttribute("width", Integer.toString((int) nrbody.getWidth() / 16 * 16));
        Element position = xmldoc.createElement("position");
        position.setAttribute("x", Integer.toString((int) nrbody.getX() / 16 * 16));
        position.setAttribute("y", Integer.toString((int) nrbody.getY() / 16 * 16));
        Element inputVariables = xmldoc.createElementNS(null, "inputVariables");
        Element variableUp = xmldoc.createElementNS(null, "variable");
        variableUp.setAttributeNS(null, "formalParameter", "IN1");
        Element connectionPointInUp = xmldoc.createElementNS(null, "connectionPointIn");
        Element relPositionInUp = xmldoc.createElement("relPosition");
        relPositionInUp.setAttribute("x", "0");
        relPositionInUp.setAttribute("y", "8");
        Element connectionUp = xmldoc.createElementNS(null, "connection");
        connectionUp.setAttributeNS(null, "refLocalId", Integer.toString(this.localIdNumber - 2));
        Element inOutVariables = xmldoc.createElementNS(null, "inOutVariables");
        Element outputVariables = xmldoc.createElementNS(null, "outputVariables");
        Element variableOut = xmldoc.createElementNS(null, "variable");
        variableOut.setAttributeNS(null, "formalParameter", "OUT1");
        Element connectionPointOut = xmldoc.createElementNS(null, "connectionPointOut");
        Element relPositionOut = xmldoc.createElement("relPosition");
        relPositionOut.setAttribute("x", Integer.toString((int) nrbody.getHeight() / 16 * 16));
        relPositionOut.setAttribute("y", "8");
        connectionPointInUp.appendChild(relPositionInUp);
        connectionPointInUp.appendChild(connectionUp);
        variableUp.appendChild(connectionPointInUp);
        inputVariables.appendChild(variableUp);
        connectionPointOut.appendChild(relPositionOut);
        variableOut.appendChild(connectionPointOut);
        outputVariables.appendChild(variableOut);
        block.appendChild(position);
        block.appendChild(inputVariables);
        block.appendChild(inOutVariables);
        block.appendChild(outputVariables);
        this.localIdNumber++;
        return block;
    }

    public void DrawBlock(TwoInputBlock block, Graph2D graph) {
        NodeRealizer nr = graph.getRealizer(block.node_Number);
        nr.getLabel().setText("(" + Integer.toString(allBlocksCount) + ")");
        allBlocksCount++;
        if (block.string_BlockName.equals("    SEL_")) block.SetLocation(new Point(block.level * 340 + 250, (block.leafCount + totalRowCount) * 130 + 20), true, graph); else block.SetLocation(new Point(block.level * 340 + 250, (block.leafCount + totalRowCount) * 130 + 20), false, graph);
        if (isSaving == true) {
            NodeRealizer nrName = graph.getRealizer(block.node_Body);
            NodeRealizer nrUp = graph.getRealizer(block.node_inputUp);
            NodeRealizer nrDown = graph.getRealizer(block.node_inputDown);
            NodeRealizer nrOut = graph.getRealizer(block.node_output);
            System.out.println(block.outputObject);
            if (nrUp.getLabelText().equals("") && nrDown.getLabelText().equals("") && !block.inputDownObject.equals("output")) {
                if (block.string_BlockName.equals("    SEL_") && block.node_sel != null) {
                    NodeRealizer nrSel = graph.getRealizer(block.node_sel);
                    this.rootElement.appendChild(CreateInVariableXml(nrSel.getLabelText(), nrSel));
                    block.localIdNumber = this.localIdNumber;
                    try {
                        this.rootElement.appendChild(CreateTwoInputBlockXml(nrName.getLabelText(), ((TwoInputBlock) block.inputUpObject).localIdNumber, ((TwoInputBlock) block.inputDownObject).localIdNumber, this.localIdNumber - 1, nrName, "OUT1", "OUT1", block));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    block.localIdNumber = this.localIdNumber;
                    this.rootElement.appendChild(CreateTwoInputBlockXml(nrName.getLabelText(), ((TwoInputBlock) block.inputUpObject).localIdNumber, ((TwoInputBlock) block.inputDownObject).localIdNumber, 0, nrName, "OUT1", "OUT1", block));
                }
                if (block.outputObject == null && !(nrOut.getLabelText().equals(""))) this.rootElement.appendChild(CreateOutVariableXml(nrOut.getLabelText(), nrOut));
            } else if (nrUp.getLabelText().equals("") && (nrDown.getLabelText().equals("") == false || block.inputDownObject.equals("output"))) {
                this.rootElement.appendChild(CreateInVariableXml(nrDown.getLabelText(), nrDown));
                if (block.string_BlockName.equals("    SEL_") && block.node_sel != null) {
                    NodeRealizer nrSel = graph.getRealizer(block.node_sel);
                    this.rootElement.appendChild(CreateInVariableXml(nrSel.getLabelText(), nrSel));
                    block.localIdNumber = this.localIdNumber;
                    this.rootElement.appendChild(CreateTwoInputBlockXml(nrName.getLabelText(), ((TwoInputBlock) block.inputUpObject).localIdNumber, this.localIdNumber - 2, this.localIdNumber - 1, nrName, "OUT1", "", block));
                } else {
                    block.localIdNumber = this.localIdNumber;
                    this.rootElement.appendChild(CreateTwoInputBlockXml(nrName.getLabelText(), ((TwoInputBlock) block.inputUpObject).localIdNumber, localIdNumber - 1, 0, nrName, "OUT1", "", block));
                }
                if (block.outputObject == null && !(nrOut.getLabelText().equals(""))) this.rootElement.appendChild(CreateOutVariableXml(nrOut.getLabelText(), nrOut));
            } else if (nrUp.getLabelText().equals("") == false && nrDown.getLabelText().equals("")) {
                this.rootElement.appendChild(CreateInVariableXml(nrUp.getLabelText(), nrUp));
                if (block.string_BlockName.equals("    SEL_") && block.node_sel != null) {
                    NodeRealizer nrSel = graph.getRealizer(block.node_sel);
                    this.rootElement.appendChild(CreateInVariableXml(nrSel.getLabelText(), nrSel));
                    block.localIdNumber = this.localIdNumber;
                    if (block.inputDownObject instanceof TwoInputBlock) {
                        this.rootElement.appendChild(CreateTwoInputBlockXml(nrName.getLabelText(), localIdNumber - 2, ((TwoInputBlock) block.inputDownObject).localIdNumber, localIdNumber - 1, nrName, "", "OUT1", block));
                    }
                } else {
                    block.localIdNumber = this.localIdNumber;
                    if (block.inputDownObject instanceof TwoInputBlock) {
                        this.rootElement.appendChild(CreateTwoInputBlockXml(nrName.getLabelText(), localIdNumber - 1, ((TwoInputBlock) block.inputDownObject).localIdNumber, 0, nrName, "", "OUT1", block));
                    }
                }
                if (block.outputObject == null && !(nrOut.getLabelText().equals(""))) this.rootElement.appendChild(CreateOutVariableXml(nrOut.getLabelText(), nrOut));
            } else if (nrUp.getLabelText().equals("") == false && nrDown.getLabelText().equals("") == false) {
                this.rootElement.appendChild(CreateInVariableXml(nrUp.getLabelText(), nrUp));
                this.rootElement.appendChild(CreateInVariableXml(nrDown.getLabelText(), nrDown));
                if (block.string_BlockName.equals("    SEL_") && block.node_sel != null) {
                    NodeRealizer nrSel = graph.getRealizer(block.node_sel);
                    this.rootElement.appendChild(CreateInVariableXml(nrSel.getLabelText(), nrSel));
                    block.localIdNumber = this.localIdNumber;
                    this.rootElement.appendChild(CreateTwoInputBlockXml(nrName.getLabelText(), localIdNumber - 3, localIdNumber - 2, localIdNumber - 1, nrName, "", "", block));
                } else {
                    block.localIdNumber = this.localIdNumber;
                    this.rootElement.appendChild(CreateTwoInputBlockXml(nrName.getLabelText(), localIdNumber - 2, localIdNumber - 1, 0, nrName, "", "", block));
                }
                String a;
                System.out.println(nrOut.getLabelText());
                if (block.outputObject == null && !(nrOut.getLabelText().equals(""))) this.rootElement.appendChild(CreateOutVariableXml(nrOut.getLabelText(), nrOut));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void DrawMuxBlock(MuxBlock block, Graph2D graph) {
        block.SetLocation(new Point(block.level * 340 + 250, block.leafCount * 130 + 20), operatorStack.size(), graph);
        NodeRealizer nr = graph.getRealizer(block.node_Number);
        nr.getLabel().setText("(" + Integer.toString(allBlocksCount) + ")");
        int[] localIds = new int[operatorStack.size()];
        int i = 0;
        for (Object b : operatorStack) {
            localIds[i++] = ((TwoInputBlock) b).localIdNumber;
        }
        if (isSaving == true) {
            NodeRealizer nrin = graph.getRealizer(block.node_inputUp);
            this.rootElement.appendChild(CreateInVariableXml("status", nrin));
            NodeRealizer nrMux = graph.getRealizer(block.node_Body);
            this.rootElement.appendChild(CreateMuxBlockXml(localIds, nrMux));
            NodeRealizer nrMuxOut = graph.getRealizer(block.node_output);
            this.rootElement.appendChild(CreateOutVariableXml(nrMuxOut.getLabelText(), nrMuxOut));
        }
        allBlocksCount++;
    }

    public void DrawMoveBlock(MoveBlock block, Graph2D graph) {
        block.SetLocation(new Point(block.level * 340 + 250, block.leafCount * 130 + 20 + 80), graph);
        NodeRealizer nr = graph.getRealizer(block.node_Number);
        nr.getLabel().setText("(" + Integer.toString(allBlocksCount) + ")");
        if (isSaving == true) {
            NodeRealizer nrMove = graph.getRealizer(block.node_Body);
            this.rootElement.appendChild(CreateMoveBlockXml(nrMove));
            NodeRealizer nrMoveOut = graph.getRealizer(block.node_output);
            this.rootElement.appendChild(CreateOutVariableXml(nrMoveOut.getLabelText(), nrMoveOut));
        }
        allBlocksCount++;
    }
}
