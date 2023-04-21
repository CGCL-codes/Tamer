package uk.co.koeth.brotzeit.android.publicfoodprovider;

import java.util.List;
import uk.co.koeth.brotzeit.android.model.FoodRecordProxy;

public interface PublicFoodRecordsProvider {

    public List<FoodRecordProxy> getPublicFoodRecords();

    public void loadPublicFoodRecords(String searchTerm);
}
