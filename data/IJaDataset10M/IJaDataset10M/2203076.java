package net.sf.chellow.monad.types;

import net.sf.chellow.monad.ProgrammerException;
import net.sf.chellow.monad.UserException;
import net.sf.chellow.monad.VFMessage;
import net.sf.chellow.monad.VFParameter;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;

public class MonadString extends MonadObject {

    public static final MonadString EMPTY_STRING;

    static {
        try {
            EMPTY_STRING = new MonadString("");
        } catch (UserException e) {
            throw new RuntimeException(e);
        } catch (ProgrammerException e) {
            throw new RuntimeException(e);
        }
    }

    public static Attr toXml(Document doc, String name, String value) {
        Attr attr = doc.createAttribute(name);
        attr.setValue(value);
        return attr;
    }

    public static void update(String name, String string, boolean excludeControlChars, Integer maxLength, Integer minLength, Character.UnicodeBlock block, boolean onlyDigits) throws UserException, ProgrammerException {
        if (excludeControlChars) {
            char[] chars = string.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (Character.isISOControl(chars[i])) {
                    throw UserException.newInvalidParameter("The field '" + name + "' contains a control character (carriage return for example) which isn't allowed.");
                }
            }
        }
        if ((maxLength != null) && (string.length() > maxLength.intValue())) {
            throw UserException.newInvalidParameter("The field '" + name + "' is too long. It shouldn't be more than " + maxLength.toString() + " characters long.");
        }
        if ((minLength != null) && (string.length() < minLength.intValue())) {
            throw UserException.newInvalidParameter("The field '" + name + "' is too short. It should be more than " + minLength.toString() + " characters in length.");
        }
        if (block != null) {
            for (int i = 0; i < string.length(); i += 1) {
                if (!block.equals(Character.UnicodeBlock.of(string.charAt(i)))) {
                    throw UserException.newInvalidParameter("The field '" + name + "' contains a character that lies outside the Unicode block '" + block.toString() + "'.");
                }
            }
        }
        if (onlyDigits) {
            for (int i = 0; i < string.length(); i++) {
                if (!Character.isDigit(string.charAt(i))) {
                    throw UserException.newInvalidParameter("Only digits are allowed.");
                }
            }
        }
    }

    private String string;

    private Integer maxLength = null;

    private Integer minLength = null;

    private Character.UnicodeBlock block = null;

    protected boolean excludeControlChars;

    protected boolean onlyDigits;

    public MonadString() {
        setTypeName("String");
    }

    public MonadString(String value) throws UserException, ProgrammerException {
        this(null, value);
    }

    public MonadString(String label, String value) throws UserException, ProgrammerException {
        this();
        setLabel(label);
        update(value);
    }

    protected MonadString(String typeName, String name, int maxLength, int minLength, Character.UnicodeBlock block, String string) throws UserException, ProgrammerException {
        super(typeName, name);
        setMaximumLength(maxLength);
        setMinimumLength(minLength);
        setAllowedCharacterBlock(block);
        excludeControlChars = true;
        update(string);
    }

    protected void setAllowedCharacterBlock(Character.UnicodeBlock block) {
        this.block = block;
    }

    protected void setMaximumLength(int maxLength) {
        this.maxLength = new Integer(maxLength);
    }

    protected void setMinimumLength(int minLength) {
        this.minLength = new Integer(minLength);
    }

    public String getString() {
        return string;
    }

    public String toString() {
        return string;
    }

    public void update(String string) throws UserException, ProgrammerException {
        if (excludeControlChars) {
            StringBuffer buffer = new StringBuffer();
            char[] chars = string.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (Character.isISOControl(chars[i])) {
                    throw UserException.newInvalidParameter(new VFMessage("The field '" + (getLabel() == null ? getTypeName() : getLabel()) + "' contains a control character (carriage return for example) which isn't allowed.", "Control characters (carriage return for example) aren't allowed."));
                }
            }
            setString(buffer.toString());
        }
        if ((maxLength != null) && (string.length() > maxLength.intValue())) {
            throw UserException.newInvalidParameter(new VFMessage("The field '" + (getLabel() == null ? getTypeName() : getLabel()) + "' is too long. It shouldn't be more than " + maxLength.toString() + " characters long.", VFMessage.STRING_TOO_LONG, new VFParameter("max_length", maxLength.toString())));
        }
        if ((minLength != null) && (string.length() < minLength.intValue())) {
            throw UserException.newInvalidParameter(new VFMessage("The field '" + (getLabel() == null ? getTypeName() : getLabel()) + "' is too short. It should be more than " + minLength.toString() + " characters in length.", VFMessage.STRING_TOO_SHORT, new VFParameter("min_length", minLength.toString())));
        }
        if (block != null) {
            for (int i = 0; i < string.length(); i += 1) {
                if (!block.equals(Character.UnicodeBlock.of(string.charAt(i)))) {
                    VFParameter[] params = { new VFParameter("position", Integer.toString(i)), new VFParameter("block", block.toString()) };
                    throw UserException.newInvalidParameter(new VFMessage("The field '" + (getLabel() == null ? getTypeName() : getLabel()) + "' contains a character that lies outside the Unicode block '" + block.toString() + "'.", VFMessage.CHARACTER_OUTSIDE_BLOCK, params));
                }
            }
        }
        if (onlyDigits) {
            for (int i = 0; i < string.length(); i++) {
                if (!Character.isDigit(string.charAt(i))) {
                    throw UserException.newInvalidParameter("Only digits are allowed.");
                }
            }
        }
        setString(string);
    }

    protected void setString(String string) {
        this.string = string;
    }

    public Attr toXML(Document doc) {
        return toXml(doc, (getLabel() == null) ? getTypeName() : getLabel(), string);
    }

    public boolean equals(Object obj) {
        boolean isEqual = false;
        if (super.equals(obj)) {
            isEqual = ((MonadString) obj).getString().equals(string);
        }
        return isEqual;
    }

    public int hashCode() {
        return string.hashCode();
    }
}
