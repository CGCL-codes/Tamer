package org.mazix.xml.impl.dom;

import static java.util.logging.Level.WARNING;
import static org.mazix.constants.ColorConstants.REVERSE_SUPPORTED_COLORS;
import static org.mazix.constants.GlobalConstants.BLANK_STRING;
import static org.mazix.constants.LanguageConstants.DEFAULT_LOCALE;
import static org.mazix.constants.LanguageConstants.SUPPORTED_LOCALES;
import static org.mazix.constants.LevelConstants.ARRIVAL_SQUARE;
import static org.mazix.constants.LevelConstants.BALL_CONTAINED_SQUARE;
import static org.mazix.constants.LevelConstants.DEFAULT_ID;
import static org.mazix.constants.LevelConstants.DEPARTURE_SQUARE;
import static org.mazix.constants.LevelConstants.EMPTY_SQUARE;
import static org.mazix.constants.LevelConstants.INFINITE_TIME;
import static org.mazix.constants.LevelConstants.KEY_CONTAINED_SQUARE;
import static org.mazix.constants.LevelConstants.KEY_DOOR_SQUARE;
import static org.mazix.constants.LevelConstants.LANTERN_CONTAINED_SQUARE;
import static org.mazix.constants.LevelConstants.MIN_COLUMNS;
import static org.mazix.constants.LevelConstants.MIN_LINES;
import static org.mazix.constants.LevelConstants.MOVABLE_WALL_CONTAINED_SQUARE;
import static org.mazix.constants.LevelConstants.ONE_WAY_DOOR_SQUARE;
import static org.mazix.constants.LevelConstants.SWITCHED_DOOR_SQUARE;
import static org.mazix.constants.LevelConstants.SWITCH_SQUARE;
import static org.mazix.constants.LevelConstants.TELEPORT_SQUARE;
import static org.mazix.constants.LevelConstants.WALL_SQUARE;
import static org.mazix.constants.ObjectConstants.DEFAULT_KEY_DOOR_OPENING;
import static org.mazix.constants.ObjectConstants.DEFAULT_LANTERN_LIGHTING;
import static org.mazix.constants.ObjectConstants.Z_SQUARE_LENGTH;
import static org.mazix.constants.XMLConstants.AUTHOR_TAG;
import static org.mazix.constants.XMLConstants.CELL_TAG;
import static org.mazix.constants.XMLConstants.COLOR_TAG;
import static org.mazix.constants.XMLConstants.COLUMNS_TAG;
import static org.mazix.constants.XMLConstants.COMMENT_TAG;
import static org.mazix.constants.XMLConstants.DIRECTION_TAG;
import static org.mazix.constants.XMLConstants.ID_TAG;
import static org.mazix.constants.XMLConstants.LANG_TAG;
import static org.mazix.constants.XMLConstants.LEVEL_LANGUAGE_TAG;
import static org.mazix.constants.XMLConstants.LEVEL_TAG;
import static org.mazix.constants.XMLConstants.LEVEL_TYPE_TAG;
import static org.mazix.constants.XMLConstants.LIGHTNING_TAG;
import static org.mazix.constants.XMLConstants.LINES_TAG;
import static org.mazix.constants.XMLConstants.LINK_X_TAG;
import static org.mazix.constants.XMLConstants.LINK_Y_TAG;
import static org.mazix.constants.XMLConstants.NAME_TAG;
import static org.mazix.constants.XMLConstants.NO_NAMESPACE_SCHEMA;
import static org.mazix.constants.XMLConstants.SERIES_LANGUAGE_TAG;
import static org.mazix.constants.XMLConstants.SERIES_TAG;
import static org.mazix.constants.XMLConstants.TIME_TAG;
import static org.mazix.constants.XMLConstants.W3C_INSTANCE;
import static org.mazix.constants.XMLConstants.W3C_XML_SCHEMA;
import static org.mazix.constants.XMLConstants.X_TAG;
import static org.mazix.constants.XMLConstants.Y_TAG;
import static org.mazix.constants.log.ErrorConstants.NUMBER_FORMAT_ERROR;
import static org.mazix.constants.log.InfoConstants.BLANK_COMMENT_INFO;
import static org.mazix.constants.log.InfoConstants.NOT_SUPPORTED_NODE_TYPE_INFO;
import static org.mazix.constants.log.WarningConstants.INCORRECT_LINK_WARNING;
import static org.mazix.constants.log.WarningConstants.NOT_SUPPORTED_COLUMN_NUMBER_WARNING;
import static org.mazix.constants.log.WarningConstants.NOT_SUPPORTED_COORDINATES_WARNING;
import static org.mazix.constants.log.WarningConstants.NOT_SUPPORTED_LANGUAGE_WARNING;
import static org.mazix.constants.log.WarningConstants.NOT_SUPPORTED_LEVEL_ID_WARNING;
import static org.mazix.constants.log.WarningConstants.NOT_SUPPORTED_LIGHNING_WARNING;
import static org.mazix.constants.log.WarningConstants.NOT_SUPPORTED_LINE_NUMBER_WARNING;
import static org.mazix.constants.log.WarningConstants.NOT_SUPPORTED_SQUARE_TYPE_WARNING;
import static org.mazix.constants.log.WarningConstants.NOT_SUPPORTED_TIME_WARNING;
import static org.mazix.constants.log.WarningConstants.NOT_SUPPORTED_XML_TAG_WARNING;
import static org.w3c.dom.Node.ELEMENT_NODE;
import java.awt.Color;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Logger;
import javax.vecmath.Vector3f;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.mazix.constants.GraphicConstants.GraphicType;
import org.mazix.constants.ObjectConstants.Direction;
import org.mazix.item.BallItem;
import org.mazix.item.KeyItem;
import org.mazix.item.LanternItem;
import org.mazix.item.MovableWallItem;
import org.mazix.kernel.Coordinates;
import org.mazix.kernel.GameLevel;
import org.mazix.kernel.LevelsSeries;
import org.mazix.log.LogUtils;
import org.mazix.square.AbstractSquare;
import org.mazix.square.ArrivalSquare;
import org.mazix.square.DepartureSquare;
import org.mazix.square.EmptySquare;
import org.mazix.square.KeyDoorSquare;
import org.mazix.square.OneWayDoorSquare;
import org.mazix.square.SwitchSquare;
import org.mazix.square.SwitchedDoorSquare;
import org.mazix.square.TeleportSquare;
import org.mazix.square.WallSquare;
import org.mazix.square.WithItem;
import org.mazix.xml.XMLLevelsSeries;
import org.mazix.xml.impl.XMLHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * The class which manages XML documents and parse XML files to fill levels series with the DOM
 * implementation.
 * 
 * @author Benjamin Croizet (graffity2199@yahoo.fr)
 * @since 0.5
 * @version 0.7
 */
class DOMLevelsSeries extends DOMTools implements XMLLevelsSeries {

    /** The class logger. */
    private static final Logger LOGGER = Logger.getLogger("org.mazix.xml.impl.dom.DOMLevelsSeries");

    /**
     * Default constructor, set document to an empty tree.
     * 
     * @throws ParserConfigurationException
     *             if the empty document can't be initialized
     * @since 0.5
     */
    public DOMLevelsSeries() throws ParserConfigurationException {
        super();
    }

    /**
     * This method reads an arrival square node and all parameters which are needed to build an
     * arrival square instance.
     * 
     * @param node
     *            the arrival square node containing squares attributes, can't be <code>null</code>.
     * @param level
     *            the {@code GameLevel} in which the square will be added. The level could be useful
     *            to get some specific attribute to build the square, can't be <code>null</code>.
     * @return the arrival square instance.
     */
    private AbstractSquare readArrivalSquareNode(final Node node, final GameLevel level) {
        return new ArrivalSquare();
    }

    /**
     * Reads the author attribute from a level node. May throw a {@code NullPointerException} if
     * {@code node} is <code>null</code>.
     * 
     * @param node
     *            the level node containing an author attribute, can't be <code>null</code>. If the
     *            node doesn't contain an author attribute, the level author will be the default
     *            one.
     * @return the string containing the level author, if found.
     * @since 0.5
     */
    private String readAuthorAttribute(final Node node) {
        assert node != null : "node is null";
        final NamedNodeMap nnm = node.getAttributes();
        final String authorAttr = nnm.getNamedItem(AUTHOR_TAG).getNodeValue();
        return XMLHelper.checkAuthor(authorAttr);
    }

    /**
     * This method reads a ball contained square node and all parameters which are needed to build a
     * ball contained square instance.
     * 
     * @param node
     *            the ball contained square node containing squares attributes, can't be
     *            <code>null</code>.
     * @param level
     *            the {@code GameLevel} in which the square will be added. The level could be useful
     *            to get some specific attribute to build the square, can't be <code>null</code>.
     * @return the ball contained square instance.
     */
    private AbstractSquare readBallContainedSquareNode(final Node node, final GameLevel level) {
        return new EmptySquare(new BallItem(level.getGraphicType(), new Vector3f(0, 0, 2 * Z_SQUARE_LENGTH)));
    }

    /**
     * Fills the {@code GameLevel} instance with the square which is described in the current cell
     * node. May throw a {@code NullPointerException} if {@code node} or {@code GameLevel} are
     * <code>null</code>.
     * 
     * @param node
     *            the cell node which describes the square, can't be {@code null}. If the node isn't
     *            an cell node, this method doesn't do anything.
     * @since 0.5
     */
    private void readCellNode(final Node node, final GameLevel level) {
        assert node != null : "node is null";
        assert level != null : "level is null";
        final int x = readXAttribute(node, level);
        final int y = readYAttribute(node, level);
        AbstractSquare square = new EmptySquare();
        final Node child = node.getFirstChild();
        if (child != null) {
            final String squareType = child.getNodeName();
            if (EMPTY_SQUARE.equals(squareType)) {
                square = readEmptySquareNode(child, level);
            } else if (BALL_CONTAINED_SQUARE.equals(squareType)) {
                square = readBallContainedSquareNode(child, level);
            } else if (DEPARTURE_SQUARE.equals(squareType)) {
                square = readDepartureSquareNode(child, level);
            } else if (ARRIVAL_SQUARE.equals(squareType)) {
                square = readArrivalSquareNode(child, level);
            } else if (KEY_DOOR_SQUARE.equals(squareType)) {
                square = readKeyDoorSquareNode(child, level);
            } else if (KEY_CONTAINED_SQUARE.equals(squareType)) {
                square = readKeyContainedSquareNode(child, level);
            } else if (MOVABLE_WALL_CONTAINED_SQUARE.equals(squareType)) {
                square = readMovableWallSquareNode(child, level);
            } else if (ONE_WAY_DOOR_SQUARE.equals(squareType)) {
                square = readOneWayDoorSquareNode(child, level);
            } else if (SWITCHED_DOOR_SQUARE.equals(squareType)) {
                square = readSwitchedDoorSquareNode(child, level);
            } else if (SWITCH_SQUARE.equals(squareType)) {
                square = readSwitchSquareNode(child, level);
            } else if (TELEPORT_SQUARE.equals(squareType)) {
                square = readTeleportSquareNode(child, level);
            } else if (WALL_SQUARE.equals(squareType)) {
                square = readWallSquareNode(child, level);
            } else {
                LOGGER.warning(LogUtils.buildLogString(NOT_SUPPORTED_SQUARE_TYPE_WARNING, squareType));
            }
            level.setSquare(square, new Coordinates(y, x));
        }
    }

    /**
     * Reads the color attribute from a square node. May throw a {@code NullPointerException} if
     * {@code node} is <code>null</code>.
     * 
     * @param node
     *            the square node containing a color attribute, can't be {@code null}. If the node
     *            doesn't contain a color attribute, the color will be the default one.
     * @return the {@code Color} instance which match the color name which have been defined in the
     *         attribute.
     * @since 0.5
     */
    private Color readColorAttribute(final Node node) {
        assert node != null : "node is null";
        final NamedNodeMap nnm = node.getAttributes();
        return XMLHelper.checkColor(nnm.getNamedItem(COLOR_TAG).getNodeValue());
    }

    /**
     * Reads the number of columns attribute from a level node. May throw a {@code
     * NullPointerException} if {@code node} is <code>null</code>.
     * 
     * @param node
     *            the level node containing a columns attribute, can't be <code>null</code>. If the
     *            node doesn't contain a columns attribute, the number of columns will be the
     *            default one.
     * @return the integer representing the number of columns of the level.
     * @since 0.5
     */
    private int readColumnsAttribute(final Node node) {
        assert node != null : "node is null";
        final NamedNodeMap nnm = node.getAttributes();
        int columns = MIN_COLUMNS;
        try {
            if (nnm.getNamedItem(COLUMNS_TAG).getNodeValue() != null) {
                columns = Integer.parseInt(nnm.getNamedItem(COLUMNS_TAG).getNodeValue());
                columns = XMLHelper.checkColumns(columns);
            }
        } catch (final NumberFormatException nfe) {
            LOGGER.warning(LogUtils.buildLogString(NOT_SUPPORTED_COLUMN_NUMBER_WARNING, columns));
            LOGGER.log(WARNING, NUMBER_FORMAT_ERROR, nfe);
            columns = MIN_COLUMNS;
        }
        return columns;
    }

    /**
     * Reads the a level comment node. May throw a {@code NullPointerException} if {@code node} is
     * <code>null</code>.
     * 
     * @param node
     *            the comment node to read, can't be <code>null</code>. If the node isn't an comment
     *            node, this method returns an empty string.
     * @return the extracted comment.
     * @since 0.5
     */
    private String readCommentNode(final Node node) {
        assert node != null : "node is null";
        final Node child = node.getFirstChild();
        String comment = BLANK_STRING;
        if (child != null) {
            comment = XMLHelper.checkComment(child.getNodeValue());
        } else {
            LOGGER.info(BLANK_COMMENT_INFO);
        }
        return comment;
    }

    /**
     * This method reads a departure square node and all parameters which are needed to build a
     * departure square instance.
     * 
     * @param node
     *            the departure square node containing squares attributes, can't be
     *            <code>null</code>.
     * @param level
     *            the {@code GameLevel} in which the square will be added. The level could be useful
     *            to get some specific attribute to build the square, can't be <code>null</code>.
     * @return the departure square instance.
     */
    private AbstractSquare readDepartureSquareNode(final Node node, final GameLevel level) {
        assert node != null : "node is null";
        final int lightning = readLightningAttribute(node);
        return new DepartureSquare(new LanternItem(lightning, level.getGraphicType(), new Vector3f(0, 0, 2 * Z_SQUARE_LENGTH)));
    }

    /**
     * Reads the direction attribute from a square node. May throw a {@code NullPointerException} if
     * {@code node} is <code>null</code>.
     * 
     * @param node
     *            the square node containing a direction attribute, can't be <code>null</code>. If
     *            the node doesn't contain a direction attribute, the direction will be the default
     *            one.
     * @return the {@code Direction} representing the square direction.
     * @since 0.5
     */
    private Direction readDirectionAttribute(final Node node) {
        assert node != null : "node is null";
        final NamedNodeMap nnm = node.getAttributes();
        final String s = nnm.getNamedItem(DIRECTION_TAG).getNodeValue();
        return XMLHelper.checkDirection(s);
    }

    /**
     * This method reads an empty square node and all parameters which are needed to build an empty
     * square instance.
     * 
     * @param node
     *            the empty square node containing squares attributes, can't be <code>null</code>.
     * @param level
     *            the {@code GameLevel} in which the square will be added. The level could be useful
     *            to get some specific attribute to build the square, can't be <code>null</code>.
     * @return the empty square instance.
     */
    private AbstractSquare readEmptySquareNode(final Node node, final GameLevel level) {
        return new EmptySquare();
    }

    /**
     * Reads the level id attribute from a level node. May throw a {@code NullPointerException} if
     * {@code node} is <code>null</code>.
     * 
     * @param node
     *            the level node containing a id attribute, can't be {@code null}. If the node
     *            doesn't contain an id attribute, the id will be the default one.
     * @return the integer representing the level id.
     * @since 0.5
     */
    private int readIdAttribute(final Node node) {
        assert node != null : "node is null";
        final NamedNodeMap nnm = node.getAttributes();
        int id = DEFAULT_ID;
        try {
            if (nnm.getNamedItem(ID_TAG).getNodeValue() != null) {
                id = Integer.parseInt(nnm.getNamedItem(ID_TAG).getNodeValue());
                if (id < 0) {
                    LOGGER.warning(LogUtils.buildLogString(NOT_SUPPORTED_LEVEL_ID_WARNING, id));
                    id = DEFAULT_ID;
                }
            }
        } catch (final NumberFormatException nfe) {
            LOGGER.warning(LogUtils.buildLogString(NOT_SUPPORTED_LEVEL_ID_WARNING, id));
            LOGGER.log(WARNING, NUMBER_FORMAT_ERROR, nfe);
            id = DEFAULT_ID;
        }
        return id;
    }

    /**
     * This method reads a key contained square node and all parameters which are needed to build a
     * key contained square instance.
     * 
     * @param node
     *            the key contained square node containing squares attributes, can't be
     *            <code>null</code>.
     * @param level
     *            the {@code GameLevel} in which the square will be added. The level could be useful
     *            to get some specific attribute to build the square, can't be <code>null</code>.
     * @return the key contained square instance.
     */
    private AbstractSquare readKeyContainedSquareNode(final Node node, final GameLevel level) {
        final Color color = readColorAttribute(node);
        return new EmptySquare(new KeyItem(color, new Vector3f(0, 0, 2 * Z_SQUARE_LENGTH)));
    }

    /**
     * This method reads a door square node and all parameters which are needed to build a door
     * square instance.
     * 
     * @param node
     *            the door square node containing squares attributes, can't be <code>null</code>.
     * @param level
     *            the {@code GameLevel} in which the square will be added. The level could be useful
     *            to get some specific attribute to build the square, can't be <code>null</code>.
     * @return the door square instance.
     */
    private AbstractSquare readKeyDoorSquareNode(final Node node, final GameLevel level) {
        final Color color = readColorAttribute(node);
        return new KeyDoorSquare(null, DEFAULT_KEY_DOOR_OPENING, color);
    }

    /**
     * Reads the a level language node. May throw a {@code NullPointerException} if {@code node} or
     * {@code GameLevel} are <code>null</code>.
     * 
     * @param node
     *            the comment node to read, can't be <code>null</code>. If the node isn't an comment
     *            node, this method returns an empty string.
     * @param level
     *            the {@code GameLevel} in which the language name and comment will be added, can't
     *            be <code>null</code>.
     * @since 0.5
     */
    private void readLevelLanguageNode(final Node node, final GameLevel level) {
        assert node != null : "node is null";
        assert level != null : "level is null";
        final NamedNodeMap nnm = node.getAttributes();
        Locale lang = DEFAULT_LOCALE;
        final String langAttr = nnm.getNamedItem(LANG_TAG).getNodeValue();
        if (langAttr != null) {
            final Locale tmp = new Locale(langAttr);
            if (SUPPORTED_LOCALES.containsKey(tmp)) {
                lang = tmp;
            } else {
                LOGGER.warning(LogUtils.buildLogString(NOT_SUPPORTED_LANGUAGE_WARNING, langAttr));
            }
        } else {
            LOGGER.warning(LogUtils.buildLogString(NOT_SUPPORTED_LANGUAGE_WARNING, langAttr));
        }
        final NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == ELEMENT_NODE) {
                if (NAME_TAG.equals(nl.item(i).getNodeName())) {
                    final String name = readNameNode(nl.item(i));
                    level.addName(lang, name);
                } else if (COMMENT_TAG.equals(nl.item(i).getNodeName())) {
                    final String comment = readCommentNode(nl.item(i));
                    level.addComment(lang, comment);
                } else {
                    LOGGER.warning(LogUtils.buildLogString(NOT_SUPPORTED_XML_TAG_WARNING, nl.item(i).getNodeName()));
                }
            } else {
                LOGGER.info(LogUtils.buildLogString(NOT_SUPPORTED_NODE_TYPE_INFO, nl.item(i).getNodeType()));
            }
        }
    }

    /**
     * {@code node} is <code>null</code>.
     * 
     * @param node
     *            the level node filled by a XML level tree, can't be {@code null}. If the node
     *            doesn't contain a level node, the level will be a default level.
     * @return a filled level instance.
     * @since 0.5
     */
    private GameLevel readLevelNode(final Node node) {
        assert node != null : "node is null";
        final String author = readAuthorAttribute(node);
        final GraphicType type = readLevelTypeAttribute(node);
        final int lines = readLinesAttribute(node);
        final int columns = readColumnsAttribute(node);
        final int id = readIdAttribute(node);
        final int time = readTimeAttribute(node);
        final GameLevel level = new GameLevel(id, lines, columns, type, time, author);
        final NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == ELEMENT_NODE) {
                if (LEVEL_LANGUAGE_TAG.equals(nl.item(i).getNodeName())) {
                    readLevelLanguageNode(nl.item(i), level);
                } else if (CELL_TAG.equals(nl.item(i).getNodeName())) {
                    readCellNode(nl.item(i), level);
                } else {
                    LOGGER.warning(LogUtils.buildLogString(NOT_SUPPORTED_XML_TAG_WARNING, nl.item(i).getNodeName()));
                }
            } else {
                LOGGER.info(LogUtils.buildLogString(NOT_SUPPORTED_NODE_TYPE_INFO, nl.item(i).getNodeType()));
            }
        }
        return level;
    }

    /**
     * @see org.mazix.xml.XMLLevelsSeries#readLevels(java.lang.String, java.lang.String)
     */
    @Override
    public LevelsSeries readLevels(final String pathname, final String schema) throws ParserConfigurationException, SAXException, IOException, JAXBException {
        assert pathname != null : "pathname is null";
        assert schema != null : "schema is null";
        assert getDocument() != null : "getDocument() is null";
        LevelsSeries levelsSeries = new LevelsSeries();
        readXml(pathname, schema);
        final NodeList nl = getDocument().getElementsByTagName(SERIES_TAG);
        for (int i = 0; i < nl.getLength(); i++) {
            levelsSeries = readLevelsSeriesNode(nl.item(i));
        }
        return levelsSeries;
    }

    /**
     * Builds and fills a levels series instance from a levels series node. May throw a {@code
     * NullPointerException} if {@code node} is <code>null</code>.
     * 
     * @param node
     *            the levels series node filled by a XML levels series tree, can't be
     *            <code>null</code>. If the node isn't an levels series node, the levels series
     *            instance will be empty.
     * @return a filled levels series instance.
     * @since 0.5
     */
    private LevelsSeries readLevelsSeriesNode(final Node node) {
        assert node != null : "node is null";
        final LevelsSeries levelsSeries = new LevelsSeries();
        final NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == ELEMENT_NODE) {
                if (SERIES_LANGUAGE_TAG.equals(nl.item(i).getNodeName())) {
                    final Locale seriesLocale = readSeriesLangAttribute(nl.item(i));
                    final String seriesName = readSeriesLanguageNode(nl.item(i));
                    levelsSeries.addName(seriesLocale, seriesName);
                } else if (LEVEL_TAG.equals(nl.item(i).getNodeName())) {
                    final GameLevel level = readLevelNode(nl.item(i));
                    levelsSeries.addLevel(level);
                } else {
                    LOGGER.warning(LogUtils.buildLogString(NOT_SUPPORTED_XML_TAG_WARNING, nl.item(i).getNodeName()));
                }
            } else {
                LOGGER.info(LogUtils.buildLogString(NOT_SUPPORTED_NODE_TYPE_INFO, nl.item(i).getNodeType()));
            }
        }
        return levelsSeries;
    }

    /**
     * Reads the level type attribute from a level node. May throw a {@code NullPointerException} if
     * {@code node} is <code>null</code>.
     * 
     * @param node
     *            the level node containing a type attribute, can't be {@code null}. If the node
     *            doesn't contain an type attribute, the type will be the default one.
     * @return the {@code GraphicType} representing the level type.
     * @since 0.5
     */
    private GraphicType readLevelTypeAttribute(final Node node) {
        assert node != null : "node is null";
        final NamedNodeMap nnm = node.getAttributes();
        final String s = nnm.getNamedItem(LEVEL_TYPE_TAG).getNodeValue();
        return XMLHelper.checkLevelType(s);
    }

    /**
     * Reads the lightning attribute from a square node. May throw a {@code NullPointerException} if
     * {@code node} is <code>null</code>.
     * 
     * @param node
     *            the square node containing a lightning attribute, can't be <code>null</code>. If
     *            the node doesn't contain an lightning attribute, the lightning will be the default
     *            one.
     * @return the integer representing the lightning.
     * @since 0.5
     */
    private int readLightningAttribute(final Node node) {
        assert node != null : "node is null";
        final NamedNodeMap nnm = node.getAttributes();
        int lightning = DEFAULT_LANTERN_LIGHTING;
        try {
            if (nnm.getNamedItem(LIGHTNING_TAG).getNodeValue() != null) {
                lightning = Integer.parseInt(nnm.getNamedItem(LIGHTNING_TAG).getNodeValue());
                lightning = XMLHelper.checkLightning(lightning);
            }
        } catch (final NumberFormatException nfe) {
            LOGGER.warning(LogUtils.buildLogString(NOT_SUPPORTED_LIGHNING_WARNING, lightning));
            LOGGER.log(WARNING, NUMBER_FORMAT_ERROR, nfe);
            lightning = DEFAULT_LANTERN_LIGHTING;
        }
        return lightning;
    }

    /**
     * Reads the number of lines attribute from a level node. May throw a {@code
     * NullPointerException} if {@code node} is <code>null</code>.
     * 
     * @param node
     *            the level node containing a lines attribute, can't be {@code null}. If the node
     *            doesn't contain a lines attribute, the number of lines will be the default one.
     * @return the integer representing the number of lines of the level.
     * @since 0.5
     */
    private int readLinesAttribute(final Node node) {
        assert node != null : "node is null";
        final NamedNodeMap nnm = node.getAttributes();
        int lines = MIN_LINES;
        try {
            if (nnm.getNamedItem(LINES_TAG).getNodeValue() != null) {
                lines = Integer.parseInt(nnm.getNamedItem(LINES_TAG).getNodeValue());
                lines = XMLHelper.checkLines(lines);
            }
        } catch (final NumberFormatException nfe) {
            LOGGER.warning(LogUtils.buildLogString(NOT_SUPPORTED_LINE_NUMBER_WARNING, lines));
            LOGGER.log(WARNING, NUMBER_FORMAT_ERROR, nfe);
            lines = MIN_LINES;
        }
        return lines;
    }

    /**
     * Reads the link x coordinate attribute from a square node. May throw a {@code
     * NullPointerException} if {@code node} is <code>null</code>.
     * 
     * @param node
     *            the square node containing a link x attribute, can't be <code>null</code>. If the
     *            node doesn't contain a link x attribute, the link x will be the default one.
     * @return the integer representing the link x coordinate.
     * @since 0.5
     */
    private int readLinkXAttribute(final Node node, final GameLevel level) {
        assert node != null : "node is null";
        final NamedNodeMap nnm = node.getAttributes();
        int link_y = 0;
        try {
            if (nnm.getNamedItem(LINK_Y_TAG).getNodeValue() != null) {
                link_y = Integer.parseInt(nnm.getNamedItem(LINK_Y_TAG).getNodeValue());
                link_y = XMLHelper.checkLinkX(link_y, level);
            }
        } catch (final NumberFormatException nfe) {
            LOGGER.warning(LogUtils.buildLogString(INCORRECT_LINK_WARNING, link_y));
            LOGGER.log(WARNING, NUMBER_FORMAT_ERROR, nfe);
            link_y = 0;
        }
        return link_y;
    }

    /**
     * Reads the link y coordinate attribute from a square node. May throw a {@code
     * NullPointerException} if {@code node} is <code>null</code>.
     * 
     * @param node
     *            the square node containing a link y attribute, can't be <code>null</code>. If the
     *            node doesn't contain a link y attribute, the link y will be the default one.
     * @return the integer representing the link y coordinate.
     * @since 0.5
     */
    private int readLinkYAttribute(final Node node, final GameLevel level) {
        assert node != null : "node is null";
        final NamedNodeMap nnm = node.getAttributes();
        int link_x = 0;
        try {
            if (nnm.getNamedItem(LINK_X_TAG).getNodeValue() != null) {
                link_x = Integer.parseInt(nnm.getNamedItem(LINK_X_TAG).getNodeValue());
                link_x = XMLHelper.checkLinkX(link_x, level);
            }
        } catch (final NumberFormatException nfe) {
            LOGGER.warning(LogUtils.buildLogString(INCORRECT_LINK_WARNING, link_x));
            LOGGER.log(WARNING, NUMBER_FORMAT_ERROR, nfe);
            link_x = 0;
        }
        return link_x;
    }

    /**
     * This method reads a movable square node and all parameters which are needed to build a
     * movable square instance.
     * 
     * @param node
     *            the movable square node containing squares attributes, can't be <code>null</code>.
     * @param level
     *            the {@code GameLevel} in which the square will be added. The level could be useful
     *            to get some specific attribute to build the square, can't be <code>null</code>.
     * @return the movable square instance.
     */
    private AbstractSquare readMovableWallSquareNode(final Node node, final GameLevel level) {
        return new EmptySquare(new MovableWallItem(level.getGraphicType(), new Vector3f(0, 0, 2 * Z_SQUARE_LENGTH)));
    }

    /**
     * Reads the a name node. May throw a {@code NullPointerException} if {@code node} is
     * <code>null</code>.
     * 
     * @param node
     *            the name node to read, can't be <code>null</code>. If the node isn't an name node,
     *            this method returns an empty string.
     * @return the name string.
     * @since 0.5
     */
    private String readNameNode(final Node node) {
        assert node != null : "node is null";
        final Node child = node.getFirstChild();
        final String name = child.getNodeValue();
        return XMLHelper.checkName(name);
    }

    /**
     * This method reads a one way square node and all parameters which are needed to build a one
     * way square instance.
     * 
     * @param node
     *            the one way square node containing squares attributes, can't be <code>null</code>.
     * @param level
     *            the {@code GameLevel} in which the square will be added. The level could be useful
     *            to get some specific attribute to build the square, can't be <code>null</code>.
     * @return the one way square instance.
     */
    private AbstractSquare readOneWayDoorSquareNode(final Node node, final GameLevel level) {
        final Direction direction = readDirectionAttribute(node);
        return new OneWayDoorSquare(null, direction);
    }

    /**
     * Reads the series locale language from a series language node. May throw a {@code
     * NullPointerException} if {@code node} is <code>null</code>.
     * 
     * @param node
     *            the series language node containing a lang attribute, can't be <code>null</code>.
     *            If the node doesn't contain a lang attribute, the series lang will be set to
     *            default.
     * @return the <code>Locale</code> representing the series language.
     * @since 0.5
     * @version 0.7
     */
    private Locale readSeriesLangAttribute(final Node node) {
        assert node != null : "node is null";
        Locale lang = DEFAULT_LOCALE;
        final String langAttr = node.getAttributes().getNamedItem(LANG_TAG).getNodeValue();
        if (langAttr != null) {
            final Locale tmp = new Locale(langAttr);
            if (SUPPORTED_LOCALES.containsKey(tmp)) {
                lang = tmp;
            } else {
                LOGGER.warning(LogUtils.buildLogString(NOT_SUPPORTED_LANGUAGE_WARNING, langAttr));
            }
        } else {
            LOGGER.warning(LogUtils.buildLogString(NOT_SUPPORTED_LANGUAGE_WARNING, langAttr));
        }
        return lang;
    }

    /**
     * Reads the series name from a language node. May throw a {@code NullPointerException} if
     * {@code node} is <code>null</code>.
     * 
     * @param node
     *            the series node containing a name attribute, can't be {@code null}. If the node
     *            doesn't contain a name attribute, the series name will be empty.
     * @return a filled profile instance.
     * @since 0.5
     */
    private String readSeriesLanguageNode(final Node node) {
        assert node != null : "node is null";
        String s = BLANK_STRING;
        final NodeList nl = node.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            if (nl.item(i).getNodeType() == ELEMENT_NODE) {
                if (NAME_TAG.equals(nl.item(i).getNodeName())) {
                    final Node child = nl.item(i).getFirstChild();
                    if (child != null && child.getNodeValue() != null) {
                        s = child.getNodeValue();
                    } else {
                        LOGGER.warning(LogUtils.buildLogString(NOT_SUPPORTED_XML_TAG_WARNING, nl.item(i).getNodeName()));
                    }
                }
            } else {
                LOGGER.info(LogUtils.buildLogString(NOT_SUPPORTED_NODE_TYPE_INFO, nl.item(i).getNodeType()));
            }
        }
        return s;
    }

    /**
     * This method reads a switched door square node and all parameters which are needed to build a
     * switched door square instance.
     * 
     * @param node
     *            the switched door square node containing squares attributes, can't be
     *            <code>null</code>.
     * @param level
     *            the {@code GameLevel} in which the square will be added. The level could be useful
     *            to get some specific attribute to build the square, can't be <code>null</code>.
     * @return the switched door square instance.
     */
    private AbstractSquare readSwitchedDoorSquareNode(final Node node, final GameLevel level) {
        final Color color = readColorAttribute(node);
        return new SwitchedDoorSquare(null, DEFAULT_KEY_DOOR_OPENING, color);
    }

    /**
     * This method reads a switch square node and all parameters which are needed to build a switch
     * square instance.
     * 
     * @param node
     *            the switch square node containing squares attributes, can't be <code>null</code>.
     * @param level
     *            the {@code GameLevel} in which the square will be added. The level could be useful
     *            to get some specific attribute to build the square, can't be <code>null</code>.
     * @return the switch square instance.
     */
    private AbstractSquare readSwitchSquareNode(final Node node, final GameLevel level) {
        final Color color = readColorAttribute(node);
        final int link_x = readLinkXAttribute(node, level);
        final int link_y = readLinkYAttribute(node, level);
        return new SwitchSquare(null, color, new Coordinates(link_x, link_y));
    }

    /**
     * This method reads a teleport square node and all parameters which are needed to build a
     * teleport square instance.
     * 
     * @param node
     *            the teleport square node containing squares attributes, can't be <code>null</code>
     *            .
     * @param level
     *            the {@code GameLevel} in which the square will be added. The level could be useful
     *            to get some specific attribute to build the square, can't be <code>null</code>.
     * @return the teleport square instance.
     */
    private AbstractSquare readTeleportSquareNode(final Node node, final GameLevel level) {
        final Color color = readColorAttribute(node);
        final int link_x = readLinkXAttribute(node, level);
        final int link_y = readLinkYAttribute(node, level);
        return new TeleportSquare(color, new Coordinates(link_x, link_y));
    }

    /**
     * Reads the time attribute from a level node. May throw a {@code NullPointerException} if
     * {@code node} is <code>null</code>.
     * 
     * @param node
     *            the level node containing a time attribute, can't be {@code null}. If the node
     *            doesn't contain a time attribute, the time will be the default one.
     * @return the integer representing the level time.
     * @since 0.5
     */
    private int readTimeAttribute(final Node node) {
        assert node != null : "node is null";
        final NamedNodeMap nnm = node.getAttributes();
        int time = INFINITE_TIME;
        try {
            if (nnm.getNamedItem(TIME_TAG).getNodeValue() != null) {
                time = Integer.parseInt(nnm.getNamedItem(TIME_TAG).getNodeValue());
                time = XMLHelper.checkTime(XMLHelper.checkTime(time));
            }
        } catch (final NumberFormatException nfe) {
            LOGGER.warning(LogUtils.buildLogString(NOT_SUPPORTED_TIME_WARNING, time));
            LOGGER.log(WARNING, NUMBER_FORMAT_ERROR, nfe);
            time = INFINITE_TIME;
        }
        return time;
    }

    /**
     * This method reads a wall square node and all parameters which are needed to build a wall
     * square instance.
     * 
     * @param node
     *            the wall square node containing squares attributes, can't be <code>null</code>.
     * @param level
     *            the {@code GameLevel} in which the square will be added. The level could be useful
     *            to get some specific attribute to build the square, can't be <code>null</code>.
     * @return the wall square instance.
     */
    private AbstractSquare readWallSquareNode(final Node node, final GameLevel level) {
        return new WallSquare();
    }

    /**
     * Reads the x coordinate attribute from a square node. May throw a {@code NullPointerException}
     * if {@code node} is <code>null</code>.
     * 
     * @param node
     *            the square node containing a x attribute, can't be {@code null}. If the node
     *            doesn't contain a x attribute, the x will be the default one.
     * @return the integer representing the x coordinate.
     * @since 0.5
     */
    private int readXAttribute(final Node node, final GameLevel level) {
        assert node != null : "node is null";
        assert level != null : "level is null";
        final NamedNodeMap nnm = node.getAttributes();
        int x = 0;
        try {
            if (nnm.getNamedItem(X_TAG).getNodeValue() != null) {
                x = Integer.parseInt(nnm.getNamedItem(X_TAG).getNodeValue());
                if (x < 0 || x > level.getColumns()) {
                    LOGGER.warning(LogUtils.buildLogString(NOT_SUPPORTED_COORDINATES_WARNING, x));
                    x = 0;
                }
            }
        } catch (final NumberFormatException nfe) {
            LOGGER.warning(LogUtils.buildLogString(NOT_SUPPORTED_COORDINATES_WARNING, x));
            LOGGER.log(WARNING, NUMBER_FORMAT_ERROR, nfe);
            x = 0;
        }
        return x;
    }

    /**
     * Reads the y coordinate attribute from a square node. May throw a {@code NullPointerException}
     * if {@code node} is <code>null</code>.
     * 
     * @param node
     *            the square node containing a y attribute, can't be {@code null}. If the node
     *            doesn't contain a y attribute, the y will be the default one.
     * @return the integer representing the y coordinate.
     * @since 0.5
     */
    private int readYAttribute(final Node node, final GameLevel level) {
        assert node != null : "node is null";
        assert level != null : "level is null";
        final NamedNodeMap nnm = node.getAttributes();
        int y = 0;
        try {
            if (nnm.getNamedItem(Y_TAG).getNodeValue() != null) {
                y = Integer.parseInt(nnm.getNamedItem(Y_TAG).getNodeValue());
                if (y < 0 || y > level.getLines()) {
                    LOGGER.warning(LogUtils.buildLogString(NOT_SUPPORTED_COORDINATES_WARNING, y));
                    y = 0;
                }
            }
        } catch (final NumberFormatException nfe) {
            LOGGER.warning(LogUtils.buildLogString(NOT_SUPPORTED_COORDINATES_WARNING, y));
            LOGGER.log(WARNING, NUMBER_FORMAT_ERROR, nfe);
            y = 0;
        }
        return y;
    }

    /**
     * @see org.mazix.xml.XMLLevelsSeries#writeLevels(org.mazix.kernel.LevelsSeries,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public void writeLevels(final LevelsSeries levelsSeries, final String pathname, final String schema) throws ParserConfigurationException, TransformerException, JAXBException {
        assert pathname != null : "pathname is null";
        assert schema != null : "schema is null";
        assert levelsSeries != null : "levelsSeries is null";
        resetDocument();
        Element root;
        Element language;
        Element name;
        Element comment;
        Element level;
        Element cell;
        Element square;
        Text text;
        root = getDocument().createElement(SERIES_TAG);
        root.setAttributeNS(W3C_XML_SCHEMA + W3C_INSTANCE, NO_NAMESPACE_SCHEMA, schema);
        for (final Locale s : levelsSeries.getNamesKeys()) {
            final String seriesName = levelsSeries.getName(s);
            language = getDocument().createElement(SERIES_LANGUAGE_TAG);
            language.setAttribute(LANG_TAG, s.getLanguage());
            name = getDocument().createElement(NAME_TAG);
            text = getDocument().createTextNode(seriesName);
            name.appendChild(text);
            language.appendChild(name);
            root.appendChild(language);
        }
        for (final GameLevel g : levelsSeries.getLevelsValues()) {
            level = getDocument().createElement(LEVEL_TAG);
            level.setAttribute(LINES_TAG, Integer.toString(g.getLines()));
            level.setAttribute(AUTHOR_TAG, g.getAuthor());
            level.setAttribute(LEVEL_TYPE_TAG, g.getGraphicType().toString().toLowerCase());
            level.setAttribute(ID_TAG, Integer.toString(g.getId()));
            level.setAttribute(TIME_TAG, Integer.toString(g.getTime()));
            level.setAttribute(COLUMNS_TAG, Integer.toString(g.getColumns()));
            for (final Locale s : g.getNamesKeys()) {
                language = getDocument().createElement(LEVEL_LANGUAGE_TAG);
                language.setAttribute(LANG_TAG, s.getLanguage());
                name = getDocument().createElement(NAME_TAG);
                text = getDocument().createTextNode(g.getName(s));
                name.appendChild(text);
                comment = getDocument().createElement(COMMENT_TAG);
                text = getDocument().createTextNode(g.getComment(s));
                comment.appendChild(text);
                language.appendChild(name);
                language.appendChild(comment);
                level.appendChild(language);
            }
            for (int i = 0; i < g.getLines(); i++) {
                for (int j = 0; j < g.getColumns(); j++) {
                    cell = getDocument().createElement(CELL_TAG);
                    cell.setAttribute(X_TAG, Integer.toString(j));
                    cell.setAttribute(Y_TAG, Integer.toString(i));
                    final Coordinates cellPos = new Coordinates(i, j);
                    final AbstractSquare currentSquare = g.getSquare(cellPos);
                    final String squareType = currentSquare.getType();
                    square = getDocument().createElement(squareType);
                    if (KEY_DOOR_SQUARE.equals(squareType)) {
                        final KeyDoorSquare k = (KeyDoorSquare) currentSquare;
                        square.setAttribute(COLOR_TAG, REVERSE_SUPPORTED_COLORS.get(k.getColor()));
                    } else if (KEY_CONTAINED_SQUARE.equals(squareType)) {
                        final EmptySquare e = (EmptySquare) currentSquare;
                        final KeyItem k = (KeyItem) e.getItem();
                        square.setAttribute(COLOR_TAG, REVERSE_SUPPORTED_COLORS.get(k.getColor()));
                    } else if (ONE_WAY_DOOR_SQUARE.equals(squareType)) {
                        final OneWayDoorSquare o = (OneWayDoorSquare) currentSquare;
                        square.setAttribute(DIRECTION_TAG, o.getDirection().toString());
                    } else if (SWITCHED_DOOR_SQUARE.equals(squareType)) {
                        final SwitchedDoorSquare s = (SwitchedDoorSquare) currentSquare;
                        square.setAttribute(COLOR_TAG, REVERSE_SUPPORTED_COLORS.get(s.getColor()));
                    } else if (SWITCH_SQUARE.equals(squareType)) {
                        final SwitchSquare s = (SwitchSquare) currentSquare;
                        square.setAttribute(COLOR_TAG, REVERSE_SUPPORTED_COLORS.get(s.getColor()));
                        square.setAttribute(X_TAG, Integer.toString(s.getKeyDoorSquareToOpen().getColumn()));
                        square.setAttribute(Y_TAG, Integer.toString(s.getKeyDoorSquareToOpen().getLine()));
                    } else if (TELEPORT_SQUARE.equals(squareType)) {
                        final TeleportSquare s = (TeleportSquare) currentSquare;
                        square.setAttribute(COLOR_TAG, REVERSE_SUPPORTED_COLORS.get(s.getColor()));
                        square.setAttribute(X_TAG, Integer.toString(s.getEmptySquareToTeleport().getColumn()));
                        square.setAttribute(Y_TAG, Integer.toString(s.getEmptySquareToTeleport().getLine()));
                    } else if (LANTERN_CONTAINED_SQUARE.equals(squareType)) {
                        final WithItem wi = (WithItem) currentSquare;
                        final LanternItem k = (LanternItem) wi.getItem();
                        square.setAttribute(LIGHTNING_TAG, Integer.toString(k.getLighting()));
                    } else {
                        LOGGER.warning(LogUtils.buildLogString(NOT_SUPPORTED_SQUARE_TYPE_WARNING, squareType));
                    }
                    cell.appendChild(square);
                    level.appendChild(cell);
                }
            }
            root.appendChild(level);
        }
        getDocument().appendChild(root);
        writeXml(pathname);
    }
}
