package v201101;

import com.google.api.adwords.lib.AdWordsService;
import com.google.api.adwords.lib.AdWordsServiceLogger;
import com.google.api.adwords.lib.AdWordsUser;
import com.google.api.adwords.lib.utils.MapUtils;
import com.google.api.adwords.v201101.cm.Dimensions;
import com.google.api.adwords.v201101.cm.Media;
import com.google.api.adwords.v201101.cm.MediaPage;
import com.google.api.adwords.v201101.cm.MediaServiceInterface;
import com.google.api.adwords.v201101.cm.MediaSize;
import com.google.api.adwords.v201101.cm.OrderBy;
import com.google.api.adwords.v201101.cm.Predicate;
import com.google.api.adwords.v201101.cm.PredicateOperator;
import com.google.api.adwords.v201101.cm.Selector;
import com.google.api.adwords.v201101.cm.SortOrder;
import java.util.Map;

/**
 * This example gets all images. To upload an image, run UploadImage.java.
 *
 * Tags: MediaService.get
 *
 * @author api.arogal@gmail (Adam Rogal)
 */
public class GetAllImages {

    public static void main(String[] args) {
        try {
            AdWordsServiceLogger.log();
            AdWordsUser user = new AdWordsUser();
            MediaServiceInterface mediaService = user.getService(AdWordsService.V201101.MEDIA_SERVICE);
            Selector selector = new Selector();
            selector.setFields(new String[] { "MediaId", "Width", "Height", "MimeType" });
            selector.setOrdering(new OrderBy[] { new OrderBy("MediaId", SortOrder.ASCENDING) });
            Predicate typePredicate = new Predicate("Type", PredicateOperator.IN, new String[] { "IMAGE" });
            selector.setPredicates(new Predicate[] { typePredicate });
            MediaPage page = mediaService.get(selector);
            if (page != null && page.getEntries() != null) {
                for (Media image : page.getEntries()) {
                    Map<MediaSize, Dimensions> dimensions = MapUtils.toMap(image.getDimensions());
                    System.out.println("Image with id '" + image.getMediaId() + "', dimensions '" + dimensions.get(MediaSize.FULL).getWidth() + "x" + dimensions.get(MediaSize.FULL).getHeight() + "', and MIME type '" + image.getMediaType() + "' was found.");
                }
            } else {
                System.out.println("No images were found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
