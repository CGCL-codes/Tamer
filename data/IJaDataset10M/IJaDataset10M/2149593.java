package library.utils.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import library.LibraryBaseServer;
import library.enums.Library;
import library.utils.BookedItemShort;
import library.utils.DocumentDescriptionShort;
import library.utils.DocumentDetails;
import library.utils.ItemInfo;
import library.utils.StorekeeperInfo;
import library.utils.persistence.DocumentDescriptionShortArray;
import library.utils.persistence.DocumentDescriptionShortGroup;
import library.utils.persistence.DocumentDescriptionShortLibraryMap;

@SuppressWarnings("serial")
public class UserSession implements Cloneable, Serializable {

    @SuppressWarnings("unchecked")
    public static final UserSession EMPTY = new UserSession(Collections.EMPTY_LIST, -1, Collections.EMPTY_LIST, Collections.EMPTY_MAP, new CounterBean(), DocumentDescriptionBean.EMPTY, DocumentDetails.EMPTY, LibraryBaseServer.NOT_FOUND_ID, Collections.EMPTY_MAP, Collections.EMPTY_MAP, Collections.EMPTY_MAP, IdBean.EMPTY, Collections.EMPTY_LIST, false, Collections.EMPTY_MAP, PersonDescriptionBean.EMPTY, 0, Collections.EMPTY_LIST, RoleBean.EMPTY, SearchBean.EMPTY, Collections.EMPTY_LIST) {

        {
            setCheckedRegistry(Collections.EMPTY_LIST);
            setLibrariesRegistry(Collections.EMPTY_LIST);
        }
    };

    private List<BookedItemShort> bookedItems;

    @Deprecated
    private Map<String, String> checked;

    private List<Boolean> checkedRegistry;

    private CounterBean counter;

    private DocumentDescriptionBean documentDescription;

    private DocumentDetails documentDetails;

    private int documentID = LibraryBaseServer.NOT_FOUND_ID;

    private Map<Integer, DocumentDescriptionShortLibraryMap> findQueryResults;

    private Map<Integer, DocumentDescriptionShortGroup> findQueryResultsLists;

    private Map<Integer, Integer> findQuerySizes;

    private List<String> history;

    private int historyIndex;

    private IdBean id;

    private List<ItemInfo> itemsInfo;

    private boolean lendingMode;

    @Deprecated
    private Map<String, Library> libraries;

    private List<Library> librariesRegistry;

    private PersonDescriptionBean personDescription;

    private int resultID;

    private DocumentDescriptionShortGroup resultList;

    private RoleBean role;

    private SearchBean search;

    private List<StorekeeperInfo> storekeeperBookedItems;

    private String username;

    public UserSession() {
    }

    public UserSession(List<String> history, int historyIndex, List<BookedItemShort> bookedItems, List<Boolean> checkedRegistry, CounterBean counter, DocumentDescriptionBean documentDescription, DocumentDetails documentDetails, int documentID, Map<Integer, DocumentDescriptionShortLibraryMap> findQueryResults, Map<Integer, DocumentDescriptionShortGroup> findQueryResultsLists, Map<Integer, Integer> findQuerySizes, IdBean id, List<ItemInfo> itemsInfo, boolean lendingMode, List<Library> librariesRegistry, PersonDescriptionBean personDescription, int resultID, DocumentDescriptionShortGroup resultList, RoleBean role, SearchBean search, List<StorekeeperInfo> storekeeperBookedItems) {
        this.bookedItems = bookedItems;
        this.counter = counter;
        this.checkedRegistry = checkedRegistry;
        this.documentDescription = documentDescription;
        this.documentDetails = documentDetails;
        this.documentID = documentID;
        this.findQueryResults = findQueryResults;
        this.findQueryResultsLists = findQueryResultsLists;
        this.findQuerySizes = findQuerySizes;
        this.history = history;
        this.historyIndex = historyIndex;
        this.id = id;
        this.itemsInfo = itemsInfo;
        this.lendingMode = lendingMode;
        this.librariesRegistry = librariesRegistry;
        this.personDescription = personDescription;
        this.resultID = resultID;
        this.resultList = resultList;
        this.role = role;
        this.search = search;
        this.storekeeperBookedItems = storekeeperBookedItems;
    }

    public UserSession(List<String> history, int historyIndex, List<BookedItemShort> bookedItems, List<Boolean> checkedRegistry, CounterBean counter, DocumentDescriptionBean documentDescription, DocumentDetails documentDetails, int documentID, Map<Integer, Map<Library, DocumentDescriptionShort[]>> findQueryResults, Map<Integer, List<DocumentDescriptionShort>> findQueryResultsLists, Map<Integer, Integer> findQuerySizes, IdBean id, List<ItemInfo> itemsInfo, boolean lendingMode, List<Library> librariesRegistry, PersonDescriptionBean personDescription, int resultID, List<DocumentDescriptionShort> resultList, RoleBean role, SearchBean search, List<StorekeeperInfo> storekeeperBookedItems) {
        this.bookedItems = bookedItems;
        this.counter = counter;
        this.checkedRegistry = checkedRegistry;
        this.documentDescription = documentDescription;
        this.documentDetails = documentDetails;
        this.documentID = documentID;
        setFindQueryResults(findQueryResults);
        setFindQueryResultsLists(findQueryResultsLists);
        this.findQuerySizes = findQuerySizes;
        this.history = history;
        this.historyIndex = historyIndex;
        this.id = id;
        this.itemsInfo = itemsInfo;
        this.lendingMode = lendingMode;
        this.librariesRegistry = librariesRegistry;
        this.personDescription = personDescription;
        this.resultID = resultID;
        setResultList(resultList);
        this.role = role;
        this.search = search;
        this.storekeeperBookedItems = storekeeperBookedItems;
    }

    @Deprecated
    public UserSession(List<String> history, int historyIndex, List<BookedItemShort> bookedItems, Map<String, String> checked, CounterBean counter, DocumentDescriptionBean documentDescription, DocumentDetails documentDetails, int documentID, Map<Integer, DocumentDescriptionShortLibraryMap> findQueryResults, Map<Integer, DocumentDescriptionShortGroup> findQueryResultsLists, Map<Integer, Integer> findQuerySizes, IdBean id, List<ItemInfo> itemsInfo, boolean lendingMode, Map<String, Library> libraries, PersonDescriptionBean personDescription, int resultID, DocumentDescriptionShortGroup resultList, RoleBean role, SearchBean search, List<StorekeeperInfo> storekeeperBookedItems) {
        this.bookedItems = bookedItems;
        this.counter = counter;
        this.checked = checked;
        this.documentDescription = documentDescription;
        this.documentDetails = documentDetails;
        this.documentID = documentID;
        this.findQueryResults = findQueryResults;
        this.findQueryResultsLists = findQueryResultsLists;
        this.findQuerySizes = findQuerySizes;
        this.history = history;
        this.historyIndex = historyIndex;
        this.id = id;
        this.itemsInfo = itemsInfo;
        this.lendingMode = lendingMode;
        this.libraries = libraries;
        this.personDescription = personDescription;
        this.resultID = resultID;
        this.resultList = resultList;
        this.role = role;
        this.search = search;
        this.storekeeperBookedItems = storekeeperBookedItems;
    }

    @Deprecated
    public UserSession(List<String> history, int historyIndex, List<BookedItemShort> bookedItems, Map<String, String> checked, CounterBean counter, DocumentDescriptionBean documentDescription, DocumentDetails documentDetails, int documentID, Map<Integer, Map<Library, DocumentDescriptionShort[]>> findQueryResults, Map<Integer, List<DocumentDescriptionShort>> findQueryResultsLists, Map<Integer, Integer> findQuerySizes, IdBean id, List<ItemInfo> itemsInfo, boolean lendingMode, Map<String, Library> libraries, PersonDescriptionBean personDescription, int resultID, List<DocumentDescriptionShort> resultList, RoleBean role, SearchBean search, List<StorekeeperInfo> storekeeperBookedItems) {
        this.bookedItems = bookedItems;
        this.counter = counter;
        this.checked = checked;
        this.documentDescription = documentDescription;
        this.documentDetails = documentDetails;
        this.documentID = documentID;
        setFindQueryResults(findQueryResults);
        setFindQueryResultsLists(findQueryResultsLists);
        this.findQuerySizes = findQuerySizes;
        this.history = history;
        this.historyIndex = historyIndex;
        this.id = id;
        this.itemsInfo = itemsInfo;
        this.lendingMode = lendingMode;
        this.libraries = libraries;
        this.personDescription = personDescription;
        this.resultID = resultID;
        setResultList(resultList);
        this.role = role;
        this.search = search;
        this.storekeeperBookedItems = storekeeperBookedItems;
    }

    @Override
    public UserSession clone() {
        List<String> _history = null;
        if (history != null) {
            _history = new ArrayList<String>();
            for (String hist : history) {
                if (hist != null) {
                    _history.add(new String(hist));
                } else {
                    _history.add(null);
                }
            }
        }
        Integer _historyIndex = new Integer(historyIndex);
        List<BookedItemShort> _bookedItems = null;
        if (bookedItems != null) {
            _bookedItems = new ArrayList<BookedItemShort>();
            for (BookedItemShort bis : bookedItems) {
                if (bis != null) {
                    _bookedItems.add(bis.clone());
                } else {
                    _bookedItems.add(null);
                }
            }
        }
        CounterBean _counter = null;
        if (counter != null) {
            _counter = counter.clone();
        }
        Map<String, String> _checked = null;
        if (checked != null) {
            _checked = new HashMap<String, String>();
            for (Entry<String, String> entry : checked.entrySet()) {
                String key = null;
                String value = null;
                if (entry.getKey() != null) {
                    key = new String(entry.getKey());
                }
                if (entry.getValue() != null) {
                    value = new String(entry.getValue());
                }
                _checked.put(key, value);
            }
        }
        final List<Boolean> _checkedRegistry;
        if (checkedRegistry != null) {
            _checkedRegistry = new ArrayList<Boolean>();
            for (Boolean b : checkedRegistry) {
                if (b != null) {
                    _checkedRegistry.add((boolean) b);
                } else {
                    _checkedRegistry.add(null);
                }
            }
        } else {
            _checkedRegistry = null;
        }
        DocumentDescriptionBean _documentDescription = null;
        if (documentDescription != null) {
            _documentDescription = documentDescription.clone();
        }
        DocumentDetails _documentDetails = null;
        if (documentDetails != null) {
            _documentDetails = documentDetails.clone();
        }
        Integer _documentID = new Integer(documentID);
        Map<Integer, DocumentDescriptionShortLibraryMap> _findQueryResults = null;
        if (findQueryResults != null) {
            _findQueryResults = new HashMap<Integer, DocumentDescriptionShortLibraryMap>();
            for (Entry<Integer, DocumentDescriptionShortLibraryMap> entry : findQueryResults.entrySet()) {
                Integer key = null;
                DocumentDescriptionShortLibraryMap value = null;
                if (entry.getKey() != null) {
                    key = new Integer(entry.getKey());
                }
                if (entry.getValue() != null) {
                    value = entry.getValue().clone();
                }
                _findQueryResults.put(key, value);
            }
        }
        Map<Integer, DocumentDescriptionShortGroup> _findQueryResultsLists = null;
        if (findQueryResultsLists != null) {
            _findQueryResultsLists = new HashMap<Integer, DocumentDescriptionShortGroup>();
            for (Entry<Integer, DocumentDescriptionShortGroup> entry : findQueryResultsLists.entrySet()) {
                Integer key = null;
                DocumentDescriptionShortGroup value = null;
                if (entry.getKey() != null) {
                    key = new Integer(entry.getKey());
                }
                if (entry.getValue() != null) {
                    value = entry.getValue().clone();
                }
                _findQueryResultsLists.put(key, value);
            }
        }
        Map<Integer, Integer> _findQuerySizes = null;
        if (findQuerySizes != null) {
            _findQuerySizes = new HashMap<Integer, Integer>();
            for (Entry<Integer, Integer> entry : findQuerySizes.entrySet()) {
                Integer key = null;
                Integer value = null;
                if (entry.getKey() != null) {
                    key = new Integer(entry.getKey());
                }
                if (entry.getValue() != null) {
                    value = new Integer(entry.getValue());
                }
                _findQuerySizes.put(key, value);
            }
        }
        IdBean _id = null;
        if (id != null) {
            _id = id.clone();
        }
        List<ItemInfo> _itemsInfo = null;
        if (itemsInfo != null) {
            _itemsInfo = new ArrayList<ItemInfo>();
            for (ItemInfo iinfo : itemsInfo) {
                ItemInfo clone = null;
                if (iinfo != null) {
                    clone = iinfo.clone();
                }
                _itemsInfo.add(clone);
            }
        }
        boolean _lendingMode = new Boolean(lendingMode);
        Map<String, Library> _libraries = null;
        if (libraries != null) {
            _libraries = new HashMap<String, Library>();
            for (Entry<String, Library> entry : libraries.entrySet()) {
                String key = null;
                Library value = null;
                if (entry.getKey() != null) {
                    key = entry.getKey();
                }
                if (entry.getValue() != null) {
                    value = Library.valueOf(entry.getValue().name());
                }
                _libraries.put(key, value);
            }
        }
        final List<Library> _librariesRegistry;
        if (librariesRegistry != null) {
            _librariesRegistry = new ArrayList<Library>();
            for (Library lib : librariesRegistry) {
                if (lib != null) {
                    _librariesRegistry.add(Library.valueOf(lib.name()));
                } else {
                    _librariesRegistry.add(null);
                }
            }
        } else {
            _librariesRegistry = null;
        }
        PersonDescriptionBean _personDescription = null;
        if (personDescription != null) {
            _personDescription = personDescription.clone();
        }
        Integer _resultID = new Integer(resultID);
        DocumentDescriptionShortGroup _resultList = null;
        if (resultList != null) {
            _resultList = resultList.clone();
        }
        RoleBean _role = null;
        if (role != null) {
            _role = new RoleBean();
            _role.setRole(role.getRole());
        }
        SearchBean _search = null;
        if (search != null) {
            _search = search.clone();
        }
        List<StorekeeperInfo> _storekeeperBookedItems = null;
        if (storekeeperBookedItems != null) {
            _storekeeperBookedItems = new ArrayList<StorekeeperInfo>();
            for (StorekeeperInfo sinfo : storekeeperBookedItems) {
                StorekeeperInfo clone = null;
                if (sinfo != null) {
                    clone = sinfo.clone();
                }
                _storekeeperBookedItems.add(clone);
            }
        }
        UserSession usess = new UserSession(_history, _historyIndex, _bookedItems, _checkedRegistry, _counter, _documentDescription, _documentDetails, _documentID, _findQueryResults, _findQueryResultsLists, _findQuerySizes, _id, _itemsInfo, _lendingMode, _librariesRegistry, _personDescription, _resultID, _resultList, _role, _search, _storekeeperBookedItems);
        usess.setChecked(_checked);
        usess.setLibraries(_libraries);
        return usess;
    }

    private DocumentDescriptionShortGroup convertDocumentDescriptionShortGroup(List<DocumentDescriptionShort> resultList) {
        DocumentDescriptionShortGroup ddsg = new DocumentDescriptionShortGroup();
        ddsg.setDescriptions(resultList);
        return ddsg;
    }

    public List<BookedItemShort> getBookedItems() {
        return bookedItems;
    }

    public Map<String, String> getChecked() {
        return checked;
    }

    public List<Boolean> getCheckedRegistry() {
        return checkedRegistry;
    }

    public CounterBean getCounter() {
        return counter;
    }

    public DocumentDescriptionBean getDocumentDescription() {
        return documentDescription;
    }

    public DocumentDetails getDocumentDetails() {
        return documentDetails;
    }

    public int getDocumentID() {
        return documentID;
    }

    public Map<Integer, Map<Library, DocumentDescriptionShort[]>> getFindQueryResults() {
        Map<Integer, Map<Library, DocumentDescriptionShort[]>> result = new HashMap<Integer, Map<Library, DocumentDescriptionShort[]>>();
        for (int id : findQueryResults.keySet()) {
            Map<Library, DocumentDescriptionShort[]> descrs = new HashMap<Library, DocumentDescriptionShort[]>();
            Map<Library, DocumentDescriptionShortArray> descriptions = findQueryResults.get(id).getDescriptions();
            for (Library lib : descriptions.keySet()) {
                DocumentDescriptionShort[] arr = descriptions.get(lib).getDescriptions();
                descrs.put(lib, arr);
            }
            result.put(id, descrs);
        }
        return result;
    }

    public Map<Integer, List<DocumentDescriptionShort>> getFindQueryResultsLists() {
        Map<Integer, List<DocumentDescriptionShort>> result = new HashMap<Integer, List<DocumentDescriptionShort>>();
        for (int id : findQueryResultsLists.keySet()) {
            List<DocumentDescriptionShort> descriptions = findQueryResultsLists.get(id).getDescriptions();
            result.put(id, descriptions);
        }
        return result;
    }

    public Map<Integer, Integer> getFindQuerySizes() {
        return findQuerySizes;
    }

    public List<String> getHistory() {
        return history;
    }

    public int getHistoryIndex() {
        return historyIndex;
    }

    public IdBean getId() {
        return id;
    }

    public List<ItemInfo> getItemsInfo() {
        return itemsInfo;
    }

    public Map<String, Library> getLibraries() {
        return libraries;
    }

    public List<Library> getLibrariesRegistry() {
        return librariesRegistry;
    }

    public PersonDescriptionBean getPersonDescription() {
        return personDescription;
    }

    public int getResultID() {
        return resultID;
    }

    public List<DocumentDescriptionShort> getResultList() {
        return resultList == null ? null : resultList.getDescriptions();
    }

    public RoleBean getRole() {
        return role;
    }

    public SearchBean getSearch() {
        return search;
    }

    public List<StorekeeperInfo> getStorekeeperBookedItems() {
        return storekeeperBookedItems;
    }

    public boolean isLendingMode() {
        return lendingMode;
    }

    public void setBookedItems(List<BookedItemShort> bookedItems) {
        this.bookedItems = bookedItems;
    }

    public void setChecked(Map<String, String> checked) {
        this.checked = checked;
    }

    public void setCheckedRegistry(List<Boolean> checkedRegistry) {
        this.checkedRegistry = checkedRegistry;
    }

    public void setCounter(CounterBean counter) {
        this.counter = counter;
    }

    public void setDocumentDescription(DocumentDescriptionBean documentDescription) {
        this.documentDescription = documentDescription;
    }

    public void setDocumentDetails(DocumentDetails documentDetails) {
        this.documentDetails = documentDetails;
    }

    public void setDocumentID(int documentID) {
        this.documentID = documentID;
    }

    public void setFindQueryResults(Map<Integer, Map<Library, DocumentDescriptionShort[]>> findQueryResults) {
        Map<Integer, DocumentDescriptionShortLibraryMap> fqr = null;
        if (findQueryResults != null) {
            fqr = new HashMap<Integer, DocumentDescriptionShortLibraryMap>();
            for (int id : findQueryResults.keySet()) {
                Map<Library, DocumentDescriptionShort[]> descriptions = findQueryResults.get(id);
                Map<Library, DocumentDescriptionShortArray> descriptions2 = new HashMap<Library, DocumentDescriptionShortArray>();
                for (Library lib : descriptions.keySet()) {
                    DocumentDescriptionShort[] arr = descriptions.get(lib);
                    DocumentDescriptionShortArray arr2 = new DocumentDescriptionShortArray();
                    arr2.setDescriptions(arr);
                    descriptions2.put(lib, arr2);
                }
                DocumentDescriptionShortLibraryMap ddslm = new DocumentDescriptionShortLibraryMap();
                ddslm.setDescriptions(descriptions2);
                fqr.put(id, ddslm);
            }
        }
        this.findQueryResults = fqr;
    }

    public void setFindQueryResultsLists(Map<Integer, List<DocumentDescriptionShort>> findQueryResultsLists) {
        Map<Integer, DocumentDescriptionShortGroup> fqrl = null;
        if (findQueryResultsLists != null) {
            fqrl = new HashMap<Integer, DocumentDescriptionShortGroup>();
            for (int id : findQueryResultsLists.keySet()) {
                List<DocumentDescriptionShort> list = findQueryResultsLists.get(id);
                DocumentDescriptionShortGroup ddsg = convertDocumentDescriptionShortGroup(list);
                fqrl.put(id, ddsg);
            }
        }
        this.findQueryResultsLists = fqrl;
    }

    public void setFindQuerySizes(Map<Integer, Integer> findQuerySizes) {
        this.findQuerySizes = findQuerySizes;
    }

    public void setHistory(List<String> history) {
        this.history = history;
    }

    public void setHistoryIndex(int historyIndex) {
        this.historyIndex = historyIndex;
    }

    public void setId(IdBean id) {
        this.id = id;
    }

    public void setItemsInfo(List<ItemInfo> itemsInfo) {
        this.itemsInfo = itemsInfo;
    }

    public void setLendingMode(boolean lendingMode) {
        this.lendingMode = lendingMode;
    }

    public void setLibraries(Map<String, Library> libraries) {
        this.libraries = libraries;
    }

    public void setLibrariesRegistry(List<Library> librariesRegistry) {
        this.librariesRegistry = librariesRegistry;
    }

    public void setPersonDescription(PersonDescriptionBean personDescription) {
        this.personDescription = personDescription;
    }

    public void setResultID(int resultID) {
        this.resultID = resultID;
    }

    public void setResultList(List<DocumentDescriptionShort> resultList) {
        if (resultList == null) {
            this.resultList = null;
        } else {
            DocumentDescriptionShortGroup ddsg = convertDocumentDescriptionShortGroup(resultList);
            this.resultList = ddsg;
        }
    }

    public void setRole(RoleBean role) {
        this.role = role;
    }

    public void setSearch(SearchBean search) {
        this.search = search;
    }

    public void setStorekeeperBookedItems(List<StorekeeperInfo> storekeeperBookedItems) {
        this.storekeeperBookedItems = storekeeperBookedItems;
    }

    @Override
    public String toString() {
        return String.format("UserSession [bookedItems=%s, checked=%s, checkedRegistry=%s, " + "counter=%s, documentDescription=%s, documentDetails=%s, documentID=%s, " + "findQueryResults=%s, findQueryResultsLists=%s, findQuerySizes=%s, " + "history=%s, historyIndex=%s, id=%s, itemsInfo=%s, lendingMode=%s, " + "libraries=%s, librariesRegistry=%s, personDescription=%s, resultID=%s, " + "resultList=%s, role=%s, search=%s, storekeeperBookedItems=%s, username=%s]", bookedItems, checked, checkedRegistry, counter, documentDescription, documentDetails, documentID, findQueryResults, findQueryResultsLists, findQuerySizes, history, historyIndex, id, itemsInfo, lendingMode, libraries, librariesRegistry, personDescription, resultID, resultList, role, search, storekeeperBookedItems, username);
    }
}
