package com.tysanclan.site.projectewok.beans;

import java.util.Collection;
import com.tysanclan.site.projectewok.entities.Regulation;
import com.tysanclan.site.projectewok.entities.Trial;
import com.tysanclan.site.projectewok.entities.Trial.Verdict;
import com.tysanclan.site.projectewok.entities.TruthsayerComplaint;
import com.tysanclan.site.projectewok.entities.TruthsayerNomination;
import com.tysanclan.site.projectewok.entities.User;

/**
 * @author Jeroen Steenbeeke
 */
public interface LawEnforcementService {

    public TruthsayerNomination nominateTruthsayer(User nominator, User nominee);

    public void acceptTruthsayerNomination(TruthsayerNomination nomination);

    public void declineTruthsayerNomination(TruthsayerNomination nomination);

    public void voteInFavor(User senator, TruthsayerNomination nomination);

    public void voteAgainst(User senator, TruthsayerNomination nomination);

    public void resolveNomination(TruthsayerNomination nomination);

    public Trial startTrial(User accuser, User accused, String motivation, Collection<Regulation> regulations);

    public Trial confirmTrial(User truthsayer, Trial trial);

    public void dismissTrial(User truthsayer, Trial trial);

    public void passVerdict(Trial trial, Verdict verdict);

    public void checkPenaltyPoints();

    public void restrainAccused(Trial trial);

    public void unrestrainAccused(Trial trial);

    public void fileComplaint(User complainer, User truthsayer, String motivation);

    public void complaintMediated(TruthsayerComplaint complaint);

    public void complaintToSenate(TruthsayerComplaint complaint, boolean byChancellor);

    public void passComplaintVote(TruthsayerComplaint complaint, User senator, boolean inFavor);

    public void resolveComplaint(TruthsayerComplaint complaint);

    public void setComplaintObserved(TruthsayerComplaint c);
}
