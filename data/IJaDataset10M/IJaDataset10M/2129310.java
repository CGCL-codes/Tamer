package net.sourceforge.djindent.parser.symboltree;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.xerces.parsers.DOMParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import net.sourceforge.djindent.parser.symboltree.exceptions.SymbolTreeBuilderException;

public class XMLSymbolTreeBuilder implements SymbolTreeBuilder {

    private String m_sourceString;

    public static void main(String args[]) {
        try {
            long currentTime = System.currentTimeMillis();
            SymbolTreeBuilder thisBuilder = new XMLSymbolTreeBuilder("/properties/djindent/symbol.xml");
            thisBuilder.build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * All that this constructor does is to save the name of the file that the xml will eventually
     * be loaded from when build is eventually called
     * @param a_sourceString This is the file name for the XML
     */
    public XMLSymbolTreeBuilder(String a_sourceString) {
        m_sourceString = a_sourceString;
    }

    /**
     * This method will construct a new symbol tree from whatever underlying data source it is implemented
     * on top of.
     * @return SymbolTree This is the newly created SymbolTree.
     */
    public SymbolTree build() throws SymbolTreeBuilderException {
        SymbolTree newSymbolTree = new SymbolTree();
        InputSource xmlFile = getInputSource();
        DOMParser dom = createDOM(xmlFile);
        parseSymbolTable(dom.getDocument(), newSymbolTree);
        return newSymbolTree;
    }

    /**
     * This method is used to create a dom from an input source
     * @return DOMParser This is the dom parser that was created
     * @throws SymbolTreeBuilderException This is the exception that is thrown if the dom parser can not be created
     */
    private DOMParser createDOM(InputSource a_inputSource) throws SymbolTreeBuilderException {
        try {
            DOMParser toReturn = new DOMParser();
            toReturn.parse(a_inputSource);
            return toReturn;
        } catch (SAXException saxe) {
            throw new SymbolTreeBuilderException(saxe);
        } catch (IOException ioe) {
            throw new SymbolTreeBuilderException(ioe);
        }
    }

    /**
     * This method is used to retrieve an attribute from an element and throw an exception if that attribute
     * is not found
     * @param a_element This is the elment that the attribute is being retrieved from
     * @param a_attributeNameString This is the name of the attribute that you are looking for
     * @return String This is the found attribute
     * @throws SymbolTreeBuilderException This is the method that is thrown if no attribute can be found
     */
    private String getAttribute(Element a_element, String a_attributeNameString) throws SymbolTreeBuilderException {
        String toReturn = a_element.getAttribute(a_attributeNameString);
        if (toReturn == null) throw new SymbolTreeBuilderException("Element " + a_element + " does not contain a " + a_attributeNameString + " attribute");
        return toReturn;
    }

    /**
     * This method is used to turn the file into something that the xml parser can deal with
     * @param InputSource This is the xerces dependent input source that it requires to read the file
     */
    private InputSource getInputSource() throws SymbolTreeBuilderException {
        InputSource toReturn = null;
        try {
            InputStream file = getClass().getResourceAsStream(m_sourceString);
            if (file == null) {
                throw new IOException("Unknown file: " + m_sourceString);
            }
            toReturn = new InputSource(new InputStreamReader(file));
        } catch (IOException ioe) {
            throw new SymbolTreeBuilderException(ioe);
        }
        return toReturn;
    }

    /**
     * This is the method that is used to take a single character node( with no sub nodes ) and turn it
     * into a CharacterSymbol.  It does this by parsing out the num and multiplicty attributes and then getting
     * the character.
     * @param a_characterNode This is the node that represents the character
     * @return CharacterSymbol This is the character symbol that was just created from the character node
     * @throws SymbolTreeBuilderException This is what is thrown if there is a problem
     */
    private CharacterSymbol parseCharacter(Node a_characterNode) throws SymbolTreeBuilderException {
        CharacterSymbol toReturn = null;
        String number = getAttribute((Element) a_characterNode, "num");
        String repeat = getAttribute((Element) a_characterNode, "repeat");
        String text = ((Text) a_characterNode.getFirstChild()).getData();
        int repeatI = 1;
        int numberI = Integer.parseInt(number);
        if ("*".equals(repeat)) {
            repeatI = Integer.MAX_VALUE;
        } else {
            repeatI = Integer.parseInt(repeat);
        }
        toReturn = new CharacterSymbol(text, numberI, repeatI);
        return toReturn;
    }

    /**
     * This is the method that is used to parse a symbol node.  The symbol node is what contains all of the
     * characters and the implementation details of the actual symbol.  This method is used to parse that
     * @param a_symbolNode This is the node that contains the symbol
     * @param a_symbolTree This is the symbol tree that is being built
     * @throws SymbolTreeBuilderException This is the exception that is thrown if there is a problems
     */
    private void parseSymbolNode(Node a_symbolNode, SymbolTree a_symbolTree) throws SymbolTreeBuilderException {
        try {
            ArrayList characterList = new ArrayList(10);
            String type = null;
            String description = null;
            NodeList symbolChildNodes = a_symbolNode.getChildNodes();
            Node currentNode;
            int currentNodeNum = 0;
            Comparator characterSort = new CharNumComparator();
            while ((currentNode = symbolChildNodes.item(currentNodeNum++)) != null) {
                if ("character".equals(currentNode.getNodeName())) {
                    CharacterSymbol thisCharacter = parseCharacter(currentNode);
                    characterList.add(thisCharacter);
                } else if ("type".equals(currentNode.getNodeName())) {
                    type = ((Text) currentNode.getFirstChild()).getData();
                } else if ("description".equals(currentNode.getNodeName())) {
                    description = ((Text) currentNode.getFirstChild()).getData();
                }
            }
            int numCharacters = characterList.size();
            if (numCharacters > 0) {
                Collections.sort(characterList, characterSort);
            }
            boolean debug = false;
            for (int currentCharacter = 0; currentCharacter < numCharacters; currentCharacter++) {
                boolean isFirstCharacter = (currentCharacter == 0);
                boolean isLeaf = (currentCharacter == (numCharacters - 1));
                CharacterSymbol thisCharacter = (CharacterSymbol) characterList.get(currentCharacter);
                if (thisCharacter != null) {
                    String text = thisCharacter.getCharacter();
                    if (debug) {
                        System.out.println(text + " - " + type + " - " + description);
                    }
                    if (text.length() > 1) {
                        int textSize = text.length();
                        String[] multipleCharacters = new String[textSize];
                        for (int thisTextChar = 0; thisTextChar < textSize; thisTextChar++) {
                            if (thisTextChar != textSize - 1) {
                                multipleCharacters[thisTextChar] = text.substring(thisTextChar, thisTextChar + 1);
                            } else {
                                multipleCharacters[thisTextChar] = text.substring(thisTextChar);
                            }
                        }
                        if (isFirstCharacter && isLeaf) {
                            if (debug) {
                                System.out.println("1");
                            }
                            a_symbolTree.addNewSymbol(multipleCharacters, thisCharacter.getMultiplicity(), type, description);
                        } else if (isFirstCharacter) {
                            if (debug) {
                                System.out.println("2");
                            }
                            a_symbolTree.addNewSymbol(multipleCharacters, thisCharacter.getMultiplicity());
                        } else if (isLeaf) {
                            if (debug) {
                                System.out.println("3");
                            }
                            a_symbolTree.addNextSymbol(multipleCharacters, thisCharacter.getMultiplicity(), type, description);
                        } else {
                            if (debug) {
                                System.out.println("4");
                            }
                            a_symbolTree.addNewSymbol(multipleCharacters, thisCharacter.getMultiplicity());
                        }
                    } else {
                        if (isFirstCharacter && isLeaf) {
                            if (debug) {
                                System.out.println("5");
                            }
                            a_symbolTree.addNewSymbol(text, thisCharacter.getMultiplicity(), type, description);
                        } else if (isFirstCharacter) {
                            if (debug) {
                                System.out.println("6");
                            }
                            a_symbolTree.addNewSymbol(text, thisCharacter.getMultiplicity());
                        } else if (isLeaf) {
                            if (debug) {
                                System.out.println("7");
                            }
                            a_symbolTree.addNextSymbol(text, thisCharacter.getMultiplicity(), type, description);
                        } else {
                            if (debug) {
                                System.out.println("8");
                            }
                            a_symbolTree.addNextSymbol(text, thisCharacter.getMultiplicity());
                        }
                    }
                }
            }
        } catch (SymbolTreeBuilderException stbe) {
            stbe.printStackTrace();
        }
    }

    /**
     * This is the method that is used to begin the parsing of the xml symbol table.  The first tag
     * is always symbol_table, this method is responsible for parsing that and then finding the types of
     * all of the child tags and passing them along to the correct methods
     * @param a_document This is the document that is being parsed
     * @param a_symbolTree This is the symbolTree that is being created
     * @throws SymbolTreeBuilderException This is the exception that is thrown if there is a problem
     */
    private void parseSymbolTable(Document a_document, SymbolTree a_symbolTree) throws SymbolTreeBuilderException {
        NodeList rootNodes = a_document.getElementsByTagName("symbol_table");
        Node root;
        int rootNodeNum = 0;
        while ((root = rootNodes.item(rootNodeNum++)) != null) {
            NodeList symbolNodes = root.getChildNodes();
            Node currentSymbolNode;
            int currentSymbolNodeNum = 0;
            while ((currentSymbolNode = symbolNodes.item(currentSymbolNodeNum++)) != null) {
                if ("symbol".equals(currentSymbolNode.getNodeName())) {
                    parseSymbolNode(currentSymbolNode, a_symbolTree);
                }
            }
        }
    }
}

/**
 * This is a class that is used to represent a character xml tag that is read
 */
class CharacterSymbol {

    private int m_numberI;

    private int m_multiplicityI;

    private String m_characterString;

    /**
     * This is the constructor.  All that it does is to save the data for later use
     * @param a_characterString This is the character itself
     * @param a_numberI This is the number of the character
     * @param a_multiplictyI This is the number of times that the given character should appear
     */
    public CharacterSymbol(String a_characterString, int a_numberI, int a_multiplicityI) {
        m_characterString = a_characterString;
        m_numberI = a_numberI;
        m_multiplicityI = a_multiplicityI;
    }

    /**
     * This is used to retrieve the character that is associated with this character tag
     * @return String This will always be a one character long string
     */
    public String getCharacter() {
        return m_characterString;
    }

    /**
     * This is used to retrieve the multiplicty of this character, or how many times it should
     * occur.
     * @return int This is the multiplicty
     */
    public int getMultiplicity() {
        return m_multiplicityI;
    }

    /**
     * This is used to return the number of this character.  Normally in a symbol characters are
     * arranged in some order, indicated by a number.  This is that number
     * @return int The number of the character
     */
    public int getNumber() {
        return m_numberI;
    }
}

/**
 * This is the comparator that is used to compare CharacterSymbols so that their numbers
 * are sorted in the correct order
 */
class CharNumComparator implements Comparator {

    /**
     * This is the empty constructor
     */
    public CharNumComparator() {
    }

    /**
     * This method is used to compare two objects to each other.  It will always return -1 if
     * both of the objects are not CharacterSymbol's.  What it does is to compare the numbers
     * of both objects and if the first object > second Object it returns 1, if first object < 
     * second Object it returns -1 else it returns 0;
     * @param a_firstObject This is the first CharacterSymbol
     * @param a_secondObject This is the second CharacterSymbol
     * @return int This is the compare result
     */
    public int compare(Object a_firstObject, Object a_secondObject) {
        int toReturn = -1;
        if ((a_firstObject instanceof CharacterSymbol) && (a_secondObject instanceof CharacterSymbol)) {
            int firstNum = ((CharacterSymbol) a_firstObject).getNumber();
            int secondNum = ((CharacterSymbol) a_secondObject).getNumber();
            if (firstNum > secondNum) {
                toReturn = 1;
            } else if (firstNum == secondNum) {
                toReturn = 0;
            }
        }
        return toReturn;
    }

    /**
     * This determines if an object is equal to this comparator
     * @param a_object This is the object that is being compared to this comparator
     * @return boolean This is whether or not they are equal.
     */
    public boolean equals(Object a_object) {
        return super.equals(a_object);
    }
}
