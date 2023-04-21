package v201203.audiencesegmentservice;

import com.google.api.ads.dfp.lib.DfpService;
import com.google.api.ads.dfp.lib.DfpServiceLogger;
import com.google.api.ads.dfp.lib.DfpUser;
import com.google.api.ads.dfp.v201203.AudienceSegment;
import com.google.api.ads.dfp.v201203.AudienceSegmentPage;
import com.google.api.ads.dfp.v201203.AudienceSegmentServiceInterface;
import com.google.api.ads.dfp.v201203.Statement;

/**
 * This example gets all audience segments.
 *
 * Tags: AudienceSegmentService.getAudienceSegmentsByStatement
 *
 * @author api.arogal@gmail.com (Adam Rogal)
 */
public class GetAllAudienceSegmentsExample {

    public static void main(String[] args) {
        try {
            DfpServiceLogger.log();
            DfpUser user = new DfpUser();
            AudienceSegmentServiceInterface audienceSegmentService = user.getService(DfpService.V201203.AUDIENCE_SEGMENT_SERVICE);
            AudienceSegmentPage page;
            Statement filterStatement = new Statement();
            int offset = 0;
            do {
                filterStatement.setQuery("LIMIT 500 OFFSET " + offset);
                page = audienceSegmentService.getAudienceSegmentsByStatement(filterStatement);
                if (page.getResults() != null) {
                    int i = page.getStartIndex();
                    for (AudienceSegment audienceSegment : page.getResults()) {
                        System.out.println(i + ") Audience segment with ID \"" + audienceSegment.getId() + "\" and name \"" + audienceSegment.getName() + "\" was found.");
                        i++;
                    }
                }
                offset += 500;
            } while (offset < page.getTotalResultSetSize());
            System.out.println("Number of results found: " + page.getTotalResultSetSize());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
