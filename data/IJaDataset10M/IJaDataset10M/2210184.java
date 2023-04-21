package org.rubypeople.rdt.internal.ti;

import java.util.LinkedList;
import java.util.List;
import org.jruby.ast.ArgumentNode;
import org.jruby.ast.BlockNode;
import org.jruby.ast.CallNode;
import org.jruby.ast.ClassNode;
import org.jruby.ast.Colon2Node;
import org.jruby.ast.ConstNode;
import org.jruby.ast.DVarNode;
import org.jruby.ast.DefnNode;
import org.jruby.ast.DefsNode;
import org.jruby.ast.GlobalAsgnNode;
import org.jruby.ast.GlobalVarNode;
import org.jruby.ast.InstAsgnNode;
import org.jruby.ast.InstVarNode;
import org.jruby.ast.LocalAsgnNode;
import org.jruby.ast.LocalVarNode;
import org.jruby.ast.Node;
import org.jruby.ast.VCallNode;
import org.jruby.lexer.yacc.ISourcePosition;
import org.jruby.lexer.yacc.SourcePosition;
import org.rubypeople.rdt.internal.core.parser.RdtWarnings;
import org.rubypeople.rdt.internal.core.parser.RubyParser;
import org.rubypeople.rdt.internal.ti.util.FirstPrecursorNodeLocator;
import org.rubypeople.rdt.internal.ti.util.INodeAcceptor;
import org.rubypeople.rdt.internal.ti.util.OffsetNodeLocator;
import org.rubypeople.rdt.internal.ti.util.ScopedNodeLocator;

public class DefaultReferenceFinder implements IReferenceFinder {

    public List<ISourcePosition> findReferences(String source, int offset) {
        List<ISourcePosition> references = new LinkedList<ISourcePosition>();
        RubyParser parser = new RubyParser(new RdtWarnings());
        Node root = parser.parse(source);
        Node orig = OffsetNodeLocator.Instance().getNodeAtOffset(root, offset);
        System.out.println("Origin: " + orig.getClass().getName());
        if (isLocalVarRef(orig)) {
            pushLocalVarRefs(root, orig, references);
        }
        if (isInstanceVarRef(orig)) {
            pushInstVarRefs(root, orig, references);
        }
        if (isGlobalVarRef(orig)) {
            pushGlobalVarRefs(root, orig, references);
        }
        if (orig instanceof ConstNode) {
            pushConstRefs(root, orig, references);
        }
        return references;
    }

    private ISourcePosition getPositionOfName(Node node, Node scope) {
        ISourcePosition pos = node.getPosition();
        String name = null;
        if (isLocalVarRef(node)) {
            name = getLocalVarRefName(node, scope);
        }
        if (isInstanceVarRef(node)) {
            name = getInstVarRefName(node, scope);
        }
        if (isGlobalVarRef(node)) {
            name = getGlobalVarRefName(node);
        }
        if (node instanceof ConstNode) {
            name = ((ConstNode) node).getName();
        }
        if (name == null) {
            System.err.println("Couldn't get the name for: " + node.toString() + " in " + scope.toString());
        }
        return new SourcePosition(pos.getFile(), pos.getStartLine(), pos.getEndLine(), pos.getStartOffset(), pos.getStartOffset() + name.length());
    }

    /**
	 * Returns the name of a local var ref (LocalAsgnNode, ArgumentNode, LocalVarNode)
	 * @param node Node to get the name of
	 * @param scope Enclosing scope (to scrape args, etc.)
	 * @return
	 */
    private String getLocalVarRefName(Node node, Node scope) {
        if (node instanceof LocalAsgnNode) {
            return ((LocalAsgnNode) node).getName();
        }
        if (node instanceof ArgumentNode) {
            return ((ArgumentNode) node).getName();
        }
        if (node instanceof LocalVarNode) {
            if (scope instanceof DefnNode) {
                return ((DefnNode) scope).getBodyNode().getLocalNames()[((LocalVarNode) node).getCount()];
            }
            if (scope instanceof DefsNode) {
                return ((DefsNode) scope).getBodyNode().getLocalNames()[((LocalVarNode) node).getCount()];
            }
            final int localVarCount = ((LocalVarNode) node).getCount();
            Node previousAssign = FirstPrecursorNodeLocator.Instance().findFirstPrecursor(scope, node.getPosition().getStartOffset(), new INodeAcceptor() {

                public boolean doesAccept(Node node) {
                    if (node instanceof LocalAsgnNode) {
                        return ((LocalAsgnNode) node).getCount() == localVarCount;
                    }
                    return false;
                }
            });
            if (previousAssign != null) {
                return ((LocalAsgnNode) previousAssign).getName();
            }
            System.err.println("Unhandled scope for local var ref node found: " + scope.toString());
        }
        if (node instanceof DVarNode) {
            return ((DVarNode) node).getName();
        }
        return null;
    }

    private String getClassNodeName(ClassNode classNode) {
        if (classNode.getCPath() instanceof Colon2Node) {
            Colon2Node c2node = (Colon2Node) classNode.getCPath();
            return c2node.getName();
        }
        System.err.println("ClassNode.getCPath() returned other than Colon2Node: " + classNode.toString());
        return null;
    }

    private String getInstVarRefName(Node node, Node scope) {
        if (node instanceof InstAsgnNode) {
            return ((InstAsgnNode) node).getName();
        }
        if (node instanceof ArgumentNode) {
            return ((InstAsgnNode) node).getName();
        }
        if (node instanceof InstVarNode) {
            return ((InstVarNode) node).getName();
        }
        if (node instanceof DVarNode) {
            return ((DVarNode) node).getName();
        }
        return null;
    }

    private String getGlobalVarRefName(Node node) {
        if (node instanceof GlobalVarNode) {
            return ((GlobalVarNode) node).getName();
        }
        if (node instanceof GlobalAsgnNode) {
            return ((GlobalAsgnNode) node).getName();
        }
        return null;
    }

    private String getMethodRefName(Node node) {
        if (node instanceof DefnNode) {
            return ((DefnNode) node).getName();
        }
        if (node instanceof DefsNode) {
            return ((DefsNode) node).getName();
        }
        if (node instanceof CallNode) {
            return ((CallNode) node).getName();
        }
        if (node instanceof VCallNode) {
            return ((VCallNode) node).getMethodName();
        }
        return null;
    }

    private boolean isLocalVarRef(Node node) {
        return ((node instanceof LocalAsgnNode) || (node instanceof ArgumentNode) || (node instanceof LocalVarNode));
    }

    private boolean isInstanceVarRef(Node node) {
        return ((node instanceof InstAsgnNode) || (node instanceof InstVarNode));
    }

    private boolean isGlobalVarRef(Node node) {
        return ((node instanceof GlobalAsgnNode) || (node instanceof GlobalVarNode));
    }

    private boolean isMethodRefNode(Node node) {
        return ((node instanceof DefnNode) || (node instanceof DefsNode) || (node instanceof CallNode) || (node instanceof VCallNode));
    }

    private void pushLocalVarRefs(Node root, Node orig, List<ISourcePosition> references) {
        System.out.println("Finding references for a local variable " + orig.toString());
        Node searchSpace = FirstPrecursorNodeLocator.Instance().findFirstPrecursor(root, orig.getPosition().getStartOffset(), new INodeAcceptor() {

            public boolean doesAccept(Node node) {
                return ((node instanceof DefnNode) || (node instanceof DefsNode));
            }
        });
        if (searchSpace == null) {
            searchSpace = root;
        }
        final Node finalSearchSpace = searchSpace;
        final String origName = getLocalVarRefName(orig, searchSpace);
        List<Node> searchResults = ScopedNodeLocator.Instance().findNodesInScope(searchSpace, new INodeAcceptor() {

            public boolean doesAccept(Node node) {
                String name = getLocalVarRefName(node, finalSearchSpace);
                return (name != null && name.equals(origName));
            }
        });
        for (Node searchResult : searchResults) {
            references.add(getPositionOfName(searchResult, searchSpace));
        }
    }

    private void pushInstVarRefs(Node root, Node orig, List<ISourcePosition> references) {
        System.out.println("Finding references for an instance variable " + orig.toString());
        Node searchSpace;
        ClassNode enclosingClass = (ClassNode) FirstPrecursorNodeLocator.Instance().findFirstPrecursor(root, orig.getPosition().getStartOffset(), new INodeAcceptor() {

            public boolean doesAccept(Node node) {
                return (node instanceof ClassNode);
            }
        });
        if (enclosingClass == null) {
            searchSpace = root;
        } else {
            final String className = getClassNodeName(enclosingClass);
            List<Node> classNodes = ScopedNodeLocator.Instance().findNodesInScope(root, new INodeAcceptor() {

                public boolean doesAccept(Node node) {
                    if (node instanceof ClassNode) {
                        return getClassNodeName((ClassNode) node).equals(className);
                    }
                    return false;
                }
            });
            BlockNode blockNode = new BlockNode(new SourcePosition("", 0));
            for (Node classNode : classNodes) {
                blockNode.add(classNode);
            }
            searchSpace = blockNode;
        }
        final Node finalSearchSpace = searchSpace;
        final String origName = getInstVarRefName(orig, searchSpace);
        List<Node> searchResults = ScopedNodeLocator.Instance().findNodesInScope(searchSpace, new INodeAcceptor() {

            public boolean doesAccept(Node node) {
                if (isInstanceVarRef(node)) {
                    String name = getInstVarRefName(node, finalSearchSpace);
                    return (name != null && name.equals(origName));
                }
                return false;
            }
        });
        for (Node searchResult : searchResults) {
            references.add(getPositionOfName(searchResult, searchSpace));
        }
    }

    private void pushGlobalVarRefs(Node root, Node orig, List<ISourcePosition> references) {
        final Node searchSpace = root;
        final String origName = getGlobalVarRefName(orig);
        List<Node> searchResults = ScopedNodeLocator.Instance().findNodesInScope(searchSpace, new INodeAcceptor() {

            public boolean doesAccept(Node node) {
                return isGlobalVarRef(node) && getGlobalVarRefName(node).equals(origName);
            }
        });
        for (Node searchResult : searchResults) {
            references.add(getPositionOfName(searchResult, searchSpace));
        }
    }

    private void pushConstRefs(Node root, Node orig, List<ISourcePosition> references) {
        if (!(orig instanceof ConstNode)) {
            return;
        }
        final String matchName = ((ConstNode) orig).getName();
        List<Node> searchResults = ScopedNodeLocator.Instance().findNodesInScope(root, new INodeAcceptor() {

            public boolean doesAccept(Node node) {
                if (node instanceof ConstNode) {
                    return ((ConstNode) node).getName().equals(matchName);
                }
                return false;
            }
        });
        for (Node searchResult : searchResults) {
            references.add(getPositionOfName(searchResult, root));
        }
    }
}
