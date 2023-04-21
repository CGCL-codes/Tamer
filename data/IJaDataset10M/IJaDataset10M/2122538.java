package com.alexmcchesney.poster.account;

import java.util.Date;
import com.alexmcchesney.poster.LoadConfigException;
import com.alexmcchesney.poster.operations.IFetchPostsOperation;
import com.alexmcchesney.poster.templates.style.IStyleTemplate;

/**
 * Interface that should be implemented by accounts which provide access to
 * blogging services
 * @author amcchesney
 *
 */
public interface IBlogAccount extends IAccount {

    /**
	 * Loads the style template file for the given target blog
	 * @param target	PostTarget representing the blog
	 * @return	StyleTemplateFile for said blog
	 * @throws LoadConfigException	Thrown if we are unable to load the blog template file.
	 */
    public IStyleTemplate getTargetStyleTemplate(PostTarget target) throws LoadConfigException;

    /**
	 * Gets a flag indicating whether line breaks should be converted to break tags
	 * on posting.
	 */
    public boolean getConvertBreaksToTags(PostTarget target);

    /**
	 * Gets a flag indicating whether break tags should be
	 * converted to real line breaks on posting.
	 */
    public boolean getConvertTagsToBreaks(PostTarget target);

    /**
	 * Gets an operation which fetches posts from the account
	 * @param iTotalPosts	Number of posts to fetch
	 * @param priorTo		Get posts prior to this time.  Note that not all services
	 * support date-based fetching, and may ignore this field.
	 * @param target		Target to fetch from
	 * @param iStartIndex	Index from which results should start.  0-based
	 * @return	IFetchPostsOperation representing the op
	 */
    public IFetchPostsOperation getFetchPostsOperation(int iTotalPosts, Date priorTo, PostTarget target, int iStartIndex);
}
