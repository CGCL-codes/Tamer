package net.sourceforge.circuitsmith.xmlparser.project;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.io.File;
import java.util.StringTokenizer;
import net.sourceforge.circuitsmith.objects.EdaGrid;
import net.sourceforge.circuitsmith.objects.EdaAbstractText.Style;
import net.sourceforge.circuitsmith.objects.EdaAbstractText.XAnchor;
import net.sourceforge.circuitsmith.objects.EdaAbstractText.YAnchor;
import net.sourceforge.circuitsmith.xmlparser.EdaSAXException;

/**
 * Maps tags to their internal representation, parses their contents and stores it in the current parser context.
 * <p>
 * @author holger
 */
final class EdaTagMapper {

    private static final String TAG_ATTRIBUTE_NAME = "N";

    private static final String TAG_ATTRIBUTE_VALUE = "V";

    private static final String TAG_OBJECT_CLASS = "OBJECT";

    private static final String TAG_TREE_NODE = "A";

    private static final String TAG_DOCUMENT_PANE = "D";

    private static final String TAG_PART = "P";

    private static final String TAG_PART_SOURCE_DOCUMENT_PATH = "SD";

    private static final String TAG_PART_SUB_SCHEMATIC_PATH = "SS";

    private static final String TAG_PART_ID = "ID";

    private static final String TAG_EXTERNAL_FILE = "F";

    private static final String TAG_DRAWING = "G";

    private static final String TAG_ATTRIBUTE_MODEL_LIST = "AL";

    private static final String TAG_LOCATION_COORDINATES = "XY";

    private static final String TAG_LAYERNAME = "L";

    private static final String TAG_COLOR = "C";

    private static final String TAG_NOT_SHOWN_ON_PART = "S";

    private static final String TAG_PLAIN_TEXT = "T";

    private static final String TAG_PLAIN_TEXT_FONT = "TF";

    private static final String TAG_PLAIN_TEXT_POSITION = "TP";

    private static final String TAG_ATTRIBUTE_TEXT_STYLE = "AS";

    private static final String TAG_ATTRIBUTE_TEXT_NAME = "AN";

    private static final String TAG_WIDTH_HEIGHT = "WH";

    private static final String TAG_LINE_WIDTH = "LW";

    private static final String TAG_SYMBOL_NAME = "SY";

    private static final String TAG_GRID_SPACING = "GS";

    private static final String TAG_GRID_METRIC = "MG";

    private static final String TAG_GRID_AUTO = "AG";

    private static final String TAG_GRID_SNAP = "SG";

    private final EdaClassnameMapper classNameMapper;

    EdaTagMapper(EdaClassnameMapper aClassMapper) {
        classNameMapper = aClassMapper;
    }

    void parseTag(final String qName, final String currentText, final EdaParserContext current) throws EdaSAXException {
        if (TAG_ATTRIBUTE_NAME.equals(qName)) {
            current.name = currentText;
            return;
        }
        if (TAG_ATTRIBUTE_VALUE.equals(qName)) {
            current.value = currentText;
            return;
        }
        if (TAG_PART_SOURCE_DOCUMENT_PATH.equals(qName)) {
            current.sourceDocumentPath = currentText;
            return;
        }
        if (TAG_PART_SUB_SCHEMATIC_PATH.equals(qName)) {
            return;
        }
        if (TAG_PART_ID.equals(qName)) {
            current.id = Long.parseLong(currentText);
            return;
        }
        if (TAG_EXTERNAL_FILE.equals(qName)) {
            if (classNameMapper.isLibraryContext(current)) {
                current.externalFile = new File(currentText);
            } else {
                current.isFlipped = true;
            }
            return;
        }
        if (TAG_DRAWING.equals(qName)) {
            return;
        }
        if (TAG_ATTRIBUTE_MODEL_LIST.equals(qName)) {
            return;
        }
        if (TAG_LOCATION_COORDINATES.equals(qName)) {
            StringTokenizer tokenizer = new StringTokenizer(currentText, ",");
            current.locationX = Float.parseFloat(tokenizer.nextToken());
            current.locationY = Float.parseFloat(tokenizer.nextToken());
            return;
        }
        if (TAG_LAYERNAME.equals(qName)) {
            current.layerName = currentText;
            return;
        }
        if (TAG_COLOR.equals(qName)) {
            current.color = new Color(Integer.parseInt(currentText));
            return;
        }
        if (TAG_NOT_SHOWN_ON_PART.equals(qName)) {
            current.isShown = false;
            return;
        }
        if (TAG_PLAIN_TEXT.equals(qName)) {
            current.value = currentText;
            return;
        }
        if (TAG_PLAIN_TEXT_FONT.equals(qName)) {
            StringTokenizer tokenizer = new StringTokenizer(currentText, ",");
            final Font font = new Font(tokenizer.nextToken(), (int) Float.parseFloat(tokenizer.nextToken()), (int) Float.parseFloat(tokenizer.nextToken()));
            current.font = font;
            return;
        }
        if (TAG_PLAIN_TEXT_POSITION.equals(qName)) {
            final int position = (int) Float.parseFloat(currentText);
            current.xAnchor = XAnchor.values()[position & 0x03];
            current.yAnchor = YAnchor.values()[(position & 0x0C) >> 2];
            return;
        }
        if (TAG_ATTRIBUTE_TEXT_STYLE.equals(qName)) {
            current.style = Style.values()[(int) Float.parseFloat(currentText)];
            return;
        }
        if (TAG_ATTRIBUTE_TEXT_NAME.equals(qName)) {
            current.name = currentText;
            return;
        }
        if (TAG_WIDTH_HEIGHT.equals(qName)) {
            StringTokenizer tokenizer = new StringTokenizer(currentText, ",");
            current.width = Float.parseFloat(tokenizer.nextToken());
            current.height = Float.parseFloat(tokenizer.nextToken());
            return;
        }
        if (TAG_LINE_WIDTH.equals(qName)) {
            current.lineWidth = Float.parseFloat(currentText);
            return;
        }
        if (TAG_TREE_NODE.equals(qName)) {
            try {
                final float angle = Float.parseFloat(currentText);
                if (angle != 0.0f) {
                    current.angle = angle;
                }
            } catch (NumberFormatException ex) {
            }
            return;
        }
        if (TAG_SYMBOL_NAME.equals(qName)) {
            current.name = currentText;
            return;
        }
        if (TAG_PART.equals(qName)) {
            if (classNameMapper.isSymbolContext(current)) {
                current.id = Long.parseLong(currentText);
            } else {
            }
            return;
        }
        if (TAG_DOCUMENT_PANE.equals(qName)) {
            return;
        }
        if (TAG_GRID_SPACING.equals(qName)) {
            StringTokenizer tokenizer = new StringTokenizer(currentText, ",");
            current.gridSpacing = new Point2D.Float(Float.parseFloat(tokenizer.nextToken()), Float.parseFloat(tokenizer.nextToken()));
            return;
        }
        if (TAG_GRID_METRIC.equals(qName)) {
            current.metric = true;
            return;
        }
        if (TAG_GRID_AUTO.equals(qName)) {
            current.autoGrid = true;
            return;
        }
        if (TAG_GRID_SNAP.equals(qName)) {
            current.snap = EdaGrid.Snap.values()[(int) (Float.parseFloat(currentText))];
            return;
        }
        throw new EdaSAXException("unknown tag '" + qName + ".");
    }

    boolean isObjectTag(final String qName) {
        return TAG_OBJECT_CLASS.equals(qName);
    }

    void validateTag(final String qName) throws EdaSAXException {
        if (TAG_ATTRIBUTE_NAME.equals(qName)) {
            return;
        }
        if (TAG_ATTRIBUTE_VALUE.equals(qName)) {
            return;
        }
        if (TAG_PART_SOURCE_DOCUMENT_PATH.equals(qName)) {
            return;
        }
        if (TAG_PART_ID.equals(qName)) {
            return;
        }
        if (TAG_EXTERNAL_FILE.equals(qName)) {
            return;
        }
        if (TAG_DRAWING.equals(qName)) {
            return;
        }
        if (TAG_ATTRIBUTE_MODEL_LIST.equals(qName)) {
            return;
        }
        if (TAG_LOCATION_COORDINATES.equals(qName)) {
            return;
        }
        if (TAG_LAYERNAME.equals(qName)) {
            return;
        }
        if (TAG_COLOR.equals(qName)) {
            return;
        }
        if (TAG_NOT_SHOWN_ON_PART.equals(qName)) {
            return;
        }
        if (TAG_PLAIN_TEXT.equals(qName)) {
            return;
        }
        if (TAG_PLAIN_TEXT_FONT.equals(qName)) {
            return;
        }
        if (TAG_PLAIN_TEXT_POSITION.equals(qName)) {
            return;
        }
        if (TAG_ATTRIBUTE_TEXT_STYLE.equals(qName)) {
            return;
        }
        if (TAG_ATTRIBUTE_TEXT_NAME.equals(qName)) {
            return;
        }
        if (TAG_WIDTH_HEIGHT.equals(qName)) {
            return;
        }
        if (TAG_LINE_WIDTH.equals(qName)) {
            return;
        }
        if (TAG_SYMBOL_NAME.equals(qName)) {
            return;
        }
        if (TAG_GRID_SPACING.equals(qName)) {
            return;
        }
        if (TAG_GRID_METRIC.equals(qName)) {
            return;
        }
        if (TAG_GRID_AUTO.equals(qName)) {
            return;
        }
        if (TAG_GRID_SNAP.equals(qName)) {
            return;
        }
        if (TAG_TREE_NODE.equals(qName) || TAG_DOCUMENT_PANE.equals(qName) || TAG_PART.equals(qName)) {
            return;
        }
        throw new EdaSAXException("unknown tag in project: " + qName);
    }
}
