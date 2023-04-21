package org.jliu.music.instrument.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import javax.swing.JPanel;
import org.jliu.music.instrument.Piano;
import org.jliu.music.instrument.PianoKey;
import org.jliu.music.instrument.PianoKeyImpl;
import org.jliu.music.instrument.PianoListener;
import org.jliu.music.theory.Note;

/**
 * A user interface for a piano keyboard.
 */
public class KeyboardJPanel extends JPanel implements Piano {

    /**
     * The minimum number of piano keys on this piano.
     */
    private static final int MIN_NUM_OF_KEYS = 3;

    /**
     * Constructs a keyboard panel.  
     * 
     * The first key and last key must not be black.  The number of keys will be 
     * adjusted (decreased) if the first key or the last key is black.
     * The number of keys must not be less than 3.  If the number of keys is
     * adjusted, the begin note may not be the same as the given begin note.
     * 
     * @param beginNote the lowest note this keyboard can produce
     * @param numOfKeys the number of the keys
     * @param pl the piano listener
     * @exception IllegalArgumentException if the given piano listener is null,
     *                  or if the number of keys is less than 3
     */
    public KeyboardJPanel(Note beginNote, int numOfKeys, PianoListener pl) {
        super();
        if (numOfKeys < MIN_NUM_OF_KEYS) {
            throw new IllegalArgumentException("Invalid number of keys: the number of keys must not be at least " + MIN_NUM_OF_KEYS);
        }
        if (pl == null) {
            throw new IllegalArgumentException("Invalid piano listener: null.");
        }
        _listener = pl;
        init(beginNote, numOfKeys, null);
    }

    /**
     * Constructs a keyboard panel with captions for keys.
     *
     * The first key and last key must not be black.  The number of keys will be 
     * adjusted (decreased) if the first key or the last key is black.
     * The number of keys must not be less than 3.  If the number of keys is
     * adjusted, the begin note may not be the same as the given begin note.
     *      
     * @param beginNote the lowest note this keyboard can produce
     * @param numOfKeys the number of keys
     * @param caps the caption string; each character of the string is assigned
     *              to a key
     * @param pl the piano listener
     * @exception IllegalArgumentException if the given piano listener is null,
     *                  or if the given caption string is null or the number
     *                  of characters is less than the number of keys
     */
    public KeyboardJPanel(Note beginNote, int numOfKeys, String caps, PianoListener pl) {
        super();
        if (numOfKeys < MIN_NUM_OF_KEYS) {
            throw new IllegalArgumentException("Invalid number of keys: the number of keys must not be at least " + MIN_NUM_OF_KEYS);
        }
        if (pl == null) {
            throw new IllegalArgumentException("Invalid piano listener: null.");
        }
        if (caps == null) {
            throw new IllegalArgumentException("Invalid caption String: null.");
        }
        if (caps.length() < this._numOfKeys) {
            throw new IllegalArgumentException("Invalid caption String: the length of the string is smaller than the number of keys.");
        }
        _listener = pl;
        init(beginNote, numOfKeys, caps);
    }

    private void init(final Note bNote, final int num, final String caps) {
        Note beginNote = bNote;
        int numOfKeys = num;
        String capStr = caps;
        if (capStr != null) {
            capStr = capStr.substring(0, numOfKeys);
        }
        if (isBlackKey[octaveSpec[beginNote.getIndex()]]) {
            beginNote = beginNote.sharp();
            numOfKeys--;
            if (capStr != null) {
                capStr = capStr.substring(1);
            }
        }
        int endIndex = (((numOfKeys % 12) + beginNote.getIndex()) % 12) - 1;
        if (endIndex < 0) {
            endIndex = 11;
        }
        if (isBlackKey[octaveSpec[endIndex]]) {
            numOfKeys--;
            if (capStr != null) {
                capStr = capStr.substring(0, capStr.length() - 1);
            }
        }
        _beginNote = beginNote;
        _keys = new PianoKey[numOfKeys];
        if (capStr != null) {
            _captions = capStr.toCharArray();
            _keyCaptionXPositions = new int[this._captions.length];
            _captionOn = true;
        }
        _keySpecs = new int[numOfKeys];
        _keyXPositions = new int[numOfKeys];
        _keyXBounds1 = new int[numOfKeys][2];
        _keyIsBlacks = new boolean[numOfKeys];
        int beginOctave1 = beginNote.getOctave();
        int offset = octaveXPositions[beginNote.getIndex()] - 1;
        for (int i = 0; i < numOfKeys; i++) {
            _keySpecs[i] = octaveSpec[beginNote.getIndex()];
            _keyXPositions[i] = (beginNote.getOctave() - beginOctave1) * octaveWidth + octaveXPositions[beginNote.getIndex()] - offset;
            int[][][] zoneXBounds;
            if (i == 0) {
                zoneXBounds = _zoneXBounds0;
            } else if (i == (numOfKeys - 1)) {
                zoneXBounds = _zoneXBoundsN;
            } else {
                zoneXBounds = _zoneXBounds;
            }
            _keyXBounds1[i][0] = _keyXPositions[i] + zoneXBounds[_keySpecs[i]][0][0];
            _keyXBounds1[i][1] = _keyXPositions[i] + zoneXBounds[_keySpecs[i]][0][1];
            _keyIsBlacks[i] = isBlackKey[_keySpecs[i]];
            _keys[i] = new PianoKeyImpl(i, beginNote, _keyIsBlacks[i]);
            beginNote = beginNote.sharp();
        }
        this._endNote = beginNote.flat();
        this._width = _keyXPositions[numOfKeys - 1] + whiteKeyWidth + 1;
        this._height = whiteKeyLength + keyBios;
        this._numOfKeys = numOfKeys;
        this._lastKeyIndex = numOfKeys - 1;
        Dimension d = new Dimension(_width, _height);
        this.setPreferredSize(d);
        this.setMaximumSize(d);
        this.setMinimumSize(d);
        addMouseListener(new MouseListener() {

            public void mousePressed(MouseEvent evt) {
                Integer pk = getPianoKeyIndexAt(evt.getX(), evt.getY());
                if (pk != null) {
                    pressPianoKey(pk);
                }
                _lastPressedKey = pk;
            }

            public void mouseReleased(MouseEvent evt) {
                Integer pk = getPianoKeyIndexAt(evt.getX(), evt.getY());
                if (pk != null) {
                    releasePianoKey(pk);
                    if (_lastPressedKey != null && !_lastPressedKey.equals(pk)) {
                        releasePianoKey(_lastPressedKey);
                    }
                } else if (_lastPressedKey != null) {
                    releasePianoKey(_lastPressedKey);
                }
                _lastPressedKey = null;
            }

            public void mouseEntered(MouseEvent evt) {
            }

            public void mouseExited(MouseEvent evt) {
                if (_lastPressedKey != null) {
                    releasePianoKey(_lastPressedKey);
                }
                _lastPressedKey = null;
            }

            public void mouseClicked(MouseEvent evt) {
            }
        });
        addMouseMotionListener(new MouseMotionListener() {

            public void mouseDragged(MouseEvent evt) {
                Integer pk = getPianoKeyIndexAt(evt.getX(), evt.getY());
                if (pk != null) {
                    if (_lastPressedKey != null && !pk.equals(_lastPressedKey)) {
                        releasePianoKey(_lastPressedKey);
                        pressPianoKey(pk);
                        _lastPressedKey = pk;
                    } else if (_lastPressedKey == null) {
                        pressPianoKey(pk);
                        _lastPressedKey = pk;
                    }
                }
            }

            public void mouseMoved(MouseEvent evt) {
            }
        });
        this.setSize(_width, _height);
    }

    /**
     * Sets the captions for this keyboard.  The number of chracters must be the same
     * as the number of the keys.  If not, this method returns immediately
     * with no changes to the current captions.  The captions can
     * be reset by passing <code>null</code>.
     * <p>
     * If the captions are successfully set, this keyboard will be repainted 
     * immediately with the captions, and <code>isCaptionEnabled</code> will
     * return <code>true</code>.
     *
     * @param captions the captions to be set
     */
    public void setCaptions(char[] captions) {
        if (captions != null && captions.length != _numOfKeys) {
            return;
        }
        this._captions = captions;
        if (captions != null) {
            Graphics g = this.getGraphics();
            if (g == null) {
                return;
            }
            FontMetrics fm = g.getFontMetrics(captionFont);
            if (_keyCaptionXPositions == null) {
                _keyCaptionXPositions = new int[_numOfKeys];
            }
            for (int i = 0; i < _keyCaptionXPositions.length; i++) {
                _keyCaptionXPositions[i] = _keyXPositions[i] + (captionXPoints[_keySpecs[i]] - fm.charWidth(captions[i])) / 2;
                if (_keyCaptionXPositions[i] < 0) {
                    _keyCaptionXPositions[i] = 0;
                }
            }
            g.dispose();
        }
        _captionOn = (captions != null);
        _bufferedImage = null;
        repaint();
    }

    /**
     * Gets the captions in a <code>char</code> array.
     *
     * @return the captions in a <code>char</code> array
     */
    public char[] getCaptions() {
        return _captions;
    }

    /**
     * Sets whether the captions are visible.
     *
     * @param b whether the captions are visible
     */
    public void setCaptionEnabled(boolean b) {
        _captionOn = b;
        _bufferedImage = null;
        repaint();
    }

    /**
     * Checks whether this keyboard is caption enabled.
     * 
     * @return true if this keyboard is caption enabled
     */
    public boolean isCaptionEnabled() {
        return _captionOn;
    }

    /**
     * Gets the lowest note on this keyboard
     * 
     * @return the lowest note on this keyboard
     */
    public Note getBeginNote() {
        return _beginNote;
    }

    public int getNumberOfKeys() {
        return _numOfKeys;
    }

    public List<PianoKey> getPianoKeys() {
        return Collections.unmodifiableList(Arrays.asList(_keys));
    }

    /**
     * Presses a piano key.  The piano key must come from this piano; otherwise
     * an <code>IndexOutOfBoundsException</code> may be thrown.
     * 
     * @param pk the piano key to be pressed
     * @exception NullPointerException if the given piano key is null
     * @exception IndexOutOfBoundsException if the piano key index is invalid
     */
    public void pressPianoKey(PianoKey pk) {
        pressPianoKey(pk.getIndex());
    }

    /**
     * Releases a piano key.  The piano key must come from this piano; otherwise
     * an <code>IndexOutOfBoundsException</code> may be thrown.
     * 
     * @param pk the piano key to be released
     * @exception NullPointerException if the given piano key is null
     * @exception IndexOutOfBoundsException if the piano key index is invalid
     */
    public void releasePianoKey(PianoKey pk) {
        releasePianoKey(pk.getIndex());
    }

    /**
     * Presses the piano key at the given index.
     * 
     * @param index the index of the piano key
     * @exception IndexOutOfBoundsException if the given index is invalid
     */
    public void pressPianoKey(int index) {
        paintPressedKey(index);
        _listener.pianoKeyPressed(_keys[index]);
    }

    /**
     * Releases the piano key at the given index.
     * 
     * @param index the index of the piano key
     * @exception IndexOutOfBoundsException if the given index is invalid
     */
    public void releasePianoKey(int index) {
        paintReleasedKey(index);
        _listener.pianoKeyReleased(_keys[index]);
    }

    /**
     * Presses the sustain pedal.  This is not implemented.
     * 
     * @param level the level
     */
    public void pressSustainPedal(int level) {
    }

    /**
     * Releases the sustain pedal.  This is not implemented.
     */
    public void releaseSustainPedal() {
    }

    /**
     * Resets this keyboard by releasing all keys.  The piano listener will
     * not be notified.
     */
    public void reset() {
        paintComponent(getGraphics());
    }

    /**
     * Paints this keyboard.  If the buffered image exists, simply paints the image.
     *
     * @param gr the Graphics object
     */
    @Override
    public void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        if (_bufferedImage == null) {
            int width1 = getSize().width;
            int height1 = getSize().height;
            _bufferedImage = createImage(width1, height1);
            Graphics g = _bufferedImage.getGraphics();
            FontMetrics fm = g.getFontMetrics(captionFont);
            g.setColor(gapColor);
            g.fillRect(0, 0, width1, height1);
            g.drawRect(0, 0, width1, height1);
            for (int i = 0; i < _numOfKeys; i++) {
                int[][] xPoints;
                int[][] yPoints;
                if (i == 0) {
                    xPoints = _xPoints0;
                    yPoints = _yPoints0;
                } else if (i == _lastKeyIndex) {
                    xPoints = _xPointsN;
                    yPoints = _yPointsN;
                } else {
                    xPoints = _xPoints;
                    yPoints = _yPoints;
                }
                int spec = _keySpecs[i];
                for (int j = 0; j < xPoints[spec].length; j++) {
                    _bufferx[j] = xPoints[spec][j] + _keyXPositions[i];
                }
                g.setColor(colors[spec]);
                g.fillPolygon(_bufferx, yPoints[spec], xPoints[spec].length);
                g.drawPolygon(_bufferx, yPoints[spec], xPoints[spec].length);
                for (int j = 0; j < darkShadeXPoints[spec].length; j++) {
                    _bufferx[j] = darkShadeXPoints[spec][j] + _keyXPositions[i];
                }
                g.setColor(darkShadeColor);
                g.drawPolyline(_bufferx, darkShadeYPoints[spec], darkShadeXPoints[spec].length);
                for (int j = 0; j < lightShadeXPoints[spec].length; j++) {
                    _bufferx[j] = lightShadeXPoints[spec][j] + _keyXPositions[i];
                }
                g.setColor(lightShadeColor);
                g.drawPolyline(_bufferx, lightShadeYPoints[spec], lightShadeXPoints[spec].length);
                if (_captionOn && _captions != null) {
                    _keyCaptionXPositions[i] = _keyXPositions[i] + (captionXPoints[_keySpecs[i]] - fm.charWidth(_captions[i])) / 2;
                    if (_keyCaptionXPositions[i] < 0) {
                        _keyCaptionXPositions[i] = 0;
                    }
                    if (isBlackKey[spec]) {
                        g.setColor(Color.white);
                    } else {
                        g.setColor(Color.black);
                    }
                    g.drawString(_captions[i] + "", _keyCaptionXPositions[i], captionYPoints[spec]);
                }
            }
            g.dispose();
        }
        gr.drawImage(_bufferedImage, 0, 0, this);
    }

    /**
     * Paints a piano key in its released state.
     *
     * @param id the index of the piano key
     */
    private void paintReleasedKey(Integer id) {
        if (id == null) {
            return;
        }
        Graphics g = getGraphics();
        int spec = _keySpecs[id];
        int xoffset = _keyXPositions[id];
        int[][] xPoints, yPoints;
        if (id == 0) {
            xPoints = _xPoints0;
            yPoints = _yPoints0;
        } else if (id == _lastKeyIndex) {
            xPoints = _xPointsN;
            yPoints = _yPointsN;
        } else {
            xPoints = _xPoints;
            yPoints = _yPoints;
        }
        g.setColor(gapColor);
        for (int i = 0; i < xPoints[spec].length; i++) {
            _bufferx[i] = xPoints[spec][i] + xoffset;
            _buffery[i] = yPoints[spec][i] + keyBios;
        }
        g.fillPolygon(_bufferx, _buffery, xPoints[spec].length);
        g.drawPolygon(_bufferx, _buffery, xPoints[spec].length);
        g.setColor(colors[spec]);
        g.fillPolygon(_bufferx, yPoints[spec], xPoints[spec].length);
        g.drawPolygon(_bufferx, yPoints[spec], xPoints[spec].length);
        g.setColor(darkShadeColor);
        for (int i = 0; i < darkShadeXPoints[spec].length; i++) {
            _bufferx[i] = darkShadeXPoints[spec][i] + xoffset;
        }
        g.drawPolyline(_bufferx, darkShadeYPoints[spec], darkShadeXPoints[spec].length);
        g.setColor(lightShadeColor);
        for (int i = 0; i < lightShadeXPoints[spec].length; i++) {
            _bufferx[i] = lightShadeXPoints[spec][i] + xoffset;
        }
        g.drawPolyline(_bufferx, lightShadeYPoints[spec], lightShadeXPoints[spec].length);
        if (_captionOn && _captions != null) {
            if (isBlackKey[spec]) {
                g.setColor(Color.white);
            } else {
                g.setColor(Color.black);
            }
            g.drawString(_captions[id] + "", _keyCaptionXPositions[id], captionYPoints[spec]);
        }
    }

    /**
     * Paint a pressed key.
     *
     * @param id the index of the key to be painted
     */
    private void paintPressedKey(Integer id) {
        if (id == null) {
            return;
        }
        Graphics g = getGraphics();
        int spec = _keySpecs[id];
        int xoffset = _keyXPositions[id];
        int[][] xPoints, yPoints;
        if (id == 0) {
            xPoints = _xPoints0;
            yPoints = _yPoints0;
        } else if (id == _lastKeyIndex) {
            xPoints = _xPointsN;
            yPoints = _yPointsN;
        } else {
            xPoints = _xPoints;
            yPoints = _yPoints;
        }
        for (int i = 0; i < xPoints[spec].length; i++) {
            _bufferx[i] = xPoints[spec][i] + xoffset;
            _buffery[i] = yPoints[spec][i] + keyBios;
        }
        g.setColor(gapColor);
        g.fillPolygon(_bufferx, yPoints[spec], xPoints[spec].length);
        g.drawPolygon(_bufferx, yPoints[spec], xPoints[spec].length);
        g.setColor(colors2[spec]);
        g.fillPolygon(_bufferx, _buffery, xPoints[spec].length);
        g.drawPolygon(_bufferx, _buffery, xPoints[spec].length);
        if (isBlackKey[spec]) {
            for (int i = 0; i < lightShadeXPoints[spec].length; i++) {
                _bufferx[i] = lightShadeXPoints[spec][i] + xoffset;
                _buffery[i] = lightShadeYPoints[spec][i] + keyBios;
            }
            g.setColor(lightShadeColor);
            g.drawPolyline(_bufferx, _buffery, lightShadeXPoints[spec].length);
        } else {
            for (int i = 0; i < darkShadeXPoints[spec].length; i++) {
                _bufferx[i] = darkShadeXPoints[spec][i] + xoffset;
                _buffery[i] = darkShadeYPoints[spec][i] + keyBios;
            }
            g.setColor(darkShadeColor);
            g.drawPolyline(_bufferx, _buffery, darkShadeXPoints[spec].length);
        }
        if (_captionOn && _captions != null) {
            if (isBlackKey[spec]) {
                g.setColor(Color.white);
            } else {
                g.setColor(Color.black);
            }
            g.drawString(_captions[id] + "", _keyCaptionXPositions[id], captionYPoints[spec] + keyBios);
        }
    }

    /**
     * Gets the piano key index by a coordinate on the UI.
     *
     * @param x the X position
     * @param y the Y position
     * @return the piano key that (x,y) falls on
     */
    private Integer getPianoKeyIndexAt(int x, int y) {
        if (y >= zoneYBounds[0][0][0] && y <= zoneYBounds[0][0][1]) {
            int i;
            for (i = 0; i < _numOfKeys; i++) {
                if (x <= _keyXBounds1[i][1]) {
                    break;
                }
            }
            if (i < _numOfKeys && x >= _keyXBounds1[i][0]) {
                return i;
            }
            return null;
        } else if (y >= zoneYBounds[0][1][0] && y <= zoneYBounds[0][1][1]) {
            for (int i = 0; i < _numOfKeys; i++) {
                if (_keyIsBlacks[i]) {
                    continue;
                }
                int temp = x - _keyXPositions[i];
                if (temp >= _zoneXBounds[0][1][0] && temp <= _zoneXBounds[0][1][1]) {
                    return i;
                }
            }
        }
        return null;
    }

    private static final int maxPoints = 20;

    private static final int[][] _xPoints = { { 0, 0, 20, 20, 11, 11 }, { 9, 9, 0, 0, 20, 20 }, { 3, 3, 0, 0, 20, 20, 14, 14 }, { 6, 6, 0, 0, 20, 20, 17, 17 }, { 3, 3, 0, 0, 20, 20, 17, 17 }, { 0, 0, 11, 11 } };

    private static final int[][] _yPoints = { { 0, 134, 134, 90, 90, 0 }, { 0, 90, 90, 134, 134, 0 }, { 0, 90, 90, 134, 134, 90, 90, 0 }, { 0, 90, 90, 134, 134, 90, 90, 0 }, { 0, 90, 90, 134, 134, 90, 90, 0 }, { 0, 86, 86, 0 } };

    private static final int[][] _xPoints0 = { { 0, 0, 20, 20, 11, 11 }, { 0, 0, 20, 20 }, { 0, 0, 20, 20, 14, 14 }, { 0, 0, 20, 20, 17, 17 }, { 0, 0, 20, 20, 17, 17 }, { 0, 0, 11, 11 } };

    private static final int[][] _yPoints0 = { { 0, 134, 134, 90, 90, 0 }, { 0, 134, 134, 0 }, { 0, 134, 134, 90, 90, 0 }, { 0, 134, 134, 90, 90, 0 }, { 0, 134, 134, 90, 90, 0 }, { 0, 86, 86, 0 } };

    private static final int[][] _xPointsN = { { 0, 0, 20, 20 }, { 9, 9, 0, 0, 20, 20 }, { 3, 3, 0, 0, 20, 20 }, { 6, 6, 0, 0, 20, 20 }, { 3, 3, 0, 0, 20, 20 }, { 0, 0, 11, 11 } };

    private static final int[][] _yPointsN = { { 0, 134, 134, 0 }, { 0, 90, 90, 134, 134, 0 }, { 0, 90, 90, 134, 134, 0 }, { 0, 90, 90, 134, 134, 0 }, { 0, 90, 90, 134, 134, 0 }, { 0, 86, 86, 0 } };

    private static final int[][] lightShadeXPoints = { { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 }, { 0, 0 } };

    private static final int[][] lightShadeYPoints = { { 90, 134 }, { 90, 134 }, { 90, 134 }, { 90, 134 }, { 90, 134 }, { 0, 83 } };

    private static final int[][] darkShadeXPoints = { { 0, 20, 20 }, { 0, 20, 20 }, { 0, 20, 20 }, { 0, 20, 20 }, { 0, 20, 20 }, { 11, 11, 0, 0, 11, 11, 0, 0, 11, 11, 0 } };

    private static final int[][] darkShadeYPoints = { { 134, 134, 90 }, { 134, 134, 90 }, { 134, 134, 90 }, { 134, 134, 90 }, { 134, 134, 90 }, { 0, 86, 86, 85, 85, 84, 84, 83, 83, 82, 82 } };

    private static final int[][][] _zoneXBounds = { { { 0, 11 }, { 0, 20 } }, { { 9, 20 }, { 0, 20 } }, { { 3, 14 }, { 0, 20 } }, { { 6, 17 }, { 0, 20 } }, { { 3, 17 }, { 0, 20 } }, { { 0, 11 } } };

    private static final int[][][] _zoneXBounds0 = { { { 0, 11 }, { 0, 20 } }, { { 0, 20 }, { 0, 20 } }, { { 0, 14 }, { 0, 20 } }, { { 0, 17 }, { 0, 20 } }, { { 0, 17 }, { 0, 20 } }, { { 0, 11 } } };

    private static final int[][][] _zoneXBoundsN = { { { 0, 20 }, { 0, 20 } }, { { 9, 20 }, { 0, 20 } }, { { 3, 20 }, { 0, 20 } }, { { 6, 20 }, { 0, 20 } }, { { 3, 20 }, { 0, 20 } }, { { 0, 11 } } };

    private static final int[][][] zoneYBounds = { { { 0, 89 }, { 90, 134 } }, { { 0, 89 }, { 90, 134 } }, { { 0, 89 }, { 90, 134 } }, { { 0, 89 }, { 90, 134 } }, { { 0, 89 }, { 90, 134 } }, { { 0, 86 } } };

    private static final int[] captionXPoints = { 21, 21, 21, 21, 21, 12 };

    private static final int[] captionYPoints = { 117, 117, 117, 117, 117, 48 };

    private static final boolean[] isBlackKey = { false, false, false, false, false, true };

    private static final int whiteKeyWidth = 21;

    private static final int whiteKeyLength = 135;

    private static final Color whiteKeyColor = new Color(230, 230, 230);

    private static final Color blackKeyColor = Color.black;

    private static final Color whiteKeyColor2 = new Color(210, 210, 210);

    private static final Color blackKeyColor2 = new Color(90, 90, 90);

    private static final Color lightShadeColor = new Color(255, 255, 255);

    private static final Color darkShadeColor = new Color(127, 127, 127);

    private static final Color[] colors = { whiteKeyColor, whiteKeyColor, whiteKeyColor, whiteKeyColor, whiteKeyColor, blackKeyColor };

    private static Color[] colors2 = { whiteKeyColor2, whiteKeyColor2, whiteKeyColor2, whiteKeyColor2, whiteKeyColor2, blackKeyColor2 };

    private static Font captionFont = new Font("Monospaced", Font.PLAIN, 10);

    private static final int[] octaveSpec = { 0, 5, 4, 5, 1, 0, 5, 2, 5, 3, 5, 1 };

    private static final int[] octaveXPositions = { 1, 14, 24, 43, 47, 70, 83, 93, 109, 116, 135, 139 };

    private static final int octaveWidth = 161;

    private static final int octaveHeight = 136;

    private static final int keyBios = 3;

    private static final Color gapColor = new Color(63, 63, 63);

    private int[] _keyXPositions;

    private int[] _keySpecs;

    private int[][] _keyXBounds1;

    private boolean[] _keyIsBlacks;

    private int[] _keyCaptionXPositions;

    private Note _beginNote;

    private Note _endNote;

    private int _numOfKeys;

    private int _lastKeyIndex;

    private int _width;

    private int _height;

    private int[] _bufferx = new int[maxPoints];

    private int[] _buffery = new int[maxPoints];

    private Image _bufferedImage = null;

    private char[] _captions;

    private boolean _captionOn;

    private Integer _lastPressedKey = null;

    private PianoKey[] _keys;

    private PianoListener _listener;
}
