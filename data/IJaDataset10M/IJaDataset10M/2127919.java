package library.database;

import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;
import library.enums.Library;
import library.enums.Role;
import library.utils.BookedItemShort;
import library.utils.DocumentDescription;
import library.utils.DocumentDescriptionShort;
import library.utils.DocumentDetails;
import library.utils.FindRequest;
import library.utils.ItemInfo;
import library.utils.PersonDescription;
import library.utils.ReaderAccountInfo;
import library.utils._Properties;
import library.utils.bean.UserSession;
import org.hibernate.HibernateException;

public interface LibraryDatabase {

    public boolean isValidUserInfo(String username, char[] password, Role role, Library library) throws SQLException;

    public int getPersonIDForUsername(String username, Role role) throws SQLException;

    public Map<Library, DocumentDescriptionShort[]> performFindQuery(FindRequest request, int rows, Library... libraries) throws SQLException;

    public Map<Integer, Integer[]> prune() throws SQLException;

    public int book(int documentID, int readerID, Library library) throws SQLException;

    public Map<Library, ItemInfo[]> getItemsInfo(int documentID) throws SQLException;

    public DocumentDetails getDocumentDetails(int documentID) throws SQLException;

    public DocumentDescriptionShort[] getShortDocumentDescriptions(Integer... documentID) throws SQLException;

    public boolean validateReader(int readerID, Library library) throws SQLException;

    public Map<Library, BookedItemShort[]> getBookedItems(int readerID, Library... libraries) throws SQLException;

    public Integer[] confirmLoan(int readerID, Library library, Integer... itemIDs) throws SQLException;

    public ReaderAccountInfo getReaderAccountInfo(int readerID, Library... library) throws SQLException;

    public boolean markBookAsCompleted(int itemID, Library library) throws SQLException;

    public Map<Integer, Map<Library, Integer[]>> addDocument(DocumentDescription descr, Map<Library, Integer> items) throws SQLException;

    public boolean removeDocument(int documentID) throws SQLException;

    public boolean editDocument(int documentID, DocumentDescription descr) throws SQLException;

    public Map<Library, Integer[]> addItems(int documentID, Map<Library, Integer> items) throws SQLException;

    public Map<Library, Integer[]> addSignaturedItems(int documentID, Map<Library, String[]> signaturedItems) throws SQLException;

    public Map<Library, Integer[]> removeItems(Map<Library, Integer[]> items) throws SQLException;

    public Map<Library, Integer[]> removeItems(int documentID, Library... libraries) throws SQLException;

    public int addPerson(PersonDescription person) throws SQLException;

    public boolean removePerson(int personID, Role role) throws SQLException;

    public boolean editPerson(int personID, PersonDescription newPerson) throws SQLException;

    public boolean storeSession(UserSession usession, Library library) throws HibernateException;

    public UserSession readSession(String username, Library library) throws HibernateException;

    public Properties props = new _Properties("/database-structure.properties");

    public static final int LENGTH_MAX_COUNTRY = Integer.parseInt(props.getProperty("length_max_country"));

    public static final int LENGTH_MAX_DOCUMENT_TYPE = Integer.parseInt(props.getProperty("length_max_document_type"));

    public static final int LENGTH_MAX_FIRST_NAME = Integer.parseInt(props.getProperty("length_max_first_name"));

    public static final int LENGTH_MAX_LANGUAGE = Integer.parseInt(props.getProperty("length_max_language"));

    public static final int LENGTH_MAX_LAST_NAME = Integer.parseInt(props.getProperty("length_max_last_name"));

    public static final int LENGTH_MAX_LIBRARY = Integer.parseInt(props.getProperty("length_max_library"));

    public static final int LENGTH_MAX_PASSWORD = Integer.parseInt(props.getProperty("length_max_password"));

    public static final int LENGTH_MAX_ROLE = Integer.parseInt(props.getProperty("length_max_role"));

    public static final int LENGTH_MAX_SIGNATURE = Integer.parseInt(props.getProperty("length_max_signature"));

    public static final int LENGTH_MAX_USERNAME = Integer.parseInt(props.getProperty("length_max_username"));

    public static final String VALUE_DUMMY = props.getProperty("value_dummy");

    public static final int VALUE_NULL_YEAR = Integer.parseInt(props.getProperty("value_null_year"));

    public static final String COLUMN_CREDENTIAL_ID = props.getProperty("column_credential_id");

    public static final String COLUMN_PASSWORD = props.getProperty("column_password");

    public static final String COLUMN_ROLE = props.getProperty("column_role");

    public static final String COLUMN_USERNAME = props.getProperty("column_username");

    public static final String TABLE_CREDENTIALS = props.getProperty("table_credentials");

    public static final String COLUMN_ADDRESS = props.getProperty("column_address");

    public static final String COLUMN_LIBRARY = props.getProperty("column_library");

    public static final String ADAPTER_AUTHORS_DOCUMENTS = props.getProperty("adapter_authors_documents");

    public static final String ADAPTER_CITIES_DOCUMENTS = props.getProperty("adapter_cities_documents");

    public static final String ADAPTER_COUNTRIES_DOCUMENTS = props.getProperty("adapter_countries_documents");

    public static final String ADAPTER_DESCRIPTIONS_DOCUMENTS = props.getProperty("adapter_descriptions_documents");

    public static final String ADAPTER_DOCUMENT_TYPES_DOCUMENTS = props.getProperty("adapter_document_types_documents");

    public static final String ADAPTER_KEYWORDS_DOCUMENTS = props.getProperty("adapter_keywords_documents");

    public static final String ADAPTER_LANGUAGES_DOCUMENTS = props.getProperty("adapter_languages_documents");

    public static final String ADAPTER_LINKED_ITEMS_DOCUMENTS = props.getProperty("adapter_linked_items_documents");

    public static final String ADAPTER_NOTES_DOCUMENTS = props.getProperty("adapter_notes_documents");

    public static final String ADAPTER_PUBLISHERS_DOCUMENTS = props.getProperty("adapter_publishers_documents");

    public static final String ADAPTER_SERIES_DOCUMENTS = props.getProperty("adapter_series_documents");

    public static final String ADAPTER_TITLES_DOCUMENTS = props.getProperty("adapter_titles_documents");

    public static final String ADAPTER_TRANSLATORS_DOCUMENTS = props.getProperty("adapter_translators_documents");

    public static final String ADAPTER_YEARS_DOCUMENTS = props.getProperty("adapter_years_documents");

    public static final String COLUMN_AUTHOR_FIRST = props.getProperty("column_author_first");

    public static final String COLUMN_AUTHOR_ID = props.getProperty("column_author_id");

    public static final String COLUMN_AUTHOR_LAST = props.getProperty("column_author_last");

    public static final String COLUMN_CITY = props.getProperty("column_city");

    public static final String COLUMN_CITY_ID = props.getProperty("column_city_id");

    public static final String COLUMN_COUNTRY = props.getProperty("column_country");

    public static final String COLUMN_COUNTRY_ID = props.getProperty("column_country_id");

    public static final String COLUMN_DESCRIPTION = props.getProperty("column_description");

    public static final String COLUMN_DESCRIPTION_ID = props.getProperty("column_description_id");

    public static final String COLUMN_DOCUMENT_ID = props.getProperty("column_document_id");

    public static final String COLUMN_DOCUMENT_TYPE = props.getProperty("column_document_type");

    public static final String COLUMN_DOCUMENT_TYPE_ID = props.getProperty("column_document_type_id");

    public static final String COLUMN_ISBN = props.getProperty("column_isbn");

    public static final String COLUMN_KEYWORD = props.getProperty("column_keyword");

    public static final String COLUMN_KEYWORD_ID = props.getProperty("column_keyword_id");

    public static final String COLUMN_LANGUAGE = props.getProperty("column_language");

    public static final String COLUMN_LANGUAGE_ID = props.getProperty("column_language_id");

    public static final String COLUMN_LINKED_ITEM = props.getProperty("column_linked_item");

    public static final String COLUMN_LINKED_ITEM_ID = props.getProperty("column_linked_item_id");

    public static final String COLUMN_NOTE = props.getProperty("column_note");

    public static final String COLUMN_NOTE_ID = props.getProperty("column_note_id");

    public static final String COLUMN_PERSON_FIRST = props.getProperty("column_person_first");

    public static final String COLUMN_PERSON_ID = props.getProperty("column_person_id");

    public static final String COLUMN_PERSON_LAST = props.getProperty("column_person_last");

    public static final String COLUMN_PUBLISHER = props.getProperty("column_publisher");

    public static final String COLUMN_PUBLISHER_ID = props.getProperty("column_publisher_id");

    public static final String COLUMN_SERIE = props.getProperty("column_serie");

    public static final String COLUMN_SERIE_ID = props.getProperty("column_serie_id");

    public static final String COLUMN_TITLE = props.getProperty("column_title");

    public static final String COLUMN_TITLE_ID = props.getProperty("column_title_id");

    public static final String COLUMN_TRANSLATOR_FIRST = props.getProperty("column_translator_first");

    public static final String COLUMN_TRANSLATOR_ID = props.getProperty("column_translator_id");

    public static final String COLUMN_TRANSLATOR_LAST = props.getProperty("column_translator_last");

    public static final String COLUMN_YEAR = props.getProperty("column_year");

    public static final String COLUMN_YEAR_ID = props.getProperty("column_year_id");

    public static final String DATABASE_DOCUMENTS_INDEX = props.getProperty("database_documents_index");

    public static final String TABLE_CITIES = props.getProperty("table_cities");

    public static final String TABLE_COUNTRIES = props.getProperty("table_countries");

    public static final String TABLE_DESCRIPTIONS = props.getProperty("table_descriptions");

    public static final String TABLE_DOCUMENT_TYPES = props.getProperty("table_document_types");

    public static final String TABLE_DOCUMENTS = props.getProperty("table_documents");

    public static final String TABLE_KEYWORDS = props.getProperty("table_keywords");

    public static final String TABLE_LANGUAGES = props.getProperty("table_languages");

    public static final String TABLE_LINKED_ITEMS = props.getProperty("table_linked_items");

    public static final String TABLE_NOTES = props.getProperty("table_notes");

    public static final String TABLE_PERSONS = props.getProperty("table_persons");

    public static final String TABLE_PUBLISHERS = props.getProperty("table_publishers");

    public static final String TABLE_SERIES = props.getProperty("table_series");

    public static final String TABLE_TITLES = props.getProperty("table_titles");

    public static final String TABLE_YEARS = props.getProperty("table_years");

    public static final String ADAPTER_LIBRARIES_READERS = props.getProperty("adapter_libraries_readers");

    public static final String ADAPTER_READERS_CREDENTIALS = props.getProperty("adapter_readers_credentials");

    public static final String COLUMN_CREDIT = props.getProperty("column_credit");

    public static final String COLUMN_READER_FIRST = props.getProperty("column_reader_first");

    public static final String COLUMN_READER_ID = props.getProperty("column_reader_id");

    public static final String COLUMN_READER_LAST = props.getProperty("column_reader_last");

    public static final String DATABASE_READERS_INDEX = props.getProperty("database_readers_index");

    public static final String TABLE_READERS = props.getProperty("table_readers");

    public static final String ADAPTER_LIBRARIES_STAFF = props.getProperty("adapter_libraries_staff");

    public static final String ADAPTER_STAFF_CREDENTIALS = props.getProperty("adapter_staff_credentials");

    public static final String COLUMN_STAFF_FIRST = props.getProperty("column_staff_first");

    public static final String COLUMN_STAFF_ID = props.getProperty("column_staff_id");

    public static final String COLUMN_STAFF_LAST = props.getProperty("column_staff_last");

    public static final String DATABASE_STAFF_INDEX = props.getProperty("database_staff_index");

    public static final String TABLE_STAFF = props.getProperty("table_staff");

    public static final String ADAPTER_DOCUMENTS_ITEMS = props.getProperty("adapter_documents_items");

    public static final String ADAPTER_ITEMS_READERS = props.getProperty("adapter_items_readers");

    public static final String COLUMN_ITEM_ID = props.getProperty("column_item_id");

    public static final String COLUMN_LEND_UNTIL = props.getProperty("column_lend_until");

    public static final String COLUMN_SIGNATURE = props.getProperty("column_signature");

    public static final String COLUMN_STATUS = props.getProperty("column_status");

    public static final String DATABASE_LIBRARYDB1 = props.getProperty("database_librarydb1");

    public static final String TABLE_ITEMS = props.getProperty("table_items");
}
