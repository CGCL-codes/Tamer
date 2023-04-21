package com.inature.oce.web.model.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpSession;
import com.inature.oce.core.api.OCEException;
import com.inature.oce.core.api.User;
import com.inature.oce.core.api.VoteManager;
import com.inature.oce.core.api.nom.VoteDefinition;
import com.inature.oce.core.service.OCEService;
import com.inature.oce.core.service.ServiceLocator;
import com.inature.oce.web.html.HTMLOptionSet;
import com.inature.oce.web.model.NodeModel;
import com.inature.oce.web.model.VoteListItem;
import com.inature.oce.web.model.util.VoteCollection;
import com.inature.oce.web.util.SessionUtilities;

/**
 * Copyright 2007 i-nature
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author Yavor Mitkov Gologanov
 *
 */
public class VotesBean implements Serializable {

    public static final long serialVersionUID = 3207306375532263493L;

    public static final String BEAN_NAME = "votesBean";

    /**
	 * 
	 * @param nodeModel
	 * @param user
	 * @throws OCEException
	 */
    public static VotesBean createInstance(NodeModel nodeModel, User user) throws OCEException {
        VotesBean bean = new VotesBean();
        OCEService service = ServiceLocator.getOCEService();
        VoteManager voteManager = service.getVoteManager();
        bean.nodeModel = nodeModel;
        bean.votesList = new ArrayList<VoteListItem>();
        int count = voteManager.getVotesCount(nodeModel.getNode().getId());
        List<VoteDefinition> votesDefinition = service.getVoteDefinitionProvider().getVoteDefinitions();
        if (votesDefinition != null) {
            Iterator<VoteDefinition> votesDefinitionIt = votesDefinition.iterator();
            while (votesDefinitionIt.hasNext()) {
                VoteDefinition nextVoteDefinition = votesDefinitionIt.next();
                VoteListItem item = new VoteListItem();
                item.setVote(nextVoteDefinition);
                if (count > 0) {
                    int voteCount = voteManager.getVotesCount(nodeModel.getNode().getId(), nextVoteDefinition.getVoteValue());
                    item.setVoteCount(voteCount);
                    if (voteCount > 0) {
                        float percent = ((float) voteCount / (float) count) * 100;
                        item.setPercent(percent);
                    } else {
                        item.setPercent(0);
                    }
                } else {
                    item.setVoteCount(0);
                    item.setPercent(0);
                }
                bean.votesList.add(item);
            }
        }
        if ((user != null) && nodeModel.isOpenForVoting()) {
            bean.votingEnabled = true;
        }
        return bean;
    }

    private List<VoteListItem> votesList = null;

    private boolean votingEnabled = false;

    private NodeModel nodeModel = null;

    private String backActionName = null;

    /**
	 * 
	 * @param session
	 * @return HTMLOptionSet
	 */
    public HTMLOptionSet getAvailableVotes(HttpSession session) {
        String locale = SessionUtilities.getLocaleString(session);
        return VoteCollection.getOptionSet(locale, null);
    }

    /**
	 * 
	 * @return boolean
	 */
    public boolean isEmpty() {
        return ((votesList == null) || (votesList.size() == 0));
    }

    /**
	 * 
	 * @param session
	 * @return List<VoteListItem>
	 */
    public List<VoteListItem> getVotesList() {
        return votesList;
    }

    /**
	 * 
	 * @return
	 */
    public boolean isVotingEnabled() {
        return votingEnabled;
    }

    /**
	 * 
	 * @return
	 */
    public NodeModel getNodeModel() {
        return nodeModel;
    }

    /**
	 * 
	 * @return
	 */
    public String getBackActionName() {
        return backActionName;
    }

    /**
	 * 
	 * @param backActionName
	 */
    public void setBackActionName(String backActionName) {
        this.backActionName = backActionName;
    }
}
