package com.anotherbigidea.flash.readers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import com.anotherbigidea.flash.SWFConstants;
import com.anotherbigidea.flash.interfaces.SWFActionBlock;
import com.anotherbigidea.flash.interfaces.SWFActions;
import com.anotherbigidea.flash.interfaces.SWFShape;
import com.anotherbigidea.flash.interfaces.SWFTagTypes;
import com.anotherbigidea.flash.interfaces.SWFText;
import com.anotherbigidea.flash.interfaces.SWFVectors;
import com.anotherbigidea.flash.movie.Actions;
import com.anotherbigidea.flash.movie.Button;
import com.anotherbigidea.flash.movie.EditField;
import com.anotherbigidea.flash.movie.Font;
import com.anotherbigidea.flash.movie.FontDefinition;
import com.anotherbigidea.flash.movie.Frame;
import com.anotherbigidea.flash.movie.Image;
import com.anotherbigidea.flash.movie.Instance;
import com.anotherbigidea.flash.movie.MorphShape;
import com.anotherbigidea.flash.movie.Movie;
import com.anotherbigidea.flash.movie.MovieClip;
import com.anotherbigidea.flash.movie.QTMovie;
import com.anotherbigidea.flash.movie.Shape;
import com.anotherbigidea.flash.movie.Symbol;
import com.anotherbigidea.flash.movie.Text;
import com.anotherbigidea.flash.movie.TimeLine;
import com.anotherbigidea.flash.movie.Transform;
import com.anotherbigidea.flash.structs.AlphaColor;
import com.anotherbigidea.flash.structs.AlphaTransform;
import com.anotherbigidea.flash.structs.ButtonRecord;
import com.anotherbigidea.flash.structs.ButtonRecord2;
import com.anotherbigidea.flash.structs.Color;
import com.anotherbigidea.flash.structs.ColorTransform;
import com.anotherbigidea.flash.structs.Matrix;
import com.anotherbigidea.flash.structs.Rect;
import com.anotherbigidea.flash.structs.SoundInfo;
import com.anotherbigidea.flash.writers.SWFActionsImpl;

/**
 * An implementation of the SWFTagTypes interface that builds a Movie object
 */
public class MovieBuilder implements SWFTagTypes {

    protected Movie movie;

    protected MovieClip clip;

    protected boolean newMovie = false;

    protected Frame frame;

    protected Map symbols = new HashMap();

    protected TimeLine timeline;

    protected Map instances = new HashMap();

    public void tagDefineVideoStream(int id, int numFrames, int width, int height) throws IOException {
    }

    /**
     * Build a new Movie
     */
    public MovieBuilder() {
        movie = new Movie();
        newMovie = true;
        timeline = movie;
    }

    /**
     * Append to an existing movie (do not change size,rate,color,version etc)
     */
    public MovieBuilder(Movie movie) {
        this.movie = movie;
        newMovie = false;
        timeline = movie;
    }

    /**
     * Build the timeline of a MovieClip
     */
    protected MovieBuilder(MovieBuilder parent, MovieClip clip) {
        this.movie = parent.movie;
        this.symbols = parent.symbols;
        this.clip = clip;
        newMovie = false;
        timeline = clip;
    }

    public Movie getMovie() {
        return movie;
    }

    /**
     * Get the defined symbols - keyed by Integer( symbolId )
     */
    public Map getDefinedSymbols() {
        return symbols;
    }

    protected Symbol getSymbol(int id) {
        return (Symbol) symbols.get(new Integer(id));
    }

    protected void saveSymbol(int id, Symbol s) {
        symbols.put(new Integer(id), s);
    }

    protected Instance getInstance(int depth) {
        return (Instance) instances.get(new Integer(depth));
    }

    protected void saveInstance(int depth, Instance inst) {
        if (inst == null) {
            try {
                throw new Exception();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        instances.put(new Integer(depth), inst);
    }

    /**
     * SWFTags interface
     */
    public void tag(int tagType, boolean longTag, byte[] contents) throws IOException {
    }

    /**
     * SWFHeader interface.
     */
    public void header(int version, long length, int twipsWidth, int twipsHeight, int frameRate, int frameCount) throws IOException {
        if (newMovie) {
            movie.setVersion(version);
            movie.setWidth(twipsWidth / SWFConstants.TWIPS);
            movie.setHeight(twipsHeight / SWFConstants.TWIPS);
            movie.setFrameRate(frameRate);
        }
    }

    /**
     * SWFTagTypes interface
     */
    public void tagEnd() throws IOException {
    }

    /**
     * SWFTagTypes interface
     */
    public void tagShowFrame() throws IOException {
        if (frame == null) timeline.appendFrame(); else frame = null;
    }

    protected Frame currentFrame() {
        if (frame == null) frame = timeline.appendFrame();
        return frame;
    }

    public void tagDefineSound(int id, int format, int frequency, boolean bits16, boolean stereo, int sampleCount, byte[] soundData) throws IOException {
    }

    public void tagDefineButtonSound(int buttonId, int rollOverSoundId, SoundInfo rollOverSoundInfo, int rollOutSoundId, SoundInfo rollOutSoundInfo, int pressSoundId, SoundInfo pressSoundInfo, int releaseSoundId, SoundInfo releaseSoundInfo) throws IOException {
    }

    public void tagStartSound(int soundId, SoundInfo info) throws IOException {
    }

    public void tagSoundStreamHead(int playbackFrequency, boolean playback16bit, boolean playbackStereo, int streamFormat, int streamFrequency, boolean stream16bit, boolean streamStereo, int averageSampleCount) throws IOException {
    }

    public void tagSoundStreamHead2(int playbackFrequency, boolean playback16bit, boolean playbackStereo, int streamFormat, int streamFrequency, boolean stream16bit, boolean streamStereo, int averageSampleCount) throws IOException {
    }

    public void tagSoundStreamBlock(byte[] soundData) throws IOException {
    }

    public void tagSerialNumber(String serialNumber) throws IOException {
    }

    public void tagGenerator(byte[] data) throws IOException {
    }

    public void tagGeneratorText(byte[] data) throws IOException {
    }

    public void tagGeneratorCommand(byte[] data) throws IOException {
    }

    public void tagGeneratorFont(byte[] data) throws IOException {
    }

    public void tagNameCharacter(byte[] data) throws IOException {
    }

    public void tagDefineBits(int id, byte[] imageData) throws IOException {
    }

    public void tagJPEGTables(byte[] jpegEncodingData) throws IOException {
    }

    public void tagDefineBitsJPEG3(int id, byte[] imageData, byte[] alphaData) throws IOException {
    }

    public SWFActions tagDoInitAction(int spriteId) throws IOException {
        return null;
    }

    /**
     * SWFTagTypes interface
     */
    public SWFActions tagDoAction() throws IOException {
        Actions acts = currentFrame().actions(movie.getVersion());
        return acts;
    }

    /**
     * SWFTagTypes interface
     */
    public SWFShape tagDefineShape(int id, Rect outline) throws IOException {
        Shape s = new Shape();
        s.setBoundingRectangle(((double) outline.getMinX()) / SWFConstants.TWIPS, ((double) outline.getMinY()) / SWFConstants.TWIPS, ((double) outline.getMaxX()) / SWFConstants.TWIPS, ((double) outline.getMaxY()) / SWFConstants.TWIPS);
        saveSymbol(id, s);
        return new ShapeBuilder(s);
    }

    /**
     * SWFTagTypes interface
     */
    public SWFShape tagDefineShape2(int id, Rect outline) throws IOException {
        Shape s = new Shape();
        s.setBoundingRectangle(((double) outline.getMinX()) / SWFConstants.TWIPS, ((double) outline.getMinY()) / SWFConstants.TWIPS, ((double) outline.getMaxX()) / SWFConstants.TWIPS, ((double) outline.getMaxY()) / SWFConstants.TWIPS);
        saveSymbol(id, s);
        return new ShapeBuilder(s);
    }

    /**
     * SWFTagTypes interface
     */
    public SWFShape tagDefineShape3(int id, Rect outline) throws IOException {
        Shape s = new Shape();
        s.setBoundingRectangle(((double) outline.getMinX()) / SWFConstants.TWIPS, ((double) outline.getMinY()) / SWFConstants.TWIPS, ((double) outline.getMaxX()) / SWFConstants.TWIPS, ((double) outline.getMaxY()) / SWFConstants.TWIPS);
        saveSymbol(id, s);
        return new ShapeBuilder(s);
    }

    /**
     * SWFTagTypes interface
     */
    public void tagFreeCharacter(int charId) throws IOException {
    }

    /**
     * SWFTagTypes interface
     */
    public void tagPlaceObject(int charId, int depth, Matrix matrix, AlphaTransform cxform) throws IOException {
        Symbol s = getSymbol(charId);
        if (s == null) return;
        timeline.setAvailableDepth(depth);
        Instance inst = currentFrame().placeSymbol(s, (matrix != null) ? new Transform(matrix) : null, cxform);
        saveInstance(depth, inst);
    }

    /**
     * SWFTagTypes interface
     */
    public SWFActions tagPlaceObject2(boolean isMove, int clipDepth, int depth, int charId, Matrix matrix, AlphaTransform cxform, int ratio, String name, int clipActionFlags) throws IOException {
        Instance inst = null;
        if (depth == 59) {
        }
        if (isMove && charId <= 0) {
            inst = getInstance(depth);
            if (inst == null) {
                System.out.println("Failed to find Instance at depth " + depth);
                return null;
            }
            currentFrame().alter(inst, (matrix != null) ? new Transform(matrix) : null, cxform, ratio);
        } else {
            Symbol s = getSymbol(charId);
            if (depth == 59) {
            }
            if (s == null) {
                System.out.println("Failed to find Symbol with id " + charId);
                return null;
            }
            if (name != null) {
                Frame frame = currentFrame();
                if (isMove) {
                    inst = frame.replaceMovieClip(s, depth, (matrix != null) ? new Transform(matrix) : null, cxform, name, null);
                } else {
                    timeline.setAvailableDepth(depth);
                    inst = frame.placeMovieClip(s, (matrix != null) ? new Transform(matrix) : null, cxform, name, null);
                }
                saveInstance(depth, inst);
            } else if (clipActionFlags != 0) {
                return new ClipActionBuilder(s, matrix, cxform, depth, name, movie.getVersion(), isMove);
            } else {
                Frame frame = currentFrame();
                if (isMove) {
                    inst = frame.replaceSymbol(s, depth, (matrix != null) ? new Transform(matrix) : null, cxform, ratio, clipDepth);
                } else {
                    timeline.setAvailableDepth(depth);
                    inst = frame.placeSymbol(s, (matrix != null) ? new Transform(matrix) : null, cxform, ratio, clipDepth);
                }
                saveInstance(depth, inst);
            }
        }
        return null;
    }

    /**
     * SWFTagTypes interface
     */
    public void tagRemoveObject(int charId, int depth) throws IOException {
        Instance inst = getInstance(depth);
        if (inst == null) return;
        currentFrame().remove(inst);
    }

    /**
     * SWFTagTypes interface
     */
    public void tagRemoveObject2(int depth) throws IOException {
        Instance inst = getInstance(depth);
        if (inst == null) return;
        currentFrame().remove(inst);
    }

    /**
     * SWFTagTypes interface
     */
    public void tagSetBackgroundColor(Color color) throws IOException {
        if (newMovie) movie.setBackColor(color);
    }

    /**
     * SWFTagTypes interface
     */
    public void tagFrameLabel(String label) throws IOException {
        tagFrameLabel(label, false);
    }

    /**
	 * SWFTagTypes interface
	 */
    public void tagFrameLabel(String label, boolean isAnchor) throws IOException {
        currentFrame().setLabel(label);
        currentFrame().setAnchor(isAnchor);
    }

    /**
     * SWFTagTypes interface
     */
    public SWFTagTypes tagDefineSprite(int id) throws IOException {
        MovieClip clip = new MovieClip();
        saveSymbol(id, clip);
        return new MovieBuilder(this, clip);
    }

    /**
     * SWFTagTypes interface
     */
    public void tagProtect(byte[] password) throws IOException {
        if (newMovie) movie.protect(true);
    }

    /**
     * SWFTagTypes interface
     */
    public void tagEnableDebug(byte[] password) throws IOException {
    }

    /**
	 * SWFTagTypes interface
	 */
    public void tagEnableDebug2(byte[] password) throws IOException {
    }

    /**
     * SWFTagTypes interface
     */
    public SWFVectors tagDefineFont(int id, int numGlyphs) throws IOException {
        FontDefinition fontDef = new FontDefinition();
        Font font = new Font(fontDef);
        saveSymbol(id, font);
        return new GlyphBuilder(fontDef, font, numGlyphs);
    }

    /**
	 * SWFTagTypes interface
	 */
    public void tagDefineFontInfo(int fontId, String fontName, int flags, int[] codes) {
        defineFontInfo(fontId, fontName, flags, codes, SWFConstants.LANGUAGE_CODE_NONE);
    }

    /**
     * SWFTagTypes interface
     */
    public void defineFontInfo(int fontId, String fontName, int flags, int[] codes, int langCode) {
        Symbol s = getSymbol(fontId);
        if (s == null || !(s instanceof Font)) return;
        Font font = (Font) s;
        FontDefinition def = font.getDefinition();
        def.setName(fontName);
        boolean isSmallText = ((flags & SWFConstants.FONT_SMALLTEXT) != 0);
        boolean isShiftJIS = ((flags & SWFConstants.FONT_SHIFTJIS) != 0);
        boolean isAnsi = ((flags & SWFConstants.FONT_ANSI) != 0);
        boolean isItalic = ((flags & SWFConstants.FONT_ITALIC) != 0);
        boolean isBold = ((flags & SWFConstants.FONT_BOLD) != 0);
        def.setFontFlags(isSmallText, isShiftJIS, isAnsi, isItalic, isBold, false);
        List glyphs = font.getGlyphList();
        int glyphCount = glyphs.size();
        for (int i = 0; i < codes.length && i < glyphCount; i++) {
            int code = codes[i];
            font.setCode(i, code);
        }
        font.setLanguageCode(langCode);
    }

    /**
	 * SWFTagTypes interface
	 */
    public void tagDefineFontInfo2(int fontId, String fontName, int flags, int[] codes, int langCode) {
        defineFontInfo(fontId, fontName, flags, codes, langCode);
    }

    /**
     * SWFTagTypes interface
     */
    public SWFVectors tagDefineFont2(int id, int flags, String name, int numGlyphs, int ascent, int descent, int leading, int[] codes, int[] advances, Rect[] bounds, int[] kernCodes1, int[] kernCodes2, int[] kernAdjustments) throws IOException {
        boolean hasMetrics = ((flags & SWFConstants.FONT2_HAS_LAYOUT) != 0);
        boolean isShiftJIS = ((flags & SWFConstants.FONT2_SHIFTJIS) != 0);
        boolean isSmallText = ((flags & SWFConstants.FONT2_SMALLTEXT) != 0);
        boolean isAnsi = ((flags & SWFConstants.FONT2_ANSI) != 0);
        boolean isItalic = ((flags & SWFConstants.FONT2_ITALIC) != 0);
        boolean isBold = ((flags & SWFConstants.FONT2_BOLD) != 0);
        FontDefinition fontDef = new FontDefinition(name, ((double) ascent) / SWFConstants.TWIPS, ((double) descent) / SWFConstants.TWIPS, ((double) leading) / SWFConstants.TWIPS, isSmallText, isShiftJIS, isAnsi, isItalic, isBold, hasMetrics);
        Font font = new Font(fontDef);
        saveSymbol(id, font);
        if (hasMetrics && kernCodes1 != null) {
            List kerns = fontDef.getKerningPairList();
            for (int i = 0; i < kernCodes1.length; i++) {
                FontDefinition.KerningPair pair = new FontDefinition.KerningPair(kernCodes1[i], kernCodes2[i], ((double) kernAdjustments[i]) / SWFConstants.TWIPS);
                kerns.add(pair);
            }
        }
        return new GlyphBuilder(fontDef, font, codes, advances, bounds);
    }

    /**
     * SWFTagTypes interface
     */
    public void tagDefineTextField(int fieldId, String fieldName, String initialText, Rect boundary, int flags, AlphaColor textColor, int alignment, int fontId, int fontSize, int charLimit, int leftMargin, int rightMargin, int indentation, int lineSpacing) throws IOException {
        Symbol f = getSymbol(fontId);
        if (f == null || !(f instanceof Font)) return;
        Font font = (Font) f;
        EditField field = new EditField(fieldName, initialText, font, ((double) fontSize) / SWFConstants.TWIPS, ((double) boundary.getMinX()) / SWFConstants.TWIPS, ((double) boundary.getMinY()) / SWFConstants.TWIPS, ((double) boundary.getMaxX()) / SWFConstants.TWIPS, ((double) boundary.getMaxY()) / SWFConstants.TWIPS);
        field.setTextColor(textColor);
        field.setAlignment(alignment);
        field.setCharLimit(charLimit);
        field.setLeftMargin(((double) leftMargin) / SWFConstants.TWIPS);
        field.setRightMargin(((double) rightMargin) / SWFConstants.TWIPS);
        field.setIndentation(((double) indentation) / SWFConstants.TWIPS);
        field.setLineSpacing(((double) lineSpacing) / SWFConstants.TWIPS);
        boolean isSelectable = ((flags & SWFConstants.TEXTFIELD_NO_SELECTION) == 0);
        boolean hasBorder = ((flags & SWFConstants.TEXTFIELD_DRAW_BORDER) != 0);
        boolean isHtml = ((flags & SWFConstants.TEXTFIELD_HTML) != 0);
        boolean usesSystemFont = ((flags & SWFConstants.TEXTFIELD_FONT_GLYPHS) == 0);
        boolean hasWordWrap = ((flags & SWFConstants.TEXTFIELD_WORD_WRAP) != 0);
        boolean isMultiline = ((flags & SWFConstants.TEXTFIELD_IS_MULTILINE) != 0);
        boolean isPassword = ((flags & SWFConstants.TEXTFIELD_IS_PASSWORD) != 0);
        boolean isEditable = ((flags & SWFConstants.TEXTFIELD_DISABLE_EDIT) == 0);
        field.setProperties(isSelectable, hasBorder, isHtml, usesSystemFont, hasWordWrap, isMultiline, isPassword, isEditable);
        saveSymbol(fieldId, field);
    }

    /**
     * SWFTagTypes interface
     */
    public SWFText tagDefineText(int id, Rect bounds, Matrix matrix) throws IOException {
        Text text = new Text(new Transform(matrix));
        saveSymbol(id, text);
        return new TextBuilder(text);
    }

    /**
     * SWFTagTypes interface
     */
    public SWFText tagDefineText2(int id, Rect bounds, Matrix matrix) throws IOException {
        Text text = new Text(new Transform(matrix));
        saveSymbol(id, text);
        return new TextBuilder(text);
    }

    /**
     * SWFTagTypes interface
     */
    public SWFActions tagDefineButton(int id, Vector buttonRecords) throws IOException {
        Button but = new Button(false);
        saveSymbol(id, but);
        for (Enumeration e = buttonRecords.elements(); e.hasMoreElements(); ) {
            ButtonRecord rec = (ButtonRecord) e.nextElement();
            Symbol s = getSymbol(rec.getCharId());
            if (s == null) continue;
            int flags = rec.getFlags();
            boolean hit = ((flags & ButtonRecord.BUTTON_HITTEST) != 0);
            boolean up = ((flags & ButtonRecord.BUTTON_UP) != 0);
            boolean over = ((flags & ButtonRecord.BUTTON_OVER) != 0);
            boolean down = ((flags & ButtonRecord.BUTTON_DOWN) != 0);
            but.addLayer(s, new Transform(rec.getMatrix()), null, rec.getLayer(), hit, up, down, over);
        }
        return new ButtonActionBuilder(but, movie.getVersion());
    }

    /**
     * SWFTagTypes interface
     */
    public void tagButtonCXForm(int buttonId, ColorTransform transform) throws IOException {
        Symbol s = getSymbol(buttonId);
        if (s == null || !(s instanceof Button)) return;
        Button but = (Button) s;
        List layers = but.getButtonLayers();
        for (Iterator it = layers.iterator(); it.hasNext(); ) {
            Button.Layer layer = (Button.Layer) it.next();
            layer.setColoring(new AlphaTransform(transform));
        }
    }

    /**
     * SWFTagTypes interface
     */
    public SWFActions tagDefineButton2(int id, boolean trackAsMenu, Vector buttonRecord2s) throws IOException {
        Button but = new Button(trackAsMenu);
        saveSymbol(id, but);
        for (Enumeration e = buttonRecord2s.elements(); e.hasMoreElements(); ) {
            ButtonRecord2 rec = (ButtonRecord2) e.nextElement();
            Symbol s = getSymbol(rec.getCharId());
            if (s == null) continue;
            int flags = rec.getFlags();
            boolean hit = ((flags & ButtonRecord.BUTTON_HITTEST) != 0);
            boolean up = ((flags & ButtonRecord.BUTTON_UP) != 0);
            boolean over = ((flags & ButtonRecord.BUTTON_OVER) != 0);
            boolean down = ((flags & ButtonRecord.BUTTON_DOWN) != 0);
            but.addLayer(s, new Transform(rec.getMatrix()), rec.getTransform(), rec.getLayer(), hit, up, down, over);
        }
        return new ButtonActionBuilder(but, movie.getVersion());
    }

    /**
     * SWFTagTypes interface
     */
    public void tagExport(String[] names, int[] ids) throws IOException {
        Symbol[] symbols = new Symbol[ids.length];
        for (int i = 0; i < ids.length; i++) {
            symbols[i] = getSymbol(ids[i]);
        }
        movie.exportSymbols(names, symbols);
    }

    /**
     * SWFTagTypes interface
     */
    public void tagImport(String movieName, String[] names, int[] ids) throws IOException {
        movie.importSymbols(movieName, names);
    }

    /**
     * SWFTagTypes interface
     */
    public void tagDefineQuickTimeMovie(int id, String filename) throws IOException {
        saveSymbol(id, new QTMovie(filename));
    }

    /**
     * SWFTagTypes interface
     */
    public void tagDefineBitsJPEG2(int id, byte[] data) throws IOException {
        saveSymbol(id, new Image.JPEG(data));
    }

    /**
     * SWFTagTypes interface
     */
    public void tagDefineBitsJPEG2(int id, InputStream jpegImage) throws IOException {
        saveSymbol(id, new Image.JPEG(jpegImage));
    }

    /**
     * SWFTagTypes interface
     */
    public void tagDefineBitsLossless(int id, int format, int width, int height, Color[] colors, byte[] imageData) throws IOException {
        saveSymbol(id, new Image.Lossless(colors, imageData, ((double) width) / SWFConstants.TWIPS, ((double) height) / SWFConstants.TWIPS, false, format));
    }

    /**
     * SWFTagTypes interface
     */
    public void tagDefineBitsLossless2(int id, int format, int width, int height, Color[] colors, byte[] imageData) throws IOException {
        saveSymbol(id, new Image.Lossless(colors, imageData, ((double) width) / SWFConstants.TWIPS, ((double) height) / SWFConstants.TWIPS, true, format));
    }

    /**
     * SWFTagTypes interface
     */
    public SWFShape tagDefineMorphShape(int id, Rect startBounds, Rect endBounds) throws IOException {
        return new MorphShapeBuilder(id, startBounds, endBounds);
    }

    /**
     * A SWFActions implementation that breaks multiple action arrays into
     * separate Actions objects
     */
    protected static class ActionsBuilder extends SWFActionsImpl {

        protected int version;

        protected Vector actions = new Vector();

        protected ActionsBuilder(int flashVersion) {
            super(null);
            version = flashVersion;
        }

        public Actions[] getActions() {
            Actions[] a = new Actions[actions.size()];
            actions.copyInto(a);
            return a;
        }

        @Override
        public SWFActionBlock start(int conditions) throws IOException {
            acts = new Actions(conditions, version);
            actions.addElement(acts);
            return acts.start(conditions);
        }
    }

    protected class ButtonActionBuilder extends MovieBuilder.ActionsBuilder {

        protected Button but;

        protected ButtonActionBuilder(Button b, int flashVersion) {
            super(flashVersion);
            but = b;
        }

        @Override
        public SWFActionBlock start(int conditions) throws IOException {
            if (conditions == 0) conditions = SWFConstants.BUTTON2_OVERDOWN2OVERUP;
            acts = but.addActions(conditions, version);
            return acts.start(conditions);
        }
    }

    protected class ClipActionBuilder extends MovieBuilder.ActionsBuilder {

        protected Symbol symbol;

        protected Transform matrix;

        protected AlphaTransform cxform;

        protected String name;

        protected int depth;

        protected boolean isMove;

        protected ClipActionBuilder(Symbol symbol, Matrix matrix, AlphaTransform cxform, int depth, String name, int version, boolean isMove) {
            super(version);
            this.symbol = symbol;
            this.matrix = (matrix != null) ? new Transform(matrix) : null;
            this.cxform = cxform;
            this.name = name;
            this.depth = depth;
            this.isMove = isMove;
        }

        /**
         * All actions are done - place the instance
         */
        @Override
        public void done() throws IOException {
            Frame frame = currentFrame();
            Instance inst = null;
            if (isMove) {
                frame.replaceMovieClip(symbol, depth, matrix, cxform, name, getActions());
            } else {
                timeline.setAvailableDepth(depth);
                frame.placeMovieClip(symbol, matrix, cxform, name, getActions());
            }
            saveInstance(depth, inst);
        }
    }

    /**
     * SWFShape implementation that builds a Shape
     */
    protected class ShapeBuilder implements SWFShape {

        protected Shape s;

        protected int currx, curry;

        protected ShapeBuilder(Shape s) {
            this.s = s;
        }

        public void done() {
        }

        public void line(int dx, int dy) {
            currx += dx;
            curry += dy;
            s.line(((double) currx) / SWFConstants.TWIPS, ((double) curry) / SWFConstants.TWIPS);
        }

        public void curve(int cx, int cy, int dx, int dy) {
            cx += currx;
            cy += curry;
            currx = cx + dx;
            curry = cy + dy;
            s.curve(((double) currx) / SWFConstants.TWIPS, ((double) curry) / SWFConstants.TWIPS, ((double) cx) / SWFConstants.TWIPS, ((double) cy) / SWFConstants.TWIPS);
        }

        public void move(int x, int y) {
            currx = x;
            curry = y;
            s.move(((double) currx) / SWFConstants.TWIPS, ((double) curry) / SWFConstants.TWIPS);
        }

        public void setFillStyle0(int styleIndex) {
            s.setLeftFillStyle(styleIndex);
        }

        public void setFillStyle1(int styleIndex) {
            s.setRightFillStyle(styleIndex);
        }

        public void setLineStyle(int styleIndex) {
            s.setLineStyle(styleIndex);
        }

        public void defineFillStyle(Color color) {
            s.defineFillStyle(color);
        }

        public void defineFillStyle(Matrix matrix, int[] ratios, Color[] colors, boolean radial) {
            s.defineFillStyle(colors, ratios, new Transform(matrix), radial);
        }

        public void defineFillStyle(int bitmapId, Matrix matrix, boolean clipped) {
            Symbol image = getSymbol(bitmapId);
            s.defineFillStyle(image, new Transform(matrix), clipped);
        }

        public void defineLineStyle(int width, Color color) {
            s.defineLineStyle(((double) width) / SWFConstants.TWIPS, color);
        }
    }

    protected class MorphShapeBuilder extends MovieBuilder.ShapeBuilder {

        protected int id;

        protected Rect startBounds;

        protected Rect endBounds;

        protected Shape shape1;

        protected MorphShapeBuilder(int id, Rect startBounds, Rect endBounds) {
            super(new Shape());
            this.id = id;
            this.startBounds = startBounds;
            this.endBounds = endBounds;
        }

        @Override
        public void done() {
            if (shape1 == null) {
                shape1 = s;
                s = new Shape();
                return;
            }
            Shape shape2 = s;
            MorphShape morph = new MorphShape(shape1, shape2);
            saveSymbol(id, morph);
        }
    }

    protected class GlyphBuilder extends MovieBuilder.ShapeBuilder {

        protected FontDefinition fontDef;

        protected Font font;

        protected int count;

        protected int[] advances;

        protected int[] codes;

        protected Rect[] bounds;

        protected List defGlyphs;

        protected List fontGlyphs;

        protected int index = 0;

        public GlyphBuilder(FontDefinition fontDef, Font font, int glyphCount) {
            super(new Shape());
            this.fontDef = fontDef;
            this.font = font;
            this.count = glyphCount;
            defGlyphs = fontDef.getGlyphList();
            fontGlyphs = font.getGlyphList();
        }

        public GlyphBuilder(FontDefinition fontDef, Font font, int[] codes, int[] advances, Rect[] bounds) {
            this(fontDef, font, codes.length);
            this.codes = codes;
            this.advances = advances;
            this.bounds = bounds;
        }

        @Override
        public void done() {
            if (index >= count) return;
            if (bounds != null) {
                Rect r = bounds[index];
                s.setBoundingRectangle(((double) r.getMinX()) / SWFConstants.TWIPS, ((double) r.getMinY()) / SWFConstants.TWIPS, ((double) r.getMaxX()) / SWFConstants.TWIPS, ((double) r.getMaxY()) / SWFConstants.TWIPS);
            }
            double advance = (advances != null) ? (((double) advances[index]) / SWFConstants.TWIPS) : 0.0;
            int code = (codes != null) ? codes[index] : 0;
            FontDefinition.Glyph g = new FontDefinition.Glyph(s, advance, code);
            defGlyphs.add(g);
            font.addGlyph(g);
            index++;
            if (index < count) s = new Shape();
        }
    }

    /**
     * A SWFText implementation that builds a Text object
     */
    protected class TextBuilder implements SWFText {

        protected Text t;

        protected Font font;

        protected double size;

        protected Color color;

        protected double x;

        protected double y;

        protected boolean hasX;

        protected boolean hasY;

        protected TextBuilder(Text text) {
            t = text;
        }

        /**
         * SWFText interface
         */
        public void font(int fontId, int textHeight) {
            Symbol f = getSymbol(fontId);
            if (f == null || !(f instanceof Font)) return;
            font = (Font) f;
            size = ((double) textHeight) / SWFConstants.TWIPS;
        }

        /**
         * SWFText interface
         */
        public void color(Color color) {
            this.color = color;
        }

        /**
         * SWFText interface
         */
        public void setX(int x) {
            hasX = true;
            this.x = ((double) x) / SWFConstants.TWIPS;
        }

        /**
         * SWFText interface
         */
        public void setY(int y) {
            hasY = true;
            this.y = ((double) y) / SWFConstants.TWIPS;
        }

        /**
         * SWFText interface
         */
        public void text(int[] glyphIndices, int[] glyphAdvances) {
            List glyphs = font.getGlyphList();
            char[] chars = new char[glyphIndices.length];
            for (int i = 0; i < glyphIndices.length; i++) {
                int index = glyphIndices[i];
                double advance = ((double) glyphAdvances[i]) / SWFConstants.TWIPS;
                advance = advance * (1024.0 / SWFConstants.TWIPS) / size;
                FontDefinition.Glyph g = (FontDefinition.Glyph) glyphs.get(index);
                chars[i] = (char) g.getCode();
                if (advance > g.getAdvance()) g.setAdvance(advance);
            }
            try {
                Font.Chars cc = font.chars(new String(chars), size);
                t.row(cc, color, x, y, hasX, hasY);
            } catch (Font.NoGlyphException nge) {
            }
            color = null;
            hasX = false;
            hasY = false;
        }

        /**
         * SWFText interface
         */
        public void done() {
        }
    }

    /**
     * @see com.anotherbigidea.flash.interfaces.SWFTagTypes#tagScriptLimits(int, int)
     */
    public void tagScriptLimits(int maxRecursionDepth, int scriptTimeoutSecs) throws IOException {
    }

    /**
     * @see com.anotherbigidea.flash.interfaces.SWFSpriteTagTypes#tagTabOrder(int, int)
     */
    public void tagTabOrder(int depth, int tabOrder) throws IOException {
    }
}
