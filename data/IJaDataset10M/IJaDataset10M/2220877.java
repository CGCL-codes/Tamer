package org.cast.isi.panel;

import org.apache.wicket.model.IModel;
import org.cast.cwm.data.Prompt;
import org.cast.cwm.data.ResponseMetadata;
import org.cast.cwm.data.Role;
import org.cast.cwm.data.User;
import org.cast.isi.ISIApplication;
import org.cast.isi.ISISession;
import org.cast.isi.data.ISIPrompt;
import org.cast.isi.data.PromptType;
import org.cast.isi.data.WordCard;
import org.cast.isi.service.ISIResponseService;

/**
 * Display a user defined word {@link WordCard} with the associated response areas.
 */
public class WordCardPanel extends ISIPanel {

    private static final long serialVersionUID = 1L;

    protected ResponseMetadata wordCardMetadata;

    protected IModel<ISIPrompt> mPrompt;

    protected IModel<User> mUser;

    protected IModel<WordCard> mWordcard;

    private boolean isTeacher;

    public WordCardPanel(String id, IModel<WordCard> mWordcard) {
        super(id);
        mUser = ISISession.get().getTargetUserModel();
        this.mWordcard = mWordcard;
        setWordCardMetadata(wordCardMetadata);
        isTeacher = ISISession.get().getUser().getRole().subsumes(Role.TEACHER);
        addWordCardResponses();
    }

    /**
	 * Add a response area for this user defined word.  These are responses are NOT associated with reading pages.
	 */
    protected void addWordCardResponses() {
        IModel<Prompt> mPrompt = ISIResponseService.get().getOrCreatePrompt(PromptType.WORD_CARD, mUser, mWordcard.getObject().getId().toString());
        ResponseList responseList = new ResponseList("responseList", mPrompt, wordCardMetadata, null, null);
        responseList.setContext("glossary");
        responseList.setAllowEdit(!isTeacher);
        responseList.setAllowNotebook(false);
        responseList.setAllowWhiteboard(false);
        add(responseList);
        ResponseButtons responseButtons = new ResponseButtons("responseButtons", mPrompt, wordCardMetadata, null);
        responseButtons.setContext("glossary");
        add(responseButtons.setVisible(!isTeacher));
    }

    public void setWordCardMetadata(ResponseMetadata wordCardMetadata) {
        this.wordCardMetadata = ISIApplication.get().getResponseMetadata();
    }
}
