package obol.format;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.PublicKey;
import java.security.PrivateKey;
import java.security.Signature;
import javax.crypto.Mac;
import obol.lang.Context;
import obol.lang.Symbol;
import obol.lang.SymbolList;
import obol.lang.SymbolTable;

/** Toplevel type for formats.
 * Formats are binary representations of messages.
 * Formats only have meaning when associated with script instances.
 * There should exists a default format, which we assume there will be converters
 * for.
 * <P><tt>$Id: Format.java,v 1.2 2008/07/05 23:41:11 perm Exp $</tt>
 */
public interface Format {

    /** Return a string with the identity of the Format being used.
     * This can be e.g. the CVS &amp;Id&amp; string, to distinguish between
     * different versions, or just the class name.
     */
    public String getFormatName();

    /** Attempts to guess the data type of the given symbol's value.
     * Format implementations should override this method to support their
     * own format, but should pass unhandled types up to their
     * super.guessValueType().
     * This method returns <tt>null</tt> on an unknown/unhandled symbol
     * value, which it could be safe to interprete as the "binary" type,
     * however, this method will return "binary" if it thinks that's more
     * correct.
     * @param sym Symbol instance which value is to be guessed the type of.
     * @return String containing the type specification, or <tt>null</tt> if
     * the type couldn't be safely guessed (which could be interpreted as
     * "binary", but we let the particular format implementation policy and
     * programmer decide that)
     * @throws FormatException if something goes wrong.
     */
    public String guessValueType(Symbol sym) throws FormatException;

    /** Convert message components into a binary representation.
     * @param msgparts Array of objects that make up the message.
     * @param out OutputStream to write binary representation to.
     */
    public void convert(SymbolList msgparts, OutputStream out) throws FormatException, IOException;

    /** Convert a binary representation of a message to its components.
     * @param in InputStream to read binary representation from.
     * @return Array of objects that make up the message.
     */
    public SymbolList convert(InputStream in) throws FormatException, IOException;

    /** Convert a binary representation of a message into the next possible
     * single symbol.
     * This method <em>may</em>be called by <tt>convert(InputStream)</tt>,
     * but <em>must</em> work on it's own.
     * @param in InputStream to read binary representation from.
     * @return Symbol containing the next tokenizable message component
     * deducable by reading from the binary message representation.
     */
    public Symbol convertNextSymbol(InputStream in) throws FormatException, IOException;

    /** Compares two message elements (Symbol values) to see if they are
     * identical.
     * Conversion to/from binary might happen.
     * @param template The template element (the one we want the
     * other one to be identical to).
     * @param test The test element (the one we test).
     */
    public boolean identicalElements(Symbol template, Symbol test);

    /** Extracts all key-relevant data from the given key and add these as
     * properties to the given symbol, or create a new symbol.
     * These data includes type and algorithm name.
     * @param sym Symbol to update properties on, or null to create a new
     * symbol.
     * @param key Key to extract data from, or null to use symbol's value.
     * @return updated, possibly new, symbol.
     */
    public Symbol keyToSymbol(Symbol sym, Key key);

    /** Loads data from a File into the given Symbol-value.
     * This method is called by the parser before the believe() method.
     * The argument Symbol <em>must</em> have a symbol-value that is a
     * java.io.File object (provided by the parser), which contents then
     * replaces the symbol-value.
     * The original File object is placed in the symbol property
     * SymbolProperties.ValueFromFile.
     * <P>This method is called by the parser as part of obtaining data
     * for the symbol being <tt>believe</tt>'d (the datasource).
     * The data read is placed in a byte-array in the  symbol-value.
     * Format implementations <em>can</em> override this method, in order to
     * do their own preprocessing on the byte array or symbol properties,
     * but the symbol-value <em>must</em> a byte-array (for subsequent
     * typing by believe).
     * <P>
     * The number of bytes read are determined by first examining the
     * symbol's SymbolProperties.NumberOfBytes property, then the symbol's 
     * SymbolProperties.NumberOfBits property, the latter rounded upwards to
     * get a whole number of bytes.
     * If neither properties are present, the file is read until EOF.
     * <B>NB:</B> Skipping data (indexed start of read) is not supported.
     * @param sym symbol instance to examine and load data into.
     * @throws FormatException if something went wrong.
     */
    public void loadFile(Symbol sym) throws FormatException;

    /** Entry-point for data generation.
     * Format implementations should override this to handle their own
     * generation types.
     * Any Format implementation of this method <em>must</em> forward
     * unhandled calls to their super-class, which should ultimately be
     * format.FormatBase.
     * @param specs Variable number of specification data (strings, numbers).  A
     * zero-spec is illegal.
     * @param currentContext for accessing current symboltables and formats.
     * @param symbolLookup <tt>true</tt> if to look up symbols in the specs,
     * <tt>false</tt> to treat all entries in the specs as strings.  This is
     * a hack to get hash generation to work properly.
     * @return Array with two elements, index 0 being Object generated
     * according to spec, and index 1 a String naming its type.
     * @throws FormatException if something went wrong
     */
    public Object[] generate(Object[] specs, Context currentContext, boolean symbolLookup) throws FormatException;

    /** Returns an array of Strings listing the supported types of this
     * Format's generate implementation.
     * @return String array of supported belief types.
     */
    public String[] generateSupportedTypes();

    /** Entry-point for believing something about a symbol.
     * Typically used to type-promote a symbol's value.
     * This method modifies the argument symbol's values.
     * promote String symbol-values into Java Number, String, or byte[].
     * <P>
     * Format implementations should override this method to handle their
     * own types, or formats on types.
     * Any class overriding this method <em>must</em> forward unhandled
     * calls to their super-class, ultimately FormatBase.
     * @param ctx the symbol's Context (ie assoiates symbol table, etc)
     * @param sym Symbol to believe something about.
     * @param spec variable number of arguments to believe about symbol, or
     * <tt>null</tt> if the symbol's value and type should be examined and
     * acted upon (eg if value is a File, load that file and process
     * according to what the SymbolProperties.Type property says. <B>NOTE</B>
     * This default implmementation of Format.believe ignores this parameter.
     * @return Symbol reference to the symbol we believed something about
     */
    public Symbol believe(Context ctx, Symbol sym, Object[] specs) throws FormatException;

    /** Returns an array of Strings listing the supported types of this
     * Format's believe implementation.
     * @return String array of supported belief types.
     */
    public String[] believeSupportedTypes();

    /** Sign message components using the given signature key.
     * Format implementations not doing their own signature handling should
     * pass calls to their superclass, ultimately format.FormatBase.
     * @param signkey Symbol containing key used to sign messages.
     * @param msgparts message components to sign.
     * @return byte array containing binary signature value.
     */
    public byte[] sign(Symbol privkey, SymbolList msgparts) throws FormatException;

    /** Verify a signature on message components.
     * Format implementations not doing their own signature handling should
     * pass calls to their superclass, ultimately format.FormatBase.
     * @param signature byte array containing binary signature value
     * @param verifykey Symbol containing key used to verify signature.
     * @param msgparts Message components to verify signature on.
     * @returns <tt>true</tt> if the signature verifies, <tt>false</tt> if
     * not.
     */
    public boolean verify(byte[] signature, Symbol verifykey, SymbolList msgparts) throws FormatException;

    /** Creates a linked encrypted outputstream.
     * Format implementations may want to override this to access the key
     * symbol, do their own (possibly weird) encryption on streams.
     * @param key Symbol containing key to use.
     * @param out OutputStream to write encrypted data to.
     * @return OutputStream that, when written to, will write encrypted data
     * to the provided OutputStream.
     */
    public OutputStream getLinkedCipherOutputStream(Symbol key, OutputStream out) throws Exception;

    /** Creates a linked decrypted inputstream.
     * Format implementations may want to override this to access the key
     * symbol, or do their own (possibley weird) decryption on streams.
     * @param key Symbol containing key to use.
     * @param out InputStream to read encrypted data from.
     * @return InputStream that, when read from, will output decrypted data
     * read from the provieded inputstream.
     */
    public InputStream getLinkedCipherInputStream(Symbol key, InputStream in) throws Exception;

    public byte[] decrypt(Symbol key, byte[] ciphertext) throws Exception;

    public byte[] encrypt(Symbol key, byte[] plaintext) throws Exception;

    /** Creates an outputstream that when written to will update a Signature
     * object.
     * This can be used for both signing and verification.
     * Format implementations may want to override this to gain access to
     * the signing key symbol, or do their own esotoric signature handling.
     * If overriden, overriders <em>must</em> call the superclass method!
     * @param sig initialized Signature object.
     * @param key Symbol containing key used to initialize signature.
     * @return Outputstream that will update signature.
     */
    public OutputStream getLinkedSigningStream(Signature sig, Symbol key) throws FormatException;

    /** Creates an outputstream that when written to will update a Mac
     * object.
     * This can be used for both signing and verification.
     * Format implementations may want to override this to gain access to
     * the key symbol, or do their own esotoric MAC handling.
     * If overriden, overriders <em>must</em> call the superclass method!
     * @param mac initialized Mac object.
     * @param key Symbol containing key used to initialize MAC.
     * @return Outputstream that will update signature.
     */
    public OutputStream getLinkedMACStream(Mac mac, Symbol key) throws FormatException;

    /** Set encryption/decryption flag.
     * This is used to tell the binary representation mechanism (convert)
     * that we're en-/decrypting data.
     * Must be called just <em>before</em> convert().
     * Implementations of this method must chain if overloading/overrriding!
     * @param key The symbol associated with the key to be used during the
     * crypto operation.
     */
    public void preCrypto(Symbol key) throws FormatException;

    /** Clear encryption/decryption flag.
     * This is used to tell the binary representation mechanism (convert)
     * that we're done en-/decrypting data.
     * Must be called just <em>after</em> convert().
     * Implementations of this method must chain if overloading/overrriding!
     */
    public void postCrypto() throws FormatException;

    /** Convert a symbol-value to a byte array. 
     * This method can convert byte[] (a no-op), Strings (encoded in Base64)
     * or Number (not floats!).
     * @param Symbol whose value is either a byte[], a String containing
     * Base64-encoded data, or a Number.
     * @return byte [].
     * @throws FormatException if something went wrong.
     */
    public byte[] convertSymbolValueToBinary(Symbol sym) throws FormatException;
}
