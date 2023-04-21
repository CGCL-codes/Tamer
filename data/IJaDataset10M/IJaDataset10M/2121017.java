package com.flagstone.transform;

import java.util.*;

/**
FSDefineTextField defines an editable text field. 
 
<p>The value entered into the text field is assigned to a specified variable 
allowing the creation of forms to accept values entered by a person viewing the 
Flash file.</p>

<p>The class contains a complex set of attributes which allows a high degree of 
control over how a text field is displayed:</p>

<table class="datasheet">

<tr><th align="left" colspan="2">Attributes</th></tr>

<tr>
<td><a name="FSDefineTextField_0">type</a></td>
<td>Identifies the data structure when it is encoded. Read-only.</td>
</tr>

<tr>
<td><a name="FSDefineTextField_1">identifier</a></td>
<td>An unique identifier for this object in the range 1..65535.</td>
</tr>
<tr>
<td><a name="FSDefineTextField_2">bounds</a></td>
<td>The bounding rectangle for the text field.</td>
</tr>
<tr>
<td><a name="FSDefineTextField_3">wordWrap</a></td>
<td>Indicates whether the text should be wrapped.</td>
</tr>
<tr>
<td><a name="FSDefineTextField_4">multiline</a></td>
<td>Indicates whether the text field contains multiple lines.</td>
</tr>
<tr>
<td><a name="FSDefineTextField_5">password</a></td>
<td>Indicates whether the text field will be used to display a password.</td>
</tr>
<tr>
<td><a name="FSDefineTextField_6">readOnly</a></td>
<td>Indicates whether the text field is read only.</td>
</tr>
<tr>
<td><a name="FSDefineTextField_7">selectable</a></td>
<td>Indicates whether the text field is selectable.</td>
</tr>
<tr>
<td><a name="FSDefineTextField_8">bordered</a></td>
<td>Indicates whether the text field is bordered.</td>
</tr>
<tr>
<td><a name="FSDefineTextField_9">HTML</a></td>
<td>Indicates whether the text field contains HTML.</td>
</tr>
<tr>
<td><a name="FSDefineTextField_10">fontIdentifier</a></td>
<td>The identifier of the font displayed in the text field.</td>
</tr>
<tr>
<td><a name="FSDefineTextField_11">height</a></td>
<td>The height of the characters in twips.</td>
</tr>
<tr>
<td><a name="FSDefineTextField_12">useFontGlyphs</a></td>
<td>Use either the glyphs defined in the movie to display the text or load the 
specified from the platform on which the Flash Player is hosted.</td>
</tr>
<tr>
<td><a name="FSDefineTextField_13">autosize</a></td>
<td>Indicates whether the text field will resize automatically to fit the text 
entered.</td>
</tr>
<tr>
<td><a name="FSDefineTextField_14">colour</a></td>
<td>The colour of the text. If set to null then the text colour defaults to 
black.</td>
</tr>
<tr>
<td><a name="FSDefineTextField_15">maxLength</a></td>
<td>The maximum length of the text field. May be set to zero is not maximum 
length is defined.</td>
</tr>
<tr>
<td><a name="FSDefineTextField_16">variableName</a></td>
<td>The name of the variable the text will be assigned to.</td>
</tr>
<tr>
<td><a name="FSDefineTextField_17">initialText</a></td>
<td>The default text displayed in the text field.</td>
</tr>
</table>

<p>Additional layout information for the spacing of the text relative to the 
text field borders can also be specified through the following set of 
attributes:</p>

<table class="datasheet">

<tr>
<td><a name="FSDefineTextField_17">alignment</a></td><td>Whether the text in the 
field is left-aligned, right-aligned, centred.</td>
</tr>
<tr>
<td><a name="FSDefineTextField_18">leftMargin</a></td><td>Left margin in 
twips.</td>
</tr>
<tr>
<td><a name="FSDefineTextField_19">rightMargin</a></td><td>Right margin in 
twips.</td>
</tr>
<tr>
<td><a name="FSDefineTextField_20">indent</a></td><td>Text indentation in 
twips.</td>
</tr>
<tr>
<td><a name="FSDefineTextField_21">leading</a></td><td>Leading in twips.</td>
</tr>
</table>

<p>The default values for the alignment is AlignLeft while the leftMargin, 
rightMargin indent and leading attributes are set to the constant
Transform.VALUE_NOT_SET. If the attributes remain unchanged then the layout 
information will not be encoded. If any of the values in this group are set then 
they must all have values assigned for the field to be displayed correctly
otherwise default values of 0 will be used.</p>

<p><b>HTML Support</b><br/>
Setting the HTML flag to true allows text marked up with a limited set of HTML 
tags to be displayed in the text field. The following tags are supported:</p>

<table class="datasheet">
<tr>
<td>&lt;p&gt;&lt;/p&gt;</td>
<td>Delimits a paragraph. Only the align attribute is supported:<br>
<p [align = left | right | center ]></p>
</td>
</tr>

<tr>
<td>&lt;br&gt;</td>
<td>Inserts a line break.></p></td>
</tr>

<tr>
<td>&lt;a&gt;&lt;/a&gt;</td>
<td>Define a hyperlink. Two attributes are supported:
<ul>
<li>href - the URL of the link.</li>
<li>target - name of a window or frame. (optional)</li>
</ul>
</td>
</tr>

<tr>
<td>&lt;font&gt;&lt;/font&gt;</td>
<td>Format enclosed text using the font. Three attributes are supported:
<ul>
<li>name - must match the name of a font defined using the FSDefineFont2 class.</li>
<li>size - the height of the font in twips.</li>
<li>color - the colour of the text in the hexadecimal format #RRGGBB.</li>
</ul>
</td>
</tr>

<tr>
<td>&lt;b&gt;&lt;/b&gt;</td>
<td>Delimits text that should be displayed in bold.</td>
</tr>

<tr>
<td>&lt;b&gt;&lt;/b&gt;</td>
<td>Delimits text that should be displayed in italics.</td>
</tr>

<tr>
<td>&lt;b&gt;&lt;/b&gt;</td>
<td>Delimits text that should be displayed underlined.</td>
</tr>

<tr>
<td>&lt;li&gt;&lt;/li&gt;</td>
<td>Display bulleted paragraph. Strictly speaking this is not an HTML list. 
The &lt;ul&gt; tag is not required and no other list format is supported.</td>
</tr>

</table>

<h1 class="datasheet">History</h1>

<p>FSDefineTextField class represents the DefineTextField from the Macromedia Flash (SWF) File Format Specification. It was introduced in Flash 4.</p>
 */
public class FSDefineTextField extends FSDefineObject {

    /** Key used to identify the wordWrapped attribute. */
    public static final String WordWrapped = "WordWrapped";

    /** Key used to identify the multiline attribute. */
    public static final String Multiline = "Multiline";

    /** Key used to identify the password attribute. */
    public static final String Password = "Password";

    /** Key used to identify the readOnly attribute. */
    public static final String ReadOnly = "ReadOnly";

    /** Key used to identify the selectable attribute. */
    public static final String Selectable = "Selectable";

    /** Key used to identify the bordered attribute. */
    public static final String Bordered = "Bordered";

    /** Key used to identify the bordered attribute. */
    public static final String HTML = "HTML";

    /** Key used to identify the useFontGlyphs attribute. */
    public static final String UseFontGlyphs = "UseFontGlyphs";

    /** Key used to identify the autoSize attribute. */
    public static final String AutoSize = "AutoSize";

    /** Key used to identify the identifier attribute. */
    public static final String FontIdentifier = "FontIdentifier";

    /** Key used to identify the fontHeight attribute. */
    public static final String FontHeight = "FontHeight";

    /** Key used to identify the color attribute. */
    public static final String Color = "Color";

    /** Key used to identify the maxLength attribute. */
    public static final String MaxLength = "MaxLength";

    /** Key used to identify the leftMargin attribute. */
    public static final String LeftMargin = "LeftMargin";

    /** Key used to identify the rightMargin attribute. */
    public static final String RightMargin = "RightMargin";

    /** Key used to identify the indent attribute. */
    public static final String Indent = "Indent";

    /** Key used to identify the leading attribute. */
    public static final String Leading = "Leading";

    /** Key used to identify the variableName attribute. */
    public static final String VariableName = "VariableName";

    /** Key used to identify the initialText attribute. */
    public static final String InitialText = "InitialText";

    /** Defines that the text displayed in a text field is left aligned. */
    public static final int AlignLeft = 0;

    /** Defines that the text displayed in a text field is right aligned. */
    public static final int AlignRight = 1;

    /** Defines that the text displayed in a text field is centre aligned. */
    public static final int AlignCenter = 2;

    /** Defines that the text displayed in a text field is justified. */
    public static final int AlignJustify = 3;

    private FSBounds bounds = null;

    private boolean wordWrapped = false;

    private boolean multiline = false;

    private boolean password = false;

    private boolean readOnly = false;

    private int reserved1 = 0;

    private boolean selectable = false;

    private boolean bordered = false;

    private boolean reserved2 = false;

    private boolean html = false;

    private boolean useFontGlyphs = false;

    private boolean autoSize = false;

    private int fontIdentifier = 0;

    private int fontHeight = 0;

    private FSColor color = null;

    private int maxLength = 0;

    private int alignment = Transform.VALUE_NOT_SET;

    private int leftMargin = Transform.VALUE_NOT_SET;

    private int rightMargin = Transform.VALUE_NOT_SET;

    private int indent = Transform.VALUE_NOT_SET;

    private int leading = Transform.VALUE_NOT_SET;

    private String variableName = "";

    private String initialText = "";

    /**
     * Construct an FSDefineTextField object, initalizing it with values decoded from
     * an encoded object.
     * 
     * @param coder an FSCoder containing the binary data.
     */
    public FSDefineTextField(FSCoder coder) {
        super(DefineTextField, 0);
        decode(coder);
    }

    /** Constructs an FSDefineTextField object with the specified identifier and with the size defined by the bounding rectangle. All other attributes are set to their default values which will result in a blank, single-line, editable text field. Any values entered will not be assigned to a variable. Since no font is specified the text will be displayed in a non-spaced font 20 twips in height.

        @param anIdentifier the unique identifier for this object.
        @param aBounds the bounding rectangle for the field which defines its size.
        */
    public FSDefineTextField(int anIdentifier, FSBounds aBounds) {
        super(DefineTextField, anIdentifier);
        setBounds(aBounds);
    }

    /** Constructs an FSDefineTextField object setting the attributes added to the Hashtable. Each entry consists of a key-value pair. The key is one of the pre-defined attribute names while the value is an instance of a wrapper class (Boolean, Integer or String) that will be assigned to the specified attribute.

        @param anIdentifier the unique identifier for this object.
        @param aBounds the bounding rectangle for the field which defines its size.
        @param attributes a collection of key-value pairs identifying the attributes to be set.
        */
    public FSDefineTextField(int anIdentifier, FSBounds aBounds, Hashtable attributes) {
        super(DefineTextField, anIdentifier);
        setBounds(aBounds);
        setAttributes(attributes);
    }

    /**
     * Constructs an FSDefineText object by copying values from an existing 
     * object.
     *
     * @param obj an FSDefineText object.
     */
    public FSDefineTextField(FSDefineTextField obj) {
        super(obj);
        bounds = new FSBounds(obj.bounds);
        wordWrapped = obj.wordWrapped;
        multiline = obj.multiline;
        password = obj.password;
        readOnly = obj.readOnly;
        reserved1 = obj.reserved1;
        selectable = obj.selectable;
        bordered = obj.bordered;
        reserved2 = obj.reserved2;
        html = obj.html;
        useFontGlyphs = obj.useFontGlyphs;
        autoSize = obj.autoSize;
        fontIdentifier = obj.fontIdentifier;
        fontHeight = obj.fontHeight;
        color = new FSColor(obj.color);
        maxLength = obj.maxLength;
        alignment = obj.alignment;
        leftMargin = obj.leftMargin;
        rightMargin = obj.rightMargin;
        indent = obj.indent;
        leading = obj.leading;
        variableName = new String(obj.variableName);
        initialText = new String(obj.initialText);
    }

    /** Gets the bounding rectangle that completely encloses the text field.

        @return the bounding rectangle for the text field.
        */
    public FSBounds getBounds() {
        return bounds;
    }

    /** Does the text field support word wrapping.

        @return a flag indicating whether the text in the field will be wrapped.
        */
    public boolean isWordWrapped() {
        return wordWrapped;
    }

    /** Does the text field support multiple lines of text.

        @return a flag indicating whether the text in the field will contain multiple lines.
        */
    public boolean isMultiline() {
        return multiline;
    }

    /** Does the text field protect passwords being entered.

        @return a flag indicating whether the text in the field represents a password.
        */
    public boolean isPassword() {
        return password;
    }

    /** Is the text field read-only.

        @return a flag indicating whether the text in the field is read-only.
        */
    public boolean isReadOnly() {
        return readOnly;
    }

    /** Is the text field selectable.

        @return a flag indicating whether the text in the field is selectable.
        */
    public boolean isSelectable() {
        return selectable;
    }

    /** Is the text field bordered.

        @return a flag indicating whether the text in the field is bordered.
        */
    public boolean isBordered() {
        return bordered;
    }

    /** Does the text field contain HTML.

        @return a flag indicating whether the text in the field contains HTML.
        */
    public boolean isHTML() {
        return html;
    }

    /** Does the text field resize to fit the contents.
    
        @return a flag indicating whether the text field will resize automatically. 
      */
    public boolean isAutoSize() {
        return autoSize;
    }

    /** Sets whether the text field will resize to fit the contents.
     *
     * @param aFlag indicate whether the text field will resize automatically.
     */
    public void setAutoSize(boolean aFlag) {
        autoSize = aFlag;
    }

    /** 
     * Indicates whether the test will be displayed using the font defined in the movie or 
     * whether a font defined on the host platform will be used.
     *
     * @return true if the text will be displayed using the glyphs from the font defined 
     * in the movie, false if the glyphs will be loaded from the platform on which the 
     * Flash Player is hosted.
     */
    public boolean useFontGlyphs() {
        return useFontGlyphs;
    }

    /** Gets the identifier of the font used to display the characters.

        @return the font identifier.
        */
    public int getFontIdentifier() {
        return fontIdentifier;
    }

    /** Gets the height of the characters.

        @return the height of the font.
        */
    public int getFontHeight() {
        return fontHeight;
    }

    /** Gets the text color.

        @return the color of the text.
        */
    public FSColor getColor() {
        return color;
    }

    /** Gets the maximum length of the text displayed.

        @return the maximum number of characters displayed.
        */
    public int getMaxLength() {
        return maxLength;
    }

    /** Gets the alignment of the text, either AlignLeft, AlignRight, AlignCenter or AlignJustify.

        @return the alignment code.
        */
    public int getAlignment() {
        return alignment;
    }

    /** Gets the left margin in twips.

        @return the left margin.
        */
    public int getLeftMargin() {
        return leftMargin;
    }

    /** Gets the right margin in twips.

        @return the right margin.
        */
    public int getRightMargin() {
        return rightMargin;
    }

    /** Gets the indentation of the first line of text in twips.

        @return the indentation of the first line.
        */
    public int getIndent() {
        return indent;
    }

    /** Gets the leading in twips.

        @return the leading.
        */
    public int getLeading() {
        return leading;
    }

    /** Gets the name of the variable the value in the text field will be assigned to.

        @return the name of the variable.
        */
    public String getVariableName() {
        return variableName;
    }

    /** Gets the default text displayed in the field.

        @return the default value displayed in the field.
    */
    public String getInitialText() {
        return initialText;
    }

    /** Gets the list of attributes for the object. The Hashtable contains a list of key-value pairs. The key is one of the pre-defined attribute names while the value is an instance of a wrapper class (Boolean, Integer or String) that contains the corresponding object attribute.

        @return the list of attributes for the object.
        */
    public Hashtable getAttributes() {
        Hashtable attributes = new Hashtable();
        attributes.put(WordWrapped, new Boolean(isWordWrapped()));
        attributes.put(Multiline, new Boolean(isMultiline()));
        attributes.put(Password, new Boolean(isPassword()));
        attributes.put(ReadOnly, new Boolean(isReadOnly()));
        attributes.put(AutoSize, new Boolean(isAutoSize()));
        attributes.put(Selectable, new Boolean(isSelectable()));
        attributes.put(Bordered, new Boolean(isBordered()));
        attributes.put(HTML, new Boolean(isHTML()));
        attributes.put(UseFontGlyphs, new Boolean(useFontGlyphs()));
        attributes.put(FontIdentifier, new Integer(getFontIdentifier()));
        attributes.put(FontHeight, new Integer(getFontHeight()));
        attributes.put(Color, getColor());
        attributes.put(MaxLength, new Integer(getMaxLength()));
        attributes.put(LeftMargin, new Integer(getLeftMargin()));
        attributes.put(RightMargin, new Integer(getRightMargin()));
        attributes.put(Indent, new Integer(getIndent()));
        attributes.put(Leading, new Integer(getLeading()));
        attributes.put(VariableName, getVariableName());
        attributes.put(InitialText, getInitialText());
        return attributes;
    }

    /** Sets the bounding rectangle of the text field.

        @param aBounds the bounding rectangle enclosing the text field.
        */
    public void setBounds(FSBounds aBounds) {
        bounds = aBounds;
    }

    /** Set whether the text field supports word wrapping.

        @param aFlag set whether the text field is word wrapped.
        */
    public void setWordWrapped(boolean aFlag) {
        wordWrapped = aFlag;
    }

    /** Set whether the text field contains multiple lines of text.

        @param aFlag set whether the text field is multiline.
        */
    public void setMultiline(boolean aFlag) {
        multiline = aFlag;
    }

    /** Set whether the text field should protect passwords entered.

        @param aFlag set whether the text field is password protected.
        */
    public void setPassword(boolean aFlag) {
        password = aFlag;
    }

    /** Set whether the text field is read-only.

        @param aFlag set whether the text field is read-only.
        */
    public void setReadOnly(boolean aFlag) {
        readOnly = aFlag;
    }

    /** Set whether the text field is selectable.

        @param aFlag set whether the text field is selectable.
        */
    public void setSelectable(boolean aFlag) {
        selectable = !aFlag;
    }

    /** Set whether the text field is bordered.

        @param aFlag set whether the text field is bordered.
        */
    public void setBordered(boolean aFlag) {
        bordered = aFlag;
    }

    /** Set whether the text field contains HTML.

        @param aFlag set whether the text field contains HTML.
        */
    public void setHTML(boolean aFlag) {
        html = aFlag;
    }

    /** 
     * Set whether the text field characters are displayed using the font defined in the movie 
     * or whether the Flash Player uses a font definition loaded from the platform on which it 
     * is hosted.
     *
     * @param aFlag set whether the text field characters will be drawn using the font in the 
     * movie (true) or use a font loaded by the Flash Player (false).
        */
    public void setUseFontGlyphs(boolean aFlag) {
        useFontGlyphs = aFlag;
    }

    /** Sets the identifier of the font used to display the characters.

        @param anIdentifier the identifier for the font that the text will be rendered in.
        */
    public void setFontIdentifier(int anIdentifier) {
        fontIdentifier = anIdentifier;
    }

    /** Sets the height of the characters.

        @param aNumber the height of the font.
        */
    public void setFontHeight(int aNumber) {
        fontHeight = aNumber;
    }

    /** Sets the text color. If set to null then the text color defaults to black.

        @param aColor the colour object that defines the text colour.
        */
    public void setColor(FSColor aColor) {
        color = aColor;
    }

    /** Sets the maximum length of the text displayed. May be set to zero if no maximum length is defined.

        @param aNumber the maximum number of characters displayed in the field.
        */
    public void setMaxLength(int aNumber) {
        maxLength = aNumber;
    }

    /** Sets the alignment of the text, either AlignLeft, AlignRight, AlignCenter or AlignJustify.

        @param aType the type of alignment.
        */
    public void setAlignment(int aType) {
        alignment = aType;
    }

    /** Sets the left margin in twips.

        @param aNumber the width of the left margin.
        */
    public void setLeftMargin(int aNumber) {
        leftMargin = aNumber;
    }

    /** Sets the right margin in twips.

        @param aNumber the width of the right margin.
        */
    public void setRightMargin(int aNumber) {
        rightMargin = aNumber;
    }

    /** Gets the indentation of the first line of text in twips.

        @param aNumber the indentation for the first line.
        */
    public void setIndent(int aNumber) {
        indent = aNumber;
    }

    /** Sets the leading in twips.

        @param aNumber the value for the leading.
        */
    public void setLeading(int aNumber) {
        leading = aNumber;
    }

    /** Sets the name of the variable the value in the text field will be assigned to.

        @param aString the name of the variable.
        */
    public void setVariableName(String aString) {
        variableName = aString;
    }

    /** Sets the value that will initially be displayed in the text field.

        @param aString the initial text displayed.
        */
    public void setInitialText(String aString) {
        initialText = aString;
    }

    /** Sets the attributes for the object. The Hashtable contains a list of key-value pairs. The key is one of the pre-defined attribute names while the value is an instance of a wrapper class (Boolean, Integer or String) that will be assigned to the specified attribute.

        @param attributes the list of attributes for the object.
        */
    public void setAttributes(Hashtable attributes) {
        if (attributes.get(WordWrapped) != null) setWordWrapped(((Boolean) attributes.get(WordWrapped)).booleanValue());
        if (attributes.get(Multiline) != null) setMultiline(((Boolean) attributes.get(Multiline)).booleanValue());
        if (attributes.get(Password) != null) setPassword(((Boolean) attributes.get(Password)).booleanValue());
        if (attributes.get(ReadOnly) != null) setReadOnly(((Boolean) attributes.get(ReadOnly)).booleanValue());
        if (attributes.get(Selectable) != null) setSelectable(((Boolean) attributes.get(Selectable)).booleanValue());
        if (attributes.get(Bordered) != null) setBordered(((Boolean) attributes.get(Bordered)).booleanValue());
        if (attributes.get(HTML) != null) setHTML(((Boolean) attributes.get(HTML)).booleanValue());
        if (attributes.get(AutoSize) != null) setAutoSize(((Boolean) attributes.get(AutoSize)).booleanValue());
        if (attributes.get(UseFontGlyphs) != null) setUseFontGlyphs(((Boolean) attributes.get(UseFontGlyphs)).booleanValue());
        if (attributes.get(FontIdentifier) != null) setFontIdentifier(((Integer) attributes.get(FontIdentifier)).intValue());
        if (attributes.get(FontHeight) != null) setFontHeight(((Integer) attributes.get(FontHeight)).intValue());
        if (attributes.get(Color) != null) setColor((FSColor) attributes.get(Color));
        if (attributes.get(MaxLength) != null) setMaxLength(((Integer) attributes.get(MaxLength)).intValue());
        if (attributes.get(LeftMargin) != null) setLeftMargin(((Integer) attributes.get(LeftMargin)).intValue());
        if (attributes.get(RightMargin) != null) setRightMargin(((Integer) attributes.get(RightMargin)).intValue());
        if (attributes.get(Indent) != null) setIndent(((Integer) attributes.get(Indent)).intValue());
        if (attributes.get(Leading) != null) setLeading(((Integer) attributes.get(Leading)).intValue());
        if (attributes.get(VariableName) != null) setVariableName((String) attributes.get(VariableName));
        if (attributes.get(InitialText) != null) setInitialText((String) attributes.get(InitialText));
    }

    public Object clone() {
        FSDefineTextField anObject = (FSDefineTextField) super.clone();
        anObject.bounds = (bounds != null) ? (FSBounds) bounds.clone() : null;
        anObject.color = (color != null) ? (FSColor) color.clone() : null;
        return anObject;
    }

    public boolean equals(Object anObject) {
        boolean result = false;
        if (super.equals(anObject)) {
            FSDefineTextField typedObject = (FSDefineTextField) anObject;
            if (bounds != null) result = bounds.equals(typedObject.bounds); else result = bounds == typedObject.bounds;
            result = result && wordWrapped == typedObject.wordWrapped;
            result = result && multiline == typedObject.multiline;
            result = result && password == typedObject.password;
            result = result && readOnly == typedObject.readOnly;
            result = result && reserved1 == typedObject.reserved1;
            result = result && autoSize == typedObject.autoSize;
            result = result && selectable == typedObject.selectable;
            result = result && bordered == typedObject.bordered;
            result = result && reserved2 == typedObject.reserved2;
            result = result && html == typedObject.html;
            result = result && useFontGlyphs == typedObject.useFontGlyphs;
            result = result && fontIdentifier == typedObject.fontIdentifier;
            result = result && fontHeight == typedObject.fontHeight;
            if (color != null) result = result && color.equals(typedObject.color); else result = result && color == typedObject.color;
            result = result && maxLength == typedObject.maxLength;
            if (containsLayoutInfo()) {
                result = result && alignment == typedObject.alignment;
                result = result && leftMargin == typedObject.leftMargin;
                result = result && rightMargin == typedObject.rightMargin;
                result = result && indent == typedObject.indent;
                result = result && leading == typedObject.leading;
            }
            if (variableName != null) result = result && variableName.equals(typedObject.variableName); else result = result && variableName == typedObject.variableName;
            if (initialText != null) result = result && initialText.equals(typedObject.initialText); else result = result && initialText == typedObject.initialText;
        }
        return result;
    }

    public void appendDescription(StringBuffer buffer, int depth) {
        buffer.append(name());
        if (depth > 0) {
            buffer.append(": { ");
            Transform.append(buffer, "bounds", bounds, depth);
            Transform.append(buffer, "wordWrapped", wordWrapped);
            Transform.append(buffer, "multiline", multiline);
            Transform.append(buffer, "password", password);
            Transform.append(buffer, "readOnly", readOnly);
            Transform.append(buffer, "autoSize", autoSize);
            Transform.append(buffer, "selectable", selectable);
            Transform.append(buffer, "bordered", bordered);
            Transform.append(buffer, "HTML", html);
            Transform.append(buffer, "useFontGlyphs", useFontGlyphs);
            Transform.append(buffer, "fontIdentifier", fontIdentifier);
            Transform.append(buffer, "fontHeight", fontHeight);
            Transform.append(buffer, "color", color, depth);
            Transform.append(buffer, "maxLength", maxLength);
            Transform.append(buffer, "alignment", alignment);
            Transform.append(buffer, "leftMargin", leftMargin);
            Transform.append(buffer, "rightMargin", rightMargin);
            Transform.append(buffer, "indent", indent);
            Transform.append(buffer, "leading", leading);
            Transform.append(buffer, "variableName", variableName);
            Transform.append(buffer, "initalText", initialText);
            buffer.append("}");
        }
    }

    public int length(FSCoder coder) {
        boolean _containsFont = containsFont();
        boolean _containsColor = containsColor();
        boolean _containsMaxLength = containsMaxLength();
        boolean _containsText = containsText();
        super.length(coder);
        coder.context[FSCoder.TransparentColors] = 1;
        length += bounds.length(coder);
        length += 2;
        length += (_containsFont) ? 4 : 0;
        length += (_containsColor) ? color.length(coder) : 0;
        length += (_containsMaxLength) ? 2 : 0;
        length += (containsLayoutInfo()) ? 9 : 0;
        length += coder.strlen(variableName, true);
        length += (_containsText) ? coder.strlen(initialText, true) : 0;
        coder.context[FSCoder.TransparentColors] = 0;
        return length;
    }

    public void encode(FSCoder coder) {
        boolean _containsFont = containsFont();
        boolean _containsColor = containsColor();
        boolean _containsMaxLength = containsMaxLength();
        boolean _containsText = containsText();
        super.encode(coder);
        coder.context[FSCoder.TransparentColors] = 1;
        bounds.encode(coder);
        coder.writeBits(_containsText ? 1 : 0, 1);
        coder.writeBits(wordWrapped ? 1 : 0, 1);
        coder.writeBits(multiline ? 1 : 0, 1);
        coder.writeBits(password ? 1 : 0, 1);
        coder.writeBits(readOnly ? 1 : 0, 1);
        coder.writeBits(_containsColor ? 1 : 0, 1);
        coder.writeBits(_containsMaxLength ? 1 : 0, 1);
        coder.writeBits(_containsFont ? 1 : 0, 1);
        coder.writeBits(0, 1);
        coder.writeBits(autoSize ? 1 : 0, 1);
        coder.writeBits(containsLayoutInfo() ? 1 : 0, 1);
        coder.writeBits(selectable ? 1 : 0, 1);
        coder.writeBits(bordered ? 1 : 0, 1);
        coder.writeBits(0, 1);
        coder.writeBits(html ? 1 : 0, 1);
        coder.writeBits(useFontGlyphs ? 1 : 0, 1);
        if (_containsFont) {
            coder.writeWord(fontIdentifier, 2);
            coder.writeWord(fontHeight, 2);
        }
        if (_containsColor) color.encode(coder);
        if (_containsMaxLength) coder.writeWord(maxLength, 2);
        if (containsLayoutInfo()) {
            coder.writeWord((alignment != Transform.VALUE_NOT_SET) ? alignment : 0, 1);
            coder.writeWord((leftMargin != Transform.VALUE_NOT_SET) ? leftMargin : 0, 2);
            coder.writeWord((rightMargin != Transform.VALUE_NOT_SET) ? rightMargin : 0, 2);
            coder.writeWord((indent != Transform.VALUE_NOT_SET) ? indent : 0, 2);
            coder.writeWord((leading != Transform.VALUE_NOT_SET) ? leading : 0, 2);
        }
        coder.writeString(variableName);
        coder.writeWord(0, 1);
        if (_containsText) {
            coder.writeString(initialText);
            coder.writeWord(0, 1);
        }
        coder.context[FSCoder.TransparentColors] = 0;
        coder.endObject(name());
    }

    public void decode(FSCoder coder) {
        boolean _containsFont = false;
        boolean _containsColor = false;
        boolean _containsMaxLength = false;
        boolean _containsText = false;
        boolean _containsLayout = false;
        super.decode(coder);
        coder.context[FSCoder.TransparentColors] = 1;
        bounds = new FSBounds(coder);
        _containsText = coder.readBits(1, false) != 0 ? true : false;
        wordWrapped = coder.readBits(1, false) != 0 ? true : false;
        multiline = coder.readBits(1, false) != 0 ? true : false;
        password = coder.readBits(1, false) != 0 ? true : false;
        readOnly = coder.readBits(1, false) != 0 ? true : false;
        _containsColor = coder.readBits(1, false) != 0 ? true : false;
        _containsMaxLength = coder.readBits(1, false) != 0 ? true : false;
        _containsFont = coder.readBits(1, false) != 0 ? true : false;
        reserved1 = coder.readBits(1, false);
        autoSize = coder.readBits(1, false) != 0 ? true : false;
        _containsLayout = coder.readBits(1, false) != 0 ? true : false;
        selectable = coder.readBits(1, false) != 0 ? true : false;
        bordered = coder.readBits(1, false) != 0 ? true : false;
        reserved2 = coder.readBits(1, false) != 0 ? true : false;
        html = coder.readBits(1, false) != 0 ? true : false;
        useFontGlyphs = coder.readBits(1, false) != 0 ? true : false;
        if (_containsFont) {
            fontIdentifier = coder.readWord(2, false);
            fontHeight = coder.readWord(2, false);
        }
        if (_containsColor) color = new FSColor(coder);
        if (_containsMaxLength) maxLength = coder.readWord(2, false);
        if (_containsLayout) {
            alignment = coder.readWord(1, false);
            leftMargin = coder.readWord(2, false);
            rightMargin = coder.readWord(2, false);
            indent = coder.readWord(2, false);
            leading = coder.readWord(2, true);
        }
        variableName = coder.readString();
        if (_containsText) initialText = coder.readString();
        coder.context[FSCoder.TransparentColors] = 0;
        coder.endObject(name());
    }

    private boolean containsColor() {
        return color != null;
    }

    private boolean containsFont() {
        return fontIdentifier != 0 && fontHeight != 0;
    }

    private boolean containsMaxLength() {
        return maxLength > 0;
    }

    private boolean containsLayoutInfo() {
        boolean layout = false;
        layout = alignment != Transform.VALUE_NOT_SET;
        layout = layout || leftMargin != Transform.VALUE_NOT_SET;
        layout = layout || rightMargin != Transform.VALUE_NOT_SET;
        layout = layout || indent != Transform.VALUE_NOT_SET;
        layout = layout || leading != Transform.VALUE_NOT_SET;
        return layout;
    }

    private boolean containsText() {
        return initialText != null && initialText.length() > 0;
    }
}
