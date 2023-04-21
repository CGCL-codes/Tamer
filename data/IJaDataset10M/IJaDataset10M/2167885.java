package unbbayes.gui.mebn.auxiliary;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

/** 
 * This class contains auxiliary objects for the MEBN edition panes
 * 
 * @author Laecio Lima dos Santos (laecio@gmail.com). 
 */
public class MebnToolkit {

    private static Color borderColor = Color.BLUE;

    private static Color colorContext = new Color(176, 252, 131);

    private static Color colorResident = new Color(254, 250, 158);

    private static Color colorInput = new Color(220, 220, 220);

    private static Color colorTabPanelButton = Color.WHITE;

    private static Color colorTextFieldUnselected = Color.WHITE;

    private static Color colorTextFieldSelected = new Color(53, 253, 193);

    private static Color colorTextFieldError = new Color(232, 13, 13);

    private static Color color1 = new Color(187, 224, 227);

    private static Color color2 = new Color(0, 0, 0);

    private static Color color3 = new Color(0, 0, 0);

    private static Color color4 = new Color(0, 0, 0);

    private static Color color5 = new Color(0, 0, 0);

    private static Color color6 = new Color(0, 0, 0);

    public static TitledBorder getBorderForTabPanel(String title) {
        TitledBorder titledBorder;
        titledBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(borderColor), title);
        titledBorder.setTitleColor(borderColor);
        titledBorder.setTitleJustification(TitledBorder.CENTER);
        return titledBorder;
    }

    public static void setBorderColor(Color newColor) {
        borderColor = newColor;
    }

    public static Color getBorderColor() {
        return borderColor;
    }

    public static Color getColorContext() {
        return colorContext;
    }

    public static void setColorContext(Color colorContext) {
        MebnToolkit.colorContext = colorContext;
    }

    public static Color getColorInput() {
        return colorInput;
    }

    public static void setColorInput(Color colorInput) {
        MebnToolkit.colorInput = colorInput;
    }

    public static Color getColorResident() {
        return colorResident;
    }

    public static void setColorResident(Color colorResident) {
        MebnToolkit.colorResident = colorResident;
    }

    public static Color getColorTextFieldError() {
        return colorTextFieldError;
    }

    public static void setColorTextFieldError(Color colorTextFieldError) {
        MebnToolkit.colorTextFieldError = colorTextFieldError;
    }

    public static Color getColorTextFieldSelected() {
        return colorTextFieldSelected;
    }

    public static void setColorTextFieldSelected(Color colorTextFieldSelected) {
        MebnToolkit.colorTextFieldSelected = colorTextFieldSelected;
    }

    public static Color getColorTextFieldUnselected() {
        return colorTextFieldUnselected;
    }

    public static void setColorTextFieldUnselected(Color colorTextFieldUnselected) {
        MebnToolkit.colorTextFieldUnselected = colorTextFieldUnselected;
    }

    public static Color getColor1() {
        return color1;
    }

    public static void setColor1(Color color1) {
        MebnToolkit.color1 = color1;
    }

    public static Color getColor2() {
        return color2;
    }

    public static void setColor2(Color color2) {
        MebnToolkit.color2 = color2;
    }

    public static Color getColor3() {
        return color3;
    }

    public static void setColor3(Color color3) {
        MebnToolkit.color3 = color3;
    }

    public static Color getColor4() {
        return color4;
    }

    public static void setColor4(Color color4) {
        MebnToolkit.color4 = color4;
    }

    public static Color getColor5() {
        return color5;
    }

    public static void setColor5(Color color5) {
        MebnToolkit.color5 = color5;
    }

    public static Color getColor6() {
        return color6;
    }

    public static void setColor6(Color color6) {
        MebnToolkit.color6 = color6;
    }

    public static Color getColorTabPanelButton() {
        return colorTabPanelButton;
    }

    public static void setColorTabPanelButton(Color colorTabPanelButton) {
        MebnToolkit.colorTabPanelButton = colorTabPanelButton;
    }
}
