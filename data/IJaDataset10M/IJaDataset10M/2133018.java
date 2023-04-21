package javax.microedition.lcdui;

/**
 * abstract superclass for all policies.
 * It is expected that each constraint type will have an
 * associated policy.  For example, PASSWORD | NUMBER and
 * PASSWORD | ANY might have their own policies, rather than
 * overloading the ones that are is used for general wrapping
 * of text and editing of numbers.  This especially makes sense
 * since PASSWORD | NUMBER probably should not support sign
 * changes.
 */
abstract class Policy {

    /** The Font for rendering */
    Font f;

    /** The line height (in pixels) */
    int lineHeight;

    /** The width of the layout */
    int width = -1;

    /** The height to display this policy */
    int height;

    /** The y coordinate of the cursor */
    int cursorY;

    /** The x coordinate of the cursor */
    int cursorX;

    /**
     * Construct a new Policy with the given Font
     *
     * @param f The Font to use for rendering
     */
    Policy(Font f) {
        this.f = f;
        lineHeight = f.getHeight();
    }

    /**
     * This routine makes sure chars are valid
     *
     * @param buffer The buffer containing the characters
     * @param offset The start of the characters in the buffer
     * @param length The length of the characters in the buffer
     * @return boolean True if all the characters in the buffer are
     *                  valid for this policy
     */
    boolean validateContent(char[] buffer, int offset, int length) {
        return true;
    }

    /**
     * Return the constrained size of the buffer
     *
     * @param size The constrained size of the buffer
     * @param buffer The char array buffer
     * @return int By default, just returns 'size'
     */
    int constrainedSize(int size, char buffer[]) {
        return size;
    }

    /**
     * Signal this policy the content has changed
     *
     * @param buf The character array buffer
     * @param numChars The number of characters effected
     * @param fromPos The start position of the change in the buffer
     * @param curPos The current position of the cursor in the buffer
     * @return delta height by which Policy height has changed
     */
    int contentChanged(char[] buf, int numChars, int fromPos, int curPos) {
        return 0;
    }

    /**
     * Set the width for this policy
     *
     * @param width The new width
     * @param buf The character array buffer
     * @param numChars The number of chars in the buffer
     * @param curPos The current cursor position in the buffer
     * @return new height after width was set
     */
    int setWidth(int width, char[] buf, int numChars, int curPos) {
        this.width = width;
        contentChanged(buf, numChars, 0, curPos);
        return height;
    }

    /**
     * Paint this Policy
     *
     * @param g The Graphics object to paint to
     * @param buf The character array
     * @param numChars The number of characters in the buffer
     * @param cursorEnabled A flag indicating the cursor is enabled
     * @param cursorPos The index of the cursor location in the buffer
     * @param noCharShow A flag indicating characters should not be shown
     *                  in plaintext.
     */
    abstract void paint(Graphics g, char[] buf, int numChars, boolean cursorEnabled, int cursorPos, boolean noCharShow);

    /**
     * Move the cursor
     *
     * @param dir The direction to move
     * @param cursorPos The position of the cursor
     * @param buf The character buffer to traverse
     * @param numChars The number of characters in the buffer
     * @return int The new location of the cursor
     */
    int moveCursor(int dir, int cursorPos, char[] buf, int numChars) {
        return cursorPos;
    }

    /**
     * Get a new policy based on the constraint
     *
     * @param constraint The type of policy constraint to get a Policy for
     * @return Policy The Policy representing the given constraing
     */
    static Policy getPolicy(int constraint) {
        Policy newPolicy;
        boolean passEnabled = false;
        Font defaultFont = Font.getDefaultFont();
        if ((constraint & TextField.PASSWORD) != 0) {
            passEnabled = true;
        }
        switch(constraint & TextField.CONSTRAINT_MASK) {
            case TextField.ANY:
            case TextField.URL:
            case TextField.EMAILADDR:
                if (passEnabled) {
                    newPolicy = new PassTextPolicy(defaultFont);
                } else {
                    newPolicy = new TextPolicy(defaultFont);
                }
                break;
            case TextField.NUMERIC:
                if (passEnabled) {
                    newPolicy = new PassNumericPolicy(defaultFont);
                } else {
                    newPolicy = new NumericPolicy(defaultFont);
                }
                break;
            case TextField.PHONENUMBER:
                if (passEnabled) {
                    newPolicy = new PassNumericPolicy(defaultFont) {

                        /**
                         * Validate the content of this policy
                         *
                         * @param buffer The char buffer
                         * @param offset The start of the data in the buffer
                         * @param length The length of the data in the buffer
                         * @return boolean True if the buffer contains only
                         *                  valid characters for this Policy
                         */
                        boolean validateContent(char[] buffer, int offset, int length) {
                            for (int i = offset; i < (offset + length); i++) {
                                char c = buffer[i];
                                if (((c < '0') || (c > '9')) && (!(c == '#' || c == '*' || c == '+'))) {
                                    return false;
                                }
                            }
                            return true;
                        }
                    };
                } else {
                    newPolicy = new PhonePolicy(defaultFont);
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
        return newPolicy;
    }

    /**
     * Get the maximum width for this policy
     *
     * @param allowedWidth The maximum allowed width (in pixels)
     * @param maxSize The maximum allowed size (in chars)
     * @return int The maximum width of this policy
     */
    int getMaxWidth(int allowedWidth, int maxSize) {
        int w = f.charWidth('W') * maxSize;
        if (w < allowedWidth) {
            return w;
        }
        return allowedWidth;
    }

    /**
     * Get the minimum height required to display this policy
     *
     * @param height
     * @return int The minimum height required to display this policy
     */
    int getMinimumHeight(int height) {
        return 2 * lineHeight;
    }
}
