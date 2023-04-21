package org.wings.border;

import java.awt.Color;
import java.awt.Insets;
import org.wings.SComponent;
import org.wings.SConstants;
import org.wings.SFont;
import org.wings.SLabel;

/**
 * Display a titled border. This depends on the implementation
 * of &lt;fieldset&gt; in the browser (NS&gt;4, MSIE&gt;=4).
 * <br>Example:
 * <fieldset>
 * 	<legend><b>Title</b></legend>
 * 		Lorem ipsum dolor sit amet, consectetuer adipiscing elit, 
 * sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna 
 * aliquam erat volutpat. Ut wisi enim ad minim veniam, quis nostrud 
 * exerci tation ullamcorper suscipit lobortis nisl ut aliquip ex 
 * ea commodo consequat. Duis autem vel eum iriure dolor in hendrerit 
 * in vulputate velit esse molestie consequat, vel illum dolore 
 * eu feugiat nulla facilisis at vero et accumsan et iusto odio 
 * dignissim qui blandit praesent luptatum zzril delenit augue 
 * duis dolore te feugait nulla facilisi. Lorem ipsum dolor sit 
 * amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod 
 * tincidunt ut laoreet dolore magna aliquam erat volutpat. 
 * Ut wisi enim ad minim veniam, quis nostrud exerci tation 
 * ullamcorper suscipit lobortis nisl ut aliquip ex ea commodo consequat. 
 * Duis autem vel eum iriure dolor in hendrerit in vulputate 
 * velit esse molestie consequat, vel illum dolore eu feugiat 
 * nulla facilisis at vero et accumsan et iusto odio dignissim 
 * qui blandit praesent luptatum zzril delenit augue duis dolore te 
 * feugait nulla facilisi. Nam liber tempor cum soluta nobis eleifend 
 * option congue nihil imperdiet doming id quod mazim placerat facer 
 * possim assum.
 * </fieldset>
 * @author <a href="mailto:andre.lison@general-bytes.com">Andre Lison</a>
 * @version $Revision: 1759 $
 */
public class STitledBorder extends SAbstractBorder {

    /**
     * @see #getCGClassID
     */
    private static final String cgClassID = "TitledBorderCG";

    /**
     * The border to use.
     */
    private SBorder fBorder;

    /**
	 * The title to display
	 */
    private String fTitle;

    /**
	 * The font to use for the title.
	 */
    private SFont fTitleFont;

    /**
	 * The title color.
	 */
    private Color fTitleColor;

    /**
	 * The title position.
	 */
    private int fTitlePosition = SConstants.LEFT_ALIGN;

    /**
     * Constructor for STitledBorder.
     */
    public STitledBorder(SBorder border) {
        super();
        setBorder(border);
    }

    /**
     * Constructor for STitledBorder.
     * @param border the border to use
     * @param title the title to display
     */
    public STitledBorder(SBorder border, String title) {
        this(border);
        setTitle(title);
    }

    /**
     * Constructor for STitledBorder. Default border 
     * type is {@link SEtchedBorder}, thickness 2
     */
    public STitledBorder(String title) {
        this(new SEtchedBorder(SEtchedBorder.LOWERED));
        fBorder.setThickness(2);
        setTitle(title);
    }

    /**
     * Gets the border.
     * @return Returns a SBorder
     */
    public SBorder getBorder() {
        return fBorder;
    }

    /**
     * Sets the border.
     * @param border The border to set
     */
    public void setBorder(SBorder border) {
        fBorder = border;
    }

    /**
      * Set the border color.
      */
    public void setColor(Color color) {
        if (fBorder != null) fBorder.setColor(color);
    }

    /**
      * Get the border color.
      * @return the color or <code>null</code>,
      *     if border is <code>null</code>
      * @see #setBorder(SBorder)
      */
    public Color getColor() {
        return fBorder != null ? fBorder.getColor() : null;
    }

    /**
     * Gets the title.
     * @return Returns a String
     */
    public String getTitle() {
        return fTitle;
    }

    /**
     * Sets the title.
     * @param title The title to set
     */
    public void setTitle(String title) {
        fTitle = title;
    }

    /**
     * Gets the title color.
     * @return Returns a Color
     */
    public Color getTitleColor() {
        return fTitleColor;
    }

    /**
     * Sets the title color.
     * @param titleColor The title color to set
     */
    public void setTitleColor(Color titleColor) {
        fTitleColor = titleColor;
    }

    /**
     * Gets the title font.
     * @return Returns a SFont
     */
    public SFont getTitleFont() {
        return fTitleFont;
    }

    /**
     * Sets the title font.
     * @param titleFont The title font to set
     */
    public void setTitleFont(SFont titleFont) {
        fTitleFont = titleFont;
    }

    /**
     * Gets the titlePosition.
     * @return Returns a int
     */
    public int getTitlePosition() {
        return fTitlePosition;
    }

    /**
     * Sets the title position.
     * @param titlePosition The title-position to set:
     * 	{@link SConstants#LEFT_ALIGN}, {@link SConstants#CENTER_ALIGN},
     * 	{@link SConstants#RIGHT_ALIGN}
     */
    public void setTitlePosition(int titlePosition) {
        fTitlePosition = titlePosition;
    }

    /**
	 * Return TitleBorderCG
	 */
    public String getCGClassID() {
        return cgClassID;
    }
}
