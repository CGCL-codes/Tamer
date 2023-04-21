package pierre.specifications;

import pierre.system.PierreResources;
import pierre.model.BrowseModel;
import java.util.*;

public class BrowseDescription extends AbstractServiceAspectDescription {

    private BrowseModel browseModel;

    public BrowseDescription(int sectionNumber, BrowseModel browseModel) {
        super(sectionNumber);
        this.browseModel = browseModel;
    }

    public String getDescription() {
        String browseSection = PierreResources.getMessage("serviceSpecification.browse.section");
        printJumpToLink("browse");
        printSectionHeader(browseSection);
        String fileSummaryInformation = PierreResources.getMessage("serviceSpecification.browse.fileSummaryInformation");
        buffer.append(fileSummaryInformation);
        String sortFeatures = PierreResources.getMessage("serviceSpecification.browse.sortFeatures");
        buffer.append("<p>");
        buffer.append(sortFeatures);
        ArrayList sortOptionList = browseModel.getBrowseAttributes();
        String[] options = (String[]) sortOptionList.toArray(new String[0]);
        Arrays.sort(options);
        printBulletedList(options);
        printComments(browseModel);
        return buffer.toString();
    }

    public String getJumpTableLinks() {
        StringBuffer jumpLinks = new StringBuffer();
        String browseSection = PierreResources.getMessage("serviceSpecification.browse.section");
        jumpLinks.append(createJumpFromLink("browse", browseSection));
        return jumpLinks.toString();
    }
}
