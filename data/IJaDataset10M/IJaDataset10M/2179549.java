package org.apache.xml.serializer.utils;

import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * An instance of this class is a ListResourceBundle that
 * has the required getContents() method that returns
 * an array of message-key/message associations.
 * <p>
 * The message keys are defined in {@link MsgKey}. The
 * messages that those keys map to are defined here.
 * <p>
 * The messages in the English version are intended to be
 * translated.
 *
 * This class is not a public API, it is only public because it is
 * used in org.apache.xml.serializer.
 *
 * @xsl.usage internal
 */
public class SerializerMessages_hu extends ListResourceBundle {

    /** The lookup table for error messages.   */
    public Object[][] getContents() {
        Object[][] contents = new Object[][] { { MsgKey.BAD_MSGKEY, "A(z) ''{0}'' üzenetkulcs nem található a(z) ''{1}'' üzenetosztályban." }, { MsgKey.BAD_MSGFORMAT, "A(z) ''{1}'' üzenetosztály ''{0}'' üzenetének formátuma hibás." }, { MsgKey.ER_SERIALIZER_NOT_CONTENTHANDLER, "A(z) ''{0}'' példányosító osztály nem valósítja meg az org.xml.sax.ContentHandler függvényt." }, { MsgKey.ER_RESOURCE_COULD_NOT_FIND, "A(z) [ {0} ] erőforrás nem található.\n {1}" }, { MsgKey.ER_RESOURCE_COULD_NOT_LOAD, "A(z) [ {0} ] erőforrást nem lehet betölteni: {1} \n {2} \t {3}" }, { MsgKey.ER_BUFFER_SIZE_LESSTHAN_ZERO, "Pufferméret <= 0" }, { MsgKey.ER_INVALID_UTF16_SURROGATE, "Érvénytelen UTF-16 helyettesítés: {0} ?" }, { MsgKey.ER_OIERROR, "IO hiba" }, { MsgKey.ER_ILLEGAL_ATTRIBUTE_POSITION, "Nem lehet {0} attribútumot hozzáadni utód csomópontok után vagy egy elem előállítása előtt.  Az attribútum figyelmen kívül marad." }, { MsgKey.ER_NAMESPACE_PREFIX, "A(z) ''{0}'' előtag névtere nincs deklarálva." }, { MsgKey.ER_STRAY_ATTRIBUTE, "A(z) ''{0}'' attribútum kívül esik az elemen." }, { MsgKey.ER_STRAY_NAMESPACE, "A(z) ''{0}''=''{1}'' névtérdeklaráció kívül esik az elemen." }, { MsgKey.ER_COULD_NOT_LOAD_RESOURCE, "Nem lehet betölteni ''{0}'' erőforrást (ellenőrizze a CLASSPATH beállítást), a rendszer az alapértelmezéseket használja." }, { MsgKey.ER_ILLEGAL_CHARACTER, "Kísérletet tett {0} értékének karakteres kiírására, de nem jeleníthető meg a megadott {1} kimeneti kódolással." }, { MsgKey.ER_COULD_NOT_LOAD_METHOD_PROPERTY, "Nem lehet betölteni a(z) ''{0}'' tulajdonságfájlt a(z) ''{1}'' metódushoz (ellenőrizze a CLASSPATH beállítást)" }, { MsgKey.ER_INVALID_PORT, "Érvénytelen portszám" }, { MsgKey.ER_PORT_WHEN_HOST_NULL, "A portot nem állíthatja be, ha a hoszt null" }, { MsgKey.ER_HOST_ADDRESS_NOT_WELLFORMED, "A hoszt nem jól formázott cím" }, { MsgKey.ER_SCHEME_NOT_CONFORMANT, "A séma nem megfelelő." }, { MsgKey.ER_SCHEME_FROM_NULL_STRING, "Nem lehet beállítani a sémát null karaktersorozatból" }, { MsgKey.ER_PATH_CONTAINS_INVALID_ESCAPE_SEQUENCE, "Az elérési út érvénytelen vezérlő jelsorozatot tartalmaz" }, { MsgKey.ER_PATH_INVALID_CHAR, "Az elérési út érvénytelen karaktert tartalmaz: {0}" }, { MsgKey.ER_FRAG_INVALID_CHAR, "A töredék érvénytelen karaktert tartalmaz" }, { MsgKey.ER_FRAG_WHEN_PATH_NULL, "A töredéket nem állíthatja be, ha az elérési út null" }, { MsgKey.ER_FRAG_FOR_GENERIC_URI, "Csak általános URI-hoz állíthat be töredéket" }, { MsgKey.ER_NO_SCHEME_IN_URI, "Nem található séma az URI-ban" }, { MsgKey.ER_CANNOT_INIT_URI_EMPTY_PARMS, "Az URI nem inicializálható üres paraméterekkel" }, { MsgKey.ER_NO_FRAGMENT_STRING_IN_PATH, "Nem adhat meg töredéket az elérési útban és a töredékben is" }, { MsgKey.ER_NO_QUERY_STRING_IN_PATH, "Nem adhat meg lekérdezési karaktersorozatot az elérési útban és a lekérdezési karaktersorozatban" }, { MsgKey.ER_NO_PORT_IF_NO_HOST, "Nem adhatja meg a portot, ha nincs megadva hoszt" }, { MsgKey.ER_NO_USERINFO_IF_NO_HOST, "Nem adhatja meg a felhasználói információkat, ha nincs megadva hoszt" }, { MsgKey.ER_XML_VERSION_NOT_SUPPORTED, "Figyelmeztetés: A kimeneti dokumentum kért verziója ''{0}''.  Az XML ezen verziója nem támogatott.  A kimeneti dokumentum verziója ''1.0'' lesz." }, { MsgKey.ER_SCHEME_REQUIRED, "Sémára van szükség!" }, { MsgKey.ER_FACTORY_PROPERTY_MISSING, "A SerializerFactory osztálynak átadott Properties objektumnak nincs ''{0}'' tulajdonsága." }, { MsgKey.ER_ENCODING_NOT_SUPPORTED, "Figyelmeztetés: A(z) ''{0}'' kódolást nem támogatja a Java futási környezet." }, { MsgKey.ER_FEATURE_NOT_FOUND, "A(z) ''{0}'' paraméter nem ismerhető fel." }, { MsgKey.ER_FEATURE_NOT_SUPPORTED, "A(z) ''{0}'' paraméter ismert, de a kért érték nem állítható be." }, { MsgKey.ER_STRING_TOO_LONG, "A létrejövő karaktersorozat túl hosszú, nem fér el egy DOMString-ben: ''{0}''." }, { MsgKey.ER_TYPE_MISMATCH_ERR, "A paraméternév értékének típusa nem kompatibilis a várt típussal." }, { MsgKey.ER_NO_OUTPUT_SPECIFIED, "Az adatkiírás céljaként megadott érték üres volt." }, { MsgKey.ER_UNSUPPORTED_ENCODING, "Nem támogatott kódolás." }, { MsgKey.ER_UNABLE_TO_SERIALIZE_NODE, "A csomópont nem példányosítható." }, { MsgKey.ER_CDATA_SECTIONS_SPLIT, "A CDATA szakasz legalább egy ']]>' lezáró jelzőt tartalmaz." }, { MsgKey.ER_WARNING_WF_NOT_CHECKED, "A szabályos formázást ellenőrző példányt nem sikerült létrehozni.  A well-formed paraméter értéke true, de a szabályos formázást nem lehet ellenőrizni." }, { MsgKey.ER_WF_INVALID_CHARACTER, "A(z) ''{0}'' csomópont érvénytelen XML karaktereket tartalmaz." }, { MsgKey.ER_WF_INVALID_CHARACTER_IN_COMMENT, "Érvénytelen XML karakter (Unicode: 0x{0}) szerepelt a megjegyzésben." }, { MsgKey.ER_WF_INVALID_CHARACTER_IN_PI, "Érvénytelen XML karakter (Unicode: 0x{0}) szerepelt a feldolgozási utasításadatokban." }, { MsgKey.ER_WF_INVALID_CHARACTER_IN_CDATA, "Érvénytelen XML karakter (Unicode: 0x{0}) szerepelt a CDATASection tartalmában." }, { MsgKey.ER_WF_INVALID_CHARACTER_IN_TEXT, "Érvénytelen XML karakter (Unicode: 0x{0}) szerepelt a csomópont karakteradat tartalmában." }, { MsgKey.ER_WF_INVALID_CHARACTER_IN_NODE_NAME, "Érvénytelen XML karakter található a(z) ''{1}'' nevű {0} csomópontban." }, { MsgKey.ER_WF_DASH_IN_COMMENT, "A \"--\" karaktersorozat nem megengedett a megjegyzésekben." }, { MsgKey.ER_WF_LT_IN_ATTVAL, "A(z) \"{0}\" elemtípussal társított \"{1}\" attribútum értéke nem tartalmazhat ''<'' karaktert." }, { MsgKey.ER_WF_REF_TO_UNPARSED_ENT, "Az értelmezés nélküli \"&{0};\" entitáshivatkozás nem megengedett." }, { MsgKey.ER_WF_REF_TO_EXTERNAL_ENT, "A(z) \"&{0};\" külső entitáshivatkozás nem megengedett egy attribútumértékben." }, { MsgKey.ER_NS_PREFIX_CANNOT_BE_BOUND, "A(z) \"{0}\" előtag nem köthető a(z) \"{1}\" névtérhez." }, { MsgKey.ER_NULL_LOCAL_ELEMENT_NAME, "A(z) \"{0}\" elem helyi neve null." }, { MsgKey.ER_NULL_LOCAL_ATTR_NAME, "A(z) \"{0}\" attribútum helyi neve null." }, { MsgKey.ER_ELEM_UNBOUND_PREFIX_IN_ENTREF, "A(z) \"{0}\" entitáscsomópont helyettesítő szövege a(z) \"{1}\" elemcsomópontot tartalmazza, amelynek nem kötött előtagja \"{2}\"." }, { MsgKey.ER_ATTR_UNBOUND_PREFIX_IN_ENTREF, "A(z) \"{0}\" entitáscsomópont helyettesítő szövege a(z) \"{1}\" attribútum-csomópontot tartalmazza, amelynek nem kötött előtagja \"{2}\"." } };
        return contents;
    }
}