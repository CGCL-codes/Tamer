package com.volantis.mcs.css.impl.parser.properties;

import com.volantis.mcs.themes.MutableStylePropertiesMock;
import com.volantis.mcs.themes.StylePropertyDetails;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.mcs.themes.properties.BackgroundXPositionKeywords;
import com.volantis.mcs.themes.properties.BackgroundYPositionKeywords;

/**
 * Test {@link BackgroundPositionParser}.
 */
public class BackgroundPositionParserTestCase extends ParsingPropertiesMockTestCaseAbstract {

    /**
     * Make sure that a single length works properly.
     */
    public void testSingleLengthValue() throws Exception {
        StyleValue expected = STYLE_VALUE_FACTORY.getPair(LENGTH_1PX, PERCENTAGE_50);
        expectSetProperty(StylePropertyDetails.BACKGROUND_POSITION, expected);
        parseDeclarations("background-position: 1px");
    }

    /**
     * Make sure that a single percentage works properly.
     */
    public void testSinglePercentageValue() throws Exception {
        StyleValue expected = STYLE_VALUE_FACTORY.getPair(PERCENTAGE_25, PERCENTAGE_50);
        expectSetProperty(StylePropertyDetails.BACKGROUND_POSITION, expected);
        parseDeclarations("background-position: 25%");
    }

    /**
     * Make sure that all combinations of the keyworws work properly.
     */
    public void testKeywords() throws Exception {
        StyleValue[] xValues = new StyleValue[] { BackgroundXPositionKeywords.CENTER, BackgroundXPositionKeywords.LEFT, BackgroundXPositionKeywords.CENTER, BackgroundXPositionKeywords.RIGHT };
        String[] xKeywords = new String[] { null, "left", "center", "right" };
        StyleValue[] yValues = new StyleValue[] { BackgroundYPositionKeywords.CENTER, BackgroundYPositionKeywords.TOP, BackgroundYPositionKeywords.CENTER, BackgroundYPositionKeywords.BOTTOM };
        String[] yKeywords = new String[] { null, "top", "center", "bottom" };
        styleSheetFactoryMock.createStyleProperties();
        for (int x = 0; x < xKeywords.length; x += 1) {
            for (int y = 0; y < yKeywords.length; y += 1) {
                String xKeyword = xKeywords[x];
                String yKeyword = yKeywords[y];
                if (xKeyword == null && yKeyword == null) {
                    continue;
                }
                doTestCombination(xKeyword, yKeyword, xValues[x], yValues[y]);
                if (xKeyword != null && yKeyword != null) {
                    doTestCombination(yKeyword, xKeyword, xValues[x], yValues[y]);
                }
            }
        }
    }

    /**
     * Make sure that all combinations of the keyworws work properly.
     */
    public void testMixedTypes() throws Exception {
        StyleValue[] firstValues = new StyleValue[] { LENGTH_1PX, PERCENTAGE_25, BackgroundXPositionKeywords.LEFT, BackgroundXPositionKeywords.CENTER, BackgroundXPositionKeywords.RIGHT };
        String[] firstKeywords = new String[] { "1px", "25%", "left", "center", "right" };
        StyleValue[] secondValues = new StyleValue[] { LENGTH_2CM, PERCENTAGE_50, BackgroundYPositionKeywords.TOP, BackgroundYPositionKeywords.CENTER, BackgroundYPositionKeywords.BOTTOM };
        String[] secondKeywords = new String[] { "2cm", "50%", "top", "center", "bottom" };
        styleSheetFactoryMock.createStyleProperties();
        for (int x = 0; x < firstKeywords.length; x += 1) {
            for (int y = 0; y < secondKeywords.length; y += 1) {
                String firstKeyword = firstKeywords[x];
                String secondKeyword = secondKeywords[y];
                if (firstKeyword == null && secondKeyword == null) {
                    continue;
                }
                doTestCombination(firstKeyword, secondKeyword, firstValues[x], secondValues[y]);
            }
        }
    }

    private void doTestCombination(String firstKeyword, String secondKeyword, final StyleValue xValue, final StyleValue yValue) {
        StringBuffer buffer;
        StyleValue pair;
        buffer = new StringBuffer("background-position:");
        if (firstKeyword != null) {
            buffer.append(" ").append(firstKeyword);
        }
        if (secondKeyword != null) {
            buffer.append(" ").append(secondKeyword);
        }
        mutableStylePropertiesMock = new MutableStylePropertiesMock("mutableStylePropertiesMock (" + buffer.toString() + ")", expectations);
        styleSheetFactoryMock.expects.createStyleProperties().returns(mutableStylePropertiesMock);
        pair = STYLE_VALUE_FACTORY.getPair(xValue, yValue);
        expectSetProperty(StylePropertyDetails.BACKGROUND_POSITION, pair);
        System.out.println("Testing " + buffer.toString());
        parseDeclarations(buffer.toString());
    }
}
