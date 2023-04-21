package org.az.hhp.clusters;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.az.hhp.clusters.Clusterizer.ClusterizingFunction;
import org.az.hhp.domain.Claim;
import org.az.hhp.domain.ClaimsList;
import org.az.hhp.domain.Dict;
import org.az.hhp.domain.Member;
import org.az.hhp.tools.DataImporter;

public class HeartClusterizingFunction implements ClusterizingFunction {

    private final Set<Integer> isPregnant = new HashSet<Integer>();

    public HeartClusterizingFunction() throws FileNotFoundException {
        final Map<Integer, Member> members = DataImporter.instance().loadMembers();
        final ClaimsList claimsY1 = DataImporter.instance().loadClaims("Y1_Claims.csv");
        final ClaimsList claimsY2 = DataImporter.instance().loadClaims("Y2_Claims.csv");
        final ClaimsList claimsY3 = DataImporter.instance().loadClaims("Y3_Claims.csv");
        for (final Member m : members.values()) {
            ArrayList<Claim> claimsByMember = claimsY1.getClaimsByMember(m.getId());
            if (claimsByMember != null) {
                for (final Claim c : claimsByMember) {
                    storeIfMatches(m, c);
                }
            }
            claimsByMember = claimsY2.getClaimsByMember(m.getId());
            if (claimsByMember != null) {
                for (final Claim c : claimsByMember) {
                    storeIfMatches(m, c);
                }
            }
            claimsByMember = claimsY3.getClaimsByMember(m.getId());
            if (claimsByMember != null) {
                for (final Claim c : claimsByMember) {
                    storeIfMatches(m, c);
                }
            }
        }
    }

    public void storeIfMatches(final Member m, final Claim c) {
        if ("HEART4".equals(Dict.primaryConditionGroups.getCodeName(c.getPrimaryConditionGroup())) || "HEART2".equals(Dict.primaryConditionGroups.getCodeName(c.getPrimaryConditionGroup()))) {
            isPregnant.add(m.getId());
        }
    }

    @Override
    public String getClusterName(final Member m) {
        return m.getAgeSex() + (isPregnant.contains(m.getId()) ? "_heart" : "_no_heart");
    }
}
