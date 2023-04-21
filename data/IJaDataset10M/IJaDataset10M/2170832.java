package plexil;

import net.n3.nanoxml.*;

public class StringLiteralASTNode extends LiteralASTNode {

    private static final long serialVersionUID = -8806474295262342033L;

    public StringLiteralASTNode() {
        super();
    }

    public IXMLElement getXmlElement(String elementType) {
        IXMLElement result = new XMLElement(elementType);
        String myText = getText();
        StringBuffer myContent = new StringBuffer(myText.length());
        int index = 1;
        if (myText.charAt(0) != '"' || myText.charAt(myText.length() - 1) != '"') {
            System.err.println("Invalid string format for \"" + myText + "\"");
            index = 0;
        }
        while (index < myText.length()) {
            int nextIdx = myText.indexOf('\\', index);
            if (nextIdx != -1) {
                if (index != nextIdx) myContent.append(myText.substring(index, nextIdx));
                nextIdx++;
                char escaped = myText.charAt(nextIdx++);
                int charcode = 0;
                switch(escaped) {
                    case 'u':
                        for (int i = 0; i < 4; i++) {
                            char hex = myText.charAt(nextIdx++);
                            if (isHexDigit(hex)) {
                                charcode = (charcode * 16) + hexDigitToInt(hex);
                            } else {
                                System.err.println("Invalid Unicode escape format for \"" + myText + "\"");
                            }
                        }
                        myContent.append((char) charcode);
                        break;
                    case '0':
                    case '1':
                    case '2':
                    case '3':
                        charcode = digitToInt(escaped);
                        escaped = myText.charAt(nextIdx++);
                        if (isOctalDigit(escaped)) {
                            charcode = (charcode * 8) + digitToInt(escaped);
                            escaped = myText.charAt(nextIdx++);
                            if (isOctalDigit(escaped)) {
                                charcode = (charcode * 8) + digitToInt(escaped);
                            } else {
                                nextIdx--;
                            }
                        } else {
                            nextIdx--;
                        }
                        myContent.append((char) charcode);
                        break;
                    case '4':
                    case '5':
                    case '6':
                    case '7':
                        charcode = digitToInt(escaped);
                        escaped = myText.charAt(nextIdx++);
                        if (isOctalDigit(escaped)) {
                            charcode = (charcode * 8) + digitToInt(escaped);
                        } else {
                            nextIdx--;
                        }
                        myContent.append((char) charcode);
                        break;
                    case 'n':
                        myContent.append('\n');
                        break;
                    case 't':
                        myContent.append('\t');
                        break;
                    case 'b':
                        myContent.append('\b');
                        break;
                    case 'f':
                        myContent.append('\f');
                        break;
                    default:
                        myContent.append(escaped);
                        break;
                }
            } else {
                nextIdx = myText.indexOf('"', index);
                if (nextIdx == -1) nextIdx = myText.length();
                myContent.append(myText.substring(index, nextIdx));
                nextIdx++;
            }
            index = nextIdx;
        }
        result.setContent(myContent.toString());
        return result;
    }
}
