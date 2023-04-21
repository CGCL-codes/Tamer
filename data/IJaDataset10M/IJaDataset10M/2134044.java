package org.deft.repository.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.deft.extension.tools.tokentrimmer.TokenAutoTrimmer;
import org.deft.repository.ast.Token;
import org.deft.repository.ast.TokenNode;
import org.deft.repository.ast.TokenNodeComparator;
import org.deft.repository.ast.TreeNode;
import org.deft.repository.ast.annotation.csformat.CSFormatInformation;
import org.deft.repository.ast.decoration.Format;
import org.deft.repository.ast.decoration.Range;
import org.deft.repository.ast.decoration.ReplaceGroup;
import org.deft.repository.ast.decoration.Templates;
import org.deft.repository.ast.decoration.selected.SelectedInformation;

public class XfsrQueryManager {

    public XfsrQueryManager() {
    }

    public void queryAndFormat(TreeNode treeNode, Query query, Format format, UUID refId) {
        List<TreeNode> queriedNodes = query(treeNode, query, refId);
        if (queriedNodes.size() > 0 && format != null) {
            doFormat(queriedNodes, format);
        }
    }

    public List<TreeNode> query(TreeNode treeNode, Query query, UUID refId) {
        List<TreeNode> queriedNodes = new LinkedList<TreeNode>();
        List<String> queryStrings = query.getQueryStrings();
        for (String qs : queryStrings) {
            List<TreeNode> nodes = treeNode.executeXPathQuery(qs);
            for (TreeNode node : nodes) {
                SelectedInformation info = new SelectedInformation();
                node.addInformation(info);
                info.addSelectedData(qs, null);
                queriedNodes.add(node);
            }
        }
        return queriedNodes;
    }

    /**
	 * Marks AST nodes with information such as "to be hidden" or "not to be hidden"
	 * @param queriedNodes
	 * @param csId
	 * @param format
	 */
    private void doFormat(List<TreeNode> queriedNodes, Format format) {
        if (queriedNodes.size() <= 0) return;
        for (String target : format.getTargetList()) {
            String sXpathTarget = "../" + target;
            for (ReplaceGroup rg : format.getReplaceGroups(target)) {
                for (String sHide : rg.getHide()) {
                    String sXpath = sXpathTarget + sHide;
                    for (TreeNode node : queriedNodes) {
                        List<TreeNode> lNodesToHide = node.executeXPathQuery(sXpath);
                        for (TreeNode n : lNodesToHide) {
                            CSFormatInformation csfi = new CSFormatInformation(format, target, rg.getId());
                            n.addInformation(csfi);
                        }
                    }
                }
                Set<TreeNode> stNoHide = new HashSet<TreeNode>();
                for (String sNoHide : rg.getHideExcept()) {
                    String sXpath = sXpathTarget + sNoHide;
                    for (TreeNode node : queriedNodes) for (TreeNode n : node.executeXPathQuery(sXpath)) {
                        stNoHide.add(n);
                    }
                }
                Set<TreeNode> stNonHiddenNodesParents = new HashSet<TreeNode>();
                Set<TreeNode> stNonHiddenNodesAncestors = new HashSet<TreeNode>();
                for (TreeNode node : stNoHide) {
                    stNonHiddenNodesAncestors.add(node);
                    TreeNode parent = node.getParent();
                    if (parent != null) {
                        stNonHiddenNodesParents.add(parent);
                    }
                    while (parent != null) {
                        stNonHiddenNodesAncestors.add(parent);
                        parent = parent.getParent();
                    }
                }
                for (TreeNode node : stNonHiddenNodesParents) {
                    List<TreeNode> children = node.getChildren();
                    for (TreeNode child : children) {
                        if (!stNonHiddenNodesAncestors.contains(child)) {
                            CSFormatInformation csfi = new CSFormatInformation(format, target, rg.getId());
                            child.addInformation(csfi);
                        }
                    }
                }
            }
        }
    }

    public TreeNode cutAndFormat(TreeNode root) {
        TreeNode newRoot = new TreeNode("root");
        List<TreeNode> selectedNodes = root.getDescendants(Templates.SELECTED);
        List<Range> lRange = new LinkedList<Range>();
        List<TokenNode> lReplacedTokens = new LinkedList<TokenNode>();
        for (TreeNode tn : selectedNodes) {
            newRoot.addChild(tn);
            List<TreeNode> lReplaced = replaceHiddenNodes(tn);
            for (TreeNode tnReplaced : lReplaced) {
                tnReplaced.serialize(lReplacedTokens);
                Range range = getRange(tnReplaced);
                if (range != null) {
                    lRange.add(range);
                }
            }
        }
        adjustTokenPosition(newRoot, lRange);
        return newRoot;
    }

    private List<TreeNode> replaceHiddenNodes(TreeNode root) {
        List<TreeNode> lReplace = new LinkedList<TreeNode>();
        List<TreeNode> hiddenNodes = root.getDescendants(Templates.CSFORMAT);
        Set<ReplaceGroup> lAppliedGroups = new HashSet<ReplaceGroup>();
        TokenAutoTrimmer t = new TokenAutoTrimmer(root);
        for (TreeNode tn : hiddenNodes) {
            CSFormatInformation csfi = (CSFormatInformation) tn.getInformation(Templates.CSFORMAT);
            Format format = csfi.getFormat();
            String target = csfi.getTarget();
            ReplaceGroup replaceGroup = format.getReplaceGroup(target, csfi.getReplaceGroupId());
            if (!lAppliedGroups.contains(replaceGroup)) {
                TokenNode tnRep = getTokenNode(tn, replaceGroup.getReplaceString());
                if (tnRep != null) {
                    tn.getParent().replaceChild(tn, tnRep);
                } else {
                    t.trim(tn);
                    tn.getParent().removeChild(tn);
                }
                lAppliedGroups.add(replaceGroup);
                lReplace.add(tn);
            } else {
                t.trim(tn);
                tn.getParent().removeChild(tn);
                lReplace.add(tn);
            }
        }
        return lReplace;
    }

    private TokenNode getTokenNode(TreeNode del, String newToken) {
        if (newToken == null || newToken.length() == 0) {
            return null;
        }
        List<TokenNode> lTokens = new ArrayList<TokenNode>();
        del.serialize(lTokens);
        if (lTokens.size() == 0) {
            return null;
        }
        Collections.sort(lTokens, new TokenNodeComparator());
        Token firstToken = lTokens.get(0).getToken();
        return new TokenNode("UNKNOWN", new Token(firstToken.getLine(), firstToken.getCol(), firstToken.getOffset(), newToken));
    }

    /**
	 * This Method is responsible for adjusting the token positions after
	 * removing parts of the tree. The changes, that occured are defined by the
	 * given Range.
	 * 
	 * @param node
	 * @param delRange
	 */
    private void adjustTokenPosition(TreeNode node, List<Range> delRange) {
        sortRange(delRange);
        List<TokenNode> serializedTokenNodes = serializeAndSort(node);
        if (delRange == null || delRange.size() == 0) {
            shiftTokenToLeftBorder(serializedTokenNodes);
            return;
        }
        int el = 0;
        int ec = 0;
        int preRangeLine = 0;
        for (Range range : delRange) {
            boolean colshift = false;
            if (range.getStartLine() != preRangeLine) {
                ec = 0;
            }
            preRangeLine = range.getEndLine();
            range.setStartLine(range.getStartLine() - el);
            range.setEndLine(range.getEndLine() - el);
            range.setStartCol(range.getStartCol() - ec);
            range.setEndCol(range.getEndCol() - ec);
            int lineMove = (range.getEndLine() - range.getStartLine());
            int colMove = (range.getEndCol() - range.getStartCol());
            for (TokenNode tokenNode : serializedTokenNodes) {
                Token token = tokenNode.getToken();
                if (token.getLine() == range.getStartLine() && token.getCol() == range.getStartCol()) {
                    String tokenText = token.getText();
                    if (tokenText.length() != 0) {
                        colMove -= token.getText().length();
                        colshift = true;
                    }
                } else if (token.getLine() == range.getEndLine()) {
                    colshift = true;
                    if (token.getCol() >= range.getEndCol()) {
                        token.setCol(token.getCol() - colMove);
                        token.setLine(token.getLine() - lineMove);
                    }
                } else if (token.getLine() > range.getEndLine()) {
                    if (colshift) {
                        token.setLine(token.getLine() - lineMove);
                    } else {
                        token.setLine(token.getLine() - (lineMove + 1));
                    }
                }
            }
            if (colshift) {
                el += lineMove;
            } else {
                el += lineMove + 1;
            }
            ec += colMove;
        }
        shiftTokenToLeftBorder(serializedTokenNodes);
    }

    /**
	 * Shifts all token to the left, so that the first token in the first line
	 * will start at column 1
	 * 
	 * @param tokenNodes
	 */
    private static void shiftTokenToLeftBorder(List<TokenNode> tokenNodes) {
        if (tokenNodes.size() > 0) {
            TokenNode tn = tokenNodes.get(0);
            int colShift = tn.getToken().getCol() - 1;
            for (TokenNode node : tokenNodes) {
                Token token = node.getToken();
                token.setCol(token.getCol() - colShift);
            }
        }
    }

    private static void sortRange(List<Range> delRange) {
        Collections.sort(delRange, new Comparator<Range>() {

            public int compare(Range r1, Range r2) {
                if ((r1.getStartLine() < r2.getStartLine()) || (r1.getStartLine() == r2.getStartLine() && r1.getStartCol() < r2.getStartCol())) {
                    return -1;
                }
                if ((r1.getStartLine() > r2.getStartLine()) || (r1.getStartLine() == r2.getStartLine() && r1.getStartCol() > r2.getStartCol())) {
                    return 1;
                }
                return 0;
            }
        });
    }

    /**
	 * Returns a Range object which holds information about the deleted part of
	 * the tree.
	 * 
	 * @param del
	 * @return
	 */
    private static Range getRange(TreeNode del) {
        List<TokenNode> token = new ArrayList<TokenNode>();
        token = serialize(del);
        Collections.sort(token, new TokenNodeComparator());
        if (token.size() == 0) {
            return null;
        }
        Token firstToken = token.get(0).getToken();
        Token lastToken = token.get(token.size() - 1).getToken();
        return new Range(firstToken.getLine(), firstToken.getCol(), lastToken.getEndLine(), lastToken.getEndCol());
    }

    private static String getName(TreeNode node) {
        TreeNode parent = node.getParent();
        int count = 0;
        for (TreeNode child : parent.getChildren()) {
            if (child.getName().equals(node.getName())) {
                if (child.getSiblingIndex() != node.getSiblingIndex()) {
                    count++;
                } else {
                    break;
                }
            }
        }
        return node.getName() + "[" + count + "]";
    }

    /**
	 * Returns a sorted list of TokenNodes from a given TreeNode
	 * 
	 * @param node
	 * @return
	 */
    private static List<TokenNode> serializeAndSort(TreeNode node) {
        List<TokenNode> result = serialize(node);
        Collections.sort(result, new TokenNodeComparator());
        return result;
    }

    /**
	 * Returns an unsorted list of TokenNodes from a given TreeNode.
	 * 
	 * @param node
	 * @return
	 */
    private static List<TokenNode> serialize(TreeNode node) {
        List<TokenNode> result = new ArrayList<TokenNode>();
        if (node instanceof TokenNode) {
            result.add((TokenNode) node);
        }
        for (TreeNode n : node.getChildren()) {
            result.addAll(serialize(n));
        }
        return result;
    }
}
