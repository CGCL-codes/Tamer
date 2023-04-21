package com.ipolyglot.service;

import java.util.List;
import com.ipolyglot.dao.WordTranslationUserScoreDTODAO;
import com.ipolyglot.model.WordTranslationUserScoreDTO;

/**
 * @author mishag
 */
public interface WordTranslationUserScoreDTOManager {

    public void setWordTranslationUserScoreDTODAO(WordTranslationUserScoreDTODAO wtusDTODAO);

    public void setOrderByHitMissDifference(boolean orderByHitMissDifference);

    /**
	 * Returns a list of WordTranslationUserScoreDTO for a list of lessons
	 * for a specific username
	 * @param lessonIds - list of lessonIds
	 * @param username - if username is specified then the hits/misses are added to each wtusDTO
	 * @param cacheResultsIfApplicable - if this parameter is set to true then if it's applicable to 
	 * 		use cache (when username is null and there is only one lesson and this lesson is negative)
	 * 		then cache will be used. If this parameter is set to false then cache will never be used.
	 * @return
	 */
    public List<WordTranslationUserScoreDTO> getWordTranslationUserScoreDTOs(List<String> lessonIds, String username, boolean cacheResultsIfApplicable);
}
