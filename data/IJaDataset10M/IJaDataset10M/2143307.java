package SyntaxHighlight;

import SyntaxHighlight.CTokenMarker;
import SyntaxHighlight.KeywordMap;
import SyntaxHighlight.Token;

/**
 *
 * @author Stuart
 */
public class CSSTokenMarker extends CTokenMarker {

    public CSSTokenMarker() {
        super(false, getKeywords());
    }

    public static KeywordMap getKeywords() {
        if (cssKeywords == null) {
            cssKeywords = new KeywordMap(false);
            cssKeywords.add("caption-side", Token.KEYWORD3);
            cssKeywords.add("width", Token.KEYWORD3);
            cssKeywords.add("height", Token.KEYWORD3);
            cssKeywords.add("display", Token.KEYWORD3);
            cssKeywords.add("float", Token.KEYWORD3);
            cssKeywords.add("color", Token.KEYWORD3);
            cssKeywords.add("width", Token.KEYWORD3);
            cssKeywords.add("height", Token.KEYWORD3);
            cssKeywords.add("display", Token.KEYWORD3);
            cssKeywords.add("float", Token.KEYWORD3);
            cssKeywords.add("visibility", Token.KEYWORD3);
            cssKeywords.add("z-index", Token.KEYWORD3);
            cssKeywords.add("font", Token.KEYWORD3);
            cssKeywords.add("family", Token.KEYWORD3);
            cssKeywords.add("size", Token.KEYWORD3);
            cssKeywords.add("style", Token.KEYWORD3);
            cssKeywords.add("variant", Token.KEYWORD3);
            cssKeywords.add("border", Token.KEYWORD3);
            cssKeywords.add("top", Token.KEYWORD3);
            cssKeywords.add("left", Token.KEYWORD3);
            cssKeywords.add("bottom", Token.KEYWORD3);
            cssKeywords.add("right", Token.KEYWORD3);
            cssKeywords.add("collapse", Token.KEYWORD3);
            cssKeywords.add("spacing", Token.KEYWORD3);
            cssKeywords.add("background", Token.KEYWORD3);
            cssKeywords.add("attachment", Token.KEYWORD3);
            cssKeywords.add("image", Token.KEYWORD3);
            cssKeywords.add("position", Token.KEYWORD3);
            cssKeywords.add("type", Token.KEYWORD3);
            cssKeywords.add("repeat", Token.KEYWORD3);
            cssKeywords.add("align", Token.KEYWORD3);
            cssKeywords.add("decoration", Token.KEYWORD3);
            cssKeywords.add("text", Token.KEYWORD3);
            cssKeywords.add("margin", Token.KEYWORD3);
            cssKeywords.add("padding", Token.KEYWORD3);
            cssKeywords.add("list", Token.KEYWORD3);
            cssKeywords.add("transform", Token.KEYWORD3);
            cssKeywords.add("cursor", Token.KEYWORD3);
            cssKeywords.add("min", Token.KEYWORD3);
            cssKeywords.add("max", Token.KEYWORD3);
            cssKeywords.add("vertical", Token.KEYWORD3);
            cssKeywords.add("visibility", Token.KEYWORD3);
            cssKeywords.add("outline", Token.KEYWORD3);
        }
        return cssKeywords;
    }

    private static void addWithProperties(String word, String[] props, byte type) {
        cssKeywords.add(word, type);
        for (int i = 0; i < props.length; i++) {
            cssKeywords.add(word + "-" + props[i], type);
        }
    }

    private static void bulkAdd(String[] keywords, byte type) {
        for (int i = 0; i < keywords.length; i++) {
            cssKeywords.add(keywords[i], type);
        }
    }

    private static KeywordMap cssKeywords;
}
