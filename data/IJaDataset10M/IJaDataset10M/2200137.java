package plugin.bn_predict.algorithm;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import plugin.bn_predict.model.BifNode;
import plugin.bn_predict.model.BifDOMBuilder;

public class BNProbTableCalculator {

    private float[][] origProbTable;

    private float[][] difProbTable;

    ArrayList<float[][]> probTableAL = new ArrayList<float[][]>();

    ArrayList<int[]> setNodes = new ArrayList<int[]>();

    private boolean[] ptFound;

    private BifDOMBuilder bdb;

    private ArrayList<BifNode> bif = new ArrayList<BifNode>();

    private int numNodes, undoIndex;

    private Color upColor = new Color(153, 255, 102);

    private Color downColor = new Color(255, 153, 153);

    private float sensitivity = .125f;

    private boolean useNeutral = false;

    public static final int ProbabilityTable = 0;

    public static final int ProbabilityChangeTable = 1;

    private int tableType = ProbabilityTable;

    /**
	 * Test Constructor
	 * @param bdb
	 */
    public BNProbTableCalculator(BifDOMBuilder bdb) {
        this.bdb = bdb;
        try {
            bif = this.bdb.build("C:/Projects/MeV/MeV_SVN/data/BN_files/affy_HG-U133_Plus_2_BN/results/FixedNetWithCPT.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
        numNodes = bif.size();
        undoIndex = 0;
        origProbTable = new float[numNodes][];
        difProbTable = new float[numNodes][];
        float[][] temp_ = new float[numNodes][];
        for (int i = 0; i < numNodes; i++) {
            origProbTable[i] = new float[bif.get(i).getBins()];
            difProbTable[i] = new float[bif.get(i).getBins()];
            temp_[i] = new float[bif.get(i).getBins()];
        }
        probTableAL.add(temp_);
        setNodes.add(new int[numNodes]);
        calculateTables();
        for (int i = 0; i < origProbTable.length; i++) {
            for (int j = 0; j < origProbTable[i].length; j++) {
                origProbTable[i][j] = probTableAL.get(undoIndex)[i][j];
            }
        }
        getDifTable();
    }

    /**
	 * Constructor
	 * @param bif - ArrayList of BifNodes
	 */
    public BNProbTableCalculator(BifDOMBuilder bdb, ArrayList<BifNode> bif) {
        this.bif = bif;
        this.bdb = bdb;
        numNodes = bif.size();
        undoIndex = 0;
        origProbTable = new float[numNodes][];
        difProbTable = new float[numNodes][];
        float[][] temp_ = new float[numNodes][];
        for (int i = 0; i < numNodes; i++) {
            origProbTable[i] = new float[bif.get(i).getBins()];
            difProbTable[i] = new float[bif.get(i).getBins()];
            temp_[i] = new float[bif.get(i).getBins()];
        }
        probTableAL.add(temp_);
        setNodes.add(new int[numNodes]);
        calculateTables();
        for (int i = 0; i < origProbTable.length; i++) {
            for (int j = 0; j < origProbTable[i].length; j++) {
                origProbTable[i][j] = probTableAL.get(undoIndex)[i][j];
            }
        }
        getDifTable();
    }

    public int getSetState(BifNode node) {
        return setNodes.get(undoIndex)[bif.indexOf(node)];
    }

    public Color getColor(BifNode node, boolean pt) {
        if (pt) return getRawValueRGBColor(node); else return getChangeRGBColor(node);
    }

    public void setUpColor(Color color) {
        upColor = color;
    }

    public void setDownColor(Color color) {
        downColor = color;
    }

    public Color getUpColor() {
        return upColor;
    }

    public Color getDownColor() {
        return downColor;
    }

    public void setSensitivity(int value) {
        sensitivity = (float) (value / 100f) * (float) (value / 100f) * (float) (value / 100f);
    }

    public int getSensitivity() {
        return (int) (100 * Math.pow(sensitivity, 1f / 3f));
    }

    private Color getChangeRGBColor(BifNode node) {
        float pct[] = getPCTableGivenChild(node);
        float hue;
        if (pct[pct.length - 1] > pct[BnConstants.DOWN]) hue = 0f / 6f; else hue = 4f / 6f;
        float[] hsbvals = new float[3];
        if (pct[pct.length - 1] > pct[BnConstants.DOWN]) Color.RGBtoHSB(upColor.getRed(), upColor.getGreen(), upColor.getBlue(), hsbvals); else Color.RGBtoHSB(downColor.getRed(), downColor.getGreen(), downColor.getBlue(), hsbvals);
        hue = hsbvals[0];
        float saturation = getColorSaturation(pct);
        float brightness = getColorBrightness(pct);
        ;
        Color rgbColor = new Color(Color.HSBtoRGB(hue, saturation, brightness));
        return rgbColor;
    }

    private Color getRawValueRGBColor(BifNode node) {
        float pct[] = getPTableGivenChild(node);
        float hue;
        if (pct[pct.length - 1] > pct[BnConstants.DOWN]) hue = 0f / 6f; else hue = 4f / 6f;
        float[] hsbvals = new float[3];
        if (pct[pct.length - 1] > pct[BnConstants.DOWN]) Color.RGBtoHSB(upColor.getRed(), upColor.getGreen(), upColor.getBlue(), hsbvals); else Color.RGBtoHSB(downColor.getRed(), downColor.getGreen(), downColor.getBlue(), hsbvals);
        hue = hsbvals[0];
        float saturation = getColorSaturation(pct);
        float brightness = getColorBrightness(pct);
        Color rgbColor = new Color(Color.HSBtoRGB(hue, saturation, brightness));
        return rgbColor;
    }

    private float getColorSaturation(float[] array) {
        return (float) Math.min(1.0, 80f * sensitivity * Math.abs(array[array.length - 1] - array[BnConstants.DOWN]));
    }

    private float getColorBrightness(float[] array) {
        if (useNeutral) return (float) Math.min(1.0, Math.abs(.75 - array[BnConstants.NEUTRAL] * .5f * sensitivity)); else return 1f;
    }

    public void setUseNeutral(boolean useNeutral) {
        this.useNeutral = useNeutral;
    }

    public boolean getUseNeutral() {
        return useNeutral;
    }

    public int getTableType() {
        return tableType;
    }

    public void setTableType(int type) {
        tableType = type;
    }

    /**
	 * Runs through all nodes, constructing probability tables of expression for each node.
	 */
    private void calculateTables() {
        ptFound = new boolean[numNodes];
        for (int i = 0; i < numNodes; i++) {
            ptFound[i] = false;
        }
        for (int i = 0; i < numNodes; i++) {
            if (!havePT(bif.get(i))) getPT(bif.get(i));
        }
        getDifTable();
    }

    public void resetNodes() {
        makeNewPT();
        for (int i = 0; i < setNodes.get(undoIndex).length; i++) {
            setNodes.get(undoIndex)[i] = 0;
        }
        calculateTables();
    }

    private void makeNewPT() {
        undoIndex++;
        while (probTableAL.size() > undoIndex) {
            probTableAL.remove(undoIndex);
        }
        while (setNodes.size() > undoIndex) {
            setNodes.remove(undoIndex);
        }
        float[][] temp_ = new float[numNodes][];
        for (int i = 0; i < numNodes; i++) {
            temp_[i] = new float[bif.get(i).getBins()];
        }
        probTableAL.add(temp_);
        setNodes.add(new int[numNodes]);
        for (int i = 0; i < setNodes.get(undoIndex).length; i++) {
            setNodes.get(undoIndex)[i] = setNodes.get(undoIndex - 1)[i];
        }
    }

    public void undo() {
        undoIndex--;
    }

    public boolean isUndoEnabled() {
        return !(undoIndex == 0);
    }

    public void redo() {
        undoIndex++;
    }

    public boolean isRedoEnabled() {
        return !(undoIndex == probTableAL.size() - 1);
    }

    private void setNode(BifNode bifNode, int set) {
        setNodes.get(undoIndex)[bif.indexOf(bifNode)] = set;
    }

    /**
	 * Raktim
	 * @param bifNodeind
	 * @param state
	 */
    private void setNode(int bifNodeind, int state) {
        makeNewPT();
        setNodes.get(undoIndex)[bifNodeind] = state;
    }

    private float[][] getDifTable() {
        upDateDif();
        return difProbTable;
    }

    /**
	 * Recursive algorithm.  Gets a Probability table for the given node.  If any of the parent's of bifNode are not solved
	 * for their respective PTs, then getPT will call itself with the parent as the given node.
	 * @param bifNode
	 */
    private void getPT(BifNode bifNode) {
        for (int i = 0; i < bdb.getParents(bifNode).size(); i++) {
            BifNode parent = bdb.getParents(bifNode).get(i);
            if (!havePT(parent)) getPT(parent);
        }
        calcPTWithParents(bifNode);
    }

    /**
	 * Calculates the probability table for a given node.  
	 * *NOTE* All parents of this node must have been solved for their probability
	 * tables before bifNode's PT can be calculated.
	 * @param bifNode
	 */
    private void calcPTWithParents(BifNode bifNode) {
        ArrayList<BifNode> parentNodes = bdb.getParents(bifNode);
        if (setNodes.get(undoIndex)[bif.indexOf(bifNode)] != 0) {
            for (int bin = 0; bin < bifNode.getBins(); bin++) {
                probTableAL.get(undoIndex)[bif.indexOf(bifNode)][bin] = 0;
            }
            probTableAL.get(undoIndex)[bif.indexOf(bifNode)][setNodes.get(undoIndex)[bif.indexOf(bifNode)] - 1] = 1;
            ptFound[bif.indexOf(bifNode)] = true;
            return;
        }
        if (parentNodes.size() == 0) {
            for (int bin = 0; bin < bifNode.getBins(); bin++) {
                try {
                    probTableAL.get(undoIndex)[bif.indexOf(bifNode)][bin] = bifNode.getCPT()[bin];
                } catch (NullPointerException npe) {
                    npe.printStackTrace();
                }
            }
            ptFound[bif.indexOf(bifNode)] = true;
            return;
        }
        int parentalBinCombinations = 1;
        for (int parent = 0; parent < parentNodes.size(); parent++) {
            parentalBinCombinations = parentalBinCombinations * parentNodes.get(parent).getBins();
        }
        for (int bin = 0; bin < bifNode.getBins(); bin++) {
            float prob = 0;
            int[] indices = new int[parentNodes.size()];
            for (int i = 0; i < parentNodes.size(); i++) {
                indices[i] = 0;
            }
            for (int i = 0; i < parentalBinCombinations; i++) {
                float p = 1;
                for (int parentCounter = 0; parentCounter < parentNodes.size(); parentCounter++) {
                    p = p * probTableAL.get(undoIndex)[bif.indexOf(parentNodes.get(parentCounter))][indices[parentCounter]];
                }
                prob = prob + p * bifNode.getCPT()[bifNode.getBins() * i + bin];
                int lastIndex = indices.length - 1;
                for (int numP = 0; numP < parentNodes.size(); numP++) {
                    indices[lastIndex]++;
                    if (indices[lastIndex] >= parentNodes.get(lastIndex).getBins()) {
                        indices[lastIndex] = 0;
                        lastIndex--;
                        continue;
                    }
                    break;
                }
            }
            probTableAL.get(undoIndex)[bif.indexOf(bifNode)][bin] = prob;
        }
        ptFound[bif.indexOf(bifNode)] = true;
    }

    /**
	 * Raktim - Integrates functionality with the GUI
	 * @param bifNodeind
	 * @param stateParent
	 * @return
	 */
    public float[] calcPTableGivenChild(int bifNodeind, int stateParent) {
        setNode(bifNodeind, stateParent);
        calculateTables();
        printCurrentPTs();
        printDifPTs();
        return getPTableGivenChild(bifNodeind);
    }

    /**
	 * Raktim - Return the PT for a node where the table has been claculated from its parent(s)
	 * To get PT for any given child node
	 * @param cNode a BifNode
	 * @return
	 */
    public float[] getPTableGivenChild(BifNode cNode) {
        int nodeInd = bif.indexOf(cNode);
        if (nodeInd == -1) return null;
        float _t[] = new float[cNode.getBins()];
        for (int t = 0; t < cNode.getBins(); t++) {
            _t[t] = probTableAL.get(undoIndex)[nodeInd][t];
        }
        return _t;
    }

    /**
	 * Dan - Return the Probablity Change Table for a node
	 * @param cNode a BifNode
	 * @return
	 */
    public float[] getPCTableGivenChild(BifNode cNode) {
        int nodeInd = bif.indexOf(cNode);
        if (nodeInd == -1) return null;
        upDateDif();
        float _t[] = new float[cNode.getBins()];
        for (int t = 0; t < cNode.getBins(); t++) {
            _t[t] = difProbTable[nodeInd][t];
        }
        return _t;
    }

    private void upDateDif() {
        for (int j = 0; j < numNodes; j++) {
            for (int i = 0; i < bif.get(j).getBins(); i++) {
                difProbTable[j][i] = probTableAL.get(undoIndex)[j][i] - origProbTable[j][i];
            }
        }
    }

    /**
	 * Raktim - Return the PT for a node where the table has been calculated from its parent(s)
	 * @param BifNode Index
	 * @return
	 */
    private float[] getPTableGivenChild(int BifNodeIndex) {
        int nodeInd = BifNodeIndex;
        if (nodeInd == -1) return null;
        float _t[] = new float[bif.get(nodeInd).getBins()];
        if (setNodes.get(undoIndex)[nodeInd] == 0) {
            for (int t = 0; t < bif.get(nodeInd).getBins(); t++) {
                _t[t] = origProbTable[nodeInd][t];
            }
        } else {
            for (int t = 0; t < bif.get(nodeInd).getBins(); t++) {
                _t[t] = probTableAL.get(undoIndex)[nodeInd][t];
            }
        }
        return _t;
    }

    /**
	 * Raktim - Return the CPT for any Node where states of all its parents are known
	 * States are 0, 1, 2 as defined in BnConstants.java
	 * @param bifNode - A child node whos CPT is requested
	 * @param parentsStates - A hash of parents and its known states
	 * @return
	 */
    public float[] getCPTgivenParentAndState(BifNode bifNode, HashMap<String, Integer> parentsStates) {
        int index = 0;
        int binSize = bifNode.getBins();
        int numParents = bifNode.numParents();
        if (numParents == 0) {
            return bifNode.getCPT();
        }
        int increment = (int) bifNode.getCPT().length;
        float[] _tmp = new float[bifNode.getBins()];
        for (int i = 0; i < bifNode.numParents(); i++, numParents--) {
            increment = increment / (bdb.getParents(bifNode).get(i).getBins());
            System.out.println("increment = " + increment);
            index += (parentsStates.get(bifNode.getParentAt(i))) * (increment);
        }
        System.out.println("index = " + index);
        System.out.println("bifNode.getCPT().length = " + bifNode.getCPT().length);
        System.out.println("bin size = " + binSize);
        for (int i = 0; i < binSize; i++) {
            _tmp[i] = bifNode.getCPT()[index + i];
        }
        return _tmp;
    }

    /**
	 * 
	 * @param bifNode
	 * @return Returns a boolean for whether or not the PT for the given node has yet been solved.
	 */
    private boolean havePT(BifNode bifNode) {
        return ptFound[bif.indexOf(bifNode)];
    }

    /**
	 * Prints a table listing the current probabilities of each state for each node.
	 */
    private void printCurrentPTs() {
        for (int i = 0; i < this.numNodes; i++) {
            System.out.print("Node: " + bif.get(i).getChild() + ":     \t");
            for (int j = 0; j < bif.get(i).getBins(); j++) {
                System.out.print(probTableAL.get(undoIndex)[i][j] + "\t");
            }
        }
    }

    /**
	 * Prints a table listing the original probabilities of each state for each node.
	 */
    private void printOriginalPTs() {
        for (int i = 0; i < this.numNodes; i++) {
            for (int j = 0; j < bif.get(i).getBins(); j++) {
            }
        }
    }

    /**
	 * Prints the difference table listing the probability changes of each state for each node from the original.
	 */
    private void printDifPTs() {
        upDateDif();
        for (int i = 0; i < this.numNodes; i++) {
            for (int j = 0; j < bif.get(i).getBins(); j++) {
                System.out.print(difProbTable[i][j] + "\t");
            }
        }
    }

    /**
	 * Prints info for each node
	 */
    private void printbdb() {
        int count = 0;
        for (int i = 0; i < bif.size(); i++) {
            float[] a = bif.get(i).getCPT();
            String b = bif.get(i).getChild();
            ArrayList<String> c = bif.get(i).getParents();
            if (b != null) {
            }
            if (c != null) {
                for (int j = 0; j < c.size(); j++) {
                }
            }
            if (a != null) {
                count++;
                for (int j = 0; j < a.length; j++) {
                }
            }
        }
    }

    private void showDialog() {
        JPanel jp = new JPanel();
        jp.setLayout(new GridBagLayout());
        for (int i = 0; i < this.numNodes; i++) {
            jp.add(new JLabel("Node: " + this.bif.get(i).getChild() + ": "), new GridBagConstraints(0, i, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 0), 0, 0));
            JButton jb = new JButton("Down");
            jb.setActionCommand(String.valueOf(i));
            jb.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    setNode(bif.get(Integer.parseInt(evt.getActionCommand())), 1);
                    calculateTables();
                    printCurrentPTs();
                    printDifPTs();
                }
            });
            jp.add(jb, new GridBagConstraints(1, i, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 0), 0, 0));
            jb = new JButton("Neutral");
            jb.setActionCommand(String.valueOf(i));
            jb.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    setNode(bif.get(Integer.parseInt(evt.getActionCommand())), 2);
                    calculateTables();
                    printCurrentPTs();
                    printDifPTs();
                }
            });
            jp.add(jb, new GridBagConstraints(2, i, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 0), 0, 0));
            jb = new JButton("Up");
            jb.setActionCommand(String.valueOf(i));
            jb.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    setNode(bif.get(Integer.parseInt(evt.getActionCommand())), 3);
                    calculateTables();
                    printCurrentPTs();
                    printDifPTs();
                }
            });
            jp.add(jb, new GridBagConstraints(3, i, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 0), 0, 0));
        }
        JButton jb = new JButton("Reset");
        jb.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                resetNodes();
                calculateTables();
                printCurrentPTs();
                printDifPTs();
            }
        });
        jp.add(jb, new GridBagConstraints(1, numNodes, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 0, 5, 0), 0, 0));
        JDialog jd = new JDialog();
        jd.add(jp);
        jd.pack();
        jd.setSize(400, 800);
        jd.setVisible(true);
    }

    public boolean is2State() {
        return false;
    }

    public boolean is3State() {
        return false;
    }
}
