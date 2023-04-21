package pauker.program;

import java.awt.*;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.zip.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

/** the base class of Pauker which contains the GUI-independend functionality
 * needed for all Pauker versions
 */
public class Pauker {

    public static final String PAUKER_VERSION = "1.4";

    /** the repeating strategy where the longest expired cards are repeated first */
    public static final int OLDEST_FIRST = 0;

    /** the repeating strategy where the shortest expired cards are repeated first */
    public static final int NEWEST_FIRST = 1;

    /** the repeating strategy where cards are repeated randomly */
    public static final int RANDOM_ORDER = 2;

    /** a putback strategy where forgotten cards are put on top of the unlearned batch */
    public static final int ON_TOP = 0;

    /** a putback strategy where forgotten cards are put at the bottom of the unlearned batch */
    public static final int AT_BOTTOM = 1;

    /** a putback strategy where forgotten cards are put at a random place within the unlearned batch */
    public static final int ANYWHERE = 2;

    /** the front side of the card */
    public static final int FRONT_SIDE = 0;

    /** the back side of the card */
    public static final int BACK_SIDE = 1;

    /** both sides of the card */
    public static final int BOTH_SIDES = 2;

    /** the batchnumber of the card */
    public static final int BATCH_NUMBER = 3;

    /** the date when the card was learned*/
    public static final int LEARNED_DATE = 4;

    /** the date when the card expires*/
    public static final int EXPIRED_DATE = 5;

    /** the way a card has to be repeated*/
    public static final int REPEATING_MODE = 6;

    /** the idle learning phase */
    public static final int DOING_NOTHING = 0;

    /** the phase of learning new cards */
    public static final int LEARNING_NEW_CARDS = 1;

    /** the phase of waiting for the ultra short term memory */
    public static final int WAITING_FOR_USTM = 2;

    /** the phase of repeating the ultra short term memory */
    public static final int REPEATING_USTM = 3;

    /** the phase of waiting for the short term memory */
    public static final int WAITING_FOR_STM = 4;

    /** the phase of repeating the short term memory */
    public static final int REPEATING_STM = 5;

    /** the phase of repeating the long term memory */
    public static final int REPEATING_LTM = 6;

    /** determines the learning phase */
    public static int learningPhase = DOING_NOTHING;

    /** the duration of the ultra short term memory */
    public static final int USTM_TIME = 18;

    /** the duration of the short term memory */
    public static final int STM_TIME = 720;

    /** the currently active/loaded lesson */
    public Lesson currentLesson;

    private static SimpleDateFormat unifiedDateFormat;

    /** the active search pattern */
    public String searchPattern;

    /** the relevant card side in string searches */
    public int searchSide = BOTH_SIDES;

    /** determines if string searches are case sensitive */
    public boolean matchCase;

    /** the index of the card where the last string match occured */
    public int lastCardIndex = -1;

    /** the side of the card where the last string match occured */
    public int lastCardSide = FRONT_SIDE;

    /** the index of the string at the card side where the last string match occured */
    public int lastStringIndex = -1;

    /** Creates new form JPauker */
    public Pauker() {
        unifiedDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        currentLesson = new Lesson();
    }

    /** returns the currently active/loaded lesson
     * @return the currently active/loaded lesson
     */
    public Lesson getCurrentLesson() {
        return currentLesson;
    }

    /** loads a lesson from an XML file
     * @param fileName the name of the file
     * @return the loaded lesson
     */
    public static Lesson readXMLFile(String fileName) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setIgnoringComments(true);
            documentBuilderFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            GZIPInputStream gzipInputStream = new GZIPInputStream(new FileInputStream(fileName));
            Document document = documentBuilder.parse(gzipInputStream);
            NodeList documentNodes = document.getChildNodes();
            Lesson newLesson = new Lesson();
            int newLessonBatchIndex = 0;
            for (int i = 0; i < documentNodes.getLength(); i++) {
                Node documentNode = documentNodes.item(i);
                switch(documentNode.getNodeType()) {
                    case Node.COMMENT_NODE:
                        break;
                    case Node.ELEMENT_NODE:
                        String elementName = documentNode.getNodeName();
                        if (elementName.equals("Lesson")) {
                            NodeList lessonNodes = documentNode.getChildNodes();
                            for (int j = 0; j < lessonNodes.getLength(); j++) {
                                Node lessonNode = lessonNodes.item(j);
                                String lessonNodeName = lessonNode.getNodeName();
                                if (lessonNodeName.equals("Description")) {
                                    Node textNode = lessonNode.getFirstChild();
                                    if (textNode != null) {
                                        String description = textNode.getNodeValue();
                                        if (description != null) {
                                            newLesson.setDescription(description);
                                        }
                                    }
                                } else if (lessonNodeName.equals("Batch")) {
                                    if (newLesson.getNumberOfBatches() < newLessonBatchIndex + 1) {
                                        newLesson.addBatch();
                                    }
                                    NodeList batchNodes = lessonNode.getChildNodes();
                                    for (int k = 0; k < batchNodes.getLength(); k++) {
                                        Node batchNode = batchNodes.item(k);
                                        String batchNodeName = batchNode.getNodeName();
                                        if (batchNodeName.equals("Card")) {
                                            Card newCard = new Card();
                                            NamedNodeMap attributes = batchNode.getAttributes();
                                            Node attribute = attributes.getNamedItem("RepeatByTyping");
                                            if (attribute != null && attribute.getNodeValue().equals("true")) {
                                                newCard.setRepeatByTyping(true);
                                            }
                                            attribute = attributes.getNamedItem("Flipped_RepeatByTyping");
                                            if (attribute != null && attribute.getNodeValue().equals("true")) {
                                                newCard.setFlippedRepeatByTyping(true);
                                            }
                                            NodeList cardNodes = batchNode.getChildNodes();
                                            for (int l = 0; l < cardNodes.getLength(); l++) {
                                                Node cardNode = cardNodes.item(l);
                                                String cardNodeName = cardNode.getNodeName();
                                                if (cardNodeName.equals("Learned_Date")) {
                                                    Node textNode = cardNode.getFirstChild();
                                                    if (textNode != null) {
                                                        String learnedDate = textNode.getNodeValue();
                                                        try {
                                                            newCard.setLearnedDate(unifiedDateFormat.parse(learnedDate));
                                                        } catch (Exception e) {
                                                        }
                                                    }
                                                } else if (cardNodeName.equals("Flipped_Learned_Date")) {
                                                    Node textNode = cardNode.getFirstChild();
                                                    if (textNode != null) {
                                                        String learnedDate = textNode.getNodeValue();
                                                        try {
                                                            newCard.setFlippedLearnedDate(unifiedDateFormat.parse(learnedDate));
                                                        } catch (Exception e) {
                                                        }
                                                    }
                                                } else if (cardNodeName.equals("FrontSide")) {
                                                    Node textNode = cardNode.getFirstChild();
                                                    if (textNode != null) {
                                                        newCard.setFrontSide(textNode.getNodeValue());
                                                    }
                                                } else if (cardNodeName.equals("BackSide")) {
                                                    Node textNode = cardNode.getFirstChild();
                                                    if (textNode != null) {
                                                        newCard.setBackSide(textNode.getNodeValue());
                                                    }
                                                } else if (cardNodeName.equals("Flipped_Batch_Number")) {
                                                    Node textNode = cardNode.getFirstChild();
                                                    if (textNode != null) {
                                                        try {
                                                            newCard.setFlippedBatchNumber(Integer.parseInt(textNode.getNodeValue()));
                                                        } catch (Exception e) {
                                                            newCard.setLearned(false);
                                                        }
                                                    }
                                                } else if (cardNodeName.equals("FrontSideFont")) {
                                                    readFont(newCard, FRONT_SIDE, cardNode);
                                                } else if (cardNodeName.equals("BackSideFont")) {
                                                    readFont(newCard, BACK_SIDE, cardNode);
                                                }
                                            }
                                            Batch batch = null;
                                            if (!newCard.isLearned()) {
                                                batch = newLesson.getBatch(0);
                                            } else {
                                                batch = newLesson.getBatch(newLessonBatchIndex);
                                            }
                                            batch.addCard(newCard);
                                        }
                                    }
                                    newLessonBatchIndex++;
                                }
                            }
                        }
                }
            }
            newLesson.trim();
            return newLesson;
        } catch (Exception exception) {
        }
        return null;
    }

    private static void readFont(Card card, int cardSide, Node cardNode) {
        String family = null;
        int style = Font.PLAIN;
        int size = 12;
        Color foreground = null;
        Color background = null;
        NamedNodeMap attributes = cardNode.getAttributes();
        Node attribute = attributes.getNamedItem("Family");
        if (attribute != null) {
            family = attribute.getNodeValue();
            attribute = attributes.getNamedItem("Size");
            if (attribute != null) {
                size = Integer.parseInt(attribute.getNodeValue());
            }
            attribute = attributes.getNamedItem("Bold");
            if (attribute != null && attribute.getNodeValue().equals("true")) {
                style = Font.BOLD;
            }
            attribute = attributes.getNamedItem("Italic");
            if (attribute != null && attribute.getNodeValue().equals("true")) {
                style += Font.ITALIC;
            }
            attribute = attributes.getNamedItem("Foreground");
            if (attribute != null) {
                int rgbValue = Integer.parseInt(attribute.getNodeValue());
                foreground = new Color(rgbValue);
            }
            attribute = attributes.getNamedItem("Background");
            if (attribute != null) {
                int rgbValue = Integer.parseInt(attribute.getNodeValue());
                background = new Color(rgbValue);
            }
            if (cardSide == FRONT_SIDE) {
                card.frontSideFont = new Font(family, style, size);
                card.frontFontColor = foreground;
                card.frontBackgroundColor = background;
            } else {
                card.backSideFont = new Font(family, style, size);
                card.backFontColor = foreground;
                card.backBackgroundColor = background;
            }
        }
    }

    /** loads a lesson from a text file
     * @param fileName the name of the file
     * @param delimeter the delimeter between the card sides
     * @param encoding the encoding of the file
     * @return the loaded lesson
     */
    public static Lesson readTextFile(String fileName, String delimeter, String encoding) {
        int delimeterLength = delimeter.length();
        Lesson newLesson = new Lesson();
        Batch unlearnedBatch = newLesson.getBatch(0);
        try {
            FileInputStream fileInputStream = new FileInputStream(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, encoding);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            boolean repeatByTyping = false;
            while (line != null) {
                int delimeterIndex = line.indexOf(delimeter);
                if (delimeterIndex != -1) {
                    String frontSideText = line.substring(0, delimeterIndex);
                    String backSideText = line.substring(delimeterIndex + delimeterLength);
                    if (backSideText.indexOf(delimeter) > 0) {
                        delimeterIndex = backSideText.indexOf(delimeter);
                        String nextText = backSideText.substring(delimeterIndex + delimeterLength);
                        backSideText = backSideText.substring(0, delimeterIndex);
                        if (nextText.equals("text")) {
                            repeatByTyping = true;
                        }
                    }
                    Card newCard = new Card(frontSideText, backSideText, repeatByTyping);
                    unlearnedBatch.addCard(newCard);
                }
                line = reader.readLine();
            }
            reader.close();
            if (unlearnedBatch.getNumberOfCards() != 0) {
                newLesson.trim();
                return newLesson;
            }
        } catch (Exception exception) {
        }
        return null;
    }

    /** returns an XML presentation of the lesson
     * @param lesson the lesson
     * @return the XML presentation of the lesson
     */
    public static String lessonToXML(Lesson lesson) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            document.appendChild(document.createComment("This is a Pauker lesson"));
            Element lessonElement = document.createElement("Lesson");
            lessonElement.setAttribute("Pauker-Version", PAUKER_VERSION);
            document.appendChild(lessonElement);
            Element comment = document.createElement("Description");
            lessonElement.appendChild(comment);
            comment.appendChild(document.createTextNode(lesson.getDescription()));
            for (int i = 0; i < lesson.getNumberOfBatches(); i++) {
                Batch batch = lesson.getBatch(i);
                Element batchElement = addBatch(document, lessonElement);
                for (int j = 0; j < batch.getNumberOfCards(); j++) {
                    addCard(document, batchElement, batch.getCard(j));
                }
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            DOMSource source = new DOMSource(document);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(outputStream);
            transformer.transform(source, result);
            return outputStream.toString("UTF-8");
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    private static Element addBatch(Document document, Element lesson) {
        Element batch = document.createElement("Batch");
        lesson.appendChild(batch);
        return batch;
    }

    private static void addCard(Document document, Element batch, Card card) {
        Element cardElement = document.createElement("Card");
        batch.appendChild(cardElement);
        Element frontSide = document.createElement("FrontSide");
        cardElement.appendChild(frontSide);
        frontSide.appendChild(document.createTextNode(card.getFrontSide().toString()));
        Element backSide = document.createElement("BackSide");
        cardElement.appendChild(backSide);
        backSide.appendChild(document.createTextNode(card.getBackSide().toString()));
        if (card.isLearned()) {
            Element learnedDateElement = document.createElement("Learned_Date");
            cardElement.appendChild(learnedDateElement);
            learnedDateElement.appendChild(document.createTextNode(unifiedDateFormat.format(card.getLearnedDate())));
        }
        cardElement.setAttribute("RepeatByTyping", card.getRepeatByTyping() ? "true" : "false");
        Date flippedLearneddate = card.getFlippedLearnedDate();
        if (flippedLearneddate != null) {
            Element flippedLearnedDateElement = document.createElement("Flipped_Learned_Date");
            cardElement.appendChild(flippedLearnedDateElement);
            flippedLearnedDateElement.appendChild(document.createTextNode(unifiedDateFormat.format(flippedLearneddate)));
            int flippedBatchNumber = card.getFlippedBatchNumber();
            Element flippedBatchNumberElement = document.createElement("Flipped_Batch_Number");
            cardElement.appendChild(flippedBatchNumberElement);
            flippedBatchNumberElement.appendChild(document.createTextNode(String.valueOf(flippedBatchNumber)));
        }
        cardElement.setAttribute("Flipped_RepeatByTyping", card.getFlippedRepeatByTyping() ? "true" : "false");
        if (card.frontSideFont != null) {
            Element frontSideFont = document.createElement("FrontSideFont");
            frontSideFont.setAttribute("Family", card.frontSideFont.getFamily());
            frontSideFont.setAttribute("Size", String.valueOf(card.frontSideFont.getSize()));
            frontSideFont.setAttribute("Bold", card.frontSideFont.isBold() ? "true" : "false");
            frontSideFont.setAttribute("Italic", card.frontSideFont.isItalic() ? "true" : "false");
            frontSideFont.setAttribute("Foreground", String.valueOf(card.frontFontColor.getRGB()));
            frontSideFont.setAttribute("Background", String.valueOf(card.frontBackgroundColor.getRGB()));
            cardElement.appendChild(frontSideFont);
            Element backSideFont = document.createElement("BackSideFont");
            backSideFont.setAttribute("Family", card.backSideFont.getFamily());
            backSideFont.setAttribute("Size", String.valueOf(card.backSideFont.getSize()));
            backSideFont.setAttribute("Bold", card.backSideFont.isBold() ? "true" : "false");
            backSideFont.setAttribute("Italic", card.backSideFont.isItalic() ? "true" : "false");
            backSideFont.setAttribute("Foreground", String.valueOf(card.backFontColor.getRGB()));
            backSideFont.setAttribute("Background", String.valueOf(card.backBackgroundColor.getRGB()));
            cardElement.appendChild(backSideFont);
        }
    }

    /** searches through a lesson for a given string
     * @param batch the batch to search through
     * @param searchPattern the pattern to search for
     * @param searchSide the side to search at
     * @param matchCase if true the search is case sensitive
     * @param forward if true the search direction is forward
     * @return if true a pattern match was found
     */
    public boolean search(Batch batch, String searchPattern, int searchSide, boolean matchCase, boolean forward) {
        this.searchPattern = searchPattern;
        this.searchSide = searchSide;
        this.matchCase = matchCase;
        int numberOfCards = batch.getNumberOfCards();
        if (numberOfCards == 0) {
            return false;
        }
        int startIndex = lastStringIndex;
        int stringIndex = -1;
        int foundSide = 0;
        int cardIndex = lastCardIndex == -1 ? 0 : lastCardIndex;
        boolean sameCard = false;
        while (stringIndex == -1) {
            Card card = batch.getCard(cardIndex);
            switch(searchSide) {
                case FRONT_SIDE:
                    if (cardIndex != lastCardIndex || lastCardSide == FRONT_SIDE || !forward) {
                        if (lastCardSide == BACK_SIDE) {
                            startIndex = -1;
                        }
                        stringIndex = getIndexOf(card, FRONT_SIDE, startIndex, forward);
                        foundSide = FRONT_SIDE;
                    }
                    break;
                case BACK_SIDE:
                    if (cardIndex != lastCardIndex || lastCardSide == BACK_SIDE || forward) {
                        if (lastCardSide == FRONT_SIDE) {
                            startIndex = -1;
                        }
                        stringIndex = getIndexOf(card, BACK_SIDE, startIndex, forward);
                        foundSide = BACK_SIDE;
                    }
                    break;
                case BOTH_SIDES:
                    if (forward) {
                        if (cardIndex != lastCardIndex || lastCardSide == FRONT_SIDE || sameCard) {
                            stringIndex = getIndexOf(card, FRONT_SIDE, startIndex, forward);
                            foundSide = FRONT_SIDE;
                            startIndex = -1;
                        }
                        if (stringIndex == -1) {
                            stringIndex = getIndexOf(card, BACK_SIDE, startIndex, forward);
                            foundSide = BACK_SIDE;
                        }
                    } else {
                        if (cardIndex != lastCardIndex || lastCardSide == BACK_SIDE || sameCard) {
                            stringIndex = getIndexOf(card, BACK_SIDE, startIndex, forward);
                            foundSide = BACK_SIDE;
                            startIndex = -1;
                        }
                        if (stringIndex == -1) {
                            stringIndex = getIndexOf(card, FRONT_SIDE, startIndex, forward);
                            foundSide = FRONT_SIDE;
                        }
                    }
            }
            if (stringIndex != -1) {
                lastCardIndex = cardIndex;
                lastCardSide = foundSide;
                lastStringIndex = stringIndex;
                break;
            }
            startIndex = -1;
            cardIndex += forward ? 1 : -1;
            if (cardIndex == -1) {
                if (lastCardIndex == -1) {
                    lastCardIndex = -1;
                    lastCardSide = FRONT_SIDE;
                    lastStringIndex = -1;
                    break;
                } else {
                    cardIndex = numberOfCards - 1;
                }
            }
            if (cardIndex == numberOfCards) {
                if (lastCardIndex == -1) {
                    lastCardIndex = -1;
                    lastCardSide = FRONT_SIDE;
                    lastStringIndex = -1;
                    break;
                } else {
                    cardIndex = 0;
                }
            }
            if (sameCard) {
                lastCardIndex = -1;
                lastCardSide = FRONT_SIDE;
                lastStringIndex = -1;
                break;
            }
            if (cardIndex == lastCardIndex) {
                sameCard = true;
            }
        }
        return stringIndex != -1;
    }

    private int getIndexOf(Card card, int side, int startIndex, boolean forward) {
        String text = null;
        if (side == FRONT_SIDE) {
            text = (String) card.getFrontSide();
        } else {
            text = (String) card.getBackSide();
        }
        if (forward) {
            startIndex++;
            if (matchCase) {
                return text.indexOf(searchPattern, startIndex);
            } else {
                return text.toLowerCase().indexOf(searchPattern.toLowerCase(), startIndex);
            }
        } else {
            if (startIndex == -1) {
                startIndex = text.length() - 1;
            } else {
                startIndex--;
            }
            if (matchCase) {
                return text.lastIndexOf(searchPattern, startIndex);
            } else {
                return text.toLowerCase().lastIndexOf(searchPattern.toLowerCase(), startIndex);
            }
        }
    }

    /** moves cards back to the unlearned batch
     * @param batch the active/choosen batch
     * @param indices the indices of the choosen cards
     */
    public void forgetCards(Batch batch, int[] indices) {
        boolean fromSummary = batch.getBatchNumber() == Batch.SUMMARY_BATCH;
        Batch unlearnedBatch = currentLesson.getBatch(0);
        for (int i = indices.length - 1; i >= 0; i--) {
            Card card = batch.getCard(indices[i]);
            card.setLearned(false);
            if (fromSummary) {
                Batch cardBatch = currentLesson.getBatch(card.getBatchNumber());
                cardBatch.removeCard(card);
            } else {
                batch.removeCard(indices[i], false);
            }
            unlearnedBatch.addCard(card);
        }
        currentLesson.trim();
    }

    /** moves cards back to the first batch
     * and sets them expired
     * @param batch the active/choosen batch
     * @param indices the indices of the choosen cards
     */
    public void instantRepeatCards(Batch batch, int[] indices) {
        boolean fromSummary = batch.getBatchNumber() == Batch.SUMMARY_BATCH;
        if (currentLesson.getNumberOfBatches() == 3) {
            currentLesson.addBatch();
        }
        Batch firstBatch = currentLesson.getBatch(3);
        for (int i = indices.length - 1; i >= 0; i--) {
            Card card = batch.getCard(indices[i]);
            if (fromSummary) {
                Batch cardBatch = currentLesson.getBatch(card.getBatchNumber());
                cardBatch.removeCard(card);
            } else {
                batch.removeCard(indices[i], false);
            }
            firstBatch.addCard(card);
            card.expire();
        }
        currentLesson.trim();
    }
}
