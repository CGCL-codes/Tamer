package be.vsko.pincettegwt.client.view;

import java.util.List;
import java.util.Map;
import be.vsko.pincettegwt.client.json.PincetteEntry;
import be.vsko.pincettegwt.client.json.QueryParameter;
import com.google.gwt.user.client.ui.Widget;

public interface BrowseView {

    public enum NotificationStatus {

        NONE, LOADING, SUCCESS, FAILURE
    }

    public interface Presenter {

        void onQueryButtonClicked();
    }

    /**
	 * Return the combination of label and values entered
	 * @return
	 */
    Map<String, Object> getMetaQueryValues();

    void setPresenter(Presenter presenter);

    void setNotification(NotificationStatus status, String message);

    void setQueryResults(List<PincetteEntry> entries, boolean tooManyResults);

    Widget asWidget();

    void addQueryElement(QueryParameter queryParamter);

    void setSuggestValues(QueryParameter queryParameter, List<String> metaPropertyValues);
}
