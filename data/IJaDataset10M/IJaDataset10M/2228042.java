package tikara.events;

import java.util.LinkedList;
import tikara.model.Vote;
import tikara.settings.ConferenceMember;

/**
 * 
    Copyright (c) 2008 by Serge Haenni
    
    This file is part of Tikara.

    Tikara is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Tikara is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Tikara.  If not, see <http://www.gnu.org/licenses/>.
    
 * @author Serge Haenni
 *
 */
public class RemoveSignatoryFromVote {

    /**
	 * The signatories which have been removed from the vote
	 */
    private LinkedList<ConferenceMember> removedSignatories;

    /**
	 * The vote which the signatories were removed from
	 */
    private Vote vote;

    /**
	 * Constructor
	 */
    public RemoveSignatoryFromVote(Vote vote, LinkedList<ConferenceMember> removedSignatories) {
        this.vote = vote;
        this.removedSignatories = removedSignatories;
    }

    /**
	 * Constructor
	 */
    public RemoveSignatoryFromVote(Vote vote, ConferenceMember removedSignatory) {
        this.vote = vote;
        removedSignatories = new LinkedList<ConferenceMember>();
        removedSignatories.add(removedSignatory);
    }

    /**
	 * Returns the removed signatories
	 */
    public LinkedList<ConferenceMember> getRemovedSignatories() {
        return removedSignatories;
    }

    /**
	 * Returns the vote these signatories were removed from
	 */
    public Vote getVote() {
        return vote;
    }
}
