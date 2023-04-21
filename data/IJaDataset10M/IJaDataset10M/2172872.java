package net.sourceforge.sqlexplorer.sessiontree.model.utility;

/** Implementation of ternary search tree. A Ternary Search Tree is a data structure that behaves 
*   in a manner that is very similar to a HashMap.
*/
class TernarySearchTree {

    private TSTNode rootNode;

    private int defaultNumReturnValues = -1;

    private int lastNumberOfReturnValues;

    /** Stores value in the TernarySearchTree. The value may be retrieved using key.
	*   @param key A string that indexes the object to be stored.
	*   @param value The object to be stored in the tree.
	*/
    public void put(String key, Object value) {
        getOrCreateNode(key).data = value;
    }

    /**
	* Retrieve the object indexed by key.
	* @param       key A String index.
	* @return      Object The object retrieved from the TernarySearchTree.
	*/
    public Object get(String key) {
        TSTNode node = getNode(key);
        if (node == null) return null;
        return node.data;
    }

    /** Removes value indexed by key. Also removes all nodes that are rendered unnecessary by the removal of this data.
	*   @param key A string that indexes the object to be removed from the tree.
	*/
    public void remove(String key) {
        deleteNode(getNode(key));
    }

    /** Sets default maximum number of values returned from matchPrefix, matchPrefixString,
	*   matchAlmost, and matchAlmostString methods.
	*   @param num The number of values that will be returned when calling the methods above. Set 
	*   this to -1 to get an unlimited number of return values. Note that 
	*   the methods mentioned above provide overloaded versions that allow you to specify the maximum number of
	*   return values, in which case this value is temporarily overridden.
	*/
    public void setNumReturnValues(int num) {
        defaultNumReturnValues = (num < 0) ? -1 : num;
    }

    private int checkNumberOfReturnValues(int numReturnValues) {
        return ((numReturnValues < 0) ? -1 : numReturnValues);
    }

    /** Returns the number of values returned by the last call to matchAlmostString or matchPrefixString methods.
	*   (This is really just for the purposes of the demo applet.)
	*/
    public int getLastNumReturnValues() {
        return lastNumberOfReturnValues;
    }

    /** Returns the Node indexed by key, or null if that node doesn't exist. Search begins at root node.
	*   @param key An index that points to the desired node.
	*   @return TSTNode The node object indexed by key. This object is an instance of an inner class
	*   named TernarySearchTree.TSTNode.
	*/
    public TSTNode getNode(String key) {
        return getNode(key, rootNode);
    }

    /** Returns the Node indexed by key, or null if that node doesn't exist. Search begins at root node.
	*   @param key An index that points to the desired node.
	*   @param startNode The top node defining the subtree to be searched.
	*   @return TSTNode The node object indexed by key. This object is an instance of an inner class
	*   named TernarySearchTree.TSTNode.
	*/
    protected TSTNode getNode(String key, TSTNode startNode) {
        if (key == null || startNode == null || key.length() == 0) return null;
        TSTNode currentNode = startNode;
        int charIndex = 0;
        while (true) {
            if (currentNode == null) return null;
            int charComp = CharUtility.compareCharsAlphabetically(key.charAt(charIndex), currentNode.splitchar);
            if (charComp == 0) {
                charIndex++;
                if (charIndex == key.length()) return currentNode;
                currentNode = currentNode.relatives[TSTNode.EQKID];
            } else if (charComp < 0) {
                currentNode = currentNode.relatives[TSTNode.LOKID];
            } else {
                currentNode = currentNode.relatives[TSTNode.HIKID];
            }
        }
    }

    /** Returns alphabetical list of all keys in the tree that begin with prefix. Only keys for nodes having non-null data 
	*   are included in the list.
	*   @param prefix Each key returned from this method will begin with the characters in prefix.
	*   @return DoublyLinkedList An implementation of a LinkedList that is java 1.1 compatible.
	*/
    public DoublyLinkedList matchPrefix(String prefix) {
        return matchPrefix(prefix, defaultNumReturnValues);
    }

    /** Returns alphabetical list of all keys in the tree that begin with prefix. Only keys for nodes having non-null data 
	*   are included in the list.
	*   @param prefix Each key returned from this method will begin with the characters in prefix.
	*   @param numReturnValues The maximum number of values returned from this method.
	*   @return DoublyLinkedList An implementation of a LinkedList that is java 1.1 compatible.
	*/
    public DoublyLinkedList matchPrefix(String prefix, int numReturnValues) {
        sortKeysNumReturnValues = checkNumberOfReturnValues(numReturnValues);
        sortKeysResult = new DoublyLinkedList();
        TSTNode startNode = getNode(prefix);
        if (startNode == null) return sortKeysResult;
        if (startNode.data != null) {
            sortKeysResult.addLast(getKey(startNode));
            sortKeysNumReturnValues--;
        }
        sortKeysList = true;
        sortKeysRecursion(startNode.relatives[TSTNode.EQKID]);
        return sortKeysResult;
    }

    /** Returns alphabetical list of all keys in the tree that begin with prefix. Only keys for nodes having non-null data 
	*   are included in the list.
	*   @param prefix Each key returned from this method will begin with the characters in prefix.
	*   @return String A string representation of all keys matching the argument prefix. Keys are delimited with the newline
	*   char ('\n').
	*/
    public String matchPrefixString(String prefix) {
        return matchPrefixString(prefix, defaultNumReturnValues);
    }

    /** Returns alphabetical list of all keys in the tree that begin with prefix. Only keys for nodes having non-null data 
	*   are included in the list.
	*   @param prefix Each key returned from this method will begin with the characters in prefix.
	*   @param numReturnValues The maximum number of values returned from this method.
	*   @return String A string representation of all keys matching the argument prefix. Keys are delimited with the newline
	*   char ('\n').
	*/
    public String matchPrefixString(String prefix, int numReturnValues) {
        TSTNode startNode = getNode(prefix);
        if (startNode == null) return "";
        sortKeysNumReturnValues = checkNumberOfReturnValues(numReturnValues);
        lastNumberOfReturnValues = sortKeysNumReturnValues;
        sortKeysBuffer = new StringBuffer();
        if (startNode.data != null) {
            sortKeysBuffer.append(getKey(startNode) + "\n");
            sortKeysNumReturnValues--;
        }
        sortKeysList = false;
        sortKeysRecursion(startNode.relatives[TSTNode.EQKID]);
        int bufferLength = sortKeysBuffer.length();
        if (bufferLength > 0) sortKeysBuffer.setLength(bufferLength - 1);
        lastNumberOfReturnValues = lastNumberOfReturnValues - sortKeysNumReturnValues;
        return sortKeysBuffer.toString();
    }

    private DoublyLinkedList sortKeysResult;

    private boolean sortKeysList;

    private StringBuffer sortKeysBuffer;

    private int sortKeysNumReturnValues;

    private void sortKeysRecursion(TSTNode currentNode) {
        if (currentNode == null) return;
        sortKeysRecursion(currentNode.relatives[TSTNode.LOKID]);
        if (sortKeysNumReturnValues == 0) return;
        if (currentNode.data != null) {
            if (sortKeysList) {
                sortKeysResult.addLast(getKey(currentNode));
            } else {
                sortKeysBuffer.append(getKey(currentNode) + "\n");
            }
            sortKeysNumReturnValues--;
        }
        sortKeysRecursion(currentNode.relatives[TSTNode.EQKID]);
        sortKeysRecursion(currentNode.relatives[TSTNode.HIKID]);
    }

    /** Returns keys sorted in alphabetical order. Includes startNode and all nodes connected to startNode.
	*   Number of keys returned is limited to numReturnValues. To get a list that isn't limited in size,
	*   set numReturnValues to -1.
	*   @param startNode The top node defining the subtree to be searched.
	*   @param numReturnValues The maximum number of values returned from this method.
	*   @return DoublyLinkedList An implementation of a LinkedList that is java 1.1 compatible.
	*/
    protected DoublyLinkedList sortKeys(TSTNode startNode, int numReturnValues) {
        sortKeysNumReturnValues = checkNumberOfReturnValues(numReturnValues);
        sortKeysResult = new DoublyLinkedList();
        sortKeysRecursion(startNode);
        return sortKeysResult;
    }

    public String sortKeysString(int numReturnValues) {
        return sortKeysString(rootNode, numReturnValues);
    }

    public String sortKeysString() {
        return sortKeysString(rootNode, defaultNumReturnValues);
    }

    /** Returns keys sorted in alphabetical order, returning a result of type String. Includes startNode 
	*   and all nodes connected to startNode. Number of keys returned is limited to numReturnValues. To get 
	*   a list that isn't limited in size, set numReturnValues to -1.
	*   @param startNode The top node defining the subtree to be searched.
	*   @param numReturnValues The maximum number of values returned from this method.
	*   @return String A string representation of keys in alphabetical order.
	*/
    public String sortKeysString(TSTNode startNode, int numReturnValues) {
        if (startNode == null) return new String("");
        sortKeysNumReturnValues = checkNumberOfReturnValues(numReturnValues);
        lastNumberOfReturnValues = sortKeysNumReturnValues;
        sortKeysBuffer = new StringBuffer();
        if (startNode.data != null) {
            sortKeysBuffer.append(getKey(startNode) + "\n");
            sortKeysNumReturnValues--;
        }
        sortKeysList = false;
        sortKeysRecursion(startNode);
        int bufferLength = sortKeysBuffer.length();
        if (bufferLength > 0) sortKeysBuffer.setLength(bufferLength - 1);
        lastNumberOfReturnValues = lastNumberOfReturnValues - sortKeysNumReturnValues;
        return sortKeysBuffer.toString();
    }

    private DoublyLinkedList matchAlmostResult;

    private StringBuffer matchAlmostBuffer;

    private boolean matchAlmostListAction;

    private int matchAlmostNumReturnValues;

    private String matchAlmostKey;

    private int matchAlmostDiff;

    private int maxMatchAlmostDiff = 4;

    /** Returns a list of keys that almost match argument key.
	*   Keys returned will have exactly diff characters that do not match the target key,
	*   where diff is equal to the last value passed in as an argument to the setMatchAlmostDiff 
	*   method. If the matchAlmost method is called before the setMatchAlmostDiff method has been
	*   called for the first time, then diff = 0.
	*   @param key The target key.
	*   @return DoublyLinkedList An implementation of a LinkedList that is java 1.1 compatible.
	*/
    public DoublyLinkedList matchAlmost(String key) {
        return matchAlmost(key, defaultNumReturnValues);
    }

    /** Returns a list of keys that almost match argument key.
	*   Keys returned will have exactly diff characters that do not match the target key,
	*   where diff is equal to the last value passed in as an argument to the setMatchAlmostDiff 
	*   method. If the matchAlmost method is called before the setMatchAlmostDiff method has been
	*   called for the first time, then diff = 0.
	*   @param key The target key.
	*   @param numReturnValues The maximum number of values returned by this method.
	*   @return DoublyLinkedList An implementation of a LinkedList that is java 1.1 compatible.
	*/
    protected DoublyLinkedList matchAlmost(String key, int numReturnValues) {
        matchAlmostListAction = true;
        matchAlmostNumReturnValues = checkNumberOfReturnValues(numReturnValues);
        matchAlmostResult = new DoublyLinkedList();
        matchAlmostKey = key;
        matchAlmostRecursion(rootNode, 0, matchAlmostDiff);
        return matchAlmostResult;
    }

    /** Returns a String representation of keys that almost match argument key.
	*   Keys returned will have exactly diff characters that do not match the target key,
	*   where diff is equal to the last value passed in as an argument to the setMatchAlmostDiff 
	*   method. If the matchAlmost method is called before the setMatchAlmostDiff method has been
	*   called for the first time, then diff = 0.
	*   @param key The target key.
	*   @return String A String representation of keys that almost match the target key. Keys are
	*   delimited by the newline char ('\n').
	*/
    public String matchAlmostString(String key) {
        return matchAlmostString(key, defaultNumReturnValues);
    }

    /** Returns a String representation of keys that almost match argument key.
	*   Keys returned will have exactly diff characters that do not match the target key,
	*   where diff is equal to the last value passed in as an argument to the setMatchAlmostDiff 
	*   method. If the matchAlmost method is called before the setMatchAlmostDiff method has been
	*   called for the first time, then diff = 0.
	*   @param key The target key.
	*   @param numReturnValues The maximum number of values returned by this method.
	*   @return String A String representation of keys that almost match the target key. Keys are
	*   delimited by the newline char ('\n').
	*/
    protected String matchAlmostString(String key, int numReturnValues) {
        matchAlmostListAction = false;
        matchAlmostNumReturnValues = checkNumberOfReturnValues(numReturnValues);
        lastNumberOfReturnValues = matchAlmostNumReturnValues;
        matchAlmostBuffer = new StringBuffer();
        matchAlmostKey = key;
        matchAlmostRecursion(rootNode, 0, matchAlmostDiff);
        int bufferLength = matchAlmostBuffer.length();
        if (bufferLength > 0) matchAlmostBuffer.setLength(bufferLength - 1);
        lastNumberOfReturnValues = lastNumberOfReturnValues - matchAlmostNumReturnValues;
        return matchAlmostBuffer.toString();
    }

    private void matchAlmostRecursion(TSTNode currentNode, int charIndex, int d) {
        if ((currentNode == null) || (d < 0) || (matchAlmostNumReturnValues == 0) || (charIndex >= matchAlmostKey.length())) return;
        int charComp = CharUtility.compareCharsAlphabetically(matchAlmostKey.charAt(charIndex), currentNode.splitchar);
        if ((d > 0) || (charComp < 0)) matchAlmostRecursion(currentNode.relatives[TSTNode.LOKID], charIndex, d);
        int nextD = (charComp == 0) ? d : d - 1;
        if ((matchAlmostKey.length() == charIndex + 1) && (nextD == 0) && (currentNode.data != null)) {
            if (matchAlmostListAction) {
                matchAlmostResult.addLast(getKey(currentNode));
            } else {
                matchAlmostBuffer.append(getKey(currentNode) + "\n");
            }
            matchAlmostNumReturnValues--;
        }
        matchAlmostRecursion(currentNode.relatives[TSTNode.EQKID], charIndex + 1, nextD);
        if ((d > 0) || (charComp > 0)) matchAlmostRecursion(currentNode.relatives[TSTNode.HIKID], charIndex, d);
    }

    /** Sets the number of characters by which words can differ from target word when 
	*   calling matchAlmost or matchAlmostString methods. Arguments 
	*   less than 1 will set the char difference to 1, and arguments greater than 4 
	*   will set the char difference to 4.
	*   @param diff The number of characters by which words can differ from target word.
	*/
    public void setMatchAlmostDiff(int diff) {
        if (diff < 0) {
            matchAlmostDiff = 0;
        } else if (diff > maxMatchAlmostDiff) {
            matchAlmostDiff = maxMatchAlmostDiff;
        } else {
            matchAlmostDiff = diff;
        }
    }

    /** Returns the Node indexed by key, creating that node if it doesn't exist, and creating any required.
	*   intermediate nodes if they don't exist.
	*   @param key A string that indexes the node that is returned.
	*   @return TSTNode The node object indexed by key. This object is an instance of an inner class
	*   named TernarySearchTree.TSTNode.
	*/
    protected TSTNode getOrCreateNode(String key) throws NullPointerException, IllegalArgumentException {
        if (key == null) throw new NullPointerException("attempt to get or create node with null key");
        if (key.length() == 0) throw new IllegalArgumentException("attempt to get or create node with key of zero length");
        if (rootNode == null) rootNode = new TSTNode(key.charAt(0), null);
        TSTNode currentNode = rootNode;
        int charIndex = 0;
        while (true) {
            int charComp = CharUtility.compareCharsAlphabetically(key.charAt(charIndex), currentNode.splitchar);
            if (charComp == 0) {
                charIndex++;
                if (charIndex == key.length()) return currentNode;
                if (currentNode.relatives[TSTNode.EQKID] == null) currentNode.relatives[TSTNode.EQKID] = new TSTNode(key.charAt(charIndex), currentNode);
                currentNode = currentNode.relatives[TSTNode.EQKID];
            } else if (charComp < 0) {
                if (currentNode.relatives[TSTNode.LOKID] == null) currentNode.relatives[TSTNode.LOKID] = new TSTNode(key.charAt(charIndex), currentNode);
                currentNode = currentNode.relatives[TSTNode.LOKID];
            } else {
                if (currentNode.relatives[TSTNode.HIKID] == null) currentNode.relatives[TSTNode.HIKID] = new TSTNode(key.charAt(charIndex), currentNode);
                currentNode = currentNode.relatives[TSTNode.HIKID];
            }
        }
    }

    private void deleteNode(TSTNode nodeToDelete) {
        if (nodeToDelete == null) return;
        nodeToDelete.data = null;
        while (nodeToDelete != null) {
            nodeToDelete = deleteNodeRecursion(nodeToDelete);
        }
    }

    private TSTNode deleteNodeRecursion(TSTNode currentNode) {
        if (currentNode == null) return null;
        if (currentNode.relatives[TSTNode.EQKID] != null || currentNode.data != null) return null;
        TSTNode currentParent = currentNode.relatives[TSTNode.PARENT];
        boolean lokidNull = currentNode.relatives[TSTNode.LOKID] == null;
        boolean hikidNull = currentNode.relatives[TSTNode.HIKID] == null;
        int childType;
        if (currentParent.relatives[TSTNode.LOKID] == currentNode) {
            childType = TSTNode.LOKID;
        } else if (currentParent.relatives[TSTNode.EQKID] == currentNode) {
            childType = TSTNode.EQKID;
        } else if (currentParent.relatives[TSTNode.HIKID] == currentNode) {
            childType = TSTNode.HIKID;
        } else {
            rootNode = null;
            return null;
        }
        if (lokidNull && hikidNull) {
            currentParent.relatives[childType] = null;
            return currentParent;
        }
        if (lokidNull) {
            currentParent.relatives[childType] = currentNode.relatives[TSTNode.HIKID];
            currentNode.relatives[TSTNode.HIKID].relatives[TSTNode.PARENT] = currentParent;
            return currentParent;
        }
        if (hikidNull) {
            currentParent.relatives[childType] = currentNode.relatives[TSTNode.LOKID];
            currentNode.relatives[TSTNode.LOKID].relatives[TSTNode.PARENT] = currentParent;
            return currentParent;
        }
        int deltaHi = currentNode.relatives[TSTNode.HIKID].splitchar - currentNode.splitchar;
        int deltaLo = currentNode.splitchar - currentNode.relatives[TSTNode.LOKID].splitchar;
        int movingKid;
        TSTNode targetNode;
        if (deltaHi == deltaLo) {
            if (Math.random() < 0.5) {
                deltaHi++;
            } else {
                deltaLo++;
            }
        }
        if (deltaHi > deltaLo) {
            movingKid = TSTNode.HIKID;
            targetNode = currentNode.relatives[TSTNode.LOKID];
        } else {
            movingKid = TSTNode.LOKID;
            targetNode = currentNode.relatives[TSTNode.HIKID];
        }
        while (targetNode.relatives[movingKid] != null) targetNode = targetNode.relatives[movingKid];
        targetNode.relatives[movingKid] = currentNode.relatives[movingKid];
        currentParent.relatives[childType] = targetNode;
        targetNode.relatives[TSTNode.PARENT] = currentParent;
        if (!lokidNull) currentNode.relatives[TSTNode.LOKID] = null;
        if (!hikidNull) currentNode.relatives[TSTNode.HIKID] = null;
        return currentParent;
    }

    /** An inner class of TernarySearchTree that represents a node in the tree.
	*/
    protected class TSTNode {

        protected static final int PARENT = 0, LOKID = 1, EQKID = 2, HIKID = 3;

        protected char splitchar;

        protected TSTNode[] relatives = new TSTNode[4];

        protected Object data;

        protected TSTNode(char splitchar, TSTNode parent) {
            this.splitchar = splitchar;
            relatives[PARENT] = parent;
        }
    }

    private StringBuffer getKeyBuffer = new StringBuffer();

    /** Returns the key that indexes the node argument.
	*   @param node The node whose index is to be calculated.
	*   @return String The string that indexes the node argument.
	*/
    protected String getKey(TSTNode node) {
        getKeyBuffer.setLength(0);
        getKeyBuffer.append(node.splitchar);
        TSTNode currentNode, lastNode;
        currentNode = node.relatives[TSTNode.PARENT];
        lastNode = node;
        while (currentNode != null) {
            if (currentNode.relatives[TSTNode.EQKID] == lastNode) getKeyBuffer.append(currentNode.splitchar);
            lastNode = currentNode;
            currentNode = currentNode.relatives[TSTNode.PARENT];
        }
        getKeyBuffer.reverse();
        return getKeyBuffer.toString();
    }

    private int numNodes;

    private boolean checkData;

    /** Returns the total number of nodes in the tree. Counts nodes whether or not they have data.
	*   @return int The total number of nodes in the tree.
	*/
    public int numNodes() {
        return numNodes(rootNode);
    }

    /** Returns the total number of nodes in the subtree below and including startingNode. Counts nodes whether or not they have data.
	*   @param startingNode The top node of the subtree. The node that defines the subtree.
	*   @return int The total number of nodes in the subtree.
	*/
    protected int numNodes(TSTNode startingNode) {
        numNodes = 0;
        checkData = false;
        recursiveNodeCalculator(startingNode);
        return numNodes;
    }

    /** Returns the number of nodes in the tree that have non-null data.
	*   @return int The number of nodes in the tree that have non-null data.
	*/
    public int numDataNodes() {
        return numDataNodes(rootNode);
    }

    /** Returns the number of nodes in the subtree below and including startingNode. Counts only nodes that have non-null data.
	*   @param startingNode The top node of the subtree. The node that defines the subtree.
	*   @return int The total number of nodes in the subtree.
	*/
    protected int numDataNodes(TSTNode startingNode) {
        numNodes = 0;
        checkData = true;
        recursiveNodeCalculator(startingNode);
        return numNodes;
    }

    private void recursiveNodeCalculator(TSTNode currentNode) {
        if (currentNode == null) return;
        recursiveNodeCalculator(currentNode.relatives[TSTNode.LOKID]);
        recursiveNodeCalculator(currentNode.relatives[TSTNode.EQKID]);
        recursiveNodeCalculator(currentNode.relatives[TSTNode.HIKID]);
        if (checkData) {
            if (currentNode.data != null) numNodes++;
        } else {
            numNodes++;
        }
    }

    /** Prints entire tree structure to standard output, beginning with the root node and workind down.
	*/
    protected void printTree() {
        System.out.println("");
        if (rootNode == null) {
            System.out.println("tree is empty");
            return;
        }
        System.out.println("Here's the entire tree structure:");
        printNodeRecursion(rootNode);
    }

    /** Prints subtree structure to standard output, beginning with startingNode and workind down.
	*/
    protected void printTree(TSTNode startingNode) {
        System.out.println("");
        if (rootNode == null) {
            System.out.println("subtree is empty");
            return;
        }
        System.out.println("Here's the entire subtree structure:");
        printNodeRecursion(startingNode);
    }

    /** Recursive method used to print out tree or subtree structure.
	*/
    private void printNodeRecursion(TSTNode currentNode) {
        if (currentNode == null) return;
        System.out.println("");
        System.out.println("( keys are delimited by vertical lines: |example key| )");
        System.out.println("info for node   |" + getKey(currentNode) + "|         node data: " + currentNode.data);
        if (currentNode.relatives[TSTNode.PARENT] == null) {
            System.out.println("parent null");
        } else {
            System.out.println("parent key   |" + getKey(currentNode.relatives[TSTNode.PARENT]) + "|       parent data: " + currentNode.relatives[TSTNode.PARENT].data);
        }
        if (currentNode.relatives[TSTNode.LOKID] == null) {
            System.out.println("lokid null");
        } else {
            System.out.println("lokid key   |" + getKey(currentNode.relatives[TSTNode.LOKID]) + "|       lo kid data: " + currentNode.relatives[TSTNode.LOKID].data);
        }
        if (currentNode.relatives[TSTNode.EQKID] == null) {
            System.out.println("eqkid null");
        } else {
            System.out.println("eqkid key   |" + getKey(currentNode.relatives[TSTNode.EQKID]) + "|       equal kid data: " + currentNode.relatives[TSTNode.EQKID].data);
        }
        if (currentNode.relatives[TSTNode.HIKID] == null) {
            System.out.println("hikid null");
        } else {
            System.out.println("hikid key   |" + getKey(currentNode.relatives[TSTNode.HIKID]) + "|       hi kid data: " + currentNode.relatives[TSTNode.HIKID].data);
        }
        printNodeRecursion(currentNode.relatives[TSTNode.LOKID]);
        printNodeRecursion(currentNode.relatives[TSTNode.EQKID]);
        printNodeRecursion(currentNode.relatives[TSTNode.HIKID]);
    }
}
