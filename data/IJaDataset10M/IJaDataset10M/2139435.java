package org.commonlibrary.lcms.savedsearch.service.impl;

import org.commonlibrary.lcms.model.*;
import org.commonlibrary.lcms.savedsearch.dao.SavedSearchDao;
import org.commonlibrary.lcms.savedsearch.service.SavedSearchService;
import org.commonlibrary.lcms.savedsearchfield.dao.SavedSearchFieldDao;
import org.commonlibrary.lcms.search.service.SearchService;
import org.commonlibrary.lcms.support.exception.DuplicatedObjectNameException;
import org.commonlibrary.lcms.support.service.impl.CrudServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of <code>SavedSearchService</code>
 * 
 * User: Carlos Monge
 * Date: Jul 24, 2009
 * Time: 11:08:17 AM
 */
@Transactional
@Service("savedSearchService")
public class SavedSearchServiceImpl extends CrudServiceImpl<SavedSearch, String> implements SavedSearchService {

    @Autowired
    private SavedSearchDao savedSearchDao;

    @Autowired
    private SavedSearchFieldDao savedSearchFieldDao;

    @Autowired
    @Qualifier("searchService")
    private SearchService searchService;

    /**
	 * @see org.commonlibrary.lcms.savedsearch.service.SavedSearchService#addSavedSearch(org.commonlibrary.lcms.model.SavedSearch)
	 */
    public String addSavedSearch(SavedSearch savedSearch) throws DuplicatedObjectNameException {
        SavedSearch existingSavedSearch = getSavedSearchByUserAndName(savedSearch.getCreator(), savedSearch.getName());
        if (null != existingSavedSearch) {
            String msg = "The saved search %s already exists";
            if (log.isWarnEnabled()) {
                log.warn(String.format("Create: duplicate name SavedSearch %s", savedSearch.getName()));
            }
            throw new DuplicatedObjectNameException(String.format(msg, savedSearch.getName()));
        }
        savedSearch.setSavedSearchFields(linkSavedSearchFields(savedSearch.getSavedSearchFields()));
        return create(savedSearch);
    }

    /**
	 * @see org.commonlibrary.lcms.savedsearch.service.SavedSearchService#advancedSearchCurriculums(org.commonlibrary.lcms.model.User, java.lang.String)
	 */
    public List<Curriculum> advancedSearchCurriculums(User user, String name) {
        SavedSearch savedSearch = getSavedSearchByUserAndName(user, name);
        if (null != savedSearch) {
            List<String> searchWords = getSearchWords(savedSearch.getSavedSearchKeywords());
            List<String> unwantedSearchWords = getSearchUnwantedWords(savedSearch.getSavedSearchKeywords());
            List<SearchField> searchFields = getSearchFields(savedSearch.getSavedSearchFields());
            String exactPhrase = savedSearch.getExactPhrase();
            Metadata metadata = savedSearch.getMetadata();
            User owner = savedSearch.getOwner();
            String ownerUserName = (null != owner) ? owner.getUsername() : null;
            int rating = savedSearch.getRating();
            int maxNumOfResultsPerPage = savedSearch.getMaxNumOfResultsPerPage();
            return searchService.advancedSearchCurricula(searchWords, exactPhrase, unwantedSearchWords, metadata, searchFields, ownerUserName, rating, maxNumOfResultsPerPage);
        } else {
            return null;
        }
    }

    /**
	 * @see org.commonlibrary.lcms.savedsearch.service.SavedSearchService#advancedSearchLearningObjects(org.commonlibrary.lcms.model.User, java.lang.String)
	 */
    public List<LearningObject> advancedSearchLearningObjects(User user, String name) {
        SavedSearch savedSearch = getSavedSearchByUserAndName(user, name);
        if (null != savedSearch) {
            List<String> searchWords = getSearchWords(savedSearch.getSavedSearchKeywords());
            List<String> unwantedSearchWords = getSearchUnwantedWords(savedSearch.getSavedSearchKeywords());
            List<SearchField> searchFields = getSearchFields(savedSearch.getSavedSearchFields());
            List<String> standardTopicIds = getStandardTopics(savedSearch.getSavedSearchKeywords());
            String exactPhrase = savedSearch.getExactPhrase();
            Metadata metadata = savedSearch.getMetadata();
            User owner = savedSearch.getOwner();
            String ownerUserName = (null != owner) ? owner.getUsername() : null;
            int rating = savedSearch.getRating();
            int maxNumOfResultsPerPage = savedSearch.getMaxNumOfResultsPerPage();
            if (null != standardTopicIds && standardTopicIds.size() > 0) {
                return searchService.advancedSearchLearningObjects(searchWords, exactPhrase, unwantedSearchWords, searchFields, ownerUserName, standardTopicIds, rating, maxNumOfResultsPerPage);
            } else {
                return searchService.advancedSearchLearningObjects(searchWords, exactPhrase, unwantedSearchWords, metadata, searchFields, ownerUserName, rating, maxNumOfResultsPerPage);
            }
        } else {
            return null;
        }
    }

    /**
	 * @see org.commonlibrary.lcms.savedsearch.service.SavedSearchService#advancedSearchUserProfiles(org.commonlibrary.lcms.model.User, java.lang.String)
	 */
    public List<UserProfile> advancedSearchUserProfiles(User user, String name) {
        SavedSearch savedSearch = getSavedSearchByUserAndName(user, name);
        if (null != savedSearch) {
            List<String> searchWords = getSearchWords(savedSearch.getSavedSearchKeywords());
            List<String> unwantedSearchWords = getSearchUnwantedWords(savedSearch.getSavedSearchKeywords());
            List<SearchField> searchFields = getSearchFields(savedSearch.getSavedSearchFields());
            String exactPhrase = savedSearch.getExactPhrase();
            Metadata metadata = savedSearch.getMetadata();
            User owner = savedSearch.getOwner();
            String ownerUserName = (null != owner) ? owner.getUsername() : null;
            int rating = savedSearch.getRating();
            int maxNumOfResultsPerPage = savedSearch.getMaxNumOfResultsPerPage();
            return searchService.advancedSearchUserProfiles(searchWords, exactPhrase, unwantedSearchWords, null, searchFields, ownerUserName, rating, maxNumOfResultsPerPage);
        } else {
            return null;
        }
    }

    /**
	 * @see org.commonlibrary.lcms.savedsearch.service.SavedSearchService#deleteSavedSearch(java.lang.String)
	 */
    public boolean deleteSavedSearch(String id) {
        SavedSearch savedSearch = savedSearchDao.findById(id);
        if (null != savedSearch) {
            savedSearchDao.remove(savedSearch);
            return true;
        } else {
            return false;
        }
    }

    /**
	 * @see org.commonlibrary.lcms.savedsearch.service.SavedSearchService#getSavedSearchByUserAndName(org.commonlibrary.lcms.model.User, java.lang.String)
	 */
    public SavedSearch getSavedSearchByUserAndName(User user, String name) {
        return savedSearchDao.getSavedSearchByUserAndName(user, name);
    }

    /**
	 * @see org.commonlibrary.lcms.savedsearch.service.SavedSearchService#getSavedSearchesByUserAndType(org.commonlibrary.lcms.model.User, org.commonlibrary.lcms.model.SearchType)
	 */
    public List<SavedSearch> getSavedSearchesByUserAndType(User user, SearchType searchType) {
        return savedSearchDao.getSavedSearchesByUserAndType(user, searchType);
    }

    /**
	 * Extracts the search fields from <code>SavedSearchField</code> objects
	 * @param savedSearchFields where are located the search fields
	 * @return the search fields from <code>SavedSearchField</code> objects
	 */
    private List<SearchField> getSearchFields(List<SavedSearchField> savedSearchFields) {
        if (null != savedSearchFields) {
            List<SearchField> searchFields = new ArrayList<SearchField>();
            for (SavedSearchField searchField : savedSearchFields) {
                searchFields.add(searchField.getSearchField());
            }
            return searchFields;
        } else {
            return null;
        }
    }

    /**
	 * Extracts the unwanted words from <code>SavedSearchKeyword</code> objects
	 * @param savedSearchKeywords where are located the unwanted words
	 * @return the unwanted words from <code>SavedSearchKeyword</code> objects
	 */
    private List<String> getSearchUnwantedWords(List<SavedSearchKeyword> savedSearchKeywords) {
        if (null != savedSearchKeywords) {
            List<String> searchUnwantedWords = new ArrayList<String>();
            String unwantedWord;
            for (SavedSearchKeyword savedSearchKeyword : savedSearchKeywords) {
                unwantedWord = savedSearchKeyword.getUnwantedSearchWord();
                if (null != unwantedWord) searchUnwantedWords.add(unwantedWord);
            }
            return searchUnwantedWords;
        } else {
            return null;
        }
    }

    /**
	 * Extracts the search words from <code>SavedSearchKeyword</code> objects
	 * @param savedSearchKeywords where are located the search words
	 * @return the search words from <code>SavedSearchKeyword</code> objects
	 */
    private List<String> getSearchWords(List<SavedSearchKeyword> savedSearchKeywords) {
        if (null != savedSearchKeywords) {
            List<String> searchWords = new ArrayList<String>();
            String word;
            for (SavedSearchKeyword savedSearchKeyword : savedSearchKeywords) {
                word = savedSearchKeyword.getSearchWord();
                if (null != word) searchWords.add(word);
            }
            return searchWords;
        } else {
            return null;
        }
    }

    /**
	 * Extracts the standard topics from <code>SavedSearchKeyword</code> objects
	 * @param savedSearchKeywords where are located the standard topics
	 * @return the standard topics from <code>SavedSearchKeyword</code> objects
	 */
    private List<String> getStandardTopics(List<SavedSearchKeyword> savedSearchKeywords) {
        if (null != savedSearchKeywords) {
            List<String> standardTopics = new ArrayList<String>();
            String standardTopic;
            for (SavedSearchKeyword savedSearchKeyword : savedSearchKeywords) {
                standardTopic = savedSearchKeyword.getStandardTopicId();
                if (null != standardTopic) standardTopics.add(standardTopic);
            }
            return standardTopics;
        } else {
            return null;
        }
    }

    public void init() {
        String msg = "%s property must be set";
        if (this.savedSearchDao == null) {
            throw new IllegalStateException(String.format(msg, "savedSearchDao"));
        }
        setCrudDao(savedSearchDao);
    }

    /**
     * Stores and links the saved search fields related to the search
     * @param savedSearchFields saved search fields to be linked
     * @return the stored search filters
     */
    private List<SavedSearchField> linkSavedSearchFields(List<SavedSearchField> savedSearchFields) {
        if (null != savedSearchFields) {
            List<SavedSearchField> newSavedSearchFields = new ArrayList<SavedSearchField>();
            SavedSearchField newSavedSearchField;
            for (SavedSearchField savedSearchField : savedSearchFields) {
                newSavedSearchField = savedSearchFieldDao.getSearchFieldByName(savedSearchField.getSearchField());
                if (null == newSavedSearchField) {
                    savedSearchFieldDao.create(savedSearchField);
                    newSavedSearchFields.add(savedSearchField);
                } else {
                    newSavedSearchFields.add(newSavedSearchField);
                }
            }
            return newSavedSearchFields;
        } else {
            return null;
        }
    }

    public void setSavedSearchDao(SavedSearchDao savedSearchDao) {
        this.savedSearchDao = savedSearchDao;
    }
}
