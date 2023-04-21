package org.xmlcml.cml.tools;

import java.util.ArrayList;
import java.util.List;
import nu.xom.Attribute;
import nu.xom.Elements;
import org.apache.log4j.Logger;
import org.xmlcml.cml.base.CMLConstants;
import org.xmlcml.cml.element.CMLArg;
import org.xmlcml.cml.element.CMLBond;
import org.xmlcml.cml.element.CMLJoin;
import org.xmlcml.cml.element.CMLLabel;
import org.xmlcml.cml.element.CMLLength;
import org.xmlcml.cml.element.CMLMolecule;
import org.xmlcml.cml.element.CMLMoleculeList;
import org.xmlcml.cml.element.CMLTorsion;
import org.xmlcml.cml.element.CMLLabel.Position;
import org.xmlcml.euclid.Util;

/** class to support result of parse.
 * 
 * @author pm286
 *
 */
class FragmentSequence implements CMLConstants {

    @SuppressWarnings("unused")
    private static Logger LOG = Logger.getLogger(FragmentSequence.class);

    List<FragmentAndBond> fragmentAndBondList = null;

    /** process concise string.
     * @param formula string
     */
    public FragmentSequence(String formula) {
        fragmentAndBondList = new ArrayList<FragmentAndBond>();
        if (formula == null) {
            throw new RuntimeException("null concise string");
        }
        formula = formula.trim();
        while (formula.length() > 0) {
            if (formula.charAt(0) != Fragment.LFRAGLIST) {
                throw new RuntimeException("expected '" + Fragment.LFRAGLIST + "' at: " + formula);
            }
            int idx = Util.indexOfBalancedBracket(Fragment.LFRAGLIST, formula);
            if (idx == -1) {
                throw new RuntimeException("Unblanced '" + Fragment.LFRAGLIST + "..." + Fragment.RFRAGLIST + "' at: " + formula);
            }
            FragmentAndBond fragmentAndBond = new FragmentAndBond();
            this.addFragmentAndBond(fragmentAndBond);
            String fragmentS = formula.substring(1, idx);
            Fragment fragment = new Fragment(fragmentS);
            fragmentAndBond.setFragment(fragment);
            formula = formula.substring(fragmentS.length() + 2);
            String countExpressionS = CountExpression.grabCountString(formula);
            if (formula.length() == 0) {
                break;
            }
            if (countExpressionS != null && !countExpressionS.equals(S_EMPTY)) {
                CountExpression countExpression = new CountExpression(countExpressionS);
                fragment.setCountExpression(countExpression);
                formula = formula.substring(countExpressionS.length());
            }
            if (formula.length() == 0) {
                break;
            }
            String joinBondS = JoinBond.grabJoinString(formula);
            if (joinBondS.equals(S_EMPTY)) {
                throw new RuntimeException("Expected bond at: " + formula);
            }
            JoinBond joinBond = new JoinBond(joinBondS);
            fragmentAndBond.setJoinBond(joinBond);
            formula = formula.substring(joinBondS.length());
        }
    }

    /** build up fragments.
     * 
     * @param fragmentAndBond
     */
    private void addFragmentAndBond(FragmentAndBond fragmentAndBond) {
        fragmentAndBondList.add(fragmentAndBond);
    }

    /** gets CMLJoin from fragments.
     * 
     * @return join
     */
    CMLMoleculeList getCMLMoleculeList() {
        CMLMoleculeList moleculeList = new CMLMoleculeList();
        moleculeList.addAttribute(new Attribute("convention", CMLJoin.FRAGMENT_CONTAINER));
        for (FragmentAndBond fragmentAndBond : fragmentAndBondList) {
            Fragment fragment = fragmentAndBond.getFragment();
            CMLMolecule molecule = fragment.getMolecule();
            Elements molecules = moleculeList.getChildCMLElements(CMLMolecule.TAG);
            CMLMolecule previousMolecule = null;
            if (molecules.size() > 0) {
                previousMolecule = (CMLMolecule) molecules.get(molecules.size() - 1);
            }
            if (previousMolecule != null && molecule.getRef() == null) {
                Elements joins = moleculeList.getChildCMLElements(CMLJoin.TAG);
                CMLJoin previousJoin = null;
                if (joins.size() > 0) {
                    previousJoin = (CMLJoin) joins.get(joins.size() - 1);
                }
                if (previousJoin != null) {
                    previousJoin.detach();
                    previousMolecule.appendChild(previousJoin);
                }
                previousMolecule.appendChild(molecule);
                CMLJoin subJoin = fragmentAndBond.getCMLJoin();
                if (subJoin != null) {
                    moleculeList.appendChild(subJoin);
                }
            } else {
                moleculeList.appendChild(molecule);
                CMLJoin subJoin = fragmentAndBond.getCMLJoin();
                if (subJoin != null) {
                    moleculeList.appendChild(subJoin);
                }
            }
        }
        return moleculeList;
    }

    /** debug string.
     * @return the string
     */
    public String getString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<FS>");
        for (FragmentAndBond fragmentAndBond : fragmentAndBondList) {
            sb.append(fragmentAndBond.getString());
        }
        sb.append("</FS>");
        return sb.toString();
    }

    /** string.
     * @return the string
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (FragmentAndBond fragmentAndBond : fragmentAndBondList) {
            sb.append(fragmentAndBond.toString());
        }
        return sb.toString();
    }
}

;

class FragmentAndBond implements CMLConstants {

    private Fragment fragment;

    private JoinBond joinBond;

    /** constructor.
     */
    public FragmentAndBond() {
    }

    /** set fragment at end of partial parse.
     * will trim brackets from fragmentList if countExpression set
     * @param fragment the complete fragment without trailing bond
     */
    void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    /** get fragment
     * 
     * @return the fragment
     */
    Fragment getFragment() {
        return fragment;
    }

    /** set joinBond.
     * 
     * @param joinBond
     */
    void setJoinBond(JoinBond joinBond) {
        this.joinBond = joinBond;
    }

    /** get join from joinBond.
     * 
     * @return the join
     */
    CMLJoin getCMLJoin() {
        return (joinBond == null) ? null : joinBond.getCMLJoin();
    }

    /** debug string.
     * @return the string
     */
    public String getString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<FB>");
        sb.append(fragment.getString());
        sb.append("<JB>");
        if (joinBond != null) {
            sb.append(joinBond.getString());
        }
        sb.append("</JB>");
        sb.append("</FB>");
        return sb.toString();
    }

    /** string.
     * @return the string
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(fragment.toString());
        if (joinBond != null) {
            sb.append(joinBond.toString());
        }
        return sb.toString();
    }
}

;

class Fragment implements CMLConstants {

    static char LFRAGLIST = C_LBRAK;

    static char RFRAGLIST = C_RBRAK;

    static char LLABEL = C_LCURLY;

    static char RLABEL = C_RCURLY;

    static char PARAM = C_PERCENT;

    private CountExpression countExpression;

    private JoinBond repeatableBond;

    private String fragmentS;

    private List<Branch> branchList;

    private String localRefS;

    private String prefixS;

    private String leftLabel;

    private String rightLabel;

    Branch branch;

    private List<Param> paramList;

    /** create fragment from string.
     * 
     * @param fragmentS
     */
    public Fragment(String fragmentS) {
        init();
        this.fragmentS = fragmentS;
        parse(this.fragmentS);
    }

    private void init() {
        this.branchList = new ArrayList<Branch>();
        this.paramList = new ArrayList<Param>();
        this.countExpression = null;
        this.leftLabel = S_EMPTY;
        this.rightLabel = S_EMPTY;
        this.prefixS = S_EMPTY;
        this.localRefS = S_EMPTY;
    }

    /** set countExpression.
     * will trim fragmentList if necessary
     * @param countExpression
     */
    void setCountExpression(CountExpression countExpression) {
        this.countExpression = countExpression;
    }

    /** adds branch to branchList
     * @param branch to add
     */
    public void addBranch(Branch branch) {
        this.branchList.add(branch);
    }

    CMLMolecule getMolecule() {
        CMLMolecule molecule = new CMLMolecule();
        String refS = (this.prefixS.equals(S_EMPTY)) ? this.localRefS : this.prefixS + S_COLON + this.localRefS;
        molecule.setRef(refS);
        if (this.countExpression != null) {
            molecule.addAttribute(new Attribute("countExpression", this.countExpression.toString()));
            if (repeatableBond != null) {
                CMLJoin join = repeatableBond.getCMLJoin();
                molecule.appendChild(join);
            }
        }
        for (Param param : paramList) {
            CMLArg arg = new CMLArg();
            arg.setName(param.name);
            arg.setString(param.value);
            molecule.insertChild(arg, 0);
        }
        for (Branch branch : branchList) {
            CMLMoleculeList join = branch.getMoleculeList();
            molecule.appendChild(join);
        }
        if (!this.leftLabel.equals(S_EMPTY)) {
            CMLLabel.setLabel(molecule, CMLLabel.Position.LEFT, leftLabel);
        }
        if (!this.rightLabel.equals(S_EMPTY)) {
            CMLLabel.setLabel(molecule, CMLLabel.Position.RIGHT, rightLabel);
        }
        return molecule;
    }

    /** string 
     * of form leftLabel? namespace? ref rightLabel? OR
     * rightLabel
     * @param refS
     */
    private void parse(String refS) {
        localRefS = refS;
        int idx = localRefS.indexOf(LLABEL);
        if (idx != -1) {
            leftLabel = localRefS.substring(0, idx);
            localRefS = localRefS.substring(idx + 1);
        }
        int lr = localRefS.length();
        if (lr == 0) {
            throw new RuntimeException("no ref given");
        }
        idx = localRefS.indexOf(S_COLON);
        if (idx != -1) {
            prefixS = localRefS.substring(0, idx);
            localRefS = localRefS.substring(idx + 1);
        }
        idx = getNextPunctuation(localRefS);
        if (idx == -1) {
        } else {
            String rightS = localRefS.substring(idx);
            localRefS = localRefS.substring(0, idx);
            while (true) {
                if (rightS.length() == 0) {
                    break;
                }
                char rc = rightS.charAt(0);
                if (JoinBond.isBondChar(rc)) {
                    String joinBondS = JoinBond.grabJoinString(rightS);
                    repeatableBond = new JoinBond(joinBondS);
                    rightS = rightS.substring(joinBondS.length());
                    if (!rightS.equals(S_EMPTY)) {
                        throw new RuntimeException("Unexpected fragment after " + "repeatableBond :" + rightS + S_COLON);
                    }
                } else if (rc == Branch.LBRANCH) {
                    String branchS = Branch.grabBranch(rightS);
                    Branch branch = new Branch(branchS.substring(1, branchS.length() - 1));
                    branchList.add(branch);
                    rightS = rightS.substring(branchS.length());
                } else if (rc == PARAM) {
                    rightS = rightS.substring(1);
                    idx = rightS.indexOf(PARAM);
                    if (idx == -1) {
                        throw new RuntimeException("missing balancing parameter delimiter: " + PARAM);
                    }
                    String paramS = rightS.substring(0, idx);
                    rightS = rightS.substring(idx + 1);
                    processParams(paramS);
                } else if (rc == RLABEL) {
                    rightS = rightS.substring(1);
                    rightLabel = rightS;
                    idx = getNextPunctuation(rightS);
                    if (idx != -1) {
                        if (JoinBond.isBondChar(rightS.charAt(idx))) {
                            rightS = rightS.substring(idx);
                            rightLabel = rightLabel.substring(0, idx);
                        } else {
                            throw new RuntimeException("Unexpected fragment after right label: " + rightS.substring(idx));
                        }
                    } else {
                        break;
                    }
                } else {
                    throw new RuntimeException("Unexpected fragment in parse: " + rightS);
                }
            }
        }
    }

    private int getNextPunctuation(String s) {
        int idx = -1;
        int l = s.length();
        for (int i = 0; i < l; i++) {
            char c = s.charAt(i);
            if (JoinBond.isBondChar(c) || c == Branch.LBRANCH || c == PARAM || c == RLABEL) {
                idx = i;
                break;
            }
        }
        return idx;
    }

    private void processParams(String p) {
        String[] params = p.split(S_COMMA);
        for (String param : params) {
            Param pp = new Param(param);
            this.paramList.add(pp);
        }
    }

    /** debug string.
     * @return string
     */
    public String getString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<F>");
        sb.append("<ll>");
        if (!leftLabel.equals(S_EMPTY)) {
            sb.append(leftLabel);
        }
        sb.append("</ll>");
        sb.append("<p>");
        if (!prefixS.equals(S_EMPTY)) {
            sb.append(prefixS);
            sb.append(S_COLON);
        }
        sb.append("</p>");
        sb.append("<lr>");
        sb.append(localRefS);
        sb.append("</lr>");
        for (Branch branch : branchList) {
            sb.append(branch.getString());
        }
        sb.append("<r>");
        if (!rightLabel.equals(S_EMPTY)) {
            sb.append(rightLabel);
        }
        sb.append("</r>");
        sb.append("<rb>");
        if (repeatableBond != null) {
            sb.append(repeatableBond.getString());
        }
        sb.append("</rb>");
        sb.append("<ce>");
        if (countExpression != null) {
            sb.append(countExpression.getString());
        }
        sb.append("</ce>");
        sb.append("</F>");
        String s = sb.toString();
        return s;
    }

    /** string.
     * @return string
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(LFRAGLIST);
        if (!leftLabel.equals(S_EMPTY)) {
            sb.append(leftLabel);
            sb.append(LLABEL);
        }
        if (!prefixS.equals(S_EMPTY)) {
            sb.append(prefixS);
            sb.append(S_COLON);
        }
        sb.append(localRefS);
        for (Branch branch : branchList) {
            sb.append(branch.toString());
        }
        if (!rightLabel.equals(S_EMPTY)) {
            sb.append(RLABEL);
            sb.append(rightLabel);
        }
        if (repeatableBond != null) {
            sb.append(repeatableBond);
        }
        sb.append(RFRAGLIST);
        if (countExpression != null) {
            sb.append(countExpression);
        }
        String s = sb.toString();
        return s;
    }
}

;

class JoinBond implements CMLConstants {

    private static Logger LOG = Logger.getLogger(JoinBond.class);

    static char LBOND = C_LSQUARE;

    static char RBOND = C_RSQUARE;

    /** single bond symbol.
     */
    public static final char SINGLEBOND = C_MINUS;

    /** double bond symbol.
     */
    public static final char DOUBLEBOND = C_EQUALS;

    /** triple bond symbol.
     */
    public static final char TRIPLEBOND = C_HASH;

    CMLLength length = null;

    CMLTorsion torsion = null;

    String torsionS = null;

    String lengthS = null;

    String order = CMLBond.SINGLE;

    String joinBondS = null;

    /** length keyword.
     */
    public static final String LEN = "l";

    /** torsion keyword.
     */
    public static final String TOR = "t";

    /** constructor.
     * 
     * @param jBondS
     */
    public JoinBond(String jBondS) {
        if (jBondS == null || jBondS.trim().length() == 0) {
            throw new RuntimeException("empty bond type");
        }
        this.joinBondS = jBondS;
        char c = jBondS.charAt(0);
        if (c == SINGLEBOND) {
            order = CMLBond.SINGLE;
        } else if (c == DOUBLEBOND) {
            order = CMLBond.DOUBLE;
        } else if (c == TRIPLEBOND) {
            order = CMLBond.TRIPLE;
        } else {
            throw new RuntimeException("Bad bond type: " + c);
        }
        jBondS = jBondS.substring(1);
        processQualifiers(jBondS);
    }

    static String grabJoinString(String fff) {
        String ff = fff;
        String s = S_EMPTY;
        if (ff.length() > 0) {
            if (!JoinBond.isBondChar(ff.charAt(0))) {
                throw new RuntimeException("expected bond type at: " + ff + " in " + fff);
            }
            String f = ff.substring(1);
            s = ff.substring(0, 1);
            if (f.length() > 0 && f.charAt(0) == JoinBond.LBOND) {
                int rbr = Util.indexOfBalancedBracket(JoinBond.LBOND, f);
                if (rbr == -1) {
                    throw new RuntimeException("Unbalanced: " + JoinBond.LBOND + "..." + JoinBond.RBOND);
                }
                s += f.substring(0, rbr + 1);
            } else {
                s = ff.substring(0, 1);
            }
        }
        return s;
    }

    /** current syntax.
     * len(1.4),tor(...)
     * tor is tor(120) or tor(evaluate(...))
     * @param jBondS
     */
    void processQualifiers(String jBondS) {
        if (jBondS.equals(S_EMPTY)) {
        } else if (jBondS.charAt(0) == LBOND) {
            int idx = Util.indexOfBalancedBracket(LBOND, jBondS);
            if (idx == -1) {
                throw new RuntimeException("Unbalanced " + LBOND + "..." + RBOND);
            }
            jBondS = jBondS.substring(1, jBondS.length() - 1);
            if (jBondS.equals(S_EMPTY)) {
                LOG.debug("empty bond qualifier");
            }
            lengthS = null;
            torsionS = null;
            while (jBondS.length() > 0) {
                if (jBondS.startsWith(LEN + C_LBRAK)) {
                    lengthS = grabKeywordBrackets(LEN, jBondS);
                    processLength(lengthS);
                    jBondS = jBondS.substring(LEN.length() + lengthS.length() + 2);
                } else if (jBondS.startsWith(TOR + C_LBRAK)) {
                    torsionS = grabKeywordBrackets(TOR, jBondS);
                    processTorsion(torsionS);
                    jBondS = jBondS.substring(TOR.length() + torsionS.length() + 2);
                } else if (jBondS.charAt(0) == C_COMMA) {
                    jBondS = jBondS.substring(1);
                } else {
                    throw new RuntimeException("must give keyworded qualifiers (" + LEN + ", etc) for bond/join");
                }
            }
        }
    }

    String grabKeywordBrackets(String keyword, String s) {
        s = s.substring(keyword.length());
        int idx = Util.indexOfBalancedBracket(C_LBRAK, s);
        if (idx == -1) {
            throw new RuntimeException("Cannot find balanced bracket in: " + s);
        }
        return s.substring(1, idx);
    }

    String grabNumber(String s) {
        int idx = s.indexOf(S_COMMA);
        if (idx != -1) {
            s = s.substring(0, idx);
        }
        try {
            new Double(s);
        } catch (NumberFormatException e) {
            s = null;
        }
        return s;
    }

    void processLength(String s) {
        try {
            Double lengthD = new Double(s);
            if (lengthD != null) {
                length = new CMLLength();
                length.setXMLContent(lengthD.doubleValue());
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("Cannot interpret as length: " + e);
        }
    }

    void processTorsion(String s) {
        try {
            Double torsionD = new Double(s);
            if (torsionD != null) {
                torsion = new CMLTorsion();
                torsion.setXMLContent(torsionD.doubleValue());
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException("Cannot interpret as torsion: " + e);
        }
    }

    CMLJoin getCMLJoin() {
        CMLJoin subJoin = new CMLJoin();
        CMLTorsion torsion = this.getTorsion();
        if (torsion != null) {
            torsion.detach();
            subJoin.appendChild(torsion);
        }
        CMLLength length = this.getLength();
        if (length != null) {
            length.detach();
            subJoin.appendChild(length);
        }
        if (this.order != null) {
            subJoin.setOrder(this.order);
        }
        return subJoin;
    }

    CMLLength getLength() {
        return length;
    }

    CMLTorsion getTorsion() {
        return torsion;
    }

    /** get debug string.
     * 
     * @return the string
     */
    public String getString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<B>");
        sb.append("<o>");
        sb.append(getOrderSymbol(order));
        sb.append("</o>");
        sb.append("<q>");
        sb.append("<l>");
        if (length != null) {
            sb.append(lengthS);
        }
        sb.append("</l>");
        sb.append("<t>");
        if (torsion != null) {
            sb.append(torsionS);
        }
        sb.append("</t>");
        sb.append("</q>");
        sb.append("</B>");
        return sb.toString();
    }

    /** get original string.
     * 
     * @return the string
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getOrderSymbol(order));
        if (length != null || torsion != null) {
            sb.append(LBOND);
            if (length != null) {
                sb.append(LEN);
                sb.append(C_LBRAK);
                sb.append(lengthS);
                sb.append(C_RBRAK);
            }
            if (torsion != null) {
                sb.append(TOR);
                sb.append(C_LBRAK);
                sb.append(torsionS);
                sb.append(C_RBRAK);
            }
            sb.append(RBOND);
        }
        return sb.toString();
    }

    /** translate CMLBond order into symbols.
     * 
     * @param orderS CMLBond.SINGLE, etc.
     * @return S_MINUS, S_EQUALS, etc.
     */
    public static String getOrderSymbol(String orderS) {
        String orderSymbol = S_MINUS;
        if (orderS == null || orderS.equals(CMLBond.SINGLE)) {
        } else if (orderS.equals(CMLBond.DOUBLE)) {
            orderSymbol = S_EQUALS;
        } else if (orderS.equals(CMLBond.TRIPLE)) {
            orderSymbol = S_HASH;
        }
        return orderSymbol;
    }

    /** does this character define a bond.
     * 
     * @param c character
     * @return tru id SINGLE/DOUBLE/TRIPLE
     */
    public static boolean isBondChar(char c) {
        boolean isChar = c == SINGLEBOND || c == DOUBLEBOND || c == TRIPLEBOND;
        return isChar;
    }
}

class Branch implements CMLConstants {

    static char LBRANCH = C_LSQUARE;

    static char RBRANCH = C_RSQUARE;

    String leftLabel;

    JoinBond joinBond;

    FragmentSequence fragmentSequence = null;

    CMLMoleculeList moleculeList;

    String branchS = S_EMPTY;

    /** parse string to create branch.
     * 
     * @param brs full branch string
     */
    public Branch(String brs) {
        this.branchS = brs;
        leftLabel = grabParentLink(branchS);
        branchS = branchS.substring(leftLabel.length());
        String joinString = JoinBond.grabJoinString(branchS);
        joinBond = new JoinBond(joinString);
        branchS = branchS.substring(joinString.length());
        fragmentSequence = new FragmentSequence(branchS);
        moleculeList = fragmentSequence.getCMLMoleculeList();
        CMLLabel.setLabel(moleculeList, Position.PARENT, leftLabel);
        CMLJoin subJoin = this.getSubJoin();
        moleculeList.insertChild(subJoin, 0);
        moleculeList.setConvention(FragmentTool.Convention.BRANCH.v);
    }

    static String grabBranch(String s) {
        if (s != null && s.length() > 0 && s.charAt(0) == LBRANCH) {
            int idx = Util.indexOfBalancedBracket(LBRANCH, s);
            s = s.substring(0, idx + 1);
        }
        return s;
    }

    private String grabParentLink(String s) {
        int idx = 0;
        while (true) {
            if (idx >= s.length()) {
                throw new RuntimeException("expect bond after parentLInk in branch");
            }
            if (JoinBond.isBondChar(s.charAt(idx))) {
                break;
            }
            idx++;
        }
        return s.substring(0, idx);
    }

    /** return new join element and add convert fragment children to molecules.
     * 
     * @return the join
     */
    CMLMoleculeList getMoleculeList() {
        return moleculeList;
    }

    private CMLJoin getSubJoin() {
        CMLJoin subJoin = new CMLJoin();
        subJoin.setOrder(joinBond.order);
        CMLLength length = joinBond.getLength();
        if (length != null) {
            length.detach();
            subJoin.appendChild(length);
        }
        CMLTorsion torsion = joinBond.getTorsion();
        if (torsion != null) {
            torsion.detach();
            subJoin.appendChild(torsion);
        }
        return subJoin;
    }

    /** debug string.
     * @return value
     */
    public String getString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<Br>");
        sb.append("<ll>");
        sb.append(leftLabel);
        sb.append("</ll>");
        sb.append("<jb>");
        sb.append(joinBond.getString());
        sb.append("</jb>");
        sb.append(fragmentSequence.getString());
        sb.append("</Br>");
        return sb.toString();
    }

    /** to string.
     * @return value
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(LBRANCH);
        sb.append(leftLabel);
        sb.append(Fragment.RLABEL);
        sb.append(joinBond.getString());
        sb.append(fragmentSequence.getString());
        sb.append(RBRANCH);
        return sb.toString();
    }
}

;

class CountExpression implements CMLConstants {

    /** left bracket for count.
     */
    static char LCOUNTBRAK = C_LBRAK;

    /** right bracket for count.
     */
    static char RCOUNTBRAK = C_RBRAK;

    /** range.
     */
    public static String RANGE = "range";

    /** gaussian.
     */
    public static String GAUSSIAN = "gaussian";

    String countExpressionS;

    /** constructor.
     * 
     * @param s raw string
     */
    public CountExpression(String s) {
        countExpressionS = s;
    }

    /** return raw string.
     * 
     * @return raw string
     */
    public String getString() {
        StringBuffer sb = new StringBuffer();
        sb.append("<CE>");
        sb.append(countExpressionS);
        sb.append("</CE>");
        return sb.toString();
    }

    /** grabs countExpression if it exists.
     * 
     * @param formula
     * @return empty string if none
     */
    static String grabCountString(String formula) {
        String s = S_EMPTY;
        int ll = 0;
        if (formula.length() == 0) {
        } else if (formula.startsWith(S_STAR + LCOUNTBRAK)) {
            ll = 1;
        } else if (formula.startsWith(RANGE + LCOUNTBRAK)) {
            ll = RANGE.length();
        } else if (formula.startsWith(GAUSSIAN + LCOUNTBRAK)) {
            ll = GAUSSIAN.length();
        } else {
        }
        if (ll != 0) {
            int rbr = Util.indexOfBalancedBracket(LCOUNTBRAK, formula.substring(ll));
            if (rbr == -1) {
                throw new RuntimeException("Cannot find balanced bracket:" + formula);
            }
            s = formula.substring(0, rbr + ll + 1);
        }
        return s;
    }

    /** to string.
     * @return value
     */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(countExpressionS);
        return sb.toString();
    }
}

;

class Param implements CMLConstants {

    String name;

    String value;

    /** parse name value pair.
     * of form a=b
     * @param s string to parse
     */
    public Param(String s) {
        String[] pp = s.split(S_EQUALS);
        if (pp.length != 2) {
            throw new RuntimeException("Param1 must be of type a=b; " + s);
        }
        name = pp[0];
        value = pp[1];
    }
}
